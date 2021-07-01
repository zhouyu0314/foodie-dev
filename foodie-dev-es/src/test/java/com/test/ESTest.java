package com.test;

import com.alibaba.fastjson.JSONObject;
import com.zy.EsApp;
import com.zy.pojo.Stu;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest(classes = EsApp.class)
@RunWith(SpringRunner.class)
public class ESTest {
    @Autowired
    private ElasticsearchTemplate esTemplate;

    /**
     * 创建索引，不推荐通过esTemplate进行索引管理（创建索引，更新映射，删除索引）
     * 此处属于DDL语言,且使用并不灵活 有一些bug 比如不能设置分片 不能指定keyword
     */
    @Test
    public void createIndex() {
        Stu stu = new Stu();
        stu.setStuId(1002L);
        stu.setName("李四");
        stu.setAge(19);
        stu.setMoney(218.12);
        stu.setSign("我是李四");
        stu.setDesc("我是李四，第一个测试数据！");
        //构建索引，同时可以插入数据，如果索引存在那么就仅仅是插入数据,如果_id相同则覆盖数据
        IndexQuery indexQuery = new IndexQueryBuilder().withObject(stu).build();
        esTemplate.index(indexQuery);
    }

    /**
     * 删除索引
     */
    @Test
    public void deleteIndex() {
        //@Document(indexName = "stu" ,type = "_doc") 自动根据indexName找到索引并删除
        esTemplate.deleteIndex(Stu.class);
    }

    //---------------------文档数据的CRUD----------------------------
    @Test
    public void updateStuDoc() {
        Map<String, Object> sourceMap = new HashMap<>();
        //put想要更新的数据
        sourceMap.put("sign", "张三的签名 v1.0.1");
        sourceMap.put("money", 188.88);

        //构建更新请求
        IndexRequest indexRequest = new IndexRequest();
        //放入更新的数据
        indexRequest.source(sourceMap);
        //构建更新对象
        UpdateQuery updateQuery = new UpdateQueryBuilder()
                .withClass(Stu.class)//指名数据所在的索引库(表名) withClass(Stu.class)
                .withId("1001")//指定需要更新的数据的_id
                .withIndexRequest(indexRequest)//放入请求内容
                .build();
        esTemplate.update(updateQuery);
        //update stu set sign='张三的签名 v1.0.1',money=188.88 where id=1001
    }

    /**
     * 根据文档id查询
     * select * from stu where id=1001
     */
    @Test
    public void queryStuDoc() {
        GetQuery query = new GetQuery();
        //此id是文档的主键id，并不是我们设置的字段
        query.setId("1001");
        //指定查询的索引和查询的条件
        Stu stu = esTemplate.queryForObject(query, Stu.class);
        System.out.println(stu);
    }

    /**
     * 根据文档的主键id删除数据
     */
    @Test
    public void delStuDoc() {
        esTemplate.delete(Stu.class, "jTGOcXgBQOrw-AfWVaGn");
    }

    //---------------------检索----------------------------

    /**
     * 检索数据 高亮 可分页
     */
    @Test
    public void SearchStuDoc() {
        String preTag = "<font color='red'>";
        String postTag = "</font>";

        //构建分页  page：第几页 size：每页显示多少条数据
        Pageable pageable = PageRequest.of(0, 10);
        //排序对象 根据money 正序排序
        SortBuilder sortBuilder = new FieldSortBuilder("money")
                .order(SortOrder.ASC);

        SearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("desc", "张三"))//添加搜索条件。在desc字段中搜寻张三
                .withHighlightFields(new HighlightBuilder.Field("desc").preTags(preTag).postTags(postTag))//高亮
                .withSort(sortBuilder)//放入排序，如果有多个排序接着写.withSort()
                .withPageable(pageable)//放入分页对象
                .build();
        AggregatedPage<Stu> pageStu = esTemplate.queryForPage(query, Stu.class,new SearchResultMapper(){
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
                List<Stu> stuListHighLight = new ArrayList<>();
                //将高亮的内容拿出替换原有的
                SearchHits hits = response.getHits();
                for (SearchHit hit : hits) {
                    HighlightField desc = hit.getHighlightFields().get("desc");
                    //获取高亮的字符串数据
                    String highLightDesc = desc.getFragments()[0].toString();

                    //获取属性字段
                    Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                    Stu stuHighLight = JSONObject.parseObject(JSONObject.toJSONString(sourceAsMap), Stu.class);
                    //高亮数据赋值给对象的属性
                    stuHighLight.setDesc(highLightDesc);
                    stuListHighLight.add(stuHighLight);
                }

                if (stuListHighLight.size() > 0) {
                    return new AggregatedPageImpl<>((List<T> )stuListHighLight);
                }
                return null;
            }
        });

        int totalPages = pageStu.getTotalPages();
        System.out.println("检索后的总的分页数:\t" + totalPages);
        List<Stu> content = pageStu.getContent();
        System.out.println("检索后的结果数据集合:\t" + content);
    }
}

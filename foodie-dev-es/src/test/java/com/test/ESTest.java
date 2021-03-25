package com.test;

import com.zy.EsApp;
import com.zy.pojo.Stu;
import org.elasticsearch.action.index.IndexRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.data.elasticsearch.core.query.UpdateQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
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
        stu.setStuId(1001L);
        stu.setName("张三");
        stu.setAge(18);
        stu.setMoney(15.5);
        stu.setSign("我是张三");
        stu.setDesc("我是张三，第一个测试数据！");
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
    public void updateDoc() {
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

    @Test
    public void query() {

    }

}

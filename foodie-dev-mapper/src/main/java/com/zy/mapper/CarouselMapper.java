package com.zy.mapper;
import com.zy.pojo.Carousel;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CarouselMapper {

	public Carousel getCarouselById(@Param(value = "id") Long id)throws Exception;

	public List<Carousel>	getCarouselListByMap(Map<String, Object> param)throws Exception;

	public Integer getCarouselCountByMap(Map<String, Object> param)throws Exception;

	public Integer insertCarousel(Carousel carousel)throws Exception;

	public Integer updateCarousel(Carousel carousel)throws Exception;


}

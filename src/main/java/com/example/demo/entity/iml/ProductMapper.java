package com.example.demo.entity.iml;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {

    @Update("UPDATE pms_product SET stock = stock - #{quantity} WHERE id = #{productId}  AND stock >= #{quantity}")
    int deductStock(@Param("productId") Long productId, @Param("quantity") Integer quantity);

    @Update("UPDATE pms_product SET stock = stock + #{quantity} WHERE id = #{productId}")
    int restoreStock(@Param("productId") Long productId, @Param("quantity") Integer quantity);
}

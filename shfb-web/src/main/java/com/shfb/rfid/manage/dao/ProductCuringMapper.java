package com.shfb.rfid.manage.dao;

import com.shfb.rfid.manage.entity.ProductCuring;

public interface ProductCuringMapper {
    int deleteByPrimaryKey(Integer product_curing_id);

    int insert(ProductCuring record);

    int insertSelective(ProductCuring record);

    ProductCuring selectByPrimaryKey(Integer product_curing_id);

    int updateByPrimaryKeySelective(ProductCuring record);

    int updateByPrimaryKey(ProductCuring record);
}
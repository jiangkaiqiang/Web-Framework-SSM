package com.smartcold.bgzigbee.manage.dao;

import java.util.List;

import com.smartcold.bgzigbee.manage.entity.StorageManageTypeEntity;

/**
 * Author: qiunian.sun
 * Date: qiunian.sun(2016-05-21 22:51)
 */
public interface StorageManageTypeMapper {

    List<StorageManageTypeEntity> findAll();

}
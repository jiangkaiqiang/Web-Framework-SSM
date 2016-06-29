package com.smartcold.zigbee.manage.service.impl;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.smartcold.zigbee.manage.dao.RdcShareMapper;
import com.smartcold.zigbee.manage.dto.RdcShareDTO;
import com.smartcold.zigbee.manage.service.RdcShareService;

/*
 * Copyright (C) DCIS 版权所有
 * 功能描述:睿库共享
 * Create on MaQiang 2016-6-27 09:28:36
 */
@Service("rdcShareService")
public class RdcShareServiceImpl implements RdcShareService {

	@Autowired
	private RdcShareMapper rdcShareMapper;
	
	/**
	 * 获得共享信息
	 * @param pageNum
	 * @param pageSize
	 * @param filter
	 * @return
	 */
	@Override
	public PageInfo<RdcShareDTO> getSERDCList(int pageNum, int pageSize,HashMap<String, Object> parameters) {
		PageHelper.startPage(pageNum, pageSize);
		Page<RdcShareDTO> serdcList = rdcShareMapper.getSERDCList(parameters);
		return new PageInfo<RdcShareDTO>(serdcList);
	}

    
}

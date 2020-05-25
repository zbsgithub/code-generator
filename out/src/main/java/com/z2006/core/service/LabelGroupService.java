                                                                
package com.z2006.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.z2006.core.common.db.mybatis.service.AbstractBaseService;
import com.z2006.core.common.db.mybatis.dao.BaseDAOInterface;

 
import com.z2006.core.dao.LabelGroupDao;
import com.z2006.core.model.LabelGroup;

/**
 * 
 * 说明：处理对标签组信息的业务操作
 * 
 * @author 张兵帅
 * 
 * @version 1.0
 * 
 * @since 2020年05月21日
 */
@Service
public class LabelGroupService extends AbstractBaseService<LabelGroup> {

	@Autowired
	private LabelGroupDao labelGroupDao;
	
	 
	@Override
	protected BaseDAOInterface<LabelGroup> getDAO() {
		return labelGroupDao;
	}

}
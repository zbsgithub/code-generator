package com.z2006.core.controller.data;

import java.io.Serializable;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.z2006.core.common.db.mybatis.result.Result;
import com.z2006.core.instance.module.LabelGroup;
import com.z2006.core.instance.dto.LabelGroupDto;
import com.z2006.core.instance.qo.defaults.LabelGroupQo;
import com.z2006.core.service.LabelGroupService;

/**
 * 
 * LabelGroup信息控制器（管理员）
 *
 * @author 张兵帅
 *
 * @version
 *
 * @since 2020年05月21日
 */
@Controller
public class LabelGroupDataController {

	@Autowired
	private  LabelGroupService labelGroupService;
	
	/**
	 * 
	 * 功能描述：标签组信息列表--普通分页
	 * 
	 * @return
	 * 
	 * @author 张兵帅
	 * 
	 * @since 2020年05月21日
	 * 
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	@RequestMapping("data/label/group/pagelist")
	@ResponseBody
	public Result pagelist(@RequestBody LabelGroupQo qo) {
		return Result.valueOf(Result.SUCCESS, "操作成功", labelGroupService.findPaginationDataByCondition(qo));
	}
	
	/**
	 * 
	 * 功能描述：标签组信息列表--通过条件查询
	 * 
	 * @return
	 * 
	 * @author 张兵帅
	 * 
	 * @since 2020年05月21日
	 * 
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	@RequestMapping("data/label/group/list")
	@ResponseBody
	public Result list(@RequestBody LabelGroupQo qo) {
		return Result.valueOf(Result.SUCCESS, "操作成功", labelGroupService.findList(qo));
	}

	

	/**
	 * 
	 * 功能描述：标签组信息详情
	 *
	 * @param id
	 * @return
	 * 
	 * @author 张兵帅
	 *
	 * @since 2015年10月30日
	 *
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	@RequestMapping("data/label/group/detail")
	@ResponseBody
	public Result detail(@RequestParam String id) {

		return Result.valueOf(Result.SUCCESS, "操作成功", labelGroupService.findById(id));
	}
	
	/**
	 * 
	 * 功能描述：标签组信息批量删除
	 *
	 * @param id
	 * @return
	 * 
	 * @author 张兵帅
	 *
	 * @since 2015年10月30日
	 *
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	@RequestMapping("data/label/group/batch_delete")
	@ResponseBody
	public Result batchDelete(@RequestParam Serializable... ids) {

	 	 labelGroupService.batchDelete(ids);
			

		return Result.valueOf(Result.SUCCESS, "操作成功");
	}

	/**
	 * 
	 * 功能描述：标签组信息删除
	 *
	 * @param id
	 * @return
	 * 
	 * @author 张兵帅
	 *
	 * @since 2015年10月30日
	 *
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	@RequestMapping("data/label/group/delete")
	@ResponseBody
	public Result delete(@RequestParam String id) {

		
			 			 labelGroupService.deleteByID(id);
			
		return Result.valueOf(Result.SUCCESS, "操作成功");
	}

	/**
	 * 
	 * 功能描述：标签组信息添加
	 *
	 * @param id
	 * @return
	 * 
	 * @author 张兵帅
	 *
	 * @since 2015年10月30日
	 *
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	@RequestMapping("data/label/group/add")
	@ResponseBody
	 	 public Result add(@RequestBody LabelGroupDto dto) {
	
	
		 				LabelGroup labelGroup = new LabelGroup();
				BeanUtils.copyProperties(dto, labelGroup);
				labelGroupService.insert(labelGroup);
		
				

		return Result.valueOf(Result.SUCCESS, "操作成功");
	}

	/**
	 * 
	 * 功能描述：标签组信息修改
	 *
	 * @param id
	 * @return
	 * 
	 * @author 张兵帅
	 *
	 * @since 2015年10月30日
	 *
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	@RequestMapping("data/label/group/update")
	@ResponseBody
	 	 public Result edit(@RequestBody LabelGroupDto dto) {
		
	 				LabelGroup labelGroup = new LabelGroup();
				BeanUtils.copyProperties(dto, labelGroup);
				labelGroupService.updateSelective(labelGroup);
		
		
		return Result.valueOf(Result.SUCCESS, "操作成功");
	}

}

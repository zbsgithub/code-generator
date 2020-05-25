
                                                                

package com.z2006.core.dao;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.type.JdbcType;
 import org.apache.ibatis.annotations.Options;


import com.z2006.core.common.db.mybatis.dao.BaseDAOInterface;
import com.z2006.core.common.db.mybatis.query.QueryInterface;
import com.z2006.core.model.LabelGroup;

/**
 * 
 *  说明：标签组信息对象的数据访问类
 * 
 * @author 张兵帅
 * 
 * @version 1.0
 * 
 * @since 2020年05月21日
 */
public interface LabelGroupDao extends BaseDAOInterface<LabelGroup> {

	 	
	/**
	 * 
	 * 功能描述：保存
	 * 
	 * @return
	 * 
	 * @author 张兵帅
	 * 
	 * @since 2020年05月21日
	 * 
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	@Insert({ "insert into label_group ( id,group_no,label_relation,label_group_relation,four_labels_no,crowd_id,create_time)  values (#{id,jdbcType=INTEGER},#{groupNo,jdbcType=VARCHAR},#{labelRelation,jdbcType=INTEGER},#{labelGroupRelation,jdbcType=INTEGER},#{fourLabelsNo,jdbcType=VARCHAR},#{crowdId,jdbcType=INTEGER},#{createTime,jdbcType=TIMESTAMP})" })
	@Override
	 	@Options(useGeneratedKeys = true, keyProperty = "id")
		public void insert(LabelGroup entity);

	/**
	 * 
	 * 功能描述：选择字段保存
	 * 
	 * @return
	 * 
	 * @author 张兵帅
	 * 
	 * @since 2020年05月21日
	 * 
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	@Insert({
			"<script>"
			+"insert into label_group "
		    +"<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\" > <if test=\"id != null\" > id, </if> <if test=\"groupNo != null\" > group_no, </if> <if test=\"labelRelation != null\" > label_relation, </if> <if test=\"labelGroupRelation != null\" > label_group_relation, </if> <if test=\"fourLabelsNo != null\" > four_labels_no, </if> <if test=\"crowdId != null\" > crowd_id, </if> <if test=\"createTime != null\" > create_time, </if>  </trim> "
		    +" <trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\" >  <if test=\"id != null\" > #{id,jdbcType=INTEGER}, </if> <if test=\"groupNo != null\" > #{groupNo,jdbcType=VARCHAR}, </if> <if test=\"labelRelation != null\" > #{labelRelation,jdbcType=INTEGER}, </if> <if test=\"labelGroupRelation != null\" > #{labelGroupRelation,jdbcType=INTEGER}, </if> <if test=\"fourLabelsNo != null\" > #{fourLabelsNo,jdbcType=VARCHAR}, </if> <if test=\"crowdId != null\" > #{crowdId,jdbcType=INTEGER}, </if> <if test=\"createTime != null\" > #{createTime,jdbcType=TIMESTAMP}, </if> </trim>"
		    +"</script>" 
			})
	@Override
	 	@Options(useGeneratedKeys = true, keyProperty = "id")
		public void insertSelective(LabelGroup entity);
	
	

	/**
	 * 
	 * 功能描述：根据ID删除
	 * 
	 * @return
	 * 
	 * @author 张兵帅
	 * 
	 * @since 2020年05月21日
	 * 
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	@Delete({ "delete from label_group where id = #{id,jdbcType=INTEGER}" })
	@Override
	public void deleteByID(Serializable id);

	/**
	 * 
	 * 功能描述：根据ID数组批量删除
	 * 
	 * @return
	 * 
	 * @author 张兵帅
	 * 
	 * @since 2020年05月21日
	 * 
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	@Delete({
			"<script>"
			+"delete from label_group where id in "
			+"<foreach  item=\"id\"  collection=\"array\" open=\"(\" separator=\",\" close=\")\" > #{id} </foreach>"
			+"</script>" 
			})
	@Override
	public void batchDelete(Serializable... ids);

	/**
	 * 
	 * 功能描述：更新
	 * 
	 * @return
	 * 
	 * @author 张兵帅
	 * 
	 * @since 2020年05月21日
	 * 
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	@Update({ "update label_group set id= #{id,jdbcType=INTEGER},group_no= #{groupNo,jdbcType=VARCHAR},label_relation= #{labelRelation,jdbcType=INTEGER},label_group_relation= #{labelGroupRelation,jdbcType=INTEGER},four_labels_no= #{fourLabelsNo,jdbcType=VARCHAR},crowd_id= #{crowdId,jdbcType=INTEGER},create_time= #{createTime,jdbcType=TIMESTAMP} where id = #{id,jdbcType=INTEGER} " })
	@Override
	public void update(LabelGroup entity);
	
	

	/**
	 * 
	 * 功能描述：选择字段更新
	 * 
	 * @return
	 * 
	 * @author 张兵帅
	 * 
	 * @since 2020年05月21日
	 * 
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	@Update({
			"<script>"
			+"update label_group "
			+"<set > <if test=\"id != null\" > id = #{id,jdbcType=INTEGER}, </if> <if test=\"groupNo != null\" > group_no = #{groupNo,jdbcType=VARCHAR}, </if> <if test=\"labelRelation != null\" > label_relation = #{labelRelation,jdbcType=INTEGER}, </if> <if test=\"labelGroupRelation != null\" > label_group_relation = #{labelGroupRelation,jdbcType=INTEGER}, </if> <if test=\"fourLabelsNo != null\" > four_labels_no = #{fourLabelsNo,jdbcType=VARCHAR}, </if> <if test=\"crowdId != null\" > crowd_id = #{crowdId,jdbcType=INTEGER}, </if> <if test=\"createTime != null\" > create_time = #{createTime,jdbcType=TIMESTAMP}, </if>  </set> "
			+"where id = #{id,jdbcType=INTEGER}"
			+"</script>" 
			})
	@Override
	public void updateSelective(LabelGroup entity);

	
	/**
	 * 
	 * 功能描述：查询所有
	 * 
	 * @return
	 * 
	 * @author 张兵帅
	 * 
	 * @since 2020年05月21日
	 * 
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	@Select({ "select * from label_group" })
	@Results({@Result(column = "id", property = "id" , jdbcType = JdbcType.INTEGER ,id = true  ),@Result(column = "group_no", property = "groupNo" , jdbcType = JdbcType.VARCHAR ),@Result(column = "label_relation", property = "labelRelation" , jdbcType = JdbcType.INTEGER ),@Result(column = "label_group_relation", property = "labelGroupRelation" , jdbcType = JdbcType.INTEGER ),@Result(column = "four_labels_no", property = "fourLabelsNo" , jdbcType = JdbcType.VARCHAR ),@Result(column = "crowd_id", property = "crowdId" , jdbcType = JdbcType.INTEGER ),@Result(column = "create_time", property = "createTime" , jdbcType = JdbcType.TIMESTAMP ) })
	@Override
	public List<LabelGroup> findAll();

	/**
	 * 
	 * 功能描述：查询总数
	 * 
	 * @return
	 * 
	 * @author 张兵帅
	 * 
	 * @since 2020年05月21日
	 * 
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	@Select({ "select count(id) from label_group" })
	@Override
	public int findTotalCount();

	/**
	 * 
	 * 功能描述：根据ID查询
	 * 
	 * @return
	 * 
	 * @author 张兵帅
	 * 
	 * @since 2020年05月21日
	 * 
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	@Select({ "select * from label_group where id = #{id,jdbcType=INTEGER}" })
	@Results({@Result(column = "id", property = "id" , jdbcType = JdbcType.INTEGER ,id = true  ),@Result(column = "group_no", property = "groupNo" , jdbcType = JdbcType.VARCHAR ),@Result(column = "label_relation", property = "labelRelation" , jdbcType = JdbcType.INTEGER ),@Result(column = "label_group_relation", property = "labelGroupRelation" , jdbcType = JdbcType.INTEGER ),@Result(column = "four_labels_no", property = "fourLabelsNo" , jdbcType = JdbcType.VARCHAR ),@Result(column = "crowd_id", property = "crowdId" , jdbcType = JdbcType.INTEGER ),@Result(column = "create_time", property = "createTime" , jdbcType = JdbcType.TIMESTAMP ) })
	@Override
	public LabelGroup findById(Serializable id);

	/**
	 * 
	 * 功能描述：根据查询对象查询
	 * 
	 * @return
	 * 
	 * @author 张兵帅
	 * 
	 * @since 2020年05月21日
	 * 
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	@Select({
			"<script>"
			+"select * from label_group "
			+"<where> 1 = 1 "
			+"<if test=\"id != null\" > and id = #{id,jdbcType=INTEGER} </if><if test=\"groupNo != null\" > and group_no = #{groupNo,jdbcType=VARCHAR} </if><if test=\"labelRelation != null\" > and label_relation = #{labelRelation,jdbcType=INTEGER} </if><if test=\"labelGroupRelation != null\" > and label_group_relation = #{labelGroupRelation,jdbcType=INTEGER} </if><if test=\"fourLabelsNo != null\" > and four_labels_no = #{fourLabelsNo,jdbcType=VARCHAR} </if><if test=\"crowdId != null\" > and crowd_id = #{crowdId,jdbcType=INTEGER} </if><if test=\"createTime != null\" > and create_time = #{createTime,jdbcType=TIMESTAMP} </if> "
			+"</where> order by id </script>" 
			})
	@Results({@Result(column = "id", property = "id" , jdbcType = JdbcType.INTEGER ,id = true  ),@Result(column = "group_no", property = "groupNo" , jdbcType = JdbcType.VARCHAR ),@Result(column = "label_relation", property = "labelRelation" , jdbcType = JdbcType.INTEGER ),@Result(column = "label_group_relation", property = "labelGroupRelation" , jdbcType = JdbcType.INTEGER ),@Result(column = "four_labels_no", property = "fourLabelsNo" , jdbcType = JdbcType.VARCHAR ),@Result(column = "crowd_id", property = "crowdId" , jdbcType = JdbcType.INTEGER ),@Result(column = "create_time", property = "createTime" , jdbcType = JdbcType.TIMESTAMP ) })
	@Override
	public List<LabelGroup> findList(QueryInterface query);

	/**
	 * 
	 * 功能描述：根据查询对象查询记录数
	 * 
	 * @return
	 * 
	 * @author 张兵帅
	 * 
	 * @since 2020年05月21日
	 * 
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	@Select({
			"<script>"
			+"select count(id) from label_group "
			+"<where> 1 = 1 "
			+"<if test=\"id != null\" > and id = #{id,jdbcType=INTEGER} </if><if test=\"groupNo != null\" > and group_no = #{groupNo,jdbcType=VARCHAR} </if><if test=\"labelRelation != null\" > and label_relation = #{labelRelation,jdbcType=INTEGER} </if><if test=\"labelGroupRelation != null\" > and label_group_relation = #{labelGroupRelation,jdbcType=INTEGER} </if><if test=\"fourLabelsNo != null\" > and four_labels_no = #{fourLabelsNo,jdbcType=VARCHAR} </if><if test=\"crowdId != null\" > and crowd_id = #{crowdId,jdbcType=INTEGER} </if><if test=\"createTime != null\" > and create_time = #{createTime,jdbcType=TIMESTAMP} </if> "
			+"</where></script>" 
			})
	@Override
	public int findTotalCountByCondition(QueryInterface query);

	/**
	 * 
	 * 功能描述：根据查询对象查询分页记录
	 * 
	 * @return
	 * 
	 * @author 张兵帅
	 * 
	 * @since 2020年05月21日
	 * 
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	@Select({
			"<script>"
			+"select * from label_group "
			+"<where> 1 = 1 "
			+"<if test=\"id != null\" > and id = #{id,jdbcType=INTEGER} </if><if test=\"groupNo != null\" > and group_no = #{groupNo,jdbcType=VARCHAR} </if><if test=\"labelRelation != null\" > and label_relation = #{labelRelation,jdbcType=INTEGER} </if><if test=\"labelGroupRelation != null\" > and label_group_relation = #{labelGroupRelation,jdbcType=INTEGER} </if><if test=\"fourLabelsNo != null\" > and four_labels_no = #{fourLabelsNo,jdbcType=VARCHAR} </if><if test=\"crowdId != null\" > and crowd_id = #{crowdId,jdbcType=INTEGER} </if><if test=\"createTime != null\" > and create_time = #{createTime,jdbcType=TIMESTAMP} </if> "
			+"</where> order by id "
			+"<if test=\"pagination==1\" > limit #{first,jdbcType=INTEGER},#{pageSize,jdbcType=INTEGER}</if>"
			+"</script>" 
			})
	@Results({@Result(column = "id", property = "id" , jdbcType = JdbcType.INTEGER ,id = true  ),@Result(column = "group_no", property = "groupNo" , jdbcType = JdbcType.VARCHAR ),@Result(column = "label_relation", property = "labelRelation" , jdbcType = JdbcType.INTEGER ),@Result(column = "label_group_relation", property = "labelGroupRelation" , jdbcType = JdbcType.INTEGER ),@Result(column = "four_labels_no", property = "fourLabelsNo" , jdbcType = JdbcType.VARCHAR ),@Result(column = "crowd_id", property = "crowdId" , jdbcType = JdbcType.INTEGER ),@Result(column = "create_time", property = "createTime" , jdbcType = JdbcType.TIMESTAMP ) })
	@Override
	public List<LabelGroup> findPaginationDataByCondition(QueryInterface query);

}


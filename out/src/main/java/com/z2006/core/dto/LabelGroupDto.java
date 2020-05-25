package com.z2006.core.dto;


                                                                                            



import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import com.z2006.core.common.util.DateUtil;

import java.util.Date;


/**
 * 
 * 
 * 
 * @author 张兵帅
 * 
 * @version 1.0
 * 
 * @since 2020年05月21日
 */
public class LabelGroupDto{


						private Integer id; //

						private String groupNo; //组编号

						private Integer labelRelation; //0.且 1.或

						private Integer labelGroupRelation; //0.且 1.或 2.差

						private String fourLabelsNo; //多个标签编码以逗号分隔

						private Integer crowdId; //

	    
    		@DateTimeFormat(pattern = DateUtil.DEFAULT_DATETIME_FORMAT)
    						private Date createTime; //


/** 以下为get,set方法 */
   		 						
        	
        	 public Integer getId() {
		        return this.id;
	        }
	        public void setId(Integer id) {
	        	this.id = id;
	        }
	

   		 						
        	
        	 public String getGroupNo() {
		        return this.groupNo;
	        }
	        public void setGroupNo(String groupNo) {
	        	this.groupNo = groupNo;
	        }
	

   		 						
        	
        	 public Integer getLabelRelation() {
		        return this.labelRelation;
	        }
	        public void setLabelRelation(Integer labelRelation) {
	        	this.labelRelation = labelRelation;
	        }
	

   		 						
        	
        	 public Integer getLabelGroupRelation() {
		        return this.labelGroupRelation;
	        }
	        public void setLabelGroupRelation(Integer labelGroupRelation) {
	        	this.labelGroupRelation = labelGroupRelation;
	        }
	

   		 						
        	
        	 public String getFourLabelsNo() {
		        return this.fourLabelsNo;
	        }
	        public void setFourLabelsNo(String fourLabelsNo) {
	        	this.fourLabelsNo = fourLabelsNo;
	        }
	

   		 						
        	
        	 public Integer getCrowdId() {
		        return this.crowdId;
	        }
	        public void setCrowdId(Integer crowdId) {
	        	this.crowdId = crowdId;
	        }
	

   		     
    		@JsonFormat(pattern = DateUtil.DEFAULT_DATETIME_FORMAT, timezone = DateUtil.DEFAULT_TIMEZONE)
    							
        	
        	 public Date getCreateTime() {
		        return this.createTime;
	        }
	        public void setCreateTime(Date createTime) {
	        	this.createTime = createTime;
	        }
	





}

package com.davidstudio.gbp.tool.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Title: ͨ��bsƽ̨���ù���ϵͳ <br>
 * Descrption�� ����ֵ������ <br>
 * Copyright��Copyright(c) 2004 <br>
 * 
 * @author zxl
 * @date 2005-1-4
 */
@SuppressWarnings("serial")
public class ObjectVO implements Serializable {

	//��������
	private String name;

	//������������
	private String cnName;

	//�����Ӧ���ݿ�����
	private String dbtableName;

	//�б���ʾҳ�棬�Ƿ��в�ѯ����(0,1)Ĭ��1�в�ѯ���� 0û�в�ѯ����
	private int isSearch;

	//����༭ʹ�õı༭ҳ��˵�� (1,2)Ĭ��ʹ��2���б༭ 1���б༭
	private int editStyle;

	//ÿҳ��ʾ��¼����
	private int numPercentPage;

	//�Ƿ����뻺�����
	private int cached;

	//��������������б�
	@SuppressWarnings("rawtypes")
	private List propertyList;

	/**
	 * @return ���� cnName��
	 */
	public String getCnName() {
		return cnName;
	}

	/**
	 * @param cnName
	 *            Ҫ���õ� cnName��
	 */
	public void setCnName(String cnName) {
		this.cnName = cnName;
	}

	/**
	 * @return ���� dbtableName��
	 */
	public String getDbtableName() {
		return dbtableName;
	}

	/**
	 * @param dbtableName
	 *            Ҫ���õ� dbtableName��
	 */
	public void setDbtableName(String dbtableName) {
		this.dbtableName = dbtableName;
	}

	/**
	 * @return ���� editStyle��
	 */
	public int getEditStyle() {
		return editStyle;
	}

	/**
	 * @param editStyle
	 *            Ҫ���õ� editStyle��
	 */
	public void setEditStyle(int editStyle) {
		this.editStyle = editStyle;
	}

	/**
	 * @return ���� isSearch��
	 */
	public int getIsSearch() {
		return isSearch;
	}

	/**
	 * @param isSearch
	 *            Ҫ���õ� isSearch��
	 */
	public void setIsSearch(int isSearch) {
		this.isSearch = isSearch;
	}

	/**
	 * @return ���� name��
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            Ҫ���õ� name��
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return ���� propertyList��
	 */
	@SuppressWarnings("rawtypes")
	public List getPropertyList() {
		return propertyList;
	}

	/**
	 * @param propertyList
	 *            Ҫ���õ� propertyList��
	 */
	@SuppressWarnings("rawtypes")
	public void setPropertyList(List propertyList) {
		this.propertyList = propertyList;
	}

	/**
	 * @return ���� numPercentPage��
	 */
	public int getNumPercentPage() {
		return numPercentPage;
	}

	/**
	 * @param numPercentPage
	 *            Ҫ���õ� numPercentPage��
	 */
	public void setNumPercentPage(int numPercentPage) {
		this.numPercentPage = numPercentPage;
	}

	public int getCached() {
		return cached;
	}

	public void setCached(int cached) {
		this.cached = cached;
	}

}
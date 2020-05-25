package com.davidstudio.gbp.tool.vo;

import java.io.Serializable;

/**
 * Title: ͨ��bsƽ̨���ù���ϵͳ <br>
 * Descrption�� ����ֵ������ <br>
 * Copyright��Copyright(c) 2004 <br>
 * 
 * @author zxl
 * @date 2005-1-4
 */
@SuppressWarnings("serial")
public class PropertyVO implements Serializable {
    //��������
    private String name;

    //������������
    private String cnName;

    //�б��Ƿ���ʾ(0,1)Ĭ��1�б���Ϣ��ʾʱ��ʾ������ 0����ʾ
    private int isList;

    //�б���ʾ��� ���������ֻ�ٷֱ�
    private String listWidth;

    //�Ƿ����б���ʾҳ��Ĳ�ѯ����(0,1)Ĭ��1�в�ѯ���� 0û�в�ѯ����
    private int isSearch;

    //������ʾ���� value��Ҫֵ(string,textarea,select,radio,checkbox,date,tree)
    private String displayType;

    //�����Ե�ֵ�Ƿ�ֻ��(1��,0��)
    private int readOnly;

    //�༭����ʾ�ĳ���,����Ϊ��
    private String display_length;

    // �༭Ĭ�ϵ�ֵ
    private String defaultValue;

    //������������ value(string,number,date,code(�����),id,boolean)
    private String proType;

    //���ݵĳ��ȣ�����󳤶�
    private int pro_length;

    //�������ֵ�С������
    private int pointLength;

    //��ʽ�����ڻ�����ĸ�ʽ
    private String style;

    //�Ƿ��Ϊ��(1��,0��)
    private int isNull;

    //�Ƿ���ظ�(1��,0��)
    private int isRepeat;

    //��Ӧ���ݿ�����ֶ�����
    private String dbcolumnTable;

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
     * @return ���� dbcolumnTable��
     */
    public String getDbcolumnTable() {
        return dbcolumnTable;
    }

    /**
     * @param dbcolumnTable
     *            Ҫ���õ� dbcolumnTable��
     */
    public void setDbcolumnTable(String dbcolumnTable) {
        this.dbcolumnTable = dbcolumnTable;
    }

    /**
     * @return ���� defaultValue��
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * @param defaultValue
     *            Ҫ���õ� defaultValue��
     */
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * @return ���� display_length��
     */
    public String getDisplay_length() {
        return display_length;
    }

    /**
     * @param display_length
     *            Ҫ���õ� display_length��
     */
    public void setDisplay_length(String display_length) {
        this.display_length = display_length;
    }

    /**
     * @return ���� displayType��
     */
    public String getDisplayType() {
        return displayType;
    }

    /**
     * @param displayType
     *            Ҫ���õ� displayType��
     */
    public void setDisplayType(String displayType) {
        this.displayType = displayType;
    }

    /**
     * @return ���� isList��
     */
    public int getIsList() {
        return isList;
    }

    /**
     * @param isList
     *            Ҫ���õ� isList��
     */
    public void setIsList(int isList) {
        this.isList = isList;
    }

    /**
     * @return ���� isNull��
     */
    public int getIsNull() {
        return isNull;
    }

    /**
     * @param isNull
     *            Ҫ���õ� isNull��
     */
    public void setIsNull(int isNull) {
        this.isNull = isNull;
    }

    /**
     * @return ���� isRepeat��
     */
    public int getIsRepeat() {
        return isRepeat;
    }

    /**
     * @param isRepeat
     *            Ҫ���õ� isRepeat��
     */
    public void setIsRepeat(int isRepeat) {
        this.isRepeat = isRepeat;
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
     * @return ���� listWidth��
     */
    public String getListWidth() {
        return listWidth;
    }

    /**
     * @param listWidth
     *            Ҫ���õ� listWidth��
     */
    public void setListWidth(String listWidth) {
        this.listWidth = listWidth;
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
     * @return ���� pointLength��
     */
    public int getPointLength() {
        return pointLength;
    }

    /**
     * @param pointLength
     *            Ҫ���õ� pointLength��
     */
    public void setPointLength(int pointLength) {
        this.pointLength = pointLength;
    }

    /**
     * @return ���� pro_length��
     */
    public int getPro_length() {
        return pro_length;
    }

    /**
     * @param pro_length
     *            Ҫ���õ� pro_length��
     */
    public void setPro_length(int pro_length) {
        this.pro_length = pro_length;
    }

    /**
     * @return ���� proType��
     */
    public String getProType() {
        return proType;
    }

    /**
     * @param proType
     *            Ҫ���õ� proType��
     */
    public void setProType(String proType) {
        this.proType = proType;
    }

    /**
     * @return ���� readOnly��
     */
    public int getReadOnly() {
        return readOnly;
    }

    /**
     * @param readOnly
     *            Ҫ���õ� readOnly��
     */
    public void setReadOnly(int readOnly) {
        this.readOnly = readOnly;
    }

    /**
     * @return ���� style��
     */
    public String getStyle() {
        return style;
    }

    /**
     * @param style
     *            Ҫ���õ� style��
     */
    public void setStyle(String style) {
        this.style = style;
    }
}
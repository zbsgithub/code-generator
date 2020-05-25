package com.davidstudio.gbp.tool.exception;

/**
 * Title: ͨ��bsƽ̨���ù���ϵͳ <br>
 * Descrption�� �Զ�����쳣�� <br>
 * Copyright��Copyright(c) 2004 <br>
 * 
 * @author zxl
 * @date 2005-1-6
 */
@SuppressWarnings("serial")
public class GbpException extends Exception {
    /**
     * ȱʡ���캯��
     */

    public GbpException() {
        super();
    }

    /**
     * ���쳣��Ϣ�Ĺ��캯��
     */

    public GbpException(String message) {
        super(message);
    }
}
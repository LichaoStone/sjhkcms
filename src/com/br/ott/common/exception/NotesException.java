/*
 * 文 件 名:  NoteException.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  cKF46827
 * 修改时间:  2011-9-1
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.br.ott.common.exception;

/**
 * 公用异常类
 * 
 * @author  cKF46827
 * @version  [版本号, 2011-9-1]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public final class NotesException extends Exception {

    /**
     * 注释内容
     */
    private static final long serialVersionUID = 2021939856959046177L;
    
    /** 错误码 */
    private int errorCode;
    
    /** 异常信息 */
    private String msg;
    
    /** 发生异常的路径 */
    private String classPath;

    /**
     * <默认构造函数>
     */
    public NotesException(int errorCode, String msg) {
        super("错误代码(" + errorCode + ")错误信息(" + msg + ")");
        this.errorCode = errorCode;
        this.msg = msg;
    }


    /**
     * <默认构造函数>
     */
    public NotesException(int errorCode, String msg, String classPath) {
        super("错误代码(" + errorCode + ")错误信息(" + msg + ")错误路径(" + classPath + ")");
        this.errorCode = errorCode;
        this.msg = msg;
        this.classPath = classPath;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getClassPath() {
        return classPath;
    }

    public void setClassPath(String classPath) {
        this.classPath = classPath;
    }
}

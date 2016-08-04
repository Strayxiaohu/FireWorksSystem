package com.xiaohu.fireworkssystem.model;

import java.util.List;

/**
 * Created by Administrator on 2016/7/26.
 */
public class CodeModel {

    /**
     * Codes : [{"Key":"1","Value":"男","Parameter":null},{"Key":"2","Value":"女","Parameter":null}]
     * Success : true
     * Message : null
     * Time : /Date(1469500919191)/
     */

    private boolean Success;
    private Object Message;
    private String Time;
    /**
     * Key : 1
     * Value : 男
     * Parameter : null
     */

    private List<CodesBean> Codes;

    public boolean isSuccess() {
        return Success;
    }

    public void setSuccess(boolean Success) {
        this.Success = Success;
    }

    public Object getMessage() {
        return Message;
    }

    public void setMessage(Object Message) {
        this.Message = Message;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String Time) {
        this.Time = Time;
    }

    public List<CodesBean> getCodes() {
        return Codes;
    }

    public void setCodes(List<CodesBean> Codes) {
        this.Codes = Codes;
    }

    public static class CodesBean {
        private String Key;
        private String Value;
        private Object Parameter;

        public String getKey() {
            return Key;
        }

        public void setKey(String Key) {
            this.Key = Key;
        }

        public String getValue() {
            return Value;
        }

        public void setValue(String Value) {
            this.Value = Value;
        }

        public Object getParameter() {
            return Parameter;
        }

        public void setParameter(Object Parameter) {
            this.Parameter = Parameter;
        }
    }
}

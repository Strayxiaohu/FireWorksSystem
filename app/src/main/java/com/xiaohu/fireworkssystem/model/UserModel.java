package com.xiaohu.fireworkssystem.model;

import java.util.List;

/**
 * Created by Administrator on 2016/7/19.
 */
public class UserModel {

    /**
     * page : 1
     * total : 1
     * list : [{"dataid":"241","receiver":"柳伟杰 先生","lat":"","lng":"","buildingid":"0","phone":"","mobilephone":"17093215800","address":"|河北保定|205","isdefault":"0"}]
     */

    private String page;
    private String total;
    /**
     * dataid : 241
     * receiver : 柳伟杰 先生
     * lat :
     * lng :
     * buildingid : 0
     * phone :
     * mobilephone : 17093215800
     * address : |河北保定|205
     * isdefault : 0
     */

    private List<ListBean> list;

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        private String dataid;
        private String receiver;
        private String lat;
        private String lng;
        private String buildingid;
        private String phone;
        private String mobilephone;
        private String address;
        private String isdefault;

        public String getDataid() {
            return dataid;
        }

        public void setDataid(String dataid) {
            this.dataid = dataid;
        }

        public String getReceiver() {
            return receiver;
        }

        public void setReceiver(String receiver) {
            this.receiver = receiver;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public String getBuildingid() {
            return buildingid;
        }

        public void setBuildingid(String buildingid) {
            this.buildingid = buildingid;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getMobilephone() {
            return mobilephone;
        }

        public void setMobilephone(String mobilephone) {
            this.mobilephone = mobilephone;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getIsdefault() {
            return isdefault;
        }

        public void setIsdefault(String isdefault) {
            this.isdefault = isdefault;
        }
    }
}

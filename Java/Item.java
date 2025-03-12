package com.example.capstone;

public class Item implements Comparable<Item> {

    String sm_re_addr;  //addr
    String firstimage;
    String refine_wgs84_logt; //mapx
    String refine_wgs84_lat;  //mapy
    String telno;   //tel
    String tursm_info_nm; //title
    String sigun_nm;

    public String getSm_re_addr() {
        return sm_re_addr;
    }
    public String getFirstimage() {
        return firstimage;
    }
    public String getRefine_wgs84_logt() {
        return refine_wgs84_logt;
    }
    public String getRefine_wgs84_lat() {
        return refine_wgs84_lat;
    }
    public String getTelno() {
        return telno;
    }
    public String getTursm_info_nm() {
        return tursm_info_nm;
    }
    public String getSigun_nm(){return sigun_nm;}

    public void setSm_re_addr(String sm_re_addr) {
        this.sm_re_addr = sm_re_addr;
    }
    public void setFirstimage(String firstimage) {
        this.firstimage = firstimage;
    }
    public void setRefine_wgs84_logt(String refine_wgs84_logt) {
        this.refine_wgs84_logt = refine_wgs84_logt;
    }
    public void setRefine_wgs84_lat(String refine_wgs84_lat) {
        this.refine_wgs84_lat = refine_wgs84_lat;
    }
    public void setTelno(String telno) {
        this.telno = telno;
    }
    public void setTursm_info_nm(String tursm_info_nm) {
        this.tursm_info_nm = tursm_info_nm;
    }
    public void setSigun_nm(String sigun_nm){this.sigun_nm = sigun_nm;}

    public int compareTo(Item another) {
        return this.tursm_info_nm.compareTo(another.tursm_info_nm);
    }
    public String getImageName() {
        return sigun_nm + " " + tursm_info_nm + ".jpg";
    }
}

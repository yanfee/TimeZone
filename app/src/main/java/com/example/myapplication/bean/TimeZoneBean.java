package com.example.myapplication.bean;

/**
 * Created by muyf
 */

public class TimeZoneBean extends BaseIndexPinyinBean {
    private String name;// 名称
    private String gmt;// 名称
    private String id;//

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGmt() {
        return gmt;
    }

    public void setGmt(String gmt) {
        this.gmt = gmt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "TimeZoneBean{" +
                "name='" + name + '\'' +
                ", gmt='" + gmt + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    @Override
    public String getTarget() {
        return name;
    }
}

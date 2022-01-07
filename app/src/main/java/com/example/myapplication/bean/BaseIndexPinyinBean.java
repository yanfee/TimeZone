package com.example.myapplication.bean;

/**
 * Created by muyf
 */

public abstract class BaseIndexPinyinBean {
    protected String indexTag;
    protected String indexPinyin;

    public String getIndexTag() {
        return indexTag;
    }

    public void setIndexTag(String indexTag) {
        this.indexTag = indexTag;
    }

    public String getIndexPinyin() {
        return indexPinyin;
    }

    public void setIndexPinyin(String indexPinyin) {
        this.indexPinyin = indexPinyin;
    }

    //需要转化成拼音的目标字段
    public abstract String getTarget();
}

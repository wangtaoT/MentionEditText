package com.wt.mention.bean;


import java.io.Serializable;
import java.util.List;

public class FormatResult implements Serializable {
    private String text;

    private List<FormatItemResult> userList = null;

    private List<FormatItemResult> topicList = null;

    public FormatResult() {
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<FormatItemResult> getUserList() {
        return userList;
    }

    public void setUserList(List<FormatItemResult> userList) {
        this.userList = userList;
    }

    public List<FormatItemResult> getTopicList() {
        return topicList;
    }

    public void setTopicList(List<FormatItemResult> topicList) {
        this.topicList = topicList;
    }
}

package com.wt.mention.edit.listener;

public interface EditDataListener {
    /**
     * at符号搜索
     *
     * @param str    内容
     * @param start  开始下标
     * @param length 长度
     */
    void onAtUserName(String str, int start, int length);

    /**
     * #号
     *
     * @param start 开始下标
     */
    void onTopicName(int start);

    /**
     * 关闭搜索框
     */
    void onCloseSearchView();
}

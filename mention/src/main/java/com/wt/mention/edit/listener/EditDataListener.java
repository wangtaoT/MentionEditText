package com.wt.mention.edit.listener;

public interface EditDataListener {
    /**
     * at符号响应
     *
     * @param str    内容
     * @param start  开始下标
     * @param length 长度
     */
    void onEditAddAt(String str, int start, int length);

    /**
     * 井号响应
     *
     * @param start 开始下标
     */
    void onEditAddHashtag(int start);

    /**
     * 关闭搜索框
     */
    void onCloseSearchView();
}

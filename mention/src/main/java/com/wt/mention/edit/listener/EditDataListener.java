package com.wt.mention.edit.listener;

public interface EditDataListener {
    /**
     * @ 符号响应
     *
     * @param str    内容
     * @param start  开始下标
     * @param length 长度
     */
    void onEditAddAt(String str, int start, int length);

    /**
     * # 符号响应
     *
     * @param start 开始下标
     */
    void onEditAddHashtag(int start);

    /**
     * 结束输入 如：空格、换行等
     */
    void onCloseEdit();
}

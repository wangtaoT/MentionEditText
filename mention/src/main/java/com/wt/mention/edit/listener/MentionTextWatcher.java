package com.wt.mention.edit.listener;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;

import com.wt.mention.bean.Range;
import com.wt.mention.edit.MentionEditText;
import com.wt.mention.edit.util.RangeManager;

import java.util.Iterator;


public class MentionTextWatcher implements TextWatcher {
    private final MentionEditText mEditText;
    private final RangeManager mRangeManager;

//    private static final String USER_REGEX = "@[^@#\\s]{0,20}?$";     //匹配最后一个@

    public MentionTextWatcher(MentionEditText editText) {
        this.mEditText = editText;
        this.mRangeManager = mEditText.getRangeManager();
    }

    //若从整串string中间插入字符，需要将插入位置后面的range相应地挪位
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        Editable editable = mEditText.getText();
//        LogUtils.e("-->>beforeTextChanged  start=" + start + " , count=" + count + " , after=" + after);
        //相同位置增加
        if (count == 0 && (mEditText.getMentionInputConnection() != null && start + 1 == mEditText.getMentionInputConnection().getLastIndex())) {
            mEditText.getMentionInputConnection().resetLastIndex();
        }
        //在末尾增加就不需要处理了
        if (start < editable.length()) {
            int end = start + count;
            int offset = after - count;

            //清理start 到 start + count之间的span
            //如果range.from = 0，也会被getSpans(0,0,ForegroundColorSpan.class)获取到
            if (start != end && !mRangeManager.isEmpty()) {
                ForegroundColorSpan[] spans = editable.getSpans(start, end, ForegroundColorSpan.class);
                for (ForegroundColorSpan span : spans) {
                    editable.removeSpan(span);
                }
            }

            //清理arraylist中上面已经清理掉的range
            //将end之后的span往后挪offset个位置
            Iterator iterator = mRangeManager.iterator();
            while (iterator.hasNext()) {
                Range range = (Range) iterator.next();
                if (range.isWrapped(start, end)) {
                    iterator.remove();
                    continue;
                }

                if (range.getFrom() >= end) {
                    range.setOffset(offset);
                }
            }
        }
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        if (count == 1 && !TextUtils.isEmpty(charSequence)) {
            char mentionChar = charSequence.toString().charAt(start);
            int selectionStart = mEditText.getSelectionStart();
            if (mentionChar == '#') {
                int index = charSequence.toString().lastIndexOf("#");
                if (mEditText.getEditDataListener() != null) {
                    mEditText.getEditDataListener().onEditAddHashtag(index);
                }
            } else if (mentionChar == ' ') {
                if (mEditText.getEditDataListener() != null) {
                    mEditText.getEditDataListener().onCloseSearchView();
                }
            }
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
        try {
            String mentionChar = editable.toString();
            int selectionStart = mEditText.getSelectionStart();
            mentionChar = mentionChar.substring(0, selectionStart);
            String[] list = mentionChar.split("@");
            if (mentionChar.endsWith("@")) {
                if (mEditText.getEditDataListener() != null) {
                    mEditText.getEditDataListener().onEditAddAt("", selectionStart - 1, 1);
                }
            } else if (!mentionChar.contains("@")
                    || (list.length == 1 && list[0].isEmpty())
                    || (list[list.length - 1].contains(" ") || list[list.length - 1].contains("\n") || list[list.length - 1].contains("#"))) {
                //1、无@符号 关闭
                //2、只有一个空字符 关闭
                //3、最后一个是 空、换行、# 关闭
                if (mEditText.getEditDataListener() != null) {
                    mEditText.getEditDataListener().onCloseSearchView();
                }
            } else {
                String keyword = list[list.length - 1];
                int index = mentionChar.lastIndexOf(keyword) - 1;
                if (mEditText.getEditDataListener() != null) {
                    mEditText.getEditDataListener().onEditAddAt(keyword, index, keyword.length() + 1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 全角转换为半角
     *
     * @param input
     * @return
     */
    public String toDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    /**
     * 全角空格为12288，半角空格为32
     * 其他字符半角(33-126)与全角(65281-65374)的对应关系是：均相差65248
     *
     * @param input 任意字符串
     * @return 全角字符串
     */
    public static String toSBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 32) {
                c[i] = (char) 12288;
                continue;
            }
            if (c[i] < 127)
                c[i] = (char) (c[i] + 65248);
        }
        return new String(c);
    }

}

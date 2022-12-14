package com.wt.mention.edit.listener;

import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;

import com.wt.mention.bean.Range;
import com.wt.mention.edit.MentionEditText;
import com.wt.mention.edit.util.RangeManager;

public class MentionInputConnection extends InputConnectionWrapper {

    private final String TAG = MentionInputConnection.class.getSimpleName();
    private final MentionEditText mEditText;
    private final RangeManager mRangeManager;

    private int lastStart = -1;
    private int lastEnd = -1;

    private final boolean isEnableEditRange;

    private final boolean isEnableSelectionByDelete;

    public MentionInputConnection(InputConnection target, boolean mutable, MentionEditText editText, boolean isEnableEditRange, boolean isEnableSelectionByDelete) {
        super(target, mutable);
        this.mEditText = editText;
        this.mRangeManager = editText.getRangeManager();
        this.isEnableEditRange = isEnableEditRange;
        this.isEnableSelectionByDelete = isEnableSelectionByDelete;
    }

    /**
     * 删除时，需要判断 #tag# 是否是选中状态，选中的tag直接删除，未选中的tag需要先选中
     *
     * @param event
     * @return
     */
    @Override
    public boolean sendKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
            if (null != mRangeManager) {
                // selectionStart表示在选择过程中不变的光标位置
                // selectionEnd表示在选择过程中移动的位置
                int selectionStart = mEditText.getSelectionStart();
                int selectionEnd = mEditText.getSelectionEnd();
                Log.d(TAG, "-->>" + "sendKeyEvent selectionStart=" + selectionStart + " , selectionEnd=" + selectionEnd);
                if (lastStart == selectionStart && lastEnd == selectionEnd) {
                    return true;
                }
                lastStart = selectionStart;
                lastEnd = selectionEnd;
                Range closestRange = mRangeManager.getRangeOfClosestMentionString(selectionStart, selectionEnd);
                Log.d(TAG, "-->> " + mEditText.getText().length());
                if (closestRange == null) {
                    mEditText.setSelected(false);
                    return super.sendKeyEvent(event);
                }

                if (!isEnableEditRange) {
                    if (isEnableSelectionByDelete) {
                        //WT 后面可能需要 删除时先整个选中 第二次再删除
                        if (mEditText.isSelected() || selectionStart == closestRange.getFrom()) {
                            mEditText.setSelected(false);
                            // 删除选中的
                            return super.sendKeyEvent(event);
                        } else {
                            // 选中
                            mEditText.setSelected(true);
                            mRangeManager.setLastSelectedRange(closestRange);
                            setSelection(closestRange.getFrom(), closestRange.getTo());
                        }
                        return true;
                    } else {
                        //直接删除整个高亮区域
                        if (selectionStart > closestRange.getFrom()) {
                            setSelection(closestRange.getFrom(), closestRange.getTo());
                            mEditText.setSelected(false);
                            return super.sendKeyEvent(event);
                        }
                    }
                } else {
                    mEditText.setSelected(false);
                    return super.sendKeyEvent(event);
                }
            }
        }
        return super.sendKeyEvent(event);
    }

    @Override
    public boolean deleteSurroundingText(int beforeLength, int afterLength) {
        if (beforeLength == 1 && afterLength == 0) {
            return sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL)) && sendKeyEvent(
                    new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL));
        }
        return super.deleteSurroundingText(beforeLength, afterLength);
    }

    public int getLastIndex() {
        return lastStart;
    }

    public void resetLastIndex() {
        lastStart = -1;
        lastEnd = -1;
    }
}

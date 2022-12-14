package com.wt.mention.edit.listener;

import android.text.Layout;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.TextView;

/**
 * 目前这个需求 感觉用OnTouchListener 实现有问题，会在span点击生效时，触发onLongClick
 * 因为onTouch的up事件返回true时，不会进入onTouchEvent方法，无法取消长按的监听，所以就触发了onLongClick
 * <p>
 * ListenerInfo li = mListenerInfo;
 * if (li != null && li.mOnTouchListener != null
 * && (mViewFlags & ENABLED_MASK) == ENABLED
 * && li.mOnTouchListener.onTouch(this, event)) {
 * result = true;
 * }
 * <p>
 * if (!result && onTouchEvent(event)) {
 * result = true;
 * }
 */
public class LinkTouchMethod implements View.OnTouchListener {
    long longClickDelay = ViewConfiguration.getLongPressTimeout();
    long startTime = 0;

    /**
     * 需要判断一下，如果触发了
     *
     * @param v
     * @param event
     * @return
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            startTime = System.currentTimeMillis();
        }
        TextView tv = (TextView) v;
        CharSequence text = tv.getText();
        if (text instanceof Spanned) {
            if (action == MotionEvent.ACTION_UP) {
                // 避免长按和点击冲突，如果超过400毫秒，认为是在长按，不执行点击操作
                if (System.currentTimeMillis() - startTime > longClickDelay) {
                    Log.d("LinkTouchMethod", "-->>认为是长按");
                    return false;
                }
                int x = (int) event.getX();
                int y = (int) event.getY();

                x -= tv.getTotalPaddingLeft();
                y -= tv.getTotalPaddingTop();

                x += tv.getScrollX();
                y += tv.getScrollY();
                Log.d("LinkTouchMethod", "-->>y=" + y + " ," + event);
                Layout layout = tv.getLayout();
                // 获取y坐标所在行数
                int line = layout.getLineForVertical(y);
                Log.d("LinkTouchMethod", "-->>line=" + line);
                // 获取所在行数 x坐标的偏移量
                int off = layout.getOffsetForHorizontal(line, x);
                ClickableSpan[] link = ((Spanned) text).getSpans(off, off, ClickableSpan.class);
                if (link.length != 0) {
                    if (x < layout.getLineWidth(line) && x > 0) {
                        link[0].onClick(tv);
                        // 需要拦截view本身的点击事件
                        return true;
                    }
                }
            }
        }
        Log.d("LinkTouchMethod", "-->>没有处理onTouch " + action);
        return false;
    }
}

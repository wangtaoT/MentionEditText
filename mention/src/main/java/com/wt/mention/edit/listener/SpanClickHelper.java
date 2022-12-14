package com.wt.mention.edit.listener;

import android.text.Layout;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

public class SpanClickHelper {

    /**
     * 没有MotionEvent,根本无法在onClick中实现点击span选中效果
     *
     * @param event
     */
    public static boolean spanClickHandle(TextView tv, MotionEvent event) {
        int action = event.getActionMasked();
        CharSequence text = tv.getText();
        if (text instanceof Spanned) {
            if (action == MotionEvent.ACTION_UP) {
                int x = (int) event.getX();
                int y = (int) event.getY();

                x -= tv.getTotalPaddingLeft();
                y -= tv.getTotalPaddingTop();

                x += tv.getScrollX();
                y += tv.getScrollY();
                Log.d("SpanClickHelper", "-->>y=" + y + ",getTotalPaddingTop=" + tv.getTotalPaddingTop() + ",getScrollY=" + tv.getScrollY());
                Layout layout = tv.getLayout();
                // 获取y坐标所在行数
                int line = layout.getLineForVertical(y);
                Log.d("SpanClickHelper", "-->>line=" + line);
                // 获取所在行数 x坐标的偏移量
                int off = layout.getOffsetForHorizontal(line, x);
                ClickableSpan[] link = ((Spanned) text).getSpans(off, off, ClickableSpan.class);
                if (link.length != 0) {
                    if (x < layout.getLineWidth(line) && x > 0) {
                        link[0].onClick(tv);
                        return true;
                    }
                }
            }
        }

        return false;
    }
}

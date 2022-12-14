package com.wt.mention.edit.util;


import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.util.List;

public class ClipboardHelper {

    public static final String TAG = ClipboardHelper.class.getSimpleName();

    private final Context mContext;
    private volatile static ClipboardHelper mInstance;
    private final ClipboardManager mClipboardManager;

    private ClipboardHelper(Context context) {
        mContext = context;
        mClipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
    }

    /**
     * 获取ClipboardUtil实例，记得初始化
     *
     * @return 单例
     */
    public static ClipboardHelper getInstance(Context context) {
        if (mInstance == null) {
            synchronized (ClipboardHelper.class) {
                if (mInstance == null) {
                    mInstance = new ClipboardHelper(context.getApplicationContext());
                }
            }
        }
        return mInstance;
    }

    /**
     * 判断剪贴板内是否有数据
     *
     * @return
     */
    public boolean hasPrimaryClip() {
        return mClipboardManager.hasPrimaryClip();
    }

    /**
     * 获取剪贴板中第一条String
     *
     * @return
     */
    public String getClipText() {
        if (!hasPrimaryClip()) {
            return null;
        }
        ClipData data = mClipboardManager.getPrimaryClip();
        if (data != null
                && mClipboardManager.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
            return data.getItemAt(0).getText().toString();
        }
        return null;
    }

    /**
     * 获取剪贴板中第一条String
     *
     * @param context
     * @return
     */
    public String getClipText(Context context) {
        return getClipText(context, 0);
    }

    /**
     * 获取剪贴板中指定位置item的string
     *
     * @param context
     * @param index
     * @return
     */
    public String getClipText(Context context, int index) {
        if (!hasPrimaryClip()) {
            return null;
        }
        ClipData data = mClipboardManager.getPrimaryClip();
        if (data == null) {
            return null;
        }
        if (data.getItemCount() > index) {
            return data.getItemAt(index).coerceToText(context).toString();
        }
        return null;
    }

    /**
     * 将文本拷贝至剪贴板
     *
     * @param text
     */
    public void copyText(String label, String text) {
        ClipData clip = ClipData.newPlainText(label, text);
        mClipboardManager.setPrimaryClip(clip);
    }

    /**
     * 将HTML等富文本拷贝至剪贴板
     *
     * @param label
     * @param text
     * @param htmlText
     */
    public void copyHtmlText(String label, String text, String htmlText) {
        ClipData clip = ClipData.newHtmlText(label, text, htmlText);
        mClipboardManager.setPrimaryClip(clip);
    }

    /**
     * 将Intent拷贝至剪贴板
     *
     * @param label
     * @param intent
     */
    public void copyIntent(String label, Intent intent) {
        ClipData clip = ClipData.newIntent(label, intent);
        mClipboardManager.setPrimaryClip(clip);
    }

    /**
     * 将Uri拷贝至剪贴板
     * If the URI is a content: URI,
     * this will query the content provider for the MIME type of its data and
     * use that as the MIME type.  Otherwise, it will use the MIME type
     * {@link ClipDescription#MIMETYPE_TEXT_URILIST}.
     * 如 uri = "content://contacts/people"，那么返回的MIME type将变成"vnd.android.cursor.dir/person"
     *
     * @param cr    ContentResolver used to get information about the URI.
     * @param label User-visible label for the clip data.
     * @param uri   The URI in the clip.
     */
    public void copyUri(ContentResolver cr, String label, Uri uri) {
        ClipData clip = ClipData.newUri(cr, label, uri);
        mClipboardManager.setPrimaryClip(clip);
    }

    /**
     * 将多组数据放入剪贴板中，如选中ListView多个Item，并将Item的数据一起放入剪贴板
     *
     * @param label    User-visible label for the clip data.
     * @param mimeType mimeType is one of them:{@link ClipDescription#MIMETYPE_TEXT_PLAIN},
     *                 {@link ClipDescription#MIMETYPE_TEXT_HTML},
     *                 {@link ClipDescription#MIMETYPE_TEXT_URILIST},
     *                 {@link ClipDescription#MIMETYPE_TEXT_INTENT}.
     * @param items    放入剪贴板中的数据
     */
    public void copyMultiple(String label, String mimeType, List<ClipData.Item> items) {
        if (items == null || items.size() == 0) {
            throw new IllegalArgumentException("argument: items error");
        }
        int size = items.size();
        ClipData clip = new ClipData(label, new String[]{mimeType}, items.get(0));
        for (int i = 1; i < size; i++) {
            clip.addItem(items.get(i));
        }
        mClipboardManager.setPrimaryClip(clip);
    }

    public void copyMultiple(String label, String[] mimeTypes, List<ClipData.Item> items) {
        if (items == null || items.size() == 0) {
            throw new IllegalArgumentException("argument: items error");
        }
        int size = items.size();
        ClipData clip = new ClipData(label, mimeTypes, items.get(0));
        for (int i = 1; i < size; i++) {
            clip.addItem(items.get(i));
        }
        mClipboardManager.setPrimaryClip(clip);
    }

    public CharSequence coercePrimaryClipToText() {
        if (!hasPrimaryClip()) {
            return null;
        }
        return mClipboardManager.getPrimaryClip().getItemAt(0).coerceToText(mContext);
    }

    public CharSequence coercePrimaryClipToStyledText() {
        if (!hasPrimaryClip()) {
            return null;
        }
        return mClipboardManager.getPrimaryClip().getItemAt(0).coerceToStyledText(mContext);
    }

    public CharSequence coercePrimaryClipToHtmlText() {
        if (!hasPrimaryClip()) {
            return null;
        }
        return mClipboardManager.getPrimaryClip().getItemAt(0).coerceToHtmlText(mContext);
    }

    /**
     * 获取当前剪贴板内容的MimeType
     *
     * @return 当前剪贴板内容的MimeType
     */
    public String getPrimaryClipMimeType() {
        if (!hasPrimaryClip()) {
            return null;
        }
        return mClipboardManager.getPrimaryClipDescription().getMimeType(0);
    }

    /**
     * 获取剪贴板内容的MimeType
     *
     * @param clip 剪贴板内容
     * @return 剪贴板内容的MimeType
     */
    public String getClipMimeType(ClipData clip) {
        return clip.getDescription().getMimeType(0);
    }

    /**
     * 获取剪贴板内容的MimeType
     *
     * @param clipDescription 剪贴板内容描述
     * @return 剪贴板内容的MimeType
     */
    public String getClipMimeType(ClipDescription clipDescription) {
        return clipDescription.getMimeType(0);
    }

    /**
     * 清空剪贴板
     */
    public void clearClip() {
        mClipboardManager.setPrimaryClip(ClipData.newPlainText(null, ""));
    }

    public ClipData getClipData() {
        if (!hasPrimaryClip()) {
            return null;
        }
        return mClipboardManager.getPrimaryClip();
    }
}

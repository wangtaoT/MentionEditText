package com.wt.mention.bean;


import androidx.annotation.NonNull;

import com.wt.mention.edit.listener.InsertData;
public class Range implements Comparable<Range> {

    private int mFrom;
    private int mTo;
    private InsertData insertData;

    public Range(int from, int to) {
        this.mFrom = from;
        this.mTo = to;
    }

    public boolean isWrapped(int start, int end) {
        return (start <= mFrom && mTo <= end) || (mFrom < start && end < mTo);
    }

    public boolean isWrappedBy(int start, int end) {
        return (start > mFrom && start < mTo) || (end > mFrom && end < mTo);
    }

    public boolean contains(int start, int end) { // 光标落在range中
        return mFrom <= start && mTo >= end;
    }

    public boolean isEqual(int start, int end) {
        return (mFrom == start && mTo == end) || (mFrom == end && mTo == start);
    }

    public int getAnchorPosition(int value) {
        if ((value - mFrom) - (mTo - value) >= 0) {
            return mTo;
        } else {
            return mFrom;
        }
    }

    public void setOffset(int offset) {
        mFrom += offset;
        mTo += offset;
    }

    @Override
    public int compareTo(@NonNull Range o) {
        return mFrom - o.mFrom;
    }

    public int getFrom() {
        return mFrom;
    }

    public void setFrom(int from) {
        this.mFrom = from;
    }

    public int getTo() {
        return mTo;
    }

    public void setTo(int to) {
        this.mTo = to;
    }

    public InsertData getInsertData() {
        return insertData;
    }

    public void setInsertData(InsertData insertData) {
        this.insertData = insertData;
    }
}


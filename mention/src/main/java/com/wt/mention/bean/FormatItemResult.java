package com.wt.mention.bean;


import android.os.Parcel;
import android.os.Parcelable;

public class FormatItemResult implements Parcelable {
    private int fromIndex;

    private int length;

    private String id;

    private String name;

    public FormatItemResult() {
    }

    protected FormatItemResult(Parcel in) {
        fromIndex = in.readInt();
        length = in.readInt();
        id = in.readString();
        name = in.readString();
    }

    public static final Creator<FormatItemResult> CREATOR = new Creator<FormatItemResult>() {
        @Override
        public FormatItemResult createFromParcel(Parcel in) {
            return new FormatItemResult(in);
        }

        @Override
        public FormatItemResult[] newArray(int size) {
            return new FormatItemResult[size];
        }
    };

    public int getFromIndex() {
        return fromIndex;
    }

    public void setFromIndex(int fromIndex) {
        this.fromIndex = fromIndex;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(fromIndex);
        dest.writeInt(length);
        dest.writeString(id);
        dest.writeString(name);
    }
}

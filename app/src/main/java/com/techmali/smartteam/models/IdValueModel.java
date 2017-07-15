package com.techmali.smartteam.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mali on 7/2/2017.
 */

public class IdValueModel  implements Parcelable {
    private String id;
    private String value;
    private boolean isChecked;

    protected IdValueModel(Parcel in) {
        id = in.readString();
        value = in.readString();
        isChecked = in.readByte() != 0;
    }

    public IdValueModel() {
    }

    public static final Creator<IdValueModel> CREATOR = new Creator<IdValueModel>() {
        @Override
        public IdValueModel createFromParcel(Parcel in) {
            return new IdValueModel(in);
        }

        @Override
        public IdValueModel[] newArray(int size) {
            return new IdValueModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(value);
        parcel.writeByte((byte) (isChecked ? 1 : 0));
    }
}

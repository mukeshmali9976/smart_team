package com.techmali.smartteam.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.techmali.smartteam.multipleimage.GalleryItem;

import java.util.ArrayList;

/**
 * Created by niharika on 1/20/2016.
 */
public class GetMediaResponse extends BaseResponse implements Parcelable {

    ArrayList<GalleryItem> Result;

    public ArrayList<GalleryItem> getResult() {
        return Result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(Result);
    }

    public GetMediaResponse() {
    }

    protected GetMediaResponse(Parcel in) {
        this.Result = in.createTypedArrayList(GalleryItem.CREATOR);
    }

    public static final Creator<GetMediaResponse> CREATOR = new Creator<GetMediaResponse>() {
        @Override
        public GetMediaResponse createFromParcel(Parcel source) {
            return new GetMediaResponse(source);
        }

        @Override
        public GetMediaResponse[] newArray(int size) {
            return new GetMediaResponse[size];
        }
    };
}

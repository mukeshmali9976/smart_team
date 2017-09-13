package com.techmali.smartteam.multipleimage;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;

public class GalleryItem implements Parcelable {

    //	public String mImagePath;
    public String ext = "";
    public boolean isSelected = false;
    public String docName;
    public String folderName;
    public File fileName;
    public boolean isFromServer;
    public boolean isThumbImageSelected;
    public boolean needToUpload = true;
    public String videoUrl;
    public boolean isVideo;

    public String attachment_id;
    public String title;
    public String thumb_image;
    public String file_name;
    public String file_type;
    public String is_external_link;
    public int ref_table;
    public String ref_id;
    public int isthumb;
    public String filepath;
    public String thumbpath;
    public String thumbimage;


    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setIsVideo(boolean isVideo) {
        this.isVideo = isVideo;
    }

    protected GalleryItem(Parcel in) {
        ext = in.readString();
        isSelected = in.readByte() != 0;
        docName = in.readString();
        folderName = in.readString();
        isFromServer = in.readByte() != 0;
        isThumbImageSelected = in.readByte() != 0;
        needToUpload = in.readByte() != 0;
        attachment_id = in.readString();
        title = in.readString();
        thumb_image = in.readString();
        file_name = in.readString();
        file_type = in.readString();
        is_external_link = in.readString();
        ref_table = in.readInt();
        ref_id = in.readString();
        isthumb = in.readInt();
        filepath = in.readString();
        thumbpath = in.readString();
        thumbimage = in.readString();
    }

    public static final Creator<GalleryItem> CREATOR = new Creator<GalleryItem>() {
        @Override
        public GalleryItem createFromParcel(Parcel in) {
            return new GalleryItem(in);
        }

        @Override
        public GalleryItem[] newArray(int size) {
            return new GalleryItem[size];
        }
    };

    public boolean isThumbImageSelected() {
        return isThumbImageSelected;
    }

    public String getThumbimage() {
        return thumbimage;
    }

    public String getFilepath() {
        return filepath;
    }

    public int getIsthumb() {
        return isthumb;
    }

    public void setIsthumb(int isthumb) {
        this.isthumb = isthumb;
    }

    public String getAttachment_id() {
        return attachment_id;
    }

    public String getTitle() {
        return title;
    }

    public String getThumb_image() {
        return thumb_image;
    }

    public String getFile_name() {
        return file_name;
    }

    public String getFile_type() {
        return file_type;
    }

    public String getIs_external_link() {
        return is_external_link;
    }

    public int getRef_table() {
        return ref_table;
    }

    public String getRef_id() {
        return ref_id;
    }

    public String getThumbpath() {
        return thumbpath;
    }

    public GalleryItem() {
    }

    public GalleryItem(int isthumb, String filepath, String thumbpath, boolean isFromServer) {
        this.isthumb = isthumb;
        this.filepath = filepath;
        this.thumbpath = thumbpath;
        this.isFromServer = isFromServer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ext);
        dest.writeByte((byte) (isSelected ? 1 : 0));
        dest.writeString(docName);
        dest.writeString(folderName);
        dest.writeByte((byte) (isFromServer ? 1 : 0));
        dest.writeByte((byte) (isThumbImageSelected ? 1 : 0));
        dest.writeByte((byte) (needToUpload ? 1 : 0));
        dest.writeString(attachment_id);
        dest.writeString(title);
        dest.writeString(thumb_image);
        dest.writeString(file_name);
        dest.writeString(file_type);
        dest.writeString(is_external_link);
        dest.writeInt(ref_table);
        dest.writeString(ref_id);
        dest.writeInt(isthumb);
        dest.writeString(filepath);
        dest.writeString(thumbpath);
        dest.writeString(thumbimage);
    }
}

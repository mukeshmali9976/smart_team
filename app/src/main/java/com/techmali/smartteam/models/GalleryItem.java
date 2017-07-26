package com.techmali.smartteam.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;

/**
 * The type Gallery item.
 */
public class GalleryItem implements Parcelable {

    /**
     * The Ext.
     */
//	public String mImagePath;
    public String ext = "";
    /**
     * The Is selected.
     */
    public boolean isSelected = false;
    /**
     * The Doc name.
     */
    public String docName;
    /**
     * The Folder name.
     */
    public String folderName;
    /**
     * The File name.
     */
    public File fileName;
    /**
     * The Is from server.
     */
    public boolean isFromServer;
    /**
     * The Is thumb image selected.
     */
    public boolean isThumbImageSelected;
    /**
     * The Need to upload.
     */
    public boolean needToUpload = true;
    /**
     * The Video url.
     */
    public String videoUrl;
    /**
     * The Is video.
     */
    public boolean isVideo;

    /**
     * The Attachment id.
     */
    public String attachment_id;
    /**
     * The Title.
     */
    public String title;
    /**
     * The Thumb image.
     */
    public String thumb_image;
    /**
     * The File name.
     */
    public String file_name;
    /**
     * The File type.
     */
    public String file_type;
    /**
     * The Is external link.
     */
    public String is_external_link;
    /**
     * The Ref table.
     */
    public int ref_table;
    /**
     * The Ref id.
     */
    public String ref_id;
    /**
     * The Isthumb.
     */
    public int isthumb;
    /**
     * The Filepath.
     */
    public String filepath;
    /**
     * The Thumbpath.
     */
    public String thumbpath;
    /**
     * The Thumbimage.
     */
    public String thumbimage;


    /**
     * Gets video url.
     *
     * @return the video url
     */
    public String getVideoUrl() {
        return videoUrl;
    }

    /**
     * Sets video url.
     *
     * @param videoUrl the video url
     */
    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    /**
     * Sets filepath.
     *
     * @param filepath the filepath
     */
    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    /**
     * Is video boolean.
     *
     * @return the boolean
     */
    public boolean isVideo() {
        return isVideo;
    }

    /**
     * Sets is video.
     *
     * @param isVideo the is video
     */
    public void setIsVideo(boolean isVideo) {
        this.isVideo = isVideo;
    }

    /**
     * Instantiates a new Gallery item.
     *
     * @param in the in
     */
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

    /**
     * The constant CREATOR.
     */
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

    /**
     * Is thumb image selected boolean.
     *
     * @return the boolean
     */
    public boolean isThumbImageSelected() {
        return isThumbImageSelected;
    }

    /**
     * Gets thumbimage.
     *
     * @return the thumbimage
     */
    public String getThumbimage() {
        return thumbimage;
    }

    /**
     * Gets filepath.
     *
     * @return the filepath
     */
    public String getFilepath() {
        return filepath;
    }

    /**
     * Gets isthumb.
     *
     * @return the isthumb
     */
    public int getIsthumb() {
        return isthumb;
    }

    /**
     * Sets isthumb.
     *
     * @param isthumb the isthumb
     */
    public void setIsthumb(int isthumb) {
        this.isthumb = isthumb;
    }

    /**
     * Gets attachment id.
     *
     * @return the attachment id
     */
    public String getAttachment_id() {
        return attachment_id;
    }

    /**
     * Gets title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets thumb image.
     *
     * @return the thumb image
     */
    public String getThumb_image() {
        return thumb_image;
    }

    /**
     * Gets file name.
     *
     * @return the file name
     */
    public String getFile_name() {
        return file_name;
    }

    /**
     * Gets file type.
     *
     * @return the file type
     */
    public String getFile_type() {
        return file_type;
    }

    /**
     * Gets is external link.
     *
     * @return the is external link
     */
    public String getIs_external_link() {
        return is_external_link;
    }

    /**
     * Gets ref table.
     *
     * @return the ref table
     */
    public int getRef_table() {
        return ref_table;
    }

    /**
     * Gets ref id.
     *
     * @return the ref id
     */
    public String getRef_id() {
        return ref_id;
    }

    /**
     * Gets thumbpath.
     *
     * @return the thumbpath
     */
    public String getThumbpath() {
        return thumbpath;
    }

    /**
     * Instantiates a new Gallery item.
     */
    public GalleryItem() {
    }

    /**
     * Instantiates a new Gallery item.
     *
     * @param isthumb      the isthumb
     * @param filepath     the filepath
     * @param thumbpath    the thumbpath
     * @param isFromServer the is from server
     */
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

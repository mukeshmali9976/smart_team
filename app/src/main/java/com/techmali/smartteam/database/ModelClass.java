package com.techmali.smartteam.database;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nrana on 25-Mar-16.
 * ModelClass
 */
public class ModelClass implements Parcelable {

    public int company_id;
    public int assessor_id;
    public long measure_assessment_id;
    public int compentency_id;
    public String behaviour_id;
    public float scale_no;
    public String behaviour_name;
    public String comment;
    public int isAssessed;
    public int user_id;
    public String peer_evaluation_category_id;
    public String peer_comment_category_id;
    public String behavior_score;
    public String is_selected;
    public String overall_comment;

    public String getPeer_evaluation_category_id() {
        return peer_evaluation_category_id;
    }

    public void setPeer_evaluation_category_id(String peer_evaluation_category_id) {
        this.peer_evaluation_category_id = peer_evaluation_category_id;
    }

    public String getPeer_comment_category_id() {
        return peer_comment_category_id;
    }

    public void setPeer_comment_category_id(String peer_comment_category_id) {
        this.peer_comment_category_id = peer_comment_category_id;
    }

    public int getCompany_id() {
        return company_id;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }

    public int getAssessor_id() {
        return assessor_id;
    }

    public void setAssessor_id(int assessor_id) {
        this.assessor_id = assessor_id;
    }

    public long getMeasure_assessment_id() {
        return measure_assessment_id;
    }

    public void setMeasure_assessment_id(long measure_assessment_id) {
        this.measure_assessment_id = measure_assessment_id;
    }

    public int getCompentency_id() {
        return compentency_id;
    }

    public void setCompentency_id(int compentency_id) {
        this.compentency_id = compentency_id;
    }

    public String getBehaviour_id() {
        return behaviour_id;
    }

    public void setBehaviour_id(String behaviour_id) {
        this.behaviour_id = behaviour_id;
    }

    public float getScale_no() {
        return scale_no;
    }

    public void setScale_no(float scale_no) {
        this.scale_no = scale_no;
    }

    public String getBehaviour_name() {
        return behaviour_name;
    }

    public void setBehaviour_name(String behaviour_name) {
        this.behaviour_name = behaviour_name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getIsAssessed() {
        return isAssessed;
    }

    public void setIsAssessed(int isAssessed) {
        this.isAssessed = isAssessed;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getBehavior_score() {
        return behavior_score;
    }

    public void setBehavior_score(String behavior_score) {
        this.behavior_score = behavior_score;
    }

    public String getIs_selected() {
        return is_selected;
    }

    public void setIs_selected(String is_selected) {
        this.is_selected = is_selected;
    }

    public String getOverall_comment() {
        return overall_comment;
    }

    public void setOverall_comment(String overall_comment) {
        this.overall_comment = overall_comment;
    }

    public ModelClass() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.company_id);
        dest.writeInt(this.assessor_id);
        dest.writeLong(this.measure_assessment_id);
        dest.writeInt(this.compentency_id);
        dest.writeString(this.behaviour_id);
        dest.writeFloat(this.scale_no);
        dest.writeString(this.behaviour_name);
        dest.writeString(this.comment);
        dest.writeInt(this.isAssessed);
        dest.writeInt(this.user_id);
        dest.writeString(this.peer_evaluation_category_id);
        dest.writeString(this.peer_comment_category_id);
        dest.writeString(this.behavior_score);
        dest.writeString(this.is_selected);
        dest.writeString(this.overall_comment);
    }

    protected ModelClass(Parcel in) {
        this.company_id = in.readInt();
        this.assessor_id = in.readInt();
        this.measure_assessment_id = in.readLong();
        this.compentency_id = in.readInt();
        this.behaviour_id = in.readString();
        this.scale_no = in.readFloat();
        this.behaviour_name = in.readString();
        this.comment = in.readString();
        this.isAssessed = in.readInt();
        this.user_id = in.readInt();
        this.peer_evaluation_category_id = in.readString();
        this.peer_comment_category_id = in.readString();
        this.behavior_score = in.readString();
        this.is_selected = in.readString();
        this.overall_comment = in.readString();
    }

    public static final Creator<ModelClass> CREATOR = new Creator<ModelClass>() {
        @Override
        public ModelClass createFromParcel(Parcel source) {
            return new ModelClass(source);
        }

        @Override
        public ModelClass[] newArray(int size) {
            return new ModelClass[size];
        }
    };
}

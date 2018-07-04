package com.pychen0918.dramalistdemo.model.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.util.Log;

import com.pychen0918.dramalistdemo.model.gson.DramaData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

@Entity(tableName = "drama")
public class Drama {
    private static final SimpleDateFormat originFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
    private static final SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());

    @PrimaryKey
    @ColumnInfo(name = "id")
    private Integer dramaId;
    private String name;
    private Integer totalViews;
    private String createdAt;
    private String thumb;
    private Float rating;

    public Drama(Integer dramaId, String name, Integer totalViews, String createdAt, String thumb, Float rating) {
        this.dramaId = dramaId;
        this.name = name;
        this.totalViews = totalViews;
        this.createdAt = createdAt;
        this.thumb = thumb;
        this.rating = rating;
    }

    public Drama(DramaData gson){
        this.dramaId = gson.getDramaId();
        this.name = gson.getName();
        this.totalViews = gson.getTotalViews();
        this.createdAt = gson.getCreatedAt();
        this.thumb = gson.getThumb();
        this.rating = gson.getRating();
    }

    public Integer getDramaId() {
        return dramaId;
    }

    public void setDramaId(Integer dramaId) {
        this.dramaId = dramaId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTotalViews() {
        return totalViews;
    }

    public void setTotalViews(Integer totalViews) {
        this.totalViews = totalViews;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getDisplayCreatedTime(){
        String dateString = "";
        try {
            dateString = targetFormat.format(originFormat.parse(getCreatedAt()));
        } catch (ParseException e) {
            Log.e("Drama Class", "Unable to parse created time: "+getCreatedAt());
            e.printStackTrace();
        }
        return dateString;
    }

    public String getDisplayRating(){
        return String.format(Locale.getDefault(), "%.1f", getRating());
    }
}

package com.example.xayru.bakingappv2.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by xayru on 2/27/2018.
 */
@Entity(tableName = "steps_table",
        foreignKeys = {@ForeignKey(entity = Recipe.class,
                parentColumns = "id",
                childColumns = "recipeId",
                onDelete = CASCADE)},
        indices = {@Index(value = "recipeId")})
public class Step {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "server_id")
    private int server_id;
    @ColumnInfo(name = "recipeId")
    private int recipeId;
    @ColumnInfo(name = "shortDescription")
    private String shrtDesc;
    @ColumnInfo(name = "description")
    private String desc;
    @ColumnInfo(name = "videoUrl")
    private String videoUrl;
    @ColumnInfo(name = "thumbUrl")
    private String thumbUrl;

    public Step(int server_id, int recipeId, String shrtDesc, String desc, String videoUrl, String thumbUrl) {
        this.server_id = server_id;
        this.recipeId = recipeId;
        this.shrtDesc = shrtDesc;
        this.desc = desc;
        this.videoUrl = videoUrl;
        this.thumbUrl = thumbUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getServer_id() {
        return server_id;
    }

    public void setServer_id(int server_id) {
        this.server_id = server_id;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public String getShrtDesc() {
        return shrtDesc;
    }

    public void setShrtDesc(String shrtDesc) {
        this.shrtDesc = shrtDesc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }
}

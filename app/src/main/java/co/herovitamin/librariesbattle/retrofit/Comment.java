package co.herovitamin.librariesbattle.retrofit;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kerry on 22/08/15.
 */
public class Comment {

    @SerializedName("id")
    private Integer id;

    @SerializedName("title")
    private String title;

    @SerializedName("content")
    private String content;

    @SerializedName("image")
    private String image;

    @SerializedName("created_at")
    private String created_at;

    @SerializedName("updated_at")
    private String updated_at;

    public Integer get_id() {
        return id;
    }

    public void set_id(Integer id) {
        this.id = id;
    }

    public String get_title() {
        return title;
    }

    public void set_title(String title) {
        this.title = title;
    }

    public String get_content() {
        return content;
    }

    public void set_content(String content) {
        this.content = content;
    }

    public String get_created_at() {
        return created_at;
    }

    public void set_created_at(String created_at) {
        this.created_at = created_at;
    }

    public String get_updated_at() {
        return updated_at;
    }

    public void set_updated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String get_image() {
        return image;
    }

    public void set_image(String image) {
        this.image = image;
    }
}

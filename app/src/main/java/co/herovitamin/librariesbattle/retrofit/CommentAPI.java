package co.herovitamin.librariesbattle.retrofit;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.mime.TypedFile;

/**
 * Created by kerry on 22/08/15.
 */
public interface CommentAPI {

    @GET("/comments.json")
    public List<Comment> get_comments();

    @GET("/comments/{id}.json")
    public void get_comment(@Path("id") String id, Callback<Comment> callback);

    @GET("/comments/{id}.json")
    public Comment get_comment(@Path("id") String id);

    @Multipart
    @POST("/comments.json")
    public void post_comment(
            @Part("comment[title]") String title,
            @Part("comment[content]") String content,
            @Part("comment[image]") TypedFile image,
            Callback<Comment> callback
    );

}
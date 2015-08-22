package co.herovitamin.librariesbattle;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.herovitamin.librariesbattle.retrofit.Comment;
import co.herovitamin.librariesbattle.retrofit.CommentAPI;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

public class PhotoActivity extends AppCompatActivity {

    @Bind(R.id.title_to_send)
    EditText title_to_send;
    @Bind(R.id.content_to_send)
    EditText content_to_send;
    @Bind(R.id.shutter)
    Button shutter;
    @Bind(R.id.send_data)
    Button send_data;

    String file_path;
    File photo_file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        ButterKnife.bind(this);
    }

    public void go(View view){
        new SendDataToServer(title_to_send.getText().toString(), content_to_send.getText().toString(), file_path).execute();
    }

    public void picture_me(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photo_file = null;
        try{
            photo_file = createImageFile();
            if(photo_file != null){
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo_file));
            }
        }catch (IOException e){

        }
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1 && resultCode == RESULT_OK){

        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        file_path = image.getAbsolutePath();
        return image;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class SendDataToServer extends AsyncTask<Void, Integer, Void>{

        RestAdapter rest_adapter;
        CommentAPI api;

        String title, content, file_path;

        public SendDataToServer(String title, String content, String file_path){
            this.title = title;
            this.content = content;
            this.file_path = file_path;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            rest_adapter = new RestAdapter.Builder()
                    .setEndpoint("https://andies.herokuapp.com")
                    .build();
            api = rest_adapter.create(CommentAPI.class);
        }

        @Override
        protected Void doInBackground(Void... params) {

            TypedFile file = new TypedFile("image/jpg", new File(file_path));
            api.post_comment(title, content, file, new Callback<Comment>() {
                @Override
                public void success(Comment comment, Response response) {
                    Log.i("Retrofit", "genial!");
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e("Retrofit error", error.toString());
                    Log.i("Retrofit", ":(");
                }
            });

            return null;
        }
    }
}

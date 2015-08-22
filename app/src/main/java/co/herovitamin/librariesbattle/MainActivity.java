package co.herovitamin.librariesbattle;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.herovitamin.librariesbattle.retrofit.Comment;
import co.herovitamin.librariesbattle.retrofit.CommentAPI;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.id_to_send)
    EditText id_to_send;
    @Bind(R.id.send_button)
    Button send_button;
    @Bind(R.id.result)
    TextView result;
    @Bind(R.id.image_from_server)
    ImageView image_from_server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

    }

    public void check_comment(View view){
        new GetOneComment(id_to_send.getText().toString()).execute();
    }

    public void start_activity(View view){
        Intent intent = new Intent(this, PhotoActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new ExtraTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public class ExtraTask extends AsyncTask<Void, Integer, Void>{

        RestAdapter rest_adapter;
        CommentAPI api;
        List<Comment> comments;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            rest_adapter = new RestAdapter.Builder()
                    .setEndpoint("https://andies.herokuapp.com/")
                    .build();
            api = rest_adapter.create(CommentAPI.class);
        }

        @Override
        protected Void doInBackground(Void... params) {
            comments = api.get_comments();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.i("Retrofit", "Total obtenido:" + comments.size());
            for(Comment tmp : comments)
                Log.i("Retrofit valores", "Id: " + tmp.get_id() + ", Title: " + tmp.get_title() + ", Contenido: " + tmp.get_content());
        }
    };

    public class GetOneComment extends AsyncTask<Void, Integer, Void>{

        Comment result_comment;
        RestAdapter rest_adapter;
        CommentAPI api;

        String id;
        boolean success;

        public GetOneComment(String id){
            this.id = id;
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

            result_comment = api.get_comment(id);

//            api.get_comment(id, new Callback<Comment>() {
//                @Override
//                public void success(Comment comment, Response response) {
//                    result_comment = comment;
//                }
//
//                @Override
//                public void failure(RetrofitError error) {
//                    Log.e("error", error.toString());
//                }
//            });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(result_comment != null){
                result.setText("Título: " + result_comment.get_title() + "\nContenido: " + result_comment.get_content() + "\nImage: " + result_comment.get_image());
                Log.i("TAG", "Título: " + result_comment.get_title() + "\nContenido: " + result_comment.get_content() + "\nImage: " + result_comment.get_image());
                Picasso.with(MainActivity.this).load("https://andies.herokuapp.com"+result_comment.get_image()).into(image_from_server);
                Toast.makeText(MainActivity.this, "Eres genial! Un unicornio acaba de nacer por tu excelente trabajo", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(MainActivity.this, "Deberias ser condenado a usar mouse de bolita por este error", Toast.LENGTH_SHORT).show();
        }
    }
}

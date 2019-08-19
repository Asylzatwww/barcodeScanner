package info.androidhive.barcodereader;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

public class PostSender extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();

    public String barcode_id = "";

    public PostSender(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_sender);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        Bundle extras = getIntent().getExtras();

        barcode_id = extras.getString("barcode_id");


        new GetContacts("post-api/search?model_size_id=" + barcode_id).execute();


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PostSender.this,MainActivity.class));
                Log.e("stop", "ss");
            }
        });
    }

    public void shvei(View v){
        EditText modelSizeQty = findViewById(R.id.modelSizeQty);
        new GetContacts("post-api/move?sklad_id=987&qty="+String.valueOf(modelSizeQty.getText())+"&model_size_id=" + barcode_id).execute();
        Log.e("qty" , String.valueOf(modelSizeQty.getText()));
    }



    private class GetContacts extends AsyncTask<Void, Void, Void> {

        private Integer qty = 0;
        public String url;

        public GetContacts(String url){
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response

            String jsonStr = sh.makeServiceCall(this.url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONArray jsonObj = new JSONArray(jsonStr);

                    // looping through All Contacts
                    for (int i = 0; i < jsonObj.length(); i++) {
                        JSONObject c = jsonObj.getJSONObject(i);
                        this.qty = Integer.valueOf(c.getString("qty"));



                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (this.qty > 0){
                findViewById(R.id.modelSizeQty).setVisibility( View.VISIBLE );
                //TextView text1 = findViewById(R.id.modelSizeTitle);
                //text1.setVisibility( View.VISIBLE );
                //text1.setText(this.qty);
                findViewById(R.id.shvei1).setVisibility( View.VISIBLE );
            } else {
                findViewById(R.id.modelSizeQty).setVisibility( View.INVISIBLE );
                //TextView text1 = findViewById(R.id.modelSizeTitle);
                //text1.setVisibility( View.VISIBLE );
                //text1.setText(this.qty);
                findViewById(R.id.shvei1).setVisibility( View.INVISIBLE );
            }
        }
    }

}

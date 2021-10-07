package com.example.latticeinnovations;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Weather extends AppCompatActivity {
    EditText city;
    TextView lat,lon,cel,far;
    Button show;
    String url1="https://api.weatherapi.com/v1/current.json?key=35c9f92ac5bf4df0811144140212307&q=";
    String url2="&aqi=no",url="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        city = findViewById(R.id.City);
        show = findViewById(R.id.result);
        lat=findViewById(R.id.Lat);
        lon = findViewById(R.id.Lon);
        cel = findViewById(R.id.cel);
        far = findViewById(R.id.Far);

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(city.getText())){
                    try {
                        url=url1+city.getText().toString()+url2;
                        //   Toast.makeText(getApplicationContext(),url,Toast.LENGTH_LONG).show();
                        run();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),"Some Error occured ",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Enter City",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    void run() throws IOException {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String myResponse = response.body().string();

                Weather.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject obj = null;
                        try {
                            //JSONArray jsonarray = new JSONArray(myResponse);
                            obj = new JSONObject(myResponse);

                            JSONObject dist = new JSONObject(obj.getString("location"));
                            lat.setText(dist.getString("lat"));
                            lon.setText(dist.getString("lon"));
                            dist = new JSONObject(obj.getString("current"));
                            cel.setText(dist.getString("temp_c"));
                            far.setText(dist.getString("temp_f"));
                            //   Toast.makeText(getApplicationContext(),dist.getJSONObject(0).getString("District"),Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),"Enter Right City",Toast.LENGTH_LONG).show();
                        }
                        // fetch JSONObject named employee

                        // Toast.makeText(getApplicationContext(),myResponse,Toast.LENGTH_LONG).show();
                        //txtString.setText(myResponse);
                    }
                });

            }
        });
    }
}
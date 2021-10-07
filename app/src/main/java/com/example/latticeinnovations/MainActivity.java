package com.example.latticeinnovations;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity  implements DatePickerDialog.OnDateSetListener {
    EditText name,mobile,dob,address1,address2,pin;
    Button Register,Check;
    Spinner Gender;
    TextView district,state;

    OkHttpClient client = new OkHttpClient();
    public String url1= "https://api.postalpincode.in/pincode/";
    String url="";
    void RegisterView(){
        ///EditTexts
        name = findViewById(R.id.Name);
        mobile = findViewById(R.id.Mobile);
        dob = findViewById(R.id.Dob);
        address1 = findViewById(R.id.Address1);
        address2 = findViewById(R.id.Address2);
        pin = findViewById(R.id.Pin);

        ///Buttons
        Register = findViewById(R.id.Register);
        Check = findViewById(R.id.Check);

        ///Spinner
        Gender = findViewById(R.id.Gender);

        //TextView
        district = findViewById(R.id.district);
        state =  findViewById(R.id.state);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RegisterView();



        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, MainActivity.this, 2020, 1, 1);
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),Weather.class);
                startActivity(i);
                finish();
            }
        });
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });
        Check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextChecker();

               // Toast.makeText(getApplicationContext(),String.valueOf(Gender.getSelectedItem()), Toast.LENGTH_SHORT).show();
            }

        });

    }

    void TextChecker(){
        if(!TextUtils.isEmpty(mobile.getText()) && mobile.getText().length()==10){
            if(!TextUtils.isEmpty(name.getText())){
                if(!TextUtils.isEmpty(dob.getText())){
                    if(!TextUtils.isEmpty(address1.getText())){
                        if(!TextUtils.isEmpty(pin.getText())){
                            /////call Something
                            try {

                                url=url1+pin.getText().toString();
                                Toast.makeText(getApplicationContext(),"LOADING",Toast.LENGTH_LONG).show();
                                run();
                            } catch (IOException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(),"Some Error occured ",Toast.LENGTH_LONG).show();
                            }
                        }else {
                            Toast.makeText(getApplicationContext(),"Enter PIN CODE",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(),"Enter Address",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(),"Enter DOB",Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(getApplicationContext(),"Enter Name",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getApplicationContext(),"Enter correct Number",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        dob.setText(i+"/"+i1+1+"/"+i2);
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

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject obj = null;
                        try {
                            JSONArray jsonarray = new JSONArray(myResponse);
                       //     obj = new JSONObject(myResponse);
                            if(jsonarray.getJSONObject(0).getString("Status").matches("Success")){
                                Register.setEnabled(true);
                            }
                            JSONArray dist = new JSONArray(jsonarray.getJSONObject(0).getString("PostOffice"));
                            district.setText(dist.getJSONObject(0).getString("District"));
                            state.setText(dist.getJSONObject(0).getString("State"));
                         //   Toast.makeText(getApplicationContext(),dist.getJSONObject(0).getString("District"),Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
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
package com.example.stpl.iotcontrol;

import android.content.Intent;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private Button Resolved;
    private ToggleButton Switch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(MainActivity.this,CheckForThreat.class));
        Date d = new Date();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        final String formattedDate = df.format(d.getTime());
        Resolved =(Button) findViewById(R.id.resolve);
        Resolved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResolveProblem("https://vijeth11.000webhostapp.com/homesecurity.php?date="+formattedDate+"&resolved=yes");
               // Toast.makeText(MainActivity.this,"https://vijeth11.000webhostapp.com/homesecurity.php?date="+formattedDate+"&resolved=yes",Toast.LENGTH_SHORT).show();

            }
        });
        Switch = (ToggleButton) findViewById(R.id.onoffbutton);
        Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                 Lightonandof("https://vijeth11.000webhostapp.com/homesecurity.php?on=True");
                    Switch.setTextOn("off");
                }
                else
                {Lightonandof("https://vijeth11.000webhostapp.com/homesecurity.php?on=False");
                    Switch.setTextOff("on");
                }
            }
        });
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                problem("http://vijeth11.000webhostapp.com/homesecurity.php?device=android");
                //Toast.makeText(MainActivity.this, "hitting url ", Toast.LENGTH_SHORT).show();
            }
        }, 0, 5000);
    }

    public void Lightonandof(String Url)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {
                        //hiding the progressbar after completion


                        // Toast.makeText(AddNewItem.this,JSON_URL,Toast.LENGTH_SHORT).show();
                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            //we have the array named hero inside the object
                            //so here we are getting that json array

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);
    }

    public void ResolveProblem(String url)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {
                        //hiding the progressbar after completion


                        // Toast.makeText(AddNewItem.this,JSON_URL,Toast.LENGTH_SHORT).show();
                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            if(obj.getString("error").toString()=="FALSE")
                                Toast.makeText(MainActivity.this,obj.getString("message").toString(),Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(MainActivity.this,obj.getString("message").toString(),Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);
    }
    public void problem(String url)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {
                        //hiding the progressbar after completion


                        // Toast.makeText(AddNewItem.this,JSON_URL,Toast.LENGTH_SHORT).show();
                        try {

                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);
                            if(!obj.has("error")) {
                                //Toast.makeText(MainActivity.this,"none",Toast.LENGTH_SHORT).show();
                                JSONArray ob = obj.getJSONArray("alerts");
                                //Toast.makeText(MainActivity.this,String.valueOf(obj.getJSONObject("alerts")),Toast.LENGTH_SHORT).show();
                                for (int i = 0; i < ob.length(); i++) {
                                    JSONObject data = ob.getJSONObject(i);

                                    String type = data.get("Type").toString();
                                    String discription = data.get("Discription").toString();
                                    //Toast.makeText(MainActivity.this,"type= "+type+"discription="+discription,Toast.LENGTH_SHORT).show();
                                    EditText edt1 = (EditText) findViewById(R.id.editType);
                                    edt1.setText(type);
                                    EditText edt2 = (EditText) findViewById(R.id.editDiscription);
                                    edt2.setText(discription);
                                    Vibrator v = (Vibrator) getSystemService(MainActivity.this.VIBRATOR_SERVICE);
                                    // Vibrate for 500 milliseconds
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        v.vibrate(VibrationEffect.createOneShot(500,VibrationEffect.DEFAULT_AMPLITUDE));
                                    }else{
                                        //deprecated in API 26
                                        v.vibrate(500);
                                    }
                                }
                                //we have the array named hero inside the object
                                //so here we are getting that json array
                            }
                            else{
                                EditText edt1 = (EditText) findViewById(R.id.editType);
                                edt1.setText("");
                                EditText edt2 = (EditText) findViewById(R.id.editDiscription);
                                edt2.setText("");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);
    }
}

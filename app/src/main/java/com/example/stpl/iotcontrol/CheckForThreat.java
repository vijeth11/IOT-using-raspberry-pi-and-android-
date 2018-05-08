package com.example.stpl.iotcontrol;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.cert.Certificate;
import java.util.Timer;
import java.util.TimerTask;


public class CheckForThreat extends Service {
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(CheckForThreat.this, "hitting url ", Toast.LENGTH_SHORT).show();

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Toast.makeText(CheckForThreat.this, "hitting url ", Toast.LENGTH_SHORT).show();
                getData("http://vijeth11.000webhostapp.com/homesecurity.php?device=android");

            }
        }, 0, 5000);
        return super.onStartCommand(intent, flags, startId);
    }

    public void getData(String Url)
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
                            if(obj.has("error")) {
                                Toast.makeText(CheckForThreat.this, "none", Toast.LENGTH_SHORT).show();
                                int mNotificationId = 001;

                                // Build Notification , setOngoing keeps the notification always in status bar
                                NotificationCompat.Builder mBuilder =
                                        new NotificationCompat.Builder(CheckForThreat.this)
                                                .setContentTitle("Stop LDB")
                                                .setContentText("Click to stop LDB")
                                                .setOngoing(true);

                                // Create pending intent, mention the Activity which needs to be
                                //triggered when user clicks on notification(StopScript.class in this case)

                                PendingIntent contentIntent = PendingIntent.getActivity(CheckForThreat.this, 0,
                                        new Intent(CheckForThreat.this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);


                                mBuilder.setContentIntent(contentIntent);


                                // Gets an instance of the NotificationManager service
                                NotificationManager mNotifyMgr =
                                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                                // Builds the notification and issues it.
                                mNotifyMgr.notify(mNotificationId, mBuilder.build());

                            }
                            JSONArray ob=obj.getJSONArray("alerts");
                            for(int i =0;i<ob.length();i++)
                            {
                                JSONObject data= ob.getJSONObject(i);
                                String type=data.get("type").toString();
                                String discription = data.get("discription").toString();
                            }
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
                        Toast.makeText(CheckForThreat.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(CheckForThreat.this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);
    }

}

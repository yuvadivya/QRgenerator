package com.example.qrc;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.Firebase;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    String user, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText username = (EditText)findViewById(R.id.etName);
        final EditText password = (EditText)findViewById(R.id.etPassword);
        final Button b=(Button)findViewById(R.id.btnLogin);
        Firebase.setAndroidContext(this);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user= username.getText().toString();
                pass= password.getText().toString();
                if(user.equals("")){
                    username.setError("can't be blank");
                }
                else if(pass.equals("")){
                    password.setError("can't be blank");
                }
                else if(!user.matches("[A-Za-z0-9]+")){
                    username.setError("only alphabet or number allowed");
                }
                else if(user.length()<5){
                    username.setError("at least 5 characters long");
                }
                else if(pass.length()<5){
                    password.setError("at least 5 characters long");
                }
                else {

                    String url = "https://qrcode-32be4.firebaseio.com/users.json";

                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String s) {
                            Firebase reference = new Firebase("https://qrcode-32be4.firebaseio.com/users");

                            if (s.equals("null")) {
                                reference.child(user).child("password").setValue(pass);
                                Toast.makeText(MainActivity.this, "registration successful", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(MainActivity.this, login.class));
                            } else {
                                try {
                                    JSONObject obj = new JSONObject(s);

                                    if (!obj.has(user)) {
                                        reference.child(user).child("password").setValue(pass);
                                        Toast.makeText(MainActivity.this, "registration successful", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(MainActivity.this, login.class));
                                    } else if (obj.has(user) && obj.getJSONObject(user).getString("password").equals(pass)){
                                        startActivity(new Intent(MainActivity.this, login.class));
                                    }
                                    else{
                                        Toast.makeText(MainActivity.this, "incorrect password", Toast.LENGTH_LONG).show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            System.out.println("" + volleyError);
                        }
                    });
                    RequestQueue rQueue = Volley.newRequestQueue(MainActivity.this);
                    rQueue.add(request);
                }
            }
        });
    }
}

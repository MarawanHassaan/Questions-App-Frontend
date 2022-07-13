package com.example.store.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.store.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class MainActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private boolean serverAuthenticated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RequestQueue queue = Volley.newRequestQueue(this);
        serverAuthenticated = isLoggedIn();

        callbackManager = CallbackManager.Factory.create();

        final LoginButton loginButton = findViewById(R.id.btn_fb_login);
        loginButton.setPermissions("public_profile, email");
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                // authenticate with backend
                String baseUrl = getString(R.string.backend_base_url) + "check_mobile_login";
                String url = String.format("%s?token=%s", baseUrl, loginResult.getAccessToken().getToken());

                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                AccessToken.setCurrentAccessToken(loginResult.getAccessToken());
                                if (response.equals(Profile.getCurrentProfile().getId())) {
                                    serverAuthenticated = true;
                                    Toast.makeText(MainActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(MainActivity.this, QuestionsActivity.class));
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("bibo", error.toString());
                                Toast.makeText(MainActivity.this, "Unable to authenticate with server", Toast.LENGTH_SHORT).show();
                            }
                        });

                queue.add(stringRequest);
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "Login cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(MainActivity.this, "Error logging in", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isLoggedIn() && serverAuthenticated) {
            startActivity(new Intent(this, QuestionsActivity.class));
        } else {
            serverAuthenticated = false;
            LoginManager.getInstance().logOut();
        }
    }

    private boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null && !accessToken.isExpired();
    }
}

package com.example.store.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.store.R;
import com.example.store.models.Question;
import com.facebook.AccessToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PostActivity extends HomeActivity  implements AdapterView.OnItemSelectedListener {

    private TextView tv_cartTotalPrice;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_post);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        final View contentView = inflater.inflate(R.layout.activity_post, null, false);
        mDrawer.addView(contentView, 0);
        getSupportActionBar().setTitle("Post a question");

        final RequestQueue queue = Volley.newRequestQueue(this);

        fb_cartCheckout.setVisibility(View.INVISIBLE);
        tv_cartTotalPrice = findViewById(R.id.tv_cart_total_price);

        final EditText quesTilte = findViewById(R.id.add_title);
        final EditText quesDescription = findViewById(R.id.add_content);
        final Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(PostActivity.this, R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        Button btn_cartCheckout = findViewById(R.id.btn_checkout_cart);
        btn_cartCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (true) {
                    JSONObject new_question = new JSONObject();
                    try {
                        new_question.put("title",quesTilte.getText() );
                        new_question.put("content",quesDescription.getText() );
                        new_question.put("category",spinner.getSelectedItem().toString());
                        //new_question.put("customer_id", 1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    //String text = String.format("Checking out %d items", itemsList.size());
                    final Toast tst_checkOut = Toast.makeText(PostActivity.this, "Posting the question", Toast.LENGTH_SHORT);
                    tst_checkOut.show();

                    JSONArray jsonArray = new JSONArray();
                    jsonArray.put(new_question);

                    String baseUrl = getString(R.string.backend_base_url) + "questions";
                    String url = String.format("%s?token=%s", baseUrl, AccessToken.getCurrentAccessToken().getToken());

                    JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, jsonArray,
                            new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    tst_checkOut.cancel();

                                    if (response.length() > 0) {
                                        //db.clearRecords();
                                        //finish();
                                        Log.d("bibo", response.toString());
                                        Toast.makeText(PostActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(PostActivity.this, QuestionsActivity.class));
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    tst_checkOut.cancel();
                                    Toast.makeText(PostActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                }
                            });

                    queue.add(jsonArrayRequest);

                    String baseUrl2 = getString(R.string.backend_base_url) + "comments/test";
                    String url2 = String.format("%s?token=%s", baseUrl2, AccessToken.getCurrentAccessToken().getToken());
                    StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url2,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.d("bibo", response);
                                    Log.d("bibo", "Comment test");

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("bibo", error.toString());
                                    Log.d("bibo", "FAIL Comment test");

                                }
                            });
                    queue.add(stringRequest2);




                } else {
                    Toast.makeText(PostActivity.this, "No items to checkout", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //String text = parent.getItemAtPosition(position).toString();
        //Toast.makeText(parent.getContext(),text, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

package com.example.store.activities;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.store.R;
import com.example.store.adapters.questionAdapter;
import com.example.store.models.Question;
import com.facebook.AccessToken;
import com.facebook.Profile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class MyquestionsActivity extends HomeActivity {

    //private ArrayList<Order> ordersList;
    //private OrdersAdapter ordersAdapter;
    private ArrayList<Question> questionArrayList;
    private questionAdapter questionAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_myquestions);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_myquestions, null, false);
        mDrawer.addView(contentView, 0);
        getSupportActionBar().setTitle("My Questions");

        fb_cartCheckout.setVisibility(View.INVISIBLE);

        final RequestQueue queue = Volley.newRequestQueue(this);

        String baseUrl = getString(R.string.backend_base_url) + "customer/orders";
        String url = String.format("%s?token=%s", baseUrl, AccessToken.getCurrentAccessToken().getToken());

        questionArrayList = new ArrayList<>();
        recyclerView = findViewById(R.id.rv_products_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //final ListView listView = findViewById(R.id.lv_orders_list);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jo = response.getJSONObject(i);
                                Log.d("bibo",jo.toString());
                                Question question = new Question();
                                question.setTitle(jo.getString("title"));
                                question.setContent(jo.getString("content"));
                                //question.setCustomer_name("Asked on "+ date_adjust(jo.getString("created_at")));
                                question.setCustomer_name(Profile.getCurrentProfile().getName());
                                question.setQuestion_id(jo.getInt("id"));
                                question.setCategory(jo.getString("category"));
                                questionArrayList.add(question);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        questionAdapter = new questionAdapter(questionArrayList);
                        //recyclerView.setAdapter(productsAdapter);
                        recyclerView.setAdapter(questionAdapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        queue.add(jsonArrayRequest);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String  date_adjust(String dateString) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyy", Locale.ENGLISH);
        LocalDate date = LocalDate.parse(dateString, inputFormatter);
        String formattedDate = outputFormatter.format(date);
        Log.d("bibo", formattedDate);
        return formattedDate;
    }
}

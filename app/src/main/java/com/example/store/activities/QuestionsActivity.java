package com.example.store.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.like.LikeButton;
import com.like.OnLikeListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class QuestionsActivity extends HomeActivity {

    private RecyclerView recyclerView;
    private ArrayList<Question> questionArrayList;
    private ArrayList<Question> questionArrayList_editable;
    private questionAdapter questionAdapter;
    private Button button_filter;
    private String[] listCategories;
    private boolean[] checkedItems;
    ArrayList<String> mUserItems = new ArrayList<>();

    //private ProductsAdapter productsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_questions);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_questions, null, false);
        mDrawer.addView(contentView, 0);
        getSupportActionBar().setTitle("Questions");

        String baseUrl = getString(R.string.backend_base_url) + "/gett";
        String url = String.format("%s?token=%s", baseUrl, AccessToken.getCurrentAccessToken().getToken());

        questionArrayList = new ArrayList<>();



        recyclerView = findViewById(R.id.rv_products_list);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        RequestQueue queue = Volley.newRequestQueue(this);


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(@NonNull JSONArray response) {
                        try {
                            Log.d("bibo", response.toString());

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jo = response.getJSONObject(i);

                                Question question = new Question();
                                question.setTitle(jo.getString("title"));
                                question.setContent(jo.getString("content"));
                                question.setCustomer_name(jo.getString("customer_name"));
                                //question.setCustomer_id(jo.getInt("customer_id"));
                                question.setQuestion_id(jo.getInt("id"));
                                question.setCategory(jo.getString("category"));

                                questionArrayList.add(question);
                                //Log.d("bibo", question.getCategory().toString());
                            }
                        } catch (JSONException e) {
                            Log.d("bibo", "theree");

                            e.printStackTrace();
                        }

                        //productsAdapter = new ProductsAdapter(productArrayList);
                        questionAdapter = new questionAdapter(questionArrayList);
                        //recyclerView.setAdapter(productsAdapter);
                        recyclerView.setAdapter(questionAdapter);
                    }


                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(@NonNull VolleyError error) {
                        Toast.makeText(QuestionsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(jsonArrayRequest);
        /*
        questionArrayList.remove(1);
        RecyclerView.removeViewAt(1);
        questionAdapter.notifyItemRemoved(1);
        questionAdapter.notifyItemRangeChanged(1, questionArrayList.size());
        */
        button_filter = (Button) findViewById(R.id.button_filter);
        listCategories = getResources().getStringArray(R.array.categories);
        checkedItems = new boolean[listCategories.length];
        for(int j=0; j<checkedItems.length;j++){
            checkedItems[j]= true;
        }
        mUserItems.add("Sports");
        mUserItems.add("Technology");
        mUserItems.add("Entertainment");
        mUserItems.add("Language");
        mUserItems.add("Politics");

        button_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder =  new AlertDialog.Builder(QuestionsActivity.this);
                mBuilder.setTitle("Select Categories");
                mBuilder.setMultiChoiceItems(listCategories, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                        if(isChecked){
                            if(!mUserItems.contains(listCategories[position])){
                                mUserItems.add(listCategories[position]);
                                Log.d("bibo","heu");
                                Log.d("bibo", String.valueOf(position));
                                Log.d("bibo", String.valueOf(mUserItems));
                            }
                        } else if(mUserItems.contains(listCategories[position])){
                            Log.d("bibo","TRR");
                            Log.d("bibo", String.valueOf(position));
                            mUserItems.remove(listCategories[position]);
                            Log.d("bibo", String.valueOf(mUserItems));
                        }

                    }
                });
                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        questionArrayList_editable = new ArrayList<>();
                        Log.d("bibo",mUserItems.toString());
                        for (int j=0; j < questionArrayList.size();j++) {
                            Question question= questionArrayList.get(j);
                            Log.d("bibo", String.valueOf(question.getContent()));
                            Log.d("bibo", String.valueOf(question.getCategory()));
                            String cat = String.valueOf(question.getCategory());
                            Log.d("bibo",cat);
                            if(mUserItems.contains(cat)){
                                Log.d("bibo","Correct");
                                questionArrayList_editable.add(question);
                            }
                        }
                        questionAdapter = new questionAdapter(questionArrayList_editable);
                        recyclerView.setAdapter(questionAdapter);

                        Log.d("bibo", String.valueOf(questionArrayList_editable.size()));



                    }
                });
                mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();


            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        if (isLoggedIn) {
            finishAffinity();
        }
    }
}

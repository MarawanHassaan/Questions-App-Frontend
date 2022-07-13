package com.example.store.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.store.R;
import com.example.store.adapters.ItemAdapter;
import com.example.store.models.Comment;
import com.example.store.models.Like;
import com.example.store.models.Question;
import com.example.store.models.delete_Dialog;
import com.example.store.models.like_main;
import com.facebook.AccessToken;
import com.facebook.Profile;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QuestionActivity extends HomeActivity {

    @Nullable
    private ElegantNumberButton btn_productQuantity;
    private TextView tv_totalPrice;
    private Double price;
    private ArrayList<Comment> commentArrayList;
    private ArrayList<Comment> finalCommentArrayList;
    ListView myListView;
    String[] items;
    String[] bodies;
    String[] comments_ids;
    String[] like_count;
    Boolean[] like_flags;
    Boolean[] dislike_flags;
    int[] like_ids;
    private CardView hcard_view;

    private EditText edit_question_title;
    private EditText edit_question_content;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_question);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final RequestQueue queue = Volley.newRequestQueue(this);
        final RequestQueue queue2 = Volley.newRequestQueue(this);
        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_question, null, false);
        mDrawer.addView(contentView, 0);
        getSupportActionBar().setTitle("Question");

        //Color Switching
        hcard_view = findViewById(R.id.cardView_question_1);


        // display product
        final TextView tv_productName = findViewById(R.id.tv_product_name);
        final TextView tv_productPrice = findViewById(R.id.tv_product_price);
        final TextView tv_productDescription = findViewById(R.id.tv_product_description);
        final TextView tv_productCategory = findViewById(R.id.tv_item_product_category);
        final ImageView imageView = findViewById(R.id.profile_pic_inside_the_question);
        //TextView tv_productStock = findViewById(R.id.tv_product_stock);
        /////////////////////////////////////////////////////////////////////////////////////
        commentArrayList = new ArrayList<Comment>();
        String baseUrl2 = getString(R.string.backend_base_url) + "comments/test";
        String url2 = String.format("%s?token=%s", baseUrl2, AccessToken.getCurrentAccessToken().getToken());
        JsonArrayRequest jsonArrayRequest2 = new JsonArrayRequest
                (Request.Method.GET, url2, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(@NonNull JSONArray response) {
                        Log.d("bibo", "TESTING >>>>>>>>>");

                        Log.d("bibo", response.toString());
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jo = response.getJSONObject(i);
                                Comment comment = new Comment();
                                comment.setComment_id(jo.getInt("id"));
                                comment.setBody(jo.getString("body"));
                                comment.setCustomer_id(jo.getInt("customer_id"));
                                comment.setQuestion_id(jo.getInt("question_id"));
                                comment.setCustomer_name(jo.getString("customer_name"));
//                                Log.d("bibo", "testing");
//                                Log.d("bibo", jo.getJSONArray("testing").toString());


                                //Log.d("bibo",jo.getString("customer_name"));
                                //Log.d("bibo",Profile.getCurrentProfile().getName());
                                //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                                //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                                JSONArray like_array = jo.getJSONArray("testing");
                                if (like_array.length() != 0) {
                                    int likes_counter = 0;
                                    for (int trunks = 0; trunks < like_array.length(); trunks++) {
                                        int inc = like_array.getJSONObject(trunks).getInt("counter");
                                        likes_counter = likes_counter + inc;

                                        String likeeeeeeeeeeeeeeeeeeer = jo.getJSONArray("users_like").getJSONObject(trunks).getString("name");
//                                        Log.d("bibo", "PPPPPPPPPPPPPPPP");
//                                        Log.d("bibo", likeeeeeeeeeeeeeeeeeeer);
                                        if (inc == 1 && Profile.getCurrentProfile().getName().equals(likeeeeeeeeeeeeeeeeeeer)) {
                                            Log.d("bibo", "P");
                                            comment.setLike_flag(true);
                                            comment.setLike_id(like_array.getJSONObject(trunks).getInt("id"));
//                                            Log.d("bibo", String.valueOf(comment.getLike_id()));
                                        } else if (inc == -1 && Profile.getCurrentProfile().getName().equals(likeeeeeeeeeeeeeeeeeeer)) {
                                            comment.setDislike_flag(true);
                                            comment.setLike_id(like_array.getJSONObject(trunks).getInt("id"));
//                                            Log.d("bibo", String.valueOf(comment.getLike_id()));
                                        } else if (inc == 0 && Profile.getCurrentProfile().getName().equals(likeeeeeeeeeeeeeeeeeeer)) {
                                            comment.setLike_id(like_array.getJSONObject(trunks).getInt("id"));
//                                            Log.d("bibo", String.valueOf(comment.getLike_id()));
                                        }

                                    }
                                    comment.setCounter_of_likes(likes_counter);
                                    //Log.d("bibo", "Biiiiiiiiiiiiiiilll gates");
                                    //Log.d("bibo", String.valueOf(comment.getCounter_of_likes()));
                                }
                                //Log.d("bibo", String.valueOf(comment.getCounter_of_likes()));

                                ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


                                JSONArray myObject = jo.getJSONArray("users_like");
                                if (myObject.length() != 0) {
                                    ArrayList<Like> likes = new ArrayList<>();
                                    for (int gohan = 0; gohan < myObject.length(); gohan++) {
                                        //Log.d("bibo", String.valueOf(myObject.getJSONObject(gohan).getInt("id")));
//                                        Log.d("bibo", "after");
                                        Like like = new Like();
                                        like.setCustomer_id(myObject.getJSONObject(gohan).getInt("id"));
                                        likes.add(like);
                                    }
                                    comment.setLikes(likes);
                                    Log.d("bibo", String.valueOf(likes.size()));
                                }

                                //ArrayList<Like> check = comment.getLikes();
                                if (comment.getLikes() == null) {
                                    //Log.d("bibo", comment.getComment_id().toString());
                                    //Log.d("bibo", "the comment has no likes");
                                }


                                ///////// TEST /////////////////////////////////////////////
                                final Intent intent = getIntent();
                                /////////////////////////////////////////////////////////////////////////////////////
                                String goku = intent.getStringExtra("question_id");
                                int gohan = Integer.parseInt(goku);

                                int vegeta = comment.getQuestion_id().intValue();
                                if (vegeta == gohan) {
                                    commentArrayList.add(comment);
                                }


                                ////////////////////////END OF TEST /////////////////////////////////////
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d("bibo", "Likes didn't work");
                            }

                        }
                        Log.d("bibo", "HEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
                        Log.d("bibo", String.valueOf(commentArrayList.size()));
                        Log.d("bibo", commentArrayList.toString());
                        Log.d("bibo", "HEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
                        items = new String[commentArrayList.size()];
                        bodies = new String[commentArrayList.size()];
                        comments_ids = new String[commentArrayList.size()];
                        like_count = new String[commentArrayList.size()];
                        like_flags = new Boolean[commentArrayList.size()];
                        dislike_flags = new Boolean[commentArrayList.size()];

                        like_ids = new int[commentArrayList.size()];

                        int i = 0;
                        for (Comment comment : commentArrayList) {
                            comments_ids[i] = comment.getComment_id().toString();
                            items[i] = comment.getCustomer_name().toString();
                            bodies[i] = comment.getBody().toString();
                            like_count[i] = String.valueOf(comment.getCounter_of_likes());


                            like_flags[i] = comment.isLike_flag();
                            dislike_flags[i] = comment.isDislike_flag();
                            like_ids[i] = comment.getLike_id();
                            //Log.d("bibo","PRINTING THE VALUES OF like ids");
                            //Log.d("bibo", String.valueOf(like_ids[i]));

                            i++;
                        }

                        //Resources res = getResources();
                        myListView = (ListView) findViewById(R.id.myListView);
                        //items = res.getStringArray(R.array.items);
                        //bodies = res.getStringArray(R.array.bodies);
                        //myListView.setAdapter(new ArrayAdapter<String>(QuestionActivity.this,R.layout.my_listview_detail,items));
                        final Intent intent2 = getIntent();
                        String question_title = intent2.getStringExtra("question_title");
                        String question_content = intent2.getStringExtra("question_content");
                        String question_customer_name = intent2.getStringExtra("question_customer_name");
                        String question_id = intent2.getStringExtra("question_id");
                        String category = intent2.getStringExtra("question_category");
                        ItemAdapter itemAdapter = new ItemAdapter(QuestionActivity.this, items, bodies, comments_ids, like_count, like_flags, dislike_flags, like_ids,
                                question_title, question_content, question_customer_name, question_id, category);
                        myListView.setAdapter(itemAdapter);


                    }


                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(@NonNull VolleyError error) {
                        Toast.makeText(QuestionActivity.this, "Error fetching products", Toast.LENGTH_SHORT).show();
                    }
                });
        queue.add(jsonArrayRequest2);

        ////////////////////////THIS PART UNNEEDED///////////////////////////////////////////////////////
        final Intent intent = getIntent();
        final String goku = intent.getStringExtra("question_id");
        Log.d("bibo", goku);
        Log.d("bibo", "Mars is replying");
        finalCommentArrayList = new ArrayList<Comment>();
        Log.d("bibo", String.valueOf(commentArrayList.size()));
        for (Comment comment : commentArrayList) {
            if (comment.getQuestion_id() == 2) {
                Log.d("bibo", String.valueOf(commentArrayList.size()));
                finalCommentArrayList.add(comment);
            }
        }
        Log.d("bibo", String.valueOf(finalCommentArrayList.size()));
        Log.d("bibo", "Mars is cooooooooooooooooooooooooooooooooo");





        /////////////////////////////////////////////////////////////////////////////////
        tv_productName.setText(intent.getStringExtra("question_title"));
        tv_productDescription.setText(intent.getStringExtra("question_content"));
        tv_productPrice.setText(intent.getStringExtra("question_customer_name"));
        if(intent.getStringExtra("question_customer_name").equals(Profile.getCurrentProfile().getName())){
            Picasso.get().load(Profile.getCurrentProfile().getProfilePictureUri(50,50)).into(imageView);
        }


        Log.d("bibo", "Mars is cooooooooooooooooooooooooooooooooo");
        tv_productCategory.setText('#' + intent.getStringExtra("question_category").toLowerCase());
        String get_my_category = intent.getStringExtra("question_category");
        if (get_my_category.equals("Sports")) {
            hcard_view.setCardBackgroundColor(Color.parseColor("#1A89cc02"));
        } else if (get_my_category.equals("Technology")) {
            hcard_view.setCardBackgroundColor(Color.parseColor("#1A8f8109"));
        } else if (get_my_category.equals("Entertainment")) {
            hcard_view.setCardBackgroundColor(Color.parseColor("#1Aeb0081"));
        } else if (get_my_category.equals("Language")) {
            hcard_view.setCardBackgroundColor(Color.parseColor("#1A076782"));
        } else if (get_my_category.equals("Politics")) {
            hcard_view.setCardBackgroundColor(Color.parseColor("#1Ae8150c"));
        }


        Log.d("bibo", "Mars is cooooooooooooooooooooooooooooooooo");
        final String goku2 = intent.getStringExtra("question_id");
        final int gohan2 = Integer.parseInt(goku);

        Button btn_add_comment = findViewById(R.id.btn_add_product_to_cart);
        btn_add_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText add_comment = findViewById(R.id.add_comment);
                JSONObject new_comment = new JSONObject();

                try {
                    new_comment.put("body", add_comment.getText());
                    new_comment.put("question_id", goku2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final Toast tst_checkOut = Toast.makeText(QuestionActivity.this, "Adding the comment", Toast.LENGTH_SHORT);
                tst_checkOut.show();
                JSONArray jsonArray = new JSONArray();
                jsonArray.put(new_comment);

                String com = "questions/" + goku2 + "/comments";
                Log.d("bibo", com);
                String baseUrl = getString(R.string.backend_base_url) + com;
                String url = String.format("%s?token=%s", baseUrl, AccessToken.getCurrentAccessToken().getToken());
                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, jsonArray,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                tst_checkOut.cancel();

                                if (response.length() > 0) {
                                    //db.clearRecords();
                                    //finish();
                                    //Log.d("bibo", response.toString());
                                    Toast.makeText(QuestionActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                    finish();
                                    startActivity(getIntent());
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                tst_checkOut.cancel();
                                Toast.makeText(QuestionActivity.this, "Error checking out cart", Toast.LENGTH_SHORT).show();
                            }
                        });
                queue2.add(jsonArrayRequest);
            }
        });


        //Delete the question and Edit buttons
        ImageButton delete_the_button = findViewById(R.id.delete_the_question);
        final ImageButton edit_the_question_button = findViewById(R.id.edit_the_question);

        String question_cs_name = intent.getStringExtra("question_customer_name");
        if (!question_cs_name.equals(Profile.getCurrentProfile().getName())) {
            delete_the_button.setVisibility(View.GONE);
            edit_the_question_button.setVisibility(View.GONE);
        }


        delete_the_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /////////////////
                new AlertDialog.Builder(QuestionActivity.this)
                        .setTitle("Delete the question")
                        .setMessage("Are you sure you want to delete this question?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                final RequestQueue queue = Volley.newRequestQueue(QuestionActivity.this);
                                JSONArray jsonArray = new JSONArray();

                                //String goku has the question id
                                String baseUrl = "https://mobilehassaanfinal.herokuapp.com/" + "questions/" + goku;
                                String url = String.format("%s?token=%s", baseUrl, AccessToken.getCurrentAccessToken().getToken());
                                StringRequest dr = new StringRequest(Request.Method.DELETE, url,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                // response
                                                Log.d("bibo", "Deleting the question......");
                                                startActivity(new Intent(QuestionActivity.this, QuestionsActivity.class));
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                // error.

                                            }
                                        }
                                );
                                queue.add(dr);

                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_delete)
                        .show();


                ////////////

            }
        });


        edit_the_question_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("bibo", "Edit button is clicked");
                LayoutInflater mInflater;
                mInflater = LayoutInflater.from(QuestionActivity.this);
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(QuestionActivity.this);

                View view = mInflater.inflate(R.layout.layout_dialog_2, null);
                builder.setView(view).setTitle("Edit your question!")
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String new_question_title = edit_question_title.getText().toString();
                                Log.d("bibo", String.valueOf(edit_question_title.getText()));
                                String new_question_content = edit_question_content.getText().toString();
                                Log.d("bibo", String.valueOf(edit_question_content.getText()));

                                String category = spinner.getSelectedItem().toString();
                                //spinner.setOnItemSelectedListener(this);
                                edit_the_question(new_question_title, new_question_content, category, goku2);
//                                Log.d("bibo", category);
//                                Log.d("bibo", "Doooooooooooooooooooooooone");
                                //Intent intent = getIntent();
                                //finish();
                                //startActivity(intent);
                                tv_productName.setText(new_question_title);
                                tv_productDescription.setText(new_question_content);
                                tv_productCategory.setText(category);

                            }
                        }).show();
                edit_question_title = view.findViewById(R.id.edit_text_comment_1);
                edit_question_content = view.findViewById(R.id.edit_text_comment_2);
                spinner = view.findViewById(R.id.spinner2);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(QuestionActivity.this, R.array.categories, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);

                edit_question_title.setText(intent.getStringExtra("question_title"));
                edit_question_content.setText(intent.getStringExtra("question_content"));
                int spinnerPosition = adapter.getPosition(intent.getStringExtra("question_category"));
                spinner.setSelection(spinnerPosition);
                builder.create();
            }
        });


    }

    public void edit_the_question(final String new_question_title, final String new_question_content, final String category, String goku2) {
        final RequestQueue queue = Volley.newRequestQueue(QuestionActivity.this);
        JSONArray jsonArray = new JSONArray();

        String baseUrl = "https://mobilehassaanfinal.herokuapp.com/" + "questions/" + goku2;
        String url = String.format("%s?token=%s", baseUrl, AccessToken.getCurrentAccessToken().getToken());
        StringRequest putRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error

                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("title", new_question_title);
                params.put("content", new_question_content);
                params.put("category", category);
                return params;
            }

        };

        queue.add(putRequest);
    }


    @Override
    protected void onDestroy() {
        //db.close();
        super.onDestroy();
    }
}

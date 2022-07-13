package com.example.store.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.store.R;
import com.example.store.activities.PostActivity;
import com.example.store.activities.QuestionActivity;
import com.example.store.activities.QuestionsActivity;
import com.example.store.models.Question;
import com.example.store.models.delete_Dialog;
import com.facebook.AccessToken;
import com.facebook.Profile;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ItemAdapter extends BaseAdapter {
    //
    String question_title;
    String question_content;
    String question_customer_name;
    String question_id;
    String category;
    Context mContext;
    LayoutInflater mInflater;
    String[] items;
    String[] bodies;
    String[] comments_ids;
    String[] like_count;
    Boolean[] like_flags;
    Boolean[] dislike_flags;
    int[] like_ids;

    private ImageButton delete_button;
    private ImageButton edit_button;
    private ImageButton like_button;
    private ImageButton dislike_button;
    private EditText edit_text_question;



    public ItemAdapter(Context c, String[] i, String[] j, String[] comments_ids,
                       String[] like_count, Boolean[] like_flags, Boolean[] dislike_flags, int[] like_ids,
                       String question_title, String question_content, String question_customer_name,
                       String question_id, String category) {
        this.question_title = question_title;
        this.question_content = question_content;
        this.question_customer_name = question_customer_name;
        this.question_id = question_id;
        this.category = category;

        this.mContext = c;
        items = i;
        bodies = j;
        this.comments_ids = comments_ids;
        this.like_count = like_count;
        this.like_flags = like_flags;
        this.dislike_flags = dislike_flags;
        Log.d("bibo", "PECO");
        Log.d("bibo", String.valueOf(like_flags.length));
        Log.d("bibo", String.valueOf(dislike_flags.length));


        this.like_ids = like_ids;

        mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int position) {
        return items[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
//        Log.d("bibo", "DISASTER");

        View v = mInflater.inflate(R.layout.my_listview_details, null);
        final View vi = mInflater.inflate(R.layout.my_listview_details, null);
        TextView customerTextView = (TextView) v.findViewById(R.id.customerTextView);
        TextView bodyTextView = (TextView) v.findViewById(R.id.bodyTextView);
        final TextView counterTextView = v.findViewById(R.id.counter);
        final boolean[] liked_flag = {false};
        final boolean[] disliked_flag = {false};
        final int[] like_id_of_user = {like_ids[position]};
        final String customer = items[position];
        final String description = bodies[position];
        final String comments_id = comments_ids[position];
        final String[] counter = {like_count[position]};



        ImageView imageView = v.findViewById(R.id.iv_item_product_image);


//        anime_like_button = v.findViewById(R.id.anime_button_like);
//        anime_unlike_button = v.findViewById(R.id.anime_button_unlike);

        like_button = (ImageButton) v.findViewById(R.id.like_button);
        dislike_button = (ImageButton) v.findViewById(R.id.dislike_button);

        if (like_flags[position] == true) {
            liked_flag[0] = true;
            //like_button.setBackgroundResource(R.drawable.ic_baseline_thumb_up_24_liked);
            like_button.setAlpha((float) 1);
//            anime_like_button.setLiked(true);
            Log.d("bibo", "Proco");
        }
        if (dislike_flags[position] == true) {
            disliked_flag[0] = true;
            //dislike_button.setBackgroundResource(R.drawable.ic_baseline_thumb_up_24_liked);
            dislike_button.setAlpha((float) 1);
//            anime_unlike_button.setLiked(true);
        }


        customerTextView.setText(customer);
        if (customer.equals(Profile.getCurrentProfile().getName())){
            Picasso.get().load(Profile.getCurrentProfile().getProfilePictureUri(64,64)).into(imageView);
        }
        bodyTextView.setText(description);
        counterTextView.setText(counter[0]);
        if (Integer.parseInt(counter[0]) >0){
            //counterTextView.setTextColor(Color.parseColor(String.valueOf(R.color.counter_positive)));
            counterTextView.setTextColor(mContext.getResources().getColor(R.color.counter_positive));
        }
        else if (Integer.parseInt(counter[0]) ==0){
            counterTextView.setTextColor(mContext.getResources().getColor(R.color.counter_neutral));
        }
        else if(Integer.parseInt(counter[0])<0){
            counterTextView.setTextColor(mContext.getResources().getColor(R.color.counter_negative));
        }


        delete_button = (ImageButton) v.findViewById(R.id.delete_button);
        edit_button = (ImageButton) v.findViewById(R.id.edit_button);



        if (!customer.equals(Profile.getCurrentProfile().getName())) {

            delete_button.setVisibility(View.GONE);
            edit_button.setVisibility(View.GONE);
        }
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(comments_id);
                Log.d("bibo", String.valueOf(position));
                Log.d("bibo", description);


            }
        });
        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog2(comments_id, description);

            }
        });

        //final ImageButton btn =  v.findViewById(R.id.dislike_button);
        //Log.d("bibo", "BBBBBBB");
        //Log.d("bibo", String.valueOf(btn.getId()));
        final ImageButton[] btn = new ImageButton[1];
        btn[0]=v.findViewById(R.id.dislike_button);
        //ImageButton[] btn_1 = new ImageButton{btn};

        like_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Log.d("bibo", String.valueOf(position));
                //Log.d("bibo", String.valueOf(liked_flag[0]));
                ////////////////////////////////////////////////////////////////////////////////////POST///////////////////////////////////////////////////////////////////////////

                if (liked_flag[0] == false && disliked_flag[0] == false && like_id_of_user[0] == 0) {
                    String c = counterTextView.getText().toString();
                    int c1 = Integer.parseInt(c) + 1;
                    like_count[position] = String.valueOf(c1);
                    counterTextView.setText(String.valueOf(c1));
                    liked_flag[0] = true;
                    like_flags[position] = true;
                    Log.d("bibo", "MMK");
                    if (c1 >0){
                        //counterTextView.setTextColor(Color.parseColor(String.valueOf(R.color.counter_positive)));
                        counterTextView.setTextColor(mContext.getResources().getColor(R.color.counter_positive));
                    }
                    else if (c1 ==0){
                        counterTextView.setTextColor(mContext.getResources().getColor(R.color.counter_neutral));
                    }
                    else if(c1<0){
                        counterTextView.setTextColor(mContext.getResources().getColor(R.color.counter_negative));
                    }
                    //v.setBackgroundResource(R.drawable.ic_baseline_thumb_up_24_liked);
                    v.setAlpha((float) 1);

                    //////////////////////////////////////////////////////////////////////v.setBackgroundResource(R.drawable.cart);

                    RequestQueue queue = Volley.newRequestQueue(mContext);
                    JSONObject new_like = new JSONObject();
                    try {
                        new_like.put("comment_id", comments_id);
                        new_like.put("counter", 1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JSONArray jsonArray = new JSONArray();
                    jsonArray.put(new_like);
                    String baseUrl = "https://mobilehassaanfinal.herokuapp.com/" + "/likes";
                    String url = String.format("%s?token=%s", baseUrl, AccessToken.getCurrentAccessToken().getToken());
                    Log.d("bibo", "MMKK");
                    JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, jsonArray,
                            new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {

                                    if (response.length() > 0) {
                                        try {
                                            int x = response.getJSONObject(0).getInt("id");
                                            Log.d("bibo", String.valueOf(x));
                                            like_id_of_user[0] = x;
                                            Log.d("bibo", String.valueOf(like_id_of_user[0]));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Log.d("bibo", "error");
                                        }

                                        Log.d("bibo", "MMKKK");

                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("bibo", error.toString());
                                }
                            });

                    queue.add(jsonArrayRequest);


                } else if (liked_flag[0] == false && disliked_flag[0] == false && like_id_of_user[0] != 0) {
                    String c = counterTextView.getText().toString();
                    int c1 = Integer.parseInt(c) + 1;
                    like_count[position] = String.valueOf(c1);
                    counterTextView.setText(String.valueOf(c1));
                    liked_flag[0] = true;
                    like_flags[position] = true;
                    Log.d("bibo", "MMK");
                    if (c1 >0){
                        //counterTextView.setTextColor(Color.parseColor(String.valueOf(R.color.counter_positive)));
                        counterTextView.setTextColor(mContext.getResources().getColor(R.color.counter_positive));
                    }
                    else if (c1 ==0){
                        counterTextView.setTextColor(mContext.getResources().getColor(R.color.counter_neutral));
                    }
                    else if(c1<0){
                        counterTextView.setTextColor(mContext.getResources().getColor(R.color.counter_negative));
                    }
                    //v.setBackgroundResource(R.drawable.ic_baseline_thumb_up_24_liked);
                    v.setAlpha((float) 1);
                    final RequestQueue queue = Volley.newRequestQueue(mContext);
                    JSONArray jsonArray = new JSONArray();

                    String baseUrl = "https://mobilehassaanfinal.herokuapp.com/" + "likes/" + like_id_of_user[0];
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
                            params.put("counter", String.valueOf(1));

                            return params;
                        }

                    };

                    queue.add(putRequest);


                } else if (liked_flag[0] == false && disliked_flag[0] == true) {
                    String c = counterTextView.getText().toString();
                    int c1 = Integer.parseInt(c) + 2;
                    like_count[position] = String.valueOf(c1);
                    counterTextView.setText(String.valueOf(c1));
                    liked_flag[0] = true;
                    disliked_flag[0] = false;
                    like_flags[position] = true;
                    dislike_flags[position] = false;
                    SystemClock.sleep(100);
                    //v.setBackgroundResource(R.drawable.ic_baseline_thumb_up_24_liked);
                    v.setAlpha((float) 1);
                    //ImageButton btn =  vi.findViewById(R.id.dislike_button);
                    //btn.setImageResource(R.drawable.ic_baseline_thumb_up_24);
                    Log.d("bibo","BBB");
                    //Log.d("bibo", String.valueOf(position));
                    if (c1 >0){
                        //counterTextView.setTextColor(Color.parseColor(String.valueOf(R.color.counter_positive)));
                        counterTextView.setTextColor(mContext.getResources().getColor(R.color.counter_positive));
                    }
                    else if (c1 ==0){
                        counterTextView.setTextColor(mContext.getResources().getColor(R.color.counter_neutral));
                    }
                    else if(c1<0){
                        counterTextView.setTextColor(mContext.getResources().getColor(R.color.counter_negative));
                    }


                    Log.d("bibo", String.valueOf(btn[0].getId()));
                    //Log.d("bibo", String.valueOf(btn.getId()));
                    //btn[0].setImageResource(R.drawable.ic_baseline_thumb_up_24);
                    btn[0].setAlpha((float) 0.3);
//
//                    Log.d("bibo", String.valueOf(btn.getId()));

                    final RequestQueue queue = Volley.newRequestQueue(mContext);
                    JSONArray jsonArray = new JSONArray();

                    String baseUrl = "https://mobilehassaanfinal.herokuapp.com/" + "likes/" + like_id_of_user[0];
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
                            params.put("counter", String.valueOf(1));

                            return params;
                        }

                    };

                    queue.add(putRequest);


                } else if (liked_flag[0] == true && disliked_flag[0] == false) {
                    //v.setBackgroundResource(R.drawable.ic_baseline_thumb_up_24);
                    v.setAlpha((float) 1);
                    String c = counterTextView.getText().toString();
                    int c1 = Integer.parseInt(c) - 1;
                    like_count[position] = String.valueOf(c1);
                    counterTextView.setText(String.valueOf(c1));
                    liked_flag[0] = false;
                    like_flags[position] = false;
                    final RequestQueue queue = Volley.newRequestQueue(mContext);
                    JSONArray jsonArray = new JSONArray();
                    v.setAlpha((float) 0.3);

                    if (c1 >0){
                        //counterTextView.setTextColor(Color.parseColor(String.valueOf(R.color.counter_positive)));
                        counterTextView.setTextColor(mContext.getResources().getColor(R.color.counter_positive));
                    }
                    else if (c1 ==0){
                        counterTextView.setTextColor(mContext.getResources().getColor(R.color.counter_neutral));
                    }
                    else if(c1<0){
                        counterTextView.setTextColor(mContext.getResources().getColor(R.color.counter_negative));
                    }

                    String baseUrl = "https://mobilehassaanfinal.herokuapp.com/" + "likes/" + like_id_of_user[0];
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
                            params.put("counter", String.valueOf(0));

                            return params;
                        }

                    };

                    queue.add(putRequest);


                }


                //like_the_button(comments_id);
            }
        });


        final ImageButton[] btn_d = new ImageButton[1];
        btn_d[0]=v.findViewById(R.id.like_button);

        dislike_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("bibo", "Eloooon");
                if (liked_flag[0] == false && disliked_flag[0] == false && like_id_of_user[0] == 0) {
                    String c = counterTextView.getText().toString();
                    int c1 = Integer.parseInt(c) - 1;
                    like_count[position] = String.valueOf(c1);
                    counterTextView.setText(String.valueOf(c1));
                    disliked_flag[0] = true;
                    dislike_flags[position] = true;

                    if (c1 >0){
                        //counterTextView.setTextColor(Color.parseColor(String.valueOf(R.color.counter_positive)));
                        counterTextView.setTextColor(mContext.getResources().getColor(R.color.counter_positive));
                    }
                    else if (c1 ==0){
                        counterTextView.setTextColor(mContext.getResources().getColor(R.color.counter_neutral));
                    }
                    else if(c1<0){
                        counterTextView.setTextColor(mContext.getResources().getColor(R.color.counter_negative));
                    }


                    //v.setBackgroundResource(R.drawable.ic_baseline_thumb_up_24_liked);
                    v.setAlpha((float) 1);
                    RequestQueue queue = Volley.newRequestQueue(mContext);
                    JSONObject new_like = new JSONObject();
                    try {
                        new_like.put("comment_id", comments_id);
                        new_like.put("counter", -1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JSONArray jsonArray = new JSONArray();
                    jsonArray.put(new_like);
                    String baseUrl = "https://mobilehassaanfinal.herokuapp.com/" + "/likes";
                    String url = String.format("%s?token=%s", baseUrl, AccessToken.getCurrentAccessToken().getToken());
                    Log.d("bibo", "MMKK");
                    JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, jsonArray,
                            new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {

                                    if (response.length() > 0) {
                                        try {
                                            int x = response.getJSONObject(0).getInt("id");
                                            Log.d("bibo", String.valueOf(x));
                                            like_id_of_user[0] = x;
                                            Log.d("bibo", String.valueOf(like_id_of_user[0]));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Log.d("bibo", "error");
                                        }

                                        Log.d("bibo", "MMKKK");

                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("bibo", error.toString());
                                }
                            });

                    queue.add(jsonArrayRequest);


                } else if (liked_flag[0] == false && disliked_flag[0] == false && like_id_of_user[0] != 0) {
                    String c = counterTextView.getText().toString();
                    int c1 = Integer.parseInt(c) - 1;
                    like_count[position] = String.valueOf(c1);
                    counterTextView.setText(String.valueOf(c1));
                    disliked_flag[0] = true;
                    dislike_flags[position] = true;
                    //v.setBackgroundResource(R.drawable.ic_baseline_thumb_up_24_liked);
                    v.setAlpha((float) 1);
                    final RequestQueue queue = Volley.newRequestQueue(mContext);
                    JSONArray jsonArray = new JSONArray();

                    if (c1 >0){
                        //counterTextView.setTextColor(Color.parseColor(String.valueOf(R.color.counter_positive)));
                        counterTextView.setTextColor(mContext.getResources().getColor(R.color.counter_positive));
                    }
                    else if (c1 ==0){
                        counterTextView.setTextColor(mContext.getResources().getColor(R.color.counter_neutral));
                    }
                    else if(c1<0){
                        counterTextView.setTextColor(mContext.getResources().getColor(R.color.counter_negative));
                    }

                    String baseUrl = "https://mobilehassaanfinal.herokuapp.com/" + "likes/" + like_id_of_user[0];
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
                            params.put("counter", String.valueOf(-1));

                            return params;
                        }

                    };

                    queue.add(putRequest);


                } else if (liked_flag[0] == false && disliked_flag[0] == true) {
                    String c = counterTextView.getText().toString();
                    int c1 = Integer.parseInt(c) + 1;
                    like_count[position] = String.valueOf(c1);
                    counterTextView.setText(String.valueOf(c1));
                    disliked_flag[0] = false;
                    dislike_flags[position] = false;

                    if (c1 >0){
                        //counterTextView.setTextColor(Color.parseColor(String.valueOf(R.color.counter_positive)));
                        counterTextView.setTextColor(mContext.getResources().getColor(R.color.counter_positive));
                    }
                    else if (c1 ==0){
                        counterTextView.setTextColor(mContext.getResources().getColor(R.color.counter_neutral));
                    }
                    else if(c1<0){
                        counterTextView.setTextColor(mContext.getResources().getColor(R.color.counter_negative));
                    }

                    //v.setBackgroundResource(R.drawable.ic_baseline_thumb_up_24);
                    v.setAlpha((float) 0.3);
                    final RequestQueue queue = Volley.newRequestQueue(mContext);
                    JSONArray jsonArray = new JSONArray();

                    String baseUrl = "https://mobilehassaanfinal.herokuapp.com/" + "likes/" + like_id_of_user[0];
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
                            params.put("counter", String.valueOf(0));

                            return params;
                        }

                    };

                    queue.add(putRequest);


                } else if (liked_flag[0] == true && disliked_flag[0] == false) {
                    String c = counterTextView.getText().toString();
                    int c1 = Integer.parseInt(c) - 2;
                    like_count[position] = String.valueOf(c1);
                    counterTextView.setText(String.valueOf(c1));
                    disliked_flag[0] = true;
                    liked_flag[0] = false;
                    dislike_flags[position] = true;
                    like_flags[position] = false;


                    SystemClock.sleep(100);
                    //v.setBackgroundResource(R.drawable.ic_baseline_thumb_up_24_liked);
                    v.setAlpha((float) 1);
                    btn_d[0].setAlpha((float) 0.3);

                    if (c1 >0){
                        //counterTextView.setTextColor(Color.parseColor(String.valueOf(R.color.counter_positive)));
                        counterTextView.setTextColor(mContext.getResources().getColor(R.color.counter_positive));
                    }
                    else if (c1 ==0){
                        counterTextView.setTextColor(mContext.getResources().getColor(R.color.counter_neutral));
                    }
                    else if(c1<0){
                        counterTextView.setTextColor(mContext.getResources().getColor(R.color.counter_negative));
                    }
                    final RequestQueue queue = Volley.newRequestQueue(mContext);
                    JSONArray jsonArray = new JSONArray();

                    String baseUrl = "https://mobilehassaanfinal.herokuapp.com/" + "likes/" + like_id_of_user[0];
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
                            params.put("counter", String.valueOf(-1));

                            return params;
                        }

                    };

                    queue.add(putRequest);


                }

            }
        });

        return v;
    }


    public void like_the_button(String comments_id) {

        Log.d("bibo", "I pressed the like button" + comments_id);
        //Log.d("bibo", String.valueOf(liked_flag));
        //liked_flag = !liked_flag;
    }


    public void openDialog2(final String comments_id, String description) {
        LayoutInflater mInflater;
        Context context = mContext.getApplicationContext();
        mInflater = LayoutInflater.from(context);
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContext);

        View view = mInflater.inflate(R.layout.layout_dialog, null);
        builder.setView(view).setTitle("Edit your comment!")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String new_edited_comment = edit_text_question.getText().toString();
                        Log.d("bibo", new_edited_comment);
                        edit_the_comment(new_edited_comment, comments_id);
                        Intent intent = new Intent(mContext, QuestionActivity.class);
                        intent.putExtra("question_id", question_id);
                        intent.putExtra("question_title", question_title);
                        intent.putExtra("question_content", question_content);
                        intent.putExtra("question_customer_name", question_customer_name);
                        intent.putExtra("question_category", category);
                        mContext.startActivity(intent);
                    }
                }).show();
        edit_text_question = view.findViewById(R.id.edit_text_comment);
        edit_text_question.setText(description);
        builder.create();
    }

    public void edit_the_comment(final String new_edited_comment, String comments_id) {
        final RequestQueue queue = Volley.newRequestQueue(mContext);
        JSONArray jsonArray = new JSONArray();

        String baseUrl = "https://mobilehassaanfinal.herokuapp.com/" + "questions/" + question_id + "/comments/" + comments_id;
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
                params.put("body", new_edited_comment);

                return params;
            }

        };

        queue.add(putRequest);

    }


    public void openDialog(final String comments_id) {

        new AlertDialog.Builder(mContext)
                .setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this comment?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        delete_the_comment(comments_id);
                        Intent intent = new Intent(mContext, QuestionActivity.class);
                        Log.d("bibo", "Please go to mars");
                        intent.putExtra("question_id", question_id);
                        intent.putExtra("question_title", question_title);
                        intent.putExtra("question_content", question_content);
                        intent.putExtra("question_customer_name", question_customer_name);
                        intent.putExtra("question_category", category);
                        mContext.startActivity(intent);
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_delete)
                .show();

    }

    public void delete_the_comment(String comments_id) {

        final RequestQueue queue = Volley.newRequestQueue(mContext);
        JSONArray jsonArray = new JSONArray();

        String baseUrl = "https://mobilehassaanfinal.herokuapp.com/" + "questions/" + question_id + "/comments/" + comments_id;
        String url = String.format("%s?token=%s", baseUrl, AccessToken.getCurrentAccessToken().getToken());
        StringRequest dr = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("bibo", "okay");
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

}

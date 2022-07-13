package com.example.store.activities;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.store.R;

public class TriviaActivity extends HomeActivity {
    RadioGroup radioGroup;
    RadioButton radioButton;
    TextView textView;
    String correct_boolean_answer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_post);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_trivia, null, false);
        mDrawer.addView(contentView, 0);
        getSupportActionBar().setTitle("Trivia");


        String url = "https://opentdb.com/api.php?amount=1&type=boolean";

        final RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.d("bibo", response.toString());
                        Log.d("bibo", "Boo");
                        int question_index = response.indexOf("question");
                        Log.d("bibo", Integer.toString(question_index));
                        int start_of_question_index = question_index+11;
                        Log.d("bibo", Integer.toString(start_of_question_index));
                        Log.d("bibo", String.valueOf(response.charAt(start_of_question_index)));

                        //To get the position of "correct_answer"
                        int correct_answer = response.indexOf("correct_answer");
                        Log.d("bibo", Integer.toString(correct_answer));

                        //To get the position of the last character of the question
                        int end_of_question_index = correct_answer-4;
                        Log.d("bibo", String.valueOf(response.charAt(end_of_question_index)));


                        //To get the question itself
                        Log.d("bibo",response.substring(start_of_question_index,end_of_question_index+1));
                        TextView question_text = findViewById(R.id.textView_question_text);
                        question_text.setText(response.substring(start_of_question_index,end_of_question_index+1)
                                .replace("&quot;","\"").replace("&#039;","'")
                                .replace("\\/"," /").replace("&rsquo;","'"));
                        //ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                        //params.setMargins(10, 0, 10, 100);
                        //question_text.setLayoutParams(params);
                        //To get the TRUE or FAlSE
                        char boolean_answer = response.charAt(correct_answer+17);
                        Log.d("bibo","The correct answer would be");
                        Log.d("bibo",String.valueOf(boolean_answer));
                        //Log.d("bibo","The correct answer in char would be");
                        //Log.d("bibo",String.valueOf(boolean_answer));


                        if (boolean_answer=='F'){
                            Log.d("bibo","Goku");
                            correct_boolean_answer = "False";
                        }
                        else{
                            correct_boolean_answer = "True";
                            Log.d("bibo","Vegeta");

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("bibo", "Coo");
                Log.d("bibo", error.toString());
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);








        fb_cartCheckout.setVisibility(View.INVISIBLE);

        radioGroup = findViewById(R.id.radioGroup);
        textView = findViewById(R.id.text_view_selected);

        Button buttonApply = findViewById(R.id.button_apply);
        buttonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(radioId);
                Log.d("bibo", (String) radioButton.getText());
                String dump = (String) radioButton.getText();

                Log.d("bibo", correct_boolean_answer);
                if ( dump.equals(correct_boolean_answer)){
                    textView.setText("You are correct!");
                    textView.setTextColor(Color.GREEN);
                }
                else{
                    textView.setText("You are incorrect!");
                    textView.setTextColor(Color.RED);

                }


            }
        });

        Button buttonNewQuestion = findViewById(R.id.new_question);
        buttonNewQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(getIntent());
            }
        });





    }
    public void checkButton(View v){
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);

        Toast.makeText(this,"Selected Radio Button: " +radioButton.getText(), Toast.LENGTH_SHORT).show();
    }
}

package com.example.store.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.store.R;
import com.example.store.activities.QuestionActivity;
import com.example.store.models.Question;
import com.example.store.view_holders.QuestionViewHolder;

import java.util.ArrayList;
import java.util.List;


public class questionAdapter extends RecyclerView.Adapter<QuestionViewHolder> {

    private List<Question> questionList;

    public questionAdapter(ArrayList<Question> questions) {
        questionList = questions;
    }


    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question, parent, false);
        return new QuestionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        final Question question = questionList.get(position);

        holder.sethtitle(question.getTitle());
        holder.sethcontent(question.getContent());
        holder.setHcustomerid(question.getCustomer_name());
        holder.setHcategory(question.getCategory().toString());
        holder.setHcard_view(question.getCategory());
        holder.setHimageView(question.getCustomer_name());
        holder.setIsRecyclable(false);

        holder.getLinearLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(@NonNull View v) {
                Intent intent = new Intent(v.getContext(), QuestionActivity.class);
                intent.putExtra("question_id", question.getQuestion_id().toString());
                intent.putExtra("question_title", question.getTitle());
                intent.putExtra("question_content", question.getContent());
                intent.putExtra("question_customer_name", question.getCustomer_name());
                intent.putExtra("question_category", question.getCategory().toString());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }
}


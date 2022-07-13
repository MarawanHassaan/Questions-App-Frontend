package com.example.store.view_holders;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.store.R;
import com.facebook.Profile;
import com.squareup.picasso.Picasso;

import java.util.Random;

public class QuestionViewHolder extends RecyclerView.ViewHolder {

    private TextView htitle, hcontent, hcustomerid, hcategory;
    private CardView hcard_view;
    private ImageView himageView;
    private LinearLayout hLinearLayout;
    private Random r;

    public QuestionViewHolder(@NonNull View itemView) {
        super(itemView);

        r = new Random();

        htitle = itemView.findViewById(R.id.tv_item_product_name);
        hcontent = itemView.findViewById(R.id.tv_item_product_price);
        hcustomerid = itemView.findViewById(R.id.tv_item_product_description);
        hcategory= itemView.findViewById(R.id.tv_item_product_category);
        hLinearLayout = itemView.findViewById(R.id.item_product_card_layout);
        hcard_view = itemView.findViewById(R.id.cardView_question);
        himageView = itemView.findViewById(R.id.iv_item_product_image);
    }

    public void sethtitle(String name) {
        htitle.setText(name);
    }
    public void sethcontent(String name) {
        hcontent.setText(name);
    }
    public void setHcustomerid(String name) {
        hcustomerid.setText(name);
    }

    public void setHimageView (String name){
        if (name.equals(Profile.getCurrentProfile().getName())){
//            Log.d("bibo","We are in");
//            Log.d("bibo",name);
            Picasso.get().load(Profile.getCurrentProfile().getProfilePictureUri(50,50)).into(himageView);
        }
    }
    public void setHcard_view(String name){
        if (name.equals("Sports")){
            hcard_view.setCardBackgroundColor(Color.parseColor("#1A89cc02"));
        }
        else if (name.equals("Technology")){
            hcard_view.setCardBackgroundColor(Color.parseColor("#1A8f8109"));
        }
        else if (name.equals("Entertainment")){
            hcard_view.setCardBackgroundColor(Color.parseColor("#1Aeb0081"));
        }
        else if (name.equals("Language")){
            hcard_view.setCardBackgroundColor(Color.parseColor("#1A076782"));
        }
        else if (name.equals("Politics")){
            hcard_view.setCardBackgroundColor(Color.parseColor("#1Ae8150c"));
        }

    }

    public void setHcategory(String name) {
        hcategory.setText('#'+name.toLowerCase());
            }



    public LinearLayout getLinearLayout() {
        return hLinearLayout;
    }
}

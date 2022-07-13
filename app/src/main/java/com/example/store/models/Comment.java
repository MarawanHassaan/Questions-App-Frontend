package com.example.store.models;

import java.util.ArrayList;

public class Comment {
    private String body;
    private Integer question_id;
    private Integer customer_id;
    private String customer_name;
    private Integer comment_id;
    private ArrayList<Like> likes;

    public int getLike_id() {
        return like_id;
    }

    public void setLike_id(int like_id) {
        this.like_id = like_id;
    }

    private boolean like_flag = false;
    private boolean dislike_flag = false;
    private int like_id=0;

    public boolean isLike_flag() {
        return like_flag;
    }

    public void setLike_flag(boolean like_flag) {
        this.like_flag = like_flag;
    }

    public boolean isDislike_flag() {
        return dislike_flag;
    }

    public void setDislike_flag(boolean dislike_flag) {
        this.dislike_flag = dislike_flag;
    }

    public int getCounter_of_likes() {
        return counter_of_likes;
    }

    public void setCounter_of_likes(int counter_of_likes) {
        this.counter_of_likes = counter_of_likes;
    }

    private int counter_of_likes;

    public Integer getComment_id() {
        return comment_id;
    }

    public ArrayList<Like> getLikes() {
        return likes;
    }

    public void setLikes(ArrayList<Like> likes) {
        this.likes = likes;
    }

    public void setComment_id(Integer comment_id) {
        this.comment_id = comment_id;
    }

    public String getBody() {
        return body;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public Integer getQuestion_id() {
        return question_id;
    }

    public Integer getCustomer_id() {
        return customer_id;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setQuestion_id(Integer question_id) {
        this.question_id = question_id;
    }

    public void setCustomer_id(Integer customer_id) {
        this.customer_id = customer_id;
    }
}

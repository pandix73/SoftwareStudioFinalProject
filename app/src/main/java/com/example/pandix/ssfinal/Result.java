package com.example.pandix.ssfinal;

/**
 * Created by shawn on 2016/6/22.
 */
public class Result {

    private String book_name;
    private String index;
    private String site;
    private String author;
    private String catorgary;

    public Result(String s1, String s2, String s3, String s4, String s5){
        book_name = s1;
        author = s2;
        catorgary = s3;
        site = s4;
        index = s5;
    }

    public String getBook_name(){
        return book_name;
    }

    public void setBook_name(String s){
        book_name = s;
    }

    public String getAuthor(){
        return author;
    }

    public void setAuthor(String s){
        author = s;
    }

    public String getCatorgary(){
        return catorgary;
    }

    public void setCatorgary(String s){
        catorgary = s;
    }

    public String getSite(){
        return site;
    }

    public void setSite(String s){
        site = s;
    }

    public String getIndex(){
        return index;
    }

    public void setIndex(String s){
        index = s;
    }

}

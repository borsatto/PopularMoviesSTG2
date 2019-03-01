package com.example.android.popularmoviesstg2.models;

public class Review {
    private String filmAuthor;
    private String filmContent;

    public Review(){
    }

    public String getFilmAuthor() {
        return filmAuthor;
    }

    public String getFilmContent() {
        return filmContent;
    }

    public void setFilmAuthor(String filmAuthor) {
        this.filmAuthor = filmAuthor;
    }

    public void setFilmContent(String filmContent) {
        this.filmContent = filmContent;
    }
}

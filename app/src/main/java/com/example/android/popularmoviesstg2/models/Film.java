package com.example.android.popularmoviesstg2.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Film implements Parcelable {
    private String filmId;
    private String filmTitle;
    private String filmPoster;
    private String filmSynopsis;
    private String filmVoting;
    private String filmReleaseDate;
    //private boolean favorite = false;

    public Film(){
    }

    private Film (Parcel parcel){
        filmId = parcel.readString();
        filmTitle = parcel.readString();
        filmPoster = parcel.readString();
        filmSynopsis = parcel.readString();
        filmVoting = parcel.readString();
        filmReleaseDate = parcel.readString();
    }

    public static final Creator<Film> CREATOR = new Creator<Film>() {
        @Override
        public Film createFromParcel(Parcel source) {
            return new Film(source);
        }

        @Override
        public Film[] newArray(int size) {
            return new Film[size];
        }
    };

    public String getFilmId() {
        return filmId;
    }

    public String getFilmTitle() {
        return filmTitle;
    }

    public String getFilmPoster() {
        return filmPoster;
    }

    public String getFilmSynopsis() {
        return filmSynopsis;
    }

    public String getFilmVoting() {
        return filmVoting;
    }

    public String getFilmReleaseDate() {
        return filmReleaseDate;
    }

    public void setFilmId(String filmId) {
        this.filmId = filmId;
    }

    public void setFilmTitle(String filmTitle) {
        this.filmTitle = filmTitle;
    }

    public void setFilmPoster(String filmPoster) {
        this.filmPoster = filmPoster;
    }

    public void setFilmSynopsis(String filmSynopsis) {
        this.filmSynopsis = filmSynopsis;
    }

    public void setFilmVoting(String filmVoting) {
        this.filmVoting = filmVoting;
    }

    public void setFilmReleaseDate(String filmReleaseDate) {
        this.filmReleaseDate = filmReleaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(filmId);
        dest.writeString(filmTitle);
        dest.writeString(filmPoster);
        dest.writeString(filmSynopsis);
        dest.writeString(filmVoting);
        dest.writeString(filmReleaseDate);
    }
}

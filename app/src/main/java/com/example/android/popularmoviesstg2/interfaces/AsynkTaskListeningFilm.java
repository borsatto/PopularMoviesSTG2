package com.example.android.popularmoviesstg2.interfaces;

import com.example.android.popularmoviesstg2.models.Film;

public interface AsynkTaskListeningFilm {

    void onTaskComplete(Film[] result);

}

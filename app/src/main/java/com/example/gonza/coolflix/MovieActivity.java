package com.example.gonza.coolflix;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Adapter;

import com.example.gonza.coolflix.adapters.MoviesAdapter;
import com.example.gonza.coolflix.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MovieActivity extends AppCompatActivity {

    List<Movie> movies;
    private static final String MOVIE_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

    /*Add RecyclerView support library to the gradle build file - DONE
    Define a model class to use as the data source - DONE
    Add a RecyclerView to your activity to display the items - DONE
    Create a custom row layout XML file to visualize the item - DONE
    Create a RecyclerView.Adapter and ViewHolder to render the item - DONE
    Bind the adapter to the data source to populate the RecyclerView - DONE
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        RecyclerView recViewMovies = findViewById(R.id.recview_movies);
        movies = new ArrayList<>();
        final MoviesAdapter adapter = new MoviesAdapter(this, movies);
        recViewMovies.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recViewMovies.setAdapter(adapter);

        AsyncHttpClient client = new AsyncHttpClient();
        //accesses the movie api that contains all the movies in the form of JSON objects
        client.get(MOVIE_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    //Each JSONArray is a movie + its info
                    JSONArray movieJsonArray = response.getJSONArray("results");
                    //List of all the movies
                    movies.addAll(Movie.fromJsonArray(movieJsonArray));
                    adapter.notifyDataSetChanged();
                    Log.d("log-tag", movieJsonArray.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
}

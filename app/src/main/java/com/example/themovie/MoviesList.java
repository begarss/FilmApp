package com.example.themovie;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.themovie.adapter.MovieAdapter;
import com.example.themovie.api.ApiService;
import com.example.themovie.api.MoviApi;
import com.example.themovie.api.PostApi;
import com.example.themovie.model.Movie;
import com.example.themovie.model.MovieResponse;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MoviesList extends Fragment {
    RecyclerView recyclerView;
    ArrayList<Movie> movies;
    MovieAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    private MoviApi postApi;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.page, container, false);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new MovieAdapter(getActivity(), movies);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        loadJSON();
//        swipeRefreshLayout.findViewById(R.id.main_content);
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                recyclerView = rootView.findViewById(R.id.recycler_view);
//                recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
//                adapter = new MovieAdapter(getActivity(), movies);
//                adapter.notifyDataSetChanged();
//                loadJSON();
//            }
//        });

        return rootView;
    }

    private void loadJSON() {
        try {
            if (BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()) {
                return;
            }
            ApiService.getApi().getPopularMoviesList(BuildConfig.THE_MOVIE_DB_API_TOKEN).enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(@NotNull Call<MovieResponse> call, Response<MovieResponse> response) {
                    Log.d("My_post_list", response.body().toString());
                    if (response.isSuccessful()) {
                        adapter.setMoviesList(response.body().getResults());
                        adapter.notifyDataSetChanged();
                    }
//                    swipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getContext(), "lolol", Toast.LENGTH_LONG).show();

                }
            });

        } catch (Exception e) {
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
            Log.d("LOL", e.toString());

        }
    }


}

package com.example.imagesearch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.imagesearch.model.Image;
import com.example.imagesearch.model.Response;
import com.jakewharton.rxbinding4.view.RxView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subscribers.DisposableSubscriber;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    final private String API_KEY = "API_KEY";
    final private String CX = "CX";
    final private String SEARCH_TYPE = "image";
    final private String FILE_TYPE = "jpg";

    static AppCompatImageView bigImage;
    static RecyclerView recyclerView;
    AppCompatButton searchButton;
    AppCompatEditText searchLine;

    private final String TAG = "TAG";
    private final List<Response> responseList = new ArrayList<>();
    private List<Image> images = new ArrayList<>();

    Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .baseUrl("https://www.googleapis.com")
            .build();

    CustomSearchApi api = retrofit.create(CustomSearchApi.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bigImage = findViewById(R.id.big_image);
        searchButton = findViewById(R.id.search_button);
        searchLine = findViewById(R.id.search_line);
        recyclerView = findViewById(R.id.recycler_view);

        RxView.clicks(searchButton).subscribe(v -> {
            hideKeyboard();

            Flowable<Response> flowable = api.getData(API_KEY, CX, Objects.requireNonNull(searchLine.getText()).toString(), SEARCH_TYPE, FILE_TYPE);

            flowable
                    .take(10)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableSubscriber<Response>() {
                        @Override
                        public void onNext(Response pictures) {
                            if (pictures != null) responseList.add(pictures);
                        }

                        @Override
                        public void onError(Throwable t) {
                            Log.d(TAG, "onError: " + t.getMessage());
                        }

                        @Override
                        public void onComplete() {
                            images = responseList.get(0).getImages();
                            ImagesAdapter adapter = new ImagesAdapter(getApplicationContext(), images);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            recyclerView.setAdapter(adapter);
                            responseList.clear();
                            Log.d(TAG, "flowable onComplete");
                        }
                    });
        });

        RxView.clicks(bigImage).subscribe(v -> {
            Log.d(TAG, "click on bigImage");
            bigImage.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        });
    }

    private void hideKeyboard() {
        InputMethodManager inputManager =
                (InputMethodManager) getApplicationContext().
                        getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(
                getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
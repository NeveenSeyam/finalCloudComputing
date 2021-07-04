package com.exp.cmfinalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.exp.cmfinalproject.api.ApiClient;
import com.exp.cmfinalproject.api.ApiInterface;
import com.exp.cmfinalproject.models.Article;
import com.exp.cmfinalproject.models.News;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.core.app.ActivityOptionsCompat.*;
import static com.exp.cmfinalproject.redesign.CardColor;
import static com.exp.cmfinalproject.redesign.fontColor;
import static com.exp.cmfinalproject.redesign.fontSize;

public class MainActivity extends AppCompatActivity implements  SwipeRefreshLayout.OnRefreshListener{

        public static final String API_KEY = "cddba27fd2ad4186acf34b3a35370dc0";
        private RecyclerView recyclerView;
        private RecyclerView.LayoutManager layoutManager;
        private List<Article> articles = new ArrayList<>();
        private Adapter adapter;
        private String TAG = MainActivity.class.getSimpleName();
        private TextView topHeadline;
        private SwipeRefreshLayout swipeRefreshLayout;
        private RelativeLayout errorLayout;
        private ImageView errorImage;
        private TextView errorTitle, errorMessage;
        private Button btnRetry;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
            swipeRefreshLayout.setOnRefreshListener(this);
            swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
            topHeadline = findViewById(R.id.topheadelines);
            recyclerView = findViewById(R.id.recyclerView);
            layoutManager = new LinearLayoutManager(MainActivity.this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setNestedScrollingEnabled(false);

            onLoadingSwipeRefresh("القدس");

            errorLayout = findViewById(R.id.errorLayout);
            errorImage = findViewById(R.id.errorImage);
            errorTitle = findViewById(R.id.errorTitle);
            errorMessage = findViewById(R.id.errorMessage);
            btnRetry = findViewById(R.id.btnRetry);
            topHeadline.setTextSize(fontSize);
            topHeadline.setTextColor(getResources().getColor(fontColor));
             SearchView searchView = (SearchView) findViewById(R.id.SearchView);

                  searchView.setQueryHint("Search Latest News...");

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    if (query.length() > 2){
                        onLoadingSwipeRefresh(query);
                    }
                    else {
                        Toast.makeText(MainActivity.this, "Type more than two letters!", Toast.LENGTH_SHORT).show();
                    }
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });

        }

        public void LoadJson(final String keyword){

            errorLayout.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(true);

            ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

            String country = "ps";
            String language = "ar";

            Call<News> call;

            if (keyword.length() > 0 ){
                call = apiInterface.getNewsSearch(keyword, language, "publishedAt", API_KEY);
            } else {
                call = apiInterface.getNews(country, API_KEY);
            }

            call.enqueue(new Callback<News>() {
                @Override
                public void onResponse(Call<News> call, Response<News> response) {
                    if (response.isSuccessful() && response.body().getArticle() != null){
                        Log.d("nev1" , ""+articles.size());

                        if (!articles.isEmpty()){
                            articles.clear();
                        }
                        Log.d("nev1" , ""+articles.size());
                        // هنا عرض الداتا بالرسايكل
                        articles = response.body().getArticle();
                        adapter = new Adapter(articles, MainActivity.this);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                        initListener();

                        topHeadline.setVisibility(View.VISIBLE);
                        swipeRefreshLayout.setRefreshing(false);


                    } else {

                        topHeadline.setVisibility(View.INVISIBLE);
                        swipeRefreshLayout.setRefreshing(false);

                        String errorCode;
                        switch (response.code()) {
                            case 404:
                                errorCode = "404 not found";
                                break;
                            case 500:
                                errorCode = "500 server broken";
                                break;
                            default:
                                errorCode = "unknown error";
                                break;
                        }

                        showErrorMessage(
                                R.drawable.no_result,
                                "No Result",
                                "Please Try Again!\n"+
                                        errorCode);

                    }
                }

                @Override
                public void onFailure(Call<News> call, Throwable t) {
                    topHeadline.setVisibility(View.INVISIBLE);
                    swipeRefreshLayout.setRefreshing(false);
                    showErrorMessage(
                            R.drawable.oops,
                            "Oops..",
                            "Network failure, Please Try Again\n"+
                                    t.toString());
                }
            });

        }



        private void initListener(){

            adapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    ImageView imageView = view.findViewById(R.id.img);
                    Intent intent = new Intent(MainActivity.this, NewsDetailActivity.class);


                    Article article = articles.get(position);
                    intent.putExtra("url", article.getUrl());
                    intent.putExtra("title", article.getTitle());
                    intent.putExtra("img",  article.getUrlToImage());
                    intent.putExtra("date",  article.getPublishedAt());
                    intent.putExtra("source",  article.getSource().getName());
                    intent.putExtra("author",  article.getAuthor());

                    Pair<View, String> pair = Pair.create((View)imageView, ViewCompat.getTransitionName(imageView));
                    ActivityOptionsCompat optionsCompat = makeSceneTransitionAnimation(
                            MainActivity.this,
                            pair
                    );


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        startActivity(intent, optionsCompat.toBundle());
                    }else {
                        startActivity(intent);
                    }

                }
            });

        }


        @Override
        public boolean onCreateOptionsMenu(Menu menu) {

            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_main, menu);

            //  SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//            final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
//            MenuItem searchMenuItem = menu.findItem(R.id.action_search);

          //  searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
      //      searchView.setQueryHint("Search Latest News...")
//
//            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                @Override
//                public boolean onQueryTextSubmit(String query) {
//                    if (query.length() > 2){
//                        onLoadingSwipeRefresh(query);
//                    }
//                    else {
//                        Toast.makeText(MainActivity.this, "Type more than two letters!", Toast.LENGTH_SHORT).show();
//                    }
//                    return false;
//                }
//
//                @Override
//                public boolean onQueryTextChange(String newText) {
//                    return false;
//                }
//            });

          //  searchMenuItem.getIcon().setVisible(false, false);

            return true;
        }

        @Override
        public void onRefresh() {
            LoadJson("Jerusalem");
        }

        private void onLoadingSwipeRefresh(final String keyword){

            swipeRefreshLayout.post(
                    new Runnable() {
                        @Override
                        public void run() {
                            LoadJson(keyword);
                        }
                    }
            );

        }
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle item selection
            switch (item.getItemId()) {
                case R.id.action_about_app:
                    Intent i = new Intent(getApplicationContext(), AboutAppActivity.class);
                    startActivity(i);
                    return true;
                case R.id.action_profile:
                    Intent n = new Intent(getApplicationContext(), MainActivityProfile.class);
                    startActivity(n);
                    return true;
                case R.id.action_setting_app:
                    Intent l = new Intent(getApplicationContext(), SettingActivity.class);
                    startActivity(l);
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }

        private void showErrorMessage(int imageView, String title, String message){

            if (errorLayout.getVisibility() == View.GONE) {
                errorLayout.setVisibility(View.VISIBLE);
            }

            errorImage.setImageResource(imageView);
            errorTitle.setText(title);
            errorMessage.setText(message);

            btnRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onLoadingSwipeRefresh("Jerusalem");
                }
            });

        }


    }

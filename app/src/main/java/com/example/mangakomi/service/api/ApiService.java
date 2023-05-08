package com.example.mangakomi.service.api;

import com.example.mangakomi.model.MangaContent;
import com.example.mangakomi.model.MangaDetail;
import com.example.mangakomi.model.MangaGenres;
import com.example.mangakomi.model.MangaLatest;
import com.example.mangakomi.model.MangaPopular;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;

public interface ApiService {
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

        Interceptor interceptor = chain -> {
            Request request = chain.request();
            Request.Builder  builder = request.newBuilder();
            return chain.proceed(builder.build());
        };

        OkHttpClient.Builder okBuilder = new OkHttpClient.Builder().addInterceptor(interceptor);
        ApiService apiService = new Retrofit.Builder()
            .baseUrl(Server.DOMAIN)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(okBuilder.build())
            .build()
            .create(ApiService.class);

    @GET(Server.URI_HOME)
    @Headers(Server.HEADER_POPULAR)
    Call<List<MangaPopular>> getListMangaPopular();

    @GET(Server.URI_HOME)
    Call<List<MangaLatest>> getListMangaLatest(@Header(Server.KEY_HEADER) String valueHeader);

    @GET(Server.URI_HOME)
    Call<List<MangaLatest>> getListMangaHot(@Header(Server.KEY_HEADER) String valueHeader);


    @GET(Server.URI_HOME)
    Call<List<MangaLatest>> getListMangaNew(@Header(Server.KEY_HEADER) String valueHeader);

    @GET(Server.URI_DETAIL)
    Call<MangaDetail> getMangaDetail(@Header(Server.KEY_HEADER) String valueHeader);

    @GET(Server.URI_CHAPTER)
    Call<MangaContent> getContentChapter(@Header(Server.KEY_HEADER) String valueHeader);

    @GET(Server.URI_CATEGORY)
    Call<MangaGenres> getMangaGenres(@Header(Server.KEY_HEADER) String valueHeader);

    @GET(Server.URI_SEARCH)
    Call<List<MangaLatest>> getSearch(@Header(Server.KEY_HEADER) String valueHeader);

    @GET(Server.URI_CHAPTER)
    Observable<MangaContent> getContentChapterObservable(@Header(Server.KEY_HEADER) String valueHeader);


}

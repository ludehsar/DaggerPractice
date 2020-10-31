package com.example.daggerpractice.di.main;

import android.app.Application;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.daggerpractice.network.main.MainAPI;
import com.example.daggerpractice.ui.main.posts.PostRecyclerAdapter;
import com.example.daggerpractice.utils.VerticalSpaceItemDecoration;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class MainModule {
    @Provides
    static PostRecyclerAdapter providePostRecyclerAdapter() {
        return new PostRecyclerAdapter();
    }

    @Provides
    static LinearLayoutManager provideLinearLayoutManager(Application application) {
        return new LinearLayoutManager(application);
    }

    @Provides
    static VerticalSpaceItemDecoration provideVerticalSpaceItemDecoration() {
        return new VerticalSpaceItemDecoration(15);
    }

    @Provides
    static MainAPI provideMainAPI(Retrofit retrofit) {
        return retrofit.create(MainAPI.class);
    }
}

package com.example.finalyear.di;


import android.content.Context;

import androidx.datastore.core.Serializer;
import androidx.datastore.rxjava3.RxDataStore;
import androidx.datastore.rxjava3.RxDataStoreBuilder;
import androidx.room.Room;

import com.example.finalyear.UserData;
import com.example.finalyear.common.apiservice.FoodApi;
import com.example.finalyear.common.utils.DietConstants;
import com.example.finalyear.db.DietRoomDb;
import com.example.finalyear.db.proto.UserDataSerializer;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
final public class AppModule {

    @Provides
    @Singleton
    Retrofit provideRetrofit() {
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.level(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
        return new Retrofit.Builder()
                .baseUrl("https://api.edamam.com/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    FoodApi provideFoodApi(Retrofit retrofit) {
        return retrofit.create(FoodApi.class);
    }

    @Provides
    @Singleton
    DietRoomDb provideRoom(@ApplicationContext Context context) {
        return Room.databaseBuilder(context, DietRoomDb.class, "dietdb").build();
    }

    @Provides
    @Singleton
    RxDataStore<UserData> provideDatastore(@ApplicationContext Context context) {
        Serializer<UserData> serializer = new UserDataSerializer();
        return new RxDataStoreBuilder<UserData>(context, "userdata.pb",
                serializer).build();
    }

    @Provides
    @Singleton
    DatabaseReference provideFireBaseDb() {
        return FirebaseDatabase.getInstance().getReference(DietConstants.USER_DATA_NODE);
    }
}

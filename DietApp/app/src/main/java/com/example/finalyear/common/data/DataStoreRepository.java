package com.example.finalyear.common.data;

import android.annotation.SuppressLint;

import androidx.datastore.rxjava3.RxDataStore;

import com.example.finalyear.Activities;
import com.example.finalyear.Counter;
import com.example.finalyear.Height;
import com.example.finalyear.UserData;
import com.example.finalyear.Weight;
import com.example.finalyear.common.data.firebasepojo.UserActivityData;
import com.example.finalyear.common.data.firebasepojo.UserInfo;
import com.example.finalyear.common.utils.Utility;

import javax.inject.Inject;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class DataStoreRepository {
    private final RxDataStore<UserData> dataStore;

    @Inject
    public DataStoreRepository(RxDataStore<UserData> dataStore) {
        this.dataStore = dataStore;
    }

    public Single<UserData> insertData(UserInfo info) {
        return dataStore.updateDataAsync(transform -> {
            UserData.Builder userDataBuilder = transform.toBuilder();

            com.example.finalyear.UserInfo.Builder infoBuilder = userDataBuilder.getInfo().toBuilder();
            Height.Builder heightB = infoBuilder.getHeight().toBuilder();
            heightB.setFt(info.getInfo().getHeight().getFt())
                    .setIn(info.getInfo().getHeight().getIn());
            Weight.Builder weightB = infoBuilder.getWeight().toBuilder();
            weightB.setKg(info.getInfo().getWeight().getKg())
                    .setGm(info.getInfo().getWeight().getGm());

            infoBuilder.setFirstName(info.getInfo().getFirstName())
                    .setLastName(info.getInfo().getLastName())
                    .setDob(info.getInfo().getDob())
                    .setGender(info.getInfo().getGender())
                    .setHeight(heightB)
                    .setWeight(weightB);

            return Single.just(userDataBuilder
                    .setEmail(info.getEmail())
                    .setUuid(info.getUuid())
                    .setInfo(infoBuilder)
                    .build());
        });
    }

    @SuppressLint("UnsafeOptInUsageWarning")
    public Single<UserData> incrementStep() {
        return dataStore.updateDataAsync(transform -> {
            int prevCounter = transform.getCounter().toBuilder().getSteps();
            float weightKg = transform.getInfo().getWeight().getKg()
                    +transform.getInfo().getWeight().getGm()/1000F;
            // Added The Step
            UserData.Builder userBuilder = transform.toBuilder();
            Counter.Builder counterBuilder = userBuilder.getCounter().toBuilder();
            counterBuilder.setSteps(prevCounter+1);
            // Add Calorie Burn Amount By Each Step
            int caloriesBurn = Utility.caloriesBurn(weightKg,prevCounter+1);
            counterBuilder.setCaloriesburn(caloriesBurn);

            return Single.just(userBuilder.setCounter(counterBuilder).build());
        });
    }

    @SuppressLint("UnsafeOptInUsageWarning")
    public Single<UserData> insertStepDate(Long date) {
        return dataStore.updateDataAsync(transform -> {
            UserData.Builder userBuilder = transform.toBuilder();
            Counter.Builder counterBuilder = userBuilder.getCounter().toBuilder();
            counterBuilder.setDate(date);
            return Single.just(userBuilder.setCounter(counterBuilder).build());
        });
    }

    @SuppressLint("UnsafeOptInUsageWarning")
    public Single<UserData> addDuration() {
        return dataStore.updateDataAsync(transform -> {
            long prevDur = transform.getCounter().getDuration();

            UserData.Builder userBuilder = transform.toBuilder();
            Counter.Builder counterBuilder = userBuilder.getCounter().toBuilder();
            counterBuilder.setDuration(prevDur+1);
            return Single.just(userBuilder.setCounter(counterBuilder).build());
        });
    }

    @SuppressLint("UnsafeOptInUsageWarning")
    public Single<UserData> submitGoalsAndRemain(UserActivityData userActivityData){
        return dataStore.updateDataAsync(transform -> {
            Activities.Builder userBuilder = transform.getActivities().toBuilder();
            userBuilder.setGoal(userActivityData.getGoal());
            userBuilder.setGoalText(userActivityData.getGoalText());
            userBuilder.setBaselineActivity(userActivityData.getBaselineActivity());
            userBuilder.setCalories(userActivityData.getCalories());
            return Single.just(transform.toBuilder().setActivities(userBuilder).build());
        });
    }

    public Single<UserData> resetCounter() {
        return dataStore.updateDataAsync(transform -> {
            Counter.Builder counterBuilder = transform.getCounter().toBuilder();
            counterBuilder.setSteps(0);
            counterBuilder.setDuration(0L);
            counterBuilder.setCaloriesburn(0);
            return Single.just(transform.toBuilder().setCounter(counterBuilder).build());
        });
    }

    @SuppressLint("UnsafeOptInUsageWarning")
    public Single<UserData> resetAll() {
        return dataStore.updateDataAsync(transform->{
            UserData.Builder userB = transform.toBuilder();
            userB.clearActivities();
            userB.clearCounter();
            userB.clearEmail();
            userB.clearInfo();
            userB.clearUuid();
            return Single.just(userB.build());
        });
    }
    @SuppressLint("UnsafeOptInUsageWarning")
    public @NonNull Flowable<Integer> getRemainCalories(){
        return dataStore.data().map(userData -> {
            int cal = userData.getActivities().getCalories()-userData.getCounter().getCaloriesburn();
            return Math.max(cal, 0);
        });
    }

    @SuppressLint("UnsafeOptInUsageWarning")
    public @NonNull Flowable<Integer> getCountOfSteps() {
        return dataStore.data().map(userData -> {
            return userData.getCounter().getSteps();
        });
    }

    @SuppressLint("UnsafeOptInUsageWarning")
    public @NonNull Flowable<Long> getStepDate() {
        return dataStore.data().map(userData -> userData.getCounter().getDate());
    }

    @SuppressLint("UnsafeOptInUsageWarning")
    public @NonNull Flowable<Integer> getStepGoal() {
        return dataStore.data().map(userData -> userData.getActivities().getGoal());
    }

    @SuppressLint("UnsafeOptInUsageWarning")
    public @NonNull Flowable<Long> getStepDuration() {
        return dataStore.data().map(userData -> {
            return userData.getCounter().getDuration();
        });
    }

    @SuppressLint("UnsafeOptInUsageWarning")
    public @NonNull Flowable<Integer> getCaloriesBurn() {
        return dataStore.data().map(userData -> {
            return userData.getCounter().getCaloriesburn();
        });
    }


    public @NonNull Flowable<Boolean> getStepAndGoalsEqual() {
        return dataStore.data().map(userData -> {
            return userData.getCounter().getSteps()==userData.getActivities().getGoal();
        });
    }

    public @NonNull Flowable<Counter> getCounterDatas() {
        return dataStore.data().map(UserData::getCounter);
    }

}

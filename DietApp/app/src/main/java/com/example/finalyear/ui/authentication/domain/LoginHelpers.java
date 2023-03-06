package com.example.finalyear.ui.authentication.domain;

import com.example.finalyear.UserData;
import com.example.finalyear.common.data.DataStoreRepository;
import com.example.finalyear.common.data.firebasepojo.UserInfo;
import com.example.finalyear.common.utils.DietConstants;
import com.example.finalyear.common.utils.DietSingleObserve;
import com.example.finalyear.ui.authentication.data.DietFireDbRepository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import javax.inject.Inject;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleEmitter;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;

public class LoginHelpers {


    private final DietFireDbRepository dietFireDbRepository;
    private final DataStoreRepository dataStoreRepository;

    @Inject
    public LoginHelpers(DietFireDbRepository
                                dietFireDbRepository, DataStoreRepository dataStoreRepository) {
        this.dietFireDbRepository = dietFireDbRepository;
        this.dataStoreRepository = dataStoreRepository;
    }


    private @NonNull Single<Status<String>> checkGoal(String uid) {
        return Single.create(emitter ->
                dietFireDbRepository.getUserDb(uid, new ValueEventListener() {
                    @Override
                    public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                        if (!snapshot.hasChild(DietConstants.USER_ACTIVITY_NODE)) {
                            emitter.onSuccess(new Status.UNSUCCESS<>(uid));
                            return;
                        }
                        emitter.onSuccess(new Status.SUCCESS<>(uid));
                    }

                    @Override
                    public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                        Timber.d(error.getMessage());
                    }
                })
        );
    }

    private @NonNull Single<Status<String>> checkInfo(String uid) {
        return Single.create(emitter ->
                dietFireDbRepository.getUserDb(uid, new ValueEventListener() {
                    @Override
                    public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                        if (!snapshot.hasChild(DietConstants.INFO_NODE)) {
                            emitter.onSuccess(new Status.UNSUCCESS<>(uid));
                            return;
                        }
                        emitter.onSuccess(new Status.SUCCESS<>(uid));
                    }

                    @Override
                    public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                        Timber.d(error.getMessage());
                        emitter.onError(new Throwable(error.toException()));
                    }
                })
        );
    }

    private @NonNull Single<Status<String>> checkEmailExist(String uid) {
        return Single.create(emitter ->
                dietFireDbRepository.getUserDb(uid, new ValueEventListener() {
                    @Override
                    public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                        if (!snapshot.hasChild("email")) {
                            emitter.onSuccess(new Status.UNSUCCESS<>(uid));
                            return;
                        }
                        emitter.onSuccess(new Status.SUCCESS<>(uid));
                    }

                    @Override
                    public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                        Timber.d(error.getMessage());
                        emitter.onError(error.toException());
                    }
                })
        );
    }


    public @NonNull Single<String> createIfNotEmail(String uid, String email, String password) {
        return Single.create(emitter ->
                dietFireDbRepository.createUserDb(new UserInfo(email, uid, password), (error, ref) -> {
                    if (error != null) {
                        emitter.onError(error.toException());
                        return;
                    }
                    emitter.onSuccess(uid);
                }));
    }


    private @NonNull Single<UserData> preserveDataToDataStore(String uid) {
        return Single
                .create(emitter -> {
                    dietFireDbRepository.getUserDb(uid, new ValueEventListener() {
                        @Override
                        public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                            UserInfo userInfo = snapshot.getValue(UserInfo.class);
                            emitter.onSuccess(userInfo);
                        }

                        @Override
                        public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                            emitter.onError(error.toException());
                        }
                    });
                })
                .flatMap(userInfo -> {
                    return dataStoreRepository.insertData((UserInfo) userInfo);
                });
    }

    public void loginSteps(Single<String> stringSingle, @NonNull SingleEmitter<LoginNavigateTo> emitter,
                           String email, String password) {
        stringSingle
                .flatMap(this::checkEmailExist)
                .flatMap(status -> {
                    if (status instanceof Status.UNSUCCESS) {
                        return createIfNotEmail(((Status.UNSUCCESS<String>) status).t,
                                email, password);
                    }
                    return Single.just(((Status.SUCCESS<String>) status).t);
                })
                .flatMap(this::checkInfo)
                .flatMap(status -> {
                    if (status instanceof Status.UNSUCCESS) {
                        emitter.onSuccess(LoginNavigateTo.SIGNUP_INFORMATION);
                        return Single.never();
                    }
                    return Single.just(((Status.SUCCESS<String>) status).t);
                })
                .flatMap(this::checkGoal)
                .flatMap(status -> {
                    if (status instanceof Status.UNSUCCESS) {
                        emitter.onSuccess(LoginNavigateTo.GOALS);
                        return Single.never();
                    }
                    return Single.just(((Status.SUCCESS<String>) status).t);
                })
                .flatMap(this::preserveDataToDataStore)
                .subscribeOn(Schedulers.io())
                .subscribe(new DietSingleObserve<UserData>() {
                    @Override
                    public void onComplete(UserData s) {
                        emitter.onSuccess(LoginNavigateTo.HOME);
                    }

                    @Override
                    public void onError(Throwable e) {
                        emitter.onError(e);
                    }
                });
    }

}

package com.example.finalyear.ui.activityuser.domain;

import com.example.finalyear.common.data.firebasepojo.UserActivityData;
import com.example.finalyear.ui.activityuser.data.UsersActivityRepository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import javax.inject.Inject;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.functions.Action;
import timber.log.Timber;

public class RetrieveUserActivitiesFireUseCase {
    private final UsersActivityRepository usersActivityRepository;

    @Inject
    public RetrieveUserActivitiesFireUseCase(UsersActivityRepository usersActivityRepository) {
        this.usersActivityRepository = usersActivityRepository;
    }

    public @NonNull Observable<UserActivityData> invoke(){
     return Observable.create(emitter -> {
         DatabaseReference dbr = usersActivityRepository.getUserDataFromFireToProto();
         dbr.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                 UserActivityData data = snapshot.getValue(UserActivityData.class);
                 if (data == null) {
                     emitter.onError(new Throwable("No Data"));
                     return;
                 }
                 emitter.onNext(data);
             }

             @Override
             public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                 emitter.onError(new Throwable(error.getMessage()));
             }
         });
     });
    }

}

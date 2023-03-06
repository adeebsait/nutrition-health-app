package com.example.finalyear.ui.home.data;

import androidx.annotation.NonNull;

import com.example.finalyear.common.utils.DietConstants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;

public class DashboardRepository {
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    @Inject
    public DashboardRepository() {
        this.databaseReference = FirebaseDatabase.getInstance().getReference(DietConstants.USER_DATA_NODE);
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    public Observable<Long> addCalories() {
        return Observable.create(emitter -> {
            if (firebaseAuth.getCurrentUser() == null) {
                emitter.onError(new Throwable("User need to logged in"));
                return;
            }
            try {
                String uuid = firebaseAuth.getCurrentUser().getUid();
                DatabaseReference dbs = databaseReference.child(uuid).child(DietConstants.USER_ACTIVITY_NODE)
                        .child("calories");

                dbs.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getValue() == null) {
                            emitter.onError(new Throwable("No Data To Show"));
                            return;
                        }
                        emitter.onNext((Long) snapshot.getValue());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        emitter.onError(new Throwable("No Data To Show"));
                    }
                });
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }
}

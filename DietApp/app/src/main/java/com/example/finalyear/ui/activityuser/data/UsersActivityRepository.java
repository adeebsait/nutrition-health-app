package com.example.finalyear.ui.activityuser.data;

import com.example.finalyear.common.utils.DietConstants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class UsersActivityRepository {


    private final FirebaseAuth auth;
    private final DatabaseReference databaseReference;

    @Inject
    public UsersActivityRepository() {
        this.auth = FirebaseAuth.getInstance();
        this.databaseReference = FirebaseDatabase.getInstance().getReference(DietConstants.USER_DATA_NODE);
    }

    public DatabaseReference getUserDataFromFireToProto() {
        if (auth.getCurrentUser() == null) {
            return  null;
        };
        String uuid = auth.getCurrentUser().getUid();
        return databaseReference
                .child(uuid).child(DietConstants.USER_ACTIVITY_NODE);
    }

}

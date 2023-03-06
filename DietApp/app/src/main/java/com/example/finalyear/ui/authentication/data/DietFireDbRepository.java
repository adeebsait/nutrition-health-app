package com.example.finalyear.ui.authentication.data;

import static com.example.finalyear.common.utils.DietConstants.USER_INVENTORY_NODE;

import androidx.annotation.NonNull;

import com.example.finalyear.common.data.firebasepojo.Info;
import com.example.finalyear.common.data.firebasepojo.UserActivityData;
import com.example.finalyear.common.data.firebasepojo.UserInfo;
import com.example.finalyear.common.utils.DietConstants;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import javax.inject.Inject;

public class DietFireDbRepository {
    private final DatabaseReference dbr;

    @Inject
    public DietFireDbRepository(DatabaseReference dbr) {
        this.dbr = dbr;
    }


    public void createUserDb(@NonNull UserInfo info, DatabaseReference.CompletionListener listener) {
        dbr.child(info.getUuid()).setValue(info, listener);
    }

    public void getUserDb(@NonNull String uid, ValueEventListener listener) {
        dbr.child(uid).addListenerForSingleValueEvent(listener);
    }

    public void addGoalInformation(UserActivityData userActivityData, String uid,
                                   DatabaseReference.CompletionListener listener) {
        dbr.child(uid).child(DietConstants.USER_ACTIVITY_NODE)
                .setValue(userActivityData, listener);
    }

    public void addInfo(String uid, Info info, DatabaseReference.CompletionListener listener) {
        ;
        dbr.child(uid).child(DietConstants.INFO_NODE)
                .setValue(info, listener);
    }

    public void removeScanFood(String uid, String key, DatabaseReference.CompletionListener listener) {
        dbr.child(uid).child(DietConstants.USER_SCAN_NODE)
                .child(key).removeValue(listener);
    }

    public DatabaseReference updateCalories(String uid) {
        return dbr.child(uid).child(DietConstants.USER_ACTIVITY_NODE)
                .child("calories");
    }

    public void getQueryForInventory(String uid, ValueEventListener listener) {
        Query query = dbr.child(uid).child(USER_INVENTORY_NODE)
                .orderByChild("quantity").limitToFirst(1);
        query.addListenerForSingleValueEvent(listener);
    }
    public void getQueryForInventoryItems(String uid, ValueEventListener listener) {
        Query query = dbr.child(uid).child(USER_INVENTORY_NODE)
                .orderByChild("quantity");
        query.addListenerForSingleValueEvent(listener);
    }
}

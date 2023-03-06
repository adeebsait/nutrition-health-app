package com.example.finalyear.ui.foodscanner.data;

import androidx.annotation.NonNull;

import com.example.finalyear.common.apiservice.FoodApi;
import com.example.finalyear.common.utils.DietConstants;
import com.example.finalyear.db.DietRoomDb;
import com.example.finalyear.ui.foodscanner.data.foodpojo.FoodResponse;
import com.example.finalyear.ui.inventory.data.InventoryItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.Callback;

public class ScannerRepository {
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    private DietRoomDb dietRoomDb;


    private final FoodApi foodApi;
    @Inject
    public ScannerRepository(DietRoomDb dietRoomDb,FoodApi foodApi) {
        this.foodApi = foodApi;
        this.dietRoomDb = dietRoomDb;
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.database = FirebaseDatabase.getInstance();
        this.databaseReference = database.getReference(DietConstants.USER_DATA_NODE);
    }


    public Single<List<InventoryItem>> getScanItem() {
        return Single.create(emitter -> {
            if (firebaseAuth.getCurrentUser() == null) {
                emitter.onError(new Throwable("Need to login user"));
                return;
            }
            String uuid = firebaseAuth.getCurrentUser().getUid();
            DatabaseReference scanref = databaseReference.child(uuid).child(DietConstants.USER_SCAN_NODE);
            scanref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<InventoryItem> list = new ArrayList<>();
                    try {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            InventoryItem item = dataSnapshot.getValue(InventoryItem.class);
                            list.add(item);
                        }
                        emitter.onSuccess(list);
                    } catch (Exception e) {
                        emitter.onError(new Throwable("Snapshot error"));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    emitter.onError(new Throwable("DatabaseError"));
                }
            });
        });
    }

    public Observable<List<ScanHistoryItem>> getHistory(){
        return  dietRoomDb.scanDao().getScanItems().toObservable();
    }

    public Observable<Object> saveHistory(String code){
        return dietRoomDb.scanDao().insertScan(new ScanHistoryItem(code)).toObservable();
    }

    public void getFoodByBarCode(String code, Callback<FoodResponse> callback) {
        foodApi.getFoodFromBarcode(code).enqueue(callback);
    }
}

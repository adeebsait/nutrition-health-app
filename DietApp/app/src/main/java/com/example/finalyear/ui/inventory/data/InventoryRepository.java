package com.example.finalyear.ui.inventory.data;

import static com.example.finalyear.common.utils.DietConstants.USER_DATA_NODE;
import static com.example.finalyear.common.utils.DietConstants.USER_INVENTORY_NODE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.finalyear.common.utils.DietConstants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import timber.log.Timber;

public class InventoryRepository {
    private final DatabaseReference databaseReference;
    private final FirebaseAuth firebaseAuth;

    @Inject
    public InventoryRepository() {
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.databaseReference = FirebaseDatabase.getInstance().getReference(USER_DATA_NODE);
    }

    public Observable<List<InventoryItem>> getInventoryList() {
        return Observable.create(emitter -> {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (firebaseAuth.getCurrentUser() == null) return;
                    String uid = firebaseAuth.getCurrentUser().getUid();
                    if (!snapshot.hasChild(uid)) return;
                    List<InventoryItem> list = new ArrayList<>();
                    try {
                        for (DataSnapshot inventoryItemSnapshot : snapshot.child(uid).child(USER_INVENTORY_NODE).getChildren()) {
                            InventoryItem item = inventoryItemSnapshot.getValue(InventoryItem.class);
                            list.add(item);
                        }
                        emitter.onNext(list);
                    } catch (Exception e) {
                        emitter.onError(new Throwable("Snapshot error"));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    emitter.onError(new Throwable(error.getMessage()));
                }
            });
        });
    }


    public Single<String> saveScanItem(InventoryItem scanItem) {
        return Single.create(emitter -> {
            if (firebaseAuth.getCurrentUser() == null) {
                emitter.onError(new Throwable("Need to login user"));
                return;
            }
            String uuid = firebaseAuth.getCurrentUser().getUid();
            String key = databaseReference.push().getKey();
            if (key == null) key = System.currentTimeMillis() + "";
            scanItem.setId(key);
            String finalKey = key;
            databaseReference.child(uuid).child(DietConstants.USER_SCAN_NODE)
                    .child(key).setValue(scanItem).addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            emitter.onError(new Throwable("Database Failed"));
                            return;
                        }
                        emitter.onSuccess(finalKey);
                    });
        });

    }


    public Single<String> saveInventoryItem(InventoryItem scanItem) {
        return Single.create(emitter -> {
            if (firebaseAuth.getCurrentUser() == null) {
                emitter.onError(new Throwable("Need to login user"));
                return;
            }
            String uuid = firebaseAuth.getCurrentUser().getUid();
            String key = databaseReference.push().getKey();
            if (key == null) key = System.currentTimeMillis() + "";
            scanItem.setId(key);
            String finalKey = key;
            databaseReference.child(uuid).child(USER_INVENTORY_NODE)
                    .child(key).setValue(scanItem).addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            emitter.onError(new Throwable("Database Failed"));
                            return;
                        }
                        emitter.onSuccess(finalKey);
                    });
        });

    }


    public Single<Boolean> reduceInventoryItem(List<InventoryItem> reducingList) {
        return Single.create(emitter -> {
            if (firebaseAuth.getCurrentUser() == null) {
//            emitter.onError(new Throwable("Need to login user"));
                return;
            }
            String uuid = firebaseAuth.getCurrentUser().getUid();
            DatabaseReference dbr = databaseReference.child(uuid).child(USER_INVENTORY_NODE);
            dbr.runTransaction(new Transaction.Handler() {
                @NonNull
                @Override
                public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                    try {
                        HashMap<String, InventoryItem> retList = new HashMap<>();

                        for (MutableData snapshot : currentData.getChildren()) {
                            retList.put(snapshot.getKey(), snapshot.getValue(InventoryItem.class));
                        }

                        for (InventoryItem inventoryItem : reducingList) {
                            String itemId = inventoryItem.getId();
                            if (!retList.containsKey(itemId)) continue;
                            try {
                                int remain = retList.get(itemId).getQuantity()
                                        - inventoryItem.getQuantity();
                                if (remain <= 0) {
                                    retList.remove(itemId);
                                    continue;
                                }
                                InventoryItem item = retList.get(itemId);
                                item.setQuantity(remain);
                                retList.put(item.getId(), item);
                            } catch (Exception e) {
                                Timber.d(e);
                            } finally {
                                currentData.setValue(retList);
                            }
                        }
                        return Transaction.success(currentData);
                    } catch (Exception e) {
                        Timber.e(e);
                        return Transaction.abort();
                    }
                }

                @Override
                public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                    if (error != null || !committed) {
                        emitter.onError(new Throwable("Failed "));
                        return;
                    }
                    emitter.onSuccess(true);
                }
            });
        });
    }

}

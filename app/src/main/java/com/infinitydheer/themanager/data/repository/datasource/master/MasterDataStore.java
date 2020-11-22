package com.infinitydheer.themanager.data.repository.datasource.master;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.infinitydheer.themanager.data.entity.ConvEntity;
import com.infinitydheer.themanager.data.entity.UserEntity;
import com.infinitydheer.themanager.data.repository.datasource.razgo.RazgoOnlineDataStore;
import com.infinitydheer.themanager.domain.listener.ChangeListener;
import static com.infinitydheer.themanager.domain.constants.S.RConst.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MasterDataStore implements MasterStore {
    private static MasterDataStore sInstance;

    private FirebaseFirestore mOnlineDb;

    private MasterDataStore() {
        this.mOnlineDb = FirebaseFirestore.getInstance();
    }

    public static synchronized MasterDataStore getInstance() {
        if (sInstance == null) {
            sInstance = new MasterDataStore();
        }
        return sInstance;
    }

    @Override
    public void getUserList(ChangeListener<List<UserEntity>> changeListener) {
        mOnlineDb.collection(COLL_META)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot result = task.getResult();

                            if (result != null && !result.isEmpty()) {
                                List<UserEntity> res = new ArrayList<>();

                                for (DocumentSnapshot documentSnapshot : result) {
                                    res.add(new UserEntity(documentSnapshot.getId(), documentSnapshot.getString(K_UPASS)));
                                }

                                changeListener.onNext(res);
                            }

                        } else {
                            changeListener.onError(task.getException());
                        }
                        changeListener.onComplete();
                    }
                });
    }

    @Override
    public void allowAccess(boolean allow, String userId, ChangeListener<Void> changeListener) {
        DocumentReference blockerDoc = mOnlineDb
                .collection(COLL_META)
                .document(userId)
                .collection(COLL_UPDATES)
                .document("-1001");

        mOnlineDb.collection(COLL_META)
                .document(userId)
                .update(K_ACCOUNT_ALLOWED, allow)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            if (!allow) {
                                blockerDoc.set(RazgoOnlineDataStore.getDocumentSurvival())
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful())
                                                    changeListener.onComplete();
                                                else
                                                    changeListener.onError(new Exception("The account access could not be revoked"));
                                            }
                                        });
                            } else {
                                blockerDoc.delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful())
                                                    changeListener.onComplete();
                                                else
                                                    changeListener.onError(new Exception("The account access could not be granted"));
                                            }
                                        });
                            }

                        } else
                            changeListener.onError(new Exception("The account access could not be revoked"));
                    }
                });
    }

    @Override
    public void getUserAccessStatus(String userId, ChangeListener<Boolean> changeListener) {
        String standardExceptionHere = "Could not get the user access status";

        mOnlineDb.collection(COLL_META).document(userId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot snapshot = task.getResult();

                            if (snapshot != null && snapshot.exists()) {
                                try {
                                    boolean allowed = true;
                                    allowed = snapshot.getBoolean(K_ACCOUNT_ALLOWED);

                                    if (allowed) {

                                        mOnlineDb.collection(COLL_META)
                                                .document(userId).collection(COLL_UPDATES).document("-1001")
                                                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot res = task.getResult();
                                                    if (res != null && res.exists()) {
                                                        changeListener.onNext(false);
                                                    } else {
                                                        changeListener.onNext(true);
                                                    }
                                                } else {
                                                    changeListener.onError(new Exception(standardExceptionHere + "1"));
                                                }

                                                changeListener.onComplete();
                                            }
                                        });

                                    } else {
                                        changeListener.onNext(false);
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    changeListener.onError(new Exception(standardExceptionHere + "2"));
                                }
                            }
                        } else {
                            changeListener.onError(new Exception(standardExceptionHere + "3"));
                        }
                    }
                });
    }

    @Override
    public void getParticipatingConversations(String userId, ChangeListener<ConvEntity> changeListener) {
        mOnlineDb.collection(COLL_META)
                .document(userId)
                .collection(COLL_PART)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot res = task.getResult();
                            if (res != null && !res.isEmpty()) {
                                List<Long> convIdList = new ArrayList<>();
                                for (DocumentSnapshot snapshot : res) {
                                    convIdList.add(Long.valueOf(snapshot.getId()));
//                                    changeListener.onNext(new ConvEntity("NoOne",Long.valueOf(snapshot.getId())));
                                }

//                                changeListener.onComplete();

                                getParticipatingConversations2(userId, changeListener, convIdList);
                            }
                        } else {
                            changeListener.onError(task.getException());
                        }
                    }
                });
    }

    public void getParticipatingConversations2(String userId, ChangeListener<ConvEntity> changeListener,
                                               List<Long> convIdList) {
        CollectionReference convCollection = mOnlineDb.collection(COLL_CONV);

        long lastId = convIdList.get(convIdList.size() - 1);

        for (long convId : convIdList) {
            convCollection.document(convId + "")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot snapshot = task.getResult();
                                if (snapshot != null && snapshot.exists()) {
                                    ConvEntity conv = new ConvEntity(userId, convId);

                                    String person1 = snapshot.getString(K_ONE_ID);
                                    String person2 = snapshot.getString(K_TWO_ID);

                                    if (person1 == null || person2 == null) return;

                                    String partnerName = person1.equals(userId) ? person2 : person1;
                                    conv.setPartnerName(partnerName);

                                    changeListener.onNext(conv);

                                    if (convId == lastId) changeListener.onComplete();
                                }
                            }
                        }
                    });
        }
    }

    @Override
    public void cleanConversation(long convId, ChangeListener<Void> then) {
        DocumentReference targetConvMeta = mOnlineDb.collection(COLL_CONV)
                .document("" + convId);

        targetConvMeta.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot result = task.getResult();
                    if (result != null && result.exists()) {
                        try {
                            final long curId = result.getLong(K_LATID) - 100;

                            cleanRazgo(convId,500,then,curId);
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Exception e = task.getException();
                    if (e != null) e.printStackTrace();

                    then.onError(e);
                    then.onComplete();
                }
            }
        });
    }

    private void cleanRazgo(long convId, long curId, long left, long fails,
                            ChangeListener<Void> then, final long endid) {
        if (curId < 0 || curId > endid || left <= 0 || fails >= 4) {
            then.onComplete();
            return;
        }

        DocumentReference targetRazgo = mOnlineDb.collection(COLL_CONV).document("" + convId)
                .collection(COLL_SYNCED).document("" + curId);

        targetRazgo.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        cleanRazgo(convId, curId - 1, left - 1, 0, then, endid);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                long nextId;

//                if (endid - curId > curId) nextId = (endid + curId) / 2;
//                else nextId = curId / 2;

                nextId = new Random().nextInt((int) endid);

                Log.d("TheManagerLog","Failed at "+curId+", now going to "+nextId);

//                if (nextId == curId) {
//                    cleanRazgo(convId, -1, 0, 6, then, endid);
//                    return;
//                }

                cleanRazgo(convId, nextId, left - 1, fails + 1, then, endid);
            }
        });
    }

    /**
     * A method to clean a single conversation collection
      * @param convId The conversation ID
     * @param howmany The number of razgos to be cleaned
     * @param then The callback to let the caller function know what happened, ie, success, failure, completion
     * @param endid The ID of the latest razgo allowed to be cleaned (won't clean any razgos with an ID greater than this)
     */
    private void cleanRazgo(long convId, long howmany, ChangeListener<Void> then, final long endid){
        CollectionReference targetCollection = mOnlineDb.collection(COLL_CONV).document(""+convId).collection(COLL_SYNCED);
        targetCollection.whereLessThan(K_ID,endid)
                .limit(howmany)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isComplete()){
                            if(task.isSuccessful()){
                                QuerySnapshot querySnapshot = task.getResult();
                                if (querySnapshot != null) {
                                    WriteBatch batch = mOnlineDb.batch();
                                    for(DocumentSnapshot documentSnapshot : querySnapshot){
                                        String razgoId = documentSnapshot.getId();
                                        batch.delete(targetCollection.document(razgoId));
                                    }
                                    //TODO: Test this code.
                                    batch.commit()
                                            .addOnSuccessListener(aVoid -> {
                                                then.onComplete();
                                                Log.d("TheManagerLog", "Done cleaning razgos");
                                            })
                                            .addOnFailureListener(then::onError);
                                }else{
                                    then.onError(task.getException());
                                }
                            }else{
                                then.onError(task.getException());
                            }
                        }else{
                            then.onError(task.getException());
                        }
                }});
    }
}

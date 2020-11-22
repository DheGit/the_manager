package com.infinitydheer.themanager.data.repository.datasource.razgo;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.WriteBatch;
import com.infinitydheer.themanager.data.entity.ConvEntity;
import com.infinitydheer.themanager.data.entity.RazgoEntity;
import com.infinitydheer.themanager.domain.constants.S;
import com.infinitydheer.themanager.domain.listener.ChangeListener;
import com.infinitydheer.themanager.presentation.ApplicationGlobals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.infinitydheer.themanager.domain.constants.S.RConst.*;

public class RazgoOnlineDataStore {
    private static RazgoOnlineDataStore sInstance;

    private FirebaseFirestore mOnlineDb;
    private ListenerRegistration mUpdateListener;
    private OnlineMainListener mMainDataStore;

    //TODO: Add cache support to reduce the number of reads as well

    private RazgoOnlineDataStore(OnlineMainListener liste){
        mOnlineDb =FirebaseFirestore.getInstance();
        this.mMainDataStore =liste;
    }

    public static synchronized RazgoOnlineDataStore getInstance(OnlineMainListener liste){
        if(sInstance ==null){
            sInstance =new RazgoOnlineDataStore(liste);
        }
        return sInstance;
    }

    public void getConvId(String partnerId, ChangeListener<Long> changeListener){
        if(changeListener==null) return;

        mOnlineDb.collection(COLL_META)
                .document(partnerId)
                .get().addOnCompleteListener(task -> {

            if(task.isSuccessful()){
                DocumentSnapshot snapshot=task.getResult();
                if(snapshot==null) return;

                if(snapshot.exists())
                    makeConversationWith(partnerId, changeListener);
                else
                    changeListener.onError(new Exception("User with given ID does not exist"));
            }else{
                changeListener.onError(new Exception("Improper network connection or state"));
            }
        });
    }
    private void makeConversationWith(String partnerId, ChangeListener<Long> changeListener){
        DocumentReference selfMeta, otherMeta, convMeta;

        selfMeta= mOnlineDb.collection(COLL_META).document(ApplicationGlobals.SELF_ID);
        otherMeta= mOnlineDb.collection(COLL_META).document(partnerId);
        convMeta= mOnlineDb.collection(COLL_CONV).document(DOC_COLL_META);

        final long[] newConvId = new long[1];

        mOnlineDb.runTransaction((Transaction.Function<Void>) transaction -> {
            try{
                long curConv=transaction.get(convMeta).getLong(K_CONV_LATID);
                ++curConv;
                newConvId[0] =curConv;
                DocumentReference targetConv= mOnlineDb.collection(COLL_CONV)
                        .document(""+curConv);

                transaction.set(selfMeta.collection(COLL_PART).document(""+curConv),getDocumentSurvival());
                transaction.set(otherMeta.collection(COLL_PART).document(""+curConv),getDocumentSurvival());
                transaction.set(targetConv,getNewConv(partnerId));

                transaction.update(convMeta,K_CONV_LATID,curConv);
            }catch (Exception e){
                changeListener.onError(e);
                Log.e(S.Constants.LOG_ID, "makeConversationWith apply: ",e);
            }
            return null;
        }).addOnSuccessListener(aVoid -> {
            RazgoOnlineDataStore.this.mMainDataStore.addConversation(new ConvEntity(partnerId,newConvId[0]));
            changeListener.onNext(newConvId[0]);
            changeListener.onComplete();
        }).addOnFailureListener(changeListener::onError);
    }

    public void syncAllConv(int limit) {
        if(ApplicationGlobals.SELF_ID.equals("")) return;

        mOnlineDb.collection(COLL_META)
                .document(ApplicationGlobals.SELF_ID)
                .collection(COLL_PART)
                .get().addOnSuccessListener(queryDocumentSnapshots -> {


                    List<Long> convIdList=new ArrayList<>();
            for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                long id= Long.valueOf(snapshot.getId());

                convIdList.add(id);
            }

            CollectionReference metaRef=mOnlineDb.collection(COLL_META).
                    document(ApplicationGlobals.SELF_ID).
                    collection(COLL_UPDATES);

            RazgoOnlineDataStore.this.syncGet1New(convIdList,false,metaRef);
        });
    }

    public void getParticipatingConv(int limit) {
        mOnlineDb.collection(COLL_META)
                .document(ApplicationGlobals.SELF_ID)
                .collection(COLL_PART)
                .get().addOnSuccessListener(queryDocumentSnapshots -> {

            for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                long id= Long.valueOf(snapshot.getId());

                mOnlineDb.collection(COLL_CONV)
                        .document("" + id)
                        .get().addOnSuccessListener(snapshot1 -> {

                    String oneId, twoId;
                    oneId = snapshot1.getString(K_ONE_ID);
                    twoId = snapshot1.getString(K_TWO_ID);
                    if (oneId == null || twoId == null) return;

                    String partnerId = (oneId.equals(ApplicationGlobals.SELF_ID)) ? twoId : oneId;

                    RazgoOnlineDataStore.this.getLast(limit, id, partnerId);
                });
            }
        });
    }
    public void getLast(int limit, long convid, String partnerId){
        this.mMainDataStore.addConversation(new ConvEntity(partnerId,convid));

        CollectionReference selfUpdateCollection= mOnlineDb.collection(COLL_META)
                .document(ApplicationGlobals.SELF_ID).collection(COLL_UPDATES);

        CollectionReference targetCollection= mOnlineDb.collection(COLL_CONV)
                .document(""+convid).collection(COLL_SYNCED);

        targetCollection
                .orderBy(K_ID, Query.Direction.DESCENDING)
                .limit(limit)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<RazgoEntity> razgoList=new ArrayList<>();
                    RazgoEntity lastEntity=new RazgoEntity();

                    for(DocumentSnapshot snapshot:queryDocumentSnapshots){
                        RazgoEntity entity=mapSnapshotToEntity(snapshot);

                        RazgoOnlineDataStore.this.mMainDataStore.onRazgoReceived(entity);

                        razgoList.add(entity);
                        lastEntity=entity;

                    }

                    ConvEntity convEntity=new ConvEntity();
                    convEntity.setConvid(convid);
                    convEntity.setPartnerName(partnerId);
                    convEntity.setLastTime(lastEntity.getDatetime());
                    convEntity.setLastSender(lastEntity.getSender());
                    convEntity.setLastMsg(lastEntity.getContent());
                    convEntity.setK(lastEntity.getK());

                    RazgoOnlineDataStore.this.mMainDataStore.onConvReceived(convEntity,true);
                    RazgoOnlineDataStore.this.mMainDataStore.addRazgosToMain(razgoList,convid);

                    selfUpdateCollection.document(""+convid).delete();

                }).addOnFailureListener(e -> {
                    Log.e(S.Constants.LOG_ID, "getLast onFailure: ",e);
                });
    }

    public void removeUpdateListener(){
        if(mUpdateListener ==null) return;

        if(S.Constants.LOG_ENABLED) Log.d(S.Constants.LOG_ID, "removeUpdateListener: removed");

        mUpdateListener.remove();
    }
    public void setUpdateListener(){
        if(S.Constants.LOG_ENABLED) Log.d(S.Constants.LOG_ID, "setUpdateListener: Called with hash: "+hashCode());

        String selfId=ApplicationGlobals.SELF_ID;
        if(selfId.equals("")) selfId= mMainDataStore.getUserId();
        if(selfId.equals("")) return;

        if(mUpdateListener !=null){
            removeUpdateListener();
            mUpdateListener =null;
        }

        if(S.Constants.LOG_ENABLED) Log.d(S.Constants.LOG_ID, "setUpdateListener: Set");

        mUpdateListener = mOnlineDb.collection(COLL_META)
                .document(selfId)
                .collection(COLL_UPDATES)
                .addSnapshotListener((queryDocumentSnapshots, e) -> RazgoOnlineDataStore.this.syncGet(false));
    }

    public void sync(){
        if(this.mMainDataStore !=null) syncGet(true);
    }
    public void syncGet(boolean callPut){
        String selfId= ApplicationGlobals.SELF_ID;
        if(selfId.equals("")) selfId= mMainDataStore.getUserId();
        if(selfId.equals("")){
            Log.e(S.Constants.LOG_ID, "syncGet: The self id is null, syncGet aborted");
            return;
        }

        CollectionReference colRef= mOnlineDb.collection(COLL_META).document(selfId).collection(COLL_UPDATES);
        List<Long> updateList=new ArrayList<>();

        colRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for(QueryDocumentSnapshot snapshot: queryDocumentSnapshots){
                String id=snapshot.getId();

                long longId=Long.valueOf(id);
                if(longId==-1001) return; //TODO Add this to the main group of constants

                updateList.add(longId);
            }
            if(updateList.size()!=0) syncGet1New(updateList, callPut, colRef);

        }).addOnFailureListener(e -> {});
    }
    public void syncGet1(List<Long> convIds, boolean callPut, CollectionReference metaRef){
        String tselfId=ApplicationGlobals.SELF_ID;
        if(tselfId.equals("")) tselfId= mMainDataStore.getUserId();
        if(tselfId.equals("")){
            Log.e(S.Constants.LOG_ID, "syncGet1: Self id is null, aborted sync");
            return;
        }

        final String selfId=tselfId;

        for(long convid: convIds){
            List<RazgoEntity> entities=new ArrayList<>();

            CollectionReference colRef= mOnlineDb.collection(COLL_CONV)
                    .document(""+convid)
                    .collection(COLL_SYNCED);

            colRef.whereEqualTo(K_SYNCED,false)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        WriteBatch setSyncBatch= mOnlineDb.batch();

                        boolean addedConv=false;

                        RazgoEntity lastEntity=null;
                        String senderId="";

                        for(QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            RazgoEntity entity=null;
                            try{
                                String sender=documentSnapshot.getString(K_SENDER);
                                long id=documentSnapshot.getLong(K_ID);

                                if(sender.equals(selfId)) continue;
                                senderId=sender;

                                if(!addedConv){
                                    this.mMainDataStore.addConversation(new ConvEntity(sender,convid));
                                    addedConv=true;
                                }

                                entity=new RazgoEntity();
                                entity=mapSnapshotToEntity(documentSnapshot);

                                RazgoOnlineDataStore.this.mMainDataStore.onRazgoReceived(entity);
                                lastEntity=entity;

                                setSyncBatch.update(colRef.document(""+id),
                                        K_SYNCED,true);
                            }catch (NullPointerException e){
                                e.printStackTrace();
                                Log.e(S.Constants.LOG_ID, "syncGet1: ", e);
                            }catch (Exception e){
                                e.printStackTrace();
                                Log.e(S.Constants.LOG_ID, "syncGet1: ",e);
                            }
                            if(entity!=null){
                                entities.add(entity);
                            }
                        }

                        final ConvEntity convEntity; //TODO Make this non-null and make code beautiful
                        if(lastEntity!=null){
                            convEntity=new ConvEntity();

                            convEntity.setLastSender(lastEntity.getSender());
                            convEntity.setK(lastEntity.getK());
                            convEntity.setConvid(convid);
                            convEntity.setPartnerName(senderId);
                            convEntity.setLastTime(lastEntity.getDatetime());
                            convEntity.setLastMsg(lastEntity.getContent());
                        }else{
                            convEntity=null;
                        }

                        RazgoOnlineDataStore.this.mMainDataStore.addRazgosToOfflineDB(entities, convid);
                        RazgoOnlineDataStore.this.mMainDataStore.onConvReceived(convEntity);

                        setSyncBatch.commit()
                                .addOnSuccessListener(aVoid -> {
                                    metaRef.document(""+convid).delete();

                                    if(callPut) RazgoOnlineDataStore.this.syncPut();
                                });
                    });
        }
    }

    public void syncGetAll(boolean callPutNew){
        String selfId= ApplicationGlobals.SELF_ID;
        if(selfId.equals("")) selfId= mMainDataStore.getUserId();
        if(selfId.equals("")){
            Log.e(S.Constants.LOG_ID, "syncGet: The self id is null, syncGet aborted");
            return;
        }

        CollectionReference partiRef=mOnlineDb.collection(COLL_META).document(selfId).collection(COLL_PART);
        CollectionReference updatesRef= mOnlineDb.collection(COLL_META).document(selfId).collection(COLL_UPDATES);
        List<Long> updateList=new ArrayList<>();

        partiRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for(QueryDocumentSnapshot snapshot: queryDocumentSnapshots){
                String id=snapshot.getId();

                long longId=Long.valueOf(id);
                if(longId==-1001) return; //TODO Add this to the main group of constants

                updateList.add(longId);
            }
            if(updateList.size()!=0) syncGet1New(updateList, callPutNew, updatesRef);

        }).addOnFailureListener(e -> {});
    }
    public void syncGet1New(List<Long> convIds, boolean callPutNew, CollectionReference metaRef){
        String tselfId=ApplicationGlobals.SELF_ID;
        if(tselfId.equals("")) tselfId= mMainDataStore.getUserId();
        if(tselfId.equals("")){
            Log.e(S.Constants.LOG_ID, "syncGet1: Self id is null, aborted sync");
            return;
        }

        final String selfId=tselfId;

        for(long convid: convIds){
            List<RazgoEntity> entities=new ArrayList<>();

            CollectionReference colRef=mOnlineDb.collection(COLL_CONV)
                    .document(""+convid)
                    .collection(COLL_SYNCED);

            Query toExecute;
            long lastRazgoId=mMainDataStore.getLastRazgoId(convid);

            if(lastRazgoId<=0){
                Log.e(S.Constants.LOG_ID, "syncGet1New: Couldn't find the razgoGroup locally, aborting sync");
                return;
            }else{
                toExecute=colRef.whereGreaterThan(K_ID,lastRazgoId).
                        orderBy(K_ID, Query.Direction.DESCENDING).limit(100);
            }

            toExecute.get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        RazgoEntity lastEntity=null;
                        String senderId="";

                        for(QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            RazgoEntity entity=null;
                            try{
                                String sender=documentSnapshot.getString(K_SENDER);
                                long id=documentSnapshot.getLong(K_ID);

                                senderId=sender;

                                entity=new RazgoEntity();
                                entity=mapSnapshotToEntity(documentSnapshot);

                                RazgoOnlineDataStore.this.mMainDataStore.onRazgoReceived(entity);
                                lastEntity=entity;
                            }catch (NullPointerException e){
                                e.printStackTrace();
                                Log.e(S.Constants.LOG_ID, "syncGet1: ", e);
                            }catch (Exception e){
                                e.printStackTrace();
                                Log.e(S.Constants.LOG_ID, "syncGet1: ",e);
                            }
                            if(entity!=null){
                                entities.add(entity);
                            }
                        }

                        final ConvEntity convEntity; //TODO Make this non-null and make code beautiful
                        if(lastEntity!=null){//FIXME And fix this too
                            convEntity=new ConvEntity();

                            convEntity.setLastSender(lastEntity.getSender());
                            convEntity.setK(lastEntity.getK());
                            convEntity.setConvid(convid);
                            convEntity.setPartnerName(senderId);
                            convEntity.setLastTime(lastEntity.getDatetime());
                            convEntity.setLastMsg(lastEntity.getContent());
                        }else{
                            convEntity=null;
                        }

                        RazgoOnlineDataStore.this.mMainDataStore.addRazgosToMain(entities, convid);
//                        RazgoOnlineDataStore.this.mMainDataStore.onConvReceived(convEntity);
                        RazgoOnlineDataStore.this.mMainDataStore.onConvReceived(convEntity,convid==convIds.get(convIds.size()-1));

                        metaRef.document(""+convid).delete();
                        if(callPutNew) RazgoOnlineDataStore.this.syncPut();
                    });
        }
    }

    public void syncPut(){
        List<Long> convList=this.mMainDataStore.getUpdateIds();
        Map<String,Object> survive=getDocumentSurvival();

        for(long convid: convList){
            List<RazgoEntity> updates=this.mMainDataStore.getUpdateRazgos(convid);

            List<Long> oldIds=new ArrayList<>();
            for(RazgoEntity entity: updates) oldIds.add(entity.getId());

            List<Long> newIds=new ArrayList<>();

            String toId=this.mMainDataStore.getPartnerId(convid);

            CollectionReference otherUpdateCollection= mOnlineDb.collection(COLL_META)
                    .document(toId).collection(COLL_UPDATES);
            DocumentReference targetDoc= mOnlineDb.collection(COLL_CONV).document(""+convid);

            List<Map<String,Object>> dataList= mapEntitiesToMaps(updates);

            mOnlineDb.runTransaction((Transaction.Function<Void>) transaction -> {
                try{
                    long lastId=transaction.get(targetDoc).getLong(K_LATID);

                    for(int i=0;i<dataList.size();i++){
                        ++lastId;
                        Map<String,Object> dataSingle=dataList.get(i);

                        updates.get(i).setId(lastId);
                        newIds.add(lastId);

                        dataSingle.put(K_ID, lastId);

                        transaction.set(targetDoc.collection(COLL_SYNCED).document(""+(lastId)),dataSingle);
                        transaction.set(otherUpdateCollection.document(""+convid),survive);
                    }

                    transaction.update(targetDoc, K_LATID, lastId);
                }catch (NullPointerException e){
                    e.printStackTrace();
                    Log.e(S.Constants.LOG_ID, "syncPut: ",e);
                }catch (Exception e){
                    e.printStackTrace();
                    Log.e(S.Constants.LOG_ID, "syncPut: ",e);
                }
                return null;
            }).addOnSuccessListener(aVoid -> {
                mMainDataStore.onRazgosSent(oldIds,newIds);

                RazgoOnlineDataStore.this.mMainDataStore.markUpdates(updates,convid);
            });
        }
    }

    //Utitlity and helper methods in the class
    public static Map<String,Object> getDocumentSurvival(){
        Map<String, Object> survival=new HashMap<>();
        survival.put(K_SUPPORT,true);
        return survival;
    }
    private Map<String,Object> getNewConv(String other){
        Map<String,Object> newConv=new HashMap<>();

        newConv.put(K_LATID,0);
        newConv.put(K_ONE_ID,ApplicationGlobals.SELF_ID);
        newConv.put(K_TWO_ID,other);

        return newConv;
    }
    //TODO Consider moving these methods to Entity classes
    private Map<String,Object> mapEntityToMap(RazgoEntity entity){
        Map<String,Object> data=new HashMap<>();

        data.put(K_ID, entity.getId());
        data.put(K_K, entity.getK());
        data.put(K_CONTENT, entity.getContent());
        data.put(K_DT, entity.getDatetime());
        data.put(K_SENDER, entity.getSender());
        data.put(K_SYNCED, false);

        return data;
    }
    private List<Map<String,Object>> mapEntitiesToMaps(List<RazgoEntity> entityList){
        List<Map<String, Object>> res=new ArrayList<>();

        for(RazgoEntity entity: entityList){
            res.add(mapEntityToMap(entity));
        }
        return res;
    }
    private RazgoEntity mapSnapshotToEntity(DocumentSnapshot snapshot) {
        if (snapshot == null) return null;

        RazgoEntity entity = new RazgoEntity();

        entity.setId(snapshot.getLong(K_ID));
        entity.setContent(snapshot.getString(K_CONTENT));
        entity.setK(snapshot.getLong(K_K).intValue());
        entity.setDatetime(snapshot.getString(K_DT));
        entity.setSender(snapshot.getString(K_SENDER));

        return entity;
    }

    public void setParentDS(OnlineMainListener mainListener){
        this.mMainDataStore =mainListener;
    }

    public interface OnlineMainListener {
        void addRazgosToOfflineDB(List<RazgoEntity> entities, long convid);
        void addConversation(ConvEntity entity);
        void addRazgosToMain(List<RazgoEntity> razgoEntities, long convid);

        void onConvReceived(ConvEntity convEntity);
        void onConvReceived(ConvEntity convEntity, boolean completed); //TODO: Consider keeping this as the only method(make the boolean parameter mandatory)
        void onRazgoReceived(RazgoEntity razgoEntity);
        void onRazgosSent(List<Long> oldIds, List<Long> newIds);

        void markUpdates(List<RazgoEntity> entities, long convid);

        String getPartnerId(long convid);
        String getUserId();
        List<Long> getUpdateIds();
        List<RazgoEntity> getUpdateRazgos(long id);
        long getLastRazgoId(long convid);
    }
}

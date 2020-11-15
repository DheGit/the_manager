package com.infinitydheer.themanager.domain.listener;

import com.infinitydheer.themanager.domain.data.RazgoDomain;

public interface RazgoListener {
    void onRazgoRecieved(RazgoDomain razgoDomain);//Update ConvScreen here too
    void onRazgoSent(long oldId, long newId);
    void onRazgoReadByPartner(long razgoId);//Think if it is good for the limits we got

}

package com.infinitydheer.themanager.presentation.model.mapper;

import com.infinitydheer.themanager.domain.data.ConvDomain;
import com.infinitydheer.themanager.domain.crypt.Nkrpt;
import com.infinitydheer.themanager.presentation.model.ConvModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to map the presentation layer data objects to the domain layer and vice versa
 */
public class ConvModelDataMapper {
    public ConvModelDataMapper(){}

    public ConvModel transformToModel(ConvDomain convDomain){
        ConvModel res=new ConvModel();
        ConvDomain domain= Nkrpt.unprocess(convDomain);
        res.setConvid(domain.getConvid());
        res.setPartnerName(domain.getPartnerName());
        res.setLastTime(domain.getLastTime());
        res.setLastMsg(domain.getLastMsg());
        res.setLastSender(domain.getLastSender());
        return res;
    }

    public List<ConvModel> tranformToModel(List<ConvDomain> convDomains){
        List<ConvModel> res=new ArrayList<>();
        for(ConvDomain convDomain: convDomains){
            ConvModel sres=transformToModel(convDomain);
            if(sres!=null) res.add(sres);
        }
        return res;
    }
}

package com.infinitydheer.themanager.data.entity.mapper;

import com.infinitydheer.themanager.data.entity.ConvEntity;
import com.infinitydheer.themanager.domain.data.ConvDomain;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to conveniently convert from Data Layer Object to Domain Layer Data Object
 */
public class ConvEntityDataMapper {
    public ConvEntityDataMapper(){}

    public ConvDomain transform(ConvEntity entity){
        ConvDomain result=new ConvDomain();
        result.setConvid(entity.getConvid());
        result.setPartnerName(entity.getPartnerName());
        result.setLastTime(entity.getLastTime());
        result.setLastMsg(entity.getLastMsg());
        result.setK(entity.getK());
        result.setLastSender(entity.getLastSender());
        return result;
    }

    public List<ConvDomain> transform(List<ConvEntity> entities){
        List<ConvDomain> res=new ArrayList<>();
        for(ConvEntity entity: entities){
            ConvDomain domain=transform(entity);
            if(domain!=null) res.add(domain);
        }
        return res;
    }
}

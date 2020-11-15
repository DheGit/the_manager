package com.infinitydheer.themanager.data.entity.mapper;

import com.infinitydheer.themanager.data.entity.RazgoEntity;
import com.infinitydheer.themanager.domain.data.RazgoDomain;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to conveniently convert from Data Layer Object to Domain Layer Data Object
 */
public class RazgoEntityDataMapper {
    public RazgoEntityDataMapper(){}

    public RazgoDomain transform(RazgoEntity inp){
        RazgoDomain oup=new RazgoDomain();
        oup.setId(inp.getId());
        oup.setDatetime(inp.getDatetime());
        oup.setSender(inp.getSender());
        oup.setK(inp.getK());
        oup.setContent(inp.getContent());
        oup.setSent(inp.isSent());
        return oup;
    }

    public List<RazgoDomain> transform(List<RazgoEntity> entities){
        List<RazgoDomain> res=new ArrayList<>();
        for(RazgoEntity entity: entities){
            RazgoDomain sres=transform(entity);
            if(sres!=null) res.add(sres);
        }
        return res;
    }

    public RazgoEntity transformToEntity(RazgoDomain inp){
        RazgoEntity oup=new RazgoEntity();
        oup.setId(inp.getId());
        oup.setDatetime(inp.getDatetime());
        oup.setSender(inp.getSender());
        oup.setK(inp.getK());
        oup.setContent(inp.getContent());
        oup.setSent(inp.isSent());
        return oup;
    }

    public List<RazgoEntity> transformToEntity(List<RazgoDomain> domains){
        List<RazgoEntity> res=new ArrayList<>();
        for(RazgoDomain domain: domains){
            RazgoEntity sres=transformToEntity(domain);
            if(sres!=null)res.add(sres);
        }
        return res;
    }
}

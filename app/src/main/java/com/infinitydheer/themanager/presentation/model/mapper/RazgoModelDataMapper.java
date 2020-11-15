package com.infinitydheer.themanager.presentation.model.mapper;

import com.infinitydheer.themanager.domain.data.RazgoDomain;
import com.infinitydheer.themanager.domain.crypt.Nkrpt;
import com.infinitydheer.themanager.presentation.model.RazgoModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to map the presentation layer data objects to the domain layer and vice versa
 */
public class RazgoModelDataMapper {
    public RazgoModelDataMapper(){}

    public RazgoDomain transform(RazgoModel model){
        RazgoDomain sres=new RazgoDomain();
        sres.setContent(model.getContent());
        sres.setId(model.getId());
        sres.setDatetime(model.getDatetime());
        sres.setSender(model.getSender());
        sres.setSent(model.isSent());
        return Nkrpt.process(sres);
    }

    public List<RazgoDomain> transform(List<RazgoModel> modelList){
        List<RazgoDomain> res=new ArrayList<>();
        for(RazgoModel model: modelList){
            RazgoDomain sres=transform(model);
            if(sres!=null) res.add(sres);
        }
        return res;
    }

    public RazgoModel transformToModel(RazgoDomain unDomain){
        RazgoDomain domain=Nkrpt.unprocess(unDomain);
        RazgoModel model=new RazgoModel();
        model.setContent(domain.getContent());
        model.setSender(domain.getSender());
        model.setId(domain.getId());
        model.setDatetime(domain.getDatetime());
        model.setSent(unDomain.isSent());
        return model;
    }

    public List<RazgoModel> transformToModel(List<RazgoDomain> domainList){
        List<RazgoModel> res=new ArrayList<>();
        for(RazgoDomain domain: domainList){
            RazgoModel sres=transformToModel(domain);
            if(sres!=null) res.add(sres);
        }
        return res;
    }
}

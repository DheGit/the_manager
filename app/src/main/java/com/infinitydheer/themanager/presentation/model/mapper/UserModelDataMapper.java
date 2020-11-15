package com.infinitydheer.themanager.presentation.model.mapper;

import com.infinitydheer.themanager.domain.data.UserDomain;
import com.infinitydheer.themanager.presentation.model.UserModel;

import java.util.ArrayList;
import java.util.List;

public class UserModelDataMapper {
    public UserModelDataMapper(){}

    public UserModel transformToModel(UserDomain inp){
        return new UserModel(inp.getUserId(),inp.getUserPass());
    }

    public List<UserModel> transformToModel(List<UserDomain> inp){
        List<UserModel> oup=new ArrayList<>();

        for(UserDomain domain: inp){
            UserModel model=transformToModel(domain);

            if(model!=null){
                oup.add(model);
            }
        }

        return oup;
    }
}

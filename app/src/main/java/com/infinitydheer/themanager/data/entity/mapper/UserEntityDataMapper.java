package com.infinitydheer.themanager.data.entity.mapper;

import com.infinitydheer.themanager.data.entity.UserEntity;
import com.infinitydheer.themanager.domain.crypt.Nkrpt;
import com.infinitydheer.themanager.domain.data.UserDomain;

import java.util.ArrayList;
import java.util.List;

public class UserEntityDataMapper {
    public UserEntityDataMapper(){}

    public UserDomain transform(UserEntity entity){
        UserDomain goodUser=new UserDomain(entity.getUserId(),
                entity.getUserPass());
        return Nkrpt.unprocess(goodUser);
    }

    public List<UserDomain> transform(List<UserEntity> entityList){
        List<UserDomain> oup=new ArrayList<>();
        for(UserEntity entity:entityList){
            UserDomain userDomain = transform(entity);

            if(userDomain!=null){
                oup.add(userDomain);
            }
        }
        return oup;
    }
}

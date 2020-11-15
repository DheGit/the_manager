package com.infinitydheer.themanager.data.entity.mapper;

import com.infinitydheer.themanager.data.entity.TaskEntity;
import com.infinitydheer.themanager.domain.data.TaskDomain;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to conveniently convert from Data Layer Object to Domain Layer Data Object
 */
public class TaskDataEntityMapper {
    public TaskDataEntityMapper(){}

    public TaskDomain transform(TaskEntity t){
        TaskDomain res=new TaskDomain();
        res.setId(t.getId());
        res.setDone(t.isDone());
        res.setDueDate(t.getDueDate());
        res.setName(t.getName());
        res.setNote(t.getNote());
        res.setQuad(t.getQuad());
        return res;
    }

    public List<TaskDomain> transform(List<TaskEntity> ts){
        List<TaskDomain> res=new ArrayList<>();
        for(TaskEntity t: ts){
            TaskDomain sres=transform(t);
            if(sres!=null){
                res.add(sres);
            }
        }
        return res;
    }

    public TaskEntity transformToEntity(TaskDomain t){
        TaskEntity res=new TaskEntity();
        res.setId(t.getId());
        res.setDone(t.isDone());
        res.setDueDate(t.getDueDate());
        res.setName(t.getName());
        res.setNote(t.getNote());
        res.setQuad(t.getQuad());
        return res;
    }

    public List<TaskEntity> tranformToEntity(List<TaskDomain> ts){
        List<TaskEntity> res=new ArrayList<>();
        for(TaskDomain t: ts){
            TaskEntity sres=transformToEntity(t);
            if(sres!=null){
                res.add(sres);
            }
        }
        return res;
    }
}

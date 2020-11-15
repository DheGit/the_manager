package com.infinitydheer.themanager.presentation.model.mapper;

import com.infinitydheer.themanager.domain.data.TaskDomain;
import com.infinitydheer.themanager.domain.utils.CalendarUtils;
import com.infinitydheer.themanager.presentation.model.TaskModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to map the presentation layer data objects to the domain layer and vice versa
 */
public class TaskModelDataMapper {
    public TaskModelDataMapper(){}

    public TaskDomain transform(TaskModel model){
        TaskDomain res=new TaskDomain();
        res.setQuad(model.getQuad());
        res.setNote(model.getNote());
        res.setName(model.getName());
        res.setDueDate(CalendarUtils.convertToString(model.getDate(),false));
        res.setDone(model.isDone());
        res.setId(model.getId());
        return res;
    }

    public List<TaskDomain> transform(List<TaskModel> models){
        List<TaskDomain> res=new ArrayList<>();
        for(TaskModel model: models){
            TaskDomain sres=transform(model);
            if(sres!=null) res.add(sres);
        }
        return res;
    }

    public TaskModel transformToModel(TaskDomain domain){
        TaskModel res=new TaskModel();
        res.setDate(CalendarUtils.convertToCalendar(domain.getDueDate(),false));
        res.setQuad(domain.getQuad());
        res.setNote(domain.getNote());
        res.setName(domain.getName());
        res.setId(domain.getId());
        res.setDone(domain.isDone());
        return res;
    }

    public List<TaskModel> transformToModel(List<TaskDomain> domains){
        List<TaskModel> res=new ArrayList<>();
        for(TaskDomain domain: domains){
            TaskModel sres=transformToModel(domain);
            if(sres!=null) res.add(sres);
        }
        return res;
    }
}

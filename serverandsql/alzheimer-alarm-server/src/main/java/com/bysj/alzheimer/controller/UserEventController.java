package com.bysj.alzheimer.controller;


import com.bysj.alzheimer.entity.UserEvent;
import com.bysj.alzheimer.service.IUserEventService;
import com.bysj.alzheimer.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author bysj
 * @since 2020-04-21
 */
@RestController
@RequestMapping("/alzheimer/user-event")
public class UserEventController {
    @Autowired
    private IUserEventService userEventService;

    @GetMapping
    public List<UserEvent> getData(){
        return userEventService.list();
    }

    @GetMapping("{eventId}")
    public UserEvent getData(@PathVariable String eventId){
        return userEventService.getById(eventId);
    }
    @PostMapping("{eventId}")
    public void delData(@PathVariable String eventId){
        userEventService.removeById(eventId);
    }
    @PostMapping
    public Integer insertData(UserEvent userEvent){
        userEvent.setCreatetime(LocalDateTime.now());
        if(userEvent.getId()!=null){
             userEventService.updateById(userEvent);
             return userEvent.getId();
        }else{
            return userEventService.saveUserEvent(userEvent).getId();
        }

    }

}


package com.bysj.alzheimer.service.impl;

import com.bysj.alzheimer.entity.UserEvent;
import com.bysj.alzheimer.mapper.UserEventMapper;
import com.bysj.alzheimer.service.IUserEventService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author bysj
 * @since 2020-04-21
 */
@Service
public class UserEventServiceImpl extends ServiceImpl<UserEventMapper, UserEvent> implements IUserEventService {

    @Override
    public UserEvent saveUserEvent(UserEvent userEvent) {
        save(userEvent);
        return userEvent;
    }
}

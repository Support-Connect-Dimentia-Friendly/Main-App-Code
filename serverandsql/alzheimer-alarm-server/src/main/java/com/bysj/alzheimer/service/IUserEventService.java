package com.bysj.alzheimer.service;

import com.bysj.alzheimer.entity.UserEvent;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author bysj
 * @since 2020-04-21
 */
public interface IUserEventService extends IService<UserEvent> {

    UserEvent saveUserEvent(UserEvent userEvent);
}

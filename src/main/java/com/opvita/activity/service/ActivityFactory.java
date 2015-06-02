package com.opvita.activity.service;

import com.opvita.activity.service.impl.ActivityServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Created by rd on 2015/5/29.
 */
@Component
public class ActivityFactory {
    private static ActivityServiceImpl localActivityService;

    public ActivityService newInstance(String issuerId) {
        if (StringUtils.isEmpty(issuerId)) {
            throw new NullPointerException("issuerId should not be null");

        } else {
            // 查询本地规则
            return ActivityFactory.localActivityService;
        }
    }

    @Autowired @Qualifier("localActivityService")
    public void setLocalActivityService(ActivityServiceImpl localActivityService) {
        ActivityFactory.localActivityService = localActivityService;
    }
}

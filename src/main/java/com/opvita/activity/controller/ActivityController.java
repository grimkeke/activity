package com.opvita.activity.controller;

import com.opvita.activity.model.Activity;
import com.opvita.activity.model.Rule;
import com.opvita.activity.utils.ListUtils;
import com.opvita.activity.daowrapper.ActivityDAO;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by rd on 2015/5/12.
 */
@Controller
@RequestMapping("/activity")
public class ActivityController {
    private static Log log = LogFactory.getLog(ActivityController.class);

    @Autowired private ActivityDAO activityDAO;

    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public String dashboard(HttpServletRequest request, ModelMap modelMap) {
        String issuerId = request.getParameter("issuerId");
        if (StringUtils.isEmpty(issuerId)) {
            modelMap.addAttribute("msg", "issuerId should not be null!");
        } else {

            List<Activity> activityList = activityDAO.getActivities(issuerId);
            log.info("activityList:" + activityList);
            modelMap.addAttribute("activityList", activityList);

            if (ListUtils.isEmpty(activityList)) {
                modelMap.addAttribute("msg", "no activities for " + issuerId);
            }
        }
        return "/activity/addActivity";
    }

    @RequestMapping(value = "/addActivity", method = RequestMethod.POST)
    @ResponseBody
    public Activity addActivity(HttpServletRequest request, Activity activity) {
        Activity savedActivity = activityDAO.saveActivityOnly(activity);
        return savedActivity;
    }

    @RequestMapping(value = "/attachRule", method = RequestMethod.POST)
    @ResponseBody
    public Activity attachRule(HttpServletRequest request, Rule rule) {
        String activityId = request.getParameter("activityId");
        Activity attachedActivity = activityDAO.attachRule(activityId, rule);
        return attachedActivity;
    }

    @RequestMapping(value = "/submitActivity", method = RequestMethod.POST)
    @ResponseBody
    public String submitActivity(HttpServletRequest request) {
        String issuerId = request.getParameter("issuerId");
        activityDAO.syncActivities(issuerId);
        return "{}";
    }
}

package com.deyuan.controller;

import com.deyuan.pojo.SysLog;
import com.deyuan.service.ISyslogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/sysLog")
public class SysLogController {

    @Autowired
    private ISyslogService  syslogService;

    @RequestMapping("/findAll")
    public ModelAndView   findAll(){
        List<SysLog>  list = syslogService.findAll();
        ModelAndView  mv = new ModelAndView();
        mv.addObject("sysLogs",list);
        mv.setViewName("syslog-list");
        return mv;
    }
}

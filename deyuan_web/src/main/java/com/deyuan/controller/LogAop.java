package com.deyuan.controller;

import com.deyuan.pojo.SysLog;
import com.deyuan.service.ISyslogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

@Component
@Aspect
public class LogAop {

    private Date visitTime;//开始时间
    private Class claszz;
    private Method method;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ISyslogService syslogService;



    //前置通知
    @Before("execution(* com.deyuan.controller.*.*(..))")
    public void doBefor(JoinPoint jp) throws NoSuchMethodException {
        visitTime = new Date();
        //具体访问的类
        claszz = jp.getTarget().getClass();
        //获取执行的方法名称
        String methodName = jp.getSignature().getName();
        //获取访问的参数
        Object[]args = jp.getArgs();
        if (args==null || args.length==0){
            method = claszz.getMethod(methodName);//获取到无参的方法
        }else{
            Class[] classArgs = new Class[args.length];
            for (int i= 0 ;i<args.length;i++) {
                classArgs[i] = args[i].getClass();
            }
            //封装参数
            claszz.getMethod(methodName,classArgs);
        }

    }


    //后置通知
    @After("execution(* com.deyuan.controller.*.*(..))")
    public void doAfter(){
           long time =  new Date().getTime()-visitTime.getTime();//访问的时长
        //获取操作的URL 通过Java的反射的方式获取
        String url = "";
        if (claszz!= null && method!=null && claszz!=LogAop.class){
            //获取类中的注解
            RequestMapping classAnnotation = (RequestMapping) claszz.getAnnotation(RequestMapping.class);
            String[] classValue = classAnnotation.value();
            //获取方法上的注解
            RequestMapping methodAnnotation = method.getAnnotation(RequestMapping.class);
            //取出vlue值
            String[] methodValue = methodAnnotation.value();
            url = classValue[0]+methodValue[0];
            //获取地址
            String ip =  request.getRemoteAddr();
            //获取当前操作地址
            SecurityContext context = SecurityContextHolder.getContext();
            //获取当前的操作用户对象
            User principal = (User) context.getAuthentication().getPrincipal();
            //获取用户名
            String userName = principal.getUsername();
            SysLog sysLog = new SysLog();
            sysLog.setIp(ip);
            sysLog.setExecutionTime(time);
            sysLog.setMethod("[类名] "+claszz.getName() +"[方法名] "+method.getName());
            sysLog.setUrl(url);
            sysLog.setUsername(userName);
            sysLog.setVisitTime(visitTime);
            syslogService.save(sysLog);


        }

    }
}

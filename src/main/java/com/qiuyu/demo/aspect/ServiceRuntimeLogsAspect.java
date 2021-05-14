package com.qiuyu.demo.aspect;

import com.qiuyu.demo.annotations.ServiceRuntimeLogs;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description:
 * @Author: qiuyu
 * @Date: 2021/3/19
 **/
@Aspect
public class ServiceRuntimeLogsAspect {

    private final Logger log = LoggerFactory.getLogger(ServiceRuntimeLogsAspect.class);


    /**
    * @Description: 定义一个只生效service中注解切点
    * @Author: qy
    * @Date: 2021/3/19 14:58
    * @return: void
    **/
    @Pointcut("within(com.qiuyu.demo.service..*) " +
            "&& @annotation(com.qiuyu.demo.annotations.ServiceRuntimeLogs)")
    public void pointCut(){ }

    /**
     *  环绕通知
     */
    @Around(value="pointCut()  && @annotation(serviceRuntimeLogs) " )
    public Object around(ProceedingJoinPoint joinPoint, ServiceRuntimeLogs serviceRuntimeLogs)  throws  Throwable{

        // 在方法前添加日志信息
        long start = System.currentTimeMillis();
        Object object  = joinPoint.proceed();  // 方法执行
        long end = System.currentTimeMillis() - start;
        int ms = (int) (end % 1000);
        int ss = (int) ((end / 1000) % 60);
        int mi = (int) (end / 1000 / 60);
        log.info("类:{}，方法:{}(),执行耗时：{}分，{}秒,{}毫秒",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                mi,ss,ms);

        return object;
    }




}

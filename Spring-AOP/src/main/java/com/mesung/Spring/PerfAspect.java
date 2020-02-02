package com.mesung.Spring;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class PerfAspect {

    //Advice가 적용되는 대상(createEvent, publishEvent 메소드 자체라고 생각하면 됨)
    //즉, 메소드를 감싸고 있다고 생각하면 됨.
    //타겟의 메소드를 호출하고 결과값을 리턴
    //@Around("execution(* com.mesung..*.EventService.*(..))") //EventService 안에 있는 모든 메소드에 logPerf() 기능을 적용해라
    @Around("@annotation(PerfLogging)")
    public Object logPerf(ProceedingJoinPoint pjp) throws Throwable {
        long begin = System.currentTimeMillis();
        Object retVal = pjp.proceed();  //해당 메소드
        System.out.println(System.currentTimeMillis() - begin);
        return retVal;
    }

    @Before("bean(simpleEventService)")
    public void hello() {
        System.out.println("hello");
    }
}

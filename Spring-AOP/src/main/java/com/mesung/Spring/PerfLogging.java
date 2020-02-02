package com.mesung.Spring;

import java.lang.annotation.*;

/*
* 메소드 성능 시간을 측정한다.
* */

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface PerfLogging {
}

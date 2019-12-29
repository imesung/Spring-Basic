package me.springKey.demo;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component @Scope(value="prototype")
public class Prototype {
}
/*
@Component @Scope(value="prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Prototype {
}
*/
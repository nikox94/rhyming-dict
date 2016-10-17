package com.serene.kid.dict.aspects;

import java.util.logging.Level;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.java.Log;

@Aspect
@Component
@Log
public class JedisAspect {

	@Around("@annotation(com.serene.kid.dict.annotations.UsesJedis)")
	public Object initJedis(final ProceedingJoinPoint pjp) throws Throwable {
		log.log(Level.FINEST, "Entering a method that uses Jedis");
		final Object ret = pjp.proceed();
		log.log(Level.FINEST, "Exiting a method that uses Jedis");
		return ret;
	}

}

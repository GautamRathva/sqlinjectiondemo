package com.sqlinjection.sqlinjectiondemo.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sqlinjection.sqlinjectiondemo.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public class SqlinjectionSafeAspect {

    @Aspect
    @Configuration
    public class SQLInjectionSafeAspect {
        private Logger logger = LogManager.getLogger(this.getClass());

        //Compulsory follow this pattern com.anantaservice.anypackagename.controller
        @Pointcut("execution(* com.sqlinjection.*.controller.*.*(..))")
        public void sqlInjectionSafeAspect() {
        }

        @Before("sqlInjectionSafeAspect()")
        public void doStuffBeforeThing(JoinPoint joinPoint) throws ResourceNotFoundException, JsonProcessingException {

            if (joinPoint.getArgs() != null) {
                for (Object obj : joinPoint.getArgs()) {
                    if (!(obj instanceof MultipartFile) && !(obj instanceof LocalDate) && !(obj instanceof HttpServletResponse)) {
                        ObjectMapper objectMapper = new ObjectMapper();
                        objectMapper.registerModule(new JavaTimeModule());
                        objectMapper.registerModule(new Jdk8Module());
                        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
                        String string = objectMapper.writeValueAsString(obj);

                        if (obj != null && !SqlSafeUtil.isSqlInjectionSafe(string)) {
                            logger.error("SQL Injection Attack Occurred For Get/Post Request--->value-->{}", obj.toString());
                            throw new ResourceNotFoundException("SQL Injection Attack Occurred For Get/Post Request");
                        }
                    } else {
                        continue;
                    }
                }

            }

        }

    }
}

package com.myfinance.financetracker.aspect;

import com.myfinance.financetracker.service.impl.VisitCounterServiceImpl;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;

@Aspect
@Component
public class VisitCounterAspect {

    @Autowired
    private VisitCounterServiceImpl visitCounterService;

    @Autowired
    private HttpServletRequest request;

    @Before("execution(* com.myfinance.financetracker.controller..*.*(..))")
    public void countVisit() {
        String url = request.getRequestURI();
        visitCounterService.incrementVisitCount(url);
    }
}
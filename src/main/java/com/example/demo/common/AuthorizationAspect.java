package com.example.demo.common;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpSession;

@Aspect
@Component
public class AuthorizationAspect {

    @Before("@annotation(com.example.demo.common.RequiredRole)")
    public void checkAuthorization(JoinPoint joinPoint) throws Throwable {
        // Ambil HttpSession dari request
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new SecurityException("Tidak dapat mengakses session");
        }
        
        HttpSession session = attributes.getRequest().getSession(false);
        
        // 1. Periksa apakah user sudah login (memeriksa atribut "username")
        if (session == null || session.getAttribute("username") == null) {
            throw new SecurityException("Anda belum login. Silakan login terlebih dahulu.");
        }
        
        // Ambil role dari session
        String userRole = (String) session.getAttribute("role");
        
        // Ambil anotasi RequiredRole dari method yang dipanggil
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RequiredRole requiredRole = method.getAnnotation(RequiredRole.class);
        
        if (requiredRole == null) {
            return;
        }
        
        String[] allowedRoles = requiredRole.value();
        
        // 2. Periksa apakah terdapat role "*" (semua role diperbolehkan)
        if (Arrays.asList(allowedRoles).contains("*")) {
            return; // Semua role diperbolehkan
        }
        
        // 3. Periksa apakah role user ada dalam array allowedRoles
        if (userRole == null || !Arrays.asList(allowedRoles).contains(userRole)) {
            throw new SecurityException("Anda tidak memiliki akses ke resource ini. Role yang diperlukan: " + Arrays.toString(allowedRoles));
        }
    }
}

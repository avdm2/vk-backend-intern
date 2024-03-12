package com.vk;

import com.vk.utils.CachedHttpServletRequest;
import io.micrometer.core.instrument.util.IOUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
public class LoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws IOException {
        CachedHttpServletRequest cachedHttpServletRequest = new CachedHttpServletRequest(request);

        String username = cachedHttpServletRequest.getUserPrincipal() == null ? "anonymous" : cachedHttpServletRequest.getUserPrincipal().getName();
        LocalDateTime time = LocalDateTime.now();
        String internalRequest = cachedHttpServletRequest.getMethod() + " " + cachedHttpServletRequest.getRequestURI();
        String role = SecurityContextHolder.getContext().getAuthentication() == null
                ? "anonymous"
                : SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
        String requestBody = IOUtils.toString(cachedHttpServletRequest.getInputStream(), StandardCharsets.UTF_8);
        log.info("Received request: username={}; time={}; internalRequest={}; role={}; requestBody={}",
                username,
                time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                internalRequest, role, requestBody);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler, Exception ex) {
        log.info("Response status: " + response.getStatus());
    }
}

package com.vk.components;

import com.vk.entities.Log;
import com.vk.repositories.LogRepository;
import com.vk.utils.CachedHttpServletRequest;
import io.micrometer.core.instrument.util.IOUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
@WebFilter(filterName = "LoggerFilter", urlPatterns = "/*")
public class LoggerFilter extends OncePerRequestFilter {

    private final LogRepository logRepository;

    public LoggerFilter(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        CachedHttpServletRequest cachedHttpServletRequest = new CachedHttpServletRequest(request);

        String username = request.getUserPrincipal() == null ? "anonymous" : request.getUserPrincipal().getName();
        LocalDateTime time = LocalDateTime.now();
        String internalRequest = request.getMethod() + " " + request.getRequestURI();
        String role = SecurityContextHolder.getContext().getAuthentication() == null
                ? "anonymous"
                : SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
        String requestBody = IOUtils.toString(cachedHttpServletRequest.getInputStream(), StandardCharsets.UTF_8);

        log.info("Received request: username={}; time={}; internalRequest={}; role={}; requestBody={};",
                username,
                time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                internalRequest, role, requestBody);

        logRepository.save(new Log()
                .setUsername(username)
                .setTime(time)
                .setInternalRequest(internalRequest)
                .setRole(role)
                .setRequestBody(requestBody));

        filterChain.doFilter(cachedHttpServletRequest, response);
    }
}

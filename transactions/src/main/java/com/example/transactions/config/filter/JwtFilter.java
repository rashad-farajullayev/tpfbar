package com.example.transactions.config.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public abstract class JwtFilter extends OncePerRequestFilter {
}


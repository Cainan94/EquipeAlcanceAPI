package com.cbt.EquipeAlcance.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class Security {
    public static boolean isADM() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ((UserDetails) principal).getAuthorities().stream().anyMatch(filter-> filter.getAuthority().equalsIgnoreCase("admin"));
    }
}

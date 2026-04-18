package com.eventmesh.ingestion.auth.utils;

import com.eventmesh.ingestion.auth.exception.CustomAuthException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@NoArgsConstructor
public class AutorizationUtil {

    /*Allow only ADMIN*/

    public static void requireAdmin(HttpServletRequest request) {
        String role = extractRole(request);

        if (!"ADMIN".equals(role)) {
            throw new CustomAuthException("ADMIN access required");
        }
    }

    public static void requireAdminOrViewer(HttpServletRequest request) {
        String role = extractRole(request);

        if (!"ADMIN".equals(role) && !"VIEWER".equals(role)) {
            throw new CustomAuthException("ADMIN or VIEWER access required");
        }
    }

    public static void requireClientOrAdmin(HttpServletRequest request) {
        String role = extractRole(request);

        if (!"CLIENT".equals(role) && !"ADMIN".equals(role)) {
            throw new CustomAuthException("CLIENT or ADMIN access required");
        }
    }

    private static String extractRole(HttpServletRequest request) {

        Object roleObj = request.getAttribute("role");

        if (roleObj == null) {
            throw new CustomAuthException("Unauthorized");
        }

        return roleObj.toString();
    }
}

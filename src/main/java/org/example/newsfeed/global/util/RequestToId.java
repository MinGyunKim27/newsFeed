package org.example.newsfeed.global.util;

import jakarta.servlet.http.HttpServletRequest;

public class RequestToId {
    public static Long requestToId(HttpServletRequest request,JwtProvider provider){
        return provider.getUserId(request.getHeader("Authorization").substring(7));
    }
}

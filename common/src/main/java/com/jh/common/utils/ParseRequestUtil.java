package com.jh.common.utils;

import jakarta.servlet.http.HttpServletRequest;

public class ParseRequestUtil {

    public int extractUserIdFromRequest(HttpServletRequest request) {
        String userIdHeader = request.getHeader("x-claim-userid");
        if (userIdHeader == null) {
            throw new RuntimeException("헤더에 사용자 ID가 없습니다.");
        }

        int userId;
        try {
            userId = Integer.parseInt(userIdHeader);
        } catch (NumberFormatException e) {
            throw new RuntimeException("헤더에 있는 사용자 ID 형식이 잘못되었습니다.");
        }

        return userId;
    }
}

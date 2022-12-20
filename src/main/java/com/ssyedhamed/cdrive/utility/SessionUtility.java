package com.ssyedhamed.cdrive.utility;

import org.apache.commons.logging.Log;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Component
public class SessionUtility {
	Log log;
	public void removeVerificationMessageFromSession() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            HttpSession session = request.getSession();
            System.out.println(session.getAttributeNames());
            session.removeAttribute("message");
        } catch (RuntimeException ex) {
            log.error("No Request: ", ex);
        }
    }
}

package org.romeole.data.controller;

import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author gongzhou
 * @title: BaseController
 * @projectName romeole
 * @description: TODO
 * @date 2020/8/2114:37
 */
public class BaseController {

    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected HttpSession session;

    @ModelAttribute
    public void httpInit(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        this.session = request.getSession();
//        String token = request.getHeader("token");
        /*if (StringUtils.isNotEmpty(token)) {
            if (TokenUtils.validToken(token)) {

            }
        }*/
    }
}

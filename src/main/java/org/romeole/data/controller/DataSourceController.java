package org.romeole.data.controller;

import lombok.extern.log4j.Log4j2;
import org.romeole.data.bean.dto.DataSourceReq;
import org.romeole.data.bean.vo.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author gongzhou
 * @title: DataSourceController
 * @projectName romeole
 * @description: TODO
 * @date 2020/8/2114:43
 */

@RestController
@RequestMapping("datasource")
@Log4j2
public class DataSourceController extends BaseController {

    @PostMapping("test_connect")
    public Result<?> testConnect(@RequestBody DataSourceReq req) {

        String url = "jdbc:mysql://".concat(req.getHost()).concat(":").concat(req.getPort()).concat("/")
                .concat(req.getSchema()).concat("?characterEncoding=UTF-8&useUnicode=true&useSSL=false");
        try {
            Connection connection = DriverManager.getConnection(url, req.getUsername(), req.getPassword());
            return Result.ok();
        } catch (SQLException throwables) {
            log.error("连接失败: {}, 原因: {}", throwables.getMessage(), throwables.getCause());
            return Result.error("连接失败!");
        }

    }


}

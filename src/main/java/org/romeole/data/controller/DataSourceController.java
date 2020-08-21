package org.romeole.data.controller;

import lombok.extern.log4j.Log4j2;
import org.romeole.data.bean.dto.DataSourceReq;
import org.romeole.data.bean.vo.Result;
import org.romeole.data.service.ConnectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


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

    private final ConnectService connectService;

    public DataSourceController(ConnectService connectService) {
        this.connectService = connectService;
    }

    @PostMapping("test_connect")
    public Result<?> testConnect(@RequestBody DataSourceReq req) {
        try {
            Connection connect = connectService.connect(req);
            List<String> tables = connectService.getTables();
            return Result.ok(tables);
        } catch (Exception e) {
            log.error("连接失败: {}", e.getMessage());
            return Result.error("连接失败!" + e.getMessage());
        }
    }



}

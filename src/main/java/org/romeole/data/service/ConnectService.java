package org.romeole.data.service;

import org.romeole.data.bean.dto.DataSourceReq;
import org.romeole.data.consts.DatasourceType;
import org.romeole.data.exception.MissingParamException;
import org.romeole.data.exception.UnsupportDBTypeException;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gongzhou
 * @title: ConnectService
 * @projectName romeole
 * @description: TODO
 * @date 2020/8/2116:15
 */
@Service
public class ConnectService {

    private static ThreadLocal<Connection> CONNECTIONS = new ThreadLocal<>();

    public Connection connect(DataSourceReq req) throws SQLException, MissingParamException, UnsupportDBTypeException {
        req.nullFieldValidate();
        Connection connection = CONNECTIONS.get();
        if (null != connection) return connection;
        DatasourceType type = req.getType();
        StringBuffer url = new StringBuffer();
        url.append("jdbc:");
        switch (type) {
            case MYSQL:
                url.append("mysql://").append(req.getHost())
                        .append(":").append(req.getPort())
                        .append("/").append(req.getSchema())
                        .append("?characterEncoding=UTF-8&useUnicode=true&useSSL=false&zeroDateTimeBehavior=convertToNull&serverTimezone=GMT%2B8");
                break;
            case ORACLE:
                url.append("oracle:thin:@").append(req.getHost())
                        .append(":").append(req.getPort())
                        .append(":").append(req.getSchema());
                break;
            default:
                throw new UnsupportDBTypeException("不支持该数据库类型");
        }
        connection = DriverManager.getConnection(url.toString(), req.getUsername(), req.getPassword());
        CONNECTIONS.set(connection);
        return connection;
    }

    public Connection getCurrentConnection() {
        return CONNECTIONS.get();
    }

    public List<String> getTables() throws SQLException {
        Connection conn = getCurrentConnection();
        conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        DatabaseMetaData databaseMetaData = conn.getMetaData();
        String schema = conn.getSchema();
        ResultSet tables = databaseMetaData.getTables(null, schema, "%", null);
        ArrayList<String> tablesList = new ArrayList<>();
        while (tables.next()) {
            tablesList.add(tables.getString("TABLE_NAME"));
        }
        return tablesList;
    }

    public List<String> getColNames(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int count = metaData.getColumnCount();
        List<String> colNameList = new ArrayList<>();
        for(int i = 1; i<=count; i++){
            colNameList.add(metaData.getColumnName(i));
        }
        System.out.println(colNameList);
//		rs.close();
        rs.first();
        return colNameList;
    }



}

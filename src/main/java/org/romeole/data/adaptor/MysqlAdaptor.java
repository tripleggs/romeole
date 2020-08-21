package org.romeole.data.adaptor;

import org.romeole.data.consts.DatasourceType;
import java.sql.Driver;

/**
 * @author gongzhou
 * @title: MysqlAdaptor
 * @projectName romeole
 * @description: TODO
 * @date 2020/8/2110:46
 */
public class MysqlAdaptor implements Adaptor {

    /**
     * 向适配器注册驱动
     *
     * @param driverName
     * @return
     */
    @Override
    public void register(String driverName) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载驱动
     *
     * @param driverName
     * @return
     */
    @Override
    public Driver getDriver(String driverName) {
        return null;
    }

    /**
     * 加载默认驱动
     *
     * @param type
     * @return
     */
    @Override
    public Driver getDriver(DatasourceType type) {
        return null;
    }

    /**
     * 获取当前驱动
     *
     * @return
     */
    @Override
    public Driver getDriver() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Driver driver;
        return null;
    }

}

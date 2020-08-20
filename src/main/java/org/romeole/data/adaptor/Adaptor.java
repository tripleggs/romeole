package org.romeole.data.adaptor;

import java.sql.Driver;

/**
 * @author gongzhou
 * @title: Adaptor
 * @projectName data_funnel
 * @description: TODO 数据源适配器
 * @date 2020/8/209:26
 */
public interface Adaptor {

    Driver register(String diverName);

    Driver getDriver(String driverName);

}

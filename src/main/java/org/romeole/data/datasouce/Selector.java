package org.romeole.data.datasouce;

import org.romeole.data.consts.DatasourceType;
import org.springframework.boot.CommandLineRunner;

import javax.sql.DataSource;

/**
 * @author gongzhou
 * @title: Selector
 * @projectName romeole
 * @description: TODO
 * @date 2020/8/2110:40
 */
public  class Selector implements CommandLineRunner {

    DataSource chose(DatasourceType type) {
        return null;
    }

    @Override
    public void run(String... args) throws Exception {

    }
}

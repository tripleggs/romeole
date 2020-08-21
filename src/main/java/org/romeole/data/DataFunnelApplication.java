package org.romeole.data;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class DataFunnelApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataFunnelApplication.class, args);
    }

}

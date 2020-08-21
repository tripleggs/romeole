package org.romeole.data.bean.dto;

import lombok.Data;
import org.romeole.data.annotation.NotNull;
import org.romeole.data.consts.DatasourceType;

/**
 * @author gongzhou
 * @title: DataSourceReq
 * @projectName romeole
 * @description: TODO
 * @date 2020/8/2115:18
 */
@Data
public class DataSourceReq extends BaseReq {

    @NotNull
    private String host;

    @NotNull
    private String port;

    private String username;

    private String password;

    private String schema;

    private String suffix;

    @NotNull
    private DatasourceType type;

}

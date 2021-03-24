package com.dog.cloud.core.base.charts;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 图表类型
 *
 * @author KING
 * @version V1.0
 * @date 2021/2/26
 **/
@Data
@ApiModel(value = "图表类型", description = "图表类型")
public class ChartsSeriesVO implements Serializable {
    /**
     * 系列名称
     */
    @ApiModelProperty(value = "系列名称")
    private String name;

    /**
     * 图表类型
     */
    @ApiModelProperty(value = "图表类型")
    private String type;

    /**
     * 数据内容
     */
    @ApiModelProperty(value = "数据内容")
    private Object[] datas;

    /**
     * 数据内容
     */
    @ApiModelProperty(value = "数据内容")
    private List<ChartsDataEntity> dataList;
}

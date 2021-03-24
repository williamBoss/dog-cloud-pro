package com.dog.cloud.core.base.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Entity基类
 *
 * @author KING
 */
@Data
@ApiModel(value = "基础实体对象", description = "基础实体对象")
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = -5128107588199459953L;

    @TableLogic
    @ApiModelProperty(value = "删除标志（0代表存在 1代表删除）")
    @TableField(value = "del_flag", fill = FieldFill.INSERT)
    private Boolean delFlag;

    /**
     * 创建者
     */
    @ApiModelProperty(value = "创建人")
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新者
     */
    @ApiModelProperty(value = "更新人")
    @TableField(value = "update_by", fill = FieldFill.UPDATE)
    private String updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    @TableField(value = "remark")
    private String remark;

    /**
     * 搜索值
     */
    @ApiModelProperty(value = "搜索值")
    private transient String searchValue;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private transient String beginTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private transient String endTime;

    /**
     * 请求参数
     */
    @ApiModelProperty(value = "请求参数")
    private transient Map<String, Object> params;

    public Map<String, Object> getParams() {
        if (params == null) {
            params = new HashMap<>();
        }
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

}

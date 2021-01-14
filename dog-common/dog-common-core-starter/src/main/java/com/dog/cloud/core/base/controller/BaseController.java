package com.dog.cloud.core.base.controller;

import com.dog.cloud.core.base.page.PageDomain;
import com.dog.cloud.core.base.page.TableSupport;
import com.dog.cloud.core.utils.StringUtils;
import com.dog.cloud.core.utils.sql.SqlUtil;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;

/**
 * web层通用数据处理
 *
 * @author KING
 */
@Slf4j
public class BaseController {

    /**
     * 设置请求分页数据
     */
    protected void startPage() {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        if (StringUtils.isNotNull(pageNum) && StringUtils.isNotNull(pageSize)) {
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            PageHelper.startPage(pageNum, pageSize, orderBy);
        }
    }

}

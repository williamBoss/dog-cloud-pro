package com.dog.cloud.system.api.factory;

import com.dog.cloud.core.model.entity.system.SysOperLog;
import com.dog.cloud.core.result.BaseResult;
import com.dog.cloud.system.api.RemoteLogService;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 日志服务降级处理
 *
 * @author KING
 */
@Component
public class RemoteLogFallbackFactory implements FallbackFactory<RemoteLogService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteLogFallbackFactory.class);

    @Override
    public RemoteLogService create(Throwable throwable) {
        log.error("日志服务调用失败:{}", throwable.getMessage());
        return new RemoteLogService() {
            @Override
            public BaseResult<Boolean> saveLog(SysOperLog sysOperLog) {
                return null;
            }

            @Override
            public BaseResult<Boolean> saveLogininfor(String username, String status, String message) {
                return null;
            }
        };

    }
}

package com.dog.cloud.gateway.service.impl;

import com.dog.cloud.cache.utils.RedisUtils;
import com.dog.cloud.core.constant.Constants;
import com.dog.cloud.core.exception.CaptchaException;
import com.dog.cloud.core.result.BaseResult;
import com.dog.cloud.core.utils.IdUtils;
import com.dog.cloud.gateway.service.ValidateCodeService;
import com.google.code.kaptcha.Producer;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FastByteArrayOutputStream;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 验证码实现处理
 *
 * @author KING
 */
@Service
public class ValidateCodeServiceImpl implements ValidateCodeService {

    @Resource(name = "captchaProducer")
    private Producer captchaProducer;

    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;

    @Resource
    private RedisUtils redisUtils;

    /**
     * 验证码类型
     */
    @Value("${captcha.type:char}")
    private String captchaType;

    /**
     * 生成验证码
     */
    @Override
    public BaseResult<Map<String, Object>> createCapcha() throws IOException, CaptchaException {
        // 保存验证码信息
        String uuid = IdUtils.simpleUUID();
        String verifyKey = Constants.CAPTCHA_CODE_KEY + uuid;

        String capStr = null, code = null;
        BufferedImage image = null;

        // 生成验证码
        if ("math".equals(captchaType)) {
            String capText = captchaProducerMath.createText();
            capStr = capText.substring(0, capText.lastIndexOf("@"));
            code = capText.substring(capText.lastIndexOf("@") + 1);
            image = captchaProducerMath.createImage(capStr);
        } else if ("char".equals(captchaType)) {
            capStr = code = captchaProducer.createText();
            image = captchaProducer.createImage(capStr);
        }
        redisUtils.set(verifyKey, code, Constants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
        // 转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpg", os);
        } catch (IOException e) {
            return BaseResult.<Map<String, Object>>failure().message(e.getMessage());
        }
        BaseResult<Map<String, Object>> result = BaseResult.success();
        Map<String, Object> captchaMap = new HashMap<>();
        captchaMap.put("uuid", uuid);
        captchaMap.put("img", Base64.encode(os.toByteArray()));
        result.data(captchaMap);
        return result;
    }

    /**
     * 校验验证码
     */
    @Override
    public void checkCapcha(String code, String uuid) throws CaptchaException {
        if (StringUtils.isEmpty(code)) {
            throw new CaptchaException("验证码不能为空");
        }
        if (StringUtils.isEmpty(uuid)) {
            throw new CaptchaException("验证码已失效");
        }
        String verifyKey = Constants.CAPTCHA_CODE_KEY + uuid;
        String captcha = redisUtils.get(verifyKey);
        redisUtils.delete(verifyKey);

        if (!code.equalsIgnoreCase(captcha)) {
            throw new CaptchaException("验证码错误");
        }
    }

}

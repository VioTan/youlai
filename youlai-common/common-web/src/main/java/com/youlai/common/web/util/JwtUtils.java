package com.youlai.common.web.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.core.common.constant.AuthConstants;
import com.sun.org.apache.regexp.internal.RE;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

/**
 * jwt 工具类
 *
 * Header(头部)、Payload(负载)、Signature(签名)
 *
 * @author:GSHG
 * @date: 2021-08-26 10:07
 * description:
 */
@Slf4j
public class JwtUtils {

    /**
     *  从请求头中获取 JWT载体key JSON对象 - 除了默认的字段，还可以扩展自定义字段
     * @return
     */
    @SneakyThrows
    public static JSONObject getJwtPayload() {
        String payload = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest().getHeader(AuthConstants.JWT_PAYLOAD_KEY);
        JSONObject jsonObject = JSONUtil.parseObj(URLDecoder.decode(payload, "UTF-8"));
        return jsonObject;

    }

    /**
     * 解析JWT获取用户的id
     * @return
     */
    public static Long getUserId(){
        Long id = getJwtPayload().getLong(AuthConstants.USER_ID_KEY);
        return id;
    }

    /**
     * 解析JWT用户名
     * @return
     */
    public static String getUsername(){
        String username = getJwtPayload().getStr(AuthConstants.USER_NAME_KEY);
        return username;
    }

    /**
     * 获取登录认证的客户端ID
     *
     * 兼容两种方式获取Oauth2客户端信息(client_id,client_sercet)
     * 方式一：client_id,client_secret放在请求路径中
     * 方式二：放在请求头(Request Headers) 中的Authorization字段，且经过过加密，例如Basic Y2xpZW50OnNlY3JldA== 明文等于 client:secret
     *
     *
     * @return
     */
    @SneakyThrows
    public static String getOAuthClientId(){
        String clientId;
        //获取请求头
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();

       //从请求路径中获取 获取登录认证的客户端ID  client_id
        clientId = request.getParameter(AuthConstants.CLIENT_ID_KEY);
        if(StrUtil.isNotBlank(clientId)){
            return clientId;
        }

        //从请求头获取 获取登录认证的客户端ID  Authorization
        String basic = request.getHeader(AuthConstants.AUTHORIZATION_KEY);
        //判断是否有请求头含有 basic
        if(StrUtil.isNotBlank(basic) && basic.startsWith(AuthConstants.BASIC_PREFIX)){
            //替换请求keyCode
            basic = basic.replace(AuthConstants.BASIC_PREFIX, Strings.EMPTY);
            //解密
            String basicPlainText = new String(new BASE64Decoder().decodeBuffer(basic),"UTF-8");
            clientId = basicPlainText.split(":")[0];
        }
        return clientId;
    }

    public static List<String> getRoles(){
        List<String> roles = null;
        JSONObject payload = getJwtPayload();

        if(payload != null && payload.containsKey(AuthConstants.JWT_AUTHORITIES_KEY)){
            roles = payload.get(AuthConstants.JWT_AUTHORITIES_KEY,List.class);
        }
        return roles;
    }


}

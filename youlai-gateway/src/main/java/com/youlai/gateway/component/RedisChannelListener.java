package com.youlai.gateway.component;

import com.core.common.constant.GlobalConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.nio.charset.StandardCharsets;

/**
 * @author:GSHG
 * @date: 2021-09-14 15:34
 * description:
 */
public class RedisChannelListener implements MessageListener {

    @Autowired
    private UrlPermRolesLocalCache urlPermRolesLocalCache;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        String channel = new String(message.getChannel(),StandardCharsets.UTF_8);
        urlPermRolesLocalCache.remove(GlobalConstants.URL_PERM_ROLES_KEY);
    }
}

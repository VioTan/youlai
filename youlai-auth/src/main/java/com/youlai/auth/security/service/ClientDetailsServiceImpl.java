package com.youlai.auth.security.service;

import com.core.common.result.Result;
import com.youlai.admin.api.pojo.entity.SysOauthClient;
import com.youlai.admin.api.service.OAuthClientFeignClient;
import com.youlai.auth.common.enums.PasswordEncoderTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;

/**
 *获取ClientDetails
 *
 * @author:GSHG
 * @date: 2021-08-26 14:44
 * description:
 */
@Service
public class ClientDetailsServiceImpl implements ClientDetailsService {
            @Autowired
            private OAuthClientFeignClient oAuthClientFeignClient;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        try {
            //通过clientId获取 系统资源
            Result<SysOauthClient> result = oAuthClientFeignClient.getOAuthClientById(clientId);
            if(Result.success().getCode().equals(result.getCode())){
                SysOauthClient client = result.getData();
                BaseClientDetails clientDetails = new BaseClientDetails(
                        client.getClientId(),
                        client.getResourceIds(),
                        client.getScope(),
                        client.getAuthorizedGrantTypes(),
                        client.getAuthorities(),
                        client.getWebServerRedirectUri());
                clientDetails.setClientSecret(PasswordEncoderTypeEnum.NOOP.getPrefx() + client.getClientSecret());
                return clientDetails;
            }else {
                throw new NoSuchClientException("No client with requested id: " + clientId);
            }


        }catch(EmptyResultDataAccessException e){
            throw new NoSuchClientException("No client with requested id:" + clientId);
        }

    }


}

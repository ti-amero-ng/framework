package com.huang.framework.authority.auth2;

import com.huang.framework.authority.properties.OAuth2ClientProperties;
import com.huang.framework.authority.properties.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * 认证服务配置
 * @author -Huang
 * @create 2020-03-18 19:00
 */
@Configuration
@EnableAuthorizationServer
@Slf4j
public class OAuth2AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired(required = false)
    public TokenStore tokenStore;

    /**
     * 只有当使用jwt的时候才会有该对象
     */
    @Autowired(required = false)
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    @Autowired(required = false)
    private TokenEnhancer jwtTokenEnhancer;

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        // 允许表单登录
        security.allowFormAuthenticationForClients();
        security.passwordEncoder(bCryptPasswordEncoder);
    }

    /**
     * 客户端应用配置
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        InMemoryClientDetailsServiceBuilder builder = clients.inMemory();
        if(ArrayUtils.isNotEmpty(securityProperties.getOauth2().getClients())){
            for(OAuth2ClientProperties c : securityProperties.getOauth2().getClients()){
                builder
                        // 客户端 id
                        .withClient(c.getClientId())
                        // 客户端 secret
                        .secret(bCryptPasswordEncoder.encode(c.getClientSecret()))
                        //令牌有效期
                        .accessTokenValiditySeconds(c.getAccessTokenValiditySeconds())
                        //refresh_token有效期
                        .refreshTokenValiditySeconds(c.getRefreshTokenValiditySeconds())
                        .redirectUris(c.getRedirectUris())
                        //客户单权限
                        .scopes(c.getScopes())
                        // 授权模式
                        .authorizedGrantTypes(c.getAuthorizedGrantTypes());
            }
        }
    }

    /**
     * 配置入口点
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        //配置使用redis来存储
        endpoints
                .tokenStore(tokenStore)
                .authenticationManager(this.authenticationManager);
        if(null != userDetailsService){
            endpoints.userDetailsService(userDetailsService);
        }
        if (jwtAccessTokenConverter != null && jwtTokenEnhancer != null) {
            //生成增强器链
            TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
            List<TokenEnhancer> enhancers = new ArrayList<>();
            enhancers.add(jwtTokenEnhancer);
            enhancers.add(jwtAccessTokenConverter);
            enhancerChain.setTokenEnhancers(enhancers);
            // 一个处理链，先添加，再转换
            endpoints
                    .tokenEnhancer(enhancerChain)
                    .accessTokenConverter(jwtAccessTokenConverter);
        }
        endpoints.pathMapping("/oauth/confirm_access",securityProperties.getOauth2().getConfirmUrl());
    }

}

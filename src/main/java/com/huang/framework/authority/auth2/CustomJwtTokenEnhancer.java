package com.huang.framework.authority.auth2;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

/**
 * token增强器
 * 可覆盖
 * @author -Huang
 *
 */
public class CustomJwtTokenEnhancer implements TokenEnhancer {
	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		Map<String, Object> info = new HashMap<>();
		info.put("company", "-Huang");

		// 设置附加信息
		((DefaultOAuth2AccessToken)accessToken).setAdditionalInformation(info);

		return accessToken;
	}
}

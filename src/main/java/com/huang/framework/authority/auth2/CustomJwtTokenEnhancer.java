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
	private Map<String, Object> map;

	public CustomJwtTokenEnhancer(Map<String, Object> map){
		this.map = map;
	}

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		if(null == map){
			map = new HashMap<>(16);
			map.put("company", "-Huang");
		}

		// 设置附加信息
		((DefaultOAuth2AccessToken)accessToken).setAdditionalInformation(map);

		return accessToken;
	}
}

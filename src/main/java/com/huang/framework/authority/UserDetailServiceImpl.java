package com.huang.framework.authority;

import com.huang.framework.authority.entity.JwtUser;
import com.huang.framework.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @Author -Huang
 * @create 2019/9/4 10:38
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        if(!s.equals("123456")){
            throw new ServiceException("账户不存在");
        }
        return new JwtUser(s,bCryptPasswordEncoder.encode(s));
    }

}

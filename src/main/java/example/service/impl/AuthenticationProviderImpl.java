package example.service.impl;


import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

/**
 * ログインフォーム利用時の認証クラス
 */
@Component
public class AuthenticationProviderImpl implements AuthenticationProvider {

    /**
     * 入力された情報を基にユーザーの照合を実施
     */
    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {

        String username = auth.getName();
        String password = auth.getCredentials().toString();

        if (StringUtils.isAllEmpty(username, password)) {
            // 例外はSpringSecurityにあったものを使用
            throw new AuthenticationCredentialsNotFoundException("ログイン情報に不備があります。");
        }

        // ユーザー認証
        User loginUser = null;
        List<GrantedAuthority> grantedAuthorityList = Lists.newArrayList();
        if ("admin".equals(username) && "admin".equals(password)) {
            loginUser = new User("admin", "admin", Lists.newArrayList());
            grantedAuthorityList.add(new SimpleGrantedAuthority("ROLE_USER"));
            grantedAuthorityList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else if ("user".equals(username) && "user".equals(password)) {
            loginUser = new User("user", "user", Lists.newArrayList());
            grantedAuthorityList.add(new SimpleGrantedAuthority("ROLE_USER"));
        }

        if (loginUser == null) {
            // 例外はSpringSecurityにあったものを使用
            throw new AuthenticationCredentialsNotFoundException("ログイン情報が存在しません。");
        }

        return new UsernamePasswordAuthenticationToken(loginUser, password, grantedAuthorityList);
    }

    /**
     * ユーザー認証クラスとして利用していいか判定
     * @return {@code true}の場合は利用可, {@code false}の場合は利用不可
     */
    @Override
    public boolean supports(Class<?> token) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(token);
    }
}
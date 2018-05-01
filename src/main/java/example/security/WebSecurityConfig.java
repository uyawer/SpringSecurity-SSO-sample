package example.security;

import example.service.impl.AuthenticationProviderImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;

import com.google.common.collect.Lists;

/**
 * Spring Security設定クラス.
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /** ログインフォーム利用時の認証クラス */
    @Autowired
    private AuthenticationProviderImpl authenticationProvider;

    /**
     * SpringSecurityを用いてページアクセスの認可を設定
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            // 認証対象外のパスを指定してアクセスを許可する
            .antMatchers("/css/**", "/js/**", "/images/**").permitAll()
            // ADMIN roleじゃないと/showAdministratorには入れない
            .antMatchers("/showAdministrator").hasRole("ADMIN")
            // それ以外は匿名アクセス禁止
            .anyRequest().authenticated()
            // ログアウト後にログイン画面を表示するための設定
            .and().logout().logoutSuccessUrl("/login").invalidateHttpSession(true).deleteCookies("JSESSIONID").permitAll()
            // フォームログインを有効化, ログインURLは"/login", ログイン成功時の遷移先は"/", ログイン失敗時の遷移先は"/login-error"
            .and().formLogin().loginPage("/login").defaultSuccessUrl("/").failureUrl("/login-error")
            .usernameParameter("inputUserName").passwordParameter("inputPassWord").permitAll()
            // OAuth2認証を有効化, ログインURLは"/login", GrantedAuthoritiesMapperを適用する
            .and().oauth2Login().loginPage("/login").permitAll().userInfoEndpoint().userAuthoritiesMapper(oauth2UserAuthoritiesMapper());
    }

    /**
     * OAuth2.0を用いてログインした場合のユーザーの権限を設定
     */
    private GrantedAuthoritiesMapper oauth2UserAuthoritiesMapper() {
        // インタフェース的には複数件受け取ることができるが、実際には権限情報(ROLE_USER)の１件のみが渡される
        return authorities -> {
            List<GrantedAuthority> mappedAuthorities = Lists.newArrayList();
            for (GrantedAuthority authority : authorities) {
                // オリジナルの権限情報は引き継ぐ
                mappedAuthorities.add(authority);
                if (OAuth2UserAuthority.class.isInstance(authority)) {
                    // OAuth 2.0 Login機能でログインしたユーザに与える権限情報(ROLE_OAUTH_USER)を追加
                    mappedAuthorities.add(new SimpleGrantedAuthority("ROLE_OAUTH_USER"));
                }
            }
            return mappedAuthorities;
        };
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) {
        // 独自認証クラスを設定する
        auth.authenticationProvider(authenticationProvider);
    }
}
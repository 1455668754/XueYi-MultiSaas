package com.xueyi.auth.handler;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.xueyi.auth.login.base.IUserDetailsService;
import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.servlet.ServletUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Setter;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.www.BasicAuthenticationConverter;
import org.springframework.util.Assert;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;

/**
 * 身份认证 处理器
 *
 * @author xueyi
 */
public class AuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private static final String USER_NOT_FOUND_PASSWORD = "userNotFoundPassword";

    private PasswordEncoder passwordEncoder;

    private volatile String userNotFoundEncodedPassword;

    @Setter
    private UserDetailsPasswordService userDetailsPasswordService;

    private final static BasicAuthenticationConverter basicConvert = new BasicAuthenticationConverter();

    public AuthenticationProvider() {
        this.setPasswordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder());
    }

    /** 密码校验 */
    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        if (authentication.getCredentials() == null) {
            this.logger.debug("Failed to authenticate since no credentials provided");
            throw new BadCredentialsException(this.messages
                    .getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        }
        String presentedPassword = authentication.getCredentials().toString();
        if (!this.passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
            this.logger.debug("Failed to authenticate since password does not match stored value");
            throw new BadCredentialsException(this.messages
                    .getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        }
    }

    @Override
    protected final UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        this.prepareTimingAttackProtection();
        HttpServletRequest request = ServletUtil.getRequest();
        if (ObjectUtil.isNull(request))
            throw new InternalAuthenticationServiceException("web request is empty");

        String grantType = request.getParameter(SecurityConstants.OAuth2ParameterNames.GRANT_TYPE.getCode());
        String accountType = request.getParameter(SecurityConstants.OAuth2ParameterNames.ACCOUNT_TYPE.getCode());
        String clientId = request.getParameter(OAuth2ParameterNames.CLIENT_ID);

        if (StrUtil.isBlank(clientId)) {
            clientId = basicConvert.convert(request).getName();
        }

        Map<String, IUserDetailsService> userDetailsServiceMap = SpringUtil.getBeansOfType(IUserDetailsService.class);

        String finalClientId = clientId;
        Optional<IUserDetailsService> optional = userDetailsServiceMap.values().stream()
                .filter(service -> service.support(finalClientId, grantType, accountType))
                .max(Comparator.comparingInt(Ordered::getOrder));

        if (optional.isEmpty()) {
            throw new InternalAuthenticationServiceException("UserDetailsService error , not register");
        }

        try {
            UserDetails loadedUser = optional.get().loadUser(authentication.getPrincipal());
            if (loadedUser == null)
                throw new InternalAuthenticationServiceException("UserDetailsService returned null, which is an interface contract violation");
            return loadedUser;
        } catch (UsernameNotFoundException exception) {
            this.mitigateAgainstTimingAttack(authentication);
            throw exception;
        } catch (InternalAuthenticationServiceException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new InternalAuthenticationServiceException(exception.getMessage(), exception);
        }
    }

    @Override
    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
        boolean upgradeEncoding = this.userDetailsPasswordService != null && this.passwordEncoder.upgradeEncoding(user.getPassword());
        if (upgradeEncoding) {
            String presentedPassword = authentication.getCredentials().toString();
            String newPassword = this.passwordEncoder.encode(presentedPassword);
            user = this.userDetailsPasswordService.updatePassword(user, newPassword);
        }

        return super.createSuccessAuthentication(principal, authentication, user);
    }

    private void prepareTimingAttackProtection() {
        if (this.userNotFoundEncodedPassword == null) {
            this.userNotFoundEncodedPassword = this.passwordEncoder.encode(USER_NOT_FOUND_PASSWORD);
        }

    }

    private void mitigateAgainstTimingAttack(UsernamePasswordAuthenticationToken authentication) {
        if (authentication.getCredentials() != null) {
            String presentedPassword = authentication.getCredentials().toString();
            this.passwordEncoder.matches(presentedPassword, this.userNotFoundEncodedPassword);
        }
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        Assert.notNull(passwordEncoder, "passwordEncoder cannot be null");
        this.passwordEncoder = passwordEncoder;
        this.userNotFoundEncodedPassword = null;
    }
}


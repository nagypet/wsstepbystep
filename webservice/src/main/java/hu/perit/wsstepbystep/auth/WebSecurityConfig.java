/*
 * Copyright 2020-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package hu.perit.wsstepbystep.auth;

import hu.perit.spvitamin.core.crypto.CryptoUtil;
import hu.perit.spvitamin.spring.config.LocalUserProperties;
import hu.perit.spvitamin.spring.config.SecurityProperties;
import hu.perit.spvitamin.spring.config.SysConfig;
import hu.perit.spvitamin.spring.rest.api.AuthApi;
import hu.perit.spvitamin.spring.security.auth.SimpleHttpSecurityBuilder;
import hu.perit.spvitamin.spring.security.auth.filter.Role2PermissionMapperFilter;
import hu.perit.wsstepbystep.rest.api.BookApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.session.SessionManagementFilter;

import java.util.Map;

/**
 * #know-how:simple-httpsecurity-builder
 *
 * @author Peter Nagy
 */

@EnableWebSecurity
@Slf4j
public class WebSecurityConfig
{
    /*
     * ============== Order(1) =========================================================================================
     */
    @Configuration
    @Order(1)
    @RequiredArgsConstructor
    public static class Order1 extends WebSecurityConfigurerAdapter
    {
        private final LocalUserProperties localUserProperties;
        private final PasswordEncoder passwordEncoder;

        /**
         * This is a global configuration, will be applied to all oder configurer adapters
         *
         * @param auth
         * @throws Exception
         */
        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception
        {
            SecurityProperties securityProperties = SysConfig.getSecurityProperties();

            // Local users for test reasons
            for (Map.Entry<String, LocalUserProperties.User> userEntry : localUserProperties.getLocaluser().entrySet())
            {

                log.warn(String.format("local user name: '%s'", userEntry.getKey()));

                String password = null;
                if (userEntry.getValue().getEncryptedPassword() != null)
                {
                    CryptoUtil crypto = new CryptoUtil();

                    password = crypto.decrypt(SysConfig.getCryptoProperties().getSecret(), userEntry.getValue().getEncryptedPassword());
                }
                else
                {
                    password = userEntry.getValue().getPassword();
                }

                auth.inMemoryAuthentication() //
                        .withUser(userEntry.getKey()) //
                        .password(passwordEncoder.encode(password)) //
                        .authorities("ROLE_" + Role.EMPTY.name());
            }
        }


        @Override
        protected void configure(HttpSecurity http) throws Exception
        {
            SimpleHttpSecurityBuilder.newInstance(http) //
                .scope( //
                    AuthApi.BASE_URL_AUTHENTICATE + "/**", //
                    BookApi.BASE_URL_BOOKS + "/**" //
                ) //
                .authorizeRequests() //
                .antMatchers(AuthApi.BASE_URL_AUTHENTICATE + "/**").fullyAuthenticated() //
                .antMatchers(HttpMethod.GET, BookApi.BASE_URL_BOOKS + "/**").hasAuthority(Permissions.BOOK_READ_ACCESS.name()) //
                .antMatchers(BookApi.BASE_URL_BOOKS + "/**").hasAuthority(Permissions.BOOK_WRITE_ACCESS.name()) //
                .anyRequest().denyAll();

            SimpleHttpSecurityBuilder.afterAuthorization(http).basicAuth();

            http.addFilterAfter(new Role2PermissionMapperFilter(), SessionManagementFilter.class);
        }
    }
}

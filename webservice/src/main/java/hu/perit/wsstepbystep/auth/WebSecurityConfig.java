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

import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.keycloak.adapters.springsecurity.filter.KeycloakSecurityContextRequestFilter;
import org.keycloak.adapters.springsecurity.management.HttpSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;
import org.springframework.security.web.session.SessionManagementFilter;

import hu.perit.spvitamin.spring.config.SpringContext;
import hu.perit.spvitamin.spring.rest.api.AuthApi;
import hu.perit.spvitamin.spring.security.auth.CustomAccessDeniedHandler;
import hu.perit.spvitamin.spring.security.auth.CustomAuthenticationEntryPoint;
import hu.perit.spvitamin.spring.security.auth.SimpleHttpSecurityBuilder;
import hu.perit.wsstepbystep.rest.api.AuthorApi;
import hu.perit.wsstepbystep.rest.api.BookApi;
import lombok.extern.slf4j.Slf4j;

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
    @KeycloakConfiguration
    @Order(1)
    public static class Order1 extends KeycloakWebSecurityConfigurerAdapter
    {

        /**
         * This is a global configuration, will be applied to all oder configurer adapters
         *
         * @param auth
         * @throws Exception
         */
        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception
        {
            KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
            auth.authenticationProvider(keycloakAuthenticationProvider);
        }


        @Override
        protected void configure(HttpSecurity http) throws Exception
        {
            CustomAuthenticationEntryPoint exceptionHandler = SpringContext.getBean(CustomAuthenticationEntryPoint.class);
            CustomAccessDeniedHandler accessDeniedHandler = SpringContext.getBean(CustomAccessDeniedHandler.class);

            http //
                .csrf().requireCsrfProtectionMatcher(keycloakCsrfRequestMatcher()) //
                .and() //
                .sessionManagement() //
                .sessionAuthenticationStrategy(sessionAuthenticationStrategy()) //
                .and() //
                .addFilterBefore(keycloakPreAuthActionsFilter(), LogoutFilter.class) //
                .addFilterBefore(keycloakAuthenticationProcessingFilter(), BasicAuthenticationFilter.class) //
                .addFilterAfter(keycloakSecurityContextRequestFilter(), SecurityContextHolderAwareRequestFilter.class) //
                .addFilterAfter(keycloakAuthenticatedActionsRequestFilter(), KeycloakSecurityContextRequestFilter.class) //
                .exceptionHandling().authenticationEntryPoint(exceptionHandler).accessDeniedHandler(accessDeniedHandler) //
                .and() //
                .logout() //
                .addLogoutHandler(keycloakLogoutHandler()) //
                .logoutUrl("/sso/logout").permitAll() //
                .logoutSuccessUrl("/");

            //            super.configure(http);
            http.authorizeRequests() //
                .antMatchers(AuthApi.BASE_URL_AUTHENTICATE).fullyAuthenticated() //
                .anyRequest().permitAll();

            //            SimpleHttpSecurityBuilder.newInstance(http) //
            //                .scope(AuthorApi.BASE_URL_AUTHORS + "/**") //
            //                .authorizeRequests() //
            //                //                .antMatchers(AuthApi.BASE_URL_AUTHENTICATE).fullyAuthenticated() //
            //                .anyRequest().fullyAuthenticated();
        }


        @Override
        @Bean
        protected SessionAuthenticationStrategy sessionAuthenticationStrategy()
        {
            return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
        }


        @Bean
        @Override
        @ConditionalOnMissingBean(HttpSessionManager.class)
        protected HttpSessionManager httpSessionManager()
        {
            return new HttpSessionManager();
        }


        @Bean
        public KeycloakSpringBootConfigResolver keycloakConfigResolver()
        {
            return new KeycloakSpringBootConfigResolver();
        }
    }


    /*
     * ============== Order(2) =========================================================================================
     */
    @Configuration
    @Order(2)
    public static class Order2 extends WebSecurityConfigurerAdapter
    {

        @Override
        protected void configure(HttpSecurity http) throws Exception
        {
            SimpleHttpSecurityBuilder.newInstance(http) //
                .scope( //
                    BookApi.BASE_URL_BOOKS + "/**", //
                    AuthorApi.BASE_URL_AUTHORS + "/**" //
                ) //
                .authorizeRequests() //                
                .antMatchers(HttpMethod.GET, BookApi.BASE_URL_BOOKS + "/**").hasAuthority(Permissions.BOOK_READ_ACCESS.name()) //
                .antMatchers(HttpMethod.GET, AuthorApi.BASE_URL_AUTHORS + "/**").hasAuthority(Permissions.AUTHOR_READ_ACCESS.name()) //
                .antMatchers(BookApi.BASE_URL_BOOKS + "/**").hasAuthority(Permissions.BOOK_WRITE_ACCESS.name()) //
                .anyRequest().denyAll();

            SimpleHttpSecurityBuilder.afterAuthorization(http).jwtAuth();

            http.addFilterAfter(new PostAuthenticationFilter(), SessionManagementFilter.class);
        }
    }

}

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

import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticatedActionsFilter;
import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticationProcessingFilter;
import org.keycloak.adapters.springsecurity.filter.KeycloakPreAuthActionsFilter;
import org.keycloak.adapters.springsecurity.filter.KeycloakSecurityContextRequestFilter;
import org.keycloak.adapters.springsecurity.management.HttpSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
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
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;
import org.springframework.security.web.session.SessionManagementFilter;

import hu.perit.spvitamin.spring.rest.api.AuthApi;
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
            auth.authenticationProvider(keycloakAuthenticationProvider());
        }


        @Override
        protected void configure(HttpSecurity http) throws Exception
        {
            // According documentation
            //            super.configure(http);
            //            http.authorizeRequests() //
            //                .antMatchers(AuthApi.BASE_URL_AUTHENTICATE).fullyAuthenticated() //
            //                .anyRequest().permitAll();


            http.requestMatchers() //
                .antMatchers(AuthApi.BASE_URL_AUTHENTICATE + "/**").and() //
                .csrf().requireCsrfProtectionMatcher(keycloakCsrfRequestMatcher()) //
                .and() //
                .sessionManagement() //
                .sessionAuthenticationStrategy(sessionAuthenticationStrategy()) //
                .and() //
                .addFilterBefore(keycloakPreAuthActionsFilter(), LogoutFilter.class) //
                .addFilterBefore(keycloakAuthenticationProcessingFilter(), LogoutFilter.class) //
                .addFilterAfter(keycloakSecurityContextRequestFilter(), SecurityContextHolderAwareRequestFilter.class) //
                .addFilterAfter(keycloakAuthenticatedActionsRequestFilter(), KeycloakSecurityContextRequestFilter.class) //
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint()) //
                .and() //
                .logout() //
                .addLogoutHandler(keycloakLogoutHandler()) //
                .logoutUrl("/sso/logout").permitAll() //
                .logoutSuccessUrl("/").and() //
                .authorizeRequests() //
                .anyRequest().fullyAuthenticated();

            // Spvitamin-style
            //            SimpleHttpSecurityBuilder.newInstance(http) //
            //                .scope(AuthApi.BASE_URL_AUTHENTICATE + "/**") //
            //                .basicAuth();
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


        /*
         * By Default, the Spring Security Adapter looks for a keycloak.json configuration file. You can make sure it 
         * looks at the configuration provided by the Spring Boot Adapter
         */
        @Bean
        public KeycloakConfigResolver keycloakConfigResolver()
        {
            return new KeycloakSpringBootConfigResolver();
        }

        // Needed because Spring Boot eagerly registers filter beans to the web application context. This prevents them being registered twice.
        @Bean
        public FilterRegistrationBean<KeycloakAuthenticationProcessingFilter> keycloakAuthenticationProcessingFilterRegistrationBean(
            KeycloakAuthenticationProcessingFilter filter)
        {
            FilterRegistrationBean<KeycloakAuthenticationProcessingFilter> registrationBean = new FilterRegistrationBean<>(filter);
            registrationBean.setEnabled(false);
            return registrationBean;
        }

        // Needed because Spring Boot eagerly registers filter beans to the web application context. This prevents them being registered twice.
        @Bean
        public FilterRegistrationBean<KeycloakPreAuthActionsFilter> keycloakPreAuthActionsFilterRegistrationBean(
            KeycloakPreAuthActionsFilter filter)
        {
            FilterRegistrationBean<KeycloakPreAuthActionsFilter> registrationBean = new FilterRegistrationBean<>(filter);
            registrationBean.setEnabled(false);
            return registrationBean;
        }

        // Needed because Spring Boot eagerly registers filter beans to the web application context. This prevents them being registered twice.
        @Bean
        public FilterRegistrationBean<KeycloakAuthenticatedActionsFilter> keycloakAuthenticatedActionsFilterBean(
            KeycloakAuthenticatedActionsFilter filter)
        {
            FilterRegistrationBean<KeycloakAuthenticatedActionsFilter> registrationBean = new FilterRegistrationBean<>(filter);
            registrationBean.setEnabled(false);
            return registrationBean;
        }

        // Needed because Spring Boot eagerly registers filter beans to the web application context. This prevents them being registered twice.
        @Bean
        public FilterRegistrationBean<KeycloakSecurityContextRequestFilter> keycloakSecurityContextRequestFilterBean(
            KeycloakSecurityContextRequestFilter filter)
        {
            FilterRegistrationBean<KeycloakSecurityContextRequestFilter> registrationBean = new FilterRegistrationBean<>(filter);
            registrationBean.setEnabled(false);
            return registrationBean;
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

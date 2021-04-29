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

package hu.perit.wsstepbystep.rest.api;

import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import hu.perit.spvitamin.spring.auth.AuthorizationToken;
import hu.perit.spvitamin.spring.security.AuthenticatedUser;
import hu.perit.spvitamin.spring.security.auth.jwt.JwtTokenProvider;
import hu.perit.spvitamin.spring.security.auth.jwt.TokenClaims;
import hu.perit.wsstepbystep.auth.Role;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Peter Nagy
 */

@ActiveProfiles({"default", "integtest"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Slf4j
class AuthControllerTest
{

    @Autowired
    private AuthController authController;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    /*
    Az az elvárás, hogy az authentikált user (SecurityContext-ben) számára generál tokent
     */
    @Test
    void testRestController()
    {
        log.debug("-----------------------------------------------------------------------------------------------------");
        log.debug("testRestController()");

        // Beállítunk egy usert a SecurityContext-be
        List<SimpleGrantedAuthority> simpleGrantedAuthorities = List.of(new SimpleGrantedAuthority("ROLE_" + Role.ADMIN.name()));
        UserDetails userDetails = AuthenticatedUser.builder().username("nagy_peter").authorities(simpleGrantedAuthorities).build();
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
            simpleGrantedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Meghívjuk a REST controllert
        AuthorizationToken authorizationToken = this.authController.authenticateUsingGET("123");
        log.debug(authorizationToken.getJwt());

        Claims claims = this.jwtTokenProvider.getClaims(authorizationToken.getJwt());
        String usernameFromJWT = claims.getSubject();
        log.debug(usernameFromJWT);
        Assertions.assertEquals("nagy_peter", usernameFromJWT);

        TokenClaims tokenClaims = new TokenClaims(claims);
        Collection<GrantedAuthority> authorities = tokenClaims.getAuthorities();
        log.debug(authorities.toString());
    }
}
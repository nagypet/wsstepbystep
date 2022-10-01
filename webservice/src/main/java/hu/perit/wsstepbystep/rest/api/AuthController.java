package hu.perit.wsstepbystep.rest.api;

import hu.perit.spvitamin.spring.auth.AuthorizationToken;
import hu.perit.spvitamin.spring.rest.api.AuthApi;
import hu.perit.spvitamin.spring.restmethodlogger.LoggedRestMethod;
import hu.perit.spvitamin.spring.security.AuthenticatedUser;
import hu.perit.spvitamin.spring.security.auth.AuthorizationService;
import hu.perit.spvitamin.spring.security.auth.jwt.JwtTokenProvider;
import hu.perit.spvitamin.spring.security.auth.jwt.TokenClaims;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi
{

    private final JwtTokenProvider tokenProvider;
    private final AuthorizationService authorizationService;

    @Override
    @LoggedRestMethod(subsystem = "auth-controller", eventId = 1)
    public AuthorizationToken authenticateUsingGET(String processID)
    {
        AuthenticatedUser authenticatedUser = this.authorizationService.getAuthenticatedUser();

        AuthorizationToken token = tokenProvider.generateToken(authenticatedUser.getUsername(),
                new TokenClaims(authenticatedUser.getUserId(), authenticatedUser.getAuthorities(), authenticatedUser.getLdapUrl()));
        return token;
    }
}

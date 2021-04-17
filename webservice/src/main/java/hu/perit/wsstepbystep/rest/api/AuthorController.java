package hu.perit.wsstepbystep.rest.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;

import hu.perit.spvitamin.core.took.Took;
import hu.perit.spvitamin.spring.logging.AbstractInterfaceLogger;
import hu.perit.spvitamin.spring.security.auth.AuthorizationService;
import hu.perit.webservice.rest.model.AuthorDTO;
import hu.perit.wsstepbystep.businesslogic.api.BookstoreService;
import hu.perit.wsstepbystep.config.Constants;

@RestController
public class AuthorController extends AbstractInterfaceLogger implements AuthorApi
{

    private final AuthorizationService authorizationService;
    private final BookstoreService bookstoreService;

    protected AuthorController(HttpServletRequest httpRequest, AuthorizationService authorizationService, BookstoreService bookstoreService)
    {
        super(httpRequest);
        this.authorizationService = authorizationService;
        this.bookstoreService = bookstoreService;
    }


    //------------------------------------------------------------------------------------------------------------------
    // getAllAuthors()
    //------------------------------------------------------------------------------------------------------------------
    @Override
    public List<AuthorDTO> getAllAuthors()
    {
        UserDetails user = this.authorizationService.getAuthenticatedUser();
        try (Took took = new Took())
        {
            this.traceIn(null, user.getUsername(), getMyMethodName(), Constants.EVENT_ID_GET_ALL_AUTHORS, "");

            return this.bookstoreService.getAllAuthors();
        }
        catch (Error | RuntimeException ex)
        {
            this.traceOut(null, user.getUsername(), getMyMethodName(), Constants.EVENT_ID_GET_ALL_AUTHORS, ex);
            throw ex;
        }
    }

    @Override
    protected String getSubsystemName()
    {
        return Constants.SUBSYSTEM_NAME;
    }

}

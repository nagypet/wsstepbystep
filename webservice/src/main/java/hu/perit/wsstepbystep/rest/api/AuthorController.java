package hu.perit.wsstepbystep.rest.api;

import hu.perit.spvitamin.spring.restmethodlogger.LoggedRestMethod;
import hu.perit.spvitamin.spring.security.auth.AuthorizationService;
import hu.perit.webservice.rest.model.AuthorWithBooksDTO;
import hu.perit.wsstepbystep.businesslogic.api.BookstoreService;
import hu.perit.wsstepbystep.config.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuthorController implements AuthorApi
{
    private final BookstoreService bookstoreService;

    //------------------------------------------------------------------------------------------------------------------
    // getAllAuthors()
    //------------------------------------------------------------------------------------------------------------------
    @Override
    @LoggedRestMethod(subsystem = Constants.SUBSYSTEM_NAME, eventId = Constants.EVENT_ID_GET_ALL_AUTHORS)
    public List<AuthorWithBooksDTO> getAllAuthors()
    {
        return this.bookstoreService.getAllAuthors();
    }
}

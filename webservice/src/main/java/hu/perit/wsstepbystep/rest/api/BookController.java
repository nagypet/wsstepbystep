package hu.perit.wsstepbystep.rest.api;

import hu.perit.spvitamin.core.took.Took;
import hu.perit.spvitamin.spring.exception.ResourceNotFoundException;
import hu.perit.spvitamin.spring.logging.AbstractInterfaceLogger;
import hu.perit.spvitamin.spring.security.auth.AuthorizationService;
import hu.perit.webservice.rest.model.BookDTO;
import hu.perit.webservice.rest.model.BookParams;
import hu.perit.webservice.rest.model.ResponseUri;
import hu.perit.wsstepbystep.businesslogic.api.BookstoreService;
import hu.perit.wsstepbystep.config.Constants;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

@RestController
public class BookController extends AbstractInterfaceLogger implements BookApi
{
    private final AuthorizationService authorizationService;
    private final BookstoreService bookstoreService;

    protected BookController(HttpServletRequest httpRequest, AuthorizationService authorizationService, BookstoreService bookstoreService)
    {
        super(httpRequest);
        this.authorizationService = authorizationService;
        this.bookstoreService = bookstoreService;
    }


    //------------------------------------------------------------------------------------------------------------------
    // getAllBooks()
    //------------------------------------------------------------------------------------------------------------------
    @Override
    public List<BookDTO> getAllBooks()
    {
        UserDetails user = this.authorizationService.getAuthenticatedUser();
        try (Took took = new Took())
        {
            traceIn(null, user.getUsername(), getMyMethodName(), Constants.EVENT_ID_GET_ALL_BOOKS, "");

            return this.bookstoreService.getAllBooks();
        }
        catch (Exception ex)
        {
            traceOut(null, user.getUsername(), getMyMethodName(), Constants.EVENT_ID_GET_ALL_BOOKS, ex);
            throw ex;
        }
    }


    //------------------------------------------------------------------------------------------------------------------
    // getBookById
    //------------------------------------------------------------------------------------------------------------------
    @Override
    public BookDTO getBookById(Long id) throws ResourceNotFoundException
    {
        UserDetails user = this.authorizationService.getAuthenticatedUser();
        try (Took took = new Took())
        {
            traceIn(null, user.getUsername(), getMyMethodName(), Constants.EVENT_ID_GET_BOOK_BY_ID, String.format("id: %d", id));

            return this.bookstoreService.getBookById(id);
        }
        catch (Error | RuntimeException | ResourceNotFoundException ex)
        {
            traceOut(null, user.getUsername(), getMyMethodName(), Constants.EVENT_ID_GET_BOOK_BY_ID, ex);
            throw ex;
        }
    }


    //------------------------------------------------------------------------------------------------------------------
    // createBook
    //------------------------------------------------------------------------------------------------------------------
    @Override
    public ResponseUri createBook(BookParams bookParams)
    {
        UserDetails user = this.authorizationService.getAuthenticatedUser();
        try (Took took = new Took())
        {
            traceIn(null, user.getUsername(), getMyMethodName(), Constants.EVENT_ID_CREATE_BOOK, bookParams.toString());

            long newUserId = this.bookstoreService.createBook(bookParams);

            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newUserId).toUri();

            return new ResponseUri().location(location.toString());
        }
        catch (Error | RuntimeException ex)
        {
            traceOut(null, user.getUsername(), getMyMethodName(), Constants.EVENT_ID_CREATE_BOOK, ex);
            throw ex;
        }
    }


    //------------------------------------------------------------------------------------------------------------------
    // updateBook
    //------------------------------------------------------------------------------------------------------------------
    @Override
    public void updateBook(Long id, BookParams bookParams) throws ResourceNotFoundException
    {
        UserDetails user = this.authorizationService.getAuthenticatedUser();
        try (Took took = new Took())
        {
            traceIn(null, user.getUsername(), getMyMethodName(), Constants.EVENT_ID_UPDATE_BOOK,
                    String.format("id: %d, bookParams: %s", id, bookParams.toString()));

            this.bookstoreService.updateBook(id, bookParams);
        }
        catch (Error | RuntimeException | ResourceNotFoundException ex)
        {
            traceOut(null, user.getUsername(), getMyMethodName(), Constants.EVENT_ID_UPDATE_BOOK, ex);
            throw ex;
        }
    }


    //------------------------------------------------------------------------------------------------------------------
    // deleteBook
    //------------------------------------------------------------------------------------------------------------------
    @Override
    public void deleteBook(Long id) throws ResourceNotFoundException
    {
        UserDetails user = this.authorizationService.getAuthenticatedUser();
        try (Took took = new Took())
        {
            traceIn(null, user.getUsername(), getMyMethodName(), Constants.EVENT_ID_DELETE_BOOK, String.format("id: %d", id));

            this.bookstoreService.deleteBook(id);
        }
        catch (Error | RuntimeException | ResourceNotFoundException ex)
        {
            traceOut(null, user.getUsername(), getMyMethodName(), Constants.EVENT_ID_DELETE_BOOK, ex);
            throw ex;
        }
    }


    @Override
    protected String getSubsystemName()
    {
        return Constants.SUBSYSTEM_NAME;
    }
}

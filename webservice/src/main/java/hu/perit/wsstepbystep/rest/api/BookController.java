package hu.perit.wsstepbystep.rest.api;

import hu.perit.spvitamin.spring.exception.ResourceNotFoundException;
import hu.perit.spvitamin.spring.restmethodlogger.LoggedRestMethod;
import hu.perit.webservice.rest.model.BookCreateParams;
import hu.perit.webservice.rest.model.BookDTO;
import hu.perit.webservice.rest.model.BookUpdateParams;
import hu.perit.webservice.rest.model.ResponseUri;
import hu.perit.wsstepbystep.businesslogic.api.BookstoreService;
import hu.perit.wsstepbystep.businesslogic.bookstore.BookMapper;
import hu.perit.wsstepbystep.config.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookController implements BookApi
{
    private final BookstoreService bookstoreService;
    private final BookMapper bookMapper;


    //------------------------------------------------------------------------------------------------------------------
    // getAllBooks()
    //------------------------------------------------------------------------------------------------------------------
    @Override
    @LoggedRestMethod(subsystem = Constants.SUBSYSTEM_NAME, eventId = Constants.EVENT_ID_GET_ALL_BOOKS)
    public List<BookDTO> getAllBooks()
    {
        return this.bookstoreService.getAllBooks();
    }


    //------------------------------------------------------------------------------------------------------------------
    // getBookById
    //------------------------------------------------------------------------------------------------------------------
    @Override
    @LoggedRestMethod(subsystem = Constants.SUBSYSTEM_NAME, eventId = Constants.EVENT_ID_GET_BOOK_BY_ID)
    public BookDTO getBookById(Long id) throws ResourceNotFoundException
    {
        return this.bookstoreService.getBookById(id);
    }


    //------------------------------------------------------------------------------------------------------------------
    // createBook
    //------------------------------------------------------------------------------------------------------------------
    @Override
    @LoggedRestMethod(subsystem = Constants.SUBSYSTEM_NAME, eventId = Constants.EVENT_ID_CREATE_BOOK)
    public ResponseUri createBook(@Valid BookCreateParams bookParams)
    {
        long newUserId = this.bookstoreService.createBook(this.bookMapper.map(bookParams));

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newUserId).toUri();

        return new ResponseUri().location(location.toString());
    }


    //------------------------------------------------------------------------------------------------------------------
    // updateBook
    //------------------------------------------------------------------------------------------------------------------
    @Override
    @LoggedRestMethod(subsystem = Constants.SUBSYSTEM_NAME, eventId = Constants.EVENT_ID_UPDATE_BOOK)
    public void updateBook(Long id, BookUpdateParams bookParams) throws ResourceNotFoundException
    {
        this.bookstoreService.updateBook(id, this.bookMapper.map(bookParams));
    }


    //------------------------------------------------------------------------------------------------------------------
    // deleteBook
    //------------------------------------------------------------------------------------------------------------------
    @Override
    @LoggedRestMethod(subsystem = Constants.SUBSYSTEM_NAME, eventId = Constants.EVENT_ID_DELETE_BOOK)
    public void deleteBook(Long id) throws ResourceNotFoundException
    {
        this.bookstoreService.deleteBook(id);
    }
}

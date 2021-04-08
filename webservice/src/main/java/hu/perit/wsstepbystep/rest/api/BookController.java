package hu.perit.wsstepbystep.rest.api;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import hu.perit.wsstepbystep.rest.model.BookDTO;
import hu.perit.wsstepbystep.rest.model.BookParams;
import hu.perit.wsstepbystep.rest.model.ResponseUri;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class BookController implements BookApi
{

    //------------------------------------------------------------------------------------------------------------------
    // getAllBooks()
    //------------------------------------------------------------------------------------------------------------------
    @Override
    public List<BookDTO> getAllBooks()
    {
        log.debug("getAllBooks()");
        List<BookDTO> books = new ArrayList<>();
        books.add(createBookDTO());
        return books;
    }


    private BookDTO createBookDTO()
    {
        BookDTO bookDTO = new BookDTO(12L);
        bookDTO.setAuthor("Vámos Miklós");
        bookDTO.setTitle("Bár");
        bookDTO.setPages(245);
        bookDTO.setDateIssued(LocalDate.of(2012, 3, 23));

        return bookDTO;
    }


    //------------------------------------------------------------------------------------------------------------------
    // getBookById
    //------------------------------------------------------------------------------------------------------------------
    @Override
    public BookDTO getBookById(Long id)
    {
        log.debug("getBookById()");
        return createBookDTO();
    }


    //------------------------------------------------------------------------------------------------------------------
    // createBook
    //------------------------------------------------------------------------------------------------------------------
    @Override
    public ResponseUri createBook(BookParams bookParams)
    {
        log.debug(String.format("createBook(%s)", bookParams.toString()));

        long newUserId = 12;

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newUserId).toUri();

        return new ResponseUri().location(location.toString());
    }


    //------------------------------------------------------------------------------------------------------------------
    // updateBook
    //------------------------------------------------------------------------------------------------------------------
    @Override
    public void updateBook(Long id, BookParams bookParams)
    {
        log.debug(String.format("updateBook(%d, %s)", id, bookParams.toString()));
    }


    //------------------------------------------------------------------------------------------------------------------
    // deleteBook
    //------------------------------------------------------------------------------------------------------------------
    @Override
    public void deleteBook(Long id)
    {
        log.debug(String.format("deleteBook(%d)", id));
    }
}

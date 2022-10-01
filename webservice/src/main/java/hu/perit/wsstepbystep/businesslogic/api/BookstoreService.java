package hu.perit.wsstepbystep.businesslogic.api;

import hu.perit.spvitamin.spring.exception.ResourceNotFoundException;
import hu.perit.webservice.rest.model.AuthorWithBooksDTO;
import hu.perit.webservice.rest.model.BookDTO;
import hu.perit.wsstepbystep.businesslogic.bookstore.BookParams;

import java.util.List;

public interface BookstoreService
{
    List<BookDTO> getAllBooks();

    public BookDTO getBookById(Long id) throws ResourceNotFoundException;

    long createBook(BookParams bookParams);

    void updateBook(Long id, BookParams bookParams) throws ResourceNotFoundException;

    void deleteBook(Long id) throws ResourceNotFoundException;

    List<AuthorWithBooksDTO> getAllAuthors();
}

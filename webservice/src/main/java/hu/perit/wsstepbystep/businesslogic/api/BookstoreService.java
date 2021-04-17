package hu.perit.wsstepbystep.businesslogic.api;

import java.util.List;

import hu.perit.spvitamin.spring.exception.ResourceNotFoundException;
import hu.perit.webservice.rest.model.AuthorDTO;
import hu.perit.webservice.rest.model.BookDTO;
import hu.perit.webservice.rest.model.BookParams;

public interface BookstoreService
{
    List<BookDTO> getAllBooks();

    public BookDTO getBookById(Long id) throws ResourceNotFoundException;

    long createBook(BookParams bookParams);
    
    void updateBook(Long id, BookParams bookParams) throws ResourceNotFoundException;

    void deleteBook(Long id) throws ResourceNotFoundException;

    List<AuthorDTO> getAllAuthors();
}

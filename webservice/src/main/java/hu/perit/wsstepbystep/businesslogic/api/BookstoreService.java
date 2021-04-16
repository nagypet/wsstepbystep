package hu.perit.wsstepbystep.businesslogic.api;

import java.util.List;

import hu.perit.webservice.rest.model.BookDTO;
import hu.perit.wsstepbystep.rest.model.BookParams;

public interface BookstoreService
{
    List<BookDTO> getAllBooks();

    long createBook(BookParams bookParams);
}

package hu.perit.wsstepbystep.rest.api;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import hu.perit.wsstepbystep.rest.model.BookDTO;
import hu.perit.wsstepbystep.rest.model.BookParams;
import hu.perit.wsstepbystep.rest.model.ResponseUri;

public interface BookApi
{
    String BASE_URL_BOOKS = "/books";

    //------------------------------------------------------------------------------------------------------------------
    // getAllBooks()
    //------------------------------------------------------------------------------------------------------------------
    @GetMapping(BASE_URL_BOOKS)
    List<BookDTO> getAllBooks();


    //------------------------------------------------------------------------------------------------------------------
    // getBookById
    //------------------------------------------------------------------------------------------------------------------
    @GetMapping(BASE_URL_BOOKS + "/{id}")
    BookDTO getBookById(@PathVariable("id") Long id);


    //------------------------------------------------------------------------------------------------------------------
    // createBook
    //------------------------------------------------------------------------------------------------------------------
    @PostMapping(BASE_URL_BOOKS)
    ResponseUri createBook(@RequestBody BookParams bookParams);


    //------------------------------------------------------------------------------------------------------------------
    // updateBook
    //------------------------------------------------------------------------------------------------------------------
    @PutMapping(BASE_URL_BOOKS + "/{id}")
    void updateBook(@PathVariable("id") Long id, @RequestBody BookParams bookParams);

    
    //------------------------------------------------------------------------------------------------------------------
    // deleteBook
    //------------------------------------------------------------------------------------------------------------------
    @DeleteMapping(BASE_URL_BOOKS + "/{id}")
    void deleteBook(@PathVariable("id") Long id);
}

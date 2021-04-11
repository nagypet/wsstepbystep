package hu.perit.wsstepbystep.rest.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import hu.perit.spvitamin.spring.logging.EventLogId;
import hu.perit.wsstepbystep.rest.model.BookDTO;
import hu.perit.wsstepbystep.rest.model.BookParams;
import hu.perit.wsstepbystep.rest.model.ResponseUri;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

public interface BookApi
{
    String BASE_URL_BOOKS = "/books";

    //------------------------------------------------------------------------------------------------------------------
    // getAllBooks()
    //------------------------------------------------------------------------------------------------------------------
    @GetMapping(BASE_URL_BOOKS)
    @ApiOperation(value = "getAllBooks() - Retrieves all books")
    @ApiResponses(value = { //
        @ApiResponse(code = 200, message = "Success"), //
        @ApiResponse(code = 401, message = "Invalid credentials"), //
        @ApiResponse(code = 500, message = "Internal server error") //
    })
    @ResponseStatus(value = HttpStatus.OK)
    @EventLogId(eventId = 1)
    List<BookDTO> getAllBooks();


    //------------------------------------------------------------------------------------------------------------------
    // getBookById
    //------------------------------------------------------------------------------------------------------------------
    @GetMapping(BASE_URL_BOOKS + "/{id}")
    @ApiOperation(value = "getBookById() - Retrieves a book by ID")
    @ApiResponses(value = { //
        @ApiResponse(code = 200, message = "Success"), //
        @ApiResponse(code = 401, message = "Invalid credentials"), //
        @ApiResponse(code = 404, message = "User not found"), //
        @ApiResponse(code = 500, message = "Internal server error") //
    })
    @ResponseStatus(value = HttpStatus.OK)
    @EventLogId(eventId = 2)
    BookDTO getBookById(@PathVariable("id") Long id);


    //------------------------------------------------------------------------------------------------------------------
    // createBook
    //------------------------------------------------------------------------------------------------------------------
    @PostMapping(BASE_URL_BOOKS)
    @ApiOperation(value = "createBook() - creates a new book")
    @ApiResponses(value = { //
        @ApiResponse(code = 201, message = "Created"), //
        @ApiResponse(code = 400, message = "Bad request"), //
        @ApiResponse(code = 401, message = "Invalid credentials"), //
        @ApiResponse(code = 409, message = "Book already exists"), //
        @ApiResponse(code = 500, message = "Internal server error") //
    })
    @ResponseStatus(value = HttpStatus.CREATED)
    @EventLogId(eventId = 3)
    ResponseUri createBook(@RequestBody BookParams bookParams);


    //------------------------------------------------------------------------------------------------------------------
    // updateBook
    //------------------------------------------------------------------------------------------------------------------
    @PutMapping(BASE_URL_BOOKS + "/{id}")
    @ApiOperation(value = "updateBook() - Updates a book by ID")
    @ApiResponses(value = { //
        @ApiResponse(code = 200, message = "Success"), //
        @ApiResponse(code = 401, message = "Invalid credentials"), //
        @ApiResponse(code = 404, message = "User not found"), //
        @ApiResponse(code = 500, message = "Internal server error") //
    })
    @ResponseStatus(value = HttpStatus.OK)
    @EventLogId(eventId = 4)
    void updateBook(@PathVariable("id") Long id, @RequestBody BookParams bookParams);


    //------------------------------------------------------------------------------------------------------------------
    // deleteBook
    //------------------------------------------------------------------------------------------------------------------
    @DeleteMapping(BASE_URL_BOOKS + "/{id}")
    @ApiOperation(value = "deleteBook() - removes a book by ID")
    @ApiResponses(value = { //
        @ApiResponse(code = 200, message = "Success"), //
        @ApiResponse(code = 400, message = "Bad request"), //
        @ApiResponse(code = 401, message = "Invalid credentials"), //
        @ApiResponse(code = 404, message = "User not found"), //
        @ApiResponse(code = 500, message = "Internal server error") //
    })
    @ResponseStatus(value = HttpStatus.OK)
    @EventLogId(eventId = 5)
    void deleteBook(@PathVariable("id") Long id);
}

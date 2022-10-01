package hu.perit.wsstepbystep.rest.api;

import hu.perit.spvitamin.spring.exception.ResourceNotFoundException;
import hu.perit.spvitamin.spring.logging.EventLogId;
import hu.perit.webservice.rest.model.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

public interface BookApi
{
    String BASE_URL_BOOKS = "/books";
            
    //------------------------------------------------------------------------------------------------------------------
    // getAllBooks()
    //------------------------------------------------------------------------------------------------------------------
    @GetMapping(BASE_URL_BOOKS)
    @ApiOperation(value = "getAllBooks() - Retrieves all books", authorizations = {@Authorization(value = "Bearer")})
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
    @ApiOperation(value = "getBookById() - Retrieves a book by ID", authorizations = {@Authorization(value = "Bearer")})
    @ApiResponses(value = { //
        @ApiResponse(code = 200, message = "Success"), //
        @ApiResponse(code = 401, message = "Invalid credentials"), //
        @ApiResponse(code = 404, message = "Book not found"), //
        @ApiResponse(code = 500, message = "Internal server error") //
    })
    @ResponseStatus(value = HttpStatus.OK)
    @EventLogId(eventId = 2)
    BookDTO getBookById(@PathVariable("id") Long id) throws ResourceNotFoundException;


    //------------------------------------------------------------------------------------------------------------------
    // createBook
    //------------------------------------------------------------------------------------------------------------------
    @PostMapping(BASE_URL_BOOKS)
    @ApiOperation(value = "createBook() - creates a new book", authorizations = {@Authorization(value = "Bearer")})
    @ApiResponses(value = { //
        @ApiResponse(code = 201, message = "Created"), //
        @ApiResponse(code = 400, message = "Bad request"), //
        @ApiResponse(code = 401, message = "Invalid credentials"), //
        @ApiResponse(code = 409, message = "Book already exists"), //
        @ApiResponse(code = 500, message = "Internal server error") //
    })
    @ResponseStatus(value = HttpStatus.CREATED)
    @EventLogId(eventId = 3)
    ResponseUri createBook(@Valid @RequestBody BookCreateParams bookParams);


    //------------------------------------------------------------------------------------------------------------------
    // updateBook
    //------------------------------------------------------------------------------------------------------------------
    @PutMapping(BASE_URL_BOOKS + "/{id}")
    @ApiOperation(value = "updateBook() - Updates a book by ID", authorizations = {@Authorization(value = "Bearer")})
    @ApiResponses(value = { //
        @ApiResponse(code = 200, message = "Success"), //
        @ApiResponse(code = 401, message = "Invalid credentials"), //
        @ApiResponse(code = 404, message = "Book not found"), //
        @ApiResponse(code = 500, message = "Internal server error") //
    })
    @ResponseStatus(value = HttpStatus.OK)
    @EventLogId(eventId = 4)
    void updateBook(@PathVariable("id") Long id, @Valid @RequestBody BookUpdateParams bookParams) throws ResourceNotFoundException;


    //------------------------------------------------------------------------------------------------------------------
    // deleteBook
    //------------------------------------------------------------------------------------------------------------------
    @DeleteMapping(BASE_URL_BOOKS + "/{id}")
    @ApiOperation(value = "deleteBook() - removes a book by ID", authorizations = {@Authorization(value = "Bearer")})
    @ApiResponses(value = { //
        @ApiResponse(code = 200, message = "Success"), //
        @ApiResponse(code = 400, message = "Bad request"), //
        @ApiResponse(code = 401, message = "Invalid credentials"), //
        @ApiResponse(code = 404, message = "Book not found"), //
        @ApiResponse(code = 500, message = "Internal server error") //
    })
    @ResponseStatus(value = HttpStatus.OK)
    @EventLogId(eventId = 5)
    void deleteBook(@PathVariable("id") Long id) throws ResourceNotFoundException;
}

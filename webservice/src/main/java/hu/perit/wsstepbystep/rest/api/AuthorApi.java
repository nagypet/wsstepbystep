package hu.perit.wsstepbystep.rest.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import hu.perit.webservice.rest.model.AuthorDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

public interface AuthorApi
{
    String BASE_URL_AUTHORS = "/authors";


    //------------------------------------------------------------------------------------------------------------------
    // getAllAuthors()
    //------------------------------------------------------------------------------------------------------------------
    @GetMapping(BASE_URL_AUTHORS)
    @ApiOperation(value = "getAllAuthors() - Retrieves all authors", authorizations = {@Authorization(value = "Bearer")})
    @ApiResponses(value = { //
        @ApiResponse(code = 200, message = "Success"), //
        @ApiResponse(code = 401, message = "Invalid credentials"), //
        @ApiResponse(code = 500, message = "Internal server error") //
    })
    @ResponseStatus(value = HttpStatus.OK)
    List<AuthorDTO> getAllAuthors();
}

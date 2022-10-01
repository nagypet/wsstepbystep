/*
 * Copyright 2020-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package hu.perit.webservice.rest.client;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import hu.perit.spvitamin.spring.auth.AuthorizationToken;
import hu.perit.webservice.rest.model.BookCreateParams;
import hu.perit.webservice.rest.model.BookDTO;
import hu.perit.webservice.rest.model.BookUpdateParams;
import hu.perit.webservice.rest.model.ResponseUri;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author Peter Nagy
 */

public interface WebserviceClient
{

    String BASE_URL_AUTHENTICATE = "/authenticate";
    String BASE_URL_BOOKS = "/books";

    //------------------------------------------------------------------------------------------------------------------
    // authenticate()
    //------------------------------------------------------------------------------------------------------------------
    @RequestLine("GET " + BASE_URL_AUTHENTICATE)
    @Headers({"Content-Type: application/json", "processID: {processID}"})
    AuthorizationToken authenticate(@Param("processID") String processID);


    //------------------------------------------------------------------------------------------------------------------
    // getAllBooks()
    //------------------------------------------------------------------------------------------------------------------
    @RequestLine("GET " + BASE_URL_BOOKS)
    @Headers({"Content-Type: application/json"})
    List<BookDTO> getAllBooks();


    //------------------------------------------------------------------------------------------------------------------
    // getBookById
    //------------------------------------------------------------------------------------------------------------------
    @RequestLine("GET " + BASE_URL_BOOKS + "/{id}")
    @Headers({"Content-Type: application/json"})
    BookDTO getBookById(@Param("id") Long id);


    //------------------------------------------------------------------------------------------------------------------
    // createBook
    //------------------------------------------------------------------------------------------------------------------
    @RequestLine("POST " + BASE_URL_BOOKS)
    @Headers({"Content-Type: application/json"})
    ResponseUri createBook(@RequestBody BookCreateParams bookParams);


    //------------------------------------------------------------------------------------------------------------------
    // updateBook
    //------------------------------------------------------------------------------------------------------------------
    @RequestLine("PUT " + BASE_URL_BOOKS + "/{id}")
    @Headers({"Content-Type: application/json"})
    void updateBook(@Param("id") long userId, @RequestBody BookUpdateParams bookParams);


    //------------------------------------------------------------------------------------------------------------------
    // deleteBook
    //------------------------------------------------------------------------------------------------------------------
    @RequestLine("DELETE " + BASE_URL_BOOKS + "/{id}")
    @Headers({"Content-Type: application/json"})
    void deleteBook(@Param("id") long userId);
}

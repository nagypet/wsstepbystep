package hu.perit.webservice.rest.model;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorWithBooksDTO extends AuthorDTO
{

    private Set<BookDTO> books;

}

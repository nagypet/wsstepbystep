package hu.perit.webservice.rest.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorDTO
{
    private Long id;
    private String name;
    private Long recVersion;
}

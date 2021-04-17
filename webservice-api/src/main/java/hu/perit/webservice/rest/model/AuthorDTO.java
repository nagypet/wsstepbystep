package hu.perit.webservice.rest.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorDTO extends Auditable
{
    private Long id;
    private String name;
}

package hu.perit.webservice.rest.model;

import java.time.LocalDate;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookDTO extends Auditable
{
    private Long id;
    private String title;
    private Set<AuthorDTO> authors;
    private Integer pages;
    private LocalDate dateIssued;
}

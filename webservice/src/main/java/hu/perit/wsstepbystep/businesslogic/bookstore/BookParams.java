package hu.perit.wsstepbystep.businesslogic.bookstore;

import hu.perit.webservice.rest.model.AuthorParams;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class BookParams
{
    private String title;
    private Set<AuthorParams> authors;
    private Integer pages;
    private LocalDate dateIssued;
    private Long recVersion;
}

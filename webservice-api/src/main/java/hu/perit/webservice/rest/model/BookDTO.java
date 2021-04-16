package hu.perit.webservice.rest.model;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookDTO
{
    public BookDTO()
    {
        super();
        this.bookId = null;
    }

    private Long bookId;
    private String title;
    private String author;
    private Integer pages;
    private LocalDate dateIssued;
}

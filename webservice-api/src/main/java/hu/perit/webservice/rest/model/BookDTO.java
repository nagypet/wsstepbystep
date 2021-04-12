package hu.perit.webservice.rest.model;

import java.time.LocalDate;

import lombok.Data;

@Data
public class BookDTO
{
    private final Long id;
    private String title;
    private String author;
    private Integer pages;
    private LocalDate dateIssued;
}

package hu.perit.webservice.rest.model;

import java.time.LocalDate;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class BookParams
{
    @Size(min = 1, max = 100)
    private String title;
    @Size(min = 1, max = 100)
    private String author;
    @Min(0)
    @Max(Integer.MAX_VALUE)
    private Integer pages;
    @NotNull
    private LocalDate dateIssued;
}
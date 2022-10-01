package hu.perit.webservice.rest.model;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

@Data
public class BookUpdateParams
{
    @Size(min = 1, max = 100)
    private String title;

    private Set<AuthorParams> authors;

    @Min(0)
    @Max(Integer.MAX_VALUE)
    private Integer pages;

    private LocalDate dateIssued;

    @NotNull
    private Long recVersion;
}

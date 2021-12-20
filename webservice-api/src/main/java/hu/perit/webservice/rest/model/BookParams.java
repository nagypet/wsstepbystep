package hu.perit.webservice.rest.model;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

@Data
public class BookParams
{
    @Size(min = 1, max = 100)
    private String title;

    @NotNull
    private Set<AuthorParams> authors;
    
    @Min(0)
    @Max(Integer.MAX_VALUE)
    private Integer pages;
    
    @NotNull
    private LocalDate dateIssued;
}

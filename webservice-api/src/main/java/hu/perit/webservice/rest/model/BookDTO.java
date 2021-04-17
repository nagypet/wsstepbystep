package hu.perit.webservice.rest.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookDTO
{
    private Long id;
    private String title;
    private Set<AuthorDTO> authors;
    private Integer pages;
    private LocalDate dateIssued;
    private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;
    private Long recVersion;
}

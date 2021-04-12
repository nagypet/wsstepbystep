package hu.perit.webservice.rest.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ResponseUri
{
    @ApiModelProperty(example = "https://localhost:8400/books/12")
    private String location;

    public ResponseUri location(String location)
    {
        this.location = location;
        return this;
    }
}

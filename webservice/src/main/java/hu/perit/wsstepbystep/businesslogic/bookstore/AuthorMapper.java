package hu.perit.wsstepbystep.businesslogic.bookstore;

import hu.perit.webservice.rest.model.AuthorParams;
import hu.perit.webservice.rest.model.AuthorWithBooksDTO;
import hu.perit.wsstepbystep.db.bookstore.table.AuthorEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthorMapper
{
    AuthorEntity map(AuthorParams params);

    AuthorWithBooksDTO map(AuthorEntity entity);
}

package hu.perit.wsstepbystep.businesslogic.bookstore;


import hu.perit.webservice.rest.model.BookCreateParams;
import hu.perit.webservice.rest.model.BookDTO;
import hu.perit.webservice.rest.model.BookUpdateParams;
import hu.perit.wsstepbystep.db.bookstore.table.BookEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface BookMapper
{
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    BookEntity map(BookParams params);

    BookDTO map(BookEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    void update(@MappingTarget BookEntity entity, BookParams params);

    BookEntity copy(BookEntity source);

    BookParams map(BookCreateParams params);
    BookParams map(BookUpdateParams params);
}

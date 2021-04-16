package hu.perit.wsstepbystep.businesslogic.bookstore;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.perit.webservice.rest.model.BookDTO;
import hu.perit.wsstepbystep.businesslogic.api.BookstoreService;
import hu.perit.wsstepbystep.db.postgres.repo.BookRepo;
import hu.perit.wsstepbystep.db.postgres.table.BookEntity;
import hu.perit.wsstepbystep.rest.model.BookParams;

@Service
public class BookstoreServiceImpl implements BookstoreService
{
    @Autowired
    private BookRepo bookRepo;

    @Override
    public List<BookDTO> getAllBooks()
    {
        List<BookEntity> bookEntities = this.bookRepo.findAll();
        return bookEntities.stream() //
            .map(be -> mapBookEntity2DTO(be)) //
            .collect(Collectors.toList());
    }


    private BookDTO mapBookEntity2DTO(BookEntity be)
    {
        ModelMapper modelMapper = new ModelMapper();

        return modelMapper.map(be, BookDTO.class);
    }


    @Override
    public long createBook(BookParams bookParams)
    {
        ModelMapper modelMapper = new ModelMapper();
        BookEntity bookEntity = modelMapper.map(bookParams, BookEntity.class);
        BookEntity newEntity = this.bookRepo.save(bookEntity);

        return newEntity.getBookId();
    }
}

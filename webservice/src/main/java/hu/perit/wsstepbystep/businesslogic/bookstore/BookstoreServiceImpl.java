package hu.perit.wsstepbystep.businesslogic.bookstore;


import hu.perit.spvitamin.core.typehelpers.LongUtils;
import hu.perit.spvitamin.spring.exception.ResourceNotFoundException;
import hu.perit.webservice.rest.model.AuthorDTO;
import hu.perit.webservice.rest.model.BookDTO;
import hu.perit.webservice.rest.model.BookParams;
import hu.perit.wsstepbystep.businesslogic.api.BookstoreService;
import hu.perit.wsstepbystep.db.postgres.repo.AuthorRepo;
import hu.perit.wsstepbystep.db.postgres.repo.BookRepo;
import hu.perit.wsstepbystep.db.postgres.table.AuthorEntity;
import hu.perit.wsstepbystep.db.postgres.table.BookEntity;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookstoreServiceImpl implements BookstoreService
{
    @Autowired
    private BookRepo bookRepo;

    @Autowired
    private AuthorRepo authorRepo;

    //------------------------------------------------------------------------------------------------------------------
    // getAllBooks()
    //------------------------------------------------------------------------------------------------------------------
    @Override
    public List<BookDTO> getAllBooks()
    {
        List<BookEntity> bookEntities = this.bookRepo.findAll();
        return bookEntities.stream() //
                .map(be -> mapBookEntity2DTO(be)) //
                .collect(Collectors.toList());
    }


    //------------------------------------------------------------------------------------------------------------------
    // getBookById
    //------------------------------------------------------------------------------------------------------------------
    @Override
    public BookDTO getBookById(Long id) throws ResourceNotFoundException
    {
        Optional<BookEntity> bookEntity = this.bookRepo.findById(id);
        if (bookEntity.isPresent())
        {
            return mapBookEntity2DTO(bookEntity.get());
        }

        throw new ResourceNotFoundException(String.format("Book with id %d cannot be found!", id));
    }


    private BookDTO mapBookEntity2DTO(BookEntity be)
    {
        ModelMapper modelMapper = new ModelMapper();

        return modelMapper.map(be, BookDTO.class);
    }


    //------------------------------------------------------------------------------------------------------------------
    // createBook
    //------------------------------------------------------------------------------------------------------------------
    @Override
    @Transactional
    public long createBook(BookParams bookParams)
    {
        return createOrUpdateBookEntity(null, bookParams);
    }


    public long createOrUpdateBookEntity(BookEntity bookEntityInput, BookParams bookParams)
    {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());

        BookEntity bookEntity = null;
        if (bookEntityInput == null)
        {
            // Creating a new BookEntity object
            bookEntity = modelMapper.map(bookParams, BookEntity.class);
        }
        else
        {
            // Mapping fields of bookParams into bookEntity
            bookEntity = bookEntityInput;
            modelMapper.map(bookParams, bookEntity);
        }

        // Authors
        if (bookParams.getAuthors() != null)
        {
            // First we save the authors without id
            List<AuthorEntity> newAuthorsToSave = bookParams.getAuthors().stream() //
                    .filter(a -> LongUtils.isBlank(a.getId())) //
                    .map(dto -> modelMapper.map(dto, AuthorEntity.class)).collect(Collectors.toList());
            List<AuthorEntity> newAuthorEntities = this.authorRepo.saveAll(newAuthorsToSave);

            // Now gather authors with id
            List<Long> authorIds = bookParams.getAuthors().stream() //
                    .filter(a -> LongUtils.isNotBlank(a.getId())) //
                    .map(dto -> dto.getId())
                    .distinct()
                    .collect(Collectors.toList());
            List<AuthorEntity> existingAuthorEntities = this.authorRepo.findAllById(authorIds);

            Set<AuthorEntity> authorEntities = new HashSet<>();
            authorEntities.addAll(existingAuthorEntities);
            authorEntities.addAll(newAuthorEntities);

            if (!bookEntity.getAuthors().equals(authorEntities))
            {
                bookEntity.setAuthorEntities(authorEntities);
            }
        }
        else
        {
            bookEntity.setAuthorEntities(null);
        }

        BookEntity savedEntity = this.bookRepo.save(bookEntity);

        return savedEntity.getId();
    }


    //------------------------------------------------------------------------------------------------------------------
    // updateBook
    //------------------------------------------------------------------------------------------------------------------
    @Override
    @Transactional
    public void updateBook(Long id, BookParams bookParams) throws ResourceNotFoundException
    {
        Optional<BookEntity> byId = this.bookRepo.findById(id);
        if (!byId.isPresent())
        {
            throw new ResourceNotFoundException(String.format("Book with id %d cannot be found!", id));
        }

        createOrUpdateBookEntity(byId.get(), bookParams);
    }


    //------------------------------------------------------------------------------------------------------------------
    // deleteBook
    //------------------------------------------------------------------------------------------------------------------
    @Override
    public void deleteBook(Long id) throws ResourceNotFoundException
    {
        Optional<BookEntity> byId = this.bookRepo.findById(id);
        if (!byId.isPresent())
        {
            throw new ResourceNotFoundException(String.format("Book with id %d cannot be found!", id));
        }

        this.bookRepo.deleteById(id);
    }


    //------------------------------------------------------------------------------------------------------------------
    // getAllAuthors()
    //------------------------------------------------------------------------------------------------------------------
    @Override
    public List<AuthorDTO> getAllAuthors()
    {
        List<AuthorEntity> authorEntities = this.authorRepo.findAll();
        return authorEntities.stream() //
                .map(ae -> mapAuthorEntity2DTO(ae)) //
                .collect(Collectors.toList());
    }


    private AuthorDTO mapAuthorEntity2DTO(AuthorEntity be)
    {
        ModelMapper modelMapper = new ModelMapper();

        return modelMapper.map(be, AuthorDTO.class);
    }
}

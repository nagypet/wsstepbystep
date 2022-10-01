package hu.perit.wsstepbystep.businesslogic.bookstore;

import hu.perit.spvitamin.core.typehelpers.LongUtils;
import hu.perit.spvitamin.spring.exception.ResourceNotFoundException;
import hu.perit.webservice.rest.model.AuthorParams;
import hu.perit.webservice.rest.model.AuthorWithBooksDTO;
import hu.perit.webservice.rest.model.BookDTO;
import hu.perit.wsstepbystep.businesslogic.api.BookstoreService;
import hu.perit.wsstepbystep.db.bookstore.repo.AuthorRepo;
import hu.perit.wsstepbystep.db.bookstore.repo.BookRepo;
import hu.perit.wsstepbystep.db.bookstore.table.AuthorEntity;
import hu.perit.wsstepbystep.db.bookstore.table.BookEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookstoreServiceImpl implements BookstoreService
{
    private final BookRepo bookRepo;
    private final AuthorRepo authorRepo;
    private final BookMapper bookMapper;
    private final AuthorMapper authorMapper;

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
        return this.bookMapper.map(be);
    }


    //------------------------------------------------------------------------------------------------------------------
    // createBook
    //------------------------------------------------------------------------------------------------------------------
    @Override
    @Transactional
    public long createBook(BookParams bookParams)
    {
        return createOrUpdateBookEntity(bookParams, null);
    }


    public long createOrUpdateBookEntity(final BookParams bookParams, final BookEntity destinationBookEntity)
    {
        BookEntity bookEntity = null;
        if (destinationBookEntity != null)
        {
            // Mapping fields of bookParams into destinationBookEntity
            bookEntity = this.bookMapper.copy(destinationBookEntity);
            this.bookMapper.update(bookEntity, bookParams);
        }
        else
        {
            // Creating a new BookEntity object
            bookEntity = this.bookMapper.map(bookParams);
        }

        // Authors
        if (bookParams.getAuthors() != null)
        {
            // First we save the authors without id
            List<AuthorEntity> newAuthorsToSave = bookParams.getAuthors().stream()
                    .filter(a -> LongUtils.isBlank(a.getId()))
                    .map(this.authorMapper::map)
                    .collect(Collectors.toList());
            List<AuthorEntity> newAuthorEntities = this.authorRepo.saveAll(newAuthorsToSave);

            // Now gather authors with id
            List<Long> authorIds = bookParams.getAuthors().stream() //
                    .filter(a -> LongUtils.isNotBlank(a.getId())) //
                    .map(AuthorParams::getId)
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
        if (byId.isEmpty())
        {
            throw new ResourceNotFoundException(String.format("Book with id %d cannot be found!", id));
        }

        createOrUpdateBookEntity(bookParams, byId.get());
    }


    //------------------------------------------------------------------------------------------------------------------
    // deleteBook
    //------------------------------------------------------------------------------------------------------------------
    @Override
    public void deleteBook(Long id) throws ResourceNotFoundException
    {
        Optional<BookEntity> byId = this.bookRepo.findById(id);
        if (byId.isEmpty())
        {
            throw new ResourceNotFoundException(String.format("Book with id %d cannot be found!", id));
        }

        this.bookRepo.deleteById(id);
    }


    //------------------------------------------------------------------------------------------------------------------
    // getAllAuthors()
    //------------------------------------------------------------------------------------------------------------------
    @Override
    public List<AuthorWithBooksDTO> getAllAuthors()
    {
        List<AuthorEntity> authorEntities = this.authorRepo.findAll();
        return authorEntities.stream() //
                .map(this::mapAuthorEntity2DTO) //
                .collect(Collectors.toList());
    }


    private AuthorWithBooksDTO mapAuthorEntity2DTO(AuthorEntity entity)
    {
        return this.authorMapper.map(entity);
    }
}

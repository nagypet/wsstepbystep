package hu.perit.wsstepbystep.db.bookstore.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.perit.wsstepbystep.db.bookstore.table.BookEntity;

public interface BookRepo extends JpaRepository<BookEntity, Long>
{

}

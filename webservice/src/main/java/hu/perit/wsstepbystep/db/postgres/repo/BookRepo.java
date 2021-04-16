package hu.perit.wsstepbystep.db.postgres.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.perit.wsstepbystep.db.postgres.table.BookEntity;

public interface BookRepo extends JpaRepository<BookEntity, Long>
{

}

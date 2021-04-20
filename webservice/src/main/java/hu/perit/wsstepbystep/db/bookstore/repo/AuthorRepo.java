package hu.perit.wsstepbystep.db.bookstore.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.perit.wsstepbystep.db.bookstore.table.AuthorEntity;

public interface AuthorRepo extends JpaRepository<AuthorEntity, Long>
{

}

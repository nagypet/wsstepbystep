package hu.perit.wsstepbystep.db.postgres.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.perit.wsstepbystep.db.postgres.table.AuthorEntity;

public interface AuthorRepo extends JpaRepository<AuthorEntity, Long>
{

}

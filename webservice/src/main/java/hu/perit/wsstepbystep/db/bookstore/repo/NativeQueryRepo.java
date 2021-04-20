package hu.perit.wsstepbystep.db.bookstore.repo;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import hu.perit.spvitamin.spring.data.config.DatasourceCollectionProperties;
import hu.perit.spvitamin.spring.data.config.DatasourceProperties;
import hu.perit.spvitamin.spring.data.nativequery.NativeQueryRepoImpl;
import hu.perit.wsstepbystep.db.bookstore.PostgresDbConfig;

@Repository
public class NativeQueryRepo
{
    private final NativeQueryRepoImpl impl;

    public NativeQueryRepo(EntityManager em, DatasourceCollectionProperties dbProperties)
    {
        DatasourceProperties datasourceProperties = dbProperties.getDatasource().get(PostgresDbConfig.PERSISTENCE_UNIT);
        this.impl = new NativeQueryRepoImpl(em, datasourceProperties.getSocketTimeout());
    }

    public List<?> getResultList(String sql)
    {
        return this.getResultList(sql, true);
    }

    public List<?> getResultList(String sql, boolean logSql)
    {
        return this.impl.getResultList(sql, logSql);
    }

    public Object getSingleResult(String sql)
    {
        return this.impl.getSingleResult(sql);
    }

    public Object getSingleResult(String sql, boolean logSql)
    {
        return this.impl.getSingleResult(sql, logSql);
    }

    @Modifying
    @Transactional
    public void executeModifyingQuery(String sql)
    {
        this.impl.executeModifyingQuery(sql);
    }
}

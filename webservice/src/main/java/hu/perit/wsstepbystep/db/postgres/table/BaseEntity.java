package hu.perit.wsstepbystep.db.postgres.table;

import lombok.Getter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@MappedSuperclass
@Getter
public abstract class BaseEntity
{
    protected abstract Long getId();

    @Version
    @Column(name = "REC_VERSION", nullable = false, updatable = true)
    private Long recVersion;

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder() //
            .append(this.getId()) //
            .hashCode();
    }


    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        BaseEntity other = (BaseEntity) obj;

        return new EqualsBuilder() //
            .append(this.getId(), other.getId()).isEquals();
    }

}

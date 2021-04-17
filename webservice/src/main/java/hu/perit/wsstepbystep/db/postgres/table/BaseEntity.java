package hu.perit.wsstepbystep.db.postgres.table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public abstract class BaseEntity
{
    protected abstract Long getId();
    
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

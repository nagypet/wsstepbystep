/*
 * Copyright 2020-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package hu.perit.wsstepbystep.db.postgres.table;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Peter Nagy
 */

@Getter
@Setter
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "book", schema = "bookstore")
public class BookEntity extends BaseEntity
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id", nullable = false, columnDefinition = "bigserial")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "pages")
    private Integer pages;

    @Column(name = "date_issued")
    private LocalDate dateIssued;

    // The owning side of the many-to-many relationship
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "bookxauthor", schema = "bookstore", // 
        joinColumns = {@JoinColumn(name = "book_id")}, //
        inverseJoinColumns = {@JoinColumn(name = "author_id")} //
    )
    // Important to have a Set here! Hibernate will generate a composite primary key only when using a Set.
    private Set<AuthorEntity> authorEntities;


    public Set<AuthorEntity> getAuthors()
    {
        if (this.authorEntities != null)
        {
            return this.authorEntities;
        }

        return Collections.emptySet();
    }
    
    
    // Auditing
    @CreatedBy
    @Column(name = "createdby", nullable = false, updatable = false)
    private String createdBy;
    
    @CreatedDate
    @Column(name = "createdat", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedBy
    @Column(name = "updatedby", nullable = false, updatable = true)
    private String updatedBy;
    
    @LastModifiedDate
    @Column(name = "updatedat", nullable = false, updatable = true)
    private LocalDateTime updatedAt;
}

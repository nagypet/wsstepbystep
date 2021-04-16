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
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

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
@Table(name = "book", schema = "bookstore")
public class BookEntity
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id", nullable = false, columnDefinition = "bigserial")
    private Long bookId;

    @Column(name = "title")
    private String title;

    @Column(name = "pages")
    private Integer pages;

    @Column(name = "date_issued")
    private LocalDate dateIssued;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "bookxauthor", schema = "bookstore", // 
        joinColumns = {@JoinColumn(name = "book_id")}, //
        inverseJoinColumns = {@JoinColumn(name = "author_id")} //
    )
    // Important to have a Set here! Hibernate will generate a composite primary key only when using a Set.
    private Set<AuthorEntity> authorEntities;
}

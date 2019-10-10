package com.murray.communications.domain.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Abstract entity class that defines the JPA entity id field type and versioning
 * which is used in optimistic locking. Optimistic locking is when you check if the record
 * was updated by someone else before you commit the transaction.
 *
 * @param <ID>
 */
@Setter
@Getter
@MappedSuperclass
public abstract class AbstractPersistableEntity<ID> implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private ID id;

    @Version
    private Long version;
}

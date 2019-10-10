package com.murray.communications.domain.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Put in place so we can use the {@link org.springframework.data.domain.AuditorAware} instead of setting in
 * service implmentation.
 * @param <U>
 * @param <ID>
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AbstractAuditableEntity<U, ID> extends AbstractPersistableEntity<ID> implements Serializable {

    @CreatedDate
    LocalDateTime createdDate;

    @LastModifiedDate
    LocalDateTime lastModifiedDate;

    @CreatedBy
    @ManyToOne
    @JoinColumn(name = "created_by")
    U createdBy;

    @LastModifiedBy
    @ManyToOne
    @JoinColumn(name = "last_modified_by")
    U lastModifiedBy;

}

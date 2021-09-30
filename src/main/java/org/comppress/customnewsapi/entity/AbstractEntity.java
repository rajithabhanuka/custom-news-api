package org.comppress.customnewsapi.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
@Data
public abstract class AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;
    @Version
    protected Long version;
    @Convert(converter = LocalDateTimeConverter.class)
    protected LocalDateTime dateCreated;
    @Convert(converter = LocalDateTimeConverter.class)
    protected LocalDateTime dateModified;
    protected Boolean deleted;

    @PrePersist
    protected void onCreate(){
        this.dateCreated = LocalDateTime.now();
        this.dateModified = dateCreated;
    }

    @PreUpdate
    protected void onUpdate(){
        this.dateModified = LocalDateTime.now();
    }

}

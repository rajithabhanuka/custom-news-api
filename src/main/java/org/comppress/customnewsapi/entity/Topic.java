package org.comppress.customnewsapi.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Data
public class Topic extends AbstractEntity{
    @Column(unique = true)
    private String name;
}
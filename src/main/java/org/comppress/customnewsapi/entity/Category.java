package org.comppress.customnewsapi.entity;

import lombok.Data;

import javax.persistence.Entity;

@Entity
@Data
public class Category extends AbstractEntity{
    private String name;
}

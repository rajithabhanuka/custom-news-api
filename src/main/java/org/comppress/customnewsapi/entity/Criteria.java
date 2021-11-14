package org.comppress.customnewsapi.entity;

import lombok.Data;

import javax.persistence.Entity;

@Entity
@Data
public class Criteria extends AbstractEntity{
    private String criteriaName;
}

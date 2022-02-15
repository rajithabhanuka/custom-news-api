package org.comppress.customnewsapi.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@Data
public class Criteria extends AbstractEntity{
    private String name;
}

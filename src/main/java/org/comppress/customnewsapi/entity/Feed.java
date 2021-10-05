package org.comppress.customnewsapi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Feed extends AbstractEntity{
    private String Category;
    private String url;
    private Long hash;
}

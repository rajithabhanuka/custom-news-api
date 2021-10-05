package org.comppress.customnewsapi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class SavedFeed extends AbstractEntity{

    @ManyToOne
    private Feed feed;
    private String feedString;

}

package org.comppress.customnewsapi.entity;

import lombok.Data;
import javax.persistence.Entity;

@Data
@Entity
public class Publisher extends AbstractEntity {

    String name;

}

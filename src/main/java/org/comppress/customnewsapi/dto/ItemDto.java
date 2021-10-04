package org.comppress.customnewsapi.dto;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@Data
public class ItemDto {

    public String title;

    public String link;

    public String description;

    public EnclosureDto enclosure;

    public String guid;

    public Date pubDate;

    public String encoded;
}

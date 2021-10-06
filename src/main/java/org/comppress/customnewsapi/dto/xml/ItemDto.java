package org.comppress.customnewsapi.dto.xml;

import lombok.Data;

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

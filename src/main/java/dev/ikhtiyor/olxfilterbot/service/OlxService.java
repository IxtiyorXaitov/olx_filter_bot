package dev.ikhtiyor.olxfilterbot.service;

import dev.ikhtiyor.olxfilterbot.payload.ListItemDTO;
import dev.ikhtiyor.olxfilterbot.payload.MainCategoryDTO;
import org.jsoup.nodes.Document;

import java.util.List;

/**
 * @author IkhtiyorDev
 * Date 17/02/22
 **/

public interface OlxService {

    public List<MainCategoryDTO> getAllMainCategories(Document doc);

    public List<ListItemDTO> getAllListItems(Document doc);

    public List<String> getAllImages(Document doc);

    public String getDescription(Document doc);

}

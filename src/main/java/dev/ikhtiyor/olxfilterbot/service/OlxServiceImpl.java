package dev.ikhtiyor.olxfilterbot.service;

import dev.ikhtiyor.olxfilterbot.payload.ListItem;
import dev.ikhtiyor.olxfilterbot.payload.MainCategory;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author IkhtiyorDev <br/>
 * Date 17/02/22
 **/
@Service
public class OlxServiceImpl implements OlxService {

    /**
     * Get categories from Document page. <br/>
     * e.g Детский мир, Недвижимость, Транспорт ...
     * <hr/>
     *
     * @param doc Document from url
     * @return List<MainCategory>
     */
    public List<MainCategory> getAllMainCategories(Document doc) {

        List<MainCategory> mainCategoryList = new ArrayList<>();

        Elements elements = doc.select(".maincategories-list > div > div > a");

        for (Element element : elements) {

            // element
            // <a href="https://www.olx.uz/detskiy-mir/" data-id="36" class="link parent">
            //	    <span>Детский мир</span>
            //	    <span class="cat-icon cat-icon-circle cat-icon-36 cat-cmt-icon-36"></span>
            // </a>

            String url = element.attr("abs:href"); // "https://www.olx.uz/detskiy-mir/"
            String title = element.text(); // Детский мир

            mainCategoryList.add(new MainCategory(title, url));
        }

        return mainCategoryList;
    }

    public List<ListItem> getAllListItems(Document doc) {

        List<ListItem> listItems = new ArrayList<>();

        Elements elements = doc.select(".maincategories-list > div > div > a");

        for (Element element : elements) {

            String title = element.select(".offer-wrapper > table > tbody > tr > td > div > h3 > a > strong").text();
            String address = element.select(".offer-wrapper > table > tbody > tr > td > div > p > small > span").text();
            String price = element.select(".price").text();
            String itemUrl = element.select(".offer-wrapper > table > tbody > tr > td > a").attr("href");
            String imageUrl = element.select("img").attr("src");

            listItems.add(new ListItem(title, address, price, itemUrl, imageUrl));

        }

        return listItems;
    }
}

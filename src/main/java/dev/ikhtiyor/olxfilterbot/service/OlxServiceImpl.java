package dev.ikhtiyor.olxfilterbot.service;

import dev.ikhtiyor.olxfilterbot.payload.ListItemDTO;
import dev.ikhtiyor.olxfilterbot.payload.MainCategoryDTO;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
     * @return List<MainCategoryDTO>
     */
    public List<MainCategoryDTO> getAllMainCategories(Document doc) {

        List<MainCategoryDTO> mainCategoryDTOList = new ArrayList<>();

        Elements elements = doc.select(".maincategories-list > div > div > a");

        for (Element element : elements) {

            // element
            // <a href="https://www.olx.uz/detskiy-mir/" data-id="36" class="link parent">
            //	    <span>Детский мир</span>
            //	    <span class="cat-icon cat-icon-circle cat-icon-36 cat-cmt-icon-36"></span>
            // </a>

            String url = element.attr("abs:href"); // "https://www.olx.uz/detskiy-mir/"
            String title = element.text(); // Детский мир

            mainCategoryDTOList.add(new MainCategoryDTO(title, url));
        }

        return mainCategoryDTOList;
    }

    public List<ListItemDTO> getAllListItems(Document doc) {

        List<ListItemDTO> listItemDTOS = new ArrayList<>();

        Elements elements = doc.select(".maincategories-list > div > div > a");

        for (Element element : elements) {

            String title = element.select(".offer-wrapper > table > tbody > tr > td > div > h3 > a > strong").text();
            String address = element.select(".offer-wrapper > table > tbody > tr > td > div > p > small > span").text();
            String price = element.select(".price").text();
            String itemUrl = element.select(".offer-wrapper > table > tbody > tr > td > a").attr("href");
            String imageUrl = element.select("img").attr("src");

            listItemDTOS.add(new ListItemDTO(title, address, price, itemUrl, imageUrl));

        }

        return listItemDTOS;
    }

    public List<String> getAllImages(Document doc) {

        Elements elements = doc.select(".swiper-zoom-container > img");

        List<String> imageList = new ArrayList<>();

        for (Element element : elements) {
            String src = element.attr("src");
            String dataSrc = element.attr("data-src");

            imageList.add(src);
            imageList.add(dataSrc);
        }

        return imageList
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

    }

    public String getDescription(Document doc) {

        return Objects.requireNonNull(doc.select(".css-g5mtbi-Text").first()).text();

    }
}

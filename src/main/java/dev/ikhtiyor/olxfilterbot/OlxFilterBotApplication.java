package dev.ikhtiyor.olxfilterbot;

import dev.ikhtiyor.olxfilterbot.payload.ListItem;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;


@SpringBootApplication
public class OlxFilterBotApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(OlxFilterBotApplication.class, args);

        Document doc = Jsoup.connect("https://www.olx.uz/oz/detskiy-mir/").get();
        Elements elements = doc.select(".wrap");


    }


}

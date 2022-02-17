package dev.ikhtiyor.olxfilterbot;

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

        Document doc = Jsoup.connect("https://en.wikipedia.org/").get();

        Elements newsHeadlines = doc.select("#mp-itn b a");
        for (Element headline : newsHeadlines) {
            System.out.println("title");
            System.out.println(headline.attr("title"));
            System.out.println("href");
            System.out.println(headline.absUrl("href"));
        }
    }

}

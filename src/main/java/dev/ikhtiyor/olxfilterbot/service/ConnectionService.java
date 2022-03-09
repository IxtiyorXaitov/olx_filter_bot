package dev.ikhtiyor.olxfilterbot.service;

import org.jsoup.nodes.Document;

/**
 * @author IkhtiyorDev
 * Date 17/02/22
 **/

public interface ConnectionService {

    Document connectHomePage();

    Document connectToPage(String url);

}

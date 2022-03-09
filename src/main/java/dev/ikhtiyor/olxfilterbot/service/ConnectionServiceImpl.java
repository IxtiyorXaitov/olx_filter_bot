package dev.ikhtiyor.olxfilterbot.service;

import dev.ikhtiyor.olxfilterbot.utils.RestConstants;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author IkhtiyorDev
 * Date 17/02/22
 **/
@Service
@RequiredArgsConstructor
public class ConnectionServiceImpl implements ConnectionService {

    @Override
    public Document connectHomePage() {
        return connect(RestConstants.BASE_PATH);
    }

    @Override
    public Document connectToPage(String url) {
        return connect(url);
    }

    private Document connect(String url) {
        try {
            return Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

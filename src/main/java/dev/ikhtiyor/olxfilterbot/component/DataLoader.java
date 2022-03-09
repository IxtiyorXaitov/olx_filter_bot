package dev.ikhtiyor.olxfilterbot.component;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author IkhtiyorDev  <br/>
 * Date 08/03/22
 **/


@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {


    @Value("${spring.sql.init.mode}")
    String initMode;

    @Value("${bot.username}")
    String username;

    @Value("${bot.token}")
    String token;

    @Value("${bot.webHookPath}")
    String webHookPath;


    @Override
    public void run(String... args) throws Exception {
        setWebhook();
    }

    public void setWebhook() throws URISyntaxException {
        String telegramBotAPI = "https://api.telegram.org/bot" + token + "/setWebhook?url=" + webHookPath;

        RestTemplate restTemplate = new RestTemplate();

        URI uri = new URI(telegramBotAPI);

        ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);

        if (result.getStatusCodeValue() != 200) {
            System.exit(1);
        }
        System.err.println("result => " + result);
    }


}

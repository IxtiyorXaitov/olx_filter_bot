package dev.ikhtiyor.olxfilterbot.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author IkhtiyorDev  <br/>
 * Date 17/02/22
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListItem {
    private String title;
    private String address;
    private String price;
    private String itemUrl;
    private String imageUrl;
}

package dev.ikhtiyor.olxfilterbot.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author IkhtiyorDev
 * Date 17/02/22
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MainCategoryDTO {

    private String title;
    private String url;
}

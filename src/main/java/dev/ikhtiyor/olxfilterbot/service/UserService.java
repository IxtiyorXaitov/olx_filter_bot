package dev.ikhtiyor.olxfilterbot.service;

import dev.ikhtiyor.olxfilterbot.entity.User;

/**
 * @author IkhtiyorDev  <br/>
 * Date 18/02/22
 **/

public interface UserService {

    User checkAndCreateUser(Long chatId);

}

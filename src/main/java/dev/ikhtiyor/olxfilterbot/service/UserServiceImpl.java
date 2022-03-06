package dev.ikhtiyor.olxfilterbot.service;

import dev.ikhtiyor.olxfilterbot.entity.User;
import dev.ikhtiyor.olxfilterbot.entity.enums.UserStepEnum;
import dev.ikhtiyor.olxfilterbot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * @author IkhtiyorDev  <br/>
 * Date 18/02/22
 **/

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    /**
     * USERNI CHATID BO'YICHA DB DAN QIDIRAMIZ
     * AGAR MAVJUD BO'LSA O'ZINI QAYTARAMIZ
     * AKS HOLDA YANGI USER YARATIB SHUNI QAYTARAMIZ
     *
     * @param chatId message chat id
     * @return User
     */
    @Override
    public User checkAndCreateUser(@NotNull Long chatId) {
        User user;

        Optional<User> optionalUser = userRepository.findByChatId(chatId);

        if (optionalUser.isEmpty()) {

            User newUser = new User();
            newUser.setChatId(chatId);
            newUser.setUserStep(UserStepEnum.START);
            user = userRepository.save(newUser);

        } else {
            user = optionalUser.get();
        }

        return user;
    }
}

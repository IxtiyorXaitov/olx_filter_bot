package dev.ikhtiyor.olxfilterbot.repository;

import dev.ikhtiyor.olxfilterbot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * @author IkhtiyorDev  <br/>
 * Date 18/02/22
 **/

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByChatId(Long chatId);

}

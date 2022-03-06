package dev.ikhtiyor.olxfilterbot.entity;

import dev.ikhtiyor.olxfilterbot.entity.enums.LanguageEnum;
import dev.ikhtiyor.olxfilterbot.entity.enums.UserStepEnum;
import dev.ikhtiyor.olxfilterbot.entity.template.AbsUUIDNotUserAuditEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author IkhtiyorDev  <br/>
 * Date 18/02/22
 **/

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class User extends AbsUUIDNotUserAuditEntity {

    @Column(name = "chat_ic", nullable = false)
    private Long chatId;

    @Column(name = "username")
    private String username;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_step")
    private UserStepEnum userStep;

    @Enumerated(EnumType.STRING)
    @Column(name = "language")
    private LanguageEnum language;

    public User(Long chatId) {
        this.chatId = chatId;
    }
}

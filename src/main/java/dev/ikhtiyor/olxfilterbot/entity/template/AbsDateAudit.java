package dev.ikhtiyor.olxfilterbot.entity.template;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author IkhtiyorDev  <br/>
 * Date 18/02/22
 **/

@Getter
@Setter
@ToString
@MappedSuperclass
public abstract class AbsDateAudit implements Serializable {
    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Timestamp createdAt;//OBJECT YANGI OCHIGANDA ISHLATILADI

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;//OBJECT O'ZGARGANDA ISHLAYDI

    private Boolean deleted = false;

}

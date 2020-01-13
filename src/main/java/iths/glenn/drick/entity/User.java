package iths.glenn.drick.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
public class User implements Serializable {
    static final long serialVersionUID = 1L;

    private @Id @GeneratedValue(strategy = GenerationType.AUTO) long id;
    @Column(name = "role")
    private Role role = Role.USER;
    private double weight = 72;

    enum Role{
        USER,
        ADMIN
    }
}
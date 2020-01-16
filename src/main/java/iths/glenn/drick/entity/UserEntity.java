package iths.glenn.drick.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
public class UserEntity implements Serializable {
    static final long serialVersionUID = 1L;

   // private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) long id;

    @Column(name = "username")
    @Id
    String username;

    @Column(name = "password")
    String password;

    @Column(name = "enabled")
    boolean enabled;

    @Column(name = "weight")
    float weight = 72f;

    private String roles = "USER";

    private String permissions = "";

    public List<String> getPermissions(){
        if(this.permissions.length() > 0){
            return Arrays.asList(this.permissions.split(","));
        }
        return new ArrayList<>();
    }

    public List<String> getRoles(){
        if(this.roles.length() > 0){
            return Arrays.asList(this.permissions.split(","));
        }
        return new ArrayList<>();
    }
}
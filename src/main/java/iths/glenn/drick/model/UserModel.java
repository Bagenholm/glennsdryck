package iths.glenn.drick.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class UserModel {

    long id;
    String username;
    String password;
    boolean enabled;
    double weight;
    private String roles;
    private String permissions;

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

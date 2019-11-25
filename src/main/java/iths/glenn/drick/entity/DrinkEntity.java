package iths.glenn.drick.entity;

import lombok.Data;

@Data
public class DrinkEntity {

    String name;

    public DrinkEntity(String name) {
        this.name = name;
    }
}

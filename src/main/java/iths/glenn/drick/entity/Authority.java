package iths.glenn.drick.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name ="authority")
public class Authority {
    @Id
    @Column(name ="username")
    String username;
    @Column(name ="authority")
    String authority;
}

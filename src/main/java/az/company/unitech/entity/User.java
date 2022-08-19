package az.company.unitech.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "full_name", length = 100, nullable = false, unique = false)
    private String fullName;

    @Column(name = "password", length = 100, nullable = false)
    private String password;

    @Column(name = "pin", length = 100, nullable = false, unique = true)
    private String pin;

    public User(String fullName, String password, String pin) {
        this.fullName = fullName;
        this.password = password;
        this.pin = pin;
    }
}

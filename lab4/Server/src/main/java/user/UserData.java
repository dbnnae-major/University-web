package user;

import javax.persistence.*;

@Entity
@Table(name = "Users")
public class UserData {
    public UserData(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public UserData() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "login")
    private String login;
    @Column(name = "password")
    private String password;
}

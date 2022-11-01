package ru.practicum.explorewithme.user.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "user_friends",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "friend_id")}
    )
    private Set<User> friends;
    @Column(name = "private_mode")
    private boolean privateMode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public Set<User> getFriends() {
        return friends;
    }

    public boolean getPrivateMode() {
        return privateMode;
    }

    public void setPrivateMode(boolean privateMode) {
        this.privateMode = privateMode;
    }
}

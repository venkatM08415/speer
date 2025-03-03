package com.app.speer.model;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name="users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "owner")
    private Set<Note> ownedNotes;

    @ManyToMany(mappedBy = "sharedWith")
    private Set<Note> sharedNotes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Note> getOwnedNotes() {
        return ownedNotes;
    }

    public void setOwnedNotes(Set<Note> ownedNotes) {
        this.ownedNotes = ownedNotes;
    }

    public Set<Note> getSharedNotes() {
        return sharedNotes;
    }

    public void setSharedNotes(Set<Note> sharedNotes) {
        this.sharedNotes = sharedNotes;
    }
}
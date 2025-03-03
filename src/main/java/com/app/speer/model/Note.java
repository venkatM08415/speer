package com.app.speer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    @JsonIgnore
    private Users owner;

    @ManyToMany
    @JoinTable(
            name = "note_shared_users",
            joinColumns = @JoinColumn(name = "note_id"),
            inverseJoinColumns = @JoinColumn(name = "users_id")
    )
    private Set<Users> sharedWith = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Users getOwner() {
        return owner;
    }

    public void setOwner(Users owner) {
        this.owner = owner;
    }

    public Set<Users> getSharedWith() {
        return sharedWith;
    }

    public void setSharedWith(Set<Users> sharedWith) {
        this.sharedWith = sharedWith;
    }


}

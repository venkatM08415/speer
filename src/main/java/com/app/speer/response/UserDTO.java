package com.app.speer.response;

import java.util.Set;

public class UserDTO {
    private Long id;
    private String username;
    private Set<NoteDTO> ownedNotes;

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

    public Set<NoteDTO> getOwnedNotes() {
        return ownedNotes;
    }

    public void setOwnedNotes(Set<NoteDTO> ownedNotes) {
        this.ownedNotes = ownedNotes;
    }
}

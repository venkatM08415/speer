package com.app.speer.response;

import java.util.Set;

public class NoteDTO {
    private Long id;
    private String title;
    private String content;
    private Long ownerId;
    private Set<Long> sharedWithUserIds;

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

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Set<Long> getSharedWithUserIds() {
        return sharedWithUserIds;
    }

    public void setSharedWithUserIds(Set<Long> sharedWithUserIds) {
        this.sharedWithUserIds = sharedWithUserIds;
    }


}

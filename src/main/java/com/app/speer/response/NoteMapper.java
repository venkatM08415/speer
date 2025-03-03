package com.app.speer.response;

import com.app.speer.model.Note;
import com.app.speer.model.Users;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;
@Component
public class NoteMapper {

    public NoteDTO toNoteDTO(Note note) {
        NoteDTO noteDTO = new NoteDTO();
        noteDTO.setId(note.getId());
        noteDTO.setTitle(note.getTitle());
        noteDTO.setContent(note.getContent());
        noteDTO.setOwnerId(note.getOwner().getId());

        Set<Long> sharedWithUserIds = note.getSharedWith().stream()
                .map(Users::getId)
                .collect(Collectors.toSet());
        noteDTO.setSharedWithUserIds(sharedWithUserIds);

        return noteDTO;
    }
}

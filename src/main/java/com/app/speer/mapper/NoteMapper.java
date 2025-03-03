package com.app.speer.mapper;

import com.app.speer.model.Note;
import com.app.speer.model.Users;
import com.app.speer.response.NoteDTO;
import org.mapstruct.Mapper;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface NoteMapper {
    NoteDTO toNoteDTO(Note note);

    default Set<Long> mapSharedWith(Set<Users> sharedWith) {
        return sharedWith.stream()
                .map(Users::getId)
                .collect(Collectors.toSet());
    }
}

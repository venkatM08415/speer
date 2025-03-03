package com.app.speer.service;

import com.app.speer.exceptions.UserSelfShareException;
import com.app.speer.model.Note;
import com.app.speer.request.NoteRequest;
import com.app.speer.model.Users;
import com.app.speer.repository.NoteRepository;
import com.app.speer.repository.UserRepository;
import com.app.speer.response.NoteDTO;
import com.app.speer.response.NoteMapper;
import com.app.speer.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(NoteService.class);
    @Autowired
    private NoteMapper noteMapper;

    public ResponseEntity<String> shareNoteWithUser(Long noteId, String username, Users currentUsers) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found"));
        if (!note.getOwner().getId().equals(currentUsers.getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You are not owner of this note");
        }
        if(note.getOwner().getUsername().equals(currentUsers.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You are the owner of this note, so sharing it with yourself is not allowed.");
        }
        Users targetUsers = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        note.getSharedWith().add(targetUsers);

        noteRepository.save(note);
        return ResponseEntity.ok("Note shared successfully");
    }
    public List<Note> getNotesForUser(String username) {
        return noteRepository.findByOwnerUsernameOrSharedWithUsername(username);
    }

    public Note createNote(NoteRequest noteRequest, String username) {
        Users owner = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Note note = new Note();
        note.setTitle(noteRequest.getTitle());
        note.setContent(noteRequest.getContent());
        note.setOwner(owner);
        return noteRepository.save(note);
    }
    public NoteDTO updateNote(Long id, Note updatedNote) {
        Note existingNote = noteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found with id: " + id));
        existingNote.setTitle(updatedNote.getTitle());
        existingNote.setContent(updatedNote.getContent());
        Note note= noteRepository.save(existingNote);
        return noteMapper.toNoteDTO(note);
    }


    public void deleteNoteById(Long id) {
        if (!noteRepository.existsById(id)) {
            logger.warn("Note not found with id: {}", id);
            throw new ResourceNotFoundException("Note not found with id: " + id);
        }
        logger.info("Deleting note with id: {}", id);
        noteRepository.deleteById(id);
    }

    public List<NoteDTO> search(String q, Long id) {
        List<Note> notes=noteRepository.searchNotes(q, id);
        List<NoteDTO> noteDTOs = notes.stream()
                .map(noteMapper::toNoteDTO)
                .collect(Collectors.toList());
        return noteDTOs;
    }
}

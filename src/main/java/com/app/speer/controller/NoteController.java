package com.app.speer.controller;

import com.app.speer.model.Note;
import com.app.speer.request.NoteRequest;
import com.app.speer.request.ShareRequest;
import com.app.speer.model.Users;
import com.app.speer.repository.NoteRepository;
import com.app.speer.repository.UserRepository;
import com.app.speer.response.NoteDTO;
import com.app.speer.response.NoteMapper;
import com.app.speer.service.NoteService;
import com.app.speer.exceptions.UserNotFoundException;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notes")
@io.github.resilience4j.ratelimiter.annotation.RateLimiter(name = "notesRateLimit")
public class NoteController {
    @Autowired
    private NoteService noteService;
    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(NoteController.class);
    @Autowired
    private NoteMapper noteMapper;

    @PostMapping("/{id}/share")
    public ResponseEntity<?> shareNote(
            @PathVariable Long id,
            @RequestBody ShareRequest shareRequest) {
        Optional<Users> currentUsers = getUser();
        logger.info("User {} sharing notes to id : {}", currentUsers.get().getUsername(), id);
        ResponseEntity<String> response = noteService.shareNoteWithUser(id, shareRequest.getUsername(), currentUsers.get());
        return response;
    }

    @GetMapping("/search")
    public ResponseEntity<List<NoteDTO>> searchNotes(
            @RequestParam String query) {
        Optional<Users> user = getUser();
        List<NoteDTO> notesDto=noteService.search(query, user.get().getId());
        return ResponseEntity.ok(notesDto);
    }

    private Optional<Users> getUser() {
        String username;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            username = userDetails.getUsername();
        } else {
             username = principal.toString();
        }
        Optional<Users> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            logger.warn("User not found with username: {}", username);
            throw new UserNotFoundException("User not found with username: " + username);
        }
        return user;
    }

    @GetMapping
    @RateLimiter(name = "notesRateLimit", fallbackMethod = "rateLimitFallback")
    public ResponseEntity<List<NoteDTO>> getUserNotes() {
        Optional<Users> user = getUser();
        List<Note> notes = noteService.getNotesForUser(user.get().getUsername());
        List<NoteDTO> noteDTOs = notes.stream()
                .map(noteMapper::toNoteDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(noteDTOs);
    }


    public ResponseEntity<?> rateLimitFallback(Exception ex) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body("Too many requests - Please try again later");
    }

    @PostMapping
    public ResponseEntity<Note> createNote(@RequestBody NoteRequest noteRequest, Authentication authentication) {
        String username = authentication.getName();
        Note createdNote = noteService.createNote(noteRequest, username);
        return ResponseEntity.ok(createdNote);
    }


    @PutMapping("/{id}")
    public ResponseEntity<NoteDTO> updateNote(@PathVariable Long id, @RequestBody Note updatedNote) {
        NoteDTO noteDTO = noteService.updateNote(id, updatedNote);
        return ResponseEntity.ok(noteDTO);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNote(@PathVariable Long id) {
        noteService.deleteNoteById(id);
        return ResponseEntity.ok("Note deleted successfully");
    }
}

package com.app.speer.repository;

import com.app.speer.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {

    @Query(value = "select * from note where " +
            "to_tsvector('english', title || ' ' || content) @@ to_tsquery('english', ?1) " +
            "and (owner_id = ?2 or id in (select note_id from note_shared_users where users_id = ?2))",
            nativeQuery = true)
    List<Note> searchNotes(String query, Long userId);


    @Query("select a from Note a where a.owner.username = :username or exists (select 1 from a.sharedWith b where b.username = :username)")
    List<Note> findByOwnerUsernameOrSharedWithUsername(@Param("username") String username);
}
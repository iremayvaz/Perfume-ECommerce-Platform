package com.iremayvaz.repository;

import com.iremayvaz.model.entity.Note;
import com.iremayvaz.model.enums.NoteType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    // NoteRepository.java içine
    @Query("SELECT DISTINCT n.name FROM Note n WHERE n.type = :type")
    List<String> findDistinctNamesByType(@Param("type") NoteType type);
}

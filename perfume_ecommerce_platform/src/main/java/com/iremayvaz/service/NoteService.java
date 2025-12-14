package com.iremayvaz.service;

import com.iremayvaz.model.entity.Note;
import com.iremayvaz.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteService {
    private final NoteRepository noteRepository;


    public List<Note> findAll() {
        return noteRepository.findAll();
    }
}

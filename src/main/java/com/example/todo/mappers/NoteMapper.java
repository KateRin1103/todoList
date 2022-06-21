package com.example.todo.mappers;

import com.example.todo.dto.NoteDTO;
import com.example.todo.entity.Note;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NoteMapper extends MapperTemplate<NoteDTO, Note>{

    NoteMapper INSTANCE = Mappers.getMapper(NoteMapper.class);

    NoteDTO toDTO(Note note);

    List<NoteDTO> toDTO(List<Note> notes);

    Note toModel(NoteDTO obj);

    List<Note> toModel(List<NoteDTO> obj);
}

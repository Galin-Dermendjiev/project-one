package com.example.demoproject.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.example.demoproject.DTO.requests.ChapterRequestDTO;
import com.example.demoproject.DTO.requests.ChapterUpdateRequestDTO;
import com.example.demoproject.DTO.responses.ChapterResponseDTO;
import com.example.demoproject.config.security.AuthorizationService;
import com.example.demoproject.dao.BookRepository;
import com.example.demoproject.dao.ChapterRepository;
import com.example.demoproject.model.Book;
import com.example.demoproject.model.Chapter;
import com.example.demoproject.model.User;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class ChapterServiceImplTests {

    @Mock
    private ChapterRepository chapterRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private AuthorizationService authorizationService;

    @InjectMocks
    private ChapterServiceImpl chapterService;

    private User user;
    private Book book;
    private Chapter chapter;
    private ChapterRequestDTO chapterRequestDTO;
    private ChapterUpdateRequestDTO chapterUpdateRequestDTO;
    private ChapterResponseDTO chapterResponseDTO;

    @BeforeEach
    public void setup() {
    	user = new User();
    	user.setUserId(1L);
    	
        book = new Book();
        book.setBookId(1L);
        book.setUser(user);
        book.setChapters(new ArrayList<>());

        chapter = new Chapter();
        chapter.setChapterId(1L);
        chapter.setTitle("Test Chapter");
        chapter.setContent("Test Content");
        chapter.setIsPublished(false);

        chapterRequestDTO = new ChapterRequestDTO();
        chapterRequestDTO.setTitle("Test Chapter");
        chapterRequestDTO.setContent("Test Content");

        chapterUpdateRequestDTO = new ChapterUpdateRequestDTO();
        chapterUpdateRequestDTO.setId(1L);
        chapterUpdateRequestDTO.setTitle("Updated Chapter");
        chapterUpdateRequestDTO.setContent("Updated Content");
        chapterUpdateRequestDTO.setIsPublished(true);

        chapterResponseDTO = new ChapterResponseDTO();
        chapterResponseDTO.setChapterId(1L);
        chapterResponseDTO.setTitle("Test Chapter");
        chapterResponseDTO.setContent("Test Content");
        chapterResponseDTO.setIsPublished(false);
    }

    @Test
    public void createChapter_BookExists_ReturnsChapterResponseDTO() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(chapterRepository.save(any(Chapter.class))).thenReturn(chapter);
        when(modelMapper.map(chapterRequestDTO, Chapter.class)).thenReturn(chapter);
        when(modelMapper.map(chapter, ChapterResponseDTO.class)).thenReturn(chapterResponseDTO);

        ChapterResponseDTO result = chapterService.createChapter(1L, chapterRequestDTO);

        assertNotNull(result);
        assertEquals(chapterResponseDTO, result);
    }

    @Test
    public void createChapter_BookDoesNotExist_ThrowsEntityNotFoundException() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> chapterService.createChapter(1L, chapterRequestDTO));
    }

    @Test
    public void getChapterById_ChapterExists_ReturnsChapterResponseDTO() {
        when(chapterRepository.findById(1L)).thenReturn(Optional.of(chapter));
        when(modelMapper.map(chapter, ChapterResponseDTO.class)).thenReturn(chapterResponseDTO);

        ChapterResponseDTO result = chapterService.getChapterById(1L);

        assertNotNull(result);
        assertEquals(chapterResponseDTO, result);
    }

    @Test
    public void getChapterById_ChapterDoesNotExist_ThrowsEntityNotFoundException() {
        when(chapterRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> chapterService.getChapterById(1L));
    }

    @Test
    public void getAllChaptersByBookId_BookExists_ReturnsListOfChapterResponseDTO() {
        List<Chapter> chapterList = Stream.of(chapter).toList();
        book.setChapters(chapterList);
        List<ChapterResponseDTO> chapterResponseDTOList = Stream.of(chapterResponseDTO).toList();
        
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(authorizationService.checkBookOwnership(1L)).thenReturn(true);
        when(modelMapper.map(any(Chapter.class), eq(ChapterResponseDTO.class))).thenReturn(chapterResponseDTO);

        List<ChapterResponseDTO> result = chapterService.getAllChaptersByBookId(1L);
        
        assertNotNull(result);
        assertEquals(chapterResponseDTOList.size(), result.size());
        assertEquals(chapterResponseDTOList, result);
    }

    @Test
    public void getAllChaptersByBookId_BookDoesNotExist_ThrowsEntityNotFoundException() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> chapterService.getAllChaptersByBookId(1L));
    }

    @Test
    public void updateChapter_ChapterExists_ReturnsUpdatedChapterResponseDTO() {
        when(chapterRepository.findById(1L)).thenReturn(Optional.of(chapter));
        when(chapterRepository.save(chapter)).thenReturn(chapter);
        when(modelMapper.map(chapter, ChapterResponseDTO.class)).thenReturn(chapterResponseDTO);

        ChapterResponseDTO result = chapterService.updateChapter(chapterUpdateRequestDTO);

        assertNotNull(result);
        assertEquals(chapterResponseDTO, result);
    }

    @Test
    public void updateChapter_ChapterDoesNotExist_ThrowsEntityNotFoundException() {
        when(chapterRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> chapterService.updateChapter(chapterUpdateRequestDTO));
    }

    @Test
    public void deleteChapterById_ChapterExists_DeletesChapter() {
        when(chapterRepository.findById(1L)).thenReturn(Optional.of(chapter));

        chapterService.deleteChapterById(1L);

        verify(chapterRepository, times(1)).deleteById(1L);
    }

    @Test
    public void publishChapter_ChapterExists_PublishesChapter() {
        when(chapterRepository.findById(1L)).thenReturn(Optional.of(chapter));

        chapterService.publishChapter(1L);

        verify(chapterRepository, times(1)).save(chapter);
        assertTrue(chapter.getIsPublished());
    }

    @Test
    public void publishChapter_ChapterDoesNotExist_ThrowsEntityNotFoundException() {
        when(chapterRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> chapterService.publishChapter(1L));
    }
}

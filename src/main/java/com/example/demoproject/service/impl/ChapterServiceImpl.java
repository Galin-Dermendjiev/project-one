package com.example.demoproject.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.example.demoproject.DTO.requests.ChapterRequestDTO;
import com.example.demoproject.DTO.requests.ChapterUpdateRequestDTO;
import com.example.demoproject.DTO.responses.ChapterResponseDTO;
import com.example.demoproject.config.security.AuthorizationService;
import com.example.demoproject.dao.BookRepository;
import com.example.demoproject.dao.ChapterRepository;
import com.example.demoproject.model.Book;
import com.example.demoproject.model.Chapter;
import com.example.demoproject.service.ChapterService;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ChapterServiceImpl implements ChapterService{
	
	private final ChapterRepository chapterRepository;
	private final BookRepository bookRepository;
	private final ModelMapper modelMapper;
	private final AuthorizationService authorizationService;
	
	@Override
	public ChapterResponseDTO createChapter(Long bookId, ChapterRequestDTO chapterRequestDTO) {
		Book book = bookRepository.findById(bookId)
				.orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + bookId));
		
		Chapter newChapter = modelMapper.map(chapterRequestDTO, Chapter.class);
		newChapter.setBook(book);
		newChapter.setIsPublished(false);
		chapterRepository.save(newChapter);
		return modelMapper.map(newChapter, ChapterResponseDTO.class);
	}

	@Override
	public ChapterResponseDTO getChapterById(Long chapterId) {
		Chapter chapter = chapterRepository.findById(chapterId)
				.orElseThrow(() -> new EntityNotFoundException("Chapter not found with id: " + chapterId));
		return modelMapper.map(chapter, ChapterResponseDTO.class);
	}

	@Override
	public List<ChapterResponseDTO> getAllChaptersByBookId(Long bookId) {
		Book book = bookRepository.findById(bookId)
				.orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + bookId));
		List<Chapter> chapters;
		boolean isAuthor = authorizationService.checkBookOwnership(bookId);
		if(isAuthor) {
			chapters = book.getChapters();
		}
		else {
			chapters = book.getChapters().stream().filter(chapter -> chapter.getIsPublished() == true).toList();
		}
		return chapters.stream().map(chapter -> modelMapper.map(chapter, ChapterResponseDTO.class)).toList();
	}

	@Override
	public ChapterResponseDTO updateChapter(ChapterUpdateRequestDTO chapter) {
		Chapter updatedChapter = chapterRepository.findById(chapter.getId())
				.orElseThrow(() -> new EntityNotFoundException("Chapter not found with id: " + chapter.getId()));
		
		updatedChapter.setTitle(chapter.getTitle());
		updatedChapter.setContent(chapter.getContent());
		
		chapterRepository.save(updatedChapter);
		return modelMapper.map(updatedChapter, ChapterResponseDTO.class);
	}

	@Override
	public void deleteChapterById(Long chapterId) {
		chapterRepository.deleteById(chapterId);
		
	}
	
	@Override
	public void publishChapter(Long chapterId) {
		Chapter updatedChapter = chapterRepository.findById(chapterId)
				.orElseThrow(() -> new EntityNotFoundException("Chapter not found with id: " + chapterId));
		updatedChapter.setIsPublished(true);
		chapterRepository.save(updatedChapter);
		
	}

}

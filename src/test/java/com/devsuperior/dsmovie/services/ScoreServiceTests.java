package com.devsuperior.dsmovie.services;

import com.devsuperior.dsmovie.dto.MovieDTO;
import com.devsuperior.dsmovie.dto.ScoreDTO;
import com.devsuperior.dsmovie.entities.MovieEntity;
import com.devsuperior.dsmovie.entities.ScoreEntity;
import com.devsuperior.dsmovie.entities.UserEntity;
import com.devsuperior.dsmovie.repositories.MovieRepository;
import com.devsuperior.dsmovie.repositories.ScoreRepository;
import com.devsuperior.dsmovie.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dsmovie.tests.MovieFactory;
import com.devsuperior.dsmovie.tests.ScoreFactory;
import com.devsuperior.dsmovie.tests.UserFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class ScoreServiceTests {
	
	@InjectMocks
	private ScoreService service;
	@Mock
	private ScoreRepository repository;
	@Mock
	private MovieRepository movieRepository;
	@Mock
	private UserService userService;

	private MovieEntity movie;
	private MovieDTO movieDTO;
	private Long existingMovieId;
	private Long nonExistingMovieId;
	private ScoreEntity score;
	private ScoreDTO scoreDTO;
	private UserEntity user;

	@BeforeEach
	void setUp() throws Exception {
		movie = MovieFactory.createMovieEntity();
		movieDTO = new MovieDTO(movie);
		existingMovieId = 1L;
		nonExistingMovieId = 2L;

		user = UserFactory.createUserEntity();

		score = ScoreFactory.createScoreEntity();
		scoreDTO = new ScoreDTO(score);

		Mockito.when(userService.authenticated()).thenReturn(user);

		Mockito.when(movieRepository.save(movie)).thenReturn(movie);

		Mockito.when(repository.saveAndFlush(score)).thenReturn(score);
	}
	
	@Test
	public void saveScoreShouldReturnMovieDTO() {
		Mockito.when(movieRepository.findById(existingMovieId)).thenReturn(Optional.of(movie));

		movie.getScores().add(score);
		movieDTO = new MovieDTO(movie);

		MovieDTO result = service.saveScore(scoreDTO);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getScore(), scoreDTO.getScore());
	}
	
	@Test
	public void saveScoreShouldThrowResourceNotFoundExceptionWhenNonExistingMovieId() {
		Mockito.when(movieRepository.findById(nonExistingMovieId)).thenReturn(Optional.empty());

		Assertions.assertThrows(ResourceNotFoundException.class, () ->{
			MovieDTO result = service.saveScore(scoreDTO);
		});
	}
}

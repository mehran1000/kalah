package ir.mastcheshmi.kalah.service;

import ir.mastcheshmi.kalah.domain.Game;
import ir.mastcheshmi.kalah.domain.KalahBoard;
import ir.mastcheshmi.kalah.exceptions.GameNotFoundException;
import ir.mastcheshmi.kalah.repository.GameRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameServiceImplTest {
    @Mock
    GameRepository gameRepository;

    @InjectMocks
    GameServiceImpl gameService;

    @Test
    void createGame() {
        Game game = new Game(new KalahBoard());
        when(this.gameRepository.save(any(Game.class)))
                .thenReturn(game);
        Game createdGame = this.gameService.createGame();
        assertThat(createdGame).isEqualTo(game);
        verify(gameRepository, times(1)).save(any(Game.class));
    }

    @Test
    @DisplayName("When the game id is valid")
    void makeMove_basic() {
        Game mockedGame = mock(Game.class);
        when(this.gameRepository.findById(100))
                .thenReturn(Optional.of(mockedGame));
        Game game = this.gameService.makeMove(100, 5);
        assertThat(game).isEqualTo(mockedGame);

        verify(mockedGame).makeMove(5);
        verify(this.gameRepository, times(1))
                .findById(100);
    }

    @Test
    @DisplayName("When the game id is invalid")
    void makeMove_gameNotFount() {
        when(this.gameRepository.findById(100))
                .thenReturn(Optional.ofNullable(null));
        assertThatThrownBy( () -> this.gameService.makeMove(100, 5))
                .isInstanceOf(GameNotFoundException.class);
    }
}
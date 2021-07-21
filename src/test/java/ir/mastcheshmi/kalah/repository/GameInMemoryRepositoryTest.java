package ir.mastcheshmi.kalah.repository;

import ir.mastcheshmi.kalah.domain.Game;
import ir.mastcheshmi.kalah.domain.KalahBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GameInMemoryRepositoryTest {
    private GameRepository gameRepository;

    @BeforeEach
    void setUp() {
        this.gameRepository = new GameInMemoryRepository();
    }

    @Test
    void findById() {
        Game game = new Game(new KalahBoard());
        this.gameRepository.save(game);
        Optional<Game> gameInRepository = this.gameRepository.findById(game.getId());
        assertThat(gameInRepository)
                .isPresent();
        assertEquals(game, gameInRepository.get());

    }

}
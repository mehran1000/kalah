package ir.mastcheshmi.kalah.repository;

import ir.mastcheshmi.kalah.domain.Game;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class GameInMemoryRepository implements GameRepository {
    private Map<Integer, Game> games = new HashMap<>();

    @Override
    public Optional<Game> findById(final int gameId) {
        return Optional.ofNullable(games.get(gameId));
    }

    @Override
    public Game save(Game game) {
        int gameId = game.getId();
        games.put(gameId, game);
        return game;
    }
}

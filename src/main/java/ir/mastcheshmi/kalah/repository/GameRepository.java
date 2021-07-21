package ir.mastcheshmi.kalah.repository;

import ir.mastcheshmi.kalah.domain.Game;

import java.util.Optional;

public interface GameRepository {
    Optional<Game> findById(int gameId);

    Game save( Game game );
}

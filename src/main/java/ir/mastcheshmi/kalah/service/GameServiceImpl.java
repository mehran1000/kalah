package ir.mastcheshmi.kalah.service;

import ir.mastcheshmi.kalah.domain.Game;
import ir.mastcheshmi.kalah.domain.KalahBoard;
import ir.mastcheshmi.kalah.exceptions.GameNotFoundException;
import ir.mastcheshmi.kalah.repository.GameRepository;
import org.springframework.stereotype.Service;

@Service
public class GameServiceImpl implements GameService {

    private GameRepository gameRepository;

    public GameServiceImpl(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public Game createGame() {
        Game game = new Game( new KalahBoard() );
        return this.gameRepository.save(game);
    }


    @Override
    public Game makeMove(int gameId, int pitId) {
        Game game = this.gameRepository.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException(gameId));

        game.makeMove(pitId);
        return game;
    }
}

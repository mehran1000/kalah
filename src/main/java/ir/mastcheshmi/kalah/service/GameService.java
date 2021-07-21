package ir.mastcheshmi.kalah.service;

import ir.mastcheshmi.kalah.domain.Game;

public interface GameService {

    /**
     * Creates a new game
     *
     * @return the new game
     */
    Game createGame();

    /**
     * Sows all the stones in the given pit for the given game on to the right, one in
     * each of the following pits, The rules of the games are documented at {@link Game}
     *
     * @param gameId
     * @param pitId  the id of the pit which the player wants to sow its stones
     * @return the game object for given gameId
     */
    Game makeMove(int gameId, int pitId);
}

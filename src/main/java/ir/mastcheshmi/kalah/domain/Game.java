package ir.mastcheshmi.kalah.domain;

import ir.mastcheshmi.kalah.exceptions.GameOverException;
import ir.mastcheshmi.kalah.exceptions.InvalidMoveException;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class represents a game which has two players,
 * Each of the two players has ​**​six pits​** ​ in front of him/her. To the right of the six pits, each player has a larger pit, his
 * Kalah or house.<br>
 * At the start of the game, six stones are put in each pit<br>
 * The player who begins picks up all the stones in any of their own pits, and sows the stones on to the right, one in
 * each of the following pits, including his own Kalah. No stones are put in the opponent's' Kalah.<br>
 *
 * The game rules are:
 * <ul>
 * <li>If the players last stone lands in his own Kalah, he gets another turn. This can be repeated any number of times before it's the other
 * player's turn.</li>
 *
 * <li>When the last stone lands in an own empty pit, the player captures this stone and all stones in the opposite pit (the
 * other players' pit) and puts them in his own Kalah.</li>
 * <li>
 * The game is over as soon as one of the sides run out of stones. The player who still has stones in his/her pits keeps
 * them and puts them in his/hers Kalah. The winner of the game is the player who has the most stones in his Kalah.
 * </li>
 *</ul>
 */
public class Game {
    private static AtomicInteger currentId = new AtomicInteger(0);
    private int id;
    private KalahBoard kalahBoard;
    private Player currentPlayer = Player.PLAYER_1;
    /**
     * if the game is over, this field specifies the winner
     * null represents which both player have the same number of stone in their kalah
     */
    private Player winner;

    private boolean isOver = false;

    public Game(KalahBoard kalahBoard) {
        this.id = currentId.incrementAndGet();
        this.kalahBoard = kalahBoard;
    }

    public int getId() {
        return id;
    }

    /**
     * If the pit id belongs to the current player(who is his/her turn), picks up all the stones in any of their own pits, and sows the stones on to the right, one in
     * each of the following pits, including his own Kalah. No stones are put in the opponent's' Kalah.<br>
     *
     * @param pitId the id of the pit which the player wants to sow its stones
     */
    public synchronized void makeMove(int pitId) {
        validateMove(pitId);
        Pit lastPit = this.kalahBoard.sow(pitId);
        manageLandOnEmptyPit(lastPit);
        if (isGameOver()) {
            detectWinner();
            return;
        }
        manageTurn(lastPit);
    }

    /**
     * The game is over as soon as one of the sides run out of stones. The player who still has stones in his/her pits keeps
     * them and puts them in his/hers Kalah.
     *
     * @return true if the game is over otherwise returns false
     */
    private boolean isGameOver() {
        boolean isPlayer1runOutOfStones = this.kalahBoard.isRunOutOfStones(Player.PLAYER_1.getSide());
        boolean isPlayer2runOutOfStones = this.kalahBoard.isRunOutOfStones(Player.PLAYER_2.getSide());

        if (isPlayer1runOutOfStones || isPlayer2runOutOfStones) {
            Player playerWithRemainingStone = isPlayer1runOutOfStones ? Player.PLAYER_2 : Player.PLAYER_1;
            moveRemainingStoneOnGameOver(playerWithRemainingStone);
            this.isOver = true;
            return true;
        }

        return false;
    }

    /**
     * The player who has more stones in his/her Kalah will be the winner.<br>
     * If the two players have the same number of stones in their Kala, the game will not have a winner
     */
    private void detectWinner() {
        Pit player1Kalah = this.kalahBoard.getPit(Player.PLAYER_1.getSide().getKalahId());
        Pit player2Kalah = this.kalahBoard.getPit(Player.PLAYER_2.getSide().getKalahId());
        if (player1Kalah.getStoneCount() == player2Kalah.getStoneCount()) {
            return;
        }

        this.winner = player1Kalah.getStoneCount() > player2Kalah.getStoneCount() ? Player.PLAYER_1 : Player.PLAYER_2;
    }

    /**
     * The player who still has stones in his/her pits keeps them and puts them in his/hers Kalah.
     *
     * @param playerWithRemainingStone The player which has stones in his pits
     */
    private void moveRemainingStoneOnGameOver(Player playerWithRemainingStone) {
        Pit kalahPit = this.kalahBoard.getPit(playerWithRemainingStone.getSide().getKalahId());
        playerWithRemainingStone.getSide().getPitIds()
                .stream()
                .map(this.kalahBoard::getPit)
                .forEach(pit -> {
                    kalahPit.addStones(pit.getStoneCount());
                    pit.setStoneCount(0);
                });
    }

    /**
     * When the last stone lands in an own empty pit, the player captures this stone and all stones in the opposite pit (the
     * other players' pit) and puts them in his own Kalah
     *
     * @param lastPit the pit which the last stone landed on it
     */
    private void manageLandOnEmptyPit(Pit lastPit) {
        if (lastPit.getStoneCount() == 1 && !lastPit.isKalah() && currentPlayer.getSide().isOwner(lastPit.getId())) {
            Pit oppositePit = this.kalahBoard.getOppositePit(lastPit);
            Pit currentPlayerKalah = this.kalahBoard.getPit(currentPlayer.getSide().getKalahId());
            //captures this stone and all stones
            currentPlayerKalah.addStones(oppositePit.getStoneCount());
            //this stone
            currentPlayerKalah.addStones(1);
            oppositePit.setStoneCount(0);
            lastPit.setStoneCount(0);
        }
    }

    private void manageTurn(Pit lastPit) {
        if (lastPit.isKalah()) {
            return;
        }

        this.currentPlayer = currentPlayer == Player.PLAYER_1 ? Player.PLAYER_2 : Player.PLAYER_1;
    }

    /**
     * Validate the source(pit Id) of the move
     *
     * @param pitId the id of the pit which the player wants to sow its stones
     */
    private void validateMove(int pitId) {
        if (isOver) {
            throw new GameOverException(winner);
        }

        if (pitId < 1 || pitId > 14) {
            throw new InvalidMoveException("The pit id must be between 1 and 14");
        }

        if (!currentPlayer.getSide().isOwner(pitId)) {
            throw new InvalidMoveException("The pit does not belong to current player:" + currentPlayer);
        }

        Pit pit = this.kalahBoard.getPit(pitId);

        if (pit.isKalah()) {
            throw new InvalidMoveException("You are not allowed to choose the Kalah pit for move");
        }

        if (pit.isEmpty()) {
            throw new InvalidMoveException("The selected pit is empty");
        }

    }

    Player getCurrentPlayer() {
        return currentPlayer;
    }

    Player getWinner() {
        return winner;
    }

    public KalahBoard getKalahBoard() {
        return kalahBoard;
    }
}

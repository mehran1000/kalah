package ir.mastcheshmi.kalah.domain;

/**
 * An enum representing the 2 players of a game.
 */
public enum Player {
    PLAYER_1(KalahBoardSide.LEFT),
    PLAYER_2(KalahBoardSide.RIGHT);

    private final KalahBoardSide side;

    /**
     * @param side The side of the board which belongs to the Player
     */
    Player(KalahBoardSide side) {
        this.side = side;
    }

    /**
     * @return The sid of board owned by the player
     */
    public KalahBoardSide getSide() {
        return side;
    }
}

package ir.mastcheshmi.kalah.domain;

import ir.mastcheshmi.kalah.exceptions.EmptyPitException;
import ir.mastcheshmi.kalah.exceptions.InvalidPitException;
import org.json.simple.JSONObject;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * The board of the 6-stone Kalah game.
 * This board contains 6 pits on each side and 2 larger pits which are called Kalah.
 *
 * @see KalahBoardSide
 */
public class KalahBoard {

    private final List<Pit> pits;
    public static final int BOARD_SIZE = 14;
    public static final int NUMBER_OF_STONES_IN_PIT = 6;

    public KalahBoard() {
        List<Pit> boardPits = IntStream.range(1, BOARD_SIZE + 1)
                .mapToObj(id -> new Pit(id, isKalah(id), isKalah(id) ? 0 : NUMBER_OF_STONES_IN_PIT))
                .collect(Collectors.toList());
        this.pits = Collections.unmodifiableList(boardPits);
    }

    public boolean isKalah(int pitId) {
        return pitId == BOARD_SIZE ||
                pitId == BOARD_SIZE / 2;
    }

    public Pit getPit(int pitId) {
        return this.pits.get(pitId - 1);
    }

    /**
     * Sows the stones on to the right, one in each of the following pits,
     * No stones are put in the pit with skipId
     *
     * @param pitId  The id of the pit which we want to sow it's stone on to the right
     * @return the last pit of sow
     */
    public Pit sow(int pitId ) {
        KalahBoardSide side = KalahBoardSide.RIGHT.isOwner(pitId) ? KalahBoardSide.RIGHT : KalahBoardSide.LEFT;

        int index = pitId - 1;

        validateSow(pitId);
        Pit pit = this.pits.get(index);

        int stoneCount = pit.getStoneCount();
        Pit lastPit = null;

        while (stoneCount > 0) {
            index = adjustIndex(index + 1);
            lastPit = this.pits.get(index);
            if (lastPit.isKalah() && lastPit.getId() != side.getKalahId()) {
                continue;
            }
            pit.decrementStone();
            lastPit.incrementStone();
            stoneCount--;
        }

        return lastPit;
    }


    private void validateSow(int pitId) {
        int index = pitId - 1;

        if( pitId < 1 || pitId > BOARD_SIZE  ){
            throw new InvalidPitException( String.format("The index of the pit(%s) is invalid", pitId));
        }

        Pit pit = this.pits.get(index);

        if( pit.isKalah()){
            throw new InvalidPitException( "The kala pit can not be selected" );
        }

        if (pit.getStoneCount() == 0) {
            throw new EmptyPitException(Integer.toString(pitId));
        }

    }

    private int adjustIndex(int index) {
        return index % this.pits.size();
    }

    /**
     * The game is over as soon as one of the sides run out of stones
     *
     * @return true if the pits of the player are empty otherwise returns false
     */
    public boolean isRunOutOfStones(KalahBoardSide side) {
        Optional<Pit> pitWithStones = side.getPitIds().stream()
                .map(id -> this.pits.get(id - 1))
                .filter(p -> p.getStoneCount() > 0)
                .findFirst();

        return !pitWithStones.isPresent();
    }

    /**
     * The index/Id of opposite pit is calculated by subtracting pit id from board size
     *
     * @param pit the pit which we need to find its opposite pit
     * @return the opposite pit
     */
    public Pit getOppositePit(Pit pit) {
        if (pit.isKalah()) {
            throw new IllegalArgumentException("The opposite pit for Kala pits is undefined");
        }
        int oppositeId = BOARD_SIZE - pit.getId();
        return getPit(oppositeId);
    }

    /**
     * Creates a JSON object key-value representation, where key is the pitId and value
     * is the number of stones in the pit
     *
     * @return The status of the board in JSONObject format
     */
    public JSONObject getStatus() {
        JSONObject boardStatus = new JSONObject();
        IntStream.range(0, this.pits.size())
                .forEach(index -> boardStatus.put(Integer.toString(index + 1),
                        this.pits.get(index).getStoneCount()));
        return boardStatus;
    }
}

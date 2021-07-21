package ir.mastcheshmi.kalah.domain;

import java.util.List;

/**
 * The board has two sides,
 * each side owned  by a player containing 6 pits and one Kalah pit
 */
public enum KalahBoardSide {
    LEFT(List.of(1, 2, 3, 4, 5, 6), 7),
    RIGHT(List.of(8, 9, 10, 11, 12, 13), 14);

    private List<Integer> pitIds;
    private int kalahId;

    /**
     * @param pitIds The ids of the pits belonging to the side
     * @param kalahId The id of the Kalah belonging to the side
     */
    KalahBoardSide( List<Integer> pitIds, int kalahId ) {
        this.pitIds = pitIds;
        this.kalahId = kalahId;
    }

    /**
     *
     * @return The list of the pit ids belonging to the side
     */
    public List<Integer> getPitIds() {
        return pitIds;
    }

    /**
     *
     * @return The id of the Kalah ids belonging to the side
     */
    public int getKalahId() {
        return kalahId;
    }

    /**
     * Checks whether the given pit id belongs to the side or not
     * @param pitId The id of the pit which we want check its owner
     * @return True if the given id belongs to the side otherwise returns false
     */
    public boolean isOwner( int pitId ){
        return this.getPitIds().contains(pitId);
    }
}

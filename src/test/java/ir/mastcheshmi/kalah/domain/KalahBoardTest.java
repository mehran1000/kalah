package ir.mastcheshmi.kalah.domain;

import ir.mastcheshmi.kalah.exceptions.EmptyPitException;
import ir.mastcheshmi.kalah.exceptions.InvalidPitException;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class KalahBoardTest {
    private KalahBoard kalahBoard;

    @BeforeEach
    void setUp() {
        this.kalahBoard = new KalahBoard();
    }

    @Test
    void isKalah() {
        KalahBoardSide.LEFT.getPitIds()
                .forEach( id -> {
                    assertThat(this.kalahBoard.isKalah(id))
                            .isFalse();
                });

        KalahBoardSide.RIGHT.getPitIds()
                .forEach( id -> {
                    assertThat(this.kalahBoard.isKalah(id))
                            .isFalse();
                });
        assertThat(this.kalahBoard.isKalah(KalahBoardSide.RIGHT.getKalahId()))
                .isTrue();
        assertThat(this.kalahBoard.isKalah(KalahBoardSide.LEFT.getKalahId()))
                .isTrue();

    }


    @Test
    @DisplayName("When tries to sow the kalah")
    void sow_thirteenthPit_sowKalah() {
        assertThatThrownBy(() -> this.kalahBoard.sow(7 ))
                .isInstanceOf(InvalidPitException.class);
        assertThatThrownBy(() -> this.kalahBoard.sow(14 ))
                .isInstanceOf(InvalidPitException.class);
    }

    @Test
    @DisplayName("When the index is invalid")
    void sow_thirteenthPit_invalidIndex() {
        assertThatThrownBy(() -> this.kalahBoard.sow(15 ))
                .isInstanceOf(InvalidPitException.class);
    }

    @Test
    @DisplayName("Sow the stones of an empty pit")
    void sow_thirteenthPit_emptyPit() {
        Pit pit9 = this.kalahBoard.getPit(9);
        pit9.setStoneCount(0);
        assertThatThrownBy(() -> this.kalahBoard.sow(9))
                .isInstanceOf(EmptyPitException.class);
    }

    @Test
    @DisplayName("Sow the stones of thirteenth pit with 9 stones(skip the kalah of left Side)")
    void sow_thirteenthPit_withNineStones() {
        int rightSideKalahId = KalahBoardSide.RIGHT.getKalahId();
        int leftSideKalahId = KalahBoardSide.LEFT.getKalahId();
        Pit pit13 = this.kalahBoard.getPit(13);
        pit13.setStoneCount(9);
        this.kalahBoard.sow(13);

        assertThat(this.kalahBoard.getPit(leftSideKalahId).getStoneCount())
                .isZero();
        assertThat(this.kalahBoard.getPit(rightSideKalahId).getStoneCount())
                .isEqualTo(1);
        assertThat(this.kalahBoard.getPit(13).getStoneCount())
                .isZero();

        List<Integer> changedPits = List.of(1,2,3,4,5,6,8,9);
        changedPits
                .forEach( id -> {
                    assertThat(this.kalahBoard.getPit(id).getStoneCount())
                            .isEqualTo(7);
                });
        List<Integer> notChangedPits = List.of(10, 11, 12);
        notChangedPits.stream()
                .forEach( id -> {
                    assertThat(this.kalahBoard.getPit(id).getStoneCount())
                            .isEqualTo(6);
                });

    }

    @Test
    @DisplayName("Sow the stones of fifth pit with 6 stones")
    void sow_fifthPit_withSixStones() {
        int leftSideKalahId = KalahBoardSide.LEFT.getKalahId();
        int rightSideKalahId = KalahBoardSide.RIGHT.getKalahId();
        this.kalahBoard.sow(5);
        assertThat(this.kalahBoard.getPit(leftSideKalahId).getStoneCount())
                .isEqualTo(1);
        assertThat(this.kalahBoard.getPit(rightSideKalahId).getStoneCount())
                .isZero();
        assertThat(this.kalahBoard.getPit(5).getStoneCount())
                .isZero();

        List<Integer> changedPits = List.of(6,8,9,10,11);
        changedPits.stream()
                .forEach( id -> {
                    assertThat(this.kalahBoard.getPit(id).getStoneCount())
                            .isEqualTo(7);
                });
        List<Integer> notChangedPits = List.of(1, 2, 3, 4, 12, 13);
        notChangedPits.stream()
                .forEach( id -> {
                    assertThat(this.kalahBoard.getPit(id).getStoneCount())
                            .isEqualTo(6);
                });
    }

    @Test
    @DisplayName("After removing all the stones of the pits from one side")
    void isRunOutOfStones_emptyPits() {
        KalahBoardSide.RIGHT.getPitIds()
                .stream()
                .forEach( pitId -> {
                    Pit pit = this.kalahBoard.getPit(pitId);
                    pit.setStoneCount(0);
                });
        assertThat( this.kalahBoard.isRunOutOfStones(KalahBoardSide.RIGHT) )
                .isTrue();
    }

    @Test
    @DisplayName("In the beginning, the all pits of both sides have 6 stones")
    void isRunOutOfStones_basic() {
        assertThat( this.kalahBoard.isRunOutOfStones(KalahBoardSide.LEFT) )
            .isFalse();
        assertThat( this.kalahBoard.isRunOutOfStones(KalahBoardSide.RIGHT) )
                .isFalse();
    }

    @Test
    @DisplayName("Throws exception for the Kalah pit")
    void getOppositePit_Kalah() {
        Pit kalah1 = this.kalahBoard.getPit(7);
        assertThatThrownBy(() -> this.kalahBoard.getOppositePit(kalah1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("The opposite pit for a normal pit")
    void getOppositePit() {
        Pit pit1 = this.kalahBoard.getPit(1);
        assertThat(this.kalahBoard.getOppositePit(pit1).getId())
                .isEqualTo(13);

        Pit pit2 = this.kalahBoard.getPit(2);
        assertThat(this.kalahBoard.getOppositePit(pit2).getId())
                .isEqualTo(12);

        Pit pit13 = this.kalahBoard.getPit(13);
        assertThat(this.kalahBoard.getOppositePit(pit13).getId())
                .isEqualTo(1);

    }

    @Test
    @DisplayName("In the beginning, all pits have 6 stones")
    void testGetStatus_afterNew_normalPits() {
        JSONObject kalahBoardStatus = this.kalahBoard.getStatus();
        KalahBoardSide.LEFT.getPitIds()
                .stream()
                .forEach( id ->
                        assertThat(kalahBoardStatus.get(Integer.toString(id))).isEqualTo(6)
                );
        KalahBoardSide.RIGHT.getPitIds()
                .stream()
                .forEach( id ->
                        assertThat(kalahBoardStatus.get(Integer.toString(id))).isEqualTo(6)
                );

        assertThat(kalahBoardStatus.get("1"))
                .isEqualTo(6);
        assertThat(kalahBoardStatus.get("12"))
                .isEqualTo(6);

        assertThat(kalahBoardStatus.get("7"))
                .isEqualTo(0);
        assertThat(kalahBoardStatus.get("14"))
                .isEqualTo(0);

    }

    @Test
    @DisplayName("In the beginning, all the Kalah pits have 0 stones")
    void testGetStatus_afterNew_kalaPits() {
        JSONObject kalahBoardStatus = this.kalahBoard.getStatus();
        assertThat(kalahBoardStatus.get(Integer.toString(KalahBoardSide.RIGHT.getKalahId())))
                .isEqualTo(0);
        assertThat(kalahBoardStatus.get(Integer.toString(KalahBoardSide.LEFT.getKalahId())))
                .isEqualTo(0);
    }

    @Test
    @DisplayName("After updating the number of stones")
    void testGetStatus_afterChanging() {
        this.kalahBoard.getPit(8 ).setStoneCount(1);
        this.kalahBoard.getPit(14 ).setStoneCount(5);
        JSONObject kalahBoardStatus = this.kalahBoard.getStatus();

        assertThat(kalahBoardStatus.get("8"))
                .isEqualTo(1);
        assertThat(kalahBoardStatus.get("14"))
                .isEqualTo(5);
    }
}
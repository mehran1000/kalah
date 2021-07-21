package ir.mastcheshmi.kalah.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class KalahBoardSideTest {

    @Test
    void getPitIds() {
        assertThat(KalahBoardSide.LEFT.getPitIds())
                .contains(1,2,3,4,5,6);

        assertThat(KalahBoardSide.RIGHT.getPitIds())
                .contains(8,9,10,11,12,13);
    }

    @Test
    void getKalahId() {
        assertThat(KalahBoardSide.LEFT.getKalahId())
                .isEqualTo(7);

        assertThat(KalahBoardSide.RIGHT.getKalahId())
                .isEqualTo(14);
    }

    @Test
    void isOwner() {
        assertThat(KalahBoardSide.LEFT.isOwner(1))
                .isTrue();
        assertThat(KalahBoardSide.LEFT.isOwner(9))
                .isFalse();

        assertThat(KalahBoardSide.RIGHT.isOwner(8))
                .isTrue();
        assertThat(KalahBoardSide.RIGHT.isOwner(2))
                .isFalse();


    }
}
package ir.mastcheshmi.kalah.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class PlayerTest {
    @Test
    void getSide() {
        assertThat(Player.PLAYER_1.getSide())
                .isEqualTo(KalahBoardSide.LEFT);
        assertThat(Player.PLAYER_2.getSide())
                .isEqualTo(KalahBoardSide.RIGHT);
    }

}
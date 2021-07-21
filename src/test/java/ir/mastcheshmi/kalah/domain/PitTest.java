package ir.mastcheshmi.kalah.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;

class PitTest {

    @Test
    void decrementStone() {
        Pit pit = new Pit(2, false, 1);
        pit.decrementStone();
        assertThat(pit.getStoneCount())
                .isZero();
        assertThatThrownBy(pit::decrementStone)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void incrementStone() {
        Pit pit = new Pit(2, false, 1);
        pit.incrementStone();
        assertThat(pit.getStoneCount())
                .isEqualTo(2);
        pit.incrementStone();
        assertThat(pit.getStoneCount())
                .isEqualTo(3);
    }

    @Test
    void addStones() {
        Pit pit = new Pit(2, false, 2);
        pit.addStones(5);
        assertThat(pit.getStoneCount())
                .isEqualTo(7);
    }

    @Test
    void isEmpty() {
        Pit pit = new Pit(2, false, 1);
        assertThat(pit.isEmpty())
                .isFalse();
        pit.decrementStone();
        assertThat(pit.isEmpty())
                .isTrue();
    }

    @Test
    void isKalah() {
        Pit pit = new Pit(2, false, 1);
        assertThat(pit.isKalah())
                .isFalse();

        pit = new Pit(2, true, 1);
        assertThat(pit.isKalah())
                .isTrue();
    }

    @Test
    void setStoneCount() {
        Pit pit = new Pit(2, false, 1);
        pit.setStoneCount(3);
        assertThat(pit.getStoneCount())
                .isEqualTo(3);
        assertThatThrownBy(() -> pit.setStoneCount(-2))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
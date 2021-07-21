package ir.mastcheshmi.kalah.domain;

import ir.mastcheshmi.kalah.exceptions.GameOverException;
import ir.mastcheshmi.kalah.exceptions.InvalidMoveException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class GameTest {
    private Game game;
    private KalahBoard kalahBoard;

    @BeforeEach
    void setUp() {
        KalahBoard kalahBoard = new KalahBoard();
        this.kalahBoard = Mockito.spy(kalahBoard);
        this.game = new Game(this.kalahBoard);
    }

    @Test
    @DisplayName("When the last pit of sow is a Kalah pit, The current player will not changed")
    void makeMove_kalaAsLastPit() {
        when(this.kalahBoard.getPit(1))
                .thenReturn(new Pit(1, false, 6));
        when(this.kalahBoard.sow(1))
                .thenReturn(new Pit(7, true, 0));
        this.game.makeMove(1);
        assertThat(this.game.getCurrentPlayer())
                .isEqualTo(Player.PLAYER_1);
    }

    @Test
    @DisplayName("When the last pit of sow is a normal pit, The current player will be changed")
    void makeMove_changeCurrentPlayer() {
        when(this.kalahBoard.getPit(2))
                .thenReturn(new Pit(2, false, 6));
        when(this.kalahBoard.sow(2))
                .thenReturn(new Pit(8, false, 7));
        this.game.makeMove(2);
        assertThat(this.game.getCurrentPlayer())
                .isEqualTo(Player.PLAYER_2);
    }

    @Test
    @DisplayName("When land on an own empty pit, all stones in the opposite pit are added to the current player's Kalah")
    void makeMove_landOnEmptyPit() {
        this.kalahBoard.getPit(2).setStoneCount(2);
        this.kalahBoard.getPit(7).setStoneCount(3);
        this.kalahBoard.getPit(4).setStoneCount(0);
        this.game.makeMove(2);
        //player was changed
        assertThat(this.game.getCurrentPlayer())
                .isEqualTo(Player.PLAYER_2);
        assertThat(this.kalahBoard.getPit(KalahBoardSide.LEFT.getKalahId()).getStoneCount())
                .isEqualTo(10);
        //the opposite pit
        assertThat(this.kalahBoard.getPit(10).getStoneCount())
                .isZero();
        // the last pit
        assertThat(this.kalahBoard.getPit(4).getStoneCount())
                .isZero();
    }

    @Test
    @DisplayName("When the pit id is invalid(less than 1 or bigger than 14)")
    void makeMove_invalidPitId() {
        assertThatThrownBy(() -> this.game.makeMove(-1))
                .isInstanceOf(InvalidMoveException.class)
                .hasMessage("Invalid move: The pit id must be between 1 and 14");
        assertThatThrownBy(() -> this.game.makeMove(15))
                .isInstanceOf(InvalidMoveException.class)
                .hasMessage("Invalid move: The pit id must be between 1 and 14");
    }

    @Test
    @DisplayName("When the pit does not belong to current player")
    void makeMove_invalidPitForCurrentPlayer() {
        assertThatThrownBy(() -> this.game.makeMove(8))
                .isInstanceOf(InvalidMoveException.class)
                .hasMessage("Invalid move: The pit does not belong to current player:" + Player.PLAYER_1);
    }

    @Test
    @DisplayName("When the pit does not belong to current player")
    void makeMove_emptyPit() {
        when(this.kalahBoard.getPit(5))
                .thenReturn(new Pit(5, false, 0));

        assertThatThrownBy(() -> this.game.makeMove(5))
                .isInstanceOf(InvalidMoveException.class)
                .hasMessage("Invalid move: The selected pit is empty");
    }

    @Test
    @DisplayName("When the pit is a Kalah pit")
    void makeMove_kalahPit() {
        when(this.kalahBoard.getPit(5))
                .thenReturn(new Pit(5, true, 2));

        assertThatThrownBy(() -> this.game.makeMove(5))
                .isInstanceOf(InvalidMoveException.class)
                .hasMessage("Invalid move: You are not allowed to choose the Kalah pit for move");
    }


    @Nested
    @DisplayName("Last move(after this move, the game gets over)")
    class AfterLastMove {
        @BeforeEach
        void makeLastMove() {
            Player.PLAYER_1.getSide().getPitIds()
                    .stream()
                    .forEach(id -> {
                        kalahBoard.getPit(id).setStoneCount(0);
                    });
            Player.PLAYER_2.getSide().getPitIds()
                    .stream()
                    .forEach(id -> {
                        kalahBoard.getPit(id).setStoneCount(0);
                    });
            kalahBoard.getPit(6).setStoneCount(1);
            kalahBoard.getPit(8).setStoneCount(4);
            kalahBoard.getPit(9).setStoneCount(3);

        }

        @Nested
        @DisplayName("Player2 wins")
        class GameWithWinner {
            @BeforeEach
            void setUpPlayer2AsWinner() {
                kalahBoard.getPit(Player.PLAYER_1.getSide().getKalahId()).setStoneCount(30);//31
                kalahBoard.getPit(Player.PLAYER_2.getSide().getKalahId()).setStoneCount(35);//42
                game.makeMove(6);
            }

            @Test
            @DisplayName("The game is over and who still has stones in his/her pits puts them in his/hers Kalah")
            void makeMove_gameIsOver() {
                assertThatThrownBy(() -> game.makeMove(8))
                        .isInstanceOf(GameOverException.class)
                        .hasMessage("Game is over: The game is over(PLAYER_2 won)" );
            }

            @Test
            @DisplayName("Checks the winner")
            void getWinner() {
                assertThat(game.getWinner())
                        .isEqualTo(Player.PLAYER_2);
            }


        }

        @Nested
        @DisplayName("Game does not have winner")
        class GameWithoutWinner {
            @BeforeEach
            void setUpDrawStatus() {
                kalahBoard.getPit(Player.PLAYER_1.getSide().getKalahId()).setStoneCount(35);
                kalahBoard.getPit(Player.PLAYER_2.getSide().getKalahId()).setStoneCount(29);
                game.makeMove(6);
            }

            @Test
            @DisplayName("The game is over and who still has stones in his/her pits puts them in his/hers Kalah")
            void makeMove_gameIsOver() {
                assertThatThrownBy(() -> game.makeMove(8))
                        .isInstanceOf(GameOverException.class)
                        .hasMessage("Game is over: The game is over(nobody won)" );
            }

            @Test
            @DisplayName("Checks the winner")
            void getWinner() {
                assertThat(game.getWinner())
                        .isNull();
            }
        }


    }


}
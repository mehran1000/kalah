package ir.mastcheshmi.kalah.exceptions;

import ir.mastcheshmi.kalah.domain.Player;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class GameOverException extends AbstractThrowableProblem {

    public GameOverException(Player p) {
        super(
                null,
                "Game is over",
                Status.BAD_REQUEST,
                String.format("The game is over(%s won)", p == null ? "nobody" : p.toString()));
    }
}

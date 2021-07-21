package ir.mastcheshmi.kalah.exceptions;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class GameNotFoundException extends AbstractThrowableProblem {

    public GameNotFoundException(int gameId) {
        super(
                null,
                "Not found",
                Status.NOT_FOUND,
                String.format("The game id(%d) was not found", gameId));
    }
}

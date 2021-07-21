package ir.mastcheshmi.kalah.exceptions;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class InvalidMoveException extends AbstractThrowableProblem {

    public InvalidMoveException(final String message) {
        super(
                null,
                "Invalid move",
                Status.BAD_REQUEST,
                message);
    }
}

package ir.mastcheshmi.kalah.exceptions;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class InvalidPitException extends AbstractThrowableProblem {

    public InvalidPitException(final String message) {
        super(
                null,
                "Invalid pit",
                Status.BAD_REQUEST,
                message);
    }
}

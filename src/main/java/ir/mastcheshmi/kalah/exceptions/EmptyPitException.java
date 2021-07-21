package ir.mastcheshmi.kalah.exceptions;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class EmptyPitException extends AbstractThrowableProblem {

    public EmptyPitException(final String pitId) {
        super(
                null,
                "Invalid pit id",
                Status.BAD_REQUEST,
                String.format("The selected Pit(%s) is empty", pitId));
    }
}

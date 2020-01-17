package iths.glenn.drick.exception;

import javax.persistence.PersistenceException;

public class TripAlreadyExistException extends PersistenceException {

    public TripAlreadyExistException(String message) {
        super(message);
    }
}

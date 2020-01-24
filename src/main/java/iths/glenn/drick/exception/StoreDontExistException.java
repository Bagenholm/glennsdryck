package iths.glenn.drick.exception;

public class StoreDontExistException extends IllegalArgumentException {

    public StoreDontExistException(String s) {
        super(s);
    }
}

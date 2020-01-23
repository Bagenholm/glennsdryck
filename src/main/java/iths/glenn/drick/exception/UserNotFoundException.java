package iths.glenn.drick.exception;

public class UserNotFoundException extends IllegalArgumentException{

    public UserNotFoundException(String message){
        super(message);
    }
}

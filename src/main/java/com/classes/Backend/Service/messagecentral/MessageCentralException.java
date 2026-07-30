package com.classes.Backend.Service.messagecentral;

public class MessageCentralException extends RuntimeException {

    public MessageCentralException(String message) {
        super(message);
    }

    public MessageCentralException(String message, Throwable cause) {
        super(message, cause);
    }
}

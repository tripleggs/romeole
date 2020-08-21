package org.romeole.data.exception;

/**
 * @author gongzhou
 * @title: MissingParamException
 * @projectName romeole
 * @description: TODO
 * @date 2020/8/2116:19
 */
public class MissingParamException extends RuntimeException {

    public MissingParamException() {super();}

    protected String code;
    protected String message;

    public MissingParamException(String code, String message) {
        super();
        this.code = code;
        this.message = message;
    }

    public MissingParamException(String code, String message, Throwable t) {
        super();
        this.code = code;
        this.message = message;
    }

    public MissingParamException(String message) {
        super(message);
        this.message = message;
    }

    public MissingParamException(String message, Throwable t) {
        super(message, t);
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "MissingParamException [code=" + code + ", message=" + message + "]";
    }

}

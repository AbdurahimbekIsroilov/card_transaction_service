package uz.pdp.cardservice2.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResult<T> implements Serializable {
    private final boolean success;
    private String message;
    private T data;
    private List<ErrorResponseDTO> errors;


    //RESPONSE WITH BOOLEAN (SUCCESS OR FAIL)
    private ApiResult() {
        this.success = true;
    }


    //SUCCESS RESPONSE WITH DATA
    private ApiResult(T data) {
        this();
        this.data = data;
    }

    //SUCCESS RESPONSE WITH DATA AND MESSAGE
    private ApiResult(T data, String message) {
        this();
        this.data = data;
        this.message = message;
    }

    //SUCCESS RESPONSE WITH MESSAGE
    private ApiResult(String message, boolean msg) {
        this();
        this.message = message;
    }

    //ERROR RESPONSE WITH MESSAGE AND ERROR CODE
    private ApiResult(String errorMsg, Integer errorCode) {
        this.success = false;
        this.errors = Collections.singletonList(new ErrorResponseDTO(errorMsg, errorCode));
    }

    //ERROR RESPONSE WITH ERROR DATA LIST
    private ApiResult(List<ErrorResponseDTO> errors) {
        this.success = false;
        this.errors = errors;
    }

    public static <E> ApiResult<E> successResponse(E data) {
        return new ApiResult<>(data);
    }

    public static <E> ApiResult<E> successResponse(E data, String message) {
        return new ApiResult<>(data, message);
    }

    public static <E> ApiResult<E> successResponse() {
        return new ApiResult<>();
    }

    public static ApiResult<String> successResponseForMsg(String message) {
        return new ApiResult<>(message, true);
    }


    public static ApiResult<ErrorResponseDTO> errorResponse(String errorMsg, Integer errorCode) {
        return new ApiResult<>(errorMsg, errorCode);
    }

    public static ApiResult<ErrorResponseDTO> errorResponse(List<ErrorResponseDTO> errors) {
        return new ApiResult<>(errors);
    }
}

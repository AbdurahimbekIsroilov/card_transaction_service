package uz.pdp.cardservice2.payload;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter

public class ErrorResponseDTO {

    private String errorMsg;


    private Integer errorCode;

    public ErrorResponseDTO(String errorMsg, Integer errorCode) {
        this.errorMsg = errorMsg;
        this.errorCode = errorCode;
    }
}


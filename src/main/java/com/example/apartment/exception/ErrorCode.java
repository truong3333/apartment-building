package com.example.apartment.exception;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    INVALID_KEY(999,"Invalid key.", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1001,"User existed.", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1002,"Username need length > 8", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1002,"Password need length > 8", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1003,"User not existed.",HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1004,"UnAuthenticated.",HttpStatus.UNAUTHORIZED),
    ROLE_EXISTED(1005,"Role existed.",HttpStatus.BAD_REQUEST),
    ROLE_NULL(1006,"List roles is empty.",HttpStatus.BAD_REQUEST),
    ROLE_NOT_FOUND(1007,"Role not found.",HttpStatus.NOT_FOUND),
    PERMISSION_EXISTED(1008,"Permission existed.",HttpStatus.BAD_REQUEST),
    PERMISSION_NULL(1009,"List permission is empty.",HttpStatus.BAD_REQUEST),
    PERMISSION_NOT_FOUND(1010,"Permission not found.",HttpStatus.NOT_FOUND),
    LIST_USER_NULL(1011,"List user is empty.",HttpStatus.BAD_REQUEST),
    APARTMENT_EXISTED(1012,"Apartment existed.",HttpStatus.BAD_REQUEST),
    APARTMENT_NULL(1013,"List apartment is empty.", HttpStatus.BAD_REQUEST),
    APARTMENT_NOT_EXISTED(1014,"Apartment not exists.", HttpStatus.NOT_FOUND),
    ISREPRESENTATIVE_EXISTED(1015,"Resident is representative existed.",HttpStatus.BAD_REQUEST),
    APARTMENT_HISTORY_NULL(1016,"List apartment history is empty.",HttpStatus.BAD_REQUEST),
    APARTMENT_HISTORY_NOT_EXISTED(1017,"Apartment history not exists.", HttpStatus.NOT_FOUND),
    APARTMENT_HISTORY_EXISTED(1018,"Resident already existed in apartment.",HttpStatus.BAD_REQUEST),
    COST_EXISTED(1019,"Cost of monthly exists",HttpStatus.BAD_REQUEST),
    COST_NOT_EXISTED(1020,"Cost not exists.",HttpStatus.NOT_FOUND),
    MONTHLY_COST_NOT_EXISTED(1021,"Monthly cost not exists",HttpStatus.NOT_FOUND),
    MONTHLY_COST_EXISTED(1022,"Monthly cost exists.",HttpStatus.BAD_REQUEST),
    MONTHLY_COST_NULL(1023,"List monthly cost is empty.",HttpStatus.BAD_REQUEST),
    NOT_IN_APARTMENT(1024,"You are not in this room or have left before.",HttpStatus.BAD_REQUEST),
    REPORT_NOT_EXISTED(1025,"Report not existed", HttpStatus.NOT_FOUND),
    ISREPRESENTATIVE_NOT_EXISTED(1026,"Apartment not exists representative", HttpStatus.NOT_FOUND),;

    private int code;
    private String message;
    private HttpStatusCode statusCode;

    ErrorCode(int code,String message,HttpStatusCode statusCode){
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

}

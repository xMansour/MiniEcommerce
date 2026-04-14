package us.exequt.ecommerce.shared;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RestResponse<T> {

    private boolean success;
    private String message;
    private T data;
    private List<String> errors;

    public static <T> RestResponse<T> success(T data) {
        return RestResponse.<T>builder()
                .success(true)
                .data(data)
                .build();
    }

    public static <T> RestResponse<T> success(String message, T data) {
        return RestResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> RestResponse<T> error(String message) {
        return RestResponse.<T>builder()
                .success(false)
                .message(message)
                .build();
    }

    public static <T> RestResponse<T> error(String message, T data) {
        return RestResponse.<T>builder()
                .success(false)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> RestResponse<T> error(String message, List<String> errors) {
        return RestResponse.<T>builder()
                .success(false)
                .message(message)
                .errors(errors)
                .build();
    }
}

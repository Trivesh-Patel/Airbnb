package com.project.airBnb.airbnbApp.advices;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ApiError {
    private HttpStatus status;
    private String message;
    @JsonFormat(pattern = "hh:mm:ss a dd-MM-yyyy")
    private LocalDateTime timestamp;
    private List<String> subErrors;
}

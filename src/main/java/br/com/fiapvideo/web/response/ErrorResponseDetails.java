package br.com.fiapvideo.web.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ErrorResponseDetails {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String titulo;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int status;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> detail;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private long timestamp;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String message;

    public ErrorResponseDetails(String titulo, int status, List<String> detail, long timestamp) {
        this.titulo = titulo;
        this.status = status;
        this.detail = detail;
        this.timestamp = timestamp;
    }

    public ErrorResponseDetails(String message) {
        this.message = message;
    }
}

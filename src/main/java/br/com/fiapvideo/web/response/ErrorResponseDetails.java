package br.com.fiapvideo.web.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ErrorResponseDetails {

    private String titulo;

    private int status;

    private List<String> detail;

    private long timestamp;


}

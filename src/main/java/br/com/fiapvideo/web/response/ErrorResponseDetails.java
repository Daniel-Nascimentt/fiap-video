package br.com.fiapvideo.web.response;

import java.util.List;

public class ErrorResponseDetails {

    private String titulo;

    private int status;

    private List<String> detail;

    private long timestamp;

    public ErrorResponseDetails() {
    }


    public ErrorResponseDetails(String titulo, int status, List<String> detail, long timestamp) {
        this.titulo = titulo;
        this.status = status;
        this.detail = detail;
        this.timestamp = timestamp;
    }

    public String getTitulo() {
        return titulo;
    }

    public int getStatus() {
        return status;
    }

    public List<String> getDetail() {
        return detail;
    }

    public long getTimestamp() {
        return timestamp;
    }




}

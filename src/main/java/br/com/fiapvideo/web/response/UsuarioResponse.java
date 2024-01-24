package br.com.fiapvideo.web.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Schema
public class UsuarioResponse {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String nome;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String email;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDate dataNascimento;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ContaResponse conta;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime cadastradoEm;

}

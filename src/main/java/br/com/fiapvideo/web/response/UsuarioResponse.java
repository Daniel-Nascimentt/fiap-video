package br.com.fiapvideo.web.response;

import br.com.fiapvideo.useCases.domain.ContaDomain;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class UsuarioResponse {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String nome;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String email;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDate dataNascimento;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonManagedReference
    private ContaResponse conta;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime cadastradoEm;

}

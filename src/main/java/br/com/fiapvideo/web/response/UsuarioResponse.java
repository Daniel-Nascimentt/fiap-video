package br.com.fiapvideo.web.response;

import br.com.fiapvideo.useCases.domain.ContaDomain;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class UsuarioResponse {

    private String nome;

    private String email;

    private LocalDate dataNascimento;

    private ContaDomain conta;

    private LocalDateTime cadastradoEm;

    public UsuarioResponse(String nome, String email, LocalDate dataNascimento, ContaDomain conta, LocalDateTime cadastradoEm) {
        this.nome = nome;
        this.email = email;
        this.dataNascimento = dataNascimento;
        this.conta = conta;
        this.cadastradoEm = cadastradoEm;
    }

}

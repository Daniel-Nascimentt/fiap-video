package br.com.fiapvideo.web.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UsuarioRequest {


    @NotBlank(message = "O nome deve ser preenchido!")
    @Size(min = 3)
    private String nome;

    @Email(message = "O e-mail precisa ser válido.")
    private String email;

    @NotNull(message = "Preencha a data de nascimento.")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Past(message = "A data de nascimento precisa estar no passado.")
    private LocalDate dataNascimento;


    public UsuarioRequest(String nome, String email, LocalDate dataNascimento) {
        this.nome = nome;
        this.email = email;
        this.dataNascimento = dataNascimento;
    }
}

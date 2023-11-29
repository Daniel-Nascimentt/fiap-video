package br.com.fiapvideo.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
public class UsuarioDomain {

    private String id;

    @NotBlank(message = "O nome deve ser preenchido!")
    @Min(value = 3)
    private String nome;

    @Email(message = "O e-mail precisa ser válido.")
    private String email;

    @NotNull(message = "Preencha a data de nascimento.")
    private LocalDate dataNascimento;

    @NotNull(message = "Uma conta precisa ser criada.")
    private ContaDomain conta;

}

package br.com.fiapvideo.useCases.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Document(value = "usuarios")
@NoArgsConstructor
public class UsuarioDomain {

    @Id
    private String id;

    @NotBlank(message = "O nome deve ser preenchido!")
    @Min(value = 3)
    private String nome;

    @Email(message = "O e-mail precisa ser válido.")
    private String email;

    @NotNull(message = "Preencha a data de nascimento.")
    private LocalDate dataNascimento;

    @NotNull(message = "Uma conta precisa ser criada.")
    @DBRef
    private ContaDomain conta;

    @NotNull(message = "Precisa de uma data de cadastro")
    private LocalDateTime cadastradoEm;

    public UsuarioDomain(String nome, String email, LocalDate dataNascimento, ContaDomain conta, LocalDateTime cadastradoEm) {
        this.nome = nome;
        this.email = email;
        this.dataNascimento = dataNascimento;
        this.conta = conta;
        this.cadastradoEm = cadastradoEm;
    }

}

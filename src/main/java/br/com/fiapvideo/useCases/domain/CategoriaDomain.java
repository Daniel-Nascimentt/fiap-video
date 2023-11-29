package br.com.fiapvideo.useCases.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CategoriaDomain {

    @NotBlank(message = "A categoria precisa ter um nome.")
    private String nome;

}

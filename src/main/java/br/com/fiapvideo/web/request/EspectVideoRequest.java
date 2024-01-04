package br.com.fiapvideo.web.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class EspectVideoRequest {

    @NotBlank(message = "Insira o id do video que sera visualizado!")
    private String videoId;

    @NotBlank(message = "Insira o email de quem vai visualizar video!")
    private String emailTelespectador;

}

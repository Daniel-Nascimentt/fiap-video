package br.com.fiapvideo.web.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PerformanceResponse {

    private Long visualizacoes;

    private Long marcadoFavorito;
}

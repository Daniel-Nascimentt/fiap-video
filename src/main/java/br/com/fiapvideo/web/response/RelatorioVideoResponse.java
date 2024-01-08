package br.com.fiapvideo.web.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
public class RelatorioVideoResponse {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Long totalVideos;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Long totalMarcadoFavorito;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Long totalViews;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Long mediaViewsPorVideo;

}

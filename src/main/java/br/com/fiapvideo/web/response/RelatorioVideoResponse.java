package br.com.fiapvideo.web.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Schema
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

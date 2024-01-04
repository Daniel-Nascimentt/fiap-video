package br.com.fiapvideo.web.response;

/**
 * Esta é uma interface genérica que define um método para conversao de domain para response.
 *
 * @param <T> O tipo do argumento que será passado para o método.
 * @param <R> O tipo de retorno que o método irá fornecer.
 */
public interface ToResponse<T, R> {

    R toResponse(T domain);

    R toResponseUpdate(T domain);

}

package br.com.fiapvideo.web.controller.advice;

import br.com.fiapvideo.exceptions.PublicadorNaoCorrespondeException;
import br.com.fiapvideo.exceptions.UsuarioNotFoundException;
import br.com.fiapvideo.exceptions.VideoNotFoundException;
import br.com.fiapvideo.web.response.ErrorResponseDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class HandlerExceptionsTest {

    @InjectMocks
    private HandlerExceptions handlerExceptions;

    private UsuarioNotFoundException usuarioNotFoundException;
    private VideoNotFoundException videoNotFoundException;
    private MethodArgumentNotValidException methodArgumentNotValidException;
    private PublicadorNaoCorrespondeException publicadorNaoCorrespondeException;
    private Exception genericException;

    @BeforeEach
    public void setup() {
        usuarioNotFoundException = new UsuarioNotFoundException();
        videoNotFoundException = new VideoNotFoundException();
        publicadorNaoCorrespondeException = new PublicadorNaoCorrespondeException("Publicador não corresponde");
        genericException = new Exception("Erro genérico");

        List<FieldError> fieldErrors = new ArrayList<>();
        fieldErrors.add(new FieldError("fakeObject", "fieldName", "mensagem de erro"));
        BindingResult bindingResult = new org.springframework.validation.BeanPropertyBindingResult(null, "fakeObject");
        fieldErrors.forEach(bindingResult::addError);

        methodArgumentNotValidException = new MethodArgumentNotValidException(null, bindingResult);
    }

    @DisplayName(value = "Dispara response entity apropriado para usuarioNotFoundException.")
    @Test
    public void usuarioNotFoundException() {
        ResponseEntity<?> responseEntity = handlerExceptions.usuarioNotFoundException(usuarioNotFoundException);
        assertErrorResponse(HttpStatus.NOT_FOUND, "Usuário não encontrado!!", responseEntity);
    }

    @DisplayName(value = "Dispara response entity apropriado para videoNotFoundException.")
    @Test
    public void videoNotFoundException() {
        ResponseEntity<?> responseEntity = handlerExceptions.videoNotFoundException(videoNotFoundException);
        assertErrorResponse(HttpStatus.NOT_FOUND, "Vídeo não encontrado!!", responseEntity);
    }

    @DisplayName(value = "Dispara response entity apropriado para methodArgumentNotValidException.")
    @Test
    public void methodArgumentNotValidException() {
        ResponseEntity<?> responseEntity = handlerExceptions.methodArgumentNotValidException(methodArgumentNotValidException);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertTrue(responseEntity.getBody() instanceof ErrorResponseDetails);
        ErrorResponseDetails errorResponseDetails = (ErrorResponseDetails) responseEntity.getBody();
        assertEquals("Por favor, verifique se todos os campos foram preenchidos corretamente!",
                errorResponseDetails.getTitulo());
        assertNotNull(errorResponseDetails.getDetail());
        assertFalse(errorResponseDetails.getDetail().isEmpty());
    }

    @DisplayName(value = "Dispara response entity apropriado para publicadorNaoCorrespondeException.")
    @Test
    public void publicadorNaoCorrespondeException() {
        ResponseEntity<?> responseEntity = handlerExceptions.publicadorNaoCorrespondeException(publicadorNaoCorrespondeException);
        assertErrorResponse(HttpStatus.BAD_REQUEST, "Publicador não corresponde", responseEntity);
    }

    @DisplayName(value = "Dispara response entity apropriado para genericException.")
    @Test
    public void genericException() {
        ResponseEntity<?> responseEntity = handlerExceptions.genericException(genericException);
        assertErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Algo inesperado aconteceu!!", responseEntity);
    }

    private void assertErrorResponse(HttpStatus expectedStatus, String expectedMessage, ResponseEntity<?> responseEntity) {
        assertNotNull(responseEntity);
        assertEquals(expectedStatus, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertTrue(responseEntity.getBody() instanceof ErrorResponseDetails);
        ErrorResponseDetails errorResponseDetails = (ErrorResponseDetails) responseEntity.getBody();
        assertEquals(expectedMessage, errorResponseDetails.getMessage());
    }

}

package com.agendei.backend.infra;

import com.agendei.backend.dto.ErroDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice // Avisa ao Spring: "Eu sou o responsável por tratar erros globais"
public class TratadorDeErros extends ResponseEntityExceptionHandler {

    // 1. Trata erros de Validação (@Valid, @NotBlank, @Email) - Erro 400
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        // Pega todos os erros de campo e cria uma lista de mensagens amigáveis
        List<String> listaDeErros = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(erro -> "Campo '" + erro.getField() + "': " + erro.getDefaultMessage())
                .collect(Collectors.toList());

        ErroDTO erroResposta = new ErroDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Erro de validação nos dados enviados.",
                LocalDateTime.now(),
                listaDeErros
        );

        return handleExceptionInternal(ex, erroResposta, headers, status, request);
    }

    // 2. Trata erros de Regra de Negócio (ex: E-mail duplicado) - Erro 409, 404, etc.
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErroDTO> handleResponseStatusException(ResponseStatusException ex) {

        ErroDTO erroResposta = new ErroDTO(
                ex.getStatusCode().value(),
                ex.getReason(), // A mensagem que colocamos no Service ("Email já cadastrado")
                LocalDateTime.now(),
                null // Sem lista de detalhes extras
        );

        return ResponseEntity.status(ex.getStatusCode()).body(erroResposta);
    }
}
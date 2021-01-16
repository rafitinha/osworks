package br.com.osworks.api.exceptionhandler;

import br.com.osworks.domain.exception.EntidadeNaoEncontradaException;
import br.com.osworks.domain.exception.NegocioException;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(NegocioException.class)
    public ResponseEntity<Object> handleNegocio(NegocioException ex, WebRequest request){
         return handleExceptionInternal(ex, getProblema(ex,HttpStatus.BAD_REQUEST), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ResponseEntity<Object> handleEntidadeNaoEncontrada(NegocioException ex, WebRequest request){
        return handleExceptionInternal(ex, getProblema(ex,HttpStatus.NOT_FOUND), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        var campos = new ArrayList<Campo>();
        for (ObjectError error: ex.getBindingResult().getAllErrors()){
            //String mensagem = error.getDefaultMessage();
            String mensagem = messageSource.getMessage(error, LocaleContextHolder.getLocale());
            String nome = ((FieldError)error).getField();

            campos.add(new Campo(nome,mensagem));
        }

        var problema = new Problema();
        problema.setStatus(status.value());
        problema.setTitulo("Um ou mais campos estão inválidos. " +
                "Faça o preenchimento correto e tente novamente");
        problema.setDataHora(OffsetDateTime.now());
        problema.setCampos(campos);


        return super.handleExceptionInternal(ex, problema, headers, status,request);
    }

    public Problema getProblema(Exception ex,HttpStatus status){

        Problema problema = new Problema();
        problema.setStatus(status.value());
        problema.setTitulo(ex.getMessage());
        problema.setDataHora(OffsetDateTime.now());

        return problema;
    }

}

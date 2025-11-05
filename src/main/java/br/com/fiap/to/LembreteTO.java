package br.com.fiap.to;

import br.com.fiap.enums.StatusLembrete;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * Classe que representa o lembrete a ser enviado por um colaborador para um paciente a respeito de um atendimento.
 * @version 2.0
 */
public class LembreteTO {
    private Long idLembrete;
    @NotNull (message = "O atributo id do Colaborador é obrigatório")
    private Long idColaborador;
    @NotNull (message = "O atributo id do Atendimento é obrigatório")
    private Long idAtendimento;
    private String assunto;
    private String mensagem;
    private LocalDate dataEnvio;
    private StatusLembrete status;

    public LembreteTO() {
    }

    public LembreteTO(Long idLembrete, Long idColaborador, Long idAtendimento, String assunto, String mensagem, LocalDate dataEnvio, StatusLembrete status) {
        this.idLembrete = idLembrete;
        this.idColaborador = idColaborador;
        this.idAtendimento = idAtendimento;
        this.assunto = assunto;
        this.mensagem = mensagem;
        this.dataEnvio = dataEnvio;
        this.status = status;
    }

    public Long getIdLembrete() {
        return idLembrete;
    }

    public void setIdLembrete(Long idLembrete) {
        this.idLembrete = idLembrete;
    }

    public Long getIdColaborador() {
        return idColaborador;
    }

    public void setIdColaborador(Long idColaborador) {
        this.idColaborador = idColaborador;
    }

    public Long getIdAtendimento() {
        return idAtendimento;
    }

    public void setIdAtendimento(Long idAtendimento) {
        this.idAtendimento = idAtendimento;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public LocalDate getDataEnvio() {
        return dataEnvio;
    }

    public void setDataEnvio(LocalDate dataEnvio) {
        this.dataEnvio = dataEnvio;
    }

    public StatusLembrete getStatus() {
        return status;
    }

    public void setStatus(StatusLembrete status) {
        this.status = status;
    }
}

package br.com.fiap.to;

import br.com.fiap.enums.FormatoAtendimento;
import br.com.fiap.enums.StatusAtendimento;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Classe que representa um atendimento, que pode ser feito por qualquer profissional da saúde a um paciente
 * @version 2.0
 *
 */
public class AtendimentoTO {
    private Long idAtendimento;
    @NotNull (message = "O atributo data do atendimento é obrigatorio")
    @FutureOrPresent(message = "A data do atendimento tem que ser no futuro")
    private LocalDate data;
    @FutureOrPresent(message = "A hora do atendimento tem que ser no futuro")
    @NotNull (message = "O atributo hora do atendimento é obrigatorio")
    private LocalTime hora;
    @NotBlank (message = "O atributo local é obrigatorio")
    private String local;
    @NotNull (message = "O atributo formato do atendimento é obrigatório")
    private FormatoAtendimento formatoAtendimento;
    private StatusAtendimento status;
    @NotNull (message = "O atributo id do Profissional da Saúde é obrigatorio")
    private Long idProfissionalSaude;
    @NotNull (message = "O atributo id do Paciente é obrigatorio")
    private Long idPaciente;

    public AtendimentoTO() {
    }

    public Long getIdAtendimento() {
        return idAtendimento;
    }

    public void setIdAtendimento(Long idAtendimento) {
        this.idAtendimento = idAtendimento;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public FormatoAtendimento getFormatoAtendimento() {
        return formatoAtendimento;
    }

    public void setFormatoAtendimento(FormatoAtendimento formatoAtendimento) {
        this.formatoAtendimento = formatoAtendimento;
    }

    public StatusAtendimento getStatus() {
        return status;
    }

    public void setStatus(StatusAtendimento status) {
        this.status = status;
    }

    public Long getIdProfissionalSaude() {
        return idProfissionalSaude;
    }

    public void setIdProfissionalSaude(Long idProfissionalSaude) {
        this.idProfissionalSaude = idProfissionalSaude;
    }

    public Long getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(Long idPaciente) {
        this.idPaciente = idPaciente;
    }
}

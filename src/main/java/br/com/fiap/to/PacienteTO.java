package br.com.fiap.to;

import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;

/**
 * Classe que representa um paciente no sistema, com dados pessoais, acompanhantes e
 * lembretes que recebem dos atendimentos
 * @version 2.0
 */
public class PacienteTO extends PessoaTO {
    private Long idPaciente;
    @NotBlank (message = "O atributo diagnostico é obrigatório")
    private String diagnostico;
    private ArrayList<AcompanhanteTO> acompanhante;
    private ArrayList<LembreteTO> lembrete;
    private ArrayList<AtendimentoTO> atendimento;

    public PacienteTO() {
    }

    public Long getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(Long idPaciente) {
        this.idPaciente = idPaciente;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public ArrayList<AcompanhanteTO> getAcompanhante() {
        return acompanhante;
    }

    public void setAcompanhante(ArrayList<AcompanhanteTO> acompanhante) {
        this.acompanhante = acompanhante;
    }

    public ArrayList<LembreteTO> getLembrete() {
        return lembrete;
    }

    public void setLembrete(ArrayList<LembreteTO> lembrete) {
        this.lembrete = lembrete;
    }

    public ArrayList<AtendimentoTO> getAtendimento() {
        return atendimento;
    }

    public void setAtendimento(ArrayList<AtendimentoTO> atendimento) {
        this.atendimento = atendimento;
    }
}

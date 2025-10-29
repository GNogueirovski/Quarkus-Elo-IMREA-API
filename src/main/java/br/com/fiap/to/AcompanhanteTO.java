package br.com.fiap.to;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Classe que representa o acompanhante de um paciente registrado.
 * @version 2.0
 */
public class AcompanhanteTO extends PessoaTO {
    private Long idAcompanhante;
    @NotNull (message = "O atributo id do paciente é obrigatório")
    private Long idPaciente;
    @NotBlank (message = "O atributo parentesco é obrigatório")
    private String parentesco;

    public AcompanhanteTO() {
    }

    public Long getIdAcompanhante() {
        return idAcompanhante;
    }

    public void setIdAcompanhante(Long idAcompanhante) {
        this.idAcompanhante = idAcompanhante;
    }

    public Long getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(Long idPaciente) {
        this.idPaciente = idPaciente;
    }

    public String getParentesco() {
        return parentesco;
    }

    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
    }
}

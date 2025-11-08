package br.com.fiap.to;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Classe que representa um profissional da saúde com as informações básicas herdadas e seus respectivos documentos
 * @version 3.0
 */
public class ProfissionalSaudeTO extends PessoaTO {
    private Long idProfissionalSaude;
    @NotBlank(message = "O atributo tipo do documento é obrigatório")
    @Size(max = 20,message = "O atributo tipo do documento pode ter no máximo 20 caracteres")
    private String tipoDocumento;
    @Size(max = 12, message = "O atributo documento pode ter no máximo 12 caracteres")
    @NotBlank(message = "O atributo documento é obrigatório")
    private String documento;
    @NotBlank(message = "O atributo especialidade é obrigatório")
    private String especialidade;

    public ProfissionalSaudeTO() {
    }

    public Long getIdProfissionalSaude() {
        return idProfissionalSaude;
    }

    public void setIdProfissionalSaude(Long idProfissionalSaude) {
        this.idProfissionalSaude = idProfissionalSaude;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }
}

package br.com.fiap.to;

/**
 * Classe para representar uma resposta do status HTTP e a mensagem da Exception em formato de objeto (para retornar um JSON).
 */
public class ErrorResponse {
    private int status;
    private String erro;

    public ErrorResponse(int status, String erro) {
        this.status = status;
        this.erro = erro;
    }

    public String getError() {
        return erro;
    }

    public Integer getStatus() {
        return status;
    }

}

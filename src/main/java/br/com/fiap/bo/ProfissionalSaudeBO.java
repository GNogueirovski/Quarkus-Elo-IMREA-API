package br.com.fiap.bo;

import br.com.fiap.dao.AtendimentoDAO;
import br.com.fiap.dao.ProfissionalSaudeDAO;
import br.com.fiap.exception.ProfissionalSaudeException;
import br.com.fiap.to.ProfissionalSaudeTO;

import java.util.ArrayList;

/**
 * Classe de com regras de negócio para gerenciar profissionais de saúde e utilizar os métodos DAO para realizar as ações com banco de dados.
 * @version 1.0
 */
public class ProfissionalSaudeBO {
    private ProfissionalSaudeDAO profissionalSaudeDAO;
    private AtendimentoDAO atendimentoDAO;

    /**
     * Salva um novo profissional de saúde no banco de dados após validar os dados.
     * @param profissionalSaudeTO Objeto ProfissionalSaudeTO contendo os dados do profissional de saúde a ser salvo.
     * @return O objeto ProfissionalSaudeTO salvo com o ID gerado.
     * @throws ProfissionalSaudeException Se já existir um profissional de saúde com o mesmo CPF ou documento.
     */
    public ProfissionalSaudeTO save(ProfissionalSaudeTO profissionalSaudeTO) throws ProfissionalSaudeException {
        String telefone = profissionalSaudeTO.getTelefone().replace("(", "").replace("-", "").replace(")", "").replace(" ", "");
        String cpf = profissionalSaudeTO.getCpf().replace(".", "").replace("-", "");

        profissionalSaudeTO.setCpf(cpf);
        profissionalSaudeTO.setTelefone(telefone);

        profissionalSaudeDAO = new ProfissionalSaudeDAO();
        if(profissionalSaudeDAO.findByCpf(cpf) != null) {
            throw new ProfissionalSaudeException("Já existe um profissional de saúde cadastrado com o CPF informado");
        }

        if (profissionalSaudeDAO.findByDocumento(profissionalSaudeTO.getDocumento()) != null) {
            throw new ProfissionalSaudeException("Já existe um profissional de saúde cadastrado com o documento informado");
        }

        return profissionalSaudeDAO.save(profissionalSaudeTO);
    }

    /**
     * Atualiza os dados de um profissional de saúde existente no banco de dados após validar os dados.
     * @param profissionalSaudeTO Objeto ProfissionalSaudeTO contendo os dados atualizados do profissional de saúde.
     * @return O objeto ProfissionalSaudeTO atualizado.
     * @throws ProfissionalSaudeException Se o profissional de saúde não existir ou se o CPF e/ou documento informado já pertencer a outro profissional.
     */
    public ProfissionalSaudeTO update(ProfissionalSaudeTO profissionalSaudeTO) throws ProfissionalSaudeException {
        String telefone = profissionalSaudeTO.getTelefone().replace("(", "").replace("-", "").replace(")", "").replace(" ", "");
        String cpf = profissionalSaudeTO.getCpf().replace(".", "").replace("-", "");

        profissionalSaudeTO.setCpf(cpf);
        profissionalSaudeTO.setTelefone(telefone);

        profissionalSaudeDAO = new ProfissionalSaudeDAO();

        if(profissionalSaudeDAO.findById(profissionalSaudeTO.getIdProfissionalSaude()) == null) {
            throw new ProfissionalSaudeException("Não existe nenhum profissional de saúde com o ID informado");
        }
        ProfissionalSaudeTO profissionalEncontrado = profissionalSaudeDAO.findByCpf(cpf);

        if (profissionalEncontrado != null && !profissionalEncontrado.getIdProfissionalSaude().equals(profissionalSaudeTO.getIdProfissionalSaude())) {
            throw new ProfissionalSaudeException("O CPF informado já pertence a outro profissional de saúde.");
        }

        ProfissionalSaudeTO profissionalDocumento = profissionalSaudeDAO.findByDocumento(profissionalSaudeTO.getDocumento());

        if (profissionalDocumento != null && !profissionalDocumento.getIdProfissionalSaude().equals(profissionalSaudeTO.getIdProfissionalSaude())) {
            throw new ProfissionalSaudeException("O documento informado já pertence a outro profissional de saúde.");
        }

        return profissionalSaudeDAO.update(profissionalSaudeTO);
    }

    /**
     * Exclui um profissional de saúde do banco de dados.
     * @param id O ID do profissional de saúde a ser excluído.
     * @return true se o profissional de saúde foi excluído com sucesso, false se o profissional não existir.
     * @throws ProfissionalSaudeException Se existirem atendimentos vinculados ao profissional de saúde.
     */

    public boolean delete(Long id) throws ProfissionalSaudeException {
        profissionalSaudeDAO = new ProfissionalSaudeDAO();
        if (profissionalSaudeDAO.findById(id) == null) {
            return false;
        }
        atendimentoDAO = new AtendimentoDAO();
        if(!atendimentoDAO.findAllByProfissional(id).isEmpty()){
            throw new ProfissionalSaudeException("Não é possível excluir o profissional de saúde pois existem atendimentos vinculados a ele.");
        }

        return profissionalSaudeDAO.delete(id);
    }

    /**
     * Recupera todos os profissionais de saúde do banco de dados.
     * @return Uma lista de ProfissionalSaudeTO representando todos os profissionais de saúde.
     */
    public ArrayList<ProfissionalSaudeTO> findAll() {
        profissionalSaudeDAO = new ProfissionalSaudeDAO();
        return profissionalSaudeDAO.findAll();
    }

    /**
     * Recupera um profissional de saúde pelo seu ID.
     * @param id O ID do profissional de saúde a ser recuperado.
     * @return ProfissionalSaudeTO correspondente ao ID informado, ou null se não encontrado.
     */
    public ProfissionalSaudeTO findById(Long id) {
        profissionalSaudeDAO = new ProfissionalSaudeDAO();
        return profissionalSaudeDAO.findById(id);
    }

}

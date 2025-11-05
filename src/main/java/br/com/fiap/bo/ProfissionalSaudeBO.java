package br.com.fiap.bo;

import br.com.fiap.dao.AtendimentoDAO;
import br.com.fiap.dao.ProfissionalSaudeDAO;
import br.com.fiap.exception.ProfissionalSaudeException;
import br.com.fiap.to.ProfissionalSaudeTO;

import java.util.ArrayList;

public class ProfissionalSaudeBO {
    private ProfissionalSaudeDAO profissionalSaudeDAO;
    private AtendimentoDAO atendimentoDAO;

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

        return profissionalSaudeDAO.update(profissionalSaudeTO);
    }
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

    public ArrayList<ProfissionalSaudeTO> findAll() {
        profissionalSaudeDAO = new ProfissionalSaudeDAO();
        return profissionalSaudeDAO.findAll();
    }

    public ProfissionalSaudeTO findById(Long id) {
        profissionalSaudeDAO = new ProfissionalSaudeDAO();
        return profissionalSaudeDAO.findById(id);
    }

}

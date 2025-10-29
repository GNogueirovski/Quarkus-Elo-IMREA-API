package br.com.fiap.bo;

import br.com.fiap.dao.ProfissionalSaudeDAO;
import br.com.fiap.to.ProfissionalSaudeTO;

import java.util.ArrayList;

public class ProfissionalSaudeBO {
    public ProfissionalSaudeTO save(ProfissionalSaudeTO profissionalSaudeTO) {
        String telefone = profissionalSaudeTO.getTelefone();
        String cpf = profissionalSaudeTO.getCpf();

        profissionalSaudeTO.setCpf(cpf.replace(".", "").replace("-", ""));
        profissionalSaudeTO.setTelefone(telefone.replace("(", "").replace("-", "").replace(")", "").replace(" ", ""));

        ProfissionalSaudeDAO profissionalSaudeDAO = new ProfissionalSaudeDAO();
        return profissionalSaudeDAO.save(profissionalSaudeTO);
    }

    public ProfissionalSaudeTO update(ProfissionalSaudeTO profissionalSaudeTO) {
        String telefone = profissionalSaudeTO.getTelefone();
        String cpf = profissionalSaudeTO.getCpf();

        profissionalSaudeTO.setCpf(cpf.replace(".", "").replace("-", ""));
        profissionalSaudeTO.setTelefone(telefone.replace("(", "").replace("-", "").replace(")", "").replace(" ", ""));

        ProfissionalSaudeDAO profissionalSaudeDAO = new ProfissionalSaudeDAO();
        return profissionalSaudeDAO.update(profissionalSaudeTO);
    }
    public boolean delete(Long id) {
        ProfissionalSaudeDAO profissionalSaudeDAO = new ProfissionalSaudeDAO();
        return profissionalSaudeDAO.delete(id);
    }

    public ArrayList<ProfissionalSaudeTO> findAll() {
        ProfissionalSaudeDAO profissionalSaudeDAO = new ProfissionalSaudeDAO();
        return profissionalSaudeDAO.findAll();
    }

    public ProfissionalSaudeTO findById(Long id) {
        ProfissionalSaudeDAO profissionalSaudeDAO = new ProfissionalSaudeDAO();
        return profissionalSaudeDAO.findById(id);
    }

}

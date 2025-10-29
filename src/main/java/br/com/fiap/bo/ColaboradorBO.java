package br.com.fiap.bo;

import br.com.fiap.dao.ColaboradorDAO;
import br.com.fiap.to.ColaboradorTO;

import java.util.ArrayList;

public class ColaboradorBO {
    public ColaboradorTO save(ColaboradorTO colaboradorTO) {
        String telefone = colaboradorTO.getTelefone();
        String cpf = colaboradorTO.getCpf();

        colaboradorTO.setCpf(cpf.replace(".", "").replace("-", ""));
        colaboradorTO.setTelefone(telefone.replace("(", "").replace("-", "").replace(")", "").replace(" ", ""));

        ColaboradorDAO colaboradorDAO = new ColaboradorDAO();
        return colaboradorDAO.save(colaboradorTO);
    }

    public ColaboradorTO update(ColaboradorTO colaboradorTO) {
        String telefone = colaboradorTO.getTelefone();
        String cpf = colaboradorTO.getCpf();
        colaboradorTO.setCpf(cpf.replace(".", "").replace("-", ""));
        colaboradorTO.setTelefone(telefone.replace("(", "").replace("-", "").replace(")", "").replace(" ", ""));

        ColaboradorDAO colaboradorDAO = new ColaboradorDAO();
        if (colaboradorDAO.findById(colaboradorTO.getIdColaborador()) == null) {
            throw new RuntimeException("Colaborador não encontrado");
        }
        return colaboradorDAO.update(colaboradorTO);
    }
    public boolean delete(Long id) {
        ColaboradorDAO colaboradorDAO = new ColaboradorDAO();
        return colaboradorDAO.delete(id);
    }

    public ArrayList<ColaboradorTO> findAll() {
        ColaboradorDAO colaboradorDAO = new ColaboradorDAO();
        return colaboradorDAO.findAll();
    }

    public ColaboradorTO findById(Long id) {
        ColaboradorDAO colaboradorDAO = new ColaboradorDAO();
        return colaboradorDAO.findById(id);
    }

}

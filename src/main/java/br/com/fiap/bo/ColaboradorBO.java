package br.com.fiap.bo;

import br.com.fiap.dao.AtendimentoDAO;
import br.com.fiap.dao.ColaboradorDAO;
import br.com.fiap.dao.LembreteDAO;
import br.com.fiap.exception.ColaboradorException;
import br.com.fiap.to.ColaboradorTO;

import java.util.ArrayList;

public class ColaboradorBO {
    private ColaboradorDAO colaboradorDAO;

    public ColaboradorTO save(ColaboradorTO colaboradorTO) throws ColaboradorException {
        String telefone = colaboradorTO.getTelefone().replace("(", "").replace("-", "").replace(")", "").replace(" ", "");
        String cpf = colaboradorTO.getCpf().replace(".", "").replace("-", "");

        colaboradorTO.setCpf(cpf);
        colaboradorTO.setTelefone(telefone);

        colaboradorDAO = new ColaboradorDAO();

        if (colaboradorDAO.findByCpf(cpf) != null) {
            throw new ColaboradorException("O CPF informado já pertence a outro colaborador.");
        }

        return colaboradorDAO.save(colaboradorTO);
    }

    public ColaboradorTO update(ColaboradorTO colaboradorTO) throws ColaboradorException {
        String telefone = colaboradorTO.getTelefone().replace("(", "").replace("-", "").replace(")", "").replace(" ", "");
        String cpf = colaboradorTO.getCpf().replace(".", "").replace("-", "");

        colaboradorTO.setCpf(cpf);
        colaboradorTO.setTelefone(telefone);

        colaboradorDAO = new ColaboradorDAO();
        if (colaboradorDAO.findById(colaboradorTO.getIdColaborador()) == null) {
            throw new ColaboradorException("Não existe nenhum colaborador com o ID informado para ser atualizado");
        }
        ColaboradorTO colaboradorEncontrado = colaboradorDAO.findByCpf(cpf);
        if (colaboradorEncontrado != null && !colaboradorEncontrado.getIdColaborador().equals(colaboradorTO.getIdColaborador())) {
            throw new ColaboradorException("O CPF informado já pertence a outro colaborador.");
        }

        return colaboradorDAO.update(colaboradorTO);
    }
    public boolean delete(Long id) throws ColaboradorException {
        colaboradorDAO = new ColaboradorDAO();
        if (colaboradorDAO.findById(id) == null) {
            return false;
        }
        LembreteDAO lembreteDAO = new LembreteDAO();
        if(!lembreteDAO.findAllByColaborador(id).isEmpty()){
            throw new ColaboradorException("Não é possível excluir o colaborador pois existem lembretes vinculados a ele.");
        }

        return colaboradorDAO.delete(id);
    }

    public ArrayList<ColaboradorTO> findAll() {
        colaboradorDAO = new ColaboradorDAO();
        return colaboradorDAO.findAll();
    }

    public ColaboradorTO findById(Long id) {
        colaboradorDAO = new ColaboradorDAO();
        return colaboradorDAO.findById(id);
    }

}

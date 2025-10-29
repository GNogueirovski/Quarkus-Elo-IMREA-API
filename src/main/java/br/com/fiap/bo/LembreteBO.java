package br.com.fiap.bo;

import br.com.fiap.dao.LembreteDAO;
import br.com.fiap.dao.PacienteDAO;
import br.com.fiap.to.LembreteTO;

import java.util.ArrayList;

public class LembreteBO {
    public LembreteTO save(LembreteTO lembreteTO) {
        LembreteDAO lembreteDAO = new LembreteDAO();
        return lembreteDAO.save(lembreteTO);
    }

    public LembreteTO update(LembreteTO lembreteTO) {
        LembreteDAO lembreteDAO = new LembreteDAO();
        if (lembreteDAO.findById(lembreteTO.getIdLembrete()) == null) {
            throw new RuntimeException("Lembrete não encontrado");
        }
        return lembreteDAO.update(lembreteTO);
    }
    public boolean delete(Long id) {
        LembreteDAO lembreteDAO = new LembreteDAO();
        return lembreteDAO.delete(id);
    }
    public LembreteTO findById(Long id) {
        LembreteDAO lembreteDAO = new LembreteDAO();
        return lembreteDAO.findById(id);
    }

    //todo verificar se existe paciente, se nao nulo pro status code correto
    public ArrayList<LembreteTO> findAllByPaciente(Long idPaciente) {
        PacienteDAO pacienteDAO = new PacienteDAO();
        if (pacienteDAO.findById(idPaciente) == null) {
            throw new RuntimeException("nao tem esse maluco");
        }
        LembreteDAO lembreteDAO = new LembreteDAO();
        return lembreteDAO.findAllByPaciente(idPaciente);
    }

}

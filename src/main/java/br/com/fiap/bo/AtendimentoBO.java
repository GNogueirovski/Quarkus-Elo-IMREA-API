package br.com.fiap.bo;

import br.com.fiap.dao.AtendimentoDAO;
import br.com.fiap.dao.PacienteDAO;
import br.com.fiap.to.AtendimentoTO;

import java.util.ArrayList;

public class AtendimentoBO {
    public AtendimentoTO save(AtendimentoTO atendimentoTO) {

        AtendimentoDAO atendimentoDAO = new AtendimentoDAO();
        return atendimentoDAO.save(atendimentoTO);
    }

    public AtendimentoTO update(AtendimentoTO atendimentoTO) {
        AtendimentoDAO atendimentoDAO = new AtendimentoDAO();
        if (atendimentoDAO.findById(atendimentoTO.getIdAtendimento()) == null) {
            throw new RuntimeException("Atendimento não encontrado");
        }
        return atendimentoDAO.update(atendimentoTO);
    }
    public boolean delete(Long id) {
        AtendimentoDAO atendimentoDAO = new AtendimentoDAO();
        return atendimentoDAO.delete(id);
    }

    public ArrayList<AtendimentoTO> findAll() {
        AtendimentoDAO atendimentoDAO = new AtendimentoDAO();
        return atendimentoDAO.findAll();
    }

    public AtendimentoTO findById(Long id) {
        AtendimentoDAO atendimentoDAO = new AtendimentoDAO();
        return atendimentoDAO.findById(id);
    }

    /**
     * Busca todos os atendimentos de um paciente
     * @param idPaciente ID do paciente
     * @return lista de objetos com os dados dos atendimentos encontrados ou null se o paciente não existir
     */
    public ArrayList<AtendimentoTO> findAllByPaciente(Long idPaciente) {
        PacienteDAO pacienteDAO = new PacienteDAO();
        if (pacienteDAO.findById(idPaciente) == null) {
            return null; // Paciente não encontrado
        }

        AtendimentoDAO atendimentoDAO = new AtendimentoDAO();
        return atendimentoDAO.findAllByPaciente(idPaciente);
    }

}

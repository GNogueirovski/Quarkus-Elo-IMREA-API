package br.com.fiap.bo;

import br.com.fiap.dao.PacienteDAO;
import br.com.fiap.to.PacienteTO;

import java.util.ArrayList;

public class PacienteBO {
    public PacienteTO save(PacienteTO pacienteTO) {
        String telefone = pacienteTO.getTelefone();
        String cpf = pacienteTO.getCpf();

        pacienteTO.setCpf(cpf.replace(".", "").replace("-", ""));
        pacienteTO.setTelefone(telefone.replace("(", "").replace("-", "").replace(")", "").replace(" ", ""));

        PacienteDAO pacienteDAO = new PacienteDAO();
        return pacienteDAO.save(pacienteTO);
    }

    public PacienteTO update(PacienteTO pacienteTO) {
        String telefone = pacienteTO.getTelefone();
        String cpf = pacienteTO.getCpf();

        pacienteTO.setCpf(cpf.replace(".", "").replace("-", ""));
        pacienteTO.setTelefone(telefone.replace("(", "").replace("-", "").replace(")", "").replace(" ", ""));

        PacienteDAO pacienteDAO = new PacienteDAO();
        return pacienteDAO.update(pacienteTO);
    }
    public boolean delete(Long id) {
        PacienteDAO pacienteDAO = new PacienteDAO();
        return pacienteDAO.delete(id);
    }

    public ArrayList<PacienteTO> findAll() {
        PacienteDAO pacienteDAO = new PacienteDAO();
        return pacienteDAO.findAll();
    }

    public PacienteTO findById(Long id) {
        PacienteDAO pacienteDAO = new PacienteDAO();
        return pacienteDAO.findById(id);
    }

    public PacienteTO findByAcompanhante(Long idAcompanhante) {
        PacienteDAO pacienteDAO = new PacienteDAO();
        return pacienteDAO.findByAcompanhante(idAcompanhante);
    }
}

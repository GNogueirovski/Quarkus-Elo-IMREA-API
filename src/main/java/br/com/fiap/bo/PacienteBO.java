package br.com.fiap.bo;

import br.com.fiap.dao.AcompanhanteDAO;
import br.com.fiap.dao.PacienteDAO;
import br.com.fiap.exception.AcompanhanteException;
import br.com.fiap.exception.PacienteException;
import br.com.fiap.to.PacienteTO;

import java.util.ArrayList;

public class PacienteBO {
    public PacienteTO save(PacienteTO pacienteTO) throws PacienteException {
        String telefone = pacienteTO.getTelefone();
        pacienteTO.setTelefone(telefone.replace("(", "").replace("-", "").replace(")", "").replace(" ", ""));

        String cpf = pacienteTO.getCpf();
        pacienteTO.setCpf(cpf.replace(".", "").replace("-", ""));

        PacienteDAO pacienteDAO = new PacienteDAO();

        if (pacienteDAO.findByCpf(pacienteTO.getCpf()) != null) {
            throw new PacienteException("Já existe um paciente cadastrado com o CPF informado");
        }


        return pacienteDAO.save(pacienteTO);
    }

    public PacienteTO update(PacienteTO pacienteTO) throws PacienteException {
        PacienteDAO pacienteDAO = new PacienteDAO();

        if (pacienteDAO.findById(pacienteTO.getIdPaciente()) == null) {
            throw new PacienteException("Não existe nenhum paciente com o ID informado para ser atualizado");
        }

        String telefone = pacienteTO.getTelefone();
        String cpf = pacienteTO.getCpf();

        pacienteTO.setCpf(cpf.replace(".", "").replace("-", ""));
        pacienteTO.setTelefone(telefone.replace("(", "").replace("-", "").replace(")", "").replace(" ", ""));

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

    public PacienteTO findByAcompanhante(Long idAcompanhante) throws AcompanhanteException {
        PacienteDAO pacienteDAO = new PacienteDAO();
        AcompanhanteDAO acompanhanteDAO = new AcompanhanteDAO();
        if(acompanhanteDAO.findById(idAcompanhante) == null) {
            throw new AcompanhanteException("Não existe nenhum acompanhante com o ID informado");
        }
        return pacienteDAO.findByAcompanhante(idAcompanhante);
    }
}

package br.com.fiap.bo;

import br.com.fiap.dao.AcompanhanteDAO;
import br.com.fiap.dao.AtendimentoDAO;
import br.com.fiap.dao.PacienteDAO;
import br.com.fiap.exception.AcompanhanteException;
import br.com.fiap.exception.PacienteException;
import br.com.fiap.to.AtendimentoTO;
import br.com.fiap.to.PacienteTO;

import java.util.ArrayList;

public class PacienteBO {
    private PacienteDAO pacienteDAO;
    private AtendimentoDAO atendimentoDAO;
    private AcompanhanteDAO acompanhanteDAO;

    public PacienteTO save(PacienteTO pacienteTO) throws PacienteException {
        String telefone = pacienteTO.getTelefone().replace("(", "").replace("-", "").replace(")", "").replace(" ", "");
        String cpf = pacienteTO.getCpf().replace(".", "").replace("-", "");

        pacienteTO.setTelefone(telefone);
        pacienteTO.setCpf(cpf);

        pacienteDAO = new PacienteDAO();

        if (pacienteDAO.findByCpf(cpf) != null) {
            throw new PacienteException("Já existe um paciente cadastrado com o CPF informado");
        }


        return pacienteDAO.save(pacienteTO);
    }

    public PacienteTO update(PacienteTO pacienteTO) throws PacienteException {

        String telefone = pacienteTO.getTelefone().replace("(", "").replace("-", "").replace(")", "").replace(" ", "");
        String cpf = pacienteTO.getCpf().replace(".", "").replace("-", "");

        pacienteTO.setCpf(cpf);
        pacienteTO.setTelefone(telefone);

        pacienteDAO = new PacienteDAO();
        if (pacienteDAO.findById(pacienteTO.getIdPaciente()) == null) {
            throw new PacienteException("Não existe nenhum paciente com o ID informado para ser atualizado");
        }

        PacienteTO pacienteEncontrado = pacienteDAO.findByCpf(cpf);
        if (pacienteEncontrado != null && !pacienteEncontrado.getIdPaciente().equals(pacienteTO.getIdPaciente())) {
            throw new PacienteException("O CPF informado já pertence a outro paciente.");
        }

        return pacienteDAO.update(pacienteTO);
    }
    public boolean delete(Long id) throws PacienteException {
        pacienteDAO = new PacienteDAO();
        if (pacienteDAO.findById(id) == null) {
            return false;
        }
        atendimentoDAO = new AtendimentoDAO();
        if (!atendimentoDAO.findAllByPaciente(id).isEmpty()) {
            throw new PacienteException("Não é possível excluir o paciente, pois existem atendimentos associados a ele.");
        }
        acompanhanteDAO = new AcompanhanteDAO();
        if (!acompanhanteDAO.findAllByPaciente(id).isEmpty()) {
            throw new PacienteException("Não é possível excluir o paciente, pois existem acompanhantes associados a ele.");
        }

        return pacienteDAO.delete(id);
    }

    public ArrayList<PacienteTO> findAll() {
        pacienteDAO = new PacienteDAO();
        return pacienteDAO.findAll();
    }

    public PacienteTO findById(Long id) {
        pacienteDAO = new PacienteDAO();
        return pacienteDAO.findById(id);
    }

    public PacienteTO findByAcompanhante(Long idAcompanhante) throws AcompanhanteException {
        pacienteDAO = new PacienteDAO();
        acompanhanteDAO = new AcompanhanteDAO();
        if(acompanhanteDAO.findById(idAcompanhante) == null) {
            throw new AcompanhanteException("Não existe nenhum acompanhante com o ID informado");
        }
        return pacienteDAO.findByAcompanhante(idAcompanhante);
    }
}

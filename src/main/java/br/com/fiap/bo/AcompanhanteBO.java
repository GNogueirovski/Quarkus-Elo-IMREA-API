package br.com.fiap.bo;

import br.com.fiap.dao.AcompanhanteDAO;
import br.com.fiap.dao.PacienteDAO;
import br.com.fiap.exception.AcompanhanteException;
import br.com.fiap.exception.PacienteException;
import br.com.fiap.to.AcompanhanteTO;

import java.time.LocalDate;
import java.util.ArrayList;

public class AcompanhanteBO {
    public AcompanhanteTO save(AcompanhanteTO acompanhanteTO) throws AcompanhanteException, PacienteException {

        String telefone = acompanhanteTO.getTelefone();
        String cpf = acompanhanteTO.getCpf();
        acompanhanteTO.setCpf(cpf.replace(".", "").replace("-", ""));
        acompanhanteTO.setTelefone(telefone.replace("(", "").replace("-", "").replace(")", "").replace(" ", ""));

        PacienteDAO pacienteDAO = new PacienteDAO();

        if (pacienteDAO.findById(acompanhanteTO.getIdPaciente()) == null) {
            throw new PacienteException("Não foi encontrado nenhum paciente para vincular a esse acompanhante");
        }
        AcompanhanteDAO acompanhanteDAO = new AcompanhanteDAO();

        if (acompanhanteDAO.findByCpf(acompanhanteTO.getCpf()) != null) {
            throw new AcompanhanteException("Já existe um acompanhante cadastrado com o CPF informado");
        }

        LocalDate dataLimiteNascimento = LocalDate.now().minusYears(18);
        LocalDate dataNascimento = acompanhanteTO.getDataNascimento();

        if (dataNascimento.isAfter(dataLimiteNascimento) || dataNascimento.minusDays(1).isEqual(dataLimiteNascimento)) {
            throw new AcompanhanteException("O acompanhante deve ter pelo menos 18 anos");
        }
        return acompanhanteDAO.save(acompanhanteTO);
    }

    public AcompanhanteTO update(AcompanhanteTO acompanhanteTO) throws AcompanhanteException, PacienteException {
        AcompanhanteDAO acompanhanteDAO = new AcompanhanteDAO();

        if (acompanhanteDAO.findById(acompanhanteTO.getIdAcompanhante()) == null) {
            throw new AcompanhanteException("Não existe nenhum acompanhante com o ID informado");
        }
        PacienteDAO pacienteDAO = new PacienteDAO();

        if (pacienteDAO.findById(acompanhanteTO.getIdPaciente()) == null) {
            throw new PacienteException("Não existe um paciente com o ID informado.");
        }

        String telefone = acompanhanteTO.getTelefone();
        String cpf = acompanhanteTO.getCpf();
        acompanhanteTO.setCpf(cpf.replace(".", "").replace("-", ""));
        acompanhanteTO.setTelefone(telefone.replace("(", "").replace("-", "").replace(")", "").replace(" ", ""));

        return acompanhanteDAO.update(acompanhanteTO);
    }

    public boolean delete(Long id) {
        AcompanhanteDAO acompanhanteDAO = new AcompanhanteDAO();
        return acompanhanteDAO.delete(id);
    }

    public ArrayList<AcompanhanteTO> findAll() {
        AcompanhanteDAO acompanhanteDAO = new AcompanhanteDAO();
        return acompanhanteDAO.findAll();
    }

    public AcompanhanteTO findById(Long id) {
        AcompanhanteDAO acompanhanteDAO = new AcompanhanteDAO();
        return acompanhanteDAO.findById(id);
    }

    public ArrayList<AcompanhanteTO> findAllByPaciente(Long idPaciente) throws PacienteException {
        PacienteDAO pacienteDAO = new PacienteDAO();
        if (pacienteDAO.findById(idPaciente) == null) {
            throw new PacienteException("Não existe nenhum paciente com o ID informado");
        }
        AcompanhanteDAO acompanhanteDAO = new AcompanhanteDAO();
        return acompanhanteDAO.findAllByPaciente(idPaciente);
    }

}

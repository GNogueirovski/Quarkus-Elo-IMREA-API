package br.com.fiap.bo;

import br.com.fiap.dao.AcompanhanteDAO;
import br.com.fiap.dao.PacienteDAO;
import br.com.fiap.exception.AcompanhanteException;
import br.com.fiap.exception.PacienteException;
import br.com.fiap.to.AcompanhanteTO;

import java.time.LocalDate;
import java.util.ArrayList;

public class AcompanhanteBO {
    private AcompanhanteDAO acompanhanteDAO;
    private PacienteDAO pacienteDAO;

    public AcompanhanteTO save(AcompanhanteTO acompanhanteTO) throws AcompanhanteException, PacienteException {

        String telefone = acompanhanteTO.getTelefone().replace("(", "").replace("-", "").replace(")", "").replace(" ", "");
        String cpf = acompanhanteTO.getCpf().replace(".", "").replace("-", "");
        acompanhanteTO.setCpf(cpf);
        acompanhanteTO.setTelefone(telefone);

        pacienteDAO = new PacienteDAO();

        if (pacienteDAO.findById(acompanhanteTO.getIdPaciente()) == null) {
            throw new PacienteException("Não foi encontrado nenhum paciente para vincular a esse acompanhante");
        }
        acompanhanteDAO = new AcompanhanteDAO();

        if (acompanhanteDAO.findByCpf(cpf) != null) {
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

        String telefone = acompanhanteTO.getTelefone().replace("(", "").replace("-", "").replace(")", "").replace(" ", "");
        String cpf = acompanhanteTO.getCpf().replace(".", "").replace("-", "");
        acompanhanteTO.setCpf(cpf);
        acompanhanteTO.setTelefone(telefone);

        acompanhanteDAO = new AcompanhanteDAO();

        pacienteDAO = new PacienteDAO();

        if (acompanhanteDAO.findById(acompanhanteTO.getIdAcompanhante()) == null) {
            throw new AcompanhanteException("Não existe nenhum acompanhante com o ID informado");
        }

        if (pacienteDAO.findById(acompanhanteTO.getIdPaciente()) == null) {
            throw new PacienteException("Não existe um paciente com o ID informado.");
        }

        AcompanhanteTO acompanhanteEncontrado = acompanhanteDAO.findByCpf(cpf);

        if (acompanhanteEncontrado != null && !acompanhanteEncontrado.getIdAcompanhante().equals(acompanhanteTO.getIdAcompanhante())) {
            throw new AcompanhanteException("O CPF informado já pertence a outro acompanhante.");
        }

        return acompanhanteDAO.update(acompanhanteTO);
    }

    public boolean delete(Long id) {
        acompanhanteDAO = new AcompanhanteDAO();
        return acompanhanteDAO.delete(id);
    }

    public ArrayList<AcompanhanteTO> findAll() {
        acompanhanteDAO = new AcompanhanteDAO();
        return acompanhanteDAO.findAll();
    }

    public AcompanhanteTO findById(Long id) {
        acompanhanteDAO = new AcompanhanteDAO();
        return acompanhanteDAO.findById(id);
    }

    public ArrayList<AcompanhanteTO> findAllByPaciente(Long idPaciente) throws PacienteException {
        pacienteDAO = new PacienteDAO();
        if (pacienteDAO.findById(idPaciente) == null) {
            throw new PacienteException("Não existe nenhum paciente com o ID informado");
        }
       acompanhanteDAO = new AcompanhanteDAO();
        return acompanhanteDAO.findAllByPaciente(idPaciente);
    }

}

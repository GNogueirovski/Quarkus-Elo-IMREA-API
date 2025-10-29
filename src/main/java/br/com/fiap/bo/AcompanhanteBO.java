package br.com.fiap.bo;

import br.com.fiap.dao.AcompanhanteDAO;
import br.com.fiap.dao.PacienteDAO;
import br.com.fiap.exception.AcompanhanteException;
import br.com.fiap.to.AcompanhanteTO;
import br.com.fiap.to.PacienteTO;

import java.time.LocalDate;
import java.util.ArrayList;

public class AcompanhanteBO {
    public AcompanhanteTO save(AcompanhanteTO acompanhanteTO) {
        try{
            LocalDate dataLimiteNascimento = LocalDate.now().minusYears(18);
            LocalDate dataNascimento = acompanhanteTO.getDataNascimento();


            PacienteDAO pacienteDAO = new PacienteDAO();
            PacienteTO pacienteEncontrado = pacienteDAO.findById( acompanhanteTO.getIdPaciente());

            if(pacienteEncontrado == null){
                throw new AcompanhanteException("Paciente: Não foi encontrado nenhum paciente para vincular a esse acompanhante");
            }

            if (dataNascimento.isAfter(dataLimiteNascimento) || dataNascimento.minusDays(1).isEqual(dataLimiteNascimento)) {
                throw new AcompanhanteException("Acompanhante: O acompanhante deve ter pelo menos 18 anos");
            }

            String telefone = acompanhanteTO.getTelefone();
            String cpf = acompanhanteTO.getCpf();
            acompanhanteTO.setCpf(cpf.replace(".", "").replace("-", ""));
            acompanhanteTO.setTelefone(telefone.replace("(", "").replace("-", "").replace(")", "").replace(" ", ""));

            AcompanhanteDAO acompanhanteDAO = new AcompanhanteDAO();
            return acompanhanteDAO.save(acompanhanteTO);

        } catch (AcompanhanteException e) {
            throw new RuntimeException(e);
        }
    }

    public AcompanhanteTO update(AcompanhanteTO acompanhanteTO) {
        String telefone = acompanhanteTO.getTelefone();
        String cpf = acompanhanteTO.getCpf();
        acompanhanteTO.setCpf(cpf.replace(".", "").replace("-", ""));
        acompanhanteTO.setTelefone(telefone.replace("(", "").replace("-", "").replace(")", "").replace(" ", ""));

        AcompanhanteDAO acompanhanteDAO = new AcompanhanteDAO();
        if (acompanhanteDAO.findById(acompanhanteTO.getIdAcompanhante()) == null) {
            throw new RuntimeException("Acompanhante não encontrado");
        }
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

    //todo verificar se existe paciente, se nao nulo pro status code correto
    public ArrayList<AcompanhanteTO> findAllByPaciente(Long idPaciente) {
        PacienteDAO pacienteDAO = new PacienteDAO();
        if (pacienteDAO.findById(idPaciente) == null) {
            throw new RuntimeException("nao tem esse maluco");
        }
        AcompanhanteDAO acompanhanteDAO = new AcompanhanteDAO();
        return acompanhanteDAO.findAllByPaciente(idPaciente);
    }

}

package br.com.fiap.bo;

import br.com.fiap.dao.AcompanhanteDAO;
import br.com.fiap.dao.PacienteDAO;
import br.com.fiap.exception.AcompanhanteException;
import br.com.fiap.exception.PacienteException;
import br.com.fiap.to.AcompanhanteTO;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Classe de com regras de negócio para gerenciar acompanhantes e utilizar os métodos DAO para realizar as ações com banco de dados.
 * @version 1.0
 */
public class AcompanhanteBO {
    private AcompanhanteDAO acompanhanteDAO;
    private PacienteDAO pacienteDAO;

    /**
     * Salva um novo acompanhante no sistema.
     * @param acompanhanteTO Objeto AcompanhanteTO contendo os dados do acompanhante a ser salvo.
     * @return Objeto AcompanhanteTO que foi salvo.
     * @throws AcompanhanteException Caso já exista um acompanhante com o mesmo CPF ou se o acompanhante for menor de 18 anos.
     * @throws PacienteException Caso o paciente vinculado não exista.
     */
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

    /**
     * Atualiza os dados de um acompanhante existente.
     * @param acompanhanteTO Objeto AcompanhanteTO com os dados atualizados.
     * @return Objeto AcompanhanteTO que foi atualizado.
     * @throws AcompanhanteException Caso o acompanhante não exista ou o CPF já pertença a outro acompanhante.
     * @throws PacienteException Caso o paciente vinculado não exista.
     */
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

    /**
     * Exclui um acompanhante pelo seu ID.
     * @param id ID do acompanhante a ser excluído.
     * @return true se o acompanhante foi excluído com sucesso, false se não foi encontrado.
     */
    public boolean delete(Long id) {
        acompanhanteDAO = new AcompanhanteDAO();
        if (acompanhanteDAO.findById(id) == null) {
            return false;
        }
        return acompanhanteDAO.delete(id);
    }

    /**
     * Busca todos os acompanhantes cadastrados.
     * @return Lista de todos os acompanhantes.
     */
    public ArrayList<AcompanhanteTO> findAll() {
        acompanhanteDAO = new AcompanhanteDAO();
        return acompanhanteDAO.findAll();
    }

    /**
     * Busca um acompanhante pelo seu ID.
     * @param id ID do acompanhante.
     * @return AcompanhanteTO correspondente ao ID informado, ou null se não encontrado.
     */
    public AcompanhanteTO findById(Long id) {
        acompanhanteDAO = new AcompanhanteDAO();
        return acompanhanteDAO.findById(id);
    }

    /**
     * Busca todos os acompanhantes vinculados a um paciente específico.
     * @param idPaciente ID do paciente.
     * @return Lista de acompanhantes vinculados ao paciente.
     * @throws PacienteException Se o paciente com o ID informado não existir.
     */
    public ArrayList<AcompanhanteTO> findAllByPaciente(Long idPaciente) throws PacienteException {
        pacienteDAO = new PacienteDAO();
        if (pacienteDAO.findById(idPaciente) == null) {
            throw new PacienteException("Não existe nenhum paciente com o ID informado");
        }
       acompanhanteDAO = new AcompanhanteDAO();
        return acompanhanteDAO.findAllByPaciente(idPaciente);
    }

}

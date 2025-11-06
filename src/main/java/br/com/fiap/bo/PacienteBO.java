package br.com.fiap.bo;

import br.com.fiap.dao.AcompanhanteDAO;
import br.com.fiap.dao.AtendimentoDAO;
import br.com.fiap.dao.PacienteDAO;
import br.com.fiap.exception.AcompanhanteException;
import br.com.fiap.exception.PacienteException;
import br.com.fiap.to.AtendimentoTO;
import br.com.fiap.to.PacienteTO;

import java.util.ArrayList;

/**
 * Classe de com regras de negócio para gerenciar pacientes e utilizar os métodos DAO para realizar as ações com banco de dados.
 * @version 1.0
 */
public class PacienteBO {
    private PacienteDAO pacienteDAO;
    private AtendimentoDAO atendimentoDAO;
    private AcompanhanteDAO acompanhanteDAO;

    /**
     * Salva um novo paciente no banco de dados após validar os dados fornecidos.
     * @param pacienteTO PacienteTO contendo os dados do paciente a ser salvo.
     * @return PacienteTO do paciente salvo.
     * @throws PacienteException se já existir um paciente com o mesmo CPF.
     */
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

    /**
     * Atualiza os dados de um paciente existente no banco de dados após validar os dados fornecidos.
     * @param pacienteTO PacienteTO contendo os dados do paciente a ser atualizado.
     * @return PacienteTO do paciente atualizado.
     * @throws PacienteException se não existir um paciente com o ID informado ou se o CPF já pertencer a outro paciente.
     */
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

    /**
     * Exclui um paciente do banco de dados após verificar se não há atendimentos ou acompanhantes associados a ele.
     * @param id ID do paciente a ser excluído.
     * @return true se o paciente foi excluído com sucesso, false se o paciente não foi encontrado.
     * @throws PacienteException se houver atendimentos ou acompanhantes associados ao paciente.
     */
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

    /**
     * Recupera todos os pacientes do banco de dados.
     * @return Lista de PacienteTO contendo todos os pacientes.
     */
    public ArrayList<PacienteTO> findAll() {
        pacienteDAO = new PacienteDAO();
        return pacienteDAO.findAll();
    }

    /**
     * Recupera um paciente pelo seu ID.
     * @param id ID do paciente a ser recuperado.
     * @return PacienteTO do paciente encontrado ou null se não for encontrado.
     */
    public PacienteTO findById(Long id) {
        pacienteDAO = new PacienteDAO();
        return pacienteDAO.findById(id);
    }

    /**
     * Recupera um paciente pelo ID do seu acompanhante.
     * @param idAcompanhante ID do acompanhante associado ao paciente.
     * @return PacienteTO do paciente encontrado.
     * @throws AcompanhanteException se não existir nenhum acompanhante com o ID informado.
     */
    public PacienteTO findByAcompanhante(Long idAcompanhante) throws AcompanhanteException {
        pacienteDAO = new PacienteDAO();
        acompanhanteDAO = new AcompanhanteDAO();
        if(acompanhanteDAO.findById(idAcompanhante) == null) {
            throw new AcompanhanteException("Não existe nenhum acompanhante com o ID informado");
        }
        return pacienteDAO.findByAcompanhante(idAcompanhante);
    }
}

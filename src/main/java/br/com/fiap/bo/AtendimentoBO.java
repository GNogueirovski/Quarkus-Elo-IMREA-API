package br.com.fiap.bo;

import br.com.fiap.dao.AtendimentoDAO;
import br.com.fiap.dao.LembreteDAO;
import br.com.fiap.dao.PacienteDAO;
import br.com.fiap.dao.ProfissionalSaudeDAO;
import br.com.fiap.enums.FormatoAtendimento;
import br.com.fiap.enums.StatusAtendimento;
import br.com.fiap.exception.AtendimentoException;
import br.com.fiap.exception.PacienteException;
import br.com.fiap.exception.ProfissionalSaudeException;
import br.com.fiap.to.AtendimentoTO;
import br.com.fiap.to.ProfissionalSaudeTO;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Classe de com regras de negócio para gerenciar atendimentos e utilizar os métodos DAO para realizar as ações com banco de dados.
 *
 * @version 1.0
 */
public class AtendimentoBO {
    private AtendimentoDAO atendimentoDAO;
    private PacienteDAO pacienteDAO;
    private ProfissionalSaudeDAO profissionalSaudeDAO;
    private LembreteDAO lembreteDAO;

    /**
     * Salva um novo atendimento após validar para evitar duplicação de consultas, se o paciente/profissional existe e se o atendimento é no futuro.
     *
     * @param atendimentoTO O objeto AtendimentoTO contendo os dados do atendimento a ser salvo.
     * @return O objeto AtendimentoTO salvo.
     * @throws ProfissionalSaudeException Se o profissional de saúde não existir ou já tiver outro atendimento no mesmo horário.
     * @throws PacienteException          Se o paciente não existir ou já tiver outro atendimento no mesmo horário.
     */
    public AtendimentoTO save(AtendimentoTO atendimentoTO) throws ProfissionalSaudeException, PacienteException, AtendimentoException {

        pacienteDAO = new PacienteDAO();

        if (pacienteDAO.findById(atendimentoTO.getIdPaciente()) == null) {
            throw new PacienteException("Não existe nenhum paciente com o ID informado");
        }

        profissionalSaudeDAO = new ProfissionalSaudeDAO();

        if (profissionalSaudeDAO.findById(atendimentoTO.getIdProfissionalSaude()) == null) {
            throw new ProfissionalSaudeException("Não existe nenhum profissional da saúde com o ID informado");
        }

        atendimentoDAO = new AtendimentoDAO();

        boolean formatoValido = atendimentoTO.getFormatoAtendimento() == FormatoAtendimento.PRESENCIAL ||
                atendimentoTO.getFormatoAtendimento() == FormatoAtendimento.REMOTO;

        if (!formatoValido) {
            throw new AtendimentoException("Formato de atendimento inválido.");
        }

        // verifica se o paciente ja tem outro atendimento no mesmo horario
        AtendimentoTO atendimentoPaciente = atendimentoDAO.findAllByPaciente(atendimentoTO.getIdPaciente()).stream()
                .filter(a -> a.getData().isEqual(atendimentoTO.getData()) && a.getHora().equals(atendimentoTO.getHora())
                        && (a.getStatus() == StatusAtendimento.MARCADO || a.getStatus() == StatusAtendimento.REMARCADO)).findFirst().orElse(null);
        if (atendimentoPaciente != null) {
            throw new PacienteException("Já existe um atendimento agendado para este paciente na data e hora informadas.");
        }

        // verifica se o profissional ja tem outro atendimento no mesmo horario
        AtendimentoTO atendimentoProfissional = atendimentoDAO.findAllByProfissional(atendimentoTO.getIdProfissionalSaude()).stream()
                .filter(a -> a.getData().isEqual(atendimentoTO.getData()) && a.getHora().equals(atendimentoTO.getHora())
                        && (a.getStatus() == StatusAtendimento.MARCADO || a.getStatus() == StatusAtendimento.REMARCADO)).findFirst().orElse(null);

        if (atendimentoProfissional != null) {
            throw new ProfissionalSaudeException("Já existe um atendimento agendado para este profissional da saúde na data e hora informadas.");
        }

        atendimentoTO.setStatus(StatusAtendimento.MARCADO);

        return atendimentoDAO.save(atendimentoTO);
    }

    /**
     * Atualiza um atendimento existente após validar as regras de negócio, como não permitir alteração de paciente, verificar conflitos de horário e status do atendimento.
     *
     * @param atendimentoTO O objeto AtendimentoTO contendo os dados atualizados do atendimento.
     * @return O objeto AtendimentoTO atualizado.
     * @throws ProfissionalSaudeException Caso não possua um profissional de saúde com o ID informado ou já tenha outro atendimento no mesmo horário.
     * @throws PacienteException Caso não possua um paciente com o ID informado ou já tenha outro atendimento no mesmo horário.
     * @throws AtendimentoException Caso o atendimento não exista, esteja cancelado/concluído, ou tente alterar o paciente.
     */
    public AtendimentoTO update(AtendimentoTO atendimentoTO) throws ProfissionalSaudeException, PacienteException, AtendimentoException {

        atendimentoDAO = new AtendimentoDAO();

        AtendimentoTO atendimentoEncontrado = atendimentoDAO.findById(atendimentoTO.getIdAtendimento());
        if (atendimentoEncontrado == null) {
            throw new AtendimentoException("Não existe nenhum atendimento com o ID informado.");
        }

        if (atendimentoEncontrado.getStatus() == StatusAtendimento.CANCELADO ||
                atendimentoEncontrado.getStatus() == StatusAtendimento.CONCLUIDO) {
            throw new AtendimentoException("Não é permitido alterar um atendimento que já foi Cancelado ou Concluído.");
        }

        if (!atendimentoEncontrado.getIdPaciente().equals(atendimentoTO.getIdPaciente())) {
            throw new AtendimentoException("Não é permitido alterar o paciente de um atendimento já existente.");
        }

        ProfissionalSaudeDAO profissionalSaudeDAO = new ProfissionalSaudeDAO();
        if (profissionalSaudeDAO.findById(atendimentoTO.getIdProfissionalSaude()) == null) {
            throw new ProfissionalSaudeException("Não existe nenhum profissional da saúde com o ID informado");
        }

        if (atendimentoTO.getStatus() != StatusAtendimento.CANCELADO) {
            // verifica se o paciente ja tem outro atendimento no mesmo horario
            AtendimentoTO atendimentoPaciente = atendimentoDAO.findAllByPaciente(atendimentoTO.getIdPaciente()).stream()
                    .filter(a -> a.getData().isEqual(atendimentoTO.getData()) && a.getHora().equals(atendimentoTO.getHora())
                            && (a.getStatus() == StatusAtendimento.MARCADO || a.getStatus() == StatusAtendimento.REMARCADO)
                            && !a.getIdAtendimento().equals(atendimentoTO.getIdAtendimento())) //
                    .findFirst().orElse(null);

            if (atendimentoPaciente != null) {
                throw new PacienteException("Paciente já possui outro atendimento agendado neste novo horário.");
            }

            // verifica se o profissional ja tem outro atendimento no mesmo horario
            AtendimentoTO atendimentoProfissional = atendimentoDAO.findAllByProfissional(atendimentoTO.getIdProfissionalSaude()).stream()
                    .filter(a -> a.getData().isEqual(atendimentoTO.getData()) && a.getHora().equals(atendimentoTO.getHora())
                            && (a.getStatus() == StatusAtendimento.MARCADO || a.getStatus() == StatusAtendimento.REMARCADO)
                            && !a.getIdAtendimento().equals(atendimentoTO.getIdAtendimento()))
                    .findFirst().orElse(null);

            if (atendimentoProfissional != null) {
                throw new ProfissionalSaudeException("Profissional já possui outro atendimento agendado neste novo horário.");
            }

            boolean dataOuHoraAlterada = !atendimentoEncontrado.getData().isEqual(atendimentoTO.getData()) ||
                    !atendimentoEncontrado.getHora().equals(atendimentoTO.getHora());

            if (dataOuHoraAlterada) {
                if (atendimentoEncontrado.getStatus() == StatusAtendimento.MARCADO ||
                        atendimentoEncontrado.getStatus() == StatusAtendimento.REMARCADO) {
                    atendimentoTO.setStatus(StatusAtendimento.REMARCADO);
                }
            } else {
                atendimentoTO.setStatus(atendimentoEncontrado.getStatus());
            }

        }

        return atendimentoDAO.update(atendimentoTO);
    }

    /**
     * Conclui um atendimento existente, alterando seu status para CONCLUIDO, desde que esteja atualmente marcado ou remarcado.
     * @param id ID do atendimento a ser concluído.
     * @return O objeto AtendimentoTO atualizado com o status CONCLUIDO.
     * @throws AtendimentoException Caso o atendimento não exista ou não esteja em um status que permita conclusão.
     */
    public AtendimentoTO concluir(Long id) throws AtendimentoException {

        atendimentoDAO = new AtendimentoDAO();

        AtendimentoTO atendimento = atendimentoDAO.findById(id);
        if (atendimento == null) {
            throw new AtendimentoException("Não existe nenhum atendimento com o ID informado.");
        }

        if (atendimento.getStatus() != StatusAtendimento.MARCADO && atendimento.getStatus() != StatusAtendimento.REMARCADO) {
            throw new AtendimentoException("Só é possível concluir atendimentos Marcados ou Remarcados.");
        }

        atendimento.setStatus(StatusAtendimento.CONCLUIDO);

        return atendimentoDAO.update(atendimento);
    }

    /**
     * Cancela um atendimento existente, alterando seu status para CANCELADO, desde que esteja atualmente marcado ou remarcado.
     * @param id ID do atendimento a ser cancelado.
     * @return O objeto AtendimentoTO atualizado com o status CANCELADO.
     * @throws AtendimentoException Caso o atendimento não exista ou não esteja em um status que permita cancelamento.
     */
    public AtendimentoTO cancelar(Long id) throws AtendimentoException {

        atendimentoDAO = new AtendimentoDAO();

        AtendimentoTO atendimento = atendimentoDAO.findById(id);
        if (atendimento == null) {
            throw new AtendimentoException("Não existe nenhum atendimento com o ID informado.");
        }

        if (atendimento.getStatus() != StatusAtendimento.MARCADO && atendimento.getStatus() != StatusAtendimento.REMARCADO) {
            throw new AtendimentoException("Só é possível cancelar atendimentos Marcados ou Remarcados.");
        }

        atendimento.setStatus(StatusAtendimento.CANCELADO);

        return atendimentoDAO.update(atendimento);
    }

    /**
     * Exclui um atendimento pelo seu ID, removendo também os lembretes associados.
     *
     * @param id ID do atendimento a ser excluído.
     * @return true se o atendimento foi excluído com sucesso, false se não foi encontrado.
     */
    public boolean delete(Long id) {
        atendimentoDAO = new AtendimentoDAO();

        if (atendimentoDAO.findById(id) == null) {
            return false;
        }

        lembreteDAO = new LembreteDAO();
        lembreteDAO.deleteByAtendimento(id);

        return atendimentoDAO.delete(id);
    }

    /**
     * Busca todos os atendimentos cadastrados.
     *
     * @return Lista de todos os atendimentos.
     */
    public ArrayList<AtendimentoTO> findAll() {
        atendimentoDAO = new AtendimentoDAO();
        return atendimentoDAO.findAll();
    }

    /**
     * Busca um atendimento pelo seu ID.
     *
     * @param id ID do atendimento a ser buscado.
     * @return O objeto AtendimentoTO correspondente ao ID informado, ou null se não encontrado.
     */
    public AtendimentoTO findById(Long id) {
        atendimentoDAO = new AtendimentoDAO();
        return atendimentoDAO.findById(id);
    }

    /**
     * Busca todos os atendimentos vinculados a um paciente específico.
     *
     * @param idPaciente ID do paciente.
     * @return Lista de atendimentos vinculados ao paciente.
     * @throws PacienteException Se o paciente com o ID informado não existir.
     */
    public ArrayList<AtendimentoTO> findAllByPaciente(Long idPaciente) throws PacienteException {
        pacienteDAO = new PacienteDAO();
        if (pacienteDAO.findById(idPaciente) == null) {
            throw new PacienteException("Não existe nenhum paciente com o ID informado");
        }

        atendimentoDAO = new AtendimentoDAO();
        return atendimentoDAO.findAllByPaciente(idPaciente);
    }

}

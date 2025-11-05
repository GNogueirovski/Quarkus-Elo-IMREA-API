package br.com.fiap.bo;

import br.com.fiap.dao.AtendimentoDAO;
import br.com.fiap.dao.LembreteDAO;
import br.com.fiap.dao.PacienteDAO;
import br.com.fiap.dao.ProfissionalSaudeDAO;
import br.com.fiap.enums.StatusAtendimento;
import br.com.fiap.exception.AtendimentoException;
import br.com.fiap.exception.PacienteException;
import br.com.fiap.exception.ProfissionalSaudeException;
import br.com.fiap.to.AtendimentoTO;
import br.com.fiap.to.ProfissionalSaudeTO;

import java.time.LocalDate;
import java.util.ArrayList;

public class AtendimentoBO {
    private AtendimentoDAO atendimentoDAO;
    private PacienteDAO pacienteDAO;
    private ProfissionalSaudeDAO profissionalSaudeDAO;
    private LembreteDAO lembreteDAO;

    public AtendimentoTO save(AtendimentoTO atendimentoTO) throws ProfissionalSaudeException, PacienteException, AtendimentoException {

        pacienteDAO = new PacienteDAO();

        if (pacienteDAO.findById(atendimentoTO.getIdPaciente()) == null) {
            throw new PacienteException("Não existe nenhum paciente com o ID informado");
        }

       profissionalSaudeDAO = new ProfissionalSaudeDAO();

        if (profissionalSaudeDAO.findById(atendimentoTO.getIdProfissionalSaude()) == null) {
            throw new ProfissionalSaudeException("Não existe nenhum profissional da saúde com o ID informado");
        }

        if (atendimentoTO.getData().isBefore(LocalDate.now())) {
            throw new AtendimentoException("Não é possível agendar um atendimento em uma data passada.");
        }
        atendimentoDAO = new AtendimentoDAO();

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

        if (atendimentoTO.getData().isBefore(LocalDate.now())) {
            throw new AtendimentoException("Não é possível agendar um atendimento em uma data passada.");
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

    public boolean delete(Long id) {
        atendimentoDAO = new AtendimentoDAO();

        if (atendimentoDAO.findById(id) == null) {
            return false;
        }

        lembreteDAO = new LembreteDAO();
        lembreteDAO.deleteByAtendimento(id);

        return atendimentoDAO.delete(id);
    }

    public ArrayList<AtendimentoTO> findAll() {
        atendimentoDAO = new AtendimentoDAO();
        return atendimentoDAO.findAll();
    }

    public AtendimentoTO findById(Long id) {
        atendimentoDAO = new AtendimentoDAO();
        return atendimentoDAO.findById(id);
    }

    public ArrayList<AtendimentoTO> findAllByPaciente(Long idPaciente) throws PacienteException {
        pacienteDAO = new PacienteDAO();
        if (pacienteDAO.findById(idPaciente) == null) {
            throw new PacienteException("Não existe nenhum paciente com o ID informado");
        }

        atendimentoDAO = new AtendimentoDAO();
        return atendimentoDAO.findAllByPaciente(idPaciente);
    }

}

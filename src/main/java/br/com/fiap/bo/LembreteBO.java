package br.com.fiap.bo;

import br.com.fiap.dao.*;
import br.com.fiap.enums.StatusLembrete;
import br.com.fiap.exception.*;
import br.com.fiap.to.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 *  * Classe de com regras de negócio para gerenciar lembretes e utilizar os métodos DAO para realizar as ações com banco de dados.
 *  @version 1.0
 */
public class LembreteBO {
    private LembreteDAO lembreteDAO;
    private AtendimentoDAO atendimentoDAO;
    private ColaboradorDAO colaboradorDAO;
    private PacienteDAO pacienteDAO;
    private ProfissionalSaudeDAO profissionalSaudeDAO;


    /**
     * Salva um novo lembrete após validar as regras de negócio necessáarias e montar o assunto e mensagem.
     * @param lembreteTO Objeto LembreteTO contendo os dados do lembrete a ser salvo.
     * @return Objeto LembreteTO salvo com os dados atualizados.
     * @throws AtendimentoException caso o atendimento não exista.
     * @throws ColaboradorException caso o colaborador não exista.
     * @throws PacienteException caso o paciente não exista.
     * @throws ProfissionalSaudeException caso o profissional de saúde não exista.
     */
    public LembreteTO save(LembreteTO lembreteTO) throws AtendimentoException, ColaboradorException, PacienteException, ProfissionalSaudeException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        atendimentoDAO = new AtendimentoDAO();
        AtendimentoTO atendimento = atendimentoDAO.findById(lembreteTO.getIdAtendimento());
        if (atendimento == null) {
            throw new AtendimentoException("Não existe um atendimento com o id informado.");
        }

        colaboradorDAO = new ColaboradorDAO();
        ColaboradorTO colaborador = colaboradorDAO.findById(lembreteTO.getIdColaborador());
        if (colaborador == null) {
            throw new ColaboradorException("Não existe um colaborador com o id informado.");
        }

        pacienteDAO = new PacienteDAO();
        PacienteTO paciente = pacienteDAO.findById(atendimento.getIdPaciente());
        if (paciente == null) {
            throw new PacienteException("Paciente vinculado ao atendimento não foi encontrado.");
        }

        profissionalSaudeDAO = new ProfissionalSaudeDAO();
        ProfissionalSaudeTO profissional = profissionalSaudeDAO.findById(atendimento.getIdProfissionalSaude());
        if (profissional == null) {
            throw new ProfissionalSaudeException("Profissional vinculado ao atendimento não foi encontrado.");
        }

        String assunto = String.format(" IMREA: Consulta com %s dia %s",
                profissional.getEspecialidade(),
                atendimento.getData().format(dtf));

        String mensagem = String.format(
                "\nOlá Sr(a) %s,\nEstamos passando para lembrar que seu atendimento foi %s.\n \nProfissional: %s (%s)\nFormato: %s \nLocal/Link: %s \nData: %s às %s",
                paciente.getNomeCompleto(),
                atendimento.getStatus().name(),
                profissional.getNomeCompleto(),
                profissional.getEspecialidade(),
                atendimento.getFormatoAtendimento(),
                atendimento.getLocal(),
                atendimento.getData().format(dtf),
                atendimento.getHora().toString()
        );

        lembreteTO.setAssunto(assunto);
        lembreteTO.setMensagem(mensagem);
        lembreteTO.setDataEnvio(LocalDate.now());
        lembreteTO.setStatus(StatusLembrete.ENVIADO);

        lembreteDAO = new LembreteDAO();
        return lembreteDAO.save(lembreteTO);
    }

    /**
     * Reenvia um lembrete existente, atualizando seu status, assunto, mensagem e data de envio.
     * @param id ID do lembrete a ser reenviado.
     * @return Objeto LembreteTO atualizado com os dados do reenvio.
     * @throws LembreteException caso o lembrete não exista.
     */
    public LembreteTO reenviar(Long id) throws LembreteException, AtendimentoException, ProfissionalSaudeException, PacienteException {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        lembreteDAO = new LembreteDAO();
        LembreteTO lembrete = lembreteDAO.findById(id);

        if (lembrete == null) {
            throw new LembreteException("Não existe nenhum lembrete com o ID informado.");
        }

        atendimentoDAO = new AtendimentoDAO();
        AtendimentoTO atendimento = atendimentoDAO.findById(lembrete.getIdAtendimento());
        if (atendimento == null) {
            throw new AtendimentoException("Não existe um atendimento com o id informado.");
        }

        profissionalSaudeDAO = new ProfissionalSaudeDAO();
        ProfissionalSaudeTO profissional = profissionalSaudeDAO.findById(atendimento.getIdProfissionalSaude());
        if (profissional == null) {
            throw new ProfissionalSaudeException("Profissional vinculado ao atendimento não foi encontrado.");
        }


        pacienteDAO = new PacienteDAO();
        PacienteTO paciente = pacienteDAO.findById(atendimento.getIdPaciente());
        if (paciente == null) {
            throw new PacienteException("Paciente vinculado ao atendimento não foi encontrado.");
        }

        String assunto = String.format(" IMREA: Consulta com %s dia %s",
                profissional.getEspecialidade(),
                atendimento.getData().format(dtf));

        String mensagem = String.format(
                "\nOlá Sr(a) %s,\nEstamos passando para lembrar que seu atendimento foi %s.\n \nProfissional: %s (%s)\nFormato: %s \nLocal/Link: %s \nData: %s às %s",
                paciente.getNomeCompleto(),
                atendimento.getStatus().name(),
                profissional.getNomeCompleto(),
                profissional.getEspecialidade(),
                atendimento.getFormatoAtendimento(),
                atendimento.getLocal(),
                atendimento.getData().format(dtf),
                atendimento.getHora().toString()
        );

        lembrete.setAssunto(assunto);
        lembrete.setMensagem(mensagem);
        lembrete.setStatus(StatusLembrete.REENVIADO);
        lembrete.setDataEnvio(LocalDate.now());

        return lembreteDAO.update(lembrete);
    }

    /**
     * Exclui um lembrete existente pelo seu ID.
     * @param id ID do lembrete a ser excluído.
     * @return true se o lembrete foi excluído com sucesso, false caso contrário.
     */
    public boolean delete(Long id) {
        lembreteDAO = new LembreteDAO();
        if (lembreteDAO.findById(id) == null) {
            return false;
        }
        return lembreteDAO.delete(id);
    }

    /**
     * Busca um lembrete pelo seu ID.
     * @param id ID do lembrete a ser buscado.
     * @return Objeto LembreteTO correspondente ao ID informado, ou null se não encontrado.
     */
    public LembreteTO findById(Long id) {
        lembreteDAO = new LembreteDAO();
        return lembreteDAO.findById(id);
    }

    /**
     * Busca todos os lembretes associados a um paciente específico.
     * @param idPaciente ID do paciente cujos lembretes serão buscados.
     * @return Lista de objetos LembreteTO associados ao paciente.
     * @throws PacienteException caso o paciente não exista.
     */
    public ArrayList<LembreteTO> findAllByPaciente(Long idPaciente) throws PacienteException {
        pacienteDAO = new PacienteDAO();
        if (pacienteDAO.findById(idPaciente) == null) {
            throw new PacienteException("Não existe nenhum paciente com o ID informado");
        }
        lembreteDAO = new LembreteDAO();
        return lembreteDAO.findAllByPaciente(idPaciente);
    }

    /**
     * Busca o último lembrete enviado para um paciente específico.
     * @param idPaciente ID do paciente cujo último lembrete será buscado.
     * @return Objeto LembreteTO correspondente ao último lembrete enviado ao paciente.
     * @throws PacienteException caso o paciente não exista.
     */
    public LembreteTO findUltimoByPaciente(Long idPaciente) throws PacienteException {
        pacienteDAO = new PacienteDAO();
        if (pacienteDAO.findById(idPaciente) == null) {
            throw new PacienteException("Não existe nenhum paciente com o ID informado");
        }
        lembreteDAO = new LembreteDAO();
        return lembreteDAO.findUltimoByPaciente(idPaciente);
    }

}

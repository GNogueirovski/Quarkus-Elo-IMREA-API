package br.com.fiap.bo;

import br.com.fiap.dao.*;
import br.com.fiap.enums.StatusLembrete;
import br.com.fiap.exception.*;
import br.com.fiap.to.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class LembreteBO {
    public LembreteTO save(LembreteTO lembreteTO) throws AtendimentoException, ColaboradorException, PacienteException, ProfissionalSaudeException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        AtendimentoDAO atendimentoDAO = new AtendimentoDAO();
        AtendimentoTO atendimento = atendimentoDAO.findById(lembreteTO.getIdAtendimento());
        if (atendimento == null) {
            throw new AtendimentoException("Não existe um atendimento com o id informado.");
        }

        ColaboradorDAO colaboradorDAO = new ColaboradorDAO();
        ColaboradorTO colaborador = colaboradorDAO.findById(lembreteTO.getIdColaborador());
        if (colaborador == null) {
            throw new ColaboradorException("Não existe um colaborador com o id informado.");
        }

        PacienteDAO pacienteDAO = new PacienteDAO();
        PacienteTO paciente = pacienteDAO.findById(atendimento.getIdPaciente());
        if (paciente == null) {
            throw new PacienteException("Paciente vinculado ao atendimento não foi encontrado.");
        }

        ProfissionalSaudeDAO profissionalSaudeDAO = new ProfissionalSaudeDAO();
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

        LembreteDAO lembreteDAO = new LembreteDAO();
        return lembreteDAO.save(lembreteTO);
    }

    public LembreteTO reenviar(Long id) throws LembreteException {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        LembreteDAO lembreteDAO = new LembreteDAO();
        LembreteTO lembrete = lembreteDAO.findById(id);

        if (lembrete == null) {
            throw new LembreteException("Não existe nenhum lembrete com o ID informado.");
        }

        AtendimentoDAO atendimentoDAO = new AtendimentoDAO();
        AtendimentoTO atendimento = atendimentoDAO.findById(lembrete.getIdAtendimento());

        ProfissionalSaudeDAO profissionalSaudeDAO = new ProfissionalSaudeDAO();
        ProfissionalSaudeTO profissional = profissionalSaudeDAO.findById(atendimento.getIdProfissionalSaude());


        PacienteDAO pacienteDAO = new PacienteDAO();
        PacienteTO paciente = pacienteDAO.findById(atendimento.getIdPaciente());

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

    public boolean delete(Long id) {
        LembreteDAO lembreteDAO = new LembreteDAO();
        if (lembreteDAO.findById(id) == null) {
            return false;
        }
        return lembreteDAO.delete(id);
    }
    public LembreteTO findById(Long id) {
        LembreteDAO lembreteDAO = new LembreteDAO();
        return lembreteDAO.findById(id);
    }

    public ArrayList<LembreteTO> findAllByPaciente(Long idPaciente) throws PacienteException {
        PacienteDAO pacienteDAO = new PacienteDAO();
        if (pacienteDAO.findById(idPaciente) == null) {
            throw new PacienteException("Não existe nenhum paciente com o ID informado");
        }
        LembreteDAO lembreteDAO = new LembreteDAO();
        return lembreteDAO.findAllByPaciente(idPaciente);
    }

    public LembreteTO findUltimoByPaciente(Long idPaciente) throws PacienteException {
        PacienteDAO pacienteDAO = new PacienteDAO();
        if (pacienteDAO.findById(idPaciente) == null) {
            throw new PacienteException("Não existe nenhum paciente com o ID informado");
        }
        LembreteDAO lembreteDAO = new LembreteDAO();
        return lembreteDAO.findUltimoByPaciente(idPaciente);
    }

}

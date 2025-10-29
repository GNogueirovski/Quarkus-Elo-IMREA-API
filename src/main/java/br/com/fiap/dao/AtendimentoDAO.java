package br.com.fiap.dao;

import br.com.fiap.enums.FormatoAtendimento;
import br.com.fiap.enums.StatusAtendimento;
import br.com.fiap.to.AtendimentoTO;
import br.com.fiap.to.PacienteTO;
import br.com.fiap.to.ProfissionalSaudeTO;

import java.sql.*;
import java.util.ArrayList;

public class AtendimentoDAO {
    /**
     * Insere um novo atendimento no banco de dados
     * @param atendimentoTO objeto contendo os dados do atendimento a ser inserido
     * @return objeto AtendimentoTO com os dados do atendimento inserido ou null se falhar
     */
    public AtendimentoTO save(AtendimentoTO atendimentoTO) {

        String sql = "insert into T_ELO_ATENDIMENTO (id_paciente, id_profissional_saude, fm_formato_atendimento, dt_data, hr_hora, lc_local, st_status) values (?,?,?,?,?,?,?)";

        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ps.setLong(1, atendimentoTO.getIdPaciente());
            ps.setLong(2, atendimentoTO.getIdProfissionalSaude());
            ps.setString(3, String.valueOf(atendimentoTO.getFormatoAtendimento()));
            ps.setDate(4, Date.valueOf(atendimentoTO.getData()));
            ps.setTime(5, Time.valueOf(atendimentoTO.getHora()));
            ps.setString(6, atendimentoTO.getLocal());
            ps.setString(7, atendimentoTO.getStatus().name());

            if (ps.executeUpdate() > 0) {
                return atendimentoTO;
            } else {
                return null;
            }

        } catch (SQLException e) {
            System.out.println("Erro ao Salvar: " + e.getMessage());
        } finally {
            ConnectionFactory.closeConnection();
        }
        return null;
    }

    /**
     * Altera os dados de um atendimento no banco de dados
     * @param atendimentoTO objeto contendo os novos dados do atendimento
     * @return objeto AtendimentoTO com os dados do atendimento atualizado ou null se falhar
     */
    public AtendimentoTO update(AtendimentoTO atendimentoTO) {

        String sql = "UPDATE T_ELO_ATENDIMENTO set id_profissional_saude=?, fm_formato_atendimento=?, dt_data=?, hr_hora=?,lc_local=?, st_status=? where id_atendimento=?";

        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ps.setLong(1, atendimentoTO.getIdProfissionalSaude());
            ps.setString(2, String.valueOf(atendimentoTO.getFormatoAtendimento()));
            ps.setDate(3, Date.valueOf(atendimentoTO.getData()));
            ps.setTime(4, Time.valueOf(atendimentoTO.getHora()));
            ps.setString(5, atendimentoTO.getLocal());
            ps.setString(6, atendimentoTO.getStatus().name());
            ps.setLong(7, atendimentoTO.getIdAtendimento());

            if (ps.executeUpdate() > 0) {
                return atendimentoTO;
            } else {
                return null;
            }

        } catch (SQLException e) {
            System.out.println("Erro ao Alterar: " + e.getMessage());
        } finally {
            ConnectionFactory.closeConnection();
        }
        return null;
    }

    /**
     * Exclui um atendimento do banco de dados
     * @param idAtendimento ID do atendimento a ser excluído
     * @return true se excluído com sucesso, false caso contrário
     */
    public boolean delete(Long idAtendimento) {

        String sql = "DELETE FROM T_ELO_ATENDIMENTO where id_atendimento=?";

        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ps.setLong(1, idAtendimento);
            return ps.executeUpdate() > 0;
        }
        catch (SQLException e) {
            System.out.println("Erro no comando SQL " + e.getMessage());
        } finally {
            ConnectionFactory.closeConnection();
        }
        return false;
    }

    /**
     * Busca todos os atendimentos no banco de dados
     * @return lista de objetos de AtendimentoTO com todos os atendimentos ou null se não encontrar
     */
    public ArrayList<AtendimentoTO> findAll() {
        ArrayList<AtendimentoTO> atendimentos = new ArrayList<>();
        String sql = "SELECT a.id_atendimento, a.id_paciente, a.id_profissional_saude, a.fm_formato_atendimento, a.dt_data, a.hr_hora, a.lc_local, a.st_status FROM T_ELO_ATENDIMENTO a";
        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    AtendimentoTO atendimentoEncontrado = new AtendimentoTO();
                    atendimentoEncontrado.setIdAtendimento(rs.getLong("id_atendimento"));
                    atendimentoEncontrado.setIdPaciente(rs.getLong("id_paciente"));
                    atendimentoEncontrado.setIdProfissionalSaude(rs.getLong("id_profissional_saude"));
                    atendimentoEncontrado.setFormatoAtendimento(FormatoAtendimento.valueOf(rs.getString("fm_formato_atendimento")));
                    atendimentoEncontrado.setData(rs.getDate("dt_data").toLocalDate());
                    atendimentoEncontrado.setHora(rs.getTime("hr_hora").toLocalTime());
                    atendimentoEncontrado.setLocal(rs.getString("lc_local"));
                    atendimentoEncontrado.setStatus(StatusAtendimento.valueOf(rs.getString("st_status")));
                    atendimentos.add(atendimentoEncontrado);
                }
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Erro na consulta: " + e.getMessage());
        } finally {
            ConnectionFactory.closeConnection();
        }
        return atendimentos;
    }

    /**
     * Busca um atendimento no banco de dados
     * @param idAtendimento ID do atendimento a ser buscado
     * @return objeto AtendimentoTO com os dados do atendimento ou null se não encontrar
     */
    public AtendimentoTO findById(Long idAtendimento) {
        AtendimentoTO atendimento = new AtendimentoTO();

        String sql = "SELECT a.id_atendimento, a.fm_formato_atendimento, a.dt_data, a.hr_hora, a.lc_local, a.st_status, p.id_paciente, p.nc_nome_completo AS nome_paciente,ps.id_profissional_saude, ps.nc_nome_completo AS nome_profissional, ps.es_especialidade FROM T_ELO_ATENDIMENTO a INNER JOIN T_ELO_PACIENTE p ON a.id_paciente = p.id_paciente INNER JOIN T_ELO_PROFISSIONAL_SAUDE ps ON a.id_profissional_saude = ps.id_profissional_saude WHERE a.id_atendimento=?";

        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ps.setLong(1, idAtendimento);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                PacienteTO paciente = new PacienteTO();
                paciente.setIdPaciente(rs.getLong("id_paciente"));

                ProfissionalSaudeTO profissional = new ProfissionalSaudeTO();
                profissional.setIdProfissionalSaude(rs.getLong("id_profissional_saude"));

                atendimento.setIdAtendimento(rs.getLong("id_atendimento"));
                atendimento.setFormatoAtendimento(FormatoAtendimento.valueOf(rs.getString("fm_formato_atendimento")));
                atendimento.setData(rs.getDate("dt_data").toLocalDate());
                atendimento.setHora(rs.getTime("hr_hora").toLocalTime());
                atendimento.setLocal(rs.getString("lc_local"));
                atendimento.setStatus(StatusAtendimento.valueOf(rs.getString("st_status")));
                atendimento.setIdPaciente(paciente.getIdPaciente());
                atendimento.setIdProfissionalSaude(profissional.getIdProfissionalSaude());
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Erro no comando SQL " + e.getMessage());
        } finally {
            ConnectionFactory.closeConnection();
        }
        return atendimento;
    }

    /**
     * Busca todos os atendimentos de um paciente
     * @param idPaciente ID do paciente
     * @return lista de objetos com os dados dos atendimentos encontrados
     */
    public ArrayList<AtendimentoTO> findAllByPaciente(Long idPaciente) {
        String sql = "SELECT a.id_atendimento, a.fm_formato_atendimento, a.dt_data, a.hr_hora, a.lc_local, a.st_status,ps.id_profissional_saude, ps.nc_nome_completo AS nome_profissional, ps.es_especialidade FROM T_ELO_ATENDIMENTO a INNER JOIN T_ELO_PROFISSIONAL_SAUDE ps ON a.id_profissional_saude = ps.id_profissional_saude WHERE a.id_paciente=?";

        ArrayList<AtendimentoTO> atendimentos = new ArrayList<>();

        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ps.setLong(1, idPaciente);
            ResultSet rs = ps.executeQuery();
            if(rs != null){
                while (rs.next()) {
                    ProfissionalSaudeTO profissional = new ProfissionalSaudeTO();
                    profissional.setIdProfissionalSaude(rs.getLong("id_profissional_saude"));

                    AtendimentoTO atendimento = new AtendimentoTO();
                    atendimento.setIdAtendimento(rs.getLong("id_atendimento"));
                    atendimento.setFormatoAtendimento(FormatoAtendimento.valueOf(rs.getString("fm_formato_atendimento")));
                    atendimento.setData(rs.getDate("dt_data").toLocalDate());
                    atendimento.setHora(rs.getTime("hr_hora").toLocalTime());
                    atendimento.setLocal(rs.getString("lc_local"));
                    atendimento.setStatus(StatusAtendimento.valueOf(rs.getString("st_status")));
                    atendimento.setIdProfissionalSaude(profissional.getIdProfissionalSaude());

                    atendimentos.add(atendimento);
                }
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Erro no comando SQL " + e.getMessage());
        } finally {
            ConnectionFactory.closeConnection();
        }
        return atendimentos;
    }
}

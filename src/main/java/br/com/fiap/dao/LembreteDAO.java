package br.com.fiap.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import br.com.fiap.to.LembreteTO;
import br.com.fiap.enums.StatusLembrete;

public class LembreteDAO {
    /**
     * Insere um novo lembrete no banco de dados
     *
     * @param lembreteTO objeto contendo os dados do lembrete a ser inserido
     * @return o objeto lembreteTO inserido ou null em caso de erro
     */
    public LembreteTO save(LembreteTO lembreteTO) {
        String sql = "insert into T_ELO_LEMBRETE (as_assunto, ms_mensagem, dt_data_envio, st_status, id_colaborador, id_atendimento) values (?,?,?,?,?,?)";

        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql, new String[]{"ID_LEMBRETE"})) {
            ps.setString(1, lembreteTO.getAssunto());
            ps.setString(2, lembreteTO.getMensagem());
            ps.setDate(3, Date.valueOf(lembreteTO.getDataEnvio()));
            ps.setString(4, lembreteTO.getStatus().name());
            ps.setLong(5, lembreteTO.getIdColaborador());
            ps.setLong(6, lembreteTO.getIdAtendimento());
            if (ps.executeUpdate() > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        lembreteTO.setIdLembrete(rs.getLong(1));
                    }
                }
                return lembreteTO;
            }return null;
        } catch (SQLException e) {
            System.out.println("Erro de SQL: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        } finally {
            ConnectionFactory.closeConnection();
        }
    }

    /**
     * Altera os dados de um lembrete existente no banco de dados
     *
     * @param lembreteTO objeto contendo os novos dados do lembrete
     * @return o objeto lembreteTO atualizado ou null em caso de erro
     */
    public LembreteTO update(LembreteTO lembreteTO) {
        String sql = "UPDATE T_ELO_LEMBRETE set as_assunto=?, ms_mensagem=?, dt_data_envio=?, st_status=?, id_colaborador=? where id_lembrete=?";

        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ps.setString(1, lembreteTO.getAssunto());
            ps.setString(2, lembreteTO.getMensagem());
            ps.setDate(3, Date.valueOf(lembreteTO.getDataEnvio()));
            ps.setString(4, lembreteTO.getStatus().name());
            ps.setLong(5, lembreteTO.getIdColaborador());
            ps.setLong(6, lembreteTO.getIdLembrete());

            if (ps.executeUpdate() > 0) {
                return lembreteTO;
            }return null;
        } catch (SQLException e) {
            System.out.println("Erro de SQL: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        } finally {
            ConnectionFactory.closeConnection();
        }
    }

    /**
     * Exclui um lembrete do banco de dados
     *
     * @param idLembrete ID do lembrete a ser excluído
     * @return true se excluído com sucesso, false caso contrário
     */
    public boolean delete(Long idLembrete) {
        String sql = "DELETE FROM T_ELO_LEMBRETE where id_lembrete=?";

        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ps.setLong(1, idLembrete);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erro no comando SQL " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        } finally {
            ConnectionFactory.closeConnection();
        }
    }

    /**
     * Busca um lembrete específico no banco de dados
     *
     * @param idLembrete ID do lembrete a ser buscado
     * @return objeto LembreteTO com os dados do lembrete encontrado ou null se não encontrar
     */
    public LembreteTO findById(Long idLembrete) {
        LembreteTO lembreteEncontrado = new LembreteTO();
        String sql = "SELECT * FROM T_ELO_LEMBRETE WHERE id_lembrete=?";

        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ps.setLong(1, idLembrete);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                lembreteEncontrado.setIdLembrete(rs.getLong("id_lembrete"));
                lembreteEncontrado.setIdColaborador(rs.getLong("id_colaborador"));
                lembreteEncontrado.setIdAtendimento(rs.getLong("id_atendimento"));
                lembreteEncontrado.setAssunto(rs.getString("as_assunto"));
                lembreteEncontrado.setMensagem(rs.getString("ms_mensagem"));
                lembreteEncontrado.setStatus(StatusLembrete.valueOf(rs.getString("st_status")));
                lembreteEncontrado.setDataEnvio(rs.getDate("dt_data_envio").toLocalDate());
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Erro no comando SQL " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        } finally {
            ConnectionFactory.closeConnection();
        }
        return lembreteEncontrado;
    }

    /**
     * Busca todos os lembretes de um paciente específico
     *
     * @param idPaciente ID do paciente
     * @return lista de objetos LembreteTO com os dados dos lembretes encontrados
     */
    public ArrayList<LembreteTO> findAllByPaciente(Long idPaciente) {
        String sql = "SELECT l.* FROM T_ELO_LEMBRETE l INNER JOIN T_ELO_ATENDIMENTO a ON a.id_atendimento = l.id_atendimento INNER JOIN T_ELO_PACIENTE p ON p.id_paciente = a.id_paciente WHERE p.id_paciente=? order by dt_data_envio desc";

        ArrayList<LembreteTO> lembretes = new ArrayList<>();

        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ps.setLong(1, idPaciente);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                LembreteTO lembreteTO = new LembreteTO();
                lembreteTO.setIdLembrete(rs.getLong("id_lembrete"));
                lembreteTO.setIdColaborador(rs.getLong("id_colaborador"));
                lembreteTO.setIdAtendimento(rs.getLong("id_atendimento"));
                lembreteTO.setAssunto(rs.getString("as_assunto"));
                lembreteTO.setMensagem(rs.getString("ms_mensagem"));
                lembreteTO.setStatus(StatusLembrete.valueOf(rs.getString("st_status")));
                lembreteTO.setDataEnvio(rs.getDate("dt_data_envio").toLocalDate());
                lembretes.add(lembreteTO);
            }
        } catch (SQLException e) {
            System.out.println("Erro no comando SQL " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        } finally {
            ConnectionFactory.closeConnection();
        }
        return lembretes;
    }

    /**
     * Busca o último lembrete enviado para um paciente
     *
     * @param idPaciente ID do paciente
     * @return objeto LembreteTO com os dados do último lembrete ou null
     */
    public LembreteTO findUltimoByPaciente(Long idPaciente) {
        LembreteTO lembreteTO = new LembreteTO();
        String sql = "SELECT l.* FROM T_ELO_LEMBRETE l INNER JOIN T_ELO_ATENDIMENTO a ON a.id_atendimento = l.id_atendimento WHERE a.id_paciente=? ORDER BY l.dt_data_envio DESC";

        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ps.setLong(1, idPaciente);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                lembreteTO.setIdLembrete(rs.getLong("id_lembrete"));
                lembreteTO.setIdColaborador(rs.getLong("id_colaborador"));
                lembreteTO.setIdAtendimento(rs.getLong("id_atendimento"));
                lembreteTO.setAssunto(rs.getString("as_assunto"));
                lembreteTO.setMensagem(rs.getString("ms_mensagem"));
                lembreteTO.setStatus(StatusLembrete.valueOf(rs.getString("st_status")));
                lembreteTO.setDataEnvio(rs.getDate("dt_data_envio").toLocalDate());
            }else{return null;}

        } catch (SQLException e) {
            System.out.println("Erro no comando SQL " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        } finally {
            ConnectionFactory.closeConnection();
        }
        return lembreteTO;
    }

    /**
     * Busca todos os lembretes de que um colaborador criou
     * @param idColaborador ID do colaborador
     * @return lista de objetos LembreteTO com os dados dos lembretes encontrados
     */
    public ArrayList<LembreteTO> findAllByColaborador(Long idColaborador) {
        String sql = "SELECT * FROM T_ELO_LEMBRETE WHERE id_colaborador = ?";
        ArrayList<LembreteTO> lembretes = new ArrayList<>();

        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ps.setLong(1, idColaborador);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                LembreteTO lembreteTO = new LembreteTO();
                lembreteTO.setIdLembrete(rs.getLong("id_lembrete"));
                lembreteTO.setIdColaborador(rs.getLong("id_colaborador"));
                lembreteTO.setIdAtendimento(rs.getLong("id_atendimento"));
                lembreteTO.setAssunto(rs.getString("as_assunto"));
                lembreteTO.setMensagem(rs.getString("ms_mensagem"));
                lembreteTO.setStatus(StatusLembrete.valueOf(rs.getString("st_status")));
                lembreteTO.setDataEnvio(rs.getDate("dt_data_envio").toLocalDate());
                lembretes.add(lembreteTO);
            }
        } catch (SQLException e) {
            System.out.println("Erro no comando SQL " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        } finally {
            ConnectionFactory.closeConnection();
        }
        return lembretes;
    }

    /**
     * Exclui todos os lembretes associados a um atendimento específico.
     * @param idAtendimento ID do atendimento "pai"
     * @return true se um ou mais lembretes foram excluídos, false caso contrário
     */
    public boolean deleteByAtendimento(Long idAtendimento) {
        String sql = "DELETE FROM T_ELO_LEMBRETE where id_atendimento = ?";

        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ps.setLong(1, idAtendimento);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erro no comando SQL " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        } finally {
            ConnectionFactory.closeConnection();
        }
    }
}

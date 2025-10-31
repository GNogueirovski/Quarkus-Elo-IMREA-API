package br.com.fiap.dao;

import br.com.fiap.to.ColaboradorTO;
import br.com.fiap.to.PacienteTO;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PacienteDAO {
    /**
     * Insere um novo paciente no banco de dados
     * @param pacienteTO objeto contendo os dados do paciente a ser inserido
     * @return mensagem informando o resultado da operação
     */
    public PacienteTO save(PacienteTO pacienteTO) {

        String sql = "insert into T_ELO_PACIENTE (nc_nome_completo, dt_data_nascimento, dc_cpf, tl_telefone, em_email, dg_diagnostico) values (?,?,?,?,?,?)";

        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ps.setString(1, pacienteTO.getNomeCompleto());
            ps.setDate(2, Date.valueOf(pacienteTO.getDataNascimento()));
            ps.setString(3, pacienteTO.getCpf());
            ps.setString(4, pacienteTO.getTelefone());
            ps.setString(5, pacienteTO.getEmail());
            ps.setString(6, pacienteTO.getDiagnostico());
            if (ps.executeUpdate() > 0) {
                return pacienteTO;
            } else {
                return null;
            }

        } catch (SQLException e) {
            System.out.println("Erro de SQL: " + e.getMessage());
        } finally {
            ConnectionFactory.closeConnection();
        }
        return null;

    }


    /**
     * Altera os dados de um paciente existente no banco de dados
     * @param pacienteTO objeto contendo os novos dados do paciente
     * @return mensagem informando o resultado da operação
     */
    public PacienteTO update(PacienteTO pacienteTO) {

        String sql = "UPDATE T_ELO_PACIENTE set nc_nome_completo=?, dt_data_nascimento=?, dc_cpf=?, tl_telefone=?, em_email=?, dg_diagnostico=? where id_paciente=?";

        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ps.setString(1, pacienteTO.getNomeCompleto());
            ps.setDate(2, Date.valueOf(pacienteTO.getDataNascimento()));
            ps.setString(3, pacienteTO.getCpf());
            ps.setString(4, pacienteTO.getTelefone());
            ps.setString(5, pacienteTO.getEmail());
            ps.setString(6, pacienteTO.getDiagnostico());
            ps.setLong(7, pacienteTO.getIdPaciente());

            if (ps.executeUpdate() > 0) {
                return pacienteTO;
            } else {
                return null;
            }

        } catch (SQLException e) {
            System.out.println("Erro de SQL: " + e.getMessage());
        }finally {
            ConnectionFactory.closeConnection();
        }
        return null;
    }


    /**
     * Exclui um paciente do banco de dados
     * @param id do paciente a ser excluído
     * @return mensagem informando o resultado da operação
     */
    public boolean delete(Long id) {

        String sql = "DELETE FROM T_ELO_PACIENTE where id_paciente=?";

        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ps.setLong(1, id);
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
     * Busca todos os pacientes no banco de dados
     * @return lista de objetos de pacienteTO com todos os pacientes ou null se não encontrar
     */
    public ArrayList<PacienteTO> findAll() {
        ArrayList<PacienteTO> pacientes = new ArrayList<>();
        String sql = "SELECT * FROM T_ELO_PACIENTE";
        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    PacienteTO pacienteEncontrado = new PacienteTO();
                    pacienteEncontrado.setIdPaciente(rs.getLong("id_paciente"));
                    pacienteEncontrado.setNomeCompleto(rs.getString("nc_nome_completo"));
                    pacienteEncontrado.setTelefone(rs.getString("tl_telefone"));
                    pacienteEncontrado.setCpf(rs.getString("dc_cpf"));
                    pacienteEncontrado.setDataNascimento(rs.getDate("dt_data_nascimento").toLocalDate());
                    pacienteEncontrado.setEmail(rs.getString("em_email"));
                    pacienteEncontrado.setDiagnostico(rs.getString("dg_diagnostico"));
                    pacientes.add(pacienteEncontrado);
                }}else{
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Erro na consulta: " + e.getMessage());
        }finally {
            ConnectionFactory.closeConnection();
        }
        return pacientes;
    }

    /**
     * Busca um paciente específico no banco de dados
     * @param id do paciente a ser buscado
     * @return objeto PacienteTO com os dados do paciente encontrado ou null
     */
    public PacienteTO findById(Long id) {
        PacienteTO pacienteEncontrado = new PacienteTO();
        String sql = "SELECT * FROM T_ELO_PACIENTE where id_paciente=?";

        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                pacienteEncontrado.setIdPaciente(rs.getLong("id_paciente"));
                pacienteEncontrado.setNomeCompleto(rs.getString("nc_nome_completo"));
                pacienteEncontrado.setCpf(rs.getString("dc_cpf"));
                pacienteEncontrado.setDataNascimento(rs.getDate("dt_data_nascimento").toLocalDate());
                pacienteEncontrado.setTelefone(rs.getString("tl_telefone"));
                pacienteEncontrado.setEmail(rs.getString("em_email"));
                pacienteEncontrado.setDiagnostico(rs.getString("dg_diagnostico"));
            }else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Erro no comando SQL " + e.getMessage());
        } finally {
            ConnectionFactory.closeConnection();
        }
        return pacienteEncontrado;
    }

    /**
     * Busca um paciente associado a um acompanhante específico
     * @param idAcompanhante ID do acompanhante
     * @return objeto PacienteTO com os dados do paciente encontrado ou null se não encontrar
     */
    public PacienteTO findByAcompanhante(Long idAcompanhante) {
        PacienteTO pacienteEncontrado = new PacienteTO();
        String sql = "SELECT p.* FROM T_ELO_PACIENTE p inner join T_ELO_ACOMPANHANTE a ON p.id_paciente = a.id_paciente where a.id_acompanhante=?";

        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ps.setLong(1, idAcompanhante);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                pacienteEncontrado.setIdPaciente(rs.getLong("id_paciente"));
                pacienteEncontrado.setNomeCompleto(rs.getString("nc_nome_completo"));
                pacienteEncontrado.setCpf(rs.getString("dc_cpf"));
                pacienteEncontrado.setDataNascimento(rs.getDate("dt_data_nascimento").toLocalDate());
                pacienteEncontrado.setTelefone(rs.getString("tl_telefone"));
                pacienteEncontrado.setEmail(rs.getString("em_email"));
                pacienteEncontrado.setDiagnostico(rs.getString("dg_diagnostico"));
            } else{
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Erro no comando SQL " + e.getMessage());
        } finally {
            ConnectionFactory.closeConnection();
        }
        return pacienteEncontrado;
    }

    /**
     * Busca um paciente específico no banco de dados a partir do CPF
     * @param cpf do paciente a ser buscado
     * @return objeto PacienteTO com os dados do paciente encontrado ou null
     */
    public PacienteTO findByCpf(String cpf) {
        PacienteTO pacienteEncontrado = new PacienteTO();
        String sql = "SELECT * FROM T_ELO_PACIENTE where dc_cpf=?";

        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ps.setString(1, cpf);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                pacienteEncontrado.setIdPaciente(rs.getLong("id_paciente"));
                pacienteEncontrado.setNomeCompleto(rs.getString("nc_nome_completo"));
                pacienteEncontrado.setCpf(rs.getString("dc_cpf"));
                pacienteEncontrado.setDataNascimento(rs.getDate("dt_data_nascimento").toLocalDate());
                pacienteEncontrado.setTelefone(rs.getString("tl_telefone"));
                pacienteEncontrado.setEmail(rs.getString("em_email"));
                pacienteEncontrado.setDiagnostico(rs.getString("dg_diagnostico"));
            }else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Erro no comando SQL " + e.getMessage());
        } finally {
            ConnectionFactory.closeConnection();
        }
        return pacienteEncontrado;
    }

}

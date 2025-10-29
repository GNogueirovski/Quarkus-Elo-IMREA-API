package br.com.fiap.dao;

import br.com.fiap.to.ColaboradorTO;
import br.com.fiap.to.ProfissionalSaudeTO;

import java.sql.*;
import java.util.ArrayList;

public class ProfissionalSaudeDAO {
    /**
     * Insere um novo profissional da saude no banco de dados
     * @param profissionalSaude objeto contendo os dados do profissional a ser inserido
     * @return mensagem informando o resultado da operação
     */
    public ProfissionalSaudeTO save(ProfissionalSaudeTO profissionalSaude) {

        String sql = "insert into T_ELO_PROFISSIONAL_SAUDE (nc_nome_completo, dt_data_nascimento, dc_cpf, tl_telefone, em_email, tp_tipo_documento, nm_documento, es_especialidade) values (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ps.setString(1, profissionalSaude.getNomeCompleto());
            ps.setDate(2, Date.valueOf(profissionalSaude.getDataNascimento()));
            ps.setString(3, profissionalSaude.getCpf());
            ps.setString(4, profissionalSaude.getTelefone());
            ps.setString(5, profissionalSaude.getEmail());
            ps.setString(6, profissionalSaude.getTipoDocumento());
            ps.setString(7, profissionalSaude.getDocumento());
            ps.setString(8, profissionalSaude.getEspecialidade());

            if (ps.executeUpdate() > 0) {
                return profissionalSaude;
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
     * Altera um profissional da saude no banco de dados
     * @param profissionalSaude objeto contendo os dados do profissional a ser alterado
     * @return mensagem informando o resultado da operação
     */
    public ProfissionalSaudeTO update(ProfissionalSaudeTO profissionalSaude) {

        String sql = "UPDATE T_ELO_PROFISSIONAL_SAUDE set nc_nome_completo=?, dt_data_nascimento=?, dc_cpf=? ,tl_telefone=?, em_email=?,tp_tipo_documento=?, nm_documento=?, es_especialidade=? where id_profissional_saude=?";

        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ps.setString(1, profissionalSaude.getNomeCompleto());
            ps.setDate(2, Date.valueOf(profissionalSaude.getDataNascimento()));
            ps.setString(3, profissionalSaude.getCpf());
            ps.setString(4, profissionalSaude.getTelefone());
            ps.setString(5, profissionalSaude.getEmail());
            ps.setString(6, profissionalSaude.getTipoDocumento());
            ps.setString(7, profissionalSaude.getDocumento());
            ps.setString(8, profissionalSaude.getEspecialidade());
            ps.setLong(9, profissionalSaude.getIdProfissionalSaude());

            if (ps.executeUpdate() > 0) {
                return profissionalSaude;
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
     * Exclui um profissional da saude no banco de dados
     * @param id do profissional a ser excluído
     * @return mensagem informando o resultado da operação
     */
    public boolean delete(Long id) {

        String sql = "DELETE FROM T_ELO_PROFISSIONAL_SAUDE where id_profissional_saude=?";

        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
        catch (SQLException e) {
            System.out.println("Erro ao excluir:" + e.getMessage());
        } finally {
            ConnectionFactory.closeConnection();
        }
        return false;
    }

    /**
     * Busca todos os profissionais da saúde no banco de dados
     * @return lista de objetos de ProfissionalSaudeTO com todos os profissionais ou null se não encontrar
     */
    public ArrayList<ProfissionalSaudeTO> findAll() {
        ArrayList<ProfissionalSaudeTO> profissionaisSaude = new ArrayList<>();
        String sql = "SELECT * FROM T_ELO_PROFISSIONAL_SAUDE";
        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    ProfissionalSaudeTO profissionalEncontrado = new ProfissionalSaudeTO();
                    profissionalEncontrado.setIdProfissionalSaude(rs.getLong("id_profissional_saude"));
                    profissionalEncontrado.setNomeCompleto(rs.getString("nc_nome_completo"));
                    profissionalEncontrado.setTelefone(rs.getString("tl_telefone"));
                    profissionalEncontrado.setCpf(rs.getString("dc_cpf"));
                    profissionalEncontrado.setDataNascimento(rs.getDate("dt_data_nascimento").toLocalDate());
                    profissionalEncontrado.setEmail(rs.getString("em_email"));
                    profissionalEncontrado.setTipoDocumento(rs.getString("tp_tipo_documento"));
                    profissionalEncontrado.setDocumento(rs.getString("nm_documento"));
                    profissionalEncontrado.setEspecialidade(rs.getString("es_especialidade"));
                    profissionaisSaude.add(profissionalEncontrado);
                }}else{
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Erro na consulta: " + e.getMessage());
        }finally {
            ConnectionFactory.closeConnection();
        }
        return profissionaisSaude;
    }


    /**
     * Busca um profissional da saúde específico no banco de dados
     * @param id ID do profissional a ser buscado
     * @return objeto ProfissionalSaudeTO com os dados do profissional encontrado ou null
     */
    public ProfissionalSaudeTO findById(Long id) {
        ProfissionalSaudeTO profissionalSaudeTO = new ProfissionalSaudeTO();
        String sql = "SELECT * FROM T_ELO_PROFISSIONAL_SAUDE where id_profissional_saude=?";

        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                profissionalSaudeTO.setIdProfissionalSaude(rs.getLong("id_profissional_saude"));
                profissionalSaudeTO.setNomeCompleto(rs.getString("nc_nome_completo"));
                profissionalSaudeTO.setTelefone(rs.getString("tl_telefone"));
                profissionalSaudeTO.setCpf(rs.getString("dc_cpf"));
                profissionalSaudeTO.setDataNascimento(rs.getDate("dt_data_nascimento").toLocalDate());
                profissionalSaudeTO.setEmail(rs.getString("em_email"));
                profissionalSaudeTO.setTipoDocumento(rs.getString("tp_tipo_documento"));
                profissionalSaudeTO.setDocumento(rs.getString("nm_documento"));
                profissionalSaudeTO.setEspecialidade(rs.getString("es_especialidade"));
            } else{
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Erro no comando SQL " + e.getMessage());
        } finally {
            ConnectionFactory.closeConnection();
        }
        return profissionalSaudeTO;
    }
}

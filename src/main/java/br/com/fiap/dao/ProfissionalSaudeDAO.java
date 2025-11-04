package br.com.fiap.dao;

import br.com.fiap.to.ProfissionalSaudeTO;
import br.com.fiap.to.ColaboradorTO;
import br.com.fiap.to.ProfissionalSaudeTO;

import java.sql.*;
import java.util.ArrayList;

public class ProfissionalSaudeDAO {
    /**
     * Insere um novo profissional da saude no banco de dados
     *
     * @param profissionalSaude objeto contendo os dados do profissional a ser inserido
     * @return mensagem informando o resultado da operação
     */
    public ProfissionalSaudeTO save(ProfissionalSaudeTO profissionalSaude) {

        String sql = "insert into T_ELO_PROFISSIONAL_SAUDE (nc_nome_completo, dt_data_nascimento, dc_cpf, tl_telefone, em_email,url_foto, tp_tipo_documento, nm_documento, es_especialidade) values (?, ?, ?,?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ps.setString(1, profissionalSaude.getNomeCompleto());
            ps.setDate(2, Date.valueOf(profissionalSaude.getDataNascimento()));
            ps.setString(3, profissionalSaude.getCpf());
            ps.setString(4, profissionalSaude.getTelefone());
            ps.setString(5, profissionalSaude.getEmail());
            if (profissionalSaude.getUrlFoto() != null) {
                ps.setString(6, profissionalSaude.getUrlFoto());
            } else {
                ps.setNull(6, Types.VARCHAR);
            }
            ps.setString(7, profissionalSaude.getTipoDocumento());
            ps.setString(8, profissionalSaude.getDocumento());
            ps.setString(9, profissionalSaude.getEspecialidade());

            if (ps.executeUpdate() > 0) {
                return profissionalSaude;
            }
            return null;

        } catch (SQLException e) {
            System.out.println("Erro ao Salvar: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        } finally {
            ConnectionFactory.closeConnection();
        }
    }

    /**
     * Altera um profissional da saude no banco de dados
     *
     * @param profissionalSaude objeto contendo os dados do profissional a ser alterado
     * @return mensagem informando o resultado da operação
     */
    public ProfissionalSaudeTO update(ProfissionalSaudeTO profissionalSaude) {

        String sql = "UPDATE T_ELO_PROFISSIONAL_SAUDE set nc_nome_completo=?, dt_data_nascimento=?, dc_cpf=? ,tl_telefone=?, em_email=?, url_foto=?,tp_tipo_documento=?, nm_documento=?, es_especialidade=? where id_profissional_saude=?";

        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ps.setString(1, profissionalSaude.getNomeCompleto());
            ps.setDate(2, Date.valueOf(profissionalSaude.getDataNascimento()));
            ps.setString(3, profissionalSaude.getCpf());
            ps.setString(4, profissionalSaude.getTelefone());
            ps.setString(5, profissionalSaude.getEmail());
            if (profissionalSaude.getUrlFoto() != null) {
                ps.setString(6, profissionalSaude.getUrlFoto());
            } else {
                ps.setNull(6, Types.VARCHAR);
            }
            ps.setString(7, profissionalSaude.getTipoDocumento());
            ps.setString(8, profissionalSaude.getDocumento());
            ps.setString(9, profissionalSaude.getEspecialidade());
            ps.setLong(10, profissionalSaude.getIdProfissionalSaude());

            if (ps.executeUpdate() > 0) {
                return profissionalSaude;
            }return null;

        } catch (SQLException e) {
            System.out.println("Erro ao Salvar: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        } finally {
            ConnectionFactory.closeConnection();
        }
    }


    /**
     * Exclui um profissional da saude no banco de dados
     *
     * @param id do profissional a ser excluído
     * @return mensagem informando o resultado da operação
     */
    public boolean delete(Long id) {

        String sql = "DELETE FROM T_ELO_PROFISSIONAL_SAUDE where id_profissional_saude=?";

        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao excluir:" + e.getMessage());
            throw new RuntimeException(e.getMessage());
        } finally {
            ConnectionFactory.closeConnection();
        }
    }

    /**
     * Busca todos os profissionais da saúde no banco de dados
     *
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
                    profissionalEncontrado.setUrlFoto(rs.getString("url_foto"));
                    profissionalEncontrado.setTipoDocumento(rs.getString("tp_tipo_documento"));
                    profissionalEncontrado.setDocumento(rs.getString("nm_documento"));
                    profissionalEncontrado.setEspecialidade(rs.getString("es_especialidade"));
                    profissionaisSaude.add(profissionalEncontrado);
                }
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Erro na consulta: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        } finally {
            ConnectionFactory.closeConnection();
        }
        return profissionaisSaude;
    }


    /**
     * Busca um profissional da saúde específico no banco de dados
     *
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
                profissionalSaudeTO.setUrlFoto(rs.getString("url_foto"));
                profissionalSaudeTO.setTipoDocumento(rs.getString("tp_tipo_documento"));
                profissionalSaudeTO.setDocumento(rs.getString("nm_documento"));
                profissionalSaudeTO.setEspecialidade(rs.getString("es_especialidade"));
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Erro no comando SQL " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        } finally {
            ConnectionFactory.closeConnection();
        }
        return profissionalSaudeTO;
    }

    /**
     * Busca um profissionalSaude específico no banco de dados a partir do CPF
     *
     * @param cpf do profissionalSaude a ser buscado
     * @return objeto profissionalSaudeTO com os dados do profissionalSaude encontrado ou null
     */
    public ProfissionalSaudeTO findByCpf(String cpf) {
        ProfissionalSaudeTO profissionalSaudeEncontrado = new ProfissionalSaudeTO();
        String sql = "SELECT * FROM T_ELO_PROFISSIONAL_SAUDE where dc_cpf=?";

        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ps.setString(1, cpf);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                profissionalSaudeEncontrado.setIdProfissionalSaude(rs.getLong("id_profissional_saude"));
                profissionalSaudeEncontrado.setNomeCompleto(rs.getString("nc_nome_completo"));
                profissionalSaudeEncontrado.setTelefone(rs.getString("tl_telefone"));
                profissionalSaudeEncontrado.setCpf(rs.getString("dc_cpf"));
                profissionalSaudeEncontrado.setDataNascimento(rs.getDate("dt_data_nascimento").toLocalDate());
                profissionalSaudeEncontrado.setEmail(rs.getString("em_email"));
                profissionalSaudeEncontrado.setUrlFoto(rs.getString("url_foto"));
                profissionalSaudeEncontrado.setTipoDocumento(rs.getString("tp_tipo_documento"));
                profissionalSaudeEncontrado.setDocumento(rs.getString("nm_documento"));
                profissionalSaudeEncontrado.setEspecialidade(rs.getString("es_especialidade"));
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Erro no comando SQL " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        } finally {
            ConnectionFactory.closeConnection();
        }
        return profissionalSaudeEncontrado;
    }

    /**
     * Busca um profissionalSaude específico no banco de dados a partir do CPF
     *
     * @param documento do profissionalSaude a ser buscado
     * @return objeto profissionalSaudeTO com os dados do profissionalSaude encontrado ou null
     */
    public ProfissionalSaudeTO findByDocumento(String documento) {
        ProfissionalSaudeTO profissionalSaudeEncontrado = new ProfissionalSaudeTO();
        String sql = "SELECT * FROM T_ELO_PROFISSIONAL_SAUDE where nm_documento=?";

        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ps.setString(1, documento);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                profissionalSaudeEncontrado.setIdProfissionalSaude(rs.getLong("id_profissional_saude"));
                profissionalSaudeEncontrado.setNomeCompleto(rs.getString("nc_nome_completo"));
                profissionalSaudeEncontrado.setTelefone(rs.getString("tl_telefone"));
                profissionalSaudeEncontrado.setCpf(rs.getString("dc_cpf"));
                profissionalSaudeEncontrado.setDataNascimento(rs.getDate("dt_data_nascimento").toLocalDate());
                profissionalSaudeEncontrado.setEmail(rs.getString("em_email"));
                profissionalSaudeEncontrado.setUrlFoto(rs.getString("url_foto"));
                profissionalSaudeEncontrado.setTipoDocumento(rs.getString("tp_tipo_documento"));
                profissionalSaudeEncontrado.setDocumento(rs.getString("nm_documento"));
                profissionalSaudeEncontrado.setEspecialidade(rs.getString("es_especialidade"));
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Erro no comando SQL " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        } finally {
            ConnectionFactory.closeConnection();
        }
        return profissionalSaudeEncontrado;
    }
}

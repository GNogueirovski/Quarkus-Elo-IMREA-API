package br.com.fiap.dao;

import br.com.fiap.to.AcompanhanteTO;
import br.com.fiap.to.PacienteTO;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AcompanhanteDAO {

    /**
     * Insere um novo acompanhante no banco de dados
     * @param acompanhanteTO objeto contendo os dados do acompanhante a ser inserido
     * @return Acompanhante salvo confirmando o sucessod da operação ou nulo
     */

    public AcompanhanteTO save(AcompanhanteTO acompanhanteTO) {

        String sql = "insert into T_ELO_ACOMPANHANTE (nc_nome_completo, dt_data_nascimento, dc_cpf, tl_telefone, em_email, pr_parentesco, id_paciente) values (?,?,?,?,?,?,?)";

        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ps.setString(1, acompanhanteTO.getNomeCompleto());
            ps.setDate(2, Date.valueOf(acompanhanteTO.getDataNascimento()));
            ps.setString(3, acompanhanteTO.getCpf());
            ps.setString(4, acompanhanteTO.getTelefone());
            ps.setString(5, acompanhanteTO.getEmail());
            ps.setString(6, acompanhanteTO.getParentesco());
            ps.setLong(7, acompanhanteTO.getIdPaciente());

            if (ps.executeUpdate() > 0) {
                return acompanhanteTO;
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
     * Altera os dados de um acompanhante no banco de dados
     * @param acompanhanteTO objeto contendo os novos dados do acompanhante
     * @return mensagem informando o resultado da operação
     */
    public AcompanhanteTO update(AcompanhanteTO acompanhanteTO) {

        String sql = "UPDATE T_ELO_ACOMPANHANTE set nc_nome_completo=?, dt_data_nascimento=?, dc_cpf=?, tl_telefone=?, em_email=?, pr_parentesco=? where id_acompanhante=?";

        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ps.setString(1, acompanhanteTO.getNomeCompleto());
            ps.setDate(2, Date.valueOf(acompanhanteTO.getDataNascimento()));
            ps.setString(3, acompanhanteTO.getCpf());
            ps.setString(4, acompanhanteTO.getTelefone());
            ps.setString(5, acompanhanteTO.getEmail());
            ps.setString(6, acompanhanteTO.getParentesco());
            ps.setLong(7, acompanhanteTO.getIdAcompanhante());
            if (ps.executeUpdate() > 0) {
                return acompanhanteTO;
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
     * Exclui um acompanhante do banco de dados
     * @param id ID do acompanhante a ser excluído
     * @return mensagem informando o resultado da operação
     */
    public boolean delete(Long id) {

        String sql = "DELETE FROM T_ELO_ACOMPANHANTE where id_acompanhante=?";
        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
        catch (SQLException e) {
            System.out.println("Erro ao excluir:" + e.getMessage());
        }
        finally {
            ConnectionFactory.closeConnection();
        }
        return false;
    }

    /**
     * Busca todos os acompanhantees no banco de dados
     * @return lista de objetos de AcompanhanteTO com todos os acompanhantees ou null se não encontrar
     */
    public ArrayList<AcompanhanteTO> findAll() {
        ArrayList<AcompanhanteTO> acompanhantes = new ArrayList<>();
        String sql = "SELECT * FROM T_ELO_ACOMPANHANTE";
        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    AcompanhanteTO acompanhanteEncontrado = new AcompanhanteTO();
                    acompanhanteEncontrado.setIdAcompanhante(rs.getLong("id_acompanhante"));
                    acompanhanteEncontrado.setNomeCompleto(rs.getString("nc_nome_completo"));
                    acompanhanteEncontrado.setTelefone(rs.getString("tl_telefone"));
                    acompanhanteEncontrado.setCpf(rs.getString("dc_cpf"));
                    acompanhanteEncontrado.setDataNascimento(rs.getDate("dt_data_nascimento").toLocalDate());
                    acompanhanteEncontrado.setEmail(rs.getString("em_email"));
                    acompanhanteEncontrado.setParentesco(rs.getString("pr_parentesco"));
                    acompanhantes.add(acompanhanteEncontrado);
                }}else{
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Erro na consulta: " + e.getMessage());
        }finally {
            ConnectionFactory.closeConnection();
        }
        return acompanhantes;
    }


    /**
     * Busca um acompanhante no banco de dados
     * @param id O ID do acompanhante a ser buscado
     * @return objeto AcompanhanteTO com os dados do acompanhante ou null se não encontrar
     */
    public AcompanhanteTO findById(Long id) {
        AcompanhanteTO acompanhanteEncontrado = new AcompanhanteTO();
        String sql = "SELECT * FROM T_ELO_ACOMPANHANTE where id_acompanhante=?";

        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                acompanhanteEncontrado.setIdAcompanhante(rs.getLong("id_acompanhante"));
                acompanhanteEncontrado.setNomeCompleto(rs.getString("nc_nome_completo"));
                acompanhanteEncontrado.setTelefone(rs.getString("tl_telefone"));
                acompanhanteEncontrado.setCpf(rs.getString("dc_cpf"));
                acompanhanteEncontrado.setDataNascimento(rs.getDate("dt_data_nascimento").toLocalDate());
                acompanhanteEncontrado.setEmail(rs.getString("em_email"));
                acompanhanteEncontrado.setParentesco(rs.getString("pr_parentesco"));
            }else{
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Erro na consulta: " + e.getMessage());
        }finally {
            ConnectionFactory.closeConnection();
        }
        return acompanhanteEncontrado;
    }

    /**
     * Busca todos os acompanhantes de um paciente específico
     * @param idPaciente o ID do paciente
     * @return lista de objetos AcompanhanteTO com os dados dos acompanhantes encontrados
     */
    public ArrayList<AcompanhanteTO> findAllByPaciente(Long idPaciente) {
        String sql = "SELECT * FROM T_ELO_ACOMPANHANTE WHERE id_paciente=?";

        ArrayList<AcompanhanteTO> acompanhantes = new ArrayList<>();

        try(PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ps.setLong(1,idPaciente);
            ResultSet rs = ps.executeQuery();
            if(rs != null){
                while (rs.next()) {
                    AcompanhanteTO acompanhante = new AcompanhanteTO();
                    acompanhante.setIdPaciente(rs.getLong("id_paciente"));
                    acompanhante.setIdAcompanhante(rs.getLong("id_acompanhante"));
                    acompanhante.setNomeCompleto(rs.getString("nc_nome_completo"));
                    acompanhante.setCpf(rs.getString("dc_cpf"));
                    acompanhante.setDataNascimento(rs.getDate("dt_data_nascimento").toLocalDate());
                    acompanhante.setTelefone(rs.getString("tl_telefone"));
                    acompanhante.setEmail(rs.getString("em_email"));
                    acompanhante.setParentesco(rs.getString("pr_parentesco"));
                    acompanhantes.add(acompanhante);
                }
            }else{
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Erro no comando SQL " + e.getMessage());
        }
        finally {
            ConnectionFactory.closeConnection();
        }
        return acompanhantes;
    }

    /**
     * Busca um acompanhante específico no banco de dados a partir do CPF
     * @param cpf do acompanhante a ser buscado
     * @return objeto acompanhanteTO com os dados do acompanhante encontrado ou null
     */
    public AcompanhanteTO findByCpf(String cpf) {
        AcompanhanteTO acompanhanteEncontrado = new AcompanhanteTO();
        String sql = "SELECT * FROM T_ELO_ACOMPANHANTE where dc_cpf=?";

        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ps.setString(1, cpf);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                acompanhanteEncontrado.setIdPaciente(rs.getLong("id_paciente"));
                acompanhanteEncontrado.setIdAcompanhante(rs.getLong("id_acompanhante"));
                acompanhanteEncontrado.setNomeCompleto(rs.getString("nc_nome_completo"));
                acompanhanteEncontrado.setCpf(rs.getString("dc_cpf"));
                acompanhanteEncontrado.setDataNascimento(rs.getDate("dt_data_nascimento").toLocalDate());
                acompanhanteEncontrado.setTelefone(rs.getString("tl_telefone"));
                acompanhanteEncontrado.setEmail(rs.getString("em_email"));
                acompanhanteEncontrado.setParentesco(rs.getString("pr_parentesco"));
            }else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Erro no comando SQL " + e.getMessage());
        } finally {
            ConnectionFactory.closeConnection();
        }
        return acompanhanteEncontrado;
    }

}

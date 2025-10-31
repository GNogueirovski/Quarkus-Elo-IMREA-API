package br.com.fiap.dao;

import br.com.fiap.to.ColaboradorTO;
import br.com.fiap.to.ColaboradorTO;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ColaboradorDAO {

    /**
     * Insere um novo colaborador no banco de dados
     * @param colaboradorTO objeto contendo os dados do colaborador a ser inserido
     * @return Colaborador salvo confirmando o sucessod da operação ou nulo
     */

    public ColaboradorTO save(ColaboradorTO colaboradorTO) {

        String sql = "insert into T_ELO_COLABORADOR (nc_nome_completo, dt_data_nascimento, dc_cpf, tl_telefone, em_email, un_unidade) values (?,?,?,?,?,?)";

        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ps.setString(1, colaboradorTO.getNomeCompleto());
            ps.setDate(2, Date.valueOf(colaboradorTO.getDataNascimento()));
            ps.setString(3, colaboradorTO.getCpf());
            ps.setString(4, colaboradorTO.getTelefone());
            ps.setString(5, colaboradorTO.getEmail());
            ps.setString(6, colaboradorTO.getUnidade());

            if (ps.executeUpdate() > 0) {
                return colaboradorTO;
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
     * Altera os dados de um colaborador no banco de dados
     * @param colaboradorTO objeto contendo os novos dados do colaborador
     * @return mensagem informando o resultado da operação
     */
    public ColaboradorTO update(ColaboradorTO colaboradorTO) {

        String sql = "UPDATE T_ELO_COLABORADOR set nc_nome_completo=?, dt_data_nascimento=?, dc_cpf=?, tl_telefone=?, em_email=?, un_unidade=? where id_colaborador=?";

        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ps.setString(1, colaboradorTO.getNomeCompleto());
            ps.setDate(2, Date.valueOf(colaboradorTO.getDataNascimento()));
            ps.setString(3, colaboradorTO.getCpf());
            ps.setString(4, colaboradorTO.getTelefone());
            ps.setString(5, colaboradorTO.getEmail());
            ps.setString(6, colaboradorTO.getUnidade());
            ps.setLong(7, colaboradorTO.getIdColaborador());
            if (ps.executeUpdate() > 0) {
                return colaboradorTO;
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
     * Exclui um colaborador do banco de dados
     * @param id ID do colaborador a ser excluído
     * @return mensagem informando o resultado da operação
     */
    public boolean delete(Long id) {

        String sql = "DELETE FROM T_ELO_COLABORADOR where id_colaborador=?";
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
     * Busca todos os colaboradores no banco de dados
     * @return lista de objetos de ColaboradorTO com todos os colaboradores ou null se não encontrar
     */
    public ArrayList<ColaboradorTO> findAll() {
        ArrayList<ColaboradorTO> colaboradores = new ArrayList<>();
        String sql = "SELECT * FROM T_ELO_COLABORADOR";
        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    ColaboradorTO colaboradorEncontrado = new ColaboradorTO();
                    colaboradorEncontrado.setIdColaborador(rs.getLong("id_colaborador"));
                    colaboradorEncontrado.setNomeCompleto(rs.getString("nc_nome_completo"));
                    colaboradorEncontrado.setTelefone(rs.getString("tl_telefone"));
                    colaboradorEncontrado.setCpf(rs.getString("dc_cpf"));
                    colaboradorEncontrado.setDataNascimento(rs.getDate("dt_data_nascimento").toLocalDate());
                    colaboradorEncontrado.setEmail(rs.getString("em_email"));
                    colaboradorEncontrado.setUnidade(rs.getString("un_unidade"));
                    colaboradores.add(colaboradorEncontrado);
                }}else{
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Erro na consulta: " + e.getMessage());
        }finally {
            ConnectionFactory.closeConnection();
        }
        return colaboradores;
    }


    /**
     * Busca um colaborador no banco de dados
     * @param id O ID do colaborador a ser buscado
     * @return objeto ColaboradorTO com os dados do colaborador ou null se não encontrar
     */
    public ColaboradorTO findById(Long id) {
        ColaboradorTO colaboradorEncontrado = new ColaboradorTO();
        String sql = "SELECT * FROM T_ELO_COLABORADOR where id_colaborador=?";

        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                colaboradorEncontrado.setIdColaborador(rs.getLong("id_colaborador"));
                colaboradorEncontrado.setNomeCompleto(rs.getString("nc_nome_completo"));
                colaboradorEncontrado.setTelefone(rs.getString("tl_telefone"));
                colaboradorEncontrado.setCpf(rs.getString("dc_cpf"));
                colaboradorEncontrado.setDataNascimento(rs.getDate("dt_data_nascimento").toLocalDate());
                colaboradorEncontrado.setEmail(rs.getString("em_email"));
                colaboradorEncontrado.setUnidade(rs.getString("un_unidade"));
            }else{
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Erro na consulta: " + e.getMessage());
        }finally {
            ConnectionFactory.closeConnection();
        }
        return colaboradorEncontrado;
    }

    /**
     * Busca um colaborador específico no banco de dados a partir do CPF
     * @param cpf do colaborador a ser buscado
     * @return objeto colaboradorTO com os dados do colaborador encontrado ou null
     */
    public ColaboradorTO findByCpf(String cpf) {
        ColaboradorTO colaboradorEncontrado = new ColaboradorTO();
        String sql = "SELECT * FROM T_ELO_COLABORADOR where dc_cpf=?";

        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ps.setString(1, cpf);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                colaboradorEncontrado.setIdColaborador(rs.getLong("id_colaborador"));
                colaboradorEncontrado.setNomeCompleto(rs.getString("nc_nome_completo"));
                colaboradorEncontrado.setCpf(rs.getString("dc_cpf"));
                colaboradorEncontrado.setDataNascimento(rs.getDate("dt_data_nascimento").toLocalDate());
                colaboradorEncontrado.setTelefone(rs.getString("tl_telefone"));
                colaboradorEncontrado.setEmail(rs.getString("em_email"));
                colaboradorEncontrado.setUnidade(rs.getString("un_unidade"));
            }else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Erro no comando SQL " + e.getMessage());
        } finally {
            ConnectionFactory.closeConnection();
        }
        return colaboradorEncontrado;
    }


}

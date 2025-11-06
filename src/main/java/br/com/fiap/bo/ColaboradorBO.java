package br.com.fiap.bo;

import br.com.fiap.dao.AtendimentoDAO;
import br.com.fiap.dao.ColaboradorDAO;
import br.com.fiap.dao.LembreteDAO;
import br.com.fiap.exception.ColaboradorException;
import br.com.fiap.to.ColaboradorTO;

import java.util.ArrayList;

/**
 * Classe de com regras de negócio para gerenciar colaboradores e utilizar os métodos DAO para realizar as ações com banco de dados.
 * @version 1.0
 */
public class ColaboradorBO {
    private ColaboradorDAO colaboradorDAO;

    /**
     * Salva um novo colaborador no sistema após validar os dados.
     *
     * @param colaboradorTO Objeto de ColaboradorTO com os dados do colaborador a ser salvo.
     * @return O objeto ColaboradorTO salvo com o ID gerado.
     * @throws ColaboradorException Se o CPF já estiver cadastrado no sistema.
     */
    public ColaboradorTO save(ColaboradorTO colaboradorTO) throws ColaboradorException {
        String telefone = colaboradorTO.getTelefone().replace("(", "").replace("-", "").replace(")", "").replace(" ", "");
        String cpf = colaboradorTO.getCpf().replace(".", "").replace("-", "");

        colaboradorTO.setCpf(cpf);
        colaboradorTO.setTelefone(telefone);

        colaboradorDAO = new ColaboradorDAO();

        if (colaboradorDAO.findByCpf(cpf) != null) {
            throw new ColaboradorException("O CPF informado já pertence a outro colaborador.");
        }

        return colaboradorDAO.save(colaboradorTO);
    }

    /**
     * Atualiza os dados de um colaborador existente após validar os dados.
     * @param colaboradorTO Objeto de ColaboradorTO com os dados do colaborador a ser atualizado.
     * @return O objeto ColaboradorTO atualizado.
     * @throws ColaboradorException Se o colaborador não existir ou se o CPF já estiver cadastrado para outro colaborador.
     */
    public ColaboradorTO update(ColaboradorTO colaboradorTO) throws ColaboradorException {
        String telefone = colaboradorTO.getTelefone().replace("(", "").replace("-", "").replace(")", "").replace(" ", "");
        String cpf = colaboradorTO.getCpf().replace(".", "").replace("-", "");

        colaboradorTO.setCpf(cpf);
        colaboradorTO.setTelefone(telefone);

        colaboradorDAO = new ColaboradorDAO();
        if (colaboradorDAO.findById(colaboradorTO.getIdColaborador()) == null) {
            throw new ColaboradorException("Não existe nenhum colaborador com o ID informado para ser atualizado");
        }
        ColaboradorTO colaboradorEncontrado = colaboradorDAO.findByCpf(cpf);
        if (colaboradorEncontrado != null && !colaboradorEncontrado.getIdColaborador().equals(colaboradorTO.getIdColaborador())) {
            throw new ColaboradorException("O CPF informado já pertence a outro colaborador.");
        }

        return colaboradorDAO.update(colaboradorTO);
    }
    /**
     * Exclui um colaborador do sistema após verificar se ele existe e se não possui lembretes vinculados.
     * @param id ID do colaborador a ser excluído.
     * @return true se o colaborador foi excluído com sucesso, false se o colaborador não existir.
     * @throws ColaboradorException Se existirem lembretes vinculados ao colaborador.
     */
    public boolean delete(Long id) throws ColaboradorException {
        colaboradorDAO = new ColaboradorDAO();
        if (colaboradorDAO.findById(id) == null) {
            return false;
        }
        LembreteDAO lembreteDAO = new LembreteDAO();
        if(!lembreteDAO.findAllByColaborador(id).isEmpty()){
            throw new ColaboradorException("Não é possível excluir o colaborador pois existem lembretes vinculados a ele.");
        }

        return colaboradorDAO.delete(id);
    }

    /**
     * Retorna uma lista de todos os colaboradores cadastrados no sistema.
     * @return Uma lista de objetos ColaboradorTO.
     */
    public ArrayList<ColaboradorTO> findAll() {
        colaboradorDAO = new ColaboradorDAO();
        return colaboradorDAO.findAll();
    }

    /**
     * Busca um colaborador pelo seu ID.
     * @param id ID do colaborador a ser buscado.
     * @return O objeto ColaboradorTO correspondente ao ID informado, ou null se não encontrado.
     */
    public ColaboradorTO findById(Long id) {
        colaboradorDAO = new ColaboradorDAO();
        return colaboradorDAO.findById(id);
    }

}

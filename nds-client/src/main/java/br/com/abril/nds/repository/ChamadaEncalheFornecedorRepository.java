package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.model.planejamento.fornecedor.ChamadaEncalheFornecedor;
import br.com.abril.nds.util.Intervalo;

/**
 * Interface do repositório para manipulação dos dados armazenados
 * da entidade {@link ChamadaEncalheFornecedor}
 * 
 * @author francisco.garcia
 *
 */
public interface ChamadaEncalheFornecedorRepository extends Repository<ChamadaEncalheFornecedor, Long> {

    /**
     * Recupera as chamadas de encalhe do fornecedor de acordo com os parâmetros
     * disponíveis
     * 
     * @param idFornecedor
     *            identificador do fornecedor, pode ser nulo, neste caso, indica
     *            todos os fornecedores
     * @param numeroSemana
     *            numero da semana para recuperação das chamadas de encalhe,
     *            pode ser nulo, neste caso a pesquisa será por intervalo de
     *            recolhimento
     * @param periodo
     *            período de recolhimento das chamadas de encalhe, pode ser
     *            nulo, neste caso a pesquisa será por número da semana de
     *            recolhimento
     * @return lista das chamadas de encalhe que satisfazem os parâmetros de
     *         consulta
     */
    List<ChamadaEncalheFornecedor> obterChamadasEncalheFornecedor(Long idFornecedor, Integer numeroSemana,  Intervalo<Date> periodo);
	
	

}

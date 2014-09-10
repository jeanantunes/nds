package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.filtro.FiltroFechamentoCEIntegracaoDTO;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.planejamento.fornecedor.ChamadaEncalheFornecedor;

/**
 * Interface do repositório para manipulação dos dados armazenados
 * da entidade {@link ChamadaEncalheFornecedor}
 * 
 * @author francisco.garcia
 *
 */
public interface ChamadaEncalheFornecedorRepository extends Repository<ChamadaEncalheFornecedor, Long> {
    
    
    List<ChamadaEncalheFornecedor> obterChamadasEncalheFornecedor(FiltroFechamentoCEIntegracaoDTO filtro);
    
    /**
     * Obtém lista de CE Fornecedor com Diferença(Perda/Ganho) Pendente
     * 
     * @param listaIdCeFornecedor
     * @param statusConfirmacao
     * @return List<ChamadaEncalheFornecedor>
     */
    List<ChamadaEncalheFornecedor> obtemCEFornecedorComDiferencaPendente(List<Long> listaIdCeFornecedor, StatusConfirmacao statusConfirmacao);
    
    List<Long> obterIdentificadorFornecedoresChamadasEncalheFornecedor(FiltroFechamentoCEIntegracaoDTO filtro);
}

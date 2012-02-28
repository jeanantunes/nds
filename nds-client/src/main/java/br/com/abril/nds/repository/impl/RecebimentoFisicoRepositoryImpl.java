package br.com.abril.nds.repository.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.RecebimentoFisicoDTO;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.estoque.RecebimentoFisico;
import br.com.abril.nds.model.fiscal.ItemNotaFiscal;
import br.com.abril.nds.model.fiscal.NotaFiscalFornecedor;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.repository.RecebimentoFisicoRepository;

/**
 * 
 * @author Gustavo
 *
 */
@Repository
public class RecebimentoFisicoRepositoryImpl extends AbstractRepository<RecebimentoFisico, Long> 
									  implements RecebimentoFisicoRepository {

	/**
	 * Construtor padrão.
	 */
	public RecebimentoFisicoRepositoryImpl() {
		super(RecebimentoFisico.class);
	}


	
	@Override
	public void alterarOrSalvarDiferencaRecebimentoFisico(List<RecebimentoFisicoDTO> listaRecebimentoFisicoDTO,
			ItemRecebimentoFisico itemRecebimentoFisico){
		//pegando todas as quantidades Fisicas e altera ou salva na tabela ItemRecebimentoFisico
		for(RecebimentoFisicoDTO dto : listaRecebimentoFisicoDTO){
			itemRecebimentoFisico.setQtdeFisico(dto.getQtdFisico());
			this.alterarOrSaveItemRecebimento(itemRecebimentoFisico);
		}		
	}
	
	private void alterarOrSaveItemRecebimento(ItemRecebimentoFisico itemrecebimentoFisico){
		getSession().saveOrUpdate(itemrecebimentoFisico);
	}
	
	public void salvarProdutoRecebimentoFisico(ProdutoEdicao produtoEdicao, Lancamento lancamento, NotaFiscalFornecedor notaFiscal,
			ItemNotaFiscal itemNotaFiscal){
		ProdutoEdicaoRepositoryImpl prod = new ProdutoEdicaoRepositoryImpl();
		prod.adicionar(produtoEdicao);
		
		LancamentoRepositoryImpl lancamentoRepository = new LancamentoRepositoryImpl();
		lancamentoRepository.adicionar(lancamento);
		
		ItemNotaFiscalRepositoryImpl itemNotaFiscalRepository = new ItemNotaFiscalRepositoryImpl();
		itemNotaFiscalRepository.adicionar(itemNotaFiscal);
		
	
	}
	
}
	


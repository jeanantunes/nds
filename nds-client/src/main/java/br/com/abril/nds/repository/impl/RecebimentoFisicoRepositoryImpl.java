package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<RecebimentoFisicoDTO>  obterItemNotaPorCnpjNota(String cnpj, String numeroNota, String serieNota ) {
		//nao mostrar Diferenca negativa na Tela		
		String hql =  "select new "+ RecebimentoFisicoDTO.class.getCanonicalName()+ 
				" ( ir.itemNotaFiscal.produtoEdicao.produto.id,  " +//codigo
				"ir.itemNotaFiscal.produtoEdicao.produto.nome,  " +//nomeProduto
				"ir.itemNotaFiscal.produtoEdicao.numeroEdicao,  " +//edicao
				"ir.itemNotaFiscal.produtoEdicao.precoVenda, " +//precocapa
				"ir.itemNotaFiscal.qtde, " +//reparteprevisto
				"ir.qtdeFisico, " +//qtefisico
				"ir.itemNotaFiscal.produtoEdicao.) " +//diferença
				"from ItemRecebimentoFisico ir, Diferenca d  " +
				"where ir.itemNotaFiscal.notaFiscal.emitente.cnpj = :cnpj and " +
				"ir.itemNotaFiscal.notaFiscal.numero = :numeroNota and " +
				"ir.itemNotaFiscal.notaFiscal.numero.serie = :serieNota";
		//RecebimentoFisicoDTO(Long codigo, String nomeProduto, Long edicao, BigDecimal precoCapa, Long repartePrevisto, BigDecimal qtdFisico, BigDecimal diferenca){
		Query query = getSession().createQuery(hql);
		query.setString("cnpj",cnpj);
		query.setString("numeroNota",numeroNota);
		query.setString("serieNota",serieNota);		
		return query.list();
		
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
	


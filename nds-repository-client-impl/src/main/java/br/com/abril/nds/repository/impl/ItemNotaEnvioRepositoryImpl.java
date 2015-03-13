package br.com.abril.nds.repository.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.ResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.DetalheItemNotaFiscalDTO;
import br.com.abril.nds.model.envio.nota.ItemNotaEnvio;
import br.com.abril.nds.model.envio.nota.ItemNotaEnvioPK;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.ItemNotaEnvioRepository;

@Repository
public class ItemNotaEnvioRepositoryImpl extends AbstractRepositoryModel<ItemNotaEnvio, ItemNotaEnvioPK> implements ItemNotaEnvioRepository {

	public ItemNotaEnvioRepositoryImpl() {
		super(ItemNotaEnvio.class);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<DetalheItemNotaFiscalDTO> obterItensNotaEnvio(Date dataEmissao, Integer numeroCota) {
		
		String hql = "select produto.codigo as codigoProduto, produto.nome as nomeProduto, "
				   + " produtoEdicao.numeroEdicao as numeroEdicao, produtoEdicao.precoVenda as precoVenda, "
				   + " sum(itemNotaEnvio.reparte) as quantidadeExemplares, produtoEdicao.id as idProdutoEdicao, "
				   + " produtoEdicao.pacotePadrao as pacotePadrao "
				   + " from ItemNotaEnvio itemNotaEnvio "
				   + " join itemNotaEnvio.itemNotaEnvioPK.notaEnvio notaEnvio "
				   + " join itemNotaEnvio.produtoEdicao produtoEdicao "
				   + " join produtoEdicao.produto produto "
				   + " where notaEnvio.dataEmissao = :dataEmissao "
				   + " and notaEnvio.destinatario.numeroCota = :numeroCota "
				   + " group by produtoEdicao.id ";
		
		Query query = super.getSession().createQuery(hql);
		
		ResultTransformer resultTransformer =
			new AliasToBeanResultTransformer(DetalheItemNotaFiscalDTO.class); 

		query.setParameter("dataEmissao", dataEmissao);
		query.setParameter("numeroCota", numeroCota);
		
		query.setResultTransformer(resultTransformer);
		
		return query.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<DetalheItemNotaFiscalDTO> obterItensNotaEnvioLancamentoProduto(Date dataEmissao, Integer numeroCota) {

		String hqlFaltasSobras = " select sum( "
						       + "            case tm.grupoMovimentoEstoque when 'FALTA_EM_COTA' then (mEstCota.qtde * -1) else "
						       + "            case tm.grupoMovimentoEstoque when 'FALTA_DE_COTA' then (mEstCota.qtde * -1) else "
				               + "            case tm.grupoMovimentoEstoque when 'SOBRA_EM_COTA' then (mEstCota.qtde) else "
				               + "            case tm.grupoMovimentoEstoque when 'SOBRA_DE_COTA' then (mEstCota.qtde) end end end end "
						       + "            ) "
						       + " from MovimentoEstoqueCota mEstCota "
						       + " join mEstCota.tipoMovimento tm "
						       + " join mEstCota.cota c "
						       + " join mEstCota.produtoEdicao pEdicao "
						       + " join mEstCota.lancamento lcto "
		                       + " where pEdicao.id = produtoEdicao.id "
		                       + " and c.numeroCota = cota.numeroCota "
		                       + " and lcto.dataLancamentoDistribuidor = :dataEmissao ";
		
		String hqlFaltasSobrasTratado = " COALESCE(("+hqlFaltasSobras+"),0) ";
		
		String hql = " select produto.codigo as codigoProduto, "
				   + " produto.nome as nomeProduto, "
				   + " produtoEdicao.numeroEdicao as numeroEdicao, "
				   + " produtoEdicao.precoVenda as precoVenda, "  
				   + " sum(CASE tipoMovimento.operacaoEstoque WHEN 'ENTRADA' THEN (mec.qtde) ELSE (mec.qtde * -1) END) as quantidadeExemplares,"
				   +   hqlFaltasSobrasTratado + " as sobrasFaltas,"
				   + " produtoEdicao.id as idProdutoEdicao, "
				   + " produtoEdicao.pacotePadrao as pacotePadrao "
				   + " from MovimentoEstoqueCota mec "
				   + " join mec.cota cota "
				   + " join mec.lancamento lancamento "
				   + " join mec.tipoMovimento tipoMovimento "
				   + " join mec.produtoEdicao produtoEdicao "
				   + " join produtoEdicao.produto produto "
				   + " where lancamento.dataLancamentoDistribuidor = :dataEmissao "
				   + " and cota.numeroCota = :numeroCota "
				   + " and tipoMovimento.grupoMovimentoEstoque not in (:gruposMovimentosEstoqueFaltasESobras) "
				   + " group by produtoEdicao.id "
				   + " order by produto.nome ";		
		
		Query query = super.getSession().createQuery(hql);
		
		ResultTransformer resultTransformer =
			new AliasToBeanResultTransformer(DetalheItemNotaFiscalDTO.class); 

		query.setParameter("dataEmissao", dataEmissao);
		
		query.setParameter("numeroCota", numeroCota);
		
		query.setParameterList("gruposMovimentosEstoqueFaltasESobras", Arrays.asList(GrupoMovimentoEstoque.FALTA_DE_COTA,
				                                                                     GrupoMovimentoEstoque.FALTA_EM_COTA,
				                                                                     GrupoMovimentoEstoque.SOBRA_DE_COTA,
				                                                                     GrupoMovimentoEstoque.SOBRA_EM_COTA));
		
		query.setResultTransformer(resultTransformer);
		
		return query.list();
	}
	
	@Override
	public DetalheItemNotaFiscalDTO obterItemNotaEnvio(Date dataEmissao,
													   Integer numeroCota,
													   Long idProdutoEdicao) {
		
		String hql = "select produto.codigo as codigoProduto, produto.nome as nomeProduto, "
				   + " produtoEdicao.numeroEdicao as numeroEdicao, produtoEdicao.precoVenda as precoVenda, "
				   + " sum(itemNotaEnvio.reparte) as quantidadeExemplares, produtoEdicao.id as idProdutoEdicao, "
				   + " produtoEdicao.pacotePadrao as pacotePadrao "
				   + " from ItemNotaEnvio itemNotaEnvio "
				   + " join itemNotaEnvio.itemNotaEnvioPK.notaEnvio notaEnvio "
				   + " join itemNotaEnvio.produtoEdicao produtoEdicao "
				   + " join produtoEdicao.produto produto "
				   + " where notaEnvio.dataEmissao = :dataEmissao "
				   + " and notaEnvio.destinatario.numeroCota = :numeroCota "
				   + " and produtoEdicao.id = :idProdutoEdicao "
				   + " group by produtoEdicao.id ";
		
		Query query = super.getSession().createQuery(hql);
		
		ResultTransformer resultTransformer =
			new AliasToBeanResultTransformer(DetalheItemNotaFiscalDTO.class); 

		query.setParameter("dataEmissao", dataEmissao);
		query.setParameter("numeroCota", numeroCota);
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		
		query.setResultTransformer(resultTransformer);
		
		return (DetalheItemNotaFiscalDTO) query.uniqueResult();
	}
	
	
	@Override
	public DetalheItemNotaFiscalDTO obterItemNotaEnvioLancamentoProduto(Date dataEmissao,
													   							Integer numeroCota,
													   							Long idProdutoEdicao) {
		
		String hql = "select produto.codigo as codigoProduto, produto.nome as nomeProduto, "
				   + " produtoEdicao.numeroEdicao as numeroEdicao, produtoEdicao.precoVenda as precoVenda, "
				   + " sum(itemNotaEnvio.reparte) as quantidadeExemplares, produtoEdicao.id as idProdutoEdicao, "
				   + " produtoEdicao.pacotePadrao as pacotePadrao "
				   + " from ItemNotaEnvio itemNotaEnvio "
				   + " join itemNotaEnvio.itemNotaEnvioPK.notaEnvio notaEnvio "
				   + " join itemNotaEnvio.produtoEdicao produtoEdicao "
				   + " join produtoEdicao.produto produto "
 				   + " join itemNotaEnvio.estudoCota estudoCota "
 				   + " join estudoCota.estudo estudo "
 				   + " join estudo.lancamentos lancamento "
				   + " where lancamento.dataLancamentoDistribuidor = :dataEmissao "
				   + " and notaEnvio.destinatario.numeroCota = :numeroCota "
				   + " and produtoEdicao.id = :idProdutoEdicao "
				   + " group by produtoEdicao.id ";
		
		Query query = super.getSession().createQuery(hql);
		
		ResultTransformer resultTransformer =
			new AliasToBeanResultTransformer(DetalheItemNotaFiscalDTO.class); 

		query.setParameter("dataEmissao", dataEmissao);
		query.setParameter("numeroCota", numeroCota);
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		
		query.setResultTransformer(resultTransformer);
		
		return (DetalheItemNotaFiscalDTO) query.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ItemNotaEnvio> obterItemNotaEnvio(Long idLancamento) {
		
		String hql = " select itemNotaEnvio "
			+ " from Lancamento lancamento "
			+ " join lancamento.estudo estudo "
			+ " join estudo.estudoCotas estudoCotas "
			+ " join estudoCotas.itemNotaEnvios itemNotaEnvio "
			+ " where lancamento.id = :idLancamento ";

		Query query = this.getSession().createQuery(hql);

		query.setParameter("idLancamento", idLancamento);

		return query.list();
	}
	
	@Override
	public void removerItemNotaEnvioPorEstudo(Long idEstudo) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" update ItemNotaEnvio itemNota set itemNota.estudoCota = null");
		hql.append(" where itemNota.estudoCota.id in (");
		hql.append(" select id from EstudoCota");
		hql.append(" where estudo.id = :idEstudo)");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idEstudo", idEstudo);
		
		query.executeUpdate();
	}
	
}

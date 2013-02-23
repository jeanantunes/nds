package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.ResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.DetalheItemNotaFiscalDTO;
import br.com.abril.nds.model.envio.nota.ItemNotaEnvio;
import br.com.abril.nds.model.envio.nota.ItemNotaEnvioPK;
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
		
		String hql = "select produto.codigo as codigoProduto, produto.nome as nomeProduto, "
				   + " produtoEdicao.numeroEdicao as numeroEdicao, produtoEdicao.precoVenda as precoVenda, "
				   + " sum(itemNotaEnvio.reparte) as quantidadeExemplares, produtoEdicao.id as idProdutoEdicao, "
				   + " produtoEdicao.pacotePadrao as pacotePadrao "
				   + " from ItemNotaEnvio itemNotaEnvio "
				   + " join itemNotaEnvio.itemNotaEnvioPK.notaEnvio notaEnvio "
				   + " join itemNotaEnvio.produtoEdicao produtoEdicao "
				   + " join produtoEdicao.produto produto "
				   + " join produtoEdicao.lancamentos lancamento "
				   + " where lancamento.dataLancamentoDistribuidor = :dataEmissao "
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
				   + " join produtoEdicao.lancamentos lancamento "
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
			+ " join estudoCotas.itemNotaEnvio itemNotaEnvio "
			+ " where lancamento.id = :idLancamento ";

		Query query = this.getSession().createQuery(hql);

		query.setParameter("idLancamento", idLancamento);

		return query.list();
	}
	
}

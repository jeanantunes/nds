package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.LancamentoPorEdicaoDTO;
import br.com.abril.nds.dto.VendaEncalheDTO;
import br.com.abril.nds.dto.VendaProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroVendaEncalheDTO;
import br.com.abril.nds.dto.filtro.FiltroVendaEncalheDTO.OrdenacaoColuna;
import br.com.abril.nds.dto.filtro.FiltroVendaProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroVendaProdutoDTO.ColunaOrdenacao;
import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.model.estoque.TipoVendaEncalhe;
import br.com.abril.nds.repository.VendaProdutoRepository;

@Repository
public class VendaProdutoRepositoryImpl extends AbstractRepository<MovimentoEstoque, Long> implements VendaProdutoRepository {

	public VendaProdutoRepositoryImpl() {
		super(MovimentoEstoque.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VendaProdutoDTO> buscarVendaPorProduto(FiltroVendaProdutoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT estoqueProduto.produtoEdicao.numeroEdicao as numEdicao, ");
		hql.append(" lancamento.dataLancamentoDistribuidor as dataLancamento, ");
		hql.append(" lancamento.dataRecolhimentoDistribuidor as dataRecolhimento, ");
		hql.append(" (estoqueProduto.qtde + estoqueProduto.qtdeSuplementar) as reparte, ");
		hql.append(" ((estoqueProduto.qtde + estoqueProduto.qtdeSuplementar) - estoqueProduto.qtdeDevolucaoEncalhe)  as venda, ");
		hql.append(" (((estoqueProduto.qtde + estoqueProduto.qtdeSuplementar) - estoqueProduto.qtdeDevolucaoEncalhe) / (estoqueProduto.qtde + estoqueProduto.qtdeSuplementar))  as percentagemVenda, ");
		hql.append(" produtoEdicao.precoVenda  as precoCapa, ");
		hql.append(" (((estoqueProduto.qtde + estoqueProduto.qtdeSuplementar) - estoqueProduto.qtdeDevolucaoEncalhe) * produtoEdicao.precoVenda)  as total ");
		
		
		hql.append(getSqlFromEWhereVendaPorProduto(filtro));
		
		hql.append(getOrderByPorEdicoes(filtro));
		
		Query query =  getSession().createQuery(hql.toString());
		
		HashMap<String, Object> param = buscarParametrosVendaProduto(filtro);
		
		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
		}
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				VendaProdutoDTO.class));
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
				
		return query.list();
		 
	}
	
	private String getSqlFromEWhereVendaPorProduto(FiltroVendaProdutoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
	

		hql.append(" from EstoqueProduto estoqueProduto ");
		hql.append(" LEFT JOIN estoqueProduto.produtoEdicao as produtoEdicao ");
		hql.append(" LEFT JOIN estoqueProduto.produtoEdicao.lancamentos as lancamento ");		
		hql.append(" LEFT JOIN estoqueProduto.produtoEdicao.produto.fornecedores as fornecedor ");
		
		boolean usarAnd = false;
		
		if(filtro.getCodigo() != null && !filtro.getCodigo().isEmpty()) { 
			hql.append( (usarAnd ? " and ":" where ") + "produtoEdicao.produto.codigo = :codigo ");
			usarAnd = true;
		}
		if(filtro.getEdicao() !=null){
			hql.append( (usarAnd ? " and ":" where ") + " produtoEdicao.numeroEdicao = :edicao ");
			usarAnd = true;
		}
		if(filtro.getIdFornecedor() !=null){
			hql.append( (usarAnd ? " and ":" where ") + " fornecedor.id = :idFornecedor ");
			usarAnd = true;
		}


		return hql.toString();
	}
	
	private String getOrderByPorEdicoes(FiltroVendaProdutoDTO filtro){
		
		if(filtro.getPaginacao() == null || filtro.getPaginacao().getSortColumn() == null){
			return "";
		}
		
		ColunaOrdenacao coluna = ColunaOrdenacao.getPorDescricao(filtro.getPaginacao().getSortColumn());
		
		StringBuilder hql = new StringBuilder();
		
		switch (coluna) {
			case EDICAO:	
				hql.append(" order by estoqueProduto.produtoEdicao.numeroEdicao desc ");
				break;
		}
		
		if (filtro.getPaginacao().getOrdenacao() != null) {
			hql.append( filtro.getPaginacao().getOrdenacao().toString());
		}
		
		return hql.toString();
	}
	
	
	private HashMap<String,Object> buscarParametrosVendaProduto(FiltroVendaProdutoDTO filtro){
		
		HashMap<String,Object> param = new HashMap<String, Object>();
		
		if(filtro.getCodigo() != null && !filtro.getCodigo().isEmpty()){ 
			param.put("codigo", filtro.getCodigo());
		}
		if(filtro.getEdicao() != null){ 
			param.put("edicao", filtro.getEdicao());
		}
		if(filtro.getIdFornecedor() != null){ 
			param.put("idFornecedor", filtro.getIdFornecedor());
		}
	
		return param;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LancamentoPorEdicaoDTO> buscarLancamentoPorEdicao(
			FiltroVendaProdutoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT DISTINCT lancamento.lancamentoInicial as dataLancamento, ");
		hql.append(" lancamento.recolhimentoFinal as dataRecolhimento, ");
		hql.append(" (estoqueProduto.qtde + estoqueProduto.qtdeSuplementar) as reparte, ");
		hql.append(" estoqueProduto.qtdeDevolucaoEncalhe  as encalhe, ");
		hql.append(" ((estoqueProduto.qtde + estoqueProduto.qtdeSuplementar) - estoqueProduto.qtdeDevolucaoEncalhe)  as venda, ");
		hql.append(" ((estoqueProduto.qtde + estoqueProduto.qtdeSuplementar) - estoqueProduto.qtdeDevolucaoEncalhe)  as vendaAcumulada, ");
		hql.append(" (((estoqueProduto.qtde + estoqueProduto.qtdeSuplementar) - estoqueProduto.qtdeDevolucaoEncalhe) / (estoqueProduto.qtde + estoqueProduto.qtdeSuplementar))  as percentualVenda ");
		
		
		hql.append(getSqlFromEWhereLancamentoPorEdicao(filtro));
		
		Query query =  getSession().createQuery(hql.toString());
		
		HashMap<String, Object> param = buscarParametrosLancamentoPorEdicao(filtro);
		
		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
		}
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				LancamentoPorEdicaoDTO.class));
		
		return query.list();
		
	}
	
	private String getSqlFromEWhereLancamentoPorEdicao(FiltroVendaProdutoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
	

		hql.append(" from LancamentoParcial lancamento ");
		hql.append(" LEFT JOIN lancamento.produtoEdicao as produtoEdicao ");
		hql.append(" LEFT JOIN lancamento.produtoEdicao.produto as produto ");
		hql.append(" LEFT JOIN lancamento.produtoEdicao.movimentoEstoques as movimentoEstoque ");
		hql.append(" LEFT JOIN movimentoEstoque.estoqueProduto as estoqueProduto ");
		
		boolean usarAnd = false;
		
		if(filtro.getCodigo() != null && !filtro.getCodigo().isEmpty()) { 
			hql.append( (usarAnd ? " and ":" where ") + "produto.codigo = :codigo ");
			usarAnd = true;
		}
		if(filtro.getEdicao() !=null){
			hql.append( (usarAnd ? " and ":" where ") + " produtoEdicao.numeroEdicao = :edicao ");
			usarAnd = true;
		}

		return hql.toString();
	}
	
	private HashMap<String,Object> buscarParametrosLancamentoPorEdicao(FiltroVendaProdutoDTO filtro){
		
		HashMap<String,Object> param = new HashMap<String, Object>();
		
		if(filtro.getCodigo() != null && !filtro.getCodigo().isEmpty()){ 
			param.put("codigo", filtro.getCodigo());
		}
		if(filtro.getEdicao() != null){ 
			param.put("edicao", filtro.getEdicao());
		}		
	
		return param;
	}

	@Override
	public Long buscarQntVendaEncalhe(FiltroVendaEncalheDTO filtro){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(getSqlFromEWhereVendaEncalhe(filtro,true));
		
		Query query = getSession().createQuery(hql.toString());
		
		HashMap<String, Object> param = getParametrosVendaProdutoEncalhe(filtro);
		
		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
		}
		
		return (Long) query.uniqueResult();		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<VendaEncalheDTO> buscarVendasEncalhe(FiltroVendaEncalheDTO filtro){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(getSqlFromEWhereVendaEncalhe(filtro,false));
		
		hql.append(getOrderVendaEncalhe(filtro));
		
		Query query = getSession().createQuery(hql.toString());
		
		HashMap<String, Object> param = getParametrosVendaProdutoEncalhe(filtro);
		
		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
		}
		
		query.setResultTransformer(new AliasToBeanResultTransformer(VendaEncalheDTO.class));
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
		
		return query.list();
	}
	
	private String getSqlFromEWhereVendaEncalhe(FiltroVendaEncalheDTO filtro, boolean isCount){
		
		StringBuilder hql = new StringBuilder();

		hql.append(" select ");
		
		if(isCount){
			
			hql.append(" count ( venda.id ) ");
		}
		else{
			
			hql.append(" venda.id as idVenda ,")
			.append(" venda.dataVenda as dataVenda ,")
			.append(" case venda.cota.pessoa.class when 'F' then venda.cota.pessoa.nome when 'J' then venda.cota.pessoa.razaoSocial end  as nomeCota ,")
			.append(" venda.cota.numeroCota as numeroCota ,")
			.append(" venda.tipoVenda as tipoVenda ,")
			.append(" venda.produtoEdicao.numeroEdicao as numeroEdicao ,")
			.append(" venda.produtoEdicao.nome as nomeProduto ,")
			.append(" venda.produtoEdicao.codigo as codigoProduto ,")
			.append(" venda.produtoEdicao.precoVenda - venda.produtoEdicao.desconto as precoCapa ,")
			.append(" venda.produtoEdicao.desconto as precoDesconto ,")
			.append(" venda.valorTotalVenda as valoTotalProduto ,")
			.append(" venda.qntProduto as qntProduto ,");
		}
	
		hql.append(" from VendaProduto venda ")
			.append(" where venda.cota.numeroCota=:numeroCota ")
			.append(" and venda.dataVenda between :periodoInicial and :periodoFinal ");
		
		if(filtro.getTipoVendaEncalhe()!= null){
			hql.append(" and venda.tipoVenda=:tipoVenda ");
		}
		
		return hql.toString();
	}
	
	private HashMap<String,Object> getParametrosVendaProdutoEncalhe(FiltroVendaEncalheDTO filtro){
		
		HashMap<String,Object> param = new HashMap<String, Object>();
		
		param.put("numeroCota", filtro.getNumeroCota());
		param.put("periodoInicial", filtro.getPeriodoInicial());
		param.put("periodoFinal", filtro.getPeriodoFinal());
		
		if(filtro.getTipoVendaEncalhe() != null ){ 
			param.put("tipoVenda", filtro.getTipoVendaEncalhe());
		}
	
		return param;
	}
	
	private String getOrderVendaEncalhe(FiltroVendaEncalheDTO filtro){
		
		if(filtro.getPaginacao() == null 
				|| filtro.getPaginacao().getSortColumn() == null
				|| filtro.getOrdenacaoColuna() == null){
			return "";
		}

		StringBuilder hql = new StringBuilder();
		
		switch (filtro.getOrdenacaoColuna()) {
			case CODIGO_PRODUTO:	
				hql.append(" order by codigoProduto ");
				break;
			case DATA:	
				hql.append(" order by dataVenda ");
				break;
			case NOME_COTA:	
				hql.append(" order by nomeCota ");
				break;
			case NOME_PRODUTO:	
				hql.append(" order by nomeProduto ");
				break;
			case NUMERO_COTA:	
				hql.append(" order by numeroCota ");
				break;
			case NUMERO_EDICAO:	
				hql.append(" order by numeroEdicao ");
				break;
			case PRECO_CAPA:	
				hql.append(" order by precoCapa ");
				break;
			case PRECO_DESCONTO:	
				hql.append(" order by precoDesconto ");
				break;
			case QNT_PRODUTO:	
				hql.append(" order by qntProduto ");
				break;
			case TIPO_VENDA:	
				hql.append(" order by tipoVenda ");
				break;
			case TOTAL_VENDA:	
				hql.append(" order by valoTotalProduto ");
				break;
		}
		
		if (filtro.getPaginacao().getOrdenacao() != null) {
			hql.append( filtro.getPaginacao().getOrdenacao().toString());
		}
		
		return hql.toString();
	}
	
	@Override
	public VendaEncalheDTO buscarVendaProdutoEncalhe(Long idVendaProduto){
		
		StringBuilder hql = new StringBuilder();

		hql.append(" select ")
			.append(" venda.id as idVenda ,")
			.append(" venda.dataVenda as dataVenda ,")
			.append(" case venda.cota.pessoa.class when 'F' then venda.cota.pessoa.nome when 'J' then venda.cota.pessoa.razaoSocial end  as nomeCota ,")
			.append(" venda.cota.numeroCota as numeroCota ,")
			.append(" venda.produtoEdicao.numeroEdicao as numeroEdicao ,")
			.append(" venda.produtoEdicao.nome as nomeProduto ,")
			.append(" venda.produtoEdicao.codigo as codigoProduto ,")
			.append(" venda.produtoEdicao.precoVenda - venda.produtoEdicao.desconto as precoCapa ,")
			.append(" venda.produtoEdicao.desconto as precoDesconto ,")
			.append(" venda.valorTotalVenda as valoTotalProduto ,")
			.append(" venda.qntProduto as qntProduto ,")
			.append(" venda.produtoEdicao.codigoBarras as codigoBarras ,")
			.append(" venda.produtoEdicao.produto.formaComercializacao.value as formaVenda ,")
			.append(" venda.cota.box.codigo as codBox ,")
			.append(" venda.tipoVenda as tipoVenda ")
			.append(" venda.dataVencimentoDebito as dataVencimentoDebito ")
	        .append(" from VendaProduto venda ")
			.append(" where venda.id=:idVendaProduto");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setResultTransformer(new AliasToBeanResultTransformer(VendaEncalheDTO.class));
		
		query.setParameter("idVendaProduto", idVendaProduto);
		
		return (VendaEncalheDTO) query.uniqueResult();
			
	}
}

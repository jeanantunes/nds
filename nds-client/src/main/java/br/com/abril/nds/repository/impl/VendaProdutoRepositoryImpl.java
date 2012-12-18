package br.com.abril.nds.repository.impl;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.LancamentoPorEdicaoDTO;
import br.com.abril.nds.dto.VendaProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroVendaProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroVendaProdutoDTO.ColunaOrdenacao;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.VendaProdutoRepository;

@Repository
public class VendaProdutoRepositoryImpl extends AbstractRepositoryModel<MovimentoEstoque, Long> implements VendaProdutoRepository {

	public VendaProdutoRepositoryImpl() {
		super(MovimentoEstoque.class);
	}

	private StringBuilder getMovimentosReparte(){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select COALESCE(sum(COALESCE(mec.qtde,0)),0) ");
		hql.append(" from MovimentoEstoqueCota mec ");
		hql.append(" join mec.tipoMovimento tipoMovimento ");
		hql.append(" where mec.lancamento.id = lancamento.id ");
		hql.append(" and tipoMovimento.grupoMovimentoEstoque = :grupoMovimentoEstoque ");
		
		return hql;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<VendaProdutoDTO> buscarVendaPorProduto(FiltroVendaProdutoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select ");
		hql.append(" estoqueProduto.produtoEdicao.numeroEdicao as numEdicao, ");
		hql.append(" lancamento.dataLancamentoDistribuidor as dataLancamento, ");
		hql.append(" lancamento.dataRecolhimentoDistribuidor as dataRecolhimento, ");
		
		hql.append(" ( ");
		hql.append( this.getMovimentosReparte() );
		hql.append(" ) as reparte, ");				
		
		hql.append(" COALESCE((("+this.getMovimentosReparte()+") - estoqueProduto.qtdeDevolucaoEncalhe), 0)  as venda, ");
		hql.append(" COALESCE(ROUND((("+this.getMovimentosReparte()+") - estoqueProduto.qtdeDevolucaoEncalhe) / ("+this.getMovimentosReparte()+")), 0) as percentagemVenda, ");
		hql.append(" produtoEdicao.precoVenda  as precoCapa, ");
		hql.append(" produtoEdicao.chamadaCapa as chamadaCapa, ");
		hql.append(" COALESCE((("+this.getMovimentosReparte()+") - estoqueProduto.qtdeDevolucaoEncalhe) * produtoEdicao.precoVenda, 0)  as total ");
		
		
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
		hql.append(" JOIN estoqueProduto.produtoEdicao as produtoEdicao ");
		hql.append(" JOIN estoqueProduto.produtoEdicao.lancamentos as lancamento ");		
		hql.append(" JOIN estoqueProduto.produtoEdicao.produto.fornecedores as fornecedor ");
		
		boolean usarAnd = false;
		
		if(filtro.getCodigo() != null && !filtro.getCodigo().isEmpty()) { 
			hql.append( (usarAnd ? " and ":" where ") + "produtoEdicao.produto.codigo = :codigo ");
			usarAnd = true;
		}
		if(filtro.getEdicao() !=null){
			hql.append( (usarAnd ? " and ":" where ") + " produtoEdicao.numeroEdicao = :edicao ");
			usarAnd = true;
		}
		if(filtro.getIdFornecedor() !=null && filtro.getIdFornecedor() != -1){
			hql.append( (usarAnd ? " and ":" where ") + " fornecedor.id = :idFornecedor ");
			usarAnd = true;
		}

		hql.append("  AND lancamento.status <> :situacaoLancamento ");
		
		hql.append(" AND (select mec.movimentoEstoqueCotaFuro ");
		hql.append("      from MovimentoEstoqueCota mec ");
		hql.append("      join mec.tipoMovimento tipoMovimento ");
		hql.append("      where mec.lancamento.id = lancamento.id ");
		hql.append("      and tipoMovimento.grupoMovimentoEstoque = :grupoMovimentoEstoque) is null ");
		
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
				hql.append(" order by estoqueProduto.produtoEdicao.numeroEdicao ");
				break;
				
			case CHAMADA_CAPA:	
				hql.append(" order by chamadaCapa ");
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
		if(filtro.getIdFornecedor() != null && filtro.getIdFornecedor() != -1){ 
			param.put("idFornecedor", filtro.getIdFornecedor());
		}
	
		param.put("situacaoLancamento", StatusLancamento.EXCLUIDO);
		
		param.put("grupoMovimentoEstoque", GrupoMovimentoEstoque.RECEBIMENTO_REPARTE);
		
		return param;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LancamentoPorEdicaoDTO> buscarLancamentoPorEdicao(
			FiltroVendaProdutoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT DISTINCT lancamento.dataLancamentoDistribuidor as dataLancamento, ");
		hql.append(" lancamento.dataRecolhimentoDistribuidor as dataRecolhimento, ");
		
		hql.append(" ( ");
		hql.append( this.getMovimentosReparte() );
		hql.append(" ) as reparte, ");		
		
		hql.append(" estoqueProduto.qtdeDevolucaoEncalhe  as encalhe, ");
		hql.append(" (("+this.getMovimentosReparte()+") - estoqueProduto.qtdeDevolucaoEncalhe)  as venda, ");
		hql.append(" (("+this.getMovimentosReparte()+") - estoqueProduto.qtdeDevolucaoEncalhe)  as vendaAcumulada, ");
		hql.append(" ROUND(((("+this.getMovimentosReparte()+") - estoqueProduto.qtdeDevolucaoEncalhe) / ("+this.getMovimentosReparte()+"))) as percentualVenda ");
		
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
	
		hql.append(" from Lancamento lancamento ");
		hql.append(" JOIN lancamento.produtoEdicao as produtoEdicao ");
		hql.append(" JOIN lancamento.produtoEdicao.produto as produto ");
		hql.append(" JOIN lancamento.produtoEdicao.movimentoEstoques as movimentoEstoque ");
		hql.append(" JOIN movimentoEstoque.estoqueProduto as estoqueProduto ");
		
		boolean usarAnd = false;
		
		if(filtro.getCodigo() != null && !filtro.getCodigo().isEmpty()) { 
			hql.append( (usarAnd ? " and ":" where ") + "produto.codigo = :codigo ");
			usarAnd = true;
		}
		if(filtro.getEdicao() !=null){
			hql.append( (usarAnd ? " and ":" where ") + " produtoEdicao.numeroEdicao = :edicao ");
			usarAnd = true;
		}
		
		hql.append("  AND lancamento.status <> :situacaoLancamento ");	
		
		hql.append(" AND (select mec.movimentoEstoqueCotaFuro ");
		hql.append("      from MovimentoEstoqueCota mec ");
		hql.append("      join mec.tipoMovimento tipoMovimento ");
		hql.append("      where mec.lancamento.id = lancamento.id ");
		hql.append("      and tipoMovimento.grupoMovimentoEstoque = :grupoMovimentoEstoque) is null ");

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
		
		param.put("situacaoLancamento", StatusLancamento.EXCLUIDO);
		
		param.put("grupoMovimentoEstoque", GrupoMovimentoEstoque.RECEBIMENTO_REPARTE);
	
		return param;
	}

}

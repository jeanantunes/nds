package br.com.abril.nds.repository.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ExpedicaoDTO;
import br.com.abril.nds.dto.filtro.FiltroResumoExpedicaoDTO;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.estoque.Expedicao;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.ExpedicaoRepository;

/**
 * Classe responsável por implementar as funcionalidades referente a expedição de lançamentos de produtos.
 * 
 * @author Discover Technology
 *
 */
@Repository
public class ExpedicaoRepositoryImpl extends AbstractRepositoryModel<Expedicao,Long> implements ExpedicaoRepository {

	public ExpedicaoRepositoryImpl() {
		super(Expedicao.class);
	}

	public Long obterQuantidadeResumoExpedicaoProdutosDoBox(FiltroResumoExpedicaoDTO filtro) {
		
		Query query = getSession().createQuery(gerarQueryResumoExpedicaoProdutosDoBox(Boolean.TRUE));
		
		query.setParameter("dataLancamento", filtro.getDataLancamento());
		query.setParameter("status",StatusLancamento.EXPEDIDO);
		query.setParameter("tipoBox", TipoBox.LANCAMENTO);
		query.setParameter("codigoBox", filtro.getCodigoBox());
		
		@SuppressWarnings("unchecked")
		List<Long> conts  = query.list();
		
		return (!conts.isEmpty())?conts.size():0L;
		
	}
	
	@SuppressWarnings("unchecked")
	public List<ExpedicaoDTO> obterResumoExpedicaoProdutosDoBox(FiltroResumoExpedicaoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(gerarQueryResumoExpedicaoProdutosDoBox(Boolean.FALSE))
					
		.append(getOrderBy(filtro));
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("dataLancamento", filtro.getDataLancamento());
		query.setParameter("status",StatusLancamento.EXPEDIDO);
		query.setParameter("tipoBox", TipoBox.LANCAMENTO);
		query.setParameter("codigoBox", filtro.getCodigoBox());
		
		if (filtro.getPaginacao() != null) {
			
			if (filtro.getPaginacao().getPosicaoInicial() != null) {
				query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			}
			
			if (filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
				query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
			}
		}
		
		return query.list(); 
		
	}
	
	/**
	 * Retorna o sql referente a consulta de Reusumo de produtos expedidos
	 * @param isCount
	 * @return String
	 */
	private String gerarQueryResumoExpedicaoProdutosDoBox(boolean isCount){
		
		StringBuilder hql = new StringBuilder();
		
		if (isCount){
			
			hql.append("SELECT count (produto.codigo) ");
		}
		else{
		
			hql.append("SELECT new ") .append(ExpedicaoDTO.class.getCanonicalName()) 
			.append(" ( ") 
						.append("produto.codigo,")
						.append("produto.nome,")
						.append("produtoEd.numeroEdicao,")
						.append("produtoEd.precoVenda,")
						.append("produtoEd.desconto,")
						.append("estudo.qtdeReparte,")
						.append(" SUM (( case ")
							.append(" when (diferenca.tipoDiferenca = 'FALTA_DE') then (-(diferenca.qtde * produtoEd.pacotePadrao))")
							.append(" when (diferenca.tipoDiferenca = 'SOBRA_DE') then (diferenca.qtde * produtoEd.pacotePadrao)")
							.append(" when (diferenca.tipoDiferenca = 'FALTA_EM') then (-diferenca.qtde)")
							.append(" when (diferenca.tipoDiferenca = 'SOBRA_EM') then (diferenca.qtde)")
							.append(" else 0")
						.append(" end )) as qntDiferenca, ")
						.append(" produtoEd.precoVenda*estudo.qtdeReparte, ")
						.append(" juridica.razaoSocial ")
						
			.append(" ) ");
		}	
		
		
		
		hql.append( "FROM" )
			.append( " Box box")
			.append(" JOIN box.cotas cota")
			.append(" JOIN cota.estudoCotas estudoCota ")
			.append(" LEFT JOIN estudoCota.rateiosDiferenca rateioDiferenca ")
			.append(" LEFT JOIN rateioDiferenca.diferenca diferenca")
			.append(" JOIN estudoCota.estudo estudo")
			.append(" JOIN estudo.produtoEdicao produtoEd")
			.append(" JOIN produtoEd.produto produto ")
			.append(" JOIN produto.fornecedores fornecedor ")
			.append(" JOIN fornecedor.juridica juridica ")
			.append(" JOIN produtoEd.lancamentos lancamento ")
			.append(" JOIN lancamento.expedicao expedicao ")

			.append(" WHERE ")
			.append(" lancamento.dataLancamentoDistribuidor =:dataLancamento ")
			.append(" and lancamento.status =:status ")
			.append(" and box.tipoBox =:tipoBox ")
			.append(" and box.codigo =:codigoBox ");
		
		hql.append(" group by ")
			.append("produto.codigo,")
			.append("produto.nome,")
			.append("produtoEd.numeroEdicao,")
			.append("produtoEd.precoVenda,")
			.append("estudo.qtdeReparte,")
			.append("produtoEd.precoVenda*estudo.qtdeReparte ");

		return hql.toString();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ExpedicaoDTO> obterResumoExpedicaoPorProduto(FiltroResumoExpedicaoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(gerarQueryResumoProduto(Boolean.FALSE))
					
		.append(getOrderBy(filtro));
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("dataLancamento", filtro.getDataLancamento());
		query.setParameter("status",StatusLancamento.EXPEDIDO);
		
		if (filtro.getPaginacao() != null) {
			
			if (filtro.getPaginacao().getPosicaoInicial() != null) {
				query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			}
			
			if (filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
				query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
			}
		}
		
		return query.list(); 
		
	}
	
	/**
	 * Retorna o sql referente a consulta de Reusumo de produtos expedidos
	 * @param isCount
	 * @return String
	 */
	private String gerarQueryResumoProduto(boolean isCount){
		
		StringBuilder hql = new StringBuilder();
		
		if (isCount){
			
			hql.append("SELECT count (produto.codigo) ");
		}
		else{
		
			hql.append("SELECT new ") .append(ExpedicaoDTO.class.getCanonicalName()) 
			.append(" ( ") 
						.append("produto.codigo,")
						.append("produto.nome,")
						.append("produtoEd.numeroEdicao,")
						.append("produtoEd.precoVenda,")
						.append("estudo.qtdeReparte,")
						.append(" SUM (( case ")
							.append(" when (diferenca.tipoDiferenca = 'FALTA_DE') then (-(diferenca.qtde * produtoEd.pacotePadrao))")
							.append(" when (diferenca.tipoDiferenca = 'SOBRA_DE') then (diferenca.qtde * produtoEd.pacotePadrao)")
							.append(" when (diferenca.tipoDiferenca = 'FALTA_EM') then (-diferenca.qtde)")
							.append(" when (diferenca.tipoDiferenca = 'SOBRA_EM') then (diferenca.qtde)")
							.append(" else 0")
						.append(" end )) as qntDiferenca, ")
						.append("produtoEd.precoVenda*estudo.qtdeReparte ")
			.append(" ) ");
		}	
		
		hql.append( "FROM" )
			.append( " Estudo estudo join estudo.lancamentos lancamento ") 
			.append( " JOIN lancamento.recebimentos itemRecebimento ")
			.append("  JOIN lancamento.expedicao expedicao ")
			.append( " LEFT JOIN itemRecebimento.diferenca diferenca")
			.append( " JOIN lancamento.produtoEdicao produtoEd ")
			.append( " JOIN produtoEd.produto produto ")
			.append(" WHERE ")
			.append(" lancamento.dataLancamentoDistribuidor =:dataLancamento ")
			.append(" and lancamento.status =:status ");
		
		hql.append(" group by ")
			.append("produto.codigo,")
			.append("produto.nome,")
			.append("produtoEd.numeroEdicao,")
			.append("produtoEd.precoVenda,")
			.append("estudo.qtdeReparte,")
			.append("produtoEd.precoVenda*estudo.qtdeReparte ");

		return hql.toString();
	}
	
	/**
	 * Retorna uma string com o conteudo da ordenação da consulta
	 * @param filtro
	 * @return
	 */
	private String getOrderBy(FiltroResumoExpedicaoDTO filtro ){
		
		StringBuilder hql = new StringBuilder();
		
		if (filtro.getOrdenacaoColunaProduto() != null ){
			
			switch (filtro.getOrdenacaoColunaProduto()) {
				
				case CODIGO_PRODUTO:
					hql.append(" ORDER BY produto.codigo ");
					break;
				case DESCRICAO_PRODUTO:
					hql.append(" ORDER BY produto.nome ");
					break;
				case NUMERO_EDICAO:
					hql.append(" ORDER BY produtoEd.numeroEdicao ");
					break;
				case PRECO_CAPA:
					hql.append(" ORDER BY produtoEd.precoVenda ");
					break;
				case REPARTE:
					hql.append(" ORDER BY estudo.qtdeReparte ");
					break;
				case DIFERENCA:
					hql.append(" ORDER BY ")
					.append(" sum( ( case ")
						.append(" when (diferenca.tipoDiferenca = 'FALTA_DE') then (-(diferenca.qtde * produtoEd.pacotePadrao))")
						.append(" when (diferenca.tipoDiferenca = 'SOBRA_DE') then (diferenca.qtde *  produtoEd.pacotePadrao)")
						.append(" when (diferenca.tipoDiferenca = 'FALTA_EM') then (-diferenca.qtde)")
						.append(" when (diferenca.tipoDiferenca = 'SOBRA_EM') then (diferenca.qtde)")
						.append(" else 0")
					.append(" end ) )");
					break;
				case VALOR_FATURADO:
					hql.append(" ORDER BY  produtoEd.precoVenda*estudo.qtdeReparte ");
					break;
				default:
					hql.append(" ORDER BY produto.codigo ");
			}
			
			if (filtro.getPaginacao().getOrdenacao() != null) {
				hql.append( filtro.getPaginacao().getOrdenacao().toString());
			}
		}
		
		return hql.toString();
	}

	@Override
	public List<ExpedicaoDTO> obterResumoExpedicaoPorBox(FiltroResumoExpedicaoDTO filtro) {
		
		Query query = getSession().createQuery(getHqlResumoLancamentoPorBox());
		
		query.setParameter("dataLancamento", filtro.getDataLancamento());
		query.setParameter("status",StatusLancamento.EXPEDIDO);
		query.setParameter("tipoBox", TipoBox.LANCAMENTO);
		
		ScrollableResults results = query.scroll();
		
		List<ExpedicaoDTO> listRetorno = new ArrayList<ExpedicaoDTO>();
		ExpedicaoDTO dto;
		
		while (results.next()){
			
			dto = (ExpedicaoDTO) results.get(0);
			
			Long qntProduto = obterQuantidadeResumoExpedicaoPorBox(dto.getIdBox(), filtro.getDataLancamento());
			dto.setQntProduto(qntProduto);
			
			listRetorno.add(dto);
		}
		
		return listRetorno;
	}
	
	/**
	 * Retorna o Hql da consulta de lançamentos agrupadas por BOX
	 * @return String
	 */
	private String getHqlResumoLancamentoPorBox(){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT new ") .append(ExpedicaoDTO.class.getCanonicalName()) 
			.append(" ( ") 
			
						.append("lancamento.dataLancamentoDistribuidor, ")
						.append("box.id,")
						.append("box.codigo || '-'|| box.nome,")
						.append("box.nome,")
						.append(" SUM (produtoEdicao.precoVenda ) as totalVendas,")
						.append(" SUM (estudoCota.qtdeEfetiva) as qntReparte,")
						.append(" SUM (( case ")
							.append(" when (diferenca.tipoDiferenca = 'FALTA_DE') then (-(diferenca.qtde * produtoEdicao.pacotePadrao))")
							.append(" when (diferenca.tipoDiferenca = 'SOBRA_DE') then (diferenca.qtde * produtoEdicao.pacotePadrao)")
							.append(" when (diferenca.tipoDiferenca = 'FALTA_EM') then (-diferenca.qtde)")
							.append(" when (diferenca.tipoDiferenca = 'SOBRA_EM') then (diferenca.qtde)")
							.append(" else 0")
						.append(" end )) as qntDiferenca, ")
						.append(" (SUM (estudoCota.qtdeEfetiva)*SUM (produtoEdicao.precoVenda )) as totalFaturado")
			.append(" ) ")
			
			.append( "FROM" )
			.append( " Box box")
			.append(" JOIN box.cotas cota")
			.append(" JOIN cota.estudoCotas estudoCota ")
			.append(" LEFT JOIN estudoCota.rateiosDiferenca rateioDiferenca ")
			.append(" LEFT JOIN rateioDiferenca.diferenca diferenca")
			.append(" JOIN estudoCota.estudo estudo")
			.append(" JOIN estudo.produtoEdicao produtoEdicao")
			.append(" JOIN produtoEdicao.lancamentos lancamento ")
			.append(" JOIN lancamento.expedicao expedicao ")
			.append(" WHERE ")
			.append(" lancamento.dataLancamentoDistribuidor =:dataLancamento ")
			.append(" and lancamento.status =:status ")
			.append(" and box.tipoBox =:tipoBox ")
			
			.append(" GROUP BY box.id,box.codigo ");
		
		return hql.toString();
	}

	@Override
	public Long obterQuantidadeResumoExpedicaoPorProduto(FiltroResumoExpedicaoDTO filtro) {
		
		Query query = getSession().createQuery(gerarQueryResumoProduto(Boolean.TRUE));
		
		query.setParameter("dataLancamento", filtro.getDataLancamento());
		query.setParameter("status",StatusLancamento.EXPEDIDO);
		
		@SuppressWarnings("unchecked")
		List<Long> conts  = query.list();
		
		return (!conts.isEmpty())?conts.size():0L;
		
	}

	@Override
	public Long obterQuantidadeResumoExpedicaoPorBox(Long idBox,Date dataLancamento) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT COUNT(produtoEdicao.id)")
			.append( "FROM" )
			.append( " Box box")
			.append(" JOIN box.cotas cota")
			.append(" JOIN cota.estudoCotas estudoCota ")
			.append(" JOIN estudoCota.estudo estudo")
			.append(" JOIN estudo.produtoEdicao produtoEdicao")
			.append(" JOIN produtoEdicao.lancamentos lancamento ")
			.append(" JOIN lancamento.expedicao expedicao ")
			.append(" WHERE ")
			.append(" lancamento.dataLancamentoDistribuidor =:dataLancamento ")
			.append(" and lancamento.status =:status ")
			.append(" and box.id=:idBox ")
			.append(" and box.tipoBox =:tipoBox ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("dataLancamento", dataLancamento);
		query.setParameter("status",StatusLancamento.EXPEDIDO);
		query.setParameter("idBox",idBox);
		query.setParameter("tipoBox", TipoBox.LANCAMENTO);
		
		
		return (Long) query.uniqueResult();
	}

	@Override
	public Date obterUltimaExpedicaoDia(Date dataOperacao) {
		Criteria criteria = getSession().createCriteria(Expedicao.class);
		criteria.setProjection(Projections.max("dataExpedicao"));
		criteria.add(Restrictions.eq("dataExpedicao", dataOperacao));
		return (Date) criteria.uniqueResult();
	}

	@Override
	public Date obterDataUltimaExpedicao() {
		Criteria criteria = getSession().createCriteria(Expedicao.class);
		criteria.setProjection(Projections.max("dataExpedicao"));
		return (Date) criteria.uniqueResult();
	}

}

package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
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
						.append("(coalesce(produtoEd.descontoProdutoEdicao.valor, produtoEd.produto.descontoProduto.valor, 0)) as desconto,")
						.append("sum(estudoCota.qtdeEfetiva) as qtdeReparte,")
						.append(" SUM (( case ")
							.append(" when (diferenca.tipoDiferenca = 'FALTA_DE') then (-(diferenca.qtde * produtoEd.pacotePadrao))")
							.append(" when (diferenca.tipoDiferenca = 'SOBRA_DE') then (diferenca.qtde * produtoEd.pacotePadrao)")
							.append(" when (diferenca.tipoDiferenca = 'FALTA_EM') then (-diferenca.qtde)")
							.append(" when (diferenca.tipoDiferenca = 'SOBRA_EM') then (diferenca.qtde)")
							.append(" else 0")
						.append(" end )) as qntDiferenca, ")
						.append(" sum(estudoCota.qtdeEfetiva) * sum(produtoEd.precoVenda), ")
						.append(" juridica.razaoSocial ")
						
			.append(" ) ");
		}
		
		hql.append( "FROM" )
			.append(" Expedicao expedicao ")
			.append(" join expedicao.lancamentos lancamento ")
			.append(" join lancamento.estudo estudo ")
			.append(" join estudo.produtoEdicao produtoEd ")
			.append(" join produtoEd.produto produto ")
			.append(" join estudo.estudoCotas estudoCota ")
			.append(" join estudoCota.cota cota ")
			.append(" join cota.box box ")
			.append(" LEFT JOIN estudoCota.rateiosDiferenca rateioDiferenca ")
			.append(" LEFT JOIN rateioDiferenca.diferenca diferenca")
			.append(" JOIN produto.fornecedores fornecedor ")
			.append(" JOIN fornecedor.juridica juridica ")
			
			.append(" WHERE ")
			.append(" lancamento.dataLancamentoDistribuidor = :dataLancamento ")
			.append(" and lancamento.status = :status ")
			.append(" and box.tipoBox = :tipoBox ")
			.append(" and box.codigo = :codigoBox ");
		
		hql.append(" group by ")
			.append("produtoEd.id ");

		return hql.toString();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ExpedicaoDTO> obterResumoExpedicaoPorProduto(FiltroResumoExpedicaoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(getHqlResumoLancamentoPorBox())
					
		.append(getOrderBy(filtro));
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("dataLancamento", filtro.getDataLancamento());
		query.setParameter("status",StatusLancamento.EXPEDIDO);
		query.setParameter("tipoBox", TipoBox.LANCAMENTO);
		
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
	 * Retorna uma string com o conteudo da ordenação da consulta
	 * @param filtro
	 * @return
	 */
	private String getOrderBy(FiltroResumoExpedicaoDTO filtro ){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" ORDER BY box.id ");
		
		if (filtro.getOrdenacaoColunaProduto() != null ){
			
			switch (filtro.getOrdenacaoColunaProduto()) {
				
				case CODIGO_PRODUTO:
					hql.append(" , produto.codigo ");
					break;
				case DESCRICAO_PRODUTO:
					hql.append(" , produto.nome ");
					break;
				case NUMERO_EDICAO:
					hql.append(" , produtoEd.numeroEdicao ");
					break;
				case PRECO_CAPA:
					hql.append(" , produtoEd.precoVenda ");
					break;
				case REPARTE:
					hql.append(" , estudo.qtdeReparte ");
					break;
				case DIFERENCA:
					hql.append(" , ")
					.append(" sum( ( case ")
						.append(" when (diferenca.tipoDiferenca = 'FALTA_DE') then (-(diferenca.qtde * produtoEd.pacotePadrao))")
						.append(" when (diferenca.tipoDiferenca = 'SOBRA_DE') then (diferenca.qtde *  produtoEd.pacotePadrao)")
						.append(" when (diferenca.tipoDiferenca = 'FALTA_EM') then (-diferenca.qtde)")
						.append(" when (diferenca.tipoDiferenca = 'SOBRA_EM') then (diferenca.qtde)")
						.append(" else 0")
					.append(" end ) )");
					break;
				case VALOR_FATURADO:
					hql.append(" ,  produtoEd.precoVenda*estudo.qtdeReparte ");
					break;
				default:
					hql.append(" , produto.codigo ");
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
		ExpedicaoDTO dto = null;
		Long qntProduto = 0L;
		BigInteger reparte = BigInteger.ZERO;
		BigDecimal valorFaturado = BigDecimal.ZERO;
		BigInteger diferenca = BigInteger.ZERO;
		ExpedicaoDTO dtoBoxAux = null;
		
		while (results.next()){
			
			dto = (ExpedicaoDTO) results.get(0);
			
			if (dtoBoxAux != null && !dto.getIdBox().equals(dtoBoxAux.getIdBox())){
				dtoBoxAux.setQntProduto(qntProduto);
				dtoBoxAux.setQntReparte(reparte);
				dtoBoxAux.setValorFaturado(valorFaturado);
				dtoBoxAux.setQntDiferenca(diferenca);
				listRetorno.add(dtoBoxAux);
				qntProduto = 0L;
				reparte = BigInteger.ZERO;
				valorFaturado = BigDecimal.ZERO;
				diferenca = BigInteger.ZERO;
			}
			
			qntProduto += obterQuantidadeResumoExpedicaoPorBox(dto.getIdBox(), filtro.getDataLancamento());
			reparte = reparte.add(dto.getQntReparte());
			valorFaturado = valorFaturado.add(dto.getValorFaturado());
			diferenca = diferenca.add(dto.getQntDiferenca());
			
			dtoBoxAux = dto;
		}
		
		if (dtoBoxAux != null){
			
			dtoBoxAux.setQntProduto(qntProduto);
			dtoBoxAux.setQntReparte(reparte);
			dtoBoxAux.setValorFaturado(valorFaturado);
			dtoBoxAux.setQntDiferenca(diferenca);
			listRetorno.add(dtoBoxAux);
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
						.append(" box.id,")
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
						.append(" (SUM (estudoCota.qtdeEfetiva)*SUM (produtoEdicao.precoVenda )) as totalFaturado,")
						.append("produto.codigo,")
						.append("produto.nome,")
						.append("produtoEdicao.numeroEdicao")
			.append(" ) ")
			
			.append( "FROM" )
			
			.append(" Expedicao expedicao ")
			.append(" join expedicao.lancamentos lancamento ")
			.append(" join lancamento.estudo estudo ")
			.append(" join estudo.produtoEdicao produtoEdicao ")
			.append(" join produtoEdicao.produto produto ")
			.append(" join estudo.estudoCotas estudoCota ")
			.append(" join estudoCota.cota cota ")
			.append(" join cota.box box ")
			.append(" LEFT JOIN estudoCota.rateiosDiferenca rateioDiferenca ")
			.append(" LEFT JOIN rateioDiferenca.diferenca diferenca")
			
			.append(" WHERE ")
			.append(" lancamento.dataLancamentoDistribuidor =:dataLancamento ")
			.append(" and lancamento.status =:status ")
			.append(" and box.tipoBox =:tipoBox ")
			
			.append(" GROUP BY box.id, produtoEdicao.id ");
		
		return hql.toString();
	}

	@Override
	public Long obterQuantidadeResumoExpedicaoPorProduto(FiltroResumoExpedicaoDTO filtro) {
		
		Query query = getSession().createQuery(getHqlResumoLancamentoPorBox());
		
		query.setParameter("dataLancamento", filtro.getDataLancamento());
		query.setParameter("status",StatusLancamento.EXPEDIDO);
		query.setParameter("tipoBox", TipoBox.LANCAMENTO);
		
		@SuppressWarnings("unchecked")
		List<Long> conts  = query.list();
		
		return (!conts.isEmpty())?conts.size():0L;
		
	}

	@Override
	public Long obterQuantidadeResumoExpedicaoPorBox(Long idBox,Date dataLancamento) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT COUNT(distinct produtoEdicao.id)")
			.append( "FROM" )
			.append(" Expedicao expedicao ")
			.append(" join expedicao.lancamentos lancamento ")
			.append(" join lancamento.estudo estudo ")
			.append(" join estudo.produtoEdicao produtoEdicao ")
			.append(" join estudo.estudoCotas estudoCota ")
			.append(" join estudoCota.cota cota ")
			.append(" join cota.box box ")
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

package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ExtratoEdicaoDTO;
import br.com.abril.nds.dto.filtro.FiltroExtratoEdicaoDTO;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.FormaComercializacao;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.model.estoque.OperacaoEstoque;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.MovimentoEstoqueRepository;

@Repository
public class MovimentoEstoqueRepositoryImpl extends AbstractRepositoryModel<MovimentoEstoque, Long> 
implements MovimentoEstoqueRepository {
	
	/**
	 * Construtor padrão.
	 */
	public MovimentoEstoqueRepositoryImpl() {
		super(MovimentoEstoque.class);
	}
	
		
	/**
	 * Obtém uma lista de extratoEdicao de acordo com statusAprovacao.
	 */
	//@Override
	@SuppressWarnings("unchecked")
	public List<ExtratoEdicaoDTO> obterListaExtratoEdicao(FiltroExtratoEdicaoDTO filtro, StatusAprovacao statusAprovacao) {

		String codigoProduto = filtro.getCodigoProduto();
		Long numeroEdicao = filtro.getNumeroEdicao();
		
		StringBuilder hql = new StringBuilder("");
		
		hql.append(" select new " + ExtratoEdicaoDTO.class.getCanonicalName() );		
		
		hql.append(" ( m.id, m.data, m.tipoMovimento.descricao, ");		
		
		hql.append(" sum(case when m.tipoMovimento.operacaoEstoque  = :tipoOperacaoEntrada then m.qtde else 0 end), ");

		hql.append(" sum(case when m.tipoMovimento.operacaoEstoque  = :tipoOperacaoSaida then m.qtde else 0 end) )  ");

		hql.append(" from MovimentoEstoque m ");		

		hql.append(" where m.produtoEdicao.numeroEdicao = :numeroEdicao and ");		

		hql.append(" m.produtoEdicao.produto.codigo = :codigoProduto ");
		
		if (filtro != null && filtro.getGruposExcluidos() != null) {
			hql.append(" and m.tipoMovimento.grupoMovimentoEstoque not in (:gruposExcluidos) ");
		}

		if(statusAprovacao != null) {
			hql.append(" and m.status = :statusAprovacao  ");
		}
		
		hql.append(" group by m.produtoEdicao.id, m.data, m.tipoMovimento.id ");		
		
		hql.append(" order by ");
		
		hql.append(" case when m.origem = 'CARGA_INICIAL' then m.data else m.dataCriacao end asc, "); // Diferencial para registros inseridos via carga todos tem a mesma data de criação
		
		hql.append(" case when m.origem = 'CARGA_INICIAL' then m.tipoMovimento.operacaoEstoque end asc, " ); // Diferencial para registros inseridos via carga todos tem a mesma data de criação
		
		hql.append(" m.id ");
		
		Query query = getSession().createQuery(hql.toString());
		
		if(statusAprovacao != null) {
			query.setParameter("statusAprovacao", statusAprovacao);
		}
		
		query.setParameter("tipoOperacaoEntrada", OperacaoEstoque.ENTRADA);
		
		query.setParameter("tipoOperacaoSaida", OperacaoEstoque.SAIDA);

		query.setParameter("codigoProduto", codigoProduto);
		
		query.setParameter("numeroEdicao", numeroEdicao);
		
		if (filtro != null && filtro.getGruposExcluidos() != null) {

			query.setParameterList("gruposExcluidos", filtro.getGruposExcluidos());
		}
		
		if (filtro != null && filtro.getPaginacao() != null) {
			
			if (filtro.getPaginacao().getPosicaoInicial() != null) {
				query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			}
			
			if (filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
				query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
			}
		}
		
		return query.list();
	}

	@Override
	public BigInteger obterReparteDistribuidoProduto(String codigoProduto){
//		 select coalesce(sum(QTDE),0) from movimento_estoque where TIPO_MOVIMENTO_ID=13 and PRODUTO_EDICAO_ID=2350
		
		Query query = getSession().createQuery("select coalesce(sum(me.qtde),0) from MovimentoEstoque me " +
				" join me.tipoMovimento tm " +
				" join me.produtoEdicao " +
				" join me.produtoEdicao.produto produto " +
				"where produto.codigo = :produtoId " +
				"and tm.id = :tipoMovimentoId");
		
		query.setParameter("produtoId", codigoProduto);
		
		final long ENVIO_AO_JORNALEIRO = 13l; //13:Envio ao jornaleiro tabela tipo_movimento 
		query.setParameter("tipoMovimentoId", ENVIO_AO_JORNALEIRO);
		
		Object uniqueResult2 = query.uniqueResult();
		BigInteger uniqueResult = (BigInteger)uniqueResult2;
		return uniqueResult;
	}
	
	/**
	 * Obtem os Grupos de Movimento de Estoque de Consignado
	 * @return List<GrupoMovimentoEstoque>
	 */
	public List<GrupoMovimentoEstoque> obterListaGrupoMovimentoConsignado(){
		
		List<GrupoMovimentoEstoque> listaGrupoMovimentoEstoque = Arrays.asList(GrupoMovimentoEstoque.ENVIO_JORNALEIRO,
																			   GrupoMovimentoEstoque.ENVIO_JORNALEIRO_JURAMENTADO,
																			   GrupoMovimentoEstoque.RECEBIMENTO_ENCALHE,
																			   GrupoMovimentoEstoque.RECEBIMENTO_ENCALHE_JURAMENTADO,
																			   GrupoMovimentoEstoque.ESTORNO_REPARTE_FURO_PUBLICACAO,
																			   GrupoMovimentoEstoque.SUPLEMENTAR_COTA_AUSENTE,
																			   GrupoMovimentoEstoque.SUPLEMENTAR_ENVIO_ENCALHE_ANTERIOR_PROGRAMACAO,
																			   GrupoMovimentoEstoque.REPARTE_COTA_AUSENTE,
																			   GrupoMovimentoEstoque.VENDA_ENCALHE,
																			   GrupoMovimentoEstoque.VENDA_ENCALHE_SUPLEMENTAR,
																			   GrupoMovimentoEstoque.ESTORNO_VENDA_ENCALHE,
																			   GrupoMovimentoEstoque.ESTORNO_VENDA_ENCALHE_SUPLEMENTAR);
		
		return listaGrupoMovimentoEstoque;
	}

	/**
	 * Obtem valor total de Consignado ou AVista da data
	 * @param data
	 * @param operacaoEstoque
	 * @param formaComercializacao
	 * @return BigDecimal
	 */
	@Override
	public BigDecimal obterSaldoDistribuidor(Date data, OperacaoEstoque operacaoEstoque, FormaComercializacao formaComercializacao) {
		
		StringBuilder hql = new StringBuilder(" select ");
		
					  hql.append(" sum(me.qtde * pe.precoVenda) ");
					  
					  hql.append(" from MovimentoEstoque me ");
					  
					  hql.append(" join me.tipoMovimento tm ");
					  
					  hql.append(" join me.produtoEdicao pe ");
					  
					  hql.append(" join pe.produto p ");
					  
					  hql.append(" where me.data = :data ");
					  
					  hql.append(" and me.status = :statusAprovado ");
					  
					  hql.append(" and p.formaComercializacao = :formaComercializacao ");
					  
					  hql.append(" and tm.grupoMovimentoEstoque in (:grupoMovimentoEnvioJornaleiro) ");
	  
	    if (operacaoEstoque!=null){
	      
		    hql.append(" and tm.operacaoEstoque = :operacaoEstoque ");
	    }
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("data", data);
		
		query.setParameter("statusAprovado", StatusAprovacao.APROVADO);
		
		query.setParameter("formaComercializacao", formaComercializacao);
		
		query.setParameterList("grupoMovimentoEnvioJornaleiro", this.obterListaGrupoMovimentoConsignado());
		
		if (operacaoEstoque!=null){
		      
			query.setParameter("operacaoEstoque", operacaoEstoque);
	    }
		
		Object result = query.uniqueResult();
		
		return (result == null) ? BigDecimal.ZERO : (BigDecimal) result;
	}
}

package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ExtratoEdicaoDTO;
import br.com.abril.nds.dto.filtro.FiltroExtratoEdicaoDTO;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.FormaComercializacao;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
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
		
		hql.append(" ( m.id, m.tipoMovimento.id, m.data, m.tipoMovimento.descricao, ld.id, ");		
		
		hql.append(" sum(case when m.tipoMovimento.operacaoEstoque  = :tipoOperacaoEntrada then m.qtde else 0 end), ");

		hql.append(" sum(case when m.tipoMovimento.operacaoEstoque  = :tipoOperacaoSaida then m.qtde else 0 end) )  ");

		hql.append(" from MovimentoEstoque m ");
		
		hql.append(" left join m.lancamentoDiferenca ld ");
			
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
	 * @return List<String>
	 */
	public List<String> obterListaGrupoMovimentoConsignado(){
		
		List<String> listaGrupoMovimentoEstoque = Arrays.asList(GrupoMovimentoEstoque.ENVIO_JORNALEIRO.name(),
																GrupoMovimentoEstoque.ENVIO_JORNALEIRO_JURAMENTADO.name(),
																GrupoMovimentoEstoque.RECEBIMENTO_ENCALHE.name(),
																GrupoMovimentoEstoque.RECEBIMENTO_ENCALHE_JURAMENTADO.name(),
																GrupoMovimentoEstoque.SUPLEMENTAR_COTA_AUSENTE.name(),
																GrupoMovimentoEstoque.SUPLEMENTAR_ENVIO_ENCALHE_ANTERIOR_PROGRAMACAO.name(),
																GrupoMovimentoEstoque.REPARTE_COTA_AUSENTE.name(),
																GrupoMovimentoEstoque.VENDA_ENCALHE_SUPLEMENTAR.name(),
																GrupoMovimentoEstoque.ESTORNO_VENDA_ENCALHE.name(),
																GrupoMovimentoEstoque.ESTORNO_VENDA_ENCALHE_SUPLEMENTAR.name());
		
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
		
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("   SELECT sum(COALESCE(me.QTDE, 0)*pe.PRECO_VENDA)			");
		sql.append("   FROM MOVIMENTO_ESTOQUE me                        	");
		
		sql.append("   INNER JOIN TIPO_MOVIMENTO tipoMovimento 		");
		sql.append("   ON me.TIPO_MOVIMENTO_ID=tipoMovimento.ID    	");
		     
		sql.append("   INNER JOIN PRODUTO_EDICAO pe                	");
		sql.append("   ON me.PRODUTO_EDICAO_ID=pe.ID               	");
		     
		sql.append("   INNER JOIN PRODUTO prod                     	");
		sql.append("   ON pe.PRODUTO_ID=prod.ID                    	");
		     
		sql.append("   LEFT JOIN ( ");
		sql.append("		select produto_edicao_furo.id as idProdutoEdicaoFuro ");
		sql.append("		from FURO_PRODUTO fp 								");
		
		sql.append("		inner join produto_edicao produto_edicao_furo 		");
		sql.append("		on produto_edicao_furo.id = fp.produto_edicao_id 	");
		
		sql.append("		where fp.data_lcto_distribuidor = :data	");
		sql.append("   ) as produtosFuradosNaData     			");
		sql.append("   ON produtosFuradosNaData.idProdutoEdicaoFuro = me.PRODUTO_EDICAO_ID	");
		
		sql.append("   WHERE	");
		     
		sql.append("   me.DATA = :data ");
		sql.append("   AND me.STATUS = :statusAprovado	");
		sql.append("   AND prod.FORMA_COMERCIALIZACAO = :formaComercializacao ");
		sql.append("   AND ( ");
		sql.append("       tipoMovimento.GRUPO_MOVIMENTO_ESTOQUE in (:grupoMovimentoEnvioJornaleiro) ");
		sql.append("   ) ");
		sql.append("   AND produtosFuradosNaData.idProdutoEdicaoFuro is null       ");
		sql.append("   AND tipoMovimento.OPERACAO_ESTOQUE = :operacaoEstoque        ");
		
		Query query = this.getSession().createSQLQuery(sql.toString());
		
		query.setParameter("data", data);
		query.setParameter("statusAprovado", StatusAprovacao.APROVADO.name());
		query.setParameter("formaComercializacao", formaComercializacao.name());
		query.setParameterList("grupoMovimentoEnvioJornaleiro", this.obterListaGrupoMovimentoConsignado());
		query.setParameter("operacaoEstoque", operacaoEstoque.name());
		
		Object result = query.uniqueResult();
		
		return (result == null) ? BigDecimal.ZERO : (BigDecimal) result;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<MovimentoEstoque> obterMovimentoEstoquePorIdProdutoEdicao(ProdutoEdicao produtoEdicao) {
		
		
		StringBuilder hql = new StringBuilder("select "); 
		hql.append("movimentoEstoque.id as id, ");
		hql.append("movimentoEstoque.tipoMovimento as tipoMovimento, ");
		hql.append("movimentoEstoque.qtde as qtde ");
		hql.append("from ");
		hql.append("MovimentoEstoque as movimentoEstoque ");
		hql.append("where movimentoEstoque.produtoEdicao = :produtoEdicao");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("produtoEdicao", produtoEdicao);
		
		query.setResultTransformer(Transformers.aliasToBean(MovimentoEstoque.class));
		return query.list();
	}
}

package br.com.abril.nds.repository.impl;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.MovimentoAprovacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroControleAprovacaoDTO;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.movimentacao.Movimento;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.MovimentoRepository;

/**
 * Classe de implementação referente ao acesso a dados da entidade 
 * {@link br.com.abril.nds.model.movimentacao.Movimento}
 * 
 * @author Discover Technology
 */
@Repository
public class MovimentoRepositoryImpl extends AbstractRepositoryModel<Movimento, Long> 
												   implements MovimentoRepository {

	public MovimentoRepositoryImpl() {
		super(Movimento.class);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<MovimentoAprovacaoDTO> obterMovimentosAprovacao(FiltroControleAprovacaoDTO filtro) {

		String hql = this.gerarQueryMovimentosAprovacao(filtro, false);
		
		if (filtro != null && filtro.getOrdenacaoColuna() != null) {
			
			switch (filtro.getOrdenacaoColuna()) {
			
				case TIPO_MOVIMENTO:
					hql += "order by movimento.tipoMovimento.descricao ";
					break;
					
				case DATA_MOVIMENTO:
					hql += "order by movimento.dataCriacao ";
					break;
					
				case NUMERO_COTA:
					hql += "order by cota.numeroCota ";
					break;
				
				case NOME_COTA:
					hql += "order by (case when (pessoa.nome is not null) then ( pessoa.nome )"
						+  " when (pessoa.razaoSocial is not null) then ( pessoa.razaoSocial )"
						+  " else null end)";
					break;
					
				case VALOR:
					hql += "order by movimento.valor ";
					break;
					
				case PARCELAS:
					hql += "order by movimento.parcelas ";
					break;
					
				case PRAZO:
					hql += "order by movimento.prazo ";
					break;
					
				case REQUERENTE:
					hql += "order by movimento.usuario.nome ";
					break;
				
				case STATUS:
					hql += "order by movimento.status ";
					break;
					
				default:
					break;
			}
			
			if (filtro.getPaginacao().getOrdenacao() != null) {
				hql += filtro.getPaginacao().getOrdenacao().toString();
			}
		}
		
		Query query = getSession().createQuery(hql);
		
		aplicarParametrosParaPesquisaMovimentoAprovacao(filtro, query);
		
 		if (filtro != null && filtro.getPaginacao() != null) {
			
			if (filtro.getPaginacao().getPosicaoInicial() != null) {
				query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			}
			
			if (filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
				query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
			}
		}
		
		query.setResultTransformer(new AliasToBeanResultTransformer(MovimentoAprovacaoDTO.class));
		
		return query.list();
	}
	
	@Override
	public Long obterTotalMovimentosAprovacao(FiltroControleAprovacaoDTO filtro) {
		
		String hql = this.gerarQueryMovimentosAprovacao(filtro, true);
		
		Query query = getSession().createQuery(hql);
		
		aplicarParametrosParaPesquisaMovimentoAprovacao(filtro, query);
		
		return (Long) query.uniqueResult();
	}
	
	/**
	 * Aplica os parâmetros para a busca de movimentos para aprovação.
	 *   
	 * @param filtro - filtro da pesquisa
	 * @param query - objeto query
	 */
	private void aplicarParametrosParaPesquisaMovimentoAprovacao(FiltroControleAprovacaoDTO filtro, 
													 	 		 Query query) {
		
		query.setParameter("aprovacaoAutomatica", false);
		
		if (filtro == null) {
			
			return;
		}
		
		if (filtro.getIdTipoMovimento() != null) {
			query.setParameter("idTipoMovimento", filtro.getIdTipoMovimento());
		}
		
		if (filtro.getDataMovimento() != null) {
			query.setParameter("dataMovimento", filtro.getDataMovimento());
		}
		
		if (filtro.getStatusAprovacao() != null) {
			query.setParameter("statusAprovacao", filtro.getStatusAprovacao());
		}
	}
	
	/**
	 * Gera a query de movimentos para aprovação.
	 *   
	 * @param filtro - filtro da pesquisa
	 * @param totalizar - flag para contagem de total
	 * 
	 * @return Query
	 */
	private String gerarQueryMovimentosAprovacao(FiltroControleAprovacaoDTO filtro, 
												 boolean totalizar) {
		
		String hql;
		
		if (totalizar) {
			
			hql = "select count(movimento) ";
			
		} else {
			
			hql = " select "
				+ " movimento.id as idMovimento, "
				+ " movimento.tipoMovimento.descricao as descricaoTipoMovimento,"
				+ " movimento.dataCriacao as dataCriacao, "
				+ " cota.numeroCota as numeroCota,"
				+ " (case when (pessoa.nome is not null) then ( pessoa.nome )"
				+ " when (pessoa.razaoSocial is not null) then ( pessoa.razaoSocial )"
				+ " else null end) as nomeCota, "
				+ " movimento.valor as valor, "
				+ " movimento.parcelas as parcelas, "
				+ " movimento.prazo as prazo, "
				+ " movimento.usuario.nome as nomeUsuarioRequerente, "
				+ " movimento.status as statusMovimento, "
				+ " movimento.motivo as motivo ";
		}
		
		hql += " from Movimento movimento "
			 + " left join movimento.cota cota "
			 + " left join cota.pessoa pessoa ";
		
		hql += " where movimento.tipoMovimento.aprovacaoAutomatica = :aprovacaoAutomatica ";
		
		if (filtro != null) {
		
			if (filtro.getIdTipoMovimento() != null) {
				
				hql += " and movimento.tipoMovimento.id = :idTipoMovimento ";
			}
			
			if (filtro.getDataMovimento() != null) {
				
				hql += "  and movimento.dataCriacao = :dataMovimento ";
			}
			
			if (filtro.getStatusAprovacao() != null) {
				
				hql += "  and movimento.status = :statusAprovacao ";
				
			}
		}
		
		return hql;
	}
	
	public boolean existeMovimentoEstoquePendente(List<GrupoMovimentoEstoque> gruposMovimento) {
		
		String hql = " select count(movimento) "
			+ " from Movimento movimento "
			+ " where movimento.tipoMovimento.grupoMovimentoEstoque in (:gruposMovimento)"
			+ " and status = :status ";
		
		Query query = getSession().createQuery(hql);
		
		query.setParameterList("gruposMovimento", gruposMovimento);
		
		query.setParameter("status", StatusAprovacao.PENDENTE);
		
		long resultado = (Long) query.uniqueResult();
		
		return resultado > 0;
	}
	
	public boolean existeMovimentoFinanceiroPendente(List<GrupoMovimentoFinaceiro> gruposMovimento) {
		
		String hql = " select count(movimento) "
			+ " from Movimento movimento "
			+ " where movimento.tipoMovimento.grupoMovimentoFinaceiro in (:gruposMovimento)"
			+ " and status = :status ";
		
		Query query = getSession().createQuery(hql);
		
		query.setParameterList("gruposMovimento", gruposMovimento);
		
		query.setParameter("status", StatusAprovacao.PENDENTE);
		
		long resultado = (Long) query.uniqueResult();
		
		return resultado > 0;
	}
	
	@Override
	public Movimento buscarPorId(Long id) {
		 
		String hql = " select movimento from Movimento movimento where movimento.id=:id ";
			
		Query query = getSession().createQuery(hql);
			
		query.setParameter("id", id);
			
		return (Movimento) query.uniqueResult();
	}
	
	
	
	
	
	 @SuppressWarnings("unchecked")
		private List<MovimentoAprovacaoDTO> getMovimentosAprovacao(FiltroControleAprovacaoDTO filtro){
			
			String sql = "";

			sql+="select ";
			
			sql+=" movimento0_.ID as idMovimento, ";
			sql+=" movimento0_.DESCRICAO_TIPO_MOVIMENTO as descricaoTipoMovimento,";
			sql+=" movimento0_.DATA_CRIACAO as dataCriacao, ";
		    sql+=" (CASE WHEN cota1_.NUMERO_COTA IS NULL THEN NULL ELSE cota1_.NUMERO_COTA END) as numeroCota,";
			sql+=" (CASE WHEN pessoa2_.NOME IS NULL THEN (CASE WHEN pessoa2_.RAZAO_SOCIAL IS NULL THEN '' ELSE pessoa2_.RAZAO_SOCIAL END) ELSE pessoa2_.NOME END) as nomeCota, ";
			sql+=" movimento0_.VALOR as valor, ";
			sql+=" movimento0_.PARCELAS as parcelas, ";
			sql+=" movimento0_.PRAZO as prazo, ";
			sql+=" movimento0_.NOME_USUARIO as nomeUsuarioRequerente, ";
			sql+=" movimento0_.STATUS as statusMovimento, ";
			sql+=" movimento0_.MOTIVO as motivo ";

			sql+=this.gerarFromMovimentosAprovacao();
			
	        Query query = this.getSession().createSQLQuery(sql);
	        
	        query.setResultTransformer(Transformers.aliasToBean(MovimentoAprovacaoDTO.class));
	        
			return (List<MovimentoAprovacaoDTO>) query.list();   
		}
		
		private BigInteger getCountMovimentosAprovacao(FiltroControleAprovacaoDTO filtro){
			
			String sql = "";

			sql+="select ";
			
			sql+="count(movimento0_.ID) as col_0_0_ ";

			sql+=this.gerarFromMovimentosAprovacao();
			
	        Query query = this.getSession().createSQLQuery(sql);
			
			return (BigInteger) query.uniqueResult();        
		}
		
		private String gerarFromMovimentosAprovacao(){
			
			String sql = " ";

			sql+="from ";
			sql+="( select ";
			sql+="me.ID,";
			sql+="APROVADO_AUTOMATICAMENTE, ";
			sql+="DATA_APROVACAO, ";
			sql+="MOTIVO, ";
			sql+="STATUS, ";
			sql+="DATA, ";
			sql+="DATA_CRIACAO, ";
			sql+="DATA_INTEGRACAO, ";
			sql+="STATUS_INTEGRACAO, ";
			sql+="APROVADOR_ID, ";
			sql+="TIPO_MOVIMENTO_ID, ";
			sql+="USUARIO_ID, ";
			sql+="ORIGEM, ";
			sql+="QTDE, ";
			sql+="COD_ORIGEM_MOTIVO, ";
			sql+="DAT_EMISSAO_DOC_ACERTO, ";
			sql+="NUM_DOC_ACERTO, ";
			sql+="PRODUTO_EDICAO_ID, ";
			sql+="ESTOQUE_PRODUTO_ID, ";
			sql+="ITEM_REC_FISICO_ID, ";
			sql+="null as DATA_LANCAMENTO_ORIGINAL, ";
			sql+="null as STATUS_ESTOQUE_FINANCEIRO, ";
			sql+="null as PRECO_COM_DESCONTO, ";
			sql+="null as PRECO_VENDA, ";
			sql+="null as VALOR_DESCONTO, ";
			sql+="null as COTA_ID, ";
			sql+="null as ESTOQUE_PROD_COTA_ID, ";
			sql+="null as ESTOQUE_PROD_COTA_JURAMENTADO_ID, ";
			sql+="null as ESTUDO_COTA_ID, ";
			sql+="null as NOTA_ENVIO_ITEM_NOTA_ENVIO_ID, ";
			sql+="null as NOTA_ENVIO_ITEM_SEQUENCIA, ";
			sql+="null as LANCAMENTO_ID, ";
			sql+="null as MOVIMENTO_ESTOQUE_COTA_FURO_ID, ";
			sql+="null as MOVIMENTO_FINANCEIRO_COTA_ID, ";
			sql+="null as PARCELAS, ";
			sql+="null as PRAZO, ";
			sql+="null as VALOR, ";
			sql+="null as LANCAMENTO_MANUAL, ";
			sql+="null as OBSERVACAO, ";
			sql+="null as BAIXA_COBRANCA_ID, ";
			sql+="null as fornecedor_ID, ";
			
			sql+="(SELECT U.NOME FROM USUARIO U WHERE U.ID = me.USUARIO_ID) as NOME_USUARIO, ";
			sql+="(SELECT TM.DESCRICAO FROM TIPO_MOVIMENTO TM WHERE TM.ID = me.TIPO_MOVIMENTO_ID) as DESCRICAO_TIPO_MOVIMENTO ";
			
			sql+="from ";
			sql+="MOVIMENTO_ESTOQUE me join ";
			sql+="TIPO_MOVIMENTO tipomovime1_ ";
			sql+="where ";
			sql+="me.TIPO_MOVIMENTO_ID=tipomovime1_.ID ";
			sql+="and tipomovime1_.APROVACAO_AUTOMATICA=1 ";
			sql+="and me.DATA_CRIACAO='2013-03-27' ";
			
			sql+="union ";
			
			sql+="select ";
			sql+="mec.ID, ";
			sql+="APROVADO_AUTOMATICAMENTE, ";
			sql+="DATA_APROVACAO, ";
			sql+="MOTIVO, ";
			sql+="STATUS, ";
			sql+="DATA, ";
			sql+="DATA_CRIACAO, ";
			sql+="DATA_INTEGRACAO, ";
			sql+="STATUS_INTEGRACAO, ";
			sql+="APROVADOR_ID, ";
			sql+="TIPO_MOVIMENTO_ID, ";
			sql+="USUARIO_ID, ";
			sql+="ORIGEM, ";
			sql+="QTDE, ";
			sql+="null as COD_ORIGEM_MOTIVO, ";
			sql+="null as DAT_EMISSAO_DOC_ACERTO, ";
			sql+="null as NUM_DOC_ACERTO, ";
			sql+="PRODUTO_EDICAO_ID, ";
			sql+="null as ESTOQUE_PRODUTO_ID, ";
			sql+="null as ITEM_REC_FISICO_ID, ";
			sql+="DATA_LANCAMENTO_ORIGINAL, ";
			sql+="STATUS_ESTOQUE_FINANCEIRO, ";
			sql+="PRECO_COM_DESCONTO, ";
			sql+="PRECO_VENDA, ";
			sql+="VALOR_DESCONTO, ";
			sql+="COTA_ID, ";
			sql+="ESTOQUE_PROD_COTA_ID, ";
			sql+="ESTOQUE_PROD_COTA_JURAMENTADO_ID, ";
			sql+="ESTUDO_COTA_ID, ";
			sql+="NOTA_ENVIO_ITEM_NOTA_ENVIO_ID, ";
			sql+="NOTA_ENVIO_ITEM_SEQUENCIA, ";
			sql+="LANCAMENTO_ID, ";
			sql+="MOVIMENTO_ESTOQUE_COTA_FURO_ID, ";
			sql+="MOVIMENTO_FINANCEIRO_COTA_ID, ";
			sql+="null as PARCELAS, ";
			sql+="null as PRAZO, ";
			sql+="null as VALOR, ";
			sql+="null as LANCAMENTO_MANUAL, ";
			sql+="null as OBSERVACAO, ";
			sql+="null as BAIXA_COBRANCA_ID, ";
			sql+="null as fornecedor_ID, ";
			
			sql+="(SELECT U.NOME FROM USUARIO U WHERE U.ID = mec.USUARIO_ID) as NOME_USUARIO, ";
			sql+="(SELECT TM.DESCRICAO FROM TIPO_MOVIMENTO TM WHERE TM.ID = mec.TIPO_MOVIMENTO_ID) as DESCRICAO_TIPO_MOVIMENTO ";
			
			sql+="from ";
			sql+="MOVIMENTO_ESTOQUE_COTA mec join ";
			sql+="TIPO_MOVIMENTO tipomovime2_ ";
			sql+="where ";
			sql+="mec.TIPO_MOVIMENTO_ID=tipomovime2_.ID ";
			sql+="and tipomovime2_.APROVACAO_AUTOMATICA=1 ";
			sql+="and mec.DATA_CRIACAO='2013-03-27' ";
			
			sql+="union ";
			
			sql+="select ";
			sql+="mfc.ID, ";
			sql+="APROVADO_AUTOMATICAMENTE, ";
			sql+="DATA_APROVACAO, ";
			sql+="MOTIVO, ";
			sql+="STATUS, ";
			sql+="DATA, ";
			sql+="DATA_CRIACAO, ";
			sql+="DATA_INTEGRACAO, ";
			sql+="STATUS_INTEGRACAO, ";
			sql+="APROVADOR_ID, ";
			sql+="TIPO_MOVIMENTO_ID, ";
			sql+="USUARIO_ID, ";
			sql+="null as ORIGEM, ";
			sql+="null as QTDE, ";
			sql+="null as COD_ORIGEM_MOTIVO, ";
			sql+="null as DAT_EMISSAO_DOC_ACERTO, ";
			sql+="null as NUM_DOC_ACERTO, ";
			sql+="null as PRODUTO_EDICAO_ID, ";
			sql+="null as ESTOQUE_PRODUTO_ID, ";
			sql+="null as ITEM_REC_FISICO_ID, ";
			sql+="null as DATA_LANCAMENTO_ORIGINAL, ";
			sql+="null as STATUS_ESTOQUE_FINANCEIRO, ";
			sql+="null as PRECO_COM_DESCONTO, ";
			sql+="null as PRECO_VENDA, ";
			sql+="null as VALOR_DESCONTO, ";
			sql+="COTA_ID, ";
			sql+="null as ESTOQUE_PROD_COTA_ID, ";
			sql+="null as ESTOQUE_PROD_COTA_JURAMENTADO_ID, ";
			sql+="null as ESTUDO_COTA_ID, ";
			sql+="null as NOTA_ENVIO_ITEM_NOTA_ENVIO_ID, ";
			sql+="null as NOTA_ENVIO_ITEM_SEQUENCIA, ";
			sql+="null as LANCAMENTO_ID, ";
			sql+="null as MOVIMENTO_ESTOQUE_COTA_FURO_ID, ";
			sql+="null as MOVIMENTO_FINANCEIRO_COTA_ID, ";
			sql+="PARCELAS, ";
			sql+="PRAZO, ";
			sql+="VALOR, ";
			sql+="LANCAMENTO_MANUAL, ";
			sql+="OBSERVACAO, ";
			sql+="BAIXA_COBRANCA_ID, ";
			sql+="fornecedor_ID, ";
			
			sql+="(SELECT U.NOME FROM USUARIO U WHERE U.ID = mfc.USUARIO_ID) as NOME_USUARIO, ";
			sql+="(SELECT TM.DESCRICAO FROM TIPO_MOVIMENTO TM WHERE TM.ID = mfc.TIPO_MOVIMENTO_ID) as DESCRICAO_TIPO_MOVIMENTO ";
			
			sql+="from ";
			sql+="MOVIMENTO_FINANCEIRO_COTA mfc ";
			sql+="join ";
			sql+="TIPO_MOVIMENTO tipomovime3_ ";
			sql+="where ";
			sql+="mfc.TIPO_MOVIMENTO_ID=tipomovime3_.ID ";
			sql+="and tipomovime3_.APROVACAO_AUTOMATICA=1 ";
			sql+="and mfc.DATA_CRIACAO='2013-03-27' ";
			sql+=") movimento0_ ";
			
			sql+="left outer join ";
			sql+="COTA cota1_ ";
			sql+="on movimento0_.COTA_ID=cota1_.ID ";
			sql+="left outer join ";
			sql+="PESSOA pessoa2_ ";
			sql+="on cota1_.PESSOA_ID=pessoa2_.ID ";
			
			return sql;
		}
	
	
}

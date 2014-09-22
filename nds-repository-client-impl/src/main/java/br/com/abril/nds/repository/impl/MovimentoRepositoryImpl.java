package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
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

		String hql = this.gerarQueryMovimentosAprovacao(filtro);
		
		if (filtro != null && filtro.getOrdenacaoColuna() != null) {
			
			switch (filtro.getOrdenacaoColuna()) {
			
				case TIPO_MOVIMENTO:
					hql += "order by descricaoTipoMovimento ";
					break;
					
				case DATA_MOVIMENTO:
					hql += "order by dataCriacao ";
					break;
					
				case NUMERO_COTA:
					hql += "order by numeroCota ";
					break;
				
				case NOME_COTA:
					hql += "order by nomeCota ";
					break;
					
				case VALOR:
					hql += "order by valor ";
					break;
					
				case PARCELAS:
					hql += "order by parcelas ";
					break;
					
				case PRAZO:
					hql += "order by prazo ";
					break;
					
				case REQUERENTE:
					hql += "order by nomeUsuarioRequerente ";
					break;
				
				case STATUS:
					hql += "order by statusMovimento ";
					break;
					
				default:
					break;
			}
			
			if (filtro.getPaginacao().getOrdenacao() != null) {
				hql += filtro.getPaginacao().getOrdenacao().toString();
			}
		}
		
		SQLQuery query = getSession().createSQLQuery(hql);
		
		query.addScalar("idMovimento", StandardBasicTypes.LONG);
		query.addScalar("descricaoTipoMovimento");
		query.addScalar("dataCriacao");
		query.addScalar("numeroCota");
		query.addScalar("nomeCota");
		query.addScalar("valor");
		query.addScalar("parcelas");
		query.addScalar("prazo");
		query.addScalar("nomeUsuarioRequerente");
		query.addScalar("statusMovimento");
		query.addScalar("motivo");
		
		this.aplicarParametrosParaPesquisaMovimentoAprovacao(filtro, query);
		
 		if (filtro != null && filtro.getPaginacao() != null) {
			
			if (filtro.getPaginacao().getPosicaoInicial() != null) {
				query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			}
			
			if (filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
				query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
			}
		}
 		
		query.setResultTransformer(Transformers.aliasToBean(MovimentoAprovacaoDTO.class));
		
		return query.list();
	}
	
	@Override
	public Long obterTotalMovimentosAprovacao(FiltroControleAprovacaoDTO filtro) {
		
		String hql = this.gerarQueryTotalMovimentosAprovacao(filtro);
		
		SQLQuery query = getSession().createSQLQuery(hql);
		
		query.addScalar("totalMovimentos", StandardBasicTypes.LONG);
		
		this.aplicarParametrosParaPesquisaMovimentoAprovacao(filtro, query);
		
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
			query.setParameter("statusAprovacao", filtro.getStatusAprovacao().name());
		}
	}
	
	public boolean existeMovimentoEstoquePendente(List<GrupoMovimentoEstoque> gruposMovimento) {
		
		String hql = " select count(movimentoEstoque) "
			+ " from MovimentoEstoque movimentoEstoque "
			+ " where tipoMovimento.grupoMovimentoEstoque in (:gruposMovimento) "
			+ " and status = :status ";
		
		Query query = getSession().createQuery(hql);
		
		query.setParameterList("gruposMovimento", gruposMovimento);
		
		query.setParameter("status", StatusAprovacao.PENDENTE);
		
		long resultado = (Long) query.uniqueResult();
		
		return resultado > 0;
	}
	
	public boolean existeMovimentoFinanceiroPendente(List<GrupoMovimentoFinaceiro> gruposMovimento) {
		
		String hql = " select count(movimentoFinanceiroCota) "
			+ " from MovimentoFinanceiroCota movimentoFinanceiroCota "
			+ " where tipoMovimento.grupoMovimentoFinaceiro in (:gruposMovimento)"
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
	
	private String gerarQueryMovimentosAprovacao(FiltroControleAprovacaoDTO filtro){
		
		StringBuilder sql = new StringBuilder();

		sql.append("select ");
		
		sql.append(" movimentoAprovacao.ID as idMovimento, ");
		sql.append(" movimentoAprovacao.DESCRICAO as descricaoTipoMovimento, ");
		sql.append(" movimentoAprovacao.DATA_CRIACAO as dataCriacao, ");
	    sql.append(" cota.NUMERO_COTA as numeroCota, ");
	    sql.append(" coalesce(pessoa.NOME, pessoa.RAZAO_SOCIAL, '') as nomeCota, ");
		sql.append(" movimentoAprovacao.VALOR as valor, ");
		sql.append(" movimentoAprovacao.PARCELAS as parcelas, ");
		sql.append(" movimentoAprovacao.PRAZO as prazo, ");
		sql.append(" movimentoAprovacao.NOME as nomeUsuarioRequerente, ");
		sql.append(" movimentoAprovacao.STATUS as statusMovimento, ");
		sql.append(" movimentoAprovacao.MOTIVO as motivo ");

		sql.append(this.gerarFromMovimentosAprovacao(filtro));
		
		return sql.toString();
	}
		
	private String gerarQueryTotalMovimentosAprovacao(FiltroControleAprovacaoDTO filtro){
		
		StringBuilder sql = new StringBuilder();

		sql.append(" select ");
		
		sql.append(" count(movimentoAprovacao.ID) as totalMovimentos ");

		sql.append(this.gerarFromMovimentosAprovacao(filtro));
		
		return sql.toString();
	}
		
	private String gerarFromMovimentosAprovacao(FiltroControleAprovacaoDTO filtro){
		
		StringBuilder sql = new StringBuilder();

		sql.append(" from ");
		sql.append(" ( ");
		
		sql.append(this.getQueryMovimentoEstoque(filtro));
		
		sql.append(" union ");
		
		sql.append(this.getQuerymMovimentoEstoqueCota(filtro));
		
		sql.append(" union ");
		
		sql.append(this.getQueryMovimentoFinanceiroCota(filtro));
		
		sql.append(" ) movimentoAprovacao ");
		
		sql.append(" left outer join ");
		sql.append(" COTA cota ");
		sql.append(" on movimentoAprovacao.COTA_ID = cota.ID ");
		sql.append(" left outer join ");
		sql.append(" PESSOA pessoa ");
		sql.append(" on cota.PESSOA_ID = pessoa.ID ");
		
		return sql.toString();
	}

	private String getQueryMovimentoEstoque(FiltroControleAprovacaoDTO filtro) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select ");
		sql.append(" movimento.ID, ");
		sql.append(" APROVADO_AUTOMATICAMENTE, ");
		sql.append(" DATA_APROVACAO, ");
		sql.append(" MOTIVO, ");
		sql.append(" STATUS, ");
		sql.append(" DATA, ");
		sql.append(" DATA_CRIACAO, ");
		sql.append(" DATA_INTEGRACAO, ");
		sql.append(" STATUS_INTEGRACAO, ");
		sql.append(" APROVADOR_ID, ");
		sql.append(" TIPO_MOVIMENTO_ID, ");
		sql.append(" USUARIO_ID, ");
		sql.append(" ORIGEM, ");
		sql.append(" QTDE, ");
		sql.append(" COD_ORIGEM_MOTIVO, ");
		sql.append(" DAT_EMISSAO_DOC_ACERTO, ");
		sql.append(" NUM_DOC_ACERTO, ");
		sql.append(" PRODUTO_EDICAO_ID, ");
		sql.append(" ESTOQUE_PRODUTO_ID, ");
		sql.append(" ITEM_REC_FISICO_ID, ");
		sql.append(" null as DATA_LANCAMENTO_ORIGINAL, ");
		sql.append(" null as STATUS_ESTOQUE_FINANCEIRO, ");
		sql.append(" null as PRECO_COM_DESCONTO, ");
		sql.append(" null as PRECO_VENDA, ");
		sql.append(" null as VALOR_DESCONTO, ");
		sql.append(" null as COTA_ID, ");
		sql.append(" null as ESTOQUE_PROD_COTA_ID, ");
		sql.append(" null as ESTUDO_COTA_ID, ");
		sql.append(" null as NOTA_ENVIO_ITEM_NOTA_ENVIO_ID, ");
		sql.append(" null as NOTA_ENVIO_ITEM_SEQUENCIA, ");
		sql.append(" null as LANCAMENTO_ID, ");
		sql.append(" null as MOVIMENTO_ESTOQUE_COTA_FURO_ID, ");
		sql.append(" null as MOVIMENTO_FINANCEIRO_COTA_ID, ");
		sql.append(" null as PARCELAS, ");
		sql.append(" null as PRAZO, ");
		sql.append(" null as VALOR, ");
		sql.append(" null as LANCAMENTO_MANUAL, ");
		sql.append(" null as OBSERVACAO, ");
		sql.append(" null as BAIXA_COBRANCA_ID, ");
		sql.append(" null as fornecedor_ID, ");
		sql.append(" tipoMovimento.APROVACAO_AUTOMATICA, ");
		sql.append(" tipoMovimento.DESCRICAO, ");
		sql.append(" usuario.NOME ");

		sql.append(" from ");
		sql.append("     MOVIMENTO_ESTOQUE movimento ");
		sql.append(" join ");
		sql.append("     TIPO_MOVIMENTO tipoMovimento ");
		sql.append("     	on tipoMovimento.ID = movimento.TIPO_MOVIMENTO_ID ");
		sql.append(" join ");
		sql.append(" 		USUARIO usuario ");
		sql.append(" 			on usuario.ID = movimento.USUARIO_ID ");
		
		sql.append(" where ");
		sql.append(" movimento.TIPO_MOVIMENTO_ID = tipoMovimento.ID ");
		
		sql.append(this.aplicarFiltro(filtro));
		
		return sql.toString();
	}

	private String getQuerymMovimentoEstoqueCota(FiltroControleAprovacaoDTO filtro) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select ");
		sql.append(" movimento.ID, ");
		sql.append(" APROVADO_AUTOMATICAMENTE, ");
		sql.append(" DATA_APROVACAO, ");
		sql.append(" MOTIVO, ");
		sql.append(" STATUS, ");
		sql.append(" DATA, ");
		sql.append(" DATA_CRIACAO, ");
		sql.append(" DATA_INTEGRACAO, ");
		sql.append(" STATUS_INTEGRACAO, ");
		sql.append(" APROVADOR_ID, ");
		sql.append(" TIPO_MOVIMENTO_ID, ");
		sql.append(" USUARIO_ID, ");
		sql.append(" ORIGEM, ");
		sql.append(" QTDE, ");
		sql.append(" null as COD_ORIGEM_MOTIVO, ");
		sql.append(" null as DAT_EMISSAO_DOC_ACERTO, ");
		sql.append(" null as NUM_DOC_ACERTO, ");
		sql.append(" PRODUTO_EDICAO_ID, ");
		sql.append(" null as ESTOQUE_PRODUTO_ID, ");
		sql.append(" null as ITEM_REC_FISICO_ID, ");
		sql.append(" DATA_LANCAMENTO_ORIGINAL, ");
		sql.append(" STATUS_ESTOQUE_FINANCEIRO, ");
		sql.append(" PRECO_COM_DESCONTO, ");
		sql.append(" PRECO_VENDA, ");
		sql.append(" VALOR_DESCONTO, ");
		sql.append(" COTA_ID, ");
		sql.append(" ESTOQUE_PROD_COTA_ID, ");
		sql.append(" ESTUDO_COTA_ID, ");
		sql.append(" NOTA_ENVIO_ITEM_NOTA_ENVIO_ID, ");
		sql.append(" NOTA_ENVIO_ITEM_SEQUENCIA, ");
		sql.append(" LANCAMENTO_ID, ");
		sql.append(" MOVIMENTO_ESTOQUE_COTA_FURO_ID, ");
		sql.append(" MOVIMENTO_FINANCEIRO_COTA_ID, ");
		sql.append(" null as PARCELAS, ");
		sql.append(" null as PRAZO, ");
		sql.append(" null as VALOR, ");
		sql.append(" null as LANCAMENTO_MANUAL, ");
		sql.append(" null as OBSERVACAO, ");
		sql.append(" null as BAIXA_COBRANCA_ID, ");
		sql.append(" null as fornecedor_ID, ");
		sql.append(" tipoMovimento.APROVACAO_AUTOMATICA, ");
		sql.append(" tipoMovimento.DESCRICAO, ");
		
		sql.append(" usuario.NOME ");
		sql.append(" from ");
		sql.append("     MOVIMENTO_ESTOQUE_COTA movimento "); 
		sql.append(" join ");
		sql.append("     TIPO_MOVIMENTO tipoMovimento ");
		sql.append("     	on tipoMovimento.ID = movimento.TIPO_MOVIMENTO_ID ");
		sql.append(" join ");
		sql.append(" 		USUARIO usuario ");
		sql.append(" 			on usuario.ID = movimento.USUARIO_ID ");
		
		sql.append(" where ");
		sql.append(" movimento.TIPO_MOVIMENTO_ID = tipoMovimento.ID ");
		
		sql.append(this.aplicarFiltro(filtro));
		
		return sql.toString();
	}
	
	private String getQueryMovimentoFinanceiroCota(FiltroControleAprovacaoDTO filtro) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select ");
		sql.append(" movimento.ID, ");
		sql.append(" APROVADO_AUTOMATICAMENTE, ");
		sql.append(" DATA_APROVACAO, ");
		sql.append(" MOTIVO, ");
		sql.append(" STATUS, ");
		sql.append(" DATA, ");
		sql.append(" DATA_CRIACAO, ");
		sql.append(" DATA_INTEGRACAO, ");
		sql.append(" STATUS_INTEGRACAO, ");
		sql.append(" APROVADOR_ID, ");
		sql.append(" TIPO_MOVIMENTO_ID, ");
		sql.append(" USUARIO_ID, ");
		sql.append(" null as ORIGEM, ");
		sql.append(" null as QTDE, ");
		sql.append(" null as COD_ORIGEM_MOTIVO, ");
		sql.append(" null as DAT_EMISSAO_DOC_ACERTO, ");
		sql.append(" null as NUM_DOC_ACERTO, ");
		sql.append(" null as PRODUTO_EDICAO_ID, ");
		sql.append(" null as ESTOQUE_PRODUTO_ID, ");
		sql.append(" null as ITEM_REC_FISICO_ID, ");
		sql.append(" null as DATA_LANCAMENTO_ORIGINAL, ");
		sql.append(" null as STATUS_ESTOQUE_FINANCEIRO, ");
		sql.append(" null as PRECO_COM_DESCONTO, ");
		sql.append(" null as PRECO_VENDA, ");
		sql.append(" null as VALOR_DESCONTO, ");
		sql.append(" COTA_ID, ");
		sql.append(" null as ESTOQUE_PROD_COTA_ID, ");
		sql.append(" null as ESTUDO_COTA_ID, ");
		sql.append(" null as NOTA_ENVIO_ITEM_NOTA_ENVIO_ID, ");
		sql.append(" null as NOTA_ENVIO_ITEM_SEQUENCIA, ");
		sql.append(" null as LANCAMENTO_ID, ");
		sql.append(" null as MOVIMENTO_ESTOQUE_COTA_FURO_ID, ");
		sql.append(" null as MOVIMENTO_FINANCEIRO_COTA_ID, ");
		sql.append(" PARCELAS, ");
		sql.append(" PRAZO, ");
		sql.append(" VALOR, ");
		sql.append(" LANCAMENTO_MANUAL, ");
		sql.append(" OBSERVACAO, ");
		sql.append(" BAIXA_COBRANCA_ID, ");
		sql.append(" fornecedor_ID, ");
		sql.append(" tipoMovimento.APROVACAO_AUTOMATICA, ");
		sql.append(" tipoMovimento.DESCRICAO, ");
		
		sql.append(" usuario.NOME ");
		sql.append(" from ");
		sql.append("     MOVIMENTO_FINANCEIRO_COTA movimento "); 
		sql.append(" join ");
		sql.append("     TIPO_MOVIMENTO tipoMovimento ");
		sql.append("     	on tipoMovimento.ID = movimento.TIPO_MOVIMENTO_ID ");
		sql.append(" join ");
		sql.append(" 		USUARIO usuario ");
		sql.append(" 			on usuario.ID = movimento.USUARIO_ID ");
		
		sql.append(" where ");
		sql.append(" movimento.TIPO_MOVIMENTO_ID = tipoMovimento.ID ");
		
		sql.append(this.aplicarFiltro(filtro));
		
		return sql.toString();
	}
	
	private String aplicarFiltro(FiltroControleAprovacaoDTO filtro) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" and tipoMovimento.APROVACAO_AUTOMATICA = :aprovacaoAutomatica ");
		
		if (filtro != null) {
			
			if (filtro.getIdTipoMovimento() != null) {
				
				sql.append(" and tipoMovimento.ID = :idTipoMovimento ");
			}
			
			if (filtro.getDataMovimento() != null) {
				
				sql.append(" and movimento.DATA_CRIACAO = :dataMovimento ");
			}
			
			if (filtro.getStatusAprovacao() != null) {
				
				sql.append(" and movimento.STATUS = :statusAprovacao ");
				
			}
		}
		
		return sql.toString();
	}
	
}

package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.NotaEnvioProdutoEdicao;
import br.com.abril.nds.model.cadastro.FormaComercializacao;
import br.com.abril.nds.model.envio.nota.NotaEnvio;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.NotaEnvioRepository;

@Repository
public class NotaEnvioRepositoryImpl  extends AbstractRepositoryModel<NotaEnvio, Long> implements NotaEnvioRepository{

	public NotaEnvioRepositoryImpl() {
		super(NotaEnvio.class);
	}

	@Override
	public boolean verificarExistenciaEmissaoDeNotasPeloEstudo(Long idEstudo) {
		
		StringBuilder hql = new StringBuilder();
		hql.append(" select count(*) from EstudoCota estudoCota")
		   .append(" where estudoCota.estudo.id = :idEstudo") 
		   .append(" and   estudoCota.itemNotaEnvio = null");
		
		Query query = this.getSession().createQuery(hql.toString());

		query.setParameter("idEstudo", idEstudo);
		
		
		Integer count = (Integer)query.uniqueResult();
		
		return count == 0;
	}
	
	@Override
	public Date obterMenorDataLancamentoPorNotaEnvio(Long numeroNotaEnvio) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select min(lancamento.dataLancamentoDistribuidor) ");
		hql.append(" from NotaEnvio notaEnvio ");
		hql.append(" join notaEnvio.listaItemNotaEnvio itemNotaEnvio ");
		hql.append(" join itemNotaEnvio.estudoCota estudoCota ");
		hql.append(" join estudoCota.estudo estudo ");
		hql.append(" join estudo.lancamentos lancamento ");
		hql.append(" where notaEnvio.id = :numeroNotaEnvio ");
		
		Query query = this.getSession().createQuery(hql.toString());

		query.setCacheable(true);
		query.setParameter("numeroNotaEnvio", numeroNotaEnvio);
		
		return (Date) query.uniqueResult();
	}
	
	
	@SuppressWarnings("unchecked")
	public List<NotaEnvioProdutoEdicao> obterEmissoesAlemDoConsignado(Long idCota, List<Long> idsProdutoEdicao, Date dataRecolhimentoCE, Date dataOperacao){
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select ");
		
		sql.append(" notaEnvio.numero as numeroNotaEnvio, ");
		
		sql.append(" notaEnvio.dataEmissao as dataEmissao,  ");
		
		sql.append(" produtoEdicao.ID as idProdutoEdicao,  ");
		
		sql.append(" itemNota.REPARTE as reparte,  ");
		
		sql.append(" m.DATA as dataConsignacao ");
		
		sql.append(" from NOTA_ENVIO_ITEM itemNota  ");
		
		sql.append(" inner join	NOTA_ENVIO notaEnvio on itemNota.NOTA_ENVIO_ID=notaEnvio.numero  ");
		sql.append(" inner join	PRODUTO_EDICAO produtoEdicao on itemNota.PRODUTO_EDICAO_ID=produtoEdicao.ID  ");
		sql.append(" inner join ESTUDO_COTA ec ON  itemNota.ESTUDO_COTA_ID = ec.ID ");
		sql.append(" inner join COTA c ON EC.COTA_ID = c.ID AND notaEnvio.NUMERO_COTA = c.NUMERO_COTA ");
		sql.append(" inner join movimento_estoque_cota m on produtoEdicao.ID = m.PRODUTO_EDICAO_ID  ");
		sql.append(" 									 and ec.ID = m.ESTUDO_COTA_ID ");
		sql.append(" 									 AND m.COTA_ID = c.ID ");
		sql.append(" inner join lancamento l ON m.LANCAMENTO_ID = l.ID ");
		
		if (idCota != null) {
		    
			sql.append(" where c.ID=:idCota ");
		}
		
		if (idsProdutoEdicao != null && idsProdutoEdicao.size() > 0) {
		    
			sql.append(" and produtoEdicao.ID in (:idsProdutoEdicao) ");
		}
		
		if (dataRecolhimentoCE != null){
		    
			sql.append(" and l.DATA_REC_DISTRIB <> :dataRecolhimentoCE ");
		}
		
		sql.append(" group by ");
		sql.append(" 	notaEnvio.numero, ");
		sql.append(" 	itemNota.NOTA_ENVIO_ID, ");
		sql.append(" 	itemNota.SEQUENCIA, ");
		sql.append(" 	produtoEdicao.ID ");
		
		
		sql.append(" UNION ALL ");
		
		
		sql.append(" select 0 as numeroNotaEnvio, ");
		
		sql.append(" null as dataEmissao, ");
		
		sql.append(" v.ID_PRODUTO_EDICAO as idProdutoEdicao, ");
		
		sql.append(" v.QNT_PRODUTO as reparte, ");
		
		sql.append(" v.DATA_OPERACAO as dataConsignacao  ");
		
		sql.append(" from venda_produto v ");
		
		if (idCota != null){
		
		    sql.append(" where v.ID_COTA = :idCota ");
		}
		
		if (idsProdutoEdicao != null && idsProdutoEdicao.size() > 0){
		    
			sql.append(" and v.ID_PRODUTO_EDICAO in (:idsProdutoEdicao) ");
		}
		
		if (dataOperacao != null){
		
			sql.append(" and v.DATA_OPERACAO <= :dataOperacao ");
		}
		
		sql.append(" and v.TIPO_COMERCIALIZACAO_VENDA = :tipoComercializacao ");
		
		Query query = this.getSession().createSQLQuery(sql.toString())
				.addScalar("numeroNotaEnvio", StandardBasicTypes.BIG_INTEGER)
				.addScalar("dataEmissao", StandardBasicTypes.DATE)
				.addScalar("dataConsignacao", StandardBasicTypes.DATE)
				.addScalar("idProdutoEdicao", StandardBasicTypes.LONG)
				.addScalar("reparte", StandardBasicTypes.BIG_INTEGER);
				
		if (idCota != null) {
			
		    query.setParameter("idCota", idCota);
		}
		
		if (idsProdutoEdicao != null && idsProdutoEdicao.size() > 0) {
		    
			query.setParameterList("idsProdutoEdicao", idsProdutoEdicao);
		}
		
		if (dataRecolhimentoCE != null) {
			
		    query.setParameter("dataRecolhimentoCE", dataRecolhimentoCE);
		}
		
		if (dataOperacao != null) {
		
			query.setParameter("dataOperacao", dataOperacao);
		}
		
		query.setParameter("tipoComercializacao", FormaComercializacao.CONSIGNADO.name());
		
		query.setResultTransformer(new AliasToBeanResultTransformer(NotaEnvioProdutoEdicao.class));
		
		return query.list();
	}
	
}

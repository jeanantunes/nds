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
	
	
	public List<NotaEnvioProdutoEdicao> obterEmissoesAlemDoConsignado(Long idCota, List<Long> idsProdutoEdicao, Date dataRecolhimentoCE, Date dataOperacao){
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select notaEnvio.numero as numeroNotaEnvio, notaEnvio.dataEmissao as dataEmissao,  ");
		sql.append(" produtoEdicao.ID as idProdutoEdicao, itemNota.REPARTE as reparte, m.DATA as dataConsignacao ");
		sql.append(" from NOTA_ENVIO_ITEM itemNota  ");
		sql.append(" inner join	NOTA_ENVIO notaEnvio on itemNota.NOTA_ENVIO_ID=notaEnvio.numero  ");
		sql.append(" inner join	PRODUTO_EDICAO produtoEdicao on itemNota.PRODUTO_EDICAO_ID=produtoEdicao.ID  ");
		sql.append(" inner join  ESTUDO_COTA ec ON  itemNota.ESTUDO_COTA_ID = ec.ID ");
		sql.append(" inner join  COTA c ON EC.COTA_ID = c.ID AND notaEnvio.NUMERO_COTA = c.NUMERO_COTA ");
		sql.append(" inner join  movimento_estoque_cota m on produtoEdicao.ID = m.PRODUTO_EDICAO_ID  ");
		sql.append(" 									 and ec.ID = m.ESTUDO_COTA_ID ");
		sql.append(" 									 AND m.COTA_ID = c.ID ");
		sql.append(" inner join  lancamento l ON m.LANCAMENTO_ID = l.ID ");
		sql.append(" where ");
		sql.append(" 	c.ID=:idCota ");
		sql.append(" 	and produtoEdicao.ID in (:idsProdutoEdicao)  ");
		sql.append(" 	AND l.DATA_REC_DISTRIB <> :dataRecolhimentoCE ");
		sql.append(" group by ");
		sql.append(" 	notaEnvio.numero , ");
		sql.append(" 	itemNota.NOTA_ENVIO_ID, ");
		sql.append(" 	itemNota.SEQUENCIA , ");
		sql.append(" 	produtoEdicao.ID ");
		sql.append(" union all ");
		sql.append(" select 0 as numeroNotaEnvio, null as dataEmissao, v.ID_PRODUTO_EDICAO as idProdutoEdicao,  ");
		sql.append(" v.QNT_PRODUTO as reparte,  v.DATA_OPERACAO as dataConsignacao ");
		sql.append(" from venda_produto v ");
		sql.append(" where v.ID_COTA = :idCota ");
		sql.append(" and v.ID_PRODUTO_EDICAO in(:idsProdutoEdicao) ");
		sql.append(" and v.DATA_OPERACAO <= :dataOperacao ");
		sql.append(" and v.TIPO_COMERCIALIZACAO_VENDA = :tipoComercializacao ");

		Query query = this.getSession().createSQLQuery(sql.toString())
				.addScalar("numeroNotaEnvio", StandardBasicTypes.BIG_INTEGER)
				.addScalar("dataEmissao", StandardBasicTypes.DATE)
				.addScalar("dataConsignacao", StandardBasicTypes.DATE)
				.addScalar("idProdutoEdicao", StandardBasicTypes.LONG)
				.addScalar("reparte", StandardBasicTypes.BIG_INTEGER)
				;
				
		query.setParameter("idCota", idCota);
		
		query.setParameterList("idsProdutoEdicao", idsProdutoEdicao);
		
		query.setParameter("dataRecolhimentoCE", dataRecolhimentoCE);
		
		query.setParameter("dataOperacao", dataOperacao);
		
		query.setParameter("tipoComercializacao", FormaComercializacao.CONSIGNADO.name());
		
		
		query.setResultTransformer(new AliasToBeanResultTransformer(NotaEnvioProdutoEdicao.class));
		
		return query.list();
		
	}
	
}

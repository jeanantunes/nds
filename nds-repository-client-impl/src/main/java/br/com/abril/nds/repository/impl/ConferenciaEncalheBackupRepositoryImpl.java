package br.com.abril.nds.repository.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ConferenciaEncalheDTO;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.estoque.ConferenciaEncalheBackup;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.ConferenciaEncalheBackupRepository;

@Repository
public class ConferenciaEncalheBackupRepositoryImpl extends AbstractRepositoryModel<ConferenciaEncalheBackup, Long> implements ConferenciaEncalheBackupRepository {

	public ConferenciaEncalheBackupRepositoryImpl() {
		super(ConferenciaEncalheBackup.class);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Long> obterIdConferenciasEncalheBackupNaData(Integer numeroCota, Date dataOperacao) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select distinct(c.id) from ConferenciaEncalheBackup c ");
		hql.append(" where c.dataOperacao = :dataOperacao and ");
		hql.append(" c.cota.numeroCota = :numeroCota ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("dataOperacao", dataOperacao);
		query.setParameter("numeroCota", numeroCota);
		
		return (List<Long>) query.list();
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ConferenciaEncalheDTO> obterDadosConferenciasEncalheBackup(Integer numeroCota, Date dataOperacao) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT ");
		
		sql.append(" CONF_ENCALHE.ID_CONFERENCIA_ORIGINAL AS idConferenciaEncalhe, ");
		
		sql.append(" CONF_ENCALHE.DIA_RECOLHIMENTO AS dia,           			   ");
		
		sql.append(" CONF_ENCALHE.QTDE AS qtdExemplar,                  	  	   ");
		
		sql.append(" CH_ENCALHE_COTA.PROCESSO_UTILIZA_NFE AS processoUtilizaNfe,   ");
		
		sql.append(" CASE WHEN (PROD_EDICAO.GRUPO_PRODUTO = :grupoProdutoCromo) THEN true ELSE false END as isContagemPacote, ");
		
		sql.append(" COALESCE(CH_ENCALHE_COTA.QTDE_PREVISTA, 0) AS qtdReparte, 	   ");
		
		sql.append(" CONF_ENCALHE.QTDE_INFORMADA AS qtdInformada,       		   ");
		
		sql.append(" CONF_ENCALHE.PRECO_CAPA_INFORMADO AS precoCapaInformado,      ");
		
		sql.append(" CONF_ENCALHE.PRODUTO_EDICAO_ID AS idProdutoEdicao, 		   ");
		
		sql.append(" PROD_EDICAO.CODIGO_DE_BARRAS AS codigoDeBarras,    		   ");
		
		sql.append(" PROD_EDICAO.CHAMADA_CAPA AS chamadaCapa,					   ");
		
		sql.append(" PESSOA_EDITOR.RAZAO_SOCIAL AS nomeEditor,					   ");
		
		sql.append(" PESSOA_FORNECEDOR.RAZAO_SOCIAL AS nomeFornecedor,			   ");			
		
		sql.append(" CH_ENCALHE.SEQUENCIA AS codigoSM,                             ");
		
		sql.append(" CH_ENCALHE.DATA_RECOLHIMENTO AS dataRecolhimento,  	       ");
		sql.append(" CH_ENCALHE.TIPO_CHAMADA_ENCALHE AS tipoChamadaEncalhe,	       ");
		sql.append(" PROD.CODIGO AS codigo,                                        ");
		sql.append(" PROD.NOME AS nomeProduto,                                     ");
		sql.append(" PROD_EDICAO.NUMERO_EDICAO AS numeroEdicao,                    ");
		
		sql.append(" COALESCE(CONF_ENCALHE.PRECO_CAPA, PROD_EDICAO.PRECO_VENDA, 0) AS precoCapa, ");
		
		sql.append(" COALESCE(CONF_ENCALHE.PRECO_COM_DESCONTO, 0) AS precoComDesconto, ");
		
		sql.append(" COALESCE( ( COALESCE(CONF_ENCALHE.PRECO_CAPA, PROD_EDICAO.PRECO_VENDA, 0)  ");
		sql.append(" - COALESCE(CONF_ENCALHE.PRECO_COM_DESCONTO, PROD_EDICAO.PRECO_VENDA, 0)), 0 ) AS desconto, ");
		
		sql.append(" CONF_ENCALHE.QTDE * ( ");
		sql.append(" COALESCE(CONF_ENCALHE.PRECO_COM_DESCONTO, PROD_EDICAO.PRECO_VENDA, 0)  ");
		sql.append(" ) AS valorTotal, ");
		
		sql.append(" CASE WHEN ");
        sql.append(" (SELECT DISTINCT plp.TIPO ");
        sql.append(" FROM LANCAMENTO lanc  ");
        sql.append(" JOIN periodo_lancamento_parcial plp ON (plp.ID = lanc.PERIODO_LANCAMENTO_PARCIAL_ID) ");
        sql.append(" JOIN produto_edicao pe ON (lanc.PRODUTO_EDICAO_ID = pe.ID) ");
        sql.append(" WHERE lanc.PRODUTO_EDICAO_ID = PROD_EDICAO.ID ");
        sql.append(" AND plp.TIPO = 'PARCIAL' ");
        sql.append(" AND lanc.status IN (:statusEmRecolhimento)) IS NOT NULL THEN TRUE ELSE FALSE END AS parcialNaoFinal, ");
		
		sql.append(" PROD_EDICAO.PACOTE_PADRAO AS pacotePadrao,              ");
		
		sql.append(" CONF_ENCALHE.DATA_OPERACAO AS dataConferencia,  ");
		sql.append(" CONF_ENCALHE.OBSERVACAO AS observacao, 	");
		sql.append(" CONF_ENCALHE.JURAMENTADA AS juramentada 	");
		
		sql.append(" FROM ");
	
		sql.append(" CONFERENCIA_ENCALHE_BACKUP CONF_ENCALHE ");
		
		sql.append(" LEFT JOIN CHAMADA_ENCALHE_COTA CH_ENCALHE_COTA ON (CH_ENCALHE_COTA.ID = CONF_ENCALHE.CHAMADA_ENCALHE_COTA_ID)	");
		sql.append(" LEFT JOIN CHAMADA_ENCALHE CH_ENCALHE ON (CH_ENCALHE.ID = CH_ENCALHE_COTA.CHAMADA_ENCALHE_ID) ");
		
		sql.append(" INNER JOIN PRODUTO_EDICAO PROD_EDICAO ON ( CONF_ENCALHE.PRODUTO_EDICAO_ID=PROD_EDICAO.ID ) ");
		sql.append(" INNER JOIN PRODUTO PROD ON (PROD_EDICAO.PRODUTO_ID=PROD.ID) ");
		
		sql.append(" INNER JOIN PRODUTO_FORNECEDOR PROD_FORNEC ON (PROD.ID = PROD_FORNEC.PRODUTO_ID)	");
		sql.append(" INNER JOIN FORNECEDOR FORNECEDOR_0 ON (FORNECEDOR_0.ID = PROD_FORNEC.FORNECEDORES_ID) 	");
		
		sql.append(" INNER JOIN EDITOR EDITOR_0 ON (PROD.EDITOR_ID = EDITOR_0.ID)			");
		sql.append(" INNER JOIN PESSOA PESSOA_FORNECEDOR ON (FORNECEDOR_0.JURIDICA_ID = PESSOA_FORNECEDOR.ID) 	");
		sql.append(" INNER JOIN PESSOA PESSOA_EDITOR ON (EDITOR_0.JURIDICA_ID = PESSOA_EDITOR.ID) 		");
		
		sql.append(" INNER JOIN COTA ON (COTA.ID = CONF_ENCALHE.COTA_ID) ");
		
		sql.append(" WHERE ");
		
		sql.append(" CONF_ENCALHE.DATA_OPERACAO = :dataOperacao AND ");
		
		sql.append(" COTA.NUMERO_COTA = :numeroCota ");
		
		sql.append(" ORDER BY CH_ENCALHE.DATA_RECOLHIMENTO, CH_ENCALHE.SEQUENCIA ");
		
		Query query = getSession().createSQLQuery(sql.toString()).setResultTransformer(new AliasToBeanResultTransformer(ConferenciaEncalheDTO.class));
		
		((SQLQuery)query).addScalar("idConferenciaEncalhe", StandardBasicTypes.LONG);
		((SQLQuery)query).addScalar("dia", StandardBasicTypes.INTEGER);
		((SQLQuery)query).addScalar("qtdExemplar", StandardBasicTypes.BIG_INTEGER);
		((SQLQuery)query).addScalar("isContagemPacote", StandardBasicTypes.BOOLEAN);
		((SQLQuery)query).addScalar("qtdReparte", StandardBasicTypes.BIG_INTEGER);
		((SQLQuery)query).addScalar("qtdInformada", StandardBasicTypes.BIG_INTEGER);
		((SQLQuery)query).addScalar("precoCapaInformado", StandardBasicTypes.BIG_DECIMAL);
		((SQLQuery)query).addScalar("idProdutoEdicao", StandardBasicTypes.LONG);
		((SQLQuery)query).addScalar("codigoDeBarras");
		((SQLQuery)query).addScalar("chamadaCapa");	
		((SQLQuery)query).addScalar("nomeEditor");
		((SQLQuery)query).addScalar("nomeFornecedor");
		((SQLQuery)query).addScalar("codigoSM", StandardBasicTypes.INTEGER);		
		((SQLQuery)query).addScalar("dataRecolhimento");
		((SQLQuery)query).addScalar("tipoChamadaEncalhe");
		((SQLQuery)query).addScalar("codigo");
		((SQLQuery)query).addScalar("nomeProduto");
		((SQLQuery)query).addScalar("numeroEdicao", StandardBasicTypes.LONG);
		((SQLQuery)query).addScalar("precoCapa");
		((SQLQuery)query).addScalar("precoComDesconto");
		((SQLQuery)query).addScalar("desconto");
		((SQLQuery)query).addScalar("valorTotal");
		((SQLQuery)query).addScalar("parcialNaoFinal", StandardBasicTypes.BOOLEAN);
		((SQLQuery)query).addScalar("pacotePadrao");
		((SQLQuery)query).addScalar("observacao");
		((SQLQuery)query).addScalar("juramentada");
		((SQLQuery)query).addScalar("processoUtilizaNfe", StandardBasicTypes.BOOLEAN);
		
		query.setParameter("grupoProdutoCromo", GrupoProduto.CROMO.name());
		
		query.setParameterList("statusEmRecolhimento", Arrays.asList(
				StatusLancamento.BALANCEADO_RECOLHIMENTO.name(),
				StatusLancamento.EM_BALANCEAMENTO_RECOLHIMENTO.name(), 
				StatusLancamento.EM_RECOLHIMENTO.name()));
		
		query.setParameter("dataOperacao", dataOperacao);
		
		query.setParameter("numeroCota", numeroCota);
		
		return (List<ConferenciaEncalheDTO>) query.list();
		
	}

	
	@Override
	public boolean existemConferenciasEncalheBackupNaData(Integer numeroCota, Date dataOperacao) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select count(conf.id) > 0 as existeBackupEncalhe from conferencia_encalhe_backup conf 	");
		hql.append(" inner join cota on (cota.id = conf.cota_id)	");
		hql.append(" where conf.data_operacao = :dataOperacao and	");
		hql.append(" cota.numero_cota = :numeroCota ");
		
		Query query = getSession().createSQLQuery(hql.toString()).addScalar("existeBackupEncalhe", StandardBasicTypes.BOOLEAN);
		
		query.setParameter("dataOperacao", dataOperacao);
		query.setParameter("numeroCota", numeroCota);
		
		return (Boolean) query.uniqueResult();
		
	}
	
	
	
}

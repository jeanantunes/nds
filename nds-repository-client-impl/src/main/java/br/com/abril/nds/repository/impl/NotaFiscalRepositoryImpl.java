package br.com.abril.nds.repository.impl;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.CotaExemplaresDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.NfeDTO;
import br.com.abril.nds.dto.filtro.FiltroMonitorNfeDTO;
import br.com.abril.nds.dto.filtro.FiltroViewNotaFiscalDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.fiscal.TipoDestinatario;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.fiscal.nota.StatusProcessamentoInterno;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.NotaFiscalRepository;
import br.com.abril.nds.vo.PaginacaoVO;

@Repository
public class NotaFiscalRepositoryImpl extends AbstractRepositoryModel<NotaFiscal, Long> implements NotaFiscalRepository {

	public NotaFiscalRepositoryImpl() {
		super(NotaFiscal.class);
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.NotaFiscalRepository#obterListaNotasFiscaisPor(br.com.abril.nds.model.fiscal.nota.StatusProcessamentoInterno)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<NotaFiscal> obterListaNotasFiscaisPor(StatusProcessamentoInterno statusProcessamentoInterno) {

		Criteria criteria = getSession().createCriteria(NotaFiscal.class);

		criteria.add(Restrictions.eq("statusProcessamentoInterno", statusProcessamentoInterno));

		return criteria.list();
	}

	public Integer obterQtdeRegistroNotaFiscal(FiltroMonitorNfeDTO filtro) {


		StringBuffer sql = new StringBuffer("");

		sql.append(" SELECT COUNT(*) FROM ( ");	

		sql.append(" SELECT NOTA_FISCAL.ID "); 
		sql.append(" FROM NOTA_FISCAL 		");
		
		boolean indAnd = false;

		if(filtro.getBox()!=null ) {

			sql.append(" INNER JOIN COTA ON ");
			sql.append(" ( NOTA_FISCAL_NOVO.PESSOA_DESTINATARIO_ID_REFERENCIA = COTA.PESSOA_ID ) ");

			sql.append(" INNER JOIN BOX ON ");
			sql.append(" (BOX.ID = COTA.BOX_ID) ");

		}

		if(	(filtro.getBox()!=null) ||
				filtro.getDataInicial() != null ||
				filtro.getDataFinal() != null 	||
				( filtro.getDocumentoPessoa() != null && !filtro.getDocumentoPessoa().isEmpty() ) ||
				( filtro.getTipoNfe() != null && !filtro.getTipoNfe().isEmpty() ) ||
				filtro.getNumeroNotaInicial() != null 	||
				filtro.getNumeroNotaFinal()!=null		||
				( filtro.getChaveAcesso() != null && !filtro.getChaveAcesso().isEmpty() ) ||
				( filtro.getSituacaoNfe() != null && !filtro.getSituacaoNfe().isEmpty() ) ||
				filtro.getSerie() != null ) {

			sql.append(" WHERE ");

		}


		if(filtro.getBox()!=null) {

			sql.append(" BOX.CODIGO = :codigoBox ");

			indAnd = true;
		}

		if(filtro.getDataInicial()!=null) {

			if(indAnd) {
				sql.append(" AND ");
			}

			sql.append(" NOTA_FISCAL_NOVO.DATA_EMISSAO >= :dataInicial ");

			indAnd = true;
		}

		if(filtro.getDataFinal()!=null) {

			if(indAnd) {
				sql.append(" AND ");
			}

			sql.append(" NOTA_FISCAL_NOVO.DATA_EMISSAO <= :dataFinal ");

			indAnd = true;

		}
		
		if(filtro.getDocumentoPessoa()!=null && !filtro.getDocumentoPessoa().isEmpty()) {

			if(indAnd) {
				sql.append(" AND ");
			}

			if(filtro.isIndDocumentoCPF()) {
				sql.append(" NOTA_FISCAL_NOVO.DOCUMENTO_DESTINATARIO = :documento AND PESSOA_DESTINATARIO.TIPO = 'F' ");
			} else {
				sql.append(" NOTA_FISCAL_NOVO.DOCUMENTO_DESTINATARIO = :documento AND PESSOA_DESTINATARIO.TIPO = 'J' ");
			}

			indAnd = true;

		}

		if(filtro.getTipoNfe()!=null && !filtro.getTipoNfe().isEmpty()) {

			if(indAnd) {
				sql.append(" AND ");
			}

			sql.append(" EXISTS(SELECT * FROM NOTA_FISCAL_PROCESSO ")
			.append("        WHERE NOTA_FISCAL_PROCESSO.NOTA_FISCAL_ID = NOTA_FISCAL_NOVO.ID ")
			.append("          AND NOTA_FISCAL_PROCESSO.PROCESSO = :tipoEmissaoNfe) ");

			indAnd = true;
		}

		if(filtro.getNumeroNotaInicial()!=null) {

			if(indAnd) {
				sql.append(" AND ");
			}

			sql.append(" NOTA_FISCAL_NOVO.NUMERO_DOCUMENTO_FISCAL >= :numeroInicial ");

			indAnd = true;

		}

		if(filtro.getNumeroNotaFinal()!=null) {

			if(indAnd) {
				sql.append(" AND ");
			}

			sql.append(" NOTA_FISCAL_NOVO.NUMERO_DOCUMENTO_FISCAL <= :numeroFinal ");

			indAnd = true;

		}

		if(filtro.getChaveAcesso()!=null && !filtro.getChaveAcesso().isEmpty()) {

			if(indAnd) {
				sql.append(" AND ");
			}

			sql.append(" NOTA_FISCAL_NOVO.CHAVE_ACESSO = :chaveAcesso ");

			indAnd = true;
		}

		if(filtro.getSituacaoNfe()!=null && !filtro.getSituacaoNfe().isEmpty()) {

			if(indAnd) {
				sql.append(" AND ");
			}

			sql.append(" NOTA_FISCAL_NOVO.STATUS = :situacaoNfe ");

			indAnd = true;
		}

		if(filtro.getSerie()!=null) {

			if(indAnd) {
				sql.append(" AND ");
			}

			sql.append(" NOTA_FISCAL_NOVO.SERIE = :serie ");

			indAnd = true;
		}

		sql.append(" ) AS NOTAS_FISCAIS");

		SQLQuery sqlQuery = getSession().createSQLQuery(sql.toString());


		if(filtro.getBox()!=null ) {
			sqlQuery.setParameter("codigoBox", filtro.getBox());
		}

		if(filtro.getDataInicial()!=null) {
			sqlQuery.setParameter("dataInicial", filtro.getDataInicial());
		}

		if(filtro.getDataFinal()!=null) {
			sqlQuery.setParameter("dataFinal", filtro.getDataFinal());
		}

		if(filtro.getDocumentoPessoa()!=null && !filtro.getDocumentoPessoa().isEmpty()) {
			sqlQuery.setParameter("documento", filtro.getDocumentoPessoa());
		}

		if(filtro.getTipoNfe()!=null && !filtro.getTipoNfe().isEmpty()) {
			sqlQuery.setParameter("tipoEmissaoNfe", filtro.getTipoNfe());
		}

		if(filtro.getNumeroNotaInicial()!=null) {
			sqlQuery.setParameter("numeroInicial", filtro.getNumeroNotaInicial());
		}

		if(filtro.getNumeroNotaFinal()!=null) {
			sqlQuery.setParameter("numeroFinal", filtro.getNumeroNotaFinal());
		}

		if(filtro.getChaveAcesso()!=null && !filtro.getChaveAcesso().isEmpty()) {
			sqlQuery.setParameter("chaveAcesso", filtro.getChaveAcesso());
		}

		if(filtro.getSituacaoNfe()!=null && !filtro.getSituacaoNfe().isEmpty()) {
			sqlQuery.setParameter("situacaoNfe", filtro.getSituacaoNfe());
		}		

		if(filtro.getSerie()!=null) {
			sqlQuery.setParameter("serie", filtro.getSerie());
		}

		BigInteger qtde = (BigInteger) sqlQuery.uniqueResult();

		return ((qtde == null) ? 0 : qtde.intValue());

	}	



	@SuppressWarnings("unchecked")
	public List<NfeDTO> pesquisarNotaFiscal(FiltroMonitorNfeDTO filtro) {

		StringBuffer sql = new StringBuffer("");

		sql.append(" SELECT ");	

		sql.append(" NOTA_FISCAL_NOVO.ID as idNotaFiscal, 					"); 

		sql.append(" NOTA_FISCAL_NOVO.NUMERO_DOCUMENTO_FISCAL as numero, 	"); 

		sql.append(" NOTA_FISCAL_NOVO.SERIE as serie, 						"); 

		sql.append(" NOTA_FISCAL_NOVO.DATA_EMISSAO as emissao, 				"); 

		sql.append(" 'NORMAL' as tipoEmissao, 		"); 	

		sql.append(" CASE WHEN PESSOA_DESTINATARIO.TIPO = 'J'  THEN NOTA_FISCAL_NOVO.DOCUMENTO_DESTINATARIO 	ELSE NULL END AS  cnpjDestinatario,	");

		sql.append(" CASE WHEN PESSOA_REMETENTE.TIPO 	= 'J'  THEN NOTA_FISCAL_NOVO.DOCUMENTO_EMITENTE 	 	ELSE NULL END AS  cnpjRemetente,	");

		sql.append(" CASE WHEN PESSOA_REMETENTE.TIPO 	= 'F'  THEN NOTA_FISCAL_NOVO.DOCUMENTO_EMITENTE 		ELSE NULL END AS  cpfRemetente,		");

		sql.append(" NOTA_FISCAL_NOVO.STATUS as statusNfe,  "); 

		sql.append(" CASE WHEN NOTA_FISCAL_NOVO.TIPO_OPERACAO = 0 THEN 'ENTRADA' ELSE 'SAIDA' END AS tipoNfe,	");

		sql.append(" NOTA_FISCAL_NOVO.MOTIVO as movimentoIntegracao ");

		sql.append(" from NOTA_FISCAL_NOVO ");

		sql.append(" LEFT JOIN PESSOA AS PESSOA_DESTINATARIO ON ");
		sql.append(" ( NOTA_FISCAL_NOVO.PESSOA_DESTINATARIO_ID_REFERENCIA = PESSOA_DESTINATARIO.ID )  ");

		sql.append(" LEFT JOIN PESSOA AS PESSOA_REMETENTE ON ");
		sql.append(" ( NOTA_FISCAL_NOVO.PESSOA_EMITENTE_ID_REFERENCIADA = PESSOA_REMETENTE.ID )  ");

		boolean indAnd = false;

		if(filtro.getBox()!=null) {

			sql.append(" INNER JOIN COTA ON ");
			sql.append(" ( NOTA_FISCAL_NOVO.PESSOA_DESTINATARIO_ID_REFERENCIA = COTA.PESSOA_ID ) ");

			sql.append(" INNER JOIN BOX ON ");
			sql.append(" (BOX.ID = COTA.BOX_ID) ");

		}

		if(	(filtro.getBox()!=null) ||
				filtro.getDataInicial() != null ||
				filtro.getDataFinal() != null 	||
				( filtro.getDocumentoPessoa() != null && !filtro.getDocumentoPessoa().isEmpty() ) ||
				( filtro.getTipoNfe() != null && !filtro.getTipoNfe().isEmpty() ) ||
				filtro.getNumeroNotaInicial() != null 	||
				filtro.getNumeroNotaFinal()!=null		||
				( filtro.getChaveAcesso() != null && !filtro.getChaveAcesso().isEmpty() ) ||
				( filtro.getSituacaoNfe() != null && !filtro.getSituacaoNfe().isEmpty() ) ||
				filtro.getSerie() != null ) {

			sql.append(" where ");

		}


		if(filtro.getBox()!=null) {

			sql.append(" BOX.CODIGO = :codigoBox ");

			indAnd = true;
		}

		if(filtro.getDataInicial()!=null) {

			if(indAnd) {
				sql.append(" AND ");
			}

			sql.append(" NOTA_FISCAL_NOVO.DATA_EMISSAO >= :dataInicial ");

			indAnd = true;
		}

		if(filtro.getDataFinal()!=null) {

			if(indAnd) {
				sql.append(" AND ");
			}

			sql.append(" NOTA_FISCAL_NOVO.DATA_EMISSAO <= :dataFinal ");

			indAnd = true;

		}

		if(filtro.getDocumentoPessoa()!=null && !filtro.getDocumentoPessoa().isEmpty()) {

			if(indAnd) {
				sql.append(" AND ");
			}

			if(filtro.isIndDocumentoCPF()) {
				sql.append(" NOTA_FISCAL_NOVO.DOCUMENTO_DESTINATARIO = :documento AND PESSOA_DESTINATARIO.TIPO = 'F' ");
			} else {
				sql.append(" NOTA_FISCAL_NOVO.DOCUMENTO_DESTINATARIO = :documento AND PESSOA_DESTINATARIO.TIPO = 'J' ");
			}

			indAnd = true;

		}

		if(filtro.getTipoNfe()!=null && !filtro.getTipoNfe().isEmpty()) {

			if(indAnd) {
				sql.append(" AND ");
			}

			sql.append(" EXISTS(SELECT * FROM NOTA_FISCAL_PROCESSO ")
			.append("        WHERE NOTA_FISCAL_PROCESSO.NOTA_FISCAL_ID = NOTA_FISCAL_NOVO.ID ")
			.append("          AND NOTA_FISCAL_PROCESSO.PROCESSO = :tipoEmissaoNfe) ");

			indAnd = true;
		}

		if(filtro.getNumeroNotaInicial()!=null) {

			if(indAnd) {
				sql.append(" AND ");
			}

			sql.append(" NOTA_FISCAL_NOVO.NUMERO_DOCUMENTO_FISCAL >= :numeroInicial ");

			indAnd = true;

		}

		if(filtro.getNumeroNotaFinal()!=null) {

			if(indAnd) {
				sql.append(" AND ");
			}

			sql.append(" NOTA_FISCAL_NOVO.NUMERO_DOCUMENTO_FISCAL <= :numeroFinal ");

			indAnd = true;

		}

		if(filtro.getChaveAcesso()!=null && !filtro.getChaveAcesso().isEmpty()) {

			if(indAnd) {
				sql.append(" AND ");
			}

			sql.append(" NOTA_FISCAL_NOVO.CHAVE_ACESSO = :chaveAcesso ");

			indAnd = true;
		}

		if(filtro.getSituacaoNfe()!=null && !filtro.getSituacaoNfe().isEmpty()) {

			if(indAnd) {
				sql.append(" AND ");
			}

			sql.append(" NOTA_FISCAL_NOVO.STATUS = :situacaoNfe ");

			indAnd = true;
		}

		if(filtro.getSerie()!=null) {

			if(indAnd) {
				sql.append(" AND ");
			}

			sql.append(" NOTA_FISCAL_NOVO.SERIE = :serie ");

			indAnd = true;
		}

		PaginacaoVO paginacao = filtro.getPaginacao();

		if (filtro.getOrdenacaoColuna() != null) {

			sql.append(" order by ");

			String orderByColumn = "";

			switch (filtro.getOrdenacaoColuna()) {

			case NOTA:
				orderByColumn = " NOTA_FISCAL_NOVO.NUMERO_DOCUMENTO_FISCAL ";
				break;
			case SERIE:
				orderByColumn = " NOTA_FISCAL_NOVO.SERIE ";
				break;
			case EMISSAO:
				orderByColumn = " NOTA_FISCAL_NOVO.DATA_EMISSAO ";
				break;
			case TIPO_EMISSAO:
				orderByColumn = " tipoEmissao ";
				break;
			case CNPJ_DESTINATARIO:
				orderByColumn = " cnpjDestinatario ";
				break;
			case CNPJ_REMETENTE:
				orderByColumn = " cnpjRemetente ";
				break;
			case CPF_REMETENTE:
				orderByColumn = " cpfRemetente ";
				break;
			case STATUS_NFE:
				orderByColumn = " NOTA_FISCAL_NOVO.STATUS ";
				break;
			case TIPO_NFE:
				orderByColumn = " NOTA_FISCAL_NOVO.TIPO_OPERACAO ";
				break;
			case MOVIMENTO_INTEGRACAO:
				orderByColumn = " NOTA_FISCAL_NOVO.MOTIVO ";
				break;
			default:
				break;
			}

			sql.append(orderByColumn);

			if (paginacao.getOrdenacao() != null) {
				sql.append(paginacao.getOrdenacao().toString());
			}

		}


		SQLQuery sqlQuery = getSession().createSQLQuery(sql.toString());

		sqlQuery.addScalar("idNotaFiscal", LongType.INSTANCE);
		sqlQuery.addScalar("numero", LongType.INSTANCE);
		sqlQuery.addScalar("serie", StringType.INSTANCE);;
		sqlQuery.addScalar("emissao");
		sqlQuery.addScalar("tipoEmissao");
		sqlQuery.addScalar("cnpjDestinatario");
		sqlQuery.addScalar("cnpjRemetente");
		sqlQuery.addScalar("cpfRemetente");
		sqlQuery.addScalar("statusNfe");
		sqlQuery.addScalar("tipoNfe");
		sqlQuery.addScalar("movimentoIntegracao");

		sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(NfeDTO.class));

		if(filtro.getBox()!=null) {
			sqlQuery.setParameter("codigoBox", filtro.getBox());
		}

		if(filtro.getDataInicial()!=null) {
			sqlQuery.setParameter("dataInicial", filtro.getDataInicial());
		}

		if(filtro.getDataFinal()!=null) {
			sqlQuery.setParameter("dataFinal", filtro.getDataFinal());
		}

		if(filtro.getDocumentoPessoa()!=null && !filtro.getDocumentoPessoa().isEmpty()) {
			sqlQuery.setParameter("documento", filtro.getDocumentoPessoa());
		}

		if(filtro.getTipoNfe()!=null && !filtro.getTipoNfe().isEmpty()) {
			sqlQuery.setParameter("tipoEmissaoNfe", filtro.getTipoNfe());
		}

		if(filtro.getNumeroNotaInicial()!=null) {
			sqlQuery.setParameter("numeroInicial", filtro.getNumeroNotaInicial());
		}

		if(filtro.getNumeroNotaFinal()!=null) {
			sqlQuery.setParameter("numeroFinal", filtro.getNumeroNotaFinal());
		}

		if(filtro.getChaveAcesso()!=null && !filtro.getChaveAcesso().isEmpty()) {
			sqlQuery.setParameter("chaveAcesso", filtro.getChaveAcesso());
		}

		if(filtro.getSituacaoNfe()!=null && !filtro.getSituacaoNfe().isEmpty()) {
			sqlQuery.setParameter("situacaoNfe", filtro.getSituacaoNfe());
		}

		if(filtro.getSerie()!=null) {
			sqlQuery.setParameter("serie", filtro.getSerie());
		}

		if (paginacao != null) {

			if (paginacao.getPosicaoInicial() != null) {

				sqlQuery.setFirstResult(paginacao.getPosicaoInicial());
			}

			if (paginacao.getQtdResultadosPorPagina() != null) {

				sqlQuery.setMaxResults(paginacao.getQtdResultadosPorPagina());
			}
		}

		return sqlQuery.list();

	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Long> obterNumerosNFePorLancamento(Long idLancamento){
		
		StringBuilder hql = new StringBuilder("select ");
		hql.append(" nota.identificacao.numeroDocumentoFiscal ")
		   .append(" from ")
		   .append(" 	Lancamento l ")
		   .append("	join l.movimentoEstoqueCotas movEst ")
		   .append("	join movEst.listaProdutoServicos prodServ ")
		   .append("	join prodServ.produtoServicoPK.notaFiscal nota ")
		   .append(" where ")
		   .append("	l.id = :idLancamento ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idLancamento", idLancamento);
		
		return query.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<CotaExemplaresDTO> consultaCotaExemplaresSumarizados(FiltroViewNotaFiscalDTO filtro) {
		
		// OBTER COTA EXEMPLARES SUMARIZADOS
		StringBuilder hql = new StringBuilder("SELECT ");
		hql.append(" mec.cota.id as idCota, ");
		hql.append(" mec.cota.numeroCota as numeroCota, ");
		hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome,'') as nomeCota,");
		hql.append(" SUM(mec.qtde) as exemplares, ");
		hql.append(" SUM(mec.valoresAplicados.precoVenda * mec.qtde) as total, "); 
		hql.append(" SUM(mec.valoresAplicados.precoComDesconto * mec.qtde) as totalDesconto"); 	
		
		Query query = queryConsultaNfeParameters(queryConsultaNfe(filtro, hql, false, false, false), filtro);
				
		if(filtro.getPaginacaoVO()!=null) {
			if(filtro.getPaginacaoVO().getPosicaoInicial()!=null) {
				query.setFirstResult(filtro.getPaginacaoVO().getPosicaoInicial());
			}

			if(filtro.getPaginacaoVO().getQtdResultadosPorPagina()!=null) {
				query.setMaxResults(filtro.getPaginacaoVO().getQtdResultadosPorPagina());
			}
		}
		
		query.setResultTransformer(new AliasToBeanResultTransformer(CotaExemplaresDTO.class));
		
		return query.list();
	}
	
	@Override
	public Long consultaCotaExemplaresSumarizadosQtd(FiltroViewNotaFiscalDTO filtro) {
		
		// OBTER COTA EXEMPLARES SUMARIZADOS
		StringBuilder hql = new StringBuilder("SELECT ");
		hql.append(" COUNT(mec.cota.id) ");
		Query query = queryConsultaNfeParameters(queryConsultaNfe(filtro, hql, true, true, false), filtro);
		
		return (long) query.list().size();
	}
	
	/**
	 * Obter conjunto de cotas
	 * @param FiltroViewNotaFiscalDTO
	 * @return List ids
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Cota> obterConjuntoCotasNotafiscal(FiltroViewNotaFiscalDTO filtro) {
		
		// OBTER ID DE TODAS AS COTAS DA TELA
		StringBuilder hql = new StringBuilder("SELECT ");
		hql.append(" mec.cota ");
		Query query = queryConsultaNfeParameters(queryConsultaNfe(filtro, hql, false, false, false), filtro);				
		return query.list();
	}
	
	/**
	 * Obter os itens da nota com base nos movimentos de estoque cota
	 * 
	 * @param filtro
	 * @return
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<MovimentoEstoqueCota> obterMovimentoEstoqueCota(FiltroViewNotaFiscalDTO filtro) {
	
		// ITENS DA NOTA FISCAL
		StringBuilder hql = new StringBuilder("SELECT mec");
		Query query = queryConsultaNfeParameters(queryConsultaNfe(filtro, hql, false, false, true), filtro);
		
		return query.list();
	}
	
	private StringBuilder queryConsultaNfe(FiltroViewNotaFiscalDTO filtro, StringBuilder hql, boolean isCount, boolean isPagination, boolean isGroup) {
		
		hql.append(" FROM MovimentoEstoqueCota mec ")
		.append(" JOIN mec.tipoMovimento tipoMovimento ")
		.append(" JOIN mec.lancamento lancamento ")
		.append(" JOIN mec.cota cota ")
		.append(" JOIN cota.pessoa pessoa ")
		.append(" LEFT JOIN cota.box box ")
		.append(" LEFT JOIN box.roteirizacao roteirizacao ")
		.append(" LEFT JOIN roteirizacao.roteiros roteiro ")
		.append(" LEFT JOIN roteiro.rotas rota ")
		.append(" JOIN mec.produtoEdicao produtoEdicao")
		.append(" JOIN produtoEdicao.produto produto ")
		.append(" JOIN produto.fornecedores fornecedor")
		.append(" WHERE mec.data BETWEEN :dataInicial AND :dataFinal ")
		.append(" AND mec.movimentoEstoqueCotaEstorno is null ")
		.append(" AND mec.movimentoEstoqueCotaFuro is null ");
		
		// Tipo de Nota:		
		if(filtro.getIdNaturezaOperacao() != null) {
			hql.append(" AND mec.tipoMovimento.id in (SELECT tm.id ");
			hql.append("FROM NaturezaOperacao no ");
			hql.append("JOIN no.tipoMovimento tm ");
			hql.append("WHERE no.id in(:tipoNota)) ");
		}
		
		// Data Emissão:	...		
		if(filtro.getDataEmissao() != null) {
			hql.append(" ");
		}
		
		// Cota:		
		if(filtro.getIdCota() != null) {
			hql.append(" AND cota.id = :cotaId ");
		}
		
		// Intervalo de Cota:
		if(filtro.getIntervalorCotaInicial() != null && filtro.getIntervalorCotaFinal() != null) {
			hql.append(" AND cota.numeroCota BETWEEN :numeroCotaInicial AND :numeroCotaFinal ");
		}
		
		// Roteiro:
		if(filtro.getIdRoteiro() != null) {
			hql.append(" AND roteiro.id = :roteiroId ");
		}
		
		// Rota:		
		if(filtro.getIdRota() != null) {
			hql.append(" AND rota.id = :rotaId ");
		}
		
		// Cota de:	 Até   
		if(filtro.getIntervaloBoxInicial() != null && filtro.getIntervaloBoxFinal() != null) {
			hql.append(" AND box.codigo between :codigoBoxInicial AND :codigoBoxFinal ");
		}
		
		if(filtro.getListIdFornecedor() != null) {
			hql.append(" AND fornecedor.id in (:fornecedor) ");
		}
		
		if(!isGroup){
			hql.append(" GROUP BY mec.cota.numeroCota ");
		}
		
		if(!isCount && !isPagination){
			if(filtro.getPaginacaoVO()!=null && filtro.getPaginacaoVO().getSortOrder() != null && filtro.getPaginacaoVO().getSortColumn() != null) {
				hql.append(" ORDER BY  ").append(filtro.getPaginacaoVO().getSortColumn()).append(" ").append(filtro.getPaginacaoVO().getSortOrder());
			}
		}
		
		return hql;
	}
	
	public Query queryConsultaNfeParameters(StringBuilder hql, FiltroViewNotaFiscalDTO filtro) {
		

		// Realizar a consulta e converter ao objeto cota exemplares.
		Query query = this.getSession().createQuery(hql.toString());		
		
		// Data Movimento:	...  Até   ...
		if (filtro.getDataInicial() != null && filtro.getDataFinal() != null) {
			query.setParameter("dataInicial", filtro.getDataInicial());
			query.setParameter("dataFinal", filtro.getDataFinal());
		}
		
		// tipo da nota fiscal		
		if(filtro.getIdNaturezaOperacao() !=null && !"".equals(filtro.getIdNaturezaOperacao())) {
			query.setParameter("tipoNota", filtro.getIdNaturezaOperacao());
		}
		
		// forncedor id		
		if(filtro.getListIdFornecedor() !=null && !filtro.getListIdFornecedor().isEmpty()) {
			query.setParameterList("fornecedor", filtro.getListIdFornecedor());
		}
		// Data Emissão:	...		
		/*if(filtro.getDataEmissao() != null) {
			
		}*/
		
		if(filtro.getIdCota() != null) {
			query.setParameter("cotaId", filtro.getIdCota());
		}
		
		if(filtro.getIntervalorCotaInicial() != null && filtro.getIntervalorCotaFinal() != null) {
			query.setParameter("numeroCotaInicial", filtro.getIntervalorCotaInicial());
			query.setParameter("numeroCotaFinal", filtro.getIntervalorCotaFinal());
		}
		
		// Roteiro:
		if(filtro.getIdRoteiro() != null) {
			query.setParameter("roteiroId", filtro.getIdRoteiro());
		}
		
		// Rota:		
		if(filtro.getIdRota() != null) {
			query.setParameter("rotaId", filtro.getIdRota());
		}
		
		// Cota de:	 Até   
		if(filtro.getIntervaloBoxInicial() != null && filtro.getIntervaloBoxFinal() != null) {
			query.setParameter("codigoBoxInicial", filtro.getIntervaloBoxInicial());
			query.setParameter("codigoBoxFinal", filtro.getIntervaloBoxFinal());
		}
		
		if(filtro.getListIdFornecedor() != null) {
			query.setParameterList("fornecedor", filtro.getListIdFornecedor());
		}
		
		return query;	
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ItemDTO<Long, String>> obterNaturezasOperacoesPorTipoDestinatario(TipoDestinatario tipoDestinatario) {
		
		
		StringBuilder sql = new StringBuilder("")
			.append("SELECT id as `key`, descricao as value ") 
			.append("FROM natureza_operacao no ")
			.append("WHERE no.TIPO_ATIVIDADE = (select TIPO_ATIVIDADE from distribuidor) ");
		
		if(tipoDestinatario != null) {
			sql.append("AND no.TIPO_DESTINATARIO = :tipoDestinatario ");
		}

		SQLQuery sqlQuery = getSession().createSQLQuery(sql.toString());
		
		if(tipoDestinatario != null) {
			sqlQuery.setParameter("tipoDestinatario", tipoDestinatario.name());
		}
		
		sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(ItemDTO.class));

		return sqlQuery.list();
		
	}
}

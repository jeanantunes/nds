package br.com.abril.nds.repository.impl;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.NfeDTO;
import br.com.abril.nds.dto.NfeImpressaoDTO;
import br.com.abril.nds.dto.filtro.FiltroImpressaoNFEDTO;
import br.com.abril.nds.dto.filtro.FiltroMonitorNfeDTO;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.fiscal.nota.StatusProcessamentoInterno;
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
	public List<NotaFiscal> obterListaNotasFiscaisPor(
			StatusProcessamentoInterno statusProcessamentoInterno) {

		Criteria criteria = getSession().createCriteria(NotaFiscal.class);

		criteria.add(Restrictions.eq("statusProcessamentoInterno", statusProcessamentoInterno));

		return criteria.list();
	}

	public Integer obterQtdeRegistroNotaFiscal(FiltroMonitorNfeDTO filtro) {


		StringBuffer sql = new StringBuffer("");

		sql.append(" SELECT COUNT(*) FROM ( ");	

		sql.append(" SELECT NOTA_FISCAL_NOVO.ID "); 
		sql.append(" FROM NOTA_FISCAL_NOVO 		");

		sql.append(" LEFT JOIN PESSOA AS PESSOA_DESTINATARIO ON ");
		sql.append(" ( NOTA_FISCAL_NOVO.PESSOA_DESTINATARIO_ID_REFERENCIA = PESSOA_DESTINATARIO.ID )  ");

		sql.append(" LEFT JOIN PESSOA AS PESSOA_REMETENTE ON ");
		sql.append(" ( NOTA_FISCAL_NOVO.PESSOA_EMITENTE_ID_REFERENCIADA = PESSOA_REMETENTE.ID )  ");

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

		sqlQuery.addScalar("idNotaFiscal", Hibernate.LONG);
		sqlQuery.addScalar("numero", Hibernate.LONG);
		sqlQuery.addScalar("serie", Hibernate.STRING);;
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

	@Transactional(readOnly=true)
	public List<NfeImpressaoDTO> buscarNFeParaImpressao(FiltroImpressaoNFEDTO filtro) {

		StringBuilder sql = new StringBuilder();
		sql.append("select new br.com.abril.nds.dto.NfeImpressaoDTO(cota, SUM(nf.informacaoValoresTotais.valorProdutos) as vlrTotal, SUM(nf.informacaoValoresTotais.valorDesconto) as vlrTotalDesconto) ");
		
		//Complementa o HQL com as clausulas de filtro
		Query q = mountarFiltroConsultaNfeParaImpressao(filtro, sql, filtro.getPaginacao());

		return q.list();
		
	}

	@Transactional(readOnly=true)
	public Integer buscarNFeParaImpressaoTotalQtd(FiltroImpressaoNFEDTO filtro) {

		StringBuilder sql = new StringBuilder();
		sql.append("select count(*) ");
		
		//Complementa o HQL com as clausulas de filtro
		Query q = mountarFiltroConsultaNfeParaImpressao(filtro, sql, null);
		
		return q.list().size();
	}
	
	//Torna reaproveitavel a parte de filtro da query
	private Query mountarFiltroConsultaNfeParaImpressao(FiltroImpressaoNFEDTO filtro, StringBuilder sql, PaginacaoVO paginacao) {
		
		sql.append("from NotaFiscal nf, Cota cota ");
		
		if(filtro.getDataMovimentoInicial() != null || filtro.getDataMovimentoFinal() != null) {
			sql.append(", MovimentoEstoqueCota movEstCota ");
		}
		
		if(filtro.getIdRoteiro() != null && filtro.getIdRoteiro() > -1) {
			sql.append(", Roteiro roteiro ");
		}
		
		if(filtro.getIdRoteiro() != null && filtro.getIdRoteiro() > -1 
				&& filtro.getIdRota() != null && filtro.getIdRota() > -1) {
			sql.append("join roteiro.rotas rota ");
		}
				
		sql.append("WHERE 1 = 1 ");
		sql.append("and nf.identificacaoDestinatario.pessoaDestinatarioReferencia.id = cota.pessoa.id ");
		sql.append("and nf.identificacao.dataEmissao = :dataEmissao ");
		
		//Filtra por datas de movimento de, entre e ate
		if(filtro.getDataMovimentoInicial() != null || filtro.getDataMovimentoFinal() != null) {
			if(filtro.getDataMovimentoInicial() != null && filtro.getDataMovimentoFinal() == null) {
				sql.append("and cota.id = movEstCota.cota.id ");
				sql.append("and movEstCota.data >= :dataInicialMovimento ");
			} else if(filtro.getDataMovimentoInicial() != null && filtro.getDataMovimentoFinal() != null) {
				sql.append("and cota.id = movEstCota.cota.id ");
				sql.append("and movEstCota.data between :dataInicialMovimento and :dataFinalMovimento ");
			} else {
				sql.append("and cota.id = movEstCota.cota.id ");
				sql.append("and movEstCota.data <= :dataFinalMovimento ");
			}
		}
		
		if(filtro.getTipoNFe() != null && Long.parseLong(filtro.getTipoNFe()) > -1) {
			sql.append("and nf.identificacao.tipoNotaFiscal.id = :tipoNotaFiscal ");			
		}
		
		//TODO: Sérgio - Acertar o id do box de roteirizacao
		if(filtro.getIdRoteiro() != null && filtro.getIdRoteiro() > -1) {
			sql.append("and roteiro.roteirizacao.id = cota.box.id ");
			sql.append("and roteiro.id = :idRoteiro ");
		}
		
		if(filtro.getIdRoteiro() != null && filtro.getIdRoteiro() > -1 
				&& filtro.getIdRota() != null && filtro.getIdRota() > -1) {
			sql.append("and rota.id = :idRota ");
		}
		
		if(filtro.getIdCotaInicial() != null || filtro.getIdCotaFinal() != null) {
			if(filtro.getIdCotaInicial() != null && filtro.getIdCotaFinal() == null) {
				sql.append("and cota.id >= :idCotaInicial ");
			} else if(filtro.getIdCotaInicial() != null && filtro.getIdCotaFinal() != null) {
				sql.append("and cota.id between :idCotaInicial and :idCotaFinal ");
			} else {
				sql.append("and cota.id <= :idCotaFinal ");
			}
		}
		
		if(filtro.getIdBoxInicial() != null || filtro.getIdBoxFinal() != null) {
			if(filtro.getIdBoxInicial() != null && filtro.getIdBoxFinal() == null) {
				sql.append("and cota.box.id >= :idBoxInicial ");
			} else if(filtro.getIdBoxInicial() != null && filtro.getIdBoxFinal() != null) {
				sql.append("and cota.box.id between :idBoxInicial and :idBoxFinal ");
			} else {
				sql.append("and cota.box.id <= :idBoxFinal ");
			}
		}
				
		sql.append("group by cota ");
		
		//Monta a ordenacao
		if (paginacao != null) {

			Map<String, String> columnToSort = new HashMap<String, String>();
			columnToSort.put("idCota", "cota.id");
			columnToSort.put("nomeCota", "cota.pessoa.nome");
			columnToSort.put("vlrTotal", "vlrTotal");
			columnToSort.put("vlrTotalDesconto", "vlrTotalDesconto");
			
			//Verifica a entrada para evitar expressoes SQL
			if (paginacao.getSortColumn() == null 
					|| paginacao.getSortColumn() != null 
					&& !paginacao.getSortColumn().matches("[a-zA-Z0-9\\._]*")) {
				paginacao.setSortColumn("idCota");
	        }
						
			sql.append("order by "+ columnToSort.get(paginacao.getSortColumn())+ " ");

			String orderByColumn = "";

			sql.append(orderByColumn);

			if (paginacao.getOrdenacao() != null) {
				sql.append(paginacao.getOrdenacao().toString());
			}

		}
		
		Query q = getSession().createQuery(sql.toString());
		
		q.setParameter("dataEmissao", new java.sql.Date(filtro.getDataEmissao().getTime()));
		
		if(filtro.getTipoNFe() != null && Long.parseLong(filtro.getTipoNFe()) > -1) {
			q.setParameter("tipoNotaFiscal", Long.parseLong(filtro.getTipoNFe()) );
		}
		
		if(filtro.getDataMovimentoInicial() != null || filtro.getDataMovimentoFinal() != null) {
			if(filtro.getDataMovimentoInicial() != null && filtro.getDataMovimentoFinal() == null) {
				q.setParameter("dataInicialMovimento", new java.sql.Date(filtro.getDataMovimentoInicial().getTime()));
			} else if(filtro.getDataMovimentoInicial() != null && filtro.getDataMovimentoFinal() != null) {
				q.setParameter("dataInicialMovimento", new java.sql.Date(filtro.getDataMovimentoInicial().getTime()));
				q.setParameter("dataFinalMovimento", new java.sql.Date(filtro.getDataMovimentoFinal().getTime()));
			} else {
				q.setParameter("dataFinalMovimento", new java.sql.Date(filtro.getDataMovimentoFinal().getTime()));
			}
		}
		
		if(filtro.getIdRoteiro() != null && filtro.getIdRoteiro() > -1) {
			q.setParameter("idRoteiro", filtro.getIdRoteiro() );
		}
		
		if(filtro.getIdRota() != null && filtro.getIdRota() > -1) {
			q.setParameter("idRota", filtro.getIdRota() );
		}
		
		if(filtro.getIdCotaInicial() != null || filtro.getIdCotaFinal() != null) {
			if(filtro.getIdCotaInicial() != null && filtro.getIdCotaFinal() == null) {
				q.setParameter("idCotaInicial", filtro.getIdCotaInicial());
			} else if(filtro.getIdCotaInicial() != null && filtro.getIdCotaFinal() != null) {
				q.setParameter("idCotaInicial", filtro.getIdCotaInicial());
				q.setParameter("idCotaFinal", filtro.getIdCotaFinal());
			} else {
				q.setParameter("idCotaFinal", filtro.getIdCotaFinal());
			}
		}
		
		if(filtro.getIdBoxInicial() != null || filtro.getIdBoxFinal() != null) {
			if(filtro.getIdBoxInicial() != null && filtro.getIdBoxFinal() == null) {
				q.setParameter("idBoxInicial", filtro.getIdBoxInicial());
			} else if(filtro.getIdBoxInicial() != null && filtro.getIdBoxFinal() != null) {
				q.setParameter("idBoxInicial", filtro.getIdBoxInicial());
				q.setParameter("idBoxFinal", filtro.getIdBoxFinal());
			} else {
				q.setParameter("idBoxFinal", filtro.getIdBoxFinal());
			}
		}
		
		if (paginacao != null && paginacao.getQtdResultadosPorPagina() != null) {
			q.setFirstResult( paginacao.getQtdResultadosPorPagina() * ( (paginacao.getPaginaAtual() - 1 )))
            .setMaxResults( paginacao.getQtdResultadosPorPagina() );
		}
		
		return q;
		
	}

}

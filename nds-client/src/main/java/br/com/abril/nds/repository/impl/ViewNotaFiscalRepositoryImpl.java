package br.com.abril.nds.repository.impl;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.NfeDTO;
import br.com.abril.nds.dto.filtro.FiltroMonitorNfeDTO;
import br.com.abril.nds.model.fiscal.ViewNotaFiscal;
import br.com.abril.nds.repository.ViewNotaFiscalRepository;
import br.com.abril.nds.vo.PaginacaoVO;

@Repository
public class ViewNotaFiscalRepositoryImpl extends AbstractRepository<ViewNotaFiscal, Integer> implements ViewNotaFiscalRepository {

	public ViewNotaFiscalRepositoryImpl() {		
		super(ViewNotaFiscal.class);		
	}
	

	public Integer obterQtdeRegistroNotaFiscal(FiltroMonitorNfeDTO filtro) {
		
		StringBuffer sql = new StringBuffer("");
		
		sql.append(" SELECT COUNT(*) FROM ( ");	
		
		sql.append(" SELECT VIEW_NOTA_FISCAL.NOTA_FISCAL_ID  ");
		
		sql.append(" FROM VIEW_NOTA_FISCAL ");
		
		sql.append(" INNER JOIN PESSOA ON  ");
		sql.append(" ( VIEW_NOTA_FISCAL.PESSOA_ID = PESSOA.ID )  ");
		
		if(filtro.getBox()!=null) {

			sql.append(" INNER JOIN COTA ON ");
			sql.append(" (COTA.ID = VIEW_NOTA_FISCAL.COTA_ID) ");
			
			sql.append(" INNER JOIN BOX ON ");
			sql.append(" (BOX.ID = COTA.BOX_ID) ");
			
		}
		
		sql.append(" WHERE ");
	
		sql.append(" VIEW_NOTA_FISCAL.EMITIDA = :emitida ");
		
		if(filtro.getBox()!=null && !filtro.getBox().isEmpty()) {
			sql.append(" AND BOX.CODIGO = :codigoBox ");
		}

		if(filtro.getDataInicial()!=null) {
			sql.append(" AND VIEW_NOTA_FISCAL.DATA_EMISSAO >= :dataInicial ");
		}
		
		if(filtro.getDataFinal()!=null) {
			sql.append(" AND VIEW_NOTA_FISCAL.DATA_EMISSAO <= :dataFinal ");
		}

		if(filtro.getDestinatario()!=null && !filtro.getDestinatario().isEmpty()) {
			
			if(filtro.isIndDocumentoCPF()) {
				sql.append(" AND PESSOA.CPF = :cpf ");
			} else {
				sql.append(" AND PESSOA.CNPJ = :cnpj ");
			}
			
		}

		if(filtro.getTipoNfe()!=null && !filtro.getTipoNfe().isEmpty()) {
			sql.append(" AND VIEW_NOTA_FISCAL.TIPO_EMISSAO_NFE = :tipoEmissaoNfe ");
		}
		
		if(filtro.getNumeroNotaInicial()!=null && !filtro.getNumeroNotaInicial().isEmpty()) {
			sql.append(" AND VIEW_NOTA_FISCAL.NUMERO >= :numeroInicial ");
		}
		
		if(filtro.getNumeroNotaFinal()!=null && !filtro.getNumeroNotaFinal().isEmpty()) {
			sql.append(" AND VIEW_NOTA_FISCAL.NUMERO <= :numeroFinal ");
		}
		
		if(filtro.getChaveAcesso()!=null && !filtro.getChaveAcesso().isEmpty()) {
			sql.append(" AND VIEW_NOTA_FISCAL.CHAVE_ACESSO = :chaveAcesso ");
		}

		if(filtro.getSituacaoNfe()!=null && !filtro.getSituacaoNfe().isEmpty()) {
			sql.append(" AND VIEW_NOTA_FISCAL.STATUS_EMISSAO_NFE = :situacaoNfe ");
		}
		
		sql.append(" ) AS REGISTROS_NF ");	
		
		SQLQuery sqlQuery = getSession().createSQLQuery(sql.toString());

		sqlQuery.setParameter("emitida", true);
		
		if(filtro.getBox()!=null && !filtro.getBox().isEmpty()) {
			sqlQuery.setParameter("codigoBox", filtro.getBox());
		}

		if(filtro.getDataInicial()!=null) {
			sqlQuery.setParameter("dataInicial", filtro.getDataInicial());
		}
		
		if(filtro.getDataFinal()!=null) {
			sqlQuery.setParameter("dataFinal", filtro.getDataFinal());
		}

		if(filtro.getDestinatario()!=null && !filtro.getDestinatario().isEmpty()) {
			if(filtro.isIndDocumentoCPF()) {
				sqlQuery.setParameter("cpf", filtro.getDestinatario());
			} else {
				sqlQuery.setParameter("cnpj", filtro.getDestinatario());
			}
		}

		if(filtro.getTipoNfe()!=null && !filtro.getTipoNfe().isEmpty()) {
			sqlQuery.setParameter("tipoEmissaoNfe", filtro.getTipoNfe());
		}
		
		if(filtro.getNumeroNotaInicial()!=null && !filtro.getNumeroNotaInicial().isEmpty()) {
			sqlQuery.setParameter("numeroInicial", filtro.getNumeroNotaInicial());
		}
		
		if(filtro.getNumeroNotaFinal()!=null && !filtro.getNumeroNotaFinal().isEmpty()) {
			sqlQuery.setParameter("numeroFinal", filtro.getNumeroNotaFinal());
		}
		
		if(filtro.getChaveAcesso()!=null && !filtro.getChaveAcesso().isEmpty()) {
			sqlQuery.setParameter("chaveAcesso", filtro.getChaveAcesso());
		}

		if(filtro.getSituacaoNfe()!=null && !filtro.getSituacaoNfe().isEmpty()) {
			sqlQuery.setParameter("situacaoNfe", filtro.getSituacaoNfe());
		}		
		
		BigInteger qtde = (BigInteger) sqlQuery.uniqueResult();
		
		return ((qtde == null) ? 0 : qtde.intValue());
		
	}

	
	@SuppressWarnings("unchecked")
	public List<NfeDTO> pesquisarNotaFiscal(FiltroMonitorNfeDTO filtro) {
		
		StringBuffer sql = new StringBuffer("");
		
		sql.append(" SELECT ");	

		sql.append(" VIEW_NOTA_FISCAL.NOTA_FISCAL_ID as idNotaFiscal, 	");
		sql.append(" VIEW_NOTA_FISCAL.NUMERO as numero, 				");
		sql.append(" VIEW_NOTA_FISCAL.SERIE as serie, 					"); 
		sql.append(" VIEW_NOTA_FISCAL.DATA_EMISSAO as emissao, 			");
		sql.append(" VIEW_NOTA_FISCAL.TIPO_EMISSAO_NFE as tipoEmissao, 	");
		
		sql.append(" CASE WHEN (VIEW_NOTA_FISCAL.NOTAS_DE = 'SAIDA' 	AND PESSOA.TIPO = 'J' ) THEN PESSOA.CNPJ ELSE NULL END AS  cnpjDestinatario, 									");
		sql.append(" CASE WHEN (VIEW_NOTA_FISCAL.NOTAS_DE = 'ENTRADA'   AND PESSOA.TIPO = 'J' ) THEN PESSOA.CNPJ ELSE NULL END AS  cnpjRemetente, 									");
		sql.append(" CASE WHEN (VIEW_NOTA_FISCAL.NOTAS_DE = 'ENTRADA'   AND PESSOA.TIPO = 'F' ) THEN PESSOA.CPF  ELSE NULL END AS  cpfRemetente, 									");
		
		sql.append(" VIEW_NOTA_FISCAL.STATUS_EMISSAO_NFE as statusNfe,  ");
		sql.append(" VIEW_NOTA_FISCAL.NOTAS_DE as tipoNfe, 				");
		sql.append(" VIEW_NOTA_FISCAL.MOVIMENTO_INTEGRACAO as movimentoIntegracao ");
		
		sql.append(" from VIEW_NOTA_FISCAL ");
		
		sql.append(" INNER JOIN PESSOA ON  ");
		sql.append(" ( VIEW_NOTA_FISCAL.PESSOA_ID = PESSOA.ID )  ");
		
		if(filtro.getBox()!=null && !filtro.getBox().isEmpty()) {

			sql.append(" INNER JOIN COTA ON ");
			sql.append(" (COTA.ID = VIEW_NOTA_FISCAL.COTA_ID) ");
			
			sql.append(" INNER JOIN BOX ON ");
			sql.append(" (BOX.ID = COTA.BOX_ID) ");
			
		}
		
		sql.append(" where ");

		sql.append(" VIEW_NOTA_FISCAL.EMITIDA = :emitida ");
		
		if(filtro.getBox()!=null && !filtro.getBox().isEmpty()) {
			sql.append(" AND BOX.CODIGO = :codigoBox ");
		}

		if(filtro.getDataInicial()!=null) {
			sql.append(" AND VIEW_NOTA_FISCAL.DATA_EMISSAO >= :dataInicial ");
		}
		
		if(filtro.getDataFinal()!=null) {
			sql.append(" AND VIEW_NOTA_FISCAL.DATA_EMISSAO <= :dataFinal ");
		}

		if(filtro.getDestinatario()!=null && !filtro.getDestinatario().isEmpty()) {
			
			if(filtro.isIndDocumentoCPF()) {
				sql.append(" AND PESSOA.CPF = :cpf ");
			} else {
				sql.append(" AND PESSOA.CNPJ = :cnpj ");
			}
			
		}

		if(filtro.getTipoNfe()!=null && !filtro.getTipoNfe().isEmpty()) {
			sql.append(" AND VIEW_NOTA_FISCAL.TIPO_EMISSAO_NFE = :tipoEmissaoNfe ");
		}
		
		if(filtro.getNumeroNotaInicial()!=null && !filtro.getNumeroNotaInicial().isEmpty()) {
			sql.append(" AND VIEW_NOTA_FISCAL.NUMERO >= :numeroInicial ");
		}
		
		if(filtro.getNumeroNotaFinal()!=null && !filtro.getNumeroNotaFinal().isEmpty()) {
			sql.append(" AND VIEW_NOTA_FISCAL.NUMERO <= :numeroFinal ");
		}
		
		if(filtro.getChaveAcesso()!=null && !filtro.getChaveAcesso().isEmpty()) {
			sql.append(" AND VIEW_NOTA_FISCAL.CHAVE_ACESSO = :chaveAcesso ");
		}

		if(filtro.getSituacaoNfe()!=null && !filtro.getSituacaoNfe().isEmpty()) {
			sql.append(" AND VIEW_NOTA_FISCAL.STATUS_EMISSAO_NFE = :situacaoNfe ");
		}
		
		PaginacaoVO paginacao = filtro.getPaginacao();
		
		if (filtro.getOrdenacaoColuna() != null) {

			sql.append(" order by ");
			
			String orderByColumn = "";
			
			switch (filtro.getOrdenacaoColuna()) {
				
					case NOTA:
						orderByColumn = " VIEW_NOTA_FISCAL.NUMERO ";
						break;
					case SERIE:
						orderByColumn = " VIEW_NOTA_FISCAL.SERIE ";
						break;
					case EMISSAO:
						orderByColumn = " VIEW_NOTA_FISCAL.DATA_EMISSAO ";
						break;
					case TIPO_EMISSAO:
						orderByColumn = " VIEW_NOTA_FISCAL.TIPO_EMISSAO_NFE ";
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
						orderByColumn = " VIEW_NOTA_FISCAL.STATUS_EMISSAO_NFE ";
						break;
					case TIPO_NFE:
						orderByColumn = " VIEW_NOTA_FISCAL.NOTAS_DE ";
						break;
					case MOVIMENTO_INTEGRACAO:
						orderByColumn = " VIEW_NOTA_FISCAL.MOVIMENTO_INTEGRACAO ";
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
		sqlQuery.addScalar("numero");
		sqlQuery.addScalar("serie");
		sqlQuery.addScalar("emissao");
		sqlQuery.addScalar("tipoEmissao");
		sqlQuery.addScalar("cnpjDestinatario");
		sqlQuery.addScalar("cnpjRemetente");
		sqlQuery.addScalar("cpfRemetente");
		sqlQuery.addScalar("statusNfe");
		sqlQuery.addScalar("tipoNfe");
		sqlQuery.addScalar("movimentoIntegracao");
		
		sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(NfeDTO.class));

		sqlQuery.setParameter("emitida", true);
		
		if(filtro.getBox()!=null && !filtro.getBox().isEmpty()) {
			sqlQuery.setParameter("codigoBox", filtro.getBox());
		}

		if(filtro.getDataInicial()!=null) {
			sqlQuery.setParameter("dataInicial", filtro.getDataInicial());
		}
		
		if(filtro.getDataFinal()!=null) {
			sqlQuery.setParameter("dataFinal", filtro.getDataFinal());
		}

		if(filtro.getDestinatario()!=null && !filtro.getDestinatario().isEmpty()) {
			if(filtro.isIndDocumentoCPF()) {
				sqlQuery.setParameter("cpf", filtro.getDestinatario());
			} else {
				sqlQuery.setParameter("cnpj", filtro.getDestinatario());
			}
		}

		if(filtro.getTipoNfe()!=null && !filtro.getTipoNfe().isEmpty()) {
			sqlQuery.setParameter("tipoEmissaoNfe", filtro.getTipoNfe());
		}
		
		if(filtro.getNumeroNotaInicial()!=null && !filtro.getNumeroNotaInicial().isEmpty()) {
			sqlQuery.setParameter("numeroInicial", filtro.getNumeroNotaInicial());
		}
		
		if(filtro.getNumeroNotaFinal()!=null && !filtro.getNumeroNotaFinal().isEmpty()) {
			sqlQuery.setParameter("numeroFinal", filtro.getNumeroNotaFinal());
		}
		
		if(filtro.getChaveAcesso()!=null && !filtro.getChaveAcesso().isEmpty()) {
			sqlQuery.setParameter("chaveAcesso", filtro.getChaveAcesso());
		}

		if(filtro.getSituacaoNfe()!=null && !filtro.getSituacaoNfe().isEmpty()) {
			sqlQuery.setParameter("situacaoNfe", filtro.getSituacaoNfe());
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

}

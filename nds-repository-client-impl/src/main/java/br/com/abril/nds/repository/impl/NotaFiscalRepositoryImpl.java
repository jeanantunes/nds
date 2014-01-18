package br.com.abril.nds.repository.impl;

import java.math.BigInteger;
import java.util.List;

import javax.sql.DataSource;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.NfeDTO;
import br.com.abril.nds.dto.RetornoNFEDTO;
import br.com.abril.nds.dto.filtro.FiltroMonitorNfeDTO;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.fiscal.nota.StatusProcessamentoInterno;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.NotaFiscalRepository;

@Repository
public class NotaFiscalRepositoryImpl extends AbstractRepositoryModel<NotaFiscal, Long> implements NotaFiscalRepository {

	@Autowired
	private DataSource dataSource;
	
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

		boolean isAnd = false;
		
		StringBuffer sql = new StringBuffer("");
		
		sql.append(" SELECT ")
		.append(" COUNT(notaFiscal.id) ")
		.append(" FROM NotaFiscal as notaFiscal");

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
		
		if(filtro.getDataInicial()!=null) {
			
			sql.append(" AND notaFiscal.identificacao.dataEmissao >= :dataInicial ");
		}

		if(filtro.getDataFinal()!=null) {
			sql.append(" notaFiscal.identificacao.dataEmissao <= :dataFinal ");
		}

		if(filtro.getDocumentoPessoa()!=null && !filtro.getDocumentoPessoa().isEmpty()) {
			sql.append(" NOTA_FISCAL_NOVO.DOCUMENTO_DESTINATARIO = :documento AND PESSOA_DESTINATARIO.TIPO = 'F' ");
		}

		if(filtro.getTipoNfe()!=null && !filtro.getTipoNfe().isEmpty()) {
			sql.append(" AND NOTA_FISCAL_PROCESSO.PROCESSO = :tipoEmissaoNfe");
		}

		if(filtro.getNumeroNotaInicial()!=null) {
			sql.append(" notaFiscal.identificacao.numeroDocumentoFiscal >= :numeroInicial ");
		}

		if(filtro.getNumeroNotaFinal()!=null) {
			sql.append(" notaFiscal.identificacao.numeroDocumentoFiscal <= :numeroFinal ");
		}

		if(filtro.getChaveAcesso()!=null && !filtro.getChaveAcesso().isEmpty()) {
			sql.append(" notaFiscal.informacaoEletronica.chaveAcesso = :chaveAcesso ");
		}

		if(filtro.getSituacaoNfe()!=null && !filtro.getSituacaoNfe().isEmpty()) {
			sql.append(" NOTA_FISCAL_NOVO.STATUS = :situacaoNfe ");
		}

		if(filtro.getSerie()!=null) {
			
			if(isAnd){
				sql.append(" AND ");
			}
			
			sql.append("notaFiscal.identificacao.serie = :serie ");
			
		}
		
		sql.append(" group by notaFiscal.id ");
		
		
		Query query = this.getSession().createQuery(sql.toString());
		
		query = createFiltroQuery(filtro, query);
		
		Long qtde = (Long) query.uniqueResult();

		return ((qtde == null) ? 0 : qtde.intValue());

	}	



	@SuppressWarnings("unchecked")
	public List<NfeDTO> pesquisarNotaFiscal(FiltroMonitorNfeDTO filtro) {

		boolean isAnd = false;
		
		StringBuffer sql = new StringBuffer("");
		
		sql.append(" SELECT ")
		.append(" notaFiscal.id as idNotaFiscal,")
		.append(" notaFiscal.identificacao.numeroDocumentoFiscal as numero,")
		.append(" notaFiscal.identificacao.serie as serie,")
		.append(" notaFiscal.identificacao.dataEmissao as emissao,")
		.append(" notaFiscal.identificacao.tipoEmissao as tipoEmissao,")
		.append(" notaFiscal.identificacaoEmitente.documento as cnpjDestinatario,")
		.append(" notaFiscal.statusProcessamentoInterno as statusNfe,")
		.append(" notaFiscal.identificacao.tipoNotaFiscal.descricao as tipoNfe,")
		.append(" notaFiscal.identificacao.tipoNotaFiscal.descricao as movimentoIntegracao")
		.append(" FROM NotaFiscal as notaFiscal");

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
		
		if(filtro.getDataInicial()!=null) {
			
			sql.append(" AND notaFiscal.identificacao.dataEmissao >= :dataInicial ");
		}

		if(filtro.getDataFinal()!=null) {
			sql.append(" notaFiscal.identificacao.dataEmissao <= :dataFinal ");
		}

		if(filtro.getDocumentoPessoa()!=null && !filtro.getDocumentoPessoa().isEmpty()) {
			sql.append(" NOTA_FISCAL_NOVO.DOCUMENTO_DESTINATARIO = :documento AND PESSOA_DESTINATARIO.TIPO = 'F' ");
		}

		if(filtro.getTipoNfe()!=null && !filtro.getTipoNfe().isEmpty()) {
			sql.append(" AND NOTA_FISCAL_PROCESSO.PROCESSO = :tipoEmissaoNfe");
		}

		if(filtro.getNumeroNotaInicial()!=null) {
			sql.append(" notaFiscal.identificacao.numeroDocumentoFiscal >= :numeroInicial ");
		}

		if(filtro.getNumeroNotaFinal()!=null) {
			sql.append(" notaFiscal.identificacao.numeroDocumentoFiscal <= :numeroFinal ");
		}

		if(filtro.getChaveAcesso()!=null && !filtro.getChaveAcesso().isEmpty()) {
			sql.append(" notaFiscal.informacaoEletronica.chaveAcesso = :chaveAcesso ");
		}

		if(filtro.getSituacaoNfe()!=null && !filtro.getSituacaoNfe().isEmpty()) {
			sql.append(" NOTA_FISCAL_NOVO.STATUS = :situacaoNfe ");
		}

		if(filtro.getSerie()!=null) {
			
			if(isAnd){
				sql.append(" AND ");
			}
			
			sql.append("notaFiscal.identificacao.serie = :serie ");
			
		}
		
		Query query = this.getSession().createQuery(sql.toString());
		
		query = createFiltroQuery(filtro, query);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(NfeDTO.class));
		
		return query.list();

	}

	private Query createFiltroQuery(FiltroMonitorNfeDTO filtro, Query query) {
		if(filtro.getBox()!=null) {
			query.setParameter("codigoBox", filtro.getBox());
		}

		if(filtro.getDataInicial()!=null) {
			query.setParameter("dataInicial", filtro.getDataInicial());
		}

		if(filtro.getDataFinal()!=null) {
			query.setParameter("dataFinal", filtro.getDataFinal());
		}

		if(filtro.getDocumentoPessoa()!=null && !filtro.getDocumentoPessoa().isEmpty()) {
			query.setParameter("documento", filtro.getDocumentoPessoa());
		}

		if(filtro.getTipoNfe()!=null && !filtro.getTipoNfe().isEmpty()) {
			query.setParameter("tipoEmissaoNfe", filtro.getTipoNfe());
		}

		if(filtro.getNumeroNotaInicial()!=null) {
			query.setParameter("numeroInicial", filtro.getNumeroNotaInicial());
		}

		if(filtro.getNumeroNotaFinal()!=null) {
			query.setParameter("numeroFinal", filtro.getNumeroNotaFinal());
		}

		if(filtro.getChaveAcesso()!=null && !filtro.getChaveAcesso().isEmpty()) {
			query.setParameter("chaveAcesso", filtro.getChaveAcesso());
		}

		if(filtro.getSituacaoNfe()!=null && !filtro.getSituacaoNfe().isEmpty()) {
			query.setParameter("situacaoNfe", filtro.getSituacaoNfe());
		}

		if(filtro.getSerie()!=null) {
			query.setParameter("serie", filtro.getSerie());
		}
		
		return query;
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
	public NotaFiscal buscarNotaFiscalNumeroSerie(RetornoNFEDTO dadosRetornoNFE) {
		
		StringBuffer sql = new StringBuffer("");

		sql.append(" SELECT notaFiscal");
		sql.append(" FROM NotaFiscal as notaFiscal");
		sql.append(" WHERE");
		

		if(dadosRetornoNFE.getIdNotaFiscal()!=null) {

			sql.append(" notaFiscal.id = :id ");

		}
		
		if(dadosRetornoNFE.getIdNotaFiscal()!=null) {

			sql.append(" AND notaFiscal.informacaoEletronica.chaveAcesso = :chave ");

		}
		
		Query query = this.getSession().createQuery(sql.toString());
		query.setParameter("id", dadosRetornoNFE.getIdNotaFiscal());
		query.setParameter("chave", dadosRetornoNFE.getChaveAcesso());
		
		query.setResultTransformer(new AliasToBeanResultTransformer(NotaFiscal.class));
		
		return (NotaFiscal) query.uniqueResult();
		
	}
}

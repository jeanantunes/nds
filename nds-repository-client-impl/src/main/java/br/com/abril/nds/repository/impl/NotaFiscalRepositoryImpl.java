package br.com.abril.nds.repository.impl;

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

		StringBuilder hql = new StringBuilder("");
		
		hql.append(" SELECT DISTINCT ")
		.append(" COUNT(notaFiscal.id) ");

		Query query = createFiltroQuery(queryConsultaPainelMonitor(filtro, hql, true, true, true),filtro);
		
		Long qtde = (Long) query.uniqueResult();

		return ((qtde == null) ? 0 : qtde.intValue());

	}	
	
	@SuppressWarnings("unchecked")
	public List<NfeDTO> pesquisarNotaFiscal(FiltroMonitorNfeDTO filtro) {
				
		StringBuilder hql = new StringBuilder("");
		
		hql.append(" SELECT ")
		.append(" notaFiscal.id as idNotaFiscal,")
		.append(" notaFiscal.notaFiscalInformacoes.identificacao.numeroDocumentoFiscal as numero,")
		.append(" notaFiscal.notaFiscalInformacoes.identificacao.serie as serie,")
		.append(" notaFiscal.notaFiscalInformacoes.identificacao.dataEmissao as emissao,")
		.append(" notaFiscal.notaFiscalInformacoes.identificacao.tipoEmissao as tipoEmissao,")
		.append(" notaFiscal.notaFiscalInformacoes.identificacaoEmitente.documento.documento as cnpjRemetente,")
		.append(" notaFiscal.notaFiscalInformacoes.identificacaoDestinatario.documento.documento as cnpjDestinatario, ")
		.append(" notaFiscal.notaFiscalInformacoes.statusProcessamentoInterno as statusNfe,")
		.append(" notaFiscal.notaFiscalInformacoes.identificacao.naturezaOperacao.descricao as tipoNfe,")
		.append(" notaFiscal.notaFiscalInformacoes.identificacao.naturezaOperacao.descricao as movimentoIntegracao");
		
		Query query = createFiltroQuery(queryConsultaPainelMonitor(filtro, hql, false, false, false),filtro);
		
		if(filtro.getPaginacao()!=null) {
			if(filtro.getPaginacao().getPosicaoInicial()!=null) {
				query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			}

			if(filtro.getPaginacao().getQtdResultadosPorPagina()!=null) {
				query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
			}
		}
		
		query.setResultTransformer(new AliasToBeanResultTransformer(NfeDTO.class));
		
		return query.list();

	}
	
	private StringBuilder queryConsultaPainelMonitor(FiltroMonitorNfeDTO filtro, StringBuilder hql, boolean isCount, boolean isPagination, boolean isGroup){
		
		hql.append(" FROM NotaFiscal as notaFiscal")
		.append(" JOIN notaFiscal.usuario as usuario");
		//.append(" LEFT JOIN notaFiscal.notaFiscalInformacoes.identificacaoEmitente.documento as docEmit")
		//.append(" LEFT JOIN notaFiscal.notaFiscalInformacoes.identificacaoDestinatario.documento as docDest");
		if(	(filtro.getBox()!=null) || filtro.getDataInicial() != null || filtro.getDataFinal() != null ||
				(filtro.getDocumentoPessoa() != null && !filtro.getDocumentoPessoa().isEmpty() ) || 
				(filtro.getTipoNfe() != null && !filtro.getTipoNfe().isEmpty() ) || 
				(filtro.getChaveAcesso() != null && !filtro.getChaveAcesso().isEmpty() ) ||
				(filtro.getSituacaoNfe() != null && !filtro.getSituacaoNfe().isEmpty() ) || 
				filtro.getNumeroNotaInicial() != null || filtro.getNumeroNotaFinal()!=null|| filtro.getSerie() != null ) {

				hql.append(" WHERE ");
		}
			
		if(filtro.getDataInicial() !=null) {
			
			hql.append(" notaFiscal.notaFiscalInformacoes.identificacao.dataEmissao >= :dataInicial ");
		}

		if(filtro.getDataFinal() !=null) {
			hql.append(" AND notaFiscal.notaFiscalInformacoes.identificacao.dataEmissao <= :dataFinal ");
		}

		if(filtro.getNumeroDocumento() != null){
			if(filtro.getDocumentoPessoa() !=null && !filtro.getDocumentoPessoa().isEmpty()) {
				if(filtro.getDocumentoPessoa().equalsIgnoreCase("cpf")){
					hql.append(" AND notaFiscal.notaFiscalInformacoes.identificacaoEmitente.documento.documento = :documento");
				}else{
					hql.append(" AND notaFiscal.notaFiscalInformacoes.identificacaoEmitente.documento.documento = :documento");
				}
			}
		}

		if(filtro.getTipoNfe() !=null && !filtro.getTipoNfe().isEmpty()) {
			hql.append(" AND notaFiscal.notaFiscalInformacoes.processos =:tipoEmissaoNfe");		}

		if(filtro.getNumeroNotaInicial() !=null) {
			hql.append(" AND notaFiscal.notaFiscalInformacoes.identificacao.numeroDocumentoFiscal >= :numeroInicial ");
		}

		if(filtro.getNumeroNotaFinal() !=null) {
			hql.append(" AND notaFiscal.notaFiscalInformacoes.identificacao.numeroDocumentoFiscal <= :numeroFinal ");
		}

		if(filtro.getChaveAcesso() !=null && !filtro.getChaveAcesso().isEmpty()) {
			hql.append(" AND notaFiscal.notaFiscalInformacoes.informacaoEletronica.chaveAcesso = :chaveAcesso ");
		}

		if(filtro.getSituacaoNfe() !=null && !filtro.getSituacaoNfe().isEmpty()) {
			hql.append(" AND NOTA_FISCAL_NOVO.STATUS = :situacaoNfe ");
		}

		if(filtro.getSerie() !=null) {
			hql.append(" AND notaFiscal.notaFiscalInformacoes.identificacao.serie = :serie ");
		}
		
		if(!isCount && !isPagination){
			if(filtro.getPaginacao()!=null && filtro.getPaginacao().getSortOrder() != null && filtro.getPaginacao().getSortColumn() != null) {
				hql.append(" ORDER BY  ").append(filtro.getPaginacao().getSortColumn()).append(" ").append(filtro.getPaginacao().getSortOrder());
			}
		}
			
		return hql;
	}
	
	private Query createFiltroQuery(StringBuilder hql, FiltroMonitorNfeDTO filtro) {
		
		Query query = this.getSession().createQuery(hql.toString());
		
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
			if(filtro.getNumeroDocumento() != null){
				if(filtro.getDocumentoPessoa().equalsIgnoreCase("cpf")){
					query.setParameter("documento", filtro.getNumeroDocumento().replaceAll("\\D", ""));				
				}else{
					query.setParameter("documento", filtro.getNumeroDocumento().replaceAll("\\D", ""));
				}
			}
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
		
		
		if(dadosRetornoNFE.getChaveAcesso()!=null) {

			sql.append(" notaFiscal.notaFiscalInformacoes.identificacao.numeroDocumentoFiscal = :numeroNotaFiscal ");

		}
		
		Query query = this.getSession().createQuery(sql.toString());
		query.setParameter("numeroNotaFiscal", dadosRetornoNFE.getNumeroNotaFiscal());
		
		return (NotaFiscal) query.uniqueResult();
		
	}
	
	@Override
	public NotaFiscal obterChaveAcesso(RetornoNFEDTO dadosRetornoNFE) {
		
		StringBuffer sql = new StringBuffer("");

		sql.append(" SELECT notaFiscal");
		sql.append(" FROM NotaFiscal as notaFiscal");
		sql.append(" WHERE");
		
		
		if(dadosRetornoNFE.getChaveAcesso()!=null) {

			sql.append(" notaFiscal.notaFiscalInformacoes.informacaoEletronica.chaveAcesso = :chaveAcesso ");

		}
		
		Query query = this.getSession().createQuery(sql.toString());
		query.setParameter("chaveAcesso", dadosRetornoNFE.getChaveAcesso());
		
		return (NotaFiscal) query.uniqueResult();
		
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<NotaFiscal> obterListaNotasFiscaisNumeroSerie(FiltroMonitorNfeDTO filtro) {

		StringBuffer sql = new StringBuffer("SELECT notaFiscal");
		sql.append(" FROM NotaFiscal as notaFiscal");
		sql.append(" WHERE");
		
		if(filtro.getSerie() !=null ) {
			sql.append(" notaFiscal.id = :serie ");
		}
		
		if(filtro.getNumeroNotaInicial() !=null) {
			sql.append(" AND notaFiscal.notaFiscalInformacoes.identificacao.numeroDocumentoFiscal = :numeroNotaFiscal ");
		}
		
		Query query = this.getSession().createQuery(sql.toString());
		query.setParameter("serie", filtro.getSerie());
		query.setParameter("numeroNotaFiscal", filtro.getNumeroNotaInicial());
		
		query.setResultTransformer(new AliasToBeanResultTransformer(NotaFiscal.class));
		
		return query.list();
	}
	
}

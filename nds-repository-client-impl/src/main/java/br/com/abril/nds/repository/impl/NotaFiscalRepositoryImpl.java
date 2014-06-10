package br.com.abril.nds.repository.impl;

import java.util.List;

import javax.sql.DataSource;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.CotaExemplaresDTO;
import br.com.abril.nds.dto.FornecedorExemplaresDTO;
import br.com.abril.nds.dto.ItemNotaFiscalPendenteDTO;
import br.com.abril.nds.dto.NfeDTO;
import br.com.abril.nds.dto.RetornoNFEDTO;
import br.com.abril.nds.dto.filtro.FiltroMonitorNfeDTO;
import br.com.abril.nds.dto.filtro.FiltroNFeDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.fiscal.MovimentoFechamentoFiscal;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.fiscal.nota.StatusProcessamento;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.NotaFiscalRepository;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

@Repository
public class NotaFiscalRepositoryImpl extends AbstractRepositoryModel<NotaFiscal, Long> implements NotaFiscalRepository {

	@Autowired
	private DataSource dataSource;

	public NotaFiscalRepositoryImpl() {
		super(NotaFiscal.class);
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.NotaFiscalRepository#obterListaNotasFiscaisPor(br.com.abril.nds.model.fiscal.nota.statusProcessamento)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<NotaFiscal> obterListaNotasFiscaisPor(StatusProcessamento statusProcessamento) {

		Criteria criteria = getSession().createCriteria(NotaFiscal.class);

		criteria.add(Restrictions.eq("statusProcessamento", statusProcessamento));

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
		.append(" ident.numeroDocumentoFiscal as numero,")
		.append(" ident.serie as serie,")
		.append(" ident.dataEmissao as emissao,")
		.append(" ident.tipoEmissao as tipoEmissao,")
		.append(" doc.documento as cnpjRemetente,")
		.append(" docDest.documento as cnpjDestinatario, ")
		.append(" nfi.statusProcessamento as statusNfe,")
		.append(" natOp.descricao as tipoNfe,")
		.append(" natOp.descricao as movimentoIntegracao");

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
		.append(" JOIN notaFiscal.notaFiscalInformacoes as nfi ")
		.append(" JOIN nfi.identificacao as ident ")
		.append(" JOIN nfi.identificacaoEmitente as identEmit ")
		.append(" JOIN nfi.identificacaoDestinatario as identDest")
		.append(" JOIN nfi.informacaoEletronica as infElet ")
		.append(" JOIN ident.naturezaOperacao as natOp ")
		.append(" LEFT JOIN natOp.processo as proc ")
		.append(" JOIN identEmit.documento as doc ")
		.append(" JOIN identDest.documento as docDest ");

		hql.append(" WHERE 1=1 ");

		if(filtro.getDataInicial() != null) {
			hql.append(" AND ident.dataEmissao >= :dataInicial ");
		}

		if(filtro.getDataFinal() != null) {
			hql.append(" AND ident.dataEmissao <= :dataFinal ");
		}

		if(filtro.getNumeroDocumento() != null){
			if(filtro.getDocumentoPessoa() != null && !filtro.getDocumentoPessoa().isEmpty()) {
				hql.append(" AND doc.documento = :documento");
			}
		}

		if(filtro.getTipoNfe() != null && !filtro.getTipoNfe().isEmpty()) {
			hql.append(" AND natOp.id = :tipoEmissaoNfe");		
		}

		if(filtro.getNumeroNotaInicial() != null) {
			hql.append(" AND ident.numeroDocumentoFiscal >= :numeroInicial ");
		}

		if(filtro.getNumeroNotaFinal() != null) {
			hql.append(" AND ident.numeroDocumentoFiscal <= :numeroFinal ");
		}

		if(filtro.getChaveAcesso() != null && !filtro.getChaveAcesso().isEmpty()) {
			hql.append(" AND infElet.chaveAcesso = :chaveAcesso ");
		}

		if(filtro.getSituacaoNfe() != null && !filtro.getSituacaoNfe().isEmpty()) {
			hql.append(" AND nfi.statusProcessamento = :situacaoNfe ");
		}

		if(filtro.getSerie() != null) {
			hql.append(" AND ident.serie = :serie ");
		}

		if(!isCount && !isPagination){
			if(filtro.getPaginacao() !=null && filtro.getPaginacao().getSortOrder() != null && filtro.getPaginacao().getSortColumn() != null) {
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
			query.setParameter("tipoEmissaoNfe", Long.valueOf(filtro.getTipoNfe()));
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



	@Override
	@SuppressWarnings("unchecked")
	public List<CotaExemplaresDTO> consultaCotaExemplaresMECSumarizados(FiltroNFeDTO filtro) {

		// OBTER COTA EXEMPLARES SUMARIZADOS
		StringBuilder hql = new StringBuilder("SELECT ");
		hql.append(" mec.cota.id as idCota, ");
		hql.append(" mec.cota.numeroCota as numeroCota, ");
		hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome,'') as nomeCota,");
		hql.append(" SUM(mec.qtde) as exemplares, ");
		hql.append(" SUM(mec.valoresAplicados.precoVenda * mec.qtde) as total, "); 
		hql.append(" SUM(mec.valoresAplicados.precoComDesconto * mec.qtde) as totalDesconto, "); 	
		hql.append(" mec.cota.situacaoCadastro as situacaoCadastro, ");
		hql.append(" cota.parametrosCotaNotaFiscalEletronica.exigeNotaFiscalEletronica as exigeNotaFiscalEletronica, ");
		hql.append(" cota.parametrosCotaNotaFiscalEletronica.contribuinteICMS as contribuinteICMS ");

		Query query = queryConsultaMECNfeParameters(queryConsultaMECNfe(filtro, hql, false, false, false), filtro);

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
	public Long consultaCotaExemplaresMECSumarizadosQtd(FiltroNFeDTO filtro) {

		// OBTER COTA EXEMPLARES SUMARIZADOS
		StringBuilder hql = new StringBuilder("SELECT ");
		hql.append(" COUNT(mec.cota.id) ");
		Query query = queryConsultaMECNfeParameters(queryConsultaMECNfe(filtro, hql, true, true, false), filtro);

		return (long) query.list().size();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<CotaExemplaresDTO> consultaCotaExemplaresMFFSumarizados(FiltroNFeDTO filtro) {

		// OBTER COTA EXEMPLARES SUMARIZADOS
		StringBuilder hql = new StringBuilder("SELECT ");
		hql.append(" mffc.cota.id as idCota, ");
		hql.append(" mffc.cota.numeroCota as numeroCota, ");
		hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome,'') as nomeCota,");
		hql.append(" SUM(mffc.qtde) as exemplares, ");
		hql.append(" SUM(mffc.valoresAplicados.precoVenda * mffc.qtde) as total, "); 
		hql.append(" SUM(mffc.valoresAplicados.precoComDesconto * mffc.qtde) as totalDesconto, "); 	
		hql.append(" mffc.cota.situacaoCadastro as situacaoCadastro, ");
		hql.append(" cota.parametrosCotaNotaFiscalEletronica.exigeNotaFiscalEletronica as exigeNotaFiscalEletronica, ");
		hql.append(" cota.parametrosCotaNotaFiscalEletronica.contribuinteICMS as contribuinteICMS ");

		Query query = queryConsultaCotaMFFNfeParameters(queryConsultaCotaMFFNfe(filtro, hql, false, false, false), filtro);

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
	public Long consultaCotaExemplaresMFFSumarizadosQtd(FiltroNFeDTO filtro) {

		// OBTER COTA EXEMPLARES SUMARIZADOS
		StringBuilder hql = new StringBuilder("SELECT ");
		hql.append(" COUNT(mffc.cota.id) ");
		Query query = queryConsultaCotaMFFNfeParameters(queryConsultaCotaMFFNfe(filtro, hql, true, true, false), filtro);

		return (long) query.list().size();
	}

	/**
	 * Obter conjunto de cotas
	 * @param FiltroNFeDTO
	 * @return List ids
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Cota> obterConjuntoCotasNotafiscal(FiltroNFeDTO filtro) {

		// OBTER TODAS AS COTAS DA TELA
		StringBuilder hql = new StringBuilder("SELECT ");
		hql.append(" mec.cota ");
		Query query = queryConsultaMECNfeParameters(queryConsultaMECNfe(filtro, hql, false, false, false), filtro);

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
	public List<MovimentoEstoqueCota> obterMovimentosEstoqueCota(FiltroNFeDTO filtro) {

		// ITENS DA NOTA FISCAL
		StringBuilder hql = new StringBuilder("SELECT mec");
		Query query = queryConsultaMECNfeParameters(queryConsultaMECNfe(filtro, hql, false, false, true), filtro);

		return query.list();
	}

	private StringBuilder queryConsultaMECNfe(FiltroNFeDTO filtro, StringBuilder hql, boolean isCount, boolean isPagination, boolean isGroup) {

		hql.append(" FROM MovimentoEstoqueCota mec ")
		.append(" JOIN mec.tipoMovimento tipoMovimento ")
		.append(" JOIN mec.lancamento lancamento ")
		.append(" JOIN mec.cota cota ")
		.append(" JOIN cota.pessoa pessoa ")
		.append(" LEFT JOIN cota.box box ")
		.append(" LEFT JOIN box.roteirizacao roteirizacao ")
		.append(" LEFT JOIN roteirizacao.roteiros roteiro ");

		if(filtro.getIdRota()!= null){	
			hql.append(" LEFT JOIN roteiro.rotas rota ");
		}

		hql.append(" JOIN mec.produtoEdicao produtoEdicao")
		.append(" JOIN produtoEdicao.produto produto ")
		.append(" JOIN produto.fornecedores fornecedor")
		.append(" WHERE mec.data BETWEEN :dataInicial AND :dataFinal ")
		.append(" AND mec.movimentoEstoqueCotaEstorno is null ")
		.append(" AND mec.movimentoEstoqueCotaFuro is null ")
		.append(" AND mec.notaFiscalEmitida = false ");

		// Tipo de Nota:		
		if(filtro.getIdNaturezaOperacao() != null) {
			hql.append(" AND mec.tipoMovimento.id in (SELECT tm.id ");
			hql.append("FROM NaturezaOperacao no ");
			hql.append("JOIN no.tipoMovimento tm ");
			hql.append("WHERE no.id in(:idNaturezaOperacao)) ");
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
		} else {
			hql.append(" GROUP BY mec ");
		}

		if(!isCount && !isPagination){
			if(filtro.getPaginacaoVO()!=null && filtro.getPaginacaoVO().getSortOrder() != null && filtro.getPaginacaoVO().getSortColumn() != null) {
				hql.append(" ORDER BY  ").append(filtro.getPaginacaoVO().getSortColumn()).append(" ").append(filtro.getPaginacaoVO().getSortOrder());
			}
		}

		return hql;
	}

	private Query queryConsultaMECNfeParameters(StringBuilder hql, FiltroNFeDTO filtro) {

		// Realizar a consulta e converter ao objeto cota exemplares.
		Query query = this.getSession().createQuery(hql.toString());		

		// Data Movimento:	...  Até   ...
		if (filtro.getDataInicial() != null && filtro.getDataFinal() != null) {
			query.setParameter("dataInicial", filtro.getDataInicial());
			query.setParameter("dataFinal", filtro.getDataFinal());
		}

		// tipo da nota fiscal		
		if(filtro.getIdNaturezaOperacao() != null && filtro.getIdNaturezaOperacao().longValue() > 0) {
			query.setParameter("idNaturezaOperacao", filtro.getIdNaturezaOperacao());
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

		return query;	
	}

	@Override
	@SuppressWarnings("unchecked")	
	public List<EstoqueProduto> obterConjuntoFornecedorNotafiscal(FiltroNFeDTO filtro) {

		StringBuilder hql = new StringBuilder("select ")
		.append(" estoqueProduto")
		.append(" from EstoqueProduto estoqueProduto ")
		.append(" JOIN estoqueProduto.produtoEdicao produtoEdicao")
		.append(" JOIN produtoEdicao.produto produto ")
		.append(" JOIN produto.fornecedores fornecedor ")
		.append(" JOIN fornecedor.juridica pj ")
		.append(" where fornecedor.id in (:idFornecedor) ");

		hql.append("GROUP BY fornecedor.id");

		Query query = this.getSession().createQuery(hql.toString());
		query.setParameterList("idFornecedor", filtro.getListIdFornecedor());

		return query.list();
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<FornecedorExemplaresDTO> consultaFornecedorExemplarSumarizado(FiltroNFeDTO filtro) {

		StringBuilder hql = new StringBuilder("select ")
		.append(" fornecedor.id AS idFornecedor, ")
		.append(" fornecedor.id as numeroFornecedor, ")
		.append(" fornecedor.codigoInterface as numeroFornecedor, ")
		.append(" pj.razaoSocial as nomeFornecedor, ")
		.append(" SUM(estoqueProduto.qtdeDevolucaoEncalhe) as exemplares, ")
		.append(" SUM(estoqueProduto.qtde) as total, ")
		.append(" SUM(estoqueProduto.qtde) as totalDesconto ")
		.append(" from EstoqueProduto as estoqueProduto ")
		.append(" JOIN estoqueProduto.produtoEdicao as produtoEdicao")
		.append(" JOIN produtoEdicao.produto as produto ")
		.append(" JOIN produto.fornecedores as fornecedor ")
		.append(" JOIN fornecedor.juridica as pj ")
		.append(" where fornecedor.id in (:idFornecedor) ");

		hql.append("GROUP BY fornecedor.id");

		Query query = this.getSession().createQuery(hql.toString());
		query.setParameterList("idFornecedor", filtro.getListIdFornecedor());

		query.setResultTransformer(new AliasToBeanResultTransformer(FornecedorExemplaresDTO.class));

		return query.list();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<EstoqueProduto> obterEstoques(FiltroNFeDTO filtro) {
		StringBuilder hql = new StringBuilder("select estoqueProduto ");

		Query query =  queryConsultaNfeEstoqueParameters(queryConsultaNfeEstoque(filtro, hql, true, true, true), filtro);

		hql.append("GROUP BY fornecedor.id");

		return query.list();
	}


	@Override
	public Long consultaFornecedorExemplaresSumarizadosQtd(FiltroNFeDTO filtro) {

		// OBTER COTA EXEMPLARES SUMARIZADOS
		StringBuilder hql = new StringBuilder("SELECT ");
		hql.append(" COUNT(fornecedor.id) ");
		Query query = queryConsultaNfeEstoqueParameters(queryConsultaNfeEstoque(filtro, hql, true, true, false), filtro);

		return (long) query.list().size();
	}

	public Query queryConsultaNfeEstoqueParameters(StringBuilder hql, FiltroNFeDTO filtro) {

		Query query = this.getSession().createQuery(hql.toString());		

		query.setParameterList("idFornecedor", filtro.getListIdFornecedor());

		return query;
	}

	public StringBuilder queryConsultaNfeEstoque(FiltroNFeDTO filtro, StringBuilder hql, boolean isCount, boolean isPagination, boolean isGroup){

		hql.append(" from EstoqueProduto as estoqueProduto ")
		.append(" JOIN estoqueProduto.produtoEdicao as produtoEdicao")
		.append(" JOIN produtoEdicao.produto as produto ")
		.append(" JOIN produto.fornecedores as fornecedor ")
		.append(" JOIN fornecedor.juridica pj ")
		.append(" where fornecedor.id in (:idFornecedor) ");

		return hql;
	}

	private StringBuilder queryConsultaCotaMFFNfe(FiltroNFeDTO filtro, StringBuilder hql, boolean isCount, boolean isPagination, boolean isGroup) {

		hql.append(" FROM MovimentoFechamentoFiscalCota mffc ")
		.append(" JOIN mffc.tipoMovimento tipoMovimento ")
		.append(" JOIN mffc.chamadaEncalheCota chamadaEncalheCota ")
		.append(" JOIN chamadaEncalheCota.chamadaEncalhe chamadaEncalhe ")
		.append(" JOIN chamadaEncalhe.lancamentos lancamentos ")
		.append(" JOIN mffc.cota cota ")
		.append(" JOIN cota.pessoa pessoa ")
		.append(" LEFT JOIN cota.box box ")
		.append(" LEFT JOIN box.roteirizacao roteirizacao ")
		.append(" LEFT JOIN roteirizacao.roteiros roteiro ");

		if(filtro.getIdRota() != null) {	
		//	hql.append(" LEFT JOIN roteiro.rotas rota ");
		}

		hql.append(" JOIN mffc.produtoEdicao produtoEdicao")
		.append(" JOIN produtoEdicao.produto produto ")
		.append(" JOIN produto.fornecedores fornecedor")
		.append(" WHERE mffc.data BETWEEN :dataInicial AND :dataFinal ")
		.append(" AND mffc.notaFiscalLiberadaEmissao = :true ")
		.append(" AND mffc.notaFiscalVendaEmitida = :false ")
		.append(" AND mffc.notaFiscalDevolucaoSimbolicaEmitida = :false ");

		// Tipo de Nota:		
		if(filtro.getIdNaturezaOperacao() != null) {
			hql.append(" AND mffc.tipoMovimento.id in (SELECT tm.id ");
			hql.append("FROM NaturezaOperacao no ");
			hql.append("JOIN no.tipoMovimento tm ");
			hql.append("WHERE no.id in(:idNaturezaOperacao)) ");
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
			hql.append(" GROUP BY mffc.cota.numeroCota ");
		} else {
			hql.append(" GROUP BY mffc ");
		}

		if(!isCount && !isPagination){
			if(filtro.getPaginacaoVO()!=null && filtro.getPaginacaoVO().getSortOrder() != null && filtro.getPaginacaoVO().getSortColumn() != null) {
				hql.append(" ORDER BY  ").append(filtro.getPaginacaoVO().getSortColumn()).append(" ").append(filtro.getPaginacaoVO().getSortOrder());
			}
		}

		return hql;
	}

	public Query queryConsultaCotaMFFNfeParameters(StringBuilder hql, FiltroNFeDTO filtro) {


		// Realizar a consulta e converter ao objeto cota exemplares.
		Query query = this.getSession().createQuery(hql.toString());		

		query.setParameter("true", true);
		
		query.setParameter("false", false);
		
		// Data Movimento:	...  Até   ...
		if (filtro.getDataInicial() != null && filtro.getDataFinal() != null) {
			query.setParameter("dataInicial", filtro.getDataInicial());
			query.setParameter("dataFinal", filtro.getDataFinal());
		}

		// tipo da nota fiscal		
		if(filtro.getIdNaturezaOperacao() != null && filtro.getIdNaturezaOperacao().longValue() > 0) {
			query.setParameter("idNaturezaOperacao", filtro.getIdNaturezaOperacao());
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

	@Override
	@SuppressWarnings("unchecked")
	public List<FornecedorExemplaresDTO> consultaFornecedorExemplaresMFFSumarizados(FiltroNFeDTO filtro) {

		// OBTER COTA EXEMPLARES SUMARIZADOS
		StringBuilder hql = new StringBuilder("SELECT ");
		hql.append(" mfff.fornecedor.id as idFornecedor, ");
		hql.append(" mfff.fornecedor.id as numeroFornecedor, ");
		hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, '') as nomeFornecedor,");
		hql.append(" SUM(mfff.qtde) as exemplares ");

		Query query = queryConsultaFornecedorMFFNfeParameters(queryConsultaFornecedorMFFNfe(filtro, hql, false, false, false), filtro);

		if(filtro.getPaginacaoVO()!=null) {
			if(filtro.getPaginacaoVO().getPosicaoInicial()!=null) {
				query.setFirstResult(filtro.getPaginacaoVO().getPosicaoInicial());
			}

			if(filtro.getPaginacaoVO().getQtdResultadosPorPagina()!=null) {
				query.setMaxResults(filtro.getPaginacaoVO().getQtdResultadosPorPagina());
			}
		}

		query.setResultTransformer(new AliasToBeanResultTransformer(FornecedorExemplaresDTO.class));

		return query.list();
	}

	@Override
	public Long consultaFornecedorExemplaresMFFSumarizadosQtd(FiltroNFeDTO filtro) {

		// OBTER COTA EXEMPLARES SUMARIZADOS
		StringBuilder hql = new StringBuilder("SELECT ");
		hql.append(" COUNT(mfff.fornecedor.id) ");
		Query query = queryConsultaFornecedorMFFNfeParameters(queryConsultaFornecedorMFFNfe(filtro, hql, true, true, false), filtro);

		return (long) query.list().size();
	}
	
	/**
	 * Obter os itens da nota com base nos movimentos de fechamento fiscal
	 * 
	 * @param filtro
	 * @return
	 */
	@Override
	public List<MovimentoFechamentoFiscal> obterMovimentosFechamentosFiscaisFornecedor(FiltroNFeDTO filtro) {
		
		// ITENS DA NOTA FISCAL
		StringBuilder hql = new StringBuilder("SELECT mfff");
		Query query = queryConsultaFornecedorMFFNfeParameters(queryConsultaFornecedorMFFNfe(filtro, hql, false, false, true), filtro);

		return query.list();
	}
	
	private StringBuilder queryConsultaFornecedorMFFNfe(FiltroNFeDTO filtro, StringBuilder hql, boolean isCount, boolean isPagination, boolean isGroup) {

		hql.append(" FROM MovimentoFechamentoFiscalFornecedor mfff ")
		.append(" JOIN mfff.tipoMovimento tipoMovimento ")
		.append(" JOIN mfff.fornecedor fornecedor ")
		.append(" JOIN fornecedor.juridica pessoa ");

		hql.append(" JOIN mfff.produtoEdicao produtoEdicao")
		.append(" JOIN produtoEdicao.produto produto ")
		.append(" JOIN produto.fornecedores fornecedor")
		.append(" WHERE mfff.data BETWEEN :dataInicial AND :dataFinal ")
		.append(" AND mfff.notaFiscalLiberadaEmissao = :true ")
		.append(" AND mfff.notaFiscalDevolucaoSimbolicaEmitida = :false ");

		// Tipo de Nota:		
		if(filtro.getIdNaturezaOperacao() != null) {
			hql.append(" AND mfff.tipoMovimento.id in (SELECT tm.id ");
			hql.append("FROM NaturezaOperacao no ");
			hql.append("JOIN no.tipoMovimento tm ");
			hql.append("WHERE no.id in(:idNaturezaOperacao)) ");
		}

		// Data Emissão:	...		
		if(filtro.getDataEmissao() != null) {
			hql.append(" ");
		}

		if(filtro.getListIdFornecedor() != null) {
			hql.append(" AND fornecedor.id in (:fornecedor) ");
		}

		if(!isGroup){
			hql.append(" GROUP BY mfff.fornecedor.id ");
		} else {
			hql.append(" GROUP BY mfff ");
		}

		if(!isCount && !isPagination){
			if(filtro.getPaginacaoVO()!=null && filtro.getPaginacaoVO().getSortOrder() != null && filtro.getPaginacaoVO().getSortColumn() != null) {
				hql.append(" ORDER BY  ").append(filtro.getPaginacaoVO().getSortColumn()).append(" ").append(filtro.getPaginacaoVO().getSortOrder());
			}
		}

		return hql;
	}

	public Query queryConsultaFornecedorMFFNfeParameters(StringBuilder hql, FiltroNFeDTO filtro) {

		// Realizar a consulta e converter ao objeto cota exemplares.
		Query query = this.getSession().createQuery(hql.toString());		

		query.setParameter("true", true);
		
		query.setParameter("false", false);
		
		// Data Movimento:	...  Até   ...
		if (filtro.getDataInicial() != null && filtro.getDataFinal() != null) {
			query.setParameter("dataInicial", filtro.getDataInicial());
			query.setParameter("dataFinal", filtro.getDataFinal());
		}

		// tipo da nota fiscal		
		if(filtro.getIdNaturezaOperacao() != null && filtro.getIdNaturezaOperacao().longValue() > 0) {
			query.setParameter("idNaturezaOperacao", filtro.getIdNaturezaOperacao());
		}

		// forncedor id		
		if(filtro.getListIdFornecedor() !=null && !filtro.getListIdFornecedor().isEmpty()) {
			query.setParameterList("fornecedor", filtro.getListIdFornecedor());
		}
		// Data Emissão:	...		
		/*if(filtro.getDataEmissao() != null) {

		}*/

		if(filtro.getIntervalorCotaInicial() != null && filtro.getIntervalorCotaFinal() != null) {
			query.setParameter("numeroCotaInicial", filtro.getIntervalorCotaInicial());
			query.setParameter("numeroCotaFinal", filtro.getIntervalorCotaFinal());
		}

		if(filtro.getListIdFornecedor() != null) {
			query.setParameterList("fornecedor", filtro.getListIdFornecedor());
		}

		return query;	
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FornecedorExemplaresDTO> consultaFornecedorExemplaresMESumarizados(FiltroNFeDTO filtro) {
		
		// OBTER FORNECEDOR EXEMPLARES SUMARIZADOS
		StringBuilder hql = new StringBuilder("SELECT ");
		hql.append(" fornecedor.id as idFornecedor, ");
		hql.append(" fornecedor.id as numeroFornecedor, ");
		hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, '') as nomeFornecedor,");
		hql.append(" SUM(me.qtde) as exemplares, ");
		hql.append(" SUM(coalesce(produtoEdicao.precoVenda, 0) * me.qtde) as total, "); 
		hql.append(" SUM(coalesce(descontoLogisticaPE.percentualDesconto, descontoLogistica.percentualDesconto, produtoEdicao.desconto, produto.desconto, 0) * me.qtde) as totalDesconto "); 
		
		Query query = queryConsultaMENfeParameters(queryConsultaMENfe(filtro, hql, false, false, false), filtro);
		
		if(filtro.getPaginacaoVO() != null) {
			if(filtro.getPaginacaoVO().getPosicaoInicial() != null) {
				query.setFirstResult(filtro.getPaginacaoVO().getPosicaoInicial());
			}
			
			if(filtro.getPaginacaoVO().getQtdResultadosPorPagina() != null) {
				query.setMaxResults(filtro.getPaginacaoVO().getQtdResultadosPorPagina());
			}
		}
		
		query.setResultTransformer(new AliasToBeanResultTransformer(FornecedorExemplaresDTO.class));
		
		return query.list();
		
	}
	

	@Override
	public Long consultaFornecedorExemplaresMESumarizadosQtd(FiltroNFeDTO filtro) {

		// OBTER COTA EXEMPLARES SUMARIZADOS
		StringBuilder hql = new StringBuilder("SELECT ");
		hql.append(" COUNT(distinct fornecedor.id) ");
		Query query = queryConsultaMENfeParameters(queryConsultaMENfe(filtro, hql, true, true, false), filtro);

		return (long) query.list().size();
	}
	
	private StringBuilder queryConsultaMENfe(FiltroNFeDTO filtro, StringBuilder hql, boolean isCount, boolean isPagination, boolean isGroup) {

		hql.append(" FROM MovimentoEstoque me ")
		.append(" JOIN me.tipoMovimento tipoMovimento ")
		.append(" JOIN me.estoqueProduto estoqueProduto ")
		.append(" JOIN estoqueProduto.produtoEdicao produtoEdicao ")
		.append(" JOIN produtoEdicao.produto produto ")
		.append(" LEFT JOIN produtoEdicao.descontoLogistica descontoLogisticaPE ")
		.append(" LEFT JOIN produto.descontoLogistica descontoLogistica ")
		.append(" JOIN produto.fornecedores fornecedor")
		.append(" JOIN fornecedor.juridica pessoa ")
		.append(" WHERE me.data BETWEEN :dataInicial AND :dataFinal ");

		// Tipo de Nota:		
		if(filtro.getIdNaturezaOperacao() != null) {
			hql.append(" AND me.tipoMovimento.id in (SELECT tm.id ");
			hql.append("FROM NaturezaOperacao no ");
			hql.append("JOIN no.tipoMovimento tm ");
			hql.append("WHERE no.id in(:idNaturezaOperacao)) ");
		}

		// Data Emissão:	...		
		if(filtro.getDataEmissao() != null) {
			hql.append(" ");
		}

		if(filtro.getListIdFornecedor() != null) {
			hql.append(" AND fornecedor.id in (:fornecedor) ");
		}

		if(!isGroup){
			hql.append(" GROUP BY fornecedor.id ");
		} else {
			hql.append(" GROUP BY me ");
		}

		if(!isCount && !isPagination){
			if(filtro.getPaginacaoVO()!=null && filtro.getPaginacaoVO().getSortOrder() != null && filtro.getPaginacaoVO().getSortColumn() != null) {
				hql.append(" ORDER BY  ").append(filtro.getPaginacaoVO().getSortColumn()).append(" ").append(filtro.getPaginacaoVO().getSortOrder());
			}
		}

		return hql;
	}
	
	private Query queryConsultaMENfeParameters(StringBuilder hql, FiltroNFeDTO filtro) {

		// Realizar a consulta e converter ao objeto cota exemplares.
		Query query = this.getSession().createQuery(hql.toString());		

		// Data Movimento:	...  Até   ...
		if (filtro.getDataInicial() != null && filtro.getDataFinal() != null) {
			query.setParameter("dataInicial", filtro.getDataInicial());
			query.setParameter("dataFinal", filtro.getDataFinal());
		}

		// tipo da Natureza de Operacao
		if(filtro.getIdNaturezaOperacao() != null && filtro.getIdNaturezaOperacao().longValue() > 0) {
			query.setParameter("idNaturezaOperacao", filtro.getIdNaturezaOperacao());
		}

		// forncedor id		
		if(filtro.getListIdFornecedor() !=null && !filtro.getListIdFornecedor().isEmpty()) {
			query.setParameterList("fornecedor", filtro.getListIdFornecedor());
		}

		return query;	
	}

	@Override
	public List<MovimentoEstoque> obterMovimentosEstoqueFornecedor(FiltroNFeDTO filtro) {
		
		// OBTER COTA EXEMPLARES SUMARIZADOS
		StringBuilder hql = new StringBuilder("SELECT me ");
		Query query = queryConsultaMENfeParameters(queryConsultaMENfe(filtro, hql, false, false, false), filtro);

		return query.list();
		
	}

	@Override
	public List<Fornecedor> obterConjuntoFornecedoresNotafiscal(FiltroNFeDTO filtro) {
		
		StringBuilder hql = new StringBuilder("SELECT ");
		hql.append(" distinct fornecedor ");
		Query query = queryConsultaMENfeParameters(queryConsultaMENfe(filtro, hql, false, false, false), filtro);

		return query.list();
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ItemNotaFiscalPendenteDTO> buscarItensPorNota(Long idConferenciaCota, String  orderBy,Ordenacao ordenacao, Integer firstResult, Integer maxResults) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT produto.codigo as codigoProduto, ");
		hql.append("produto.nome as nomeProduto, ");		
		hql.append("produtoEdicao.numeroEdicao as numeroEdicao, ");
		hql.append("conf.qtdeInformada as qtdInformada, ");
		hql.append("conf.qtde as qtdRecebida, ");
		hql.append("conf.precoCapaInformado as precoCapa, ");
		hql.append(" (item.desconto) as desconto, ");
		hql.append(" (conf.precoCapaInformado - (conf.precoCapaInformado * (item.desconto) / 100)) AS precoDesconto, ");
		hql.append(" (conf.precoCapaInformado - (conf.precoCapaInformado * (item.desconto) / 100) * conf.qtdeInformada) AS totalDoItem, ");
		hql.append("conf.data as dataConferenciaEncalhe, ");
		hql.append("chamadaEncalhe.dataRecolhimento as dataChamadaEncalhe ");
		
		hql.append(getHqlFromEWhereItensPendentes());
		
		Query query =  getSession().createQuery(hql.toString());
		
		query.setParameter("idConferenciaCota", idConferenciaCota);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ItemNotaFiscalPendenteDTO.class));
		
		if(firstResult != null) {
			query.setFirstResult(firstResult);
		}
		
		if(maxResults != null) { 
			query.setMaxResults(maxResults);			
		}
		
		return query.list();		 
		
	}
	
private String getHqlFromEWhereItensPendentes() {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" from ItemNotaFiscalEntrada as item ");
		hql.append(" JOIN item.notaFiscal as nf ");
		hql.append(" JOIN nf.controleConferenciaEncalheCota as confCota ");
		hql.append(" JOIN item.produtoEdicao as produtoEdicao ");
		hql.append(" LEFT JOIN produtoEdicao.produto as produto ");
		hql.append(" LEFT JOIN produto.fornecedores as fornecedores ");
		hql.append(" left join confCota.conferenciasEncalhe as conf  ");
		hql.append(" left join conf.chamadaEncalheCota as chamadaCota  ");
		hql.append(" left join chamadaCota.chamadaEncalhe chamadaEncalhe  ");
		
		hql.append(" WHERE ");
		
		hql.append(" confCota.id = :idConferenciaCota ");
		
		return hql.toString();
	}
	
	@Override
	public Integer qtdeNota(Long idConferenciaCota) {
		
		StringBuilder hql = new StringBuilder();
		hql.append(" select count(item.id) ");			
		hql.append(getHqlFromEWhereItensPendentes());		
		Query query =  getSession().createQuery(hql.toString());
		
		query.setParameter("idConferenciaCota", idConferenciaCota);
		Long totalRegistros = (Long) query.uniqueResult();
		
		return (totalRegistros == null) ? 0 : totalRegistros.intValue();
		 
	}
}
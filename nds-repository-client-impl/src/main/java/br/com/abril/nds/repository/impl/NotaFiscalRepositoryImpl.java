package br.com.abril.nds.repository.impl;

import java.util.List;

import javax.sql.DataSource;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.CotaExemplaresDTO;
import br.com.abril.nds.dto.FornecedorExemplaresDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.NfeDTO;
import br.com.abril.nds.dto.RetornoNFEDTO;
import br.com.abril.nds.dto.filtro.FiltroMonitorNfeDTO;
import br.com.abril.nds.dto.filtro.FiltroNFeDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.fiscal.TipoDestinatario;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.fiscal.nota.StatusProcessamento;
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
		
		if(filtro.getDataInicial() !=null) {
			
			hql.append(" AND ident.dataEmissao >= :dataInicial ");
		}

		if(filtro.getDataFinal() !=null) {
			hql.append(" AND ident.dataEmissao <= :dataFinal ");
		}

		if(filtro.getNumeroDocumento() != null){
			if(filtro.getDocumentoPessoa() !=null && !filtro.getDocumentoPessoa().isEmpty()) {
				hql.append(" AND doc.documento = :documento");
			}
		}

		if(filtro.getTipoNfe() !=null && !filtro.getTipoNfe().isEmpty()) {
			hql.append(" AND proc.nome = :tipoEmissaoNfe");		
		}

		if(filtro.getNumeroNotaInicial() !=null) {
			hql.append(" AND ident.numeroDocumentoFiscal >= :numeroInicial ");
		}

		if(filtro.getNumeroNotaFinal() !=null) {
			hql.append(" AND ident.numeroDocumentoFiscal <= :numeroFinal ");
		}

		if(filtro.getChaveAcesso() !=null && !filtro.getChaveAcesso().isEmpty()) {
			hql.append(" AND infElet.chaveAcesso = :chaveAcesso ");
		}

		if(filtro.getSituacaoNfe() !=null && !filtro.getSituacaoNfe().isEmpty()) {
			hql.append(" AND nfi.statusProcessamento = :situacaoNfe ");
		}

		if(filtro.getSerie() !=null) {
			hql.append(" AND ident.serie = :serie ");
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
	


	@Override
	@SuppressWarnings("unchecked")
	public List<CotaExemplaresDTO> consultaCotaExemplaresSumarizados(FiltroNFeDTO filtro) {
		
		// OBTER COTA EXEMPLARES SUMARIZADOS
		StringBuilder hql = new StringBuilder("SELECT ");
		hql.append(" mec.cota.id as idCota, ");
		hql.append(" mec.cota.numeroCota as numeroCota, ");
		hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome,'') as nomeCota,");
		hql.append(" SUM(mec.qtde) as exemplares, ");
		hql.append(" SUM(mec.valoresAplicados.precoVenda * mec.qtde) as total, "); 
		hql.append(" SUM(mec.valoresAplicados.precoComDesconto * mec.qtde) as totalDesconto, "); 	
		hql.append(" mec.cota.situacaoCadastro as situacaoCadastro ");
		
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
	public Long consultaCotaExemplaresSumarizadosQtd(FiltroNFeDTO filtro) {
		
		// OBTER COTA EXEMPLARES SUMARIZADOS
		StringBuilder hql = new StringBuilder("SELECT ");
		hql.append(" COUNT(mec.cota.id) ");
		Query query = queryConsultaNfeParameters(queryConsultaNfe(filtro, hql, true, true, false), filtro);
		
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
	public List<MovimentoEstoqueCota> obterMovimentosEstoqueCota(FiltroNFeDTO filtro) {
	
		// ITENS DA NOTA FISCAL
		StringBuilder hql = new StringBuilder("SELECT mec");
		Query query = queryConsultaNfeParameters(queryConsultaNfe(filtro, hql, false, false, true), filtro);
		
		return query.list();
	}
	
	private StringBuilder queryConsultaNfe(FiltroNFeDTO filtro, StringBuilder hql, boolean isCount, boolean isPagination, boolean isGroup) {
		
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
		.append(" AND mec.movimentoEstoqueCotaFuro is null ")
		.append(" AND mec.notaFiscalEmitida = false ");
		
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
	
	public Query queryConsultaNfeParameters(StringBuilder hql, FiltroNFeDTO filtro) {
		

		// Realizar a consulta e converter ao objeto cota exemplares.
		Query query = this.getSession().createQuery(hql.toString());		
		
		// Data Movimento:	...  Até   ...
		if (filtro.getDataInicial() != null && filtro.getDataFinal() != null) {
			query.setParameter("dataInicial", filtro.getDataInicial());
			query.setParameter("dataFinal", filtro.getDataFinal());
		}
		
		// tipo da nota fiscal		
		if(filtro.getIdNaturezaOperacao() != null && filtro.getIdNaturezaOperacao().longValue() > 0) {
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
	
}

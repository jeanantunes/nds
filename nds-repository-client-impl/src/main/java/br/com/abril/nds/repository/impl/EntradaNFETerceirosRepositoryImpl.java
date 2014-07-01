package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ConsultaEntradaNFETerceirosPendentesDTO;
import br.com.abril.nds.dto.ConsultaEntradaNFETerceirosRecebidasDTO;
import br.com.abril.nds.dto.filtro.FiltroEntradaNFETerceiros;
import br.com.abril.nds.model.fiscal.NotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.StatusNotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.EntradaNFETerceirosRepository;

@Repository
public class EntradaNFETerceirosRepositoryImpl extends AbstractRepositoryModel<NotaFiscalEntrada, Long> implements EntradaNFETerceirosRepository {

	public EntradaNFETerceirosRepositoryImpl() {
		super(NotaFiscalEntrada.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ConsultaEntradaNFETerceirosRecebidasDTO> consultarNotasRecebidas(FiltroEntradaNFETerceiros filtro, boolean limitar) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT notaFiscalEntrada.numero as numeroNota, ");
		hql.append("        notaFiscalEntrada.serie as serie, ");
		hql.append("        notaFiscalEntrada.chaveAcesso as chaveAcesso, ");
		hql.append("        notaFiscalEntrada.dataEmissao as dataEmissao, ");
		hql.append("        CASE WHEN ");
		hql.append("        ( ");
		hql.append("             SELECT COUNT(notaFiscalEntradaCotaCCE) ");
		hql.append("             FROM ControleConferenciaEncalheCota controleConferenciaEncalheCota ");
		hql.append("               LEFT JOIN controleConferenciaEncalheCota.notaFiscalEntradaCota as notaFiscalEntradaCotaCCE ");
		hql.append("               LEFT JOIN notaFiscalEntradaCotaCCE.naturezaOperacao as tipoNotaFiscalNF ");
		hql.append("             WHERE controleConferenciaEncalheCota = controleConferenciaEncalheCotaNF  ");
		hql.append("               AND tipoNotaFiscalNF.tipoOperacao = :tipoOperacaoEntrada ");
		hql.append("        ) = 0 THEN 'Entrada' ELSE 'Complementar' END  as tipoNotaFiscal, ");
		hql.append("        COALESCE(fornecedorPessoa.razaoSocial, cotaPessoa.razaoSocial, cotaPessoa.nome) as nome, ");		
		hql.append("        notaFiscalEntrada.valorBruto as valorNota, ");		
		hql.append("        ( CASE WHEN ");		
		hql.append("          ( ");		
		hql.append("             SELECT SUM(COALESCE(notaFiscalEntradaCotaCCE.valorNF, notaFiscalEntradaCotaCCE.valorProdutos, notaFiscalEntradaCotaCCE.valorLiquido, 0)) ");
		hql.append("             FROM ControleConferenciaEncalheCota controleConferenciaEncalheCota ");
		hql.append("               LEFT JOIN controleConferenciaEncalheCota.notaFiscalEntradaCota as notaFiscalEntradaCotaCCE ");
		hql.append("             WHERE controleConferenciaEncalheCota = controleConferenciaEncalheCotaNF ");
		hql.append("          ) < ( ");
		hql.append("             SELECT SUM(conferenciasEncalhe.qtde * conferenciasEncalhe.precoCapaInformado) ");
		hql.append("             FROM ControleConferenciaEncalheCota controleConferenciaEncalheCota ");
		hql.append("               LEFT JOIN controleConferenciaEncalheCota.conferenciasEncalhe as conferenciasEncalhe ");
		hql.append("             WHERE controleConferenciaEncalheCota = controleConferenciaEncalheCotaNF ");
		hql.append("           ) THEN 0 ELSE 1 END ");		
		hql.append("        ) as contemDiferenca ");
		
		hql.append(getSqlFromEWhereNotaEntrada(filtro));
		
		hql.append(getOrderBy(filtro));
		
		Query query =  getSession().createQuery(hql.toString());
		
		setarParametrosQuery(filtro, query, false);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ConsultaEntradaNFETerceirosRecebidasDTO.class));
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) { 
			query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
		}
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null && limitar) { 
			query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
		}
		
		return query.list();
	}
	
	private String getSqlFromEWhereNotaEntrada(FiltroEntradaNFETerceiros filtro) {
		
		StringBuilder hql = new StringBuilder();
	
		hql.append(" FROM NotaFiscalEntrada as notaFiscalEntrada ");
		hql.append(" LEFT JOIN notaFiscalEntrada.naturezaOperacao as no ");
		hql.append(" LEFT JOIN notaFiscalEntrada.cota as cota ");
		hql.append(" LEFT JOIN cota.pessoa as cotaPessoa ");
		hql.append(" LEFT JOIN notaFiscalEntrada.fornecedor as fornecedor ");
		hql.append(" LEFT JOIN fornecedor.juridica as fornecedorPessoa ");
		hql.append(" LEFT JOIN notaFiscalEntrada.controleConferenciaEncalheCota as controleConferenciaEncalheCotaNF ");
		
		hql.append(" where no.tipoOperacao = :tipoOperacaoEntrada ");
		
		if (filtro.getTipoNota() != null && !FiltroEntradaNFETerceiros.TipoNota.TODAS.equals(filtro.getTipoNota())) {
			// hql.append(" and no.tipoOperacao = :complementar ");
		}

		if (filtro.getCota() != null) {
			hql.append( " and cota.id = :idCota ");
		}
		
		if (filtro.getListIdFornecedor() != null && !filtro.getListIdFornecedor().isEmpty()) {
			hql.append( " and fornecedor in (:fornecedor) ");
		}
		
		if(filtro.getDataInicial() != null && filtro.getDataFinal() != null){
			hql.append( " and date(notaFiscalEntrada.dataEmissao) between :dataInicial and :dataFinal ");
		}

		return hql.toString();
	}
	
	private String getOrderBy(FiltroEntradaNFETerceiros filtro){
		
		if(filtro.getPaginacao() == null || filtro.getPaginacao().getSortColumn() == null){
			return "";
		}
		StringBuilder hql = new StringBuilder();
		
		if (StatusNotaFiscalEntrada.NAO_RECEBIDA.equals(filtro.getStatusNotaFiscalEntrada())) {
			hql.append(" order by notaFiscalEntrada.numero ");
		} else {
			hql.append(" order by cota.numeroCota ");
		}
		
		if (filtro.getPaginacao().getOrdenacao() != null) {
			hql.append( filtro.getPaginacao().getOrdenacao().toString());
		}
		
		return hql.toString();
	}
	
	private void buscarParametros(FiltroEntradaNFETerceiros filtro, Query query, boolean count){
		
		if (!count || filtro.getStatusNotaFiscalEntrada().equals(StatusNotaFiscalEntrada.RECEBIDA) || filtro.getTipoNota() != null && !FiltroEntradaNFETerceiros.TipoNota.TODAS.equals(filtro.getTipoNota())) {
			query.setParameter("tipoOperacaoEntrada", TipoOperacao.ENTRADA);
		}

		if (filtro.getTipoNota() != null && !FiltroEntradaNFETerceiros.TipoNota.TODAS.equals(filtro.getTipoNota())) {
			if(FiltroEntradaNFETerceiros.TipoNota.ENTRADA.equals(filtro.getTipoNota())) {
				// query.setParameter("complementar", TipoNota.ENTRADA);
			} else {
				// query.setParameter("complementar", TipoNota.SAIDA);
			}
		}

		if (filtro.getCota() != null) {
			query.setParameter("idCota", filtro.getCota().getId());
		}

		if (filtro.getListIdFornecedor() != null && !filtro.getListIdFornecedor().isEmpty()) {
			// query.setParameterList("fornecedor", filtro.getListIdFornecedor());
		}

		if(filtro.getDataInicial() != null && filtro.getDataFinal() != null){
			query.setParameter("dataInicial", filtro.getDataInicial());
			query.setParameter("dataFinal", filtro.getDataFinal());
		}		

	}

	private void setarParametrosQuery(FiltroEntradaNFETerceiros filtro, Query query, boolean count){
		
		if (!count || filtro.getStatusNotaFiscalEntrada().equals(StatusNotaFiscalEntrada.RECEBIDA) || filtro.getTipoNota() != null && !FiltroEntradaNFETerceiros.TipoNota.TODAS.equals(filtro.getTipoNota())) {
			query.setParameter("tipoOperacaoEntrada", TipoOperacao.SAIDA);
		}

		if (filtro.getTipoNota() != null && !FiltroEntradaNFETerceiros.TipoNota.TODAS.equals(filtro.getTipoNota())) {
			if(FiltroEntradaNFETerceiros.TipoNota.ENTRADA.equals(filtro.getTipoNota())) {
				// query.setParameter("complementar", TipoNota.ENTRADA);
			} else {
				// query.setParameter("complementar", TipoNota.SAIDA);
			}
		}

		if (filtro.getCota() != null) {
			query.setParameter("idCota", filtro.getCota().getId());
		}

		if (filtro.getListIdFornecedor() != null && !filtro.getListIdFornecedor().isEmpty()) {
			query.setParameterList("fornecedor", filtro.getListIdFornecedor());
		}

		if(filtro.getDataInicial() != null && filtro.getDataFinal() != null){
			query.setParameter("dataInicial", filtro.getDataInicial());
			query.setParameter("dataFinal", filtro.getDataFinal());
		}		

	}
	
	@Override
	public Integer buscarTotalNotas(FiltroEntradaNFETerceiros filtro) {
		
		StringBuilder hql = new StringBuilder();
		hql.append(" select count(cota) ");
		
		if(StatusNotaFiscalEntrada.RECEBIDA.equals(filtro.getStatusNotaFiscalEntrada())) {
			hql.append(getSqlFromEWhereNotaEntrada(filtro));			
		}else{
			hql.append(getSqlFromEWhereNotaPendenteRecebimento(filtro, false));
		}
		
		Query query =  getSession().createQuery(hql.toString());
		
		buscarParametros(filtro, query, true);
		
		Long totalRegistros = (Long) query.uniqueResult();
		
		return (totalRegistros == null) ? 0 : totalRegistros.intValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ConsultaEntradaNFETerceirosPendentesDTO> consultaNotasPendentesRecebimento(FiltroEntradaNFETerceiros filtro, boolean limitar) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT cota.numeroCota as numeroCota, ");
		hql.append("        coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome, '') as nome, ");		
		hql.append("        controleConferenciaEncalheCota.dataOperacao as dataEncalhe, ");
		hql.append("        nf.serie as serie, ");
		hql.append("        nf.chaveAcesso as chaveAcesso, ");
		hql.append("        nf.numero as numeroNfe, ");
		hql.append("        nf.id as idNotaFiscalEntrada, ");
		hql.append("        CASE WHEN ");
		hql.append("        ( ");
		hql.append("             SELECT COUNT(notaFiscalEntradaCota) ");
		hql.append("             FROM ControleConferenciaEncalheCota controleConferenciaEncalheCotaNF ");
		hql.append("               LEFT JOIN controleConferenciaEncalheCotaNF.notaFiscalEntradaCota as notaFiscalEntradaCota ");
		hql.append("               LEFT JOIN notaFiscalEntradaCota.naturezaOperacao as tipoNotaFiscal ");
		hql.append("             WHERE controleConferenciaEncalheCotaNF = controleConferenciaEncalheCota  ");
		hql.append("               AND tipoNotaFiscal.tipoOperacao = :tipoOperacaoEntrada");
		hql.append("        ) = 0 THEN 'Entrada' ELSE 'Complementar' END  as tipoNotaFiscal, ");
		hql.append("        ( ");
		hql.append("             SELECT SUM(COALESCE(notaFiscalEntradaCota.valorNF, notaFiscalEntradaCota.valorProdutos, notaFiscalEntradaCota.valorLiquido, 0)) ");
		hql.append("             FROM ControleConferenciaEncalheCota controleConferenciaEncalheCotaNF ");
		hql.append("               LEFT JOIN controleConferenciaEncalheCotaNF.notaFiscalEntradaCota as notaFiscalEntradaCota ");
		hql.append("             WHERE controleConferenciaEncalheCotaNF = controleConferenciaEncalheCota ");
		hql.append("        ) as valorNota, ");
		hql.append("  item.preco as valorReal, ");
		// hql.append(" ((notaFiscalEntradaCotas.valorDesconto) -  (conferenciasEncalhe.qtde * conferenciasEncalhe.precoComDesconto)) as diferenca, ");
		hql.append(" ( ");
		hql.append("  	item.preco - (SELECT SUM(notaFiscalEntradaCota.valorDesconto) ");
		hql.append("  	FROM ControleConferenciaEncalheCota controleConferenciaDesconto ");
		hql.append("  	LEFT JOIN controleConferenciaDesconto.notaFiscalEntradaCota as notaFiscalEntradaCota ");
		hql.append("  	WHERE controleConferenciaDesconto.processoUtilizaNfe = true and controleConferenciaDesconto = controleConferenciaEncalheCota ");
		hql.append("  )) as diferenca, ");
		hql.append("        'Pendente' as status, ");
		hql.append(" controleConferenciaEncalheCota.id  as idControleConferenciaEncalheCota ");
		
		hql.append(getSqlFromEWhereNotaPendenteRecebimento(filtro, true));
		
		hql.append(getOrderByNotasPendentes(filtro));
		
		Query query =  getSession().createQuery(hql.toString());
		
		buscarParametros(filtro, query, false);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ConsultaEntradaNFETerceirosPendentesDTO.class));
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null && limitar) 
			query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());			
		 
		return query.list();
		 
	}
	
	private String getSqlFromEWhereNotaPendenteRecebimento(FiltroEntradaNFETerceiros filtro, boolean isGroup) {
		
		StringBuilder hql = new StringBuilder();
	
		hql.append(" from ItemNotaFiscalEntrada as item ");
		hql.append(" LEFT JOIN item.notaFiscal as nf ");
		hql.append(" LEFT JOIN nf.controleConferenciaEncalheCota as controleConferenciaEncalheCota ");
		hql.append(" LEFT JOIN nf.fornecedor as fornecedor ");
		hql.append(" LEFT JOIN fornecedor.juridica as fornecedorPessoa ");
		hql.append(" LEFT JOIN controleConferenciaEncalheCota.cota as cota ");
		hql.append(" LEFT JOIN cota.pessoa as pessoa ");
		hql.append(" LEFT JOIN pessoa.telefones as telefone ");
		hql.append(" where ");
		hql.append(" ( ");
		hql.append("  	item.preco > (SELECT SUM(notaFiscalEntradaCota.valorDesconto) ");
		hql.append("  	FROM ControleConferenciaEncalheCota controleConferenciaDesconto ");
		hql.append("  	LEFT JOIN controleConferenciaDesconto.notaFiscalEntradaCota as notaFiscalEntradaCota ");
		hql.append("  	WHERE controleConferenciaDesconto.processoUtilizaNfe = true and controleConferenciaDesconto = controleConferenciaEncalheCota ");
		hql.append("  )) ");
		hql.append(" and controleConferenciaEncalheCota.processoUtilizaNfe = true ");
		
		if (filtro.getTipoNota() != null && !FiltroEntradaNFETerceiros.TipoNota.TODAS.equals(filtro.getTipoNota())) {
			hql.append("   and ( ");
			hql.append("             SELECT COUNT(notaFiscalEntradaCota) ");
			hql.append("             FROM ControleConferenciaEncalheCota controleConferenciaEncalheCotaNF ");
			hql.append("               LEFT JOIN controleConferenciaEncalheCotaNF.notaFiscalEntradaCota as notaFiscalEntradaCota ");
			hql.append("               LEFT JOIN notaFiscalEntradaCota.naturezaOperacao as tipoNotaFiscal ");
			hql.append("             WHERE controleConferenciaEncalheCotaNF = controleConferenciaEncalheCota  ");
			hql.append("               AND tipoNotaFiscal.tipoOperacao = :tipoOperacaoEntrada ");
			if(FiltroEntradaNFETerceiros.TipoNota.SAIDA.equals(filtro.getTipoNota())) {
				hql.append("        ) > 0");
			} else {
				hql.append("        ) = 0");
			}
		}

		if(filtro.getCota() != null) {			
			hql.append( " and cota.id = :idCota ");			
		}
		
		if(filtro.getTipoNota() != null && !FiltroEntradaNFETerceiros.TipoNota.TODAS.equals(filtro.getTipoNota())) {
			// hql.append(" and no.tipoOperacao = :complementar ");
		}
		
		if(filtro.getDataInicial() != null && filtro.getDataFinal() != null){
			hql.append( " and date(controleConferenciaEncalheCota.dataOperacao) between :dataInicial and :dataFinal ");
		}
		
		if(filtro.getListIdFornecedor() != null && !filtro.getListIdFornecedor().isEmpty()){
			
			// hql.append(" and fornecedor in (:fornecedor) ");
		}
		
		if(isGroup){			
			hql.append(" group by cota.id, controleConferenciaEncalheCota.dataOperacao ");
		}
		
		return hql.toString();
	}
	
	private String getOrderByNotasPendentes(FiltroEntradaNFETerceiros filtro){
		
		if(filtro.getPaginacao() == null || filtro.getPaginacao().getSortColumn() == null){
			return "";
		}
		StringBuilder hql = new StringBuilder();
		
		hql.append(" order by cota.numeroCota ");
		
		if (filtro.getPaginacao().getOrdenacao() != null) {
			hql.append( filtro.getPaginacao().getOrdenacao().toString());
		}
		
		return hql.toString();
	}

	@Override
	public Integer qtdeNotasRecebidas(FiltroEntradaNFETerceiros filtro) {
		
		StringBuilder hql = new StringBuilder();
		hql.append(" select count(notaFiscalEntrada.numero) ");
		hql.append(getSqlFromEWhereNotaEntrada(filtro));			
		
		Query query =  getSession().createQuery(hql.toString());
		
		setarParametrosQuery(filtro, query, true);
		
		Long totalRegistros = (Long) query.uniqueResult();
		
		return (totalRegistros == null) ? 0 : totalRegistros.intValue();
	}
	
	@Override
	public Integer qtdeNotasPendentesRecebimento(FiltroEntradaNFETerceiros filtro) {
		
		StringBuilder hql = new StringBuilder();
		hql.append(" select count(cota) ");
		
		hql.append(getSqlFromEWhereNotaPendenteRecebimento(filtro, false));
		
		Query query =  getSession().createQuery(hql.toString());
		
		buscarParametros(filtro, query, true);
		
		Long totalRegistros = (Long) query.uniqueResult();
		
		return (totalRegistros == null) ? 0 : totalRegistros.intValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ConsultaEntradaNFETerceirosPendentesDTO> consultaNotasPendentesEmissao(FiltroEntradaNFETerceiros filtro, boolean limitar) {
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT cota.numeroCota as numeroCota, ");
		hql.append("        coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome, '') as nome, ");		
		hql.append("        controleConferenciaEncalheCota.dataOperacao as dataEncalhe, ");
		hql.append("        nf.serie as serie, ");
		hql.append("        nf.chaveAcesso as chaveAcesso, ");
		hql.append("        nf.numero as numeroNfe, ");
		hql.append("        nf.id as idNotaFiscalEntrada, ");
		hql.append("        CASE WHEN ");
		hql.append("        ( ");
		hql.append("             SELECT COUNT(notaFiscalEntradaCota) ");
		hql.append("             FROM ControleConferenciaEncalheCota controleConferenciaEncalheCotaNF ");
		hql.append("               LEFT JOIN controleConferenciaEncalheCotaNF.notaFiscalEntradaCota as notaFiscalEntradaCota ");
		hql.append("               LEFT JOIN notaFiscalEntradaCota.naturezaOperacao as tipoNotaFiscal ");
		hql.append("             WHERE controleConferenciaEncalheCotaNF = controleConferenciaEncalheCota  ");
		hql.append("               AND tipoNotaFiscal.tipoOperacao = :tipoOperacaoEntrada");
		hql.append("        ) = 0 THEN 'Entrada' ELSE 'Complementar' END  as tipoNotaFiscal, ");
		hql.append("        ( ");
		hql.append("             SELECT SUM(COALESCE(notaFiscalEntradaCota.valorNF, notaFiscalEntradaCota.valorProdutos, notaFiscalEntradaCota.valorLiquido, 0)) ");
		hql.append("             FROM ControleConferenciaEncalheCota controleConferenciaEncalheCotaNF ");
		hql.append("               LEFT JOIN controleConferenciaEncalheCotaNF.notaFiscalEntradaCota as notaFiscalEntradaCota ");
		hql.append("             WHERE controleConferenciaEncalheCotaNF = controleConferenciaEncalheCota ");
		hql.append("        ) as valorNota, ");
		hql.append("  item.preco as valorReal, ");
		hql.append(" ( ");
		hql.append("  	item.preco - (SELECT SUM(notaFiscalEntradaCota.valorDesconto) ");
		hql.append("  	FROM ControleConferenciaEncalheCota controleConferenciaDesconto ");
		hql.append("  	LEFT JOIN controleConferenciaDesconto.notaFiscalEntradaCota as notaFiscalEntradaCota ");
		hql.append("  	WHERE controleConferenciaDesconto.processoUtilizaNfe = true and controleConferenciaDesconto = controleConferenciaEncalheCota ");
		hql.append("  )) as diferenca, ");
		hql.append("        'Pendente' as status, ");
		hql.append(" controleConferenciaEncalheCota.id  as idControleConferenciaEncalheCota ");
		
		hql.append(getSqlFromEWhereNotaPendenteEmissao(filtro, true));
		
		hql.append(getOrderByNotasPendentes(filtro));
		
		Query query =  getSession().createQuery(hql.toString());
		
		buscarParametros(filtro, query, false);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ConsultaEntradaNFETerceirosPendentesDTO.class));
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) { 
			query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
		}
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null && limitar) { 
			query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());			
		}
		
		return query.list();
	}
	
	private String getSqlFromEWhereNotaPendenteEmissao(FiltroEntradaNFETerceiros filtro, boolean isGroup) {
		
		StringBuilder hql = new StringBuilder();
	
		hql.append(" from ItemNotaFiscalEntrada as item ");
		hql.append(" LEFT JOIN item.notaFiscal as nf ");
		hql.append(" LEFT JOIN nf.controleConferenciaEncalheCota as controleConferenciaEncalheCota ");
		hql.append(" LEFT JOIN nf.fornecedor as fornecedor ");
		hql.append(" LEFT JOIN fornecedor.juridica as fornecedorPessoa ");
		hql.append(" LEFT JOIN controleConferenciaEncalheCota.cota as cota ");
		hql.append(" LEFT JOIN cota.pessoa as pessoa ");
		hql.append(" LEFT JOIN pessoa.telefones as telefone ");
		hql.append(" where ");
		hql.append(" ( ");
		hql.append("  	item.preco < (SELECT SUM(notaFiscalEntradaCota.valorDesconto) ");
		hql.append("  	FROM ControleConferenciaEncalheCota controleConferenciaDesconto ");
		hql.append("  	LEFT JOIN controleConferenciaDesconto.notaFiscalEntradaCota as notaFiscalEntradaCota ");
		hql.append("  	WHERE controleConferenciaDesconto.processoUtilizaNfe = true and controleConferenciaDesconto = controleConferenciaEncalheCota ");
		hql.append("  )) ");
		hql.append(" and controleConferenciaEncalheCota.processoUtilizaNfe = true ");
		
		if (filtro.getTipoNota() != null && !FiltroEntradaNFETerceiros.TipoNota.TODAS.equals(filtro.getTipoNota())) {
			hql.append("   and ( ");
			hql.append("             SELECT COUNT(notaFiscalEntradaCota) ");
			hql.append("             FROM ControleConferenciaEncalheCota controleConferenciaEncalheCotaNF ");
			hql.append("               LEFT JOIN controleConferenciaEncalheCotaNF.notaFiscalEntradaCota as notaFiscalEntradaCota ");
			hql.append("               LEFT JOIN notaFiscalEntradaCota.naturezaOperacao as tipoNotaFiscal ");
			hql.append("             WHERE controleConferenciaEncalheCotaNF = controleConferenciaEncalheCota  ");
			hql.append("               AND tipoNotaFiscal.tipoOperacao = :tipoOperacaoEntrada ");
			if(FiltroEntradaNFETerceiros.TipoNota.SAIDA.equals(filtro.getTipoNota())) {
				hql.append("        ) > 0");
			} else {
				hql.append("        ) = 0");
			}
		}

		if(filtro.getCota() != null) {			
			hql.append( " and cota.id = :idCota ");			
		}
		
		if(filtro.getTipoNota() != null && !FiltroEntradaNFETerceiros.TipoNota.TODAS.equals(filtro.getTipoNota())) {
			// hql.append(" and no.tipoOperacao = :complementar ");
		}
		
		if(filtro.getDataInicial() != null && filtro.getDataFinal() != null){
			hql.append( " and date(controleConferenciaEncalheCota.dataOperacao) between :dataInicial and :dataFinal ");
		}
		
		if(filtro.getListIdFornecedor() != null && !filtro.getListIdFornecedor().isEmpty()){
			
			// hql.append(" and fornecedor in (:fornecedor) ");
		}
		
		if(isGroup){			
			hql.append(" group by cota.id, controleConferenciaEncalheCota.dataOperacao ");
		}
		
		return hql.toString();
	}

	@Override
	public Integer qtdeNotasPendentesEmissao(FiltroEntradaNFETerceiros filtro) {
		StringBuilder hql = new StringBuilder();
		hql.append(" select count(cota) ");
		
		hql.append(getSqlFromEWhereNotaPendenteEmissao(filtro, false));
		
		Query query =  getSession().createQuery(hql.toString());
		
		buscarParametros(filtro, query, true);
		
		Long totalRegistros = (Long) query.uniqueResult();
		
		return (totalRegistros == null) ? 0 : totalRegistros.intValue();
	}
}

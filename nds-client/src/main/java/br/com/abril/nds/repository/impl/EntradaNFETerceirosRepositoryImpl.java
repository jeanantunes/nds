package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ConsultaEntradaNFETerceirosPendentesDTO;
import br.com.abril.nds.dto.ConsultaEntradaNFETerceirosRecebidasDTO;
import br.com.abril.nds.dto.ItemNotaFiscalPendenteDTO;
import br.com.abril.nds.dto.filtro.FiltroEntradaNFETerceiros;
import br.com.abril.nds.model.fiscal.GrupoNotaFiscal;
import br.com.abril.nds.model.fiscal.NotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.StatusNotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.repository.EntradaNFETerceirosRepository;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

@Repository
public class EntradaNFETerceirosRepositoryImpl extends AbstractRepositoryModel<NotaFiscalEntrada, Long> implements
		EntradaNFETerceirosRepository {

	public EntradaNFETerceirosRepositoryImpl() {
		super(NotaFiscalEntrada.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ConsultaEntradaNFETerceirosRecebidasDTO> buscarNFNotasRecebidas(FiltroEntradaNFETerceiros filtro, boolean limitar) {
		
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
		hql.append("               LEFT JOIN notaFiscalEntradaCotaCCE.tipoNotaFiscal as tipoNotaFiscalNF ");
		hql.append("             WHERE controleConferenciaEncalheCota = controleConferenciaEncalheCotaNF  ");
		hql.append("               AND tipoNotaFiscalNF.tipoOperacao = :tipoOperacaoEntrada ");
		hql.append("               AND tipoNotaFiscalNF.grupoNotaFiscal = :complementar ");
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
		
		buscarParametros(filtro, query, false);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				ConsultaEntradaNFETerceirosRecebidasDTO.class));
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null && limitar) 
			query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
		 
		return query.list();
	}
	
	private String getSqlFromEWhereNotaEntrada(FiltroEntradaNFETerceiros filtro) {
		
		StringBuilder hql = new StringBuilder();
	
		hql.append(" FROM NotaFiscalEntrada as notaFiscalEntrada ");
		hql.append(" LEFT JOIN notaFiscalEntrada.tipoNotaFiscal as tipoNotaFiscal ");
		hql.append(" LEFT JOIN notaFiscalEntrada.cota as cota ");
		hql.append(" LEFT JOIN cota.pessoa as cotaPessoa ");
		hql.append(" LEFT JOIN notaFiscalEntrada.fornecedor as fornecedor ");
		hql.append(" LEFT JOIN fornecedor.juridica as fornecedorPessoa ");
		hql.append(" LEFT JOIN notaFiscalEntrada.controleConferenciaEncalheCota as controleConferenciaEncalheCotaNF ");
		
		hql.append(" where tipoNotaFiscal.tipoOperacao = :tipoOperacaoEntrada ");
		
		if (filtro.getTipoNota() != null && !FiltroEntradaNFETerceiros.TipoNota.TODAS.equals(filtro.getTipoNota())) {
			if(FiltroEntradaNFETerceiros.TipoNota.COMPLEMENTAR.equals(filtro.getTipoNota())) {
				hql.append("   and tipoNotaFiscal.grupoNotaFiscal = :complementar ");
			} else {
				hql.append("   and tipoNotaFiscal.grupoNotaFiscal != :complementar ");
			}
		}

		if (filtro.getCota() != null) {
			hql.append( " and cota.id = :idCota ");
		}
		
		if (filtro.getFornecedor() != null
				&& filtro.getFornecedor().getId() != null
				&& filtro.getFornecedor().getId() > 0
				&& StatusNotaFiscalEntrada.RECEBIDA.equals(filtro.getStatusNotaFiscalEntrada())) {
			hql.append( " and fornecedor.id = :idFornecedor ");
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

		if (!count || filtro.getTipoNota() != null && !FiltroEntradaNFETerceiros.TipoNota.TODAS.equals(filtro.getTipoNota())) {
			query.setParameter("complementar", GrupoNotaFiscal.NF_TERCEIRO_COMPLEMENTAR);
		}

		if (filtro.getCota() != null) {
			query.setParameter("idCota", filtro.getCota().getId());
		}

		if (filtro.getFornecedor() != null
				&& filtro.getFornecedor().getId() != null
				&& filtro.getFornecedor().getId() > 0
				&& StatusNotaFiscalEntrada.RECEBIDA.equals(filtro.getStatusNotaFiscalEntrada())) {
			query.setParameter("idFornecedor", filtro.getFornecedor().getJuridica().getId());
		}

		if(filtro.getDataInicial() != null && filtro.getDataFinal() != null){
			query.setParameter("dataInicial", filtro.getDataInicial());
			query.setParameter("dataFinal", filtro.getDataFinal());
		}		

	}

	@Override
	public Integer buscarTotalNotas(
			FiltroEntradaNFETerceiros filtro) {
		
		StringBuilder hql = new StringBuilder();
		hql.append(" select count(cota) ");
		
		if(StatusNotaFiscalEntrada.RECEBIDA.equals(filtro.getStatusNotaFiscalEntrada())) {
			hql.append(getSqlFromEWhereNotaEntrada(filtro));			
		}else{
			hql.append(getSqlFromEWhereNotaPendente(filtro));
		}
		
		Query query =  getSession().createQuery(hql.toString());
		
		buscarParametros(filtro, query, true);
		
		Long totalRegistros = (Long) query.uniqueResult();
		
		return (totalRegistros == null) ? 0 : totalRegistros.intValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ConsultaEntradaNFETerceirosPendentesDTO> buscarNFNotasPendentes(
			FiltroEntradaNFETerceiros filtro, boolean limitar) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT cota.numeroCota as numeroCota, ");
		hql.append("        pessoa.nome as nome, ");		
		hql.append("        conferenciasEncalhe.data as dataEncalhe, ");		
		hql.append("        CASE WHEN ");
		hql.append("        ( ");
		hql.append("             SELECT COUNT(notaFiscalEntradaCota) ");
		hql.append("             FROM ControleConferenciaEncalheCota controleConferenciaEncalheCotaNF ");
		hql.append("               LEFT JOIN controleConferenciaEncalheCotaNF.notaFiscalEntradaCota as notaFiscalEntradaCota ");
		hql.append("               LEFT JOIN notaFiscalEntradaCota.tipoNotaFiscal as tipoNotaFiscal ");
		hql.append("             WHERE controleConferenciaEncalheCotaNF = controleConferenciaEncalheCota  ");
		hql.append("               AND tipoNotaFiscal.tipoOperacao = :tipoOperacaoEntrada");
		hql.append("               AND tipoNotaFiscal.grupoNotaFiscal = :complementar ");
		hql.append("        ) = 0 THEN 'Entrada' ELSE 'Complementar' END  as tipoNotaFiscal, ");
		hql.append("        ( ");
		hql.append("             SELECT SUM(COALESCE(notaFiscalEntradaCota.valorNF, notaFiscalEntradaCota.valorProdutos, notaFiscalEntradaCota.valorLiquido, 0)) ");
		hql.append("             FROM ControleConferenciaEncalheCota controleConferenciaEncalheCotaNF ");
		hql.append("               LEFT JOIN controleConferenciaEncalheCotaNF.notaFiscalEntradaCota as notaFiscalEntradaCota ");
		hql.append("             WHERE controleConferenciaEncalheCotaNF = controleConferenciaEncalheCota ");
		hql.append("        ) as valorNota, ");
		hql.append("        (conferenciasEncalhe.qtde * conferenciasEncalhe.precoCapaInformado ) as valorReal, ");
		hql.append("        ((conferenciasEncalhe.qtdeInformada * conferenciasEncalhe.precoCapaInformado) - ( ");
		hql.append("             SELECT SUM(COALESCE(notaFiscalEntradaCota.valorNF, notaFiscalEntradaCota.valorProdutos, notaFiscalEntradaCota.valorLiquido, 0)) ");
		hql.append("             FROM ControleConferenciaEncalheCota controleConferenciaEncalheCotaNF ");
		hql.append("               LEFT JOIN controleConferenciaEncalheCotaNF.notaFiscalEntradaCota as notaFiscalEntradaCota ");
		hql.append("             WHERE controleConferenciaEncalheCotaNF = controleConferenciaEncalheCota ");
		hql.append("        )) as diferenca, ");
		hql.append("        'Pendente' as status, ");
		hql.append(" controleConferenciaEncalheCota.id  as idControleConferenciaEncalheCota ");
		
		hql.append(getSqlFromEWhereNotaPendente(filtro));
		
		hql.append(getOrderByNotasPendentes(filtro));
		
		Query query =  getSession().createQuery(hql.toString());
		
		buscarParametros(filtro, query, false);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				ConsultaEntradaNFETerceirosPendentesDTO.class));
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null && limitar) 
			query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());			
		 
		return query.list();
		 
	}
	
	private String getSqlFromEWhereNotaPendente(FiltroEntradaNFETerceiros filtro) {
		
		StringBuilder hql = new StringBuilder();
	
		hql.append(" from ControleConferenciaEncalheCota as controleConferenciaEncalheCota ");
		hql.append(" LEFT JOIN controleConferenciaEncalheCota.cota as cota ");
		hql.append(" LEFT JOIN cota.pessoa as pessoa ");
		hql.append(" LEFT JOIN controleConferenciaEncalheCota.conferenciasEncalhe as conferenciasEncalhe");
		
		hql.append(" where ( ");
		hql.append("            SELECT SUM(COALESCE(notaFiscalEntradaCota.valorNF, notaFiscalEntradaCota.valorProdutos, notaFiscalEntradaCota.valorLiquido, 0)) ");
		hql.append("            FROM ControleConferenciaEncalheCota controleConferenciaEncalheCotaNF ");
		hql.append("              LEFT JOIN controleConferenciaEncalheCotaNF.notaFiscalEntradaCota as notaFiscalEntradaCota ");
		hql.append("            WHERE controleConferenciaEncalheCotaNF = controleConferenciaEncalheCota ");
		hql.append("       ) < ( ");
		hql.append("            SELECT SUM(conferenciasEncalheNF.qtde * conferenciasEncalheNF.precoCapaInformado) ");
		hql.append("            FROM ControleConferenciaEncalheCota controleConferenciaEncalheCotaNF ");
		hql.append("              LEFT JOIN controleConferenciaEncalheCotaNF.conferenciasEncalhe as conferenciasEncalheNF ");
		hql.append("            WHERE controleConferenciaEncalheCotaNF = controleConferenciaEncalheCota ");
		hql.append("       ) ");
		
		if (filtro.getTipoNota() != null && !FiltroEntradaNFETerceiros.TipoNota.TODAS.equals(filtro.getTipoNota())) {
			hql.append("   and ( ");
			hql.append("             SELECT COUNT(notaFiscalEntradaCota) ");
			hql.append("             FROM ControleConferenciaEncalheCota controleConferenciaEncalheCotaNF ");
			hql.append("               LEFT JOIN controleConferenciaEncalheCotaNF.notaFiscalEntradaCota as notaFiscalEntradaCota ");
			hql.append("               LEFT JOIN notaFiscalEntradaCota.tipoNotaFiscal as tipoNotaFiscal ");
			hql.append("             WHERE controleConferenciaEncalheCotaNF = controleConferenciaEncalheCota  ");
			hql.append("               AND tipoNotaFiscal.tipoOperacao = :tipoOperacaoEntrada");
			hql.append("               AND tipoNotaFiscal.grupoNotaFiscal = :complementar ");
			if(FiltroEntradaNFETerceiros.TipoNota.COMPLEMENTAR.equals(filtro.getTipoNota())) {
				hql.append("        ) > 0");
			} else {
				hql.append("        ) = 0");
			}
		}

		if(filtro.getCota() != null) {			
			hql.append( " and cota.id = :idCota ");			
		}
		
		if(filtro.getDataInicial() != null && filtro.getDataFinal() != null){
			hql.append( " and date(controleConferenciaEncalheCota.dataOperacao) between :dataInicial and :dataFinal ");
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

	@SuppressWarnings("unchecked")
	@Override
	public List<ItemNotaFiscalPendenteDTO> buscarItensPorNota(
			Long idConferenciaCota, String  orderBy,Ordenacao ordenacao, Integer firstResult, Integer maxResults) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT produto.codigo as codigoProduto, ");
		hql.append("produto.nome as nomeProduto, ");		
		hql.append("produtoEdicao.numeroEdicao as numeroEdicao, ");
		hql.append("produtoEdicao.numeroEdicao as numeroEdicao, ");
		hql.append("conf.qtdeInformada as qtdInformada, ");
		hql.append("conf.qtde as qtdRecebida, ");
		hql.append("conf.precoCapaInformado as precoCapa, ");
		hql.append(" ("+ getSubSqlQueryValorDesconto() +") as desconto, ");
		hql.append(" (conf.precoCapaInformado - (conf.precoCapaInformado * ("+ getSubSqlQueryValorDesconto() +") / 100)) AS precoDesconto, ");
		hql.append(" (conf.precoCapaInformado - (conf.precoCapaInformado * ("+ getSubSqlQueryValorDesconto() +") / 100) * conf.qtdeInformada) AS totalDoItem, ");
		hql.append("conf.qtde as qtdRecebida, ");
		hql.append("conf.data as dataConferenciaEncalhe, ");
		hql.append("chamadaEncalhe.dataRecolhimento as dataChamadaEncalhe ");
		
		hql.append(getHqlFromEWhereItensPendentes());
		
		Query query =  getSession().createQuery(hql.toString());
		
		query.setParameter("idConferenciaCota", idConferenciaCota);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				ItemNotaFiscalPendenteDTO.class));
		
		if(firstResult != null) 
			query.setFirstResult(firstResult);
		
		if(maxResults != null) 
			query.setMaxResults(maxResults);			
		 
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
	
	
	private String getSubSqlQueryValorDesconto() {
		
		StringBuilder hql = new StringBuilder("coalesce ((select view.desconto");
		hql.append(" from ViewDesconto view ")
		   .append(" where view.cotaId = confCota.cota.id ")
		   .append(" and view.produtoEdicaoId = produtoEdicao.id ")
		   .append(" and view.fornecedorId = fornecedores.id),0) ");
		
		return hql.toString();
		
	}

	@Override
	public Integer buscarTodasItensPorNota(Long idConferenciaCota) {
		
		StringBuilder hql = new StringBuilder();
		hql.append(" select count(item.id) ");			
		hql.append(getHqlFromEWhereItensPendentes());		
		Query query =  getSession().createQuery(hql.toString());
		
		query.setParameter("idConferenciaCota", idConferenciaCota);
		Long totalRegistros = (Long) query.uniqueResult();
		
		return (totalRegistros == null) ? 0 : totalRegistros.intValue();
		 
	}
	
}

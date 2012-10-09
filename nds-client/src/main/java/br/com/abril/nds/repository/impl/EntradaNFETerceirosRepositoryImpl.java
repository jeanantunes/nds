package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ConsultaEntradaNFETerceirosRecebidasDTO;
import br.com.abril.nds.dto.ConsultaEntradaNFETerceirosPendentesDTO;
import br.com.abril.nds.dto.ItemNotaFiscalPendenteDTO;
import br.com.abril.nds.dto.filtro.FiltroEntradaNFETerceiros;
import br.com.abril.nds.model.fiscal.NotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.StatusNotaFiscalEntrada;
import br.com.abril.nds.repository.EntradaNFETerceirosRepository;

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
		
		hql.append(" SELECT notaFiscalEntrada.numero as numeroNfe, ");
		hql.append("        notaFiscalEntrada.serie as serie, ");
		hql.append("        notaFiscalEntrada.chaveAcesso as chaveAcesso, ");
		hql.append("        notaFiscalEntrada.dataEmissao as dataEmissao, ");
		hql.append("        tipoNotaFiscal.tipoOperacao as tipoNotaFiscal, ");
		hql.append("        COALESCE(fornecedorPessoa.razaoSocial, cotaPessoa.razaoSocial, cotaPessoa.nome) as nome, ");		
		hql.append("        notaFiscalEntrada.valorBruto as valorNota, ");		
		hql.append("        0 as contemDiferenca ");
		
		hql.append(getSqlFromEWhereNotaEntrada(filtro));
		
		hql.append(getOrderBy(filtro));
		
		Query query =  getSession().createQuery(hql.toString());
		
		buscarParametros(filtro, query);
		
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
		
		boolean usarAnd = false;
		
		if(filtro.getCota() != null) {			
			hql.append( (usarAnd ? " and ":" where ") +" cota.id = :idCota ");
			usarAnd = true;
		}
		
		if(filtro.getFornecedor() != null) {			
			hql.append( (usarAnd ? " and ":" where ") +" fornecedor.id = :idFornecedor ");
			usarAnd = true;
		}
		
		if(filtro.getDataInicial() != null && filtro.getDataFinal() != null){
			hql.append( (usarAnd ? " and ":" where ") +" date(notaCota.dataEmissao) between :dataInicial and :dataFinal ");
			usarAnd = true;			
		}

		return hql.toString();
	}
	
	private String getOrderBy(FiltroEntradaNFETerceiros filtro){
		
		if(filtro.getPaginacao() == null || filtro.getPaginacao().getSortColumn() == null){
			return "";
		}
		StringBuilder hql = new StringBuilder();
		
		hql.append(" order by nota.numero ");
		
		if (filtro.getPaginacao().getOrdenacao() != null) {
			hql.append( filtro.getPaginacao().getOrdenacao().toString());
		}
		
		return hql.toString();
	}
	
	private void buscarParametros(FiltroEntradaNFETerceiros filtro, Query query){
		
		if(filtro.getCota() != null) { 
			query.setParameter("idCota", filtro.getCota().getPessoa().getId());
		}

		if(filtro.getFornecedor() != null) { 
			query.setParameter("idFornecedor", filtro.getFornecedor().getJuridica().getId());
		}

		if(filtro.getDataInicial() != null && filtro.getDataFinal() != null){
			query.setParameter("dataInicial", filtro.getDataInicial());
			query.setParameter("dataFinal", filtro.getDataFinal());
		}		

	}

	@Override
	public Integer buscarTotalNotasRecebidas(
			FiltroEntradaNFETerceiros filtro) {
		
		StringBuilder hql = new StringBuilder();
		hql.append(" select count(nota) ");
		
		if(filtro.getStatusNotaFiscalEntrada().equals(StatusNotaFiscalEntrada.RECEBIDA)){
			hql.append(getSqlFromEWhereNotaEntrada(filtro));			
		}else{
			hql.append(getSqlFromEWhereNotaPendente(filtro));
		}
		
		Query query =  getSession().createQuery(hql.toString());
		
		buscarParametros(filtro, query);
		
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
		hql.append("        tipoNotaFiscal.tipoOperacao as tipoNotaFiscal, ");
		hql.append("        (conferenciasEncalhe.qtdeInformada * conferenciasEncalhe.precoCapaInformado ) as valorNota, ");
		hql.append("        (conferenciasEncalhe.qtde * conferenciasEncalhe.precoCapaInformado ) as valorReal, ");
		hql.append("        ((conferenciasEncalhe.qtdeInformada * conferenciasEncalhe.precoCapaInformado) -  (conferenciasEncalhe.qtde * conferenciasEncalhe.precoCapaInformado)) as diferenca, ");
		hql.append("        notaFiscalEntradaCota.numero as numeroNfe, ");
		hql.append("        notaFiscalEntradaCota.serie as serie, ");
		hql.append("        notaFiscalEntradaCota.chaveAcesso as chaveAcesso, ");
		hql.append("        notaFiscalEntradaCota.id as idNotaFiscalEntrada ");
		
		hql.append(getSqlFromEWhereNotaPendente(filtro));
		
		hql.append(getOrderByNotasPendentes(filtro));
		
		Query query =  getSession().createQuery(hql.toString());
		
		buscarParametros(filtro, query);
		
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
		hql.append(" LEFT JOIN controleConferenciaEncalheCota.notaFiscalEntradaCota as notaFiscalEntradaCota ");
		hql.append(" LEFT JOIN notaFiscalEntradaCota.cota as cota ");
		hql.append(" LEFT JOIN cota.pessoa as pessoa ");
		hql.append(" LEFT JOIN notaFiscalEntradaCota.tipoNotaFiscal as tipoNotaFiscal ");
		hql.append(" LEFT JOIN controleConferenciaEncalheCota.conferenciasEncalhe as conferenciasEncalhe");
		
		
		hql.append(" where tipoNotaFiscal.tipoOperacao = 'ENTRADA' ");
		
		if(StatusNotaFiscalEntrada.PENDENTE_RECEBIMENTO.equals(filtro.getStatusNotaFiscalEntrada())){
			hql.append(" and ((conferenciasEncalhe.qtdeInformada * conferenciasEncalhe.precoCapaInformado) -  (conferenciasEncalhe.qtde * conferenciasEncalhe.precoCapaInformado)) > 0 ");
			
		}else if(StatusNotaFiscalEntrada.PENDENTE_EMISAO.equals(filtro.getStatusNotaFiscalEntrada())){
			hql.append(" and ((conferenciasEncalhe.qtdeInformada * conferenciasEncalhe.precoCapaInformado) -  (conferenciasEncalhe.qtde * conferenciasEncalhe.precoCapaInformado)) < 0 ");
		}
		
		boolean usarAnd = false;
		
		if(filtro.getCota() != null) {			
			hql.append( (usarAnd ? " and ":" where ") +" cota.id = :idCota ");
			usarAnd = true;
		}
		
//		if(filtro.getFornecedor() != null) {			
//			hql.append( (usarAnd ? " and ":" where ") +" fornecedor.id = :idFornecedor ");
//			usarAnd = true;
//		}
		
		if(filtro.getDataInicial() != null && filtro.getDataFinal() != null){
			hql.append( (usarAnd ? " and ":" where ") +" date(notaCota.dataEmissao) between :dataInicial and :dataFinal ");
			usarAnd = true;			
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
			FiltroEntradaNFETerceiros filtro) {
		
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
		
		hql.append(getHqlFromEWhereItensPendentes(filtro));
		
		Query query =  getSession().createQuery(hql.toString());
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				ItemNotaFiscalPendenteDTO.class));
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());			
		 
		return query.list();		 
		
	}
	
	private String getHqlFromEWhereItensPendentes(FiltroEntradaNFETerceiros filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" from ItemNotaFiscalEntrada as item, ControleConferenciaEncalheCota as confCota ");
		hql.append(" LEFT JOIN item.notaFiscal as nf ");
		hql.append(" LEFT JOIN item.produtoEdicao as produtoEdicao ");
		hql.append(" LEFT JOIN produtoEdicao.produto as produto ");
		hql.append(" LEFT JOIN produto.fornecedores as fornecedores ");
		hql.append(" left join confCota.conferenciasEncalhe as conf  ");
		hql.append(" left join conf.chamadaEncalheCota as chamadaCota  ");
		hql.append(" left join chamadaCota.chamadaEncalhe chamadaEncalhe  ");
		
		hql.append(" WHERE confCota.notaFiscalEntradaCota.id = nf.id  and nf.id = :idNota ");
		
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
	public Integer buscarTodasItensPorNota(FiltroEntradaNFETerceiros filtro) {
		
		StringBuilder hql = new StringBuilder();
		hql.append(" select count(produto.codigo) ");		
		
		hql.append(getHqlFromEWhereItensPendentes(filtro));
		
		Query query =  getSession().createQuery(hql.toString());
		
		Long totalRegistros = (Long) query.uniqueResult();
		
		return (totalRegistros == null) ? 0 : totalRegistros.intValue();
		 
	}
	
}

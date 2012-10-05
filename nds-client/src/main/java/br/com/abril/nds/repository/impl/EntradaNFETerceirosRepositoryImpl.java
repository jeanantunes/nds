package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ConsultaNFENotasPendentesDTO;
import br.com.abril.nds.dto.ConsultaEntradaNFETerceirosDTO;
import br.com.abril.nds.dto.ItemNotaFiscalPendenteDTO;
import br.com.abril.nds.dto.filtro.FiltroEntradaNFETerceiros;
import br.com.abril.nds.model.fiscal.NotaFiscalEntrada;
import br.com.abril.nds.repository.EntradaNFETerceirosRepository;

@Repository
public class EntradaNFETerceirosRepositoryImpl extends AbstractRepositoryModel<NotaFiscalEntrada, Long> implements
		EntradaNFETerceirosRepository {

	public EntradaNFETerceirosRepositoryImpl() {
		super(NotaFiscalEntrada.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ConsultaEntradaNFETerceirosDTO> buscarNFNotasRecebidas(FiltroEntradaNFETerceiros filtro, boolean limitar) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT nota.numero as numeroNfe, ");
		hql.append("        nota.serie as serie, ");
		hql.append("        nota.chaveAcesso as chaveAcesso, ");
		hql.append("        nota.dataEmissao as dataEmissao, ");
		hql.append("        tipoNotaFiscal.nopDescricao as tipoNotaFiscal, ");
		hql.append("        COALESCE(emitente.razaoSocial, emitente.nome) as nome, ");		
		hql.append("        nota.valorBruto as valorNota, ");		
		hql.append("        false as contemDiferenca ");
		
		hql.append(getSqlFromEWhereNotaEntrada(filtro));
		
		hql.append(getOrderBy(filtro));
		
		Query query =  getSession().createQuery(hql.toString());
		
		buscarParametros(filtro, query);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				ConsultaEntradaNFETerceirosDTO.class));
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null && limitar) 
			query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
		 
		return query.list();
	}
	
	private String getSqlFromEWhereNotaEntrada(FiltroEntradaNFETerceiros filtro) {
		
		StringBuilder hql = new StringBuilder();
	
		hql.append(" FROM NotaFiscalEntrada as nota ");
		hql.append(" LEFT JOIN nota.tipoNotaFiscal as tipoNotaFiscal ");
		hql.append(" LEFT JOIN nota.cota as cota ");
		hql.append(" LEFT JOIN nota.fornecedor as fornecedor ");
		
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
		
		if(filtro.getStatusNotaFiscalEntrada().name().equals("RECEBIDA")){
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
	public List<ConsultaNFENotasPendentesDTO> buscarNFNotasPendentes(
			FiltroEntradaNFETerceiros filtro, boolean limitar) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT cota.numeroCota as numeroCota, ");
		hql.append("pessoa.nome as nome, ");		
		hql.append("conf.data as dataEncalhe, ");		
		hql.append("tipo.tipoOperacao as tipoNota, ");
		hql.append("(conf.qtdeInformada * conf.precoCapaInformado ) as vlrNota, ");
		hql.append("(conf.qtde * conf.precoCapaInformado ) as vlrReal, ");
		hql.append("((conf.qtdeInformada * conf.precoCapaInformado) -  (conf.qtde * conf.precoCapaInformado)) as diferenca, ");
		hql.append(" notaCota.numero as numeroNfe, ");
		hql.append(" notaCota.serie as serie, ");
		hql.append(" notaCota.chaveAcesso as chaveAcesso, ");
		hql.append(" notaCota.id as idNotaFiscalEntrada ");
		
		hql.append(getSqlFromEWhereNotaPendente(filtro));
		
		hql.append(getOrderByNotasPendentes(filtro));
		
		Query query =  getSession().createQuery(hql.toString());
		
		buscarParametros(filtro, query);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				ConsultaNFENotasPendentesDTO.class));
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null && limitar) 
			query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());			
		 
		return query.list();
		 
	}
	
	private String getSqlFromEWhereNotaPendente(FiltroEntradaNFETerceiros filtro) {
		
		StringBuilder hql = new StringBuilder();
	
		hql.append(" from ControleConferenciaEncalheCota as controleCota ");
		hql.append(" LEFT JOIN controleCota.notaFiscalEntradaCota as notaCota ");
		hql.append(" LEFT JOIN notaCota.cota as cota ");
		hql.append(" LEFT JOIN cota.pessoa as pessoa ");
		hql.append(" LEFT JOIN notaCota.tipoNotaFiscal as tipo ");
		hql.append(" LEFT JOIN controleCota.conferenciasEncalhe as conf");
		
		
		hql.append(" where tipo.tipoOperacao = 'ENTRADA' ");
		
		if(filtro.getStatusNotaFiscalEntrada().name().equals("PENDENTE_RECEBIMENTO")){
			hql.append(" and ((conf.qtdeInformada * conf.precoCapaInformado) -  (conf.qtde * conf.precoCapaInformado)) > 0 ");
			
		}else if(filtro.getStatusNotaFiscalEntrada().name().equals("PENDENTE_EMISAO")){
			hql.append(" and ((conf.qtdeInformada * conf.precoCapaInformado) -  (conf.qtde * conf.precoCapaInformado)) < 0 ");
		}
		
		/*if(filtro.getCodigoCota() != null && !filtro.getCodigoCota().equals("") ) {			
			hql.append( " and cota.numeroCota = :numeroCota ");
		}
		if(filtro.getData() != null){
			hql.append( " and date(notaCota.dataEmissao) = :data ");
		}*/

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
//		query.setParameter("idNota", filtro.getCodigoNota());
		
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
//		query.setParameter("idNota", filtro.getCodigoNota());
		
		Long totalRegistros = (Long) query.uniqueResult();
		
		return (totalRegistros == null) ? 0 : totalRegistros.intValue();
		 
	}
	
}

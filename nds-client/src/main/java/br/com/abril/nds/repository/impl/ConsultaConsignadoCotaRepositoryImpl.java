package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ConsultaConsignadoCotaDTO;
import br.com.abril.nds.dto.ConsultaConsignadoCotaPeloFornecedorDTO;
import br.com.abril.nds.dto.TotalConsultaConsignadoCotaDetalhado;
import br.com.abril.nds.dto.filtro.FiltroConsultaConsignadoCotaDTO;
import br.com.abril.nds.model.cadastro.TipoCota;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque.Dominio;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.OperacaoEstoque;
import br.com.abril.nds.repository.ConsultaConsignadoCotaRepository;
import br.com.abril.nds.repository.TipoMovimentoEstoqueRepository;

@Repository
public class ConsultaConsignadoCotaRepositoryImpl extends AbstractRepositoryModel<MovimentoEstoqueCota, Long> implements
		ConsultaConsignadoCotaRepository {

	@Autowired
	private TipoMovimentoEstoqueRepository tipoMovimentoEstoqueRepository;

	public ConsultaConsignadoCotaRepositoryImpl() {
		super(MovimentoEstoqueCota.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ConsultaConsignadoCotaDTO> buscarConsignadoCota(
			FiltroConsultaConsignadoCotaDTO filtro, boolean limitar) {
		 
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT produto.codigo as codigoProduto, ")
		   .append("        produto.nome as nomeProduto, ")	
		   .append("        cota.id as cotaId, ")
		   .append("        pe.id as produtoEdicaoId, ")	
		   .append("        pe.numeroEdicao as numeroEdicao, ")
		   .append("        pessoa.razaoSocial as nomeFornecedor, ")
		   .append("        CASE WHEN movimentoEstoqueCotaFuro is not null THEN movimento.dataCriacao ")
		   .append("			 ELSE (CASE WHEN tipoMovimento = :tipoMovimentoCotaFuro THEN movimento.dataCriacao ELSE lancamento.dataLancamentoDistribuidor END) END as dataLancamento, ")
		   .append("        pe.precoVenda as precoCapa, ")
		   .append("        movimento.valoresAplicados.valorDesconto as desconto, ")
		   .append("        (pe.precoVenda - (pe.precoVenda * (movimento.valoresAplicados.valorDesconto) / 100)) as precoDesconto, ")
		   .append("        CASE WHEN movimento.tipoMovimento.operacaoEstoque  = :tipoOperacaoEntrada THEN (movimento.qtde) ELSE (movimento.qtde*-1) END as reparte, ")		
		   .append("        CASE WHEN movimento.tipoMovimento.operacaoEstoque  = :tipoOperacaoEntrada THEN (pe.precoVenda * movimento.qtde) ELSE (pe.precoVenda * movimento.qtde*-1) END as total, ")
		   .append("        CASE WHEN movimento.tipoMovimento.operacaoEstoque  = :tipoOperacaoEntrada THEN ((pe.precoVenda - (pe.precoVenda * (movimento.valoresAplicados.valorDesconto) / 100)) * movimento.qtde) ELSE ((pe.precoVenda - (pe.precoVenda * (movimento.valoresAplicados.valorDesconto) / 100)) * movimento.qtde*-1) END as totalDesconto ");
		
		hql.append(getHQLFromEWhereConsignadoCota(filtro));
		
		hql.append(getOrderBy(filtro));

		Query query =  getSession().createQuery(hql.toString());
				
		buscarParametrosConsignadoCota(query, filtro);
		
		query.setParameter("tipoMovimentoCotaFuro", tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.ESTORNO_REPARTE_FURO_PUBLICACAO));
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				ConsultaConsignadoCotaDTO.class));
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null && limitar) 
			query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
		
		return query.list();
				
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ConsultaConsignadoCotaPeloFornecedorDTO> buscarMovimentosCotaPeloFornecedor(
			FiltroConsultaConsignadoCotaDTO filtro, boolean limitar) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT cota.numeroCota as numeroCota, ")
		   .append("        (CASE WHEN (pessoaCota.nome is not null) then ( pessoaCota.nome ) ")
		   .append(" 			WHEN (pessoaCota.razaoSocial is not null) then ( pessoaCota.razaoSocial )")
		   .append(" 			ELSE null END) as nomeCota, ")
		   .append("        SUM(movimento.qtde *  ")
		   .append("            CASE WHEN tipoMovimento.grupoMovimentoEstoque IN (:tipoMovimentoEntrada) THEN 1 ")
		   .append("                 WHEN tipoMovimento.grupoMovimentoEstoque IN (:tipoMovimentoSaida) THEN -1 ")
		   .append("                 ELSE 0 END) as consignado, ")
		   .append("        SUM(pe.precoVenda * movimento.qtde *  ")
		   .append("            CASE WHEN tipoMovimento.grupoMovimentoEstoque IN (:tipoMovimentoEntrada) THEN 1 ")
		   .append("                 WHEN tipoMovimento.grupoMovimentoEstoque IN (:tipoMovimentoSaida) THEN -1 ")
		   .append("                 ELSE 0 END) as total, ")
		   .append("        SUM((pe.precoVenda - (pe.precoVenda * (movimento.valoresAplicados.valorDesconto) / 100)) * movimento.qtde *  ")
		   .append("            CASE WHEN tipoMovimento.grupoMovimentoEstoque IN (:tipoMovimentoEntrada) THEN 1 ")
		   .append("                 WHEN tipoMovimento.grupoMovimentoEstoque IN (:tipoMovimentoSaida) THEN -1 ")
		   .append("                 ELSE 0 END)  as totalDesconto, ")
		   .append("        pessoa.razaoSocial as nomeFornecedor,  ")
		   .append("        fornecedor.id as idFornecedor  ");
		
		hql.append(getHQLFromEWhereConsignadoCota(filtro));
		
		hql.append(getGroupBy(filtro));

		hql.append(getOrderBy(filtro));
		
		Query query =  getSession().createQuery(hql.toString());
		
		buscarParametrosConsignadoCota(query, filtro);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				ConsultaConsignadoCotaPeloFornecedorDTO.class));
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null && limitar) 
			query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
				
		return query.list();
		 
	}
	
	@Override
	public Long buscarTodasMovimentacoesPorCota(
			FiltroConsultaConsignadoCotaDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT count(movimento)  ");
		
		hql.append(getHQLFromEWhereConsignadoCota(filtro));

		Query query =  getSession().createQuery(hql.toString());
		
		buscarParametrosConsignadoCota(query, filtro);
		
		return (Long) query.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Long buscarTodosMovimentosCotaPeloFornecedor(
			FiltroConsultaConsignadoCotaDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT count(movimento)  ");
		
		hql.append(getHQLFromEWhereConsignadoCota(filtro));
		
		hql.append(getGroupBy(filtro));

		Query query =  getSession().createQuery(hql.toString());
		
		buscarParametrosConsignadoCota(query, filtro);
		
		List<Long> totalRegistros = query.list();
		
		return (totalRegistros == null) ? 0L : totalRegistros.size();
	}

	@Override
	public BigDecimal buscarTotalGeralDaCota(
			FiltroConsultaConsignadoCotaDTO filtro) {
		

		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT SUM(( (pe.precoVenda - (pe.precoVenda * (movimento.valoresAplicados.valorDesconto) / 100)) * movimento.qtde ");

		hql.append("            * CASE WHEN tipoMovimento.operacaoEstoque = (:tipoOperacaoEntrada) THEN 1 ")
		   .append("                   WHEN tipoMovimento.operacaoEstoque = (:tipoOperacaoSaida) THEN -1 ")
		   .append("                   ELSE 0 END ");

		// TODO: Verificar pq existe isso se mesmo com a cota, isto deve ser aplicado na consulta consignado cota
		/*if (filtro.getIdCota() == null) {
			hql.append("            * CASE WHEN tipoMovimento.grupoMovimentoEstoque IN (:tipoMovimentoEntrada) THEN 1 ")
			   .append("                   WHEN tipoMovimento.grupoMovimentoEstoque IN (:tipoMovimentoSaida) THEN -1 ")
			   .append("                   ELSE 0 END ");
		}*/
		hql.append("           )) ");
		
		hql.append(getHQLFromEWhereConsignadoCota(filtro));
		
		Query query =  getSession().createQuery(hql.toString());
		
		buscarParametrosConsignadoCota(query, filtro);
		
		BigDecimal totalRegistros = (BigDecimal) query.uniqueResult();
		
		return (totalRegistros == null) ? BigDecimal.ZERO : totalRegistros;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TotalConsultaConsignadoCotaDetalhado> buscarTotalDetalhado(
			FiltroConsultaConsignadoCotaDTO filtro) {
		 
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT SUM(( (pe.precoVenda - (pe.precoVenda * (movimento.valoresAplicados.valorDesconto) / 100)) * movimento.qtde ");

		if (filtro.getIdCota() == null) {
			hql.append("            * CASE WHEN tipoMovimento.grupoMovimentoEstoque IN (:tipoMovimentoEntrada) THEN 1 ")
			   .append("                   WHEN tipoMovimento.grupoMovimentoEstoque IN (:tipoMovimentoSaida) THEN -1 ")
			   .append("                   ELSE 0 END ");
		}
		hql.append("            )) as total, ");
		
		hql.append("pessoa.razaoSocial as nomeFornecedor");
		
		hql.append(getHQLFromEWhereConsignadoCota(filtro));
		
		hql.append(getGroupByTotalDetalhado(filtro));

		Query query =  getSession().createQuery(hql.toString());
		
		buscarParametrosConsignadoCota(query, filtro);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				TotalConsultaConsignadoCotaDetalhado.class));
		
		return query.list();
	}
	
	private String getHQLFromEWhereConsignadoCota(FiltroConsultaConsignadoCotaDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
	
		hql.append(" FROM MovimentoEstoqueCota movimento ");
		hql.append(" join movimento.lancamento lancamento ");
		hql.append(" LEFT JOIN movimento.cota as cota ");
		hql.append(" LEFT JOIN movimento.tipoMovimento as tipoMovimento ");
		hql.append(" LEFT JOIN movimento.produtoEdicao as pe ");
		hql.append(" LEFT JOIN pe.produto as produto ");
		hql.append(" LEFT JOIN cota.parametroCobranca parametroCobranca ");
		hql.append(" LEFT JOIN produto.fornecedores as fornecedor ");
		hql.append(" LEFT JOIN fornecedor.juridica as pessoa ");		
		hql.append(" LEFT JOIN cota.pessoa as pessoaCota ");
		hql.append(" LEFT JOIN movimento.movimentoEstoqueCotaFuro as movimentoEstoqueCotaFuro ");
		
		hql.append(" WHERE tipoMovimento.grupoMovimentoEstoque IN (:tipoMovimento) " );
		hql.append("   AND parametroCobranca.tipoCota = :tipoCota  " );
		
		if(filtro.getIdCota() != null ) { 
			hql.append("   AND cota.id = :numeroCota");			
		}
		if(filtro.getIdFornecedor() != null) { 
			hql.append("   AND fornecedor.id = :idFornecedor");
		}
		
		//Filtra os registro que ja teve enclahe finalizado
		hql.append(" AND not exists ( ")
		.append(" select chamadaCota  from ConferenciaEncalhe conferencia " +
				" join   conferencia.chamadaEncalheCota chamadaCota " +
				" join   chamadaCota.chamadaEncalhe chamadaEncalhe " +
				" join   conferencia.controleConferenciaEncalheCota controleCota " +
				" join   controleCota.controleConferenciaEncalhe controle, "  +
				" FechamentoEncalhe fechamentoEncalhe" )
		.append(" where  chamadaEncalhe.produtoEdicao.id = pe.id and chamadaCota.cota.id = cota.id " +
				" and fechamentoEncalhe.fechamentoEncalhePK.produtoEdicao.id = pe.id " +
				" and fechamentoEncalhe.fechamentoEncalhePK.dataEncalhe = controleCota.dataOperacao ) ");

		return hql.toString();
	}
	
	private String getOrderBy(FiltroConsultaConsignadoCotaDTO filtro){
		
		if(filtro.getPaginacao() == null || filtro.getPaginacao().getSortColumn() == null){
			return "";
		}
		StringBuilder hql = new StringBuilder();
		
		boolean ordenar = false; 
		
		if(filtro.getIdCota() != null && !ordenar){
			hql.append(" ORDER BY cota.numeroCota ");
			ordenar = true;
		}
		
		if(filtro.getIdFornecedor() != null && !ordenar){
			hql.append(" ORDER BY cota.numeroCota ");
			ordenar = true;
		}
		
		
		if (filtro.getPaginacao().getOrdenacao() != null && ordenar) {
			hql.append( filtro.getPaginacao().getOrdenacao().toString());
		}
		
		return hql.toString();
	}

	private String getGroupBy(FiltroConsultaConsignadoCotaDTO filtro){
		StringBuilder hql = new StringBuilder();
		
	    hql.append("  GROUP BY cota.numeroCota,  ")
		   .append("          fornecedor.id ");

		return hql.toString();	
	}
	
	private String getGroupByTotalDetalhado(FiltroConsultaConsignadoCotaDTO filtro){
		
		StringBuilder hql = new StringBuilder();
		
	    hql.append("  GROUP BY fornecedor.id ");

		return hql.toString();	
	}
	
	private void buscarParametrosConsignadoCota(Query query, FiltroConsultaConsignadoCotaDTO filtro){
		
		if(query.getQueryString().contains(":tipoOperacaoEntrada")) {
			query.setParameter("tipoOperacaoEntrada", OperacaoEstoque.ENTRADA);
		}

		if(query.getQueryString().contains(":tipoOperacaoSaida")) {
			query.setParameter("tipoOperacaoSaida", OperacaoEstoque.SAIDA);
		}

		List<GrupoMovimentoEstoque> listaGrupoMovimentoEstoquesEntrada = new ArrayList<GrupoMovimentoEstoque>();
		List<GrupoMovimentoEstoque> listaGrupoMovimentoEstoquesSaida = new ArrayList<GrupoMovimentoEstoque>();
		List<GrupoMovimentoEstoque> listaGrupoMovimentoEstoques = new ArrayList<GrupoMovimentoEstoque>();
		
		if (filtro.getIdCota() == null) { 
			for(GrupoMovimentoEstoque grupoMovimentoEstoque: GrupoMovimentoEstoque.values()) {
				if(Dominio.COTA.equals(grupoMovimentoEstoque.getDominio())) {
					if(OperacaoEstoque.ENTRADA.equals(grupoMovimentoEstoque.getOperacaoEstoque())){
						listaGrupoMovimentoEstoquesEntrada.add(grupoMovimentoEstoque);
					}else if(OperacaoEstoque.SAIDA.equals(grupoMovimentoEstoque.getOperacaoEstoque())){
						listaGrupoMovimentoEstoquesSaida.add(grupoMovimentoEstoque);
					}
				}
			} 
			listaGrupoMovimentoEstoques.addAll(listaGrupoMovimentoEstoquesEntrada);
			listaGrupoMovimentoEstoques.addAll(listaGrupoMovimentoEstoquesSaida);

		} else {
			listaGrupoMovimentoEstoquesEntrada.add(GrupoMovimentoEstoque.RECEBIMENTO_REPARTE);
			listaGrupoMovimentoEstoques = listaGrupoMovimentoEstoquesEntrada;
			
			for(GrupoMovimentoEstoque grupoMovimentoEstoque: GrupoMovimentoEstoque.values()) {
				if(Dominio.COTA.equals(grupoMovimentoEstoque.getDominio())) {
					if(OperacaoEstoque.SAIDA.equals(grupoMovimentoEstoque.getOperacaoEstoque())){
						listaGrupoMovimentoEstoquesSaida.add(grupoMovimentoEstoque);
					}
				}
			}
			listaGrupoMovimentoEstoques.addAll(listaGrupoMovimentoEstoquesSaida);
			
		}
		
		
		if(query.getQueryString().contains(":tipoMovimentoEntrada")) {
			query.setParameterList("tipoMovimentoEntrada", listaGrupoMovimentoEstoquesEntrada);
		}
		
		if(query.getQueryString().contains(":tipoMovimentoSaida")) {
			query.setParameterList("tipoMovimentoSaida", listaGrupoMovimentoEstoquesSaida);
		}
		
		query.setParameterList("tipoMovimento", listaGrupoMovimentoEstoques);
		query.setParameter("tipoCota", TipoCota.CONSIGNADO);
		
		if(filtro.getIdCota() != null ) { 
			query.setParameter("numeroCota", filtro.getIdCota());
		}
		if(filtro.getIdFornecedor() != null ) { 
			query.setParameter("idFornecedor", filtro.getIdFornecedor());
		}
	
	}
		
}

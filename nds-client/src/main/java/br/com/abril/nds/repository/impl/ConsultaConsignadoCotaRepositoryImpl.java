package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
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

@Repository
public class ConsultaConsignadoCotaRepositoryImpl extends AbstractRepositoryModel<MovimentoEstoqueCota, Long> implements
		ConsultaConsignadoCotaRepository {

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
		   .append("        pe.numeroEdicao as numeroEdicao, ")
		   .append("        pessoa.razaoSocial as nomeFornecedor, ")
		   .append("        movimento.data as dataLancamento, ")
		   .append("        pe.precoVenda as precoCapa, ")
		   .append("        ("+ this.getHQLDesconto() +") as desconto, ")
		   .append("        (pe.precoVenda - (pe.precoVenda * ("+ this.getHQLDesconto() +") / 100)) as precoDesconto, ")
		   .append("        SUM(movimento.qtde *  ")
		   .append("            CASE WHEN tipoMovimento.grupoMovimentoEstoque IN (:tipoMovimentoEntrada) THEN 1 ")
		   .append("                 WHEN tipoMovimento.grupoMovimentoEstoque IN (:tipoMovimentoSaida) THEN -1 ")
		   .append("                 ELSE 0 END) as reparte, ")		
		   .append("        SUM(pe.precoVenda * movimento.qtde *  ")
		   .append("            CASE WHEN tipoMovimento.grupoMovimentoEstoque IN (:tipoMovimentoEntrada) THEN 1 ")
		   .append("                 WHEN tipoMovimento.grupoMovimentoEstoque IN (:tipoMovimentoSaida) THEN -1 ")
		   .append("                 ELSE 0 END) as total, ")
		   .append("        SUM( (pe.precoVenda - (pe.precoVenda * ("+ this.getHQLDesconto() +") / 100)) * movimento.qtde *  ")
		   .append("            CASE WHEN tipoMovimento.grupoMovimentoEstoque IN (:tipoMovimentoEntrada) THEN 1 ")
		   .append("                 WHEN tipoMovimento.grupoMovimentoEstoque IN (:tipoMovimentoSaida) THEN -1 ")
		   .append("                 ELSE 0 END)  as totalDesconto ");
		
		hql.append(getHQLFromEWhereConsignadoCota(filtro));
		
		hql.append(getGroupBy(filtro));

		hql.append(getOrderBy(filtro));

		Query query =  getSession().createQuery(hql.toString());
		
		buscarParametrosConsignadoCota(query, filtro);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				ConsultaConsignadoCotaDTO.class));
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null && limitar) 
			query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
				
		return  query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ConsultaConsignadoCotaPeloFornecedorDTO> buscarMovimentosCotaPeloFornecedor(
			FiltroConsultaConsignadoCotaDTO filtro, boolean limitar) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT cota.numeroCota as numeroCota, ")
		   .append("        pessoaCota.nome as nomeCota, ")
		   .append("        SUM(movimento.qtde *  ")
		   .append("            CASE WHEN tipoMovimento.grupoMovimentoEstoque IN (:tipoMovimentoEntrada) THEN 1 ")
		   .append("                 WHEN tipoMovimento.grupoMovimentoEstoque IN (:tipoMovimentoSaida) THEN -1 ")
		   .append("                 ELSE 0 END) as consignado, ")
		   .append("        SUM(pe.precoVenda * movimento.qtde *  ")
		   .append("            CASE WHEN tipoMovimento.grupoMovimentoEstoque IN (:tipoMovimentoEntrada) THEN 1 ")
		   .append("                 WHEN tipoMovimento.grupoMovimentoEstoque IN (:tipoMovimentoSaida) THEN -1 ")
		   .append("                 ELSE 0 END) as total, ")
		   .append("        SUM((pe.precoVenda - (pe.precoVenda * ("+ this.getHQLDesconto() +") / 100)) * movimento.qtde *  ")
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
				
		return  query.list();
		 
	}
	
	@Override
	public Integer buscarTodasMovimentacoesPorCota(
			FiltroConsultaConsignadoCotaDTO filtro, boolean limitar) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT count(movimento)  ");
		
		hql.append(getHQLFromEWhereConsignadoCota(filtro));
		
		hql.append(getGroupBy(filtro));

		Query query =  getSession().createQuery(hql.toString());
		
		buscarParametrosConsignadoCota(query, filtro);
		
		List<Long> totalRegistros = query.list();
		
		//Long totalRegistros = (Long) query.uniqueResult();
		
//		for(Long totalRegistro: listaTotalRegistros){
//			totalRegistros = totalRegistros + totalRegistro;
//		}
		
		return (totalRegistros == null) ? 0 : totalRegistros.size();
		
		
	}

	@Override
	public BigDecimal buscarTotalGeralDaCota(
			FiltroConsultaConsignadoCotaDTO filtro) {
		

		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT SUM(( (pe.precoVenda - (pe.precoVenda * ("+ this.getHQLDesconto() +") / 100)) * movimento.qtde *  ")
		   .append("            CASE WHEN tipoMovimento.grupoMovimentoEstoque IN (:tipoMovimentoEntrada) THEN 1 ")
		   .append("                 WHEN tipoMovimento.grupoMovimentoEstoque IN (:tipoMovimentoSaida) THEN -1 ")
		   .append("                 ELSE 0 END)) ");
		
		hql.append(getHQLFromEWhereConsignadoCota(filtro));
		
//		hql.append(getGroupBy(filtro));

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
		
		hql.append("SELECT SUM(( (pe.precoVenda - (pe.precoVenda * ("+ this.getHQLDesconto() +") / 100)) * movimento.qtde *  ")
		   .append("            CASE WHEN tipoMovimento.grupoMovimentoEstoque IN (:tipoMovimentoEntrada) THEN 1 ")
		   .append("                 WHEN tipoMovimento.grupoMovimentoEstoque IN (:tipoMovimentoSaida) THEN -1 ")
		   .append("                 ELSE 0 END)) as total, ");
		hql.append("pessoa.razaoSocial as nomeFornecedor");
		
		hql.append(getHQLFromEWhereConsignadoCota(filtro));
		
		hql.append(getGroupBy(filtro));

		Query query =  getSession().createQuery(hql.toString());
		
		buscarParametrosConsignadoCota(query, filtro);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				TotalConsultaConsignadoCotaDetalhado.class));
		
		return query.list();
	}
	
	private String getHQLFromEWhereConsignadoCota(FiltroConsultaConsignadoCotaDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
	
		hql.append(" FROM MovimentoEstoqueCota movimento ");
		hql.append(" LEFT JOIN movimento.cota as cota ");
		hql.append(" LEFT JOIN movimento.tipoMovimento as tipoMovimento ");
		hql.append(" LEFT JOIN movimento.produtoEdicao as pe ");
		hql.append(" LEFT JOIN pe.produto as produto ");
		hql.append(" LEFT JOIN cota.parametroCobranca parametroCobranca ");
		hql.append(" LEFT JOIN produto.fornecedores as fornecedor ");
		hql.append(" LEFT JOIN fornecedor.juridica as pessoa ");		
		if(filtro.getIdCota() == null || filtro.getIdFornecedor() != null){
			hql.append(" LEFT JOIN cota.pessoa as pessoaCota ");
		}

		hql.append(" WHERE tipoMovimento.grupoMovimentoEstoque IN (:tipoMovimento) " );
		hql.append("   AND parametroCobranca.tipoCota = :tipoCota  " );
		
		if(filtro.getIdCota() != null ) { 
			hql.append("   AND cota.id = :numeroCota");			
		}
		if(filtro.getIdFornecedor() != null) { 
			hql.append("   AND fornecedor.id = :idFornecedor");
		}
		

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
		
		if(filtro.getIdCota() != null){
	        hql.append(" GROUP BY produto.codigo, ")
			   .append("          produto.nome, ")	
			   .append("          pe.numeroEdicao , ")
			   .append("          pessoa.razaoSocial,  ")
			   .append("          fornecedor.id, ")
			   .append("          movimento.data, ")
			   .append("          pe.precoVenda ");
		} else {
	        hql.append("  GROUP BY cota.numeroCota,  ")
			   .append("          pessoaCota.nome,  ")
			   .append("          pessoa.razaoSocial,  ")
			   .append("          fornecedor.id ");
		}

		return hql.toString();	
	}
	
	private void buscarParametrosConsignadoCota(Query query, FiltroConsultaConsignadoCotaDTO filtro){
		
		List<GrupoMovimentoEstoque> listaGrupoMovimentoEstoquesEntrada = new ArrayList<GrupoMovimentoEstoque>();
		List<GrupoMovimentoEstoque> listaGrupoMovimentoEstoquesSaida = new ArrayList<GrupoMovimentoEstoque>();
		
		for(GrupoMovimentoEstoque grupoMovimentoEstoque: GrupoMovimentoEstoque.values()) {
			if(Dominio.COTA.equals(grupoMovimentoEstoque.getDominio())) {
				if(OperacaoEstoque.ENTRADA.equals(grupoMovimentoEstoque.getOperacaoEstoque())){
					listaGrupoMovimentoEstoquesEntrada.add(grupoMovimentoEstoque);
				}else if(OperacaoEstoque.SAIDA.equals(grupoMovimentoEstoque.getOperacaoEstoque())){
					listaGrupoMovimentoEstoquesSaida.add(grupoMovimentoEstoque);
				}
			}
		}
		
		List<GrupoMovimentoEstoque> listaGrupoMovimentoEstoques = new ArrayList<GrupoMovimentoEstoque>();
		listaGrupoMovimentoEstoques.addAll(listaGrupoMovimentoEstoquesEntrada);
		listaGrupoMovimentoEstoques.addAll(listaGrupoMovimentoEstoquesSaida);
		
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
	
	private String getHQLDesconto(){
		
		StringBuilder hql = new StringBuilder();

		hql.append(" COALESCE((SELECT view.desconto")
		   .append(" FROM ViewDesconto view ")
		   .append(" WHERE view.cotaId = cota.id ")
		   .append("   AND view.produtoEdicaoId = pe.id ")
		   .append("   AND view.fornecedorId = fornecedor.id),0) ");
		
		return hql.toString();
	}
	
}

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
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque.Dominio;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.OperacaoEstoque;
import br.com.abril.nds.model.estoque.StatusEstoqueFinanceiro;
import br.com.abril.nds.repository.AbstractRepositoryModel;
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
		
		hql.append(" SELECT  ")
		   .append(" produto.codigo as codigoProduto, 		")
		   .append(" produto.nome as nomeProduto, 			")	
		   .append(" cota.id as cotaId, 					")
		   .append(" pe.id as produtoEdicaoId, 				")	
		   .append(" pe.numeroEdicao as numeroEdicao, 		")
		   .append(" pessoa.razaoSocial as nomeFornecedor, 	")
		   .append(" case when lancamento is not null then lancamento.dataLancamentoDistribuidor else movimento.data end as dataLancamento,")
		   .append(" coalesce(movimento.valoresAplicados.precoVenda, pe.precoVenda, 0) as precoCapa, ")
		   
		   .append(" coalesce(movimento.valoresAplicados.valorDesconto, 0) as desconto, ")
		   .append(" coalesce(movimento.valoresAplicados.precoComDesconto, pe.precoVenda, 0) as precoDesconto, ")
		   
		   .append("	    (sum((case when tipoMovimento.operacaoEstoque = 'ENTRADA'  then movimento.qtde else 0 end) ")
		   .append("	      - (case when tipoMovimento.operacaoEstoque = 'SAIDA' then movimento.qtde else 0 end) )) as reparte, ")
		   
		   .append(" ( coalesce( ")
		   .append("		movimento.valoresAplicados.precoVenda, pe.precoVenda, 0) * ")
		   .append("	    (sum((case when tipoMovimento.operacaoEstoque = 'ENTRADA' then movimento.qtde else 0 end) ")
		   .append("	      - (case when tipoMovimento.operacaoEstoque = 'SAIDA' then movimento.qtde else 0 end) ))  ")
		   .append(" ) as total, ")
		   
		   .append(" ( coalesce( ")
		   .append("        movimento.valoresAplicados.precoComDesconto, pe.precoVenda, 0) * ")
		   .append("	    (sum((case when tipoMovimento.operacaoEstoque = 'ENTRADA' then movimento.qtde else 0 end) ")
		   .append("	      - (case when tipoMovimento.operacaoEstoque = 'SAIDA' then movimento.qtde else 0 end) ) ) ")
		   .append(" ) as totalDesconto ");
		   
		hql.append(createFromConsultaConsignadoCota(filtro));
		
		if (filtro.getPaginacao().getSortColumn() != null) {
			hql.append(" ORDER BY ");
			hql.append(filtro.getPaginacao().getSortColumn());		
		
			if (filtro.getPaginacao().getOrdenacao() != null) {
				hql.append(" ");
				hql.append( filtro.getPaginacao().getOrdenacao().toString());
			}
		}

		Query query =  getSession().createQuery(hql.toString());
		
		query.setParameter("tipoMovimentoEstorno", GrupoMovimentoEstoque.ESTORNO_REPARTE_COTA_FURO_PUBLICACAO);
		
		if(filtro.getIdCota() != null ) { 
			query.setParameter("idCota", filtro.getIdCota());
		}
		
		if(filtro.getIdFornecedor() != null ) { 
			query.setParameter("idFornecedor", filtro.getIdFornecedor());
		}
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				ConsultaConsignadoCotaDTO.class));
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null && limitar) 
			query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
		
		return query.list();
				
	}

	private String createFromConsultaConsignadoCota(FiltroConsultaConsignadoCotaDTO filtro) {
		StringBuilder hql = new StringBuilder();
		
		hql.append(" FROM MovimentoEstoqueCota movimento, ProdutoEdicao pe ");
		hql.append("  LEFT JOIN movimento.lancamento lancamento ");
		hql.append("  JOIN movimento.cota as cota ");
		hql.append("  JOIN movimento.tipoMovimento as tipoMovimento ");
		hql.append("  JOIN pe.produto as produto ");
		hql.append("  JOIN cota.parametroCobranca parametroCobranca ");
		hql.append("  JOIN produto.fornecedores as fornecedor ");
		hql.append("  JOIN fornecedor.juridica as pessoa ");		
		hql.append("  JOIN cota.pessoa as pessoaCota ");
		
		hql.append(" WHERE movimento.produtoEdicao.id = pe.id " );
		
		hql.append(" AND movimento.movimentoEstoqueCotaFuro is null " );
		
		hql.append(" AND movimento.tipoMovimento.grupoMovimentoEstoque not in (:tipoMovimentoEstorno) " );
		
		 if(filtro.getIdCota() != null ) { 
			hql.append(" AND cota.id = :idCota ");			
		}
		if(filtro.getIdFornecedor() != null) { 
			hql.append(" AND fornecedor.id = :idFornecedor ");
		}
		
		hql.append(" GROUP BY case when lancamento is not null then lancamento.dataLancamentoDistribuidor else movimento.data end ");
		
		hql.append(" HAVING sum( (case when tipoMovimento.operacaoEstoque = 'ENTRADA'  then movimento.qtde else 0 end)  ");
		hql.append("	-  (case when tipoMovimento.operacaoEstoque = 'SAIDA' then movimento.qtde else 0 end) ) <> 0 ");
		  
		
		return hql.toString();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ConsultaConsignadoCotaPeloFornecedorDTO> buscarMovimentosCotaPeloFornecedor(
			FiltroConsultaConsignadoCotaDTO filtro, boolean limitar) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT cota.numeroCota as numeroCota, 		")
		   .append(" (CASE WHEN (pessoaCota.nome is not null)	")
		   .append(" THEN ( pessoaCota.nome ) 					")
		   .append(" WHEN (pessoaCota.razaoSocial is not null) 	")
		   .append(" THEN (pessoaCota.razaoSocial)  			")
		   .append(" ELSE null END) as nomeCota, 			    ")
		   
		   .append(" SUM(movimento.qtde) as consignado, 				")
		   
		   .append(" SUM( coalesce(movimento.valoresAplicados.precoVenda, pe.precoVenda, 0)  * movimento.qtde) as total, ")
		   
		   .append(" SUM( coalesce(movimento.valoresAplicados.precoComDesconto, pe.precoVenda, 0) * movimento.qtde )  as totalDesconto, ")
		   
		   .append(" pessoa.razaoSocial as nomeFornecedor,  ")
		   
		   .append(" fornecedor.id as idFornecedor  ");
		
		hql.append(getHQLFromEWhereConsignadoCota(filtro));
		
		hql.append(getGroupBy(filtro));

		if (filtro.getPaginacao().getSortColumn() != null) {
			hql.append(" ORDER BY ");
			hql.append(filtro.getPaginacao().getSortColumn());		
		
			if (filtro.getPaginacao().getOrdenacao() != null) {
				hql.append(" ");
				hql.append( filtro.getPaginacao().getOrdenacao().toString());
			}
		}
		
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
	
	@SuppressWarnings("unchecked")
	@Override
	public Long buscarTodasMovimentacoesPorCota(
			FiltroConsultaConsignadoCotaDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT count(movimento)  ");
		
		hql.append(createFromConsultaConsignadoCota(filtro));
		
		Query query =  getSession().createQuery(hql.toString());
		
		if(filtro.getIdCota() != null ) { 
			query.setParameter("idCota", filtro.getIdCota());
		}
		
		if(filtro.getIdFornecedor() != null ) { 
			query.setParameter("idFornecedor", filtro.getIdFornecedor());
		}
		
		query.setParameter("tipoMovimentoEstorno", GrupoMovimentoEstoque.ESTORNO_REPARTE_COTA_FURO_PUBLICACAO);
		
		List<Long> totalRegistros = query.list();
		
		return (totalRegistros == null) ? 0L : totalRegistros.size();

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
		
		hql.append(" SELECT SUM( coalesce(movimento.valoresAplicados.precoComDesconto, pe.precoVenda, 0)  * movimento.qtde) ");
		
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
		
		hql.append("SELECT SUM(coalesce(movimento.valoresAplicados.precoComDesconto, pe.precoVenda, 0) * movimento.qtde) as total, ");
		
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
		hql.append(" LEFT join movimento.lancamento lancamento ");
		hql.append("  JOIN movimento.cota as cota ");
		hql.append("  JOIN movimento.tipoMovimento as tipoMovimento ");
		hql.append("  JOIN movimento.produtoEdicao as pe ");
		hql.append("  JOIN pe.produto as produto ");
		hql.append("  JOIN cota.parametroCobranca parametroCobranca ");
		hql.append("  JOIN produto.fornecedores as fornecedor ");
		hql.append("  JOIN fornecedor.juridica as pessoa ");		
		hql.append("  JOIN cota.pessoa as pessoaCota ");
		hql.append("  LEFT JOIN movimento.movimentoEstoqueCotaFuro as movimentoEstoqueCotaFuro ");
		
		hql.append(" WHERE tipoMovimento.grupoMovimentoEstoque IN (:tipoMovimentoEntrada) " );
		
		hql.append(" AND (movimento.statusEstoqueFinanceiro is null ");
		hql.append(" or movimento.statusEstoqueFinanceiro = :statusEstoqueFinanceiro ) " );
		
		hql.append(" AND movimento.tipoMovimento.operacaoEstoque = :tipoOperacaoEntrada ");
		
		hql.append(" AND movimentoEstoqueCotaFuro.id is null ");
		
		if(filtro.getIdCota() != null ) { 
			hql.append("   AND cota.id = :idCota");			
		}
		if(filtro.getIdFornecedor() != null) { 
			hql.append("   AND fornecedor.id = :idFornecedor");
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
	
	private List<GrupoMovimentoEstoque> obterGruposMovimentoEstoqueDeEntradaNaCota() {
		
		List<GrupoMovimentoEstoque> listaGrupoMovimentoEstoquesEntrada = new ArrayList<GrupoMovimentoEstoque>();
		
		for(GrupoMovimentoEstoque grupoMovimentoEstoque: GrupoMovimentoEstoque.values()) {
			
			if(Dominio.COTA.equals(grupoMovimentoEstoque.getDominio()) && 
			   OperacaoEstoque.ENTRADA.equals(grupoMovimentoEstoque.getOperacaoEstoque())) {
				
				listaGrupoMovimentoEstoquesEntrada.add(grupoMovimentoEstoque);
			
			}
			
		}
		
		return listaGrupoMovimentoEstoquesEntrada;
		
	}
	
	private void buscarParametrosConsignadoCota(Query query, FiltroConsultaConsignadoCotaDTO filtro){

		List<GrupoMovimentoEstoque> listaGrupoMovimentoEstoquesEntrada = new ArrayList<GrupoMovimentoEstoque>();
		
		if (filtro.getIdCota() == null) {
			
			listaGrupoMovimentoEstoquesEntrada = obterGruposMovimentoEstoqueDeEntradaNaCota();
			

		} else {
			
			listaGrupoMovimentoEstoquesEntrada.add(GrupoMovimentoEstoque.RECEBIMENTO_REPARTE);
			
		}
		
		query.setParameter("tipoOperacaoEntrada", OperacaoEstoque.ENTRADA);
		
		query.setParameterList("tipoMovimentoEntrada", listaGrupoMovimentoEstoquesEntrada);
		
		query.setParameter("statusEstoqueFinanceiro", StatusEstoqueFinanceiro.FINANCEIRO_NAO_PROCESSADO);
		
		if(filtro.getIdCota() != null ) { 
			query.setParameter("idCota", filtro.getIdCota());
		}
		
		if(filtro.getIdFornecedor() != null ) { 
			query.setParameter("idFornecedor", filtro.getIdFornecedor());
		}
	
	}
		
}

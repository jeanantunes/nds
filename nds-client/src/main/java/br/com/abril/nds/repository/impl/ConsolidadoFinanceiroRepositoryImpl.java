package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.ResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.EncalheCotaDTO;
import br.com.abril.nds.dto.ViewContaCorrenteCotaDTO;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.financeiro.ConsolidadoFinanceiroCota;
import br.com.abril.nds.repository.ConsolidadoFinanceiroRepository;

@Repository
public class ConsolidadoFinanceiroRepositoryImpl extends AbstractRepository<ConsolidadoFinanceiroCota,Long> implements ConsolidadoFinanceiroRepository {

	public ConsolidadoFinanceiroRepositoryImpl(){
		
		super(ConsolidadoFinanceiroCota.class);
		
	}
	
	@SuppressWarnings("unchecked")
	public List<ViewContaCorrenteCotaDTO> buscarListaDeConsolidado(Integer numeroCota){
		
		StringBuffer hql = new StringBuffer();
		
		hql.append(" select ");
		
		hql.append("    consolidadoFinanceiroCota.dataConsolidado as dataConsolidado, 		");
		hql.append(" 	consolidadoFinanceiroCota.valorPostergado as valorPostergado,		");
		hql.append(" 	consolidadoFinanceiroCota.numeroAtrasados as numeroAtradao,	    	");
		hql.append(" 	consolidadoFinanceiroCota.consignado as consignado,       	        ");
		hql.append("  	consolidadoFinanceiroCota.encalhe as encalhe,               		");
		hql.append("  	consolidadoFinanceiroCota.vendaEncalhe as vendaEncalhe,     		");
		hql.append("  	consolidadoFinanceiroCota.debitoCredito as debCred,	            	");
		hql.append(" 	consolidadoFinanceiroCota.encargos as encargos,             		");
		hql.append(" 	consolidadoFinanceiroCota.pendente as pendente, 		        	");		
		hql.append(" 	consolidadoFinanceiroCota.total as total            				");
			
		hql.append(" from ");

		hql.append(" ConsolidadoFinanceiroCota consolidadoFinanceiroCota ");
		
		hql.append(" where ");
		
		hql.append(" consolidadoFinanceiroCota.cota.numeroCota = :numeroCota ");
		
		Query query  = getSession().createQuery(hql.toString());
		
		ResultTransformer resultTransformer = new AliasToBeanResultTransformer(ViewContaCorrenteCotaDTO.class);
		
		query.setResultTransformer(resultTransformer);

		query.setParameter("numeroCota", numeroCota);
		
		return query.list();
	}
	
	public boolean verificarConsodidadoCotaPorData(Long idCota, Date data){
		StringBuilder hql = new StringBuilder("select count (c.id) from ConsolidadoFinanceiroCota c ");
		hql.append(" where c.cota.id = :idCota ")
		   .append(" and c.dataConsolidado = :data ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idCota", idCota);
		query.setParameter("data", data);
		
		Long quant = (Long) query.uniqueResult();
		
		return quant == null ? false : quant > 0;
	}
	
	/**
	 * Método que obtém uma lista de encalhe por produto e cota
	 */
	@SuppressWarnings("unchecked")
	public List<EncalheCotaDTO> obterMovimentoEstoqueCotaEncalhe(Integer numeroCota, Date dataConsolidado){
		
		StringBuffer hql = new StringBuffer("");
		
		hql.append(" select ");
		hql.append("p.codigo as codigoProduto, ");
		hql.append("p.nome as nomeProduto, ");
		//hql.append("f.juridica.razaoSocial as nomefornecedor, ");
		hql.append("pe.numeroEdicao as numeroEdicao, ");
		hql.append("pe.precoVenda as precoCapa, ");		
		hql.append("(pe.precoVenda - pe.desconto) as precoComDesconto, ");
		hql.append(" (pe.precoVenda * qtde) as total, ");         
		hql.append("mec.qtde as encalhe ");		
		
		hql.append("FROM ConsolidadoFinanceiroCota consolidado ");
		
		hql.append("LEFT JOIN consolidado.cota c ");
		hql.append("LEFT JOIN consolidado.movimentos mfc ");
		hql.append("LEFT JOIN mfc.movimentos mec ");		
		//hql.append("LEFT JOIN mec.tipoMovimento tp ");		
		hql.append("LEFT JOIN mec.estoqueProdutoCota epc ");
		hql.append("LEFT JOIN epc.produtoEdicao pe ");
		hql.append("LEFT JOIN pe.produto p ");
		//hql.append("LEFT JOIN p.fornecedores f ");
						
		hql.append(" WHERE c.numeroCota =:numeroCota ");
		
		hql.append(" and consolidado.dataConsolidado =:dataConsolidado ");		
		hql.append(" and tp.grupoMovimentoEstoque =:grupoMovimentoEstoque");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("numeroCota", numeroCota);		
		query.setParameter("dataConsolidado", dataConsolidado);		
		query.setParameter("grupoMovimentoEstoque", GrupoMovimentoEstoque.ENVIO_ENCALHE);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				EncalheCotaDTO.class));
		
		return query.list();
		
	}
	
}

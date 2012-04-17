package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.ResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ConsignadoCotaDTO;
import br.com.abril.nds.dto.EncalheCotaDTO;
import br.com.abril.nds.dto.FiltroConsolidadoConsignadoCotaDTO;
import br.com.abril.nds.dto.ViewContaCorrenteCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsolidadoEncalheCotaDTO;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.financeiro.ConsolidadoFinanceiroCota;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.repository.ConsolidadoFinanceiroRepository;
import br.com.abril.nds.vo.PaginacaoVO;

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
	public List<EncalheCotaDTO> obterMovimentoEstoqueCotaEncalhe(FiltroConsolidadoEncalheCotaDTO filtro){
		
		StringBuffer hql = new StringBuffer("");
		
		hql.append(" select ");
		hql.append(" p.codigo as codigoProduto, ");	
		hql.append(" p.nome as nomeProduto, ");
		hql.append(" f.juridica.razaoSocial as nomeFornecedor, ");				
		hql.append(" pe.numeroEdicao as numeroEdicao, ");
		hql.append(" pe.precoVenda as precoCapa, ");		
		hql.append(" (pe.precoVenda - pe.desconto) as precoComDesconto, ");
		hql.append(" sum(mec.qtde*(pe.precoVenda - pe.desconto)) as total, ");         
		hql.append(" sum(mec.qtde) as encalhe ");		
		
		hql.append(" FROM ConsolidadoFinanceiroCota consolidado ");
		
		hql.append(" LEFT JOIN consolidado.cota c ");
		hql.append(" LEFT JOIN consolidado.movimentos mfc ");
		hql.append(" LEFT JOIN mfc.movimentos mec ");		
		hql.append(" LEFT JOIN mec.tipoMovimento tp ");		
		hql.append(" LEFT JOIN mec.estoqueProdutoCota epc ");
		hql.append(" LEFT JOIN epc.produtoEdicao pe ");
		hql.append(" LEFT JOIN pe.produto p ");
		hql.append(" LEFT JOIN p.fornecedores f ");
						
		hql.append(" WHERE c.numeroCota =:numeroCota ");
		
		hql.append(" and consolidado.dataConsolidado =:dataConsolidado ");		
		hql.append(" and tp.grupoMovimentoEstoque =:grupoMovimentoEstoque ");
		hql.append(" and mfc.tipoMovimento.grupoMovimentoFinaceiro =:grupoMovimentoFinanceiro ");
		
		hql.append(" GROUP BY	");
		
		hql.append(" p.codigo, ");
		hql.append(" p.nome, ");
		hql.append(" pe.numeroEdicao, ");
		hql.append(" pe.precoVenda, ");
		hql.append(" pe.desconto, ");
		hql.append(" f.juridica.razaoSocial ");
		
		PaginacaoVO paginacao = filtro.getPaginacao();

		if (filtro.getOrdenacaoColuna() != null) {

			hql.append(" order by ");
			
			String orderByColumn = "";
			
				switch (filtro.getOrdenacaoColuna()) {
				
					case CODIGO_PRODUTO:
						orderByColumn = " codigoProduto ";
						break;
					case NOME_PRODUTO:
						orderByColumn = " nomeProduto ";
						break;
					case NUMERO_EDICAO:
						orderByColumn = " numeroEdicao ";
						break;
					case PRECO_CAPA:
						orderByColumn = " precoVenda ";
						break;
					case PRECO_COM_DESCONTO:
						orderByColumn = " precoComDesconto ";
						break;				
					case ENCALHE:
						orderByColumn = " encalhe ";
						break;
					case FORNECEDOR:
						orderByColumn = " fornecedor ";
						break;
					case TOTAL:
						orderByColumn = " total ";
						break;
					default:
						break;
				}
			
			hql.append(orderByColumn);
			
			if (paginacao.getOrdenacao() != null) {
				
				hql.append(paginacao.getOrdenacao().toString());
				
			}			
		}
					
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("numeroCota", filtro.getNumeroCota());
		query.setParameter("dataConsolidado", filtro.getDataConsolidado());		
		query.setParameter("grupoMovimentoEstoque", GrupoMovimentoEstoque.ENVIO_ENCALHE);
		query.setParameter("grupoMovimentoFinanceiro", GrupoMovimentoFinaceiro.ENVIO_ENCALHE);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				EncalheCotaDTO.class));
		
		return query.list();
		
	}
	
	public List<ConsignadoCotaDTO> obterMovimentoEstoqueCotaConsignado(FiltroConsolidadoConsignadoCotaDTO filtro){
		//TODO: Consulta Consignados
		return null;
		
	}
	
}

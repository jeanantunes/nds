package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.ResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ConsignadoCotaDTO;
import br.com.abril.nds.dto.ConsultaVendaEncalheDTO;
import br.com.abril.nds.dto.EncalheCotaDTO;
import br.com.abril.nds.dto.FiltroConsolidadoConsignadoCotaDTO;
import br.com.abril.nds.dto.ViewContaCorrenteCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsolidadoEncalheCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsolidadoVendaCotaDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.financeiro.ConsolidadoFinanceiroCota;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.repository.ConsolidadoFinanceiroRepository;
import br.com.abril.nds.vo.PaginacaoVO;

@Repository
public class ConsolidadoFinanceiroRepositoryImpl extends
		AbstractRepositoryModel<ConsolidadoFinanceiroCota, Long> implements
		ConsolidadoFinanceiroRepository {

	public ConsolidadoFinanceiroRepositoryImpl() {

		super(ConsolidadoFinanceiroCota.class);

	}

	@SuppressWarnings("unchecked")
	public List<ViewContaCorrenteCotaDTO> buscarListaDeConsolidado(
			Integer numeroCota) {

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
		hql.append(" 	consolidadoFinanceiroCota.total as total ,          				");
		hql.append(" 	consolidadoFinanceiroCota.id as id            				        ");

		hql.append(" from ");

		hql.append(" ConsolidadoFinanceiroCota consolidadoFinanceiroCota ");

		hql.append(" where ");

		hql.append(" consolidadoFinanceiroCota.cota.numeroCota = :numeroCota ");

		Query query = getSession().createQuery(hql.toString());

		ResultTransformer resultTransformer = new AliasToBeanResultTransformer(
				ViewContaCorrenteCotaDTO.class);

		query.setResultTransformer(resultTransformer);

		query.setParameter("numeroCota", numeroCota);

		return query.list();
	}

	public boolean verificarConsodidadoCotaPorDataOperacao(Long idCota) {
		StringBuilder hql = new StringBuilder(
				"select count (c.id) from ConsolidadoFinanceiroCota c, Distribuidor d ");
		hql.append(" where c.cota.id = :idCota ")
		   .append(" and c.dataConsolidado = d.dataOperacao ");

		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idCota", idCota);

		Long quant = (Long) query.uniqueResult();

		return quant == null ? false : quant > 0;
	}

	/**
	 * Método que obtém uma lista de encalhe por produto e cota
	 */
	@SuppressWarnings("unchecked")
	public List<EncalheCotaDTO> obterMovimentoEstoqueCotaEncalhe(
			FiltroConsolidadoEncalheCotaDTO filtro) {

		StringBuffer hql = new StringBuffer();

		hql.append(" select ");
		hql.append(" p.codigo as codigoProduto, ");
		hql.append(" p.nome as nomeProduto, ");
		hql.append(" f.juridica.razaoSocial as nomeFornecedor, ");
		hql.append(" pe.numeroEdicao as numeroEdicao, ");
		hql.append(" pe.precoVenda as precoCapa, ");
		hql.append(" ("+ this.obterSQLDescontoObterMovimentoEstoqueCotaEncalhe() +") as desconto, ");
		hql.append(" (pe.precoVenda - (pe.precoVenda * ("+ this.obterSQLDescontoObterMovimentoEstoqueCotaEncalhe() +") / 100)) as precoComDesconto, ");
		hql.append(" sum(mec.qtde*(pe.precoVenda - (pe.precoVenda * ("+ this.obterSQLDescontoObterMovimentoEstoqueCotaEncalhe() +") / 100))) as total, ");
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
		hql.append(" LEFT JOIN f.juridica juridica ");

		hql.append(" WHERE c.numeroCota =:numeroCota ");

		hql.append(" and consolidado.dataConsolidado =:dataConsolidado ");
		hql.append(" and tp.grupoMovimentoEstoque =:grupoMovimentoEstoque ");
		hql.append(" and mfc.tipoMovimento.grupoMovimentoFinaceiro =:grupoMovimentoFinanceiro ");

		hql.append(" GROUP BY	");

		hql.append(" p.codigo, ");
		hql.append(" p.nome, ");
		hql.append(" pe.numeroEdicao, ");
		hql.append(" pe.precoVenda, ");
		hql.append(" desconto, ");
		hql.append(" juridica.razaoSocial ");

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
				orderByColumn = " precoCapa ";
				break;
			case PRECO_COM_DESCONTO:
				orderByColumn = " precoComDesconto ";
				break;
			case ENCALHE:
				orderByColumn = " encalhe ";
				break;
			case FORNECEDOR:
				orderByColumn = " nomeFornecedor ";
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
		query.setParameter("grupoMovimentoEstoque",
				GrupoMovimentoEstoque.ENVIO_ENCALHE);
		query.setParameter("grupoMovimentoFinanceiro",
				GrupoMovimentoFinaceiro.ENVIO_ENCALHE);

		query.setResultTransformer(new AliasToBeanResultTransformer(
				EncalheCotaDTO.class));

		return query.list();

	}
	
	private String obterSQLDescontoObterMovimentoEstoqueCotaEncalhe(){
		
		StringBuilder hql = new StringBuilder("coalesce ((select view.desconto");
		hql.append(" from ViewDesconto view ")
		   .append(" where view.cotaId = c.id ")
		   .append(" and view.produtoEdicaoId = pe.id ")
		   .append(" and view.fornecedorId = f.id),0) ");
		
		return hql.toString();
	}

	@SuppressWarnings(value = "unchecked")
	public List<ConsultaVendaEncalheDTO> obterMovimentoVendaEncalhe(
			FiltroConsolidadoVendaCotaDTO filtro) {
		StringBuffer hql = new StringBuffer();

		hql.append(" select ");
		hql.append(" p.codigo as codigoProduto, ");
		hql.append(" p.nome as nomeProduto, ");
		hql.append(" f.juridica.razaoSocial as nomeFornecedor, ");
		hql.append(" pe.numeroEdicao as numeroEdicao, ");
		hql.append(" pe.precoVenda as precoCapa, ");
		hql.append(" ("+ this.obterSQLDescontoObterMovimentoVendaEncalhe() +") as desconto, ");
		hql.append(" (pe.precoVenda - (pe.precoVenda * ("+ this.obterSQLDescontoObterMovimentoVendaEncalhe() +") / 100)) as precoComDesconto, ");
		hql.append(" sum(mec.qtde*(pe.precoVenda - (pe.precoVenda * ("+ this.obterSQLDescontoObterMovimentoVendaEncalhe() +") / 100))) as total, ");
		hql.append(" box.codigo as box,");
		hql.append(" mec.qtde as exemplares");

		hql.append(" FROM ConsolidadoFinanceiroCota consolidado ");

		hql.append(" LEFT JOIN consolidado.cota cota ");
		hql.append(" LEFT JOIN cota.box box ");
		hql.append(" LEFT JOIN consolidado.movimentos mfc ");
		hql.append(" LEFT JOIN mfc.movimentos mec ");
		hql.append(" LEFT JOIN mec.tipoMovimento tp ");
		hql.append(" LEFT JOIN mec.estoqueProdutoCota epc ");
		hql.append(" LEFT JOIN epc.produtoEdicao pe ");
		hql.append(" LEFT JOIN pe.produto p ");
		hql.append(" LEFT JOIN p.fornecedores f ");

		hql.append(" WHERE consolidado.id =:idConsolidado ");
		hql.append(" and tp.grupoMovimentoEstoque =:grupoMovimentoEstoque ");
		hql.append(" and mfc.tipoMovimento.grupoMovimentoFinaceiro =:grupoMovimentoFinanceiro ");

		hql.append(" GROUP BY	");

		hql.append(" p.codigo, ");
		hql.append(" p.nome, ");
		hql.append(" pe.numeroEdicao, ");
		hql.append(" pe.precoVenda, ");
		hql.append(" desconto, ");
		hql.append(" f.juridica.razaoSocial ");
		
		if (filtro.getOrdenacaoColuna() != null) {
			hql.append(" order by ");			
			hql.append(filtro.getOrdenacaoColuna().toString());			
			
			if (filtro.getPaginacao() != null && filtro.getPaginacao().getOrdenacao() != null) {
				hql.append(" ").append(filtro.getPaginacao().getOrdenacao().toString());
			}
			
		}
		

		Query query = getSession().createQuery(hql.toString());
		
		
		getSession().createCriteria(hql.toString());

		query.setParameter("idConsolidado", filtro.getIdConsolidado());
		query.setParameter("grupoMovimentoEstoque",
				GrupoMovimentoEstoque.VENDA_ENCALHE);
		query.setParameter("grupoMovimentoFinanceiro",
				GrupoMovimentoFinaceiro.COMPRA_ENCALHE);
		
		if(filtro.getPaginacao() != null && filtro.getPaginacao().getPosicaoInicial() != null && filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
			query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
		}

		query.setResultTransformer(new AliasToBeanResultTransformer(
				ConsultaVendaEncalheDTO.class));

		return query.list();
	}
	
	private String obterSQLDescontoObterMovimentoVendaEncalhe(){
		
		StringBuilder hql = new StringBuilder("coalesce ((select view.desconto");
		hql.append(" from ViewDesconto view ")
		   .append(" where view.cotaId = cota.id ")
		   .append(" and view.produtoEdicaoId = pe.id ")
		   .append(" and view.fornecedorId = f.id),0) ");
		
		return hql.toString();
	}

	/**
	 * Método que obtem os movimentos de Envio ao Jornaleiro (Consignado) para conta corrente da Cota
	 */
	@SuppressWarnings("unchecked")
	public List<ConsignadoCotaDTO> obterMovimentoEstoqueCotaConsignado(FiltroConsolidadoConsignadoCotaDTO filtro){
		
		StringBuffer hql = new StringBuffer("");
		
		hql.append(" select ");
		hql.append(" p.codigo as codigoProduto, ");	
		hql.append(" p.nome as nomeProduto, ");
		hql.append(" juridica.razaoSocial as nomeFornecedor, ");				
		hql.append(" pe.numeroEdicao as numeroEdicao, ");
		hql.append(" pe.precoVenda as precoCapa, ");
		hql.append(" ("+ this.obterSQLDescontoObterMovimentoEstoqueCotaConsignado() +") as desconto, ");
		hql.append(" (pe.precoVenda - (pe.precoVenda * ("+ this.obterSQLDescontoObterMovimentoEstoqueCotaConsignado() +") / 100)) as precoComDesconto, ");
		hql.append(" ec.qtdePrevista as reparteSugerido, ");
		hql.append(" ec.qtdeEfetiva as reparteFinal, ");
		hql.append(" (ec.qtdePrevista - ec.qtdeEfetiva) as diferenca, ");
		hql.append(" d.tipoDiferenca as motivo, ");		
		hql.append(" sum(mec.qtde*(pe.precoVenda - (pe.precoVenda * ("+ this.obterSQLDescontoObterMovimentoEstoqueCotaConsignado() +") / 100))) as total ");

		hql.append(" FROM ConsolidadoFinanceiroCota consolidado ");
		
		hql.append(" JOIN consolidado.cota c ");
		hql.append(" JOIN consolidado.movimentos mfc ");
		hql.append(" JOIN mfc.movimentos mec ");		
		hql.append(" JOIN mec.tipoMovimento tp ");		
		hql.append(" JOIN mec.estoqueProdutoCota epc ");
		hql.append(" JOIN mec.estudoCota ec ");
		hql.append(" JOIN epc.produtoEdicao pe ");
		
		hql.append(" LEFT JOIN ec.rateiosDiferenca rd ");
		hql.append(" LEFT JOIN rd.diferenca d ");
			
		hql.append(" JOIN pe.produto p ");
		hql.append(" JOIN p.fornecedores f ");
		hql.append(" JOIN f.juridica juridica ");
		
		
						
		hql.append(" WHERE c.numeroCota =:numeroCota ");
		
		hql.append(" and consolidado.dataConsolidado =:dataConsolidado ");		
		hql.append(" and tp.grupoMovimentoEstoque =:grupoMovimentoEstoque ");
		hql.append(" and mfc.tipoMovimento.grupoMovimentoFinaceiro =:grupoMovimentoFinanceiro ");
		
		hql.append(" GROUP BY	");
		
		hql.append(" p.codigo, ");
		hql.append(" p.nome, ");
		hql.append(" pe.numeroEdicao, ");
		hql.append(" pe.precoVenda, ");
		hql.append(" desconto, ");
		hql.append(" juridica.razaoSocial ");
		
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
						orderByColumn = " precoCapa ";
						break;
					case PRECO_COM_DESCONTO:
						orderByColumn = " precoComDesconto ";
						break;				
					case REPARTE_SUGERIDO:
						orderByColumn = " reparteSugerido ";
						break;
					case REPARTE_FINAL:
						orderByColumn = " reparteFinal ";
						break;
					case DIFERENCA:
						orderByColumn = " diferenca ";
						break;
					case MOTIVO:
						orderByColumn = " motivo ";
						break;						
					case FORNECEDOR:
						orderByColumn = " nomeFornecedor ";
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
		
		Session session = getSession();
					
		Query query = session.createQuery(hql.toString());
		
		query.setParameter("numeroCota", filtro.getNumeroCota());
		query.setParameter("dataConsolidado", filtro.getDataConsolidado());		
		query.setParameter("grupoMovimentoEstoque", GrupoMovimentoEstoque.RECEBIMENTO_REPARTE);
		query.setParameter("grupoMovimentoFinanceiro", GrupoMovimentoFinaceiro.RECEBIMENTO_REPARTE);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				ConsignadoCotaDTO.class));
		
		return query.list();
		
 	}
	
	private String obterSQLDescontoObterMovimentoEstoqueCotaConsignado(){
		
		StringBuilder hql = new StringBuilder("coalesce ((select view.desconto");
		hql.append(" from ViewDesconto view ")
		   .append(" where view.cotaId = c.id ")
		   .append(" and view.produtoEdicaoId = pe.id ")
		   .append(" and view.fornecedorId = f.id),0) ");
		
		return hql.toString();
	}

	@Override
	public ConsolidadoFinanceiroCota obterConsolidadoPorIdMovimentoFinanceiro(Long idMovimentoFinanceiro) {
		
		StringBuilder hql = new StringBuilder("select c from ConsolidadoFinanceiroCota c join c.movimentos mov ");
		hql.append(" where mov.id = :idMovimentoFinanceiro ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idMovimentoFinanceiro", idMovimentoFinanceiro);
		
		return (ConsolidadoFinanceiroCota) query.uniqueResult();
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.ConsolidadoFinanceiroRepository#buscarUltimaBaixaAutomaticaDia(java.util.Date)
	 */
	@Override
	public Date buscarUltimaDividaGeradaDia(Date dataOperacao) {
		Criteria criteria = getSession().createCriteria(ConsolidadoFinanceiroCota.class);
		criteria.add(Restrictions.eq("dataConsolidado", dataOperacao));
		criteria.setProjection(Projections.max("dataConsolidado"));
		return (Date) criteria.uniqueResult();
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.ConsolidadoFinanceiroRepository#buscarDiaUltimaBaixaAutomatica()
	 */
	@Override
	public Date buscarDiaUltimaDividaGerada() {
		Criteria criteria = getSession().createCriteria(ConsolidadoFinanceiroCota.class);
		criteria.setProjection(Projections.max("dataConsolidado"));
		return (Date) criteria.uniqueResult();
	}

	@Override
	public Long obterQuantidadeDividasGeradasData(Date data) {
		
		Query query = 
				this.getSession().createQuery(
						"select count(c.id) from ConsolidadoFinanceiroCota c where c.dataConsolidado = :data ");
		
		query.setParameter("data", data);
		
		return (Long) query.uniqueResult();
	}

	@Override
	public ConsolidadoFinanceiroCota buscarPorCotaEData(Cota cota,
			java.sql.Date data) {
		
		Criteria criteria = getSession().createCriteria(ConsolidadoFinanceiroCota.class);
		
		criteria.add(Restrictions.eq("cota", cota));
		criteria.add(Restrictions.eq("dataConsolidado", data));
		
		criteria.setMaxResults(1);
		
		return (ConsolidadoFinanceiroCota) criteria.uniqueResult();
	}
}
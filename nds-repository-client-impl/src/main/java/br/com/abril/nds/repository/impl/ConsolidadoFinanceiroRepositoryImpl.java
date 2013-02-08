package br.com.abril.nds.repository.impl;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.client.vo.ContaCorrenteCotaVO;
import br.com.abril.nds.dto.ConsignadoCotaDTO;
import br.com.abril.nds.dto.ConsultaVendaEncalheDTO;
import br.com.abril.nds.dto.EncalheCotaDTO;
import br.com.abril.nds.dto.FiltroConsolidadoConsignadoCotaDTO;
import br.com.abril.nds.dto.ViewContaCorrenteCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsolidadoEncalheCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsolidadoVendaCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroViewContaCorrenteCotaDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.financeiro.ConsolidadoFinanceiroCota;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.StatusBaixa;
import br.com.abril.nds.repository.AbstractRepositoryModel;
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

	/**
	 * Método que obtém uma lista de encalhe por produto e cota
	 */
	@SuppressWarnings("unchecked")
	public List<EncalheCotaDTO> obterMovimentoEstoqueCotaEncalhe(
			FiltroConsolidadoEncalheCotaDTO filtro) {

		StringBuffer hql = new StringBuffer("select ");
		
		hql.append(" p.codigo as codigoProduto, ");
		hql.append(" p.nome as nomeProduto, ");
		hql.append(" juridica.razaoSocial as nomeFornecedor, ");
		hql.append(" pe.numeroEdicao as numeroEdicao, ");
		hql.append(" pe.precoVenda as precoCapa, ");
		hql.append(" (mec.valoresAplicados.valorDesconto) as desconto, ");
		hql.append(" (pe.precoVenda - (pe.precoVenda * coalesce((mec.valoresAplicados.valorDesconto),0) / 100)) as precoComDesconto, ");
		hql.append(" sum(mec.qtde*(pe.precoVenda - (pe.precoVenda * coalesce((mec.valoresAplicados.valorDesconto),0) / 100))) as total, ");
		hql.append(" sum(mec.qtde) as encalhe ");
		
		hql.append(" from ConsolidadoFinanceiroCota consolidado ");
		hql.append(" left join consolidado.cota c ");
		hql.append(" left join consolidado.movimentos mfc ");
		hql.append(" left join mfc.movimentos mec ");
		hql.append(" left join mec.tipoMovimento tp ");
		hql.append(" left join mec.estoqueProdutoCota epc ");
		hql.append(" left join epc.produtoEdicao pe ");
		hql.append(" left join pe.produto p ");
		hql.append(" left join p.fornecedores f ");
		hql.append(" left join f.juridica juridica ");
		
		hql.append(" where c.numeroCota =:numeroCota ");
		hql.append(" and consolidado.dataConsolidado =:dataConsolidado ");
		hql.append(" and mfc.tipoMovimento.grupoMovimentoFinaceiro =:grupoMovimentoFinanceiro ");
		
		hql.append(" group by ");
		hql.append(" p.codigo, ");
		hql.append(" p.nome, ");
		hql.append(" pe.numeroEdicao, ");
		hql.append(" pe.precoVenda, ");
		hql.append(" juridica.razaoSocial ");
		
		hql.append(" union all ");
		
		hql.append("select ");
		hql.append(" p.codigo as codigoProduto, ");
		hql.append(" p.nome as nomeProduto, ");
		hql.append(" juridica.razaoSocial as nomeFornecedor, ");
		hql.append(" pe.numeroEdicao as numeroEdicao, ");
		hql.append(" pe.precoVenda as precoCapa, ");
		hql.append(" (mec.valoresAplicados.valorDesconto) as desconto, ");
		hql.append(" (pe.precoVenda - (pe.precoVenda * coalesce((mec.valoresAplicados.valorDesconto),0) / 100)) as precoComDesconto, ");
		hql.append(" sum(mec.qtde*(pe.precoVenda - (pe.precoVenda * coalesce((mec.valoresAplicados.valorDesconto),0) / 100))) as total, ");
		hql.append(" sum(mec.qtde) as encalhe ");
		
		hql.append(" from MovimentoFinanceiroCota mfc ");
		hql.append(" left join mfc.cota c ");
		hql.append(" left join mfc.movimentos mec ");
		hql.append(" left join mec.tipoMovimento tp ");
		hql.append(" left join mec.estoqueProdutoCota epc ");
		hql.append(" left join epc.produtoEdicao pe ");
		hql.append(" left join pe.produto p ");
		hql.append(" left join p.fornecedores f ");
		hql.append(" left join f.juridica juridica ");
		
		hql.append(" where c.numeroCota =:numeroCota ");
		hql.append(" and mfc.data =:dataConsolidado ");
		hql.append(" and mfc.tipoMovimento.grupoMovimentoFinaceiro =:grupoMovimentoFinanceiro ");
		hql.append(" and mfc.id not in ");
		hql.append(" (select mov.id from ConsolidadoFinanceiroCota c join c.movimentos mov) ");
		
		hql.append(" group by ");
		hql.append(" p.codigo, ");
		hql.append(" p.nome, ");
		hql.append(" pe.numeroEdicao, ");
		hql.append(" pe.precoVenda, ");
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
		query.setParameter("grupoMovimentoFinanceiro", GrupoMovimentoFinaceiro.ENVIO_ENCALHE);
		
		if (paginacao != null &&
				paginacao.getQtdResultadosPorPagina() != null &&
				paginacao.getPaginaAtual() != null){
			
			query.setMaxResults(paginacao.getQtdResultadosPorPagina());
			query.setFirstResult(paginacao.getPaginaAtual() - 1 * paginacao.getQtdResultadosPorPagina());
		}

		query.setResultTransformer(new AliasToBeanResultTransformer(EncalheCotaDTO.class));

		return query.list();

	}

	@SuppressWarnings(value = "unchecked")
	public List<ConsultaVendaEncalheDTO> obterMovimentoVendaEncalhe(
			FiltroConsolidadoVendaCotaDTO filtro) {
		
		StringBuffer hql = new StringBuffer("select ");
		
		hql.append(" p.codigo as codigoProduto, ");
		hql.append(" p.nome as nomeProduto, ");
		hql.append(" f.juridica.razaoSocial as nomeFornecedor, ");
		hql.append(" pe.numeroEdicao as numeroEdicao, ");
		hql.append(" pe.precoVenda as precoCapa, ");
		hql.append(" (mec.valoresAplicados.valorDesconto) as desconto, ");
		hql.append(" (pe.precoVenda - (pe.precoVenda * (mec.valoresAplicados.valorDesconto) / 100)) as precoComDesconto, ");
		hql.append(" sum(mec.qtde*(pe.precoVenda - (pe.precoVenda * (mec.valoresAplicados.valorDesconto) / 100))) as total, ");
		hql.append(" box.codigo as box,");
		hql.append(" mec.qtde as exemplares");
		
		hql.append(" from ConsolidadoFinanceiroCota consolidado ");
		hql.append(" left join consolidado.cota cota ");
		hql.append(" left join cota.box box ");
		hql.append(" left join consolidado.movimentos mfc ");
		hql.append(" left join mfc.movimentos mec ");
		hql.append(" left join mec.tipoMovimento tp ");
		hql.append(" left join mec.estoqueProdutoCota epc ");
		hql.append(" left join epc.produtoEdicao pe ");
		hql.append(" left join pe.produto p ");
		hql.append(" left join p.fornecedores f ");
		
		hql.append(" where consolidado.dataConsolidado =:dataConsolidado ");
		hql.append(" and cota.numeroCota = :numeroCota ");
		hql.append(" and tp.grupoMovimentoEstoque =:grupoMovimentoEstoque ");
		hql.append(" and mfc.tipoMovimento.grupoMovimentoFinaceiro =:grupoMovimentoFinanceiro ");
		
		hql.append(" group by ");
		hql.append(" p.codigo, ");
		hql.append(" p.nome, ");
		hql.append(" pe.numeroEdicao, ");
		hql.append(" pe.precoVenda, ");
		hql.append(" f.juridica.razaoSocial ");
		
		hql.append(" union all ");
		
		hql.append("select ");
		hql.append(" p.codigo as codigoProduto, ");
		hql.append(" p.nome as nomeProduto, ");
		hql.append(" f.juridica.razaoSocial as nomeFornecedor, ");
		hql.append(" pe.numeroEdicao as numeroEdicao, ");
		hql.append(" pe.precoVenda as precoCapa, ");
		hql.append(" (mec.valoresAplicados.valorDesconto) as desconto, ");
		hql.append(" (pe.precoVenda - (pe.precoVenda * (mec.valoresAplicados.valorDesconto) / 100)) as precoComDesconto, ");
		hql.append(" sum(mec.qtde*(pe.precoVenda - (pe.precoVenda * (mec.valoresAplicados.valorDesconto) / 100))) as total, ");
		hql.append(" box.codigo as box,");
		hql.append(" mec.qtde as exemplares");
		
		hql.append(" from MovimentoFinanceiroCota mfc ");
		hql.append(" left join mfc.cota cota ");
		hql.append(" left join cota.box box ");
		hql.append(" left join mfc.movimentos mec ");
		hql.append(" left join mec.tipoMovimento tp ");
		hql.append(" left join mec.estoqueProdutoCota epc ");
		hql.append(" left join epc.produtoEdicao pe ");
		hql.append(" left join pe.produto p ");
		hql.append(" left join p.fornecedores f ");
		
		hql.append(" where cota.numeroCota =:numeroCota ");
		hql.append(" and mfc.data = :dataConsolidado ");
		hql.append(" and tp.grupoMovimentoEstoque =:grupoMovimentoEstoque ");
		hql.append(" and mfc.tipoMovimento.grupoMovimentoFinaceiro =:grupoMovimentoFinanceiro ");
		hql.append(" and mfc.id not in ");
		hql.append(" (select mov.id from ConsolidadoFinanceiroCota c join c.movimentos mov) ");
		
		hql.append(" group by ");
		hql.append(" p.codigo, ");
		hql.append(" p.nome, ");
		hql.append(" pe.numeroEdicao, ");
		hql.append(" pe.precoVenda, ");
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

		query.setParameter("dataConsolidado", filtro.getDataConsolidado());
		query.setParameter("numeroCota", filtro.getNumeroCota());
		query.setParameter("grupoMovimentoEstoque",	GrupoMovimentoEstoque.VENDA_ENCALHE);
		query.setParameter("grupoMovimentoFinanceiro", GrupoMovimentoFinaceiro.COMPRA_ENCALHE);
		
		if(filtro.getPaginacao() != null && filtro.getPaginacao().getPosicaoInicial() != null && filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
			query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
		}

		query.setResultTransformer(new AliasToBeanResultTransformer(
				ConsultaVendaEncalheDTO.class));

		return query.list();
	}

	/**
	 * Método que obtem os movimentos de Envio ao Jornaleiro (Consignado) para conta corrente da Cota
	 */
	@SuppressWarnings("unchecked")
	public List<ConsignadoCotaDTO> obterMovimentoEstoqueCotaConsignado(FiltroConsolidadoConsignadoCotaDTO filtro){
		
		StringBuffer hql = new StringBuffer("select ");
		
		hql.append(" p.codigo as codigoProduto, ");
		hql.append(" p.nome as nomeProduto, ");
		hql.append(" juridica.razaoSocial as nomeFornecedor, ");
		hql.append(" pe.numeroEdicao as numeroEdicao, ");
		hql.append(" pe.precoVenda as precoCapa, ");
		hql.append(" (mec.valoresAplicados.valorDesconto) as desconto, ");
		hql.append(" (pe.precoVenda - (pe.precoVenda * coalesce((mec.valoresAplicados.valorDesconto),0) / 100)) as precoComDesconto, ");
		hql.append(" ec.qtdePrevista as reparteSugerido, ");
		hql.append(" ec.qtdeEfetiva as reparteFinal, ");
		hql.append(" (ec.qtdePrevista - ec.qtdeEfetiva) as diferenca, ");
		hql.append(" d.tipoDiferenca as motivo, ");
		hql.append(" sum(mec.qtde*(pe.precoVenda - (pe.precoVenda * coalesce((mec.valoresAplicados.valorDesconto),0) / 100))) as total ");
		
		hql.append(" from ConsolidadoFinanceiroCota consolidado ");
		hql.append(" join consolidado.cota c ");
		hql.append(" join consolidado.movimentos mfc ");
		hql.append(" join mfc.movimentos mec ");
		hql.append(" join mec.tipoMovimento tp ");
		hql.append(" join mec.estoqueProdutoCota epc ");
		hql.append(" join mec.estudoCota ec ");
		hql.append(" join epc.produtoEdicao pe ");
		hql.append(" left join ec.rateiosDiferenca rd ");
		hql.append(" left join rd.diferenca d ");
		hql.append(" join pe.produto p ");
		hql.append(" join p.fornecedores f ");
		hql.append(" join f.juridica juridica ");
		
		hql.append(" where c.numeroCota =:numeroCota ");
		hql.append(" and consolidado.dataConsolidado =:dataConsolidado ");
		hql.append(" and mfc.tipoMovimento.grupoMovimentoFinaceiro =:grupoMovimentoFinanceiro ");
		
		hql.append(" group by ");
		hql.append(" p.codigo, ");
		hql.append(" p.nome, ");
		hql.append(" pe.numeroEdicao, ");
		hql.append(" pe.precoVenda, ");
		hql.append(" juridica.razaoSocial ");
		
		hql.append(" union all ");
		
		hql.append(" select ");
		hql.append(" p.codigo as codigoProduto, ");
		hql.append(" p.nome as nomeProduto, ");
		hql.append(" juridica.razaoSocial as nomeFornecedor, ");			
		hql.append(" pe.numeroEdicao as numeroEdicao, ");
		hql.append(" pe.precoVenda as precoCapa, ");
		hql.append(" (mec.valoresAplicados.valorDesconto) as desconto, ");
		hql.append(" (pe.precoVenda - (pe.precoVenda * coalesce((mec.valoresAplicados.valorDesconto),0) / 100)) as precoComDesconto, ");
		hql.append(" ec.qtdePrevista as reparteSugerido, ");
		hql.append(" ec.qtdeEfetiva as reparteFinal, ");
		hql.append(" (ec.qtdePrevista - ec.qtdeEfetiva) as diferenca, ");
		hql.append(" d.tipoDiferenca as motivo, ");
		hql.append(" sum(mec.qtde*(pe.precoVenda - (pe.precoVenda * coalesce((mec.valoresAplicados.valorDesconto),0) / 100))) as total ");
		
		hql.append(" from MovimentoFinanceiroCota mfc ");
		hql.append(" join mfc.cota c ");
		hql.append(" join mfc.movimentos mec ");
		hql.append(" join mec.tipoMovimento tp ");
		hql.append(" join mec.estoqueProdutoCota epc ");
		hql.append(" join mec.estudoCota ec ");
		hql.append(" join epc.produtoEdicao pe ");
		hql.append(" left join ec.rateiosDiferenca rd ");
		hql.append(" left join rd.diferenca d ");
		hql.append(" join pe.produto p ");
		hql.append(" join p.fornecedores f ");
		hql.append(" join f.juridica juridica ");
		
		hql.append(" where c.numeroCota =:numeroCota ");
		hql.append(" and mfc.data =:dataConsolidado ");	
		hql.append(" and mfc.tipoMovimento.grupoMovimentoFinaceiro =:grupoMovimentoFinanceiro ");
		hql.append(" and mfc.id not in ");
		hql.append(" (select mov.id from ConsolidadoFinanceiroCota c join c.movimentos mov) ");
		
		hql.append(" group by ");
		hql.append(" p.codigo, ");
		hql.append(" p.nome, ");
		hql.append(" pe.numeroEdicao, ");
		hql.append(" pe.precoVenda, ");
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
		query.setParameter("grupoMovimentoFinanceiro", GrupoMovimentoFinaceiro.RECEBIMENTO_REPARTE);
		
		if (paginacao != null &&
				paginacao.getQtdResultadosPorPagina() != null &&
				paginacao.getPaginaAtual() != null){
			
			query.setMaxResults(paginacao.getQtdResultadosPorPagina());
			query.setFirstResult(paginacao.getPaginaAtual() - 1 * paginacao.getQtdResultadosPorPagina());
		}
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ConsignadoCotaDTO.class));
		
		return query.list();
		
 	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ConsolidadoFinanceiroCota> obterConsolidadoPorIdMovimentoFinanceiro(Long idMovimentoFinanceiro) {
		
		StringBuilder hql = new StringBuilder("select c from ConsolidadoFinanceiroCota c join c.movimentos mov ");
		hql.append(" where mov.id = :idMovimentoFinanceiro ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idMovimentoFinanceiro", idMovimentoFinanceiro);
		
		return query.list();
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
	public Long obterQuantidadeDividasGeradasData(List<Long> idsCota) {
		
		StringBuilder hql = new StringBuilder("select count(c.id) ");
		hql.append(" from ConsolidadoFinanceiroCota c, Distribuidor d, Divida divida ")
		   .append(" where c.dataConsolidado = d.dataOperacao ")
		   .append(" and c.id = divida.consolidado.id ")
		   .append(" and divida.data = d.dataOperacao ");
		
		if (idsCota != null) {
			
			hql.append("and c.cota.id in (:idsCota)");
		}
		
		Query query = 
				this.getSession().createQuery(hql.toString());
		
		if (idsCota != null) {
			
			query.setParameterList("idsCota", idsCota);
		}
		
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

	@SuppressWarnings("unchecked")
	@Override
	public List<ConsolidadoFinanceiroCota> obterConsolidadosDataOperacao(Long idCota) {
		
		StringBuilder hql = new StringBuilder("select c from ConsolidadoFinanceiroCota c, Distribuidor d ");
		
		if (idCota != null){
			
			hql.append(" join c.cota cota ");
		}
		
		hql.append(" where c.dataConsolidado = d.dataOperacao ");
		
		if (idCota != null){
			
			hql.append(" and cota.id = :idCota ");
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		
		if (idCota != null){
			
			query.setParameter("idCota", idCota);
		}
		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ContaCorrenteCotaVO> obterContaCorrente(FiltroViewContaCorrenteCotaDTO filtro,
			List<Long> tiposMovimentoCredito, List<Long> tiposMovimentoDebito,
			List<Long> tipoMovimentoEncalhe, List<Long> tiposMovimentoEncargos,
			List<Long> tiposMovimentoPostergadoCredito, List<Long> tiposMovimentoPostergadoDebito,
			List<Long> tipoMovimentoVendaEncalhe){
		
		StringBuilder sql = new StringBuilder("select ");
		sql.append(" cfc.ID as id, ")
		   .append(" cfc.COTA_ID as cotaId, ")
		   .append(" COTA.NUMERO_COTA as numeroCota, ")
		   .append(" cfc.CONSIGNADO as consignado, ")
		   .append(" cfc.DT_CONSOLIDADO as dataConsolidado, ")
		   .append(" cfc.DEBITO_CREDITO as debitoCredito, ")
		   .append(" cfc.ENCALHE as encalhe, ")
		   .append(" cfc.ENCARGOS as encargos, ")
		   .append(" cfc.PENDENTE as pendente, ")
		   .append(" cfc.TOTAL as total, ")
		   .append(" cfc.VALOR_POSTERGADO as valorPostergado, ")
		   .append(" cfc.VENDA_ENCALHE as vendaEncalhe, ")
		   .append(" 'CONSOLIDADO' as tipo, ")
		   .append("(")
		   .append(" select case when divida.DATA is not null then divida.DATA else acumulada.DATA end as dataRaiz ")
		   .append(" from DIVIDA acumulada ")
		   .append(" left join DIVIDA divida ")
		   .append("      ON divida.ID = acumulada.DIVIDA_RAIZ_ID ")
		   .append(" where acumulada.CONSOLIDADO_ID = cfc.ID ")
		   .append(" )AS dataRaiz, ")
		   .append(" coalesce((select sum(bc.VALOR_PAGO) ")
		   .append("           from BAIXA_COBRANCA bc ")
		   .append("           inner join COBRANCA cobranca ")
		   .append("                 ON cobranca.ID = bc.COBRANCA_ID ")
		   .append("           inner join DIVIDA divida ")
		   .append("                 on divida.ID = cobranca.DIVIDA_ID ")
		   .append("           where bc.STATUS not in (:statusBaixaCobranca) ")
		   .append("           and cota.ID = cobranca.COTA_ID ")
		   .append("           and divida.CONSOLIDADO_ID = cfc.ID ")
		   .append("           and cfc.ID),0) as valorPago, ")
		   .append(" total - coalesce((select sum(bc.VALOR_PAGO) ")
		   .append("           from BAIXA_COBRANCA bc ")
		   .append("           inner join COBRANCA cobranca ")
		   .append("                 on cobranca.ID = bc.COBRANCA_ID ")
		   .append("           inner join DIVIDA divida ")
		   .append("                 on divida.ID = cobranca.DIVIDA_ID ")
		   .append("           where bc.STATUS not in (:statusBaixaCobranca) ")
		   .append("           and cota.ID = cobranca.COTA_ID ")
		   .append("           and divida.CONSOLIDADO_ID = cfc.ID ")
		   .append("           and cfc.ID),0) as saldo ")
		   .append(" from CONSOLIDADO_FINANCEIRO_COTA cfc ")
		   .append(" inner join COTA cota on cota.ID = cfc.COTA_ID")
		   .append(" where cota.NUMERO_COTA = :numeroCota ");
		
		if (filtro.getInicioPeriodo() != null && filtro.getFimPeriodo() != null){
			
			sql.append(" and cfc.DT_CONSOLIDADO between :inicioPeriodo and :fimPeriodo ");
		}
		
		sql.append(" union all ")
		   
		   .append(" select ")
		   .append(" null as id, ")
		   .append(" mfc.COTA_ID as cotaId, ")
		   .append(" null as numeroCota, ")
		   .append(" null as consignado, ")
		   .append(" mfc.DATA as dataConsolidado, ")
		   
		   //crédito
		   .append("(coalesce((select sum(m.VALOR) ")
		   .append(" from MOVIMENTO_FINANCEIRO_COTA m ")
		   .append(" inner join COTA on COTA.ID = m.COTA_ID")
		   .append(" where COTA.NUMERO_COTA = :numeroCota ")
		   .append(" and m.TIPO_MOVIMENTO_ID in (:tiposMovimentoCredito) ")
		   .append(" and m.DATA not in (")
		   .append("     select DT_CONSOLIDADO ")
		   .append("     from CONSOLIDADO_FINANCEIRO_COTA ")
		   .append("     inner join COTA on COTA.ID = CONSOLIDADO_FINANCEIRO_COTA.COTA_ID")
		   .append(") and m.DATA = mfc.DATA ")
		   .append("),0) - ")
		   //débito
		   .append(" coalesce((select sum(m.VALOR) ")
		   .append(" from MOVIMENTO_FINANCEIRO_COTA m ")
		   .append(" inner join COTA on COTA.ID = m.COTA_ID")
		   .append(" where COTA.NUMERO_COTA = :numeroCota ")
		   .append(" and m.TIPO_MOVIMENTO_ID in (:tiposMovimentoDebito) ")
		   .append(" and m.DATA not in (")
		   .append("     select DT_CONSOLIDADO ")
		   .append("     from CONSOLIDADO_FINANCEIRO_COTA ")
		   .append("     inner join COTA on COTA.ID = CONSOLIDADO_FINANCEIRO_COTA.COTA_ID")
		   .append(") and m.DATA = mfc.DATA ")
		   .append("),0)")
		   .append(") as debitoCredito, ")
		   
		   //encalhe
		   .append("coalesce((select sum(m.VALOR) ")
		   .append(" from MOVIMENTO_FINANCEIRO_COTA m ")
		   .append(" inner join COTA on COTA.ID = m.COTA_ID")
		   .append(" where COTA.NUMERO_COTA = :numeroCota ")
		   .append(" and m.TIPO_MOVIMENTO_ID in (:tipoMovimentoEncalhe) ")
		   .append(" and m.DATA not in (")
		   .append("     select DT_CONSOLIDADO ")
		   .append("     from CONSOLIDADO_FINANCEIRO_COTA ")
		   .append("     inner join COTA on COTA.ID = CONSOLIDADO_FINANCEIRO_COTA.COTA_ID")
		   .append(") and m.DATA = mfc.DATA ")
		   .append("),0) as encalhe, ")
		   
		   //encargos
		   .append("coalesce((select sum(m.VALOR) ")
		   .append(" from MOVIMENTO_FINANCEIRO_COTA m ")
		   .append(" inner join COTA on COTA.ID = m.COTA_ID")
		   .append(" where COTA.NUMERO_COTA = :numeroCota ")
		   .append(" and m.TIPO_MOVIMENTO_ID in (:tiposMovimentoEncargos) ")
		   .append(" and m.DATA not in (")
		   .append("     select DT_CONSOLIDADO ")
		   .append("     from CONSOLIDADO_FINANCEIRO_COTA ")
		   .append("     inner join COTA on COTA.ID = CONSOLIDADO_FINANCEIRO_COTA.COTA_ID")
		   .append(") and m.DATA = mfc.DATA ")
		   .append("),0) as encargos, ")
		   
		   //pendente
		   .append("coalesce((select sum(m.VALOR) ")
		   .append(" from MOVIMENTO_FINANCEIRO_COTA m ")
		   .append(" inner join COTA on COTA.ID = m.COTA_ID")
		   .append(" where COTA.NUMERO_COTA = :numeroCota ")
		   .append(" and m.DATA not in (")
		   .append("     select DT_CONSOLIDADO ")
		   .append("     from CONSOLIDADO_FINANCEIRO_COTA ")
		   .append("     inner join COTA on COTA.ID = CONSOLIDADO_FINANCEIRO_COTA.COTA_ID")
		   .append(") and m.DATA = mfc.DATA ")
		   .append("),0) as pendente, ")
		   
		   //total
		   .append("coalesce((select sum(m.VALOR) ")
		   .append(" from MOVIMENTO_FINANCEIRO_COTA m ")
		   .append(" inner join COTA on COTA.ID = m.COTA_ID")
		   .append(" where COTA.NUMERO_COTA = :numeroCota ")
		   .append(" and m.DATA not in (")
		   .append("     select DT_CONSOLIDADO ")
		   .append("     from CONSOLIDADO_FINANCEIRO_COTA ")
		   .append("     inner join COTA on COTA.ID = CONSOLIDADO_FINANCEIRO_COTA.COTA_ID")
		   .append(") and m.DATA = mfc.DATA ")
		   .append("),0) as total, ")
		   
		   //valorPostergado
		   //valorPostergado credito
		   .append("(coalesce((select sum(m.VALOR) ")
		   .append(" from MOVIMENTO_FINANCEIRO_COTA m ")
		   .append(" inner join COTA on COTA.ID = m.COTA_ID")
		   .append(" where COTA.NUMERO_COTA = :numeroCota ")
		   .append(" and m.TIPO_MOVIMENTO_ID in (:tiposMovimentoPostergadoCredito) ")
		   .append(" and m.DATA not in (")
		   .append("     select DT_CONSOLIDADO ")
		   .append("     from CONSOLIDADO_FINANCEIRO_COTA ")
		   .append("     inner join COTA on COTA.ID = CONSOLIDADO_FINANCEIRO_COTA.COTA_ID ")
		   .append(") and m.DATA = mfc.DATA ")
		   .append("),0) - ")
		   //valorPostergado débito
		   .append(" coalesce((select sum(m.VALOR) ")
		   .append(" from MOVIMENTO_FINANCEIRO_COTA m ")
		   .append(" inner join COTA on COTA.ID = m.COTA_ID")
		   .append(" where COTA.NUMERO_COTA = :numeroCota ")
		   .append(" and m.TIPO_MOVIMENTO_ID in (:tiposMovimentoPostergadoDebito) ")
		   .append(" and m.DATA not in (")
		   .append("     select DT_CONSOLIDADO ")
		   .append("     from CONSOLIDADO_FINANCEIRO_COTA ")
		   .append("     inner join COTA on COTA.ID = CONSOLIDADO_FINANCEIRO_COTA.COTA_ID")
		   .append(") and m.DATA = mfc.DATA ")
		   .append("),0)")
		   .append(") as valorPostergado, ")
		   
		   //vendaEncalhe
		   .append("coalesce((select sum(m.VALOR) ")
		   .append(" from MOVIMENTO_FINANCEIRO_COTA m ")
		   .append(" inner join COTA on COTA.ID = m.COTA_ID")
		   .append(" where COTA.NUMERO_COTA = :numeroCota ")
		   .append(" and m.TIPO_MOVIMENTO_ID in (:tipoMovimentoVendaEncalhe) ")
		   .append(" and m.DATA not in (")
		   .append("     select DT_CONSOLIDADO ")
		   .append("     from CONSOLIDADO_FINANCEIRO_COTA ")
		   .append("     inner join COTA on COTA.ID = CONSOLIDADO_FINANCEIRO_COTA.COTA_ID ")
		   .append(") and m.DATA = mfc.DATA ")
		   .append("),0) as vendaEncalhe, ")
		   
		   //tipo
		   .append(" 'MOVIMENTO FINAN' as tipo, ")
		   
		   //data raiz
		   .append(" mfc.DATA as dataRaiz, ")
		   
		   //valor pago
		   .append(" 0 as valorPago, ")
		   
		   //saldo
		   .append("coalesce((select sum(m.VALOR) ")
		   .append(" from MOVIMENTO_FINANCEIRO_COTA m ")
		   .append(" inner join COTA on COTA.ID = m.COTA_ID")
		   .append(" where COTA.NUMERO_COTA = :numeroCota ")
		   .append(" and m.DATA not in (")
		   .append("     select DT_CONSOLIDADO ")
		   .append("     from CONSOLIDADO_FINANCEIRO_COTA ")
		   .append("     inner join COTA on COTA.ID = CONSOLIDADO_FINANCEIRO_COTA.COTA_ID ")
		   .append(") and m.DATA = mfc.DATA ")
		   .append("),0) as saldo ")
		   
		   .append(" from MOVIMENTO_FINANCEIRO_COTA mfc ")
		   .append(" inner join COTA on COTA.ID = mfc.COTA_ID")
		   .append(" where COTA.NUMERO_COTA = :numeroCota ")
		   .append(" and mfc.DATA not in (")
		   .append("     select DT_CONSOLIDADO ")
		   .append("     from CONSOLIDADO_FINANCEIRO_COTA ")
		   .append("     inner join COTA on COTA.ID = CONSOLIDADO_FINANCEIRO_COTA.COTA_ID")
		   .append(")");
		
		if (filtro.getInicioPeriodo() != null && filtro.getFimPeriodo() != null){
			
			sql.append(" and mfc.DATA between :inicioPeriodo and :fimPeriodo ");
		}
		
		sql.append(" group by mfc.DATA ");
		
		if (filtro.getColunaOrdenacao() != null) {
			sql.append(" order by ").append(filtro.getColunaOrdenacao()).append(" ");
			
			if (filtro.getPaginacao() != null) {
				
				sql.append(filtro.getPaginacao().getOrdenacao());
			}else{
				
				sql.append("asc");
			}
		}
		
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		
		query.addScalar("id", StandardBasicTypes.LONG);
		query.addScalar("cotaId", StandardBasicTypes.LONG);
		query.addScalar("numeroCota", StandardBasicTypes.INTEGER);
		query.addScalar("consignado", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("dataConsolidado", StandardBasicTypes.DATE);
		query.addScalar("debitoCredito", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("encalhe", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("encargos", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("pendente", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("total", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("valorPostergado", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("vendaEncalhe", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("tipo", StandardBasicTypes.STRING);
		query.addScalar("dataRaiz", StandardBasicTypes.DATE);
		query.addScalar("valorPago", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("saldo", StandardBasicTypes.BIG_DECIMAL);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ContaCorrenteCotaVO.class));
		
		query.setParameter("numeroCota", filtro.getNumeroCota());
		
		List<StatusBaixa> statusBaixaCobranca = 
				Arrays.asList(StatusBaixa.NAO_PAGO_BAIXA_JA_REALIZADA,
						StatusBaixa.NAO_PAGO_DIVERGENCIA_DATA,
						StatusBaixa.NAO_PAGO_DIVERGENCIA_VALOR,
						StatusBaixa.NAO_PAGO_POSTERGADO);
		
		query.setParameterList("statusBaixaCobranca", statusBaixaCobranca);
		
		if(filtro.getInicioPeriodo()!= null && filtro.getFimPeriodo()!= null){
			
			query.setParameter("inicioPeriodo", filtro.getInicioPeriodo());
			query.setParameter("fimPeriodo", filtro.getFimPeriodo());
		}
		
		query.setParameterList("tiposMovimentoCredito", tiposMovimentoCredito);
		query.setParameterList("tiposMovimentoDebito", tiposMovimentoDebito);
		query.setParameterList("tipoMovimentoEncalhe", tipoMovimentoEncalhe);
		query.setParameterList("tiposMovimentoEncargos", tiposMovimentoEncargos);
		query.setParameterList("tiposMovimentoPostergadoCredito", tiposMovimentoPostergadoCredito);
		query.setParameterList("tiposMovimentoPostergadoDebito", tiposMovimentoPostergadoDebito);
		query.setParameterList("tipoMovimentoVendaEncalhe", tipoMovimentoVendaEncalhe);
		
		PaginacaoVO paginacao = filtro.getPaginacao();
		if (paginacao != null) {
			
			if (paginacao.getPosicaoInicial() != null) {
				
				query.setFirstResult(paginacao.getPosicaoInicial());
			}
			
			if (paginacao.getQtdResultadosPorPagina() != null) {
				
				query.setMaxResults(paginacao.getQtdResultadosPorPagina());
			}
		}
		
		return query.list();
	}
	
	@Override
	public BigInteger countObterContaCorrente(FiltroViewContaCorrenteCotaDTO filtro){
		
		StringBuilder sql = new StringBuilder("select count(cotaId) from (");
		sql.append(" select cfc.COTA_ID as cotaId, ")
		   .append(" cfc.DT_CONSOLIDADO as dataConsolidado ")
		   .append(" from CONSOLIDADO_FINANCEIRO_COTA cfc ")
		   .append(" inner join COTA on COTA.ID = cfc.COTA_ID")
		   .append(" where COTA.NUMERO_COTA = :numeroCota ");
		
		if (filtro.getInicioPeriodo() != null && filtro.getFimPeriodo() != null){
			
			sql.append(" and cfc.DT_CONSOLIDADO between :inicioPeriodo and :fimPeriodo ");
		}
		
		sql.append(" union all ")
		   
		   .append(" select mfc.COTA_ID as cotaId, ")
		   .append(" mfc.DATA as dataConsolidado ")
		   .append(" from MOVIMENTO_FINANCEIRO_COTA mfc ")
		   .append(" inner join COTA on COTA.ID = mfc.COTA_ID")
		   .append(" where COTA.NUMERO_COTA = :numeroCota ")
		   .append(" and mfc.DATA not in (")
		   .append("     select DT_CONSOLIDADO ")
		   .append("     from CONSOLIDADO_FINANCEIRO_COTA ")
		   .append("     inner join COTA on COTA.ID = CONSOLIDADO_FINANCEIRO_COTA.COTA_ID")
		   .append(")");
		
		if (filtro.getInicioPeriodo() != null && filtro.getFimPeriodo() != null){
			
			sql.append(" and mfc.DATA between :inicioPeriodo and :fimPeriodo ");
		}
		
		sql.append(" group by dataConsolidado ")
		   .append(") as tmp ");
		
		Query query = this.getSession().createSQLQuery(sql.toString());
		
		query.setParameter("numeroCota", filtro.getNumeroCota());
		
		if(filtro.getInicioPeriodo()!= null && filtro.getFimPeriodo()!= null){
			
			query.setParameter("inicioPeriodo", filtro.getInicioPeriodo());
			query.setParameter("fimPeriodo", filtro.getFimPeriodo());
		}
		
		return (BigInteger) query.uniqueResult();
	}
}
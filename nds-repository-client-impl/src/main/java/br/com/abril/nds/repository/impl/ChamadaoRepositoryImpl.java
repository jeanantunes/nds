package br.com.abril.nds.repository.impl;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ConsignadoCotaChamadaoDTO;
import br.com.abril.nds.dto.ResumoConsignadoCotaChamadaoDTO;
import br.com.abril.nds.dto.filtro.FiltroChamadaoDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.StatusEstoqueFinanceiro;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoChamadaEncalhe;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.ChamadaoRepository;

/**
 * Classe de implementação referente ao acesso a dados
 * para as pesquisas de consignados do chamadão.
 * 
 * @author Discover Technology
 */
@Repository
public class ChamadaoRepositoryImpl extends AbstractRepositoryModel<Cota,Long> implements ChamadaoRepository {

	
	/**
	 * Construtor padrão
	 */
	public ChamadaoRepositoryImpl() {
		super(Cota.class);
	}

	@Override
	public ResumoConsignadoCotaChamadaoDTO obterResumoConsignadosParaChamadao(FiltroChamadaoDTO filtro) {
		
		StringBuilder hql = new StringBuilder("select SUM(consignadoCota.qtdExemplaresTotal) AS qtdExemplaresTotal, SUM(consignadoCota.valorTotal) AS valorTotal FROM ( ");
		
		hql.append("SELECT ")
			.append(" if(tipo.operacao_estoque = 'ENTRADA', mec.qtde, -mec.qtde) AS qtdExemplaresTotal, ")
			.append(" (mec.PRECO_COM_DESCONTO) * (if(tipo.operacao_estoque = 'ENTRADA', mec.qtde, -mec.qtde)) AS valorTotal ");
		
		hql.append(this.gerarQueryConsignados(filtro));
		
		hql.append(" ) AS consignadoCota");
		
		Query query = this.getSession().createSQLQuery(hql.toString())
			.addScalar("qtdExemplaresTotal", StandardBasicTypes.BIG_INTEGER)
			.addScalar("valorTotal", StandardBasicTypes.BIG_DECIMAL);
		
		this.aplicarParametrosParaPesquisaConsignadosCota(filtro, query);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
			ResumoConsignadoCotaChamadaoDTO.class));
			
		return (ResumoConsignadoCotaChamadaoDTO) query.uniqueResult();
	}
	
	@Override
	public ResumoConsignadoCotaChamadaoDTO obterResumoConsignadosComChamadao(FiltroChamadaoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT ")
			.append(" sum(chamadaEncalheCota.qtdePrevista) as qtdExemplaresTotal, ")
			.append(" sum(mec.valoresAplicados.precoComDesconto * mec.qtde) as valorTotal ");
		
		hql.append(this.gerarQueryChamadasEncalhe(filtro));
		
		Query query = this.getSession().createQuery(hql.toString());
		
		this.aplicarParametrosParaPesquisaChamadaEncalhe(filtro, query);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
			ResumoConsignadoCotaChamadaoDTO.class));
			
		return (ResumoConsignadoCotaChamadaoDTO) query.uniqueResult();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<ConsignadoCotaChamadaoDTO> obterConsignadosParaChamadao(FiltroChamadaoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT ");
		hql.append("     produto.CODIGO AS codigoProduto, ");
		hql.append("     produto.NOME AS nomeProduto, ");
		hql.append("     produtoEdicao.NUMERO_EDICAO AS numeroEdicao, ");
		hql.append("     produtoEdicao.PRECO_VENDA AS precoVenda, ");
		hql.append("     mec.VALOR_DESCONTO AS desconto, ");
		hql.append("     mec.PRECO_COM_DESCONTO AS precoDesconto, ");
		hql.append("     sum(if(tipo.operacao_estoque = 'ENTRADA', mec.qtde, -mec.qtde)) as reparte, ");
		hql.append("     pessoa.RAZAO_SOCIAL AS nomeFornecedor, ");
		hql.append("     lancamento.DATA_REC_PREVISTA AS dataRecolhimento, ");
		hql.append("     sum(if(tipo.operacao_estoque = 'ENTRADA', mec.qtde, -mec.qtde)) * mec.PRECO_VENDA AS valorTotal, ");
		hql.append("     sum(if(tipo.operacao_estoque = 'ENTRADA', mec.qtde, -mec.qtde)) * mec.PRECO_COM_DESCONTO AS valorTotalDesconto, ");
		hql.append("     min(lancamento.ID) AS idLancamento, ");
		hql.append("     produtoEdicao.POSSUI_BRINDE AS possuiBrinde ");
		
		hql.append(this.gerarQueryConsignados(filtro));
		
		if (filtro != null && filtro.getOrdenacaoColuna() != null) {
			
			switch (filtro.getOrdenacaoColuna()) {
				
				case CODIGO_PRODUTO:
					hql.append(" ORDER BY codigoProduto ");
					break;
					
				case NOME_PRODUTO:
					hql.append(" ORDER BY nomeProduto ");
					break;
					
				case EDICAO:
					hql.append(" ORDER BY numeroEdicao ");
					break;
				
				case BRINDE:
					hql.append(" ORDER BY possuiBrinde ");
					break;
					
				case PRECO_VENDA:
					hql.append(" ORDER BY precoVenda ");
					break;
					
				case PRECO_DESCONTO:
					hql.append(" ORDER BY precoDesconto ");
					break;
					
				case REPARTE:
					hql.append(" ORDER BY reparte ");
					break;
					
				case FORNECEDOR:
					hql.append(" ORDER BY nomeFornecedor ");
					break;
					
				case RECOLHIMENTO:
					hql.append(" ORDER BY dataRecolhimento ");
					break;
				
				case VALOR_TOTAL:
					hql.append(" ORDER BY valorTotal ");
					break;
				
				case VALOR_TOTAL_DESCONTO:
					hql.append(" ORDER BY valorTotalDesconto ");
					break;
					
				default:
					break;
			}
			
			if (filtro.getPaginacao() != null && filtro.getPaginacao().getOrdenacao() != null) {
				
				hql.append(filtro.getPaginacao().getOrdenacao().toString());
			}
		}
		
		Query query = this.getSession().createSQLQuery(hql.toString())
			.addScalar("codigoProduto").addScalar("nomeProduto")
			.addScalar("numeroEdicao", StandardBasicTypes.LONG).addScalar("precoVenda")
			.addScalar("precoDesconto").addScalar("reparte", StandardBasicTypes.BIG_INTEGER)
			.addScalar("nomeFornecedor").addScalar("dataRecolhimento")
			.addScalar("valorTotal").addScalar("valorTotalDesconto")
			.addScalar("idLancamento", StandardBasicTypes.LONG)
			.addScalar("possuiBrinde", StandardBasicTypes.BOOLEAN);
		
		this.aplicarParametrosParaPesquisaConsignadosCota(filtro, query);
		
		if (filtro != null && filtro.getPaginacao() != null) {
			
			if (filtro.getPaginacao().getPosicaoInicial() != null) {
				query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			}
			
			if (filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
				query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
			}
		}
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ConsignadoCotaChamadaoDTO.class));
		
		return query.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<ConsignadoCotaChamadaoDTO> obterConsignadosComChamadao(FiltroChamadaoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("select distinct ")
			.append("mec.lancamento.id as idLancamento, ")
			.append("produto.codigo as codigoProduto, ")
			.append("produto.nome as nomeProduto, ")
			.append("produtoEdicao.numeroEdicao as numeroEdicao, ")
			.append("mec.valoresAplicados.precoVenda as precoVenda, ")
			.append("(mec.valoresAplicados.precoComDesconto) as precoDesconto, ")
			.append("chamadaEncalheCota.qtdePrevista as reparte, ")
			.append("juridica.razaoSocial as nomeFornecedor, ")
			.append("chamadaEncalhe.dataRecolhimento as dataRecolhimento, ")
			.append("mec.valoresAplicados.precoVenda * mec.qtde as valorTotal, ")
			.append("(mec.valoresAplicados.precoComDesconto) * mec.qtde as valorTotalDesconto, ")
			.append("produtoEdicao.possuiBrinde as possuiBrinde ");
		
		hql.append(this.gerarQueryChamadasEncalhe(filtro));
		
		if (filtro != null && filtro.getOrdenacaoColuna() != null) {
			
			switch (filtro.getOrdenacaoColuna()) {
				
				case CODIGO_PRODUTO:
					hql.append(" order by codigoProduto ");
					break;
					
				case NOME_PRODUTO:
					hql.append(" order by nomeProduto ");
					break;
					
				case EDICAO:
					hql.append(" order by numeroEdicao ");
					break;
				
				case BRINDE:
					hql.append(" order by possuiBrinde ");
					break;
					
				case PRECO_VENDA:
					hql.append(" order by precoVenda ");
					break;
					
				case PRECO_DESCONTO:
					hql.append(" order by precoDesconto ");
					break;
					
				case REPARTE:
					hql.append(" order by reparte ");
					break;
					
				case FORNECEDOR:
					hql.append(" order by nomeFornecedor ");
					break;
					
				case RECOLHIMENTO:
					hql.append(" order by dataRecolhimento ");
					break;
				
				case VALOR_TOTAL:
					hql.append(" order by valorTotal ");
					break;
				
				case VALOR_TOTAL_DESCONTO:
					hql.append(" order by valorTotalDesconto ");
					break;
					
				default:
					break;
			}
			
			if (filtro.getPaginacao().getOrdenacao() != null) {
				
				hql.append(filtro.getPaginacao().getOrdenacao().toString());
			}
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		
		this.aplicarParametrosParaPesquisaChamadaEncalhe(filtro, query);
		
		if (filtro != null && filtro.getPaginacao() != null) {
			
			if (filtro.getPaginacao().getPosicaoInicial() != null) {
				query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			}
			
			if (filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
				query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
			}
		}
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ConsignadoCotaChamadaoDTO.class));
		
		return query.list();
	}
	
	@Override
	public Long obterTotalConsignadosParaChamadao(FiltroChamadaoDTO filtro) {
		
		StringBuilder hql = new StringBuilder("SELECT COUNT(consignadoCota.totalConsignados) AS totalConsignados FROM ( ");
				
		hql.append("SELECT COUNT(cota.ID) AS totalConsignados ");
				
		hql.append(this.gerarQueryConsignados(filtro));
		
		hql.append(" ) AS consignadoCota ");
		
		Query query = getSession().createSQLQuery(hql.toString())
			.addScalar("totalConsignados", StandardBasicTypes.LONG);
		
		this.aplicarParametrosParaPesquisaConsignadosCota(filtro, query);
		
		return (Long) query.uniqueResult();
	}
	
	@Override
	public Long obterTotalConsignadosComChamadao(FiltroChamadaoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
				
		hql.append("SELECT count(chamadaEncalhe.id) ");
				
		hql.append(this.gerarQueryChamadasEncalhe(filtro));
		
		Query query = getSession().createQuery(hql.toString());
		
		this.aplicarParametrosParaPesquisaChamadaEncalhe(filtro, query);
		
		return (Long) query.uniqueResult();
	}
	
	/**
	 * Gera a query de consignados da cota.
	 *   
	 * @param filtro - filtro da pesquisa
	 * 
	 * @return Query
	 */
	private StringBuilder gerarQueryConsignados(FiltroChamadaoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" FROM LANCAMENTO lancamento ");
		hql.append(" INNER JOIN PRODUTO_EDICAO produtoEdicao ON lancamento.PRODUTO_EDICAO_ID = produtoEdicao.ID ");		
		hql.append(" INNER JOIN PRODUTO produto ON produtoEdicao.PRODUTO_ID = produto.ID ");
		hql.append(" INNER JOIN MOVIMENTO_ESTOQUE_COTA mec ON mec.LANCAMENTO_ID = lancamento.ID ");
		hql.append(" INNER JOIN TIPO_MOVIMENTO tipo ON mec.TIPO_MOVIMENTO_ID = TIPO.ID ");
		hql.append(" INNER JOIN COTA cota ON cota.id = mec.COTA_ID ");
		hql.append(" INNER JOIN ESTOQUE_PRODUTO_COTA estoqueProdCota ");
		hql.append("         ON (produtoEdicao.ID = estoqueProdCota.PRODUTO_EDICAO_ID and cota.id = estoqueProdCota.COTA_ID) ");
		hql.append(" INNER JOIN PRODUTO_FORNECEDOR produtoFornecedor ON produtoFornecedor.PRODUTO_ID = produto.ID ");
		hql.append(" INNER JOIN FORNECEDOR fornecedor ON produtoFornecedor.fornecedores_ID = fornecedor.ID ");
		hql.append(" INNER JOIN PESSOA pessoa ON fornecedor.JURIDICA_ID = pessoa.ID ");
		
	//  hql.append(" WHERE   tipo.GRUPO_MOVIMENTO_ESTOQUE = :grupoMovRecebimentoReparte ");
		
		hql.append(" WHERE lancamento.STATUS IN (:statusLancamento) ");
		
		if (filtro.getDataChamadao() != null) {
			
			hql.append("      AND lancamento.DATA_REC_DISTRIB >= :dataRecolhimento ");
		}
		
		hql.append("      AND mec.status_estoque_financeiro = :statusEstoqueFinanceiro ");
		
		hql.append("      AND (estoqueProdCota.QTDE_RECEBIDA - estoqueProdCota.QTDE_DEVOLVIDA) > 0 ");
		
		hql.append(" AND NOT EXISTS ( ");
		hql.append(" 	SELECT chamadaEncalheCota.COTA_ID FROM CHAMADA_ENCALHE_COTA chamadaEncalheCota ");
		hql.append(" 	JOIN CHAMADA_ENCALHE chamadaEncalhe ON chamadaEncalheCota.CHAMADA_ENCALHE_ID = chamadaEncalhe.id ");
		hql.append(" 	WHERE chamadaEncalheCota.COTA_ID = cota.ID ");
		hql.append(" 	AND chamadaEncalhe.PRODUTO_EDICAO_ID = produtoEdicao.ID ");
		hql.append(" 	AND chamadaEncalhe.TIPO_CHAMADA_ENCALHE = :tipoChamadaEncalhe ");
		hql.append(" 	AND chamadaEncalheCota.FECHADO = false ");
		hql.append(" ) ");
		
		if (filtro != null) {
		
			if (filtro.getNumeroCota() != null ) {
				
				hql.append(" AND cota.NUMERO_COTA = :numeroCota ");
			}

			if (filtro.getIdFornecedor() != null) {

				hql.append(" AND produtoFornecedor.FORNECEDORES_ID = :idFornecedor ");
			}

			if (filtro.getIdEditor() != null) {

				hql.append(" AND produto.EDITOR_ID = :idEditor ");
			}
		}
		
		hql.append(" GROUP BY produtoEdicao.ID");
		
		return hql;
	}
	
	/**
	 * Gera a query de chamadas de encalhe da cota.
	 *   
	 * @param filtro - filtro da pesquisa
	 * 
	 * @return Query
	 */
	private StringBuilder gerarQueryChamadasEncalhe(FiltroChamadaoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" FROM ChamadaEncalheCota chamadaEncalheCota ")
			.append(" JOIN chamadaEncalheCota.chamadaEncalhe chamadaEncalhe ")
			.append(" JOIN chamadaEncalhe.produtoEdicao produtoEdicao ")
			.append(" JOIN produtoEdicao.produto produto ")
			.append(" JOIN produto.fornecedores fornecedores ")
			.append(" JOIN fornecedores.juridica juridica ")
			.append(" JOIN chamadaEncalheCota.cota cota ")
			.append(" JOIN cota.movimentoEstoqueCotas mec ")
			
			.append(" WHERE chamadaEncalhe.tipoChamadaEncalhe = :tipoChamadaEncalhe ")
			.append(" AND mec.tipoMovimento.grupoMovimentoEstoque = :grupoMovimento ")
			.append(" AND mec.lancamento.id in ( select lan.id from ChamadaEncalhe cham join cham.lancamentos lan where cham.id = chamadaEncalhe.id ) ")
			.append(" AND mec.movimentoEstoqueCotaFuro is null ");
		
		if (filtro != null) {
		
			if (filtro.getDataChamadao() != null) {
				
				hql.append(" AND chamadaEncalhe.dataRecolhimento = :dataChamadao ");
			}
			
			if (filtro.getNumeroCota() != null ) {
				
				hql.append(" AND cota.numeroCota = :numeroCota ");
			}

			if (filtro.getIdFornecedor() != null) {

				hql.append(" AND fornecedores.id = :idFornecedor ");
			}

			if (filtro.getIdEditor() != null) {

				hql.append(" AND produto.editor.id = :idEditor ");
			}
		}
		
		return hql;
	}
	
	/**
	 * Aplica os parâmetros para a busca de cosignados da cota.
	 *   
	 * @param filtro - filtro da pesquisa
	 * @param query - objeto query
	 */
	private void aplicarParametrosParaPesquisaConsignadosCota(FiltroChamadaoDTO filtro, Query query) {
		
	//  query.setParameter("grupoMovRecebimentoReparte", GrupoMovimentoEstoque.RECEBIMENTO_REPARTE.name());
		
		query.setParameter("tipoChamadaEncalhe", TipoChamadaEncalhe.CHAMADAO.name());
		
		query.setParameter("statusEstoqueFinanceiro", StatusEstoqueFinanceiro.FINANCEIRO_NAO_PROCESSADO.name());
		
		List<String> statusLancamento = new ArrayList<>();
		
		statusLancamento.add(StatusLancamento.EXPEDIDO.name());
		statusLancamento.add(StatusLancamento.EM_BALANCEAMENTO_RECOLHIMENTO.name());
		
		query.setParameterList("statusLancamento", statusLancamento);
		
		if (filtro == null) {
			
			return;
		}
		
		if (filtro.getDataChamadao() != null) {
			query.setParameter("dataRecolhimento", filtro.getDataChamadao());
		}
		
		if (filtro.getNumeroCota() != null) {
			query.setParameter("numeroCota", filtro.getNumeroCota());
		}
		
		if (filtro.getIdFornecedor() != null) {
			query.setParameter("idFornecedor", filtro.getIdFornecedor());
		}
		
		if (filtro.getIdEditor() != null) {
			query.setParameter("idEditor", filtro.getIdEditor());
		}
	}
	
	/**
	 * Aplica os parâmetros para a busca de chamada de encalhe.
	 *   
	 * @param filtro - filtro da pesquisa
	 * @param query - objeto query
	 */
	private void aplicarParametrosParaPesquisaChamadaEncalhe(FiltroChamadaoDTO filtro, 
													 	 	 Query query) {
		
		query.setParameter("tipoChamadaEncalhe", TipoChamadaEncalhe.CHAMADAO);
		query.setParameter("grupoMovimento", GrupoMovimentoEstoque.RECEBIMENTO_REPARTE);
		
		if (filtro == null) {
			
			return;
		}
		
		if (filtro.getDataChamadao() != null) {
			query.setParameter("dataChamadao", filtro.getDataChamadao());
		}
		
		if (filtro.getNumeroCota() != null) {
			query.setParameter("numeroCota", filtro.getNumeroCota());
		}
		
		if (filtro.getIdFornecedor() != null) {
			query.setParameter("idFornecedor", filtro.getIdFornecedor());
		}
		
		if (filtro.getIdEditor() != null) {
			query.setParameter("idEditor", filtro.getIdEditor());
		}
	}

}

package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.SuplementarFecharDiaDTO;
import br.com.abril.nds.dto.VendaFechamentoDiaDTO;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.OperacaoEstoque;
import br.com.abril.nds.model.estoque.TipoEstoque;
import br.com.abril.nds.model.estoque.TipoVendaEncalhe;
import br.com.abril.nds.repository.AbstractRepository;
import br.com.abril.nds.repository.ResumoSuplementarFecharDiaRepository;
import br.com.abril.nds.vo.PaginacaoVO;

@Repository
public class ResumoSuplementarFecharDiaRepositoryImpl extends AbstractRepository implements
		ResumoSuplementarFecharDiaRepository {

	@Override
	public BigDecimal obterValorEstoqueLogico(Date dataOperacao) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT ");
		hql.append(" SUM(f.valorSaldo) ");
		hql.append(" FROM FechamentoDiarioConsolidadoSuplementar f ");
		hql.append(" JOIN f.fechamentoDiario as fd ");
		hql.append(" WHERE fd.dataFechamento = :dataOperacao");
		hql.append(" ) ");
		
		Query query = super.getSession().createQuery(hql.toString());
		query.setParameter("dataOperacao", dataOperacao);
		
		BigDecimal total =  (BigDecimal) query.uniqueResult();
		
		return total != null ? total : BigDecimal.ZERO ;
	}

	@Override
	public BigDecimal obterValorTransferencia(Date dataOperacao) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT ");
		hql.append("SUM( " +
		  "  CASE WHEN tm.grupoMovimentoEstoque in (:grupoEntradaSuplementar) " +
		  "  THEN (COALESCE( me.qtde, 0) * pe.precoVenda) " +
		  "  ELSE (CASE WHEN tm.grupoMovimentoEstoque in (:grupoSaidaSuplementar) THEN (COALESCE( -(me.qtde), 0) * pe.precoVenda) ELSE 0 END)" +
		  "  END) ");       
		hql.append(" FROM MovimentoEstoque as me ");
		hql.append(" JOIN me.tipoMovimento as tm ");       
		hql.append(" JOIN me.produtoEdicao as pe ");
		hql.append(" WHERE me.data = :dataOperacao");
		hql.append(" GROUP BY me.data");
		Query query = super.getSession().createQuery(hql.toString());
		query.setParameter("dataOperacao", dataOperacao);
	
        query.setParameterList("grupoEntradaSuplementar", Arrays.asList(
                GrupoMovimentoEstoque.SUPLEMENTAR_COTA_AUSENTE,
                GrupoMovimentoEstoque.SUPLEMENTAR_ENVIO_ENCALHE_ANTERIOR_PROGRAMACAO,
                GrupoMovimentoEstoque.ENTRADA_SUPLEMENTAR_ENVIO_REPARTE,
                GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_SUPLEMENTAR,
                GrupoMovimentoEstoque.ALTERACAO_REPARTE_COTA_PARA_SUPLEMENTAR));  
	
		query.setParameterList("grupoSaidaSuplementar", Arrays.asList(
		       GrupoMovimentoEstoque.REPARTE_COTA_AUSENTE,
		       GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_SUPLEMENTAR,
		       GrupoMovimentoEstoque.TRANSFERENCIA_PARCIAL_SAIDA_SUPLEMENTAR));
		
		BigDecimal total =  (BigDecimal) query.uniqueResult();
		
		return total != null ? total : BigDecimal.ZERO ;
	}

	@Override
	public BigDecimal obterValorVenda(Date dataOperacao) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT   ");
		hql.append(" SUM(ve.qntProduto * pe.precoVenda)");
		hql.append(" FROM VendaProduto as ve ");		 
		hql.append(" JOIN ve.produtoEdicao as pe ");					
		hql.append(" JOIN pe.produto as p ");					
		hql.append(" WHERE ve.dataOperacao = :dataOperacao ");
		
		hql.append(" AND ve.tipoVenda = :suplementar");

		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("dataOperacao", dataOperacao);
		
		query.setParameter("suplementar", TipoVendaEncalhe.SUPLEMENTAR);
		
		BigDecimal total =  (BigDecimal) query.uniqueResult();
		
		return total != null ? total : BigDecimal.ZERO;
	}
	
	@Override
	public BigDecimal obterValorAlteracaoPreco(Date dataOperacao) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select sum(coalesce(ep.QTDE_SUPLEMENTAR, 0) * (valor_atual - valor_antigo)) tot ")
		.append(" from historico_alteracao_preco_venda hapv                                          ")
		.append(" join estoque_produto ep on ep.PRODUTO_EDICAO_ID=hapv.PRODUTO_EDICAO_ID             ")
		.append(" join PRODUTO_EDICAO pe on ep.PRODUTO_EDICAO_ID=pe.ID                               ")
		.append(" join PRODUTO p on p.ID = pe.PRODUTO_ID                                             ")
		.append(" where hapv.id = (select max(id)                                                    ")
		.append(" 	from historico_alteracao_preco_venda hapv                                        ")
		.append("   where hapv.PRODUTO_EDICAO_ID = pe.ID                                             ")
		.append("   and hapv.DATA_OPERACAO = (select DATA_OPERACAO from distribuidor))               ");
	
		Query query = super.getSession().createSQLQuery(sql.toString());
		
		BigDecimal total =  (BigDecimal) query.uniqueResult();
		
		return total != null ? total : BigDecimal.ZERO ;
	}

	@Override
	public BigDecimal obterValorFisico(Date dataOperacao) {
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT SUM(ep.qtdeSuplementar * pe.precoVenda) ");
		hql.append(" FROM EstoqueProduto as ep ");
		hql.append(" JOIN ep.produtoEdicao pe ");
	
		Query query = super.getSession().createQuery(hql.toString());		
		
		BigDecimal total =  (BigDecimal) query.uniqueResult();
		return total != null ? total : BigDecimal.ZERO ;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VendaFechamentoDiaDTO> obterVendasSuplementar(Date dataOperacao) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT pe.id as idProdutoEdicao, p.codigo as codigo,  ");
		hql.append(" p.nome as nomeProduto, ");
		hql.append(" pe.numeroEdicao as numeroEdicao, ");
		hql.append(" ve.qntProduto as qtde, ");
		hql.append(" (ve.qntProduto * pe.precoVenda) as valor, ");
		hql.append(" ve.dataVenda as dataRecolhimento ");
		
		hql.append(" FROM VendaProduto as ve ");		 
		hql.append(" JOIN ve.produtoEdicao as pe ");					
		hql.append(" JOIN pe.produto as p ");					
		hql.append(" WHERE ve.dataOperacao = :dataOperacao ");
		
		hql.append(" AND ve.tipoVenda = :suplementar");

		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("dataOperacao", dataOperacao);
		
		query.setParameter("suplementar", TipoVendaEncalhe.SUPLEMENTAR);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				VendaFechamentoDiaDTO.class));
		
		return query.list();
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SuplementarFecharDiaDTO> obterDadosGridSuplementar(Date data, PaginacaoVO paginacao) {
	    Objects.requireNonNull(data, "Data para consulta estoque suplementar não deve ser nula!");


	    StringBuilder sql = new StringBuilder();

	    sql.append(" select ");
	    sql.append(" produtoedi1_.ID as idProdutoEdicao, ");
	    sql.append(" produto2_.CODIGO as codigo, ");
	    sql.append(" produto2_.NOME as nomeProduto, ");
	    sql.append(" produtoedi1_.NUMERO_EDICAO as numeroEdicao, ");
	    sql.append(" produtoedi1_.PRECO_VENDA as precoVenda, ");
	    sql.append(" coalesce(estoquepro0_.QTDE_SUPLEMENTAR, 0) as quantidadeContabil, ");
	    
	    sql.append(" coalesce(HISTORICO_ESTOQUE_PRODUTO.QTDE_SUPLEMENTAR, 0) as quantidadeLogico, ");
	    
	    sql.append(" ( select ");
	    sql.append(" coalesce(sum(vendaprodu8_.QNT_PRODUTO), ");
	    sql.append(" 0) ");
	    sql.append(" from ");
	    sql.append(" VENDA_PRODUTO vendaprodu8_, ");
	    sql.append(" PRODUTO_EDICAO produtoedi9_ ");
	    sql.append(" where ");
	    sql.append(" vendaprodu8_.ID_PRODUTO_EDICAO=produtoedi9_.ID ");
	    sql.append(" and vendaprodu8_.ID_PRODUTO_EDICAO=produtoedi1_.ID ");
	    sql.append("                 and vendaprodu8_.DATA_OPERACAO= :data ");
	    sql.append(" and vendaprodu8_.TIPO_VENDA_ENCALHE=:tipoVendaSuplementar ");
	    sql.append(" ) as quantidadeVenda,  ");
	    
	    sql.append(" coalesce(( ");
	    sql.append(" select ");
	    sql.append(" sum(coalesce(movimentoe10_.QTDE, 0)) ");
	    sql.append(" from ");
	    sql.append(" MOVIMENTO_ESTOQUE movimentoe10_, ");
	    sql.append(" PRODUTO_EDICAO produtoedi11_, ");
	    sql.append(" TIPO_MOVIMENTO tipomovime12_ ");
	    sql.append(" where ");
	    sql.append(" movimentoe10_.PRODUTO_EDICAO_ID=produtoedi11_.ID ");
	    sql.append(" and movimentoe10_.TIPO_MOVIMENTO_ID=tipomovime12_.ID ");
	    sql.append("                 and movimentoe10_.DATA= :data ");
	    sql.append(" and movimentoe10_.PRODUTO_EDICAO_ID=produtoedi1_.ID ");
	    sql.append(" and movimentoe10_.STATUS=:statusAprovado ");
	    sql.append(" and ( ");
	    sql.append(" tipomovime12_.GRUPO_MOVIMENTO_ESTOQUE in (:grupoEntradaSuplementar) ) ");
	    
	    sql.append(" ), 0) as quantidadeTransferenciaEntrada,  ");
	    
	   
	    sql.append(" coalesce(( ");
	    sql.append(" select ");
	    sql.append(" sum(coalesce(movimentoe13_.QTDE, 0)) ");
	    sql.append(" from ");
	    sql.append(" MOVIMENTO_ESTOQUE movimentoe13_, ");
	    sql.append(" PRODUTO_EDICAO produtoedi14_, ");
	    sql.append(" TIPO_MOVIMENTO tipomovime15_ ");
	    sql.append(" where ");
	    sql.append(" movimentoe13_.PRODUTO_EDICAO_ID=produtoedi14_.ID ");
	    sql.append(" and movimentoe13_.TIPO_MOVIMENTO_ID=tipomovime15_.ID ");
	    sql.append("                 and movimentoe13_.DATA= :data ");
	    sql.append("             and movimentoe13_.PRODUTO_EDICAO_ID=produtoedi1_.ID ");
	    sql.append(" and movimentoe13_.STATUS=:statusAprovado ");
	    sql.append("             and ( ");
	    sql.append(" tipomovime15_.GRUPO_MOVIMENTO_ESTOQUE in ( :grupoSaidaSuplementar ) ");
	    sql.append(" ) ");
	    sql.append(" ), 0) as quantidadeTransferenciaSaida ");
	    
	    sql.append(" from ");
	    sql.append(" ESTOQUE_PRODUTO estoquepro0_ ");
	    
	    sql.append(" inner join ");
	    sql.append(" PRODUTO_EDICAO produtoedi1_ ");
	    sql.append(" on estoquepro0_.PRODUTO_EDICAO_ID=produtoedi1_.ID ");
	    
	    sql.append(" inner join ");
	    sql.append(" PRODUTO produto2_ ");
	    sql.append(" on produtoedi1_.PRODUTO_ID=produto2_.ID ");
	    
	    
	    sql.append(" left join ");
	    sql.append(" HISTORICO_ESTOQUE_PRODUTO on ");
	    sql.append(" ( 		");
	    
	    sql.append(" HISTORICO_ESTOQUE_PRODUTO.PRODUTO_EDICAO_ID = produtoedi1_.ID AND	");
	    sql.append(" HISTORICO_ESTOQUE_PRODUTO.DATA = ");
	    
	    sql.append(" ( select max(hist.data)  ");
	    sql.append(" from historico_estoque_produto hist 	");
	    sql.append(" where hist.produto_edicao_id = produtoedi1_.id	) ");
	    
	    sql.append(" ) 		");
	    
   		sql.append(" group by estoquepro0_.id ");
   		sql.append(" having ((quantidadeContabil + quantidadeTransferenciaEntrada - quantidadeTransferenciaSaida - quantidadeVenda) <> 0) ");
   		sql.append(" order by idProdutoEdicao "); 

		SQLQuery query = getSession().createSQLQuery(sql.toString())
									 .addScalar("idProdutoEdicao", StandardBasicTypes.LONG)
									 .addScalar("codigo", StandardBasicTypes.STRING)
									 .addScalar("nomeProduto", StandardBasicTypes.STRING)
									 .addScalar("numeroEdicao", StandardBasicTypes.LONG)
									 .addScalar("precoVenda", StandardBasicTypes.BIG_DECIMAL)
									 .addScalar("quantidadeContabil", StandardBasicTypes.BIG_INTEGER)
									 .addScalar("quantidadeLogico", StandardBasicTypes.BIG_INTEGER)
									 .addScalar("quantidadeVenda", StandardBasicTypes.BIG_INTEGER)
									 .addScalar("quantidadeTransferenciaEntrada", StandardBasicTypes.BIG_INTEGER)
									 .addScalar("quantidadeTransferenciaSaida", StandardBasicTypes.BIG_INTEGER);
									 
		
		query.setParameter("data", data);
		query.setParameter("tipoVendaSuplementar", TipoVendaEncalhe.SUPLEMENTAR.name());
        query.setParameter("statusAprovado", StatusAprovacao.APROVADO.name());   
        
        query.setParameterList("grupoEntradaSuplementar", Arrays.asList(
                        GrupoMovimentoEstoque.SUPLEMENTAR_COTA_AUSENTE.name(),
                        GrupoMovimentoEstoque.SUPLEMENTAR_ENVIO_ENCALHE_ANTERIOR_PROGRAMACAO.name(),
                        GrupoMovimentoEstoque.ESTORNO_VENDA_ENCALHE_SUPLEMENTAR.name(),
                        GrupoMovimentoEstoque.ENTRADA_SUPLEMENTAR_ENVIO_REPARTE.name(),
                        GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_SUPLEMENTAR.name(),
                        GrupoMovimentoEstoque.ALTERACAO_REPARTE_COTA_PARA_SUPLEMENTAR.name()));  
        
        query.setParameterList("grupoSaidaSuplementar", Arrays.asList(
                GrupoMovimentoEstoque.REPARTE_COTA_AUSENTE.name(),
                GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_SUPLEMENTAR.name()));
        
        if (paginacao != null) {
            query.setFirstResult(paginacao.getPosicaoInicial());
            query.setMaxResults(paginacao.getQtdResultadosPorPagina());
        }
		
        query.setResultTransformer(Transformers.aliasToBean(SuplementarFecharDiaDTO.class));
        
        List<SuplementarFecharDiaDTO> lista = query.list();

		return lista;
	}

	@Override
	public Long contarProdutoEdicaoSuplementar() {

	    StringBuilder hql = new StringBuilder("select count(distinct produtoEdicao) ");
	    hql.append("from EstoqueProduto as estoqueProduto ");
        hql.append("join estoqueProduto.produtoEdicao produtoEdicao ");
        hql.append("where estoqueProduto.qtdeSuplementar is not null and estoqueProduto.qtdeSuplementar > 0 ");
	   
        Query query = getSession().createQuery(hql.toString());
                
        return (Long) query.uniqueResult();
	}

	@Override
	public Long contarVendasSuplementar(Date dataOperacao) {
		StringBuilder hql = new StringBuilder("select count(vendaEncalhe) from VendaProduto vendaEncalhe ");
        hql.append("where vendaEncalhe.dataOperacao = :data and vendaEncalhe.tipoVenda = :tipoVendaEncalhe ");
        
        Query query = getSession().createQuery(hql.toString());
        query.setParameter("data", dataOperacao);
        query.setParameter("tipoVendaEncalhe", TipoVendaEncalhe.SUPLEMENTAR); 
        
        return (Long) query.uniqueResult();
	}

	@Override
	public BigDecimal obterValorInventario(Date dataOperacao) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT ");
		hql.append(" SUM(  ");
		hql.append(" CASE WHEN tm.operacaoEstoque = :entrada ");
		hql.append(" THEN (COALESCE( me.qtde, 0) * pe.precoVenda) ");
		hql.append(" ELSE (COALESCE(-me.qtde, 0) * pe.precoVenda) END) ");       
		hql.append(" FROM MovimentoEstoque as me ");
		hql.append(" JOIN me.tipoMovimento as tm ");       
		hql.append(" JOIN me.produtoEdicao as pe ");
		hql.append(" JOIN me.lancamentoDiferenca as ld ");
		hql.append(" JOIN ld.diferenca as d ");
		hql.append(" WHERE me.data = :dataOperacao ");
		hql.append(" AND me.origem = :origemInventario ");
		hql.append(" AND d.tipoEstoque = :tipoEstoque ");
		hql.append(" GROUP BY me.data");
		
		Query query = super.getSession().createQuery(hql.toString());
		query.setParameter("dataOperacao", dataOperacao);
	
		query.setParameter("origemInventario", Origem.INVENTARIO);  
        query.setParameter("entrada", OperacaoEstoque.ENTRADA);  
        query.setParameter("tipoEstoque", TipoEstoque.SUPLEMENTAR);
        
		BigDecimal total =  (BigDecimal) query.uniqueResult();
		
		return total != null ? total : BigDecimal.ZERO;
	}

}
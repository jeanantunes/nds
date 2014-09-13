package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.EncalheFecharDiaDTO;
import br.com.abril.nds.dto.ResumoEncalheFecharDiaDTO;
import br.com.abril.nds.dto.VendaFechamentoDiaDTO;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.FormaComercializacao;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.estoque.TipoVendaEncalhe;
import br.com.abril.nds.model.integracao.StatusIntegracao;
import br.com.abril.nds.model.movimentacao.StatusOperacao;
import br.com.abril.nds.repository.AbstractRepository;
import br.com.abril.nds.repository.ResumoEncalheFecharDiaRepository;
import br.com.abril.nds.vo.PaginacaoVO;

@Repository
public class ResumoEncalheFecharDiaRepositoryImpl extends AbstractRepository implements
		ResumoEncalheFecharDiaRepository {
	
	@SuppressWarnings("unchecked")
	@Override
	public List<EncalheFecharDiaDTO> obterDadosGridEncalhe(Date data, PaginacaoVO paginacao) {
	    Objects.requireNonNull(data, "Data para consulta dos produtos conferidos no encalhe não deve ser nula!");
	      
        StringBuilder hql = new StringBuilder();
        
        hql.append(" select  ")
	        .append(" produto.CODIGO as codigo,")
	        .append(" produtoEdicao.ID as idProdutoEdicao,")
	        .append(" produto.NOME as nomeProduto,")
	        .append(" produtoEdicao.NUMERO_EDICAO as numeroEdicao,")
	        .append(" produtoEdicao.PRECO_VENDA as precoVenda,")
	        .append(" sum(conferenciaEncalhe.QTDE) as qtdeLogico,")
	        
	         //QTDE ENCALHE LÓGICO JURAMENTADO
	        .append(" (select coalesce(sum(conferenciaEncalheJuramentado.QTDE),0)")
	        .append(" from")
	        .append(" CONFERENCIA_ENCALHE conferenciaEncalheJuramentado,")
	        .append(" PRODUTO_EDICAO produtoEdicaoJuramentado,")
	        .append(" CONTROLE_CONFERENCIA_ENCALHE_COTA controleConferenciaEnalheJuramentado")
	        .append(" where")
	        .append(" conferenciaEncalheJuramentado.PRODUTO_EDICAO_ID=produtoEdicaoJuramentado.ID")
	        .append(" and conferenciaEncalheJuramentado.CONTROLE_CONFERENCIA_ENCALHE_COTA_ID=controleConferenciaEnalheJuramentado.ID")
	        .append(" and conferenciaEncalheJuramentado.PRODUTO_EDICAO_ID=produtoEdicao.ID")
	        .append(" and controleConferenciaEnalheJuramentado.STATUS= :statusOperacao ")
	        .append(" and conferenciaEncalheJuramentado.DATA= :data ")
	        .append(" and conferenciaEncalheJuramentado.JURAMENTADA=1) as qtdeLogicoJuramentado,")
	        
	         //QTDE ENCALHE FÍSICO
	        .append(" (coalesce((select fechamentoEncalhe.QUANTIDADE ") 
	        .append(" from fechamento_encalhe fechamentoEncalhe ")
	        .append(" where fechamentoEncalhe.DATA_ENCALHE = :data ")
	        .append(" and fechamentoEncalhe.PRODUTO_EDICAO_ID = produtoEdicao.ID), ")
	        .append(" (select fechamentoEncalheBox.QUANTIDADE ")
	        .append(" from fechamento_encalhe_box fechamentoEncalheBox ")
	        .append(" where fechamentoEncalheBox.DATA_ENCALHE = :data ")
	        .append(" and fechamentoEncalheBox.PRODUTO_EDICAO_ID = produtoEdicao.ID),0) ) as qtdeFisico," )
	        
	          //QTDE VENDA ENCALHE
	        .append(" (select coalesce(sum(vendaProdutoEncalhe.QNT_PRODUTO),0) ")
	        .append(" from VENDA_PRODUTO vendaProdutoEncalhe, ")
	        .append(" PRODUTO_EDICAO produtoEdicaovendaProdutoEncalhe ")
	        .append(" where ")
	        .append(" vendaProdutoEncalhe.ID_PRODUTO_EDICAO=produtoEdicaovendaProdutoEncalhe.ID ")
	        .append(" and vendaProdutoEncalhe.ID_PRODUTO_EDICAO=produtoEdicao.ID ")
	        .append(" and vendaProdutoEncalhe.DATA_OPERACAO= :data ")
	        .append(" and vendaProdutoEncalhe.TIPO_VENDA_ENCALHE= :tipoVendaEncalhe ")
	        .append(" and vendaProdutoEncalhe.TIPO_COMERCIALIZACAO_VENDA= :tipoComercializacaoVista) as qtdeVendaEncalhe ")
	        
	        .append(" from ")
	        .append(" CONFERENCIA_ENCALHE conferenciaEncalhe ")
	        .append(" inner join PRODUTO_EDICAO produtoEdicao ")
	        .append(" on conferenciaEncalhe.PRODUTO_EDICAO_ID=produtoEdicao.ID ")
	        .append(" inner join PRODUTO produto ")
	        .append(" on produtoEdicao.PRODUTO_ID=produto.ID ")
	        .append(" join CONTROLE_CONFERENCIA_ENCALHE_COTA controleConferenciaEnalhe ")
	        .append(" where ")
	        .append(" conferenciaEncalhe.CONTROLE_CONFERENCIA_ENCALHE_COTA_ID=controleConferenciaEnalhe.ID ")
	        .append(" and controleConferenciaEnalhe.DATA_OPERACAO= :data ")
	        .append(" and controleConferenciaEnalhe.STATUS= :statusOperacao ")
	        .append(" and conferenciaEncalhe.QTDE > 0 ")
	        .append(" group by produtoEdicao.ID  ")
	        .append(" order by codigo asc ");
         
        Query query = getSession().createSQLQuery(hql.toString())
        		.addScalar("idProdutoEdicao", StandardBasicTypes.LONG)
        		.addScalar("codigo", StandardBasicTypes.STRING)
        		.addScalar("nomeProduto", StandardBasicTypes.STRING)
        		.addScalar("numeroEdicao", StandardBasicTypes.LONG)
        		.addScalar("precoVenda", StandardBasicTypes.BIG_DECIMAL)
        		.addScalar("qtdeLogico", StandardBasicTypes.BIG_INTEGER)
        		.addScalar("qtdeLogicoJuramentado", StandardBasicTypes.BIG_INTEGER)
        		.addScalar("qtdeFisico", StandardBasicTypes.BIG_INTEGER)
        		.addScalar("qtdeVendaEncalhe", StandardBasicTypes.BIG_INTEGER);
        
        query.setParameter("statusOperacao", StatusOperacao.CONCLUIDO.name());
        query.setParameter("data", data);
        query.setParameter("tipoVendaEncalhe", TipoVendaEncalhe.ENCALHE.name());
        query.setParameter("tipoComercializacaoVista", FormaComercializacao.CONTA_FIRME.name());
        
        if (paginacao != null) {
            query.setFirstResult(paginacao.getPosicaoInicial());
            query.setMaxResults(paginacao.getQtdResultadosPorPagina());
        }
        
        query.setResultTransformer(new AliasToBeanResultTransformer(EncalheFecharDiaDTO.class));
        
        List<EncalheFecharDiaDTO> lista = query.list();
       
        for(EncalheFecharDiaDTO item : lista){
        	
        	 BigInteger qtdeFisicoLogicoJuramentado = item.getQtdeFisico().add(item.getQtdeLogicoJuramentado());
        	 
             //Diferenca = (Físico + Lógico Juramentado) - (Lógico - Venda de Encalhe);
             BigInteger qtdeDiferenca = qtdeFisicoLogicoJuramentado.subtract((item.getQtdeLogico()).subtract(item.getQtdeVendaEncalhe()));
             
             item.setQtdeDiferenca(qtdeDiferenca);
        }
       
        return lista;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long contarProdutoEdicaoEncalhe(Date data) {
	    Objects.requireNonNull(data, "Data para contagem dos produtos conferidos no encalhe não deve ser nula!");

	    StringBuilder hql = new StringBuilder("select count(distinct produtoEdicao) ");
	    hql.append(" from ConferenciaEncalhe conferenciaEncalhe ");
        hql.append(" join conferenciaEncalhe.produtoEdicao produtoEdicao ");
        hql.append(" where conferenciaEncalhe.controleConferenciaEncalheCota.dataOperacao = :data and ");
        hql.append(" conferenciaEncalhe.controleConferenciaEncalheCota.status = :statusOperacao ");
        hql.append(" and conferenciaEncalhe.qtde > 0 ");
        
        Query query = getSession().createQuery(hql.toString());
        
        query.setParameter("statusOperacao", StatusOperacao.CONCLUIDO);
        query.setParameter("data", data);
                
        return (Long) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VendaFechamentoDiaDTO> obterDadosVendaEncalhe(Date dataOperacao, PaginacaoVO paginacao) {
		StringBuilder hql = new StringBuilder();
		
		StringBuffer hqlProdutoEdicaoConferenciaEncalhe = new StringBuffer()
		.append(" ( ")
		.append(" select distinct pe.id from ControleConferenciaEncalheCota ccec ")
		.append(" inner join ccec.conferenciasEncalhe conferenciaEncalhe	")
		.append(" inner join conferenciaEncalhe.produtoEdicao pe ")
		.append(" where ccec.dataOperacao = :dataOperacao and  ")
		.append(" ccec.status = :statusOperacao  ")
		.append(" ) ");
		
		
		hql.append(" SELECT pe.id as idProdutoEdicao , p.codigo as codigo,  ");
		hql.append(" p.nome as nomeProduto, ");
		hql.append(" pe.numeroEdicao as numeroEdicao, ");
		hql.append(" ve.qntProduto as qtde, ");
		hql.append(" (ve.qntProduto * pe.precoVenda) as valor, ");
		hql.append(" ve.dataVenda as dataRecolhimento ");
		
		hql.append(" FROM VendaProduto as ve ");		 
		hql.append(" JOIN ve.produtoEdicao as pe ");					
		hql.append(" JOIN pe.produto as p ");					
		hql.append(" WHERE ve.dataOperacao = :dataOperacao ");
		hql.append(" AND ve.tipoComercializacaoVenda = :tipoComercializacaoVenda ");
		
		hql.append(" AND ve.tipoVenda = :encalhe");

		hql.append(" AND pe.id in ").append(hqlProdutoEdicaoConferenciaEncalhe.toString());
		
		
		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("dataOperacao", dataOperacao);
		
		query.setParameter("statusOperacao", StatusOperacao.CONCLUIDO);
		
		query.setParameter("encalhe", TipoVendaEncalhe.ENCALHE);
		
		query.setParameter("tipoComercializacaoVenda", FormaComercializacao.CONTA_FIRME);	
		
		if (paginacao != null) {
		    query.setFirstResult(paginacao.getPosicaoInicial());
	        query.setMaxResults(paginacao.getQtdResultadosPorPagina());
	    }
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				VendaFechamentoDiaDTO.class));
		
		return query.list();
	}

    /**
     * {@inheritDoc}
     */
	@Override
    public ResumoEncalheFecharDiaDTO obterResumoEncalhe(Date data) {
	   
	    Objects.requireNonNull(data, "Data para resumo de encalhe não deve ser nula!");
       
        String tempalteHqlProdutosEdicaoEncalhe = new StringBuilder()
	        .append(" select ")
	        .append("	distinct conferenciEncalheProduto.PRODUTO_EDICAO_ID ")
	        .append(" from ")
	        .append("	CONFERENCIA_ENCALHE conferenciEncalheProduto, ")
	        .append("	CONTROLE_CONFERENCIA_ENCALHE_COTA controleConferenciaEncalheProduto ")
	        .append(" where ")
	        .append(" conferenciEncalheProduto.CONTROLE_CONFERENCIA_ENCALHE_COTA_ID=controleConferenciaEncalheProduto.ID ")
	        .append(" and controleConferenciaEncalheProduto.DATA_OPERACAO= :data ")
	        .append(" and controleConferenciaEncalheProduto.STATUS= :statusOperacao ").toString();
	        
        String templateHqlDiferencas = new StringBuilder()
        	.append("(select coalesce( sum(diferenca.QTDE * produtoEdicaoDiferenca.PRECO_VENDA),0) ")
			.append("   from DIFERENCA diferenca ")
			.append("   inner join LANCAMENTO_DIFERENCA lancamentoDiferenca ")
			.append("           on diferenca.LANCAMENTO_DIFERENCA_ID=lancamentoDiferenca.ID ")
			.append("   inner join MOVIMENTO_ESTOQUE movimentoEstoqueDiferenca ")
			.append("           on lancamentoDiferenca.MOVIMENTO_ESTOQUE_ID=movimentoEstoqueDiferenca.ID, ")
			.append("       PRODUTO_EDICAO produtoEdicaoDiferenca ")
			.append("   where ")
			.append("       diferenca.PRODUTO_EDICAO_ID=produtoEdicaoDiferenca.ID ")
			.append("       and diferenca.DATA_MOVIMENTACAO= :data ")
			.append("       and diferenca.TIPO_DIFERENCA in (:%s) ")
			.append("       and movimentoEstoqueDiferenca.STATUS= :movimentoAprovado ")
			.append("       and movimentoEstoqueDiferenca.STATUS_INTEGRACAO =:statusIntegracaoRecolhimento ")
			.append("       and lancamentoDiferenca.STATUS in (:statusPerdaGanho) ")
			.append("		 and diferenca.PRODUTO_EDICAO_ID in ( ").append(tempalteHqlProdutosEdicaoEncalhe).append(")")
			.append("  ) as %s ").toString();
					        
        StringBuilder hql = new StringBuilder()
        	.append("  select coalesce( sum(conferenciaEncalhe.QTDE * produtoEdicao.PRECO_VENDA),0) as totalLogico , ")
        
	        .append("coalesce(   ")  
	        .append("   (SELECT sum(fechamentoEncalhe.QUANTIDADE * produtoEdicaoFechamento.PRECO_VENDA) ")
	        .append("		FROM fechamento_encalhe fechamentoEncalhe ")
	        .append("		JOIN produto_edicao produtoEdicaoFechamento ON produtoEdicaoFechamento.ID = fechamentoEncalhe.PRODUTO_EDICAO_ID ")
	        .append("		WHERE fechamentoEncalhe.DATA_ENCALHE = :data ), ")
	        .append("   (SELECT sum(fechamentoEncalheBox.QUANTIDADE * produtoEdicaoFechamentoBox.PRECO_VENDA) ")
	        .append("		FROM fechamento_encalhe_box fechamentoEncalheBox ")
	        .append("		JOIN produto_edicao produtoEdicaoFechamentoBox ON produtoEdicaoFechamentoBox.ID = fechamentoEncalheBox.PRODUTO_EDICAO_ID ")
	        .append("		WHERE fechamentoEncalheBox.DATA_ENCALHE = :data ),0) as totalFisico," )  
	         
	        .append(" (select coalesce(sum(conferenciaEncalheJuramentado.QTDE*produtoEdicaoJuramentado.PRECO_VENDA),0) ")
	        .append("  from CONFERENCIA_ENCALHE conferenciaEncalheJuramentado, ")
	        .append("    PRODUTO_EDICAO produtoEdicaoJuramentado, ")
	        .append("    CONTROLE_CONFERENCIA_ENCALHE_COTA controleConferenciaEnalheJuramentado ")
	        .append("  where ")
	        .append("    conferenciaEncalheJuramentado.PRODUTO_EDICAO_ID=produtoEdicaoJuramentado.ID ")
	        .append("    and conferenciaEncalheJuramentado.CONTROLE_CONFERENCIA_ENCALHE_COTA_ID=controleConferenciaEnalheJuramentado.ID ")
	        .append("    and conferenciaEncalheJuramentado.JURAMENTADA=1 ")
	        .append("    and controleConferenciaEnalheJuramentado.STATUS= :statusOperacao ")
	        .append("    and conferenciaEncalheJuramentado.DATA= :data ) as totalJuramentado, ")
	        
	        .append("(select coalesce(sum(vendaProdutoEncalhe.QNT_PRODUTO*produtoEdicaovendaProdutoEncalhe.PRECO_VENDA), 0) ")
	        .append(" from ")
	        .append("   VENDA_PRODUTO vendaProdutoEncalhe, ")
	        .append("   PRODUTO_EDICAO produtoEdicaovendaProdutoEncalhe ")
	        .append(" where ")
	        .append("   vendaProdutoEncalhe.ID_PRODUTO_EDICAO=produtoEdicaovendaProdutoEncalhe.ID ")
	        .append("   and vendaProdutoEncalhe.ID_PRODUTO_EDICAO in ( ").append(tempalteHqlProdutosEdicaoEncalhe).append(" ) ")
			.append("   and vendaProdutoEncalhe.DATA_OPERACAO= :data ")
			.append("   and vendaProdutoEncalhe.TIPO_VENDA_ENCALHE= :tipoVendaEncalhe ")
			.append("   and vendaProdutoEncalhe.TIPO_COMERCIALIZACAO_VENDA= :tipoComercializacaoVista ")
			.append(") as venda,  ")
			
			//TOTAL SOBRAS
	        .append(String.format(templateHqlDiferencas, "tipoDiferencaSobras", "totalSobras")).append(",")
	        
	        //TOTAL FALTAS
	        .append(String.format(templateHqlDiferencas, "tipoDiferencaFaltas", "totalFaltas"))
	        
	        .append(" from CONFERENCIA_ENCALHE conferenciaEncalhe ")
	        .append(" inner join PRODUTO_EDICAO produtoEdicao ")
	        .append(" 	on conferenciaEncalhe.PRODUTO_EDICAO_ID=produtoEdicao.ID ")
	        .append(" inner join  PRODUTO produto ")
	        .append(" 	on produtoEdicao.PRODUTO_ID=produto.ID ")
	        .append(" join CONTROLE_CONFERENCIA_ENCALHE_COTA controleConferenciaEnalhe ")
	        .append(" where ")
	        .append("  conferenciaEncalhe.CONTROLE_CONFERENCIA_ENCALHE_COTA_ID=controleConferenciaEnalhe.ID ")
	        .append("  and controleConferenciaEnalhe.DATA_OPERACAO= :data ")
	        .append("  and controleConferenciaEnalhe.STATUS= :statusOperacao ");
	        
        Query query = getSession().createSQLQuery(hql.toString())
							        .addScalar("totalLogico", StandardBasicTypes.BIG_DECIMAL)
							        .addScalar("totalFisico", StandardBasicTypes.BIG_DECIMAL)
							        .addScalar("totalJuramentado", StandardBasicTypes.BIG_DECIMAL)
							        .addScalar("venda", StandardBasicTypes.BIG_DECIMAL)
							        .addScalar("totalSobras", StandardBasicTypes.BIG_DECIMAL)
							        .addScalar("totalFaltas", StandardBasicTypes.BIG_DECIMAL);
	       
        query.setParameter("data", data);
        query.setParameter("movimentoAprovado", StatusAprovacao.APROVADO.name());
        query.setParameter("statusIntegracaoRecolhimento", StatusIntegracao.ENCALHE.name());
        query.setParameter("statusOperacao", StatusOperacao.CONCLUIDO.name());
        query.setParameter("tipoVendaEncalhe", TipoVendaEncalhe.ENCALHE.name());
        query.setParameter("tipoComercializacaoVista", FormaComercializacao.CONTA_FIRME.name());
        
        query.setParameterList("tipoDiferencaSobras", Arrays.asList(TipoDiferenca.SOBRA_DE.name(),
        															TipoDiferenca.SOBRA_DE_DIRECIONADA_COTA.name(),
        															TipoDiferenca.SOBRA_EM.name(),
        															TipoDiferenca.SOBRA_EM_DIRECIONADA_COTA.name(),
        															TipoDiferenca.GANHO_DE.name(), 
        															TipoDiferenca.GANHO_EM.name()));
        
        query.setParameterList("tipoDiferencaFaltas", Arrays.asList(TipoDiferenca.FALTA_EM_DIRECIONADA_COTA.name(),
        															TipoDiferenca.FALTA_DE.name(), 
        															TipoDiferenca.FALTA_EM.name(), 
        															TipoDiferenca.PERDA_DE.name(), 
        															TipoDiferenca.PERDA_EM.name()));
        
        query.setParameterList("statusPerdaGanho", Arrays.asList(StatusAprovacao.GANHO.name(), StatusAprovacao.PERDA.name()));
        
        query.setResultTransformer(new AliasToBeanResultTransformer(ResumoEncalheFecharDiaDTO.class));
        
        ResumoEncalheFecharDiaDTO resultado  = (ResumoEncalheFecharDiaDTO) query.uniqueResult();
  		
        BigDecimal faltaSobras = resultado.getTotalSobras().subtract(resultado.getTotalFaltas());
          
        // FISICO + sobras - faltas
        BigDecimal valorFisico = resultado.getTotalFisico().subtract(resultado.getVenda());
  		
        //Saldo = Lógico - (Físico - Venda de Encalhe + Sobras - Faltas ;
        BigDecimal saldo = resultado.getTotalLogico().subtract(valorFisico).add(faltaSobras).subtract(resultado.getVenda());
      
  		resultado.setTotalFisico(valorFisico);
  		resultado.setSaldo(saldo);
  			
        return resultado;
    }

    @Override
    public Long contarVendasEncalhe(Date data) {
        Objects.requireNonNull(data, "Data para contagem das vendas de encalhe não deve ser nula!");
        
        
        StringBuffer hqlProdutoEdicaoConferenciaEncalhe = new StringBuffer()
		.append(" ( ")
		.append(" select distinct pe.id from ControleConferenciaEncalheCota ccec ")
		.append(" inner join ccec.conferenciasEncalhe conferenciaEncalhe ")
		.append(" inner join conferenciaEncalhe.produtoEdicao pe ")
		.append(" where ccec.dataOperacao = :data and  ")
		.append(" ccec.status = :statusOperacao  ")
		.append(" ) ");
        
        
        StringBuilder hql = new StringBuilder("select count(vendaEncalhe) from VendaProduto vendaEncalhe ");
        
        hql.append(" where vendaEncalhe.dataOperacao = :data and vendaEncalhe.tipoVenda = :tipoVendaEncalhe ");
        
        hql.append(" and vendaEncalhe.tipoComercializacaoVenda = :tipoComercializacaoVista");
        
        hql.append(" and vendaEncalhe.produtoEdicao.id in ").append(hqlProdutoEdicaoConferenciaEncalhe.toString());
        
        
        Query query = getSession().createQuery(hql.toString());
        
        query.setParameter("data", data);
        query.setParameter("statusOperacao", StatusOperacao.CONCLUIDO);
        query.setParameter("tipoVendaEncalhe", TipoVendaEncalhe.ENCALHE);
        query.setParameter("tipoComercializacaoVista", FormaComercializacao.CONTA_FIRME);        
        
        return (Long) query.uniqueResult();
    }

}

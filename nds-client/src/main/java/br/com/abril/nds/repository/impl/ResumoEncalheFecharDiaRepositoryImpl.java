package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.EncalheFecharDiaDTO;
import br.com.abril.nds.dto.VendaFechamentoDiaDTO;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.FormaComercializacao;
import br.com.abril.nds.model.estoque.TipoVendaEncalhe;
import br.com.abril.nds.model.planejamento.TipoChamadaEncalhe;
import br.com.abril.nds.repository.ResumoEncalheFecharDiaRepository;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.PaginacaoVO;

@Repository
public class ResumoEncalheFecharDiaRepositoryImpl extends AbstractRepository implements
		ResumoEncalheFecharDiaRepository {
    
	@Override
	public BigDecimal obterValorEncalheFisico(Date dataOperacaoDistribuidor, boolean juramentada) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT (ce.qtde * pe.precoVenda) ");			
		hql.append(" from ConferenciaEncalhe AS ce ");		
		hql.append(" JOIN ce.produtoEdicao as pe ");		
		hql.append(" WHERE ce.data = :dataOperacaoDistribuidor ");
		
		if(juramentada){
			hql.append(" AND ce.juramentada = :juramentada ");
		}
		
		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("dataOperacaoDistribuidor", dataOperacaoDistribuidor);
		
		if(juramentada){
			query.setParameter("juramentada", juramentada);
		}
		
		BigDecimal total =  (BigDecimal) query.uniqueResult();
		
		return total != null ? total : BigDecimal.ZERO ;
	}

	@Override
	public BigDecimal obterValorEncalheLogico(Date dataOperacaoDistribuidor) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT (cec.qtdePrevista * pe.precoVenda) ");			
		hql.append(" from ChamadaEncalheCota AS cec ");		
		hql.append(" JOIN cec.chamadaEncalhe AS ce ");		
		hql.append(" JOIN ce.produtoEdicao as pe ");		
		hql.append(" WHERE ce.dataRecolhimento = :dataOperacaoDistribuidor ");
		hql.append(" AND ce.tipoChamadaEncalhe = :tipoChamadaEncalhe ");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("dataOperacaoDistribuidor", dataOperacaoDistribuidor);		
		query.setParameter("tipoChamadaEncalhe", TipoChamadaEncalhe.MATRIZ_RECOLHIMENTO);		
		
		BigDecimal total =  (BigDecimal) query.uniqueResult();
		
		return total != null ? total : BigDecimal.ZERO ;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<EncalheFecharDiaDTO> obterDadosGridEncalhe(Date data, PaginacaoVO paginacao) {
	    StringBuilder hql = new StringBuilder("select new map(produto.codigo as codigo, ");
        hql.append("produto.nome as nomeProduto, ");
        hql.append("produtoEdicao.numeroEdicao as numeroEdicao, ");
        hql.append("produtoEdicao.precoVenda as precoVenda, ");
        
        //QTDE ENCALHE LÓGICO
        hql.append("sum(conferenciaEncalhe.qtde) as qtdeLogico, ");
        
        //QTDE ENCALHE LÓGICO JURAMENTADO
        hql.append("(select coalesce(sum(conferenciaEncalheJuramentada.qtde), 0) from ConferenciaEncalhe conferenciaEncalheJuramentada ");
        hql.append("where conferenciaEncalheJuramentada.produtoEdicao = produtoEdicao and ");
        hql.append("conferenciaEncalheJuramentada.data = :data and conferenciaEncalheJuramentada.juramentada is true) as qtdeLogicoJuramentado, ");
        
        //QTDE ENCALHE FÍSICO
        hql.append("(select cast(fechamentoEncalhe.quantidade as big_integer) from FechamentoEncalhe fechamentoEncalhe ");
        hql.append("where fechamentoEncalhe.fechamentoEncalhePK.produtoEdicao = produtoEdicao and ");
        hql.append("fechamentoEncalhe.fechamentoEncalhePK.dataEncalhe = :data) as qtdeFisico, ");
        
        //QTDE VENDA ENCALHE
        hql.append("(select coalesce(sum(vendaEncalhe.qntProduto), 0) from VendaProduto vendaEncalhe ");
        hql.append("where vendaEncalhe.produtoEdicao = produtoEdicao and ");
        hql.append("vendaEncalhe.dataVenda = :data and vendaEncalhe.tipoVenda = :tipoVendaEncalhe ");
        hql.append("and vendaEncalhe.tipoComercializacaoVenda = :tipoComercializacaoVista) as qtdeVendaEncalhe)");
        
        hql.append("from ConferenciaEncalhe conferenciaEncalhe ");
        hql.append("join conferenciaEncalhe.produtoEdicao produtoEdicao ");
        hql.append("join produtoEdicao.produto produto ");
        hql.append("where conferenciaEncalhe.data = :data ");
        hql.append("group by produtoEdicao ");
        hql.append("order by codigo asc");
        
        Query query = getSession().createQuery(hql.toString());
        query.setParameter("data", data);
        query.setParameter("tipoVendaEncalhe", TipoVendaEncalhe.ENCALHE);
        query.setParameter("tipoComercializacaoVista", FormaComercializacao.CONTA_FIRME);
        
        if (paginacao != null) {
            query.setFirstResult(paginacao.getPosicaoInicial());
            query.setMaxResults(paginacao.getQtdResultadosPorPagina());
        }
        
        List<Map<String, Object>> maps = query.list();
       
        List<EncalheFecharDiaDTO> lista = new ArrayList<>(maps.size());
        
        for (Map<String, Object> map : maps) {
            String codigo = (String) map.get("codigo");
            String nomeProduto = (String) map.get("nomeProduto");
            Long numeroEdicao = (Long) map.get("numeroEdicao");
            BigDecimal precoVenda = (BigDecimal) map.get("precoVenda");
            BigInteger qtdeLogico = (BigInteger) map.get("qtdeLogico");
            BigInteger qtdeLogicoJuramentado = (BigInteger) map.get("qtdeLogicoJuramentado");
            BigInteger qtdeFisico = Util.nvl((BigInteger) map.get("qtdeFisico"), BigInteger.ZERO);
            BigInteger qtdeVendaEncalhe = (BigInteger) map.get("qtdeVendaEncalhe");
            
            BigInteger qtdeFisicoLogicoJuramentado = qtdeFisico.add(qtdeLogicoJuramentado);
            //Diferenca = Lógico - (Físico + Lógico Juramentado) - Venda de Encalhe;
            BigInteger qtdeDiferenca = qtdeLogico.subtract(qtdeFisicoLogicoJuramentado).subtract(qtdeVendaEncalhe);
            
            EncalheFecharDiaDTO encalheDTO = new EncalheFecharDiaDTO(codigo, nomeProduto, numeroEdicao, precoVenda, qtdeLogico,
                    qtdeLogicoJuramentado, qtdeFisico, qtdeVendaEncalhe, qtdeDiferenca);
            
            lista.add(encalheDTO);
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
	    hql.append("from ConferenciaEncalhe conferenciaEncalhe ");
        hql.append("join conferenciaEncalhe.produtoEdicao produtoEdicao ");
        hql.append("where conferenciaEncalhe.data = :data ");
	   
        Query query = getSession().createQuery(hql.toString());
        query.setParameter("data", data);
                
        return (Long) query.uniqueResult();
	}
	
	@Override
	public BigDecimal obterValorFaltasOuSobras(Date dataOperacao, StatusAprovacao status) {

		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT (me.qtde * pe.precoVenda) ");			
		hql.append(" FROM LancamentoDiferenca AS ld ");		
		hql.append(" JOIN ld.movimentoEstoque as me ");	
		hql.append(" JOIN me.produtoEdicao as pe ");	
		hql.append(" WHERE me.data = :dataOperacaoDistribuidor ");
		hql.append(" AND ld.status = :status ");			
		
		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("dataOperacaoDistribuidor", dataOperacao);
		
		if(status.equals(StatusAprovacao.PERDA)){
			query.setParameter("status", StatusAprovacao.PERDA);			
		}else{
			query.setParameter("status", StatusAprovacao.GANHO);
		}
		
		BigDecimal total =  (BigDecimal) query.uniqueResult();
		
		return total != null ? total : BigDecimal.ZERO ;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VendaFechamentoDiaDTO> obterDadosVendaEncalhe(Date dataOperacao) {
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT p.codigo as codigo,  ");
		hql.append(" p.nome as nomeProduto, ");
		hql.append(" pe.numeroEdicao as numeroEdicao, ");
		hql.append(" ve.qntProduto as qtde, ");
		hql.append(" (ve.qntProduto * pe.precoVenda) as valor, ");
		hql.append(" ve.dataVenda as dataRecolhimento ");
		
		hql.append(" FROM VendaProduto as ve ");		 
		hql.append(" JOIN ve.produtoEdicao as pe ");					
		hql.append(" JOIN pe.produto as p ");					
		hql.append(" WHERE ve.dataVenda = :dataOperacao ");
		hql.append(" AND ve.tipoComercializacaoVenda = :tipoComercializacaoVenda ");
		
		hql.append(" AND ve.tipoVenda = :suplementar");

		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("dataOperacao", dataOperacao);
		
		query.setParameter("suplementar", TipoVendaEncalhe.ENCALHE);
		
		query.setParameter("tipoComercializacaoVenda", FormaComercializacao.CONTA_FIRME);		
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				VendaFechamentoDiaDTO.class));
		
		return query.list();
	}

	@Override
	public BigDecimal obterValorVendaEncalhe(Date dataOperacao) {
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT   ");
		hql.append(" SUM(ve.qntProduto * pe.precoVenda)");
		hql.append(" FROM VendaProduto as ve ");		 
		hql.append(" JOIN ve.produtoEdicao as pe ");					
		hql.append(" JOIN pe.produto as p ");					
		hql.append(" WHERE ve.dataVenda = :dataOperacao ");
		hql.append(" AND ve.tipoComercializacaoVenda = :tipoComercializacaoVenda ");		
		hql.append(" AND ve.tipoVenda = :suplementar");

		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("dataOperacao", dataOperacao);
		
		query.setParameter("suplementar", TipoVendaEncalhe.ENCALHE);
		
		query.setParameter("tipoComercializacaoVenda", FormaComercializacao.CONTA_FIRME);	
		
		BigDecimal total =  (BigDecimal) query.uniqueResult();
		
		return total != null ? total : BigDecimal.ZERO ;
	}

}

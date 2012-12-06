package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.EncalheFecharDiaDTO;
import br.com.abril.nds.dto.ResumoEncalheFecharDiaDTO;
import br.com.abril.nds.dto.VendaFechamentoDiaDTO;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.FormaComercializacao;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.estoque.TipoVendaEncalhe;
import br.com.abril.nds.repository.ResumoEncalheFecharDiaRepository;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.PaginacaoVO;

@Repository
public class ResumoEncalheFecharDiaRepositoryImpl extends AbstractRepository implements
		ResumoEncalheFecharDiaRepository {
	
	@SuppressWarnings("unchecked")
	@Override
	public List<EncalheFecharDiaDTO> obterDadosGridEncalhe(Date data, PaginacaoVO paginacao) {
	    Objects.requireNonNull(data, "Data para consulta dos produtos conferidos no encalhe não deve ser nula!");
	    
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

    /**
     * {@inheritDoc}
     */
	@Override
    public ResumoEncalheFecharDiaDTO obterResumoEncalhe(Date data) {
	    Objects.requireNonNull(data, "Data para resumo de encalhe não deve ser nula!");
        
	    String templateHqlProdutoEdicaoEncalhe = new StringBuilder("(select distinct(confEncalhe.produtoEdicao.id) from ConferenciaEncalhe confEncalhe ")
        .append("where confEncalhe.data = :data)").toString();
	    
	    String templateHqlDiferenca =  new StringBuilder("(select sum(case when diferenca.tipoDiferenca = :%s then (diferenca.qtde * diferenca.produtoEdicao.pacotePadrao * diferenca.produtoEdicao.precoVenda) ")
        .append("else (diferenca.qtde * diferenca.produtoEdicao.precoVenda) end) from Diferenca diferenca where diferenca.dataMovimento = :data and diferenca.tipoDiferenca in (:%s, :%s) ")
        .append("and diferenca.lancamentoDiferenca.status in(:statusPerdaGanho) and diferenca.produtoEdicao.id in ").append(templateHqlProdutoEdicaoEncalhe).append(") as %s ").toString();
	    
        //TOTAL ENCALHE LÓGICO
        StringBuilder hql = new StringBuilder("select new map(sum(conferenciaEncalhe.qtde * produtoEdicao.precoVenda) as totalLogico, ");
        
        //TOTAL ENCALHE FÍSICO
        hql.append("(select sum(fechamentoEncalhe.quantidade * fechamentoEncalhe.fechamentoEncalhePK.produtoEdicao.precoVenda) from  ");
        hql.append("FechamentoEncalhe fechamentoEncalhe where fechamentoEncalhe.fechamentoEncalhePK.dataEncalhe = :data) as totalFisico, ");
        
        //TOTAL ENCALHE LÓGICO JURAMENTADO
        hql.append("(select coalesce(sum(conferenciaEncalheJuramentada.qtde * conferenciaEncalheJuramentada.produtoEdicao.precoVenda), 0) ");
        hql.append("from ConferenciaEncalhe conferenciaEncalheJuramentada where conferenciaEncalheJuramentada.juramentada is true and ");
        hql.append("conferenciaEncalheJuramentada.data = :data) as totalJuramentado, ");
        
        //TOTAL VENDA ENCALHE
        hql.append("(select coalesce(sum(vendaEncalhe.qntProduto * vendaEncalhe.produtoEdicao.precoVenda), 0) from VendaProduto vendaEncalhe ");
        hql.append("where vendaEncalhe.produtoEdicao.id in (").append(templateHqlProdutoEdicaoEncalhe).append(") and ");
        hql.append("vendaEncalhe.dataVenda = :data and vendaEncalhe.tipoVenda = :tipoVendaEncalhe ");
        hql.append("and vendaEncalhe.tipoComercializacaoVenda = :tipoComercializacaoVista) as totalVenda, ");
        
        //TOTAL SOBRAS
        hql.append(String.format(templateHqlDiferenca, "tipoDiferencaSobraDe", "tipoDiferencaSobraDe", "tipoDiferencaSobraEm", "totalSobras")).append(",");
        
        //TOTAL FALTAS
        hql.append(String.format(templateHqlDiferenca, "tipoDiferencaFaltaDe", "tipoDiferencaFaltaDe", "tipoDiferencaFaltaEm", "totalFaltas")).append(")");

        hql.append("from ConferenciaEncalhe conferenciaEncalhe ");
        hql.append("join conferenciaEncalhe.produtoEdicao produtoEdicao ");
        hql.append("join produtoEdicao.produto produto ");
        hql.append("where conferenciaEncalhe.data = :data ");
        
        Query query = getSession().createQuery(hql.toString());
        query.setParameter("data", data);
        query.setParameter("tipoVendaEncalhe", TipoVendaEncalhe.ENCALHE);
        query.setParameter("tipoComercializacaoVista", FormaComercializacao.CONTA_FIRME);
        query.setParameter("tipoDiferencaSobraDe", TipoDiferenca.SOBRA_DE);
        query.setParameter("tipoDiferencaSobraEm", TipoDiferenca.SOBRA_EM);
        query.setParameter("tipoDiferencaFaltaDe", TipoDiferenca.FALTA_DE);
        query.setParameter("tipoDiferencaFaltaEm", TipoDiferenca.FALTA_EM);
        query.setParameterList("statusPerdaGanho", Arrays.asList(StatusAprovacao.GANHO, StatusAprovacao.PERDA));
        
        @SuppressWarnings("unchecked")
        Map<String, Object> map = (Map<String, Object>) query.uniqueResult();
  
        BigDecimal totalLogico = Util.nvl((BigDecimal) map.get("totalLogico"), BigDecimal.ZERO);
        BigDecimal totalFisico = Util.nvl((BigDecimal) map.get("totalFisico"), BigDecimal.ZERO);
        BigDecimal totalJuramentado = Util.nvl((BigDecimal) map.get("totalJuramentado"), BigDecimal.ZERO);
        BigDecimal totalVenda = Util.nvl((BigDecimal) map.get("totalVenda"), BigDecimal.ZERO);
        BigDecimal totalSobras = Util.nvl((BigDecimal) map.get("totalSobras"), BigDecimal.ZERO);
        BigDecimal totalFaltas = Util.nvl((BigDecimal) map.get("totalFaltas"), BigDecimal.ZERO);
        
        BigDecimal fisicoJuramentado = totalFisico.add(totalJuramentado);
        BigDecimal faltaSobras = totalSobras.subtract(totalFaltas);
        
        //Saldo = Lógico - (Físico + Lógico Juramentado) - Venda de Encalhe + Sobras - Faltas ;
        BigDecimal saldo = totalLogico.subtract(fisicoJuramentado).subtract(totalVenda).add(faltaSobras);
     
        ResumoEncalheFecharDiaDTO dto = new ResumoEncalheFecharDiaDTO();
        dto.setTotalLogico(totalLogico);
        dto.setTotalFisico(totalFisico);
        dto.setTotalJuramentado(totalJuramentado);
        dto.setVenda(totalVenda);
        dto.setTotalSobras(totalSobras);
        dto.setTotalFaltas(totalFaltas);
        dto.setSaldo(saldo);
	    
        return dto;
    }

}

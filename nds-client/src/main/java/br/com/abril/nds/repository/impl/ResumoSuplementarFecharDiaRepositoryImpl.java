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

import br.com.abril.nds.dto.SuplementarFecharDiaDTO;
import br.com.abril.nds.dto.VendaFechamentoDiaDTO;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.FormaComercializacao;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.TipoVendaEncalhe;
import br.com.abril.nds.repository.ResumoSuplementarFecharDiaRepository;
import br.com.abril.nds.util.Util;

@Repository
public class ResumoSuplementarFecharDiaRepositoryImpl extends AbstractRepository implements
		ResumoSuplementarFecharDiaRepository {

	@Override
	public BigDecimal obterValorEstoqueLogico(Date dataOperacao) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT ");
				hql.append("( ");
				hql.append(" coalesce( (SELECT me2.qtde from MovimentoEstoque as me2 ");       
				hql.append(" JOIN me2.tipoMovimento as tm2 ");       
				hql.append(" WHERE tm2.grupoMovimentoEstoque = :entradaSuplementarEnvioReparte");
				hql.append(" AND me2.data = :dataOperacao), 0)");
			hql.append(" + ");
				hql.append(" coalesce( (SELECT me2.qtde from MovimentoEstoque as me2");       
				hql.append(" JOIN me2.tipoMovimento as tm2 ");       
				hql.append(" WHERE tm2.grupoMovimentoEstoque = :suplementarCotaAusente");
				hql.append(" AND me2.data = :dataOperacao),0) ");
			hql.append(" - ");							
				hql.append(" coalesce( (SELECT me2.qtde from MovimentoEstoque as me2");       
				hql.append(" JOIN me2.tipoMovimento as tm2 ");       
				hql.append(" WHERE tm2.grupoMovimentoEstoque = :reparteCotaAusente");
				hql.append(" AND me2.data = :dataOperacao),0)");
				hql.append(" ) ");
		hql.append("  * pe.precoVenda");
		hql.append(" FROM MovimentoEstoque as me ");
		hql.append(" JOIN me.produtoEdicao as pe ");		
		hql.append(" WHERE me.data = :dataOperacao");
		hql.append(" GROUP BY me.data");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("dataOperacao", dataOperacao);
		
		query.setParameter("entradaSuplementarEnvioReparte", GrupoMovimentoEstoque.ENTRADA_SUPLEMENTAR_ENVIO_REPARTE);
		query.setParameter("suplementarCotaAusente", GrupoMovimentoEstoque.SUPLEMENTAR_COTA_AUSENTE);
		query.setParameter("reparteCotaAusente", GrupoMovimentoEstoque.REPARTE_COTA_AUSENTE);
		
		BigDecimal total =  (BigDecimal) query.uniqueResult();
		
		return total != null ? total : BigDecimal.ZERO ;
	}

	@Override
	public BigDecimal obterValorTransferencia(Date dataOperacao) {
		
		StringBuilder hql = new StringBuilder();
		
			hql.append("SELECT ");
			hql.append("( ");
				hql.append(" coalesce( (SELECT me2.qtde from MovimentoEstoque as me2 ");       
				hql.append(" JOIN me2.tipoMovimento as tm2 ");       
				hql.append(" WHERE tm2.grupoMovimentoEstoque = :transferenciaEntradaSuplementar");
				hql.append(" AND me2.data = :dataOperacao), 0)");
			hql.append(" - ");
				hql.append(" coalesce( (SELECT me2.qtde from MovimentoEstoque as me2");       
				hql.append(" JOIN me2.tipoMovimento as tm2 ");       
				hql.append(" WHERE tm2.grupoMovimentoEstoque = :suplementarCotaAusente");
				hql.append(" AND me2.data = :dataOperacao),0) ");			
			hql.append(" ) ");
			hql.append("  * pe.precoVenda");
			hql.append(" FROM MovimentoEstoque as me ");
			hql.append(" JOIN me.produtoEdicao as pe ");		
			hql.append(" WHERE me.data = :dataOperacao");
			hql.append(" GROUP BY me.data");
		

		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("dataOperacao", dataOperacao);
		
		query.setParameter("transferenciaEntradaSuplementar", GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_SUPLEMENTAR );
		query.setParameter("suplementarCotaAusente", GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_SUPLEMENTAR);
		
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
		hql.append(" WHERE ve.dataVenda = :dataOperacao ");
		hql.append(" AND ve.tipoComercializacaoVenda = :tipoComercializacaoVenda ");
		
		hql.append(" AND ve.tipoVenda = :suplementar");

		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("dataOperacao", dataOperacao);
		
		query.setParameter("suplementar", TipoVendaEncalhe.SUPLEMENTAR);
		
		query.setParameter("tipoComercializacaoVenda", FormaComercializacao.CONTA_FIRME);	
		
		BigDecimal total =  (BigDecimal) query.uniqueResult();
		
		return total != null ? total : BigDecimal.ZERO ;
	}

	@Override
	public BigDecimal obterValorFisico(Date dataOperacao) {
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT SUM(ep.qtdeSuplementar) ");
		hql.append("FROM EstoqueProduto as ep ");
	
		Query query = super.getSession().createQuery(hql.toString());		
		
		BigInteger total =  (BigInteger) query.uniqueResult();
		BigDecimal totalFormatado = new BigDecimal(total);
		
		return totalFormatado != null ? totalFormatado : BigDecimal.ZERO ;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VendaFechamentoDiaDTO> obterVendasSuplementar(Date dataOperacao) {
		
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
		
		query.setParameter("suplementar", TipoVendaEncalhe.SUPLEMENTAR);
		
		query.setParameter("tipoComercializacaoVenda", FormaComercializacao.CONTA_FIRME);		
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				VendaFechamentoDiaDTO.class));
		
		return query.list();
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SuplementarFecharDiaDTO> obterDadosGridSuplementar(Date data) {
	    Objects.requireNonNull(data, "Data para consulta estoque suplementar não deve ser nula!");

	    String templateHqlTransferencia =  new StringBuilder("(select sum(movimentoEstoque.qtde) from MovimentoEstoque movimentoEstoque ")
	    .append("where movimentoEstoque.data = :data and movimentoEstoque.produtoEdicao = produtoEdicao and movimentoEstoque.status = :statusAprovado ")
        .append("and movimentoEstoque.tipoMovimento.grupoMovimentoEstoque in (:%s)) as %s ").toString();
	    
	    StringBuilder hql = new StringBuilder("select new map(produto.codigo as codigo, ");
		hql.append("produto.nome as nomeProduto, ");
		hql.append("produtoEdicao.numeroEdicao as numeroEdicao, ");
		hql.append("produtoEdicao.precoVenda as precoVenda, ");                               
		
		//QTDE CONTÁBIL (ESTOQUE ATUAL)
		hql.append("coalesce(estoqueProduto.qtdeSuplementar, 0) as quantidadeContabil, ");
		
		//QTDE LOGICO (ESTOQUE ANTERIOR)
		hql.append("(select coalesce(historicoEstoque.qtdeSuplementar, 0) from HistoricoEstoqueProduto historicoEstoque ");
		hql.append("where historicoEstoque.produtoEdicao = produtoEdicao and historicoEstoque.data = " );
		hql.append("(select max(historicoAnterior.data) from HistoricoEstoqueProduto historicoAnterior where ");
		hql.append("historicoAnterior.produtoEdicao = historicoEstoque.produtoEdicao)) as quantidadeLogico,");
		
		//QTDE VENDA SUPLEMENTAR
        hql.append("(select coalesce(sum(vendaSuplementar.qntProduto), 0) from VendaProduto vendaSuplementar ");
        hql.append("where vendaSuplementar.produtoEdicao = produtoEdicao and ");
        hql.append("vendaSuplementar.dataVenda = :data and vendaSuplementar.tipoVenda = :tipoVendaSuplementar ");
        hql.append("and vendaSuplementar.tipoComercializacaoVenda = :tipoComercializacaoVista) as quantidadeVenda, ");
        
        //QTDE ENTRADA TRANSFERENCIA
        hql.append(String.format(templateHqlTransferencia, "grupoEntradaSuplementar", "quantidadeTransferenciaEntrada")).append(",");
          
        //QTDE SAIDA TRANSFERENCIA
        hql.append(String.format(templateHqlTransferencia, "grupoSaidaSuplementar", "quantidadeTransferenciaSaida")).append(") ");
 	
		hql.append("from EstoqueProduto as estoqueProduto ");               
		hql.append("join estoqueProduto.produtoEdicao as produtoEdicao ");
		hql.append("join produtoEdicao.produto as produto ");
		hql.append("order by codigo ");
		
		Query query = getSession().createQuery(hql.toString());	
		
		query.setParameter("data", data);
		query.setParameter("tipoVendaSuplementar", TipoVendaEncalhe.SUPLEMENTAR);
        query.setParameter("tipoComercializacaoVista", FormaComercializacao.CONTA_FIRME);   
        query.setParameter("statusAprovado", StatusAprovacao.APROVADO);   
        
        query.setParameterList("grupoEntradaSuplementar", Arrays.asList(
                        GrupoMovimentoEstoque.SUPLEMENTAR_COTA_AUSENTE,
                        GrupoMovimentoEstoque.SUPLEMENTAR_ENVIO_ENCALHE_ANTERIOR_PROGRAMACAO,
                        GrupoMovimentoEstoque.ESTORNO_VENDA_ENCALHE_SUPLEMENTAR,
                        GrupoMovimentoEstoque.ENTRADA_SUPLEMENTAR_ENVIO_REPARTE,
                        GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_SUPLEMENTAR));  
        
        query.setParameterList("grupoSaidaSuplementar", Arrays.asList(
                GrupoMovimentoEstoque.REPARTE_COTA_AUSENTE,
                GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_SUPLEMENTAR));
		
	    List<Map<String, Object>> maps = query.list();
	       
        List<SuplementarFecharDiaDTO> lista = new ArrayList<>(maps.size());
        
        for (Map<String, Object> map : maps) {
            String codigo = (String) map.get("codigo");
            String nomeProduto = (String) map.get("nomeProduto");
            Long numeroEdicao = (Long) map.get("numeroEdicao");
            BigDecimal precoVenda = (BigDecimal) map.get("precoVenda");
            BigInteger quantidadeContabil = Util.nvl((BigInteger) map.get("quantidadeContabil"), BigInteger.ZERO);
            BigInteger quantidadeLogico = Util.nvl((BigInteger) map.get("quantidadeLogico"), BigInteger.ZERO);
            BigInteger quantidadeVenda = Util.nvl((BigInteger) map.get("quantidadeVenda"), BigInteger.ZERO);
            BigInteger quantidadeTransferenciaEntrada = Util.nvl((BigInteger) map.get("quantidadeTransferenciaEntrada"), BigInteger.ZERO);
            BigInteger quantidadeTransferenciaSaida = Util.nvl((BigInteger) map.get("quantidadeTransferenciaSaida"), BigInteger.ZERO);

            SuplementarFecharDiaDTO dto = new SuplementarFecharDiaDTO();
            dto.setCodigo(codigo);
            dto.setNomeProduto(nomeProduto);
            dto.setNumeroEdicao(numeroEdicao);
            dto.setPrecoVenda(precoVenda);
            dto.setQuantidadeContabil(quantidadeContabil);
            dto.setQuantidadeLogico(quantidadeLogico);
            dto.setQuantidadeVenda(quantidadeVenda);
            dto.setQuantidadeTransferenciaEntrada(quantidadeTransferenciaEntrada);
            dto.setQuantidadeTransferenciaSaida(quantidadeTransferenciaSaida);
            lista.add(dto);
        }
		return lista;
	}

}

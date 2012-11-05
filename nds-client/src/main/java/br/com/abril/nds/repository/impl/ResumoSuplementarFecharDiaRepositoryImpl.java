package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.client.vo.VendaSuplementarVO;
import br.com.abril.nds.dto.VendaSuplementarDTO;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.repository.ResumoSuplementarFecharDiaRepository;

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
		
		hql.append("SELECT ");
				hql.append(" ( ");
				hql.append(" coalesce( (SELECT me2.qtde from MovimentoEstoque as me2 ");       
				hql.append(" JOIN me2.tipoMovimento as tm2 ");       
				hql.append(" WHERE tm2.grupoMovimentoEstoque = :vendaEncalheSuplementar");
				hql.append(" AND me2.data = :dataOperacao), 0) ");
			hql.append(" - ");
				hql.append(" coalesce( (SELECT me2.qtde from MovimentoEstoque as me2");       
				hql.append(" JOIN me2.tipoMovimento as tm2 ");       
				hql.append(" WHERE tm2.grupoMovimentoEstoque = :estornoVendaEncalheSuplementar");
				hql.append(" AND me2.data = :dataOperacao),0) ");
			hql.append(" ) * pe.precoVenda ");
		hql.append(" FROM MovimentoEstoque as me ");		 
		hql.append(" JOIN me.produtoEdicao as pe ");		
		hql.append(" WHERE me.data = :dataOperacao");				
		hql.append(" GROUP BY me.data");
	

	Query query = super.getSession().createQuery(hql.toString());
	
	query.setParameter("dataOperacao", dataOperacao);
	
	query.setParameter("vendaEncalheSuplementar", GrupoMovimentoEstoque.VENDA_ENCALHE_SUPLEMENTAR );
	query.setParameter("estornoVendaEncalheSuplementar", GrupoMovimentoEstoque.ESTORNO_VENDA_ENCALHE_SUPLEMENTAR );
	
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
	public List<VendaSuplementarDTO> obterVendasSuplementar(Date dataOperacao) {
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT ");				
			hql.append(" (SELECT me2.id from MovimentoEstoque as me2 ");       
			hql.append(" JOIN me2.tipoMovimento as tm2 ");       
			hql.append(" WHERE tm2.grupoMovimentoEstoque = :vendaEncalheSuplementar");
			hql.append(" AND me2.data = :dataOperacao) as idVendaSuplementar, ");			
			hql.append(" (SELECT me2.id from MovimentoEstoque as me2");       
			hql.append(" JOIN me2.tipoMovimento as tm2 ");       
			hql.append(" WHERE tm2.grupoMovimentoEstoque = :estornoVendaEncalheSuplementar");
			hql.append(" AND me2.data = :dataOperacao) as idEncalheSuplementar ");			
		hql.append(" FROM MovimentoEstoque as me ");		 
		hql.append(" JOIN me.produtoEdicao as pe ");		
		hql.append(" WHERE me.data = :dataOperacao");				
		hql.append(" GROUP BY me.data");

		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("dataOperacao", dataOperacao);
		
		query.setParameter("vendaEncalheSuplementar", GrupoMovimentoEstoque.VENDA_ENCALHE_SUPLEMENTAR );
		query.setParameter("estornoVendaEncalheSuplementar", GrupoMovimentoEstoque.ESTORNO_VENDA_ENCALHE_SUPLEMENTAR );
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				VendaSuplementarVO.class));
		
		List<VendaSuplementarVO> teste = query.list();
		List<VendaSuplementarDTO> listaFinal = new ArrayList<VendaSuplementarDTO>();
		
		for(VendaSuplementarVO vo: teste){
			if(vo.getIdVendaSuplementar() != null){				
				listaFinal.add(obterVenda(vo));				
			}
		}
		return listaFinal;
		
	}

	private VendaSuplementarDTO obterVenda(VendaSuplementarVO vo) {
		
		StringBuilder hql = new StringBuilder();
		
		
		hql.append(" SELECT p.codigo as codigo,  ");
			hql.append(" p.nomeComercial as nomeProduto, ");
			hql.append(" pe.numeroEdicao as numeroEdicao, ");
			hql.append(" me.qtde as qtde, ");
			hql.append(" (me.qtde * pe.precoVenda) as valor, ");
			hql.append(" ce.data as dataRecolhimento ");					
		hql.append(" FROM ConferenciaEncalhe as ce ");		 
		hql.append(" JOIN ce.movimentoEstoque as me ");		 
		hql.append(" JOIN me.produtoEdicao as pe ");
		hql.append(" JOIN pe.produto as p ");
		hql.append(" WHERE me.id = :idMovimentacao");				
		

		Query query = super.getSession().createQuery(hql.toString());	
		
		query.setParameter("idMovimentacao", vo.getIdVendaSuplementar());		
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				VendaSuplementarDTO.class));
		
		return (VendaSuplementarDTO) query.uniqueResult();
		
	}

}

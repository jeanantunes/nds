package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.DetalheGarantiaCadastradaDTO;
import br.com.abril.nds.dto.GarantiaCadastradaDTO;
import br.com.abril.nds.model.cadastro.Cheque;
import br.com.abril.nds.model.cadastro.TipoGarantia;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantia;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaCaucaoLiquida;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaChequeCaucao;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaFiador;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaImovel;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaNotaPromissoria;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaOutros;
import br.com.abril.nds.repository.CotaGarantiaRepository;

/**
 * 
 * @author Diego Fernandes
 *
 */
@Repository
public class CotaGarantiaRepositoryImpl extends AbstractRepositoryModel<CotaGarantia, Long> implements CotaGarantiaRepository {

	public CotaGarantiaRepositoryImpl() {
		super(CotaGarantia.class);
	}

	@Override
	public CotaGarantia getByCota(Long idCota) {
		Criteria criteria = getSession().createCriteria(CotaGarantia.class);
		
		criteria.add(Restrictions.eq("cota.id", idCota));
		return (CotaGarantia) criteria.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends CotaGarantia> T  getByCota(Long idCota, Class<T> type) {
		Criteria criteria = getSession().createCriteria(CotaGarantia.class);
		
		criteria.add(Restrictions.eq("cota.id", idCota));
		criteria.add(Restrictions.eq("class", type));
		return (T) criteria.uniqueResult();
	}
	
	

	@Override
	public void deleteListaImoveis(Long idGarantia) {
		
		Query query = getSession().createSQLQuery(" DELETE FROM IMOVEL WHERE GARANTIA_ID = :idGarantia ");
		query.setParameter("idGarantia", idGarantia).executeUpdate();

	}

	@Override
	public void deleteListaOutros(Long idGarantia) {
		
		Query query = getSession().createSQLQuery(" DELETE FROM GARANTIA_COTA_OUTROS WHERE GARANTIA_ID = :idGarantia ");
		query.setParameter("idGarantia", idGarantia).executeUpdate();
		
	}

	
	@Override
	public void deleteByCota(Long idCota) {
		
		Query query = getSession().createQuery("DELETE FROM CotaGarantia this_  WHERE this_.cota.id = :idCota");
		query.setParameter("idCota", idCota).executeUpdate();
		
	}
	
	
	@Override
	public Cheque getCheque(long idCheque){
		Criteria criteria = getSession().createCriteria(Cheque.class);
		
		criteria.add(Restrictions.idEq(idCheque));
		return (Cheque) criteria.uniqueResult();
	}

	@Override
	public CotaGarantiaFiador obterCotaGarantiaFiadorPorIdFiador(Long idFiador){
		
		Criteria criteria = this.getSession().createCriteria(CotaGarantiaFiador.class);
		criteria.add(Restrictions.eq("fiador.id", idFiador));
		
		return (CotaGarantiaFiador) criteria.uniqueResult();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<GarantiaCadastradaDTO> obterGarantiasCadastradas() {

		StringBuilder hql = new StringBuilder();

		hql.append(" select TYPE(garantia) as tipoGarantia, ")
		   .append(" 		count(distinct garantia.cota.id) as quantidadeCotas, ")
		   .append("  		sum( ")
		   .append(" 			 case when garantia.class = " + CotaGarantiaFiador.class.getSimpleName())
		   .append("			 	then garantiaFiador.valor ")
		   .append(" 			 when garantia.class = " + CotaGarantiaCaucaoLiquida.class.getSimpleName())
		   .append("			 	then garantiaCaucaoLiquida.valor ")
		   .append(" 			 when garantia.class = " + CotaGarantiaChequeCaucao.class.getSimpleName())
		   .append("			 	then garantiaChequeCaucao.valor ")
		   .append(" 			 when garantia.class = " + CotaGarantiaImovel.class.getSimpleName())
		   .append("			 	then garantiaImovel.valor ")
		   .append(" 			 when garantia.class = " + CotaGarantiaNotaPromissoria.class.getSimpleName())
		   .append("			 	then garantiaNotaPromissoria.valor ")
		   .append(" 			 when garantia.class = " + CotaGarantiaOutros.class.getSimpleName())
		   .append("			 	then garantiaOutros.valor ")
		   .append(" 		end ) as valorTotal ")
		   .append(" from CotaGarantia garantia ")
		   .append(" left join garantia.caucaoLiquidas as garantiaCaucaoLiquida ")
		   .append(" left join garantia.cheque as garantiaChequeCaucao ")
		   .append(" left join garantia.fiador.garantias as garantiaFiador ")
		   .append(" left join garantia.imoveis as garantiaImovel ")
		   .append(" left join garantia.notaPromissoria as garantiaNotaPromissoria ")
		   .append(" left join garantia.outros as garantiaOutros ")
		   .append(" group by garantia.class ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setResultTransformer(new AliasToBeanResultTransformer(GarantiaCadastradaDTO.class));
		
		return query.list();
	}

	
	
	
	
	private String obterHqlFaturamentoCota(){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" COALESCE((select sum( mec.qtde  * ( produtoEdicao.precoVenda ) ) from MovimentoEstoqueCota mec" )
		   .append("           join mec.produtoEdicao produtoEdicao " )
		   .append("           join produtoEdicao.produto produto " ) 
		   .append("           join mec.tipoMovimento tipoMovimento " )
		   .append("           join tipoMovimento.grupoMovimentoEstoque grupoMovimento ")
		   .append("           where mec.cota = garantia.cota ")
		   .append("           and grupoMovimento in (:grupoMovimentoReparte))")
		   .append(" ,0) - " )
		   .append(" COALESCE((select sum( mec.qtde  * ( produtoEdicao.precoVenda ) ) from MovimentoEstoqueCota mec" )
		   .append("           join mec.produtoEdicao produtoEdicao " )
		   .append("           join produtoEdicao.produto produto " ) 
		   .append("           join mec.tipoMovimento tipoMovimento " )
		   .append("           join tipoMovimento.grupoMovimentoEstoque grupoMovimento ")
		   .append("           where mec.cota = garantia.cota ")
		   .append("           and grupoMovimento in (:grupoMovimentoEncalhe))")
		   .append(" ,0) " );  
		
		return hql.toString();
	}
	
	
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<DetalheGarantiaCadastradaDTO> obterDetalheGarantiaCadastrada(TipoGarantia tipoGarantia) {
		
		StringBuilder hql = new StringBuilder();

		hql.append(" select ")
		
		   .append(" cota.numeroCota as numeroCota, ")
		   .append(" case when pessoa.nomeFantasia = null then pessoa.nome else pessoa.nomeFantasia end as nomeCota, ")
		   .append(" TYPE(garantia) as tipoGarantia, ")
		   .append(" garantia.data as vencimento, ")
		   
		   
		   
		   //.append(" COALESCE((select sum(cons.total) from ConsolidadoFinanceiroCota cons where cons.cota = garantia.cota),0)  as faturamentoMes, ")    
          
		   .append("(")
		   .append(  this.obterHqlFaturamentoCota())
		   .append(") as faturamentoMes, ");    

		   switch(tipoGarantia){
		       
		       case FIADOR :
		    	   
		    	   hql.append(" COALESCE(( select sum(gar.valor) from Garantia gar where gar in (garantiaTipo) ),0) as valor, ")

		              .append(" ( ")
		              .append("      ( COALESCE((select sum(gar.valor) from Garantia gar where gar in (garantiaTipo) ),0) *  ")
		              .append("        COALESCE((select sum(cons.total) from ConsolidadoFinanceiroCota cons where cons.cota = garantia.cota),0) ")
		              .append("      ) / 100")
		              .append(" ) as percGarantiaFaturamento ")

		    	      .append(" from CotaGarantia garantia ")
		    	      .append(" join garantia.fiador.garantias as garantiaTipo ");   
		       break;	  
		       
		       case CAUCAO_LIQUIDA :
		    	   
		    	   hql.append(" COALESCE((select sum(cl.valor) from CaucaoLiquida cl where cl in (garantiaTipo) ),0) as valor, ")
		    	      
		    	      .append(" ( ")
		              .append("      ( COALESCE((select sum(cl.valor) from CaucaoLiquida cl where cl in (garantiaTipo) ),0) *  ")
		              .append("        COALESCE((select sum(cons.total) from ConsolidadoFinanceiroCota cons where cons.cota = garantia.cota),0) ")
		              .append("      ) / 100")
		              .append(" ) as percGarantiaFaturamento ")
		              
		    	      .append(" from CotaGarantia garantia ")
		    	      .append(" join garantia.caucaoLiquidas as garantiaTipo ");  
			   break;
		       
		       case CHEQUE_CAUCAO :
		    	   hql.append(" COALESCE(garantiaTipo.valor,0) as valor, ")
		    	   
		    	      .append(" ( ")
		              .append("      ( COALESCE(garantiaTipo.valor,0) *  ")
		              .append("        COALESCE((select sum(cons.total) from ConsolidadoFinanceiroCota cons where cons.cota = garantia.cota),0) ")
		              .append("      ) / 100")
		              .append(" ) as percGarantiaFaturamento ")
		              
		    	      .append(" from CotaGarantia garantia ")
		    	      .append(" join garantia.cheque as garantiaTipo ");  
			   break;
		       
		       case IMOVEL :
		    	   
		    	   hql.append(" COALESCE((select sum(im.valor) from Imovel im where im in (garantiaTipo) ),0) as valor, ")
		    	   
		    	      .append(" ( ")
		              .append("      ( COALESCE((select sum(im.valor) from Imovel im where im in (garantiaTipo) ),0) *  ")
		              .append("        COALESCE((select sum(cons.total) from ConsolidadoFinanceiroCota cons where cons.cota = garantia.cota),0) ")
		              .append("      ) / 100")
		              .append(" ) as percGarantiaFaturamento ")
		    	   
		    	      .append(" from CotaGarantia garantia ")
		    	      .append(" join garantia.imoveis as garantiaTipo ");  
			   break;
		       
		       case NOTA_PROMISSORIA :
		    	   
		    	   hql.append(" COALESCE(garantiaTipo.valor,0) as valor, ")
		    	   
		    	      .append(" ( ")
		              .append("      ( COALESCE(garantiaTipo.valor,0) *  ")
		              .append("        COALESCE((select sum(cons.total) from ConsolidadoFinanceiroCota cons where cons.cota = garantia.cota),0) ")
		              .append("      ) / 100")
		              .append(" ) as percGarantiaFaturamento ")   
		    	    
		    	      .append(" from CotaGarantia garantia ")
		    	      .append(" join garantia.notaPromissoria as garantiaTipo ");  
			   break;
			   
		       case OUTROS :
		    	   
		    	   hql.append(" COALESCE(garantiaTipo.valor,0) as valor, ")
		    	   
		    	      .append(" ( ")
		              .append("      ( COALESCE(garantiaTipo.valor,0) *  ")
		              .append("        COALESCE((select sum(cons.total) from ConsolidadoFinanceiroCota cons where cons.cota = garantia.cota),0) ")
		              .append("      ) / 100")
		              .append(" ) as percGarantiaFaturamento ")
		              
		    	      .append(" from CotaGarantia garantia ")
		    	      .append(" join garantia.outros as garantiaTipo ");  
		       break;
		   }
		   
		   hql.append(" join garantia.cota cota ")
		      .append(" join cota.pessoa as pessoa ")
		      .append(" group by cota ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setResultTransformer(new AliasToBeanResultTransformer(DetalheGarantiaCadastradaDTO.class));
		
		return query.list();
	}
	
	
}

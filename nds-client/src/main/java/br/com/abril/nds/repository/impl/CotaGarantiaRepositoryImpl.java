package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.RelatorioDetalheGarantiaDTO;
import br.com.abril.nds.dto.RelatorioGarantiasDTO;
import br.com.abril.nds.model.cadastro.Cheque;
import br.com.abril.nds.model.cadastro.StatusGarantia;
import br.com.abril.nds.model.cadastro.TipoGarantia;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantia;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaCaucaoLiquida;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaChequeCaucao;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaFiador;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaImovel;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaNotaPromissoria;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaOutros;
import br.com.abril.nds.model.estoque.OperacaoEstoque;
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
	public List<RelatorioGarantiasDTO> obterGarantiasCadastradas(StatusGarantia status, Date data) {

		StringBuilder hql = new StringBuilder();

		hql.append(" select TYPE(garantia) as tipoGarantia, ")
		   .append(" 		count(distinct garantia.cota.id) as qtdCotas, ")
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
		   .append(" 		end ) as vlrTotal ")
		   .append(" from CotaGarantia garantia ")
		   .append(" left join garantia.caucaoLiquidas as garantiaCaucaoLiquida ")
		   .append(" left join garantia.cheque as garantiaChequeCaucao ")
		   .append(" left join garantia.fiador.garantias as garantiaFiador ")
		   .append(" left join garantia.imoveis as garantiaImovel ")
		   .append(" left join garantia.notaPromissoria as garantiaNotaPromissoria ")
		   .append(" left join garantia.outros as garantiaOutros ");
		   
	   if (status!=null && data!=null){
		   if (status.equals(StatusGarantia.VENCIDA)){
			   
			   hql.append(" where garantia.data <= :data ");
		   }
		   
           if (status.equals(StatusGarantia.A_VENCER)){
			   
        	   hql.append(" where garantia.data >= :data ");
		   } 
	   }
		   
		hql.append(" group by garantia.class ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		if (status!=null && data!=null){
		    query.setParameter("data", data);
		}
		
		query.setResultTransformer(new AliasToBeanResultTransformer(RelatorioGarantiasDTO.class));
		
		return query.list();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long obterCountGarantiasCadastradas(StatusGarantia status, Date data) {

		StringBuilder hql = new StringBuilder();

		hql.append(" select count(distinct garantia) ")
		   .append(" from CotaGarantia garantia ")
		   .append(" left join garantia.caucaoLiquidas as garantiaCaucaoLiquida ")
		   .append(" left join garantia.cheque as garantiaChequeCaucao ")
		   .append(" left join garantia.fiador.garantias as garantiaFiador ")
		   .append(" left join garantia.imoveis as garantiaImovel ")
		   .append(" left join garantia.notaPromissoria as garantiaNotaPromissoria ")
		   .append(" left join garantia.outros as garantiaOutros ");
		
		if (status!=null && data!=null){
		   if (status.equals(StatusGarantia.VENCIDA)){
			   
			   hql.append(" where garantia.data <= :data ");
		   }
		   
           if (status.equals(StatusGarantia.A_VENCER)){
			   
        	   hql.append(" where garantia.data >= :data ");
		   } 
	    }
		
		Query query = this.getSession().createQuery(hql.toString());
		
		if (status!=null && data!=null){
		    query.setParameter("data", data);
		}
		
		return (Long) query.uniqueResult();
	}
	
	private String obterHqlDescontoProdutoCota(){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("   ( mec.produtoEdicao.precoVenda - ")
		   .append("    ( mec.produtoEdicao.precoVenda * ")
		   .append("     ( COALESCE((select desconto ")
		   .append("                 from ViewDesconto ")
		   .append("                 where cotaId = garantia.cota ")
		   .append("                 and produtoEdicaoId = mec.produtoEdicao.id ")
		   .append("                ),0) / 100 )    ")
		   .append("   ))");

		return hql.toString();
	}
	private String obterHqlFaturamentoCota(){
		
		StringBuilder hql = new StringBuilder();
         
		hql.append("(")
		   .append(" COALESCE( ")
		   .append("          ( select sum(mec.qtde * ("+this.obterHqlDescontoProdutoCota()+") )  ")
		   .append("            from MovimentoEstoqueCota mec" )
		   .append("            where mec.cota = garantia.cota ")
		   .append("            and mec.tipoMovimento.operacaoEstoque = :movimentoEntrada")
		   .append("            and mec.data = :data ")
		   .append("          )")
		   .append("       ,0)  -  "  )
		   .append(" COALESCE( ")
		   .append("          ( select sum(mec.qtde * ("+this.obterHqlDescontoProdutoCota()+") )  ")
		   .append("            from MovimentoEstoqueCota mec" )
		   .append("            where mec.cota = garantia.cota ")
		   .append("            and mec.tipoMovimento.operacaoEstoque = :movimentoSaida")
		   .append("            and mec.data = :data ")
		   .append("          )")
		   .append("       ,0) " )
		   .append(")");
		
		return hql.toString();
	}
	
	private String obterHqlTipoGarantia(TipoGarantia tipoGarantia){
		  
		boolean caucaoLiquida = false;
		
		StringBuilder hql = new StringBuilder();

		switch(tipoGarantia){
	       
		   case FIADOR :

		       hql.append(" from CotaGarantia garantia ")
	    	      .append(" join garantia.fiador.garantias as garantiaTipo ");   
	       break;	  
	       
	       case CAUCAO_LIQUIDA :

	    	   hql.append(" from CotaGarantia garantia ")  
	    	      .append(" join garantia.caucaoLiquidas as garantiaTipo ");

	    	   caucaoLiquida = true;
		   break;
	       
	       case CHEQUE_CAUCAO :

	    	   hql.append(" from CotaGarantia garantia ")
	    	      .append(" join garantia.cheque as garantiaTipo ");  
		   break;
	       
	       case IMOVEL :

	    	   hql.append(" from CotaGarantia garantia ")
	    	      .append(" join garantia.imoveis as garantiaTipo ");  
		   break;
	       
	       case NOTA_PROMISSORIA :

	    	   hql.append(" from CotaGarantia garantia ")
	    	      .append(" join garantia.notaPromissoria as garantiaTipo ");  
		   break;
		   
	       case OUTROS :

	    	   hql.append(" from CotaGarantia garantia ")
	    	      .append(" join garantia.outros as garantiaTipo ");  
	       break;
	    }
		
		hql.append(" join garantia.cota cota ")
	       .append(" join cota.pessoa as pessoa ");
		
		if (caucaoLiquida){
			
			hql.append("  where garantiaTipo.atualizacao = ( select max(cl.atualizacao) from CaucaoLiquida cl where cl.id = garantiaTipo.id ) ");
		}
		
		return hql.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<RelatorioDetalheGarantiaDTO> obterDetalheGarantiaCadastrada(TipoGarantia tipoGarantia, Date data, String sortColumn, String sortOrder) {

		StringBuilder hql = new StringBuilder();

		hql.append(" select ")
		
		   .append(" cota.numeroCota as cota, ")
		   .append(" case when pessoa.nomeFantasia = null then pessoa.nome else pessoa.nomeFantasia end as nome, ")
		   .append("'")
		   .append(  tipoGarantia.getDescricao())
		   .append("' as garantia, ")
		   .append(" garantia.data as vencto, ")     
		   .append(" COALESCE(garantiaTipo.valor,0) as vlrGarantia, ")
		   
		   .append(" ROUND( ")
		   .append("   (")
		   .append(     this.obterHqlFaturamentoCota())
		   .append("   ) ")  
		   .append(" ,2) as faturamento, ")

	       .append(" ROUND( ")
           .append("        COALESCE( ( garantiaTipo.valor *  ")
           .append("          (100 / (")
		   .append(            this.obterHqlFaturamentoCota())
		   .append("          )) ")
           .append("        ),0)")
           .append(" ,2) as garantiaFaturamento ");
 
	    hql.append(this.obterHqlTipoGarantia(tipoGarantia))

	       .append(" group by cota ");
	
		hql.append(" order by ")
	       .append(sortColumn!=null && !sortColumn.equals("")?sortColumn:" vencto ")
	       .append(" ")
	       .append(sortOrder!=null && !sortOrder.equals("")?sortOrder:" desc ");

		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("data", data);
		query.setParameter("movimentoEntrada", OperacaoEstoque.ENTRADA);
		query.setParameter("movimentoSaida", OperacaoEstoque.SAIDA);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(RelatorioDetalheGarantiaDTO.class));

		return query.list();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long obterCountDetalheGarantiaCadastrada(TipoGarantia tipoGarantia, Date data) {
		
		StringBuilder hql = new StringBuilder();

		hql.append(" select count(distinct cota) ");
 
		hql.append(this.obterHqlTipoGarantia(tipoGarantia));
		
		Query query = this.getSession().createQuery(hql.toString());
		
		return (Long) query.uniqueResult();
	}
	
}

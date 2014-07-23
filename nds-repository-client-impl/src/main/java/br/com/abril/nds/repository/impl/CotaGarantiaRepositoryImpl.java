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
import br.com.abril.nds.dto.filtro.FiltroRelatorioGarantiasDTO;
import br.com.abril.nds.model.cadastro.CaucaoLiquida;
import br.com.abril.nds.model.cadastro.Cheque;
import br.com.abril.nds.model.cadastro.TipoGarantia;
import br.com.abril.nds.model.cadastro.TipoStatusGarantia;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantia;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaFiador;
import br.com.abril.nds.model.estoque.OperacaoEstoque;
import br.com.abril.nds.repository.AbstractRepositoryModel;
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
	
	@Override
	public Long getQtdCotaGarantiaByCota(Long idCota){
		
		StringBuilder hql = new StringBuilder("select distinct ");
		hql.append(" count(cg.id) ")
		   .append(" from CotaGarantia cg ")
		   .append(" join cg.cota cota ")
		   .append(" where cota.id = :idCota ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idCota", idCota);
		
		return (Long) query.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends CotaGarantia> T  getByCota(Long idCota, Class<T> type) {
		Criteria criteria = getSession().createCriteria(CotaGarantia.class);
		
		criteria.add(Restrictions.eq("cota.id", idCota));
		criteria.add(Restrictions.eq("class", type));
		return (T) criteria.uniqueResult();
	}
	
	/**
	 * Obtem lista de CaucaoLiquida da Cota
	 * @param idCota
	 * @return List<CaucaoLiquida>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<CaucaoLiquida> getCaucaoLiquidasCota(Long idCota) {
		
		StringBuilder hql = new StringBuilder("select ");
		hql.append(" cgcl.caucaoLiquidas ")
		   .append(" from  CotaGarantiaCaucaoLiquida cgcl ")
		   .append(" join  cgcl.cota cota ")
		   .append(" where cota.id = :idCota ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("idCota", idCota);
		
		return query.list();
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
	public List<RelatorioGarantiasDTO> obterGarantiasCadastradas(FiltroRelatorioGarantiasDTO filtro) {

		StringBuilder hql = new StringBuilder();

		hql.append(" select garantia.tipoGarantia as tipoGarantia, ")
		   .append(" 		count(distinct garantia.cota.id) as qtdCotas, ")
		   .append("  		sum( ")
		   .append(" 			 case when garantia.tipoGarantia = :tipoGarantiaFiador ")
		   .append("			 	then coalesce(garantiaFiador.valor, 0) ")
		   .append(" 			 when garantia.tipoGarantia = :tipoGarantiaCaucaoLiquida ")
		   .append("			 	then coalesce(garantiaCaucaoLiquida.valor, 0) ")
		   .append(" 			 when garantia.tipoGarantia = :tipoGarantiaChequeCaucao")
		   .append("			 	then coalesce(garantiaChequeCaucao.valor, 0) ")
		   .append(" 			 when garantia.tipoGarantia = :tipoGarantiaImovel")
		   .append("			 	then coalesce(garantiaImovel.valor, 0) ")
		   .append(" 			 when garantia.tipoGarantia = :tipoGarantiaNotaPromissoria")
		   .append("			 	then coalesce(garantiaNotaPromissoria.valor, 0) ")
		   .append(" 			 when garantia.tipoGarantia = :tipoGarantiaOutros")
		   .append("			 	then coalesce(garantiaOutros.valor, 0) ")
		   .append(" 		end ) as vlrTotal ")
		   .append(" from CotaGarantia garantia ")
		   .append(" left join garantia.caucaoLiquidas as garantiaCaucaoLiquida ")
		   .append(" left join garantia.cheque as garantiaChequeCaucao ")
		   .append(" left join garantia.fiador.garantias as garantiaFiador with garantiaFiador is not null ")
		   .append(" left join garantia.imoveis as garantiaImovel ")
		   .append(" left join garantia.notaPromissoria as garantiaNotaPromissoria ")
		   .append(" left join garantia.outros as garantiaOutros ")
		   .append(" where garantia.tipoGarantia in (select tga.tipoGarantia from Distribuidor d join d.tiposGarantiasAceita tga) ");
		   
	   TipoStatusGarantia status = filtro.getStatusGarantia();	

	   Date data = filtro.getDataBaseCalculo();
	   if (status!=null && data!=null){
		   if (status.equals(TipoStatusGarantia.VENCIDA)){
			   
			   hql.append(" and garantia.data <= :data ");
		   }
           if (status.equals(TipoStatusGarantia.A_VENCER)){
			   
        	   hql.append(" and garantia.data >= :data ");
		   } 
	   }
		   
		hql.append(" group by garantia.tipoGarantia ");

		if (filtro.getPaginacao()!=null){
			hql.append(" order by ")
		       .append(filtro.getPaginacao().getSortColumn()!=null && !filtro.getPaginacao().getSortColumn().equals("")?filtro.getPaginacao().getSortColumn():" tipoGarantia ")
		       .append(" ")
		       .append(filtro.getPaginacao().getSortOrder()!=null && !filtro.getPaginacao().getSortOrder().equals("")?filtro.getPaginacao().getSortOrder():" desc ");
	    }
		
		Query query = this.getSession().createQuery(hql.toString());
		
		//Controla a paginação
		if(filtro.getPaginacao() != null
				&& (filtro.getPaginacao().getPaginaAtual() != null 
				&& filtro.getPaginacao().getPaginaAtual() > 0) 
				&& (filtro.getPaginacao().getQtdResultadosPorPagina() != null 
				&& filtro.getPaginacao().getQtdResultadosPorPagina() > 0)) {
			
			query.setFirstResult((filtro.getPaginacao().getPaginaAtual() - 1) * filtro.getPaginacao().getQtdResultadosPorPagina());
			query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
		}
		
		query.setParameter("tipoGarantiaFiador", TipoGarantia.FIADOR);
		query.setParameter("tipoGarantiaCaucaoLiquida", TipoGarantia.CAUCAO_LIQUIDA);
		query.setParameter("tipoGarantiaChequeCaucao", TipoGarantia.CHEQUE_CAUCAO);
		query.setParameter("tipoGarantiaImovel", TipoGarantia.IMOVEL);
		query.setParameter("tipoGarantiaNotaPromissoria", TipoGarantia.NOTA_PROMISSORIA);
		query.setParameter("tipoGarantiaOutros", TipoGarantia.OUTROS);
		
		if (status != null && data != null){
		    query.setParameter("data", data);
		}
		
		query.setResultTransformer(new AliasToBeanResultTransformer(RelatorioGarantiasDTO.class));
		
		return query.list();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long obterCountGarantiasCadastradas(FiltroRelatorioGarantiasDTO filtro) {

		StringBuilder hql = new StringBuilder();

		hql	.append(" select count(distinct garantia.tipoGarantia) ")
		   	.append(" from CotaGarantia garantia ")
		   	.append(" left join garantia.caucaoLiquidas as garantiaCaucaoLiquida ")
		   	.append(" left join garantia.cheque as garantiaChequeCaucao ")
		   	.append(" left join garantia.fiador.garantias as garantiaFiador ")
		   	.append(" left join garantia.imoveis as garantiaImovel ")
		   	.append(" left join garantia.notaPromissoria as garantiaNotaPromissoria ")
		   	.append(" left join garantia.outros as garantiaOutros ")
			.append(" where garantia.tipoGarantia in (select tga.tipoGarantia from Distribuidor d join d.tiposGarantiasAceita tga) ");
		
		TipoStatusGarantia status = filtro.getStatusGarantia();	
	
	    Date data = filtro.getDataBaseCalculo();
		if (status!=null && data!=null){
		   if (status.equals(TipoStatusGarantia.VENCIDA)){
			   
			   hql.append(" and garantia.data <= :data ");
		   }
		   
           if (status.equals(TipoStatusGarantia.A_VENCER)){
			   
        	   hql.append(" and garantia.data >= :data ");
		   } 
	    }
		
		Query query = this.getSession().createQuery(hql.toString());
		
		if (status!=null && data!=null){
		    query.setParameter("data", data);
		}
		
		return (Long) query.uniqueResult();
	}

	private String obterHqlFaturamentoCota(){
		
		StringBuilder hql = new StringBuilder();
         
		hql.append("(")
		   .append(" COALESCE( ")
		   .append("          ( select sum(mec.qtde * (mec.valoresAplicados.precoVenda - (mec.valoresAplicados.precoVenda * mec.valoresAplicados.valorDesconto / 100 )) )  ")
		   .append("            from MovimentoEstoqueCota mec" )
		   .append("            where mec.cota = garantia.cota ")
		   .append("            and mec.tipoMovimento.operacaoEstoque = :movimentoEntrada")
		   .append("            and mec.data = :data ")
		   .append("          )")
		   .append("       ,0)  -  "  )
		   .append(" COALESCE( ")
		   .append("          ( select sum(mec.qtde * (mec.valoresAplicados.precoVenda * mec.valoresAplicados.valorDesconto / 100) )  ")
		   .append("            from MovimentoEstoqueCota mec" )
		   .append("            where mec.cota = garantia.cota ")
		   .append("            and mec.tipoMovimento.operacaoEstoque = :movimentoSaida")
		   .append("            and mec.data = :data ")
		   .append("          )")
		   .append("       ,0) " )
		   .append(")");
		
		return hql.toString();
	}
	
	private String obterHqlTipoGarantia(FiltroRelatorioGarantiasDTO filtro){
		  
		boolean caucaoLiquida = false;
		
		StringBuilder hql = new StringBuilder();

		TipoGarantia tipoGarantia = filtro.getTipoGarantia();
		TipoStatusGarantia status = filtro.getStatusGarantia();	
		Date data = filtro.getDataBaseCalculo();
		
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
		
		
		String j = " WHERE ";
		
		if (caucaoLiquida){
			
			hql.append(j+" garantiaTipo.atualizacao = ( select max(cl.atualizacao) from CaucaoLiquida cl where cl.id = garantiaTipo.id ) ");
			j = "AND";
		}
		   
		if (status!=null && data!=null){
		   if (status.equals(TipoStatusGarantia.VENCIDA)){
			   
			   hql.append(j+" garantia.data <= :data ");
		   }
           if (status.equals(TipoStatusGarantia.A_VENCER)){
			   
        	   hql.append(j+" garantia.data >= :data ");
		   } 
	    }

		return hql.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<RelatorioDetalheGarantiaDTO> obterDetalheGarantiaCadastrada(FiltroRelatorioGarantiasDTO filtro) {

		StringBuilder hql = new StringBuilder();
		
		TipoGarantia tipoGarantia = filtro.getTipoGarantia();	
		Date data = filtro.getDataBaseCalculo();

		hql.append(" select ")
		
		   .append(" cota.numeroCota as cota, ")
		   .append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome, '') as nome, ")
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
 
	    hql.append(this.obterHqlTipoGarantia(filtro))

	       .append(" group by cota ");
	    
	    if (filtro.getPaginacao()!=null){
			hql.append(" order by ")
		       .append(filtro.getPaginacao().getSortColumn()!=null && !filtro.getPaginacao().getSortColumn().equals("")?filtro.getPaginacao().getSortColumn():" vencto ")
		       .append(" ")
		       .append(filtro.getPaginacao().getSortOrder()!=null && !filtro.getPaginacao().getSortOrder().equals("")?filtro.getPaginacao().getSortOrder():" desc ");
	    }
		
		Query query = this.getSession().createQuery(hql.toString());
		
		//Controla a paginação
		if(filtro.getPaginacao() != null
				&& (filtro.getPaginacao().getPaginaAtual() != null 
				&& filtro.getPaginacao().getPaginaAtual() > 0) 
				&& (filtro.getPaginacao().getQtdResultadosPorPagina() != null 
				&& filtro.getPaginacao().getQtdResultadosPorPagina() > 0)) {
			
			query.setFirstResult((filtro.getPaginacao().getPaginaAtual() - 1) * filtro.getPaginacao().getQtdResultadosPorPagina());
			query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
		}
		
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
	public Long obterCountDetalheGarantiaCadastrada(FiltroRelatorioGarantiasDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		TipoStatusGarantia status = filtro.getStatusGarantia();
		
		Date data = filtro.getDataBaseCalculo();

		hql.append(" select count(distinct garantia) ");
 
		hql.append(this.obterHqlTipoGarantia(filtro));
		
		Query query = this.getSession().createQuery(hql.toString());
		
		if (status!=null && data!=null){
		    query.setParameter("data", data);
		}
		
		return (Long) query.uniqueResult();
	}

	@Override
	public boolean existeCaucaoLiquidasCota(Long idCota) {
		
		StringBuilder hql = new StringBuilder("select case count(cgcl.id) when 0 then false else true end ");
		hql.append(" from  CotaGarantiaCaucaoLiquida cgcl ")
		   .append(" join  cgcl.cota cota ")
		   .append(" where cota.id = :idCota ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("idCota", idCota);
		
		return (boolean) query.uniqueResult();
	}

	@Override
	public boolean verificarQuitacaoCaucaoLiquida(Long idCota) {
		
		StringBuilder hql = new StringBuilder("select case p.valor when 0 then false else true end ");
		hql.append(" from  CotaGarantiaCaucaoLiquida cgcl ")
		   .append(" join  cgcl.formaPagamento p ")
		   .append(" join  cgcl.cota cota ")
		   .append(" where cota.id = :idCota ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("idCota", idCota);
		
		return (boolean) query.uniqueResult();
	}
}
package br.com.abril.nds.repository.impl;

import java.util.List;

import br.com.abril.nds.client.vo.EstudoComplementarVO;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.repository.EstudoComplementarRepository;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import br.com.abril.nds.repository.AbstractRepositoryModel;

@Transactional
@Repository
public class EstudoComplementarRepositoryImpl extends AbstractRepositoryModel<EstudoCota, Long>implements
		EstudoComplementarRepository {

	public EstudoComplementarRepositoryImpl() {
		super(EstudoCota.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<EstudoCota> selecionarBancas(EstudoComplementarVO estudoComplementarVO) {
		
		String bancas = montaSQL(estudoComplementarVO);
		Query query = super.getSession()
				            .createSQLQuery(bancas)
		
				            .addEntity(EstudoCota.class);
		
		       query.setParameter("estudoId", estudoComplementarVO.getCodigoEstudo());
		       //query.setParameter("reparte", estudoComplementarVO.getReparteLancamento());
				
			
				
				List result = query.list();		
				return result;
	}

	private String montaSQL(EstudoComplementarVO estudoComplementarVO) {
	
		StringBuilder sqlStmt = new StringBuilder();
		StringBuilder sqlTipoSelecao = new StringBuilder();
		
		if (estudoComplementarVO.getTipoSelecao()==1){
			
			sqlTipoSelecao.append( " Select distinct ranking_segmento.COTA_ID FROM ranking_segmento ");
			sqlTipoSelecao.append( "                  where (select max(ranking_segmento.data_geracao_rank)  from ranking_segmento) ");
			
			
		}
		
		if (estudoComplementarVO.getTipoSelecao()==2){
			sqlTipoSelecao.append( " Select distinct ranking_faturamento.COTA_ID FROM ranking_faturamento ");
			sqlTipoSelecao.append( "                  where (select max(ranking_faturamento.data_geracao_rank)  from ranking_faturamento) ");
			
			
		}
		
		//-- SELEÇÃO DAS BANCAS E DISTRIBUIÇÃO DO ESTUDO COMPLEMENTAR

		sqlStmt.append( "select distinct ec.* from ");
		sqlStmt.append( "             produto_edicao     pe, "); 
		sqlStmt.append( "		      estudo_cota        ec, ");
		sqlStmt.append( "             produto_fornecedor pf, "); 
		sqlStmt.append( "             cota_fornecedor    cf, ");
		sqlStmt.append( "             cota               co  ");

		sqlStmt.append( " where  "); 
		sqlStmt.append( "      pe.PRODUTO_ID = pf.PRODUTO_ID ");
		sqlStmt.append( "  and ec.COTA_ID    = cf.COTA_ID ");
		sqlStmt.append( "  and ec.COTA_ID    = co.ID      ");
		sqlStmt.append( "  and co.id in (:tipoSelecao ) ");
		sqlStmt.append( "  and (ec.REPARTE=0 or ec.REPARTE is null) ");
		sqlStmt.append( "  and ec.ESTUDO_ID             = :estudoId ");
		sqlStmt.append( "  and co.SITUACAO_CADASTRO   = 'ATIVO' ");
		sqlStmt.append( "  and ec.CLASSIFICACAO  in ('CL', 'GN', 'SM','SH') ");
		sqlStmt.append( "  and 0 = (select count(epe.PRODUTO_EDICAO_ID) soma from estudo_produto_edicao_base epe where epe.PRODUTO_EDICAO_ID  = pe.id ) "); 
		
		sqlStmt.append( " union ");

		sqlStmt.append( "select distinct ec.* from ");
		sqlStmt.append( "             produto_edicao     pe, "); 
		sqlStmt.append( "		      estudo_cota        ec, ");
		sqlStmt.append( "             produto_fornecedor pf, "); 
		sqlStmt.append( "             cota_fornecedor    cf, ");
		sqlStmt.append( "             cota               co  ");

		sqlStmt.append( " where  "); 
		sqlStmt.append( "      pe.PRODUTO_ID = pf.PRODUTO_ID ");
		sqlStmt.append( "  and ec.COTA_ID    = cf.COTA_ID ");
		sqlStmt.append( "  and ec.COTA_ID    = co.ID      ");
		sqlStmt.append( "  and co.id in (:tipoSelecao ) ");
		sqlStmt.append( "  and (ec.REPARTE=0 or ec.REPARTE is null) ");
		sqlStmt.append( "  and ec.ESTUDO_ID             = :estudoId ");
		sqlStmt.append( "  and co.SITUACAO_CADASTRO   = 'ATIVO' ");
		sqlStmt.append( "  and ec.CLASSIFICACAO  in ('CL', 'GN', 'SM','SH') ");
		sqlStmt.append( "  and 1 = (select count(epe.PRODUTO_EDICAO_ID) soma from estudo_produto_edicao_base epe where epe.PRODUTO_EDICAO_ID  = pe.id ) "); 
		    
		sqlStmt.append( " union ");
		sqlStmt.append( "select distinct ec.* from ");
		sqlStmt.append( "             produto_edicao     pe, "); 
		sqlStmt.append( "		      estudo_cota        ec, ");
		sqlStmt.append( "             produto_fornecedor pf, "); 
		sqlStmt.append( "             cota_fornecedor    cf, ");
		sqlStmt.append( "             cota               co  ");

		sqlStmt.append( " where  "); 
		sqlStmt.append( "      pe.PRODUTO_ID = pf.PRODUTO_ID ");
		sqlStmt.append( "  and ec.COTA_ID    = cf.COTA_ID ");
		sqlStmt.append( "  and ec.COTA_ID    = co.ID      ");
		sqlStmt.append( "  and co.id in (:tipoSelecao ) ");
		sqlStmt.append( "  and (ec.REPARTE=0 or ec.REPARTE is null) ");
		sqlStmt.append( "  and ec.ESTUDO_ID             = :estudoId ");
		sqlStmt.append( "  and co.SITUACAO_CADASTRO   = 'ATIVO' ");
		sqlStmt.append( "  and ec.CLASSIFICACAO  in ('CL', 'GN', 'SM','SH') ");
		sqlStmt.append( "  and 2 = (select count(epe.PRODUTO_EDICAO_ID) soma from estudo_produto_edicao_base epe where epe.PRODUTO_EDICAO_ID  = pe.id ) "); 
		
		    
		sqlStmt.append( " union ");

		sqlStmt.append( "select distinct ec.* from ");
		sqlStmt.append( "             produto_edicao     pe, "); 
		sqlStmt.append( "		      estudo_cota        ec, ");
		sqlStmt.append( "             produto_fornecedor pf, "); 
		sqlStmt.append( "             cota_fornecedor    cf, ");
		sqlStmt.append( "             cota               co  ");

		sqlStmt.append( " where  "); 
		sqlStmt.append( "      pe.PRODUTO_ID = pf.PRODUTO_ID ");
		sqlStmt.append( "  and ec.COTA_ID    = cf.COTA_ID ");
		sqlStmt.append( "  and ec.COTA_ID    = co.ID      ");
		sqlStmt.append( "  and co.id in (:tipoSelecao ) ");
		sqlStmt.append( "  and (ec.REPARTE=0 or ec.REPARTE is null) ");
		sqlStmt.append( "  and ec.ESTUDO_ID             = :estudoId ");
		sqlStmt.append( "  and co.SITUACAO_CADASTRO   = 'ATIVO' ");
		sqlStmt.append( "  and ec.CLASSIFICACAO  in ('CL', 'GN', 'SM','SH') ");
		sqlStmt.append( "  and 3 = (select count(epe.PRODUTO_EDICAO_ID) soma from estudo_produto_edicao_base epe where epe.PRODUTO_EDICAO_ID  = pe.id ) "); 
		
		
		System.out.println("--------->>" + sqlStmt.toString().replaceAll(":tipoSelecao", sqlTipoSelecao.toString()));
		
		return sqlStmt.toString().replaceAll(":tipoSelecao", sqlTipoSelecao.toString());
		
		 
		
		
	}

	@Override
	public long gerarNumeroEstudoComplementar() {
		
		String queryString = "select max(id) from Estudo";
		Query query = super.getSession().createQuery(queryString );
		
		Long max = (Long) query.list().get(0);  
		
		return max+1;
	}

}

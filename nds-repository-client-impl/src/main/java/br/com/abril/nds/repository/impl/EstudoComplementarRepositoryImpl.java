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
			
			sqlTipoSelecao.append( "select ranking_segmento.COTA_ID from ranking_segmento ");
			sqlTipoSelecao.append( "                 where ranking_segmento.DATA_GERACAO_RANK = (select max(DATA_GERACAO_RANK) from  ranking_segmento) ");
			sqlTipoSelecao.append( "                   and ranking_segmento.PRODUTO_EDICAO_ID = b.PRODUTO_EDICAO_ID ");
			sqlTipoSelecao.append( "                   and ranking_segmento.TIPO_SEGMENTO_PRODUTO_ID = prod.TIPO_SEGMENTO_PRODUTO_ID ");
			
			
		}
		
		if (estudoComplementarVO.getTipoSelecao()==2){
			sqlTipoSelecao.append( "select ranking_faturamento.COTA_ID from ranking_faturamento ");
			sqlTipoSelecao.append( "                 where ranking_faturamento.DATA_GERACAO_RANK = (select max(DATA_GERACAO_RANK) from  ranking_faturamento) ");
			
		}
		
		//-- SELEÇÃO DAS BANCAS E DISTRIBUIÇÃO DO ESTUDO COMPLEMENTAR

		sqlStmt.append( "select distinct ec.* from estudo, ");
		sqlStmt.append( "             produto_edicao b, "); 
		sqlStmt.append( "		        produto prod, ");
		sqlStmt.append( "              fornecedor fornec, "); 
		sqlStmt.append( "              produto_fornecedor prod_for, ");
		sqlStmt.append( "              cota_fornecedor cot_for, ");
		sqlStmt.append( "		      cota, ");
		sqlStmt.append( "			  estudo_cota ec ");

		sqlStmt.append( " where estudo.PRODUTO_EDICAO_ID = b.ID "); 
		sqlStmt.append( "  and  b.PRODUTO_ID            = prod.ID ");
		sqlStmt.append( "  and prod.ID                  = prod_for.PRODUTO_ID ");
		sqlStmt.append( "  and prod_for.fornecedores_ID = fornec.ID ");
		sqlStmt.append( "  and fornec.id                = cot_for.FORNECEDOR_ID ");
		sqlStmt.append( "  and cot_for.COTA_ID          = cota.ID ");
		  
		sqlStmt.append( "  and cota.id in (:tipoSelecao ) ");
		
		                 
		
		sqlStmt.append( "  and estudo.id = ec.ESTUDO_ID ");
		sqlStmt.append( "  and cota.SITUACAO_CADASTRO   = 'ATIVO' ");
		sqlStmt.append( "  and estudo.ID                = :estudoId ");
		sqlStmt.append( "  and ec.CLASSIFICACAO  in ('CL', 'GN', 'SM','SH') ");
		sqlStmt.append( "  and 0 = (select sum(epe.PRODUTO_EDICAO_ID) soma from estudo_produto_edicao epe where epe.PRODUTO_EDICAO_ID  = b.id ) "); 
		sqlStmt.append( "  and cota.RECEBE_RECOLHE_PARCIAIS = true ");
		
		sqlStmt.append( " union ");

		sqlStmt.append( "select distinct ec.* from estudo, ");
		sqlStmt.append( "             produto_edicao b, "); 
		sqlStmt.append( "		        produto prod, ");
		sqlStmt.append( "              fornecedor fornec, "); 
		sqlStmt.append( "              produto_fornecedor prod_for, ");
		sqlStmt.append( "              cota_fornecedor cot_for, ");
		sqlStmt.append( "		      cota, ");
		sqlStmt.append( "			  estudo_cota ec ");

		sqlStmt.append( " where estudo.PRODUTO_EDICAO_ID = b.ID "); 
		sqlStmt.append( "  and  b.PRODUTO_ID            = prod.ID ");
		sqlStmt.append( "  and prod.ID                  = prod_for.PRODUTO_ID ");
		sqlStmt.append( "  and prod_for.fornecedores_ID = fornec.ID ");
		sqlStmt.append( "  and fornec.id                = cot_for.FORNECEDOR_ID ");
		sqlStmt.append( "  and cot_for.COTA_ID          = cota.ID ");
		  
		sqlStmt.append( "  and cota.id in (:tipoSelecao ) ");
		
		                 
		
		sqlStmt.append( "  and estudo.id = ec.ESTUDO_ID ");
		sqlStmt.append( "  and cota.SITUACAO_CADASTRO   = 'ATIVO' ");
		sqlStmt.append( "  and estudo.ID                = :estudoId ");
		sqlStmt.append( "  and ec.CLASSIFICACAO  in ('CL', 'GN', 'SM','VZ') ");
		sqlStmt.append( "  and 1 = (select sum(epe.PRODUTO_EDICAO_ID) soma from estudo_produto_edicao epe where epe.PRODUTO_EDICAO_ID  = b.id ) "); 
		sqlStmt.append( "  and cota.RECEBE_RECOLHE_PARCIAIS = true ");
		    
		sqlStmt.append( " union ");
		
		sqlStmt.append( "select distinct ec.* from estudo, ");
		sqlStmt.append( "             produto_edicao b, "); 
		sqlStmt.append( "		        produto prod, ");
		sqlStmt.append( "              fornecedor fornec, "); 
		sqlStmt.append( "              produto_fornecedor prod_for, ");
		sqlStmt.append( "              cota_fornecedor cot_for, ");
		sqlStmt.append( "		      cota, ");
		sqlStmt.append( "			  estudo_cota ec ");

		sqlStmt.append( " where estudo.PRODUTO_EDICAO_ID = b.ID " ); 
		sqlStmt.append( "  and  b.PRODUTO_ID            = prod.ID ");
		sqlStmt.append( "  and prod.ID                  = prod_for.PRODUTO_ID ");
		sqlStmt.append( "  and prod_for.fornecedores_ID = fornec.ID ");
		sqlStmt.append( "  and fornec.id                = cot_for.FORNECEDOR_ID ");
		sqlStmt.append( "  and cot_for.COTA_ID          = cota.ID ");
		  
		sqlStmt.append( "  and cota.id in (:tipoSelecao ) ");
		
		                 
		
		sqlStmt.append( "  and estudo.id = ec.ESTUDO_ID ");
		sqlStmt.append( "  and cota.SITUACAO_CADASTRO   = 'ATIVO' ");
		sqlStmt.append( "  and estudo.ID                = :estudoId ");
		sqlStmt.append( "  and ec.CLASSIFICACAO  in ('CL', 'GN', 'SM','VZ') ");
		sqlStmt.append( "  and 2 = (select sum(epe.PRODUTO_EDICAO_ID) soma from estudo_produto_edicao epe where epe.PRODUTO_EDICAO_ID  = b.id ) "); 
		sqlStmt.append( "  and cota.RECEBE_RECOLHE_PARCIAIS = true ");
		    
		sqlStmt.append( " union ");

		sqlStmt.append( "select distinct ec.* from estudo, ");
		sqlStmt.append( "             produto_edicao b, "); 
		sqlStmt.append( "		        produto prod, ");
		sqlStmt.append( "              fornecedor fornec, "); 
		sqlStmt.append( "              produto_fornecedor prod_for, ");
		sqlStmt.append( "              cota_fornecedor cot_for, ");
		sqlStmt.append( "		      cota, ");
		sqlStmt.append( "			  estudo_cota ec ");

		sqlStmt.append( " where estudo.PRODUTO_EDICAO_ID = b.ID "); 
		sqlStmt.append( "  and  b.PRODUTO_ID            = prod.ID ");
		sqlStmt.append( "  and prod.ID                  = prod_for.PRODUTO_ID ");
		sqlStmt.append( "  and prod_for.fornecedores_ID = fornec.ID ");
		sqlStmt.append( "  and fornec.id                = cot_for.FORNECEDOR_ID ");
		sqlStmt.append( "  and cot_for.COTA_ID          = cota.ID ");
		  
		sqlStmt.append( "  and cota.id in (:tipoSelecao ) ");
		
		                 
		
		sqlStmt.append( "  and estudo.id = ec.ESTUDO_ID ");
		sqlStmt.append( "  and cota.SITUACAO_CADASTRO   = 'ATIVO' ");
		sqlStmt.append( "  and estudo.ID                = :estudoId ");
		sqlStmt.append( "  and ec.CLASSIFICACAO  in ('CL', 'GN', 'SM','VZ') ");
		sqlStmt.append( "  and 3 = (select sum(epe.PRODUTO_EDICAO_ID) soma from estudo_produto_edicao epe where epe.PRODUTO_EDICAO_ID  = b.id ) "); 
		sqlStmt.append( "  and cota.RECEBE_RECOLHE_PARCIAIS = true ");
		
		
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

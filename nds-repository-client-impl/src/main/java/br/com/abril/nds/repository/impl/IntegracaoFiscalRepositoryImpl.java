package br.com.abril.nds.repository.impl;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ExtratoEdicaoArquivoP7DTO;
import br.com.abril.nds.dto.IntegracaoFiscalDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaIntegracaoFiscal;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.IntegracaoFiscalRepository;
/**
 * Classe de implementação referente ao acesso a dados da entidade
 *
 * 
 */

@Repository
public class IntegracaoFiscalRepositoryImpl extends AbstractRepositoryModel<MovimentoEstoque, Long> implements IntegracaoFiscalRepository {
 
	@Autowired
	private DistribuidorRepository distribuidorRepository;
	
	public IntegracaoFiscalRepositoryImpl() {
		super(MovimentoEstoque.class);
	}

	@Override
	public List<IntegracaoFiscalDTO> pesquisarPorMesAno(FiltroConsultaIntegracaoFiscal filtro) {
		
		return null;
	}

	@Override
	public void obterFixacoesRepartePorProduto(FiltroConsultaIntegracaoFiscal filtro) {
		// TODO Auto-generated method stub
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ExtratoEdicaoArquivoP7DTO> inventarioP7(Date time) {
		
		Distribuidor dist = distribuidorRepository.obter();
		StringBuilder sql = getInvetarioSQL(Boolean.FALSE);
		        
		Query query = 
				this.getSession().createSQLQuery(
						sql.toString());
		
		Calendar c = GregorianCalendar.getInstance();
		c.setTime(time);
		
		String val = c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.DAY_OF_MONTH);
		query.setParameter("dataInventario",val);
		query.setParameter("dataInventario2",val);
		query.setParameter("codEmpresa",dist.getJuridica().getCnpj().substring(0,dist.getJuridica().getCnpj().indexOf("/")).replace(".",""));
		query.setParameter("codFilial",dist.getCodigo().toString());
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ExtratoEdicaoArquivoP7DTO.class));
		
		return query.list();
	}

	private StringBuilder getInvetarioSQL(Boolean isCount) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select ");
		
		if(isCount==false){
			
			sql.append(" :codEmpresa cod_empresa, ")
			.append(" :codFilial cod_filial, ")
			.append(" replace(cast(round(t.custo_unitario,4) as char),'.',',') custo_unitario,  ") 
			.append(" cast(round(sum(entrada)-sum(saida)) as char) quantidade, ")
			.append(" 'UN' as unidade_medida, ")
			.append(" replace(cast(round((sum(entrada)-sum(saida))*t.custo_unitario,3) as char),'.',',') custo_total, ") 
			.append(" DATE_FORMAT(last_day(:dataInventario2),'%d/%m/%Y') dat_inventario, ") 
			.append(" cast(coalesce(T.ncmCodigo,'') as char) as classif_fiscal ");
			
		}else{
			sql.append(" count(*) ");
		}
		
		sql.append(" from")
				.append(" (select ")
		        .append(" me.ID as MOVIMENTO_ID, ")
		        .append(" pe.id as produto_edicao_id, ")
		        .append(" pe.preco_venda as custo_unitario, ")
		        .append(" me.DATA as DATA_CRIACAO, ")
		        .append(" tipomovime1_.DESCRICAO as DESCRICAO, ")
		        .append(" ncm.codigo as ncmCodigo, ")
		        .append(" sum(case  when tipomovime1_.OPERACAO_ESTOQUE='ENTRADA' then me.QTDE ") 
		            .append(" else 0 ") 
		        .append(" end) as ENTRADA, ")
		        .append(" sum(case when tipomovime1_.OPERACAO_ESTOQUE='SAIDA' then me.QTDE ") 
		            .append(" else 0 ") 
		        .append(" end) as SAIDA ") 
		    .append(" from ")
		        .append(" MOVIMENTO_ESTOQUE me, ")
		        .append(" TIPO_MOVIMENTO tipomovime1_ cross ") 
		    .append(" join ")
		        .append(" PRODUTO_EDICAO pe cross ") 
		    .append(" join ")
		        .append(" PRODUTO p ") 
		        .append(" left join TIPO_PRODUTO tp on p.tipo_produto_id = tp.ID ")
		        .append(" left join NCM ncm on ncm.ID = tp.NCM_ID ")
		        
		    .append(" where ")
		        .append(" me.TIPO_MOVIMENTO_ID=tipomovime1_.ID ") 
		        .append(" and me.PRODUTO_EDICAO_ID=pe.ID ") 
		        .append(" and pe.PRODUTO_ID=p.ID ") 
		        .append(" and me.DATA_CRIACAO < last_day(:dataInventario) ")
		        .append(" and me.STATUS='APROVADO' ");
		        
		    sql.append(" group by ")
		        .append(" me.PRODUTO_EDICAO_ID , ")
		        .append(" me.DATA , ")
		        .append(" me.TIPO_MOVIMENTO_ID "); 
		        if(isCount==true){
		        	sql.append(" having entrada-saida > 0 ");
		        }
		    sql.append(" order by ")
		        .append(" case ") 
		            .append(" when me.ORIGEM='CARGA_INICIAL' then me.DATA ") 
		            .append(" else me.DATA_CRIACAO ") 
		        .append(" end asc, ")
		        .append(" case ") 
		            .append(" when me.ORIGEM='CARGA_INICIAL' then tipomovime1_.OPERACAO_ESTOQUE ") 
		        .append(" end asc ");
		        
		        
		        
		        sql.append(" ) AS T ");
//		        -- where 
		        if(isCount==false){
		        	sql.append(" group by t.produto_edicao_id ")
		        	.append(" having quantidade > 0 ");		    
		        	
		        }else{
		        }
		    
		return sql;
	}

	@Override
	public Integer countInventarioP7(Date time) {
		
		StringBuilder sql = getInvetarioSQL(Boolean.TRUE);
		        
		Query query = 
				this.getSession().createSQLQuery(
						sql.toString());
		
		Calendar c = GregorianCalendar.getInstance();
		c.setTime(time);
		
		String val = c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.DAY_OF_MONTH);
		query.setParameter("dataInventario",val);
		/*query.setParameter("dataInventario2",val);
		query.setParameter("codEmpresa",dist.getJuridica().getCnpj().substring(0,dist.getJuridica().getCnpj().indexOf("/")).replace(".",""));
		query.setParameter("codFilial",dist.getCodigo().toString());*/
		Object uniqueResult = query.uniqueResult();
		
		return ((BigInteger)uniqueResult).intValue();
	}
	
}


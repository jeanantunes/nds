package br.com.abril.nds.repository.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ConsultaFollowupChamadaoDTO;
import br.com.abril.nds.dto.filtro.FiltroFollowupChamadaoDTO;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.StatusEstoqueFinanceiro;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.FollowupChamadaoRepository;

@Repository
public class FollowupChamadaoRepositoryImpl  extends AbstractRepositoryModel<ConsultaFollowupChamadaoDTO,Long> implements FollowupChamadaoRepository {

	public FollowupChamadaoRepositoryImpl() {
		super(ConsultaFollowupChamadaoDTO.class);	
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ConsultaFollowupChamadaoDTO> obterConsignadosParaChamadao(FiltroFollowupChamadaoDTO filtro) {

		StringBuilder hql = new StringBuilder();
		
        hql.append(" SELECT CONSULTA_CHAMADAO.numeroCota as numeroCota, ");
		
		hql.append("        CONSULTA_CHAMADAO.nomeJornaleiro as nomeJornaleiro, ");
		
		hql.append("        CONSULTA_CHAMADAO.dataProgramadoChamadao as dataProgramadoChamadao, ");
		
		hql.append("        CONSULTA_CHAMADAO.dataHistoricoEdicao as dataHistoricoEdicao, ");
		
		hql.append("        SUM(CONSULTA_CHAMADAO.valorTotalConsignado) as valorTotalConsignado,  ");
		
		hql.append("        CONSULTA_CHAMADAO.qtdDiasSuspensao as qtdDiasSuspensao ");
		
		hql.append(" FROM ");
		
		hql.append(" ( ");
		
		hql.append(this.getSqlSelectChamadao());
		
		hql.append(this.getFromChamadao());

		hql.append(this.getSqlWhereChamadao(filtro));
		
		hql.append(" UNION ALL ");
		
		hql.append(this.getSqlSelectChamadao());
		
		hql.append(this.getFromChamadao());

		hql.append(this.getSqlWhereChamadaoCotaSuspensa(filtro));
		
		hql.append(" ) as CONSULTA_CHAMADAO ");
		
		hql.append(" GROUP BY CONSULTA_CHAMADAO.numeroCota, CONSULTA_CHAMADAO.nomeJornaleiro ");
		
		hql.append(" ORDER BY CONSULTA_CHAMADAO.dataProgramadoChamadao DESC ");
		
		Query query =  getSession().createSQLQuery(hql.toString()).addScalar("numeroCota", StandardBasicTypes.INTEGER)
															      .addScalar("nomeJornaleiro", StandardBasicTypes.STRING)
															      .addScalar("valorTotalConsignado", StandardBasicTypes.BIG_DECIMAL)
															      .addScalar("dataProgramadoChamadao", StandardBasicTypes.DATE)
															      .addScalar("dataHistoricoEdicao", StandardBasicTypes.DATE)
															      .addScalar("qtdDiasSuspensao", StandardBasicTypes.INTEGER);

		HashMap<String, Object> param = aplicarParametros(filtro);
		
		setParameters(query, param);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ConsultaFollowupChamadaoDTO.class));
		
		if(filtro.getPaginacao() != null) {
			
			if(filtro.getPaginacao().getQtdResultadosPorPagina() != null){ 
				
				query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			}	
		}
		
		return query.list();
	}
	
    private StringBuilder getSqlSelectChamadao(){
	   
	    StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT C.NUMERO_COTA as numeroCota, ");
		
		hql.append("       P.NOME as nomeJornaleiro, ");
		
		hql.append("       CE.DATA_RECOLHIMENTO as dataProgramadoChamadao, ");
		
		hql.append("       HSC.DATA_EDICAO as dataHistoricoEdicao, ");
		
		hql.append("       (MEC.PRECO_COM_DESCONTO * MEC.QTDE) as valorTotalConsignado,  ");
		
		hql.append("       DATEDIFF((SELECT DIST.DATA_OPERACAO FROM DISTRIBUIDOR DIST), HSC.DATA_INICIO_VALIDADE) as qtdDiasSuspensao ");
		
		return hql;
    }

	private StringBuilder getFromChamadao(){
		
        StringBuilder hql = new StringBuilder();	

		hql.append(" FROM CHAMADA_ENCALHE_COTA CEC ");
		
		hql.append("  INNER JOIN COTA C ON C.ID = CEC.COTA_ID ");
		
        hql.append("  INNER JOIN PESSOA P ON P.ID = C.PESSOA_ID ");
		
		hql.append("  INNER JOIN HISTORICO_SITUACAO_COTA HSC ON HSC.COTA_ID = C.ID ");
		
        hql.append("  INNER JOIN CHAMADA_ENCALHE CE ON CE.ID = CEC.CHAMADA_ENCALHE_ID ");
        
        hql.append("  INNER JOIN PRODUTO_EDICAO PE ON PE.ID = CE.PRODUTO_EDICAO_ID ");

		hql.append("  INNER JOIN CHAMADA_ENCALHE_LANCAMENTO CEL ON CEL.CHAMADA_ENCALHE_ID = CE.ID ");
		
		hql.append("  INNER JOIN LANCAMENTO L ON L.ID = CEL.LANCAMENTO_ID AND L.PRODUTO_EDICAO_ID = PE.ID ");
		
		hql.append("  INNER JOIN MOVIMENTO_ESTOQUE_COTA MEC ON MEC.LANCAMENTO_ID = L.ID ");
		
		hql.append("                                        AND MEC.MOVIMENTO_ESTOQUE_COTA_FURO_ID IS NULL ");
		
		hql.append("                                        AND MEC.COTA_ID = C.ID ");
		
		hql.append("                                        AND (MEC.STATUS_ESTOQUE_FINANCEIRO IS NULL OR MEC.STATUS_ESTOQUE_FINANCEIRO = :statusEstoqueFinanceiro) ");

		hql.append("  INNER JOIN TIPO_MOVIMENTO TM ON TM.ID = MEC.TIPO_MOVIMENTO_ID AND TM.GRUPO_MOVIMENTO_ESTOQUE = :grupoMovRecebimentoReparte ");

		return hql;
	}

    private String getSqlWhereChamadao(FiltroFollowupChamadaoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();	

		hql.append(" WHERE CE.TIPO_CHAMADA_ENCALHE = 'CHAMADAO' ");

		hql.append(" AND C.SITUACAO_CADASTRO <> 'SUSPENSO' " );
		
		hql.append(" AND L.STATUS IN (:statusLancamento) ");

		return hql.toString();
   }
	
   private String getSqlWhereChamadaoCotaSuspensa(FiltroFollowupChamadaoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();	

		hql.append(" WHERE CE.TIPO_CHAMADA_ENCALHE = 'CHAMADAO' ");
		
		hql.append(" AND HSC.NOVA_SITUACAO = 'SUSPENSO' " );
		
		hql.append(" AND C.SITUACAO_CADASTRO = 'SUSPENSO' ");
		
		hql.append(" AND L.STATUS IN (:statusLancamento) ");
		
		hql.append(" AND CE.DATA_RECOLHIMENTO >= (SELECT DIST.DATA_OPERACAO FROM DISTRIBUIDOR DIST) ");
		
		if(filtro.getQuantidadeDiasSuspenso() >= 0) {
			
			hql.append(" AND ( DATEDIFF((SELECT DIST.DATA_OPERACAO FROM DISTRIBUIDOR DIST), HSC.DATA_INICIO_VALIDADE) >= :diasSuspensaoDistribuidor ) ");
		}

		if(filtro.getValorConsignadoLimite() != null){
			
			hql.append(" HAVING (");
			
		    hql.append("          SUM(MEC.PRECO_COM_DESCONTO * (MEC.QTDE)) >= :valorConsignadoDistribuidor ");
		
		    hql.append("        ) ");
		}	

		return hql.toString();
   }

   private HashMap<String,Object> aplicarParametros(FiltroFollowupChamadaoDTO filtro) {
		HashMap<String,Object> param = new HashMap<String, Object>();
		
		param.put("statusLancamento", Arrays.asList(StatusLancamento.EXPEDIDO.name(), 
				                                    StatusLancamento.EM_BALANCEAMENTO_RECOLHIMENTO.name()));
		
		if(filtro.getValorConsignadoLimite() != null){
			
		   param.put("valorConsignadoDistribuidor", filtro.getValorConsignadoLimite());
		}
		
		if(filtro.getQuantidadeDiasSuspenso() >= 0) {
			
		    param.put("diasSuspensaoDistribuidor", filtro.getQuantidadeDiasSuspenso());
		}
		
		param.put("statusEstoqueFinanceiro", StatusEstoqueFinanceiro.FINANCEIRO_NAO_PROCESSADO.name());
		
		param.put("grupoMovRecebimentoReparte", GrupoMovimentoEstoque.RECEBIMENTO_REPARTE.name());		
		
		return param;
   }	
  
}

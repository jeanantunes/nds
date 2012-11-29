package br.com.abril.nds.repository.impl;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanConstructorResultTransformer;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ReparteFecharDiaDTO;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.ResumoReparteFecharDiaRepository;
import br.com.abril.nds.util.DateUtil;


@Repository
public class ResumoReparteFecharDiaRepositoryImpl  extends AbstractRepository implements ResumoReparteFecharDiaRepository {
    
    private static final Logger LOG = LoggerFactory.getLogger(ResumoReparteFecharDiaRepositoryImpl.class);
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ReparteFecharDiaDTO> obterValorReparte(Date dataOperacaoDistribuidor, boolean soma) {
		
		StringBuilder hql = new StringBuilder();
		
		if(soma){
			hql.append(" SELECT SUM(pe.precoVenda * l.reparte) as valorTotalReparte ");			
		}else{
			hql.append(" SELECT p.codigo as codigo,  ");
			hql.append(" p.nome as nomeProduto, ");
			hql.append(" pe.numeroEdicao as numeroEdicao ");
		}
		hql.append(" from Lancamento l ");
		hql.append(" JOIN l.produtoEdicao as pe ");
		hql.append(" JOIN pe.produto as p ");
		hql.append(" WHERE l.dataLancamentoPrevista = :dataOperacaoDistribuidor ");
		hql.append(" AND l.status IN ( :listaStatus )");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		List<StatusLancamento> listaStatus = new ArrayList<StatusLancamento>();
		
		listaStatus.add(StatusLancamento.CONFIRMADO);
		listaStatus.add(StatusLancamento.BALANCEADO);
		listaStatus.add(StatusLancamento.ESTUDO_FECHADO);
		
		query.setParameter("dataOperacaoDistribuidor", dataOperacaoDistribuidor);
		
		query.setParameterList("listaStatus", listaStatus);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ReparteFecharDiaDTO.class));
		
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ReparteFecharDiaDTO> obterValorDiferenca(Date dataOperacaoDistribuidor, boolean soma, String tipoDiferenca) {
		
		StringBuilder hql = new StringBuilder();
		
		if(soma){
			if(tipoDiferenca.equals("sobra")){
				hql.append(" SELECT SUM(pe.precoVenda * dif.qtde) as sobras ");				
			}else{
				hql.append(" SELECT SUM(pe.precoVenda * dif.qtde) as faltas ");
			}
		}else{
			hql.append(" SELECT p.codigo as codigo,  ");
			hql.append(" p.nome as nomeProduto, ");
			hql.append(" pe.numeroEdicao as numeroEdicao ");
		}
		
		
		hql.append(" FROM Diferenca as dif ");
		hql.append(" JOIN dif.produtoEdicao as pe ");
		hql.append(" JOIN pe.produto as p ");
		hql.append(" WHERE dif.dataMovimento = :dataOperacaoDistribuidor ");		
		hql.append(" AND dif.tipoDiferenca IN (:listaTipoDiferenca) ");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		List<TipoDiferenca> listaTipoDiferenca = new ArrayList<TipoDiferenca>();
		
		if(tipoDiferenca.equals("sobra")){
			listaTipoDiferenca.add(TipoDiferenca.SOBRA_DE);
			listaTipoDiferenca.add(TipoDiferenca.SOBRA_EM);			
		}else if(tipoDiferenca.equals("falta")){
			listaTipoDiferenca.add(TipoDiferenca.FALTA_DE);
			listaTipoDiferenca.add(TipoDiferenca.FALTA_EM);		
		}
		
		query.setParameter("dataOperacaoDistribuidor", dataOperacaoDistribuidor);
		
		query.setParameterList("listaTipoDiferenca", listaTipoDiferenca);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ReparteFecharDiaDTO.class));
		
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ReparteFecharDiaDTO> obterValorTransferencia(Date dataOperacaoDistribuidor, boolean soma) {
		 
		StringBuilder hql = new StringBuilder();
		

		if(soma){
			hql.append(" SELECT SUM(pe.precoVenda * me.qtde) as transferencias ");			
		}else{
			hql.append(" SELECT p.codigo as codigo,  ");
			hql.append(" p.nome as nomeProduto, ");
			hql.append(" pe.numeroEdicao as numeroEdicao ");
		}
		
		hql.append(" from MovimentoEstoque me ");
		hql.append(" JOIN me.tipoMovimento as tm ");
		hql.append(" JOIN me.produtoEdicao as pe ");
		hql.append(" JOIN pe.produto as p ");
		hql.append(" WHERE me.dataCriacao = :dataOperacaoDistribuidor ");
		hql.append(" AND tm.grupoMovimentoEstoque IN (:listaGrupoMovimentoEstoque) ");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		List<GrupoMovimentoEstoque> listaGrupoMovimentoEstoque = new ArrayList<GrupoMovimentoEstoque>();
		
		listaGrupoMovimentoEstoque.add(GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_LANCAMENTO);
		listaGrupoMovimentoEstoque.add(GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_PRODUTOS_DANIFICADOS);
		listaGrupoMovimentoEstoque.add(GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_RECOLHIMENTO);
		listaGrupoMovimentoEstoque.add(GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_SUPLEMENTAR);
		
		query.setParameter("dataOperacaoDistribuidor", dataOperacaoDistribuidor);
		
		query.setParameterList("listaGrupoMovimentoEstoque", listaGrupoMovimentoEstoque);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ReparteFecharDiaDTO.class));
		
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ReparteFecharDiaDTO> obterValorDistribuido(Date dataOperacaoDistribuidor , boolean soma) {
		 
		StringBuilder hql = new StringBuilder();
		

		if(soma){
			hql.append(" SELECT SUM(pe.precoVenda * me.qtde) as distribuidos ");			
		}else{
			hql.append(" SELECT p.codigo as codigo,  ");
			hql.append(" p.nome as nomeProduto, ");
			hql.append(" pe.numeroEdicao as numeroEdicao ");
		}
		hql.append(" from MovimentoEstoque me ");
		hql.append(" JOIN me.tipoMovimento as tm ");
		hql.append(" JOIN me.produtoEdicao as pe ");
		hql.append(" JOIN pe.produto as p ");
		hql.append(" WHERE me.dataCriacao = :dataOperacaoDistribuidor ");
		hql.append(" AND tm.grupoMovimentoEstoque = :grupoMovimento ");
		hql.append(" AND me.status = :status ");
		
		Query query = super.getSession().createQuery(hql.toString());
		
				
		query.setParameter("dataOperacaoDistribuidor", dataOperacaoDistribuidor);		
		query.setParameter("grupoMovimento", GrupoMovimentoEstoque.ENVIO_JORNALEIRO);		
		query.setParameter("status", StatusAprovacao.APROVADO);		
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ReparteFecharDiaDTO.class));
		
		
		return query.list();
	}
		
	@SuppressWarnings("unchecked")
    @Override
	public List<ReparteFecharDiaDTO> obterResumoReparte(Date data){
	    Objects.requireNonNull(data, "Data para consulta ao resumo do reparte não deve ser nula!");

        Date dataInicio = DateUtil.removerTimestamp(data);
        Date dataFim = DateUtil.adicionarDias(dataInicio, 1);
        
        String exemplaresDiferencaDe = "qtde * produtoEdicao.pacotePadrao";
        String exemplaresDiferencaEm = "qtde";
        
        String templateHqlDiferenca = new StringBuilder("(select sum(%s) from Diferenca diferenca ") 
        .append("where diferenca.dataMovimento = :data and diferenca.produtoEdicao.id = produtoEdicao.id and diferenca.tipoDiferenca = :%s) as %s ").toString();
        
        StringBuilder hql = new StringBuilder("select produto.codigo as codigo, ");
        hql.append("produto.nome as nomeProduto, ");
        hql.append("produtoEdicao.numeroEdicao as numeroEdicao, ");
        hql.append("produtoEdicao.precoVenda as precoVenda, ");
        hql.append("lancamento.reparte as qtdeReparte, ");
        hql.append(String.format(templateHqlDiferenca, exemplaresDiferencaDe, "tipoDiferencaSobraDe", "qtdeSobraDe")).append(",");
        hql.append(String.format(templateHqlDiferenca, exemplaresDiferencaEm, "tipoDiferencaSobraEm", "qtdeSobraEm")).append(",");
        hql.append(String.format(templateHqlDiferenca, exemplaresDiferencaDe, "tipoDiferencaFaltaDe", "qtdeFaltaDe")).append(",");
        hql.append(String.format(templateHqlDiferenca, exemplaresDiferencaEm, "tipoDiferencaFaltaEm", "qtdeFaltaEm")).append(",");
        
        hql.append("(select sum(movimentoEstoque.qtde) from MovimentoEstoque movimentoEstoque where movimentoEstoque.data = :data "); 
        hql.append("and movimentoEstoque.produtoEdicao.id = produtoEdicao.id and movimentoEstoque.status = :statusAprovado ");
        hql.append("and movimentoEstoque.tipoMovimento.grupoMovimentoEstoque = :grupoMovimentoEnvioJornaleiro ) as qtdeDistribuido, ");
        
        hql.append("(select sum(case when movimentoEstoque.tipoMovimento.grupoMovimentoEstoque = :grupoTransferenciaLancamentoEntrada ");
        hql.append("then movimentoEstoque.qtde else (movimentoEstoque.qtde * -1) end) from MovimentoEstoque movimentoEstoque where movimentoEstoque.data = :data "); 
        hql.append("and movimentoEstoque.produtoEdicao.id = produtoEdicao.id and movimentoEstoque.status = :statusAprovado ");
        hql.append("and movimentoEstoque.tipoMovimento.grupoMovimentoEstoque in (:grupoTransferenciaLancamentoEntrada, :grupoTransferenciaLancamentoSaida)) as qtdeTransferencia ");
     
        hql.append("from Expedicao expedicao ");
        hql.append("join expedicao.lancamentos lancamento ");
        hql.append("join lancamento.produtoEdicao produtoEdicao ");
        hql.append("join produtoEdicao.produto produto ");
        hql.append("where expedicao.dataExpedicao >= :dataInicio and expedicao.dataExpedicao < :dataFim ");
    
        Query query = getSession().createQuery(hql.toString());
        query.setParameter("data", dataInicio);
        query.setParameter("dataInicio", dataInicio);
        query.setParameter("dataFim", dataFim);
        query.setParameter("tipoDiferencaSobraDe", TipoDiferenca.SOBRA_DE);
        query.setParameter("tipoDiferencaSobraEm", TipoDiferenca.SOBRA_EM);
        query.setParameter("tipoDiferencaFaltaDe", TipoDiferenca.FALTA_DE);
        query.setParameter("tipoDiferencaFaltaEm", TipoDiferenca.FALTA_EM);
        query.setParameter("statusAprovado", StatusAprovacao.APROVADO);
        query.setParameter("grupoMovimentoEnvioJornaleiro", GrupoMovimentoEstoque.ENVIO_JORNALEIRO);
        query.setParameter("grupoTransferenciaLancamentoEntrada", GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_LANCAMENTO);
        query.setParameter("grupoTransferenciaLancamentoSaida", GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_LANCAMENTO);
        
        
        try {
            Constructor<ReparteFecharDiaDTO> constructor = ReparteFecharDiaDTO.class
                    .getConstructor(String.class, String.class, Long.class,
                            BigDecimal.class, BigInteger.class,
                            BigInteger.class, BigInteger.class,
                            BigInteger.class, BigInteger.class,
                            BigInteger.class, BigInteger.class);
            query.setResultTransformer(new AliasToBeanConstructorResultTransformer(constructor));
        } catch (NoSuchMethodException | SecurityException e) {
            String msg = "Erro definindo result transformer para classe: " + ReparteFecharDiaDTO.class.getName();
            LOG.error(msg, e);
            throw new RuntimeException(msg, e);
        } 
        return query.list();
	}


}

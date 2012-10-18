package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ConsultaConsignadoCotaDTO;
import br.com.abril.nds.dto.ContasApagarConsultaPorDistribuidorDTO;
import br.com.abril.nds.dto.filtro.FiltroContasAPagarDTO;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.ContasAPagarRepository;

@Repository
public class ContasAPagarRepositoryImpl extends AbstractRepository implements ContasAPagarRepository{

	@SuppressWarnings("unchecked")
	@Override
	public List<Date> buscarDatasLancamentoContasAPagar(FiltroContasAPagarDTO filtro) {
		
		Query query = this.getSession().createQuery(
				this.montarQueryPorDistribuidor(true, filtro));
		
		this.setarParametrosQueryporDistribuidor(query, filtro);
		
		return query.list();
	}	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ContasApagarConsultaPorDistribuidorDTO> pesquisarPorDistribuidor(FiltroContasAPagarDTO filtro) {
		
		Query query = this.getSession().createQuery(
				this.montarQueryPorDistribuidor(false, filtro));
		
		this.setarParametrosQueryporDistribuidor(query, filtro);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				ConsultaConsignadoCotaDTO.class));
		
		return query.list();
	}
	
	private String montarQueryPorDistribuidor(boolean buscarDatas, FiltroContasAPagarDTO filtro){
		
		StringBuilder hql = new StringBuilder();
		
		if (buscarDatas){
			
			hql.append("select (l.dataRecolhimentoDistribuidor) ");
		} else {
			
			hql.append("select l.dataRecolhimentoDistribuidor as data, ")
			   .append(" sum(l.produtoEdicao.precoVenda * l.reparte) as consignado ")
			   
			   //pesquisaPorDistribuidorValorPorGrupoMovimento
			   .append(",(select sum(m.qtde * m.produtoEdicao.precoVenda) ")
			   .append(" from MovimentoEstoque m ")
			   .append(" where m.data = l.dataRecolhimentoDistribuidor ")
			   .append(" and m.qtde is not null ")
			   .append(" and m.produtoEdicao.precoVenda is not null")
			   .append(" and m.tipoMovimento.grupoMovimentoEstoque in (:movimentosSuplementar)) as suplementacao ")
			   
			   //TODO fazer o mesmo para movimentos de grupo de estoque suplementar de saida
			   
			   //pesquisaPorDistribuidorFaltasSobras
			   .append(",(select sum(l3.diferenca.qtde * l3.diferenca.produtoEdicao.precoVenda) ")
			   .append(" from LancamentoDiferenca l3 ")
			   .append(" where l3.dataProcessamento = l.dataRecolhimentoDistribuidor ")
			   .append(" and l3.diferenca.qtde is not null ")
			   .append(" and l3.diferenca.produtoEdicao.precoVenda is not null")
			   .append(" and (l3.diferenca.tipoDiferenca = :tipoDiferencaEm or l3.diferenca.tipoDiferenca = :tipoDiferencaDe)")
			   .append(" group by l3.diferenca.tipoDiferenca) as faltasSobras ")
			   
			   //TODO fazer o mesmo para os outros movimentos de falta e sobra
			   
			   //pesquisaPorDistribuidorPerdasGanhos
			   .append(",(select sum(l2.diferenca.qtde * l2.diferenca.produtoEdicao.precoVenda) ")
			   .append(" from LancamentoDiferenca l2 ")
			   .append(" where l2.dataProcessamento = l.dataRecolhimentoDistribuidor ")
			   .append(" and l2.diferenca.qtde is not null ")
			   .append(" and l2.diferenca.produtoEdicao.precoVenda is not null ")
			   .append(" and l2.status = :statusPerda ")
			   .append(" group by l2.diferenca.tipoDiferenca) as debitoCredito ")
			   
			   //TODO fazer o mesmo para os outros status de ganho
			   
			   .append("");
		}
		
		hql.append(" from Lancamento l ")
		   .append(" join l.produtoEdicao.produto.fornecedores f ")
		   .append(" where l.dataLancamentoDistribuidor ");
		
		if (buscarDatas){
			
			hql.append(" between :inicio and :fim ");
		} else {
			
			hql.append(" = :dataLancamento ");
		}
		
		hql.append(" and l.reparte is not null ")
		   .append(" and l.produtoEdicao.precoVenda is not null ")
		   .append(" and l.status = :statusLancamento ");
		
		if (filtro.getIdsFornecedores() != null && !filtro.getIdsFornecedores().isEmpty()){
			
			hql.append(" and f.id in (:idsFornecedores) ");
		}
		
		hql.append(" group by l.dataRecolhimentoDistribuidor ")
		   .append(" order by l.dataRecolhimentoDistribuidor asc ");
		
		return hql.toString();
	}
	
	private void setarParametrosQueryporDistribuidor(Query query, FiltroContasAPagarDTO filtro){
			
		query.setParameter("inicio", filtro.getDataDe());
		query.setParameter("fim", filtro.getDataAte());
		
//		List<GrupoMovimentoEstoque> movimentosSuplementar = new ArrayList<GrupoMovimentoEstoque>();
//		movimentosSuplementar.add(GrupoMovimentoEstoque.SUPLEMENTAR_COTA_AUSENTE);
//		movimentosSuplementar.add(GrupoMovimentoEstoque.SUPLEMENTAR_ENVIO_ENCALHE_ANTERIOR_PROGRAMACAO);
//		movimentosSuplementar.add(GrupoMovimentoEstoque.ESTORNO_VENDA_ENCALHE_SUPLEMENTAR);
//		movimentosSuplementar.add(GrupoMovimentoEstoque.ENTRADA_SUPLEMENTAR_ENVIO_REPARTE);
//		movimentosSuplementar.add(GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_SUPLEMENTAR);
//		query.setParameterList("movimentosSuplementar", movimentosSuplementar);
//		//TODO fazer o mesmo para movimentos de grupo de estoque suplementar de saida
//		
//		query.setParameter("tipoDiferencaEm", TipoDiferenca.FALTA_EM);
//		query.setParameter("tipoDiferencaDe", TipoDiferenca.FALTA_DE);
//		//TODO fazer o mesmo para os outros movimentos de falta e sobra
//		
//		query.setParameter("statusPerda", StatusAprovacao.PERDA);
		//TODO fazer o mesmo para os outros status de ganho
				
		query.setParameter("statusLancamento", StatusLancamento.CONFIRMADO);
		
		if (filtro.getIdsFornecedores() != null && !filtro.getIdsFornecedores().isEmpty()){
			
			query.setParameterList("idsFornecedores", filtro.getIdsFornecedores());
		}
	}
	
	@Override
	public BigDecimal pesquisaPorDistribuidorValorPorGrupoMovimento(Date dataMovimento, 
			List<GrupoMovimentoEstoque> movimentosSuplementar){
		
		StringBuilder hql = new StringBuilder("select sum(m.qtde * m.produtoEdicao.precoVenda) ");
		hql.append(" from MovimentoEstoque m ")
		   .append(" where m.data = :dataMovimento ")
		   .append(" and m.qtde is not null ")
		   .append(" and m.produtoEdicao.precoVenda is not null")
		   .append(" and m.tipoMovimento.grupoMovimentoEstoque in (:movimentosSuplementar) ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("dataMovimento", dataMovimento);
		
		query.setParameterList("movimentosSuplementar", movimentosSuplementar);
		
		return (BigDecimal) query.uniqueResult();
	}
	
//	@Override
//	public BigDecimal pesquisaPorDistribuidorSuplementacaoEntrada(Date dataMovimento){
//		
//		StringBuilder hql = new StringBuilder("select sum(m.qtde * m.produtoEdicao.precoVenda) ");
//		hql.append(" from MovimentoEstoque m ")
//		   .append(" where m.data = :dataMovimento ")
//		   .append(" and m.qtde is not null ")
//		   .append(" and m.produtoEdicao.precoVenda is not null")
//		   .append(" and m.tipoMovimento.grupoMovimentoEstoque in (:movimentosSuplementar) ");
//		
//		Query query = this.getSession().createQuery(hql.toString());
//		query.setParameter("dataMovimento", dataMovimento);
//		
//		List<GrupoMovimentoEstoque> movimentosSuplementar = new ArrayList<GrupoMovimentoEstoque>();
//		movimentosSuplementar.add(GrupoMovimentoEstoque.SUPLEMENTAR_COTA_AUSENTE);
//		movimentosSuplementar.add(GrupoMovimentoEstoque.SUPLEMENTAR_ENVIO_ENCALHE_ANTERIOR_PROGRAMACAO);
//		movimentosSuplementar.add(GrupoMovimentoEstoque.ESTORNO_VENDA_ENCALHE_SUPLEMENTAR);
//		movimentosSuplementar.add(GrupoMovimentoEstoque.ENTRADA_SUPLEMENTAR_ENVIO_REPARTE);
//		movimentosSuplementar.add(GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_SUPLEMENTAR);
//		
//		query.setParameterList("movimentosSuplementar", movimentosSuplementar);
//		
//		return (BigDecimal) query.uniqueResult();
//	}
//	
//	@Override
//	public BigDecimal pesquisaPorDistribuidorSuplementacaoSaida(Date dataMovimento){
//		
//		StringBuilder hql = new StringBuilder("select sum(m.qtde * m.produtoEdicao.precoVenda) ");
//		hql.append(" from MovimentoEstoque m ")
//		   .append(" where m.data = :dataMovimento ")
//		   .append(" and m.qtde is not null ")
//		   .append(" and m.produtoEdicao.precoVenda is not null")
//		   .append(" and m.tipoMovimento.grupoMovimentoEstoque in (:movimentosSuplementar) ");
//		
//		Query query = this.getSession().createQuery(hql.toString());
//		query.setParameter("dataMovimento", dataMovimento);
//		
//		List<GrupoMovimentoEstoque> movimentosSuplementar = new ArrayList<GrupoMovimentoEstoque>();
//		movimentosSuplementar.add(GrupoMovimentoEstoque.REPARTE_COTA_AUSENTE);
//		movimentosSuplementar.add(GrupoMovimentoEstoque.VENDA_ENCALHE_SUPLEMENTAR);
//		movimentosSuplementar.add(GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_SUPLEMENTAR);
//		
//		query.setParameterList("movimentosSuplementar", movimentosSuplementar);
//		
//		return (BigDecimal) query.uniqueResult();
//	}
	
	@Override
	public BigDecimal pesquisaPorDistribuidorFaltasSobras(Date dataMovimento, TipoDiferenca tipoDiferenca){
		
		StringBuilder hql = new StringBuilder("select sum(l.diferenca.qtde * l.diferenca.produtoEdicao.precoVenda) ");
		hql.append(" from LancamentoDiferenca l ")
		   .append(" where l.dataProcessamento = :dataMovimento ")
		   .append(" and l.diferenca.qtde is not null ")
		   .append(" and l.diferenca.produtoEdicao.precoVenda is not null")
		   .append(" and (l.diferenca.tipoDiferenca = :tipoDiferencaEm or l.diferenca.tipoDiferenca = :tipoDiferencaDe)")
		   .append(" group by l.diferenca.tipoDiferenca ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("dataMovimento", dataMovimento);
		
		if (TipoDiferenca.FALTA_DE.equals(tipoDiferenca) || TipoDiferenca.FALTA_EM.equals(tipoDiferenca)){
			
			query.setParameter("tipoDiferencaEm", TipoDiferenca.FALTA_EM);
			query.setParameter("tipoDiferencaDe", TipoDiferenca.FALTA_DE);
		} else {
			
			query.setParameter("tipoDiferencaEm", TipoDiferenca.SOBRA_EM);
			query.setParameter("tipoDiferencaDe", TipoDiferenca.SOBRA_DE);
		}
		
		return (BigDecimal) query.uniqueResult();
	}
	
	@Override
	public BigDecimal pesquisaPorDistribuidorPerdasGanhos(Date dataMovimento, StatusAprovacao status){
		
		StringBuilder hql = new StringBuilder("select sum(l.diferenca.qtde * l.diferenca.produtoEdicao.precoVenda) ");
		hql.append(" from LancamentoDiferenca l ")
		   .append(" where l.dataProcessamento = :dataMovimento ")
		   .append(" and l.diferenca.qtde is not null ")
		   .append(" and l.diferenca.produtoEdicao.precoVenda is not null")
		   .append(" and l.status = :status")
		   .append(" group by l.diferenca.tipoDiferenca ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("dataMovimento", dataMovimento);
		query.setParameter("status", status);
		
		
		return (BigDecimal) query.uniqueResult();
	}
}
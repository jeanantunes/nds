package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.EncalheFecharDiaDTO;
import br.com.abril.nds.model.planejamento.TipoChamadaEncalhe;
import br.com.abril.nds.repository.ResumoEncalheFecharDiaRepository;

@Repository
public class ResumoEncalheFecharDiaRepositoryImpl extends AbstractRepository implements
		ResumoEncalheFecharDiaRepository {

	@Override
	public BigDecimal obterValorEncalheFisico(Date dataOperacaoDistribuidor, boolean juramentada) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT (ce.qtde * pe.precoVenda) ");			
		hql.append(" from ConferenciaEncalhe AS ce ");		
		hql.append(" JOIN ce.produtoEdicao as pe ");		
		hql.append(" WHERE ce.data = :dataOperacaoDistribuidor ");
		
		if(juramentada){
			hql.append(" AND ce.juramentada = :juramentada ");
		}
		
		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("dataOperacaoDistribuidor", dataOperacaoDistribuidor);
		
		if(juramentada){
			query.setParameter("juramentada", juramentada);
		}
		
		BigDecimal total =  (BigDecimal) query.uniqueResult();
		
		return total != null ? total : BigDecimal.ZERO ;
	}

	@Override
	public BigDecimal obterValorEncalheLogico(Date dataOperacaoDistribuidor) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT (cec.qtdePrevista * pe.precoVenda) ");			
		hql.append(" from ChamadaEncalheCota AS cec ");		
		hql.append(" JOIN cec.chamadaEncalhe AS ce ");		
		hql.append(" JOIN ce.produtoEdicao as pe ");		
		hql.append(" WHERE ce.dataRecolhimento = :dataOperacaoDistribuidor ");
		hql.append(" AND ce.tipoChamadaEncalhe = :tipoChamadaEncalhe ");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("dataOperacaoDistribuidor", dataOperacaoDistribuidor);		
		query.setParameter("tipoChamadaEncalhe", TipoChamadaEncalhe.MATRIZ_RECOLHIMENTO);		
		
		BigDecimal total =  (BigDecimal) query.uniqueResult();
		
		return total != null ? total : BigDecimal.ZERO ;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EncalheFecharDiaDTO> obterDadosGridEncalhe(Date dataOperacaoDistribuidor) {

		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT p.codigo as codigo,  ");
		hql.append(" p.nome as nomeProduto, ");
		hql.append(" pe.numeroEdicao as numeroEdicao, ");
		hql.append(" pe.precoVenda as precoVenda, ");
		hql.append(" COUNT(*) as qtde ");
		hql.append(" from ConferenciaEncalhe AS ce ");		
		hql.append(" JOIN ce.produtoEdicao as pe ");
		hql.append(" JOIN pe.produto as p ");
		hql.append(" WHERE ce.data = :dataOperacaoDistribuidor ");
		hql.append(" GROUP BY p.codigo, p.nome, pe.numeroEdicao, pe.precoVenda ");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("dataOperacaoDistribuidor", dataOperacaoDistribuidor);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(EncalheFecharDiaDTO.class));
		
		List<EncalheFecharDiaDTO> listaDeEncalheFisico = query.list();
		

		StringBuilder hqlJuramentado = new StringBuilder();
		
		hqlJuramentado.append(" SELECT p.codigo as codigo,  ");
		hqlJuramentado.append(" p.nome as nomeProduto, ");
		hqlJuramentado.append(" pe.numeroEdicao as numeroEdicao, ");
		hqlJuramentado.append(" pe.precoVenda as precoVenda, ");
		hqlJuramentado.append(" COUNT(*) as qtde ");
		hqlJuramentado.append(" from ConferenciaEncalhe AS ce ");		
		hqlJuramentado.append(" JOIN ce.produtoEdicao as pe ");
		hqlJuramentado.append(" JOIN pe.produto as p ");
		hqlJuramentado.append(" WHERE ce.data = :dataOperacaoDistribuidor ");
		hqlJuramentado.append(" AND ce.juramentada = :juramentada ");
		hqlJuramentado.append(" GROUP BY p.codigo, p.nome, pe.numeroEdicao, pe.precoVenda ");
		
		Query queryJuramentado = super.getSession().createQuery(hqlJuramentado.toString());
		
		queryJuramentado.setParameter("dataOperacaoDistribuidor", dataOperacaoDistribuidor);
		queryJuramentado.setParameter("juramentada", true);
		
		queryJuramentado.setResultTransformer(new AliasToBeanResultTransformer(EncalheFecharDiaDTO.class));
		
		List<EncalheFecharDiaDTO> listaDeEncalheJuramentado = queryJuramentado.list();
		
		StringBuilder hqlLogico = new StringBuilder();
		
		hqlLogico.append(" SELECT p.codigo as codigo,  ");
		hqlLogico.append(" p.nome as nomeProduto, ");
		hqlLogico.append(" pe.numeroEdicao as numeroEdicao, ");
		hqlLogico.append(" pe.precoVenda as precoVenda, ");
		hqlLogico.append(" COUNT(*) as qtde ");		
		hqlLogico.append(" from ChamadaEncalheCota AS cec ");		
		hqlLogico.append(" JOIN cec.chamadaEncalhe AS ce ");		
		hqlLogico.append(" JOIN ce.produtoEdicao as pe ");		
		hqlLogico.append(" WHERE ce.dataRecolhimento = :dataOperacaoDistribuidor ");
		hqlLogico.append(" AND ce.tipoChamadaEncalhe = :tipoChamadaEncalhe ");
		
		Query queryLogico = super.getSession().createQuery(hql.toString());
		
		queryLogico.setParameter("dataOperacaoDistribuidor", dataOperacaoDistribuidor);		
		queryLogico.setParameter("tipoChamadaEncalhe", TipoChamadaEncalhe.MATRIZ_RECOLHIMENTO);		
		
		List<EncalheFecharDiaDTO> listaDeEncalheLogico = queryLogico.list();
		
		return obterListaFinalGridEncalhe(listaDeEncalheFisico,
				listaDeEncalheJuramentado, listaDeEncalheLogico);
		
	}

	private List<EncalheFecharDiaDTO> obterListaFinalGridEncalhe(List<EncalheFecharDiaDTO> listaDeEncalheFisico, List<EncalheFecharDiaDTO> listaDeEncalheJuramentado,
			List<EncalheFecharDiaDTO> listaDeEncalheLogico) {
		
		Set<EncalheFecharDiaDTO> listaFinal = new HashSet<>();
		
		listaFinal.addAll(listaDeEncalheJuramentado);
		
		listaFinal.addAll(listaDeEncalheFisico);
		
		listaFinal.addAll(listaDeEncalheLogico);
		
		List<EncalheFecharDiaDTO> goma = new ArrayList<EncalheFecharDiaDTO>(listaFinal);
		
		return goma;
	}

}

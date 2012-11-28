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
import br.com.abril.nds.dto.VendaFechamentoDiaDTO;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.FormaComercializacao;
import br.com.abril.nds.model.estoque.TipoVendaEncalhe;
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
		hql.append(" COALESCE(COUNT(*),0) as qtde ");
		
		hql.append(" FROM ConferenciaEncalhe AS ce ");		
		hql.append(" JOIN ce.produtoEdicao as pe ");
		hql.append(" JOIN pe.produto as p ");
		
		hql.append(" WHERE ce.data = :dataOperacaoDistribuidor ");
		hql.append(" GROUP BY p.codigo, p.nome, pe.numeroEdicao, pe.precoVenda ");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("dataOperacaoDistribuidor", dataOperacaoDistribuidor);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(EncalheFecharDiaDTO.class));
		
		List<EncalheFecharDiaDTO> listaFinal = query.list();
		
		hql = new StringBuilder();
		
		hql.append(" SELECT p.codigo as codigo,  ");
		hql.append(" p.nome as nomeProduto, ");
		hql.append(" pe.numeroEdicao as numeroEdicao, ");
		hql.append(" pe.precoVenda as precoVenda, ");
		hql.append(" COALESCE(COUNT(*),0) as qtde ");
		
		hql.append(" FROM ConferenciaEncalhe AS ce ");		
		hql.append(" JOIN ce.produtoEdicao as pe ");
		hql.append(" JOIN pe.produto as p ");
		
		hql.append(" WHERE ce.data = :dataOperacaoDistribuidor ");
		hql.append(" AND ce.juramentada = :juramentada ");
		hql.append(" GROUP BY p.codigo, p.nome, pe.numeroEdicao, pe.precoVenda ");
		
		query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("dataOperacaoDistribuidor", dataOperacaoDistribuidor);
		query.setParameter("juramentada", true);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(EncalheFecharDiaDTO.class));
		
		List<EncalheFecharDiaDTO> listaDeEncalheJuramentado = query.list();
		
		listaFinal = obterListaFinalParaGridEncalhe(listaFinal, listaDeEncalheJuramentado, "quantidade");
		
		hql = new StringBuilder();

		hql.append(" SELECT p.codigo as codigo,  ");
		hql.append(" p.nome as nomeProduto, ");
		hql.append(" pe.numeroEdicao as numeroEdicao, ");
		hql.append(" pe.precoVenda as precoVenda, ");
		hql.append(" COUNT(*) as qtde ");
		
		hql.append(" from ChamadaEncalheCota AS cec ");		
		hql.append(" JOIN cec.chamadaEncalhe AS ce ");		
		hql.append(" JOIN ce.produtoEdicao as pe ");
		hql.append(" JOIN pe.produto as p ");
		
		hql.append(" WHERE ce.dataRecolhimento = :dataOperacaoDistribuidor ");
		hql.append(" AND ce.tipoChamadaEncalhe in (:listaTipoChamadaEncalhe) ");
		hql.append(" GROUP BY p.codigo, p.nome, pe.numeroEdicao, pe.precoVenda ");
		
		query = super.getSession().createQuery(hql.toString());
		
		List<TipoChamadaEncalhe> listaTipoChamadaEncalhe = new ArrayList<TipoChamadaEncalhe>();
		
		listaTipoChamadaEncalhe.add(TipoChamadaEncalhe.MATRIZ_RECOLHIMENTO);
		listaTipoChamadaEncalhe.add(TipoChamadaEncalhe.ANTECIPADA);
		listaTipoChamadaEncalhe.add(TipoChamadaEncalhe.CHAMADAO);
		
		query.setParameter("dataOperacaoDistribuidor", dataOperacaoDistribuidor);		
		query.setParameterList("listaTipoChamadaEncalhe", listaTipoChamadaEncalhe);	
		
		query.setResultTransformer(new AliasToBeanResultTransformer(EncalheFecharDiaDTO.class));
		
		List<EncalheFecharDiaDTO> listaDeEncalheLogico = query.list();
		
		listaFinal = obterListaFinalParaGridEncalhe(listaFinal, listaDeEncalheLogico, "quantidade");
		
		//FIM DAS SUB-LISTAS		
		
		hql = new StringBuilder();
		
		hql.append(" SELECT p.codigo as codigo,  ");
		hql.append(" p.nome as nomeProduto, ");
		hql.append(" pe.numeroEdicao as numeroEdicao, ");
		hql.append(" pe.precoVenda as precoVenda, ");
		hql.append(" count(*) as qtde ");		
		
		hql.append(" FROM VendaProduto as ve ");		 
		hql.append(" JOIN ve.produtoEdicao as pe ");					
		hql.append(" JOIN pe.produto as p ");					
		hql.append(" WHERE ve.dataVenda = :dataOperacao ");
		hql.append(" AND ve.tipoComercializacaoVenda = :tipoComercializacaoVenda ");		
		hql.append(" AND ve.tipoVenda = :suplementar");

		query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("dataOperacao", dataOperacaoDistribuidor);
		
		query.setParameter("suplementar", TipoVendaEncalhe.ENCALHE);
		
		query.setParameter("tipoComercializacaoVenda", FormaComercializacao.CONTA_FIRME);		
		
		query.setResultTransformer(new AliasToBeanResultTransformer(EncalheFecharDiaDTO.class));
		
		List<EncalheFecharDiaDTO> listaDeVendaEncalhe = query.list();
		
		listaFinal = obterListaFinalParaGridEncalhe(listaFinal, listaDeVendaEncalhe, "vendaEncalhe");
		
		return completarListaComItemDiferenca(listaFinal);
		
	}

	private List<EncalheFecharDiaDTO> obterListaFinalParaGridEncalhe(List<EncalheFecharDiaDTO> listaDeQuantidades, 
				List<EncalheFecharDiaDTO> listaParaComparacao, String tipoLista){
	
		Set<EncalheFecharDiaDTO> listaRetorno = new HashSet<EncalheFecharDiaDTO>(listaDeQuantidades);
		
		for(EncalheFecharDiaDTO dtoPrincipal: listaDeQuantidades){
			EncalheFecharDiaDTO objetoFinal = dtoPrincipal;
			for(EncalheFecharDiaDTO comp: listaParaComparacao){
				if(objetoFinal.getCodigo().equals(comp.getCodigo()) && objetoFinal.getNumeroEdicao().equals(comp.getNumeroEdicao())){
					if(tipoLista.equals("quantidade")){
						objetoFinal.setQtde(objetoFinal.getQtde() + 1);
					}
					if(tipoLista.equals("vendaEncalhe")){
						objetoFinal.setQtdeVendaEncalhe(comp.getQtde().longValue());							
					}
					listaRetorno.add(objetoFinal);
				}else{
					listaRetorno.add(comp);								
				}
			}				
		}
		
		return new ArrayList<EncalheFecharDiaDTO>(listaRetorno);
	}
	
	private List<EncalheFecharDiaDTO> completarListaComItemDiferenca(List<EncalheFecharDiaDTO> listaFinal) {
		
		Set<EncalheFecharDiaDTO> listaRetorno = new HashSet<EncalheFecharDiaDTO>(listaFinal);
		for(EncalheFecharDiaDTO dto : listaFinal){
			EncalheFecharDiaDTO objetoFinal = dto;
			if(dto.getQtde() != null && dto.getQtdeVendaEncalhe() != null){
				long diferenca = dto.getQtde() - dto.getQtdeVendaEncalhe();
				objetoFinal.setDiferencas(diferenca);				
			}else{
				objetoFinal.setDiferencas(null);
			}
			listaRetorno.add(objetoFinal);
		}
		return formatarDadosNulos(new ArrayList<EncalheFecharDiaDTO>(listaRetorno));
	}
	
	private List<EncalheFecharDiaDTO> formatarDadosNulos(ArrayList<EncalheFecharDiaDTO> listaFinal) {
		 
		Set<EncalheFecharDiaDTO> listaRetorno = new HashSet<EncalheFecharDiaDTO>(listaFinal);
		for(EncalheFecharDiaDTO dto : listaFinal){
			EncalheFecharDiaDTO objetoFinal = dto;
			if(dto.getQtde() == null){
				objetoFinal.setQtdeFormatado("");
			}else{
				objetoFinal.setQtdeFormatado(dto.getQtde().toString());
			}
			if(dto.getQtdeVendaEncalhe() == null){
				objetoFinal.setVendaEncalheFormatado("");
			}else{
				objetoFinal.setVendaEncalheFormatado(dto.getQtdeVendaEncalhe().toString());				
			}
			if(dto.getDiferencas() == null){
				objetoFinal.setDifencaFormatado("");
			}else{
				objetoFinal.setDifencaFormatado(dto.getDiferencas().toString());								
			}
			
			listaRetorno.add(objetoFinal);
		}
		
		return new ArrayList<EncalheFecharDiaDTO>(listaRetorno);
	}

	@Override
	public BigDecimal obterValorFaltasOuSobras(Date dataOperacao, StatusAprovacao status) {

		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT (me.qtde * pe.precoVenda) ");			
		hql.append(" FROM LancamentoDiferenca AS ld ");		
		hql.append(" JOIN ld.movimentoEstoque as me ");	
		hql.append(" JOIN me.produtoEdicao as pe ");	
		hql.append(" WHERE me.data = :dataOperacaoDistribuidor ");
		hql.append(" AND ld.status = :status ");			
		
		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("dataOperacaoDistribuidor", dataOperacao);
		
		if(status.equals(StatusAprovacao.PERDA)){
			query.setParameter("status", StatusAprovacao.PERDA);			
		}else{
			query.setParameter("status", StatusAprovacao.GANHO);
		}
		
		BigDecimal total =  (BigDecimal) query.uniqueResult();
		
		return total != null ? total : BigDecimal.ZERO ;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VendaFechamentoDiaDTO> obterDadosVendaEncalhe(Date dataOperacao) {
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT p.codigo as codigo,  ");
		hql.append(" p.nome as nomeProduto, ");
		hql.append(" pe.numeroEdicao as numeroEdicao, ");
		hql.append(" ve.qntProduto as qtde, ");
		hql.append(" (ve.qntProduto * pe.precoVenda) as valor, ");
		hql.append(" ve.dataVenda as dataRecolhimento ");
		
		hql.append(" FROM VendaProduto as ve ");		 
		hql.append(" JOIN ve.produtoEdicao as pe ");					
		hql.append(" JOIN pe.produto as p ");					
		hql.append(" WHERE ve.dataVenda = :dataOperacao ");
		hql.append(" AND ve.tipoComercializacaoVenda = :tipoComercializacaoVenda ");
		
		hql.append(" AND ve.tipoVenda = :suplementar");

		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("dataOperacao", dataOperacao);
		
		query.setParameter("suplementar", TipoVendaEncalhe.ENCALHE);
		
		query.setParameter("tipoComercializacaoVenda", FormaComercializacao.CONTA_FIRME);		
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				VendaFechamentoDiaDTO.class));
		
		return query.list();
	}

	@Override
	public BigDecimal obterValorVendaEncalhe(Date dataOperacao) {
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT   ");
		hql.append(" SUM(ve.qntProduto * pe.precoVenda)");
		hql.append(" FROM VendaProduto as ve ");		 
		hql.append(" JOIN ve.produtoEdicao as pe ");					
		hql.append(" JOIN pe.produto as p ");					
		hql.append(" WHERE ve.dataVenda = :dataOperacao ");
		hql.append(" AND ve.tipoComercializacaoVenda = :tipoComercializacaoVenda ");		
		hql.append(" AND ve.tipoVenda = :suplementar");

		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("dataOperacao", dataOperacao);
		
		query.setParameter("suplementar", TipoVendaEncalhe.ENCALHE);
		
		query.setParameter("tipoComercializacaoVenda", FormaComercializacao.CONTA_FIRME);	
		
		BigDecimal total =  (BigDecimal) query.uniqueResult();
		
		return total != null ? total : BigDecimal.ZERO ;
	}

}

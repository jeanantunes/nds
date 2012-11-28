package br.com.abril.nds.repository.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ReparteFecharDiaDTO;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.ResumoReparteFecharDiaRepository;

@Repository
public class ResumoReparteFecharDiaRepositoryImpl  extends AbstractRepository implements ResumoReparteFecharDiaRepository {
	
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
	public List<ReparteFecharDiaDTO> obterResumoReparte(Date dataOperacaoDistribuidor ){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT p.codigo as codigo,  ");
		hql.append(" p.nome as nomeProduto, ");
		hql.append(" pe.numeroEdicao as numeroEdicao, ");
		hql.append(" pe.precoVenda as precoVenda, ");
		hql.append(" count(*) as qtdReparte ");
		
		hql.append(" from Lancamento l ");
		hql.append(" JOIN l.produtoEdicao as pe ");
		hql.append(" JOIN pe.produto as p ");
		hql.append(" WHERE l.dataLancamentoPrevista = :dataOperacaoDistribuidor ");
		hql.append(" AND l.status IN (:listaStatus)");
		hql.append(" GROUP BY p.codigo, p.nome, pe.numeroEdicao, pe.precoVenda");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		List<StatusLancamento> listaStatus = new ArrayList<StatusLancamento>();
		
		listaStatus.add(StatusLancamento.CONFIRMADO);
		listaStatus.add(StatusLancamento.BALANCEADO);
		listaStatus.add(StatusLancamento.ESTUDO_FECHADO);
		
		query.setParameter("dataOperacaoDistribuidor", dataOperacaoDistribuidor);
		
		query.setParameterList("listaStatus", listaStatus);		
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ReparteFecharDiaDTO.class));
		
		List<ReparteFecharDiaDTO> listaFinal = query.list();
		
		////////////////////////////////////////////////////////////
		
		hql = new StringBuilder();
		
		hql.append(" SELECT p.codigo as codigo,  ");
		hql.append(" p.nome as nomeProduto, ");
		hql.append(" pe.numeroEdicao as numeroEdicao, ");
		hql.append(" pe.precoVenda as precoVenda, ");
		hql.append(" count(*) as qtdSobras ");
		
		hql.append(" FROM Diferenca as dif ");
		hql.append(" JOIN dif.produtoEdicao as pe ");
		hql.append(" JOIN pe.produto as p ");
		hql.append(" WHERE dif.dataMovimento = :dataOperacaoDistribuidor ");		
		hql.append(" AND dif.tipoDiferenca IN (:listaSobras) ");
		hql.append(" GROUP BY p.codigo, p.nome, pe.numeroEdicao, pe.precoVenda");
		
		query = super.getSession().createQuery(hql.toString());
		
		List<TipoDiferenca> listaDeSobras = new ArrayList<TipoDiferenca>();
		
		listaDeSobras.add(TipoDiferenca.SOBRA_DE);
		listaDeSobras.add(TipoDiferenca.SOBRA_EM);
		
		query.setParameter("dataOperacaoDistribuidor", dataOperacaoDistribuidor);
		
		query.setParameterList("listaSobras", listaDeSobras);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ReparteFecharDiaDTO.class));
		
		List<ReparteFecharDiaDTO> listaFinalDeSobras = query.list();
		
		listaFinal =  obterListaFinalParaGridReparte(listaFinal, listaFinalDeSobras, "sobras");
		
		//FINAL DE SOBRAS
		
		hql = new StringBuilder();
		
		hql.append(" SELECT p.codigo as codigo,  ");
		hql.append(" p.nome as nomeProduto, ");
		hql.append(" pe.numeroEdicao as numeroEdicao, ");
		hql.append(" pe.precoVenda as precoVenda, ");
		hql.append(" count(*) as qtdFaltas ");
		
		hql.append(" FROM Diferenca as dif ");
		hql.append(" JOIN dif.produtoEdicao as pe ");
		hql.append(" JOIN pe.produto as p ");
		hql.append(" WHERE dif.dataMovimento = :dataOperacaoDistribuidor ");		
		hql.append(" AND dif.tipoDiferenca IN (:listaDeFaltas) ");
		hql.append(" GROUP BY p.codigo, p.nome, pe.numeroEdicao, pe.precoVenda");
		
		query = super.getSession().createQuery(hql.toString());
		
		List<TipoDiferenca> listaDeFaltas = new ArrayList<TipoDiferenca>();
		
		listaDeFaltas.add(TipoDiferenca.FALTA_DE);
		listaDeFaltas.add(TipoDiferenca.FALTA_EM);
		
		query.setParameter("dataOperacaoDistribuidor", dataOperacaoDistribuidor);
		
		query.setParameterList("listaDeFaltas", listaDeFaltas);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ReparteFecharDiaDTO.class));
		
		List<ReparteFecharDiaDTO> listaFinalDeFaltas = query.list();
		
		listaFinal =  obterListaFinalParaGridReparte(listaFinal, listaFinalDeFaltas, "faltas");
		
		//FIM DAS FALTAS
		
		hql = new StringBuilder();
		
		hql.append(" SELECT p.codigo as codigo,  ");
		hql.append(" p.nome as nomeProduto, ");
		hql.append(" pe.numeroEdicao as numeroEdicao, ");
		hql.append(" pe.precoVenda as precoVenda, ");
		hql.append(" count(*) as qtdTransferido ");
		
		hql.append(" from MovimentoEstoque me ");
		hql.append(" JOIN me.tipoMovimento as tm ");
		hql.append(" JOIN me.produtoEdicao as pe ");
		hql.append(" JOIN pe.produto as p ");
		hql.append(" WHERE me.dataCriacao = :dataOperacaoDistribuidor ");
		hql.append(" AND tm.grupoMovimentoEstoque IN (:listaGrupoMovimentoEstoque) ");
		hql.append(" GROUP BY p.codigo, p.nome, pe.numeroEdicao, pe.precoVenda");
		
		query = super.getSession().createQuery(hql.toString());
		
		List<GrupoMovimentoEstoque> listaGrupoMovimentoEstoque = new ArrayList<GrupoMovimentoEstoque>();
		
		listaGrupoMovimentoEstoque.add(GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_LANCAMENTO);
		listaGrupoMovimentoEstoque.add(GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_PRODUTOS_DANIFICADOS);
		listaGrupoMovimentoEstoque.add(GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_RECOLHIMENTO);
		listaGrupoMovimentoEstoque.add(GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_SUPLEMENTAR);
		
		query.setParameter("dataOperacaoDistribuidor", dataOperacaoDistribuidor);
		
		query.setParameterList("listaGrupoMovimentoEstoque", listaGrupoMovimentoEstoque);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ReparteFecharDiaDTO.class));
		
		List<ReparteFecharDiaDTO> listaFinalDeTransferencias = query.list();
		
		listaFinal =  obterListaFinalParaGridReparte(listaFinal, listaFinalDeTransferencias, "transferencias");
		
		//FIM DOS TRANSFERIDOS
		
		hql = new StringBuilder();
		
		hql.append(" SELECT p.codigo as codigo,  ");
		hql.append(" p.nome as nomeProduto, ");
		hql.append(" pe.numeroEdicao as numeroEdicao, ");
		hql.append(" pe.precoVenda as precoVenda, ");
		hql.append(" count(*) as qtdDistribuido ");
		
		hql.append(" from MovimentoEstoque me ");
		hql.append(" JOIN me.tipoMovimento as tm ");
		hql.append(" JOIN me.produtoEdicao as pe ");
		hql.append(" JOIN pe.produto as p ");
		hql.append(" WHERE me.dataCriacao = :dataOperacaoDistribuidor ");
		hql.append(" AND tm.grupoMovimentoEstoque = :grupoMovimento ");
		hql.append(" AND me.status = :status ");
		hql.append(" GROUP BY p.codigo, p.nome, pe.numeroEdicao, pe.precoVenda");
		
		query = super.getSession().createQuery(hql.toString());
		
				
		query.setParameter("dataOperacaoDistribuidor", dataOperacaoDistribuidor);		
		query.setParameter("grupoMovimento", GrupoMovimentoEstoque.ENVIO_JORNALEIRO);		
		query.setParameter("status", StatusAprovacao.APROVADO);		
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ReparteFecharDiaDTO.class));
		
		List<ReparteFecharDiaDTO> listaFinalDeDistribuidos = query.list();
		
		listaFinal =  obterListaFinalParaGridReparte(listaFinal, listaFinalDeDistribuidos, "distribuidos");
		
		listaFinal = completarListaComItensCalculados(listaFinal);
		
		return listaFinal;
	}
	
	private List<ReparteFecharDiaDTO> obterListaFinalParaGridReparte(List<ReparteFecharDiaDTO> listaFinal, List<ReparteFecharDiaDTO> listaParaComparacao, 
			String tipoDaLista){
		
		if(listaFinal != null){
			int tamanhoListaReparte = listaFinal.size();
			int tamanhoListaComparacao = listaParaComparacao.size();
			Set<ReparteFecharDiaDTO> listaRetorno = new HashSet<ReparteFecharDiaDTO>(listaFinal);
			
			if(tamanhoListaReparte >= tamanhoListaComparacao){
				for(ReparteFecharDiaDTO reparte: listaFinal){
					ReparteFecharDiaDTO objetoFinal = reparte;
					for(ReparteFecharDiaDTO comp: listaParaComparacao){
						if(reparte.getCodigo().equals(comp.getCodigo()) && reparte.getNumeroEdicao() == comp.getNumeroEdicao()){
							if(tipoDaLista.equals("sobras")){
								objetoFinal.setQtdSobras(comp.getQtdSobras());							
							}else if(tipoDaLista.equals("faltas")){
								objetoFinal.setQtdFaltas(comp.getQtdFaltas());
							}else if(tipoDaLista.equals("transferencias")){
								objetoFinal.setQtdTransferido(comp.getQtdTransferido());
							}else if(tipoDaLista.equals("distribuidos")){
								objetoFinal.setQtdDistribuido(comp.getQtdDistribuido());
							}
							listaRetorno.add(objetoFinal);
						}else{
							listaRetorno.add(objetoFinal);								
						}
					}				
				}
			}
			return new ArrayList<ReparteFecharDiaDTO>(listaRetorno);
		}else{
			return listaParaComparacao;
		}
	}
	
	private List<ReparteFecharDiaDTO> completarListaComItensCalculados(List<ReparteFecharDiaDTO> listaFinal) {
		
		Set<ReparteFecharDiaDTO> listaRetorno = new HashSet<ReparteFecharDiaDTO>(listaFinal);
		for(ReparteFecharDiaDTO dto : listaFinal){
			ReparteFecharDiaDTO objetoFinal = dto;
			if(dto.getQtdReparte() != null && dto.getQtdSobras() != null && dto.getQtdFaltas() != null){
				long aDistribuir = (dto.getQtdReparte() + dto.getQtdSobras()) - dto.getQtdFaltas();
				objetoFinal.setQtdADistribuir(aDistribuir);
				long sobaDistribuida = aDistribuir - dto.getQtdDistribuido();
				objetoFinal.setQtdSobraDiferenca(sobaDistribuida);
				long diferenca = dto.getQtdDistribuido() - sobaDistribuida;
				objetoFinal.setQtdDiferenca(diferenca);
				listaRetorno.add(objetoFinal);
			}
		}
		
		
		
		return formatarDadosNulos(new ArrayList<ReparteFecharDiaDTO>(listaRetorno));
	}

	private List<ReparteFecharDiaDTO> formatarDadosNulos(ArrayList<ReparteFecharDiaDTO> listaFinal) {
		 
		Set<ReparteFecharDiaDTO> listaRetorno = new HashSet<ReparteFecharDiaDTO>(listaFinal);
		for(ReparteFecharDiaDTO dto : listaFinal){
			ReparteFecharDiaDTO objetoFinal = dto;
			if(dto.getQtdReparte() == null){
				objetoFinal.setQtdReparteFormatado("");
			}else{
				objetoFinal.setQtdReparteFormatado(dto.getQtdReparte().toString());
			}
			if(dto.getQtdSobras() == null){
				objetoFinal.setQtdSobrasFormatado("");
			}else{
				objetoFinal.setQtdSobrasFormatado(dto.getQtdSobras().toString());
			}
			if(dto.getQtdFaltas() == null){
				objetoFinal.setQtdFaltasFormatado("");
			}else{
				objetoFinal.setQtdFaltasFormatado(dto.getQtdFaltas().toString());
			}
			if(dto.getQtdTransferido() == null){
				objetoFinal.setQtdTransferenciaFormatado("");
			}else{
				objetoFinal.setQtdTransferenciaFormatado(dto.getQtdTransferido().toString());
			}
			if(dto.getQtdADistribuir() == null){
				objetoFinal.setQtdADistribuirFormatado("");
			}else{
				objetoFinal.setQtdADistribuirFormatado(dto.getQtdADistribuir().toString());
			}
			if(dto.getQtdDistribuido() == null){
				objetoFinal.setQtdDistribuidoFormatado("");
			}else{
				objetoFinal.setQtdDistribuidoFormatado(dto.getQtdDistribuido().toString());
			}
			if(dto.getQtdSobraDiferenca() == null){
				objetoFinal.setQtdSobraDistribuidoFormatado("");
			}else{
				objetoFinal.setQtdSobraDistribuidoFormatado(dto.getQtdSobraDiferenca().toString());
			}
			if(dto.getQtdDiferenca() == null){
				objetoFinal.setQtdDiferencaFormatado("");
			}else{
				objetoFinal.setQtdDiferencaFormatado(dto.getQtdDiferenca().toString());
			}
			listaRetorno.add(objetoFinal);
		}
		
		return new ArrayList<ReparteFecharDiaDTO>(listaRetorno);
	}

}

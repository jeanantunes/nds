﻿package br.com.abril.nds.process.verificartotalfixacoes;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.dao.CotaDAO;
import br.com.abril.nds.model.estudo.ClassificacaoCota;
import br.com.abril.nds.model.estudo.CotaDesenglobada;
import br.com.abril.nds.model.estudo.CotaEnglobada;
import br.com.abril.nds.model.estudo.CotaEstudo;
import br.com.abril.nds.model.estudo.EstudoTransient;
import br.com.abril.nds.model.estudo.ProdutoEdicaoEstudo;
import br.com.abril.nds.process.ProcessoAbstrato;

/**
 * Processo que tem como objetivo efetuar o cálculo da divisão do reparte entre as cotas encontradas para o perfil definido no
 * setup do estudo, levando em consideração todas as variáveis também definidas no setup.
 * <p style="white-space: pre-wrap;">
 * SubProcessos: - N/A Processo Pai: - {@link VerificarTotalFixacoes}
 * 
 * Processo Anterior: N/A Próximo Processo: N/A
 * </p>
 */
@Component
public class SelecaoBancas extends ProcessoAbstrato {

	private static final BigDecimal BIGDECIMAL_100 = BigDecimal.valueOf(100);
	
	@Autowired
	private CotaDAO cotaDAO;

	@Override
	public void executar(EstudoTransient estudo) {
		List<CotaEstudo> cotasComHistorico = trataCotasComEnglobacao(cotaDAO.getCotasComEdicoesBase(estudo));

		calcularTotais(cotasComHistorico);
		
		estudo.setCotas(cotasComHistorico);
	}

	private void calcularTotais(List<CotaEstudo> cotasComHistorico) {
		for (CotaEstudo cota : cotasComHistorico) {
			BigDecimal totalEdicoes = BigDecimal.ZERO;
			BigDecimal totalVenda = BigDecimal.ZERO;
			BigDecimal totalReparte = BigDecimal.ZERO;
			for (ProdutoEdicaoEstudo edicao : cota.getEdicoesRecebidas()) {
		totalEdicoes = totalEdicoes.add(BigDecimal.ONE);
		totalVenda = totalVenda.add(edicao.getVenda());
		totalReparte = totalReparte.add(edicao.getReparte());
			}
			if (!cota.getClassificacao().equals(ClassificacaoCota.CotaNova)) {
				if (totalReparte.compareTo(BigDecimal.ZERO) == 0) {
					cota.setClassificacao(ClassificacaoCota.BancaComReparteZeroMinimoZeroCotaAntiga);
				}
				if (totalVenda.compareTo(BigDecimal.ZERO) == 0) {
					cota.setClassificacao(ClassificacaoCota.BancaComTotalVendaZeraMinimoZeroCotaAntiga);
				}
			}
	    if (totalEdicoes.compareTo(BigDecimal.ZERO) != 0) {
		cota.setVendaMediaNominal(totalVenda.divide(totalEdicoes, 0, BigDecimal.ROUND_HALF_UP));
		}
	}
    }

	private List<CotaEstudo> trataCotasComEnglobacao(List<CotaEstudo> cotasComHistorico) {
		LinkedHashMap<Long, CotaEstudo> cotasComHistoricoMap = new LinkedHashMap<>();
		for (CotaEstudo cota : cotasComHistorico) {
			cotasComHistoricoMap.put(cota.getId(), cota);
		}
		
		for (CotaDesenglobada cotaDesenglobada : cotaDAO.buscarCotasDesenglobadas()) {
			
			if (cotasComHistoricoMap.containsKey(cotaDesenglobada.getId())) {
				cotasComHistoricoMap.get(cotaDesenglobada.getId()).setClassificacao(ClassificacaoCota.EnglobaDesengloba);
				
				for (ProdutoEdicaoEstudo edicaoCotaDesenglobada : cotasComHistoricoMap.get(cotaDesenglobada.getId()).getEdicoesRecebidas()) {
					BigDecimal reparteInicial = edicaoCotaDesenglobada.getReparte();
					BigDecimal vendaInicial = edicaoCotaDesenglobada.getVenda();
					
					for (CotaEnglobada cotaEnglobada : cotaDesenglobada.getCotasEnglobadas()) {
						BigDecimal porcentualEnglobacao = BigDecimal.valueOf(cotaEnglobada.getPorcentualEnglobacao()).divide(BIGDECIMAL_100);
						
						if (cotasComHistoricoMap.containsKey(cotaEnglobada.getId())) {
			    distribuiEnglobacao(reparteInicial, vendaInicial, porcentualEnglobacao, edicaoCotaDesenglobada,
				    cotasComHistoricoMap.get(cotaEnglobada.getId()));
						} else {
							CotaEstudo cota = cotaDAO.getCotaById(cotaEnglobada.getId());
							distribuiEnglobacao(reparteInicial, vendaInicial, porcentualEnglobacao, edicaoCotaDesenglobada, cota);
							cotasComHistoricoMap.put(cota.getId(), cota);
						}
					}
				}
			}
		}
		
		return new ArrayList<>(cotasComHistoricoMap.values());
	}

    private void distribuiEnglobacao(BigDecimal reparteInicial, BigDecimal vendaInicial, BigDecimal porcentualEnglobacao,
	    ProdutoEdicaoEstudo edicaoCotaDesenglobada, CotaEstudo cotaEnglobada) {
		
		cotaEnglobada.setClassificacao(ClassificacaoCota.EnglobaDesengloba);
		ProdutoEdicaoEstudo edicaoCotaEnglobada = buscaEdicaoPorNumeroLancamento(edicaoCotaDesenglobada, cotaEnglobada.getEdicoesRecebidas());
		
		BigDecimal reparteTransferir = reparteInicial.multiply(porcentualEnglobacao);
		edicaoCotaDesenglobada.setReparte(edicaoCotaDesenglobada.getReparte().subtract(reparteTransferir));
		edicaoCotaEnglobada.setReparte(edicaoCotaEnglobada.getReparte().add(reparteTransferir));
		
		BigDecimal vendaTransferir = vendaInicial.multiply(porcentualEnglobacao);
		edicaoCotaDesenglobada.setVenda(edicaoCotaDesenglobada.getVenda().subtract(vendaTransferir));
		edicaoCotaEnglobada.setVenda(edicaoCotaEnglobada.getVenda().add(vendaTransferir));
		
	}

	private ProdutoEdicaoEstudo buscaEdicaoPorNumeroLancamento(ProdutoEdicaoEstudo edicaoCotaDesenglobada, List<ProdutoEdicaoEstudo> edicoesRecebidas) {
		for (ProdutoEdicaoEstudo produtoEdicao : edicoesRecebidas) {
	    if (produtoEdicao.getNumeroEdicao().equals(edicaoCotaDesenglobada.getNumeroEdicao())) {
				return produtoEdicao;
			}
		}
		return atualizaListaCotaEnglobadaComEdicaoClonada(edicaoCotaDesenglobada, edicoesRecebidas);
	}

    private ProdutoEdicaoEstudo atualizaListaCotaEnglobadaComEdicaoClonada(ProdutoEdicaoEstudo edicaoCotaDesenglobada,
	    List<ProdutoEdicaoEstudo> edicoesRecebidas) {
		ProdutoEdicaoEstudo produtoEdicao = new ProdutoEdicaoEstudo();
		try {
			BeanUtils.copyProperties(produtoEdicao, edicaoCotaDesenglobada);
		} catch (IllegalAccessException | InvocationTargetException e) {
			Log.debug("Erro ao clonar ProdutoEdicao para CotaEnglobada", e);
		}
		produtoEdicao.setReparte(BigDecimal.ZERO);
		produtoEdicao.setVenda(BigDecimal.ZERO);
		return produtoEdicao;
	}

}

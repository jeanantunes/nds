package br.com.abril.nds.process.verificartotalfixacoes;

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
import br.com.abril.nds.model.ClassificacaoCota;
import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.CotaDesenglobada;
import br.com.abril.nds.model.CotaEnglobada;
import br.com.abril.nds.model.ProdutoEdicao;
import br.com.abril.nds.process.ProcessoAbstrato;

/**
 * Processo que tem como objetivo efetuar o cálculo da divisão do reparte entre
 * as cotas encontradas para o perfil definido no setup do estudo, levando em
 * consideração todas as variáveis também definidas no setup.
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
	protected void executarProcesso() {
		List<Cota> cotasComHistorico = trataCotasComEnglobacao(cotaDAO.getCotasComEdicoesBase(getEstudo()));

		calcularTotais(cotasComHistorico);
		
		getEstudo().setCotas(cotasComHistorico);
	}

	private void calcularTotais(List<Cota> cotasComHistorico) {
		for (Cota cota : cotasComHistorico) {
			BigDecimal totalEdicoes = BigDecimal.ZERO;
			BigDecimal totalVenda = BigDecimal.ZERO;
			BigDecimal totalReparte = BigDecimal.ZERO;
			for (ProdutoEdicao edicao : cota.getEdicoesRecebidas()) {
				totalEdicoes.add(BigDecimal.ONE);
				totalVenda.add(edicao.getVenda());
				totalReparte.add(edicao.getReparte());
			}
			if (!cota.getClassificacao().equals(ClassificacaoCota.CotaNova)) {
				if (totalReparte.compareTo(BigDecimal.ZERO) == 0) {
					cota.setClassificacao(ClassificacaoCota.BancaComReparteZeroMinimoZeroCotaAntiga);
				}
				if (totalVenda.compareTo(BigDecimal.ZERO) == 0) {
					cota.setClassificacao(ClassificacaoCota.BancaComTotalVendaZeraMinimoZeroCotaAntiga);
				}
			}
			cota.setVendaMediaNominal(totalVenda.divide(totalEdicoes));
		}
	}

	private List<Cota> trataCotasComEnglobacao(List<Cota> cotasComHistorico) {
		LinkedHashMap<Long, Cota> cotasComHistoricoMap = new LinkedHashMap<>();
		for (Cota cota : cotasComHistorico) {
			cotasComHistoricoMap.put(cota.getId(), cota);
		}
		
		for (CotaDesenglobada cotaDesenglobada : cotaDAO.buscarCotasDesenglobadas()) {
			
			if (cotasComHistoricoMap.containsKey(cotaDesenglobada.getId())) {
				cotasComHistoricoMap.get(cotaDesenglobada.getId()).setClassificacao(ClassificacaoCota.EnglobaDesengloba);
				
				for (ProdutoEdicao edicaoCotaDesenglobada : cotasComHistoricoMap.get(cotaDesenglobada.getId()).getEdicoesRecebidas()) {
					BigDecimal reparteInicial = edicaoCotaDesenglobada.getReparte();
					BigDecimal vendaInicial = edicaoCotaDesenglobada.getVenda();
					
					for (CotaEnglobada cotaEnglobada : cotaDesenglobada.getCotasEnglobadas()) {
						BigDecimal porcentualEnglobacao = BigDecimal.valueOf(cotaEnglobada.getPorcentualEnglobacao()).divide(BIGDECIMAL_100);
						
						if (cotasComHistoricoMap.containsKey(cotaEnglobada.getId())) {
							distribuiEnglobacao(reparteInicial, vendaInicial, porcentualEnglobacao, edicaoCotaDesenglobada, cotasComHistoricoMap.get(cotaEnglobada.getId()));
						} else {
							Cota cota = cotaDAO.getCotaById(cotaEnglobada.getId());
							distribuiEnglobacao(reparteInicial, vendaInicial, porcentualEnglobacao, edicaoCotaDesenglobada, cota);
							cotasComHistoricoMap.put(cota.getId(), cota);
						}
					}
				}
			}
		}
		
		return new ArrayList<>(cotasComHistoricoMap.values());
	}

	private void distribuiEnglobacao(BigDecimal reparteInicial,
			BigDecimal vendaInicial, BigDecimal porcentualEnglobacao, ProdutoEdicao edicaoCotaDesenglobada,
			Cota cotaEnglobada) {
		
		cotaEnglobada.setClassificacao(ClassificacaoCota.EnglobaDesengloba);
		ProdutoEdicao edicaoCotaEnglobada = buscaEdicaoPorNumeroLancamento(edicaoCotaDesenglobada, cotaEnglobada.getEdicoesRecebidas());
		
		BigDecimal reparteTransferir = reparteInicial.multiply(porcentualEnglobacao);
		edicaoCotaDesenglobada.setReparte(edicaoCotaDesenglobada.getReparte().subtract(reparteTransferir));
		edicaoCotaEnglobada.setReparte(edicaoCotaEnglobada.getReparte().add(reparteTransferir));
		
		BigDecimal vendaTransferir = vendaInicial.multiply(porcentualEnglobacao);
		edicaoCotaDesenglobada.setVenda(edicaoCotaDesenglobada.getVenda().subtract(vendaTransferir));
		edicaoCotaEnglobada.setVenda(edicaoCotaEnglobada.getVenda().add(vendaTransferir));
		
	}

	private ProdutoEdicao buscaEdicaoPorNumeroLancamento(ProdutoEdicao edicaoCotaDesenglobada, List<ProdutoEdicao> edicoesRecebidas) {
		for (ProdutoEdicao produtoEdicao : edicoesRecebidas) {
			if(produtoEdicao.getNumeroEdicao().equals(edicaoCotaDesenglobada.getNumeroEdicao())) {
				return produtoEdicao;
			}
		}
		return atualizaListaCotaEnglobadaComEdicaoClonada(edicaoCotaDesenglobada, edicoesRecebidas);
	}

	private ProdutoEdicao atualizaListaCotaEnglobadaComEdicaoClonada(ProdutoEdicao edicaoCotaDesenglobada, List<ProdutoEdicao> edicoesRecebidas) {
		ProdutoEdicao produtoEdicao = new ProdutoEdicao();
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

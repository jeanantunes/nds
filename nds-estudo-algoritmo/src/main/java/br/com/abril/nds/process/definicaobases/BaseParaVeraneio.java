package br.com.abril.nds.process.definicaobases;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.dao.DefinicaoBasesDAO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.estudo.EstudoTransient;
import br.com.abril.nds.model.estudo.ProdutoEdicaoEstudo;
import br.com.abril.nds.process.ProcessoAbstrato;
import br.com.abril.nds.service.EstudoAlgoritmoService;
import br.com.abril.nds.vo.ValidacaoVO;

/**
 * Processo que tem como objetivo efetuar o cálculo da divisão do reparte entre
 * as cotas encontradas para o perfil definido no setup do estudo, levando em
 * consideração todas as variáveis também definidas no setup.
 * <p style="white-space: pre-wrap;">
 * SubProcessos: - N/A Processo Pai: - {@link DefinicaoBases}
 * 
 * Processo Anterior: N/A Próximo Processo: {@link BaseParaSaidaVeraneio}
 * </p>
 */
@Component
public class BaseParaVeraneio extends ProcessoAbstrato {

	private static int NUM_MAX_EDICOES_BASES = 6;

	private static int NUM_MAX_EDICOES_BASES_COMPLEMENTARES = 2;

	@Autowired
	private EstudoAlgoritmoService estudoAlgoritmoService;

	@Autowired
	private DefinicaoBasesDAO definicaoBasesDAO;

	@Override
	public void executar(EstudoTransient estudo)  {

		if(estudo == null || !estudo.isPracaVeraneio()) {
			return;
		}

		List<ProdutoEdicaoEstudo> edicoes = new ArrayList<ProdutoEdicaoEstudo>();
		if(estudo.getProdutoEdicaoEstudo() != null 
				&& estudoAlgoritmoService.validaPeriodoVeraneio(estudo.getProdutoEdicaoEstudo().getDataLancamento())) {

			List<ProdutoEdicaoEstudo> edicoesVeraneio = estudoAlgoritmoService.obterEdicoesPenultimoVeraneio(estudo);
			if(edicoesVeraneio != null && !edicoesVeraneio.isEmpty()) {

				edicoes.addAll(edicoesVeraneio); 
			}

			edicoesVeraneio = estudoAlgoritmoService.obterEdicoesUltimoVeraneio(estudo);
			if(edicoesVeraneio != null && !edicoesVeraneio.isEmpty()) {

				edicoes.addAll(edicoesVeraneio); 
			}

			for (ProdutoEdicaoEstudo produtoEdicao : edicoes) {
				produtoEdicao.setIndicePeso(BigDecimal.valueOf(2));
			}

			if(edicoes.isEmpty()) {

				List<ProdutoEdicaoEstudo> edicoesComplementares = definicaoBasesDAO.getEdicoesBases(estudo.getProdutoEdicaoEstudo());
				edicoesComplementares = estudoAlgoritmoService.limitarEdicoesApenasSeis(edicoesComplementares, estudo);

				for(ProdutoEdicaoEstudo pee : edicoesComplementares) {
					edicoes.add(pee);
				}

			} else if(edicoes.size() < NUM_MAX_EDICOES_BASES) {

				List<ProdutoEdicaoEstudo> edicoesComplementares = definicaoBasesDAO.getEdicoesBases(estudo.getProdutoEdicaoEstudo());

				edicoesComplementares = estudoAlgoritmoService.limitarEdicoesApenasSeis(edicoesComplementares, estudo);

				if(NUM_MAX_EDICOES_BASES - edicoes.size() == 1 && edicoesComplementares.size() > 1) {
					edicoes.add(edicoesComplementares.get(1));
				} else if(NUM_MAX_EDICOES_BASES - edicoes.size() >= NUM_MAX_EDICOES_BASES_COMPLEMENTARES) {

					if(edicoesComplementares.size() == 2) {

						edicoes.add(edicoesComplementares.get(0));
					} else if(edicoesComplementares.size() > 2) {

						edicoes.add(edicoesComplementares.get(0));
						edicoes.add(edicoesComplementares.get(1));
					}
				}

			}

			edicoes = estudoAlgoritmoService.limitarEdicoesApenasSeis(edicoes, estudo);
		} else {

			edicoes = estudoAlgoritmoService.buscaEdicoesAnosAnterioresSaidaVeraneio(estudo.getProdutoEdicaoEstudo());
			edicoes = estudoAlgoritmoService.limitarEdicoesApenasSeis(edicoes, estudo);			
		}

		estudo.setEdicoesBase(new LinkedList<ProdutoEdicaoEstudo>(edicoes));

	}

	private boolean isEdicoesMesmoMesAnosAnterioresValidas(DateTime dataLancamento, List<ProdutoEdicaoEstudo> edicoes) {

		List<ProdutoEdicaoEstudo> edicoesEncontradas = extrairEdicoesMesmoMesAnosAnteriores(dataLancamento, edicoes);
		
		return (edicoesEncontradas != null && edicoesEncontradas.size() == 2);
	}

	private List<ProdutoEdicaoEstudo> extrairEdicoesMesmoMesAnosAnteriores(DateTime dataLancamento, List<ProdutoEdicaoEstudo> edicoes) {
		
		List<ProdutoEdicaoEstudo> edicoesEncontradas = new ArrayList<ProdutoEdicaoEstudo>(); 
		
		int ultimoAno = (dataLancamento.getYear() - 1);
		int penultimoAno = (dataLancamento.getYear() - 2);
		
		Map<Integer, List<Date>> datasAnosAnteriores = new HashMap<Integer, List<Date>>();
		
		List<Date> listUltimoAno = new ArrayList<Date>();
		List<Date> listPenultimoAno = new ArrayList<Date>();
		
		for(ProdutoEdicaoEstudo pee : edicoes) {
			
			if(pee.getDataLancamento() != null) {
				
				if(new DateTime(pee.getDataLancamento()).getYear() == ultimoAno) {
					
					listUltimoAno.add(pee.getDataLancamento());
				} 
				
				if(new DateTime(pee.getDataLancamento()).getYear() == penultimoAno) {
					
					listPenultimoAno.add(pee.getDataLancamento());
				}
			}
		}
		
		datasAnosAnteriores.put(ultimoAno, listUltimoAno);
		datasAnosAnteriores.put(penultimoAno, listPenultimoAno);
		
		Date dataMaisProximaUltimoAno = estudoAlgoritmoService.extrairDataMaisProximaLancamento(dataLancamento, ultimoAno, datasAnosAnteriores);
		Date dataMaisProximaPenultimoAno = estudoAlgoritmoService.extrairDataMaisProximaLancamento(dataLancamento, penultimoAno, datasAnosAnteriores);
		
		for(ProdutoEdicaoEstudo pee : edicoes) {
			
			if(pee.getDataLancamento() != null) {
				
				if(pee.getDataLancamento().equals(dataMaisProximaUltimoAno)) {
					
					edicoesEncontradas.add(pee);
				} 
				
				if(pee.getDataLancamento().equals(dataMaisProximaPenultimoAno)) {
					
					edicoesEncontradas.add(pee);
				}
			}
		}
		
		return edicoesEncontradas;
		
	}

	private void adicionarEdicoesAnterioresAoEstudoSaidaVeraneio(ProdutoEdicaoEstudo produtoEdicao, EstudoTransient estudo) {
		
		List<ProdutoEdicaoEstudo> edicoesAnosAnterioresSaidaVeraneio = estudoAlgoritmoService.buscaEdicoesAnosAnterioresSaidaVeraneio(produtoEdicao);
		
		if (!edicoesAnosAnterioresSaidaVeraneio.isEmpty()) {
			
			for(ProdutoEdicaoEstudo pee : edicoesAnosAnterioresSaidaVeraneio) {
				if(!estudo.getEdicoesBase().contains(pee)) {
					estudo.getEdicoesBase().add(pee);
				}
			}
			
		}
	}

	private void adicionarEdicoesAnterioresAoEstudo(ProdutoEdicaoEstudo produtoEdicaoBase, EstudoTransient estudo)  {
		
		List<ProdutoEdicaoEstudo> edicoesAnosAnteriores = estudoAlgoritmoService.buscaEdicoesAnosAnterioresVeraneio(produtoEdicaoBase);
		
		if (edicoesAnosAnteriores.isEmpty()) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Não foram encontradas outras bases para veraneio, favor inserir bases manualmente."));
		}
		
		for (ProdutoEdicaoEstudo edicao : edicoesAnosAnteriores) {
			edicao.setIndicePeso(BigDecimal.valueOf(2));
			if(!estudo.getEdicoesBase().contains(edicao)) {
				estudo.getEdicoesBase().add(edicao);
			} else if(estudo.getEdicoesBase().contains(edicao) 
					&& !estudo.getEdicoesBase().get(estudo.getEdicoesBase().indexOf(edicao)).getIndicePeso().equals(edicao.getIndicePeso())) {
				estudo.getEdicoesBase().get(estudo.getEdicoesBase().indexOf(edicao)).setIndicePeso(BigDecimal.valueOf(2));
			}
		}
		
	}

}
package br.com.abril.nds.process.definicaobases;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.joda.time.DateTime;
import org.joda.time.MonthDay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.dao.DefinicaoBasesDAO;
import br.com.abril.nds.enumerators.DataReferencia;
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
		
		List<ProdutoEdicaoEstudo> edicoes = null;
		if(estudo.getProdutoEdicaoEstudo() != null 
				&& validaPeriodoVeraneio(estudo.getProdutoEdicaoEstudo().getDataLancamento())) {
			
			//Obtem edicoes do mesmo mes em ate 2 anos anteriores
			edicoes = definicaoBasesDAO.listaEdicoesAnosAnterioresMesmoMes(estudo.getProdutoEdicaoEstudo());
			
			if(edicoes == null || edicoes.isEmpty()) {
				
				//Se nao houver edicoes no anos anteriores no mesmo mes, obtem do periodo de veraneio
				edicoes = definicaoBasesDAO.listaEdicoesAnosAnterioresVeraneio(estudo.getProdutoEdicaoEstudo()
						, estudoAlgoritmoService.getDatasPeriodoVeraneio(estudo.getProdutoEdicaoEstudo()));
			}

			if(edicoes != null && !edicoes.isEmpty()) {
				
				if(!this.isEdicoesMesmoMesAnosAnterioresValidas(new DateTime(estudo.getProdutoEdicaoEstudo().getDataLancamento()), edicoes)) {
					
					Calendar calPE = Calendar.getInstance();
					calPE.setTime(estudo.getProdutoEdicaoEstudo().getDataLancamento());
					int mesProdutoEdicao = calPE.get(Calendar.MONTH);
					int anoProdutoEdicao = calPE.get(Calendar.YEAR);
					
					int anoEncontrado = 0;
					
					for (ProdutoEdicaoEstudo produtoEdicao : edicoes) {
						Calendar cal = Calendar.getInstance();
						cal.setTime(produtoEdicao.getDataLancamento());
						int mes = cal.get(Calendar.MONTH);
						
						if(mes == mesProdutoEdicao) {
							anoEncontrado = cal.get(Calendar.YEAR);
						}
					}
					
					List<ProdutoEdicaoEstudo> edicoesComplementares = definicaoBasesDAO.listaEdicoesAnosAnterioresVeraneio(estudo.getProdutoEdicaoEstudo()
							, ((anoProdutoEdicao - anoEncontrado) == 1) ? 
									estudoAlgoritmoService.getDatasPenultimoVeraneio(estudo.getProdutoEdicaoEstudo())
									: estudoAlgoritmoService.getDatasUltimoVeraneio(estudo.getProdutoEdicaoEstudo()));
					
					List<Date> dates = new ArrayList<Date>();
					for(ProdutoEdicaoEstudo ed : edicoesComplementares) {
						dates.add(ed.getDataLancamento());
					}
					
					Calendar cal = Calendar.getInstance();
					cal.setTime(estudo.getProdutoEdicaoEstudo().getDataLancamento());
					cal.add(Calendar.YEAR, ((anoProdutoEdicao - anoEncontrado) == 1) ? -2 : -1);
					
					DateTime dataLancamentoProdutoEdicao = new DateTime(cal.getTime());										
					DateTime dataMaisProximaAnterior = new DateTime(new TreeSet<Date>(dates).lower(cal.getTime()));
					DateTime dataMaisProximaPosterior = new DateTime(new TreeSet<Date>(dates).higher(cal.getTime()));
					
					DateTime dataMaisProxima = null;
					if(Math.abs(dataLancamentoProdutoEdicao.toDate().getTime() - dataMaisProximaAnterior.toDate().getTime()) < 
							Math.abs(dataLancamentoProdutoEdicao.toDate().getTime() - dataMaisProximaPosterior.toDate().getTime())
							&& dataLancamentoProdutoEdicao.monthOfYear() == dataMaisProximaAnterior.monthOfYear()) {
							
						dataMaisProxima = dataMaisProximaAnterior;
						
					} else {
						
						dataMaisProxima = dataMaisProximaPosterior;
					}
					//}
					
					for(ProdutoEdicaoEstudo ed : edicoesComplementares) {
						
						if(ed.getDataLancamento().equals(dataMaisProxima) && !edicoes.contains(ed)) {
							edicoes.add(ed);
							break;
						}
					}
					
				}
				
				for (ProdutoEdicaoEstudo produtoEdicao : edicoes) {
					produtoEdicao.setIndicePeso(BigDecimal.valueOf(2));
				}
				
				if(edicoes.size() < NUM_MAX_EDICOES_BASES) {
					
					List<ProdutoEdicaoEstudo> edicoesComplementares = definicaoBasesDAO.getEdicoesBases(estudo.getProdutoEdicaoEstudo());
					//.listaEdicoesAnosAnterioresVeraneio(estudo.getProdutoEdicaoEstudo()
					//, estudoAlgoritmoService.getDatasPeriodoVeraneio(estudo.getProdutoEdicaoEstudo()));
					
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
				
			}
			
			edicoes = estudoAlgoritmoService.limitarEdicoesApenasSeis(edicoes, estudo);
		} else {
			
			edicoes = estudoAlgoritmoService.buscaEdicoesAnosAnterioresSaidaVeraneio(estudo.getProdutoEdicaoEstudo());
			edicoes = estudoAlgoritmoService.limitarEdicoesApenasSeis(edicoes, estudo);			
		}
		
		// copia lista para não afetar o loop após modificações.
		//List<ProdutoEdicaoEstudo> edicoes = new ArrayList<ProdutoEdicaoEstudo>(estudo.getEdicoesBase());

		/*
		for (ProdutoEdicaoEstudo produtoEdicao : edicoes) {
			if (validaPeriodoVeraneio(produtoEdicao.getDataLancamento())) {
				produtoEdicao.setIndicePeso(BigDecimal.valueOf(2));
				//adicionarEdicoesAnterioresAoEstudo(produtoEdicao, estudo);
			} else {
				//adicionarEdicoesAnterioresAoEstudoSaidaVeraneio(produtoEdicao, estudo);
			}
		}
		*/
		
		estudo.setEdicoesBase(new LinkedList<ProdutoEdicaoEstudo>(edicoes));

	}

	private boolean isEdicoesMesmoMesAnosAnterioresValidas(DateTime dataLancamento, List<ProdutoEdicaoEstudo> edicoes) {

		extrairEdicoesMesmoMesAnosAnteriores(dataLancamento, edicoes);
		
		return false;
	}

	private void extrairEdicoesMesmoMesAnosAnteriores(DateTime dataLancamento, List<ProdutoEdicaoEstudo> edicoes) {
		Map<Integer, List<ProdutoEdicaoEstudo>> datasAnosAnteriores = new HashMap<Integer, List<ProdutoEdicaoEstudo>>();
		
		List<ProdutoEdicaoEstudo> ultimoAno = new ArrayList<ProdutoEdicaoEstudo>();
		List<ProdutoEdicaoEstudo> penultimoAno = new ArrayList<ProdutoEdicaoEstudo>();
		
		for(ProdutoEdicaoEstudo pee : edicoes) {
			
			if(pee.getDataLancamento() != null) {
				if(new DateTime(pee.getDataLancamento()).getYear() == (dataLancamento.getYear() - 1)) {
					ultimoAno.add(pee);
				} else if(new DateTime(pee.getDataLancamento()).getYear() == (dataLancamento.getYear() - 2)) {
					penultimoAno.add(pee);
				}
			}
		}
		
		datasAnosAnteriores.put((dataLancamento.getYear() - 1), ultimoAno);
		datasAnosAnteriores.put((dataLancamento.getYear() - 2), penultimoAno);
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

	public boolean validaPeriodoVeraneio(Date dataLancamento) {
		MonthDay inicioVeraneio = MonthDay.parse(DataReferencia.DEZEMBRO_20.getData());
		MonthDay fimVeraneio = MonthDay.parse(DataReferencia.FEVEREIRO_28.getData());
		MonthDay dtLancamento = new MonthDay(dataLancamento);

		return dtLancamento.isAfter(inicioVeraneio) || dtLancamento.isBefore(fimVeraneio);
	}

}
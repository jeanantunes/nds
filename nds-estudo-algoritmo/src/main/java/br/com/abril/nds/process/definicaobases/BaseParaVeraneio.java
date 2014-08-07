package br.com.abril.nds.process.definicaobases;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.MonthDay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

	@Autowired
	private EstudoAlgoritmoService estudoAlgoritmoService;

	@Override
	public void executar(EstudoTransient estudo)  {
		
		// copia lista para não afetar o loop após modificações.
		List<ProdutoEdicaoEstudo> edicoes = new ArrayList<ProdutoEdicaoEstudo>(estudo.getEdicoesBase());

		if (estudo.isPracaVeraneio()) {
			for (ProdutoEdicaoEstudo produtoEdicao : edicoes) {
				if (validaPeriodoVeraneio(produtoEdicao.getDataLancamento())) {
					produtoEdicao.setIndicePeso(BigDecimal.valueOf(2));
					adicionarEdicoesAnterioresAoEstudo(produtoEdicao, estudo);
				} else {
					adicionarEdicoesAnterioresAoEstudoSaidaVeraneio(produtoEdicao, estudo);
				}
			}
		}
	}

	private void adicionarEdicoesAnterioresAoEstudoSaidaVeraneio(ProdutoEdicaoEstudo produtoEdicao, EstudoTransient estudo) {
		
		List<ProdutoEdicaoEstudo> edicoesAnosAnterioresSaidaVeraneio = estudoAlgoritmoService.buscaEdicoesAnosAnterioresSaidaVeraneio(produtoEdicao);
		
		if (!edicoesAnosAnterioresSaidaVeraneio.isEmpty()) {
			
			for(ProdutoEdicaoEstudo pee : edicoesAnosAnterioresSaidaVeraneio) {
				if(!estudo.getEdicoesBase().contains(pee)) {
					estudo.getEdicoesBase().add(pee);
				}
			}
			/*
			Set<ProdutoEdicaoEstudo> edicoesBases = new HashSet<ProdutoEdicaoEstudo>(estudo.getEdicoesBase());
			edicoesBases.addAll(edicoesAnosAnterioresSaidaVeraneio);
			estudo.getEdicoesBase().clear();
			estudo.getEdicoesBase().addAll(edicoesBases);
			*/
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
				//estudo.getEdicoesBase().addAll(edicoesAnosAnteriores);
			} else if(estudo.getEdicoesBase().contains(edicao) 
					&& !estudo.getEdicoesBase().get(estudo.getEdicoesBase().indexOf(edicao)).getIndicePeso().equals(edicao.getIndicePeso())) {
				estudo.getEdicoesBase().get(estudo.getEdicoesBase().indexOf(edicao)).setIndicePeso(BigDecimal.valueOf(2));
			}
		}
		
	}

	public boolean validaPeriodoVeraneio(Date dataLancamento) {
		MonthDay inicioVeraneio = MonthDay.parse(DataReferencia.DEZEMBRO_20.getData());
		MonthDay fimVeraneio = MonthDay.parse(DataReferencia.FEVEREIRO_15.getData());
		MonthDay dtLancamento = new MonthDay(dataLancamento);

		return dtLancamento.isAfter(inicioVeraneio) || dtLancamento.isBefore(fimVeraneio);
	}

}
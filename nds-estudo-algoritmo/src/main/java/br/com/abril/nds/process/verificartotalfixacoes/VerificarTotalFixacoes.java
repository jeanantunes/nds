package br.com.abril.nds.process.verificartotalfixacoes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.model.estudo.EstudoTransient;
import br.com.abril.nds.process.ProcessoAbstrato;
import br.com.abril.nds.process.somarfixacoes.SomarFixacoes;

/**
 * Processo que tem como objetivo efetuar o cálculo da divisão do reparte entre
 * as cotas encontradas para o perfil definido no setup do estudo, levando em
 * consideração todas as variáveis também definidas no setup.
 * <p style="white-space: pre-wrap;">
 * SubProcessos: - {@link SelecaoBancas} Processo Pai: - N/A
 * 
 * Processo Anterior: {@link SomarFixacoes} Próximo Processo:
 * {@link MontaTabelaEstudos}
 * </p>
 */
@Component
public class VerificarTotalFixacoes extends ProcessoAbstrato {

	@Autowired
	private SelecaoBancas selecaoBancas;

	@Override
	public void executar(EstudoTransient estudo) throws Exception {
		selecaoBancas.executar(estudo);
	}

}

package br.com.abril.nds.process.verificartotalfixacoes;

import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.process.ProcessoAbstrato;
import br.com.abril.nds.process.montatabelaestudos.MontaTabelaEstudos;
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
public class VerificarTotalFixacoes extends ProcessoAbstrato {

    public VerificarTotalFixacoes(Estudo estudo) {
	super(estudo);
    }
    
    @Override
    protected void executarProcesso() throws Exception {
	SelecaoBancas selecaoBancas = new SelecaoBancas(super.getEstudo());
	selecaoBancas.executar();
    }

}

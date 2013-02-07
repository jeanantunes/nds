package br.com.abril.nds.process.calculoreparte;

import br.com.abril.nds.dao.CotaDAO;
import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.process.ProcessoAbstrato;
import br.com.abril.nds.process.definicaobases.DefinicaoBases;

/**
 * Processo que tem como objetivo efetuar o cálculo da divisão do reparte entre
 * as cotas encontradas para o perfil definido no setup do estudo, levando em
 * consideração todas as variáveis também definidas no setup.
 * <p style="white-space: pre-wrap;">
 * SubProcessos: - {@link MinimoMaximo} - {@link GravarReparteJuramentado} -
 * {@link AjusteFinalReparte} - {@link ReparteComplementarPorCota} -
 * {@link GravarReparteFinalCota} Processo Pai: - N/A
 * 
 * Processo Anterior: {@link DefinicaoBases} Próximo Processo: N/A
 * </p>
 */
public class CalcularReparte extends ProcessoAbstrato {

    @Override
    public void executarProcesso() throws Exception {

	// TODO Popular o estudo - Criar Logica para chamar subProcesso
	// FIXME Retirar esse trecho
	super.estudo = new Estudo();
	super.estudo.setCotas(new CotaDAO().getCotas());

	MinimoMaximo minimoMaximo = new MinimoMaximo(super.estudo);
	minimoMaximo.executar();

	GravarReparteJuramentado gravarReparteJuramentado = new GravarReparteJuramentado(
		minimoMaximo.getEstudo());
	gravarReparteJuramentado.executar();

	AjusteFinalReparte ajusteFinalReparte = new AjusteFinalReparte(
		gravarReparteJuramentado.getEstudo());
	ajusteFinalReparte.executar();

	ReparteComplementarPorCota reparteComplementarPorCota = new ReparteComplementarPorCota(
		ajusteFinalReparte.getEstudo());
	reparteComplementarPorCota.executar();

	GravarReparteFinalCota gravarReparteFinalCota = new GravarReparteFinalCota(
		reparteComplementarPorCota.getEstudo());
	gravarReparteFinalCota.executar();

	super.estudo = gravarReparteFinalCota.getEstudo();
    }

}

package br.com.abril.nds.process;

import java.util.Date;

import br.com.abril.nds.dao.CotaDAO;
import br.com.abril.nds.dao.ProdutoEdicaoDAO;
import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.process.ajustecota.AjusteCota;
import br.com.abril.nds.process.ajustereparte.AjusteReparte;
import br.com.abril.nds.process.bonificacoes.Bonificacoes;
import br.com.abril.nds.process.calculoreparte.CalcularReparte;
import br.com.abril.nds.process.complementarautomatico.ComplementarAutomatico;
import br.com.abril.nds.process.correcaovendas.CorrecaoVendas;
import br.com.abril.nds.process.definicaobases.DefinicaoBases;
import br.com.abril.nds.process.encalhemaximo.EncalheMaximo;
import br.com.abril.nds.process.jornaleirosnovos.JornaleirosNovos;
import br.com.abril.nds.process.medias.Medias;
import br.com.abril.nds.process.montatabelaestudos.MontaTabelaEstudos;
import br.com.abril.nds.process.redutorautomatico.RedutorAutomatico;
import br.com.abril.nds.process.reparteminimo.ReparteMinimo;
import br.com.abril.nds.process.reparteproporcional.ReparteProporcional;
import br.com.abril.nds.process.somarfixacoes.SomarFixacoes;
import br.com.abril.nds.process.vendamediafinal.VendaMediaFinal;
import br.com.abril.nds.process.verificartotalfixacoes.VerificarTotalFixacoes;

/**
 * Processo que tem como objetivo efetuar o cálculo da divisão do reparte entre
 * as cotas encontradas para o perfil definido no setup do estudo, levando em
 * consideração todas as variáveis também definidas no setup.
 * <p style="white-space: pre-wrap;">
 * SubProcessos: - {@link DefinicaoBases} - {@link SomarFixacoes} -
 * {@link VerificarTotalFixacoes} - {@link MontaTabelaEstudos} -
 * {@link CorrecaoVendas} - {@link Medias} - {@link Bonificacoes} -
 * {@link AjusteCota} - {@link JornaleirosNovos} - {@link VendaMediaFinal} -
 * {@link AjusteReparte} - {@link RedutorAutomatico} - {@link ReparteMinimo} -
 * {@link ReparteProporcional} - {@link EncalheMaximo} -
 * {@link ComplementarAutomatico} - {@link CalcularReparte} Processo Pai: - N/A
 * 
 * Processo Anterior: N/A Próximo Processo: N/A
 * </p>
 */
public class Principal {

	private Estudo estudo = new Estudo();
	
	public void executar(Date data, Integer fornecedor) {
		try {
			RedutorAutomatico ra = new RedutorAutomatico(estudo);
			ra.executar();
			estudo = ra.getEstudo();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String args[]) {
		new Principal().executar(new Date(), 1);
	}

	public void loadCotas() {
		CotaDAO cotaDAO = new CotaDAO();
		estudo.setCotas(cotaDAO.getCotas());
		for (Cota cota : estudo.getCotas()) {
			ProdutoEdicaoDAO ped = new ProdutoEdicaoDAO();
			cota.setEdicoesRecebidas(ped.getEdicaoRecebidas(cota));
			cota = cotaDAO.getAjustesReparteCota(cota);
		}
	}

	public void carregarParametros() {

	}
}

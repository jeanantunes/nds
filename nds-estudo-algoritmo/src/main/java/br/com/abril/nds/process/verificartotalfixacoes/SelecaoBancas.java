package br.com.abril.nds.process.verificartotalfixacoes;

import br.com.abril.nds.dao.CotaDAO;
import br.com.abril.nds.model.Estudo;
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
public class SelecaoBancas extends ProcessoAbstrato {

    public SelecaoBancas(Estudo estudo) {
	super(estudo);
    }
    
    @Override
    protected void executarProcesso() {
	Estudo estudo = super.getEstudo();	

	CotaDAO cotaDAO = new CotaDAO();
	estudo.setCotas(cotaDAO.getCotasComEdicoesBase(estudo));
    }

}

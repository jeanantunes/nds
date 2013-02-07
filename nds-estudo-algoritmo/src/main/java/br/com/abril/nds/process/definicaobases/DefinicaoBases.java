package br.com.abril.nds.process.definicaobases;

import java.util.Arrays;

import br.com.abril.nds.dao.CotaDAO;
import br.com.abril.nds.model.Estrategia;
import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.model.ProdutoEdicao;
import br.com.abril.nds.process.ProcessoAbstrato;
import br.com.abril.nds.process.somarfixacoes.SomarFixacoes;
import br.com.abril.nds.service.PreparaEstudoService;

/**
 * Processo que tem como objetivo efetuar o cálculo da divisão do reparte entre
 * as cotas encontradas para o perfil definido no setup do estudo, levando em
 * consideração todas as variáveis também definidas no setup.
 * <p * style="white-space: pre-wrap;">
 * SubProcessos: - {@link BaseParaVeraneio} - {@link BaseParaSaidaVeraneio}
 * Processo Pai: - N/A
 * 
 * Processo Anterior: N/A Próximo Processo: {@link SomarFixacoes}
 * </p>
 */
public class DefinicaoBases extends ProcessoAbstrato {

    @Override
    public void executarProcesso() throws Exception {

	// TODO Popular o estudo - Criar Logica para chamar subProcesso
	// FIXME Retirar esse trecho
	super.estudo = new Estudo();
	PreparaEstudoService estudoService;
	CotaDAO cotaDAO = new CotaDAO();
	super.estudo.setCotas(cotaDAO.getCotas());

	// TODO: implementar método calcular do Processo DefinicaoBases
	Estrategia estrategia = new Estrategia();
//	estrategia.setEdicaoBases(cotaDAO.getListEdicaoBasePorCota(estudo.getCotas()));
	// estrategia.getEdicaoBases();
	
	BaseParaVeraneio baseParaVeraneio = new BaseParaVeraneio(super.estudo);
	baseParaVeraneio.executar();
	BaseParaSaidaVeraneio baseParaSaidaVeraneio = new BaseParaSaidaVeraneio(
		baseParaVeraneio.getEstudo());
	baseParaSaidaVeraneio.executar();
<<<<<<< HEAD
=======
	// TODO: implementar método calcular do Processo DefinicaoBases
	Estrategia estrategia = new Estrategia();
	estrategia.setEdicaoBases(Arrays.asList(new ProdutoEdicao()));

	// estrategia.getEdicaoBases();
>>>>>>> branch 'master' of https://pedroxs@bitbucket.org/pedroxs/nds.git
    }
}

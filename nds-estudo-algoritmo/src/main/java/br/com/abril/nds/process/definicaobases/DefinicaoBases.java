package br.com.abril.nds.process.definicaobases;

import java.util.ArrayList;
import java.util.List;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.model.ProdutoEdicao;
import br.com.abril.nds.process.ProcessoAbstrato;
import br.com.abril.nds.process.somarfixacoes.SomarFixacoes;
import br.com.abril.nds.service.PreparaEstudoService;

/**
 * Processo que tem como objetivo efetuar o cálculo da divisão do reparte entre as cotas encontradas para o 
 * perfil definido no setup do estudo, levando em consideração todas as variáveis também definidas no setup.
 * <p * style="white-space: pre-wrap;">
 * SubProcessos:
 * 		- {@link BaseParaVeraneio}
 * 		- {@link BaseParaSaidaVeraneio}
 * Processo Pai:
 * 		- N/A
 * 
 * Processo Anterior: N/A
 * Próximo Processo: {@link SomarFixacoes}
 * </p>
 */
public class DefinicaoBases extends ProcessoAbstrato {

	private PreparaEstudoService estudoService = new PreparaEstudoService();

	public DefinicaoBases() {
		super(new Estudo());
	}

	@Override
	public void executarProcesso() throws Exception {

		// TODO Popular o estudo - Criar Logica para chamar subProcesso
		// FIXME Retirar esse trecho
		Estudo estudo = (Estudo) super.genericDTO;
		estudo.setCotas(estudoService.populaCotasParaEstudo());

		// TODO: implementar método calcular do Processo DefinicaoBases
		List<Cota> cotas = estudo.getCotas();
		List<ProdutoEdicao> edicoesBase = new ArrayList<>();
		for (Cota cota : cotas) {
			List<ProdutoEdicao> edicoesRecebidas = cota.getEdicoesRecebidas();
			for (ProdutoEdicao produtoEdicao : edicoesRecebidas) {
				// TODO implementar logica para separar as edições de base
				if (produtoEdicao.isEdicaoAberta()) {
					edicoesBase.add(produtoEdicao);
				}
			}
		}

		BaseParaVeraneio baseParaVeraneio = new BaseParaVeraneio(estudo);
		baseParaVeraneio.executar();
		BaseParaSaidaVeraneio baseParaSaidaVeraneio = new BaseParaSaidaVeraneio((Estudo) baseParaVeraneio.getGenericDTO());
		baseParaSaidaVeraneio.executar();

		super.genericDTO = baseParaSaidaVeraneio.getGenericDTO();
	}
}

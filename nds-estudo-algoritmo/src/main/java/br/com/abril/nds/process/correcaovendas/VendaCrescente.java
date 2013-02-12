package br.com.abril.nds.process.correcaovendas;

import java.math.BigDecimal;
import java.util.List;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.ProdutoEdicao;
import br.com.abril.nds.process.ProcessoAbstrato;

/**
 * Processo que tem como objetivo efetuar o cálculo da divisão do reparte entre
 * as cotas encontradas para o perfil definido no setup do estudo, levando em
 * consideração todas as variáveis também definidas no setup.
 * <p style="white-space: pre-wrap;">
 * SubProcessos: - N/A Processo Pai: - {@link CorrecaoVendas}
 * 
 * Processo Anterior: {@link CorrecaoTendencia} Próximo Processo: N/A
 * </p>
 */
public class VendaCrescente extends ProcessoAbstrato {

    private List<ProdutoEdicao> listProdutoEdicaoFechada;

    public VendaCrescente(Cota cota,
	    List<ProdutoEdicao> listProdutoEdicaoFechada) {
	super(cota);
	this.listProdutoEdicaoFechada = listProdutoEdicaoFechada;
    }

    @Override
    protected void executarProcesso() {

	BigDecimal indiceVendaCrescente = BigDecimal.ONE;

	Cota cota = (Cota) super.getGenericDTO();

	if (listProdutoEdicaoFechada != null
		&& !listProdutoEdicaoFechada.isEmpty()) {

	    if (listProdutoEdicaoFechada.size() >= 4) {

		boolean ajustarIndice = false;
		String previousNome = "";
		BigDecimal previousNumeroEdicao = BigDecimal.ZERO;

		int iEdicaoBase = 0;
		while (iEdicaoBase < listProdutoEdicaoFechada.size()) {

		    ProdutoEdicao produtoEdicao = listProdutoEdicaoFechada
			    .get(iEdicaoBase);

		    if (previousNumeroEdicao.compareTo(BigDecimal.ZERO) == 1) {

			if (previousNome.equalsIgnoreCase(produtoEdicao
				.getNome())) {

			    BigDecimal divNumeroEdicao = previousNumeroEdicao
				    .divide(new BigDecimal(produtoEdicao
					    .getNumeroEdicao()), 4,
					    BigDecimal.ROUND_FLOOR);

			    if (divNumeroEdicao.compareTo(BigDecimal.ONE) == 1) {
				ajustarIndice = true;
			    } else {
				ajustarIndice = false;
				break;
			    }

			} else {
			    ajustarIndice = false;
			    break;
			}
		    }

		    previousNumeroEdicao = new BigDecimal(
			    produtoEdicao.getNumeroEdicao());

		    previousNome = produtoEdicao.getNome();

		    iEdicaoBase++;
		}

		if (ajustarIndice) {
		    indiceVendaCrescente = indiceVendaCrescente
			    .add(new BigDecimal(0.1));
		}
	    }
	}
	
	
	indiceVendaCrescente = indiceVendaCrescente.divide(
		new BigDecimal(1), 2, BigDecimal.ROUND_FLOOR);
	
	cota.setIndiceVendaCrescente(indiceVendaCrescente);
	
	super.genericDTO = cota;
    }

}

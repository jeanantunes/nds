package br.com.abril.nds.process.correcaovendas;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Component;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.ProdutoEdicao;
import br.com.abril.nds.process.ProcessoAbstrato;
import br.com.abril.nds.process.medias.Medias;
import br.com.abril.nds.process.montatabelaestudos.MontaTabelaEstudos;

/**
 * Processo que tem como objetivo efetuar o cálculo da divisão do reparte entre
 * as cotas encontradas para o perfil definido no setup do estudo, levando em
 * consideração todas as variáveis também definidas no setup.
 * <p style="white-space: pre-wrap;">
 * SubProcessos: - {@link CorrecaoIndividual} - {@link CorrecaoTendencia} - {@link VendaCrescente} Processo Pai: - N/A
 * 
 * Processo Anterior: {@link MontaTabelaEstudos} Próximo Processo: {@link Medias} </p>
 */
@Component
public class CorrecaoVendas extends ProcessoAbstrato {

    /**
     * <h2>Processo: Correção de Vendas</h2>
     * <p><b>Recuperar as cotas armazenadas na tabela e para cada edição base por cota aplicar a regra abaixo e<br>depois armazenar os valores encontrados (vendaCorr) na
     * mesma tabela.</b></p>
     * <p>Se QtdeEdsBase > 1</p>
     * <p><pre>Se Edição = 1 ou Publicação <> Fascículos / Coleções</pre></p>
     * <p><pre>Procedure CorreçãoIndividual</pre></p>
     * <p><pre>Procedure Correção Tendência</pre></p>
     * <p><pre>Endif</pre></p>
     * <p>Endif</p>
     * <p>Se cota recebeu 4 ou mais edições-base fechadas</p>
     * <pre>Procedure VendaCrescente</pre>
     * <p>Endif</p>
     */
    @Override
    protected void executarProcesso() throws Exception {

	Cota cota = (Cota) super.genericDTO;

	List<ProdutoEdicao> listEdicaoRecebida = cota.getEdicoesRecebidas();

	if (listEdicaoRecebida != null && listEdicaoRecebida.size() > 1) {

	    BigDecimal totalReparte = BigDecimal.ZERO;
	    BigDecimal totalVenda = BigDecimal.ZERO;

	    for (ProdutoEdicao produtoEdicao : listEdicaoRecebida) {

		if (produtoEdicao.getNumeroEdicao().compareTo(new Long(1)) == 0 || !produtoEdicao.isColecao()) {

		    CorrecaoIndividual correcaoIndividual = new CorrecaoIndividual(produtoEdicao);
		    correcaoIndividual.executar();

		    totalReparte = totalReparte.add(produtoEdicao.getReparte());

		    totalVenda = totalVenda.add(produtoEdicao.getVenda());
		}
	    }

	    if (totalReparte.compareTo(BigDecimal.ZERO) == 1) {
		CorrecaoTendencia correcaoTendencia = new CorrecaoTendencia(cota, totalReparte, totalVenda);
		correcaoTendencia.executar();
	    }
	}

	VendaCrescente vendaCrescente = new VendaCrescente(cota);
	vendaCrescente.executar();
    }
}

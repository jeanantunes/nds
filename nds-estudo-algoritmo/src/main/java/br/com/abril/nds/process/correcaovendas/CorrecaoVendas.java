package br.com.abril.nds.process.correcaovendas;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.model.estudo.CotaEstudo;
import br.com.abril.nds.model.estudo.EstudoTransient;
import br.com.abril.nds.model.estudo.ProdutoEdicaoEstudo;
import br.com.abril.nds.process.medias.Medias;

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
public class CorrecaoVendas {

    @Autowired
    private CorrecaoIndividual correcaoIndividual;

    @Autowired
    private CorrecaoTendencia correcaoTendencia;

    @Autowired
    private VendaCrescente vendaCrescente;

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
    public void executar(CotaEstudo cota, EstudoTransient estudo) throws Exception {

		if (cota.getEdicoesRecebidas() != null && cota.getEdicoesRecebidas().size() > 1) {
	
		    BigDecimal totalReparte = BigDecimal.ZERO;
		    BigDecimal totalVenda = BigDecimal.ZERO;
	
		    for (ProdutoEdicaoEstudo produtoEdicao : cota.getEdicoesRecebidas()) {
				if ((estudo.getProdutoEdicaoEstudo().getNumeroEdicao().compareTo(Long.valueOf(1)) == 0) || (!estudo.getProdutoEdicaoEstudo().isColecao())) {
				    correcaoIndividual.executar(produtoEdicao);
				    totalReparte = totalReparte.add(produtoEdicao.getReparte());
				    totalVenda = totalVenda.add(produtoEdicao.getVenda());
				}
		    }
		    
		    if (totalReparte.compareTo(BigDecimal.ZERO) == 1) {
		    	correcaoTendencia.executar(cota, totalReparte, totalVenda);
		    }
		}
		
		vendaCrescente.executar(cota);
    }
}

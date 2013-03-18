package br.com.abril.nds.process.correcaovendas;

import java.math.BigDecimal;
<<<<<<< HEAD
import java.util.ArrayList;
import java.util.List;
=======

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
>>>>>>> branch 'master' of https://adenilton@bitbucket.org/pedroxs/nds.git

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
    @Override
    protected void executarProcesso() throws Exception {

	Cota cota = (Cota) super.genericDTO;
<<<<<<< HEAD

	List<ProdutoEdicao> listEdicaoFechada = new ArrayList<ProdutoEdicao>();
	
	List<ProdutoEdicao> listEdicaoRecebida = cota.getEdicoesRecebidas();

	if (listEdicaoRecebida != null && listEdicaoRecebida.size() > 1) {
=======
	
	if (cota.getEdicoesRecebidas() != null && cota.getEdicoesRecebidas().size() > 1) {
>>>>>>> branch 'master' of https://adenilton@bitbucket.org/pedroxs/nds.git

	    BigDecimal totalReparte = BigDecimal.ZERO;
	    BigDecimal totalVenda = BigDecimal.ZERO;

<<<<<<< HEAD
	    for (ProdutoEdicao produtoEdicao : listEdicaoRecebida) {
		
		if(!produtoEdicao.isEdicaoAberta()) {
		    listEdicaoFechada.add(produtoEdicao);
		}
		
=======
	    for (ProdutoEdicao produtoEdicao : cota.getEdicoesRecebidas()) {
>>>>>>> branch 'master' of https://adenilton@bitbucket.org/pedroxs/nds.git
		if (produtoEdicao.getNumeroEdicao().compareTo(new Long(1)) == 0 || !produtoEdicao.isColecao()) {
		    correcaoIndividual.setGenericDTO(produtoEdicao);
		    correcaoIndividual.executar();
		    
		    totalReparte = totalReparte.add(produtoEdicao.getReparte());
		    totalVenda = totalVenda.add(produtoEdicao.getVenda());
		}
	    }
	    if (totalReparte.compareTo(BigDecimal.ZERO) == 1) {
		correcaoTendencia.setGenericDTO(cota);
		correcaoTendencia.setTotalReparte(totalReparte);
		correcaoTendencia.setTotalVenda(totalVenda);
		correcaoTendencia.executar();
	    }
	}
<<<<<<< HEAD
	
	if(listEdicaoFechada.size() >= 4) {
	    VendaCrescente vendaCrescente = new VendaCrescente(cota);
	    vendaCrescente.executar();
	}
=======
	vendaCrescente.setGenericDTO(cota);
	vendaCrescente.executar();
>>>>>>> branch 'master' of https://adenilton@bitbucket.org/pedroxs/nds.git
    }
}

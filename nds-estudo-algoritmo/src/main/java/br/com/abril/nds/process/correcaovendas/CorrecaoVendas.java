package br.com.abril.nds.process.correcaovendas;

import java.util.ArrayList;
import java.util.List;

import br.com.abril.nds.dao.EstoqueProdutoCotaDAO;
import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.EstoqueProdutoCota;
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
public class CorrecaoVendas extends ProcessoAbstrato {

    public CorrecaoVendas(Cota cota) {
	super(cota);
    }

    /**
     * <h2>Processo: Correção de Vendas</h2>
     * 
     * <p><b>Recuperar as cotas armazenadas na tabela e para cada edição base por cota aplicar a regra abaixo e<br>
     * depois armazenar os valores encontrados (vendaCorr) na mesma tabela.</b></p>
     * 
     * <p>Se QtdeEdsBase > 1</p>
     * 
     * <p><pre>Se Edição = 1 ou Publicação <> Fascículos / Coleções</pre></p>
     * <p><pre><pre>Procedure CorreçãoIndividual</pre></pre></p>
     * <p><pre><pre>Procedure Correção Tendência</pre></pre></p>
     * <p><pre>Endif</pre></p>
     * <p>Endif</p>
     * 
     * <p>Se cota recebeu 4 ou mais edições-base fechadas</p>
     * <p><pre>Procedure VendaCrescente</pre></p>
     * <p>Endif</p>
     */
    @Override
    protected void executarProcesso() throws Exception {

	List<ProdutoEdicao> listProdutoEdicaoFechada = new ArrayList<ProdutoEdicao>();

	Cota cota = (Cota) super.genericDTO;

	List<ProdutoEdicao> listEdicaoBase = cota.getEdicoesBase();

	List<EstoqueProdutoCota> listEstoqueProdutoCota = new EstoqueProdutoCotaDAO()
		.getByCotaIdProdutoEdicaoId(cota, listEdicaoBase);

	cota.setEstoqueProdutoCotas(listEstoqueProdutoCota);

	CorrecaoTendencia correcaoTendencia = new CorrecaoTendencia(cota);
	correcaoTendencia.executar();

	cota = (Cota) correcaoTendencia.getGenericDTO();

	if (listEdicaoBase != null && listEdicaoBase.size() > 1) {

	    int iEstoqueProdutoCota = 0;
	    while (iEstoqueProdutoCota < listEstoqueProdutoCota.size()) {

		EstoqueProdutoCota estoqueProdutoCota = listEstoqueProdutoCota
			.get(iEstoqueProdutoCota);

		if (estoqueProdutoCota.getProdutoEdicao().getNumeroEdicao()
			.compareTo(new Long(1)) == 0
			|| !estoqueProdutoCota.getProdutoEdicao().isColecao()) {

		    CorrecaoIndividual correcaoIndividual = new CorrecaoIndividual(
			    estoqueProdutoCota);
		    correcaoIndividual.executar();

		    estoqueProdutoCota = (EstoqueProdutoCota) correcaoIndividual
			    .getGenericDTO();

		}

		if (!estoqueProdutoCota.getProdutoEdicao().isEdicaoAberta()) {
		    listProdutoEdicaoFechada.add(estoqueProdutoCota
			    .getProdutoEdicao());
		}

		listEstoqueProdutoCota.set(iEstoqueProdutoCota,
			estoqueProdutoCota);

		iEstoqueProdutoCota++;
	    }

	}

	cota.setEstoqueProdutoCotas(listEstoqueProdutoCota);

	VendaCrescente vendaCrescente = new VendaCrescente(cota,
		listProdutoEdicaoFechada);
	vendaCrescente.executarProcesso();

	cota = (Cota) vendaCrescente.getGenericDTO();

	super.genericDTO = cota;
    }

}

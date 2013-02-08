package br.com.abril.nds.process.correcaovendas;

import br.com.abril.nds.dao.CotaDAO;
import br.com.abril.nds.model.Estudo;
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

    public CorrecaoVendas() {
	super(new Estudo());
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

	// TODO Popular o estudo - Criar Logica para chamar subProcesso
	// FIXME Retirar esse trecho
	Estudo estudo = (Estudo) super.getGenericDTO();
	estudo.setCotas(new CotaDAO().getCotas());

	CorrecaoTendencia correcaoTendencia = new CorrecaoTendencia(estudo);

	correcaoTendencia.executar();

	super.genericDTO = correcaoTendencia.getGenericDTO();

    }

}

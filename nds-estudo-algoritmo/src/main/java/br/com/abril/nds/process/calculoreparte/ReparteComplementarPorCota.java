package br.com.abril.nds.process.calculoreparte;

import java.util.ArrayList;
import java.util.List;

import br.com.abril.nds.model.ClassificacaoCota;
import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.process.ProcessoAbstrato;

/**
 * Processo que tem como objetivo efetuar o cálculo da divisão do reparte entre
 * as cotas encontradas para o perfil definido no setup do estudo, levando em
 * consideração todas as variáveis também definidas no setup.
 * <p style="white-space: pre-wrap;">SubProcessos:
 * 	- N/A
 * Processo Pai:
 * 	- {@link CalcularReparte}
 * 
 * Processo Anterior: {@link AjusteFinalReparte}
 * Próximo Processo: {@link GravarReparteFinalCota}
 * </p>
 */
public class ReparteComplementarPorCota extends ProcessoAbstrato {

    private List<Ordenador> ordenadorList = new ArrayList<Ordenador>();

    public ReparteComplementarPorCota(Estudo estudo) {
	super(estudo);

	//	Prioridade de recebimento de reparte:

	/*
	 * A: As que nao receberam as edicoes-base, porem receberam a edicao aberta, caso exista, 
	 * da maior para menor no ranking de segmento da publicação(cotas SH);
	 */
	ordenadorList.add(new Ordenador("A"){
	    @Override
	    boolean filtrar(Cota cota) {
		if(cota.getClassificacao().equals(ClassificacaoCota.BancaComReparteZeroMinimoZeroCotaAntiga)){
		    return true;
		}
		return false;
	    }
	});

	/*
	 * B: As que não receberam as edições-base, da maior para a menor no ranking de segmento da publicação (cotas SH);
	 */
	ordenadorList.add(new Ordenador("B"){
	    @Override
	    boolean filtrar(Cota cota) {
		if(cota.getClassificacao().equals(ClassificacaoCota.BancaComReparteZeroMinimoZeroCotaAntiga)){
		    return true;
		}
		return false;
	    }
	});

	/*
	 * C: As que receberam 1 edição das edições-base, da maior para a menor no ranking de segmento da publicação (cotas VZ);
	 */
	ordenadorList.add(new Ordenador("C"){
	    @Override
	    boolean filtrar(Cota cota) {
		if(cota.getClassificacao().equals(ClassificacaoCota.BancaComTotalVendaZeraMinimoZeroCotaAntiga)){
		    return true;
		}
		return false;
	    }
	});

	/*
	 * D: As que receberam 2 edições das edições-base, da maior para a menor no ranking de segmento da publicação (cotas VZ);
	 */
	ordenadorList.add(new Ordenador("D"){
	    @Override
	    boolean filtrar(Cota cota) {
		if(cota.getClassificacao().equals(ClassificacaoCota.BancaComTotalVendaZeraMinimoZeroCotaAntiga)){
		    return true;
		}
		return false;
	    }
	});

	/*
	 * E: As que receberam 3 ou mais edições das edições-base, da maior para a menor no ranking de segmento da publicação (cotas VZ).
	 */
	ordenadorList.add(new Ordenador("E"){
	    @Override
	    boolean filtrar(Cota cota) {
		if(cota.getClassificacao().equals(ClassificacaoCota.BancaComTotalVendaZeraMinimoZeroCotaAntiga)){
		    return true;
		}
		return false;
	    }
	});

    }

    @Override
    protected void executarProcesso() {

	//    	1)	Listar todas as cotas ativas que não entraram no Estudo Normal, considerando-se as exclus�es por CLASSIFICAÇÃO, SEGMENTO e MIX; 
	//    	2)	Excluir Cotas que não recebem Complementar ( marcado no Cadastro de Cotas )
	List<Cota> cotaListRecebeComplementar = new ArrayList<Cota>();
	List<Cota> cotaListOrdenada = new ArrayList<Cota>();

	for(Cota cota:getEstudo().getCotas()){
	    if (cota.isRecebeReparteComplementar() == false && (
		    cota.getClassificacao().notIn(ClassificacaoCota.BancaSemClassificacaoDaPublicacao,
			    ClassificacaoCota.BancaQueRecebemDeterminadoSegmento, ClassificacaoCota.CotaMix))){
		cotaListRecebeComplementar.add(cota);
	    }
	}

	//    	3)	Ordená-las na seguinte prioridade de recebimento de reparte:
	for(Ordenador ordenador:this.ordenadorList){
	    for(Cota c:cotaListRecebeComplementar){
		if(ordenador.filtrar(c)){
		    //TODO: FAZER ORDENACAO
		    cotaListOrdenada.add(c);
		}
	    }
	}

	/*
	 * 4)	As bancas receberão a quantidade de reparte por banca definido no estudo (default = 2 exemplares)
	 *  ou 1 pacote-padrão se a distribuição for por múltiplos 
	 * até acabar o reparte complementar, sempre considerando-se a priorização acima. 
	 * Caso haja saldo a distribuir e todas as bancas selecionadas já receberam, 
	 * enviar 1 exemplar ou 1 pacote-padrão se a distribuição for por múltiplos para as bancas do estudo normal, 
	 * da maior para a menor até finalizar o estoque. 
	 * Não incluir bancas marcadas com `FX` `MX` e `MM` nessa redistribuição;
	 */
	for(Cota c:cotaListOrdenada){
	    if (c.getClassificacao().notIn(ClassificacaoCota.ReparteFixado, ClassificacaoCota.CotaMix, ClassificacaoCota.MaximoMinimo)){
		//TODO: FAZER REDISTRIBUICAO
		//    			5)	Marcar cotas com �CP�
		c.setClassificacao(ClassificacaoCota.BancaEstudoComplementar);
	    }
	}

	//getEstudo().setCotas(cotaListOrdenada);

    }

    // FIXME: talvez usar o Guava do google para ordenar?
    // TODO: verificar se o código está correto
    private abstract class Ordenador{
	//	private String name;
	public Ordenador(String name){
	    //	    this.setName(name);
	}
	abstract boolean filtrar(Cota cota);

	//	public String getName() {
	//	    return name;
	//	}
	//	public void setName(String name) {
	//	    this.name = name;
	//	}
    }
}

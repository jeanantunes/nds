package br.com.abril.nds.process.ajustefinalreparte;

import java.math.BigInteger;
import java.util.LinkedList;

import org.springframework.stereotype.Component;

import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.estudo.ClassificacaoCota;
import br.com.abril.nds.model.estudo.CotaEstudo;
import br.com.abril.nds.model.estudo.EstudoTransient;
import br.com.abril.nds.process.ProcessoAbstrato;
import br.com.abril.nds.process.calculoreparte.CalcularReparte;

/**
 * Processo que tem como objetivo efetuar o cálculo da divisão do reparte entre as cotas encontradas para o perfil definido no
 * setup do estudo, levando em consideração todas as variáveis também definidas no setup.
 * <p style="white-space: pre-wrap;">
 * SubProcessos: - N/A Processo Pai: - {@link CalcularReparte}
 * 
 * Processo Anterior: {@link AjusteFinalReparte} Próximo Processo: {@link GravarReparteFinalCota}
 * </p>
 */
@Component
public class ReparteComplementarPorCota extends ProcessoAbstrato {

    LinkedList<CotaEstudo> listaOrdenada = new LinkedList<>();

    @Override
    public void executar(EstudoTransient estudo) throws Exception {
	ordenarLista(estudo);
	distribuirReparteComplementar(estudo);
    }

    private void ordenarLista(EstudoTransient estudo) {
	// Lista de cotas que não receberam as edições-base, porém receberam a edição aberta
	LinkedList<CotaEstudo> listaA = new LinkedList<>();
	// Lista de cotas que não receberam as edições-base
	LinkedList<CotaEstudo> listaB = new LinkedList<>();
	// Lista de cotas que receberam 1 edição base
	LinkedList<CotaEstudo> listaC = new LinkedList<>();
	// Lista de cotas que receberam 2 edições base
	LinkedList<CotaEstudo> listaD = new LinkedList<>();
	// Lista de cotas que receberam 3 ou mais edições das edições base
	LinkedList<CotaEstudo> listaE = new LinkedList<>();

	for (CotaEstudo cota : estudo.getCotasExcluidas()) {
	    if ((cota.getReparteCalculado().compareTo(BigInteger.ZERO) == 0) && cota.isRecebeReparteComplementar()
		    && cota.getSituacaoCadastro().equals(SituacaoCadastro.ATIVO)) {
		if ((cota.getEdicoesRecebidas().size() == 0) && (cota.getClassificacao().equals(ClassificacaoCota.BancaSemHistorico)) &&
			(cota.isRecebeuUltimaEdicaoAberta())) {
		    listaA.add(cota);
		} else if ((cota.getEdicoesRecebidas().size() == 0) && (cota.getClassificacao().equals(ClassificacaoCota.BancaSemHistorico))) {
		    listaB.add(cota);
		} else if ((cota.getEdicoesRecebidas().size() == 1) && (cota.getClassificacao().equals(ClassificacaoCota.BancaComVendaZero))) {
		    listaC.add(cota);
		} else if ((cota.getEdicoesRecebidas().size() == 2) && (cota.getClassificacao().equals(ClassificacaoCota.BancaComVendaZero))) {
		    listaD.add(cota);
		} else if ((cota.getEdicoesRecebidas().size() >= 3) && (cota.getClassificacao().equals(ClassificacaoCota.BancaComVendaZero))) {
		    listaE.add(cota);
		}
	    }
	}
	listaOrdenada.addAll(listaA);
	listaOrdenada.addAll(listaB);
	listaOrdenada.addAll(listaC);
	listaOrdenada.addAll(listaD);
	listaOrdenada.addAll(listaE);
    }

    private void distribuirReparteComplementar(EstudoTransient estudo) {
	BigInteger reparte = BigInteger.valueOf(2);
	if (estudo.isDistribuicaoPorMultiplos()) {
	    reparte = estudo.getPacotePadrao();
	}
	for (CotaEstudo cota : listaOrdenada) {
	    cota.setReparteCalculado(cota.getReparteCalculado().add(reparte));
	    cota.setClassificacao(ClassificacaoCota.BancaEstudoComplementar);
	    estudo.setReparteComplementar(estudo.getReparteComplementar().subtract(reparte));
	    if (estudo.getReparteComplementar().compareTo(BigInteger.ZERO) <= 0) {
		break;
	    }
	}
	BigInteger reparteGeral = BigInteger.ONE;
	while (estudo.getReparteComplementar().compareTo(BigInteger.ZERO) > 0) {
	    for (CotaEstudo cota : estudo.getCotas()) {
		cota.setReparteCalculado(cota.getReparteCalculado().add(reparteGeral));
		estudo.setReparteComplementar(estudo.getReparteComplementar().subtract(reparte));
		if (estudo.getReparteComplementar().compareTo(BigInteger.ZERO) <= 0) {
		    break;
		}
	    }
	}
    }

    // private List<Ordenador> ordenadorList = new ArrayList<>();
    //
    // @Autowired
    // private RankingSegmentoDAO rankingSegmentoDAO;
    //
    // private List<Long> cotasIdList;
    //
    // private EstudoTransient estudoTransient;
    //
    // public void initComponents() {
    // cotasIdList = rankingSegmentoDAO.getCotasOrdenadasMaiorMenor(estudoTransient.getCotas(),
    // estudoTransient.getProdutoEdicaoEstudo());
    //
    // // Prioridade de recebimento de reparte:
    //
    // /*
    // * A: As que nao receberam as edicoes-base, porem receberam a edicao aberta, caso exista, da maior para menor no ranking
    // * de segmento da publicação(cotas SH);
    // */
    // ordenadorList.add(new Ordenador() {
    //
    // @Override
    // void filtrar(List<CotaEstudo> cotaListRecebeComplementar) {
    //
    // List<CotaEstudo> cList = new ArrayList<>();
    // for (CotaEstudo cota : cotaListRecebeComplementar) {
    // if (cota.getClassificacao().equals(ClassificacaoCota.BancaComReparteZeroMinimoZeroCotaAntiga)
    // && cota.isCotaSoRecebeuEdicaoAberta()) {
    // /*
    // * Se idProduto das recebidas é igual ao idProduto das edicoes-base então recebeu edicaoAberta
    // */
    // for (ProdutoEdicaoEstudo pe : cota.getEdicoesRecebidas()) {
    // for (ProdutoEdicaoEstudo edBase : estudoTransient.getEdicoesBase()) {
    // if (pe.getProduto().getId().equals(edBase.getProduto().getId())) {
    // cList.add(cota);
    // }
    // }
    // }
    // }
    // }
    // realizarReparteComplementar(cList);
    // }
    // });
    //
    // /*
    // * B: As que n�o receberam as edi��es-base, da maior para a menor no ranking de segmento da publica��o (cotas SH);
    // */
    // ordenadorList.add(new Ordenador() {
    // @Override
    // void filtrar(List<CotaEstudo> cotaListRecebeComplementar) {
    //
    // List<CotaEstudo> cList = new ArrayList<>();
    // for (CotaEstudo cota : cotaListRecebeComplementar) {
    // if (cota.getClassificacao().equals(ClassificacaoCota.BancaComReparteZeroMinimoZeroCotaAntiga)
    // && cota.isCotaSoRecebeuEdicaoAberta()) {
    //
    // for (ProdutoEdicaoEstudo pe : cota.getEdicoesRecebidas()) {
    // for (ProdutoEdicaoEstudo edBase : estudoTransient.getEdicoesBase()) {
    // if (pe.getProduto().getId().equals(edBase.getProduto().getId())) {
    // continue;
    // }
    // }
    // }
    // cList.add(cota);
    // }
    // }
    // realizarReparteComplementar(cList);
    // }
    // });
    //
    // /*
    // * C: As que receberam 1 ediçãoo das ediçõees-base, da maior para a menor no ranking de segmento da publicação (cotas VZ);
    // */
    // ordenadorList.add(new Ordenador() {
    // @Override
    // void filtrar(List<CotaEstudo> cotaListRecebeComplementar) {
    // List<CotaEstudo> cList = new ArrayList<>();
    // for (CotaEstudo cota : cotaListRecebeComplementar) {
    // if (cota.getClassificacao().equals(ClassificacaoCota.BancaComTotalVendaZeraMinimoZeroCotaAntiga)
    // && getQtdeEdicoesBaseRecebida(cota) == 1) {
    // cList.add(cota);
    // }
    // }
    // realizarReparteComplementar(cList);
    // }
    // });
    //
    // /*
    // * D: As que receberam 2 edi��es das edi��es-base, da maior para a menor no ranking de segmento da publica��o (cotas VZ);
    // */
    // ordenadorList.add(new Ordenador() {
    // @Override
    // void filtrar(List<CotaEstudo> cotaListRecebeComplementar) {
    // List<CotaEstudo> cList = new ArrayList<>();
    // for (CotaEstudo cota : cotaListRecebeComplementar) {
    // if (cota.getClassificacao().equals(ClassificacaoCota.BancaComTotalVendaZeraMinimoZeroCotaAntiga)
    // && getQtdeEdicoesBaseRecebida(cota) == 2) {
    // cList.add(cota);
    // }
    // }
    // realizarReparteComplementar(cList);
    // }
    // });
    //
    // /*
    // * E: As que receberam 3 ou mais edi��es das edi��es-base, da maior para a menor no ranking de segmento da publica��o
    // * (cotas VZ).
    // */
    // ordenadorList.add(new Ordenador() {
    // @Override
    // void filtrar(List<CotaEstudo> cotaListRecebeComplementar) {
    // List<CotaEstudo> cList = new ArrayList<>();
    // for (CotaEstudo cota : cotaListRecebeComplementar) {
    // if (cota.getClassificacao().equals(ClassificacaoCota.BancaComTotalVendaZeraMinimoZeroCotaAntiga)
    // && getQtdeEdicoesBaseRecebida(cota) >= 3) {
    // cList.add(cota);
    // }
    // }
    // realizarReparteComplementar(cList);
    // }
    // });
    // }
    //
    // @Override
    // public void executar(EstudoTransient estudo) {
    // estudoTransient = estudo;
    // initComponents();
    //
    // // 1) Listar todas as cotas ativas que não entraram no Estudo Normal, considerando-se as exclusões por CLASSIFICAÇÃO,
    // // SEGMENTO e MIX;
    // // 2) Excluir Cotas que não recebem Complementar ( marcado no Cadastro de Cotas )
    // List<CotaEstudo> cotaListRecebeComplementar = new ArrayList<>();
    //
    // for (CotaEstudo cota : estudo.getCotas()) {
    // if (cota.isRecebeReparteComplementar() == false
    // && (!cota.getClassificacao().equals(ClassificacaoCota.BancaSemClassificacaoDaPublicacao)
    // && !cota.getClassificacao().equals(ClassificacaoCota.BancaQueRecebemDeterminadoSegmento) && !cota.getClassificacao()
    // .equals(ClassificacaoCota.CotaMix))) {
    // cotaListRecebeComplementar.add(cota);
    // }
    // }
    //
    // // 3) Ordena-las na seguinte prioridade de recebimento de reparte:
    // if (estudo.getReparteComplementar() != null) {
    // loop: while (estudo.getReparteComplementar().compareTo(BigInteger.ZERO) == 1) {
    // for (Ordenador ordenador : this.ordenadorList) {
    // ordenador.filtrar(cotaListRecebeComplementar);
    // if (estudo.getReparteComplementar().compareTo(BigInteger.ZERO) <= 0) {
    // break loop;
    // }
    // }
    // }
    // }
    //
    // /*
    // * 4) As bancas receberao a quantidade de reparte por banca definido no estudo (default = 2 exemplares) ou 1 pacote-padr�o
    // * se a distribuicao for por multiplos ate acabar o reparte complementar, sempre considerando-se a prioriza��o acima.
    // *
    // * Caso haja saldo a distribuir e todas as bancas selecionadas j� receberam, enviar 1 exemplar ou 1 pacote-padr�o se a
    // * distribui��o for por m�ltiplos para as bancas do estudo normal, da maior para a menor at� finalizar o estoque. N�o
    // * incluir bancas marcadas com `FX` `MX` e `MM` nessa redistribui��o;
    // */
    // while (estudo.getReparteDistribuir().compareTo(BigInteger.ZERO) == 1) {
    // for (CotaEstudo c : estudo.getCotas()) {
    // if (!c.getClassificacao().equals(ClassificacaoCota.ReparteFixado) &&
    // !c.getClassificacao().equals(ClassificacaoCota.CotaMix)
    // && !c.getClassificacao().equals(ClassificacaoCota.MaximoMinimo)) {
    //
    // // TODO: fazer redistribuicao
    // // 5) Marcar cotas com 'CP'
    // c.setClassificacao(ClassificacaoCota.BancaEstudoComplementar);
    // c.setReparteCalculado(c.getReparteCalculado().add(BigInteger.ONE));
    // estudo.setReparteDistribuir(estudo.getReparteDistribuir().subtract(BigInteger.ONE));
    // }
    // }
    // }
    // }
    //
    // public int getQtdeEdicoesBaseRecebida(CotaEstudo cota) {
    //
    // List<ProdutoEdicaoEstudo> edicoesRecebidas = cota.getEdicoesRecebidas();
    // List<ProdutoEdicaoEstudo> edicoesBase = estudoTransient.getEdicoesBase();
    //
    // int qtdeEdicoesBaseRecebidas = 0;
    //
    // for (ProdutoEdicaoEstudo produtoEdicaoBase : edicoesBase) {
    // for (ProdutoEdicaoEstudo edRec : edicoesRecebidas) {
    // if (edRec.getId().equals(produtoEdicaoBase.getId())) {
    // qtdeEdicoesBaseRecebidas++;
    // }
    // }
    // }
    // return qtdeEdicoesBaseRecebidas;
    // }
    //
    // private void realizarReparteComplementar(List<CotaEstudo> cList) {
    // /*
    // * 4) As bancas receberão a quantidade de reparte por banca definido no estudo (default = 2 exemplares) ou 1 pacote-padrão
    // * se a distribuição for por múltiplos até acabar o reparte complementar, sempre considerando-se a priorização acima.
    // */
    // for (Long id : cotasIdList) {
    // for (CotaEstudo c : cList) {
    // if (c.getId().equals(id)) {
    // if (estudoTransient.isDistribuicaoPorMultiplos()) {
    // c.setReparteCalculado(c.getReparteCalculado().add(estudoTransient.getPacotePadrao()));
    // } else {
    //
    // }
    // estudoTransient.setReparteComplementar(estudoTransient.getReparteComplementar().subtract(BigInteger.ONE));
    //
    // if (estudoTransient.getReparteComplementar().compareTo(BigInteger.ZERO) <= 0) {
    // return;
    // }
    // }
    // }
    // }
    // }
    //
    // // XXX talvez usar o Guava do google para ordenar?
    // private abstract class Ordenador {
    // abstract void filtrar(List<CotaEstudo> cotaListRecebeComplementar);
    // }
}

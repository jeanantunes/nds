package br.com.abril.nds.process.verificartotalfixacoes;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDate;
import org.joda.time.Years;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.dao.CotaDAO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.estudo.ClassificacaoCota;
import br.com.abril.nds.model.estudo.CotaDesenglobada;
import br.com.abril.nds.model.estudo.CotaEnglobada;
import br.com.abril.nds.model.estudo.CotaEstudo;
import br.com.abril.nds.model.estudo.EstudoTransient;
import br.com.abril.nds.model.estudo.ProdutoEdicaoEstudo;
import br.com.abril.nds.process.ProcessoAbstrato;
import br.com.abril.nds.service.EstudoAlgoritmoService;
import br.com.abril.nds.util.BigDecimalUtil;

/**
 * Processo que tem como objetivo efetuar o cálculo da divisão do reparte entre as cotas encontradas para o perfil definido no
 * setup do estudo, levando em consideração todas as variáveis também definidas no setup.
 * <p style="white-space: pre-wrap;">
 * SubProcessos: - N/A Processo Pai: - {@link VerificarTotalFixacoes}
 * 
 * Processo Anterior: N/A Próximo Processo: N/A
 * </p>
 */
@Component
public class SelecaoBancas extends ProcessoAbstrato {

    private static final BigDecimal BIGDECIMAL_100 = BigDecimal.valueOf(100);

    @Autowired
    private CotaDAO cotaDAO;

    @Autowired
    private EstudoAlgoritmoService estudoAlgoritmoService;

    @Override
    public void executar(EstudoTransient estudo) throws Exception {

	List<CotaEstudo> cotas = cotaDAO.getCotas(estudo);
	List<Map<Long, CotaEstudo>> historico = new ArrayList<>();
	
	for (ProdutoEdicaoEstudo edicao : estudo.getEdicoesBase()) {
	    historico.add(cotaDAO.getHistoricoCota(edicao));
	}

	boolean existeCotaComHistorico = false;
	for (CotaEstudo cota : cotas) {
	    
		cota.setEdicoesRecebidas(new ArrayList<ProdutoEdicaoEstudo>());
	
		for (Map<Long, CotaEstudo> item : historico) {
			if (item.get(cota.getId()) != null) {
			    cota.getEdicoesRecebidas().addAll(item.get(cota.getId()).getEdicoesRecebidas());
			}
	    }
	    
		if (cota.getEdicoesRecebidas().size() > 0) {
	    	existeCotaComHistorico = true;
	    }
	}

	if (!existeCotaComHistorico) {
	    throw new ValidacaoException(TipoMensagem.WARNING, "Não foram encontradas cotas com historico para estas edições de base.");
	}

	Map<Long, CotaEstudo> cotasComHistoricoMap = new LinkedHashMap<>();

	
	
	
	
	// excluindo cotas que nao recebem o segmento, ou nao recebem a classificacao ou sao alternativas e nao possuem mix
    // ou nao recebem do fornecedor
	for (CotaEstudo cota : cotas) {
	    if (cota.getClassificacao().in(ClassificacaoCota.BancaMixSemDeterminadaPublicacao, ClassificacaoCota.CotaNaoRecebeEsseSegmento,
		    ClassificacaoCota.BancaSemClassificacaoDaPublicacao, ClassificacaoCota.BancaSuspensa, ClassificacaoCota.CotaNaoRecebeDesseFornecedor) ||
		    cota.getSituacaoCadastro().equals(SituacaoCadastro.SUSPENSO)) {
	    	estudo.getCotasExcluidas().add(cota);
	    }
	    cotasComHistoricoMap.put(cota.getId(), cota);
	}
	
	for (CotaEstudo cota : estudo.getCotasExcluidas()) {
	    
		cotasComHistoricoMap.remove(cota.getId());
		
		//Calcular venda média cotas excluídas
		calcularTotais(cota, estudo);
	}
	// fim da exclusao
	
	for (CotaEstudo cota : cotasComHistoricoMap.values()) {
	    if (cota.getClassificacao().equals(ClassificacaoCota.CotaNova) && cota.getEdicoesRecebidas().size() >= 3) {
	    	cota.setClassificacao(ClassificacaoCota.SemClassificacao);
	    }
	    calcularTotais(cota, estudo);
	}
	
	tratarCotasComEnglobacao(cotasComHistoricoMap);

	cotaDAO.getComponentesCota(cotas);

	List<Long> idsCotas = new ArrayList<>();
	for (CotaEstudo cota : cotasComHistoricoMap.values()) {
	    // excluindo as cotas que não entram no estudo
	    if (cota.getClassificacao().in(ClassificacaoCota.BancaComVendaZero, ClassificacaoCota.BancaSemHistorico, ClassificacaoCota.ReparteFixado)) {
			// separando as cotas que podem e as que nao podem receber parciais
			if (estudo.getProdutoEdicaoEstudo().getPeriodo() > 1 && !cota.isRecebeParcial() && !cota.isExcecaoParcial()) {
			    cota.setReparteCalculado(BigInteger.ZERO);
			    estudo.getCotasForaDaRegiao().add(cota);
			} else {
			    if (cota.getClassificacao().equals(ClassificacaoCota.BancaSemHistorico)) {
			    	idsCotas.add(cota.getId());
			    }
			    estudo.getCotasExcluidas().add(cota);
			}	
	    }
	}
	for (CotaEstudo cota : estudo.getCotasExcluidas()) {
	    cotasComHistoricoMap.remove(cota.getId());
	}
	for (CotaEstudo cota : estudo.getCotasForaDaRegiao()) {
	    cotasComHistoricoMap.remove(cota.getId());
	}

	// removendo cotas que não podem receber reparte parcial
	for (CotaEstudo cota : cotasComHistoricoMap.values()) {
	    if (estudo.getProdutoEdicaoEstudo().getPeriodo() > 1 && !cota.isRecebeParcial() && !cota.isExcecaoParcial()) {
			cota.setReparteCalculado(BigInteger.ZERO);
			cota.setClassificacao(ClassificacaoCota.BancaForaDaRegiaoDistribuicao);
			estudo.getCotasForaDaRegiao().add(cota);
	    }
	}
	
	for (CotaEstudo cota : estudo.getCotasForaDaRegiao()) {
	    cotasComHistoricoMap.remove(cota.getId());
	}
	
	// removendo cotas que não podem receber reparte parcial dentre as cotas sem histórico (SH) e com venda zero (VZ)
	for (CotaEstudo cota : estudo.getCotasForaDaRegiao()) {
	    if (estudo.getProdutoEdicaoEstudo().getPeriodo() > 1 && !cota.isRecebeParcial() && !cota.isExcecaoParcial() &&
		    cota.getClassificacao().in(ClassificacaoCota.BancaComVendaZero, ClassificacaoCota.BancaSemHistorico)) {
			cota.setReparteCalculado(BigInteger.ZERO);
			cota.setClassificacao(ClassificacaoCota.BancaForaDaRegiaoDistribuicao);
	    }
	}
	
	// removendo cotas fora da regiao
	if (estudo.getDistribuicaoVendaMediaDTO() != null) {
	    validarComponentes(new LinkedList<CotaEstudo>(cotasComHistoricoMap.values()), estudo);
	}
	for (CotaEstudo cota : cotasComHistoricoMap.values()) {
	    if (cota.getClassificacao().equals(ClassificacaoCota.BancaForaDaRegiaoDistribuicao)) {
	    	estudo.getCotasForaDaRegiao().add(cota);
	    }
	}
	for (CotaEstudo cota : estudo.getCotasForaDaRegiao()) {
	    cotasComHistoricoMap.remove(cota.getId());
	} // fim da remocao

	if (idsCotas.size() > 0) {
	    List<Long> numerosEdicao = new ArrayList<>();
	    if (estudo.getProdutoEdicaoEstudo().getNumeroEdicao() > 1) {
	    	numerosEdicao.add(estudo.getProdutoEdicaoEstudo().getNumeroEdicao() - 1);
	    }
	    if (estudo.getProdutoEdicaoEstudo().getNumeroEdicao() > 2) {
	    	numerosEdicao.add(estudo.getProdutoEdicaoEstudo().getNumeroEdicao() - 2);
	    }
	    if (numerosEdicao.size() > 0) {
	    	List<Long> cotasQueReceberam = cotaDAO.buscarCotasQueReceberamUltimaEdicaoAberta(estudo.getProdutoEdicaoEstudo().getProduto().getCodigo(), idsCotas, numerosEdicao);
			
	    	for (CotaEstudo cota : estudo.getCotasExcluidas()) {
			    if (cotasQueReceberam.contains(cota.getId())) {
			    	cota.setRecebeuUltimaEdicaoAberta(true);
			    }
			}
	    }
	}
	
	estudo.setCotas(new LinkedList<>(cotasComHistoricoMap.values()));
    }

    private List<CotaEstudo> validarComponentes(List<CotaEstudo> cotas, EstudoTransient estudo) {

	// selecao de componente/elemento
	if (estudo.getDistribuicaoVendaMediaDTO().getComponente() != null && estudo.getDistribuicaoVendaMediaDTO().getElemento() != null) {
	    estudo.setComplementarAutomatico(false);
	    for (CotaEstudo cota : cotas) {
			String [] vetor = {estudo.getDistribuicaoVendaMediaDTO().getElemento(),
					estudo.getDistribuicaoVendaMediaDTO().getElemento2(),estudo.getDistribuicaoVendaMediaDTO().getElemento3()};
			if (!estudoAlgoritmoService.isCotaDentroDoComponenteElemento(estudo.getDistribuicaoVendaMediaDTO().getComponente(), vetor, cota)) {
			    cota.setClassificacao(ClassificacaoCota.BancaForaDaRegiaoDistribuicao);
			    cota.setReparteCalculado(BigInteger.ZERO, estudo);
			}
	    }
	}

	// marcando bancas que estao fora do percentual de abrangencia
	if (estudo.getDistribuicaoVendaMediaDTO().getAbrangenciaCriterio() != null &&
		estudo.getDistribuicaoVendaMediaDTO().getAbrangencia() != null) {
	    estudo.setComplementarAutomatico(false);
	    if (estudo.getDistribuicaoVendaMediaDTO().getAbrangenciaCriterio().equalsIgnoreCase("Segmento")) {
		Collections.sort(cotas, new Comparator<CotaEstudo>() {

		    @Override
		    public int compare(CotaEstudo cota1, CotaEstudo cota2) {
			return cota2.getQtdeRankingSegmento().compareTo(cota1.getQtdeRankingSegmento());
		    }
		});
	    } else if (estudo.getDistribuicaoVendaMediaDTO().getAbrangenciaCriterio().equalsIgnoreCase("Faturamento")) {
		Collections.sort(cotas, new Comparator<CotaEstudo>() {

		    @Override
		    public int compare(CotaEstudo cota1, CotaEstudo cota2) {
			return cota2.getQtdeRankingFaturamento().compareTo(cota1.getQtdeRankingFaturamento());
		    }
		});
	    } else {
		Collections.sort(cotas, new Comparator<CotaEstudo>() {

		    @Override
		    public int compare(CotaEstudo cota1, CotaEstudo cota2) {
			return cota2.getVendaMedia().compareTo(cota1.getVendaMedia());
		    }
		});
	    }
	    BigDecimal qtdeCotasAtivas = BigDecimal.ZERO;
	    for (CotaEstudo cota : cotas) {
			if (cota.getSituacaoCadastro().equals(SituacaoCadastro.ATIVO)) {
			    qtdeCotasAtivas = qtdeCotasAtivas.add(BigDecimal.ONE);
			}
	    }
	    for (CotaEstudo cota : estudo.getCotasExcluidas()) {
			if (cota.getSituacaoCadastro().equals(SituacaoCadastro.ATIVO)) {
			    qtdeCotasAtivas = qtdeCotasAtivas.add(BigDecimal.ONE);
			}
	    }
	    for (CotaEstudo cota : estudo.getCotasForaDaRegiao()) {
			if (cota.getSituacaoCadastro().equals(SituacaoCadastro.ATIVO)) {
			    qtdeCotasAtivas = qtdeCotasAtivas.add(BigDecimal.ONE);
			}
	    }
	    
	    BigDecimal abrangencia = new BigDecimal(estudo.getDistribuicaoVendaMediaDTO().getAbrangencia()).multiply(BigDecimal.valueOf(0.01));
	    BigDecimal qtdeCotasAbrangencia = qtdeCotasAtivas.multiply(abrangencia);
	    qtdeCotasAbrangencia = qtdeCotasAbrangencia.setScale(0, BigDecimal.ROUND_HALF_UP);
	    // lista utilizada para que utilizemos apenas as bancas ativas
	    
	    List<CotaEstudo> temp = new ArrayList<>();
	    for (CotaEstudo cota : cotas) {
			if (cota.getSituacaoCadastro().equals(SituacaoCadastro.ATIVO)) {
			    temp.add(cota);
			}
	    }

	    boolean estudoTemReparteMinimo = estudo.getReparteMinimo() != null;
	    for (int i = 0; i < temp.size(); i++) {
			if (BigDecimal.valueOf(i).compareTo(qtdeCotasAbrangencia) >= 0 &&
				!(temp.get(i).isNova() && estudoTemReparteMinimo)) {// se houver reparte minimo não remove as cotas novas do estudo		 
			    temp.get(i).setClassificacao(ClassificacaoCota.BancaForaDaRegiaoDistribuicao);
			    temp.get(i).setReparteCalculado(BigInteger.ZERO, estudo);
			    
			    temp.get(i).setReparteMinimoFinal(BigInteger.ONE);
			}
	    }
	}

	// marcando bancas fora da regiao de distribuicao
	if (estudo.getDistribuicaoVendaMediaDTO() != null && !estudo.getDistribuicaoVendaMediaDTO().isCotasAVista()) {
	    for (CotaEstudo cota : cotas) {
			for (String item : cota.getTiposCota()) {
			    if (item.equals("A_VISTA")) {
					cota.setClassificacao(ClassificacaoCota.BancaForaDaRegiaoDistribuicao);
					cota.setReparteCalculado(BigInteger.ZERO, estudo);
			    }
			}
	    }
	}

		// removendo excecoes da lista de cotas
		if (estudo.getDistribuicaoVendaMediaDTO().getExcecaoDeBancasComponente() != null &&
			estudo.getDistribuicaoVendaMediaDTO().getExcecaoDeBancas().size() > 0) {
		    
			String[] vetor = new String[estudo.getDistribuicaoVendaMediaDTO().getExcecaoDeBancas().size()];
		    for (int i = 0; i < estudo.getDistribuicaoVendaMediaDTO().getExcecaoDeBancas().size(); i++) {
		    	vetor[i] = estudo.getDistribuicaoVendaMediaDTO().getExcecaoDeBancas().get(i); 
		    }
		    
		    for (CotaEstudo cota : cotas) {
				if (cota.getClassificacao().notIn(ClassificacaoCota.BancaComVendaZero, ClassificacaoCota.BancaSemHistorico)) {		    
				    if (estudoAlgoritmoService.isCotaDentroDoComponenteElemento(estudo.getDistribuicaoVendaMediaDTO().getExcecaoDeBancasComponente(), vetor, cota)) {
				    	cota.setClassificacao(ClassificacaoCota.BancaForaDaRegiaoDistribuicao);
				    	cota.setReparteCalculado(BigInteger.ZERO, estudo);
				    }
				}
		    }
		}
		return cotas;
    }

    private void calcularTotais(CotaEstudo cota, EstudoTransient estudo) {
	BigDecimal totalEdicoes = BigDecimal.ZERO;
	BigDecimal totalVenda = BigDecimal.ZERO;
	BigDecimal totalReparte = BigDecimal.ZERO;

	// Verificacao se a cota so recebeu edicoes abertas e somatoria delas
	if (cota.getEdicoesRecebidas().size() > 0) {
	    cota.setCotaSoRecebeuEdicaoAberta(true);
	}
	cota.setSomaReparteEdicoesAbertas(BigDecimal.ZERO);

	for (ProdutoEdicaoEstudo edicao : cota.getEdicoesRecebidas()) {
	    
		totalEdicoes = totalEdicoes.add(BigDecimal.ONE);
	    totalVenda   = BigDecimalUtil.isMaiorQueZero(edicao.getVenda()) ? totalVenda.add(edicao.getVenda()) : totalVenda.add(BigDecimal.ZERO);
	    totalReparte = totalReparte.add(edicao.getReparte());
	    
	    if (edicao.isEdicaoAberta()) {
	    	cota.setSomaReparteEdicoesAbertas(cota.getSomaReparteEdicoesAbertas().add(edicao.getReparte()));
	    } else {
			if (cota.getVendaEdicaoMaisRecenteFechada() == null) {
				if(edicao.getVenda() != null){
					cota.setVendaEdicaoMaisRecenteFechada(edicao.getVenda().setScale(0, BigDecimal.ROUND_HALF_UP).toBigInteger());
				}else{
					cota.setVendaEdicaoMaisRecenteFechada(edicao.getReparte().setScale(0, BigDecimal.ROUND_HALF_UP).toBigInteger());
				}
			}
		
			if(edicao.getVenda() == null){
				edicao.setVenda(BigDecimal.ZERO);
			}
		
			cota.setCotaSoRecebeuEdicaoAberta(false);
	    }
	}
	if (totalEdicoes.compareTo(BigDecimal.ZERO) > 0) {
	    cota.setVendaMediaNominal(totalVenda.divide(totalEdicoes, 2, BigDecimal.ROUND_HALF_UP));
	    cota.setVendaMedia(cota.getVendaMediaNominal());
	}
	if (cota.getClassificacao().notIn(ClassificacaoCota.CotaNova, ClassificacaoCota.CotaMix, ClassificacaoCota.ReparteFixado,
		ClassificacaoCota.BancaForaDaRegiaoDistribuicao) &&
		!(estudo.getDistribuicaoVendaMediaDTO() != null && estudo.getDistribuicaoVendaMediaDTO().getAbrangencia() != null &&
		estudo.getReparteMinimo() != null)) {
	    
	    if(cota.getReparteMinimo().compareTo(BigInteger.ZERO) == 0){
	    	if (totalReparte.compareTo(BigDecimal.ZERO) == 0) {
	    		cota.setClassificacao(ClassificacaoCota.BancaSemHistorico);
	    	} else if (totalVenda.compareTo(BigDecimal.ZERO) == 0) {
	    		cota.setClassificacao(ClassificacaoCota.BancaComVendaZero);
	    	}
	    }
		
	}
	if (estudo.isUsarFixacao() && cota.getReparteFixado() != null && !cota.getClassificacao().equals(ClassificacaoCota.BancaForaDaRegiaoDistribuicao)) {
	    
		cota.setClassificacao(ClassificacaoCota.ReparteFixado);

		if(estudo.isDistribuicaoPorMultiplos() && estudo.getPacotePadrao() != null){

		    BigDecimal verificador = new BigDecimal(cota.getReparteFixado()).divide(new BigDecimal(estudo.getPacotePadrao()), 0, BigDecimal.ROUND_HALF_UP);
		    
		    BigInteger reparteArredondadoOuPorMult = BigInteger.valueOf(verificador.intValue()).multiply(estudo.getPacotePadrao());
	    
		    if(verificador.intValue() < 1){
		    	cota.setReparteCalculado(estudo.getPacotePadrao(), estudo);
		    }else{
		    	cota.setReparteCalculado(reparteArredondadoOuPorMult, estudo);
		    }
	    	
	    }else{
	    	cota.setReparteCalculado(cota.getReparteFixado(), estudo);
	    }
	    
	}
    }

    private void tratarCotasComEnglobacao(Map<Long, CotaEstudo> cotasComHistoricoMap) {

	for (CotaDesenglobada cotaDesenglobada : cotaDAO.buscarCotasDesenglobadas()) {

	    if (cotasComHistoricoMap.containsKey(cotaDesenglobada.getId())) {
		if (cotasComHistoricoMap.get(cotaDesenglobada.getId()).getClassificacao().notIn(ClassificacaoCota.ReparteFixado,
			ClassificacaoCota.BancaForaDaRegiaoDistribuicao) &&
			!cotasComHistoricoMap.get(cotaDesenglobada.getId()).getSituacaoCadastro().equals(SituacaoCadastro.SUSPENSO)) {
		    cotasComHistoricoMap.get(cotaDesenglobada.getId()).setClassificacao(ClassificacaoCota.EnglobaDesengloba);
		}

		if (cotasComHistoricoMap.get(cotaDesenglobada.getId()).getEdicoesRecebidas() != null) {
		    for (ProdutoEdicaoEstudo edicaoCotaDesenglobada : cotasComHistoricoMap.get(cotaDesenglobada.getId()).getEdicoesRecebidas()) {
			BigDecimal reparteInicial = edicaoCotaDesenglobada.getReparte();
			BigDecimal vendaInicial = edicaoCotaDesenglobada.getVenda();

			for (CotaEnglobada cotaEnglobada : cotaDesenglobada.getCotasEnglobadas()) {
			    BigDecimal porcentualEnglobacao = BigDecimal.valueOf(cotaEnglobada.getPorcentualEnglobacao()).divide(BIGDECIMAL_100);

			    if (validaEnglobacaoComPeriodoVigente(cotaEnglobada.getDataInclusao())) {
					if (cotasComHistoricoMap.containsKey(cotaEnglobada.getId())) {
					    distribuiEnglobacao(reparteInicial, vendaInicial, porcentualEnglobacao, edicaoCotaDesenglobada,
					    cotasComHistoricoMap.get(cotaEnglobada.getId()));
					} else {
					    CotaEstudo cota = cotaDAO.getCotaById(cotaEnglobada.getId());
					    distribuiEnglobacao(reparteInicial, vendaInicial, porcentualEnglobacao, edicaoCotaDesenglobada, cota);
					    cotasComHistoricoMap.put(cota.getId(), cota);
					}
			    }
			}
		    }
		}
	    }
	}
    }

    private boolean validaEnglobacaoComPeriodoVigente(Date dataInclusao) {
    	return LocalDate.fromDateFields(dataInclusao).plus(Years.ONE).isAfter(LocalDate.now());
    }

    private void distribuiEnglobacao(BigDecimal reparteInicial, BigDecimal vendaInicial, BigDecimal porcentualEnglobacao,
	    ProdutoEdicaoEstudo edicaoCotaDesenglobada, CotaEstudo cotaEnglobada) {

	if (cotaEnglobada.getClassificacao() != null && cotaEnglobada.getClassificacao().notIn(ClassificacaoCota.ReparteFixado,
		ClassificacaoCota.BancaForaDaRegiaoDistribuicao) &&
		cotaEnglobada.getSituacaoCadastro() != null && !cotaEnglobada.getSituacaoCadastro().equals(SituacaoCadastro.SUSPENSO)) {

	    cotaEnglobada.setClassificacao(ClassificacaoCota.EnglobaDesengloba);
	    ProdutoEdicaoEstudo edicaoCotaEnglobada = buscaEdicaoPorNumeroLancamento(edicaoCotaDesenglobada, cotaEnglobada.getEdicoesRecebidas());

	    if (edicaoCotaDesenglobada != null) {
		BigDecimal reparteTransferir = reparteInicial.multiply(porcentualEnglobacao);
		edicaoCotaDesenglobada.setReparte(edicaoCotaDesenglobada.getReparte().subtract(reparteTransferir));
		edicaoCotaEnglobada.setReparte(edicaoCotaEnglobada.getReparte().add(reparteTransferir));

		BigDecimal vendaTransferir = vendaInicial.multiply(porcentualEnglobacao);
		edicaoCotaDesenglobada.setVenda(edicaoCotaDesenglobada.getVenda().subtract(vendaTransferir));
		edicaoCotaEnglobada.setVenda(edicaoCotaEnglobada.getVenda().add(vendaTransferir));
	    }
	}
    }

    private ProdutoEdicaoEstudo buscaEdicaoPorNumeroLancamento(ProdutoEdicaoEstudo edicaoCotaDesenglobada, List<ProdutoEdicaoEstudo> edicoesRecebidas) {
	if (edicoesRecebidas != null) {
	    for (ProdutoEdicaoEstudo produtoEdicao : edicoesRecebidas) {
			if (produtoEdicao.getNumeroEdicao().equals(edicaoCotaDesenglobada.getNumeroEdicao())) {
			    return produtoEdicao;
			}
	    }
	}
	return atualizaListaCotaEnglobadaComEdicaoClonada(edicaoCotaDesenglobada, edicoesRecebidas);
    }

    private ProdutoEdicaoEstudo atualizaListaCotaEnglobadaComEdicaoClonada(ProdutoEdicaoEstudo edicaoCotaDesenglobada,
	    List<ProdutoEdicaoEstudo> edicoesRecebidas) {
		ProdutoEdicaoEstudo produtoEdicao = new ProdutoEdicaoEstudo();
		BeanUtils.copyProperties(edicaoCotaDesenglobada, produtoEdicao);
		produtoEdicao.setReparte(BigDecimal.ZERO);
		produtoEdicao.setVenda(BigDecimal.ZERO);
	return produtoEdicao;
    }
}

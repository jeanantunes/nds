package br.com.abril.nds.controllers.lancamento;

import static br.com.caelum.vraptor.view.Results.json;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.DivisaoEstudoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.planejamento.EstudoGerado;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.EstudoProdutoEdicaoBaseService;
import br.com.abril.nds.service.EstudoService;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/dividirEstudo")
public class DividirEstudoController extends BaseController {

    // private Validator validator;

    @Autowired
    private Result result;

    @Autowired
    private HttpServletResponse httpResponse;

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private CalendarioService calendarioService;

    @Autowired
    private EstudoService estudoService;
    
    @Autowired
    private EstudoProdutoEdicaoBaseService estudoProdutoEdicaoBaseService;

    // public DividirEstudoController(Result result, HttpSession httpSession, Validator validator) {
    // this.result = result;
    // this.httpSession = httpSession;
    // this.validator = validator;
    // }

    @Path("/index")
    public void index() {

	String data = DateUtil.formatarDataPTBR(new Date());
	result.include("data", data);
    }

    @Path("/gerarDivisao")
    @Post
    public void gerarDivisao(DivisaoEstudoDTO divisaoEstudo) {

	List<String> mensagensValidacao = new ArrayList<String>();

	BigDecimal percentualDivisaoEstudoOriginal = BigDecimal.ONE;

	Integer iPercentualDivisaoPrimeiroEstudo = divisaoEstudo.getPercentualDivisaoPrimeiroEstudo();
	Integer iPercentualDivisaoSegundoEstudo = divisaoEstudo.getPercentualDivisaoSegundoEstudo();
	divisaoEstudo.setDataDistribuicaoParaPesquisa(divisaoEstudo.getDataDistribuicao());

	if (iPercentualDivisaoPrimeiroEstudo != null && iPercentualDivisaoSegundoEstudo != null) {

	    BigDecimal percentualDivisaoPrimeiroEstudo = new BigDecimal(iPercentualDivisaoPrimeiroEstudo.toString());
	    BigDecimal percentualDivisaoSegundoEstudo = new BigDecimal(iPercentualDivisaoSegundoEstudo.toString());

//	    BigDecimal somaPercentual = percentualDivisaoPrimeiroEstudo.add(percentualDivisaoSegundoEstudo).add(percentualDivisaoEstudoOriginal);
	    BigDecimal somaPercentual = percentualDivisaoPrimeiroEstudo.add(percentualDivisaoSegundoEstudo);
	    BigDecimal cem = BigDecimal.TEN.multiply(BigDecimal.TEN);

	    if (somaPercentual.compareTo(cem) != 0) {
		mensagensValidacao.add("- O total da soma do percentual da divisão deve ser igual a 100% (1% do Estudo Original e 99% dos Estudos Divididos)!");
	    } else {

	    EstudoGerado estudoOriginal = estudoService.obterEstudoByEstudoOriginalFromDivisaoEstudo(divisaoEstudo);

		if (estudoOriginal != null) {

		    BigInteger quantidadeReparte = estudoOriginal.getQtdeReparte();

		    BigDecimal divPercentualEstudoOriginal = percentualDivisaoEstudoOriginal.divide(cem, 2, BigDecimal.ROUND_FLOOR);
		    BigDecimal divPercentualPrimeiroEstudo = percentualDivisaoPrimeiroEstudo.divide(cem, 2, BigDecimal.ROUND_FLOOR);
		    BigDecimal divPercentualSegundoEstudo = percentualDivisaoSegundoEstudo.divide(cem, 2, BigDecimal.ROUND_FLOOR);

		    BigDecimal reparteEstudoOriginal = divPercentualEstudoOriginal.multiply(new BigDecimal(quantidadeReparte)).divide(BigDecimal.ONE, 0,
			    BigDecimal.ROUND_HALF_UP);
		    BigDecimal repartePrimeiroEstudo = divPercentualPrimeiroEstudo.multiply(new BigDecimal(quantidadeReparte)).divide(BigDecimal.ONE, 0,
			    BigDecimal.ROUND_HALF_UP);

		    BigDecimal reparteSegundoEstudo = divPercentualSegundoEstudo.multiply(new BigDecimal(quantidadeReparte)).divide(BigDecimal.ONE, 0,
			    BigDecimal.ROUND_HALF_UP);

		    Integer qtdeReparteDivisao = divisaoEstudo.getQuantidadeReparte();

		    estudoOriginal.setQtdeReparte(reparteEstudoOriginal.toBigInteger());

		    divisaoEstudo.setRepartePrimeiroEstudo(null);
		    divisaoEstudo.setReparteSegundoEstudo(null);
		    divisaoEstudo.setDataLancamentoPrimeiroEstudo(null);
		    divisaoEstudo.setDataLancamentoSegundoEstudo(null);

		    if (qtdeReparteDivisao != null && qtdeReparteDivisao > 0) {

			if (repartePrimeiroEstudo.compareTo(new BigDecimal(qtdeReparteDivisao)) == 1
				&& reparteSegundoEstudo.compareTo(new BigDecimal(qtdeReparteDivisao)) == 1) {

			    divisaoEstudo.setRepartePrimeiroEstudo(repartePrimeiroEstudo.toBigInteger());
			    divisaoEstudo.setReparteSegundoEstudo(reparteSegundoEstudo.toBigInteger());

			} else {
			    divisaoEstudo.setRepartePrimeiroEstudo(repartePrimeiroEstudo.add(reparteSegundoEstudo).toBigInteger());
			}
		    } else {
			divisaoEstudo.setRepartePrimeiroEstudo(repartePrimeiroEstudo.toBigInteger());
			divisaoEstudo.setReparteSegundoEstudo(reparteSegundoEstudo.toBigInteger());
		    }

//		    divisaoEstudo.setDataLancamentoPrimeiroEstudo(DateUtil.formatarDataPTBR(estudoOriginal.getDataLancamento()));

		    Long maxId = estudoService.obterUltimoAutoIncrement();
		    
		    divisaoEstudo.setNumeroPrimeiroEstudo(maxId++);
		    divisaoEstudo.setNumeroSegundoEstudo(maxId);

		    result.use(json()).from(divisaoEstudo).recursive().serialize();
		} else {
		    mensagensValidacao.add("- Estudo Original não encontrado!");
		}
	    }
	} else {
	    mensagensValidacao.add("- Informar os percentuais!");
	}

	if (!mensagensValidacao.isEmpty()) {
	    throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, mensagensValidacao));
	}

    }

    @Path("/confirmar")
    @Post
    public void confirmar(DivisaoEstudoDTO divisaoEstudo) {

	List<String> mensagensValidacao = new ArrayList<String>();
	TipoMensagem tipoMensagem = null;

	String dataLancamentoPrimeiroEstudo = divisaoEstudo.getDataLancamentoPrimeiroEstudo();
	String dataLancamentoSegundoEstudo = divisaoEstudo.getDataLancamentoSegundoEstudo();
	divisaoEstudo.setDataDistribuicaoParaPesquisa(divisaoEstudo.getDataDistribuicao());
	
	List<EstudoGerado> listEstudo = null; 
	
	if (dataLancamentoSegundoEstudo != null && !dataLancamentoSegundoEstudo.equalsIgnoreCase("")) {

	    if (!dataLancamentoSegundoEstudo.equalsIgnoreCase(dataLancamentoPrimeiroEstudo)) {

	    EstudoGerado estudoOriginal = estudoService.obterEstudoByEstudoOriginalFromDivisaoEstudo(divisaoEstudo);

	    EstudoGerado primeiroEstudo = (EstudoGerado)SerializationUtils.clone(estudoOriginal);
		primeiroEstudo.setId(divisaoEstudo.getNumeroPrimeiroEstudo());
//		primeiroEstudo.setReparteDistribuir(divisaoEstudo.getRepartePrimeiroEstudo());
		primeiroEstudo.setQtdeReparte(divisaoEstudo.getRepartePrimeiroEstudo());
		primeiroEstudo.setDataLancamento(DateUtil.parseData(dataLancamentoPrimeiroEstudo, Constantes.DATE_PATTERN_PT_BR));
 
		EstudoGerado segundoEstudo = (EstudoGerado) SerializationUtils.clone(estudoOriginal);
		segundoEstudo.setId(divisaoEstudo.getNumeroSegundoEstudo());
//		segundoEstudo.setReparteDistribuir(divisaoEstudo.getRepartePrimeiroEstudo());
//		segundoEstudo.setReparteDistribuir(divisaoEstudo.getReparteSegundoEstudo());
		segundoEstudo.setQtdeReparte(divisaoEstudo.getReparteSegundoEstudo());
		segundoEstudo.setDataLancamento(DateUtil.parseData(dataLancamentoSegundoEstudo, Constantes.DATE_PATTERN_PT_BR));

		listEstudo = new ArrayList<EstudoGerado>();
		listEstudo.add(primeiroEstudo);
		listEstudo.add(segundoEstudo);

		List<Long> listIdEstudoAdiconado = this.estudoService.salvarDivisao(estudoOriginal, listEstudo,divisaoEstudo);

		for (Long estudoDividido : listIdEstudoAdiconado) {
			this.estudoProdutoEdicaoBaseService.copiarEdicoesBase(estudoOriginal.getId(),estudoDividido);
		}

		
		
		mensagensValidacao.add("Estudo dividido com sucesso! Os número(s) gerado(s) foram : "+StringUtils.join(listIdEstudoAdiconado," - "));

		tipoMensagem = TipoMensagem.SUCCESS;

	    } else {
		mensagensValidacao.add("- A data de lançamento do Segundo Estudo não deve ser igual ao Estudo Original!");
	    }

	} else {
	    mensagensValidacao.add("- Informar a data de lançamento do segundo estudo!");
	}

	if (!mensagensValidacao.isEmpty()) {
	    if (TipoMensagem.SUCCESS.equals(tipoMensagem)){
	    	
	    	List<Object> l  = new ArrayList<>();
	    	l.add(mensagensValidacao.get(0));
	    	l.add(new Long[]{listEstudo.get(0).getQtdeReparte().longValue(),listEstudo.get(1).getQtdeReparte().longValue()});
	    	
	    	result.use(Results.json()).from(l,"result").recursive().serialize();
	    	
	    }
	    else if (TipoMensagem.WARNING.equals(tipoMensagem))
	    	throw new ValidacaoException(new ValidacaoVO(tipoMensagem, mensagensValidacao));
	}
	
	this.result.nothing();

    }

    @Path("/cancelar")
    @Post
    public void cancelar(DivisaoEstudoDTO divisaoEstudo) {

	divisaoEstudo.setPercentualDivisaoPrimeiroEstudo(null);
	divisaoEstudo.setPercentualDivisaoSegundoEstudo(null);
	divisaoEstudo.setQuantidadeReparte(null);
	divisaoEstudo.setNumeroPrimeiroEstudo(null);
	divisaoEstudo.setNumeroSegundoEstudo(null);
	divisaoEstudo.setRepartePrimeiroEstudo(null);
	divisaoEstudo.setReparteSegundoEstudo(null);
	divisaoEstudo.setDataLancamentoPrimeiroEstudo(null);
	divisaoEstudo.setDataLancamentoSegundoEstudo(null);

	result.use(json()).from(divisaoEstudo).recursive().serialize();
    }
}

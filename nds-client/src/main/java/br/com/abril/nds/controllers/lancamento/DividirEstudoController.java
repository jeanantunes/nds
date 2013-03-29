package br.com.abril.nds.controllers.lancamento;

import static br.com.caelum.vraptor.view.Results.json;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.DivisaoEstudoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.EstudoService;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

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

	BigDecimal percentualDivisaoEstudoOriginal = BigDecimal.ONE;

	Integer iPercentualDivisaoPrimeiroEstudo = divisaoEstudo.getPercentualDivisaoPrimeiroEstudo();
	Integer iPercentualDivisaoSegundoEstudo = divisaoEstudo.getPercentualDivisaoSegundoEstudo();

	if (iPercentualDivisaoPrimeiroEstudo != null && iPercentualDivisaoSegundoEstudo != null) {

	    BigDecimal percentualDivisaoPrimeiroEstudo = new BigDecimal(iPercentualDivisaoPrimeiroEstudo.toString());
	    BigDecimal percentualDivisaoSegundoEstudo = new BigDecimal(iPercentualDivisaoSegundoEstudo.toString());

	    BigDecimal somaPercentual = percentualDivisaoPrimeiroEstudo.add(percentualDivisaoSegundoEstudo).add(percentualDivisaoEstudoOriginal);
	    BigDecimal cem = BigDecimal.TEN.multiply(BigDecimal.TEN);

	    if (somaPercentual.compareTo(cem) != 0) {

		List<String> mensagensValidacao = new ArrayList<String>();
		mensagensValidacao.add("- O total da soma do percentual da divisão deve ser igual a 100% (1% do Estudo Original e 99% dos Estudos Divididos)!");

		if (!mensagensValidacao.isEmpty()) {
		    throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, mensagensValidacao));
		}

	    } else {

		Estudo estudoOriginal = estudoService.obterEstudoByEstudoOriginalFromDivisaoEstudo(divisaoEstudo);

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

		    Estudo primeiroEstudo = new Estudo();
		    primeiroEstudo.setDataLancamento(estudoOriginal.getDataLancamento());

		    Estudo segundoEstudo = new Estudo();

		    if (qtdeReparteDivisao != null && qtdeReparteDivisao > 0) {

			if (repartePrimeiroEstudo.compareTo(new BigDecimal(qtdeReparteDivisao)) == 1
				&& reparteSegundoEstudo.compareTo(new BigDecimal(qtdeReparteDivisao)) == 1) {

			    primeiroEstudo.setReparteDistribuir(repartePrimeiroEstudo.toBigInteger());
			    segundoEstudo.setReparteDistribuir(reparteSegundoEstudo.toBigInteger());

			} else {
			    primeiroEstudo.setReparteDistribuir(repartePrimeiroEstudo.add(reparteSegundoEstudo).toBigInteger());
			}
		    }

		    result.use(json()).from(divisaoEstudo).serialize();

		}
	    }
	} else {
	    List<String> mensagensValidacao = new ArrayList<String>();
	    mensagensValidacao.add("- Informar os percentuais!");

	    if (!mensagensValidacao.isEmpty()) {
		throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, mensagensValidacao));
	    }
	}

    }

    @Path("/confirmar")
    @Post
    public void confirmar(DivisaoEstudoDTO divisaoEstudo) {

    }

}

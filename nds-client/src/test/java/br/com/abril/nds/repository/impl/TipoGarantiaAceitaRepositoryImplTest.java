package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.ParametroContratoCota;
import br.com.abril.nds.model.cadastro.ParametrosAprovacaoDistribuidor;
import br.com.abril.nds.model.cadastro.ParametrosRecolhimentoDistribuidor;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.TipoGarantia;
import br.com.abril.nds.model.cadastro.TipoGarantiaAceita;
import br.com.abril.nds.repository.TipoGarantiaAceitaRepository;

public class TipoGarantiaAceitaRepositoryImplTest extends
		AbstractRepositoryImplTest {

	@Autowired
	private TipoGarantiaAceitaRepository tipoGarantiaAceitaRepository;
	
	TipoGarantiaAceita tipoGarantiaAceita;

	@Before
	public void setup() {
		
		ParametrosAprovacaoDistribuidor parametrosAprovacaoDistribuidor = new ParametrosAprovacaoDistribuidor();

		parametrosAprovacaoDistribuidor.setAjusteEstoque(true);
		parametrosAprovacaoDistribuidor.setDebitoCredito(true);
		parametrosAprovacaoDistribuidor.setDevolucaoFornecedor(true);
		parametrosAprovacaoDistribuidor.setFaltasSobras(true);
		parametrosAprovacaoDistribuidor.setNegociacao(true);
		parametrosAprovacaoDistribuidor.setPostergacaoCobranca(true);
		
		ParametroContratoCota parametroContrato = Fixture
				.criarParametroContratoCota(
						"<font color=\"blue\"><b>CONSIDERANDO QUE:</b></font><br>"
								+ "<br>"
								+ "<b>(i)</b>	A Contratante contempla, dentro de seu objeto social, a atividade de distribuição de livros, jornais, revistas, impressos e publicações em geral e, portanto, necessita de serviços de transporte de revistas;"
								+ "<br>"
								+ "<b>(ii)</b>	A Contratada é empresa especializada e, por isso, capaz de prestar serviços de transportes, bem como declara que possui qualificação técnica e documentação necessária para a prestação dos serviços citados acima;"
								+ "<br>"
								+ "<b>(iii)</b>	A Contratante deseja contratar a Contratada para a prestação dos serviços de transporte de revistas;"
								+ "<br>"
								+ "RESOLVEM, mútua e reciprocamente, celebrar o presente Contrato de Prestação de Serviços de Transporte de Revistas (“Contrato”), que se obrigam a cumprir, por si e seus eventuais sucessores a qualquer título, em conformidade com os termos e condições a seguir:"
								+ "<br><br>"
								+ "<font color=\"blue\"><b>1.	OBJETO DO CONTRATO</b><br></font>"
								+ "<br>"
								+ "<b>1.1.</b>	O presente contrato tem por objeto a prestação dos serviços pela Contratada de transporte de revistas, sob sua exclusiva responsabilidade, sem qualquer relação de subordinação com a Contratante e dentro da melhor técnica, diligência, zelo e probidade, consistindo na disponibilização de veículos e motoristas que atendam a demanda da Contratante.",
						"neste ato, por seus representantes infra-assinados, doravante denominada simplesmente CONTRATADA.",
						30, 30);
		save(parametroContrato);

		Distribuidor distribuidor = new Distribuidor();
		distribuidor.setCodigo(1);
		distribuidor.setParametroContratoCota(parametroContrato);
		distribuidor.setCapacidadeDistribuicao(BigDecimal.TEN);
		distribuidor.setCapacidadeRecolhimento(BigDecimal.TEN);
		distribuidor.setFatorRelancamentoParcial(5);
		distribuidor.setDataOperacao(Fixture.criarData(05, Calendar.SEPTEMBER, 2011));
		distribuidor
				.setInformacoesComplementaresProcuracao("informacoesComplementaresProcuracao");
		distribuidor.setNegociacaoAteParcelas(10);
		distribuidor
				.setParametrosAprovacaoDistribuidor(parametrosAprovacaoDistribuidor);
		distribuidor
				.setParametrosRecolhimentoDistribuidor(new ParametrosRecolhimentoDistribuidor());
		distribuidor.setPrazoAvisoPrevioValidadeGarantia(40);
		distribuidor.setPrazoFollowUp(50);
		distribuidor.setValorConsignadoSuspensaoCotas(new BigDecimal(9999999));
		distribuidor.setQtdDiasLimiteParaReprogLancamento(2);
		distribuidor.setCodigoDistribuidorDinap("6248116");
		
		PessoaJuridica juridica = Fixture.pessoaJuridica("Treelog",
				"61.438.248/0001-23", "000000000000", "sys.discover@gmail.com", "99.999-9");
		save(juridica);
		distribuidor.setJuridica(juridica);
		
		save(distribuidor);
		
		tipoGarantiaAceita = Fixture.criarTipoGarantiaAceita(distribuidor, TipoGarantia.FIADOR);
		
		save(tipoGarantiaAceita);

	}

	@Test
	public void alterarOuCriar(){
		tipoGarantiaAceitaRepository.alterarOuCriar(tipoGarantiaAceita);
		
	}
}

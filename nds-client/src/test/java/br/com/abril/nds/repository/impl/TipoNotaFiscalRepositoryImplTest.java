package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Date;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.GrupoNotaFiscal;
import br.com.abril.nds.model.fiscal.NotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaFornecedor;
import br.com.abril.nds.model.fiscal.StatusNotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.repository.TipoNotaFiscalRepository;

public class TipoNotaFiscalRepositoryImplTest extends AbstractRepositoryImplTest{
	
	@Autowired
	private TipoNotaFiscalRepository tipoNotaFiscalRepository;
	
	String cnpj = "00.000.000/0001-00";
	String chave = "11111";
	Long numeroNota = 2344242L;
	String serie = "345353543";
	
	@Before
	public void setup() {
		TipoFornecedor tipoFornecedor = Fixture.tipoFornecedorPublicacao();
		
		Fornecedor fornecedor = Fixture.fornecedorFC(tipoFornecedor);
		save(fornecedor);
		
		CFOP cfop  = Fixture.cfop5102();
		save(cfop);

		TipoNotaFiscal tp = Fixture.tipoNotaFiscalDevolucao();
		save(tp);
		
		NotaFiscalEntrada notaFiscal = new NotaFiscalEntradaFornecedor();
		notaFiscal.setCfop(cfop);
		notaFiscal.setChaveAcesso(chave);
		notaFiscal.setNumero(numeroNota);
		notaFiscal.setSerie(serie);
		notaFiscal.setDataEmissao(new Date());
		notaFiscal.setDataExpedicao(new Date());
		notaFiscal.setOrigem(Origem.INTERFACE);
		notaFiscal.setValorBruto(BigDecimal.TEN);
		notaFiscal.setValorLiquido(BigDecimal.TEN);
		notaFiscal.setValorDesconto(BigDecimal.TEN);
		((NotaFiscalEntradaFornecedor)notaFiscal).setFornecedor(fornecedor);
		
		notaFiscal.setStatusNotaFiscal(StatusNotaFiscalEntrada.PENDENTE);
		notaFiscal.setTipoNotaFiscal(tp);
		save(notaFiscal);
		
	}
	
	@Test
	public void testObterTipoNotaFiscal() {
		
		TipoNotaFiscal tipoNotaFiscal = tipoNotaFiscalRepository.obterTipoNotaFiscal(GrupoNotaFiscal.DEVOLUCAO_MERCADORIA_FORNECEDOR);
		
		Assert.assertTrue(tipoNotaFiscal != null);
		
	}
	
}

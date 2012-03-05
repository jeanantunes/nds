package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.filtro.FiltroConsultaNotaFiscalDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.NotaFiscal;
import br.com.abril.nds.model.fiscal.NotaFiscalFornecedor;
import br.com.abril.nds.model.fiscal.StatusNotaFiscal;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.repository.NotaFiscalRepository;

public class NotaFiscalRepositoryImplTest extends AbstractRepositoryImplTest{
	
	@Autowired
	private NotaFiscalRepository notaFiscalRepository;
	
	String cnpj = "00.000.000/0001-00";
	String chave = "11111";
	String numeroNota = "2344242";
	String serie = "345353543";
	
	@Before
	public void setup() {
		
		
		PessoaJuridica pj = Fixture.juridicaFC();
		getSession().save(pj);
		
		CFOP cfop =new CFOP();
		cfop.setCodigo("1");
		cfop.setDescricao("cfop desc");
		cfop.setId(1L);
		getSession().save(cfop);
		
		NotaFiscal notaFiscal = new NotaFiscalFornecedor();
		notaFiscal.setCfop(cfop);
		notaFiscal.setChaveAcesso(chave);
		notaFiscal.setNumero(numeroNota);
		notaFiscal.setSerie(serie);
		notaFiscal.setDataEmissao(new Date(System.currentTimeMillis()));
		notaFiscal.setDataExpedicao(new Date(System.currentTimeMillis()));
		notaFiscal.setEmitente(pj);
		notaFiscal.setOrigem(Origem.INTERFACE);
		notaFiscal.setValorBruto(BigDecimal.TEN);
		notaFiscal.setValorLiquido(BigDecimal.TEN);
		notaFiscal.setValorDesconto(BigDecimal.TEN);
		
		TipoNotaFiscal tp = new TipoNotaFiscal();
		tp.setId(1L);
		tp.setDescricao("desc");		
		tp.setTipoOperacao(TipoOperacao.ENTRADA);
		getSession().save(tp);
		
		notaFiscal.setStatusNotaFiscal(StatusNotaFiscal.PENDENTE);
		notaFiscal.setTipoNotaFiscal(tp);
		getSession().save(notaFiscal);
		
	}
	
	@Test
	public void obterNotaFiscalPorAtributosEmRecebimentoFisico() {
		FiltroConsultaNotaFiscalDTO filtro = new FiltroConsultaNotaFiscalDTO();
		filtro.setCnpj(cnpj);
		filtro.setChave(chave);
		filtro.setNumeroNota(numeroNota);
		filtro.setSerie(serie);
		
		List<NotaFiscal> listaNotas = notaFiscalRepository.obterNotaFiscalPorNumeroSerieCnpj(filtro);
		
		Assert.assertEquals(1, listaNotas.size());		
		
	}
}

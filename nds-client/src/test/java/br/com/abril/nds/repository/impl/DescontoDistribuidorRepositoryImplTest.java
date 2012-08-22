package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.TipoDescontoDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoDescontoDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.model.cadastro.desconto.DescontoDistribuidor;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.DescontoDistribuidorRepository;

public class DescontoDistribuidorRepositoryImplTest extends AbstractRepositoryImplTest{
	
	@Autowired
	private DescontoDistribuidorRepository descontoDistribuidorRepository;
	private DescontoDistribuidor desconto4;
	private Fornecedor fornecedor3;
	
	
	@Before
	public void setup() {
		
		PessoaJuridica juridicaDistrib = Fixture.pessoaJuridica("Distribuidor Acme",
				"56003315000147", "333333333331", "distrib_acme@mail.com", "99.999-9");
		save(juridicaDistrib);
		
		Distribuidor distribuidor = Fixture.distribuidor(1, juridicaDistrib, new Date(), null);
		save(distribuidor);
		
		TipoFornecedor tipoFornecedor = Fixture.tipoFornecedorPublicacao();
		save(tipoFornecedor);
		
		PessoaJuridica pessoa1 = Fixture.pessoaJuridica("Distribuidor Acme",
				"56003315000148", "333333333332", "distrib_acme@mail.com", "99.999-9");
		save(pessoa1);
		

		PessoaJuridica pessoa2 = Fixture.pessoaJuridica("Distribuidor Acme 1",
				"56003315000149", "333333333333", "distrib_acme@mail.com", "99.999-9");
		save(pessoa2);
		

		PessoaJuridica pessoa3 = Fixture.pessoaJuridica("Distribuidor Acm 2",
				"56003315000145", "333333333334", "distrib_acme@mail.com", "99.999-9");
		save(pessoa3);
		

		PessoaJuridica pessoa4 = Fixture.pessoaJuridica("Distribuidor Acme 3",
				"56003315000142", "333333333335", "distrib_acme@mail.com", "99.999-9");
		save(pessoa4);
		
		Fornecedor fornecedor = Fixture.fornecedor(pessoa1, SituacaoCadastro.ATIVO, false, tipoFornecedor,null);
		
		Fornecedor fornecedor1 = Fixture.fornecedor(pessoa2, SituacaoCadastro.ATIVO, false, tipoFornecedor,null);
		
		Fornecedor fornecedor2 = Fixture.fornecedor(pessoa3, SituacaoCadastro.ATIVO, false, tipoFornecedor,null);
		
		fornecedor3 = Fixture.fornecedor(pessoa4, SituacaoCadastro.ATIVO, false, tipoFornecedor,null);
		
		save(fornecedor, fornecedor1, fornecedor2,fornecedor3);
		
		Usuario usuario = Fixture.usuarioJoao();
		save(usuario);
		
		Set<Fornecedor> fornecedores = new HashSet<Fornecedor>();
		fornecedores.add(fornecedor);

		DescontoDistribuidor desconto2 = Fixture.descontoDistribuidor(new BigDecimal(2), distribuidor, fornecedores, usuario, new Date());
		
		Set<Fornecedor> fornecedores2 = new HashSet<Fornecedor>();
		fornecedores2.add(fornecedor);
		fornecedores2.add(fornecedor1);

		DescontoDistribuidor desconto3 = Fixture.descontoDistribuidor(new BigDecimal(3), distribuidor, fornecedores2, usuario, new Date());
		
		Set<Fornecedor> fornecedores3 = new HashSet<Fornecedor>();
		fornecedores3.add(fornecedor);
		fornecedores3.add(fornecedor1);
		fornecedores3.add(fornecedor2);
		fornecedores3.add(fornecedor3);

		desconto4 = Fixture.descontoDistribuidor(new BigDecimal(4), distribuidor, fornecedores3, usuario, new Date());
		
		save(desconto2,desconto3,desconto4);
		
	}
	
	@Test
	public void testbuscarDescontos() {
		
		FiltroTipoDescontoDTO filtro = new FiltroTipoDescontoDTO();
		
		filtro.setOrdenacaoColuna(FiltroTipoDescontoDTO.OrdenacaoColunaConsulta.DATA_ALTERACAO);
		
		List<TipoDescontoDTO> lista = descontoDistribuidorRepository.buscarDescontos(filtro);
		
		Assert.assertTrue(!lista.isEmpty());
		
		filtro.setOrdenacaoColuna(FiltroTipoDescontoDTO.OrdenacaoColunaConsulta.DESCONTO);
		
		lista = descontoDistribuidorRepository.buscarDescontos(filtro);
		
		Assert.assertTrue(!lista.isEmpty());
		
		filtro.setOrdenacaoColuna(FiltroTipoDescontoDTO.OrdenacaoColunaConsulta.FORNECEDORES);
		
		lista = descontoDistribuidorRepository.buscarDescontos(filtro);
		
		Assert.assertTrue(!lista.isEmpty());
		
		filtro.setOrdenacaoColuna(FiltroTipoDescontoDTO.OrdenacaoColunaConsulta.SEQUENCIAL);
		
		lista = descontoDistribuidorRepository.buscarDescontos(filtro);
		
		Assert.assertTrue(!lista.isEmpty());
		
		filtro.setOrdenacaoColuna(FiltroTipoDescontoDTO.OrdenacaoColunaConsulta.USUARIO);
		
		lista = descontoDistribuidorRepository.buscarDescontos(filtro);
		
		Assert.assertTrue(!lista.isEmpty());
	}
	
	@Test
	public void testbuscarQuantidadeDescontos() {
		
		FiltroTipoDescontoDTO filtro = new FiltroTipoDescontoDTO();
		
		Integer quantidade = descontoDistribuidorRepository.buscarQuantidadeDescontos(filtro);
		
	    Assert.assertTrue(quantidade > 0);
	}
	
	@Test
	public void testeBuscarUltimoDescontoPorData(){
		
		DescontoDistribuidor desconto = descontoDistribuidorRepository.buscarUltimoDescontoValido(desconto4.getId(), fornecedor3);
		
		Assert.assertNull(desconto);
	}
	
}

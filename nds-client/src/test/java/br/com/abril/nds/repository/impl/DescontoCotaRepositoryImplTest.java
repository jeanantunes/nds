package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.TipoDescontoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoDescontoCotaDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.model.cadastro.desconto.DescontoCota;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.DescontoCotaRepository;

public class DescontoCotaRepositoryImplTest extends AbstractRepositoryImplTest{
	
	
	@Autowired
	private DescontoCotaRepository descontoCotaRepository;
	
	
	@Before
	public void setup() {
		
		PessoaJuridica juridicaCota = Fixture.pessoaJuridica("Distribuidor Acme",
				"56003315000147", "333333333333", "distrib_acme@mail.com", "99.999-9");
		save(juridicaCota);
		
		Box box = Fixture.boxReparte300();
		
		Cota cota = Fixture.cota(001, juridicaCota, SituacaoCadastro.ATIVO, box);
		save(cota);
		
		TipoFornecedor tipoFornecedor = Fixture.tipoFornecedorPublicacao();
		save(tipoFornecedor);
		
		PessoaJuridica pessoa1 = Fixture.pessoaJuridica("Distribuidor Acme",
				"56003315000148", "333333333333", "distrib_acme@mail.com", "99.999-9");
		save(pessoa1);
		

		PessoaJuridica pessoa2 = Fixture.pessoaJuridica("Distribuidor Acme 1",
				"56003315000149", "333333333333", "distrib_acme@mail.com", "99.999-9");
		save(pessoa2);
		

		PessoaJuridica pessoa3 = Fixture.pessoaJuridica("Distribuidor Acm 2",
				"56003315000145", "333333333333", "distrib_acme@mail.com", "99.999-9");
		save(pessoa3);
		

		PessoaJuridica pessoa4 = Fixture.pessoaJuridica("Distribuidor Acme 3",
				"56003315000142", "333333333333", "distrib_acme@mail.com", "99.999-9");
		save(pessoa4);
		
		Fornecedor fornecedor = Fixture.fornecedor(pessoa1, SituacaoCadastro.ATIVO, false, tipoFornecedor,null);
		
		Fornecedor fornecedor1 = Fixture.fornecedor(pessoa2, SituacaoCadastro.ATIVO, false, tipoFornecedor,null);
		
		Fornecedor fornecedor2 = Fixture.fornecedor(pessoa3, SituacaoCadastro.ATIVO, false, tipoFornecedor,null);
		
		Fornecedor fornecedor3 = Fixture.fornecedor(pessoa4, SituacaoCadastro.ATIVO, false, tipoFornecedor,null);
		
		save(fornecedor, fornecedor1, fornecedor2,fornecedor3);
		
		Usuario usuario = Fixture.usuarioJoao();
		save(usuario);
		
		Set<Fornecedor> fornecedores = new HashSet<Fornecedor>();
		fornecedores.add(fornecedor);
		DescontoCota desconto2 = Fixture.descontoCota(new BigDecimal(2), cota, fornecedores, usuario);
		
		Set<Fornecedor> fornecedores2 = new HashSet<Fornecedor>();
		fornecedores2.add(fornecedor);
		fornecedores2.add(fornecedor1);
		DescontoCota desconto3 = Fixture.descontoCota(new BigDecimal(3), cota, fornecedores2, usuario);
		
		Set<Fornecedor> fornecedores3 = new HashSet<Fornecedor>();
		fornecedores3.add(fornecedor);
		fornecedores3.add(fornecedor1);
		fornecedores3.add(fornecedor2);
		fornecedores3.add(fornecedor3);
		DescontoCota desconto4 = Fixture.descontoCota(new BigDecimal(4), cota, fornecedores3, usuario);
		
		save(desconto2,desconto3,desconto4);
		
	}
	
	@Test
	public void testObterDescontoCota() {
		
		FiltroTipoDescontoCotaDTO filtro = new FiltroTipoDescontoCotaDTO();
		
		filtro.setOrdenacaoColuna(FiltroTipoDescontoCotaDTO.OrdenacaoColunaConsulta.DATA_ALTERACAO);
		
		List<TipoDescontoCotaDTO> lista = descontoCotaRepository.obterDescontoCota(filtro);
		
		Assert.assertTrue(!lista.isEmpty());
		
		filtro.setOrdenacaoColuna(FiltroTipoDescontoCotaDTO.OrdenacaoColunaConsulta.DESONTO);
		
		lista = descontoCotaRepository.obterDescontoCota(filtro);
		
		Assert.assertTrue(!lista.isEmpty());
		
		filtro.setOrdenacaoColuna(FiltroTipoDescontoCotaDTO.OrdenacaoColunaConsulta.FORNECEDORES);
		
		lista = descontoCotaRepository.obterDescontoCota(filtro);
		
		Assert.assertTrue(!lista.isEmpty());
		
		filtro.setOrdenacaoColuna(FiltroTipoDescontoCotaDTO.OrdenacaoColunaConsulta.NOME_COTA);
		
		lista = descontoCotaRepository.obterDescontoCota(filtro);
		
		Assert.assertTrue(!lista.isEmpty());
		
		filtro.setOrdenacaoColuna(FiltroTipoDescontoCotaDTO.OrdenacaoColunaConsulta.NUMERO_COTA);
		
		lista = descontoCotaRepository.obterDescontoCota(filtro);
		
		Assert.assertTrue(!lista.isEmpty());
		
        filtro.setOrdenacaoColuna(FiltroTipoDescontoCotaDTO.OrdenacaoColunaConsulta.USUARIO);
		
		lista = descontoCotaRepository.obterDescontoCota(filtro);
		
		Assert.assertTrue(!lista.isEmpty());
	}
	
	@Test
	public void testObterQuantidadeDescontoCota() {
		
		FiltroTipoDescontoCotaDTO filtro = new FiltroTipoDescontoCotaDTO();
		
		Integer quantidade = descontoCotaRepository.obterQuantidadeDescontoCota(filtro);
		
	    Assert.assertTrue(quantidade > 0);
	}
	
}

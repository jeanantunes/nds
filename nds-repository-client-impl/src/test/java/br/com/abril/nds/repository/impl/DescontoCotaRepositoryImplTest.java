package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.model.cadastro.desconto.DescontoCota;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.DescontoCotaRepository;
import br.com.abril.nds.vo.PaginacaoVO;

public class DescontoCotaRepositoryImplTest extends AbstractRepositoryImplTest{
	
	
	@Autowired
	private DescontoCotaRepository descontoCotaRepository;

	private DescontoCota desconto3;

	private Fornecedor fornecedor1;
	
	private static Cota cota;
	
	@Before
	public void setup() {
		
		PessoaJuridica juridicaDistrib = Fixture.pessoaJuridica("Distribuidor Acme",
				"56003315000146", "333333333334", "distrib_acme@mail.com", "99.999-9");
		save(juridicaDistrib);
		
		PessoaJuridica juridicaCota = Fixture.pessoaJuridica("Cota Juridica",
				"56003315000147", "333333333333", "distrib_acme@mail.com", "99.999-9");
		save(juridicaCota);
		
		Distribuidor distribuidor = Fixture.distribuidor(1, juridicaDistrib, new Date(), null);
		save(distribuidor);
		
		Box box = Fixture.boxReparte300();
		save(box);
		
		cota = Fixture.cota(001, juridicaCota, SituacaoCadastro.ATIVO, box);
		save(cota);
		
		TipoFornecedor tipoFornecedor = Fixture.tipoFornecedorPublicacao();
		save(tipoFornecedor);
		
		PessoaJuridica pessoa1 = Fixture.pessoaJuridica("Fornecedor 1",
				"56003315000148", "333333333335", "distrib_acme@mail.com", "99.999-9");
		save(pessoa1);
		

		PessoaJuridica pessoa2 = Fixture.pessoaJuridica("Fornecedor 2",
				"56003315000149", "333333333353", "distrib_acme@mail.com", "99.999-9");
		save(pessoa2);
		

		PessoaJuridica pessoa3 = Fixture.pessoaJuridica("Fornecedor 3",
				"56003315000145", "333333353333", "distrib_acme@mail.com", "99.999-9");
		save(pessoa3);
		

		PessoaJuridica pessoa4 = Fixture.pessoaJuridica("Fornecedor 4",
				"56003315000142", "333533333333", "distrib_acme@mail.com", "99.999-9");
		save(pessoa4);
		
		fornecedor1 = Fixture.fornecedor(pessoa1, SituacaoCadastro.ATIVO, tipoFornecedor,null);
		
		Fornecedor fornecedor2 = Fixture.fornecedor(pessoa2, SituacaoCadastro.ATIVO, tipoFornecedor,null);
		
		Fornecedor fornecedor3 = Fixture.fornecedor(pessoa3, SituacaoCadastro.ATIVO, tipoFornecedor,null);
		
		Fornecedor fornecedor4 = Fixture.fornecedor(pessoa4, SituacaoCadastro.ATIVO, tipoFornecedor,null);
		
		save(fornecedor1, fornecedor2, fornecedor3,fornecedor4);
		
		Usuario usuario = Fixture.usuarioJoao();
		save(usuario);
		
		Set<Fornecedor> fornecedores = new HashSet<Fornecedor>();
		fornecedores.add(fornecedor1);
		DescontoCota desconto1 = Fixture.descontoCota(new BigDecimal(2), distribuidor, cota, fornecedores, usuario, new Date());
		
		Set<Fornecedor> fornecedores2 = new HashSet<Fornecedor>();
		fornecedores2.add(fornecedor1);
		fornecedores2.add(fornecedor2);

		DescontoCota desconto2 = Fixture.descontoCota(new BigDecimal(3), distribuidor, cota, fornecedores2, usuario, new Date());
		
		Set<Fornecedor> fornecedores3 = new HashSet<Fornecedor>();
		fornecedores3.add(fornecedor1);
		fornecedores3.add(fornecedor2);
		fornecedores3.add(fornecedor3);
		fornecedores3.add(fornecedor4);

		desconto3 = Fixture.descontoCota(new BigDecimal(4), distribuidor, cota, fornecedores3, usuario, new Date());
		
		save(desconto1,desconto2,desconto3);
		
	}
	
	@Test
	public void testObterDescontoCota() {
		
		FiltroTipoDescontoCotaDTO filtro = new FiltroTipoDescontoCotaDTO();
		List<TipoDescontoCotaDTO> lista = new ArrayList<TipoDescontoCotaDTO>();
		PaginacaoVO paginacao = new PaginacaoVO();
		paginacao.setSortOrder("asc");
		filtro.setPaginacao(paginacao);
		
		filtro.setOrdenacaoColuna(FiltroTipoDescontoCotaDTO.OrdenacaoColunaConsulta.DATA_ALTERACAO);
		lista = descontoCotaRepository.obterDescontoCota(filtro);
		Assert.assertTrue(!lista.isEmpty());
		
		filtro.setOrdenacaoColuna(FiltroTipoDescontoCotaDTO.OrdenacaoColunaConsulta.DESCONTO);
		lista = descontoCotaRepository.obterDescontoCota(filtro);
		Assert.assertTrue(!lista.isEmpty());
		
		filtro.setOrdenacaoColuna(FiltroTipoDescontoCotaDTO.OrdenacaoColunaConsulta.FORNECEDORES);
		lista = descontoCotaRepository.obterDescontoCota(filtro);
		Assert.assertTrue(!lista.isEmpty());
		
		filtro.setOrdenacaoColuna(FiltroTipoDescontoCotaDTO.OrdenacaoColunaConsulta.NOME_COTA);
		lista = descontoCotaRepository.obterDescontoCota(filtro);
		Assert.assertTrue(!lista.isEmpty());
		
		filtro.setOrdenacaoColuna(FiltroTipoDescontoCotaDTO.OrdenacaoColunaConsulta.NUMERO_COTA);
		filtro.setNomeCota(cota.getPessoa().getNome());
		filtro.setNumeroCota(cota.getNumeroCota());
		lista = descontoCotaRepository.obterDescontoCota(filtro);
		Assert.assertTrue(!lista.isEmpty());
		
        filtro.setOrdenacaoColuna(FiltroTipoDescontoCotaDTO.OrdenacaoColunaConsulta.USUARIO);
		lista = descontoCotaRepository.obterDescontoCota(filtro);
		Assert.assertTrue(!lista.isEmpty());
		
        filtro.setOrdenacaoColuna(FiltroTipoDescontoCotaDTO.OrdenacaoColunaConsulta.TIPO_DESCONTO);
		lista = descontoCotaRepository.obterDescontoCota(filtro);
		Assert.assertTrue(!lista.isEmpty());
	}
	
	@Test
	public void testObterQuantidadeDescontoCota() {
		
		FiltroTipoDescontoCotaDTO filtro = new FiltroTipoDescontoCotaDTO();
		Integer quantidade = descontoCotaRepository.obterQuantidadeDescontoCota(filtro);
	    Assert.assertTrue(quantidade > 0);
	}

	@Test
	public void testObterQuantidadeDescontoCotaNumeroCota() {
		
		FiltroTipoDescontoCotaDTO filtro = new FiltroTipoDescontoCotaDTO();
		filtro.setNumeroCota(1);
		
		Integer quantidade = descontoCotaRepository.obterQuantidadeDescontoCota(filtro);
	    Assert.assertTrue(quantidade > 0);
	}

	
	@Test
	public void obterQuantidadeDescontoCotaPaginacao() {
		
		FiltroTipoDescontoCotaDTO filtro = new FiltroTipoDescontoCotaDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setPaginaAtual(1);
		filtro.getPaginacao().setQtdResultadosPorPagina(1);
		
		List<TipoDescontoCotaDTO> tipoDescontoCotaDTOs = descontoCotaRepository.obterDescontoCota(filtro);
		
		Assert.assertNotNull(tipoDescontoCotaDTOs); 
	  }
	
	@Test
	public void obterQuantidadeDescontoCotaNumeroCota() {
		
		FiltroTipoDescontoCotaDTO filtro = new FiltroTipoDescontoCotaDTO();
		filtro.setNumeroCota(1);
		
		List<TipoDescontoCotaDTO> tipoDescontoCotaDTOs = descontoCotaRepository.obterDescontoCota(filtro);
		
		Assert.assertNotNull(tipoDescontoCotaDTOs); 
	  }
	
}

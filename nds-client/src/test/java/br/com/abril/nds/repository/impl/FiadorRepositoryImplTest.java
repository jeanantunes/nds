package br.com.abril.nds.repository.impl;

import java.util.Date;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ConsultaFiadorDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaFiadorDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaFiadorDTO.OrdenacaoColunaFiador;
import br.com.abril.nds.model.cadastro.EstadoCivil;
import br.com.abril.nds.model.cadastro.Fiador;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Sexo;
import br.com.abril.nds.repository.FiadorRepository;
import br.com.abril.nds.repository.PessoaRepository;
import br.com.abril.nds.vo.PaginacaoVO;

public class FiadorRepositoryImplTest extends AbstractRepositoryImplTest{

	private final String CPF = "0123456789";
	
	private final String CNPJ = "9876543210";
	
	@Autowired
	private FiadorRepository fiadorRepository;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Before
	public void setUp(){
		PessoaFisica pessoaFisica = new PessoaFisica();
		pessoaFisica.setEmail("teste@teste.com");
		pessoaFisica.setCpf(CPF);
		pessoaFisica.setDataNascimento(new Date());
		pessoaFisica.setEstadoCivil(EstadoCivil.CASADO);
		pessoaFisica.setNome("tião");
		pessoaFisica.setOrgaoEmissor("SSP");
		pessoaFisica.setRg(CPF);
		pessoaFisica.setSexo(Sexo.MASCULINO);
		this.pessoaRepository.adicionar(pessoaFisica);
		
		Fiador fiador = new Fiador();
		fiador.setPessoa(pessoaFisica);
		fiador.setInicioAtividade(new Date());
		this.fiadorRepository.adicionar(fiador);
		
		PessoaJuridica pessoaJuridica = new PessoaJuridica();
		pessoaJuridica.setEmail("teste@teste.com");
		pessoaJuridica.setCnpj(CNPJ);
		pessoaJuridica.setNomeFantasia("tião macalé");
		pessoaJuridica.setRazaoSocial("eita");
		this.pessoaRepository.adicionar(pessoaJuridica);
		
		fiador = new Fiador();
		fiador.setPessoa(pessoaJuridica);
		fiador.setInicioAtividade(new Date());
		
		this.fiadorRepository.adicionar(fiador);
	}
	
	@Test
	public void obterFiadorPorCpfTest(){
		Fiador fiador = this.fiadorRepository.obterFiadorPorCpf(CPF);
		
		Assert.assertNotNull(fiador);
	}
	
	@Test
	public void obterFiadorPorCnpjTest(){
		Fiador fiador = this.fiadorRepository.obterFiadorPorCnpj(CNPJ);
		
		Assert.assertNotNull(fiador);
	}
	
	@Test
	public void obterFiadoresCpfCnpjTest(){
		FiltroConsultaFiadorDTO filtroConsultaFiadorDTO = new FiltroConsultaFiadorDTO();
		filtroConsultaFiadorDTO.setCpfCnpj(CPF);
		filtroConsultaFiadorDTO.setOrdenacaoColunaFiador(OrdenacaoColunaFiador.NOME);
		filtroConsultaFiadorDTO.setPaginacaoVO(new PaginacaoVO(1, 50, "asc"));
		
		ConsultaFiadorDTO consultaFiadorDTO = this.fiadorRepository.obterFiadoresCpfCnpj(filtroConsultaFiadorDTO);
		
		Assert.assertNotNull(consultaFiadorDTO);
		
		filtroConsultaFiadorDTO = new FiltroConsultaFiadorDTO();
		filtroConsultaFiadorDTO.setCpfCnpj(CNPJ);
		filtroConsultaFiadorDTO.setOrdenacaoColunaFiador(OrdenacaoColunaFiador.CODIGO);
		filtroConsultaFiadorDTO.setPaginacaoVO(new PaginacaoVO(1, 50, "desc"));
		
		consultaFiadorDTO = this.fiadorRepository.obterFiadoresCpfCnpj(filtroConsultaFiadorDTO);
		
		Assert.assertNotNull(consultaFiadorDTO);
	}
}

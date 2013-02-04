package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ConsultaFiadorDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaFiadorDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaFiadorDTO.OrdenacaoColunaFiador;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.EstadoCivil;
import br.com.abril.nds.model.cadastro.Fiador;
import br.com.abril.nds.model.cadastro.Pessoa;
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

	@Test
	public void buscaFiador() {
		List<ItemDTO<Long, String>> dtos =  fiadorRepository.buscaFiador("", 12);
		
		Assert.assertEquals(2, dtos.size());
		
		Fiador fiador = fiadorRepository.obterPorCpfCnpj(CNPJ);
		
		Assert.assertNotNull(fiador);
	}
	
//	TESTES SEM USO DE MASSA
	
//	getNome
	@Test
	public void testarObterFiadoresCpfCnpjGetNome() {
		
		FiltroConsultaFiadorDTO filtroConsultaFiadorDTO = new FiltroConsultaFiadorDTO();
		filtroConsultaFiadorDTO.setNome("testeNome");
		filtroConsultaFiadorDTO.setCpfCnpj(CPF);
		filtroConsultaFiadorDTO.setOrdenacaoColunaFiador(OrdenacaoColunaFiador.NOME);
		filtroConsultaFiadorDTO.setPaginacaoVO(new PaginacaoVO(1, 50, "asc"));
		
		ConsultaFiadorDTO consultaFiadorDTO = this.fiadorRepository.obterFiadoresCpfCnpj(filtroConsultaFiadorDTO);	
		
		Assert.assertNotNull(consultaFiadorDTO);
		
	}
	
//	CPF_CNPJ
	@Test
	public void testarObterFiadoresCpfCnpjOrdenacaoCOlunaFiadorCPFCNPJ() {
		
		FiltroConsultaFiadorDTO filtroConsultaFiadorDTO = new FiltroConsultaFiadorDTO();
		filtroConsultaFiadorDTO.setNome("testeNome");
		filtroConsultaFiadorDTO.setCpfCnpj(CPF);
		filtroConsultaFiadorDTO.setOrdenacaoColunaFiador(OrdenacaoColunaFiador.CPF_CNPJ);
		filtroConsultaFiadorDTO.setPaginacaoVO(new PaginacaoVO(1, 50, "asc"));
		
		ConsultaFiadorDTO consultaFiadorDTO = this.fiadorRepository.obterFiadoresCpfCnpj(filtroConsultaFiadorDTO);	
		
		Assert.assertNotNull(consultaFiadorDTO);
		
	}
	
//	EMAIL
	@Test
	public void testarObterFiadoresCpfCnpjOrdenacaoCOlunaFiadorEMAIL() {
		
		FiltroConsultaFiadorDTO filtroConsultaFiadorDTO = new FiltroConsultaFiadorDTO();
		filtroConsultaFiadorDTO.setNome("testeNome");
		filtroConsultaFiadorDTO.setCpfCnpj(CPF);
		filtroConsultaFiadorDTO.setOrdenacaoColunaFiador(OrdenacaoColunaFiador.EMAIL);
		filtroConsultaFiadorDTO.setPaginacaoVO(new PaginacaoVO(1, 50, "asc"));
		
		ConsultaFiadorDTO consultaFiadorDTO = this.fiadorRepository.obterFiadoresCpfCnpj(filtroConsultaFiadorDTO);
		
		Assert.assertNotNull(consultaFiadorDTO);
		
	}
	
//	NOME
	@Test
	public void testarObterFiadoresCpfCnpjOrdenacaoCOlunaFiadorNOME() {
		
		FiltroConsultaFiadorDTO filtroConsultaFiadorDTO = new FiltroConsultaFiadorDTO();
		filtroConsultaFiadorDTO.setNome("testeNome");
		filtroConsultaFiadorDTO.setCpfCnpj(CPF);
		filtroConsultaFiadorDTO.setOrdenacaoColunaFiador(OrdenacaoColunaFiador.NOME);
		filtroConsultaFiadorDTO.setPaginacaoVO(new PaginacaoVO(1, 50, "asc"));
		
		ConsultaFiadorDTO consultaFiadorDTO = this.fiadorRepository.obterFiadoresCpfCnpj(filtroConsultaFiadorDTO);	
		
		Assert.assertNotNull(consultaFiadorDTO);
		
	}
	
//	RG_INSCRICAO
	@Test
	public void testarObterFiadoresCpfCnpjOrdenacaoCOlunaFiadorRGINSCRICAO() {
		
		FiltroConsultaFiadorDTO filtroConsultaFiadorDTO = new FiltroConsultaFiadorDTO();
		filtroConsultaFiadorDTO.setNome("testeNome");
		filtroConsultaFiadorDTO.setCpfCnpj(CPF);
		filtroConsultaFiadorDTO.setOrdenacaoColunaFiador(OrdenacaoColunaFiador.RG_INSCRICAO);
		filtroConsultaFiadorDTO.setPaginacaoVO(new PaginacaoVO(1, 50, "asc"));
		
		ConsultaFiadorDTO consultaFiadorDTO = this.fiadorRepository.obterFiadoresCpfCnpj(filtroConsultaFiadorDTO);	
		
		Assert.assertNotNull(consultaFiadorDTO);
		
	}
	
//	TELEFONE
	@Test
	public void testarObterFiadoresCpfCnpjOrdenacaoCOlunaFiadorTELEFONE() {
		
		FiltroConsultaFiadorDTO filtroConsultaFiadorDTO = new FiltroConsultaFiadorDTO();
		filtroConsultaFiadorDTO.setNome("testeNome");
		filtroConsultaFiadorDTO.setCpfCnpj(CPF);
		filtroConsultaFiadorDTO.setOrdenacaoColunaFiador(OrdenacaoColunaFiador.TELEFONE);
		filtroConsultaFiadorDTO.setPaginacaoVO(new PaginacaoVO(1, 50, "asc"));
		
		ConsultaFiadorDTO consultaFiadorDTO = this.fiadorRepository.obterFiadoresCpfCnpj(filtroConsultaFiadorDTO);	
		
		Assert.assertNotNull(consultaFiadorDTO);
		
	}
	
	@Test
	public void testarBuscarPessoaFiadorPorId() {
		
		Pessoa pessoa;
		
		Long idFiador = 1L;
		
		pessoa = fiadorRepository.buscarPessoaFiadorPorId(idFiador);
		
//		Assert.assertNotNull(pessoa);
		
	}
	
	@Test
	public void testarBuscarIdPessoaFiador() {
		
		Long idPessoaFiador;
		
		Long idFiador = 1L;
		
		idPessoaFiador = fiadorRepository.buscarIdPessoaFiador(idFiador);
		
//		Assert.assertNotNull(idPessoaFiador);
		
	}
	
	@Test
	public void testarBuscarSociosFiador() {
		
		List<Pessoa> listaSociosFiador;
		
		Long idFiador = 1L;
		
		listaSociosFiador = fiadorRepository.buscarSociosFiador(idFiador);
		
		Assert.assertNotNull(listaSociosFiador);
		
	}
	
	@Test
	public void testarBuscarDataInicioAtividadeFiadorPorId() {
		
		Date dataInicioAtividade;
		
		Long id = 1L;
		
		dataInicioAtividade = fiadorRepository.buscarDataInicioAtividadeFiadorPorId(id);
		
//		Assert.assertNotNull(dataInicioAtividade);
		
	}
	
	@Test
	public void testarObterCotasAssociadaFiador() {
		
		List<Cota> cotasAssociada;
		
		Long idFiador = 1L;
		Set<Long> cotasIgnorar = new HashSet<Long>();
		cotasIgnorar.add(1L);
		cotasIgnorar.add(2L);
		cotasIgnorar.add(3L);
		
		cotasAssociada = fiadorRepository.obterCotasAssociadaFiador(idFiador, cotasIgnorar);
		
		Assert.assertNotNull(cotasAssociada);
		
	}
	
	@Test
	public void testarVerificarAssociacaoFiadorCota() {
		
		boolean verificarAssociacao;
		
		Long idFiador = 1L;
		Integer numeroCota = 1;
		Set<Long> idsIgnorar = new HashSet<Long>();
		idsIgnorar.add(1L);
		idsIgnorar.add(2L);
		idsIgnorar.add(3L);
		
		verificarAssociacao = fiadorRepository.verificarAssociacaoFiadorCota(idFiador, numeroCota, idsIgnorar);
		
		Assert.assertFalse(verificarAssociacao);
		
	}
	
	@Test
	public void testarBuscarSocioFiadorPorCPF() {
		
		PessoaFisica pessoaFisica;
		
		Long idFiador = 1L;
		String cpf = CPF;
		
		pessoaFisica = fiadorRepository.buscarSocioFiadorPorCPF(idFiador, cpf);
		
		Assert.assertNull(pessoaFisica);
		
	}
	
	@Test
	public void testarBuscaFiador() {
		
		List<ItemDTO<Long,String>> buscaFiador;
		
		String nome = "testeNome";
		int maxResults = 1;
		
		buscaFiador = fiadorRepository.buscaFiador(nome, maxResults);
		
		Assert.assertNotNull(buscaFiador);
		
	}
	
	@Test
	public void obterFiadorPorNome(){
		List<Pessoa> lista = fiadorRepository.obterFiadorPorNome("testeNome");
		
		Assert.assertNotNull(lista);
	}
	
	
	
}

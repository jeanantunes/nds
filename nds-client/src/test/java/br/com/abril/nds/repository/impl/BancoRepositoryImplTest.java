package br.com.abril.nds.repository.impl;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.filtro.FiltroConsultaBancosDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaBancosDTO.OrdenacaoColunaBancos;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.repository.BancoRepository;
import br.com.abril.nds.repository.FormaCobrancaRepository;
import br.com.abril.nds.vo.PaginacaoVO;

public class BancoRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private BancoRepository bancoRepository;

	@Autowired
	private FormaCobrancaRepository formaCobrancaRepository;

	// Testa obterQuantidadeBancos() inserindo todos os filtros
	@Test
	public void testarObterQuantidadeBancos() {

		FiltroConsultaBancosDTO filtro = new FiltroConsultaBancosDTO(
				"TesteNome", "1a", "TesteCedente", true);

		Long quantidadeBancos = bancoRepository.obterQuantidadeBancos(filtro);

		Assert.assertNotNull(quantidadeBancos);

	}

	@Test
	public void testarObterQuantidadeBancosNome() {

		FiltroConsultaBancosDTO filtro = new FiltroConsultaBancosDTO();
		filtro.setNome("teste");

		Long quantidadeBancos = bancoRepository.obterQuantidadeBancos(filtro);

		Assert.assertNotNull(quantidadeBancos);

	}

	@Test
	public void testarObterQuantidadeBancosNumero() {

		FiltroConsultaBancosDTO filtro = new FiltroConsultaBancosDTO();
		filtro.setNumero("1a");

		Long quantidadeBancos = bancoRepository.obterQuantidadeBancos(filtro);

		Assert.assertNotNull(quantidadeBancos);

	}

	@Test
	public void testarObterQuantidadeBancosCedente() {

		FiltroConsultaBancosDTO filtro = new FiltroConsultaBancosDTO();
		filtro.setCedente("testeCedente");

		Long quantidadeBancos = bancoRepository.obterQuantidadeBancos(filtro);

		Assert.assertNotNull(quantidadeBancos);

	}

	// Testa obterBancos() inserindo todos os filtros
	@Test
	public void testarObterBancos() {

		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "ASC", "b.id");

		FiltroConsultaBancosDTO filtro = new FiltroConsultaBancosDTO(
				"TesteNome", "1a", "TesteCedente", true);
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColunaBancos.NUMERO_BANCO);

		List<Banco> bancos = bancoRepository.obterBancos(filtro);

		Assert.assertNotNull(bancos);

	}

	@Test
	public void testarObterBancosNome() {

		FiltroConsultaBancosDTO filtro = new FiltroConsultaBancosDTO();
		filtro.setNome("teste");

		List<Banco> bancos = bancoRepository.obterBancos(filtro);

		Assert.assertNotNull(bancos);

	}

	@Test
	public void testarObterBancosNumero() {

		FiltroConsultaBancosDTO filtro = new FiltroConsultaBancosDTO();
		filtro.setNumero("1a");

		List<Banco> bancos = bancoRepository.obterBancos(filtro);

		Assert.assertNotNull(bancos);

	}

	@Test
	public void testarObterBancosCedente() {

		FiltroConsultaBancosDTO filtro = new FiltroConsultaBancosDTO();
		filtro.setCedente("testeCedente");

		List<Banco> bancos = bancoRepository.obterBancos(filtro);

		Assert.assertNotNull(bancos);

	}

	// Testa o SWITCH CASE
	@Test
	public void testarObterBancosOrdenacaoColunaCODIGO() {

		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "ASC", "b.id");

		FiltroConsultaBancosDTO filtro = new FiltroConsultaBancosDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColunaBancos.CODIGO_BANCO);

		List<Banco> bancos = bancoRepository.obterBancos(filtro);

		Assert.assertNotNull(bancos);

	}
	
	@Test
	public void testarObterBancosOrdenacaoColunaNUMERO() {

		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "ASC", "b.id");

		FiltroConsultaBancosDTO filtro = new FiltroConsultaBancosDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColunaBancos.NUMERO_BANCO);

		List<Banco> bancos = bancoRepository.obterBancos(filtro);

		Assert.assertNotNull(bancos);

	}
	
	@Test
	public void testarObterBancosOrdenacaoColunaNOME() {

		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "ASC", "b.id");

		FiltroConsultaBancosDTO filtro = new FiltroConsultaBancosDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColunaBancos.NOME_BANCO);

		List<Banco> bancos = bancoRepository.obterBancos(filtro);

		Assert.assertNotNull(bancos);

	}
	
	@Test
	public void testarObterBancosOrdenacaoColunaAGENCIA() {

		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "ASC", "b.id");

		FiltroConsultaBancosDTO filtro = new FiltroConsultaBancosDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColunaBancos.AGENCIA_BANCO);

		List<Banco> bancos = bancoRepository.obterBancos(filtro);

		Assert.assertNotNull(bancos);

	}
	
	@Test
	public void testarObterBancosOrdenacaoColunaCONTA() {

		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "ASC", "b.id");

		FiltroConsultaBancosDTO filtro = new FiltroConsultaBancosDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColunaBancos.CONTA_CORRENTE_BANCO);

		List<Banco> bancos = bancoRepository.obterBancos(filtro);

		Assert.assertNotNull(bancos);

	}
	
	@Test
	public void testarObterBancosOrdenacaoColunaCEDENTE() {

		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "ASC", "b.id");

		FiltroConsultaBancosDTO filtro = new FiltroConsultaBancosDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColunaBancos.CEDENTE_BANCO);

		List<Banco> bancos = bancoRepository.obterBancos(filtro);

		Assert.assertNotNull(bancos);

	}
	
	@Test
	public void testarObterBancosOrdenacaoColunaAPELIDO() {

		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "ASC", "b.id");

		FiltroConsultaBancosDTO filtro = new FiltroConsultaBancosDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColunaBancos.APELIDO_BANCO);

		List<Banco> bancos = bancoRepository.obterBancos(filtro);

		Assert.assertNotNull(bancos);

	}
	
	@Test
	public void testarObterBancosOrdenacaoColunaCARTEIRA() {

		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "ASC", "b.id");

		FiltroConsultaBancosDTO filtro = new FiltroConsultaBancosDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColunaBancos.CARTEIRA_BANCO);

		List<Banco> bancos = bancoRepository.obterBancos(filtro);

		Assert.assertNotNull(bancos);

	}
	
	@Test
	public void testarObterBancosOrdenacaoColunaATIVO() {

		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "ASC", "b.id");

		FiltroConsultaBancosDTO filtro = new FiltroConsultaBancosDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColunaBancos.ATIVO_BANCO);

		List<Banco> bancos = bancoRepository.obterBancos(filtro);

		Assert.assertNotNull(bancos);

	}
	
	@Test
	public void testarObterBancoPorNumero() {
		
		Banco banco;		
		banco = bancoRepository.obterBancoPorNumero("123");
		
//		Assert.assertNull(banco);		
		
	}
	
	@Test
	public void testarObterBanco() {
		
		String numeroBanco = "123";
		Long numeroAgencia = 1L;
		Long numeroConta = 2L;
		
		Banco banco;		
		banco = bancoRepository.obterBanco(numeroBanco,numeroAgencia,numeroConta);
		
//		Assert.assertNull(banco);	
		
	}
	
	@Test
	public void testarObterBancoPorNome() {
		
		String nome = "teste";
		
		Banco banco;		
		banco = bancoRepository.obterbancoPorNome(nome);
		
//		Assert.assertNull(banco);	
		
	}
	
	@Test
	public void testarDesativarBanco() {
		
		Long idBanco = 1L;
				
		bancoRepository.desativarBanco(idBanco);
		
//		Assert.assertNull();	
		
	}
	
	@Test
	public void testarVerificarPedencias() {
		
		Long idBanco = 1L;
				
		boolean pendencia = bancoRepository.verificarPedencias(idBanco);
		
		Assert.assertFalse(pendencia);	
		
	}

	@SuppressWarnings("unused")
	@Test
	public void obterbancoPorApelido(){
		Banco banco = bancoRepository.obterbancoPorApelido("");
	}
	
}

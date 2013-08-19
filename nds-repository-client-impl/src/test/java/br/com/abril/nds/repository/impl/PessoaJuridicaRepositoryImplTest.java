package br.com.abril.nds.repository.impl;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.cadastro.PessoaJuridica;

public class PessoaJuridicaRepositoryImplTest extends AbstractRepositoryImplTest{
	
	@Autowired
	private PessoaJuridicaRepositoryImpl pessoaJuridicaRepositoryImpl;
	
	@SuppressWarnings("unused")
	@Test
	public void buscarPorCnpj(){
		String cnpj = "123.456.789-89";
		PessoaJuridica pessoaJuridica = pessoaJuridicaRepositoryImpl.buscarPorCnpj(cnpj);
	}
	
	@SuppressWarnings("unused")
	@Test
	public void buscarCnpjPorFornecedor(){
		String nomeFantasia = "Nome";
		PessoaJuridica pessoaJuridica = pessoaJuridicaRepositoryImpl.buscarCnpjPorFornecedor(nomeFantasia);
	}
	
	@SuppressWarnings("unused")
	@Test
	public void hasCnpj(){
		String cnpj = "123.456.789-89";
		Long idPessoa = 1L;
		boolean hasCnpjOrIE = pessoaJuridicaRepositoryImpl.hasCnpj(cnpj, idPessoa);
	}
	
	@SuppressWarnings("unused")
	@Test
	public void hasInscricaoEstadual(){
		String inscricaoEstadual = "123.456.789-89";
		Long idPessoa = 1L;
		boolean hasCnpjOrIE = pessoaJuridicaRepositoryImpl.hasInscricaoEstadual(inscricaoEstadual, idPessoa);
		
	}

	@SuppressWarnings("unused")
	@Test
	public void hasCnpjsemIdPessoa(){
		String cnpj = "123.456.789-89";
		Long idPessoa = null;
		boolean hasCnpjOrIE = pessoaJuridicaRepositoryImpl.hasCnpj(cnpj, idPessoa);
		
	}
	
	@SuppressWarnings("unused")
	@Test
	public void hasInscricaoEstadualsemIdPessoa(){
		String inscricaoEstadual = "123.456.789-89";
		Long idPessoa = null;
		boolean hasCnpjOrIE = pessoaJuridicaRepositoryImpl.hasInscricaoEstadual(inscricaoEstadual, idPessoa);
		
	}
	
}

package br.com.abril.nds.service.builders;

import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TelefoneCota;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.fiscal.nfe.NotaFiscalNds;
import br.com.abril.nds.model.fiscal.nota.CNPJDestinatario;
import br.com.abril.nds.model.fiscal.nota.CPFDestinatario;
import br.com.abril.nds.model.fiscal.nota.DocumentoDestinatario;
import br.com.abril.nds.model.fiscal.nota.IdentificacaoDestinatario;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.fiscal.notafiscal.NotaFiscalEndereco;
import br.com.abril.nds.model.fiscal.notafiscal.NotaFiscalPessoaFisica;
import br.com.abril.nds.model.fiscal.notafiscal.NotaFiscalPessoaJuridica;

public class EmitenteDestinatarioBuilder {

	public static NotaFiscalNds montarEnderecoEmitenteDestinatarioEstoqueProduto(NotaFiscalNds notaFiscal, EstoqueProduto estoque) {
		
		notaFiscal.getEmitenteDestinario().setNome("XXXXXXXXX");
		notaFiscal.getEmitenteDestinario().setEmail("XXXXXXXX");
		
		return notaFiscal;
		
	}

	public static void montarEnderecoEmitenteDestinatario(NotaFiscal notaFiscal2, Cota cota) {
		
		if(notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoDestinatario() == null) {
			notaFiscal2.getNotaFiscalInformacoes().setIdentificacaoDestinatario(new IdentificacaoDestinatario());
		}
		
		if(notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco() == null) {
			notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoDestinatario().setEndereco(new NotaFiscalEndereco());
		}
		
		if (cota.getPessoa() instanceof PessoaJuridica) {
			PessoaJuridica pessoaJuridica = (PessoaJuridica) cota.getPessoa();
			notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoDestinatario().setNome(pessoaJuridica.getRazaoSocial());
			notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoDestinatario().setEmail(pessoaJuridica.getEmail());
			
			notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoDestinatario().setPessoaDestinatarioReferencia(new NotaFiscalPessoaJuridica());
			notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getPessoaDestinatarioReferencia().setNome(pessoaJuridica.getRazaoSocial());
			notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getPessoaDestinatarioReferencia().setEmail(pessoaJuridica.getEmail());
			notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getPessoaDestinatarioReferencia().setIdPessoaOriginal(pessoaJuridica.getId());
			
		} else if (cota.getPessoa() instanceof PessoaFisica) {			
			PessoaFisica pessoaFisica = (PessoaFisica) cota.getPessoa();
			notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoDestinatario().setNome(pessoaFisica.getNome());
			notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoDestinatario().setEmail(pessoaFisica.getEmail());
			
			notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoDestinatario().setPessoaDestinatarioReferencia(new NotaFiscalPessoaFisica());
			notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getPessoaDestinatarioReferencia().setNome(pessoaFisica.getNome());
			notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getPessoaDestinatarioReferencia().setEmail(pessoaFisica.getEmail());
			notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getPessoaDestinatarioReferencia().setIdPessoaOriginal(pessoaFisica.getId());
			
		} else {
			throw new ValidacaoException(TipoMensagem.ERROR, "Tipo de destinatário não identificado.");
		}
		
		DocumentoDestinatario documento = null;
		if(cota.getPessoa() instanceof PessoaJuridica) { 
			documento = new CNPJDestinatario();
		} else {
			documento = new CPFDestinatario();
		}
		documento.setDocumento(cota.getPessoa().getDocumento());
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoDestinatario().setDocumento(documento);
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoDestinatario().setInscricaoEstadual("123");
		
		// notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco().setId(cota.getEnderecoPrincipal().getEndereco().getId());
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco().setLogradouro(cota.getEnderecoPrincipal().getEndereco().getLogradouro());
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco().setNumero(cota.getEnderecoPrincipal().getEndereco().getNumero());
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco().setComplemento(cota.getEnderecoPrincipal().getEndereco().getComplemento());
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco().setBairro(cota.getEnderecoPrincipal().getEndereco().getBairro());
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco().setCep(cota.getEnderecoPrincipal().getEndereco().getCep());
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco().setCidade(cota.getEnderecoPrincipal().getEndereco().getCidade());
		//FIXME: Ajustar para trazer o codigo do municipio
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco().setCodigoCidadeIBGE(3550308L);
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco().setUf(cota.getEnderecoPrincipal().getEndereco().getUf());
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco().setCodigoUf(35L);
		//FIXME: Ajustar os campos codigo e nome do pais
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco().setCodigoPais(1058L);
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco().setPais("Brasil");
		
		if(cota.getEnderecoPrincipal().getEndereco().getCodigoUf() != null) {
			Long codigoUF = Long.parseLong(cota.getEnderecoPrincipal().getEndereco().getCodigoUf());
			//FIXME: ajustar o codigo do estado
			notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco().setCodigoUf(35L);
		}
		
		for (TelefoneCota telefone : cota.getEnderecoPrincipal().getCota().getTelefones()) {
			notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoEmitente().getTelefone().setNumero(telefone.getTelefone().getNumero());
			notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoEmitente().getTelefone().setRamal(telefone.getTelefone().getRamal());
			notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoEmitente().getTelefone().setDDD(telefone.getTelefone().getDdd());
			
			break;
		}
	}
	
	public static void montarEnderecoEmitenteDestinatario(NotaFiscal notaFiscal2, Distribuidor distribuidor) {
		
		if(notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoDestinatario() == null) {
			notaFiscal2.getNotaFiscalInformacoes().setIdentificacaoDestinatario(new IdentificacaoDestinatario());
		}
		
		if(notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco() == null) {
			notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoDestinatario().setEndereco(new NotaFiscalEndereco());
		}
		
		if (distribuidor.getJuridica() instanceof PessoaJuridica) {
			PessoaJuridica pessoaJuridica = (PessoaJuridica) distribuidor.getJuridica();
			notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoDestinatario().setNome(pessoaJuridica.getRazaoSocial());
			notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoDestinatario().setEmail(pessoaJuridica.getEmail());
			
			notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoDestinatario().setPessoaDestinatarioReferencia(new NotaFiscalPessoaJuridica());
			notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getPessoaDestinatarioReferencia().setNome("JORNALIROS DIVERSOS");
			notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getPessoaDestinatarioReferencia().setEmail(pessoaJuridica.getEmail());
			notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getPessoaDestinatarioReferencia().setIdPessoaOriginal(pessoaJuridica.getId());
			
		} else {
			throw new ValidacaoException(TipoMensagem.ERROR, "Tipo de destinatário não identificado.");
		}
		
		DocumentoDestinatario documento = new CNPJDestinatario();
		
		documento.setDocumento(distribuidor.getJuridica().getDocumento());
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoDestinatario().setDocumento(documento);
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoDestinatario().setInscricaoEstadual(distribuidor.getJuridica().getInscricaoEstadual());
		
		for (Endereco endereco : distribuidor.getJuridica().getEnderecos()) {
			notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco().setLogradouro(endereco.getLogradouro());
			notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco().setNumero(endereco.getNumero());
			notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco().setComplemento(endereco.getComplemento());
			notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco().setBairro(endereco.getBairro());
			notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco().setCep(endereco.getCep());
			notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco().setCidade(endereco.getCidade());
			//FIXME: Ajustar para trazer o codigo do municipio
			notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco().setCodigoCidadeIBGE(Long.valueOf(endereco.getCodigoCidadeIBGE()));
			notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco().setUf(endereco.getUf());
			notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco().setCodigoUf(Long.valueOf(endereco.getCodigoUf()));
			//FIXME: Ajustar os campos codigo e nome do pais
			notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco().setCodigoPais(1058L);
			notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco().setPais("Brasil");
			
			break;
		}
		
		for (Telefone telefone : distribuidor.getJuridica().getTelefones()) {
			notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoEmitente().getTelefone().setNumero(telefone.getNumero());
			notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoEmitente().getTelefone().setRamal(telefone.getRamal());
			notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoEmitente().getTelefone().setDDD(telefone.getDdd());
			
			break;
		}
	}
	
	public static void montarEnderecoEmitenteDestinatario(NotaFiscal notaFiscal2, EstoqueProduto estoque) {
		
		if(notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoDestinatario() == null) {
			notaFiscal2.getNotaFiscalInformacoes().setIdentificacaoDestinatario(new IdentificacaoDestinatario());
		}
		
		if(notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco() == null) {
			notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoDestinatario().setEndereco(new NotaFiscalEndereco());
		}
	}
}
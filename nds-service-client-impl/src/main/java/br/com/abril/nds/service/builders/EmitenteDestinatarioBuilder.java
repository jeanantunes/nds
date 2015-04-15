package br.com.abril.nds.service.builders;

import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoDistribuidor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TelefoneCota;
import br.com.abril.nds.model.cadastro.TelefoneFornecedor;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.fiscal.TipoDestinatario;
import br.com.abril.nds.model.fiscal.nota.CNPJDestinatario;
import br.com.abril.nds.model.fiscal.nota.CPFDestinatario;
import br.com.abril.nds.model.fiscal.nota.DocumentoDestinatario;
import br.com.abril.nds.model.fiscal.nota.IdentificacaoDestinatario;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.fiscal.notafiscal.NotaFiscalEndereco;
import br.com.abril.nds.model.fiscal.notafiscal.NotaFiscalPessoaFisica;
import br.com.abril.nds.model.fiscal.notafiscal.NotaFiscalPessoaJuridica;
import br.com.abril.nds.util.StringUtil;

public class EmitenteDestinatarioBuilder {

	public static void montarEnderecoEmitenteDestinatario(NotaFiscal notaFiscal, Cota cota) {
		
		if(cota != null) {
			notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().setCota(cota);
		}

		DocumentoDestinatario documento = montarDadosPessoa(notaFiscal, cota.getPessoa());
		
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().setTipoDestinatario(TipoDestinatario.COTA);
		documento.setDocumento(cota.getPessoa().getDocumento().replaceAll("/", "").replaceAll("\\.", "").replaceAll("-", ""));
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().setDocumento(documento);

		String tipoLogradouro = "";
		if(cota.getEnderecoPrincipal() == null) {
			throw new ValidacaoException(TipoMensagem.WARNING, String.format("Cota %s sem endereco principal.", cota.getNumeroCota())); 
		}
		
		if(cota.getEnderecoPrincipal().getEndereco() == null) {
			throw new ValidacaoException(TipoMensagem.WARNING, String.format("Cota %s sem endereço. ", cota.getNumeroCota())); 
		}
		
		if(cota.getEnderecoPrincipal().getEndereco().getTipoLogradouro() == null) {
			throw new ValidacaoException(TipoMensagem.WARNING, String.format("Cota %s sem Tipo Logradouro no endereço.", cota.getNumeroCota())); 
		}
		
		if(cota.getEnderecoPrincipal().getEndereco() != null && cota.getEnderecoPrincipal().getEndereco().getTipoLogradouro() != null) {
			tipoLogradouro = cota.getEnderecoPrincipal().getEndereco().getTipoLogradouro();
		}
		
		StringBuilder logradouro = new StringBuilder(tipoLogradouro)
			.append(" ")
			.append(cota.getEnderecoPrincipal().getEndereco().getLogradouro());
		
		
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco().setLogradouro(StringUtil.limparString(logradouro.toString()));
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco().setNumero(cota.getEnderecoPrincipal().getEndereco().getNumero());
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco().setComplemento(StringUtil.limparString(cota.getEnderecoPrincipal().getEndereco().getComplemento()));
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco().setBairro(StringUtil.limparString(cota.getEnderecoPrincipal().getEndereco().getBairro()));
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco().setCep(cota.getEnderecoPrincipal().getEndereco().getCep());
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco().setCidade(StringUtil.limparString(cota.getEnderecoPrincipal().getEndereco().getCidade()));
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco().setCodigoCidadeIBGE(cota.getEnderecoPrincipal().getEndereco().getCodigoCidadeIBGE());
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco().setUf(cota.getEnderecoPrincipal().getEndereco().getUf());
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco().setCodigoUf(cota.getEnderecoPrincipal().getEndereco().getCodigoUf());
		//FIXME: Ajustar os campos codigo e nome do pais
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco().setCodigoPais(1058);
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco().setPais("Brasil");

		for (TelefoneCota telefone : cota.getEnderecoPrincipal().getCota().getTelefones()) {
			
			notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getTelefone().setDDD(telefone.getTelefone().getDdd());
			notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getTelefone().setNumero(telefone.getTelefone().getNumero());
			notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getTelefone().setRamal(telefone.getTelefone().getRamal());

			break;
		}
	}

	public static void montarEnderecoEmitenteDestinatario(NotaFiscal notaFiscal, Fornecedor fornecedor) {

		DocumentoDestinatario documento = montarDadosPessoa(notaFiscal, fornecedor.getJuridica());
		
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().setTipoDestinatario(TipoDestinatario.FORNECEDOR);
		documento.setDocumento(fornecedor.getJuridica().getDocumento().replaceAll("/", "").replaceAll("\\.", "").replaceAll("-", ""));
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().setDocumento(documento);

		StringBuilder logradouro = new StringBuilder(fornecedor.getEnderecoPrincipal().getEndereco().getTipoLogradouro())
		.append(" ")
		.append(fornecedor.getEnderecoPrincipal().getEndereco().getLogradouro());
		
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco().setLogradouro(StringUtil.limparString(logradouro.toString()));
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco().setNumero(fornecedor.getEnderecoPrincipal().getEndereco().getNumero());
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco().setComplemento(StringUtil.limparString(fornecedor.getEnderecoPrincipal().getEndereco().getComplemento()));
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco().setBairro(StringUtil.limparString(fornecedor.getEnderecoPrincipal().getEndereco().getBairro()));
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco().setCep(fornecedor.getEnderecoPrincipal().getEndereco().getCep().replaceAll("-", ""));
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco().setCidade(StringUtil.limparString(fornecedor.getEnderecoPrincipal().getEndereco().getCidade()));
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco().setCodigoCidadeIBGE(fornecedor.getEnderecoPrincipal().getEndereco().getCodigoCidadeIBGE());
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco().setUf(fornecedor.getEnderecoPrincipal().getEndereco().getUf());
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco().setCodigoUf(fornecedor.getEnderecoPrincipal().getEndereco().getCodigoUf());
		//FIXME: Ajustar os campos codigo e nome do pais
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco().setCodigoPais(1058);
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco().setPais("Brasil");

		for (TelefoneFornecedor telefone : fornecedor.getTelefones()) {
			
			notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getTelefone().setNumero(telefone.getTelefone().getNumero());
			notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getTelefone().setRamal(telefone.getTelefone().getRamal());
			notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getTelefone().setDDD(telefone.getTelefone().getDdd());

			break;
		}
	}

	public static void montarEnderecoEmitenteDestinatario(NotaFiscal notaFiscal, Distribuidor distribuidor) {
		
		DocumentoDestinatario documento = montarDadosPessoa(notaFiscal, distribuidor.getJuridica());
		
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().setTipoDestinatario(TipoDestinatario.DISTRIBUIDOR);
		documento.setDocumento(distribuidor.getJuridica().getDocumento().replaceAll("/", "").replaceAll("\\.", "").replaceAll("-", ""));
		
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().setDocumento(documento);
		
		if(distribuidor.getJuridica().getInscricaoEstadual() != null && !distribuidor.getJuridica().getInscricaoEstadual().isEmpty()){
			notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().setInscricaoEstadual(distribuidor.getJuridica().getInscricaoEstadual().trim());			
		}
		
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().setIndicadorDestinatario(9);
		
		if(distribuidor.getJuridica().getEnderecos() != null 
				&& !distribuidor.getJuridica().getEnderecos().isEmpty()) {
			
			for (Endereco endereco : distribuidor.getJuridica().getEnderecos()) {
				
				montarEndereco(notaFiscal, endereco);
				
				break;
			}
		} else {
			
			EnderecoDistribuidor enderecoDistribuidor =  distribuidor.getEnderecoDistribuidor();
			if(enderecoDistribuidor != null && enderecoDistribuidor.getEndereco() != null) {
				montarEndereco(notaFiscal, enderecoDistribuidor.getEndereco());
			} else {
				throw new ValidacaoException(TipoMensagem.ERROR, "Problemas com o Endereço do distribuidor.");
			}
			
		}
		
		
		for (Telefone telefone : distribuidor.getJuridica().getTelefones()) {
			
			notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getTelefone().setNumero(telefone.getNumero());
			notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getTelefone().setRamal(telefone.getRamal());
			notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getTelefone().setDDD(telefone.getDdd());
			
			break;
		}
	}

	private static void montarEndereco(NotaFiscal notaFiscal, Endereco endereco) {
		
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco().setTipoLogradouro(endereco.getTipoLogradouro());
		
		StringBuilder logradouro = new StringBuilder(endereco.getTipoLogradouro())
		.append(" ")
		.append(endereco.getLogradouro());
		
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco().setLogradouro(StringUtil.limparString(logradouro.toString()));
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco().setNumero(endereco.getNumero());
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco().setComplemento(StringUtil.limparString(endereco.getComplemento()));
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco().setBairro(StringUtil.limparString(endereco.getBairro()));
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco().setCep(endereco.getCep());
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco().setCidade(StringUtil.limparString(endereco.getCidade()));
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco().setCodigoCidadeIBGE(endereco.getCodigoCidadeIBGE()); // 3550308L 
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco().setUf(endereco.getUf());
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco().setCodigoUf(endereco.getCodigoUf()); // 35L
		//FIXME: Ajustar os campos codigo e nome do pais
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco().setCodigoPais(1058);
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco().setPais("Brasil");
		
	}

	
	private static DocumentoDestinatario montarDadosPessoa(NotaFiscal notaFiscal, Pessoa pessoa) {
		
		if(notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario() == null) {
			notaFiscal.getNotaFiscalInformacoes().setIdentificacaoDestinatario(new IdentificacaoDestinatario());
		}

		if(notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco() == null) {
			notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().setEndereco(new NotaFiscalEndereco());
		}

		if (pessoa instanceof PessoaJuridica) {
			PessoaJuridica pessoaJuridica = (PessoaJuridica) pessoa;
			notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().setNome(pessoaJuridica.getRazaoSocial().trim());
			notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().setEmail(pessoaJuridica.getEmail());

			notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().setPessoaDestinatarioReferencia(new NotaFiscalPessoaJuridica());
			notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getPessoaDestinatarioReferencia().setNome(pessoaJuridica.getRazaoSocial());
			notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getPessoaDestinatarioReferencia().setEmail(pessoaJuridica.getEmail());
			notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getPessoaDestinatarioReferencia().setIdPessoaOriginal(pessoaJuridica.getId());
			
			if(pessoaJuridica.getInscricaoEstadual() != null && !pessoaJuridica.getInscricaoEstadual().isEmpty()){
				notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().setInscricaoEstadual(pessoaJuridica.getInscricaoEstadual().trim());
			}
			
			notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().setIndicadorDestinatario(9);
		} else if (pessoa instanceof PessoaFisica) {			
			PessoaFisica pessoaFisica = (PessoaFisica) pessoa;
			notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().setNome(pessoaFisica.getNome().trim());
			notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().setEmail(pessoaFisica.getEmail());

			notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().setPessoaDestinatarioReferencia(new NotaFiscalPessoaFisica());
			notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getPessoaDestinatarioReferencia().setNome(pessoaFisica.getNome());
			notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getPessoaDestinatarioReferencia().setEmail(pessoaFisica.getEmail());
			notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getPessoaDestinatarioReferencia().setIdPessoaOriginal(pessoaFisica.getId());
//			notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().setInscricaoEstadual("");
			notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().setIndicadorDestinatario(9);
		} else {
			throw new ValidacaoException(TipoMensagem.ERROR, "Tipo de destinatário não identificado.");
		}

		DocumentoDestinatario documento = null;
		if(pessoa instanceof PessoaJuridica) { 
			documento = new CNPJDestinatario();
		} else {
			documento = new CPFDestinatario();
		}
		
		return documento;
	}


	public static void montarEnderecoEmitenteDestinatario(NotaFiscal notaFiscal, EstoqueProduto estoque) {

		if(notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario() == null) {
			notaFiscal.getNotaFiscalInformacoes().setIdentificacaoDestinatario(new IdentificacaoDestinatario());
		}

		if(notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco() == null) {
			notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().setEndereco(new NotaFiscalEndereco());
		}

		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().setTipoDestinatario(TipoDestinatario.FORNECEDOR);
	}
}
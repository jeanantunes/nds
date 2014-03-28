package br.com.abril.nds.service.builders;

import java.util.List;

import br.com.abril.nds.model.cadastro.EnderecoTransportador;
import br.com.abril.nds.model.cadastro.Transportador;
import br.com.abril.nds.model.cadastro.Veiculo;
import br.com.abril.nds.model.fiscal.NaturezaOperacao;
import br.com.abril.nds.model.fiscal.nota.InformacaoTransporte;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.fiscal.nota.TransportadorWrapper;
import br.com.abril.nds.model.fiscal.notafiscal.NotaFiscalEndereco;

public class NotaFiscalTransportadorBuilder {
	
	public static NotaFiscal montarTransportador(NotaFiscal notaFiscal, NaturezaOperacao naturezaOperacao, List<Transportador> transportadores){
		
		NaturezaOperacao naOperacao = new NaturezaOperacao();
		
		naOperacao.setId(naturezaOperacao.getId());
		naOperacao.setCfopEstado(naturezaOperacao.getCfopEstado());
		naOperacao.setCfopOutrosEstados(naturezaOperacao.getCfopOutrosEstados());
		naOperacao.setDescricao(naturezaOperacao.getDescricao());
		// notaFiscal.setNaturezaOperacao(naOperacao);
		
		if(notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte() == null){
			notaFiscal.getNotaFiscalInformacoes().setInformacaoTransporte(new InformacaoTransporte());
		}
		
		if(notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().getTransportadorWrapper() == null){
			notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().setTransportadorWrapper(new TransportadorWrapper());
		}
		
		if(notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().getTransportadorWrapper().getVeiculo() == null){
			notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().getTransportadorWrapper().setVeiculo(new br.com.abril.nds.model.fiscal.nota.Veiculo());
		}
		
		NotaFiscalEndereco endereco = null;
		if(notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().getTransportadorWrapper().getEndereco() == null){
			endereco = new NotaFiscalEndereco();
		}
		
		if(transportadores == null || transportadores.isEmpty()){
			
			notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().getTransportadorWrapper().setDocumento("07348198000148");	
			notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().getTransportadorWrapper().setNome("Treelog");	
			endereco.setBairro("Osasco");
			endereco.setLogradouro("Kenkiti Shinomoto");
			endereco.setCep("08250000");
			endereco.setNumero("158");
			endereco.setCodigoCidadeIBGE(3550308L);
			endereco.setCidade("Sãp Paulo");
			endereco.setCodigoPais(30L);
			endereco.setCodigoUf(20L);
			endereco.setComplemento("XXXX");
			endereco.setPais("Brasil");
			endereco.setTipoLogradouro("Rua");
			endereco.setUf("SP");
			notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().getTransportadorWrapper().setEndereco(endereco);
			notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().setModalidadeFrete(1);
		}else{
			for (Transportador transportador : transportadores) {
				
				notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().getTransportadorWrapper().setDocumento(transportador.getPessoaJuridica().getCnpj());	
				notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().getTransportadorWrapper().setNome(transportador.getPessoaJuridica().getNome());	
				
				montarEnderecoTransporte(notaFiscal, transportador.getEnderecosTransportador());
				// montarVeiculo(notaFiscal, transportador.getVeiculos());
				break;
			}
			
		}
		
		return notaFiscal;
	}
	
	private static void montarEnderecoTransporte(NotaFiscal notaFiscal, List<EnderecoTransportador> enderecoTransportadores){
		
		if(enderecoTransportadores == null || enderecoTransportadores.isEmpty()){
			notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().getTransportadorWrapper().getEndereco().setBairro("Osasco");
			notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().getTransportadorWrapper().getEndereco().setLogradouro("Kenkiti Shinomoto");
			notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().getTransportadorWrapper().getEndereco().setCep("08250000");
			notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().getTransportadorWrapper().getEndereco().setNumero("158");
			notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().getTransportadorWrapper().getEndereco().setCodigoCidadeIBGE(3550308L);
			notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().getTransportadorWrapper().getEndereco().setCidade("Sãp Paulo");
			notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().getTransportadorWrapper().getEndereco().setCodigoPais(30L);
			notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().getTransportadorWrapper().getEndereco().setCodigoUf(20L);
			notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().getTransportadorWrapper().getEndereco().setComplemento("XXXX");
			notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().getTransportadorWrapper().getEndereco().setPais("Brasil");
			notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().getTransportadorWrapper().getEndereco().setTipoLogradouro("Rua");
			notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().getTransportadorWrapper().getEndereco().setUf("SP");
			notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().setModalidadeFrete(1);
			
		}else{

			for (EnderecoTransportador enderecoTransportador : enderecoTransportadores){
				if(notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().getTransportadorWrapper().getEndereco() == null){
					notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().getTransportadorWrapper().setEndereco(new NotaFiscalEndereco());
				}
				if(enderecoTransportador.isPrincipal()){
					
					NotaFiscalEndereco endereco = new NotaFiscalEndereco(); 
					
					endereco.setBairro(enderecoTransportador.getEndereco().getBairro());
					endereco.setCep(enderecoTransportador.getEndereco().getCep());
					endereco.setNumero(enderecoTransportador.getEndereco().getNumero());
					endereco.setCodigoCidadeIBGE(Long.valueOf(enderecoTransportador.getEndereco().getCodigoCidadeIBGE()));
					endereco.setCidade(enderecoTransportador.getEndereco().getCidade());
					endereco.setCodigoPais(0L);
					endereco.setCodigoUf(35L); //Long.valueOf(enderecoTransportador.getEndereco().getCodigoUf()));
					endereco.setComplemento(enderecoTransportador.getEndereco().getComplemento());
					endereco.setPais("Brasil");
					endereco.setTipoLogradouro(enderecoTransportador.getEndereco().getTipoLogradouro());
					endereco.setUf(enderecoTransportador.getEndereco().getUf());
					notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().getTransportadorWrapper().setEndereco(endereco);
					notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().setModalidadeFrete(1);
					break;
				}
			}
		}
	} 
	
	
	@SuppressWarnings("unused")
	private static void montarVeiculo(NotaFiscal notaFiscal, List<Veiculo> veiculos){
		
		for (Veiculo veiculo : veiculos) {			
			notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().getTransportadorWrapper().getVeiculo().setPlaca(veiculo.getPlaca());
			notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().getTransportadorWrapper().getVeiculo().setUf("SP");
			notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().getTransportadorWrapper().getVeiculo().setRegistroTransCarga("Revista");
			
			break;
		}
	}
}

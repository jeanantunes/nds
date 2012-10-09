package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.abril.nds.export.cnab.cobranca.DetalheSegmentoP;
import br.com.abril.nds.export.cnab.cobranca.Header;
import br.com.abril.nds.export.cnab.cobranca.Trailer;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.financeiro.Boleto;
import br.com.abril.nds.repository.BoletoRepository;
import br.com.abril.nds.repository.CobrancaRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.service.GeradorArquivoCobrancaBancoService;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.DateUtil;

import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;
import com.ancientprogramming.fixedformat4j.format.impl.FixedFormatManagerImpl;


@Service
public class GeradorArquivoCobrancaBancoServiceImpl implements GeradorArquivoCobrancaBancoService {
	
	@Autowired
	private CobrancaRepository cobrancaRepository;
	
	@Autowired
	private DistribuidorRepository distribuidorRepository;
	
	@Autowired
	private BoletoRepository boletoRepository;
	
	@Override
	public void gerarArquivoCobranca(Map<Banco, List<DetalheSegmentoP>> mapaArquivoCobranca) {
		
		FixedFormatManager manager = new FixedFormatManagerImpl();
		
		Header header = new Header();
		
		header.setCodigoBanco(11L);
		header.setLote(22L);
		
		String retornoHeader = manager.export(header);
		
		Trailer trailer = new Trailer();
		
		trailer.setCodigoBanco(3L);
		
		String retornoTrailer = manager.export(trailer);
	}
	
	/**
	 * 
	 * Retorna um map com a estrutura do arquivo preenchida para geração.
	 * 
	 * @return Map<Banco, List<DetalheSegmentoP>> 
	 */
	private Map<Banco, List<DetalheSegmentoP>> prepararDadosCobranca(){
		
		Map<Banco, List<DetalheSegmentoP>> inputDados = new HashMap<Banco, List<DetalheSegmentoP>>();
		
		Distribuidor distribuidor = distribuidorRepository.obter();
		
		List<Boleto> cobrancas = boletoRepository.obterBoletosGeradosNaDataOperacaoDistribuidor(distribuidor.getDataOperacao());
		
		for(Boleto cobranca : cobrancas){
			
			DetalheSegmentoP detalhe = obterDetalheSegmentoP(cobranca);
			
			Banco banco = cobranca.getBanco();
			
			List<DetalheSegmentoP> listaDetalheP = 
										((inputDados.containsKey(banco)))
											?  inputDados.get(banco) 
													: new ArrayList<DetalheSegmentoP>(); 
			listaDetalheP.add(detalhe);
			
			inputDados.put(banco, listaDetalheP);
		}
		
		return inputDados;
	}
	
	/**
	 * Retorna um objeto com o detalhamento da segmentação "P" para geração de arquivo de remessa.
	 * 
	 * @param cobranca - cobrança a ser processada
	 * 
	 * @return DetalheSegmentoP
	 */
	private DetalheSegmentoP obterDetalheSegmentoP(Boleto cobranca) {
		
		DetalheSegmentoP detalheSegmentoP = new DetalheSegmentoP();
		
		Banco banco = cobranca.getBanco();
		
		
		//Controle
		detalheSegmentoP.setCodigoBanco(Long.parseLong(banco.getNumeroBanco()));
		detalheSegmentoP.setLote(null);
		
		//Serviço
		detalheSegmentoP.setNumeroRegistro(null);
		detalheSegmentoP.setSegmento(null);
		detalheSegmentoP.setCnab1(null);
		detalheSegmentoP.setCodigoMovimento(null);
		
		//Conta Corrente
		detalheSegmentoP.setCodigoAgencia(banco.getAgencia());
		detalheSegmentoP.setDvAgencia(banco.getDvAgencia());
		detalheSegmentoP.setNumeroConta(Long.parseLong(banco.getNumeroBanco()));
		detalheSegmentoP.setDvConta(banco.getDvConta());
		detalheSegmentoP.setDvAgenciaConta(null);
		detalheSegmentoP.setNossoNumero(cobranca.getNossoNumero());
		
		//Caracteristica Cobrança
		detalheSegmentoP.setCodigoCarteira(banco.getCarteira().longValue());
		detalheSegmentoP.setCadastramento(null);
		detalheSegmentoP.setTipoDocumento(null);
		detalheSegmentoP.setEmissaoBloqueto(null);
		detalheSegmentoP.setDistribuicaoBloqueto(null);
		detalheSegmentoP.setNumeroDocumento(null);
		detalheSegmentoP.setDataVencimento(this.getDataFormatoCNAB(cobranca.getDataVencimento()));
		detalheSegmentoP.setValorTitulo(this.getValorFormatoCNAB(cobranca.getValor()));
		detalheSegmentoP.setAgenciaCobradora(banco.getAgencia());
		detalheSegmentoP.setEspecieTitulo(null);
		detalheSegmentoP.setAceite(null);
		detalheSegmentoP.setDataEmissaoTitulo(this.getDataFormatoCNAB(cobranca.getDataEmissao()));
		
		//Juros
		detalheSegmentoP.setCodigoJurosMora(null);
		detalheSegmentoP.setDataJurosMora(null);
		detalheSegmentoP.setJurosMora(null);
		
		//Desconto
		detalheSegmentoP.setCodigoDesconto(null);
		detalheSegmentoP.setDataDesconto(null);
		detalheSegmentoP.setDesconto(null);
		detalheSegmentoP.setValorIOF(null);
		detalheSegmentoP.setValorAbatimento(null);
		detalheSegmentoP.setIdentificacaoTituloEmpresa(null);
		detalheSegmentoP.setCodigoProtesto(null);
		detalheSegmentoP.setPrazoProtesto(null);
		detalheSegmentoP.setCodigoBaixaDevolucao(null);
		detalheSegmentoP.setPrazoBaixaDevolucao(null);
		detalheSegmentoP.setCodigoMoeda(null);
		detalheSegmentoP.setUsoLivre(null);
		
		return null;
	}
	
	private Long getValorFormatoCNAB(BigDecimal valor){
		
		return Long.parseLong( valor.toString().replace(".","").trim());
	}
	
	private Long getDataFormatoCNAB(Date data ){
		
		return Long.parseLong(DateUtil.formatarData(data, Constantes.FORMATO_DATA_ARQUIVO_CNAB));
	}
}

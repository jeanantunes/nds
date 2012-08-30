package br.com.abril.nds.service.impl;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.CobrancaImpressaoDTO;
import br.com.abril.nds.dto.GeraDividaDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.EnderecoDistribuidor;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.Roteirizacao;
import br.com.abril.nds.model.cadastro.TelefoneDistribuidor;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.financeiro.Cobranca;
import br.com.abril.nds.repository.CobrancaRepository;
import br.com.abril.nds.service.BoletoService;
import br.com.abril.nds.service.DocumentoCobrancaService;
import br.com.abril.nds.service.EmailService;
import br.com.abril.nds.service.PoliticaCobrancaService;
import br.com.abril.nds.service.RoteirizacaoService;
import br.com.abril.nds.service.exception.AutenticacaoEmailException;
import br.com.abril.nds.util.AnexoEmail;
import br.com.abril.nds.util.TipoMensagem;

@Service
public class DocumentoCobrancaServiceImpl implements DocumentoCobrancaService {

	@Autowired
	private CobrancaRepository cobrancaRepository;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private PoliticaCobrancaService politicaCobrancaService;
	
	@Autowired
	private RoteirizacaoService roteirizacaoService;
	
	
	@Override
	@Transactional
	public byte[] gerarDocumentoCobranca(String nossoNumero) {
		
		Cobranca cobranca = this.cobrancaRepository.obterCobrancaPorNossoNumero(nossoNumero);
		
		byte[] retorno = null;
	
		try {
			
			switch (cobranca.getTipoCobranca()) {
				case BOLETO:
					retorno = boletoService.gerarImpressaoBoleto(nossoNumero);
					break;
					
				default:
					retorno = getDocumentoCobranca(cobranca);
			}
			
		} catch (Exception e) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Erro ao gerar arquivo de cobrança para nosso número: " + nossoNumero + " - " + e.getMessage());
		}
		
		this.cobrancaRepository.incrementarVia(nossoNumero);
		
		return retorno;
	}
	
	@Transactional
	@Override
	public void enviarDocumentoCobrancaPorEmail(String nossoNumero){
		
		Cobranca cobranca = this.cobrancaRepository.obterCobrancaPorNossoNumero(nossoNumero);
		
		try {
			switch (cobranca.getTipoCobranca()) {
				case BOLETO:

					boletoService.enviarBoletoEmail(nossoNumero);
					break;

				default:
					enviarDocumentoPorEmail(cobranca);
				
			}
		} catch (Exception e) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Erro ao enviar e-mail de arquivo de cobrança para nosso número: " + nossoNumero + " - " + e.getMessage());
		}
		
		this.cobrancaRepository.incrementarVia(nossoNumero);
	}
	
	/**
	 * @see 
	 */
	@Transactional
	@Override
	public byte[] gerarDocumentoCobranca(List<GeraDividaDTO> dividas, TipoCobranca tipoCobranca) {
		
		List<String> listNossoNumero = getNossoNumeros(dividas); 
		
		byte[] arquivo = null;
		
		try {
		
			if(TipoCobranca.BOLETO.equals(tipoCobranca)){
	
				arquivo =boletoService.gerarImpressaoBoletos(listNossoNumero);
			}
			else{
				
				arquivo = gerarDocumentoCobrancas(listNossoNumero);
			}
			
			if(arquivo!= null){
				this.cobrancaRepository.incrementarVia( listNossoNumero.toArray(new String[] {}) );
			}
		} catch (Exception e) {
			throw new ValidacaoException(TipoMensagem.ERROR, e.getMessage());
		}
		
		return arquivo;
	}
	
	/**
	 * Retorna um grupo de documentos de cobrança. 
	 * @param listNossoNumero
	 * @return byte[]
	 * @throws JRException
	 */
	
	private byte[] gerarDocumentoCobrancas(List<String> listNossoNumero) throws Exception {
		
		List<CobrancaImpressaoDTO> cobrancas = new ArrayList<CobrancaImpressaoDTO>();
		
		Cobranca cobranca = null;
		
		Distribuidor distribuidor = distribuidorService.obter();
		
		for(String nossoNumero : listNossoNumero) {
			
			cobranca = this.cobrancaRepository.obterCobrancaPorNossoNumero(nossoNumero);
			
			cobrancas.add( obterCobrancaImpressaoDTO(cobranca, distribuidor));
		}
		
		byte[] arquivo = gerarDocumentoIreport(cobrancas);
		
		return arquivo ;
	}
	
	/**
	 * Retorna uma lista com os nosso número referente as cobranças.
	 * @param dividas
	 * @return List<String>
	 */
	private List<String> getNossoNumeros(List<GeraDividaDTO> dividas){
		
		List<String> list = new ArrayList<String>();
		
		for(GeraDividaDTO dto : dividas){
			list.add(dto.getNossoNumero());
		}
		
		return list;
	}
	
	/**
	 * Envia um tipo de cobrança por email.
	 * @param cobranca
	 * @throws AutenticacaoEmailException
	 * @throws JRException 
	 */
	private void enviarDocumentoPorEmail(Cobranca cobranca) throws AutenticacaoEmailException, Exception {
		
		PoliticaCobranca politicaPrincipal = this.politicaCobrancaService.obterPoliticaCobrancaPrincipal();
		
		String assunto=(politicaPrincipal!=null
						? politicaPrincipal.getAssuntoEmailCobranca():"");
		
		String mensagem=(politicaPrincipal!=null
						? politicaPrincipal.getMensagemEmailCobranca():"");
		
		String emailCota = cobranca.getCota().getPessoa().getEmail();
		String[] destinatarios = new String[]{emailCota};
		
		byte[] arquivo = getDocumentoCobranca(cobranca);
		
		AnexoEmail anexoEmail = new AnexoEmail();
		anexoEmail.setNome(cobranca.getNossoNumero());
		anexoEmail.setAnexo(arquivo);
		anexoEmail.setTipoAnexo(AnexoEmail.TipoAnexo.PDF);
		
		emailService.enviar(assunto,mensagem,destinatarios,anexoEmail);
	}
	
	/**
	 * Retorna o documento de cobrança gerado pelo Ireport
	 * @param cobrancas
	 * @return byte[]
	 * @throws JRException
	 */
	private byte[] getDocumentoCobranca(Cobranca... cobrancas) throws Exception{
		
		Distribuidor distribuidor  = distribuidorService.obter();
		
		List<CobrancaImpressaoDTO> list = new ArrayList<CobrancaImpressaoDTO>();
		
		for(Cobranca cobranca : cobrancas){
			list.add(obterCobrancaImpressaoDTO(cobranca,distribuidor));
		}
		
		return gerarDocumentoIreport(list);
	}
	
	/**
	 * Retorna um grupo de documentos de cobrança gerado pelo Ireport
	 * @param list
	 * @return  byte[] 
	 * @throws JRException
	 */
	private byte[] gerarDocumentoIreport(List<CobrancaImpressaoDTO> list) throws Exception{
		
		JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);
		
		 Map<String, Object> map = getInformacoesDistribuido();
		 URL url = Thread.currentThread().getContextClassLoader()
				 	.getResource("/reports/cobranca.jasper");
		 //Executa o decode do path do arquivo
		 //String path = url.toURI().getPath();
		 String path = "/nds-client/reports/cobranca.jasper";
		 
		 return  JasperRunManager.runReportToPdf(path, map, jrDataSource);
	}
	
	/**
	 * Retorna as informações do distribuidor para montagem dos parâmetros do Ireport
	 * @return Map<String, Object>
	 */
	private Map<String, Object> getInformacoesDistribuido(){
		
		Distribuidor distribuidor = distribuidorService.obter();
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		EnderecoDistribuidor enderecoDistribuidor = distribuidor.getEnderecoDistribuidor();
		
		map.put("cidade",(enderecoDistribuidor.getEndereco()==null)?"": enderecoDistribuidor.getEndereco().getCidade());
		map.put("enderecoDistribuidor", (enderecoDistribuidor.getEndereco()==null)?"":enderecoDistribuidor.getEndereco().getLogradouro() );
		
		for(TelefoneDistribuidor telefone : distribuidor.getTelefones()){
			
			if(telefone.isPrincipal()){
				map.put("telefoneDistribuidor", (telefone.getTelefone()== null)?""
						:telefone.getTelefone().getDdd() + " - " +
							telefone.getTelefone().getNumero());
				break;
			}
		}
		
		map.put("data", new Date());
		map.put("nomeDistribuidor",distribuidor.getJuridica().getRazaoSocial());
		
		return map;
	}
	
	/**
	 * Monta a estrutura do objeto para impressão no Ireport
	 * @param cobranca 
	 * @param distribuidor
	 * @return CobrancaImpressaoDTO
	 */
	private CobrancaImpressaoDTO obterCobrancaImpressaoDTO(Cobranca cobranca,Distribuidor distribuidor){
		
		CobrancaImpressaoDTO impressaoDTO = new CobrancaImpressaoDTO();
		
		Banco banco  = cobranca.getBanco();
		
		if(banco!= null){
			impressaoDTO.setAgencia(banco.getAgencia().intValue());
			impressaoDTO.setConta(banco.getConta().toString());
			impressaoDTO.setNomeBanco(banco.getNome());
		}
		
		Cota cota  = cobranca.getCota();
		
		if(cota!= null){
			
			Pessoa pessoa  = cota.getPessoa();
			
			if (pessoa instanceof PessoaFisica){
				impressaoDTO.setNomeCota(((PessoaFisica) pessoa).getNome());
			}
			else if(pessoa instanceof PessoaJuridica){
				impressaoDTO.setNomeCota(((PessoaJuridica) pessoa).getRazaoSocial()); 
			}
			
			impressaoDTO.setNomeFavorecido(distribuidor.getJuridica().getRazaoSocial());
			impressaoDTO.setNumeroCota(cota.getNumeroCota());
			
			impressaoDTO.setBox((cota.getBox()!= null)?cota.getBox().getCodigo() + " - " + cota.getBox().getNome():"");
			
			Roteirizacao roteirizacao = roteirizacaoService.buscarRoteirizacaoDeCota(cota.getNumeroCota());
			
			if(roteirizacao!= null){
				
				impressaoDTO.setRota( (roteirizacao.getRota()!= null)? roteirizacao.getRota().getCodigoRota():"");
				impressaoDTO.setRoteiro( (roteirizacao.getRota().getRoteiro()!= null)? roteirizacao.getRota().getRoteiro().getDescricaoRoteiro():"");
		
			}
			else{
		
				impressaoDTO.setRota("");
				impressaoDTO.setRoteiro("");
			}
		}
		
		impressaoDTO.setTipoCobranca(cobranca.getTipoCobranca());
		impressaoDTO.setValor(cobranca.getValor());
		impressaoDTO.setVencimento(cobranca.getDataVencimento());
		
		return impressaoDTO;
	}
	
}

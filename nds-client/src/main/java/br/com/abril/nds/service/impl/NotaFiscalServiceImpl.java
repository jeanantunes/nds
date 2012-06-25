package br.com.abril.nds.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.ConsultaLoteNotaFiscalDTO;
import br.com.abril.nds.dto.RetornoNFEDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoCota;
import br.com.abril.nds.model.cadastro.EnderecoDistribuidor;
import br.com.abril.nds.model.cadastro.ParametroSistema;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TelefoneCota;
import br.com.abril.nds.model.cadastro.TelefoneDistribuidor;
import br.com.abril.nds.model.cadastro.TipoParametroSistema;
import br.com.abril.nds.model.fiscal.nota.IdentificacaoDestinatario;
import br.com.abril.nds.model.fiscal.nota.IdentificacaoEmitente;
import br.com.abril.nds.model.fiscal.nota.IdentificacaoEmitente.RegimeTributario;
import br.com.abril.nds.model.fiscal.nota.InformacaoEletronica;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.fiscal.nota.RetornoComunicacaoEletronica;
import br.com.abril.nds.model.fiscal.nota.Status;
import br.com.abril.nds.model.fiscal.nota.StatusProcessamentoInterno;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.NotaFiscalRepository;
import br.com.abril.nds.repository.TelefoneCotaRepository;
import br.com.abril.nds.service.NotaFiscalService;
import br.com.abril.nds.service.ParametroSistemaService;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.export.fiscal.nota.NFEExporter;


/**
 * Classe de implementação de serviços referentes a entidade
 * {@link br.com.abril.nds.model.fiscal.nota.NotaFiscal}  
 * 
 * @author Discover Technology
 *
 */
@Service
public class NotaFiscalServiceImpl implements NotaFiscalService {
	
	@Autowired
	private NotaFiscalRepository notaFiscalDAO;
	
	@Autowired
	private ParametroSistemaService parametroSistemaService;
	
	@Autowired
	private CotaRepository cotaRepository;
	
	@Autowired
	private TelefoneCotaRepository telefoneCotaRepository;
	
	@Autowired
	private DistribuidorRepository distribuidorRepository;
	
	@Override
	public Map<Long, Integer> obterTotalItensNotaFiscalPorCotaEmLote(
			ConsultaLoteNotaFiscalDTO dadosConsultaLoteNotaFiscal) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NotaFiscal> gerarDadosNotaFicalEmLote(
			ConsultaLoteNotaFiscalDTO dadosConsultaLoteNotaFiscal) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.NotaFiscalService#processarRetornoNotaFiscal(br.com.abril.nds.dto.RetornoNFEDTO)
	 */
	@Override
	@Transactional
	public List<RetornoNFEDTO> processarRetornoNotaFiscal(List<RetornoNFEDTO> listaDadosRetornoNFE) {

		List<RetornoNFEDTO> listaDadosRetornoNFEProcessados = new ArrayList<RetornoNFEDTO>();

		for (RetornoNFEDTO dadosRetornoNFE : listaDadosRetornoNFE) {

			NotaFiscal notaFiscal = this.notaFiscalDAO.buscarPorId(dadosRetornoNFE.getIdNotaFiscal());

			if (notaFiscal != null) {

				IdentificacaoEmitente emitente = notaFiscal.getIdentificacaoEmitente();

				String cpfCnpjEmitente = emitente.getPessoaEmitenteReferencia().getDocumento();

				InformacaoEletronica informacaoEletronica = notaFiscal.getInformacaoEletronica();

				if (cpfCnpjEmitente.equals(dadosRetornoNFE.getCpfCnpj())) {

					if (StatusProcessamentoInterno.ENVIADA.equals(notaFiscal.getStatusProcessamentoInterno())) {

						if (Status.AUTORIZADO.equals(dadosRetornoNFE.getStatus()) || 
								Status.USO_DENEGADO.equals(dadosRetornoNFE.getStatus())) {

							listaDadosRetornoNFEProcessados.add(dadosRetornoNFE);
						}

					} else if (StatusProcessamentoInterno.RETORNADA.equals(notaFiscal.getStatusProcessamentoInterno())) {

						if (Status.AUTORIZADO.equals(informacaoEletronica.getRetornoComunicacaoEletronica().getStatus()) && 
								Status.CANCELAMENTO_HOMOLOGADO.equals(dadosRetornoNFE.getStatus())) {

							listaDadosRetornoNFEProcessados.add(dadosRetornoNFE);
						}
					}
				}
			}
		}
		
		return listaDadosRetornoNFEProcessados;
	}

	@Override
	public void cancelarNotaFiscal(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void denegarNotaFiscal(Long id) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.NotaFiscalService#autorizarNotaFiscal(br.com.abril.nds.dto.RetornoNFEDTO)
	 */
	@Override
	@Transactional
	public void autorizarNotaFiscal(RetornoNFEDTO dadosRetornoNFE) {
		
		NotaFiscal notaFiscal = this.notaFiscalDAO.buscarPorId(dadosRetornoNFE.getIdNotaFiscal());
		
		InformacaoEletronica informacaoEletronica = notaFiscal.getInformacaoEletronica();
		
		informacaoEletronica.setChaveAcesso(dadosRetornoNFE.getChaveAcesso());

		RetornoComunicacaoEletronica retornoComunicacaoEletronica = new RetornoComunicacaoEletronica();
		retornoComunicacaoEletronica.setDataRecebimento(dadosRetornoNFE.getDataRecebimento());
		retornoComunicacaoEletronica.setMotivo(dadosRetornoNFE.getMotivo());
		retornoComunicacaoEletronica.setProtocolo(dadosRetornoNFE.getProtocolo());
		retornoComunicacaoEletronica.setStatus(dadosRetornoNFE.getStatus());

		informacaoEletronica.setRetornoComunicacaoEletronica(retornoComunicacaoEletronica);

		notaFiscal.setInformacaoEletronica(informacaoEletronica);
		notaFiscal.setStatusProcessamentoInterno(StatusProcessamentoInterno.RETORNADA);

		this.notaFiscalDAO.merge(notaFiscal);
		
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.NotaFiscalService#enviarNotaFiscal(java.lang.Long)
	 */
	@Override
	@Transactional
	public void enviarNotaFiscal(Long id) {
		
		NotaFiscal notaFiscal = this.notaFiscalDAO.buscarPorId(id);
		
		notaFiscal.setStatusProcessamentoInterno(StatusProcessamentoInterno.ENVIADA);
		
		this.notaFiscalDAO.merge(notaFiscal);
	}
	
	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.NotaFiscalService#exportarNotasFiscais()
	 */
	@Override
	@Transactional
	public synchronized void exportarNotasFiscais() throws FileNotFoundException, IOException {
		
		List<NotaFiscal> notasFiscaisParaExportacao =
				this.notaFiscalDAO.obterListaNotasFiscaisPor(StatusProcessamentoInterno.GERADA);
		
		String dados = "";
		
		try {
		
			dados = gerarArquivoNota(notasFiscaisParaExportacao);
		
		} catch (Exception e) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Falha ao gerar arquivo de exportação"));
		}		
		
		ParametroSistema pathNFEExportacao =
				this.parametroSistemaService.buscarParametroPorTipoParametro(
							TipoParametroSistema.PATH_INTERFACE_NFE_EXPORTACAO);
		
		File diretorioExportacaoNFE = new File(pathNFEExportacao.getValor());
		
		
		if (!diretorioExportacaoNFE.isDirectory()) {
			throw new FileNotFoundException("O diretório de exportação parametrizado não é válido!");
		}
		
		File notaExportacao = 
				new File(diretorioExportacaoNFE + File.pathSeparator + new File("NFeExportacao"));
		
		FileWriter fileWriter;
			
		fileWriter = new FileWriter(notaExportacao);
		
		BufferedWriter buffer = new BufferedWriter(fileWriter);
			
		buffer.write(dados);
		
		for (NotaFiscal notaFiscal : notasFiscaisParaExportacao) {
			this.enviarNotaFiscal(notaFiscal.getId());
		}
	}
	
	
	private String gerarArquivoNota(List<NotaFiscal> notasFiscaisParaExportacao) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
	
		StringBuffer sBuffer = new StringBuffer();
	
		NFEExporter nfeExporter = new NFEExporter();
	
		for(NotaFiscal notaFiscal : notasFiscaisParaExportacao) {
			nfeExporter.clear();
			nfeExporter.execute(notaFiscal);
			sBuffer.append(nfeExporter.toString());
		}
		
		return sBuffer.toString();
	}
	
	private IdentificacaoEmitente carregaEmitente(){		
		IdentificacaoEmitente identificacaoEmitente = new IdentificacaoEmitente();
		
		Distribuidor distribuidor = distribuidorRepository.obter();
		if(distribuidor == null){
			throw new ValidacaoException(TipoMensagem.ERROR, "Endereço principal do distribuidor não encontrada!");
		}
		identificacaoEmitente.setDocumento(distribuidor.getJuridica().getCnpj());
		identificacaoEmitente.setInscricaoEstual(distribuidor.getJuridica().getInscricaoEstadual());
		identificacaoEmitente.setInscricaoMunicipal(distribuidor.getJuridica().getInscricaoMunicipal());
		identificacaoEmitente.setNome(distribuidor.getJuridica().getNome());
		identificacaoEmitente.setNomeFantasia(distribuidor.getJuridica().getNomeFantasia());
		
		EnderecoDistribuidor enderecoDistribuidor =   distribuidorRepository.obterEnderecoPrincipal();

		if(enderecoDistribuidor == null){
			throw new ValidacaoException(TipoMensagem.ERROR, "Endereço principal do distribuidor não encontrada!");
		}
		
		Endereco endereco = enderecoDistribuidor.getEndereco();
		endereco.setId(null);
		endereco.setPessoa(null);
		identificacaoEmitente.setEndereco(endereco);		
		
		TelefoneDistribuidor telefoneDistribuidor = distribuidorRepository.obterTelefonePrincipal();
		
		if(telefoneDistribuidor == null){
			throw new ValidacaoException(TipoMensagem.ERROR, "Telefone principal do distribuidor não encontrada!");
		}
		Telefone telefone = telefoneDistribuidor.getTelefone();
		telefone.setId(null);
		telefone.setPessoa(null);
		identificacaoEmitente.setTelefone(telefone);
		identificacaoEmitente.setRegimeTributario(RegimeTributario.SIMPLES_NACIONAL);
		
		return identificacaoEmitente;		
	}
	
	
	private IdentificacaoDestinatario carregaDestinatario(Long idCota){
		IdentificacaoDestinatario destinatario = new IdentificacaoDestinatario();
		Cota cota = cotaRepository.buscarPorId(idCota);		
		if(cota == null){
			throw new ValidacaoException(TipoMensagem.ERROR, "Cota " + idCota + " não encontrada!");
		}
		
		destinatario.setDocumento(cota.getPessoa().getDocumento());
		destinatario.setEmail(cota.getPessoa().getEmail());
		
		EnderecoCota  enderecoCota = cotaRepository.obterEnderecoPrincipal(idCota);
		
		if(enderecoCota == null){
			throw new ValidacaoException(TipoMensagem.ERROR, "Endereço principal da cota " + idCota + " não encontrada!");
		}
		Endereco endereco = enderecoCota.getEndereco();
		endereco.setId(null);
		endereco.setPessoa(null);
		
		destinatario.setEndereco(endereco);
		
		
		if(cota.getPessoa() instanceof PessoaJuridica){
			PessoaJuridica pessoaJuridica = (PessoaJuridica) cota.getPessoa();
			destinatario.setInscricaoEstual(pessoaJuridica.getInscricaoEstadual());
			destinatario.setNomeFantasia(pessoaJuridica.getNomeFantasia());
		}
		destinatario.setNome(cota.getPessoa().getNome());
		destinatario.setPessoaDestinatarioReferencia(cota.getPessoa());
		
		
		TelefoneCota telefoneCota = telefoneCotaRepository.obterTelefonePrincipal(idCota);
		if(telefoneCota == null){
			throw new ValidacaoException(TipoMensagem.ERROR, "Telefone principal da cota " + idCota + " não encontrada!");
		}
		Telefone telefone = telefoneCota.getTelefone();
		telefone.setId(null);
		telefone.setPessoa(null);
		destinatario.setTelefone(telefone);		
		
		return destinatario;
	}
	
}

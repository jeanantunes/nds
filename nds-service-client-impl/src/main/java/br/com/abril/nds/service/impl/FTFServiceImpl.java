package br.com.abril.nds.service.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import v1.pessoadetalhe.ebo.abril.types.PessoaDto;
import v1.pessoadetalhe.ebo.abril.types.PessoaType;
import br.com.abril.nds.dto.FTFReportDTO;
import br.com.abril.nds.dto.RetornoNFEDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.enums.TipoParametroSistema;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.ftfutil.FTFBaseDTO;
import br.com.abril.nds.ftfutil.FTFParser;
import br.com.abril.nds.model.fiscal.nota.InformacaoEletronica;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.fiscal.nota.StatusProcessamento;
import br.com.abril.nds.model.fiscal.nota.StatusRetornado;
import br.com.abril.nds.model.ftf.envio.FTFEnvTipoRegistro00;
import br.com.abril.nds.model.ftf.envio.FTFEnvTipoRegistro01;
import br.com.abril.nds.model.ftf.envio.FTFEnvTipoRegistro02;
import br.com.abril.nds.model.ftf.envio.FTFEnvTipoRegistro03;
import br.com.abril.nds.model.ftf.envio.FTFEnvTipoRegistro05;
import br.com.abril.nds.model.ftf.envio.FTFEnvTipoRegistro06;
import br.com.abril.nds.model.ftf.envio.FTFEnvTipoRegistro08;
import br.com.abril.nds.model.ftf.envio.FTFEnvTipoRegistro09;
import br.com.abril.nds.model.ftf.retorno.FTFRetTipoRegistro00;
import br.com.abril.nds.model.ftf.retorno.FTFRetTipoRegistro01;
import br.com.abril.nds.model.ftf.retorno.FTFRetTipoRegistro09;
import br.com.abril.nds.model.ftf.retorno.FTFRetornoRET;
import br.com.abril.nds.model.integracao.ParametroSistema;
import br.com.abril.nds.repository.FTFRepository;
import br.com.abril.nds.repository.NotaFiscalRepository;
import br.com.abril.nds.service.FTFService;
import br.com.abril.nds.service.NotaFiscalService;
import br.com.abril.nds.service.PessoaCRPWSService;
import br.com.abril.nds.service.integracao.ParametroSistemaService;
import br.com.abril.nds.util.StringUtil;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.ItemEncalheBandeiraVO;
import br.com.abril.nds.vo.NotaEncalheBandeiraVO;

@Service
public class FTFServiceImpl implements FTFService {

	private static final Logger LOGGER = LoggerFactory.getLogger(FTFServiceImpl.class);

	@Autowired
	private FTFRepository ftfRepository;

	@Autowired
	private NotaFiscalRepository notaFiscalRepository;

	@Autowired
	private PessoaCRPWSService pessoaCRPService;

	@Autowired
	private ParametroSistemaService parametroSistemaService;

	@Autowired
	private NotaFiscalService notaFiscalService;
	
	private static String PERCENTUAL_RATEIO = "10000";
	
	@Override
	@Transactional
	public FTFReportDTO gerarFtf(final List<NotaFiscal> notas) {

		if(notas == null || notas.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhuma nota localizada.");
		}

		long idNaturezaOperacao = verificarNaturezaOperacao(notas);

		List<FTFBaseDTO> list = new ArrayList<FTFBaseDTO>();
		FTFReportDTO report = new FTFReportDTO();

		List<String> validacaoBeans = new ArrayList<String>();

		FTFEnvTipoRegistro00 regTipo00 = ftfRepository.obterRegistroTipo00(idNaturezaOperacao);

		FTFEnvTipoRegistro09 regTipo09 = ftfRepository.obterRegistroTipo09(idNaturezaOperacao);

		list.add(regTipo00);

		List<FTFEnvTipoRegistro01> listTipoRegistro01 = this.carregarRegistro01(notas, idNaturezaOperacao, validacaoBeans, regTipo00, regTipo09);

		List<FTFEnvTipoRegistro01> listTipoRegistro01Cadastrados = this.obterPessoasCadastradasCRP(report, listTipoRegistro01);

		for (FTFEnvTipoRegistro01 ftfEnvTipoRegistro01 : listTipoRegistro01Cadastrados) {

			long idNF = Long.parseLong(ftfEnvTipoRegistro01.getNumeroDocOrigem());

			List<FTFEnvTipoRegistro02> obterResgistroTipo02 = this.carregarRegistroTipo02(idNaturezaOperacao, validacaoBeans, idNF);			
			ftfEnvTipoRegistro01.setItemNFList(obterResgistroTipo02);

			if("F".equals(ftfEnvTipoRegistro01.getTipoPessoaDestinatario())) {

				FTFEnvTipoRegistro08 registroTipo08 = carregarRegitrosTipo08(validacaoBeans, idNF);
				ftfEnvTipoRegistro01.setRegTipo08(registroTipo08);
			}

			if(ftfRepository.verificarRegistroVenda(idNaturezaOperacao)){				
				List<FTFEnvTipoRegistro03> regTipo03 = carregarRegitrosTipo03(obterResgistroTipo02, validacaoBeans);
				ftfEnvTipoRegistro01.setItemNFList03(regTipo03);
			}

			if(ftfRepository.verificarRegistroVenda(idNaturezaOperacao)){
				this.carregarRegistroTipo05(validacaoBeans, ftfEnvTipoRegistro01, idNF);
			}
			
			this.carregarRegistroTipo06(validacaoBeans, ftfEnvTipoRegistro01, idNF);
		}
		
		
		this.setarRegistrosTipo06(list, listTipoRegistro01Cadastrados);

		this.logarErrosEncontrados(validacaoBeans);

		String totalPedidos = Integer.toString(listTipoRegistro01Cadastrados.size());
		String totalRegistros = Integer.toString(list.size());

		regTipo00.setQtdePedidos(totalPedidos);
		regTipo00.setQtdeRegistros(totalRegistros);

		regTipo09.setQtdePedidos(totalPedidos);
		regTipo09.setQtdeRegistros(totalRegistros);
		list.add(regTipo09);

		return gerarArquivoFTF(notas, list, report, regTipo00, totalPedidos);
	}

	private List<FTFEnvTipoRegistro03> carregarRegitrosTipo03(List<FTFEnvTipoRegistro02> obterResgistroTipo02, List<String> validacaoBeans) {

		List<FTFEnvTipoRegistro03> listaEnvTipoRegistro03 = new ArrayList<>();

		for(FTFEnvTipoRegistro02 ftfTipoRegistro02 : obterResgistroTipo02){
			FTFEnvTipoRegistro03 ftfEnvTipoRegistro03 = new FTFEnvTipoRegistro03();

			ftfEnvTipoRegistro03.setTipoRegistro("3");
			ftfEnvTipoRegistro03.setCodigoEstabelecimentoEmissor(ftfTipoRegistro02.getCodigoEstabelecimentoEmissor());
			ftfEnvTipoRegistro03.setCnpjEmpresaEmissora(ftfTipoRegistro02.getCnpjEmpresaEmissora());
			ftfEnvTipoRegistro03.setCodLocal(ftfTipoRegistro02.getCodLocal());
			ftfEnvTipoRegistro03.setNumeroDocOrigem(ftfTipoRegistro02.getNumeroDocOrigem());
			ftfEnvTipoRegistro03.setNumItemPedido(ftfTipoRegistro02.getNumItemPedido());
			ftfEnvTipoRegistro03.setTipoPedido(ftfTipoRegistro02.getTipoPedido());
			ftfEnvTipoRegistro03.setCodSetorialCRP(ftfTipoRegistro02.getCentroLucroCorporativo());
			ftfEnvTipoRegistro03.setPercentualRateio(PERCENTUAL_RATEIO);
			listaEnvTipoRegistro03.add(ftfEnvTipoRegistro03);

		}

		return listaEnvTipoRegistro03;
	}
	
	private void carregarRegistroTipo05(List<String> validacaoBeans, FTFEnvTipoRegistro01 ftfEnvTipoRegistro01, long idNF) {
		FTFEnvTipoRegistro05 regTipo05 = ftfRepository.obterRegistroTipo05(idNF);
		ftfEnvTipoRegistro01.setRegTipo05(regTipo05);
	}
	
	private void setarRegistrosTipo06(List<FTFBaseDTO> list, List<FTFEnvTipoRegistro01> listTipoRegistro01Cadastrados) {

		for (FTFEnvTipoRegistro01 ftfEnvTipoRegistro01 : listTipoRegistro01Cadastrados) {

			list.add(ftfEnvTipoRegistro01);
			list.addAll(ftfEnvTipoRegistro01.getItemNFList());

			if(ftfEnvTipoRegistro01.getItemNFList03() != null){				
				list.addAll(ftfEnvTipoRegistro01.getItemNFList03());
			}

			if(ftfEnvTipoRegistro01.getRegTipo06() != null){
				list.add(ftfEnvTipoRegistro01.getRegTipo06());				
			}

			if(ftfEnvTipoRegistro01.getRegTipo08() != null){
				list.add(ftfEnvTipoRegistro01.getRegTipo08());				
			}
			
			if(ftfEnvTipoRegistro01.getRegTipo05() != null){
				list.add(ftfEnvTipoRegistro01.getRegTipo05());				
			}
		}
	}

	private void logarErrosEncontrados(List<String> validacaoBeans) {
		if(validacaoBeans.size() > 0) {
			//throw new ValidacaoException(TipoMensagem.ERROR, validacaoBeans);
			for(String err : validacaoBeans) {
				LOGGER.error(err);
			}

		}
	}

	private void carregarRegistroTipo06(List<String> validacaoBeans, FTFEnvTipoRegistro01 ftfEnvTipoRegistro01, long idNF) {
		FTFEnvTipoRegistro06 regTipo06 = ftfRepository.obterRegistroTipo06(idNF);
		if(regTipo06 != null) {
			ftfEnvTipoRegistro01.setRegTipo06(regTipo06);
			validacaoBeans.addAll(regTipo06.validateBean());
		}
	}

	private List<FTFEnvTipoRegistro02> carregarRegistroTipo02(long idNaturezaOperacao, List<String> validacaoBeans, long idNF) {
		List<FTFEnvTipoRegistro02> obterResgistroTipo02 = ftfRepository.obterResgistroTipo02(idNF, idNaturezaOperacao);
		for(FTFEnvTipoRegistro02 ftfetr02 : obterResgistroTipo02) {
			validacaoBeans.addAll(ftfetr02.validateBean());
		}
		return obterResgistroTipo02;
	}

	private List<FTFEnvTipoRegistro01> carregarRegistro01(final List<NotaFiscal> notas, 
			final long idNaturezaOperacao, List<String> validacaoBeans,
			final FTFEnvTipoRegistro00 regTipo00, 
			final FTFEnvTipoRegistro09 regTipo09) {

		List<FTFEnvTipoRegistro01> listTipoRegistro01 = ftfRepository.obterResgistroTipo01(notas, idNaturezaOperacao);

		validacaoBeans.addAll(regTipo00.validateBean());
		validacaoBeans.addAll(regTipo09.validateBean());

		for(FTFEnvTipoRegistro01 ftfetr01 : listTipoRegistro01) {
			validacaoBeans.addAll(ftfetr01.validateBean());
		}

		return listTipoRegistro01;
	}

	private FTFReportDTO gerarArquivoFTF(final List<NotaFiscal> notas, List<FTFBaseDTO> list, FTFReportDTO report, FTFEnvTipoRegistro00 regTipo00, String totalPedidos) {

		ParametroSistema pathNFEExportacao = this.parametroSistemaService.buscarParametroPorTipoParametro(TipoParametroSistema.PATH_INTERFACE_NFE_EXPORTACAO_FTF);

		File diretorioExportacaoNFE = new File(pathNFEExportacao.getValor());

		File f = new File(String.format("%s/%s", pathNFEExportacao.getValor(), regTipo00.getNomeArquivo()));

		BufferedWriter bw = null;
		try {

			if (!diretorioExportacaoNFE.isDirectory()) {
				throw new FileNotFoundException("O diretório["+ pathNFEExportacao.getValor() +"] de exportação parametrizado não é válido!");
			}

			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), "ASCII"));
			FTFParser ftfParser = new FTFParser();

			for (FTFBaseDTO dto : list) {
				ftfParser.parseFTF(dto, bw);
				bw.newLine();
			}

			bw.flush();
			bw.close();

		} catch (FileNotFoundException e) {
			LOGGER.error("Não foi possivel localizar o arquivo no diretorio especifico!", e);
			throw new ValidacaoException(TipoMensagem.ERROR, "Não foi possivel localizar o arquivo no diretorio especifico!");
		} catch (IOException e) {
			LOGGER.error("Erro ao acessar arquivo!", e);
			throw new ValidacaoException(TipoMensagem.ERROR, "Erro ao acessar arquivo!");
		} catch (Exception e) {
			LOGGER.error("Erro ao gerar o arquivo!", e);
		}

		report.setPedidosGerados(Integer.parseInt(totalPedidos));

		this.salvarNotas(notas);

		return report;
	}

	private void salvarNotas(final List<NotaFiscal> notas) {
		for (NotaFiscal notaFiscal : notas) {
			notaFiscalService.enviarNotaFiscal(notaFiscal.getId());
		}
	}

	private FTFEnvTipoRegistro08 carregarRegitrosTipo08(List<String> validacaoBeans, long idNF) {

		FTFEnvTipoRegistro08 regTipo08 = ftfRepository.obterRegistroTipo08(idNF);
		validacaoBeans.addAll(regTipo08.validateBean());
		return regTipo08;
	}

	private long verificarNaturezaOperacao(final List<NotaFiscal> notas) {
		long idNaturezaOperacao = 0;
		for(NotaFiscal nf : notas) {

			if(idNaturezaOperacao == 0) {
				idNaturezaOperacao = nf.getNotaFiscalInformacoes().getIdentificacao().getNaturezaOperacao().getId();
			} else {
				if(idNaturezaOperacao != nf.getNotaFiscalInformacoes().getIdentificacao().getNaturezaOperacao().getId()) {
					throw new ValidacaoException(TipoMensagem.WARNING, "Lista de Notas fiscais com Naturezas de Operações diferentes.");
				}
			}
			idNaturezaOperacao = nf.getNotaFiscalInformacoes().getIdentificacao().getNaturezaOperacao().getId();
		}
		return idNaturezaOperacao;
	}

	private List<FTFEnvTipoRegistro01> obterPessoasCadastradasCRP(FTFReportDTO report, List<FTFEnvTipoRegistro01> listTipoRegistro01) {
		List<FTFEnvTipoRegistro01> listTipoRegistro01Cadastrados = new ArrayList<>();

		for (FTFEnvTipoRegistro01 ftfEnvTipoRegistro01 : listTipoRegistro01) {
			String cpfCnpjDestinatario = Util.removerMascaraCnpj(ftfEnvTipoRegistro01.getCpfCnpjDestinatario());

			if(!verificarPessoaWs(cpfCnpjDestinatario)){
				// report.getNaoCadastradosCRP().add(ftfEnvTipoRegistro01);
				listTipoRegistro01Cadastrados.add(ftfEnvTipoRegistro01);
			}else {
				listTipoRegistro01Cadastrados.add(ftfEnvTipoRegistro01);
			}
		}
		return listTipoRegistro01Cadastrados;
	}

	/**
	 * 
	 * @param type 1 CNPJ, 2 CPF
	 * @param cpfCnpj
	 */
	private boolean verificarPessoaWs(final String cpfCnpj) {
		boolean valid = false;
		//		String testedCpnj = "68252618000182";
		// validar as pessoas no CRP
		
		PessoaDto wsResponse = pessoaCRPService.obterDadosFiscais(getCodTipoDocFrom(cpfCnpj), cpfCnpj);
		PessoaType pessoaCRP = wsResponse.getPessoa();

		if (pessoaCRP != null && !StringUtil.isEmpty(pessoaCRP.getNome())) {
			valid = true;
		}

		return valid;
	}

	private int getCodTipoDocFrom(String cpfCnpj){
		return cpfCnpj.length() == 14 ? 1 : 2;
	}

	@SuppressWarnings("resource")
	public List<FTFRetornoRET> processarArquivosRet(final List<File> files){

		List<FTFRetornoRET> l = new ArrayList<FTFRetornoRET>();

		for (File file : files) {
			List<FTFBaseDTO> list = new ArrayList<FTFBaseDTO>();

			BufferedReader br;
			try {
				br = new BufferedReader(new FileReader(file));
				while(br.ready()){

					String line = br.readLine();
					FTFBaseDTO ftfdto = FTFParser.parseLinhaRetornoFTF(line);
					list.add(ftfdto);
				}

				FTFRetornoRET n = new FTFRetornoRET();
				for (FTFBaseDTO dto : list) {
					if(dto instanceof FTFRetTipoRegistro00){
						n.setTipo00((FTFRetTipoRegistro00)dto);
					}else if(dto instanceof FTFRetTipoRegistro01){
						n.getTipo01List().add((FTFRetTipoRegistro01)dto);
					}else if(dto instanceof FTFRetTipoRegistro09){
						n.setTipo09((FTFRetTipoRegistro09)dto);
					}
				}

				l.add(n);

			} catch (FileNotFoundException e) {
				LOGGER.error("Não foi possivel localizar o arquivo no diretorio especifico!", e);
				throw new ValidacaoException(TipoMensagem.ERROR, "Não foi possivel localizar o arquivo no diretorio especifico!");
			} catch (IOException e) {
				LOGGER.error("Erro ao acessar arquivo!", e);
				throw new ValidacaoException(TipoMensagem.ERROR, "Erro ao acessar arquivo!");
			} catch (Exception e) {
				LOGGER.error("Erro ao gerar o arquivo!", e);
				throw new ValidacaoException(TipoMensagem.ERROR, "Erro ao gerar o arquivo!");
			}

		}

		return l;
	}

	public void atualizarRetornoFTF(final List<FTFRetTipoRegistro01> list){

		this.ftfRepository.atualizarRetornoFTF(list);
	}
	
	public List<NotaEncalheBandeiraVO> obterNotasNaoEnviadas() {
		
		return this.ftfRepository.obterNotasNaoEnviadas();
		
	}
	
	public List<ItemEncalheBandeiraVO> obterItensNotasNaoEnviadas(Integer notaId) {
		
		return this.ftfRepository.obterItensNotasNaoEnviadas(  notaId);
		
	}
	
	public void atualizaFlagInterfaceNotasEnviadas(Integer notaId,boolean flag) {
		
		 this.ftfRepository.atualizaFlagInterfaceNotasEnviadas( notaId, flag);
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.com.abril.nds.service.NotaFiscalService#processarRetornoNotaFiscal
	 * (br.com.abril.nds.dto.RetornoNFEDTO)
	 */
	@Override
	@Transactional
	public List<RetornoNFEDTO> processarRetornoFTF(List<RetornoNFEDTO> listaDadosRetornoNFE) {

		List<RetornoNFEDTO> listaDadosRetornoNFEProcessados = new ArrayList<RetornoNFEDTO>();

		for (RetornoNFEDTO dadosRetornoNFE : listaDadosRetornoNFE) {

			if (dadosRetornoNFE.getNumeroNotaFiscal() != null || dadosRetornoNFE.getProtocolo() != null) {
				NotaFiscal notaFiscal = null;

				notaFiscal = this.notaFiscalRepository.buscarPorId(dadosRetornoNFE.getIdNota());

				if (notaFiscal != null) {
					InformacaoEletronica informacaoEletronica = notaFiscal.getNotaFiscalInformacoes().getInformacaoEletronica();


						if (StatusProcessamento.EM_PROCESSAMENTO.equals(notaFiscal.getNotaFiscalInformacoes().getStatusProcessamento())) {
							if (StatusRetornado.AUTORIZADO.equals(dadosRetornoNFE.getStatus()) 
									|| StatusRetornado.USO_DENEGADO.equals(dadosRetornoNFE.getStatus())
									|| StatusRetornado.CANCELAMENTO_HOMOLOGADO.equals(dadosRetornoNFE.getStatus())) {
								listaDadosRetornoNFEProcessados.add(dadosRetornoNFE);
							}

						} else if (StatusProcessamento.RETORNADA.equals(notaFiscal.getNotaFiscalInformacoes().getStatusProcessamento())) {

							if (StatusRetornado.AUTORIZADO.equals(informacaoEletronica.getRetornoComunicacaoEletronica().getStatusRetornado())
									|| StatusRetornado.CANCELAMENTO_HOMOLOGADO.equals(dadosRetornoNFE.getStatus())) {

								listaDadosRetornoNFEProcessados.add(dadosRetornoNFE);
							}
						} else if (StatusProcessamento.SOLICITACAO_CANCELAMENTO.equals(notaFiscal.getNotaFiscalInformacoes().getStatusProcessamento())) {
							listaDadosRetornoNFEProcessados.add(dadosRetornoNFE);
						} else {
							throw new ValidacaoException(TipoMensagem.ERROR, "A chave de acesso do arquivo não confere com a base de dados.");
						}
					}
				}
		}

		return listaDadosRetornoNFEProcessados;
	}
	
}
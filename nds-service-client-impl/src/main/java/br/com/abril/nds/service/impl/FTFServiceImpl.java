package br.com.abril.nds.service.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import v1.pessoadetalhe.ebo.abril.types.PessoaDto;
import v1.pessoadetalhe.ebo.abril.types.PessoaType;
import br.com.abril.nds.dto.FTFReportDTO;
import br.com.abril.nds.enums.TipoParametroSistema;
import br.com.abril.nds.ftfutil.FTFBaseDTO;
import br.com.abril.nds.ftfutil.FTFParser;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.ftf.envio.FTFEnvTipoRegistro00;
import br.com.abril.nds.model.ftf.envio.FTFEnvTipoRegistro01;
import br.com.abril.nds.model.ftf.envio.FTFEnvTipoRegistro02;
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

@Service
public class FTFServiceImpl implements FTFService {

	@Autowired
	private FTFRepository ftfRepository;
	
	@Autowired
	private NotaFiscalRepository fiscalRepository;
	
	@Autowired
	private PessoaCRPWSService pessoaCRPService;
	
	@Autowired
	private ParametroSistemaService parametroSistemaService;
	
	@Autowired
	private NotaFiscalService notaFiscalService;

	@Override
	@Transactional(readOnly = true)
	public FTFReportDTO gerarFtf(List<NotaFiscal> notas, long idNaturezaOperacao) {
		List<FTFBaseDTO> list = new ArrayList<FTFBaseDTO>();
		FTFReportDTO report = new FTFReportDTO();
		
		if (notas == null) {
			notas = fiscalRepository.buscarTodos();
		}
		
		FTFEnvTipoRegistro00 regTipo00 = ftfRepository.obterRegistroTipo00(idNaturezaOperacao);
		//FIXME: ajustar se estiver certo
		FTFEnvTipoRegistro08 regTipo08 = ftfRepository.obterRegistroTipo08(notas.get(0).getId());
		FTFEnvTipoRegistro09 regTipo09 = ftfRepository.obterRegistroTipo09(idNaturezaOperacao);
		
		list.add(regTipo00);

		List<FTFEnvTipoRegistro01> listTipoRegistro01 = ftfRepository.obterResgistroTipo01(notas, idNaturezaOperacao);
		List<FTFEnvTipoRegistro01> listTipoRegistro01Cadastrados = listTipoRegistro01;// = obterPessoasCadastradasCRP(report, listTipoRegistro01);
		
		for (FTFEnvTipoRegistro01 ftfEnvTipoRegistro01 : listTipoRegistro01Cadastrados) {
			long idNF = Long.parseLong(ftfEnvTipoRegistro01.getNumeroDocOrigem());
			
			List<FTFEnvTipoRegistro02> obterResgistroTipo02 = ftfRepository.obterResgistroTipo02(idNF, idNaturezaOperacao);
			ftfEnvTipoRegistro01.setItemNFList(obterResgistroTipo02);
			
			FTFEnvTipoRegistro06 regTipo06 = ftfRepository.obterRegistroTipo06(idNF);
			if(regTipo06 != null)
				ftfEnvTipoRegistro01.setRegTipo06(regTipo06);
		}
		
		for (FTFEnvTipoRegistro01 ftfEnvTipoRegistro01 : listTipoRegistro01Cadastrados) {
			list.add(ftfEnvTipoRegistro01);
			list.addAll(ftfEnvTipoRegistro01.getItemNFList());
			if(ftfEnvTipoRegistro01.getRegTipo06() != null)
				list.add(ftfEnvTipoRegistro01.getRegTipo06());
		}
		
		String totalPedidos = Integer.toString(listTipoRegistro01Cadastrados.size());
		String totalRegistros = Integer.toString(list.size());
		
		regTipo00.setQtdePedidos(totalPedidos);
		regTipo00.setQtdeRegistros(totalRegistros);

		list.add(regTipo08);
		
		regTipo09.setQtdePedidos(totalPedidos);
		regTipo09.setQtdeRegistros(totalRegistros);
		list.add(regTipo09);
		
		ParametroSistema pathNFEExportacao = this.parametroSistemaService.buscarParametroPorTipoParametro(TipoParametroSistema.PATH_INTERFACE_NFE_EXPORTACAO_FTF);

		File diretorioExportacaoNFE = new File(pathNFEExportacao.getValor());
		
		File f = new File(String.format("%s/%s",pathNFEExportacao.getValor(),regTipo00.getNomeArquivo()));
		
		BufferedWriter bw = null;
		try {
			
			if (!diretorioExportacaoNFE.isDirectory()) {
				throw new FileNotFoundException(
						"O diretório["+ pathNFEExportacao.getValor() +"] de exportação parametrizado não é válido!");
			}
			
			bw = new BufferedWriter(new FileWriter(f));
			FTFParser ftfParser = new FTFParser();
			
			for (FTFBaseDTO dto : list) {
				ftfParser.parseFTF(dto, bw);
				bw.newLine();
			}
			
			bw.flush();
			bw.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		report.setPedidosGerados(Integer.parseInt(totalPedidos));
		
		for (NotaFiscal notaFiscal : notas) {
			notaFiscalService.enviarNotaFiscal(notaFiscal.getId());
		}
		
		return report;
	}

	private List<FTFEnvTipoRegistro01> obterPessoasCadastradasCRP(
			FTFReportDTO report, List<FTFEnvTipoRegistro01> listTipoRegistro01) {
		List<FTFEnvTipoRegistro01> listTipoRegistro01Cadastrados = new ArrayList<>();
		
		for (FTFEnvTipoRegistro01 ftfEnvTipoRegistro01 : listTipoRegistro01) {
			String cpfCnpjDestinatario = ftfEnvTipoRegistro01.getCpfCnpjDestinatario();
			
			if(!verificarPessoaWs(cpfCnpjDestinatario)){
				report.getNaoCadastradosCRP().add(ftfEnvTipoRegistro01);
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
	private boolean verificarPessoaWs(String cpfCnpj) {
		boolean valid = false;
//		String testedCpnj = "68252618000182";
		// validar as pessoas no CRP
		PessoaDto wsResponse = pessoaCRPService.obterDadosFiscais("NDS", getCodTipoDocFrom(cpfCnpj), cpfCnpj);
		PessoaType pessoaCRP = wsResponse.getPessoa();
		
		if (pessoaCRP != null && !StringUtil.isEmpty(pessoaCRP.getNome())) {
			valid = true;
		}
		
		return valid;
	}
	
	private int getCodTipoDocFrom(String cpfCnpj){
		return cpfCnpj.length() == 14 ? 1 : 2;
	}
	
	private String decodeCodTipoDocFrom(int type){
		return type == 1 ? "CNPJ" : "CPF";
	}
	
	
	public List<FTFRetornoRET> processarArquivosRet(File...files){
		
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
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		
		
		return l;
	}

	
	
	public void atualizarRetornoFTF(List<FTFRetTipoRegistro01> list){
		
		this.ftfRepository.atualizarRetornoFTF(list);
		
	}
	
}

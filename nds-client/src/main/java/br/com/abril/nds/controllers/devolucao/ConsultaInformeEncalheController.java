package br.com.abril.nds.controllers.devolucao;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.lightcouch.NoDocumentException;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.InformeEncalheDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.TipoImpressaoInformeEncalheDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.CapaService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.LancamentoService;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.interceptor.download.ByteArrayDownload;
import br.com.caelum.vraptor.interceptor.download.Download;

/**
 * 
 * Classe responsável por controlas as ações da pagina de Informe de
 * Recolhimentos.
 * 
 * @author Discover Technology
 * 
 */
@Resource
@Path(value = "/devolucao/informeEncalhe")
public class ConsultaInformeEncalheController {

	@Autowired
	private Result result;

	@Autowired
	private LancamentoService lancamentoService;

	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired 
	private CapaService capaService;

	@Get("/")
	public void index() {
		result.include("fornecedores", fornecedorService
				.obterFornecedoresIdNome(SituacaoCadastro.ATIVO, true));
	}

	@Post("/busca.json")
	public void busca(Long idFornecedor, Integer semanaRecolhimento,
			Calendar dataRecolhimento, String sortname, String sortorder,
			int rp, int page) {
		Calendar dataInicioRecolhimento = null, dataFimRecolhimento = null;

		if ((semanaRecolhimento == null) ^ (dataRecolhimento == null)) {
			if (semanaRecolhimento != null) {
				dataInicioRecolhimento = Calendar.getInstance();

				if (semanaRecolhimento > dataInicioRecolhimento
						.getMaximum(Calendar.WEEK_OF_YEAR)) {
					throw new ValidacaoException(new ValidacaoVO(
							TipoMensagem.ERROR, "Semana inválida."));
				}

				dataInicioRecolhimento.set(Calendar.WEEK_OF_YEAR,
						semanaRecolhimento);
				dataInicioRecolhimento.add(Calendar.DAY_OF_MONTH, -1);
				dataFimRecolhimento = (Calendar) dataInicioRecolhimento.clone();
				dataFimRecolhimento.add(Calendar.DAY_OF_MONTH, 7);

			} else if (dataRecolhimento != null) {
				dataInicioRecolhimento = dataRecolhimento;
				dataFimRecolhimento = dataRecolhimento;
			}
		} else {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR,
					"Informe [Semana] ou [Data Recolhimento]"));
		}
		Long quantidade = lancamentoService
				.quantidadeLancamentoInformeRecolhimento(idFornecedor,
						dataInicioRecolhimento, dataFimRecolhimento);
		List<InformeEncalheDTO> informeEncalheDTOs = lancamentoService
				.obterLancamentoInformeRecolhimento(idFornecedor,
						dataInicioRecolhimento, dataFimRecolhimento, sortname,
						Ordenacao.valueOf(sortorder.toUpperCase()), page * rp
								- rp, rp);

		result.use(FlexiGridJson.class).from(informeEncalheDTOs)
				.total(quantidade.intValue()).page(page).serialize();
	}

	@Post("/imprimir")
	public Download imprimir(Long idFornecedor, Integer semanaRecolhimento,
			Calendar dataRecolhimento,
			TipoImpressaoInformeEncalheDTO tipoImpressao, String sortname,
			String sortorder) throws IOException {
		Calendar dataInicioRecolhimento = null, dataFimRecolhimento = null;

		if ((semanaRecolhimento == null) ^ (dataRecolhimento == null)) {
			if (semanaRecolhimento != null) {
				dataInicioRecolhimento = Calendar.getInstance();

				if (semanaRecolhimento > dataInicioRecolhimento
						.getMaximum(Calendar.WEEK_OF_YEAR)) {
					throw new ValidacaoException(new ValidacaoVO(
							TipoMensagem.ERROR, "Semana inválida."));
				}

				dataInicioRecolhimento.set(Calendar.WEEK_OF_YEAR,
						semanaRecolhimento);
				dataInicioRecolhimento.add(Calendar.DAY_OF_MONTH, -1);
				dataFimRecolhimento = (Calendar) dataInicioRecolhimento.clone();
				dataFimRecolhimento.add(Calendar.DAY_OF_MONTH, 7);

			} else if (dataRecolhimento != null) {
				dataInicioRecolhimento = dataRecolhimento;
				dataFimRecolhimento = dataRecolhimento;
			}
		} else {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR,
					"Informe [Semana] ou [Data Recolhimento]"));
		}

		List<InformeEncalheDTO> informeEncalheDTOs = lancamentoService
				.obterLancamentoInformeRecolhimento(idFornecedor,
						dataInicioRecolhimento, dataFimRecolhimento, sortname,
						Ordenacao.valueOf(sortorder.toUpperCase()), null, null);

		List<List<ItemDTO<Integer, byte[]>>> capas = preparaDadosImpressao(informeEncalheDTOs);
		
		//TODO: Chamar relatorio;
		return new ByteArrayDownload(new byte[0], "application/pdf", "relatorio.pdf", true);

	}

	/**
	 * Prepara lista de capas com sequencia na lista de InformeEncalheDTO
	 * @param informeEncalheDTOs
	 * @throws IOException
	 */
	private List<List<ItemDTO<Integer, byte[]>>> preparaDadosImpressao(
			List<InformeEncalheDTO> informeEncalheDTOs) throws IOException {
		List<List<ItemDTO<Integer, byte[]>>> capas = new ArrayList<List<ItemDTO<Integer, byte[]>>>();
		int quantidadeLinha = 5;
		List<ItemDTO<Integer, byte[]>> linha = null;
		int seqCapa = 0;
		for (InformeEncalheDTO informeEncalheDTO : informeEncalheDTOs) {			
			if(linha == null  ||  linha.size() == quantidadeLinha){
				linha = new ArrayList<ItemDTO<Integer, byte[]>>(quantidadeLinha);
				capas.add(linha);
			}
			
			try {
				InputStream inputStream = capaService.getCapaInputStream(
						informeEncalheDTO.getCodigoProduto(),
						informeEncalheDTO.getNumeroEdicao());
				byte[] foto = IOUtils.toByteArray(inputStream);
				
				seqCapa++;
				linha.add(new ItemDTO<Integer, byte[]>(seqCapa, foto));
				informeEncalheDTO.setSeqCapa(seqCapa);
			} catch (NoDocumentException e) {
				continue;
			}
			
			
		}
		
		return capas;
	}

}

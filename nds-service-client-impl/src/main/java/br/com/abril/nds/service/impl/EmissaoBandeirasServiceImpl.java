package br.com.abril.nds.service.impl;

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.ImpressaoBandeiraVO;
import br.com.abril.nds.dto.BandeirasDTO;
import br.com.abril.nds.dto.FornecedorDTO;
import br.com.abril.nds.dto.filtro.FiltroImpressaoNFEDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.TipoAtividade;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.fiscal.NaturezaOperacao;
import br.com.abril.nds.model.fiscal.TipoDestinatario;
import br.com.abril.nds.model.fiscal.TipoEmitente;
import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.model.movimentacao.TipoMovimento;
import br.com.abril.nds.repository.FornecedorRepository;
import br.com.abril.nds.repository.ImpressaoNFeRepository;
import br.com.abril.nds.service.EmissaoBandeirasService;
import br.com.abril.nds.service.NaturezaOperacaoService;
import br.com.abril.nds.service.ParametrosDistribuidorService;
import br.com.abril.nds.service.RecolhimentoService;
import br.com.abril.nds.service.TipoMovimentoService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.util.JasperUtil;
import br.com.abril.nds.vo.PaginacaoVO;

@Service
public class EmissaoBandeirasServiceImpl implements EmissaoBandeirasService {
	
	@Autowired
	private RecolhimentoService recolhimentoService;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private FornecedorRepository fornecedorRepository;
	
	@Autowired
    protected ParametrosDistribuidorService parametrosDistribuidorService;
	
	@Autowired
	private ImpressaoNFeRepository impressaoNFeRepository;
	
	@Autowired
	private NaturezaOperacaoService naturezaOperacaoService;
	
	@Autowired
	private TipoMovimentoService tipoMovimentoService;
	
	@Override
	@Transactional
	public List<BandeirasDTO> obterBandeirasDaSemana(Integer semana, Long fornecedor, PaginacaoVO paginacaoVO) {
		
		Intervalo<Date> periodoRecolhimento = null;
		
		try {
			periodoRecolhimento = recolhimentoService.getPeriodoRecolhimento(semana);
		} catch (IllegalArgumentException e) {
			throw new ValidacaoException(TipoMensagem.WARNING, e.getMessage());
		}
		
		GrupoMovimentoEstoque grupoMovimentoEstoque = GrupoMovimentoEstoque.DEVOLUCAO_ENCALHE;
		TipoMovimento tipoMovimento = tipoMovimentoService.buscarTipoMovimentoEstoque(grupoMovimentoEstoque);
		TipoAtividade tipoAtividade = distribuidorService.obter().getTipoAtividade();
		TipoDestinatario tipoDestinatario = TipoDestinatario.FORNECEDOR;
		TipoEmitente tipoEmitente = TipoEmitente.DISTRIBUIDOR;
		TipoOperacao tipoOperacao = TipoOperacao.SAIDA;
		NaturezaOperacao naturezaOperacao = naturezaOperacaoService.obterNaturezaOperacaoPor(tipoAtividade, tipoDestinatario, tipoEmitente, tipoOperacao, tipoMovimento);
		if(naturezaOperacao == null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Erro ao obter a Natureza de Operação.");
		}
		FiltroImpressaoNFEDTO filtroNFE = new FiltroImpressaoNFEDTO();
		filtroNFE.setDataEmissaoInicial(periodoRecolhimento.getDe());
		filtroNFE.setDataEmissaoFinal(periodoRecolhimento.getAte());
		filtroNFE.setIdNaturezaOperacao(naturezaOperacao.getId());
		filtroNFE.setNaturezaOperacao(naturezaOperacao);
				
		return impressaoNFeRepository.obterNotafiscalImpressaoBandeira(filtroNFE);
	}
	
	@Override
	@Transactional
	public Long countObterBandeirasDaSemana(Integer semana, Long fornecedor) {
		
		Intervalo<Date> periodoRecolhimento = null;
		
		try {
			periodoRecolhimento = recolhimentoService.getPeriodoRecolhimento(semana);
		} catch (IllegalArgumentException e) {
			throw new ValidacaoException(TipoMensagem.WARNING, e.getMessage());
		}

		return 1l;
	}
	
	@Override
	public byte[] imprimirBandeira(Integer semana, Long fornecedorId, Date[] datasEnvio, Integer[] numeroPallets) throws Exception {
		
		if(datasEnvio == null || datasEnvio.length < 1) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Data(s) de Envio inválida(s).");
		}
		
		if(numeroPallets == null || numeroPallets.length < 1) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Número(s) de Pallets inválido(s).");
		}
		
		if(numeroPallets.length != datasEnvio.length) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Número(s) de Pallets e Datas inválido(s).");
		}
		
		List<BandeirasDTO> bandeiras = this.obterBandeirasDaSemana(semana, fornecedorId, null);
		
		Fornecedor f = fornecedorRepository.buscarPorId(fornecedorId);
		List<ImpressaoBandeiraVO> listaRelatorio = new ArrayList<ImpressaoBandeiraVO>(); 
		FornecedorDTO fornecedor = new FornecedorDTO();
		fornecedor.setIdFornecedor(f.getId());
		fornecedor.setNomeFantasia(f.getJuridica().getNomeFantasia());
		fornecedor.setCanalDistribuicao(f.getCanalDistribuicao());
		fornecedor.setPraca(distribuidorService.obter().getEnderecoDistribuidor().getEndereco().getCidade());
		
		for(BandeirasDTO bandeira : bandeiras) {
			for (int i = 0; i < datasEnvio.length; i++) {
				for (int j = 1; j <= numeroPallets[i]; j++) {
					String editor = bandeira.getCodigoEditor() +" - "+ bandeira.getNomeEditor();
					listaRelatorio.add(new ImpressaoBandeiraVO(fornecedor, j +" / "+ numeroPallets[i], semana, datasEnvio[i], editor, bandeira.getChaveNFe()));
				}
			}
		}
	    
		return this.gerarRelatorio(listaRelatorio);
	}

	@Override
	@Transactional
	public byte[] imprimirBandeiraManual(String semana, Integer numeroPallets,
			String fornecedor, String praca,
			String canal, String dataEnvio, String titulo) throws Exception {
		
		List<ImpressaoBandeiraVO> listaRelatorio = new ArrayList<ImpressaoBandeiraVO>(); 
		
		for (int i = 1; i <= numeroPallets; i++) {
			listaRelatorio.add(new ImpressaoBandeiraVO(fornecedor, semana, praca, canal, i +" / "+ numeroPallets, dataEnvio, titulo));
		}
				
		return this.gerarRelatorio(listaRelatorio);
	}
	
	private byte[] gerarRelatorio(List<ImpressaoBandeiraVO> listaRelatorio) throws URISyntaxException, JRException {
		
		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(listaRelatorio); 
		
	    URL url = Thread.currentThread().getContextClassLoader().getResource("/reports/emissao_bandeira.jasper");
		
		String path = url.toURI().getPath();
		
		InputStream inputStream = parametrosDistribuidorService.getLogotipoDistribuidor();
		
		if(inputStream == null) {
			inputStream = new ByteArrayInputStream(new byte[0]);
		}
		
		Image image = JasperUtil.getImagemRelatorio(inputStream);
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		parameters.put("LOGO_DISTRIBUIDOR", image);
		
		return JasperRunManager.runReportToPdf(path, parameters,ds);
	}

}
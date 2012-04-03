package br.com.abril.nds.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.GeraDividaDTO;
import br.com.abril.nds.dto.filtro.FiltroDividaGeradaDTO;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.repository.DividaRepository;
import br.com.abril.nds.service.DocumentoCobrancaService;
import br.com.abril.nds.service.ImpressaoDividaService;

@Service
public class ImpressaoDividaServiceImpl implements ImpressaoDividaService {
	
	@Autowired
	private DividaRepository dividaRepository;
	
	@Autowired
	private DocumentoCobrancaService documentoCobrancaService;
	
	@Transactional
	@Override
	public byte[] gerarArquivoImpressao(String nossoNumero) {
		
		return documentoCobrancaService.gerarDocumentoCobranca(nossoNumero);
	}
	
	@Transactional
	@Override
	public byte[] gerarArquivoImpressao(FiltroDividaGeradaDTO filtro) {
		
		List<GeraDividaDTO> dividas = null;
		
		if( TipoCobranca.BOLETO.equals(filtro.getTipoCobranca())){
			dividas = dividaRepository.obterDividasGeradas(filtro);
		}
		else {
			dividas = dividaRepository.obterDividasGeradasSemBoleto(filtro);
		}
		
		return documentoCobrancaService.gerarDocumentoCobranca(dividas, filtro.getTipoCobranca());
	}

	@Transactional
	@Override
	public void enviarArquivoPorEmail(String nossoNumero) {
		
		documentoCobrancaService.enviarDocumentoCobrancaPorEmail(nossoNumero);
	}
	
	@Transactional(readOnly=true)
	@Override
	public List<GeraDividaDTO> obterDividasGeradas(FiltroDividaGeradaDTO filtro) {
	
		return dividaRepository.obterDividasGeradas(filtro);
	}
	
	@Transactional(readOnly=true)
	@Override
	public Long obterQuantidadeDividasGeradas(FiltroDividaGeradaDTO filtro) {
		
		return dividaRepository.obterQuantidadeRegistroDividasGeradas(filtro);
	}
	
	@Transactional(readOnly=true)
	@Override
	public Boolean validarDividaGerada(Date dataMovimento) {
		
		Long quantidadeRegistro = dividaRepository.obterQunatidadeDividaGeradas(dataMovimento);
		
		return (quantidadeRegistro == null || quantidadeRegistro == 0) ? Boolean.FALSE : Boolean.TRUE;
	}

}

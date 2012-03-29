package br.com.abril.nds.service.impl;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.GeraDividaDTO;
import br.com.abril.nds.dto.filtro.FiltroDividaGeradaDTO;
import br.com.abril.nds.repository.DividaRepository;
import br.com.abril.nds.service.ImpressaoDividaService;

@Service
public class ImpressaoDividaServiceImpl implements ImpressaoDividaService {
	
	@Autowired
	private DividaRepository dividaRepository;
	
	@Override
	public byte[] gerarArquivoImpressao(String nossoNumero) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void enviarArquivoPorEmail(String nossoNumero) {
		// TODO Auto-generated method stub
		
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

	@Override
	public File gerarArquivoDividas(FiltroDividaGeradaDTO filtro) {
		// TODO Auto-generated method stub
		return null;
	}

}

package br.com.abril.nds.service.impl;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.abril.nds.dto.GeraDividaDTO;
import br.com.abril.nds.dto.filtro.FiltroDividaGeradaDTO;
import br.com.abril.nds.service.ImpressaoDividaService;

@Service
public class ImpressaoDividaServiceImpl implements ImpressaoDividaService {

	@Override
	public byte[] gerarArquivoImpressao(String nossoNumero) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void enviarArquivoPorEmail(String nossoNumero) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<GeraDividaDTO> obterDividasGeradas(FiltroDividaGeradaDTO filtro) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long obterQuantidadeDividasGeradas(FiltroDividaGeradaDTO filtro) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean validarDividaGerada(Date dataMovimento) {
		// TODO Auto-generated method stub
		return Boolean.FALSE;
	}

	@Override
	public File gerarArquivoDividas(FiltroDividaGeradaDTO filtro) {
		// TODO Auto-generated method stub
		return null;
	}

}

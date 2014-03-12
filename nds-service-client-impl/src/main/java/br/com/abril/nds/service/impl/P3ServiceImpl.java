package br.com.abril.nds.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.P3DTO;
import br.com.abril.nds.ftfutil.FTFParser;
import br.com.abril.nds.repository.P3Repository;
import br.com.abril.nds.service.P3Service;

@Service
public class P3ServiceImpl implements P3Service {

	@Autowired
	private P3Repository repoP3;

	@Override
	@Transactional
	public File gerarTxt(Date dataInicial, Date dataFinal) {

		List<P3DTO> p3DTO = new ArrayList<>();

		Boolean isRegimeEspecial = isRegimeEspecial();

		if (isRegimeEspecial) {

			p3DTO.addAll(notaEntrada(dataInicial, dataFinal));
			p3DTO.addAll(notaSaida(dataInicial, dataFinal));

		} else {

			p3DTO.addAll(notaEntrada(dataInicial, dataFinal));
			p3DTO.addAll(notaSaida(dataInicial, dataFinal));
			p3DTO.addAll(notaFicalNovo(dataInicial, dataFinal));
			p3DTO.addAll(notaEnvio(dataInicial, dataFinal));

		}

		return gerarArquivoP3(p3DTO);
	}

	@Override
	@Transactional
	public File gerarMovimentacaoCompletaTxt(Date dataInicial, Date dataFinal) {

		List<P3DTO> p3DTO = new ArrayList<>();

		p3DTO.addAll(gerarMovimentacaoCompleta(dataInicial, dataFinal));

		return gerarArquivoP3(p3DTO);
	}

	private File gerarArquivoP3(List<P3DTO> p3DTO) {
		File fileP3 = new File("arquivoP3.txt");
		BufferedWriter bw = null;

		try {
			bw = new BufferedWriter(new FileWriter(fileP3));
			FTFParser ftfParser = new FTFParser();

			for (P3DTO dto : p3DTO) {
				ftfParser.parseFTF(dto, bw);
				bw.newLine();
			}

			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileP3;
	}

	@Transactional
	private Boolean isRegimeEspecial() {
		Boolean isRegimeEspecial = repoP3.isRegimeEspecial();
		return isRegimeEspecial;
	}

	
	@Transactional
	private List<P3DTO> gerarMovimentacaoCompleta(Date dataInicial, Date dataFinal) {

		List<P3DTO> notaEntrada = repoP3.obterP3MovimentacaoCompleta(dataInicial, dataFinal);
		inserirNumSequencia(notaEntrada);

		return notaEntrada;
	}
	
	
	@Transactional
	private List<P3DTO> notaEntrada(Date dataInicial, Date dataFinal) {

		List<P3DTO> notaEntrada = repoP3.obterP3SemRegimeEspecial_Entrada(
				dataInicial, dataFinal);

		inserirNumSequencia(notaEntrada);

		return notaEntrada;
	}

	@Transactional
	private List<P3DTO> notaSaida(Date dataInicial, Date dataFinal) {

		List<P3DTO> notaSaida = repoP3.obterP3SemRegimeEspecial_Saida(
				dataInicial, dataFinal);

		inserirNumSequencia(notaSaida);

		return notaSaida;
	}

	@Transactional
	private List<P3DTO> notaFicalNovo(Date dataInicial, Date dataFinal) {

		List<P3DTO> notaFiscalNovo = repoP3
				.obterP3ComRegimeEspecial_NotaFiscalNovo(dataInicial, dataFinal);

		inserirNumSequencia(notaFiscalNovo);

		return notaFiscalNovo;
	}

	@Transactional
	private List<P3DTO> notaEnvio(Date dataInicial, Date dataFinal) {

		List<P3DTO> notaEnvio = repoP3.obterP3ComRegimeEspecial_NotaEnvio(
				dataInicial, dataFinal);

		inserirNumSequencia(notaEnvio);

		return notaEnvio;
	}

	private void inserirNumSequencia(List<P3DTO> registrosNota) {

		Integer numSequenciaItem = 1;
		String numDoc = "";

		for (P3DTO registro : registrosNota) {
			if (registro.getNumDocumento().equalsIgnoreCase(numDoc)) {
				numSequenciaItem++;
				registro.setNumSequencialItem(numSequenciaItem.toString());
			} else {
				numSequenciaItem = 1;
				numDoc = registro.getNumDocumento();
				registro.setNumSequencialItem(numSequenciaItem.toString());
			}
		}
	}

	@Transactional
	@Override
	public Integer countBusca(Date dataInicial, Date dataFinal) {

		Integer count = 0;

		Boolean isRegimeEsp = isRegimeEspecial();

		if (isRegimeEsp) {
			count += repoP3.count_obterP3SemRegimeEspecial_Entrada(dataInicial,
					dataFinal);
			count += repoP3.count_obterP3SemRegimeEspecial_Saida(dataInicial,
					dataFinal);
		} else {
			count += repoP3.count_obterP3SemRegimeEspecial_Entrada(dataInicial,
					dataFinal);
			count += repoP3.count_obterP3SemRegimeEspecial_Saida(dataInicial,
					dataFinal);
			count += repoP3.count_obterP3ComRegimeEspecial_NotaEnvio(
					dataInicial, dataFinal);
			count += repoP3.count_obterP3ComRegimeEspecial_NotaFiscalNovo(
					dataInicial, dataFinal);
		}

		return count;
	}

}

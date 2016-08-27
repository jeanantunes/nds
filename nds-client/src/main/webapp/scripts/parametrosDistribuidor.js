var parametrosDistribuidorController = $.extend(true, {
	path : contextPath + '/administracao/parametrosDistribuidor/',
	gravarDiasDistribuidorFornecedor: function() {
		
		var listaFornecedoresLancamento = [];
		var listaDiasLancamento         = [];
		var listaDiasRecolhimento       = [];

		$('#selectFornecedoresLancamento :selected', this.workspace).each(function(i, selected) {
			listaFornecedoresLancamento[i] = $(selected).val();
		    //textvalues[i] = $(selected).text();
		});

		$('#selectDiasLancamento :selected', this.workspace).each(function(i, selected) {
			listaDiasLancamento[i] = $(selected).val();
		});

		$('#selectDiasRecolhimento :selected', this.workspace).each(function(i, selected) {
			listaDiasRecolhimento[i] = $(selected).val();
		});
		
		var data = [{name: 'selectFornecedoresLancamento', value: listaFornecedoresLancamento},
		            {name: 'selectDiasLancamento', 		   value: listaDiasLancamento},
		            {name: 'selectDiasRecolhimento', 	   value: listaDiasRecolhimento}];

		$.postJSON(parametrosDistribuidorController.path + "gravarDiasDistribuidorFornecedor", 
				   data,
				   function (resultado) {
						exibirMensagem(resultado.tipoMensagem, 
									   resultado.listaMensagens);
						parametrosDistribuidorController.recarregarDiasDistribuidorFornecedorGrid();
						$('#selectDiasLancamento', this.workspace).val('');
						$('#selectFornecedoresLancamento', this.workspace).val('');
						$('#selectDiasRecolhimento', this.workspace).val('');
				   });
	},

	excluirDiasDistribuidorFornecedor: function(id) {
		var data = [{name: 'codigoFornecedor', value: id}];
		$.postJSON(parametrosDistribuidorController.path + "excluirDiasDistribuicaoFornecedor",
				   data,
				   function (resultado) {
					   exibirMensagem(resultado.tipoMensagem, 
					   				  resultado.listaMensagens);
					   parametrosDistribuidorController.recarregarDiasDistribuidorFornecedorGrid();
				   });
	},

	recarregarDiasDistribuidorFornecedorGrid: function() {
		$.postJSON(parametrosDistribuidorController.path + "recarregarDiasDistribuidorFornecedorGrid", 
			null,
			function (resultado) {
				if (resultado.mensagens) {
					exibirMensagem(
							resultado.tipoMensagem, 
							resultado.listaMensagens
					);
				}
				
				var startTag = '<table width="441" border="0" cellpadding="0" cellspacing="1" id="tableDiasDistribuidorFornecedor"><tr class="header_table"><td>Fornecedor</td><td align="center">Lançamento</td><td align="center">Recolhimento</td><td align="center">&nbsp;</td></tr>';
				var endTag = '</table>';
				var newTable = startTag;
				$.each(resultado, function(index, row) {
					newTable += '<tr class="class_linha_1"><td width="139">' + 
					row.fornecedor.juridica.nomeFantasia + '</td><td width="144" align="center">' + 
					row.diasLancamento + '</td><td width="125" align="center">' + 
					row.diasRecolhimento + 
					'</td><td width="28" align="center"><a href="javascript:;" onclick="parametrosDistribuidorController.excluirDiasDistribuidorFornecedor(' + row.fornecedor.id + ')" ><img src="'+ contextPath +'/images/ico_excluir.gif" width="15" height="15" alt="Excluir" /></a></td></tr>';
				});
				newTable += endTag;
				$('#spanDiasDistribuidorFornecedor', this.workspace).html(newTable);
			}
		);
	},

	gravar: function() {
		var data = [
			{name:'parametrosDistribuidor.razaoSocial', value: $('#razaoSocial', this.workspace).val()},
			{name:'parametrosDistribuidor.nomeFantasia', value: $('#nomeFantasia', this.workspace).val()},
			{name:'parametrosDistribuidor.cnpj', value: $('#cnpj', this.workspace).val()},
			{name:'parametrosDistribuidor.inscricaoEstadual', value: $('#inscricaoEstadual', this.workspace).val()},
			{name:'parametrosDistribuidor.inscricaoMunicipal', value: $('#inscricaoMunicipal', this.workspace).val()},
			{name:'parametrosDistribuidor.email', value: $('#email', this.workspace).val()},
			{name:'parametrosDistribuidor.codigoDistribuidorDinap', value: $('#codigoDistribuidorDinap', this.workspace).val()},
			{name:'parametrosDistribuidor.codigoDistribuidorFC', value: $('#codigoDistribuidorFC', this.workspace).val()},
			
			{name:'parametrosDistribuidor.endereco.tipoEndereco', value: $('#tipoEndereco', this.workspace).val()},
			{name:'parametrosDistribuidor.endereco.cep', value: $('#DISTRIBUIDORcep', this.workspace).val()},
			{name:'parametrosDistribuidor.endereco.tipoLogradouro', value: $('#tipoLogradouro', this.workspace).val()},
			{name:'parametrosDistribuidor.endereco.logradouro', value: $('#logradouro', this.workspace).val()},
			{name:'parametrosDistribuidor.endereco.numero', value: $('#numero', this.workspace).val()},
			{name:'parametrosDistribuidor.endereco.complemento', value: $('#complemento', this.workspace).val()},
			{name:'parametrosDistribuidor.endereco.bairro', value: $('#bairro', this.workspace).val()},
			{name:'parametrosDistribuidor.endereco.localidade', value: $('#cidade', this.workspace).val()},
			{name:'parametrosDistribuidor.endereco.uf', value: $('#DISTRIBUIDORuf', this.workspace).val()}, 
			{name:'parametrosDistribuidor.endereco.codigoCidadeIBGE', value: $('#DISTRIBUIDORcodigoCidadeIBGE', this.workspace).val() != undefined ? $('#DISTRIBUIDORcodigoCidadeIBGE', this.workspace).val() : ''},
			{name:'parametrosDistribuidor.endereco.codigoBairro', value: $('#codigoBairro', this.workspace).val() != undefined ? $('#codigoBairro', this.workspace).val() : ''},
			
			{name:'parametrosDistribuidor.numeroTelefone', value: $('#numeroTelefone', this.workspace).val()},
			{name:'parametrosDistribuidor.numeroDDD', value: $('#numeroDDD', this.workspace).val()},
			
			{name:'parametrosDistribuidor.relancamentoParciaisEmDias', value: $('#relancamentoParciaisEmDias', this.workspace).val()},
			{name:'parametrosDistribuidor.aceitaEncalheJuramentada', value: $('#aceitaEncalheJuramentada', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.tipoContabilizacaoCE', value: $("input[name='parametrosDistribuidor.tipoContabilizacaoCE']:checked", this.workspace).val()},
			{name:'parametrosDistribuidor.aceitaRecolhimentoParcialAtraso', value: $('#aceitaRecolhimentoParcialAtraso', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.supervisionaVendaNegativa', value: $('#supervisionaVendaNegativa', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.chamadaoDiasSuspensao', value: $('#chamadaoDiasSuspensao', this.workspace).val()},
			{name:'parametrosDistribuidor.chamadaoValorConsignado', value: $('#chamadaoValorConsignado', this.workspace).val()},
			{name:'parametrosDistribuidor.diaRecolhimentoPrimeiro', value: $('#diaRecolhimentoPrimeiro', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.diaRecolhimentoSegundo', value: $('#diaRecolhimentoSegundo', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.diaRecolhimentoTerceiro', value: $('#diaRecolhimentoTerceiro', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.diaRecolhimentoQuarto', value: $('#diaRecolhimentoQuarto', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.diaRecolhimentoQuinto', value: $('#diaRecolhimentoQuinto', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.limiteCEProximaSemana', value: $('#limiteCEProximaSemana', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.conferenciaCegaRecebimento', value: $('#conferenciaCegaRecebimento', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.conferenciaCegaEncalhe', value: $('#conferenciaCegaEncalhe', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.capacidadeManuseioHomemHoraLancamento', value: $('#capacidadeManuseioHomemHoraLancamento', this.workspace).val()},
			{name:'parametrosDistribuidor.capacidadeManuseioHomemHoraRecolhimento', value: $('#capacidadeManuseioHomemHoraRecolhimento', this.workspace).val()},
			{name:'parametrosDistribuidor.reutilizacaoCodigoCotaInativa', value: $('#reutilizacaoCodigoCotaInativa', this.workspace).val()},
			{name:'parametrosDistribuidor.utilizaSugestaoIncrementoCodigo', value: $('#utilizaSugestaoIncrementoCodigo', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.slipImpressao', value: $('#slipImpressao', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.slipEmail', value: $('#slipEmail', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.boletoImpressao', value: $('#boletoImpressao', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.boletoEmail', value: $('#boletoEmail', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.boletoSlipImpressao', value: $('#boletoSlipImpressao', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.boletoSlipEmail', value: $('#boletoSlipEmail', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.reciboImpressao', value: $('#reciboImpressao', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.reciboEmail', value: $('#reciboEmail', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.notaEnvioImpressao', value: $('#notaEnvioImpressao', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.notaEnvioEmail', value: $('#notaEnvioEmail', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.chamadaEncalheImpressao', value: $('#chamadaEncalheImpressao', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.chamadaEncalheEmail', value: $('#chamadaEncalheEmail', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.impressaoInterfaceLED', value: $("input[name='interfaceLED']:checked", this.workspace).val()},
			{name:'parametrosDistribuidor.nomeArquivoInterfaceLED1', value: $('#nomeArquivoInterfaceLED1', this.workspace).val()},
			{name:'parametrosDistribuidor.nomeArquivoInterfaceLED2', value: $('#nomeArquivoInterfaceLED2', this.workspace).val()},
			{name:'parametrosDistribuidor.nomeArquivoInterfaceLED3', value: $('#nomeArquivoInterfaceLED3', this.workspace).val()},
			{name:'parametrosDistribuidor.nomeArquivoInterfaceLED4', value: $('#nomeArquivoInterfaceLED4', this.workspace).val()},
			{name:'parametrosDistribuidor.impressaoNECADANFE', value: $("input[name='impressaoNECADANFE']:checked", this.workspace).val()},
			{name:'parametrosDistribuidor.impressaoCE', value: $("input[name='impressaoCE']:checked", this.workspace).val()},
			{name:'parametrosDistribuidor.utilizaContratoComCotas', value: $('#utilizaContratoComCotas', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.prazoContrato', value: $('#prazoContrato', this.workspace).val()},
			{name:'parametrosDistribuidor.informacoesComplementaresContrato', value: $('#informacoesComplementaresContrato', this.workspace).val()},
			{name:'parametrosDistribuidor.utilizaProcuracaoEntregadores', value: $('#utilizaProcuracaoEntregadores', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.informacoesComplementaresProcuracao', value: $('#informacoesComplementaresProcuracao', this.workspace).val() != undefined ? $('#informacoesComplementaresProcuracao', this.workspace).val() : '' },
			{name:'parametrosDistribuidor.utilizaTermoAdesaoEntregaBancas', value: $('#utilizaTermoAdesaoEntregaBancas', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.complementoTermoAdesaoEntregaBancas', value: $('#complementoTermoAdesaoEntregaBancas', this.workspace).val() != undefined ? $('#complementoTermoAdesaoEntregaBancas', this.workspace).val() : '' },
			{name:'parametrosDistribuidor.utilizaGarantiaPdv', value: $('#utilizaGarantiaPdv', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.utilizaChequeCaucao', value: $('#utilizaChequeCaucao', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.validadeChequeCaucao', value: $('#validadeChequeCaucao', this.workspace).val()},
			{name:'parametrosDistribuidor.utilizaFiador', value: $('#utilizaFiador', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.validadeFiador', value: $('#validadeFiador', this.workspace).val()},
			{name:'parametrosDistribuidor.utilizaImovel', value: $('#utilizaImovel', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.validadeImovel', value: $('#validadeImovel', this.workspace).val()},
			{name:'parametrosDistribuidor.utilizaCaucaoLiquida', value: $('#utilizaCaucaoLiquida', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.validadeCaucaoLiquida', value: $('#validadeCaucaoLiquida', this.workspace).val()},
			{name:'parametrosDistribuidor.utilizaNotaPromissoria', value: $('#utilizaNotaPromissoria', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.validadeNotaPromissoria', value: $('#validadeNotaPromissoria', this.workspace).val()},
			{name:'parametrosDistribuidor.validadeAntecedenciaValidade', value: $('#validadeAntecedenciaValidade', this.workspace).val()},
			{name:'parametrosDistribuidor.utilizaOutros', value: $('#utilizaOutros', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.validadeOutros', value: $('#validadeOutros', this.workspace).val()},
			{name:'parametrosDistribuidor.sugereSuspensaoQuandoAtingirBoletos', value: $('#sugereSuspensaoQuandoAtingirBoletos', this.workspace).val() != undefined ? $('#sugereSuspensaoQuandoAtingirBoletos', this.workspace).val() : ''},
			{name:'parametrosDistribuidor.sugereSuspensaoQuandoAtingirReais', value: $('#sugereSuspensaoQuandoAtingirReais', this.workspace).val() != undefined ? $('#sugereSuspensaoQuandoAtingirReais', this.workspace).val() : ''},
			{name:'parametrosDistribuidor.sugereSuspensao', value: $('#sugereSuspensaoDistribuidor', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.parcelamentoDividas', value: $('#parcelamentoDividas', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.negociacaoAteParcelas', value: $('#negociacaoAteParcelas', this.workspace).val() != undefined ? $('#negociacaoAteParcelas', this.workspace).val() : ''},
			{name:'parametrosDistribuidor.aceitaBaixaPagamentoMaior', value: $('#aceitaBaixaPagamentoMaior', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.aceitaBaixaPagamentoMenor', value: $('#aceitaBaixaPagamentoMenor', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.aceitaBaixaPagamentoVencido', value: $('#aceitaBaixaPagamentoVencido', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.numeroDiasNovaCobranca', value: $('#numeroDiasNovaCobranca', this.workspace).val() != undefined ?  $('#numeroDiasNovaCobranca', this.workspace).val() : ''},
			{name:'parametrosDistribuidor.assuntoEmailCobranca', value: $('#assuntoEmailCobranca', this.workspace).val() != undefined ? $('#assuntoEmailCobranca', this.workspace).val() : ''},
			{name:'parametrosDistribuidor.mensagemEmailCobranca', value: $('#mensagemEmailCobranca', this.workspace).val() != undefined ? $('#mensagemEmailCobranca', this.workspace).val() : ''},
			{name:'parametrosDistribuidor.utilizaDesconto', value: $('#utilizaDesconto', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.descricaoTaxaExtra', value: $('#descricaoTaxaExtra', this.workspace).val() != undefined ? $('#descricaoTaxaExtra', this.workspace).val() : ''},
			{name:'parametrosDistribuidor.percentualTaxaExtra', value: $('#percentualTaxaExtra', this.workspace).val() != "" ? 
					parseFloat($('#percentualTaxaExtra', this.workspace).val().replace(',', '.')).toFixed(2) : 0 },
					
			
			
			{name:'parametrosDistribuidor.pararAcumuloDividas', value: $('#pararAcumuloDividas', this.workspace).val() != undefined ? $('#pararAcumuloDividas', this.workspace).val() : ''},
			
			{name:'parametrosDistribuidor.percentualDesconto', value: $('#tab-negociacao-percentualDesconto', this.workspace).val() != undefined ? $('#tab-negociacao-percentualDesconto', this.workspace).val() : ''},		
			{name:'parametrosDistribuidor.utilizaControleAprovacao', value: $('#utilizaControleAprovacao', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.paraDebitosCreditos', value: $('#paraDebitosCreditos', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.negociacao', value: $('#negociacao', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.ajusteEstoque', value: $('#ajusteEstoque', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.postergacaoCobranca', value: $('#postergacaoCobranca', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.devolucaoFornecedor', value: $('#devolucaoFornecedor', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.recibo', value: $('#recibo', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.faltasSobras', value: $('#faltasSobras', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.consolidadoCota', value: $('#cobranca', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.aprovacaoFaltaDe', value: $('#aprovacaoFaltaDe', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.aprovacaoSobraDe', value: $('#aprovacaoSobraDe', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.aprovacaoFaltaEm', value: $('#aprovacaoFaltaEm', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.aprovacaoSobraEm', value: $('#aprovacaoSobraEm', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.prazoFollowUp', value: $('#prazoFollowUp', this.workspace).val() != undefined ? $('#prazoFollowUp', this.workspace).val() : ''},
			{name:'parametrosDistribuidor.prazoFollowUpFaltaDe', value: $('#prazoFollowUpFaltaDe', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.prazoFollowUpSobraDe', value: $('#prazoFollowUpSobraDe', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.prazoFollowUpFaltaEm', value: $('#prazoFollowUpFaltaEm', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.prazoFollowUpSobraEm', value: $('#prazoFollowUpSobraEm', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.prazoAvisoPrevioValidadeGarantia', value: $('#prazoAvisoPrevioValidadeGarantia', this.workspace).val() != undefined ? $('#prazoAvisoPrevioValidadeGarantia', this.workspace).val() : ''},
			{name:'parametrosDistribuidor.prazoAvisoPrevioValidadeGarantiaFaltaDe', value: $('#prazoAvisoPrevioValidadeGarantiaFaltaDe', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.prazoAvisoPrevioValidadeGarantiaSobraDe', value: $('#prazoAvisoPrevioValidadeGarantiaSobraDe', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.prazoAvisoPrevioValidadeGarantiaFaltaEm', value: $('#prazoAvisoPrevioValidadeGarantiaFaltaEm', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.prazoAvisoPrevioValidadeGarantiaSobraEm', value: $('#prazoAvisoPrevioValidadeGarantiaSobraEm', this.workspace).is(':checked')},
			
			{name:'parametrosDistribuidor.cnae', value: $('#fiscal-cnae', this.workspace).val()},
			{name:'parametrosDistribuidor.tipoAtividade', value: $('#tipoAtividade', this.workspace).val()},
			{name:'parametrosDistribuidor.regimeTributario.id', value: $('#regimeTributario', this.workspace).val()},
			{name:'parametrosDistribuidor.nfInformacoesAdicionais', value: $('#informacoesAdicionais', this.workspace).val()},
			{name:'parametrosDistribuidor.possuiRegimeEspecialDispensaInterna', value: $('#possuiRegimeEspecialDispensaInterna', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.numeroDispositivoLegal', value: $('#numeroDispositivoLegal', this.workspace).val()},
			{name:'parametrosDistribuidor.dataLimiteVigenciaRegimeEspecial', value: $('#dataLimiteVigenciaRegimeEspecial', this.workspace).val()},

			//Grid Distribuicao
			{name:'parametrosDistribuidor.geracaoAutomaticaEstudo', value: $('#geracaoAutomaticaEstudo', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.infoReparteComplementar', value: $('#infoReparteComplementar', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.vendaMediaMais', value: $('#vendaMediaMais', this.workspace).val()},
			{name:'parametrosDistribuidor.pracaVeraneio', value: $('#pracaVeraneio', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.complementarAutomatico', value: $('#complementarAutomatico', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.percentualMaximoFixacao', value: $('#percentualMaximoFixacao', this.workspace).val()},
			
			//Grid Classificacao Cota
			{name:'parametrosDistribuidor.listClassificacaoCota[0].id', value: $('#listClassificacaoCota0\\.id').val()},
			{name:'parametrosDistribuidor.listClassificacaoCota[0].codigoClassificacaoCota', value: $('#listClassificacaoCota0\\.codigoClassificacaoCota').val()},
			{name:'parametrosDistribuidor.listClassificacaoCota[0].valorDe', value: $('#listClassificacaoCota0\\.valorDe').val().replace(/^.*\s|\./g, '').replace(/,/g, ',')},
			{name:'parametrosDistribuidor.listClassificacaoCota[0].valorAte', value:$('#listClassificacaoCota0\\.valorAte').val().replace(/^.*\s|\./g, '').replace(/,/g, ',')},
			
			{name:'parametrosDistribuidor.listClassificacaoCota[1].id', value: $('#listClassificacaoCota1\\.id').val()},
			{name:'parametrosDistribuidor.listClassificacaoCota[1].codigoClassificacaoCota', value: $('#listClassificacaoCota1\\.codigoClassificacaoCota').val()},
			{name:'parametrosDistribuidor.listClassificacaoCota[1].valorDe', value: $('#listClassificacaoCota1\\.valorDe').val().replace(/^.*\s|\./g, '').replace(/,/g, ',')},
			{name:'parametrosDistribuidor.listClassificacaoCota[1].valorAte', value:$('#listClassificacaoCota1\\.valorAte').val().replace(/^.*\s|\./g, '').replace(/,/g, ',')},
			
			{name:'parametrosDistribuidor.listClassificacaoCota[2].id', value: $('#listClassificacaoCota2\\.id').val()},
			{name:'parametrosDistribuidor.listClassificacaoCota[2].codigoClassificacaoCota', value: $('#listClassificacaoCota2\\.codigoClassificacaoCota').val()},
			{name:'parametrosDistribuidor.listClassificacaoCota[2].valorDe', value: $('#listClassificacaoCota2\\.valorDe').val().replace(/^.*\s|\./g, '').replace(/,/g, ',')},
			{name:'parametrosDistribuidor.listClassificacaoCota[2].valorAte', value:$('#listClassificacaoCota2\\.valorAte').val().replace(/^.*\s|\./g, '').replace(/,/g, ',')},
			
			{name:'parametrosDistribuidor.listClassificacaoCota[3].id', value: $('#listClassificacaoCota3\\.id').val()},
			{name:'parametrosDistribuidor.listClassificacaoCota[3].codigoClassificacaoCota', value: $('#listClassificacaoCota3\\.codigoClassificacaoCota').val()},
			{name:'parametrosDistribuidor.listClassificacaoCota[3].valorDe', value: $('#listClassificacaoCota3\\.valorDe').val().replace(/^.*\s|\./g, '').replace(/,/g, ',')},
			{name:'parametrosDistribuidor.listClassificacaoCota[3].valorAte', value:$('#listClassificacaoCota3\\.valorAte').val().replace(/^.*\s|\./g, '').replace(/,/g, ',')},
			
			{name:'parametrosDistribuidor.listClassificacaoCota[4].id', value: $('#listClassificacaoCota4\\.id').val()},
			{name:'parametrosDistribuidor.listClassificacaoCota[4].codigoClassificacaoCota', value: $('#listClassificacaoCota4\\.codigoClassificacaoCota').val()},
			{name:'parametrosDistribuidor.listClassificacaoCota[4].valorDe', value: $('#listClassificacaoCota4\\.valorDe').val().replace(/^.*\s|\./g, '').replace(/,/g, ',')},
			{name:'parametrosDistribuidor.listClassificacaoCota[4].valorAte', value:$('#listClassificacaoCota4\\.valorAte').val().replace(/^.*\s|\./g, '').replace(/,/g, ',')},
			
			//Grid Percentual Excedente
			{name:'parametrosDistribuidor.listPercentualExcedente[0].id', value: $('#listPercentualExcedente0\\.id').val()},
			{name:'parametrosDistribuidor.listPercentualExcedente[0].eficiencia', value: $('#listPercentualExcedente0\\.eficiencia').val()},
			{name:'parametrosDistribuidor.listPercentualExcedente[0].venda', value: $('#listPercentualExcedente0\\.venda').val()},
			{name:'parametrosDistribuidor.listPercentualExcedente[0].pdv', value: $('#listPercentualExcedente0\\.pdv').val()},
			
			{name:'parametrosDistribuidor.listPercentualExcedente[1].id', value: $('#listPercentualExcedente1\\.id').val()},
			{name:'parametrosDistribuidor.listPercentualExcedente[1].eficiencia', value: $('#listPercentualExcedente1\\.eficiencia').val()},
			{name:'parametrosDistribuidor.listPercentualExcedente[1].venda', value: $('#listPercentualExcedente1\\.venda').val()},
			{name:'parametrosDistribuidor.listPercentualExcedente[1].pdv', value: $('#listPercentualExcedente1\\.pdv').val()},
			
			{name:'parametrosDistribuidor.listPercentualExcedente[2].id', value: $('#listPercentualExcedente2\\.id').val()},
			{name:'parametrosDistribuidor.listPercentualExcedente[2].eficiencia', value: $('#listPercentualExcedente2\\.eficiencia').val()},
			{name:'parametrosDistribuidor.listPercentualExcedente[2].venda', value: $('#listPercentualExcedente2\\.venda').val()},
			{name:'parametrosDistribuidor.listPercentualExcedente[2].pdv', value: $('#listPercentualExcedente2\\.pdv').val()},
			
		];
		
		itensTiposNotasFiscais = [];
		
		$.each($('#tabFiscal :input[type=radio]', this.workspace), function(index, value) {
			itensTiposNotasFiscais[index] = value.name;
		});
		
		itensTiposNotasFiscais = jQuery.unique(itensTiposNotasFiscais);
		
		$.each(itensTiposNotasFiscais, function(index, value) {
			data.push({name:'parametrosDistribuidor.tiposNotasFiscais[].nome', value: itensTiposNotasFiscais[index]});
			data.push({name:'parametrosDistribuidor.tiposNotasFiscais[].valor', value: $('input[name='+ itensTiposNotasFiscais[index] +']:checked').val() != undefined ? $('input[name='+ itensTiposNotasFiscais[index] +']:checked').val() : -1});
		});
		
		
		$.each($('#regimeTributarioTributos input[type=text]'), function(key, value) {
			data.push({name:'parametrosDistribuidor.tributosAliquotas[].id', value: $('#id' + value.name).val()});
			data.push({name:'parametrosDistribuidor.tributosAliquotas[].tributoId', value: $('#tributoId' + value.name).val()});
			data.push({name:'parametrosDistribuidor.tributosAliquotas[].valor', value: $('#valor' + value.name).val()});
		});
	
		$.postJSON(parametrosDistribuidorController.path + "gravar",
				   data,
				   function (resultado) {
					   exibirMensagem(resultado.tipoMensagem, 
					   				  resultado.listaMensagens);
					   parametrosDistribuidorController.recarregarDiasDistribuidorFornecedorGrid();
				   });
	},
	
	changeFlagRegimeEspecial : function() {
		if($('#possuiRegimeEspecialDispensaInterna').is(':checked')) {
			$('.camposEspecificosRegimeEspecial').show();
		} else {
			$('.camposEspecificosRegimeEspecial').hide();
		}
		
	},

	atualizarLogo: function() {
		
		$("#div_imagem_logotipo").empty();
		
		var img = $("<img />");
		img.load(
		    function() {						
		    	$("#div_imagem_logotipo").append(img);
		    }
		)
		.attr('src', parametrosDistribuidorController.path + "getLogo?timestamp=" + new Date().getTime())
		.attr('width', 110)
		.attr('height', 70)
		.attr('alt', "Logotipo Distribuidor");
	},

	salvarLogo: function() {
		
		$('#formParamentrosDistribuidor', this.workspace).submit();
	},

	tratarRespostaSalvarLogo: function() {

		parametrosDistribuidorController.atualizarLogo();
	},

	popup_confirm: function() {
	    
	    var arrayMensagemWarning = new Array();
	    
	    var vendaMediaMais = $('#vendaMediaMais', this.workspace).val();
	    var percentualMaximoFixacao = $('#percentualMaximoFixacao', this.workspace).val();
	    var percentualTaxaExtra =  $('#percentualTaxaExtra', this.workspace).val() != "" ? 
				parseFloat($('#percentualTaxaExtra', this.workspace).val().replace(',', '.')).toFixed(2) : 0 ;
	    var descricaoTaxaExtra = $('#descricaoTaxaExtra', this.workspace).val() != undefined ? $('#descricaoTaxaExtra', this.workspace).val() : '';
	    var eficiencia0 = parseInt($('#listPercentualExcedente0\\.venda', this.workspace).val()) + parseInt($('#listPercentualExcedente0\\.pdv', this.workspace).val());
	    var eficiencia1 = parseInt($('#listPercentualExcedente1\\.venda', this.workspace).val()) + parseInt($('#listPercentualExcedente1\\.pdv', this.workspace).val());
	    var eficiencia2 = parseInt($('#listPercentualExcedente2\\.venda', this.workspace).val()) + parseInt($('#listPercentualExcedente2\\.pdv', this.workspace).val());
	    
	    var slipImpressao = $('#slipImpressao', this.workspace).is(':checked');
		var slipEmail = $('#slipEmail', this.workspace).is(':checked');
		var boletoImpressao = $('#boletoImpressao', this.workspace).is(':checked');
		var boletoEmail = $('#boletoEmail', this.workspace).is(':checked');
		var boletoSlipImpressao = $('#boletoSlipImpressao', this.workspace).is(':checked');
		var boletoSlipEmail = $('#boletoSlipEmail', this.workspace).is(':checked');
		var reciboImpressao = $('#reciboImpressao', this.workspace).is(':checked');
		var reciboEmail = $('#reciboEmail', this.workspace).is(':checked');
		var notaEnvioImpressao = $('#notaEnvioImpressao', this.workspace).is(':checked');
		var notaEnvioEmail = $('#notaEnvioEmail', this.workspace).is(':checked');
		var chamadaEncalheImpressao = $('#chamadaEncalheImpressao', this.workspace).is(':checked');
		var chamadaEncalheEmail = $('#chamadaEncalheEmail', this.workspace).is(':checked');
	    
	    
	    if(vendaMediaMais > 10 || vendaMediaMais < 1) {
	    	arrayMensagemWarning.push("- \'Venda Média +\' deve ser de 01 a 10!");
	    }
		
	    if(percentualMaximoFixacao > 75 || percentualMaximoFixacao < 1) {
	    	arrayMensagemWarning.push("- \'% Máximo de Fixação\' deve ser de 1% a 75%!");
	    }
	   
	    if(percentualTaxaExtra > 0 &&  descricaoTaxaExtra.length == 0 ) {
			arrayMensagemWarning.push("- \'Descrição de Taxa Extra obrigatorio!");
	    } 
	    if(percentualTaxaExtra == 0 && ( descricaoTaxaExtra.length > 0) ) {
			arrayMensagemWarning.push("- \'Obrigatorio informar valor da Taxa Extra quando informado a Descricao!");
	    } 
	    
	    if(eficiencia0 != 100) {
	    	arrayMensagemWarning.push("- '\> 60 %\' deve ter o total da soma igual a 100%!");
	    }
	    
	    if(eficiencia1 != 100) {
	    	arrayMensagemWarning.push("- '\> 30% a 60%\' deve ter o total da soma igual a 100%!");
	    }
	    
	    if(eficiencia2 != 100) {
	    	arrayMensagemWarning.push("- '\0% a 30%\' deve ter o total da soma igual a 100%!");
	    }
	    
	    if(slipImpressao == false && slipEmail == false){
	    	arrayMensagemWarning.push("Selecione pelo menos um tipo de emissão para SLIP! ");
	    }
	    
	    if(boletoImpressao == false && boletoEmail == false){
	    	arrayMensagemWarning.push("Selecione pelo menos um tipo de emissão para Boleto / Recibo! ");
	    }

	    if(boletoSlipImpressao == false && boletoSlipEmail == false){
	    	arrayMensagemWarning.push("Selecione pelo menos um tipo de emissão para Boleto + Slip! ");
	    }
	    
	    if(notaEnvioImpressao == false && notaEnvioEmail == false){
	    	arrayMensagemWarning.push("Selecione pelo menos um tipo de emissão para Nota de Envio! ");
	    }

	    if(chamadaEncalheImpressao == false && chamadaEncalheEmail == false){
	    	arrayMensagemWarning.push("Selecione pelo menos um tipo de emissão para Chamada de Encalhe! ");
	    }
	    
	    if(arrayMensagemWarning.length > 0) {
	    	exibirMensagem('WARNING', arrayMensagemWarning);
	    } else {
	    	$("#confirmarParametrosDistribuidor").dialog("open");
			
	    }
	},
	
	popup_pesq_fornecedor: function() {
		
		$("#dialog-pesq-fornecedor", this.workspace).dialog({
			resizable: false,
			height:300,
			width:490,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$(this).dialog("close");
					$(".forncedoresSel", this.workspace).css('display','table-cell');
				},
				"Cancelar": function() {
					$(this).dialog("close");
				}
			}, 
			form: $("#dialog-pesq-fornecedor", this.workspace).parents("form")
		});
	},
		
	//callback function to bring a hidden box back
	callback: function() {
		setTimeout(function() {
			$("#effect:visible", this.workspace).removeAttr("style").fadeOut();

		}, 1000 );
	},

//	removeFornecedor: function(){
//		$( ".forncedoresSel" ).fadeOut('fast');
//	},

	utilizaContratoCotasListener: function() {
		if ($('#utilizaContratoComCotas', this.workspace).is(':checked')) {
			$('#prazoContrato', this.workspace).enable();
		} else {
			$('#prazoContrato', this.workspace).val("").disable();
			$('#informacoesComplementaresContrato', this.workspace).wysiwyg('clear');
		}
		
		parametrosDistribuidorController.exibirContratoCota();
	},

	utilizaProcuracaoEntregadoresListener: function() {
		if (!$('#utilizaProcuracaoEntregadores', this.workspace).is(':checked')) {
			$('#informacoesComplementaresProcuracao', this.workspace).wysiwyg('clear');
		}
		
		parametrosDistribuidorController.exibirProcuracao();
		
	},

	utilizaTermoAdesaoListener: function() {
		if (!$('#utilizaTermoAdesaoEntregaBancas', this.workspace).is(':checked')) {
			$('#complementoTermoAdesaoEntregaBancas', this.workspace).wysiwyg('clear');
		}
		
		parametrosDistribuidorController.exibirAdesao();		
	},

	utilizaGarantiaListener: function(tipoGarantia, validadeGarantia) {
		if ($(tipoGarantia, this.workspace).is(':checked')) {
			$('#' + validadeGarantia, this.workspace).enable();
		} else {
			$('#' + validadeGarantia, this.workspace).val('').disable();
		}
	},
	
	mostraTabelaGarantiasAceitas: function(){
		if ($('#utilizaGarantiaPdv', this.workspace).is(':checked')) {
			$('#tabelaGarantiasAceitas', this.workspace).show();
		} else {
			$('#utilizaChequeCaucao', this.workspace).uncheck();
			$('#validadeChequeCaucao', this.workspace).val('').disable();
			
			$('#utilizaCaucaoLiquida', this.workspace).uncheck();
			$('#validadeCaucaoLiquida', this.workspace).val('').disable();
			
			$('#utilizaFiador', this.workspace).uncheck();
			$('#validadeFiador', this.workspace).val('').disable();
			
			$('#utilizaNotaPromissoria', this.workspace).uncheck();
			$('#validadeNotaPromissoria', this.workspace).val('').disable();
			
			$('#utilizaImovel', this.workspace).uncheck();
			$('#validadeImovel', this.workspace).val('').disable();
			
		
			
			$('#utilizaOutros', this.workspace).uncheck();
			$('#validadeOutros', this.workspace).val('').disable();
			
			$('#tabelaGarantiasAceitas', this.workspace).hide();
		}
	},

	alternarControleAprovacao: function(){
		
		if ($("#utilizaControleAprovacao", this.workspace).is(":checked")){
			
			$("table#controlesAprovacao input[type=checkbox]", this.workspace).enable();
		} else {
			
			$("table#controlesAprovacao input[type=checkbox]", this.workspace).uncheck();
			$("table#controlesAprovacao input[type=checkbox]", this.workspace).disable();
		}
		
		$("input:checkbox[id=controlesAprovacao]", this.workspace).attr("disabled", "disabled");
	},
	
	init: function() {
		
		$("#confirmarParametrosDistribuidor").dialog({
			autoOpen: false,
			resizable: false,
			height:160,
			width:400,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$("#confirmarParametrosDistribuidor").dialog("close");
					parametrosDistribuidorController.gravar();						
				},
				"Cancelar": function() {
					$("#confirmarParametrosDistribuidor").dialog("close");
				}
			}, 
			//form: $("#dialog-confirm", this.workspace).parents("form")
		});
		
		$("#dataLimiteVigenciaRegimeEspecial", this.workspac ).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		
		$("#dataLimiteVigenciaRegimeEspecial", this.workspace).mask("99/99/9999");
		
		$('#informacoesComplementaresContrato', this.workspace).wysiwyg();
		$('#informacoesComplementaresContrato', this.workspace).wysiwyg({controls:"font-family,italic,|,undo,redo"});
		
		$('#informacoesComplementaresProcuracao', this.workspace).wysiwyg();
		$('#informacoesComplementaresProcuracao', this.workspace).wysiwyg({controls:"font-family,italic,|,undo,redo"});
		
		$('#complementoTermoAdesaoEntregaBancas', this.workspace).wysiwyg();
		$('#complementoTermoAdesaoEntregaBancas', this.workspace).wysiwyg({controls:"font-family,italic,|,undo,redo"});
		
		
		$('#mensagemEmailCobranca', this.workspace).wysiwyg({
            initialContent: function() {return "<p><br></p>";},
            controls: "bold,italic,underline,|,undo,redo"
		});
		
		$("#numeroDiasNovaCobranca", this.workspace).numeric();
		
		var options = {
			success: parametrosDistribuidorController.tratarRespostaSalvarLogo,
	    };
		
		$('#formParamentrosDistribuidor', this.workspace).ajaxForm(options);
		
		$("input[id^='reutilizacaoCodigoCotaInativa']", this.workspace).numeric();

		$("input[id^='capacidadeManuseioHomemHoraLancamento']", this.workspace).maskMoney({
			 thousands:'', 
			 decimal:'', 
			 precision:0
		});

		$("input[id^='capacidadeManuseioHomemHoraRecolhimento']", this.workspace).maskMoney({
			 thousands:'', 
			 decimal:'', 
			 precision:0
		});

		$("input[id^='prazoContrato']", this.workspace).maskMoney({
			 thousands:'', 
			 decimal:'', 
			 precision:0
		});

		$("#validadeCaucaoLiquida", this.workspace).maskMoney({
			 thousands:'', 
			 decimal:'', 
			 precision:0
		});
		
		$("#validadeAntecedenciaValidade", this.workspace).maskMoney({
			 thousands:'', 
			 decimal:'', 
			 precision:0
		});
		
		$("#validadeFiador", this.workspace).maskMoney({
			 thousands:'', 
			 decimal:'', 
			 precision:0
		});
		
		$("#validadeImovel", this.workspace).maskMoney({
			 thousands:'', 
			 decimal:'', 
			 precision:0
		});
		
		$("#validadeChequeCaucao", this.workspace).maskMoney({
			 thousands:'', 
			 decimal:'', 
			 precision:0
		});
		
		$("#validadeNotaPromissoria", this.workspace).maskMoney({
			 thousands:'', 
			 decimal:'', 
			 precision:0
		});
		
		$("#validadeOutros", this.workspace).maskMoney({
			 thousands:'', 
			 decimal:'', 
			 precision:0
		});
		
		$("input[id^='sugereSuspensaoQuandoAtingirBoletos']", this.workspace).maskMoney({
			 thousands:'.', 
			 decimal:',', 
			 precision:0
		});	
		
		$("input[id^='sugereSuspensaoQuandoAtingirReais']", this.workspace).maskMoney({
			 thousands:'.', 
			 decimal:',', 
			 precision:2
		});

		$("input[id^='negociacaoAteParcelas']", this.workspace).maskMoney({
			 thousands:'.', 
			 decimal:',', 
			 precision:0
		});
		
		$("input[id^='prazoAvisoPrevioValidadeGarantia']", this.workspace).maskMoney({
			 thousands:'.', 
			 decimal:',', 
			 precision:0
		});	
		
		$("input[id^='prazoFollowUp']", this.workspace).maskMoney({
			 thousands:'.', 
			 decimal:',', 
			 precision:0
		});	
		
		$("#chamadaoValorConsignado", this.workspace).maskMoney({
			 thousands:'.', 
			 decimal:',' 
		});
		
		$("#chamadaoDiasSuspensao", this.workspace).maskMoney({
			 thousands:'', 
			 decimal:'', 
			 precision:0
		});
		
		$("#percentualTaxaExtra", this.workspace).maskMoney({
			thousands:'', 
			 decimal:',', 
			 precision:2
		});
		
		$("#tipoEndereco", this.workspace).val($("#tipoEnderecoHidden").val());
		$("#pararAcumuloDividas", this.workspace).val($("#pararAcumuloDividasHidden").val());
		
		var hasLogotipo = $("#hasLogotipoHidden").val();
		
		if (eval(hasLogotipo)) {
			
			parametrosDistribuidorController.atualizarLogo();
		}
		
		$("#relancamentoParciaisEmDias", this.workspace).val($("#relancamentoParciaisEmDias").val());
		
		//$('input:radio[name=interfaceLED][value=' + $("#impressaoInterfaceLEDHidden").val() + ']', this.workspace).click();
		$('input:radio[name=impressaoNECADANFE][value=' + $("#impressaoNECADANFEHidden").val() + ']', this.workspace).click();
		$('input:radio[name=impressaoCE][value=' + $("#impressaoCEHidden").val() + ']', this.workspace).click();
		
		parametrosDistribuidorController.utilizaContratoCotasListener();
		
		$("#tabDistribuidor", this.workspace).tabs({
			show: function(event, ui) {
				var btnSalvar = $('#btnSalvar', this.workspace);
				if(ui.index == 2) {
					btnSalvar.hide();
				} else {
					btnSalvar.show();
				}
			}
		});
		
		parametrosDistribuidorController.alternarControleAprovacao();
		
		if ($("#_parametrosDistribuidorimpressaoNECADANFE", this.workspace).val() == "MODELO_1") {
			
			$("#impressaoNECADANFEMODELO1", this.workspace).check();
		} else if ($("#_parametrosDistribuidorimpressaoNECADANFE", this.workspace).val() == "MODELO_2") {
			
			$("#impressaoNECADANFEMODELO2", this.workspace).check();
		} else {
			
			$("#impressaoNECADANFE", this.workspace).check();
		}
		
		
		if ($("#_parametrosDistribuidorimpressaoCE", this.workspace).val() == "MODELO_1") {
			
			$("#impressaoCEModelo1", this.workspace).check();
		} else {
			
			$("#impressaoCEModelo2", this.workspace).check();
		}
		
		if ($("#_parametrosDistribuidorimpressaoInterfaceLED", this.workspace).val() == "MODELO_1") {
			
			$("#interfaceLEDMODELO1", this.workspace).check();
		} else if ($("#_parametrosDistribuidorimpressaoInterfaceLED", this.workspace).val() == "MODELO_2") {
			
			$("#interfaceLEDMODELO2", this.workspace).check();
		} else if ($("#_parametrosDistribuidorimpressaoInterfaceLED", this.workspace).val() == "MODELO_3") {
			
			$("#interfaceLEDMODELO3", this.workspace).check();
		}else{
			$("#interfaceLEDMODELO4", this.workspace).check();
		}
		
		$("#cnpj", this.workspace).mask("99.999.999/9999-99");
		
		$("#numero", this.workspace).numeric();
		
		parametrosDistribuidorController.exibirProcuracao();
		
		parametrosDistribuidorController.exibirAdesao();

		parametrosDistribuidorController.exibirContratoCota();
		
		parametrosDistribuidorController.desabilitarItens();
		
		$("#numeroDDD", this.workspace).numeric();
		$("#numeroTelefone", this.workspace).numeric();
		
		
		// Configs iniciais da aba Distribuição - F2
		$('#listClassificacaoCota0\\.valorDe', this.workspace)
		.add('#listClassificacaoCota0\\.valorAte', this.workspace)
		.add('#listClassificacaoCota1\\.valorDe', this.workspace)
		.add('#listClassificacaoCota1\\.valorAte', this.workspace)
		.add('#listClassificacaoCota2\\.valorDe', this.workspace)
		.add('#listClassificacaoCota2\\.valorAte', this.workspace)
		.add('#listClassificacaoCota3\\.valorDe', this.workspace)
		.add('#listClassificacaoCota3\\.valorAte', this.workspace)
		.add('#listClassificacaoCota4\\.valorDe', this.workspace)
		.add('#listClassificacaoCota4\\.valorAte', this.workspace)
		.on('blur', function() {
			$(this).formatCurrency({
				symbol: 'R$ ',
				decimalSymbol: ',',
				digitGroupSymbol: '.'
			});
			
		})
		.formatCurrency({
			symbol: 'R$ ',
			decimalSymbol: ',',
			digitGroupSymbol: '.'
		});
		
		if ($('#listClassificacaoCota4\\.valorDe', this.workspace).val() === '') {
			$('#listClassificacaoCota4\\.valorDe', this.workspace).val('0.00').formatCurrency({
				symbol: 'R$ ',
				decimalSymbol: ',',
				digitGroupSymbol: '.'
			});
		}
		
		$('#listClassificacaoCota1\\.valorAte', this.workspace)
		.on('blur', function(){
			if (this.value) {
				$('#listClassificacaoCota0\\.valorAte', this.workspace)
				.val(this.value)
				.formatCurrency({
					symbol: 'R$ ',
					decimalSymbol: ',',
					digitGroupSymbol: '.'
				});
			}
		});
		
		$('#listClassificacaoCota2\\.valorAte', this.workspace)
		.add('#listClassificacaoCota3\\.valorAte', this.workspace)
		.add('#listClassificacaoCota4\\.valorAte', this.workspace)
		.on('blur', function(){
			if (this.value) {
				var intId = parseInt(this.id.match(/\d/), 10);
				var intIdValorDe = intId - 1;
				var campoValorDe = $('#listClassificacaoCota' + intIdValorDe + '\\.valorDe');
				var numValorAte = parseFloat(this.value.replace(/\D/g,'')) / 100;
				campoValorDe.val((numValorAte + 0.01).toFixed(2)).formatCurrency({
					symbol: 'R$ ',
					decimalSymbol: ',',
					digitGroupSymbol: '.'
				});
				if (intId === 2) {
					$('#listClassificacaoCota0\\.valorDe').val(campoValorDe.val());
				}
			}
		});
		
		$('#vendaMediaMais', this.workspace)
		.add('#percentualMaximoFixacao', this.workspace)
		.add('#listPercentualExcedente0\\.venda', this.workspace)
		.add('#listPercentualExcedente0\\.pdv', this.workspace)
		.add('#listPercentualExcedente1\\.venda', this.workspace)
		.add('#listPercentualExcedente1\\.pdv', this.workspace)
		.add('#listPercentualExcedente2\\.venda', this.workspace)
		.add('#listPercentualExcedente2\\.pdv', this.workspace)
		.numeric();
		
		parametrosDistribuidorController.obterTributosPeloRegimeTributario();
		// fim F2
		
		parametrosDistribuidorController.exibe_form_suspencao_distribuidor($('#sugereSuspensaoDistribuidor', this.workspace).is(':checked'));
	},
	
	desabilitarItens : function() {


		$("#tabDistribuidor", this.workspace).tabs( "option", "disabled", [8] );
		$("#chamadaoDiasSuspensao", this.workspace).disable();
		$("#chamadaoValorConsignado", this.workspace).disable();

//		$("#tabDistribuidor", this.workspace).tabs( "option", "disabled", [7] );
//		$("#chamadaoDiasSuspensao", this.workspace).disable();
//		$("#chamadaoValorConsignado", this.workspace).disable();

		
		//$("#utilizaDesconto", this.workspace).disable();
		//$("#tab-negociacao-percentualDesconto", this.workspace).disable();
		//$("#parcelamentoDividas", this.workspace).disable();
		//$("#negociacaoAteParcelas", this.workspace).disable();
	},
	
	dialogConfirmarGrupo: function() {
		 
		$("#dialog-salvar", this.workspace).dialog({
			resizable: false,
			height:'auto',
			width:350,
			modal: true,
			buttons: {
				"Confirmar": function() {
					
					OD.confirmarGrupo();
				},
				"Cancelar": function() {
					$(this).dialog( "close" );
				}
			},
			beforeClose: function() {
				
				$("#nomeDiferenca", this.workspace).val("");
				$("#diaSemana", this.workspace).val("");
			},
		});
	 },
	 
	 exibirProcuracao:function(){
		
		 if($('#utilizaProcuracaoEntregadores', this.workspace).is(':checked')){
			 $(".exibirProcuracao").show();
		 }else{
			 $(".exibirProcuracao").hide();
		 }
	 },
	 
	 
	 exibirAdesao:function(){
		
		 if($('#utilizaTermoAdesaoEntregaBancas', this.workspace).is(':checked')){
			 $(".exibirAdesao").show();
		 }else{
			 $(".exibirAdesao").hide();
		 }
	 },
	 
	 exibirContratoCota:function(){
		
		 if($('#utilizaContratoComCotas', this.workspace).is(':checked')){
			 $(".exibirContratoComCotas").show();
		 }else{
			 $(".exibirContratoComCotas").hide();
		 }
	 },
	 
	 mensagemControleAprovacao:function(check){
		 
		 if (check==false){
			 
			 $.postJSON(parametrosDistribuidorController.path + "mensagemControleAprovacao",null,
					   function (result) {
				 
							exibirMensagem(result.tipoMensagem, result.listaMensagens);
					   });
		 }
	 },
	 
	 obterTributosPeloRegimeTributario : function() {
		 
		 var params = [];
		 
		 $('#regimeTributarioTributos').html('');
		 
		 params.push({name: 'regimeTributarioId', value: $('#regimeTributario', this.workspace).val()});
		 $.postJSON(this.path + "obterTributosPeloRegimeTributario", params,
		 function (result) {
	 
			 var impTR = $('<tr>');
			 $.each(result, function(key, value) {

				 var tributo = '';
				 var input = '';
				 
				 $.each(value, function(k, v) {

					 var impTD = $('<td>');
					 if(k == 'tributo') {
						 tributo = value[k];
					 }
					 
					 switch(k) {
					 
					 	case 'valor':
					 		input = $('<input>').attr({
							    type: 'text',
							    id: k + tributo,
							    name: tributo,
							    style: 'width: 30px',
							    value: floatToPrice(value[k])
					 		});
					 		break;
					 	
					 	case 'id':
					 	case 'tributoId':
					 		input = $('<input>').attr({
							    type: 'hidden',
							    id: k + value['tributo'],
							    name: k + value['tributo'],
							    value: value[k]
					 		});
					 		break;
					 		
					 	case 'tributoDescricao':
					 		input = value[k] +':';
					 		break;
					 		
					 	case 'tipoAliquota':
					 		tipoAliquota = (value[k] == "Percentual") ? '%' : '$';
					 		input = '';
					 		break;
					 	
					 	default:
					 		input = '';
					 }
					 
					 if(input != '') {
						 impTD.append(input);
						 impTR.append(impTD);
					 }
					 
				 });
				 
				 impTD = $('<td>');
				 impTD.append(tipoAliquota);
				 impTR.append(impTD);
				 
			 });
			 $('#regimeTributarioTributos').append(impTR);
			 
		 });
		 
		 setTimeout(function() {
			 $.each($('#regimeTributarioTributos input[type=text]'), function(key, value) {
				 $("#valor"+ value.name, this.workspace).maskMoney({
					 thousands:'.', 
					 decimal:',' 
				 }); 
			 });
		 }, 100);
		 
	 },

	 exibe_form_suspencao_distribuidor : function(exibir) {
			
		if(exibir){
			
			$(".form-suspensao-hidden-class").show();
		}	
		else{
			
			$(".form-suspensao-hidden-class").hide();
			
			$("#sugereSuspensaoQuandoAtingirBoletos", this.workspace).val("");

        	$("#sugereSuspensaoQuandoAtingirReais", this.workspace).val("");
		}	
	}
	 
}, BaseController);

//@ sourceURL=parametrosDistribuidor.js

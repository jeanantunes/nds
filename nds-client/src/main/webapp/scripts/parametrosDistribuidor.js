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
		$.postJSON(
				parametrosDistribuidorController.path + "recarregarDiasDistribuidorFornecedorGrid", 
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
				$.each(resultado.rows, function(index, row) {
					newTable += '<tr class="class_linha_1"><td width="139">' + 
					row.cell.fornecedor.juridica.nomeFantasia + '</td><td width="144" align="center">' + 
					row.cell.diasLancamento + '</td><td width="125" align="center">' + 
					row.cell.diasRecolhimento + 
					'</td><td width="28" align="center"><a href="javascript:;" onclick="parametrosDistribuidorController.excluirDiasDistribuidorFornecedor(' + row.cell.fornecedor.id + ')" ><img src="'+ contextPath +'/images/ico_excluir.gif" width="15" height="15" alt="Excluir" /></a></td></tr>';
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
			{name:'parametrosDistribuidor.cnpjPrincipal', value: $('#cnpjPrincipal', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.email', value: $('#email', this.workspace).val()},
			{name:'parametrosDistribuidor.codigoDistribuidorDinap', value: $('#codigoDistribuidorDinap', this.workspace).val()},
			{name:'parametrosDistribuidor.codigoDistribuidorFC', value: $('#codigoDistribuidorFC', this.workspace).val()},
			
			{name:'parametrosDistribuidor.endereco.tipoEndereco', value: $('#tipoEndereco', this.workspace).val()},
			{name:'parametrosDistribuidor.endereco.cep', value: $('#cep', this.workspace).val()},
			{name:'parametrosDistribuidor.endereco.tipoLogradouro', value: $('#tipoLogradouro', this.workspace).val()},
			{name:'parametrosDistribuidor.endereco.logradouro', value: $('#logradouro', this.workspace).val()},
			{name:'parametrosDistribuidor.endereco.numero', value: $('#numero', this.workspace).val()},
			{name:'parametrosDistribuidor.endereco.complemento', value: $('#complemento', this.workspace).val()},
			{name:'parametrosDistribuidor.endereco.bairro', value: $('#bairro', this.workspace).val()},
			{name:'parametrosDistribuidor.endereco.localidade', value: $('#cidade', this.workspace).val()},
			{name:'parametrosDistribuidor.endereco.uf', value: $('#uf', this.workspace).val()},
			
			{name:'parametrosDistribuidor.regimeTributario', value: $('#regimeTributario', this.workspace).val()},
			{name:'parametrosDistribuidor.obrigacaoFiscal', value: $('#obrigacaoFiscal', this.workspace).val()},
			{name:'parametrosDistribuidor.regimeEspecial', value: $('#regimeEspecial', this.workspace).is(':checked')},
			
			{name:'parametrosDistribuidor.relancamentoParciaisEmDias', value: $('#relancamentoParciaisEmDias', this.workspace).val()},
			{name:'parametrosDistribuidor.aceitaEncalheJuramentada', value: $('#aceitaEncalheJuramentada', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.tipoContabilizacaoCE', value: $('input[name=parametrosDistribuidor.radioTipoContabilizacaoCE]:checked', this.workspace).val()},
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
			{name:'parametrosDistribuidor.impressaoNECADANFE', value: $("input[name='impressaoNECADANFE']:checked", this.workspace).val()},
			{name:'parametrosDistribuidor.impressaoCE', value: $("input[name='impressaoCE']:checked", this.workspace).val()},
			{name:'parametrosDistribuidor.utilizaContratoComCotas', value: $('#utilizaContratoComCotas', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.prazoContrato', value: $('#prazoContrato', this.workspace).val()},
			{name:'parametrosDistribuidor.informacoesComplementaresContrato', value: $('#informacoesComplementaresContrato', this.workspace).val()},
			{name:'parametrosDistribuidor.utilizaProcuracaoEntregadores', value: $('#utilizaProcuracaoEntregadores', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.informacoesComplementaresProcuracao', value: $('#informacoesComplementaresProcuracao', this.workspace).val()},
			{name:'parametrosDistribuidor.utilizaTermoAdesaoEntregaBancas', value: $('#utilizaTermoAdesaoEntregaBancas', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.complementoTermoAdesaoEntregaBancas', value: $('#complementoTermoAdesaoEntregaBancas', this.workspace).val()},
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
			{name:'parametrosDistribuidor.utilizaAntecedenciaValidade', value: $('#utilizaAntecedenciaValidade', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.validadeAntecedenciaValidade', value: $('#validadeAntecedenciaValidade', this.workspace).val()},
			{name:'parametrosDistribuidor.utilizaOutros', value: $('#utilizaOutros', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.validadeOutros', value: $('#validadeOutros', this.workspace).val()},
			{name:'parametrosDistribuidor.sugereSuspensaoQuandoAtingirBoletos', value: $('#sugereSuspensaoQuandoAtingirBoletos', this.workspace).val()},
			{name:'parametrosDistribuidor.sugereSuspensaoQuandoAtingirReais', value: $('#sugereSuspensaoQuandoAtingirReais', this.workspace).val()},
			{name:'parametrosDistribuidor.parcelamentoDividas', value: $('#parcelamentoDividas', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.negociacaoAteParcelas', value: $('#negociacaoAteParcelas', this.workspace).val()},		
			{name:'parametrosDistribuidor.utilizaDesconto', value: $('#utilizaDesconto', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.percentualDesconto', value: $('#percentualDesconto', this.workspace).val()},		
			{name:'parametrosDistribuidor.utilizaControleAprovacao', value: $('#utilizaControleAprovacao', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.paraDebitosCreditos', value: $('#paraDebitosCreditos', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.negociacao', value: $('#negociacao', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.ajusteEstoque', value: $('#ajusteEstoque', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.postergacaoCobranca', value: $('#postergacaoCobranca', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.devolucaoFornecedor', value: $('#devolucaoFornecedor', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.recibo', value: $('#recibo', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.faltasSobras', value: $('#faltasSobras', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.aprovacaoFaltaDe', value: $('#aprovacaoFaltaDe', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.aprovacaoSobraDe', value: $('#aprovacaoSobraDe', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.aprovacaoFaltaEm', value: $('#aprovacaoFaltaEm', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.aprovacaoSobraEm', value: $('#aprovacaoSobraEm', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.prazoFollowUp', value: $('#prazoFollowUp', this.workspace).val()},
			{name:'parametrosDistribuidor.prazoFollowUpFaltaDe', value: $('#prazoFollowUpFaltaDe', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.prazoFollowUpSobraDe', value: $('#prazoFollowUpSobraDe', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.prazoFollowUpFaltaEm', value: $('#prazoFollowUpFaltaEm', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.prazoFollowUpSobraEm', value: $('#prazoFollowUpSobraEm', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.prazoAvisoPrevioValidadeGarantia', value: $('#prazoAvisoPrevioValidadeGarantia', this.workspace).val()},
			{name:'parametrosDistribuidor.prazoAvisoPrevioValidadeGarantiaFaltaDe', value: $('#prazoAvisoPrevioValidadeGarantiaFaltaDe', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.prazoAvisoPrevioValidadeGarantiaSobraDe', value: $('#prazoAvisoPrevioValidadeGarantiaSobraDe', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.prazoAvisoPrevioValidadeGarantiaFaltaEm', value: $('#prazoAvisoPrevioValidadeGarantiaFaltaEm', this.workspace).is(':checked')},
			{name:'parametrosDistribuidor.prazoAvisoPrevioValidadeGarantiaSobraEm', value: $('#prazoAvisoPrevioValidadeGarantiaSobraEm', this.workspace).is(':checked')}
		];
		
		$.postJSON(parametrosDistribuidorController.path + "gravar",
				   data,
				   function (resultado) {
					   exibirMensagem(resultado.tipoMensagem, 
					   				  resultado.listaMensagens);
					   parametrosDistribuidorController.recarregarDiasDistribuidorFornecedorGrid();
				   });
	},

	atualizarLogo: function() {
		
		$("#imgLogo", this.workspace).attr("src", parametrosDistribuidorController.path + "atualizarLogo?timestamp=" + new Date().getTime());
	},

	salvarLogo: function() {
		
		$('#formParamentrosDistribuidor', this.workspace).submit();
	},

	tratarRespostaSalvarLogo: function() {

		parametrosDistribuidorController.atualizarLogo();
	},

	popup_confirm: function() {
		
		$("#dialog-confirm", this.workspace).dialog({
			resizable: false,
			height:160,
			width:400,
			modal: true,
			buttons: {
				"Confirmar": function() {
					parametrosDistribuidorController.gravar();
					$(this).dialog("close");
				},
				"Cancelar": function() {
					$(this).dialog("close");
				}
			}
		});
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
			}
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
	},

	utilizaProcuracaoEntregadoresListener: function() {
		if (!$('#utilizaProcuracaoEntregadores', this.workspace).is(':checked')) {
			$('#informacoesComplementaresProcuracao', this.workspace).wysiwyg('clear');
		} 
	},

	utilizaTermoAdesaoListener: function() {
		if (!$('#utilizaTermoAdesaoEntregaBancas', this.workspace).is(':checked')) {
			$('#complementoTermoAdesaoEntregaBancas', this.workspace).wysiwyg('clear');
		} 
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
			
			$('#utilizaAntecedenciaValidade', this.workspace).uncheck();
			$('#validadeAntecedenciaValidade', this.workspace).val('').disable();
			
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
	
	init: function(){
		
		$('#informacoesComplementaresContrato', this.workspace).wysiwyg();
		$('#informacoesComplementaresContrato', this.workspace).wysiwyg({controls:"font-family,italic,|,undo,redo"});
		$('#informacoesComplementaresContrato', this.workspace).wysiwyg('setContent','${parametrosDistribuidor.informacoesComplementaresContrato}');
		$('#informacoesComplementaresProcuracao', this.workspace).wysiwyg();
		$('#informacoesComplementaresProcuracao', this.workspace).wysiwyg({controls:"font-family,italic,|,undo,redo"});
		$('#informacoesComplementaresProcuracao', this.workspace).wysiwyg('setContent','${parametrosDistribuidor.informacoesComplementaresProcuracao}');
		$('#complementoTermoAdesaoEntregaBancas', this.workspace).wysiwyg();
		$('#complementoTermoAdesaoEntregaBancas', this.workspace).wysiwyg({controls:"font-family,italic,|,undo,redo"});
		$('#complementoTermoAdesaoEntregaBancas', this.workspace).wysiwyg('setContent','${parametrosDistribuidor.complementoTermoAdesaoEntregaBancas}');
		
		var options = {
			success: parametrosDistribuidorController.tratarRespostaSalvarLogo,
	    };
		
		$('#formParamentrosDistribuidor', this.workspace).ajaxForm(options);
		
		$("input[id^='reutilizacaoCodigoCotaInativa']", this.workspace).maskMoney({
			 thousands:'', 
			 decimal:'', 
			 precision:0
		});

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
			 decimal:',', 
		});
		
		$("#chamadaoDiasSuspensao", this.workspace).maskMoney({
			 thousands:'', 
			 decimal:'', 
			 precision:0
		});
		
		$("#tipoEndereco", this.workspace).val("${parametrosDistribuidor.endereco.tipoEndereco}");
		$("#regimeTributario", this.workspace).val("${parametrosDistribuidor.regimeTributario}");
		$("#obrigacaoFiscal", this.workspace).val("${parametrosDistribuidor.obrigacaoFiscal}");
		
		$("#relancamentoParciaisEmDias", this.workspace).val('${parametrosDistribuidor.relancamentoParciaisEmDias}');
		
		$('input:radio[name=interfaceLED][value=${parametrosDistribuidor.impressaoInterfaceLED}]', this.workspace).click();
		$('input:radio[name=impressaoNECADANFE][value=${parametrosDistribuidor.impressaoNECADANFE}]', this.workspace).click();
		$('input:radio[name=impressaoCE][value=${parametrosDistribuidor.impressaoCE}]', this.workspace).click();
		
		parametrosDistribuidorController.utilizaContratoCotasListener();
		
		$("#tabDistribuidor", this.workspace).tabs();
		
		parametrosDistribuidorController.alternarControleAprovacao();
		
		if ($("#_parametrosDistribuidorimpressaoNECADANFE", this.workspace).val() == "MODELO_1"){
			
			$("#impressaoNECADANFEMODELO1", this.workspace).check();
		} else if ($("#_parametrosDistribuidorimpressaoNECADANFE", this.workspace).val() == "MODELO_2"){
			
			$("#impressaoNECADANFEMODELO2", this.workspace).check();
		} else {
			
			$("#impressaoNECADANFE", this.workspace).check();
		}
		
		
		if ($("#_parametrosDistribuidorimpressaoCE", this.workspace).val() == "MODELO_1"){
			
			$("#impressaoCEModelo1", this.workspace).check();
		} else {
			
			$("#impressaoCEModelo2", this.workspace).check();
		}
		
		if ($("#_parametrosDistribuidorimpressaoInterfaceLED", this.workspace).val() == "MODELO_1"){
			
			$("#interfaceLEDMODELO1", this.workspace).check();
		} else if ($("#_parametrosDistribuidorimpressaoInterfaceLED", this.workspace).val() == "MODELO_2"){
			
			$("#interfaceLEDMODELO2", this.workspace).check();
		} else {
			
			$("#interfaceLEDMODELO3", this.workspace).check();
		}
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
			}
		});
	 }
}, BaseController);

$(function() {
	
	parametrosDistribuidorController.init();
});
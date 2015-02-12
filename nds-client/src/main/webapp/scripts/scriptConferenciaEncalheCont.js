var ConferenciaEncalheCont = $.extend(true, {
	
	processandoConferenciaEncalhe: false,
	
	modalAberta: false,
	
	idProdutoEdicaoNovoEncalhe: "",
	
	valorAnteriorInput : undefined,
	
	resetValue : true,

	init : function() {
		
		$(".outrosVlrsGrid", ConferenciaEncalheCont.workspace).flexigrid({
			dataType : 'json',
			colModel : [ {
				display : 'Data',
				name : 'dataLancamento',
				width : 100,
				sortable : false,
				align : 'left'
			}, {
				display : 'Tipo de Lançamento',
				name : 'tipoLancamentoDescricao',
				width : 140,
				sortable : false,
				align : 'left'
			},{
				display : 'Observações',
				name : 'observacoes',
				width : 140,
				sortable : true,
				align : 'left'
			}, {
				display : 'Valor R$',
				name : 'valor',
				width : 100,
				sortable : false,
				align : 'right'
			} ],
			width : 540,
			height : 250
		});
		
		//hack para message dialog com tamanho fixo
		$(".message-dialog-encalhe > div", ConferenciaEncalheCont.workspace).css('width', '93%');
		
		$("#contingencia-numeroCota", ConferenciaEncalheCont.workspace).numeric();
		$("#contingencia-numeroCota", ConferenciaEncalheCont.workspace).focus();
		$("#exemplaresNovoEncalhe", ConferenciaEncalheCont.workspace).numeric();
		
		$("#contingencia-vlrCE", ConferenciaEncalheCont.workspace).maskMoney({
			 thousands:'.', 
			 decimal:',', 
			 precision:2
		});
		
		$("#contingencia-valorNotaFiscal", ConferenciaEncalheCont.workspace).maskMoney({
			 thousands:'.', 
			 decimal:',', 
			 precision:2
		});
		
		$("#numNotaFiscal", ConferenciaEncalheCont.workspace).numeric();
		
		$("#chaveAcessoNFE", ConferenciaEncalheCont.workspace).numeric();
		
		$("#qtdCE", ConferenciaEncalheCont.workspace).numeric();
		
		$("#dataNotaFiscal", ConferenciaEncalheCont.workspace).mask("99/99/9999");
		
		$("#contingencia-numeroCota", ConferenciaEncalheCont.workspace).keypress(function(e) {
			
			if(e.keyCode == 13 && !visibleOverlay()) {

				ConferenciaEncalheCont.pesquisarCota();
			}
		});
		
		ConferenciaEncalheCont.inicializarAutoCompleteSugestaoProdutoEdicao();
		
		ConferenciaEncalheCont.criarComboBoxEncalhe();

		ConferenciaEncalheCont.removerAtalhos();
		
		ConferenciaEncalheCont.atribuirAtalhos();
		
		ConferenciaEncalheCont.numeroCotaEditavel(true);
	},

	removerAtalhos: function() {
		
		$(document.body).unbind('keydown.adicionarProduto');
		$(document.body).unbind('keydown.popUpNotaFiscal');
		$(document.body).unbind('keydown.salvarConferencia');
		$(document.body).unbind('keydown.finalizarConferencia');
		
	},
	
	atribuirAtalhos: function(){
		$(document.body).unbind();
		
		var permissaoAlteracao = ($('#permissaoAlteracao',workspace).val()=="true");
		
		$(document.body).bind('keydown.adicionarProduto', jwerty.event('F2',function() {
			if(!permissaoAlteracao){
				exibirAcessoNegado();
				return;
			}
			if (!ConferenciaEncalheCont.modalAberta){
				
				ConferenciaEncalheCont.limparCamposNovoEncalhe();
				ConferenciaEncalheCont.popup_novo_encalhe();
			}
		}));
		
		$(document.body).bind('keydown.popUpNotaFiscal', jwerty.event('F6',function() {
			if(!permissaoAlteracao){
				exibirAcessoNegado();
				return;
			}
			if (!ConferenciaEncalheCont.modalAberta){
				
				ConferenciaEncalheCont.popup_notaFiscal();
			}
			
		}));
		
		
		$(document.body).bind('keydown.salvarConferencia', jwerty.event('F8',function() {
			if(!permissaoAlteracao){
				exibirAcessoNegado();
				return;
			}
			if (!ConferenciaEncalheCont.modalAberta) {
				
				if(document.activeElement != undefined && document.activeElement.id != undefined && document.activeElement.id.indexOf('qtdExemplaresGrid_') > -1) {
					
					ConferenciaEncalheCont.verificarPermissaoSuperVisor($(document.activeElement).attr('elIndex'), true);
					return;
				}
				
				ConferenciaEncalheCont.processandoConferenciaEncalhe = true;
				
				$("#contingencia-numeroCota", ConferenciaEncalheCont.workspace).focus();
				
				setTimeout(function() {
					ConferenciaEncalheCont.atualizarValoresGridInteira(ConferenciaEncalheCont.popup_salvarInfos);
				}, 1000);
				
			}
			
		}));
		
		$(document.body).bind('keydown.finalizarConferencia', jwerty.event('F9',function() {
			if(!permissaoAlteracao){
				exibirAcessoNegado();
				return;
			}
			if (!ConferenciaEncalheCont.modalAberta){

				if(document.activeElement != undefined && document.activeElement.id != undefined && document.activeElement.id.indexOf('qtdExemplaresGrid_') > -1) {
					
					ConferenciaEncalheCont.verificarPermissaoSuperVisor($(document.activeElement).attr('elIndex'), true);
					return;
				}
				
				ConferenciaEncalheCont.processandoConferenciaEncalhe = true;
				
				$("#contingencia-numeroCota", ConferenciaEncalheCont.workspace).focus();
				
				setTimeout(function() {
					ConferenciaEncalheCont.atualizarValoresGridInteira(ConferenciaEncalheCont.verificarCobrancaGerada);
				}, 1000);
			}
			
		}));
		
	},
	
	pesquisarCota : function(){
		
		var numeroCotaDipopnivelPesquisa = $("#contingencia-numeroCota",ConferenciaEncalheCont.workspace).attr('readonly');
		
		if (numeroCotaDipopnivelPesquisa == 'readonly'){
			return;
		}
		
		var numeroCotaEmConferencia = $("#contingencia-numeroCota", ConferenciaEncalheCont.workspace).val();
		
		ConferenciaEncalheCont.limpaTela();
		
		$("#contingencia-numeroCota", ConferenciaEncalheCont.wokspace).val(numeroCotaEmConferencia);
		
		
		var data = [
		            {name: 'numeroCota', value : $("#contingencia-numeroCota", ConferenciaEncalheCont.workspace).val()}, 
		            {name: 'indObtemDadosFromBD', value : true},
		            {name: 'indConferenciaContingencia', value: true}
		           ];
		
		$.postJSON(contextPath + "/devolucao/conferenciaEncalhe/iniciarConferenciaEncalhe", data,
			function(result){
				
				if (typeof result.IND_REABERTURA != 'undefined' && result.IND_REABERTURA == 'S'){
					
					ConferenciaEncalheCont.modalAberta = true;
					
					$("#dialog-reabertura", ConferenciaEncalheCont.workspace).dialog({
						resizable : false,
						height : 200,
						width : 360,
						modal : true,
						buttons : {
							"Sim" : function() {
								
								$("#dialog-reabertura", ConferenciaEncalheCont.workspace).dialog("close");
								
								ConferenciaEncalheCont.carregarListaConferencia(data);
								
								ConferenciaEncalheCont.ifCotaEmiteNfe(data, ConferenciaEncalheCont.popup_alert);
								
								ConferenciaEncalheCont.numeroCotaEditavel(false);
							},
							"Não" : function() {
								
								ConferenciaEncalheCont.removerTravaConferenciaEncalheCotaUsuario();
								
								$("#dialog-reabertura", ConferenciaEncalheCont.workspace).dialog("close");
								
								ConferenciaEncalheCont.numeroCotaEditavel(true);
							}
						}, 
						
						open : function() {
							
							ConferenciaEncalheCont.configurarNavegacaoSetas($('#form-reabertura').find('button'));
							
							setTimeout(function(){
								$($('#form-reabertura').find('button')[0]).focus();
							}, 1);
							
						},
						
						close : function(){
							
							ConferenciaEncalheCont.modalAberta = false;
							
							$("#contingencia-numeroCota", ConferenciaEncalheCont.workspace).focus();
							
							ConferenciaEncalheCont.numeroCotaEditavel(true);
						},
						form: $("#dialog-reabertura", this.workspace).parents("form")
					});
				} else {
					
					if(typeof result.IND_COTA_RECOLHE_NA_DATA != undefined && result.IND_COTA_RECOLHE_NA_DATA == 'N' ) {
						
						exibirMensagem('WARNING', [result.msg]);
						
					} 
					
					ConferenciaEncalheCont.carregarListaConferencia(data);
					
					$("#dialog-reabertura", ConferenciaEncalheCont.workspace).dialog("close");
					ConferenciaEncalheCont.ifCotaEmiteNfe(data, ConferenciaEncalheCont.popup_alert);
					
					ConferenciaEncalheCont.numeroCotaEditavel(true);
				}
			},
			function() {
				
				$("#contingencia-numeroCota", ConferenciaEncalheCont.workspace).val("");
				
				focusSelectRefField($("#contingencia-numeroCota", ConferenciaEncalheCont.workspace));
			}
		);
	},
	ifCotaEmiteNfe :  function(data, fnCotaEmiteNfe) {
		$.postJSON(contextPath + "/devolucao/conferenciaEncalhe/verificarCotaEmiteNFe", data, 
		function(result){
			if(result.IND_COTA_EMITE_NFE) {
				fnCotaEmiteNfe();
			} 
		});
	},
	
	recalcularValoresFinalizar : function(index){
		
		var data = [
            {name: "idConferencia", value: $("#idConferenciaEncalheHiddenFinalizarConf_" + index, ConferenciaEncalheCont.workspace).val()},
            {name: "qtdInformada", value: $('#qtdeInformadaFinalizarConf_' + index, ConferenciaEncalheCont.workspace).val()},
            {name: "valorCapaInformado", value : $('#precoCapaFinalizarConf_' + index, ConferenciaEncalheCont.workspace).val()}
		];
		
		$.postJSON(contextPath + "/devolucao/conferenciaEncalhe/alterarQtdeValorInformado", 
			data, 
			function(result){
				
				$("#valorTotalConferenciaFinalizar_" + index, ConferenciaEncalheCont.workspace).text(parseFloat(result.conf.valorTotal).toFixed(2));
				
				$("#somatorioQtdInformada", ConferenciaEncalheCont.workspace).text(parseFloat(result.qtdInformada).toFixed(2));
				$("#somatorioQtdRecebida", ConferenciaEncalheCont.workspace).text(parseFloat(result.qtdRecebida).toFixed(2));
				$("#somatorioTotal", ConferenciaEncalheCont.workspace).text(parseFloat(result.valorPagarAtualizado).toFixed(2));
			}
		);
	},
	
	criarComboBoxEncalhe : function() {
		
		$.postJSON(contextPath + "/devolucao/conferenciaEncalhe/obterBoxLogado", null,
				
			function(idBoxLogado){
	
				$.postJSON(contextPath + "/devolucao/conferenciaEncalhe/carregarComboBoxEncalheContingencia", null,
						
					function(result){
						
						var opcoesBox = '';
						
						$.each(result, function(key, value) {
							
							opcoesBox = opcoesBox + "<option "+ (idBoxLogado.long==key?" selected ":" ") +" value="+key+">"+value+"</option>"; 
						});
						
						$('#boxLogado', ConferenciaEncalheCont.workspace).html(opcoesBox);
						
						ConferenciaEncalheCont.popup_logado();
						
					}
				);
		    }
		);
		
	},

	popup_logado : function() {
		
		ConferenciaEncalheCont.modalAberta = true;
		
		$("#dialog-logado", ConferenciaEncalheCont.workspace).dialog({
			resizable : false,
			height : 180,
			width : 460,
			modal : true,
			buttons : {
				"Confirmar" : function(e) {
					
					$.postJSON(contextPath + "/devolucao/conferenciaEncalhe/salvarIdBoxSessao", {idBox:$("#boxLogado").val()}, 
						function(){
							
							$("#dialog-logado", ConferenciaEncalheCont.workspace).dialog("close");
							$('#numeroCota', ConferenciaEncalheCont.workspace).focus();
						}, null, true, "idModalBoxRecolhimento"
					);
				},
				"Cancelar" : function(e) {

					var evt = e || window.event;
					evt.preventDefault();
					
					$(this).dialog("close");
					
					$('#pesq_cota', ConferenciaEncalheCont.workspace).focus();
					
					$('#workspace').tabs('remove', $('#workspace').tabs('option','selected'));
					
				}
			}, open : function(){
				
				ConferenciaEncalheCont.configurarNavegacaoSetas($('#form-logado').find('button'));
				
				setTimeout(function(){
					$($('#form-logado').find('button')[0]).focus();
				}, 1);
				
				
			}, close : function(){
				
				ConferenciaEncalheCont.modalAberta = false;
			},
			form: $("#dialog-logado", this.workspace).parents("form")			
		});
	},
	
	gerarDocumentosConferenciaEncalhe : function(tiposDocumento) {
		var cont = 1;

		//Imprime todos os documentos recebidos
		for(var i = 0; i < tiposDocumento.length; i++) {
			
			var data = [{name: 'tipo_documento_impressao_encalhe', value: tiposDocumento[i]}];
			$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/imprimirDocumentosCobranca', 
					data,
					function(resultado){
				
				if(resultado != "" && resultado.resultado!=""){
					
					var callApplet = '';
					callApplet+='<applet archive="scripts/applet/ImpressaoFinalizacaoEncalheApplet.jar" code="br.com.abril.nds.matricial.ImpressaoFinalizacaoEncalheApplet.class" width="10" height="10">';
					callApplet+='	<param name="tipo_documento_impressao_encalhe" value="'+resultado.tipo_documento_impressao_encalhe+'">';
					callApplet+='	<param name="conteudoImpressao" value="'+resultado.resultado+'">';
					callApplet+='</applet>';						
					
					$('#replaceAppletFinal'+cont).html(callApplet);
					$('#idImpressaoFinalizacaoApplet'+cont, ConferenciaEncalheCont.workspace).show();		
					
					$('#replaceAppletFinal'+cont).html("");
					$('#idImpressaoFinalizacaoApplet'+cont, ConferenciaEncalheCont.workspace).hide();		
					cont++;
				}
			}
			); 	
		}
	},
	
	verificarValorTotalNotaFiscal : function() {
		var data = [{name: 'indConferenciaContingencia', value: true}];
		
		$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/verificarValorTotalNotaFiscal', data,
				
				function(result){
				
					if(result.tipoMensagem == 'SUCCESS') {
						
						if(result.indGeraDocumentoConfEncalheCota == true) {

							ConferenciaEncalheCont.gerarDocumentosConferenciaEncalhe(result.tipos_documento_impressao_encalhe);
							
						}
						
						ConferenciaEncalheCont.limpaTela();
						
					} else {

						$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/carregarListaConferencia', 
							null, 
							function(result){
								
								ConferenciaEncalheCont.abrirDialogNotaFiscalDivergente(result);
							}
						);
					}
				
					exibirMensagem(result.tipoMensagem, result.listaMensagens);
				}, null , true, "idModalDadosNotaFiscal"
		);
		
	},
	
	verificarValorTotalCE : function() {
		
		var data = [{name: "valorCEInformado", value: parseFloat($("#contingencia-vlrCE", ConferenciaEncalheCont.workspace).val())},
		            {name: "qtdCEInformado", value: parseFloat($("#qtdCE", ConferenciaEncalheCont.workspace).val())}];
		
		$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/verificarValorTotalCE', data, 
		
			function(conteudo) {
				
				if(conteudo.valorCEInformadoValido == true) {
					
					ConferenciaEncalheCont.verificarValorTotalNotaFiscal();
					
				} else {
					
					$("#msgConfirmar", ConferenciaEncalheCont.wokspace).text(conteudo.mensagemConfirmacao);
					
					$("#dialog-confirmar", ConferenciaEncalheCont.workspace).dialog({
						resizable : false,
						height : 160,
						width : 400,
						modal : true,
						buttons : {
							"Sim" : function() {
								
								ConferenciaEncalheCont.verificarValorTotalNotaFiscal();
								$("#dialog-confirmar", ConferenciaEncalheCont.workspace).dialog("close");
							},
							"Não" : function() {
								
								$("#dialog-confirmar", ConferenciaEncalheCont.workpace).dialog("close");
							}
						},
						
						open : function() {
							
							ConferenciaEncalheCont.configurarNavegacaoSetas($('#form-confirmar').find('button'));
							
							setTimeout(function(){
								$($('#form-confirmar').find('button')[0]).focus();
							}, 1);
							
						},
						
						form: $("#dialog-confirmar", ConferenciaEncalheCont.workspace).parents("form")
					});
				}
			
			}
		);
	},
	
	removerTravaConferenciaEncalheCotaUsuario : function() {
		
		$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/removerTravaConferenciaEncalheCotaUsuario');
		
	},
	
	carregarListaConferencia: function(data){
		
		$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/carregarListaConferencia', 
				data, 
				function(result){
					
					ConferenciaEncalheCont.preProcessarConsultaConferenciaEncalhe(result);
					
					bloquearItensEdicao(ConferenciaEncalheCont.workspace);
				}
		);
	},
	indDistribuidorAceitaJuramentado:null,
	preProcessarConsultaConferenciaEncalhe : function(result){
		
		if (result.mensagens){
			
			exibirMensagem(result.mensagens.tipoMensagem, result.mensagens.listaMensagens);
			return;
		}
		
		var innerTable = '';
		
		var modeloConferenciaEncalhe = result.listaConferenciaEncalhe;
		
		var totalExemplaresFooter = 0;
		
		var cotaAVista = result.cotaAVista;
		
		ConferenciaEncalheCont.indDistribuidorAceitaJuramentado = result.indDistribuidorAceitaJuramentado;
		
		$("._dadosConfEncalhe", ConferenciaEncalheCont.workspace).remove();
		
		if (modeloConferenciaEncalhe){
		
			$.each(modeloConferenciaEncalhe, 
				function(index, value) {
				
					var parcialNaoFinal =  value.parcialNaoFinal;
				
					var _class;
					
					if (index % 2 == 0){
						_class = "class_linha_1 _dadosConfEncalhe";
					} else {
						_class = "class_linha_2 _dadosConfEncalhe";
					}
					
					innerTable += "<tr class='" + _class + " _dados' name='linhaConfEncalhe'>";
					
					innerTable += "<input type='hidden' id='idProdutoEdicaoGrid_"+index+"' value='" + value.idProdutoEdicao + "'/>";
					
					innerTable += "<td nowrap='nowrap'>" + value.codigo + "</td>";
					
					innerTable += "<td nowrap='nowrap'>" + value.nomeProduto + "</td>";
					
					innerTable += "<td nowrap='nowrap' style='text-align: center;'>" + value.numeroEdicao + "</td>";
					
					if (value.dia || value.dataRecolhimento){
						
						if (value.dia && value.dia > 0){
						
							innerTable += "<td style='text-align: center;' nowrap='nowrap'>" + value.dia + "º" + "</td>";
						} else {
							
							innerTable += "<td style='text-align: center;' nowrap='nowrap' style='width: 20px;'>" + value.dataRecolhimento + "</td>";
						}
					} else {
						
						innerTable += "<td></td>";
					}
					
					
					innerTable += "<td style='text-align: right;' nowrap='nowrap'>" + parseFloat(value.precoCapa).toFixed(2) + "</td>";
					
					//innerTable += "<td style='text-align: right;' nowrap='nowrap'>" + parseFloat(value.desconto).toFixed(2) + "</td>";
					innerTable += "<td style='text-align: right;' nowrap='nowrap'>" + parseFloat(value.precoComDesconto).toFixed(4) + "</td>";
					
					var valorExemplares = parseInt(value.qtdExemplar);
					
					valorExemplares = isNaN(valorExemplares) ? 0 : valorExemplares;
					
					totalExemplaresFooter += valorExemplares;
					
					innerTable += "<td style='text-align: center;' nowrap='nowrap'>" + value.qtdReparte + "</td>";
					
					innerTable += "<td nowrap='nowrap' style='text-align: center;'>";
					
					var inputExemplares = '<input isEdicao="true" name="inputValorExemplares" tabindex="' + (++index) + 
						'" onkeydown="ConferenciaEncalheCont.nextInputExemplares('+ index +', window.event);" elIndex="'+ index +'" id="qtdExemplaresGrid_'+ index + 
						'" maxlength="255" onkeyup="ConferenciaEncalheCont.redefinirValorTotalExemplaresFooter()" '+
						' onchange="ConferenciaEncalheCont.valorAnteriorInput = this.defaultValue;ConferenciaEncalheCont.verificarPermissaoSuperVisor('+ index +');" style="width:90px; text-align: center;" value="' + valorExemplares + '"/>' +
						'<input id="idConferenciaEncalheHidden_' + index + '" type="hidden" value="' + value.idConferenciaEncalhe + '"/>';
					
					innerTable += inputExemplares + "</td>";
					
					innerTable += "<td align='right' nowrap='nowrap' id='valorTotalConferencia_" + index + "'>" + parseFloat(value.valorTotal).toFixed(4) + "</td>";
					
					var inputCheckBoxJuramentada = '';
					
					if (ConferenciaEncalheCont.indDistribuidorAceitaJuramentado == true) {

						if(parcialNaoFinal == true && !cotaAVista) {
							
							inputCheckBoxJuramentada = '<input isEdicao="true" type="checkbox" ' + (value.juramentada == true ? 'checked="checked"' : '')
							+ ' onchange="ConferenciaEncalheCont.valorAnteriorInput = this.defaultValue;ConferenciaEncalheCont.verificarPermissaoSuperVisor('+ index +');" id="checkGroupJuramentada_' + index + '"/>';
							
							
						} else {
							
							inputCheckBoxJuramentada = '<input isEdicao="true" type="checkbox" disabled="disabled" id="checkGroupJuramentada_' + index + '"/>';
							
						}
						
						innerTable += "<td style='text-align: center;' nowrap='nowrap'>" + inputCheckBoxJuramentada + "</td>";
					
					} 
					
					innerTable += "</tr>";
					
					$(innerTable).appendTo("#dadosGridConferenciaEncalheContingencia", ConferenciaEncalheCont.workspace);
					
					innerTable = '';
				}
			);
			
			if (!ConferenciaEncalheCont.indDistribuidorAceitaJuramentado) {
				
				$("#colunaJuramentada", ConferenciaEncalheCont.workspace).hide();
			}
			
			$("#totalExemplaresFooter", ConferenciaEncalheCont.workspace).html(totalExemplaresFooter);
			
			$('input[id*="qtdExemplaresGrid"]', ConferenciaEncalheCont.workspace).numericPacotePadrao();

		}
		
		ConferenciaEncalheCont.processandoConferenciaEncalhe = false;
		
		$(".outrosVlrsGrid", ConferenciaEncalheCont.workspace).flexAddData({
			page: result.listaDebitoCredito.page, total: result.listaDebitoCredito.total, rows: ConferenciaEncalheCont.formatarDadosDebitoCredito(result.listaDebitoCredito.rows)
		});
		
		$("#totalReparte", ConferenciaEncalheCont.workspace).text(parseFloat(result.reparte).toFixed(4));
		$("#totalEncalhe", ConferenciaEncalheCont.workspace).text(parseFloat(result.valorEncalhe).toFixed(4));
		$("#contingencia-valorVendaDia", ConferenciaEncalheCont.workspace).text(parseFloat(result.valorVendaDia).toFixed(4));
		$("#totalOutrosValores", ConferenciaEncalheCont.workspace).text(parseFloat(result.valorDebitoCredito).toFixed(2));
		$("#contingencia-valorAPagar", ConferenciaEncalheCont.workspace).text(parseFloat(result.valorPagar).toFixed(2));
		
		$(".dadosFiltro", ConferenciaEncalheCont.workspace).show();
		$("#nomeCota", ConferenciaEncalheCont.workspace).text(result.razaoSocial);
		$("#statusCota", ConferenciaEncalheCont.workspace).text(result.situacao);
		
		focusSelectRefField($("[name=inputValorExemplares]", ConferenciaEncalheCont.workspace).first());
	},
	
	formatarDadosDebitoCredito : function(listaDebitoCredito) {
		
		if(listaDebitoCredito){
			
			$.each(listaDebitoCredito, function(index, value){
				
				value.cell.valor = floatToPrice(parseFloat(value.cell.valor).toFixed(4), 4);
				
				var strDataArr;
				if(value.cell.dataLancamento.indexOf('-') > -1) {
					strDataArr = value.cell.dataLancamento.split('-');
				
					value.cell.dataLancamento = strDataArr[2] +'/'+ strDataArr[1] +'/'+ strDataArr[0];
				}
								
			});
		
		}
		
		return listaDebitoCredito;
		
	},
	
	atualizarValoresGridInteira : function(executarPosAtualizacaoGrid) {
		
		var listaItemGrid = $("._dadosConfEncalhe", ConferenciaEncalheCont.workspace);
		
		var data = [];
		
		$(listaItemGrid).each(function(index, element){
			
			var valorIdConferenciaEncalhe = $(element).find("[id^='idConferenciaEncalheHidden_']").val();
			var valorQtdExemplares = $(element).find("[id^='qtdExemplaresGrid_']").val();
			var valorJuramentado = ( $(element).find("[id^='checkGroupJuramentada_']").attr("checked") == 'checked' );
			
			data.push({
				
				idConferenciaEncalhe: valorIdConferenciaEncalhe,
				qtdExemplarDaGrid: valorQtdExemplares,
	            juramentada: valorJuramentado
			
			});
			
			
		});
		
		var param = serializeArrayToPost('listaConferenciaEncalhe', 
				data);
		
		param.indConferenciaContingencia = true;
		
		$.postJSON(contextPath + "/devolucao/conferenciaEncalhe/atualizarValoresGridInteira", 
				param, 
				function(result){
			
					executarPosAtualizacaoGrid();
			
				}, function(){
					
					var data = [
								  {name: 'numeroCota', 			value : $("#contingencia-numeroCota", ConferenciaEncalheCont.workspace).val()}, 
								  {name: 'indObtemDadosFromBD', value : false},
								  {name: 'indConferenciaContingencia', value: false}
								 ];
								
					ConferenciaEncalheCont.carregarListaConferencia(data);				
				}
			
		);
		
		
	},
	
	atualizarValores: function(index, focusOnInput) {
		
		if(ConferenciaEncalheCont.processandoConferenciaEncalhe){
			return;
		}
		
		$("#qtdExemplaresGrid_" + index, ConferenciaEncalheCont.workspace).prop("defaultValue",
				$("#qtdExemplaresGrid_" + index, ConferenciaEncalheCont.workspace).val());
		
		var juramentado = false;
		
		if( $("#checkGroupJuramentada_" + index, ConferenciaEncalheCont.workspace).attr("checked") == 'checked' ) {
			juramentado = true;
		}
		
		var data = [
            {name: "idConferencia", value: $("#idConferenciaEncalheHidden_" + index, ConferenciaEncalheCont.workspace).val()},
            {name: "qtdExemplares", value: $("#qtdExemplaresGrid_" + index, ConferenciaEncalheCont.workspace).val()},
            {name: "juramentada", value: juramentado}, {name: 'indConferenciaContingencia', value: true}
		];
		
		$.postJSON(contextPath + "/devolucao/conferenciaEncalhe/atualizarValores", 
			data, 
			function(result){
			
			    $("#qtdExemplaresGrid_" + index, ConferenciaEncalheCont.workspace).val(result.conf.qtdExemplar);
			
				$("#valorTotalConferencia_" + index, ConferenciaEncalheCont.workspace).text(parseFloat(result.conf.valorTotal).toFixed(4));
				
				$("#totalReparte", ConferenciaEncalheCont.workspace).text(parseFloat(result.reparte).toFixed(4));
				$("#totalEncalhe", ConferenciaEncalheCont.workspace).text(parseFloat(result.valorEncalhe).toFixed(4));
				$("#contingencia-valorVendaDia", ConferenciaEncalheCont.workspace).text(parseFloat(result.valorVendaDia).toFixed(4));
				$("#totalOutrosValores", ConferenciaEncalheCont.workspace).text(parseFloat(result.valorDebitoCredito).toFixed(2));
				$("#contingencia-valorAPagar", ConferenciaEncalheCont.workspace).text(parseFloat(result.valorPagar).toFixed(2));
				
				ConferenciaEncalheCont.numeroCotaEditavel(false);
				
				if(focusOnInput) {
					
					var position = $("#qtdExemplaresGrid_" + index, ConferenciaEncalheCont.workspace).val().length;
					
					setTimeout(function(){
						$("#qtdExemplaresGrid_" + index).focus().selectRange(0, position);
					},1);
					
				}
				
			},
			function(result) {

				if (result.mensagens){

					$("#qtdExemplaresGrid_" + index, ConferenciaEncalheCont.workspace).val(ConferenciaEncalheCont.valorAnteriorInput);
					
					var position = $("#qtdExemplaresGrid_" + index, ConferenciaEncalheCont.workspace).val().length;
					
					setTimeout(function(){
						$("#qtdExemplaresGrid_" + index).focus().selectRange(0, position);
					},1);
					
				}
			}, null, null, false
		);
	},
	
	verificarPermissaoSuperVisor : function(index, salvandoOuFinalizandoConferenciaCont) {
		
		if(ConferenciaEncalheCont.processandoConferenciaEncalhe) {
			return;
		}
		
		var data = [
            {name: "idConferencia", value: $("#idConferenciaEncalheHidden_" + index, ConferenciaEncalheCont.workspace).val()},
            {name: "qtdExemplares", value: $("#qtdExemplaresGrid_" + index, ConferenciaEncalheCont.workspace).val()},
            {name: 'indConferenciaContingencia', value: true}
		];
		
		$.postJSON(contextPath + "/devolucao/conferenciaEncalhe/verificarPermissaoSupervisor", 
			data, 
			function(result) {
				
				if (result && result.result != "") {
					
					ConferenciaEncalheCont.resetValue = true;
					
					if (result[0]){
						
						exibirMensagem('WARNING', [result[1]]);
						ConferenciaEncalheCont.resetValue = false;
						ConferenciaEncalheCont.atualizarValores(index);
						ConferenciaEncalheCont.redefinirValorTotalExemplaresFooter();
					} else {
						
						$("#msgSupervisor", ConferenciaEncalheCont.workspace).text(result[1]);
						
						$("#dialog-autenticar-supervisor", ConferenciaEncalheCont.workspace).dialog({
							resizable: false,
							height:'auto',
							width:400,
							modal: true,
							buttons: {
								"Ok": function() {
									
									ConferenciaEncalheCont.resetValue = false;
									ConferenciaEncalheCont.autenticarSupervisor(index, salvandoOuFinalizandoConferenciaCont);
									
								},
								"Cancelar": function() {
									$("#qtdExemplaresGrid_" + index, ConferenciaEncalheCont.workspace).val(ConferenciaEncalheCont.valorAnteriorInput);
									$(this).dialog("close");
								}
							},
							form: $("#dialog-autenticar-supervisor", this.workspace).parents("form"),
							
							open : function() {
								
								ConferenciaEncalheCont.configurarNavegacaoSetas($('#form-autenticar-supervisor').find('button'));
								
								setTimeout(function(){
									
									$('#inputUsuarioSup', this.workspace).select();
									
								}, 1);
								
							},
							
							close: function(){
								
								if (ConferenciaEncalheCont.resetValue){
									
									$("#qtdExemplaresGrid_" + index, ConferenciaEncalheCont.workspace).val(ConferenciaEncalheCont.valorAnteriorInput);
									
									setTimeout(function(){
										$("#qtdExemplaresGrid_" + index, ConferenciaEncalheCont.workspace).select();
									},1);
									
								}
								
								ConferenciaEncalheCont.redefinirValorTotalExemplaresFooter();
							}
						});
					}
					
				} else {
					ConferenciaEncalheCont.atualizarValores(index);
					ConferenciaEncalheCont.redefinirValorTotalExemplaresFooter();
				}
			}, null, null, null, false
		);
	},
	
	autenticarSupervisor : function(index, callback, paramCallback, keepDialog){
		
		var paramUsuario = {
			usuario:$("#inputUsuarioSup", ConferenciaEncalheCont.workspace).val(),
			senha:$("#inputSenha", ConferenciaEncalheCont.workspace).val()
		};
		
		if (paramUsuario.usuario == '' || paramUsuario.senha == ''){
			
			exibirMensagem(
				'WARNING', 
				['Usuário e senha são obrigatórios.']
			);
			
			ConferenciaEncalheCont.resetValue = true;
			
			return;
		}
		
		$.postJSON(
			contextPath + "/devolucao/conferenciaEncalhe/verificarPermissaoSupervisor", 
			paramUsuario,
			function(result) {
				
				$("#inputUsuarioSup", ConferenciaEncalheCont.workspace).val("");
				$("#inputSenha", ConferenciaEncalheCont.workspace).val("");
				
				if (result.tipoMensagem){
					
					exibirMensagem(
						result.tipoMensagem, 
						result.listaMensagens
					);
					
					ConferenciaEncalheCont.resetValue = true;
					
					return;
				}
				
				if (index) {
					
					ConferenciaEncalheCont.atualizarValores(index, true);
					
					setTimeout(function() {
						ConferenciaEncalheCont.atualizarValoresGridInteira(ConferenciaEncalheCont.popup_salvarInfos);
					}, 1000);
				}
				
				if (callback) {
					callback(paramCallback, keepDialog);
				}
				
				$("#dialog-autenticar-supervisor", ConferenciaEncalheCont.workspace).dialog("close");
				return;
			},
			function (){
				ConferenciaEncalheCont.resetValue = true;
			}
		);
	},
	
	enterOnSupervisorModal : function(event){
		
		if (event && event.keyCode == 13){
			
			$(".ui-dialog-buttonpane button:contains('Ok')", ConferenciaEncalheCont.workspace).click();
		}
	},
	
	numeroCotaEditavel : function(r){
		
		if (r==false){
			
			$("#contingencia-numeroCota",ConferenciaEncalheCont.workspace).attr('readonly', true);
		}
		else{
			
			$("#contingencia-numeroCota",ConferenciaEncalheCont.workspace).attr('readonly', false);
		}
	},
	
	inicializarAutoCompleteSugestaoProdutoEdicao: function(){
		
		var functionBuscaProduto = function(url, term, successCallback) { 
		
			$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/' + url, 
				{codigoNomeProduto: term}, 
				function(result){
						
					successCallback(result);
						
				}, function(result) {
					
					$("#dialog-encalhe input[name!='lstProdutos']", ConferenciaEncalheCont.workspace).val("");

					$("#dialog-encalhe input", ConferenciaEncalheCont.workspace).first().focus();
					
					$("#dialog-encalhe input", ConferenciaEncalheCont.workspace).first().keydown();
					
				}, true, "idModalNovoEncalhe");
		};
		

		$("#lstProdutos", ConferenciaEncalheCont.workspace).autocomplete({
			
			source: function(request, response) {
				
				functionBuscaProduto('autocompletarProdutoPorCodigoNome', request.term, function(result) {
					
					response(result);
				});	
				
			},
			select: function(event, ui){
				
				$.postJSON(contextPath + "/devolucao/conferenciaEncalhe/pesquisarProdutoEdicaoPorId",
					[{name: "idProdutoEdicao", value: ui.item.chave.long}],
					function(result2){
					
						if (result2){
							
							ConferenciaEncalheCont.idProdutoEdicaoNovoEncalhe = ui.item.chave.long;
							
							$("#numEdicaoNovoEncalhe", ConferenciaEncalheCont.workspace).val(result2.numeroEdicao);
							
							$("#precoCapaNovoEncalhe", ConferenciaEncalheCont.workspace).val(parseFloat(result2.precoVenda).toFixed(2));
							
							$("#descontoNovoEncalhe", ConferenciaEncalheCont.workspace).val(parseFloat(result2.desconto).toFixed(4));
							
							$("#exemplaresNovoEncalhe", ConferenciaEncalheCont.workspace).val("");
							
							$("#valorTotalNovoEncalhe", ConferenciaEncalheCont.workspace).val("");

							if(result2.parcial){
								
								$(".isParcial", ConferenciaEncalheCont.workspace).show();
							}
							else{
								
								$(".isParcial", ConferenciaEncalheCont.workspace).hide();
							}
							
							$("#exemplaresNovoEncalhe", ConferenciaEncalheCont.workspace).focus();
						}
							
						
					}, function() {
					
						ConferenciaEncalheCont.limparCamposNovoEncalhe();
						
						$("#lstProdutos", ConferenciaEncalheCont.workspace).focus();
						
						$(".isParcial", ConferenciaEncalheCont.workspace).show();
					}, 
					true, "idModalNovoEncalhe"
				);
			},
			delay : 500
		});	
	},

	adicionarEncalhe: function(keepDialog){	

		var _this = this;
		
		var existeProduto = false;
		
		$("input[id*='idProdutoEdicaoGrid']", ConferenciaEncalheCont.workspace).each(function(){
			
			if (ConferenciaEncalheCont.idProdutoEdicaoNovoEncalhe == $(this).val()) {
				
				exibirMensagem("WARNING", ["Este produto já está sendo utilizado."]);
				
				existeProduto = true;
				
				return false;
			}
		});

		if (existeProduto) {
		
			return;
		}
		
		//$("#qtdExemplaresGrid_" + index, ConferenciaEncalheCont.workspace).select();
		var data = [{name: "produtoEdicaoId", value: ConferenciaEncalheCont.idProdutoEdicaoNovoEncalhe}, 
		            {name: "qtdExemplares", value: $("#exemplaresNovoEncalhe", ConferenciaEncalheCont.workspace).val()},
		            {name: "juramentada", value:$('#checkboxJueramentadaNovoEncalhe', ConferenciaEncalheCont.workspace).attr('checked') == 'checked' },
		            {name: "indConferenciaContingencia", value: true}];
		
		$.postJSON(contextPath + "/devolucao/conferenciaEncalhe/verificarPermissaoSupervisor", 
			data, 
			function(result){
				
				if (result && Array.isArray(result) && result.result != ""){
					
					if (result[0]){
						
						exibirMensagem('WARNING', [result[1]]);
						_this._adicionarNovoProduto(data, keepDialog);
					} else {
						
						$("#msgSupervisor", ConferenciaEncalheCont.workspace).text(result[1]);
						
						$("#dialog-autenticar-supervisor", ConferenciaEncalheCont.workspace).dialog({
							resizable: false,
							height:'auto',
							width:400,
							modal: true,
							buttons: {
								"Ok": function() {
									
									ConferenciaEncalheCont.resetValue = false;
									ConferenciaEncalheCont.autenticarSupervisor(null, _this._adicionarNovoProduto, data, keepDialog);
									
								},
								"Cancelar": function() {
									
									$(this).dialog("close");
								}
							},
							
							open : function() {
								
								ConferenciaEncalheCont.configurarNavegacaoSetas($('#form-autenticar-supervisor').find('button'));
								
								setTimeout(function(){
									
									$('#inputUsuarioSup', this.workspace).select();
									
								}, 1);
								
							},
							
							form: $("#dialog-autenticar-supervisor", this.workspace).parents("form")
						});
					}
					
				} else {
					
					_this._adicionarNovoProduto(data, keepDialog);
				}
			}
		);
	},
	
	_adicionarNovoProduto : function(params, keepDialog){
		
		$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/adicionarProdutoConferido', params,
			function(result){
			
				ConferenciaEncalheCont.preProcessarConsultaConferenciaEncalhe(result);
				
				ConferenciaEncalheCont.limparCamposNovoEncalhe();
				
				if(!keepDialog) {
					$("#dialog-encalhe", ConferenciaEncalheCont.workspace).dialog("close");
				} else {
					focusSelectRefField($("#dialog-encalhe input", ConferenciaEncalheCont.workspace).first());
				}
				
				bloquearItensEdicao(ConferenciaEncalheCont.workspace);
	
			}, null, true, "idModalNovoEncalhe"
		);
	},
	
	limparCamposNovoEncalhe: function(){
		
		$("#lstProdutos", ConferenciaEncalheCont.workspace).val("");
		
		$("#numEdicaoNovoEncalhe", ConferenciaEncalheCont.workspace).val("");
		
		$("#precoCapaNovoEncalhe", ConferenciaEncalheCont.workspace).val("");
		
		$("#descontoNovoEncalhe", ConferenciaEncalheCont.workspace).val("");
		
		$("#exemplaresNovoEncalhe", ConferenciaEncalheCont.workspace).val("");
		
		$("#valorTotalNovoEncalhe", ConferenciaEncalheCont.workspace).val("");
		
		$(".isParcial", ConferenciaEncalheCont.workspace).show();
		
		if(ConferenciaEncalheCont.indDistribuidorAceitaJuramentado){
			
			$("#checkboxJueramentadaNovoEncalhe", ConferenciaEncalheCont.workspace).removeAttr('disabled');
		}else{
			
			$("#checkboxJueramentadaNovoEncalhe", ConferenciaEncalheCont.workspace).attr('disabled', true);
		}
		
		$("#checkboxJueramentadaNovoEncalhe", ConferenciaEncalheCont.workspace).removeAttr("checked");
	},
	
	calcularValorTotalNovoEncalhe: function() {
		
		$("#valorTotalNovoEncalhe", ConferenciaEncalheCont.workspace).val(parseFloat(($("#descontoNovoEncalhe", ConferenciaEncalheCont.workspace).val()) * $("#exemplaresNovoEncalhe", ConferenciaEncalheCont.workspace).val()).toFixed(2));
	},
	
	carregarGridItensNotaFiscal : function(modeloConferenciaEncalhe) {
	
		$("._dadosConfEncalheFinalizar", ConferenciaEncalheCont.workspace).remove();
		
		var innerTable = '';
		
		$.each(modeloConferenciaEncalhe, 
				function(index, value) {
					
					var _class;
					
					if (index % 2 == 0){
						_class = "class_linha_1 _dadosConfEncalheFinalizar";
					} else {
						_class = "class_linha_2 _dadosConfEncalheFinalizar";
					}
					
					innerTable += "<tr class='" + _class + "'>";
					
					innerTable += "<td>" + value.codigo + "<input id='idConferenciaEncalheHiddenFinalizarConf_"+ index +"' type='hidden' value='" + value.idConferenciaEncalhe + "'/></td>";
					
					innerTable += "<td>" + value.nomeProduto + "</td>";
					
					innerTable += "<td style='text-align: center;'>" + value.numeroEdicao + "</td>";
					
					if (value.dia || value.dataRecolhimento){
						
						if (value.dia && value.dia > 0){
						
							innerTable += "<td style='text-align: center;' nowrap='nowrap'>" + value.dia + "º" + "</td>";
						} else {
							
							innerTable += "<td style='text-align: center;' nowrap='nowrap' style='width: 20px;'>" + value.dataRecolhimento + "</td>";
						}
					} else {
						
						innerTable += "<td></td>";
					}
					
					innerTable +=
						'<td style="text-align: center"><input id="qtdeInformadaFinalizarConf_'+ index +'" onchange="ConferenciaEncalheCont.recalcularValoresFinalizar('+ index +');" type="text" maxlength="255" style="width:50px; text-align: center;" value="' + parseInt(value.qtdInformada) + '"/></td>';
					
					innerTable += "<td style='text-align: center;'>" + (value.qtdExemplar ? parseInt(value.qtdExemplar) : "0") + "</td>";
				
					innerTable +=
						'<td style="text-align: center;"><input id="precoCapaFinalizarConf_'+ index +'" onchange="ConferenciaEncalheCont.recalcularValoresFinalizar('+ index +');" maxlength="255" style="width:50px; text-align: right;" value="' + parseFloat(value.precoCapaInformado).toFixed(2) + '"/></td>';
					
					innerTable += "<td style='text-align: right;'>" + parseFloat(value.desconto).toFixed(4) + "</td>";
					
					innerTable += "<td style='text-align: right;' id='valorTotalConferenciaFinalizar_" + index + "'>" + parseFloat(value.valorTotal).toFixed(2) + "</td>";
					
					var imgExclusao = '<img src="' + contextPath + '/images/ico_excluir.gif" width="15" height="15" alt="Salvar" hspace="5" border="0" />';
					innerTable += '<td style="text-align: center;"><a href="javascript:;" onclick="ConferenciaEncalheCont.excluirConferencia(' + value.idConferenciaEncalhe + ');">' + imgExclusao + '</a></td>';
					
					innerTable += "</tr>";
					
					$(innerTable).appendTo("#contingencia-dadosGridConferenciaEncalheFinalizar", ConferenciaEncalheCont.workspace);
					
					innerTable = '';
				}
			);
	},
	
	excluirConferencia : function(idConferenciaEncalhe){
		
		ConferenciaEncalheCont.modalAberta = true;
		
		$("#dialog-excluir-conferencia", ConferenciaEncalheCont.workspace).dialog({
			resizable : false,
			height : 180,
			width : 460,
			modal : true,
			buttons : {
				"Confirmar" : function() {
					
					var data = [{name : "idConferenciaEncalhe", value : idConferenciaEncalhe}];
					
					$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/excluirConferencia', data,
							
						function (result){
							
							ConferenciaEncalheCont.preProcessarConsultaConferenciaEncalhe(result);
							
							ConferenciaEncalheCont.carregarGridItensNotaFiscal(result.listaConferenciaEncalhe);
							
							bloquearItensEdicao(ConferenciaEncalheCont.workspace);
							
							$("#dialog-excluir-conferencia", ConferenciaEncalheCont.workspace).dialog("close");
						}
					
					);
				},
				"Cancelar" : function() {
					
					$(this).dialog("close");
					$('#pesq_cota', ConferenciaEncalheCont.workspace).focus();
				}
			}, 
			
			open : function() {
				
				ConferenciaEncalheCont.configurarNavegacaoSetas($('#form-excluir-conferencia').find('button'));
				
				setTimeout(function(){
					$($('#form-excluir-conferencia').find('button')[0]).focus();
				}, 1);
				
			},
			
			close : function(){
				
				ConferenciaEncalheCont.modalAberta = false;
			},
			form: $("#dialog-excluir-conferencia", this.workspace).parents("form")			
		});
	},
	
	mostrarChaveAcesso : function() {
		
		if($('input:radio[name=radioNFE]:checked').val() == 'S') {
			
			$("#divForChaveAcessoNFE", ConferenciaEncalheCont.workspace).show();
			
		} else {

			$("#divForChaveAcessoNFE", ConferenciaEncalheCont.workspace).hide();
			
		}
	},
	
	redefinirValorTotalExemplaresFooter: function() {
		
		var totalAtualizado = 0;
		
		$("input[name='inputValorExemplares']", ConferenciaEncalheCont.workspace).each(function() {
			
			var parsedValue = parseInt($(this, ConferenciaEncalheCont.workspace).val());
			
			var valor = isNaN(parsedValue) ? 0 : parsedValue;

			totalAtualizado += valor;
		});
		
		$("#totalExemplaresFooter", ConferenciaEncalheCont.workspace).html(totalAtualizado);
	},

	validarInputExemplares :function(curIndex, valorRealExemplares) {
		
		var input = $('[tabindex='+ curIndex + ']');
		
		var valorInputado = input.val();

		if (valorInputado > valorRealExemplares) {
			
			exibirMensagem('WARNING', [ "O valor digitado deve ser menor ou igual ao total do reparte." ]);
			
			input.val(valorRealExemplares);
			
			this.redefinirValorTotalExemplaresFooter();
		}
	},

	nextInputExemplares : function(curIndex, evt) {
	
		if (evt.keyCode == 13 || evt.keyCode == 40) {
			
			var nextElement = $('input[tabindex=' + (curIndex + 1) + '][name="inputValorExemplares"]');
			
			if (!nextElement.length) {
				
				return;
			}

			var position = $(nextElement).val().length;
			
			setTimeout(function(){
				$(nextElement).focus().selectRange(0, position);
			},1);

		} else if (event.keyCode == 38) {
			
			var previousElement = $('input[tabindex=' + (curIndex - 1) + '][name="inputValorExemplares"]');
			
			if (!previousElement.length) {
				
				return;
			}

			var position = $(previousElement).val().length;
			
			setTimeout(function(){
				$(previousElement).focus().selectRange(0, position);
			},1);
			
		} 
	},
	
	abrirDialogNotaFiscalDivergente: function(result){
		$(".message-dialog-encalhe > div", ConferenciaEncalheCont.workspace).hide();
		if (result.mensagens){
			
			exibirMensagem(result.mensagens.tipoMensagem, result.mensagens.listaMensagens);
			return;
		}
		
		ConferenciaEncalheCont.modalAberta = true;
		
		$("#dialog-dadosNotaFiscalContingencia", ConferenciaEncalheCont.workspace).dialog({
			
			resizable : false,
			height : 'auto',
			width : 877,
			modal : true,
			buttons : {
				"Confirmar" : function() {
					var data = [{name: 'indConferenciaContingencia', value: true}];
					
					$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/finalizarConferencia', data,
						
						function(conteudo){
							
						if(conteudo.tipoMensagem == 'SUCCESS') {
							
							$("#dialog-dadosNotaFiscalContingencia", ConferenciaEncalheCont.workspace).dialog("close");
							
							if(conteudo.indGeraDocumentoConfEncalheCota == true) {

								ConferenciaEncalheCont.gerarDocumentosConferenciaEncalhe(conteudo.tipos_documento_impressao_encalhe);
								
							}
							
						}

						exibirMensagem(conteudo.tipoMensagem, conteudo.listaMensagens);
						
						
						}, function(conteudo) {
							
							ConferenciaEncalheCont.limpaTela();
							
							
						}, true, "idModalDadosNotaFiscal"
						
					);
					
				},
				"Cancelar" : function() {
					
					$(this).dialog("close");
				}
			},
			
			open : function() {
				
				ConferenciaEncalheCont.configurarNavegacaoSetas($('#form-dadosNotaFiscalContingencia').find('button'));
				
				setTimeout(function(){
					$($('#form-dadosNotaFiscalContingencia').find('button')[0]).focus();
				}, 1);
				
			},
			
			close : function(){
				
				ConferenciaEncalheCont.modalAberta = false;
			},
			form: $("#dialog-dadosNotaFiscalContingencia", this.workspace).parents("form")			
		});
		
		var notaFiscal = result.notaFiscal;
		
		if (notaFiscal){
		
			$("#numeroNotaFiscalExibir", ConferenciaEncalheCont.workspace).text(notaFiscal.numero);
			$("#serieExibir", ConferenciaEncalheCont.workspace).text(notaFiscal.serie);
			$("#contingencia-dataExibir", ConferenciaEncalheCont.workspace).text(notaFiscal.dataEmissao);
			$("#contingencia-valorTotalNotaFiscalExibir", ConferenciaEncalheCont.workspace).text(parseFloat(notaFiscal.valorProdutos).toFixed(2));
			$("#chaveAcessoExibir", ConferenciaEncalheCont.workspace).text(notaFiscal.chaveAcesso);
		}
		
		
		
		var modeloConferenciaEncalhe = result.listaConferenciaEncalhe;
		
		$("._dadosConfEncalheFinalizar", ConferenciaEncalheCont.workspace).remove();
		
		if (modeloConferenciaEncalhe){
		
			ConferenciaEncalheCont.carregarGridItensNotaFiscal(modeloConferenciaEncalhe);
			
			$('input[id*="qtdeInformadaFinalizarConf"]', ConferenciaEncalheCont.workspace).numeric();
			$('input[id*="precoCapaFinalizarConf"]', ConferenciaEncalheCont.workspace).numeric();
		}
		
		$("#somatorioQtdInformada", ConferenciaEncalheCont.workspace).text(parseInt(result.qtdInformada));
		$("#somatorioQtdRecebida", ConferenciaEncalheCont.workspace).text(parseInt(result.qtdRecebida));
		$("#somatorioTotal", ConferenciaEncalheCont.workspace).text(parseFloat(result.valorPagar).toFixed(2));
		
		bloquearItensEdicao(ConferenciaEncalheCont.workspace);
	},
	
	popup_novo_encalhe: function () {
		
		var _this = this;
		
		ConferenciaEncalheCont.modalAberta = true;
		$(".message-dialog-encalhe > div", ConferenciaEncalheCont.workspace).hide();
		$("#dialog-encalhe", ConferenciaEncalheCont.workspace).dialog({
			resizable : false,
			height : 'auto',
			width : 450,
			modal : true,
			buttons : {
				"Confirmar" : function() {
					
					_this.adicionarEncalhe();
				},
				"Cancelar" : function() {
					
					$(this).dialog("close");
				}
			},
			
			open : function() {
				
				ConferenciaEncalheCont.configurarNavegacaoSetas($('#form-encalhe').find('button'));
				
				setTimeout(function(){
					$("#dialog-encalhe", ConferenciaEncalheCont.workspace).find("input").first().focus();
				}, 1);
				
			},
			
			close : function(){
				
				ConferenciaEncalheCont.modalAberta = false;
			},
			form: $("#dialog-encalhe", this.workspace).parents("form")			
		});

	},
	
	popup_alert : function() {
		
		ConferenciaEncalheCont.modalAberta = true;
		$(".message-dialog-encalhe > div", ConferenciaEncalheCont.workspace).hide();
		$("#dialog-alert", ConferenciaEncalheCont.workspace).dialog({
			resizable : false,
			height : 190,
			width : 460,
			modal : true,
			buttons : {
				"Sim" : function() {
					
					$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/carregarNotaFiscal', null, 
						function(result){
							
							$("#numNotaFiscal", ConferenciaEncalheCont.workspace).val(result.numero);
							$("#serieNotaFiscal", ConferenciaEncalheCont.workspace).val(result.serie);
							$("#dataNotaFiscal", ConferenciaEncalheCont.workspace).val(result.dataEmissao);
						    $("#chaveAcessoNFE", ConferenciaEncalheCont.workspace).val(result.chaveAcesso);
							
						    if (result.valorProdutos){
							    
						    	$("#contingencia-valorNotaFiscal", ConferenciaEncalheCont.workspace).val(parseFloat(result.valorProdutos).toFixed(2));
								
						    }
							
							$("#dialog-alert", ConferenciaEncalheCont.workspace).dialog("close");
							ConferenciaEncalheCont.popup_notaFiscal();
						}
					);
				},
				"Não" : function() {
					
					$("#dialog-alert", ConferenciaEncalheCont.workspace).dialog("close");
					
					$("#contingencia-vlrCE", ConferenciaEncalheCont.workspace).focus();
					
					$("#qtdCE", ConferenciaEncalheCont.workspace).focus();
					
				}
			}, open : function(){
				
				ConferenciaEncalheCont.configurarNavegacaoSetas($('#form-alert').find('button'));
				
				setTimeout(function(){
					$($('#form-alert').find('button')[0]).focus();
				}, 1);
				
				
			}, close : function(){
				
				ConferenciaEncalheCont.modalAberta = false;
			},
			form: $("#dialog-alert", this.workspace).parents("form")			
		});
		
		$("#dialog-alert", ConferenciaEncalheCont.workspace).show();
	},
	
	popup_notaFiscal: function () {
		
		ConferenciaEncalheCont.modalAberta = true;
		$(".message-dialog-encalhe > div", ConferenciaEncalheCont.workspace).hide();
		$("#dialog-notaFiscal", ConferenciaEncalheCont.workspace).dialog({
			resizable : false,
			height : 360,
			width : 750,
			modal : true,
			buttons : {
				
				"Confirmar" : function() {
					
					var data = []; 
					
					if($('input:radio[name=radioNFE]:checked', ConferenciaEncalheCont.workspace).val() == 'S') {
						
						data = [
								{name : "notaFiscal.numero", value : $("#numNotaFiscal", ConferenciaEncalheCont.workspace).val()},
								{name : "notaFiscal.serie", value : $("#serieNotaFiscal", ConferenciaEncalheCont.workspace).val()},
								{name : "notaFiscal.dataEmissao", value : $("#dataNotaFiscal", ConferenciaEncalheCont.workspace).val()},
								{name : "notaFiscal.valorProdutos", value : priceToFloat($("#contingencia-valorNotaFiscal", ConferenciaEncalheCont.workspace).val())},
								{name : "notaFiscal.chaveAcesso", value : $("#chaveAcessoNFE", ConferenciaEncalheCont.workspace).val()}
						];
						
					} else {
						
						data = [
								{name : "notaFiscal.numero", value : $("#numNotaFiscal", ConferenciaEncalheCont.workspace).val()},
								{name : "notaFiscal.serie", value : $("#serieNotaFiscal", ConferenciaEncalheCont.workspace).val()},
								{name : "notaFiscal.dataEmissao", value : $("#dataNotaFiscal", ConferenciaEncalheCont.workspace).val()},
								{name : "notaFiscal.valorProdutos", value : priceToFloat($("#contingencia-valorNotaFiscal", ConferenciaEncalheCont.workspace).val())}
						];
						
					}
					
					$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/salvarNotaFiscal', data, 
						function(result){
							
							$("#dialog-notaFiscal", ConferenciaEncalheCont.workspace).dialog("close");
							
							$("#contingencia-vlrCE", ConferenciaEncalheCont.workspace).val($("#contingencia-valorNotaFiscal", ConferenciaEncalheCont.workspace).val());
							
							$("#contingencia-vlrCE", ConferenciaEncalheCont.workspace).focus();

							$("#qtdCE", ConferenciaEncalheCont.workspace).focus();

							
					}, null, true, "dialog-notaFiscal"
					);
				},
				"Cancelar" : function() {
					$(this).dialog("close");
					$("#contingencia-vlrCE", ConferenciaEncalheCont.workspace).focus();
					$("#qtdCE", ConferenciaEncalheCont.workspace).focus();
				}
			}, 
			
			open : function() {
				
				ConferenciaEncalheCont.configurarNavegacaoSetas($('#form-notaFiscal').find('button'));
				
				setTimeout(function(){
					$($('#form-notaFiscal').find('button')[0]).focus();
				}, 1);
				
			},
			
			close : function(){
				
				ConferenciaEncalheCont.modalAberta = false;
			},
			form: $("#dialog-notaFiscal", this.workspace).parents("form")			
		});

	},

	popup_outros_valores: function () {
		
		ConferenciaEncalheCont.modalAberta = true;
		$(".message-dialog-encalhe > div", ConferenciaEncalheCont.workspace).hide();
		$("#dialog-outros-valores", ConferenciaEncalheCont.workspace).dialog({
			resizable : false,
			height : 430,
			width : 600,
			modal : true,
			buttons : {
				"Fechar" : function() {
					
					$(this).dialog("close");
				}
			}, 
			
			open : function() {
				
				ConferenciaEncalheCont.configurarNavegacaoSetas($('#form-outros-valores').find('button'));
				
				setTimeout(function(){
					$($('#form-outros-valores').find('button')[0]).focus();
				}, 1);
				
			},
			
			close : function(){
				
				ConferenciaEncalheCont.modalAberta = false;
			},
			form: $("#dialog-outros-valores", this.workspace).parents("form")			
		});

	},
	
	configurarNavegacaoSetas : function (bts) {
		
		$(bts).each(function(index, element){
			
			$(element).keydown(function(e){
				
				if(e.keyCode == 39) {
					var campo = $(this).next();
					$(campo).focus();
				} else if (e.keyCode == 37) {
					var campo = $(this).prev();
					$(campo).focus();
				}
				
			});
			
		});
		
	},
	
	popup_salvarInfos : function() {
		
		ConferenciaEncalheCont.modalAberta = true;
		$(".message-dialog-encalhe > div", ConferenciaEncalheCont.workspace).hide();
		$("#dialog-salvar", ConferenciaEncalheCont.workspace).dialog({
			resizable : false,
			height : 190,
			width : 460,
			modal : true,
			buttons : {
				"Confirmar" : function() {
					
					var data = [{name: 'indConferenciaContingencia', value: true}];					
					$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/salvarConferencia', data,
						function(result){
					
							if(result.tipoMensagem == 'SUCCESS') {
								
								ConferenciaEncalheCont.limpaTela();
								
							}						
							
							exibirMensagem(result.tipoMensagem, result.listaMensagens);
								
							$("#dialog-salvar", ConferenciaEncalheCont.workspace).dialog("close");
							
							ConferenciaEncalheCont.numeroCotaEditavel(true);
					
						}, function(conteudo) {
							
							ConferenciaEncalheCont.limpaTela();
							
							
						}, true, "idModalConfirmarSalvarConf"
					);
				},
				"Cancelar" : function() {
					
					$(this).dialog("close");
				}

			}, 
			
			open : function() {
				
				ConferenciaEncalheCont.configurarNavegacaoSetas($('#form-salvar').find('button'));
				
				setTimeout(function(){
					$($('#form-salvar').find('button')[0]).focus();
				}, 1);
				
			},
			
			close : function(){
				
				ConferenciaEncalheCont.modalAberta = false;
			},
			form: $("#dialog-salvar", this.workspace).parents("form")			
		});

	},
	
	verificarCobrancaGerada: function(){
		
		$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/verificarCobrancaGerada', null,
		
			function(conteudo){
			
				if(conteudo && conteudo.tipoMensagem == 'WARNING') {
					
					$("#msgRegerarCobranca", ConferenciaEncalheCont.workspace).text(conteudo.listaMensagens[0]);
					
					$("#dialog-confirmar-regerar-cobranca", ConferenciaEncalheCont.workspace).dialog({
						resizable : false,
						height : 'auto',
						width : 680,
						modal : true,
						buttons : {
							"Confirmar" : function() {
								
								$("#dialog-confirmar-regerar-cobranca", ConferenciaEncalheCont.workspace).dialog("close");
								ConferenciaEncalheCont.verificarValorTotalCE();
								
								ConferenciaEncalheCont.numeroCotaEditavel(true);
							},
							"Cancelar" : function(){
							
								$("#dialog-confirmar-regerar-cobranca", ConferenciaEncalheCont.workspace).dialog("close");
							}
						},
						
						open : function() {
							
							ConferenciaEncalheCont.configurarNavegacaoSetas($('#form-confirmar-regerar-cobranca').find('button'));
							
							setTimeout(function(){
								$($('#form-confirmar-regerar-cobranca').find('button')[0]).focus();
							}, 1);
							
						},
						
						form: $("#dialog-confirmar-regerar-cobranca", ConferenciaEncalheCont.workspace).parents("form")
					});
					
				} else {
					
					ConferenciaEncalheCont.verificarValorTotalCE();
					
					ConferenciaEncalheCont.numeroCotaEditavel(true);
				}
				
			}, null, true, "dialog-confirmar-regerar-cobranca"
		);
	},
	limpaTela:function(){
		ConferenciaEncalheCont.preProcessarConsultaConferenciaEncalhe({reparte:0,valorEncalhe:0,valorVendaDia:0,valorDebitoCredito:0,valorPagar:0, listaDebitoCredito:{page:0,total:0, rows:null}});
		$(".dadosFiltro", ConferenciaEncalheCont.workspace).hide();
		$("#totalExemplaresFooter", ConferenciaEncalheCont.workspace).html(0);
		
		$("#contingencia-numeroCota", ConferenciaEncalheCont.workspace).val("");
		
		$("#contingencia-numeroCota", ConferenciaEncalheCont.workspace).select();
		
		$("#contingencia-numeroCota", ConferenciaEncalheCont.workspace).focus();
		
		bloquearItensEdicao(ConferenciaEncalheCont.workspace);
	},
	
	/**
     * Verifica se houve alteração na conferencia de encalhe da cota. 
     * Caso positivo será informado ao usuario antes de fechar a 
     * aba de conferência de encalhe da cota.
     * 
     * @param self
     * @param index
     */
	verificarAlteracoesConferenciaEncalheParaFecharAba : function(self, index) {
		
		$.postJSON(contextPath + "/devolucao/conferenciaEncalhe/verificarConferenciaEncalheCotaStatus", null,
		
		function(result){
			
			if(result.CONFERENCIA_ENCALHE_COTA_STATUS == 'INICIADA_NAO_SALVA') {
		
				ConferenciaEncalheCont.modalAberta = true;
				
				$("#dialog-conferencia-nao-salva", ConferenciaEncalheCont.workspace).dialog({
					resizable : false,
					height : 180,
					width : 460,
					modal : true,
					buttons : {
						"Sim" : function() {
							
							window.event.preventDefault();
							
							ConferenciaEncalheCont.removerTravaConferenciaEncalheCotaUsuario();
							
							$("#dialog-conferencia-nao-salva", ConferenciaEncalheCont.workspace).dialog("close");
							
							$(self).tabs("remove", index);
							
							ConferenciaEncalheCont.modalAberta = false;
							
							ConferenciaEncalheCont.numeroCotaEditavel(true);
							
						},
						"Não" : function() {
							
							window.event.preventDefault();
							
							$("#dialog-conferencia-nao-salva", ConferenciaEncalheCont.workspace).dialog("close");
							
							ConferenciaEncalheCont.modalAberta = false;
							
							ConferenciaEncalheCont.numeroCotaEditavel(false);
							
						}
					},
					
					open : function() {
						
						ConferenciaEncalheCont.configurarNavegacaoSetas($('#form-conferencia-nao-salva').find('button'));
						
						setTimeout(function(){
							$($('#form-conferencia-nao-salva').find('button')[0]).focus();
						}, 1);
						
					},
					
					form: $("#dialog-conferencia-nao-salva", this.workspace).parents("form")
				});
				
				
			} else {
				
				ConferenciaEncalheCont.removerTravaConferenciaEncalheCotaUsuario();
				
				$(self).tabs("remove", index);
				
			}
		});	
	 }
}, BaseController);

//@ sourceURL=scriptConferenciaEncalheCont.js
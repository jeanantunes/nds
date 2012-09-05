var ConferenciaEncalheCont = $.extend(true, {
	
	modalAberta: false,
	
	idProdutoEdicaoNovoEncalhe: "",

	init : function() {
		
		$(function() {
			
			$(".outrosVlrsGrid", ConferenciaEncalheCont.workspace).flexigrid({
				dataType : 'json',
				colModel : [ {
					display : 'Data',
					name : 'data',
					width : 100,
					sortable : false,
					align : 'left'
				}, {
					display : 'Tipo de Lançamento',
					name : 'tipoLancto',
					width : 140,
					sortable : false,
					align : 'left'
				}, {
					display : 'Valor R$',
					name : 'valor',
					width : 100,
					sortable : false,
					align : 'right'
				} ],
				width : 400,
				height : 250
			});
			
			//hack para message dialog com tamanho fixo
			$(".message-dialog-encalhe > div", ConferenciaEncalheCont.workspace).css('width', '93%');
			
			$("#numeroCota", ConferenciaEncalheCont.workspace).numeric();
			$("#numeroCota", ConferenciaEncalheCont.workspace).focus();
			$("#exemplaresNovoEncalhe", ConferenciaEncalheCont.workspace).numeric();
			$("#vlrCE", ConferenciaEncalheCont.workspace).numeric();
			$("#dataNotaFiscal", ConferenciaEncalheCont.workspace).mask("99/99/9999");
			
			$("#numeroCota", ConferenciaEncalheCont.workspace).keypress(function(e) {
				
				if (e.keyCode == 13) {
					
					ConferenciaEncalheCont.pesquisarCota();
				}
			});
			
			$("#lstProdutos", ConferenciaEncalheCont.workspace).keypress(function(e){
				
				ConferenciaEncalheCont.pesquisarProdutoPorCodigoNome();
			});
			
			ConferenciaEncalheCont.criarComboBoxEncalhe();
			
		});

		shortcut.add("F2", function() {
			
			if (!ConferenciaEncalheCont.modalAberta){
				
				ConferenciaEncalheCont.limparCamposNovoEncalhe();
				ConferenciaEncalheCont.popup_novo_encalhe();
			}
		});

		shortcut.add("F6", function() {
			
			if (!ConferenciaEncalheCont.modalAberta){
				
				ConferenciaEncalheCont.popup_notaFiscal();
			}
		});

		shortcut.add("F8", function() {
			
			if (!ConferenciaEncalheCont.modalAberta){
			
				ConferenciaEncalheCont.verificarValorTotalCE();
			}
		});

		shortcut.add("F9", function() {
			
			if (!ConferenciaEncalheCont.modalAberta){
				
				ConferenciaEncalheCont.popup_salvarInfos();
			}
		});		
	},
	
	pesquisarCota : function(){
		
		var data = [
		            {name: 'numeroCota', value : $("#numeroCota", ConferenciaEncalheCont.workspace).val()}, 
		            {name: 'indObtemDadosFromBD', value : true},
		            {name: 'indConferenciaContingencia', value: true}
		           ];
		
		$.postJSON(contextPath + "/devolucao/conferenciaEncalhe/verificarReabertura", data,
			function(result){
				
				if (result.listaMensagens && result.listaMensagens[0] == "REABERTURA"){
					
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
								ConferenciaEncalheCont.popup_alert();
							},
							"Não" : function() {
								$("#dialog-reabertura", ConferenciaEncalheCont.workspace).dialog("close");
							}
						}, close : function(){
							
							ConferenciaEncalheCont.modalAberta = false;
						},
						form: $("#dialog-reabertura", this.workspace).parents("form")
					});
				} else {
					
					ConferenciaEncalheCont.carregarListaConferencia(data);
					$("#dialog-reabertura", ConferenciaEncalheCont.workspace).dialog("close");
					ConferenciaEncalheCont.popup_alert();
				}
			}
		);
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
				$("#somatorioTotal", ConferenciaEncalheCont.workspace).text(parseFloat(result.valorPagar).toFixed(2));
			}
		);
	},
	
	criarComboBoxEncalhe : function() {
		
		$.postJSON(contextPath + "/devolucao/conferenciaEncalhe/carregarComboBoxEncalheContingencia", null,
				
			function(result){
				
				var opcoesBox = '';
				
				$.each(result.boxes, function(key, value) {
					opcoesBox = opcoesBox + "<option value="+key+">"+value+"</option>"; 
				});
				
				$('#boxLogado', ConferenciaEncalheCont.workspace).html(opcoesBox);
				
				ConferenciaEncalheCont.popup_logado();
				
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
				"Confirmar" : function() {
					
					$.postJSON(contextPath + "/devolucao/conferenciaEncalhe/salvarIdBoxSessao", "idBox=" + $("#boxLogado").val(), 
						function(){
							
							$("#dialog-logado", ConferenciaEncalheCont.workspace).dialog("close");
							$('#numeroCota', ConferenciaEncalheCont.workspace).focus();
						}, null, true, "idModalBoxRecolhimento"
					);
				},
				"Cancelar" : function() {
					$(this).dialog("close");
					$('#pesq_cota', ConferenciaEncalheCont.workspace).focus();
				}
			}, open : function(){
				
				$("#boxLogado", ConferenciaEncalheCont.workspace).focus();
			}, close : function(){
				
				ConferenciaEncalheCont.modalAberta = false;
			},
			form: $("#dialog-logado", this.workspace).parents("form")			
		});
	},
	
	gerarDocumentosConferenciaEncalhe : function(tiposDocumento) {
		
		var fileArray = [];
		
		 $.each(tiposDocumento, function(index, value){
			 
			 if(value == 'SLIP') {
				 fileArray[index] = contextPath + '/devolucao/conferenciaEncalhe/gerarSlip';
			 } 
			 
			 if(value == 'BOLETO_OU_RECIBO') {
				 fileArray[index] = contextPath + '/devolucao/conferenciaEncalhe/gerarBoletoOuRecibo';
			 }
			 
		 });
		
		 var fileIndex = 0;

		 $('#download-iframe', ConferenciaEncalheCont.workspace).attr('src', fileArray[fileIndex]);
		 
		 fileIndex++;

		 var interval = setInterval(function() {
			 
		        if(fileIndex < fileArray.length) {
		            
		        	$('#download-iframe', ConferenciaEncalheCont.workspace).attr('src', fileArray[fileIndex]);
		            
		        	fileIndex++;
		            
		        } else {
		            clearInterval(interval);
		        }
		        
		    }, 100 );
		
	},
	
	verificarValorTotalNotaFiscal : function() {
		
		$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/verificarValorTotalNotaFiscal',null,
				
				function(result){
				
					if(result.tipoMensagem == 'SUCCESS') {
						
						if(result.indGeraDocumentoConfEncalheCota == true) {

							ConferenciaEncalheCont.gerarDocumentosConferenciaEncalhe(result.tiposDocumento);
							
						}
						
						var data = [
						     
						     {name: 'numeroCota', value : $("#numeroCota", ConferenciaEncalheCont.workspace).val()}, 
						     {name: 'indObtemDadosFromBD', value : true},
						     {name: 'indConferenciaContingencia', value: false}
						];
						
						ConferenciaEncalheCont.carregarListaConferencia(data);
						
					}
				
					exibirMensagem(result.tipoMensagem, result.listaMensagens);
				},
				
				function(){
					
					$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/carregarListaConferencia', 
							null, 
							function(result){
								
								ConferenciaEncalheCont.abrirDialogNotaFiscalDivergente(result);
							}
					);
				}, true, "idModalDadosNotaFiscal"
		);
		
	},
	
	verificarValorTotalCE : function() {
		
		var data = [{name: "valorCEInformado", value: $("#vlrCE", ConferenciaEncalheCont.workspace).val()}];
		
		$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/verificarValorTotalCE', data, 
		
				function(conteudo) {
					
					if(conteudo.valorCEInformadoValido == true) {
						
						ConferenciaEncalheCont.verificarValorTotalNotaFiscal();
						
					} else {
						
						var indConfirmacaoValorCE = confirm(conteudo.mensagemConfirmacao);
						
						if(indConfirmacaoValorCE == true) {
							
							ConferenciaEncalheCont.verificarValorTotalNotaFiscal();
							
						} else {
							
							return;
							
						}
						
						
						
					}
				
				}
				
		);

		
	},	
	
	carregarListaConferencia: function(data){
		
		$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/carregarListaConferencia', 
				data, 
				function(result){
					
					ConferenciaEncalheCont.preProcessarConsultaConferenciaEncalhe(result);
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
		
		ConferenciaEncalheCont.indDistribuidorAceitaJuramentado = result.indDistribuidorAceitaJuramentado;
		
		$("._dadosConfEncalhe", ConferenciaEncalheCont.workspace).remove();
		
		if (modeloConferenciaEncalhe){
		
			$.each(modeloConferenciaEncalhe, 
				function(index, value) {
				
					var parcial = value.parcial;
				
					var _class;
					
					if (index % 2 == 0){
						_class = "class_linha_1 _dadosConfEncalhe";
					} else {
						_class = "class_linha_2 _dadosConfEncalhe";
					}
					
					innerTable += "<tr class='" + _class + " _dados'>";
					
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
					
					innerTable += "<td style='text-align: right;' nowrap='nowrap'>" + parseFloat(value.desconto).toFixed(2) + "</td>";
					
					innerTable += "<td nowrap='nowrap' style='text-align: center;'>";
					
					var valorExemplares = parseInt(value.qtdExemplar);
					
					var inputExemplares = '<input id="qtdExemplaresGrid_' + index + '" maxlength="255" onchange="ConferenciaEncalheCont.atualizarValores('+ index +');" style="width:90px; text-align: center;" value="' + valorExemplares + '"/>' +
						'<input id="idConferenciaEncalheHidden_' + index + '" type="hidden" value="' + value.idConferenciaEncalhe + '"/>';
					
					innerTable += inputExemplares + "</td>";
					
					innerTable += "<td align='right' nowrap='nowrap' id='valorTotalConferencia_" + index + "'>" + parseFloat(value.valorTotal).toFixed(2) + "</td>";
					
					var inputCheckBoxJuramentada = '';
					
					if(ConferenciaEncalheCont.indDistribuidorAceitaJuramentado == true && parcial == true) {
						
						inputCheckBoxJuramentada = '<input type="checkbox" ' + (value.juramentada == true ? 'checked="checked"' : '')
						+ ' onchange="ConferenciaEncalheCont.atualizarValores('+ index +');" id="checkGroupJuramentada_' + index + '"/>';
						
						
					} else {
						
						inputCheckBoxJuramentada = '<input type="checkbox" disabled="disabled" id="checkGroupJuramentada_' + index + '"/>';
						
					}
					
					innerTable += "<td style='text-align: center;' nowrap='nowrap'>" + inputCheckBoxJuramentada + "</td>";
					
					innerTable += "</tr>";
					
					$(innerTable).appendTo("#dadosGridConferenciaEncalhe");
					
					innerTable = '';
				}
			);
			
			$('input[id*="qtdExemplaresGrid"]').numeric();
		}
		
		$(".outrosVlrsGrid", ConferenciaEncalheCont.workspace).flexAddData({
			page: result.listaDebitoCredito.page, total: result.listaDebitoCredito.total, rows: result.listaDebitoCredito.rows
		});
		
		$("#totalReparte", ConferenciaEncalheCont.workspace).text(parseFloat(result.reparte).toFixed(2));
		$("#totalEncalhe", ConferenciaEncalheCont.workspace).text(parseFloat(result.valorEncalhe).toFixed(2));
		$("#valorVendaDia", ConferenciaEncalheCont.workspace).text(parseFloat(result.valorVendaDia).toFixed(2));
		$("#totalOutrosValores", ConferenciaEncalheCont.workspace).text(parseFloat(result.valorDebitoCredito).toFixed(2));
		$("#valorAPagar", ConferenciaEncalheCont.workspace).text(parseFloat(result.valorPagar).toFixed(2));
		
		$(".dadosFiltro", ConferenciaEncalheCont.workspace).show();
		$("#nomeCota", ConferenciaEncalheCont.workspace).text(result.razaoSocial);
		$("#statusCota", ConferenciaEncalheCont.workspace).text(result.situacao);
	},
	
	atualizarValores: function(index){
		
		var juramentado = false;
		
		if( $("#checkGroupJuramentada_" + index, ConferenciaEncalheCont.workspace).attr("checked") == 'checked' ) {
			juramentado = true;
		}
		
		var data = [
            {name: "idConferencia", value: $("#idConferenciaEncalheHidden_" + index, ConferenciaEncalheCont.workspace).val()},
            {name: "qtdExemplares", value: $("#qtdExemplaresGrid_" + index, ConferenciaEncalheCont.workspace).val()},
            {name: "juramentada", value: juramentado}
		];
		
		$.postJSON(contextPath + "/devolucao/conferenciaEncalhe/atualizarValores", 
			data, 
			function(result){
				
				$("#valorTotalConferencia_" + index, ConferenciaEncalheCont.workspace).text(parseFloat(result.conf.valorTotal).toFixed(2));
				
				$("#totalReparte", ConferenciaEncalheCont.workspace).text(parseFloat(result.reparte).toFixed(2));
				$("#totalEncalhe", ConferenciaEncalheCont.workspace).text(parseFloat(result.valorEncalhe).toFixed(2));
				$("#valorVendaDia", ConferenciaEncalheCont.workspace).text(parseFloat(result.valorVendaDia).toFixed(2));
				$("#totalOutrosValores", ConferenciaEncalheCont.workspace).text(parseFloat(result.valorDebitoCredito).toFixed(2));
				$("#valorAPagar", ConferenciaEncalheCont.workspace).text(parseFloat(result.valorPagar).toFixed(2));
			}
		);
	},
	
	pesquisarProdutoPorCodigoNome: function(){
		
		var codigoNomeProduto = $("#lstProdutos", ConferenciaEncalheCont.workspace).val();
		
		if (codigoNomeProduto && codigoNomeProduto.length > 0){
			$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/pesquisarProdutoPorCodigoNome', 
					"codigoNomeProduto=" + codigoNomeProduto, 
				function(result){
					
					if (result[0]){
						
						$("#lstProdutos", ConferenciaEncalheCont.workspace).autocomplete({
							source: result,
							select: function(event, ui){
								
								$.postJSON(contextPath + "/devolucao/conferenciaEncalhe/pesquisarProdutoEdicaoPorId",
									[{name: "idProdutoEdicao", value: ui.item.chave.long}],
									function(result2){
									
										if (result2){
											
											ConferenciaEncalheCont.idProdutoEdicaoNovoEncalhe = ui.item.chave.long;
											$("#lstProdutos", ConferenciaEncalheCont.workspace).val(ui.item.chave.string);
											$("#numEdicaoNovoEncalhe", ConferenciaEncalheCont.workspace).val(result2.numeroEdicao);
											$("#precoCapaNovoEncalhe", ConferenciaEncalheCont.workspace).val(parseFloat(result2.precoVenda).toFixed(2));
											$("#descontoNovoEncalhe", ConferenciaEncalheCont.workspace).val(parseFloat(result2.desconto).toFixed(2));
										}
											
										
									}, function() {
									
										ConferenciaEncalheCont.limparCamposNovoEncalhe();
										$("#lstProdutos", ConferenciaEncalheCont.workspace).focus();
									}, 
									true, "idModalNovoEncalhe"
								);
							}
						});
					}
				}, null, true, "idModalNovoEncalhe"
			);
		}
	},
	
	adicionarEncalhe: function(){
		
		var data = [{name: "idProdutoEdicao", value: ConferenciaEncalheCont.idProdutoEdicaoNovoEncalhe}, 
		            {name: "quantidade", value: $("#exemplaresNovoEncalhe", ConferenciaEncalheCont.workspace).val()},
		            {name:"juramentada", value:$('#checkboxJueramentadaNovoEncalhe', ConferenciaEncalheCont.workspace).attr('checked') == 'checked' }];
		
		
		$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/adicionarProdutoConferido', data,
			function(result){
				
				ConferenciaEncalheCont.preProcessarConsultaConferenciaEncalhe(result);
				
				ConferenciaEncalheCont.limparCamposNovoEncalhe();
				
				$("#lstProdutos", ConferenciaEncalheCont.workspace).focus();
				
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
		
		if(ConferenciaEncalheCont.indDistribuidorAceitaJuramentado){
			$("#checkboxJueramentadaNovoEncalhe", ConferenciaEncalheCont.workspace).removeAttr('disabled');
		}else{
			$("#checkboxJueramentadaNovoEncalhe", ConferenciaEncalheCont.workspace).attr('disabled', true);
		}
		$("#checkboxJueramentadaNovoEncalhe", ConferenciaEncalheCont.workspace).removeAttr("checked");
	},
	
	calcularValorTotalNovoEncalhe: function() {
		
		$("#valorTotalNovoEncalhe", ConferenciaEncalheCont.workspace).val(parseFloat(($("#precoCapaNovoEncalhe", ConferenciaEncalheCont.workspace).val() - $("#descontoNovoEncalhe", ConferenciaEncalheCont.workspace).val()) * $("#exemplaresNovoEncalhe", ConferenciaEncalheCont.workspace).val()).toFixed(2));
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
					
					innerTable += "<td style='text-align: right;'>" + parseFloat(value.desconto).toFixed(2) + "</td>";
					
					innerTable += "<td style='text-align: right;' id='valorTotalConferenciaFinalizar_" + index + "'>" + parseFloat(value.valorTotal).toFixed(2) + "</td>";
					
					var imgExclusao = '<img src="' + contextPath + '/images/ico_excluir.gif" width="15" height="15" alt="Salvar" hspace="5" border="0" />';
					innerTable += '<td style="text-align: center;"><a href="javascript:;" onclick="ConferenciaEncalheCont.excluirConferencia(' + value.idConferenciaEncalhe + ');">' + imgExclusao + '</a></td>';
					
					innerTable += "</tr>";
					
					$(innerTable).appendTo("#dadosGridConferenciaEncalheFinalizar");
					
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
							
							$("#dialog-excluir-conferencia", ConferenciaEncalheCont.workspace).dialog("close");
						}
					
					);
				},
				"Cancelar" : function() {
					
					$(this).dialog("close");
					$('#pesq_cota', ConferenciaEncalheCont.workspace).focus();
				}
			}, close : function(){
				
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
	
	abrirDialogNotaFiscalDivergente: function(result){
		$(".message-dialog-encalhe > div", ConferenciaEncalheCont.workspace).hide();
		if (result.mensagens){
			
			exibirMensagem(result.mensagens.tipoMensagem, result.mensagens.listaMensagens);
			return;
		}
		
		ConferenciaEncalheCont.modalAberta = true;
		
		$("#dialog-dadosNotaFiscal", ConferenciaEncalheCont.workspace).dialog({
			resizable : false,
			height : 'auto',
			width : 877,
			modal : true,
			buttons : {
				"Confirmar" : function() {
					
					$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/finalizarConferencia', null,
						
						function(conteudo){
							
						if(conteudo.tipoMensagem == 'SUCCESS') {
							
							$("#dialog-dadosNotaFiscal", ConferenciaEncalheCont.workspace).dialog("close");
							
							if(conteudo.indGeraDocumentoConfEncalheCota == true) {

								ConferenciaEncalheCont.gerarDocumentosConferenciaEncalhe(conteudo.tiposDocumento);
								
							}
							
							var data = [
							  {name: 'numeroCota', 			value : $("#numeroCota", ConferenciaEncalheCont.workspace).val()}, 
							  {name: 'indObtemDadosFromBD', value : true},
							  {name: 'indConferenciaContingencia', value: false}
							 ];
							
							ConferenciaEncalheCont.carregarListaConferencia(data);
							
						}

						exibirMensagem(conteudo.tipoMensagem, conteudo.listaMensagens);
						
						
						}, null, true, "idModalDadosNotaFiscal"
						
					);
					
				},
				"Cancelar" : function() {
					
					$(this).dialog("close");
				}
			}, close : function(){
				
				ConferenciaEncalheCont.modalAberta = false;
			},
			form: $("#dialog-dadosNotaFiscal", this.workspace).parents("form")			
		});
		
		var notaFiscal = result.notaFiscal;
		
		if (notaFiscal){
		
			$("#numeroNotaFiscalExibir", ConferenciaEncalheCont.workspace).text(notaFiscal.numero);
			$("#serieExibir", ConferenciaEncalheCont.workspace).text(notaFiscal.serie);
			$("#dataExibir", ConferenciaEncalheCont.workspace).text(notaFiscal.dataEmissao);
			$("#valorTotalNotaFiscalExibir", ConferenciaEncalheCont.workspace).text(parseFloat(notaFiscal.valorProdutos).toFixed(2));
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
	},
	
	popup_novo_encalhe: function () {
		
		ConferenciaEncalheCont.modalAberta = true;
		$(".message-dialog-encalhe > div", ConferenciaEncalheCont.workspace).hide();
		$("#dialog-encalhe", ConferenciaEncalheCont.workspace).dialog({
			resizable : false,
			height : 'auto',
			width : 450,
			modal : true,
			buttons : {
				"Confirmar" : function() {
					
					$(this).dialog("close");
				},
				"Cancelar" : function() {
					
					$(this).dialog("close");
				}
			}, close : function(){
				
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
							    
						    	$("#valorNotaFiscal", ConferenciaEncalheCont.workspace).val(parseFloat(result.valorProdutos).toFixed(2));
								
						    }
							
							$("#dialog-alert", ConferenciaEncalheCont.workspace).dialog("close");
							ConferenciaEncalheCont.popup_notaFiscal();
						}
					);
				},
				"Não" : function() {
					
					$("#dialog-alert", ConferenciaEncalheCont.workspace).dialog("close");
					$("#vlrCE", ConferenciaEncalheCont.workspace).focus();
				}
			}, open : function(){
				
				$(this).parent('div', ConferenciaEncalheCont.workspace).find('button:contains("Sim")').focus();
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
								{name : "notaFiscal.valorProdutos", value : $("#valorNotaFiscal", ConferenciaEncalheCont.workspace).val()},
								{name : "notaFiscal.chaveAcesso", value : $("#chaveAcessoNFE", ConferenciaEncalheCont.workspace).val()}
						];
						
					} else {
						
						data = [
								{name : "notaFiscal.numero", value : $("#numNotaFiscal", ConferenciaEncalheCont.workspace).val()},
								{name : "notaFiscal.serie", value : $("#serieNotaFiscal", ConferenciaEncalheCont.workspace).val()},
								{name : "notaFiscal.dataEmissao", value : $("#dataNotaFiscal", ConferenciaEncalheCont.workspace).val()},
								{name : "notaFiscal.valorProdutos", value : $("#valorNotaFiscal", ConferenciaEncalheCont.workspace).val()}
						];
						
					}
					
					$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/salvarNotaFiscal', data, 
						function(result){
							
							$("#dialog-notaFiscal", ConferenciaEncalheCont.workspace).dialog("close");
							
							$("#vlrCE", ConferenciaEncalheCont.workspace).val(parseFloat($("#valorNotaFiscal", ConferenciaEncalheCont.workspace).val()).toFixed(2));
							
							$("#vlrCE", ConferenciaEncalheCont.workspace).focus();
						}, null, true, "dialog-notaFiscal"
					);
				},
				"Cancelar" : function() {
					$(this).dialog("close");
					$("#vlrCE", ConferenciaEncalheCont.workspace).focus();
				}
			}, close : function(){
				
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
			width : 460,
			modal : true,
			buttons : {
				"Fechar" : function() {
					
					$(this).dialog("close");
				}
			}, close : function(){
				
				ConferenciaEncalheCont.modalAberta = false;
			},
			form: $("#dialog-outros-valores", this.workspace).parents("form")			
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
					
					$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/salvarConferencia', null,
						function(result){
					
							if(result.tipoMensagem == 'SUCCESS') {
								
								var data = [
								      {name : 'numeroCota', value : $("#numeroCota", ConferenciaEncalheCont.workspace).val()}, 
								      {name: 'indObtemDadosFromBD', value : true},
								      {name: 'indConferenciaContingencia', value: true}
								];
								
								ConferenciaEncalheCont.carregarListaConferencia(data);
								
							}						
							
							exibirMensagem(result.tipoMensagem, result.listaMensagens);
								
							$("#dialog-salvar", ConferenciaEncalheCont.workspace).dialog("close");
					
						},null, true, "idModalConfirmarSalvarConf"
					);
				},
				"Cancelar" : function() {
					
					$(this).dialog("close");
				}

			}, close : function(){
				
				ConferenciaEncalheCont.modalAberta = false;
			},
			form: $("#dialog-salvar", this.workspace).parents("form")			
		});

	}
}, BaseController);

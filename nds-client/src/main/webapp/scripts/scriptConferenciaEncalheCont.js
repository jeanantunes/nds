var ConferenciaEncalheCont = {
	
	modalAberta: false,
	
	idProdutoEdicaoNovoEncalhe: "",
		
	pesquisarCota : function(){
		
		var data = [
		            {name: 'numeroCota', value : $("#numeroCota").val()}, 
		            {name: 'indObtemDadosFromBD', value : true},
		            {name: 'indConferenciaContingencia', value: true}
		           ];
		
		$.postJSON(contextPath + "/devolucao/conferenciaEncalhe/verificarReabertura", data,
			function(result){
				
				if (result.listaMensagens && result.listaMensagens[0] == "REABERTURA"){
					
					ConferenciaEncalheCont.modalAberta = true;
					
					$("#dialog-reabertura").dialog({
						resizable : false,
						height : 200,
						width : 360,
						modal : true,
						buttons : {
							"Sim" : function() {
								
								$("#dialog-reabertura").dialog("close");
								ConferenciaEncalheCont.carregarListaConferencia(data);
								ConferenciaEncalheCont.popup_alert();
							},
							"Não" : function() {
								$("#dialog-reabertura").dialog("close");
							}
						}, close : function(){
							
							ConferenciaEncalheCont.modalAberta = false;
						}
					});
				} else {
					
					ConferenciaEncalheCont.carregarListaConferencia(data);
					$("#dialog-reabertura").dialog("close");
					ConferenciaEncalheCont.popup_alert();
				}
			}
		);
	},
	
	recalcularValoresFinalizar : function(index){
		
		var data = [
            {name: "idConferencia", value: $("#idConferenciaEncalheHiddenFinalizarConf_" + index).val()},
            {name: "qtdInformada", value: $('#qtdeInformadaFinalizarConf_' + index).val()},
            {name: "valorCapaInformado", value : $('#precoCapaFinalizarConf_' + index).val()}
		];
		
		$.postJSON(contextPath + "/devolucao/conferenciaEncalhe/alterarQtdeValorInformado", 
			data, 
			function(result){
				
				$("#valorTotalConferenciaFinalizar_" + index).text(parseFloat(result.conf.valorTotal).toFixed(2));
				
				$("#somatorioQtdInformada").text(parseFloat(result.qtdInformada).toFixed(2));
				$("#somatorioQtdRecebida").text(parseFloat(result.qtdRecebida).toFixed(2));
				$("#somatorioTotal").text(parseFloat(result.valorPagar).toFixed(2));
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
				
				$('#boxLogado').html(opcoesBox);
				
				ConferenciaEncalheCont.popup_logado();
				
			}
		);
		
		
	},
	
	popup_logado : function() {
		
		ConferenciaEncalheCont.modalAberta = true;
		
		$("#dialog-logado").dialog({
			resizable : false,
			height : 180,
			width : 460,
			modal : true,
			buttons : {
				"Confirmar" : function() {
					
					$.postJSON(contextPath + "/devolucao/conferenciaEncalhe/salvarIdBoxSessao", "idBox=" + $("#boxLogado").val(), 
						function(){
							
							$("#dialog-logado").dialog("close");
							$('#numeroCota').focus();
						}, null, true, "idModalBoxRecolhimento"
					);
				},
				"Cancelar" : function() {
					$(this).dialog("close");
					$('#pesq_cota').focus();
				}
			}, open : function(){
				
				$("#boxLogado").focus();
			}, close : function(){
				
				ConferenciaEncalheCont.modalAberta = false;
			}
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

		 $('#download-iframe').attr('src', fileArray[fileIndex]);
		 
		 fileIndex++;

		 var interval = setInterval(function() {
			 
		        if(fileIndex < fileArray.length) {
		            
		        	$('#download-iframe').attr('src', fileArray[fileIndex]);
		            
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
						     
						     {name: 'numeroCota', value : $("#numeroCota").val()}, 
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
		
		var data = [{name: "valorCEInformado", value: $("#vlrCE").val()}];
		
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
	
	preProcessarConsultaConferenciaEncalhe : function(result){
		
		if (result.mensagens){
			
			exibirMensagem(result.mensagens.tipoMensagem, result.mensagens.listaMensagens);
			return;
		}
		
		var innerTable = '';
		
		var modeloConferenciaEncalhe = result.listaConferenciaEncalhe;
		
		var indDistribuidorAceitaJuramentado = result.indDistribuidorAceitaJuramentado;
		
		$("._dadosConfEncalhe").remove();
		
		if (modeloConferenciaEncalhe){
		
			$.each(modeloConferenciaEncalhe, 
				function(index, value) {
					
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
					
					if(indDistribuidorAceitaJuramentado == true) {
						
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
		
		$(".outrosVlrsGrid").flexAddData({
			page: result.listaDebitoCredito.page, total: result.listaDebitoCredito.total, rows: result.listaDebitoCredito.rows
		});
		
		$("#totalReparte").text(parseFloat(result.reparte).toFixed(2));
		$("#totalEncalhe").text(parseFloat(result.valorEncalhe).toFixed(2));
		$("#valorVendaDia").text(parseFloat(result.valorVendaDia).toFixed(2));
		$("#totalOutrosValores").text(parseFloat(result.valorDebitoCredito).toFixed(2));
		$("#valorAPagar").text(parseFloat(result.valorPagar).toFixed(2));
		
		$(".dadosFiltro").show();
		$("#nomeCota").text(result.razaoSocial);
		$("#statusCota").text(result.situacao);
	},
	
	atualizarValores: function(index){
		
		var juramentado = false;
		
		if( $("#checkGroupJuramentada_" + index).attr("checked") == 'checked' ) {
			juramentado = true;
		}
		
		var data = [
            {name: "idConferencia", value: $("#idConferenciaEncalheHidden_" + index).val()},
            {name: "qtdExemplares", value: $("#qtdExemplaresGrid_" + index).val()},
            {name: "juramentada", value: juramentado}
		];
		
		$.postJSON(contextPath + "/devolucao/conferenciaEncalhe/atualizarValores", 
			data, 
			function(result){
				
				$("#valorTotalConferencia_" + index).text(parseFloat(result.conf.valorTotal).toFixed(2));
				
				$("#totalReparte").text(parseFloat(result.reparte).toFixed(2));
				$("#totalEncalhe").text(parseFloat(result.valorEncalhe).toFixed(2));
				$("#valorVendaDia").text(parseFloat(result.valorVendaDia).toFixed(2));
				$("#totalOutrosValores").text(parseFloat(result.valorDebitoCredito).toFixed(2));
				$("#valorAPagar").text(parseFloat(result.valorPagar).toFixed(2));
			}
		);
	},
	
	pesquisarProdutoPorCodigoNome: function(){
		
		var codigoNomeProduto = $("#lstProdutos").val();
		
		if (codigoNomeProduto && codigoNomeProduto.length > 0){
			$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/pesquisarProdutoPorCodigoNome', 
					"codigoNomeProduto=" + codigoNomeProduto, 
				function(result){
					
					if (result[0]){
						
						$("#lstProdutos").autocomplete({
							source: result,
							select: function(event, ui){
								
								$.postJSON(contextPath + "/devolucao/conferenciaEncalhe/pesquisarProdutoEdicaoPorId",
									[{name: "idProdutoEdicao", value: ui.item.chave.long}],
									function(result2){
									
										if (result2){
											
											ConferenciaEncalheCont.idProdutoEdicaoNovoEncalhe = ui.item.chave.long;
											$("#lstProdutos").val(ui.item.chave.string);
											$("#numEdicaoNovoEncalhe").val(result2.numeroEdicao);
											$("#precoCapaNovoEncalhe").val(parseFloat(result2.precoVenda).toFixed(2));
											$("#descontoNovoEncalhe").val(parseFloat(result2.desconto).toFixed(2));
										}
											
										
									}, function() {
									
										ConferenciaEncalheCont.limparCamposNovoEncalhe();
										$("#lstProdutos").focus();
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
		            {name: "quantidade", value: $("#exemplaresNovoEncalhe").val()}];
		
		$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/adicionarProdutoConferido', data,
			function(result){
				
				ConferenciaEncalheCont.preProcessarConsultaConferenciaEncalhe(result);
				
				ConferenciaEncalheCont.limparCamposNovoEncalhe();
				
				$("#lstProdutos").focus();
				
			}, null, true, "idModalNovoEncalhe"
		);
	},
	
	limparCamposNovoEncalhe: function(){
		
		$("#lstProdutos").val("");
		$("#numEdicaoNovoEncalhe").val("");
		$("#precoCapaNovoEncalhe").val("");
		$("#descontoNovoEncalhe").val("");
		$("#exemplaresNovoEncalhe").val("");
		$("#valorTotalNovoEncalhe").val("");
		$("#checkboxJueramentadaNovoEncalhe").removeAttr("checked");
	},
	
	calcularValorTotalNovoEncalhe: function() {
		
		$("#valorTotalNovoEncalhe").val(parseFloat(($("#precoCapaNovoEncalhe").val() - $("#descontoNovoEncalhe").val()) * $("#exemplaresNovoEncalhe").val()).toFixed(2));
	},
	
	
	carregarGridItensNotaFiscal : function(modeloConferenciaEncalhe) {
	
		$("._dadosConfEncalheFinalizar").remove();
		
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
		
		$("#dialog-excluir-conferencia").dialog({
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
							
							$("#dialog-excluir-conferencia").dialog("close");
						}
					
					);
				},
				"Cancelar" : function() {
					
					$(this).dialog("close");
					$('#pesq_cota').focus();
				}
			}, close : function(){
				
				ConferenciaEncalheCont.modalAberta = false;
			}
		});
	},
	
	mostrarChaveAcesso : function() {
		
		if($('input:radio[name=radioNFE]:checked').val() == 'S') {
			
			$("#divForChaveAcessoNFE").show();
			
		} else {

			$("#divForChaveAcessoNFE").hide();
			
		}
		
	},
	
	abrirDialogNotaFiscalDivergente: function(result){
		$(".message-dialog-encalhe > div").hide();
		if (result.mensagens){
			
			exibirMensagem(result.mensagens.tipoMensagem, result.mensagens.listaMensagens);
			return;
		}
		
		ConferenciaEncalheCont.modalAberta = true;
		
		$("#dialog-dadosNotaFiscal").dialog({
			resizable : false,
			height : 'auto',
			width : 877,
			modal : true,
			buttons : {
				"Confirmar" : function() {
					
					$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/finalizarConferencia', null,
						
						function(conteudo){
							
						if(conteudo.tipoMensagem == 'SUCCESS') {
							
							$("#dialog-dadosNotaFiscal").dialog("close");
							
							if(conteudo.indGeraDocumentoConfEncalheCota == true) {

								ConferenciaEncalheCont.gerarDocumentosConferenciaEncalhe(conteudo.tiposDocumento);
								
							}
							
							var data = [
							  {name: 'numeroCota', 			value : $("#numeroCota").val()}, 
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
			}
		});
		
		var notaFiscal = result.notaFiscal;
		
		if (notaFiscal){
		
			$("#numeroNotaFiscalExibir").text(notaFiscal.numero);
			$("#serieExibir").text(notaFiscal.serie);
			$("#dataExibir").text(notaFiscal.dataEmissao);
			$("#valorTotalNotaFiscalExibir").text(parseFloat(notaFiscal.valorProdutos).toFixed(2));
			$("#chaveAcessoExibir").text(notaFiscal.chaveAcesso);
		}
		
		
		
		var modeloConferenciaEncalhe = result.listaConferenciaEncalhe;
		
		$("._dadosConfEncalheFinalizar").remove();
		
		if (modeloConferenciaEncalhe){
		
			ConferenciaEncalheCont.carregarGridItensNotaFiscal(modeloConferenciaEncalhe);
			
			$('input[id*="qtdeInformadaFinalizarConf"]').numeric();
			$('input[id*="precoCapaFinalizarConf"]').numeric();
		}
		
		$("#somatorioQtdInformada").text(parseInt(result.qtdInformada));
		$("#somatorioQtdRecebida").text(parseInt(result.qtdRecebida));
		$("#somatorioTotal").text(parseFloat(result.valorPagar).toFixed(2));
	},
	
	popup_novo_encalhe: function () {
		
		ConferenciaEncalheCont.modalAberta = true;
		$(".message-dialog-encalhe > div").hide();
		$("#dialog-encalhe").dialog({
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
			}
		});

	},
	
	popup_alert : function() {
		
		ConferenciaEncalheCont.modalAberta = true;
		$(".message-dialog-encalhe > div").hide();
		$("#dialog-alert").dialog({
			resizable : false,
			height : 190,
			width : 460,
			modal : true,
			buttons : {
				"Sim" : function() {
					
					$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/carregarNotaFiscal', null, 
						function(result){
							
							$("#numNotaFiscal").val(result.numero);
							$("#serieNotaFiscal").val(result.serie);
							$("#dataNotaFiscal").val(result.dataEmissao);
						    $("#chaveAcessoNFE").val(result.chaveAcesso);
							
						    if (result.valorProdutos){
							    
						    	$("#valorNotaFiscal").val(parseFloat(result.valorProdutos).toFixed(2));
								
						    }
							
							$("#dialog-alert").dialog("close");
							ConferenciaEncalheCont.popup_notaFiscal();
						}
					);
				},
				"Não" : function() {
					
					$("#dialog-alert").dialog("close");
					$("#vlrCE").focus();
				}
			}, open : function(){
				
				$(this).parent('div').find('button:contains("Sim")').focus();
			}, close : function(){
				
				ConferenciaEncalheCont.modalAberta = false;
			}
		});
		
		$("#dialog-alert").show();
	},
	
	popup_notaFiscal: function () {
		
		ConferenciaEncalheCont.modalAberta = true;
		$(".message-dialog-encalhe > div").hide();
		$("#dialog-notaFiscal").dialog({
			resizable : false,
			height : 360,
			width : 750,
			modal : true,
			buttons : {
				
				"Confirmar" : function() {
					
					var data = []; 
					
					if($('input:radio[name=radioNFE]:checked').val() == 'S') {
						
						data = [
								{name : "notaFiscal.numero", value : $("#numNotaFiscal").val()},
								{name : "notaFiscal.serie", value : $("#serieNotaFiscal").val()},
								{name : "notaFiscal.dataEmissao", value : $("#dataNotaFiscal").val()},
								{name : "notaFiscal.valorProdutos", value : $("#valorNotaFiscal").val()},
								{name : "notaFiscal.chaveAcesso", value : $("#chaveAcessoNFE").val()}
						];
						
					} else {
						
						data = [
								{name : "notaFiscal.numero", value : $("#numNotaFiscal").val()},
								{name : "notaFiscal.serie", value : $("#serieNotaFiscal").val()},
								{name : "notaFiscal.dataEmissao", value : $("#dataNotaFiscal").val()},
								{name : "notaFiscal.valorProdutos", value : $("#valorNotaFiscal").val()}
						];
						
					}
					
					$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/salvarNotaFiscal', data, 
						function(result){
							
							$("#dialog-notaFiscal").dialog("close");
							
							$("#vlrCE").val(parseFloat($("#valorNotaFiscal").val()).toFixed(2));
							
							$("#vlrCE").focus();
						}, null, true, "dialog-notaFiscal"
					);
				},
				"Cancelar" : function() {
					$(this).dialog("close");
					$("#vlrCE").focus();
				}
			}, close : function(){
				
				ConferenciaEncalheCont.modalAberta = false;
			}
		});

	},

	popup_outros_valores: function () {
		
		ConferenciaEncalheCont.modalAberta = true;
		$(".message-dialog-encalhe > div").hide();
		$("#dialog-outros-valores").dialog({
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
			}
		});

	},
	
	popup_salvarInfos : function() {
		
		ConferenciaEncalheCont.modalAberta = true;
		$(".message-dialog-encalhe > div").hide();
		$("#dialog-salvar").dialog({
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
								      {name : 'numeroCota', value : $("#numeroCota").val()}, 
								      {name: 'indObtemDadosFromBD', value : true},
								      {name: 'indConferenciaContingencia', value: true}
								];
								
								ConferenciaEncalheCont.carregarListaConferencia(data);
								
							}						
							
							exibirMensagem(result.tipoMensagem, result.listaMensagens);
								
							$("#dialog-salvar").dialog("close");
					
						},null, true, "idModalConfirmarSalvarConf"
					);
				},
				"Cancelar" : function() {
					
					$(this).dialog("close");
				}

			}, close : function(){
				
				ConferenciaEncalheCont.modalAberta = false;
			}
		});

	}
};

$(function() {
	
	$(".outrosVlrsGrid").flexigrid({
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
	$(".message-dialog-encalhe > div").css('width', '93%');
	
	$("#numeroCota").numeric();
	$("#numeroCota").focus();
	$("#exemplaresNovoEncalhe").numeric();
	$("#vlrCE").numeric();
	$("#dataNotaFiscal").mask("99/99/9999");
	
	$("#numeroCota").keypress(function(e) {
		
		if (e.keyCode == 13) {
			
			ConferenciaEncalheCont.pesquisarCota();
		}
	});
	
	$("#lstProdutos").keypress(function(e){
		
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
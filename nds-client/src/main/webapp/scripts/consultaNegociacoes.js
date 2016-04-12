var consultaNegociacoesController = $.extend(true, {
//	negociacaoMap : null,
	init : function() {
		
		$('#consultaNegociacaoNumeroCota', consultaNegociacoesController.workspace).bind({
			keyup: function(){
				onlyNumeric(event);
			},

			change: function(){
				pesquisaCota.pesquisarPorNumeroCota('#consultaNegociacaoNumeroCota', '#consultaNegociacaoNomeCota');
			}
		});
		
		$("#consultaNegociacaoDataNegociacaoDe").datepicker({
			showOn: "button",
			buttonImage: contextPath + "/images/calendar.gif",
			buttonImageOnly: true
		});
		
		$("#consultaNegociacaoDataNegociacaoAte").datepicker({
			showOn: "button",
			buttonImage: contextPath + "/images/calendar.gif",
			buttonImageOnly: true
		});
		
		$("#consultaNegociacaoDataVencimentoDe").datepicker({
			showOn: "button",
			buttonImage: contextPath + "/images/calendar.gif",
			buttonImageOnly: true
		});
		
		$("#consultaNegociacaoDataVencimentoAte").datepicker({
			showOn: "button",
			buttonImage: contextPath + "/images/calendar.gif",
			buttonImageOnly: true
		});
		
		$("#cn_checknegociacaoAvulsa").change(function() {
			consultaNegociacoesController.atualizarFormaCobranca_popup($(this).is(":checked"));
		});
		
		$("#negociacoesGrid", consultaNegociacoesController.workspace).flexigrid({
			preProcess: consultaNegociacoesController.executarPreProcessamentoGridNegociacoes,
			dataType : 'json',
			colModel : [ {
				display : 'Cota',
				name : 'numeroCota',
				width : 50,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nomeCota',
				width : 150,
				sortable : true,
				align : 'left'
			}, {
				display : 'Status Cota',
				name : 'statusCota',
				width : 65,
				sortable : true,
				align : 'center'
			}, {
				display : 'Dívida Inicial',
				name : 'dividaInicial',
				width : 75,
				sortable : true,
				align : 'center'
			}, {
				display : 'Encargos',
				name : 'valorEncargos',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Dívida Neg.',
				name : 'dividaNegociada',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Dt. Negociação',
				name : 'dataNegociacao',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : 'Dt. Vencimento',
				name : 'dataVencimento',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : 'Parcela / %',
				name : 'parcela',
				width : 55,
				sortable : true,
				align : 'center'
			}, {
				display : 'Parcela R$',
				name : 'valorParcela',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Situação',
				name : 'situacaoParcela',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 80,
				sortable : false,
				align : 'center'
			} ],
			sortname : "dataNegociacao",
			sortorder : "desc",
			width : 950,
			height : 200,
			usepager : true,
			useRp : true,
			rp : 15,
			colMove: false,
			showToggleBtn: false,
			showTableToggleBtn : true
		});

	},
	
	valorSelecionadoSemEncargo: 0,
	valorEncargoSelecionado: 0,
	idCobrancaSelecionada: 0,
	idNegociacaoSelecionada: 0,
	
	executarPreProcessamentoGridNegociacoes : function (resultado){
		
		if (resultado.mensagens) {

			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			
			$(".gridNegociacoesDiv").hide();

			return resultado;
		}
		
		$.each(resultado.rows, function(index, row) {
			
			var botaoOpcoes = '<a href="javascript:;" nomeCota="'+row.cell.nomeCota+'" negociacaoId="'+row.cell.idNegociacao+'" cobrancaId="'+row.cell.idCobranca+'" statusCota="'+row.cell.statusCota+'" onclick="consultaNegociacoesController.popup_formaPgto('+row.cell.numeroCota+', '+row.cell.valorParcela+', this);" style="cursor:pointer" rel="tipsy" title="Detalhes">' +
			'<img src="' + contextPath + '/images/ico_detalhes.png" hspace="5" border="0" />'+	
			'</a>';
			
			var botaoImprimirNegociacao = '&nbsp;<a href="javascript:;" onclick="consultaNegociacoesController.imprimirNegociacao('+row.cell.idNegociacao+', '+row.cell.valorParcela+');" style="cursor:pointer" rel="tipsy" title="Imprimir Negociação">' +
			'<img src="' + contextPath + '/images/ico_impressora.gif" hspace="5" border="0" />'+	
			'</a>';
			
			var botaoImprimirBoletos = '&nbsp;<a href="javascript:;" onclick="consultaNegociacoesController.imprimirBoleto('+row.cell.idNegociacao+');" style="cursor:pointer" rel="tipsy" title="Imprimir Boletos">' +
			'<img src="' + contextPath + '/images/ico_impressora.gif" hspace="5" border="0" />'+	
			'</a>';
			
			row.cell.acao = botaoOpcoes+botaoImprimirNegociacao+botaoImprimirBoletos;
			
			if(row.cell.valorEncargos == undefined){
				row.cell.valorEncargos = 'isento';
			}
						
		});
		
		
		$(".areaBts").show();
		$("#negociacoesGrid", consultaNegociacoesController.workspace).show();
		
		return resultado;
	},
	
	pesquisarNegociacoes : function(){
		
		$(".gridNegociacoesDiv").show();
		
		$("#negociacoesGrid").flexOptions({
			url: contextPath + "/financeiro/consultaNegociacoes/buscarNegociacoes",
			dataType : 'json',
			params : consultaNegociacoesController.obterParametrosFiltro()
		});
		
		$("#negociacoesGrid").flexReload();	
		
	},
	
	obterParametrosFiltro : function(){
		
		var data = [];
		
		var cota = $("#consultaNegociacaoNumeroCota").val();
		var situacao = $("#consultaNegociacaoSituacaoParcela").val();
		var dtNegociacaoDe = $("#consultaNegociacaoDataNegociacaoDe").val();
		var dtNegociacaoAte = $("#consultaNegociacaoDataNegociacaoAte").val();
		var dtVencDe = $("#consultaNegociacaoDataVencimentoDe").val();
		var dtVencAte = $("#consultaNegociacaoDataVencimentoAte").val();
		
		data.push({name:'filtro.numeroCota', value: cota});
		data.push({name:'filtro.situacaoParcela', value: situacao});
		data.push({name:'filtro.dataNegociacaoDe', value: dtNegociacaoDe});
		data.push({name:'filtro.dataNegociacaoAte', value: dtNegociacaoAte});
		data.push({name:'filtro.dataVencimentoDe', value: dtVencDe});
		data.push({name:'filtro.dataVencimentoAte', value: dtVencAte});
		
		return data;
		
	},
	
	popup_formaPgto : function(numeroCota, valorParcela, elemento) {

		var nomeCota = elemento.attributes.getNamedItem('nomeCota').textContent;
		var statusCota = elemento.attributes.getNamedItem('statusCota').textContent;
		var idCobranca = elemento.attributes.getNamedItem('cobrancaId').textContent;
		var idNegociacao = elemento.attributes.getNamedItem('negociacaoId').textContent;
		
		$("#cn_formaPgtoForm")[0].reset();
		
		consultaNegociacoesController.limparPopupFormaPgto();
				
		$('span[name$="botoes"]').hide();
		
		
		$.postJSON(contextPath + '/financeiro/consultaNegociacoes/buscarDetalhesNegociacao',
				{'idNegociacao':idNegociacao}, 
				function(result) {
					
					if(result.tipoNegociacao == "COMISSAO"){
						
					}else{
						
						$('#cn_pagamentoEm', consultaNegociacoesController.workspace).attr("checked", true);
						
						$('#cn_checknegociacaoAvulsa', consultaNegociacoesController.workspace).attr("checked", result.negAvulsa);
						$('.cn_comissaoAtual', consultaNegociacoesController.workspace).hide();
						$('.cn_pgtos', consultaNegociacoesController.workspace).show();	
						
						$("#cn_selectPagamento").append("<option selected='selected'>" + result.tipoDePagamento + "</option>");
						
						consultaNegociacoesController.opcaoFormasPagto(result.tipoDePagamento); 
						
						$('#cn_radio'+result.tipoFormaCobranca, consultaNegociacoesController.workspace).attr("checked", true);
						
						if($('#cn_selectPagamento',consultaNegociacoesController.workspace).val() == 'CHEQUE'){
							consultaNegociacoesController.geraLinhasCheque(result.listParcelas);
						}else{
							consultaNegociacoesController.geraLinhasParcelas(result.listParcelas, result.ativaAoPagar);
						}
						
						$("#cn_selectBancosBoleto").append("<option selected='selected'>" + result.nomeBanco + "</option>");
						
						$('#cn_checkReceberEmail').attr("checked", result.isReceberPorEmail);
						
						$('#cn_isentaEncargos').attr("checked", result.isIsentaEncargos);
						
						$("#cn_selectParcelas").val(result.qtdParcelas);
						
						if(result.tipoDePagamento == 'BOLETO' || result.tipoDePagamento == 'BOLETO_EM_BRANCO') {
		            		$("#cn_botaoImprimirBoleto", consultaNegociacoesController.workspace).show();
		            		$("#cn_botaoImprimirBoleto", consultaNegociacoesController.workspace).append('<a href="javascript:;" onclick="consultaNegociacoesController.imprimirBoleto('+idNegociacao+');" >'+ 
		            				'<img src="'+contextPath+'/images/ico_impressora.gif" hspace="5" border="0" /> Imprimir Boletos </a>');
		            	} else {
		            		$("#cn_botaoImprimirRecibo", consultaNegociacoesController.workspace).show();
		            	}
						
		            	$("#cn_botaoImprimirNegociacao", consultaNegociacoesController.workspace).show();
		            	
		            	$("#cn_botaoImprimirNegociacao", consultaNegociacoesController.workspace).append('<a href="javascript:;" onclick="consultaNegociacoesController.imprimirNegociacao('+idNegociacao+', '+valorParcela+');">'+
		            			'<img src="'+contextPath+'/images/ico_impressora.gif" hspace="5" border="0" /> Imprimir Negociação </a>');
		            	
					}
					
					$('#cn_formaPgto_numEnomeCota').html('<strong>Cota:</strong> '+ numeroCota +' - <strong>Nome: </strong>'+ nomeCota+' - <strong>Status: </strong>'+ statusCota);
					$('#cn_dividaSelecionada').html(floatToPrice(valorParcela));
					$('#cn_valorSelecionado').val(floatToPrice(valorParcela));
					$('#cn_numeroCota').val(numeroCota);
	
					$("#cn_dialog-NegociacaoformaPgto").dialog({
						resizable: false,
						height:550,
						width:760,
						modal: true,
						buttons: {
							"Cancelar": function() {
								 $(this).dialog("close");
							}
						},
						open: function(event, ui) {
							
							$(this).keydown(function(event) {
								if (event.keyCode == $.ui.keyCode.ENTER) {
									event.stopPropagation();
									event.preventDefault();
								}
							});
							$(".negociacaoClass").disable(true);
						},
						beforeClose: function() {
							$(".negociacaoClass").removeAttr("disabled");
						},
					});
				},
				function(result) {
					
				});
				
		
//		if (valorParcela == "0,00") {
//			exibirMensagem("WARNING", ["Não há possibilidades de negociar este valor!"], "");
//			return;
//		}
//		
//		consultaNegociacoesController.valorSelecionadoSemEncargo = floatToPrice(valorParcela);
//		consultaNegociacoesController.idCobrancaSelecionada = idCobranca;
//		consultaNegociacoesController.idNegociacaoSelecionada = idNegociacao;
//		
//		$("#cn_formaPgtoForm")[0].reset();
//
//		consultaNegociacoesController.limparPopupFormaPgto();
//		
//		$('span[name$="botoes"]').hide();
//		
//		$.postJSON(contextPath + '/financeiro/negociacaoDivida/buscarComissaoCotaPorNumeroCota',
//			{'numeroCota':numeroCota}, 
//			function(result) {
//
//				if (isNaN(result[0])){
//					
//					$("#cn_negociacaoPorComissao",consultaNegociacoesController.workspace).attr("disabled", true);
//					$("#cn_negociacaoPorComissao-tr",consultaNegociacoesController.workspace).hide();
//					
//				} else {
////					var comissao = result[0];
//					
//					$("#cn_comissaoUtilizar",consultaNegociacoesController.workspace).val(floatToPrice(result[0]));
////					$("#cn_comissaoUtilizar",consultaNegociacoesController.workspace).val(floatToPrice(comissao));
//					
//					$("#cn_negociacaoPorComissao",consultaNegociacoesController.workspace).attr("disabled", false);
//					$("#cn_negociacaoPorComissao-tr",consultaNegociacoesController.workspace).show();
//				}
//
//				$("#cn_selectPagamento", consultaNegociacoesController.workspace).val(result[1]);
//				
//				$('#cn_formaPgto_numEnomeCota',consultaNegociacoesController.workspace).html('<strong>Cota:</strong> '+ numeroCota +' - <strong>Nome: </strong>'+ nomeCota+' - <strong>Status: </strong>'+ statusCota);
//				$('#cn_dividaSelecionada',consultaNegociacoesController.workspace).html(floatToPrice(valorParcela));
//				$('#cn_valorSelecionado',consultaNegociacoesController.workspace).val(floatToPrice(valorParcela));
//				$('#cn_numeroCota',consultaNegociacoesController.workspace).val(numeroCota);
//
//				$("#cn_dialog-NegociacaoformaPgto").dialog({
//					resizable: false,
//					height:550,
//					width:760,
//					modal: true,
//					buttons: {
//						"Confirmar": function() {
//							
//							consultaNegociacoesController.confirmarNegociacao();							
//						},
//						"Cancelar": function() {
//							 $("#cn_dialog-NegociacaoformaPgto", consultaNegociacoesController.workspace).dialog("close");
//						}
//					},
//					form: $("#cn_formaPgtoForm", consultaNegociacoesController.workspace),
//					close: function(event, ui) {
//						
//						consultaNegociacoesController.pesquisar();
//					},
//					open: function(event, ui) {
//						
//						$(this).keydown(function(event) {
//							if (event.keyCode == $.ui.keyCode.ENTER) {
//								event.stopPropagation();
//								event.preventDefault();
//							}
//						});
//						
//						consultaNegociacoesController.tratarSituacaoCota(consultaNegociacoesController.situacaoCota);
//					}
//				});
//			} ,
//			null
//		);
	},
	
	limparPopupFormaPgto : function() {
		
		$('.cn_comissaoAtual', consultaNegociacoesController.workspace).hide();
		
		consultaNegociacoesController.esconderPagamentoParcelas();
	},
	
	esconderPagamentoParcelas : function() {
		
		$('.cn_pgtos', consultaNegociacoesController.workspace).hide();
		$('.cn_semanal', consultaNegociacoesController.workspace).hide();
		$('.cn_quinzenal', consultaNegociacoesController.workspace).hide();
		$('.cn_mensal', consultaNegociacoesController.workspace).hide();
		$('#cn_gridVenctos', consultaNegociacoesController.workspace).hide();
		$('#cn_gridCheque', consultaNegociacoesController.workspace).hide();
//		$('#cn_divChequeDeposito', consultaNegociacoesController.workspace).hide();
		$('#cn_divBanco', consultaNegociacoesController.workspace).hide();
		$('#cn_checknegociacaoAvulsa', consultaNegociacoesController.workspace).attr("checked", false);
	},
	
	confirmarNegociacao : function(){
		
		var negociacaoPorComissao = $("#cn_negociacaoPorComissao", consultaNegociacoesController.workspace).is(":checked");
		var negociacaoAvulsa = $("#cn_checknegociacaoAvulsa", consultaNegociacoesController.workspace).is(":checked");

		var params = [
              {
            	  name: "porComissao", 
            	  value: negociacaoPorComissao
              },
              {
            	  name: "numeroCota", 
            	  value: $("#cn_numeroCota").val()
              },
              {
            	  name: "comissaoUtilizar",
            	  value: $("#cn_comissaoUtilizar", consultaNegociacoesController.workspace).val()
              },
              {
            	  name: "tipoCobranca",
            	  value: $("#cn_selectPagamento", consultaNegociacoesController.workspace).val()
              },
              {
            	  name: "tipoFormaCobranca",
            	  value: $("[name='filtro.periodicidade']:checked", consultaNegociacoesController.workspace).val()
              },
              {
            	  name: "diaInicio",
            	  value: $("#cn_diaInputQuinzenal1", consultaNegociacoesController.workspace).val() == "" ?
            			  $("#cn_mensalDia", consultaNegociacoesController.workspace).val() : 
            				   $("#cn_diaInputQuinzenal1", consultaNegociacoesController.workspace).val()
              },
              {
            	  name: "diaFim",
            	  value: $("#cn_diaInputQuinzenal2", consultaNegociacoesController.workspace).val()
              },
              {
            	  name: "negociacaoAvulsa",
            	  value: negociacaoAvulsa
              },
              {
            	  name: "isentaEncargos",
            	  value: $("#cn_isentaEncargos", consultaNegociacoesController.workspace).is(":checked")
              }
//              , TODO verificar necessidade
//              {
//            	  name: "ativarAposPagar",
//            	  value: $("[name='radioAtivarApos']:checked", consultaNegociacoesController.workspace).val() ? $("[name='radioAtivarApos']:checked", consultaNegociacoesController.workspace).val() : ""
//              }
		];
		
		$.each($("[name='cn_semanalDias']:checked", consultaNegociacoesController.workspace), function (index, value){
			params.push({
				name: 'diasSemana['+index+']',
				value: value.value
			});
		});
		
//		$.each($("[name='checkDividasSelecionadas']:checked", consultaNegociacoesController.workspace), function (index, value){
//			params.push({
//				name: 'idsCobrancas['+ index +']',
//				value: value.value
//			});
//		});
		
		params.push({
			name: 'idsCobrancas[0]',
			value: consultaNegociacoesController.idCobrancaSelecionada
		});
		
		var tipoPgto = $("#cn_selectPagamento", consultaNegociacoesController.workspace).val();
		
		if (tipoPgto == 'BOLETO' || 
				tipoPgto == 'BOLETO_EM_BRANCO' || 
				tipoPgto == 'DEPOSITO' ||
				tipoPgto == 'DINHEIRO' ||
				tipoPgto == 'OUTROS' ||  
				tipoPgto == 'TRANSFERENCIA_BANCARIA'){
			
			$.each($("[name='cn_vencimentoParcela']", consultaNegociacoesController.workspace), function(index, value){
				
				params.push(
					{
						name: "parcelas["+ index +"].dataVencimento",
						value: value.value
					},
					{
						name: "parcelas["+ index +"].encargos",
						value: $("[name=cn_encargoParcela]", consultaNegociacoesController.workspace)[index].value
					},
					{
						name: "parcelas["+ index +"].movimentoFinanceiroCota.valor",
						value: $("[name=cn_valorParcela]", consultaNegociacoesController.workspace)[index].value
					}
				);
			});
			
		} else if (tipoPgto == 'CHEQUE'){
			
			$.each($("[name=cn_vencimentoCheque]", consultaNegociacoesController.workspace), function(index, value){
				
				params.push(
					{
						name: "parcelas["+ index +"].dataVencimento",
						value: value.value
					},
					{
						name: "parcelas["+ index +"].numeroCheque",
						value: $("[name=numCheque]", consultaNegociacoesController.workspace)[index].value
					},
					{
						name: "parcelas["+ index +"].movimentoFinanceiroCota.valor",
						value: $("[name=valorCheque]", consultaNegociacoesController.workspace)[index].value
					}
				);
			});
		}
		
		var valorDividaComissao = $('#cn_dividaSelecionada', consultaNegociacoesController.wokspace).html();
		
		if ($("#isentaEncargos", consultaNegociacoesController.workspace).is(":checked")) {

			valorDividaComissao = consultaNegociacoesController.valorSelecionadoSemEncargo;
		}
		
		params.push({
			name: 'valorDividaComissao',
			value: valorDividaComissao
		});

		params.push({
			name: 'idBanco',
			value: $("#cn_selectBancosBoleto", consultaNegociacoesController.wokspace).val()
		});
		
		params.push({
			name: 'recebeCobrancaPorEmail',
			value: $('#cn_checkReceberEmail', consultaNegociacoesController.wokspace).is(":checked")
		});
		
		$.postJSON(contextPath + '/financeiro/negociacaoDivida/confirmarNegociacaoDivida',
			params, 
			function(result) {
			
	            if (result.tipoMensagem && result.listaMensagens) {
	                
	            	exibirMensagemDialog(result.tipoMensagem, result.listaMensagens);
	            }
	            
	            if(result.tipoMensagem=='SUCCESS') {
	            	
	            	$("#cn_botaoImprimirNegociacao", consultaNegociacoesController.workspace).show();
	            	
	            	if (!$("#cn_negociacaoPorComissao", consultaNegociacoesController.workspace).is(":checked")) {
	            			
	            		if($("#cn_checknegociacaoAvulsa", consultaNegociacoesController.workspace).is(":checked")) {
		            		
	            			if(tipoPgto == 'BOLETO' || tipoPgto == 'BOLETO_EM_BRANCO') {
		            		
			            		$("#cn_botaoImprimirBoleto", consultaNegociacoesController.workspace).show();
			            	} else {
			            		$("#cn_botaoImprimirRecibo", consultaNegociacoesController.workspace).show();
			            	}
	            		}
	            	
	            	}	

				} else {
	            	$("#cn_botaoImprimirRecibo", consultaNegociacoesController.workspace).hide();
	            	$("#cn_botaoImprimirNegociacao", consultaNegociacoesController.workspace).hide();
	            	$("#cn_botaoImprimirBoleto", consultaNegociacoesController.workspace).hide();
	            }
		
		});
	},
	
	geraLinhasParcelas : function(result, ativaAoPagar) {
		
		consultaNegociacoesController.parcelas = result;
		
		$('#cn_encargos').show();
		
		if($('#cn_selectPagamento').val() != ""){
			var tabela = $('#cn_tabelaParcelas').get(0);
			var totalParcela = 0;
			var totalEncargos = 0;
			var totalParcTotal = 0;
			
			
			$('#cn_header_table_Ativar', consultaNegociacoesController.workspace).hide();

			if(this.situacaoCota == 'ATIVO'){				
				$('#cn_header_table_Ativar', consultaNegociacoesController.workspace).hide();
			}else{
				$('#cn_header_table_Ativar', consultaNegociacoesController.workspace).show();
			}

			while(tabela.rows.length > 2){
				tabela.deleteRow(2);
			}

			for (var i=1; i <= result.length; i++){
				var linha = tabela.insertRow(i+1);
				var coluna1 = linha.insertCell(0);
				var coluna2 = linha.insertCell(1);
				var coluna3 = linha.insertCell(2);
				var coluna4 = linha.insertCell(3);
				var coluna5 = linha.insertCell(4);
								
				if(this.situacaoCota != 'ATIVO'){
					var coluna6 = linha.insertCell(5);
				}
				for (var j=0; j < tabela.rows[i+1].cells.length; j++){
					tabela.rows[i+1].cells[j].style.textAlign = "center";
				}

				coluna1.innerHTML = result[i-1].numParcela+'&ordf;';
				coluna2.innerHTML = '<input type="text" class="cn_dtVencDinam" name="cn_vencimentoParcela" id="cn_vencimentoParcela'+i+'" style="width: 70px;" value="'+result[i-1].dataVencimento+'" onchange="consultaNegociacoesController.recalcularParcelas('+(i-1)+',this,true)"/>';
				coluna3.innerHTML = '<input type="text" name="cn_valorParcela" id="cn_parcela'+i+'" style="width: 60px; text-align: right;" value="'+result[i-1].parcela+'" onchange="consultaNegociacoesController.recalcularParcelas('+(i-1)+',this, false)" />';
				coluna4.innerHTML = '<input type="text" name="cn_encargoParcela" id="cn_encargos'+i+'" style="width: 60px; text-align: right;" value="'+result[i-1].encargos+'" disabled="disabled"/>';
				coluna5.innerHTML = '<input type="text" name="cn_parcTotal" id="cn_parcTotal'+i+'" style="width: 60px; text-align: right;" value="'+result[i-1].parcTotal+'"/ disabled="disabled">';
				coluna6.innerHTML = '<input type="radio" name="radioAtivarApos" id="cn_ativarAoPagar'+i+'" value="'+ i +'" />';
				
				if(ativaAoPagar != undefined && ativaAoPagar == i){
					coluna6.innerHTML = '<input type="radio" checked="yes" name="radioAtivarApos" id="cn_ativarAoPagar'+i+'" value="'+ i +'" />';
				}else{
					coluna6.innerHTML = '<input type="radio" name="radioAtivarApos" id="cn_ativarAoPagar'+i+'" value="'+ i +'" />';
				}
				
				totalParcela = sumPrice(result[i-1].parcela, totalParcela);
				totalEncargos = sumPrice(result[i-1].encargos, totalEncargos);
				totalParcTotal = sumPrice(result[i-1].parcTotal, totalParcTotal);
				
			}
			
			var linha = tabela.insertRow(tabela.rows.length);
			
			linha.insertCell(0);
			linha.insertCell(1);
			
			var colunaParcela = linha.insertCell(2);
			var colunaEncargos = linha.insertCell(3);
			var colunaParcTotal = linha.insertCell(4);	
			
			linha.insertCell(5);
			
			colunaParcela.style.textAlign = "center";
			colunaParcela.innerHTML = 'R$ '+ totalParcela;
			colunaEncargos.style.textAlign = "center";
			colunaEncargos.innerHTML = 'R$ '+ totalEncargos;
			colunaParcTotal.style.textAlign = "center";
			colunaParcTotal.innerHTML = 'R$ ' + totalParcTotal;
			
			this.totalParcelas = totalParcTotal;
			
			consultaNegociacoesController.valorEncargoSelecionado =  totalEncargos;
			
			$(".cn_dtVencDinam", consultaNegociacoesController.workspace).mask("99/99/9999");
		}
	},
	
	geraLinhasCheque :function(result) {
		
		consultaNegociacoesController.parcelas = result;
		
		$('#cn_encargos').hide();
		
		if($('#cn_selectPagamento').val() != ""){
			
			var tabela = $('#cn_tabelaCheque').get(0);
			
			var totalParcela = 0;
			
			while(tabela.rows.length > 1){
				tabela.deleteRow(1);
			}
						
			$.each(result, function(i, row) {
				
				i++;
				
				var linha = tabela.insertRow(i);
				
				var coluna1 = linha.insertCell(0);
				var coluna2 = linha.insertCell(1);
				var coluna3 = linha.insertCell(2);
				var coluna4 = linha.insertCell(3);
				
				for (var j=0; j < tabela.rows[i].cells.length; j++){
					tabela.rows[i].cells[j].style.textAlign = "center";
				}
				
				coluna1.innerHTML = '<td><input class="cn_dtVencDinam" value="'+row.dataVencimento+'" type="text" name="cn_vencimentoCheque" id="cn_vencimentoCheque'+i+'"style="width:100px;" onchange="consultaNegociacoesController.recalcularParcelas('+(i-1)+',this,true)"/></td>';
				coluna2.innerHTML = '<td><input value="'+row.parcela+'" type="text" name="cn_valorCheque" id="cn_valor'+i+'" style="width:100px; text-align:right;" onchange="consultaNegociacoesController.recalcularParcelas('+(i-1)+',this, false)"/></td>';
				coluna3.innerHTML = '<td><input value="'+i+'" type="text" name="cn_numCheque" id="cn_numCheque'+i+'" style="width:100px;"/></td>';
				coluna4.innerHTML = '<td align="center"><a onclick="consultaNegociacoesController.excluirCheque('+i+')" href="javascript:;"><img src="'+contextPath+'/images/ico_excluir.gif" border="0" align="Excluir Linha" /></a></td>';
			
				totalParcela += parseFloat( formatMoneyValue(result[i-1].parcela) );
				
			});
			
			var linha = tabela.insertRow(tabela.rows.length);
			
			linha.insertCell(0);
			var colunaParcela = linha.insertCell(1);
			linha.insertCell(2);	
			
			linha.insertCell(3);
			
			colunaParcela.style.textAlign = "RIGHT";
			colunaParcela.innerHTML = '<div id="cn_totalCheque"> ' + 'R$ '+ floatToPrice(totalParcela.toFixed(2)) +'</div>';
			
			$(".cn_dtVencDinam", consultaNegociacoesController.workspace).mask("99/99/9999");
		}
	},
	
	recalcularParcelas:function(idParcela,input,porData){
		
		var parcela = consultaNegociacoesController.parcelas[idParcela];
		var parcelaValorAntigo = parcela.parcela; 
		var parcelaInput = $("#cn_parcela" + (++idParcela));
		
		if(!porData){			
			parcela.modificada = true;
			parcela.parcela = $(input).val();
		}else{
			parcela.modificada = true;
			parcela.dataVencimento = $(input).val();
		}
		
		var params = consultaNegociacoesController.getParamsCalcularParcelas();
		
		$.each(consultaNegociacoesController.parcelas, function(index, value){
			if(!value.ativarAoPagar){
				value.ativarAoPagar = "";
			}
			
			params.push(
				{
					name: "parcelas["+ index +"].dataVencimento",
					value: value.dataVencimento
				},
				{
					name: "parcelas["+ index +"].parcela",
					value: value.parcela
				},
				{
					name: "parcelas["+ index +"].ativarAoPagar",
					value: value.ativarAoPagar
				},
				{
					name: "parcelas["+ index +"].modificada",
					value: value.modificada
				},
				{
					name: "parcelas["+ index +"].numParcela",
					value: value.numParcela
				}
			);
		});
		
		$.postJSON(contextPath + '/financeiro/negociacaoDivida/recalcularParcelas.json',
				params,
				function(result) {
					if($('#cn_selectPagamento',consultaNegociacoesController.workspace).val() == 'CHEQUE'){
						consultaNegociacoesController.geraLinhasCheque(result);
					}else{
//						consultaNegociacoesController.geraLinhasParcelas(result);
					}
				}, function(result) {
					$(parcelaInput).val(parcelaValorAntigo);
					parcela.parcela = parcelaValorAntigo;
				}
		);		
	},
	
	getParamsCalcularParcelas : function() {
		
		var params = [];
		
		
		$.each($("[name=cn_semanalDias]:checked", consultaNegociacoesController.workspace), function (index, value){
			params.push({
				name: 'filtro.semanalDias['+index+'].numDia',
				value: value.value
			});
			
		});

		params.push({
			name: 'filtro.tipoPagamento',
			value: $("#cn_selectPagamento", consultaNegociacoesController.workspace).val()
		});
		
		params.push({
			name: 'filtro.isentaEncargos',
			value: $("#cn_isentaEncargos", consultaNegociacoesController.workspace).is(":checked")
		});

		params.push({
			name: 'filtro.valorSelecionado',
			value: $('#cn_valorSelecionado', consultaNegociacoesController.wokspace).val()
		});

		params.push({
            name: "filtro.periodicidade",
            value: $("[name='filtro.periodicidade']:checked", consultaNegociacoesController.workspace).val()
        });		
	
		params.push({
            name: "filtro.qntdParcelas",
            value: $('#cn_selectParcelas').val()
        });		
				
		params.push({
            name: "filtro.quinzenalDia1",
            value: $('#cn_diaInputQuinzenal1').val()
        });		
		
		params.push({
            name: "filtro.quinzenalDia2",
            value: $('#cn_diaInputQuinzenal2').val()
        });
		
		params.push({
            name: "filtro.mensalDia",
            value: $('#cn_mensalDia').val()
        });
		
		params.push({
            name: "filtro.idBanco",
            value: $('#cn_selectBancosBoleto').val()
        });
		
		params.push({
            name: "filtro.numeroCota",
            value: $('#cn_numeroCota', consultaNegociacoesController.workspace).val()
        });
		
		params.push({
            name: "filtro.valorSelecionadoSemEncargo",
            value: consultaNegociacoesController.valorSelecionadoSemEncargo
        });
		
		params.push({
            name: "filtro.valorEncargoSelecionado",
            value: consultaNegociacoesController.valorEncargoSelecionado
        });
		
		return params;
	},
	
	ativarAoPagarOnchange:function(idParcela,input){		
		var parcela = consultaNegociacoesController.parcelas[idParcela];
		parcela.ativarAoPagar = $(input).val();
	},
	
	excluirCheque : function(i) {
		$('#cn_tabelaCheque').get(0).deleteRow(i);
		consultaNegociacoesController.recalcularTotalCheque();
	},
	
	recalcularTotalCheque : function () {
		
		var total = '0,00';
		
		$('input[name$="valorCheque"]').each(function(){
			total = sumPrice(total,this.value);
		});
		
		$('#cn_totalCheque').html('R$ ' + total);
	},
	
	comissaoCota : function() {
		$('.cn_comissaoAtual', consultaNegociacoesController.workspace).show();
		
		consultaNegociacoesController.esconderPagamentoParcelas();		
	},
	
	esconderPagamentoParcelas : function() {
		
		$('.cn_pgtos', consultaNegociacoesController.workspace).hide();
		$('.cn_semanal', consultaNegociacoesController.workspace).hide();
		$('.cn_quinzenal', consultaNegociacoesController.workspace).hide();
		$('.cn_mensal', consultaNegociacoesController.workspace).hide();
		$('#cn_gridVenctos', consultaNegociacoesController.workspace).hide();
		$('#cn_gridCheque', consultaNegociacoesController.workspace).hide();
		$('#cn_divChequeDeposito', consultaNegociacoesController.workspace).hide();
		$('#cn_divBanco', consultaNegociacoesController.workspace).hide();
		$('#cn_checknegociacaoAvulsa', consultaNegociacoesController.workspace).attr("checked", false);
	},
	
	obterParcelasValorMinimo : function() {
		
		$.postJSON(contextPath + '/financeiro/negociacaoDivida/obterQuantidadeParcelas.json',
				consultaNegociacoesController.getParamsCalcularParcelas(),
				function(result) {
			
			        $('#cn_selectParcelas', consultaNegociacoesController.workspace).val(result.int);
					consultaNegociacoesController.opcaoFormasPagto($('#cn_selectPagamento', consultaNegociacoesController.workspace).val()); 
					consultaNegociacoesController.calcularParcelas();
			
				}
		);		
	},
	
	mostraPgto : function() {
		
		$('#cn_checknegociacaoAvulsa', consultaNegociacoesController.workspace).check();
		consultaNegociacoesController.atualizarFormaCobranca(true);
		consultaNegociacoesController.obterParcelasValorMinimo();
		
		$('.cn_comissaoAtual', consultaNegociacoesController.workspace).hide();
		$('.cn_pgtos', consultaNegociacoesController.workspace).show();		
	},
	
	mostraSemanal : function(){
		$('.cn_semanal', consultaNegociacoesController.workspace).show();
		$('.cn_quinzenal', consultaNegociacoesController.workspace).hide();
		$('.cn_mensal', consultaNegociacoesController.workspace).hide();
	},
	
	mostraMensal :function(){
		$('.cn_semanal', consultaNegociacoesController.workspace).hide();
		$('.cn_quinzenal', consultaNegociacoesController.workspace).hide();
		$('.cn_mensal', consultaNegociacoesController.workspace).show();
	},
	
	mostraDiario : function(){
		$('.cn_semanal', consultaNegociacoesController.workspace).hide();
		$('.cn_quinzenal', consultaNegociacoesController.workspace).hide();
		$('.cn_mensal', consultaNegociacoesController.workspace).hide();
	},
		
	mostraQuinzenal : function(){
		$('.cn_semanal', consultaNegociacoesController.workspace).hide();
		$('.cn_quinzenal', consultaNegociacoesController.workspace).show();
		$('.cn_mensal', consultaNegociacoesController.workspace).hide();
	},
	
	atualizarFormaCobranca: function(isNegociacaoAvulsa) {
		
		var numeroCota = $('#cn_numeroCota', consultaNegociacoesController.workspace).val();

		var params = {
			numeroCota: numeroCota,
			isNegociacaoAvulsa: isNegociacaoAvulsa
		};
		
		$.postJSON(
			contextPath + '/financeiro/negociacaoDivida/atualizarFormaCobranca',
			params,
			function(result) {

				$("#cn_selectPagamento").empty();
				$("#cn_selectBancosBoleto").empty();
				
				if (result && result.length > 0) {

					$.each(result, function(index, value) {
						
						consultaNegociacoesController.appendTipoPagamentoOption(value);

						consultaNegociacoesController.appendBancoOption(value);
					});

					var optSelected = $("#cn_selectPagamento option:selected");

					if (optSelected) {
						
						if (!isNegociacaoAvulsa) {
							
							consultaNegociacoesController.tipoCobrancaDefaultCota = $("#cn_selectPagamento").val();
						}
						
						consultaNegociacoesController.opcaoFormasPagto(optSelected.val()); 
						consultaNegociacoesController.calcularParcelas();
					}
				}
			} 
		);
	},
	
	appendTipoPagamentoOption: function(value) {
		
		var selected = value.tipoCobranca === consultaNegociacoesController.tipoCobrancaDefaultCota ? 'selected' : '';

		var optionExists = $("#cn_selectPagamento option").filter(function() {
			return $(this).attr('value') === value.tipoCobranca;
		}).length;

		if (!optionExists && value.tipoCobranca) {

			$("#cn_selectPagamento").append("<option value='" 
										+ value.tipoCobranca + "'" 
										+ selected + ">" 
										+ value.descricaoTipoCobranca 
										+ " </option>");
		}
	},

	appendBancoOption: function(value) {
		
		var optionExists = $("#cn_selectBancosBoleto option").filter(function() {
			return $(this).attr('value') == value.idBanco;
		}).length;
		
		if (!optionExists && value.idBanco) {
			
			$("#cn_selectBancosBoleto").append("<option value='" + value.idBanco + "'>" + value.nomeBanco + "</option>");
		}
	},
	
	opcaoFormasPagto : function(value){
		
		var dadosParcela = [
			'BOLETO',
			'BOLETO_EM_BRANCO',
			'DEPOSITO',
			'TRANSFERENCIA_BANCARIA',
			'DINHEIRO',
			'OUTROS'
		];
		
		var dadosBanco = [
			'DEPOSITO',
			'DINHEIRO'
		];

		if ($.inArray(value, dadosParcela) >= 0) {
			$('#cn_gridVenctos', consultaNegociacoesController.workspace).show();
			$('#cn_gridCheque', consultaNegociacoesController.workspace).hide();
			
			if($.inArray(value, dadosBanco) < 0){
				$('#cn_divChequeDeposito', consultaNegociacoesController.workspace).hide();
				$('#cn_divBanco', consultaNegociacoesController.workspace).show();
			} else {
				$('#cn_divBanco', consultaNegociacoesController.workspace).hide();
			}
		}else if (value == 'CHEQUE'){
			$('#cn_gridVenctos', consultaNegociacoesController.workspace).hide();
			$('#cn_gridCheque', consultaNegociacoesController.workspace).show();
			$('#cn_divBanco', consultaNegociacoesController.workspace).hide();

		}else {
			$('#cn_gridVenctos', consultaNegociacoesController.workspace).show();
			$('#cn_gridCheque', consultaNegociacoesController.workspace).hide();
			$('#cn_divChequeDeposito', consultaNegociacoesController.workspace).hide();
			$('#cn_divBanco', consultaNegociacoesController.workspace).hide();

		}
		
	},
	
	calcularParcelas : function(){
		
//		if ($("#cn_isentaEncargos", consultaNegociacoesController.workspace).is(":checked")
//				|| $('#cn_selectPagamento').val() === 'CHEQUE') {
//
//			$("#cn_dividaSelecionada").html(
//				consultaNegociacoesController.valorSelecionadoSemEncargo
//			);
//		
//		} else {
//
//			$("#cn_dividaSelecionada").html(
//				$("#cn_totalSelecionado", this.workspace).html()
//			);
//		}
		// TODO Verificar como será composto do valor da dívida + encargos
		
		$("#cn_dividaSelecionada").html(consultaNegociacoesController.valorSelecionadoSemEncargo);
			
		if($('#cn_selectPagamento').val() != ""){
			
			$.postJSON(contextPath + '/financeiro/negociacaoDivida/calcularParcelas.json',
					consultaNegociacoesController.getParamsCalcularParcelas(),
					function(result) {
						if($('#cn_selectPagamento').val() == 'CHEQUE'){
							consultaNegociacoesController.geraLinhasCheque(result);
						}else{
//							consultaNegociacoesController.geraLinhasParcelas(result);
						}
					}
			);
		}
	},
	
	calcularParcelasSemanal : function() {
		
		var semanalDias = $("[name=cn_semanalDias]:checked", consultaNegociacoesController.workspace);
		
		if (semanalDias.length > 0) {
			
			consultaNegociacoesController.calcularParcelas();
		}
	},
	
	calcularParcelasQuinzenal : function() {
		
		var quinzenalDia1 = parseInt($('#cn_diaInputQuinzenal1', consultaNegociacoesController.workspace).val());
		
		if (quinzenalDia1){
			if (quinzenalDia1 > 17){
				
				$('#cn_diaInputQuinzenal1', consultaNegociacoesController.workspace).val("17");
				$('#cn_diaInputQuinzenal2', consultaNegociacoesController.workspace).val("31");
			} else {
				
				$('#cn_diaInputQuinzenal2', consultaNegociacoesController.workspace).val(quinzenalDia1 + 14);
			}
			
			consultaNegociacoesController.calcularParcelas();
			
			$('#cn_mensalDia', consultaNegociacoesController.workspace).val("");
		}
	},
	
	calcularParcelasMensal : function() {
		
		var mensalDia = $('#cn_mensalDia', consultaNegociacoesController.workspace).val();
		
		if ($.trim(mensalDia)) {
			
			if (parseInt(mensalDia) > 31){
				
				 $('#cn_mensalDia', consultaNegociacoesController.workspace).val("31");
			}
			
			consultaNegociacoesController.calcularParcelas();
			
			$('#cn_diaInputQuinzenal1', consultaNegociacoesController.workspace).val("");
			$('#cn_diaInputQuinzenal2', consultaNegociacoesController.workspace).val("");
		}
	},
	
	atualizarFormaCobranca_popup : function(isNegociacaoAvulsa){
		
			if(!isNegociacaoAvulsa){
		
			$("<div>Os valores negociados serão somados na cobrança do jornaleiro no dia do vencimento!</div>", this.workspace).dialog({
				resizable: false,
				height:130,
				width:280,
				title: 'Negociação Avulsa',
				modal: true,
				buttons: {
					"Confirmar": function() {
						$('#cn_checknegociacaoAvulsa').prop('checked', false);																				
						$( this ).dialog( "close" );		
						consultaNegociacoesController.atualizarFormaCobranca(false);		
						
					},
					"Cancelar": function() {
						$('#cn_checknegociacaoAvulsa', consultaNegociacoesController.workspace).check();
						$( this ).dialog( "close" );
					}
				},
			});
		}else{
			consultaNegociacoesController.atualizarFormaCobranca(true);
		}
	},
	
	tratarSituacaoCota: function(situacaoCota) {
		
		var isInativa = situacaoCota == 'INATIVO';

		if (isInativa) {
			$('#cn_pagamentoEm', consultaNegociacoesController.workspace).click();
		}

		$('#cn_checknegociacaoAvulsa', consultaNegociacoesController.workspace).attr("checked", isInativa);

		$('#cn_checknegociacaoAvulsa', consultaNegociacoesController.workspace).attr("disabled", isInativa);

		$('#cn_negociacaoPorComissao', consultaNegociacoesController.workspace).attr("disabled", isInativa);
	},
	
	imprimirNegociacao : function(idNegociacao, vlrNegociacao) {
		
		var totalDivida = 0;
		
		if(!idNegociacao && consultaNegociacoesController.idNegociacaoSelecionada > 0){
			idNegociacao = consultaNegociacoesController.idNegociacaoSelecionada;
		}
		
//		var totalDivida = $('#totalSelecionado', negociacaoDividaController.wokspace).html();
		totalDivida = $('#cn_dividaSelecionada', consultaNegociacoesController.wokspace).html();
		
		
		if ($("#cn_isentaEncargos").is(":checked")) {
			totalDivida = negociacaoDividaController.valorSelecionadoSemEncargo;
		}
		
		if(totalDivida == 0 && vlrNegociacao > 0){
			totalDivida = vlrNegociacao;
		}
		
		var url = contextPath + '/financeiro/negociacaoDivida/imprimirNegociacaoDivida?valorDividaSelecionada=' + totalDivida +'&idNegociacao='+idNegociacao;
		window.open(url, '_blank');
	},
	
	imprimirBoleto : function(idNegociacao) {
		
		var url = contextPath + '/financeiro/negociacaoDivida/imprimirBoletosNegociacao?idNegociacao='+idNegociacao;
		window.open(url, '_blank');
	},
	
	impressaoBoletoDialog : function() {
		
		var idNegociacao = 
		
		negociacaoDividaController.imprimirBoleto();
	},

	
}, BaseController);
//@ sourceURL=consultaNegociacoes.js

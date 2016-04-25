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
	idNegociacaoSelecionada: 0,
	valorParcelaSelecionada: 0,
	
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
			
			var valor = row.cell.valorParcela != undefined ? row.cell.valorParcela : row.cell.dividaNegociada;
			
			var botaoOpcoes = '<a href="javascript:;" nomeCota="'+row.cell.nomeCota+'" negociacaoId="'+row.cell.idNegociacao+'" cobrancaId="'+row.cell.idCobranca+'" statusCota="'+row.cell.statusCota+'" onclick="consultaNegociacoesController.popup_formaPgto('+row.cell.numeroCota+', '+valor+', this);" style="cursor:pointer" rel="tipsy" title="Detalhes">' +
			'<img src="' + contextPath + '/images/ico_detalhes.png" hspace="5" border="0" />'+	
			'</a>';
			
			var botaoImprimirNegociacao = '&nbsp;<a href="javascript:;" onclick="consultaNegociacoesController.imprimirNegociacao('+row.cell.idNegociacao+', '+valor+');" style="cursor:pointer" rel="tipsy" title="Imprimir Negociação">' +
			'<img src="' + contextPath + '/images/ico_impressora.gif" hspace="5" border="0" />'+	
			'</a>';
			
			var botaoImprimirBoletos = '&nbsp;<a href="javascript:;" onclick="consultaNegociacoesController.imprimirBoleto('+row.cell.idNegociacao+');" style="cursor:pointer" rel="tipsy" title="Imprimir Boletos">' +
			'<img src="' + contextPath + '/images/ico_impressora.gif" hspace="5" border="0" />'+	
			'</a>';
			
			row.cell.acao = botaoOpcoes+botaoImprimirNegociacao+botaoImprimirBoletos;
			
			if(row.cell.valorEncargos == undefined){
				row.cell.valorEncargos = 'isento';
			}
			
			if(row.cell.dataVencimento == undefined){
				row.cell.dataVencimento = '';
			}
			
			if(row.cell.valorParcela == undefined){
				row.cell.valorParcela = '-';
			}
			
			if(row.cell.countParcelas == 0){
				row.cell.parcela = '-';
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
		
		consultaNegociacoesController.idNegociacaoSelecionada = idNegociacao;
		consultaNegociacoesController.valorParcelaSelecionada = valorParcela;
		
		$("#cn_formaPgtoForm")[0].reset();
		
		consultaNegociacoesController.limparPopupFormaPgto();
				
		$('span[name$="botoes"]').hide();
		
		
		$.postJSON(contextPath + '/financeiro/consultaNegociacoes/buscarDetalhesNegociacao',
				{'idNegociacao':idNegociacao, 'idCobranca': idCobranca}, 
				function(result) {
					
					if(result.tipoNegociacao == "COMISSAO"){
						$('#cn_negociacaoPorComissao').attr("checked", true);
						consultaNegociacoesController.comissaoCota();
						
						$('#cn_isentaEncargos').attr("checked", result.isIsentaEncargos);
						
						$("#cn_comissaoUtilizar").val(result.comissaoParaSaldoDivida);
						
						$("#divComissao").show();
						$('#divPagamento').hide();
						
					}else{
						
						$("#divComissao").hide();
						$('#divPagamento').show();
						
						$('#cn_pagamentoEm').attr("checked", true);
						
						$('#cn_checknegociacaoAvulsa').attr("checked", result.negAvulsa);
						$('.cn_pgtos').show();	
						
						$("#cn_selectPagamento").append("<option selected='selected'>" + result.tipoDePagamento + "</option>");
						
						consultaNegociacoesController.opcaoFormasPagto(result.tipoDePagamento); 
						
						$('#cn_radio'+result.tipoFormaCobranca).attr("checked", true);
						
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
		            		$("#cn_botaoImprimirBoleto").show();
		            	} else {
		            		$("#cn_botaoImprimirRecibo").show();
		            	}
						
		            	$("#cn_botaoImprimirNegociacao").show();
		            	
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
	},
	
	limparPopupFormaPgto : function() {
		
//		$('.cn_comissaoAtual', consultaNegociacoesController.workspace).hide();
		
		consultaNegociacoesController.esconderPagamentoParcelas();
	},
	
	esconderPagamentoParcelas : function() {
		
		$('.cn_pgtos', consultaNegociacoesController.workspace).hide();
		$('.cn_semanal', consultaNegociacoesController.workspace).hide();
		$('.cn_quinzenal', consultaNegociacoesController.workspace).hide();
		$('.cn_mensal', consultaNegociacoesController.workspace).hide();
		$('#cn_gridVenctos', consultaNegociacoesController.workspace).hide();
		$('#cn_gridCheque', consultaNegociacoesController.workspace).hide();
		$('#cn_divBanco', consultaNegociacoesController.workspace).hide();
		$('#cn_checknegociacaoAvulsa', consultaNegociacoesController.workspace).attr("checked", false);
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
				coluna2.innerHTML = '<input type="text" class="cn_dtVencDinam" name="cn_vencimentoParcela" id="cn_vencimentoParcela'+i+'" style="width: 70px;" value="'+result[i-1].dataVencimento+'" />';
				coluna3.innerHTML = '<input type="text" name="cn_valorParcela" id="cn_parcela'+i+'" style="width: 60px; text-align: right;" value="'+result[i-1].parcela+'" />';
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
				
				coluna1.innerHTML = '<td><input class="cn_dtVencDinam" value="'+row.dataVencimento+'" type="text" name="cn_vencimentoCheque" id="cn_vencimentoCheque'+i+'"style="width:100px;" /></td>';
				coluna2.innerHTML = '<td><input value="'+row.parcela+'" type="text" name="cn_valorCheque" id="cn_valor'+i+'" style="width:100px; text-align:right;" /></td>';
				coluna3.innerHTML = '<td><input value="'+i+'" type="text" name="cn_numCheque" id="cn_numCheque'+i+'" style="width:100px;"/></td>';
				coluna4.innerHTML = '<td align="center"><a href="javascript:;"><img src="'+contextPath+'/images/ico_excluir.gif" border="0" align="Excluir Linha" /></a></td>';
			
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
	
	mostraPgto : function() {
		
		$('#cn_checknegociacaoAvulsa', consultaNegociacoesController.workspace).check();
		
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
	
	imprimirNegociacao : function(idNegociacao, vlrNegociacao) {
		
		var totalDivida = 0;
		
		if(!idNegociacao && consultaNegociacoesController.idNegociacaoSelecionada > 0){
			idNegociacao = consultaNegociacoesController.idNegociacaoSelecionada;
		}
		
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
	
	imprimirRecibo : function(idNegociacao) {
		
		var url = contextPath + '/financeiro/negociacaoDivida/imprimirReciboConsultaNegociacao?idNegociacao='+idNegociacao;
		window.open(url, '_blank');
	},
	
	imprimirBoletoDialog : function() {
		consultaNegociacoesController.imprimirBoleto(consultaNegociacoesController.idNegociacaoSelecionada);
	},
	
	imprimirNegociacaoDialog : function() {
		consultaNegociacoesController.imprimirNegociacao(consultaNegociacoesController.idNegociacaoSelecionada, consultaNegociacoesController.valorParcelaSelecionada);
		
	},
	
	imprimirReciboDialog : function() {
		consultaNegociacoesController.imprimirRecibo(consultaNegociacoesController.idNegociacaoSelecionada);
	},

	
}, BaseController);
//@ sourceURL=consultaNegociacoes.js

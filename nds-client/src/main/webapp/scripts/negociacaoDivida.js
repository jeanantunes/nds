var negociacaoDividaController = $.extend(true, {

	path : contextPath + '/financeiro/negociacaoDivida/',
	
	situacaoCota: null,
	
	tipoCobrancaDefaultCota: null,

	init : function() {
		negociacaoDividaController.initGridNegociacao();
		negociacaoDividaController.initGridNegociacaoDetalhe();
		$("#negociacaoPorComissao", negociacaoDividaController.workspace).check();
		negociacaoDividaController.comissaoCota();
		
		$('#comissaoUtilizar', negociacaoDividaController.workspace).priceFormat({
			allowNegative: false,
			centsSeparator: ',',
		    thousandsSeparator: '.'
		});

		$("#checknegociacaoAvulsa").change(function() {
			negociacaoDividaController.atualizarFormaCobranca_popup($(this).is(":checked"));
		});

		$('#diaInputQuinzenal1', negociacaoDividaController.workspace).numeric();
		
		$('#diaInputQuinzenal2', negociacaoDividaController.workspace).numeric();
		
		$('#mensalDia', negociacaoDividaController.workspace).numeric();
		
		$("#negociacaoDivida_numCota", negociacaoDividaController.workspace).numeric();

		this.situacaoCota = null;		
	},
	
	atualizarFormaCobranca_popup : function(isNegociacaoAvulsa){
		
		//if (!$("#checknegociacaoAvulsa", negociacaoDividaController.workspace).is(":checked")){
			if(!isNegociacaoAvulsa){
		
			$("<div>Os valores negociados serão somados na cobrança do jornaleiro no dia do vencimento!</div>", this.workspace).dialog({
				resizable: false,
				height:130,
				width:280,
				title: 'Negociação Avulsa',
				modal: true,
				buttons: {
					"Confirmar": function() {
						$('#checknegociacaoAvulsa').prop('checked', false);																				
						$( this ).dialog( "close" );		
						negociacaoDividaController.atualizarFormaCobranca(false);		
						
					},
					"Cancelar": function() {
						$('#checknegociacaoAvulsa', negociacaoDividaController.workspace).check();
						//negociacaoDividaController.atualizarFormaCobranca(true);
						$( this ).dialog( "close" );
					}
				},
				//form: $("#dialog-detalhe", this.workspace).parents("form")
			});
			
		// exibirMensagem("WARNING", ["Os valores negociados serão somados na cobrança do jornaleiro no dia do vencimento!"], "");
		}else{
			negociacaoDividaController.atualizarFormaCobranca(true);
		}
	},
	
	atualizarFormaCobranca: function(isNegociacaoAvulsa) {
		
		var numeroCota = $("#negociacaoDivida_numCota").val();

		var params = {
			numeroCota: numeroCota,
			isNegociacaoAvulsa: isNegociacaoAvulsa
		};
		
		$.postJSON(
			contextPath + '/financeiro/negociacaoDivida/atualizarFormaCobranca',
			params,
			function(result) {

				$("#selectPagamento").empty();
				$("#selectBancosBoleto").empty();
				
				if (result && result.length > 0) {

					$.each(result, function(index, value) {
						
						negociacaoDividaController.appendTipoPagamentoOption(value);

						negociacaoDividaController.appendBancoOption(value);
					});

					var optSelected = $("#selectPagamento option:selected");

					if (optSelected) {
						
						if (!isNegociacaoAvulsa) {
							
							negociacaoDividaController.tipoCobrancaDefaultCota = $("#selectPagamento").val();
						}
						
						negociacaoDividaController.opcaoFormasPagto(optSelected.val()); 
						negociacaoDividaController.calcularParcelas();
					}
				}
			} 
		);
	},
 
	appendTipoPagamentoOption: function(value) {
		
		var selected = value.tipoCobranca === negociacaoDividaController.tipoCobrancaDefaultCota ? 'selected' : '';

		var optionExists = $("#selectPagamento option").filter(function() {
			return $(this).attr('value') === value.tipoCobranca;
		}).length;

		if (!optionExists && value.tipoCobranca) {

			$("#selectPagamento").append("<option value='" 
										+ value.tipoCobranca + "'" 
										+ selected + ">" 
										+ value.descricaoTipoCobranca 
										+ " </option>");
		}
	},

	appendBancoOption: function(value) {
		
		var optionExists = $("#selectBancosBoleto option").filter(function() {
			return $(this).attr('value') == value.idBanco;
		}).length;
		
		if (!optionExists && value.idBanco) {
			
			$("#selectBancosBoleto").append("<option value='" + value.idBanco + "'>" + value.nomeBanco + "</option>");
		}
	},
	
	
	pesquisarCota : function(numeroCota) {
		var self =  this;
		if (numeroCota || numeroCota != ''){
			negociacaoDividaController.esconderGridEBotoes();
			
			$.postJSON(contextPath + '/cadastro/cota/pesquisarPorNumero',
					{numeroCota:numeroCota}, 
					function(result) {
						$('#negociacaoDivida_statusCota').html(result.status);
						$('#negociacaoDivida_nomeCota').html(result.nome);
						self.situacaoCota =  result.situacaoCadastro;
						//if ($("#pagamentoEm", negociacaoDividaController.workspace).is(":checked")){
						  //negociacaoDividaController.atualizarFormaCobranca_popup(true);
						//} else{
						  //$('#checknegociacaoAvulsa', negociacaoDividaController.workspace).check();
						  //negociacaoDividaController.atualizarFormaCobranca(true);
						//}
					},
					function() {
						$('#negociacaoDivida_statusCota').html('');
						$('#negociacaoDivida_nomeCota').html('');
					}
			);
		}
	},
	
	esconderGridEBotoes : function() {
		
		$(".grids", this.workspace).hide();
		
		$(".areaBts", this.workspace).find("span").hide();		
	},
	
	pesquisar : function() {
		
		$("#negociacaoCheckAll", negociacaoDividaController.workspace).uncheck();
		$("#totalSelecionado", negociacaoDividaController.workspace).html('0,00');
		$('#negociacaoDivida_numEnomeCota').html($('#negociacaoDivida_numCota').val() +' - '+ $('#negociacaoDivida_nomeCota').html());
		
		negociacaoDividaController.tipoCobrancaDefaultCota = null;
		
		var params = $("#negociacaoDividaForm", this.workspace).serialize();
	
		$(".negociacaoGrid", this.workspace).flexOptions({
			url : this.path + 'pesquisar.json?' + params, 
			onSuccess: function() {bloquearItensEdicao(negociacaoDividaController.workspace);},
			preProcess : negociacaoDividaController.montaColunaDetalhesAcao,
			newp : 1
		});
		
		$(".negociacaoGrid").flexReload();
		
		$(".grids", this.workspace).show();
				
		$(".areaBts", this.workspace).find("span").show();
	},
	
	pesquisarDetalhes : function(idCobranca) {
		
		$.postJSON(
			this.path + 'pesquisarDetalhes.json', 
			[{name: 'idCobranca' , value: idCobranca}],
			function(result){
				
				$(".negociacaoDetalheGrid", negociacaoDividaController.workspace).flexAddData({
					page: 1, total: 1, rows: result[0].rows
				});
				
				$('#id_saldo', negociacaoDividaController.workspace).text(floatToPrice(result[1]));
			});
	},
	
	retornoPesquisaDetalhes : function(result) {
		
		$.each(result.rows, function(index, row) {
			
			row.cell.valor = floatToPrice(row.cell.valor);
			row.cell.data = row.cell.data.$;
		});
		
		return result;
	},
	
	montaColunaDetalhesAcao : function(data) {
		
		if (data.mensagens) {
			
			exibirMensagem(data.mensagens.tipoMensagem, 
						   data.mensagens.listaMensagens);
			
			negociacaoDividaController.esconderGridEBotoes();
			
			return data;
		}

		var total = 0;

		$.each(data.rows, function(index, value) {
			
			var detalhes = '<a href="javascript:;" onclick="negociacaoDividaController.popup_detalhe('+value.cell.idCobranca+');" title="Ver Detalhes"><img src="' + contextPath + '/images/ico_detalhes.png" alt="Detalhes" border="0" /></a>    ';
			var acao = '<input isEdicao="true" name="checkDividasSelecionadas" value="'+ value.cell.idCobranca +'" type="checkbox" class="negociacaoCheck" onclick="negociacaoDividaController.verificarCheck()"></input> ';
			value.cell.detalhes = detalhes;
			value.cell.acao = acao;

			total += parseFloat( formatMoneyValue(value.cell.total) ); 
			
			value.cell.total = floatToPrice(formatMoneyValue(value.cell.total));
			value.cell.vlDivida = floatToPrice(formatMoneyValue(value.cell.vlDivida));
			
		});

		$('#total', this.workspace).html(floatToPrice(total.toFixed(2)));

		return data;
	},
	
	calcularParcelasSemanal : function() {
		
		var semanalDias = $("[name=semanalDias]:checked", negociacaoDividaController.workspace);
		
		if (semanalDias.length > 0) {
			
			negociacaoDividaController.calcularParcelas();
		}
	},
	
	calcularParcelasQuinzenal : function() {
		
		var quinzenalDia1 = parseInt($('#diaInputQuinzenal1', negociacaoDividaController.workspace).val());
		
		if (quinzenalDia1){
			if (quinzenalDia1 > 17){
				
				$('#diaInputQuinzenal1', negociacaoDividaController.workspace).val("17");
				$('#diaInputQuinzenal2', negociacaoDividaController.workspace).val("31");
			} else {
				
				$('#diaInputQuinzenal2', negociacaoDividaController.workspace).val(quinzenalDia1 + 14);
			}
			
			negociacaoDividaController.calcularParcelas();
			
			$('#mensalDia', negociacaoDividaController.workspace).val("");
		}
	},
	
	calcularParcelasMensal : function() {
		
		var mensalDia = $('#mensalDia', negociacaoDividaController.workspace).val();
		
		if ($.trim(mensalDia)) {
			
			if (parseInt(mensalDia) > 31){
				
				 $('#mensalDia', negociacaoDividaController.workspace).val("31");
			}
			
			negociacaoDividaController.calcularParcelas();
			
			$('#diaInputQuinzenal1', negociacaoDividaController.workspace).val("");
			$('#diaInputQuinzenal2', negociacaoDividaController.workspace).val("");
		}
	},
	
	calcularParcelas : function(){
		
		if ($("#isentaEncargos", negociacaoDividaController.workspace).is(":checked")
				|| $('#selectPagamento').val() === 'CHEQUE') {

			$("#dividaSelecionada").html(
				negociacaoDividaController.valorSelecionadoSemEncargo
			);
		
		} else {

			$("#dividaSelecionada").html(
				$("#totalSelecionado", this.workspace).html()
			);
		}
			
		if($('#selectPagamento').val() != ""){
			
			$.postJSON(contextPath + '/financeiro/negociacaoDivida/calcularParcelas.json',
					negociacaoDividaController.getParamsCalcularParcelas(),
					function(result) {
						if($('#selectPagamento').val() == 'CHEQUE'){
							negociacaoDividaController.geraLinhasCheque(result);
						}else{
							negociacaoDividaController.geraLinhasParcelas(result);
						}
					}
			);
		}
	},
	
	getParamsCalcularParcelas : function() {
		
		var params = [];
		
		
		$.each($("[name=semanalDias]:checked", negociacaoDividaController.workspace), function (index, value){
			params.push({
				name: 'filtro.semanalDias['+index+'].numDia',
				value: value.value
			});
			
		});

		params.push({
			name: 'filtro.tipoPagamento',
			value: $("#selectPagamento", negociacaoDividaController.workspace).val()
		});
		
		params.push({
			name: 'filtro.isentaEncargos',
			value: $("#isentaEncargos", negociacaoDividaController.workspace).is(":checked")
		});

		params.push({
			name: 'filtro.valorSelecionado',
			value: $('#dividaSelecionada', negociacaoDividaController.wokspace).html()
		});

		params.push({
            name: "filtro.periodicidade",
            value: $("[name='filtro.periodicidade']:checked", negociacaoDividaController.workspace).val()
        });		
	
		params.push({
            name: "filtro.qntdParcelas",
            value: $('#selectParcelas').val()
        });		
				
		params.push({
            name: "filtro.quinzenalDia1",
            value: $('#diaInputQuinzenal1').val()
        });		
		
		params.push({
            name: "filtro.quinzenalDia2",
            value: $('#diaInputQuinzenal2').val()
        });
		
		params.push({
            name: "filtro.mensalDia",
            value: $('#mensalDia').val()
        });
		
		params.push({
            name: "filtro.idBanco",
            value: $('#selectBancosBoleto').val()
        });
		
		params.push({
            name: "filtro.numeroCota",
            value: $('#negociacaoDivida_numCota').val()
        });
		
		params.push({
            name: "filtro.valorSelecionadoSemEncargo",
            value: negociacaoDividaController.valorSelecionadoSemEncargo
        });
		
		params.push({
            name: "filtro.valorEncargoSelecionado",
            value: negociacaoDividaController.valorEncargoSelecionado
        });
		
		return params;
	},
	
	popup_detalhe : function(idCobranca) {
		this.pesquisarDetalhes(idCobranca);
		$("#dialog-detalhe", this.workspace).dialog({
			resizable: false,
			height:420,
			width:700,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
				}
			},
			form: $("#dialog-detalhe", this.workspace).parents("form")
		});
	},
	
	popup_formaPgto : function() {

		if ($("#totalSelecionado", this.workspace).html() == "0,00") {
			exibirMensagem("WARNING", ["Não foram selecionadas dívidas para negociação."], "");
			return;
		}
		
		$("#formaPgtoForm")[0].reset();

		negociacaoDividaController.limparPopupFormaPgto();
		
		$('span[name$="botoes"]').hide();
		
		$.postJSON(contextPath + '/financeiro/negociacaoDivida/buscarComissaoCota',
			null, 
			function(result) {

				if (isNaN(result[0])){
					
					$("#negociacaoPorComissao",negociacaoDividaController.workspace).attr("disabled", true);
					$("#negociacaoPorComissao-tr",negociacaoDividaController.workspace).hide();
					
				} else {
					
					$("#comissaoUtilizar",negociacaoDividaController.workspace).val(floatToPrice(result[0]));
					
					$("#negociacaoPorComissao",negociacaoDividaController.workspace).attr("disabled", false);
					$("#negociacaoPorComissao-tr",negociacaoDividaController.workspace).show();
				}

				$("#selectPagamento", negociacaoDividaController.workspace).val(result[1]);

				$('#formaPgto_numEnomeCota',negociacaoDividaController.workspace).html('<strong>Cota:</strong> ' + $('#negociacaoDivida_numCota',negociacaoDividaController.workspace).val() +' - <strong>Nome: </strong>'+ $('#negociacaoDivida_nomeCota').html()+' - <strong>Status: </strong>'+ $('#negociacaoDivida_statusCota').html());
				$('#dividaSelecionada',negociacaoDividaController.workspace).html($('#totalSelecionado',negociacaoDividaController.workspace).html());
				$('#valorSelecionado',negociacaoDividaController.workspace).val(priceToFloat($('#totalSelecionado',negociacaoDividaController.workspace).html()));
				$('#numeroCota',negociacaoDividaController.workspace).val($('#negociacaoDivida_numCota',negociacaoDividaController.workspace).val());

				$("#dialog-NegociacaoformaPgto").dialog({
					resizable: false,
					height:550,
					width:760,
					modal: true,
					buttons: {
						"Confirmar": function() {
							
							negociacaoDividaController.confirmarNegociacao();							
						},
						"Cancelar": function() {
							 $("#dialog-NegociacaoformaPgto", negociacaoDividaController.workspace).dialog("close");
						}
					},
					form: $("#formaPgtoForm", negociacaoDividaController.workspace),
					close: function(event, ui) {
						
						negociacaoDividaController.pesquisar();
					},
					open: function(event, ui) {
						
						$(this).keydown(function(event) {
							if (event.keyCode == $.ui.keyCode.ENTER) {
								event.stopPropagation();
								event.preventDefault();
							}
						});
						
						negociacaoDividaController.tratarSituacaoCota(negociacaoDividaController.situacaoCota);
					}
				});
			} ,
			null
		);
	},
	
	limparCampos : function() {
		
	},
	
	confirmarNegociacao : function(){
		
		var negociacaoPorComissao = $("#negociacaoPorComissao", negociacaoDividaController.workspace).is(":checked");
		var negociacaoAvulsa = $("#checknegociacaoAvulsa", negociacaoDividaController.workspace).is(":checked");

		var params = [
              {
            	  name: "porComissao", 
            	  value: negociacaoPorComissao
              },
              {
            	  name: "comissaoUtilizar",
            	  value: $("#comissaoUtilizar", negociacaoDividaController.workspace).val()
              },
              {
            	  name: "tipoCobranca",
            	  value: $("#selectPagamento", negociacaoDividaController.workspace).val()
              },
              {
            	  name: "tipoFormaCobranca",
            	  value: $("[name='filtro.periodicidade']:checked", negociacaoDividaController.workspace).val()
              },
              {
            	  name: "diaInicio",
            	  value: $("#diaInputQuinzenal1", negociacaoDividaController.workspace).val() == "" ?
            			  $("#mensalDia", negociacaoDividaController.workspace).val() : 
            				   $("#diaInputQuinzenal1", negociacaoDividaController.workspace).val()
              },
              {
            	  name: "diaFim",
            	  value: $("#diaInputQuinzenal2", negociacaoDividaController.workspace).val()
              },
              {
            	  name: "negociacaoAvulsa",
            	  value: negociacaoAvulsa
              },
              {
            	  name: "isentaEncargos",
            	  value: $("#isentaEncargos", negociacaoDividaController.workspace).is(":checked")
              },
              {
            	  name: "ativarAposPagar",
            	  value: $("[name='radioAtivarApos']:checked", negociacaoDividaController.workspace).val() ? $("[name='radioAtivarApos']:checked", negociacaoDividaController.workspace).val() : ""
              }
		];
		
		$.each($("[name='semanalDias']:checked", negociacaoDividaController.workspace), function (index, value){
			params.push({
				name: 'diasSemana['+index+']',
				value: value.value
			});
		});
		
		$.each($("[name='checkDividasSelecionadas']:checked", negociacaoDividaController.workspace), function (index, value){
			params.push({
				name: 'idsCobrancas['+ index +']',
				value: value.value
			});
		});
		
		var tipoPgto = $("#selectPagamento", negociacaoDividaController.workspace).val();
		
		if (tipoPgto == 'BOLETO' || 
				tipoPgto == 'BOLETO_EM_BRANCO' || 
				tipoPgto == 'DEPOSITO' ||
				tipoPgto == 'DINHEIRO' ||
				tipoPgto == 'OUTROS' ||  
				tipoPgto == 'TRANSFERENCIA_BANCARIA'){
			
			$.each($("[name='vencimentoParcela']", negociacaoDividaController.workspace), function(index, value){
				
				params.push(
					{
						name: "parcelas["+ index +"].dataVencimento",
						value: value.value
					},
					{
						name: "parcelas["+ index +"].encargos",
						value: $("[name=encargoParcela]", negociacaoDividaController.workspace)[index].value
					},
					{
						name: "parcelas["+ index +"].movimentoFinanceiroCota.valor",
						value: $("[name=valorParcela]", negociacaoDividaController.workspace)[index].value
					}
				);
			});
			
		} else if (tipoPgto == 'CHEQUE'){
			
			$.each($("[name=vencimentoCheque]", negociacaoDividaController.workspace), function(index, value){
				
				params.push(
					{
						name: "parcelas["+ index +"].dataVencimento",
						value: value.value
					},
					{
						name: "parcelas["+ index +"].numeroCheque",
						value: $("[name=numCheque]", negociacaoDividaController.workspace)[index].value
					},
					{
						name: "parcelas["+ index +"].movimentoFinanceiroCota.valor",
						value: $("[name=valorCheque]", negociacaoDividaController.workspace)[index].value
					}
				);
			});
		}
		
		var valorDividaComissao = $('#totalSelecionado', negociacaoDividaController.wokspace).html();
		
		if ($("#isentaEncargos", negociacaoDividaController.workspace).is(":checked")) {

			valorDividaComissao = negociacaoDividaController.valorSelecionadoSemEncargo;
		}

		params.push({
			name: 'valorDividaComissao',
			value: valorDividaComissao
		});

		params.push({
			name: 'idBanco',
			value: $("#selectBancosBoleto", negociacaoDividaController.wokspace).val()
		});
		
		params.push({
			name: 'recebeCobrancaPorEmail',
			value: $('#checkReceberEmail', negociacaoDividaController.wokspace).is(":checked")
		});
		
		$.postJSON(contextPath + '/financeiro/negociacaoDivida/confirmarNegociacao',
			params, 
			function(result) {
			
	            if (result.tipoMensagem && result.listaMensagens) {
	                
	            	exibirMensagemDialog(result.tipoMensagem, result.listaMensagens);
	            }
	            
	            if(result.tipoMensagem=='SUCCESS') {
	            	
	            	$("#botaoImprimirNegociacao", negociacaoDividaController.workspace).show();
	            	
	            	if (!$("#negociacaoPorComissao", negociacaoDividaController.workspace).is(":checked")) {
	            			
	            		if($("#checknegociacaoAvulsa", negociacaoDividaController.workspace).is(":checked")) {
		            		
	            			if(tipoPgto == 'BOLETO' || tipoPgto == 'BOLETO_EM_BRANCO') {
		            		
			            		$("#botaoImprimirBoleto", negociacaoDividaController.workspace).show();
			            	} else {
			            		$("#botaoImprimirRecibo", negociacaoDividaController.workspace).show();
			            	}
	            		}
	            	
	            	}	

				} else {
	            	$("#botaoImprimirRecibo", negociacaoDividaController.workspace).hide();
	            	$("#botaoImprimirNegociacao", negociacaoDividaController.workspace).hide();
	            	$("#botaoImprimirBoleto", negociacaoDividaController.workspace).hide();
	            }
		
		});
	},

	imprimirNegociacao : function() {
		
		var totalDivida = $('#totalSelecionado', negociacaoDividaController.wokspace).html();
		
		if ($("#isentaEncargos").is(":checked")) {
			totalDivida = negociacaoDividaController.valorSelecionadoSemEncargo;
		}

		var url = contextPath + '/financeiro/negociacaoDivida/imprimirNegociacao?valorDividaSelecionada=' + totalDivida;
		window.open(url, '_blank');
	},

	geraLinhasCheque :function(result) {
		
		negociacaoDividaController.parcelas = result;
		
		$('#encargos').hide();
		
		if($('#selectPagamento').val() != ""){
			
			var tabela = $('#tabelaCheque').get(0);
			
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
				
				coluna1.innerHTML = '<td><input class="dtVencDinam" value="'+row.dataVencimento+'" type="text" name="vencimentoCheque" id="vencimentoCheque'+i+'"style="width:100px;" onchange="negociacaoDividaController.recalcularParcelas('+(i-1)+',this,true)"/></td>';
				coluna2.innerHTML = '<td><input value="'+row.parcela+'" type="text" name="valorCheque" id="valor'+i+'" style="width:100px; text-align:right;" onchange="negociacaoDividaController.recalcularParcelas('+(i-1)+',this, false)"/></td>';
				coluna3.innerHTML = '<td><input value="'+i+'" type="text" name="numCheque" id="numCheque'+i+'" style="width:100px;"/></td>';
				coluna4.innerHTML = '<td align="center"><a onclick="negociacaoDividaController.excluirCheque('+i+')" href="javascript:;"><img src="'+contextPath+'/images/ico_excluir.gif" border="0" align="Excluir Linha" /></a></td>';
			
				totalParcela += parseFloat( formatMoneyValue(result[i-1].parcela) );
				
			});
			
			var linha = tabela.insertRow(tabela.rows.length);
			
			linha.insertCell(0);
			var colunaParcela = linha.insertCell(1);
			linha.insertCell(2);	
			
			linha.insertCell(3);
			
			colunaParcela.style.textAlign = "RIGHT";
			colunaParcela.innerHTML = '<div id="totalCheque"> ' + 'R$ '+ floatToPrice(totalParcela.toFixed(2)) +'</div>';
			
			$(".dtVencDinam", negociacaoDividaController.workspace).mask("99/99/9999");
		}
	},
	
	prev: function(event) {
		event.preventDefault();
	},
	
	recalcularTotalCheque : function () {
		
		var total = '0,00';
		
		$('input[name$="valorCheque"]').each(function(){
			total = sumPrice(total,this.value);
		});
		
		$('#totalCheque').html('R$ ' + total);
	},
	
	excluirCheque : function(i) {
		$('#tabelaCheque').get(0).deleteRow(i);
		negociacaoDividaController.recalcularTotalCheque();
	},
	
	geraLinhasParcelas : function(result) {
		
		negociacaoDividaController.parcelas = result;
		
		$('#encargos').show();
		
		if($('#selectPagamento').val() != ""){
			var tabela = $('#tabelaParcelas').get(0);
			var totalParcela = 0;
			var totalEncargos = 0;
			var totalParcTotal = 0;
			
			
			$('#header_table_Ativar', negociacaoDividaController.workspace).hide();

			if(this.situacaoCota == 'ATIVO'){				
				$('#header_table_Ativar', negociacaoDividaController.workspace).hide();
			}else{
				$('#header_table_Ativar', negociacaoDividaController.workspace).show();
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
				coluna2.innerHTML = '<input type="text" class="dtVencDinam" name="vencimentoParcela" id="vencimentoParcela'+i+'" style="width: 70px;" value="'+result[i-1].dataVencimento+'" onchange="negociacaoDividaController.recalcularParcelas('+(i-1)+',this,true)"/>';
				coluna3.innerHTML = '<input type="text" name="valorParcela" id="parcela'+i+'" style="width: 60px; text-align: right;" value="'+result[i-1].parcela+'" onchange="negociacaoDividaController.recalcularParcelas('+(i-1)+',this, false)" />';
				coluna4.innerHTML = '<input type="text" name="encargoParcela" id="encargos'+i+'" style="width: 60px; text-align: right;" value="'+result[i-1].encargos+'" disabled="disabled"/>';
				coluna5.innerHTML = '<input type="text" name="parcTotal" id="parcTotal'+i+'" style="width: 60px; text-align: right;" value="'+result[i-1].parcTotal+'"/ disabled="disabled">';
				
				if(this.situacaoCota != 'ATIVO'){
					coluna6.innerHTML = '<input type="radio" name="radioAtivarApos" id="ativarAoPagar'+i+'" value="'+ i +'" onchange="negociacaoDividaController.ativarAoPagarOnchange('+(i-1)+',this)" />';
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
			
			$(".dtVencDinam", negociacaoDividaController.workspace).mask("99/99/9999");
		}
	},
	
	tratarSituacaoCota: function(situacaoCota) {
	
		var isInativa = situacaoCota == 'INATIVO';

		if (isInativa) {
			$('#pagamentoEm', negociacaoDividaController.workspace).click();
		}

		$('#checknegociacaoAvulsa', negociacaoDividaController.workspace).attr("checked", isInativa);

		$('#checknegociacaoAvulsa', negociacaoDividaController.workspace).attr("disabled", isInativa);

		$('#negociacaoPorComissao', negociacaoDividaController.workspace).attr("disabled", isInativa);
	},

	ativarAoPagarOnchange:function(idParcela,input){		
		var parcela = negociacaoDividaController.parcelas[idParcela];
		parcela.ativarAoPagar = $(input).val();
	},
	
	recalcularParcelas:function(idParcela,input,porData){
		var parcela = negociacaoDividaController.parcelas[idParcela];
		var parcelaValorAntigo = parcela.parcela; 
		var parcelaInput = $("#parcela" + (++idParcela));
		if(!porData){			
			parcela.modificada = true;
			parcela.parcela = $(input).val();
		}else{
			parcela.modificada = true;
			parcela.dataVencimento = $(input).val();
		}
		var params = negociacaoDividaController.getParamsCalcularParcelas();
		$.each(negociacaoDividaController.parcelas, function(index, value){
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
					if($('#selectPagamento',negociacaoDividaController.workspace).val() == 'CHEQUE'){
						negociacaoDividaController.geraLinhasCheque(result);
					}else{
						negociacaoDividaController.geraLinhasParcelas(result);
					}
				}, function(result) {
					$(parcelaInput).val(parcelaValorAntigo);
					parcela.parcela = parcelaValorAntigo;
				}
		);		
	},

	limparPopupFormaPgto : function() {
	
		$('.comissaoAtual', negociacaoDividaController.workspace).hide();
		
		negociacaoDividaController.esconderPagamentoParcelas();
	},
	
	esconderPagamentoParcelas : function() {
		
		$('.pgtos', negociacaoDividaController.workspace).hide();
		$('.semanal', negociacaoDividaController.workspace).hide();
		$('.quinzenal', negociacaoDividaController.workspace).hide();
		$('.mensal', negociacaoDividaController.workspace).hide();
		$('#gridVenctos', negociacaoDividaController.workspace).hide();
		$('#gridCheque', negociacaoDividaController.workspace).hide();
		$('#divChequeDeposito', negociacaoDividaController.workspace).hide();
		$('#divBanco', negociacaoDividaController.workspace).hide();
		$('#checknegociacaoAvulsa', negociacaoDividaController.workspace).attr("checked", false);
	},
	
	comissaoCota : function() {
		$('.comissaoAtual', negociacaoDividaController.workspace).show();

		negociacaoDividaController.esconderPagamentoParcelas();		
	},
	
	obterParcelasValorMinimo : function() {
		
		$.postJSON(contextPath + '/financeiro/negociacaoDivida/obterQuantidadeParcelas.json',
				negociacaoDividaController.getParamsCalcularParcelas(),
				function(result) {
			
			        $('#selectParcelas', negociacaoDividaController.workspace).val(result.int);
					negociacaoDividaController.opcaoFormasPagto($('#selectPagamento', negociacaoDividaController.workspace).val()); 
					negociacaoDividaController.calcularParcelas();
			
				}
		);		
	},
	
	mostraPgto : function() {
		
		$('#checknegociacaoAvulsa', negociacaoDividaController.workspace).check();
		negociacaoDividaController.atualizarFormaCobranca(true);
		negociacaoDividaController.obterParcelasValorMinimo();
		
		$('.comissaoAtual', negociacaoDividaController.workspace).hide();
		$('.pgtos', negociacaoDividaController.workspace).show();		
	},
	
	mostraSemanal : function(){
		$('.semanal', negociacaoDividaController.workspace).show();
		$('.quinzenal', negociacaoDividaController.workspace).hide();
		$('.mensal', negociacaoDividaController.workspace).hide();
	},
	
	mostraMensal :function(){
		$('.semanal', negociacaoDividaController.workspace).hide();
		$('.quinzenal', negociacaoDividaController.workspace).hide();
		$('.mensal', negociacaoDividaController.workspace).show();
	},
	
	mostraDiario : function(){
		$('.semanal', negociacaoDividaController.workspace).hide();
		$('.quinzenal', negociacaoDividaController.workspace).hide();
		$('.mensal', negociacaoDividaController.workspace).hide();
	},
		
	mostraQuinzenal : function(){
		$('.semanal', negociacaoDividaController.workspace).hide();
		$('.quinzenal', negociacaoDividaController.workspace).show();
		$('.mensal', negociacaoDividaController.workspace).hide();
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
			$('#gridVenctos', negociacaoDividaController.workspace).show();
			$('#gridCheque', negociacaoDividaController.workspace).hide();
			
			if($.inArray(value, dadosBanco) < 0){
				$('#divChequeDeposito', negociacaoDividaController.workspace).hide();
				$('#divBanco', negociacaoDividaController.workspace).show();
			} else {
				$('#divBanco', negociacaoDividaController.workspace).hide();
			}
		}else if (value == 'CHEQUE'){
			$('#gridVenctos', negociacaoDividaController.workspace).hide();
			$('#gridCheque', negociacaoDividaController.workspace).show();
			$('#divBanco', negociacaoDividaController.workspace).hide();

		}else {
			$('#gridVenctos', negociacaoDividaController.workspace).show();
			$('#gridCheque', negociacaoDividaController.workspace).hide();
			$('#divChequeDeposito', negociacaoDividaController.workspace).hide();
			$('#divBanco', negociacaoDividaController.workspace).hide();

		}
		
	},
	
	valorSelecionadoSemEncargo: 0,
	valorEncargoSelecionado: 0,
	
	verificarCheck : function() {
		
		negociacaoDividaController.valorSelecionadoSemEncargo = 0;
		negociacaoDividaController.valorEncargoSelecionado = 0;
		
		var todosChecados = true;
		var totalSelecionado = $("#totalSelecionado", this.workspace);
		totalSelecionado.html('0,00');
		$(".negociacaoCheck", this.workspace).each(function(index, element) {	
			var total = $('td[abbr="total"] >div', element.parentNode.parentNode.parentNode);
			var vlrDividaSemEncargo = $('td[abbr="vlDivida"] >div', element.parentNode.parentNode.parentNode);
			var vlrEncargo = $('td[abbr="encargos"] >div', element.parentNode.parentNode.parentNode);
			if (!element.checked) {
				todosChecados = false;
			}
			if(element.checked){
				totalSelecionado.html(sumPrice(totalSelecionado.html(), total.html()));
				
				negociacaoDividaController.valorSelecionadoSemEncargo = 
					sumPrice(vlrDividaSemEncargo.html(), negociacaoDividaController.valorSelecionadoSemEncargo);
				
				negociacaoDividaController.valorEncargoSelecionado = 
					sumPrice(vlrEncargo.html(), negociacaoDividaController.valorEncargoSelecionado);
			}
			
		});
		
		$("#negociacaoCheckAll", negociacaoDividaController.workspace).get(0).checked = todosChecados;
	},
	
	
	checkAll : function (check) {
		
		$(".negociacaoCheck", negociacaoDividaController.workspace).each(function(index, element) {
			element.checked = check.checked;
		});
		negociacaoDividaController.verificarCheck();
	},
	
	initGridNegociacao : function() {
		$(".negociacaoGrid", negociacaoDividaController.workspace).flexigrid({
			dataType : 'json',
			colModel : [  {
				display : 'Data Emiss&atilde;o',
				name : 'dtEmissao',
				width : 150,
				sortable : true,
				align : 'center'
			}, {
				display : 'Data Vencimento',
				name : 'dtVencimento',
				width : 150,
				sortable : true,
				align : 'center'
			}, {
				display : 'Prazo',
				name : 'prazo',
				width : 100,
				sortable : true,
				align : 'center'
			}, {
				display : 'Valor Divida R$',
				name : 'vlDivida',
				width : 140,
				sortable : true,
				align : 'right'
			}, {
				display : 'Encargos',
				name : 'encargos',
				width : 100,
				sortable : true,
				align : 'center',
			}, {
				display : 'Total R$',
				name : 'total',
				width : 100,
				sortable : true,
				align : 'right',
			}, {
				display : 'Detalhes',
				name : 'detalhes',
				width : 60,
				sortable : true,
				align : 'center',
			}, {
				display : '',
				name : 'acao',
				width : 40,
				sortable : false,
				align : 'center',
			}],
			sortname : "dtEmissao",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			showTableToggleBtn : true,
			width : 960,
			height : 180
		});
	},
	
	initGridNegociacaoDetalhe : function() {
		$(".negociacaoDetalheGrid", negociacaoDividaController.workspace).flexigrid({
			dataType : 'json',
			preProcess : negociacaoDividaController.retornoPesquisaDetalhes,
			colModel : [ {
				display : 'Data',
				name : 'data',
				width : 90,
				sortable : true,
				align : 'center'
			},{
				display : 'R$',
				name : 'valor',
				width : 60,
				sortable : true,
				align : 'right'
			},  {
				display : 'Observa&ccedil;&atilde;o',
				name : 'observacao',
				width : 400,
				sortable : true,
				align : 'left'
			}],
			width : 620,
			height : 160
		});		
	},

}, BaseController);

//@ sourceURL=negociacaoDivida.js

var negociacaoDividaController = $.extend(true, {

	path : contextPath + '/financeiro/negociacaoDivida/',

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
	},

	pesquisarCota : function(numeroCota) {

		$(".grids", this.workspace).hide();
		
		$.postJSON(contextPath + '/cadastro/cota/pesquisarPorNumero',
				{numeroCota:numeroCota}, 
				function(result) {
					$('#negociacaoDivida_statusCota').html(result.status);
					$('#negociacaoDivida_nomeCota').html(result.nome);
				},
				function() {
					$('#negociacaoDivida_statusCota').html('');
					$('#negociacaoDivida_nomeCota').html('');
				}
				
		);
	},
	
	pesquisar : function() {
		
		$("#totalSelecionado", this.workspace).html('0,00');
		$('#negociacaoDivida_numEnomeCota').html($('#negociacaoDivida_numCota').val() +' - '+ $('#negociacaoDivida_nomeCota').html());
		
		var params = $("#negociacaoDividaForm", this.workspace).serialize();
	
		$(".negociacaoGrid", this.workspace).flexOptions({
			url : this.path + 'pesquisar.json?' + params, 
			preProcess : negociacaoDividaController.montaColunaDetalhesAcao,
			newp : 1
		});
		
		$(".negociacaoGrid").flexReload();
		
		$(".grids", this.workspace).show();
				
		
	},
	
	pesquisarDetalhes : function(idCobranca) {
		
		$(".negociacaoDetalheGrid", this.workspace).flexOptions({
			url : this.path + 'pesquisarDetalhes.json?' , 
			params: [{name: 'idCobranca' , value: idCobranca}],
			newp : 1,
			preProcess: negociacaoDividaController.retornoPesquisaDetalhes
		});
			
		$(".negociacaoDetalhesGrid").flexReload();
		
		
	},
	
	retornoPesquisaDetalhes : function(result) {
		
		var saldo = 0.0;
		
		$.each(result.rows, function(index, row) {
			saldo += row.cell.valorDouble;
		});
		
		$('#id_saldo').text(floatToPrice(saldo.toFixed(2)));
		
		return result;
	},
	
	utilizarAlterado : function() {
		
		var perc = priceToFloat( $('#comissaoUtilizar').val());
		
		
		
		var atual = priceToFloat( $('#comissaoAtualCota').val());
		
		var vlrFinal = atual - perc;
		
		$('#comissaoComSaldo').val( replaceAll(  vlrFinal.toFixed(2) , ".", ","));
	},
	
	montaColunaDetalhesAcao : function(data) {
		
		var total = '0,00';
		
		$.each(data.rows, function(index, value) {
			
			var detalhes = '<a href="javascript:;" onclick="negociacaoDividaController.popup_detalhe('+value.cell.idCobranca+');" title="Ver Detalhes"><img src="' + contextPath + '/images/ico_detalhes.png" alt="Detalhes" border="0" /></a>    ';
			var acao = '<input name="checkDividasSelecionadas" value="'+ value.cell.idCobranca +'" type="checkbox" class="negociacaoCheck" onclick="negociacaoDividaController.verificarCheck()"></input> ';
			value.cell.detalhes = detalhes;
			value.cell.acao = acao;
			
			total = sumPrice(value.cell.total, total);
		});

		$('#total', this.workspace).html(total);
		
		
		return data;
	},
	
	calcularParcelas : function(){
		if($('#selectPagamento').val() != ""){
			
			$.postJSON(contextPath + '/financeiro/negociacaoDivida/calcularParcelas.json',
					negociacaoDividaController.getParamsCalcularParcelas(),
					function(result) {
						if($('#selectPagamento').val() == 'CHEQUE'){
							negociacaoDividaController.geraLinhasCheque(result);
						}else{
							negociacaoDividaController.geraLinhasParcelas(result);
						}
					},
					function() {
						
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
			value: priceToFloat($('#dividaSelecionada', negociacaoDividaController.wokspace).html())
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
				
		return params;
	},
	
	popup_detalhe : function(idCobranca) {
		negociacaoDividaController.pesquisarDetalhes(idCobranca);
		$(".negociacaoDetalheGrid").flexReload();
		$("#dialog-detalhe").dialog({
			resizable: false,
			height:420,
			width:700,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	},
	
	popup_formaPgto : function() {
		
		if ($("#totalSelecionado", this.workspace).html() == "0,00") {
			exibirMensagem("WARNING", ["Não foram selecionadas dívidas para negociação."], "")
			return;
		}
		
		$('span[name$="botoes"]').hide();
		
		$.postJSON(contextPath + '/financeiro/negociacaoDivida/buscarComissaoCota',
			null, 
			function(result) {
			
				$("#comissaoAtualCota",negociacaoDividaController.workspace).val(floatToPrice(result));
				
				$('#formaPgto_numEnomeCota',negociacaoDividaController.workspace).html('<strong>Cota:</strong> ' + $('#negociacaoDivida_numCota',negociacaoDividaController.workspace).val() +' - <strong>Nome: </strong>'+ $('#negociacaoDivida_nomeCota').html());
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
					 form: $("#formaPgtoForm", negociacaoDividaController.workspace)
	
				});
			} ,
			null
		);
	},
	
	confirmarNegociacao : function(){
		
		var params = [
              {
            	  name: "porComissao", 
            	  value: $("#negociacaoPorComissao", negociacaoDividaController.workspace).is(":checked")
              },
              {
            	  name: "comissaoAtualCota",
            	  value: priceToFloat($("#comissaoAtualCota", negociacaoDividaController.workspace).val())
              },
              {
            	  name: "comissaoUtilizar",
            	  value: priceToFloat($("#comissaoUtilizar", negociacaoDividaController.workspace).val())
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
            	  value: $("#diaInputQuinzenal1", negociacaoDividaController.workspace).val()
              },
              {
            	  name: "diaFim",
            	  value: $("#diaInputQuinzenal2", negociacaoDividaController.workspace).val()
              },
              {
            	  name: "negociacaoAvulsa",
            	  value: $("#checknegociacaoAvulsa", negociacaoDividaController.workspace).is(":checked")
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
				tipoPgto == 'TRANSFERENCIA_BANCARIA'){
			
			$.each($("[name='vencimentoParcela']", negociacaoDividaController.workspace), function(index, value){
				
				params.push(
					{
						name: "parcelas["+ index +"].dataVencimento",
						value: value.value
					},
					{
						name: "parcelas["+ index +"].encargos",
						value: priceToFloat($("[name=encargoParcela]", negociacaoDividaController.workspace)[index].value)
					},
					{
						name: "parcelas["+ index +"].movimentoFinanceiroCota.valor",
						value: priceToFloat($("[name=valorParcela]", negociacaoDividaController.workspace)[index].value)
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
						value: priceToFloat($("[name=numCheque]", negociacaoDividaController.workspace)[index].value)
					},
					{
						name: "parcelas["+ index +"].movimentoFinanceiroCota.valor",
						value: priceToFloat($("[name=valorCheque]", negociacaoDividaController.workspace)[index].value)
					}
				);
			});
		}
		
		params.push({
			name: 'idBanco',
			value: $("#selectBancosBoleto", negociacaoDividaController.wokspace).val()
		});
		
		params.push({
			name: 'valorDividaComissao',
			value: priceToFloat($('#totalSelecionado', negociacaoDividaController.wokspace).html())
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
	            	$('span[name$="botoes"]').show();
	            } else {
	            	$('span[name$="botoes"]').hide();
	            }
			}
		
		);
	},
		
	geraLinhasCheque :function(result) {
		
		$('#encargos').hide();
		
		if($('#selectPagamento').val() != ""){
			
			var tabela = $('#tabelaCheque').get(0);
			
			var totalParcela = '0,00';
			
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
				
				coluna1.innerHTML = '<td><input value="'+row.dataVencimento+'" type="text" name="vencimentoCheque" id="vencimentoCheque'+i+'"style="width:100px;" readonly="readonly"/></td>';
				coluna2.innerHTML = '<td><input value="'+row.parcela+'" type="text" name="valorCheque" id="valor'+i+'" style="width:100px; text-align:right;" onchange="negociacaoDividaController.recalcularTotalCheque()"/></td>';
				coluna3.innerHTML = '<td><input value="'+i+'" type="text" name="numCheque" id="numCheque'+i+'"  style="width:100px;" readonly="readonly"/></td>';
				coluna4.innerHTML = '<td align="center"><a onclick="negociacaoDividaController.excluirCheque('+i+')" href="javascript:;"><img src="'+contextPath+'/images/ico_excluir.gif" border="0" align="Excluir Linha" /></a></td>';
			
				totalParcela = sumPrice(result[i-1].parcela, totalParcela);
				
			});
			
			var linha = tabela.insertRow(tabela.rows.length);
			
			linha.insertCell(0);
			var colunaParcela = linha.insertCell(1);
			linha.insertCell(2);	
			
			linha.insertCell(3);
			
			colunaParcela.style.textAlign = "RIGHT";
			colunaParcela.innerHTML = '<div id="totalCheque"> ' + 'R$ '+totalParcela + '</div>';
			
		}
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
		
		$('#encargos').show();
		
		if($('#selectPagamento').val() != ""){
			var tabela = $('#tabelaParcelas').get(0);
			var totalParcela = '0,00';
			var totalEncargos = '0,00';
			var totalParcTotal = '0,00';
			
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
				var coluna6 = linha.insertCell(5);
				
				for (var j=0; j < tabela.rows[i+1].cells.length; j++){
					tabela.rows[i+1].cells[j].style.textAlign = "center";
				}

				coluna1.innerHTML = result[i-1].numParcela+'&ordf;';
				coluna2.innerHTML = '<input type="text" name="vencimentoParcela" id="vencimentoParcela'+i+'" style="width: 70px;" value="'+result[i-1].dataVencimento+'"/>';
				coluna3.innerHTML = '<input type="text" name="valorParcela" id="parcela'+i+'" style="width: 60px; text-align: right;" value="'+result[i-1].parcela+'"/>';
				coluna4.innerHTML = '<input type="text" name="encargoParcela" id="encargos'+i+'" style="width: 60px; text-align: right;" value="'+result[i-1].encargos+'"/>';
				coluna5.innerHTML = '<input type="text" name="parcTotal" id="parcTotal'+i+'" style="width: 60px; text-align: right;" value="'+result[i-1].parcTotal+'"/>';
				coluna6.innerHTML = '<input type="radio" name="radioAtivarApos" id="ativarAoPagar'+i+'" value="'+ i +'" />';
				
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
			colunaParcela.innerHTML = 'R$ '+totalParcela;
			colunaEncargos.style.textAlign = "center";
			colunaEncargos.innerHTML = 'R$ '+totalEncargos;
			colunaParcTotal.style.textAlign = "center";
			colunaParcTotal.innerHTML = 'R$ '+totalParcTotal;
			
		}
	},

	comissaoCota : function() {
		$('.comissaoAtual', negociacaoDividaController.workspace).show();
		$('.pgtos', negociacaoDividaController.workspace).hide();
		$('.semanal', negociacaoDividaController.workspace).hide();
		$('.quinzenal', negociacaoDividaController.workspace).hide();
		$('.mensal', negociacaoDividaController.workspace).hide();
		$('#gridVenctos', negociacaoDividaController.workspace).hide();
		$('#gridCheque', negociacaoDividaController.workspace).hide();
		$('#divChequeDeposito', negociacaoDividaController.workspace).hide();
		$('#divBanco', negociacaoDividaController.workspace).hide();
		
	},
	
	mostraPgto : function() {
		
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
		
		
		if (value == 'BOLETO' || value == 'BOLETO_EM_BRANCO' || value == 'DEPOSITO' || value == 'TRANSFERENCIA_BANCARIA'){
			$('#gridVenctos', negociacaoDividaController.workspace).show();
			$('#gridCheque', negociacaoDividaController.workspace).hide();
			
			if(value != 'DEPOSITO'){
				$('#divChequeDeposito', negociacaoDividaController.workspace).hide();
				$('#divBanco', negociacaoDividaController.workspace).show();
			}
		}else if (value == 'CHEQUE'){
			$('#gridVenctos', negociacaoDividaController.workspace).hide();
			$('#gridCheque', negociacaoDividaController.workspace).show();
			$('#divBanco', negociacaoDividaController.workspace).show();

		}else {
			$('#gridVenctos', negociacaoDividaController.workspace).hide();
			$('#gridCheque', negociacaoDividaController.workspace).hide();
			$('#divChequeDeposito', negociacaoDividaController.workspace).hide();
			$('#divBanco', negociacaoDividaController.workspace).hide();

		}
	},
	
	verificarCheck : function() {
		
		var todosChecados = true;
		var totalSelecionado = $("#totalSelecionado", this.workspace);
		totalSelecionado.html('0,00');
		$(".negociacaoCheck", this.workspace).each(function(index, element) {	
			var total = $('td[abbr="total"] >div', element.parentNode.parentNode.parentNode);
			if (!element.checked) {
				todosChecados = false;
			}
			if(element.checked){
				totalSelecionado.html(sumPrice(totalSelecionado.html(), total.html()));
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
			colModel : [ {
				display : 'Data',
				name : 'data',
				width : 90,
				sortable : true,
				align : 'center'
			},{
				display : ' ',
				name : 'tipo',
				width : 80,
				sortable : true,
				align : 'left'
			},{
				display : 'R$',
				name : 'valor',
				width : 60,
				sortable : true,
				align : 'right'
			},  {
				display : 'Observa&ccedil;&atilde;o',
				name : 'observacao',
				width : 320,
				sortable : true,
				align : 'left'
			}],
			width : 620,
			height : 160
		});		
	},

}, BaseController);

//@ sourceURL=negociacaoDivida.js

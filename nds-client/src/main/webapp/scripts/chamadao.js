var chamadaoController = $.extend(true, {
	
	checkAll: false,
	
	nonSelected: [],
	
	parciais: {
		
		qtdProdutosParcial: 0,
		qtdExemplaresParcial: 0,
		valorParcial: 0
		
	},
	
	ACAO_TELA: "PESQUISAR",
	
	init : function() {
		var followUp = $('#numeroCotaFollowUp', chamadaoController.workspace).val();
		
		chamadaoController.inicializar();
		if(followUp != ''){			
			chamadaoController.pesquisar();
		}
	},
	
	getQueryString : function() {
		var result = {}; 
		var queryString = location.search.substring(1);
		var re = /([^&=]+)=([^&]*)/g;
		var m;

		while (m = re.exec(queryString)) {
			result[decodeURIComponent(m[1])] = decodeURIComponent(m[2]);
		}

		return result;
	},

	popularGridPeloFollowUp : function(numeroCota, dataChamadaoFormatada){
				
		
		$(".chamadaoGrid", chamadaoController.workspace).flexOptions({
			url: contextPath + "/devolucao/chamadao/pesquisarConsignados",
			onSuccess: function() {
				
				var checkAllSelected = chamadaoController.verifyCheckAll();
				
				if (checkAllSelected) {
					
					$("input[name='checkConsignado']", chamadaoController.workspace).each(function() {
						
						this.checked = true;
					});
				}
			},
			params: [
		         {name:'numeroCota', value: numeroCota},
		         {name:'dataChamadaoFormatada', value: dataChamadaoFormatada},
		         {name:'idFornecedor', value: idFornecedor}
		    ],
		    newp: 1,
		});
		
		$(".chamadaoGrid", chamadaoController.workspace).flexReload();

	},
	
	iniciarGrid : function() {
		
		$(".chamadaoGrid", chamadaoController.workspace).flexigrid({
			preProcess: chamadaoController.executarPreProcessamento,
			postProcess: function() { alert('oi');} ,
			dataType : 'json',
			colModel : [ {
				display : 'Código',
				name : 'codigo',
				width : 55,
				sortable : true,
				align : 'center'
			}, {
				display : 'Produto',
				name : 'produto',
				width : 85,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'edicao',
				width : 50,
				sortable : true,
				align : 'center'
			}, {
				display : 'Brinde',
				name : 'brinde',
				width : 30,
				sortable : true,
				align : 'right'
			}, {
				display : 'Preço Capa R$',
				name : 'precoVenda',
				width : 80,
				sortable : true,
				align : 'right'
			}, {
				display : 'Preço c/ Desc. R$',
				name : 'precoDesconto',
				width : 70,
				sortable : true,
				align : 'right'
			}, {
				display : 'Reparte',
				name : 'reparte',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Fornecedor',
				name : 'fornecedor',
				width : 75,
				sortable : true,
				align : 'left'
			}, {
				display : 'Recolhimento',
				name : 'dataRecolhimento',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : 'Valor R$',
				name : 'valorTotal',
				width : 70,
				sortable : true,
				align : 'right'
			}, {
				display : 'Valor c/ desconto R$',
				name : 'valorTotalDesconto',
				width : 90,
				sortable : true,
				align : 'right'
			}, {
				display : ' ',
				name : 'sel',
				width : 40,
				sortable : false,
				align : 'center'
			}, {
				display: '',
				name: 'lancamentoHidden',
				width: 0,
				sortable: false
			}],
			sortname : "codigo",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 180
		});
	},
	
	iniciarData : function() {
		
		$("#dataChamadao", chamadaoController.workspace).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true,
			defaultDate: new Date()
		});
		
		$("#novaDataChamadao", chamadaoController.workspace).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true,
			defaultDate: new Date()
		});
		
		$("#dataChamadao", chamadaoController.workspace).mask("99/99/9999");
		
		$("#novaDataChamadao", chamadaoController.workspace).mask("99/99/9999");
	},
	
	inicializar : function() {
		
		chamadaoController.iniciarGrid();
		
		chamadaoController.iniciarData();
		
		$(".area", chamadaoController.workspace).hide();
		
		$("#numeroCotaChamadao", chamadaoController.workspace).focus();
		
		$("#descricaoCota", chamadaoController.workspace).autocomplete({source: ""});

		
		if(chamadaoController.getQueryString()["carregarGrid"] == true) {
			
			var numeroCota = chamadaoController.getQueryString()["numeroCota"];
			var dataChamadaoFormatada = chamadaoController.getQueryString()["data"];
			popularGridPeloFollowUp(numeroCota,dataChamadaoFormatada);
		}
	},
	
	validarMatrizRecolhimentoConfirmada : function() {
		
		var followUp = $('#numeroCotaFollowUp', chamadaoController.workspace).val();
		
		var dataChamadaoFormatada;
		
		if (followUp != '') {
			
			dataChamadaoFormatada = $("#dataCotaFollowUp", chamadaoController.workspace).val();
			
		} else {
			
			dataChamadaoFormatada = $("#dataChamadao", chamadaoController.workspace).val();
		}
		
		$.postJSON(
			contextPath + "/devolucao/chamadao/validarMatrizRecolhimentoConfirmada",
			{'dataPesquisa': dataChamadaoFormatada},
			function(result) {
				
				if (result) {
					
					chamadaoController.popupUsoMatrizRecolhimentoConfirmada();
					
				} else {
					
					chamadaoController.pesquisar();
				}
			}
		);
	},
	
	popupUsoMatrizRecolhimentoConfirmada : function() {
		
		$("#dialogUsoMatrizRecolhimentoConfirmada", chamadaoController.workspace).dialog({
			resizable: false,
			height:'auto',
			width:300,
			modal: true,
			buttons: 
			{
				"Confirmar": function() {
					
					chamadaoController.pesquisar();
					
					$(this).dialog("close");
				
				}, "Cancelar": function() {
					
					$(this).dialog("close");
				}
			},
			form: $("#dialogUsoMatrizRecolhimentoConfirmada", this.workspace).parents("form")			
		});
		
		$("#dialogUsoMatrizRecolhimentoConfirmada", chamadaoController.workspace).show();
	},
		
	pesquisar : function() {
		
		chamadaoController.ACAO_TELA = "PESQUISAR";
		
		dataHolder.clearAction('chamadaoHolder');
		
		chamadaoController.nonSelected = [];
		
		chamadaoController.zerarCamposParciais();
		
		var followUp = $('#numeroCotaFollowUp', chamadaoController.workspace).val();
		
		var numeroCota;
		var dataChamadaoFormatada;
		var idFornecedor;
		var idEditor;
		var comChamadaEncalhe;
		
		if(followUp != '') {
			numeroCota = $("#numeroCotaFollowUp", chamadaoController.workspace).val();
			dataChamadaoFormatada = $("#dataCotaFollowUp", chamadaoController.workspace).val();
		} else {
			numeroCota = $("#numeroCotaChamadao", chamadaoController.workspace).val();
			dataChamadaoFormatada = $("#dataChamadao", chamadaoController.workspace).val();
			idFornecedor = $("#idFornecedor", chamadaoController.workspace).val();
			idEditor = $("#idEditor", chamadaoController.workspace).val();
			comChamadaEncalhe = $("#comChamadaEncalhe", chamadaoController.workspace).is(":checked");
		}
		
		if (comChamadaEncalhe) {
			$("#divNovaDataChamadao").show();
			$("#divBotoesChamadaEncalhe").show();
			$("#divBotaoConfirmarChamadao").hide();
		} else {
			$("#divNovaDataChamadao").hide();
			$("#divBotoesChamadaEncalhe").hide();
			$("#divBotaoConfirmarChamadao").show();
		}

		$(".chamadaoGrid", chamadaoController.workspace).flexOptions({
			url: contextPath + "/devolucao/chamadao/pesquisarConsignados",
			onSuccess: function() {
				
				var checkAllSelected = $("#checkAll",chamadaoController.workspace).is(":checked");
				
				if (checkAllSelected) {
					
					$("input[name='checkConsignado']", chamadaoController.workspace).each(function() {
						
						var idLancamento = eval($(this).closest("tr").find('.lancamentoHidden').val());

						var checked = !($.inArray(idLancamento, chamadaoController.nonSelected) >= 0);
						
						this.checked = checked;
					});
				}
			},
			params: [
		         {name:'numeroCota', value: numeroCota},
		         {name:'dataChamadaoFormatada', value: dataChamadaoFormatada},
		         {name:'idFornecedor', value: idFornecedor},
		         {name:'idEditor', value: idEditor},
		         {name:'chamadaEncalhe', value: comChamadaEncalhe}
		    ],
		    newp: 1,
		});
		
		$(".chamadaoGrid", chamadaoController.workspace).flexReload();
	},

	executarPreProcessamento : function(resultado) {
		
		if (resultado.mensagens) {
			
			if(resultado.mensagens.tipoMensagem!= "WARNING"){
				
				exibirMensagem(
						resultado.mensagens.tipoMensagem, 
						resultado.mensagens.listaMensagens
					);
			}
			else{
				
				if(chamadaoController.ACAO_TELA == "PESQUISAR" ){
					
					exibirMensagem(
						resultado.mensagens.tipoMensagem, 
						resultado.mensagens.listaMensagens
					);
				}
			}
			
			$(".grids", chamadaoController.workspace).hide();
			$(".area", chamadaoController.workspace).hide();
		
			return resultado;
		}
		
		$.each(resultado.tableModel.rows, function(index, row) {
			
			var spanReparte = "<span id='reparte" + row.id + "'>"
						+ row.cell.reparte + "</span>";
			
			var spanValorTotalDesconto = "<span id='valorTotal" + row.id + "'>"
						+ row.cell.valorTotalDesconto + "</span>";
			
			var idLancamento = (row.cell.idLancamento) ? row.cell.idLancamento : "";

			var inputCheck = chamadaoController.getInputCheckBox(idLancamento, row.id, row.cell.checked);
			
			var inputHidden = '<input type="hidden" class="lancamentoHidden" value="' + idLancamento + '"/>';
						   
			row.cell.reparte = spanReparte;
			row.cell.valorTotalDesconto = spanValorTotalDesconto;
			row.cell.sel = inputCheck;
			row.cell.lancamentoHidden = inputHidden;
		});
		
		$("#qtdProdutosTotal", chamadaoController.workspace).val(resultado.qtdProdutosTotal);
		$("#qtdExemplaresTotal", chamadaoController.workspace).val(resultado.qtdExemplaresTotal);
		$("#valorTotal", chamadaoController.workspace).val(priceToFloat( resultado.valorTotal ));
		
		var checkAllSelected = chamadaoController.verifyCheckAll();
		
		if (checkAllSelected) {
			
			chamadaoController.duplicarCamposParciais();
			
		}
		
		$(".grids", chamadaoController.workspace).show();
		$(".area", chamadaoController.workspace).show();
		
		return resultado.tableModel;
	},
	
	getInputCheckBox : function(idLancamento, rowId, checked) {

		var inputCheck = '<input type="checkbox" id="ch' + rowId + '"'
						   + ' name="checkConsignado"'
						   + ' value="' + rowId + '"';
		
		inputCheck = inputCheck.concat(checked ? ' checked="checked"' : '');
		
		inputCheck = inputCheck.concat(' onclick="chamadaoController.calcularParcial(this)" '
						+ ' onchange="chamadaoController.selecionarLinha(' + idLancamento + ', this.checked);" />');
		
		return inputCheck;
	},
	
	selecionarLinha : function(idLancamento, checked) {
		
		if (!checked && chamadaoController.checkAll) {
		
			chamadaoController.nonSelected.push(idLancamento);
		}
		
		dataHolder.hold('chamadaoHolder', idLancamento, 'checado', checked);
	},

	selecionarTodos : function(input) {
		
		checkAll(input, "checkConsignado");
		
		$("input[name='checkConsignado']", chamadaoController.workspace).each(function() {
			
			var checado = this.checked;
			
			clickLineFlexigrid(this, checado);
		});
		
		if (input.checked) {
			
			chamadaoController.duplicarCamposParciais();
			
		} else {
			
			chamadaoController.zerarCamposParciais();
		}
		
		chamadaoController.nonSelected = [];
		
		this.checkAll = input.checked;
	},
	
	calcularParcial : function(input) {

		var checado = input.checked;
		
		clickLineFlexigrid(input, checado);
		
		if (checado) {
			
			chamadaoController.parciais.qtdProdutosParcial += 1;
			
			var reparte = $("#reparte" + input.value).html();
			reparte = removeMascaraPriceFormat(reparte);
			chamadaoController.parciais.qtdExemplaresParcial += intValue(reparte);
			
			var valor = $("#valorTotal" + input.value).html();
			
			valor = priceToFloat(valor);
			chamadaoController.parciais.valorParcial = parseFloat(chamadaoController.parciais.valorParcial) + parseFloat(valor);
		
		} else {
			
			chamadaoController.parciais.qtdProdutosParcial -= 1;
			
			var reparte = $("#reparte" + input.value).html();
			reparte = removeMascaraPriceFormat(reparte);
			chamadaoController.parciais.qtdExemplaresParcial -= intValue(reparte);
			
			var valor = $("#valorTotal" + input.value).html();
			
			valor = priceToFloat(valor);
			chamadaoController.parciais.valorParcial = parseFloat(chamadaoController.parciais.valorParcial) - parseFloat(valor);
		}

		chamadaoController.parciais.valorParcial = parseFloat(chamadaoController.parciais.valorParcial).toFixed(4);
		
		$("#qtdProdutosParcial", chamadaoController.workspace).val(chamadaoController.parciais.qtdProdutosParcial);
		$("#qtdExemplaresParcial", chamadaoController.workspace).val(chamadaoController.parciais.qtdExemplaresParcial);
		$("#valorParcial", chamadaoController.workspace).val(parseFloat(chamadaoController.parciais.valorParcial).toFixed(2));
	},
	
	verifyCheckAll : function() {
		return ($("#checkAll", chamadaoController.workspace).attr("checked") == "checked");
	},
	
	duplicarCamposParciais : function() {
			
		$("#qtdProdutosParcial", chamadaoController.workspace).val($("#qtdProdutosTotal", chamadaoController.workspace).val());
		$("#qtdExemplaresParcial", chamadaoController.workspace).val($("#qtdExemplaresTotal", chamadaoController.workspace).val());
		$("#valorParcial", chamadaoController.workspace).val($("#valorTotal", chamadaoController.workspace).val());
		
		chamadaoController.parciais.qtdProdutosParcial = $("#qtdProdutosTotal", chamadaoController.workspace).val();
		chamadaoController.parciais.qtdExemplaresParcial = $("#qtdExemplaresTotal", chamadaoController.workspace).val();
		chamadaoController.parciais.valorParcial = $("#valorTotal", chamadaoController.workspace).val();
	},
	
	zerarCamposParciais : function() {
		
		$("#qtdProdutosParcial", chamadaoController.workspace).val(0);
		$("#qtdExemplaresParcial", chamadaoController.workspace).val(0);
		$("#valorParcial", chamadaoController.workspace).val(floatToPrice(0));
		
		chamadaoController.parciais.qtdProdutosParcial = 0;
		chamadaoController.parciais.qtdExemplaresParcial = 0;
		chamadaoController.parciais.valorParcial = 0;
	},
	
	aplicarMascaraCampos : function() {
		
		$("#valorParcial", chamadaoController.workspace).priceFormat({
			allowNegative: true,
			centsSeparator: ',',
		    thousandsSeparator: '.',
		    centsLimit: 2
		});
	},
	
	confirmar : function(acao) {
		
		chamadaoController.limparNovaDataChamadao();
		
		$( "#dialog-confirm", chamadaoController.workspace ).dialog({
			resizable: false,
			height:'auto',
			width:320,
			modal: true,
			buttons: {
				"Confirmar": function() {

					chamadaoController.realizarChamadao(acao);
				},
				"Cancelar": function() {
					
					chamadaoController.limparNovaDataChamadao();
					
					$(this).dialog("close");
				}
			},
			beforeClose: function() {
			
				clearMessageDialogTimeout();
			},
			form: $("#dialog-confirm", this.workspace).parents("form")
		});
	},
	
	limparNovaDataChamadao : function() {
		
		$("#novaDataChamadao").val("");		
	},
	
	realizarChamadao : function(acao) {
		
		var isReprogramacao = (acao == "REPROGRAMAR")?true:false;
		
		var param ={novaDataChamadaoFormatada: $("#novaDataChamadao").val(), 
					chamarTodos: chamadaoController.verifyCheckAll(),
					reprogramacao: isReprogramacao};
		
		param = serializeArrayToPost('listaChamadao', chamadaoController.getListaChamadao(), param);
		
		param.idsIgnorados = chamadaoController.nonSelected;
		
		$.postJSON(contextPath + "/devolucao/chamadao/confirmarChamadao",param,
				   function(result) {
						
						$("#dialog-confirm", chamadaoController.workspace).dialog("close");
						
						var tipoMensagem = result.tipoMensagem;
						var listaMensagens = result.listaMensagens;
						
						if (tipoMensagem && listaMensagens) {
							
							exibirMensagem(tipoMensagem, listaMensagens);
						}
						
						$(".chamadaoGrid", chamadaoController.workspace).flexReload();
						
						$("#checkAll", chamadaoController.workspace).attr("checked", false);
						
						chamadaoController.ACAO_TELA = "";
					},
				   null,
				   true
		);
	},
	
	getListaChamadao : function() {
		
		var linhasDaGrid = $('.chamadaoGrid tr', chamadaoController.workspace);
		
		var listaChamadao = new Array();
		
		var checkAllSelected = chamadaoController.verifyCheckAll();
		
		if (!checkAllSelected) {
			
			$.each(linhasDaGrid, function(index, value) {
				
				var linha = $(value);
				
				var colunaCheck = linha.find("td")[11];
				
				var inputCheck = $(colunaCheck).find("div").find('input[name="checkConsignado"]');
				
				var checked = inputCheck.attr("checked") == "checked";
				
				if (checked) {
					
					var colunaCodProduto = linha.find("td")[0];
					var colunaNumEdicao = linha.find("td")[2];
					var inputHiddenLancamento = linha.find("td")[12];
					var colunaDataRecolhimento = linha.find("td")[8];
					
					var codProduto = $(colunaCodProduto).find("div").html();
					var numEdicao = $(colunaNumEdicao).find("div").html();
					var lancamento = $($(inputHiddenLancamento).find("div").html()).val();
					var dataRecolhimento = $(colunaDataRecolhimento).find("div").html();
					
					
					listaChamadao.push({codigoProduto:codProduto, numeroEdicao:numEdicao, idLancamento:lancamento, dataRecolhimento:dataRecolhimento});
				}
			});
		}
		
		return listaChamadao;
	},
	
	cancelarChamadao : function() {
		var param = {chamarTodos:chamadaoController.verifyCheckAll() };
		
		param = serializeArrayToPost('listaChamadao', chamadaoController.getListaChamadao(), param);
		
		$.postJSON(contextPath + "/devolucao/chamadao/cancelarChamadao",
					param,
				   function(result) {
						
						var tipoMensagem = result.tipoMensagem;
						var listaMensagens = result.listaMensagens;
						
						if (tipoMensagem && listaMensagens) {
							
							exibirMensagem(tipoMensagem, listaMensagens);
						}
						
						$(".chamadaoGrid", chamadaoController.workspace).flexReload();
						
						$("#checkAll", chamadaoController.workspace).attr("checked", false);
						
						chamadaoController.ACAO_TELA = "";
					},
				   null
		);
	}
	
}, BaseController);

//@ sourceURL=chamadao.js


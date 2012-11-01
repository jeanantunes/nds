var chamadaoController = $.extend(true, {
	
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
				display : 'Preço Venda R$',
				name : 'precoVenda',
				width : 80,
				sortable : true,
				align : 'right'
			}, {
				display : 'Preço Desconto R$',
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
		
		$("#numeroCota", chamadaoController.workspace).focus();
		
		$("#descricaoCota", chamadaoController.workspace).autocomplete({source: ""});

		
		if(chamadaoController.getQueryString()["carregarGrid"] == true) {
			
			var numeroCota = chamadaoController.getQueryString()["numeroCota"];
			var dataChamadaoFormatada = chamadaoController.getQueryString()["data"];
			popularGridPeloFollowUp(numeroCota,dataChamadaoFormatada);
		}
	},
		
	pesquisar : function() {
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
			numeroCota = $("#numeroCota", chamadaoController.workspace).val();
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

			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			
			$(".grids", chamadaoController.workspace).hide();

			return resultado;
		}
		
		$.each(resultado.tableModel.rows, function(index, row) {
			
			var spanReparte = "<span id='reparte" + row.id + "'>"
						+ row.cell.reparte + "</span>";
			
			var spanValorTotal = "<span id='valorTotal" + row.id + "'>"
						+ row.cell.valorTotal + "</span>";
			
			var inputCheck = '<input type="checkbox" id="ch' + row.id + '"'
						   + ' name="checkConsignado"'
						   + ' value="' + row.id + '"'
						   + ' onclick="chamadaoController.calcularParcial()" />';
			
			var idLancamento = (row.cell.idLancamento) ? row.cell.idLancamento : "";
			
			var inputHidden = '<input type="hidden" class="lancamentoHidden" value="' + idLancamento + '"/>';
						   
			row.cell.reparte = spanReparte;
			row.cell.valorTotal = spanValorTotal;
			row.cell.sel = inputCheck;
			row.cell.lancamentoHidden = inputHidden;
		});
		
		$("#qtdProdutosTotal", chamadaoController.workspace).val(resultado.qtdProdutosTotal);
		$("#qtdExemplaresTotal", chamadaoController.workspace).val(resultado.qtdExemplaresTotal);
		$("#valorTotal", chamadaoController.workspace).val(resultado.valorTotal);
		
		var checkAllSelected = chamadaoController.verifyCheckAll();
		
		if (checkAllSelected) {
			
			chamadaoController.duplicarCamposParciais();
			
		} else {
			
			chamadaoController.zerarCamposParciais();
		}
		
		$(".grids", chamadaoController.workspace).show();
		
		return resultado.tableModel;
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
	},
	
	calcularParcial : function() {

		var qtdProdutosParcial = 0;
		var qtdExemplaresParcial = 0;
		var valorParcial = 0;
		
		$("input[name='checkConsignado']", chamadaoController.workspace).each(function() {
		
			var checado = this.checked;
			
			clickLineFlexigrid(this, checado);
			
			if (checado) {
				
				qtdProdutosParcial = qtdProdutosParcial + 1;
				
				var reparte = $("#reparte" + this.value).html();
				reparte = removeMascaraPriceFormat(reparte);
				qtdExemplaresParcial = qtdExemplaresParcial + intValue(reparte);
				
				var valor = $("#valorTotal" + this.value).html();
				valor = removeMascaraPriceFormat(valor);
				valorParcial = valorParcial + intValue(valor);
			
			} else {
				
				$("#checkAll", chamadaoController.workspace).attr("checked", false);
			}
		});
		
		$("#qtdProdutosParcial", chamadaoController.workspace).val(qtdProdutosParcial);
		$("#qtdExemplaresParcial", chamadaoController.workspace).val(qtdExemplaresParcial);
		$("#valorParcial", chamadaoController.workspace).val(valorParcial);
		
		chamadaoController.aplicarMascaraCampos();
	},
	
	verifyCheckAll : function() {
		return ($("#checkAll", chamadaoController.workspace).attr("checked") == "checked");
	},
	
	duplicarCamposParciais : function() {
			
		$("#qtdProdutosParcial", chamadaoController.workspace).val($("#qtdProdutosTotal", chamadaoController.workspace).val());
		$("#qtdExemplaresParcial", chamadaoController.workspace).val($("#qtdExemplaresTotal", chamadaoController.workspace).val());
		$("#valorParcial", chamadaoController.workspace).val($("#valorTotal", chamadaoController.workspace).val());
	},
	
	zerarCamposParciais : function() {
		
		$("#qtdProdutosParcial", chamadaoController.workspace).val(0);
		$("#qtdExemplaresParcial", chamadaoController.workspace).val(0);
		$("#valorParcial", chamadaoController.workspace).val(0);
		
		chamadaoController.aplicarMascaraCampos();
	},
	
	aplicarMascaraCampos : function() {
		
		$("#qtdExemplaresParcial", chamadaoController.workspace).priceFormat({
			allowNegative: true,
			centsSeparator: '',
		    thousandsSeparator: '.'
		});
		
		$("#valorParcial", chamadaoController.workspace).priceFormat({
			allowNegative: true,
			centsSeparator: ',',
		    thousandsSeparator: '.'
		});
	},
	
	confirmar : function() {
		
		chamadaoController.limparNovaDataChamadao();
		
		$( "#dialog-confirm", chamadaoController.workspace ).dialog({
			resizable: false,
			height:'auto',
			width:320,
			modal: true,
			buttons: {
				"Confirmar": function() {

					chamadaoController.realizarChamadao();
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
	
	realizarChamadao : function() {
		var param ={novaDataChamadaoFormatada:$("#novaDataChamadao").val(), chamarTodos:chamadaoController.verifyCheckAll()};
		
		param = serializeArrayToPost('listaChamadao', chamadaoController.getListaChamadao(), param);
		
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
					
					var codProduto = $(colunaCodProduto).find("div").html();
					var numEdicao = $(colunaNumEdicao).find("div").html();
					var lancamento = $($(inputHiddenLancamento).find("div").html()).val();
					
					
					listaChamadao.push({codigoProduto:codProduto,numeroEdicao:numEdicao,idLancamento:lancamento});
				}
			});
		}
		
		return listaChamadao;
	},
	
	cancelarChamadao : function() {
		var param = {chamarTodos:chamadaoController.verifyCheckAll() };
		param = serializeArrayToPost('fornecedores', fornecedores, param);
		
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
					},
				   null
		);
	}
	
}, BaseController);

//@ sourceURL=chamadao.js

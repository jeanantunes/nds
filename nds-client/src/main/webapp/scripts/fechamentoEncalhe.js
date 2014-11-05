var fechamentoEncalheController = $.extend(true, {

	vDataEncalhe : '',
	vFornecedorId : '',
	vBoxId : '',
	isFechamento : false,
	arrayCotasAusentesSession: [],
	checkMarcarTodosCotasAusentes : false,
	checkAllGrid: false,
	isAllFechamentos: false,
	nonSelected: [],
	fechamentosManuais: [],
	modoBox: {
		ativo: false,
		alterado: false,
		idBox: '',
		idBoxNaoSalvo: '',
		isBoxAlterado: function() {
			return this.ativo && this.alterado;
		},
		reset: function() {
			this.ativo = false;
			this.alterado = false;
			this.idBox = '';
			this.idBoxNaoSalvo = '';
		},
		alterarBox: function(alterado) {
			if (this.ativo) {
				this.alterado = alterado;								 
			} else {
				this.reset();
			}
		},
		checarAlteracoes: function() {
			if (this.isBoxAlterado()) {
				fechamentoEncalheController.exibirMensagemBoxNaoSalvo();
			} else {
				fechamentoEncalheController.limpaGridPesquisa();
			}
		},
		changeBox: function(value) {

			this.checarAlteracoes();
			
			if (this.isBoxAlterado()) {
				this.idBoxNaoSalvo = this.idBox; 
			} else {
				this.idBoxNaoSalvo = '';
			}
			
			this.idBox = value;
			this.ativo = value && value != "";
			this.alterarBox(false);

			fechamentoEncalheController.desabilitarBotaoConfirmar(true);
		},
		getCurrentBoxId: function() {			
			return this.idBoxNaoSalvo ? this.idBoxNaoSalvo : this.idBox;
		},
		clearBoxNaoSalvo: function() {
			this.idBoxNaoSalvo = '';
		}
	},
	
	init : function() {
		
		var sizeNomeProduto = 110;
		var toggleColunaJuramentado = $("#toggleColunaJuramentado").val()==="true";
		
		if($("#permissaoColExemplDevolucao", fechamentoEncalheController.workspace).val() != "true") {
			sizeNomeProduto = 465;
		}
		
		$("#fechamentoEncalhe-datepickerDe", fechamentoEncalheController.workspace).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		$("#dtPostergada", fechamentoEncalheController.workspace).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});

		$(".cotasGrid", fechamentoEncalheController.workspace).flexigrid({
			onSuccess: function() {bloquearItensEdicao(fechamentoEncalheController.workspace);},
			preProcess: fechamentoEncalheController.preprocessamentoGrid,
			dataType : 'json',
			colModel : [ {
				display : 'Cota',
				name : 'numeroCota',
				width : 50,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'colaboradorName',
				width : 110,
				sortable : true,
				align : 'left'
			}, {
				display : 'Box',
				name : 'boxName',
				width : 37,
				sortable : true,
				align : 'left'
			}, {
				display : 'Roteiro',
				name : 'roteiroName',
				width : 85,
				sortable : true,
				align : 'left'
			}, {
				display : 'Rota',
				name : 'rotaName',
				width : 80,
				sortable : true,
				align : 'left'
			}, {
				display : 'A&ccedil;ao',
				name : 'acao',
				width : 110,
				sortable : true,
				align : 'left'
			}, {
				display : ' ',
				name : 'check',
				width : 20,
				sortable : false,
				align : 'center'
			}],
			sortname : "numeroCota",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 600,
			height : 240,
			singleSelect : true
		});
		$(".fechamentoGrid", fechamentoEncalheController.workspace).flexigrid({
			dataType : 'json',
			onSuccess: function() {
				bloquearItensEdicao(fechamentoEncalheController.workspace);
				fechamentoEncalheController.replicarItens();
			},
			preProcess: fechamentoEncalheController.preprocessamentoGridFechamento,
			colModel : [ {
				display : 'Sequencia',
				name : 'sequencia',
				width : 50,
				sortable : true,
				align : 'left'
			}, {
				display : 'C&oacute;digo',
				name : 'codigo',
				width : 50,
				sortable : true,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'produto',
				width : sizeNomeProduto,
				sortable : true,
				align : 'left'
			},{
				display : 'Edi&ccedil;&atilde;o',
				name : 'edicao',
				width : 40,
				sortable : true,
				align : 'left'
			}, {
				display : 'Pre&ccedil;o Capa R$',
				name : 'precoCapaFormatado',
				width : 80,
				sortable : true,
				align : 'right'
			}, {
				display : 'Pre&ccedil;o Desc R$',
				name : 'precoCapaDescFormatado',
				width : 80,
				sortable : true,
				align : 'right'
			}, {
				display : 'Total R$',
				name : 'totalFormatado',
				width : 60,
				sortable : false,
				align : 'right'
			}, {
				display : 'Devolu&ccedil;&atilde;o',
				name : 'exemplaresDevolucaoFormatado',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Exe. Juramentado',
				name : 'exemplaresJuramentadoFormatado',
				hide: toggleColunaJuramentado,
				width : 100,
				sortable : true,
				align : 'center'
			}, {
				display : 'Venda de Encalhe',
				name : 'exemplaresVendaEncalheFormatado',
				width : 100,
				sortable : true,
				align : 'center'
			}, {
				display : 'F&iacute;sico',
				name : 'fisico',
				width : 70,
				sortable : false,
				align : 'center'
			}, {
				display : 'Diferen&ccedil;a',
				name : 'diferenca',
				width : 50,
				sortable : false,
				align : 'right'
			},{
				display : 'Estoque',
				name : 'estoque',
				width : 60,
				sortable : false,
				align : 'right'
			},{
				display : 'Replicar Qtde.',
				name : 'replicar',
				width : 80,
				sortable : false,
				align : 'center'
			}],
			sortname : "sequencia",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : false,
			width : 1020,
			height : 'auto',
			singleSelect : true
		});
		
		if($("#permissaoColExemplDevolucao", fechamentoEncalheController.workspace).val() != "true"){
			$(".fechamentoGrid", fechamentoEncalheController.workspace).flexToggleCol(6,false);
			$(".fechamentoGrid", fechamentoEncalheController.workspace).flexToggleCol(7,false);
			$(".fechamentoGrid", fechamentoEncalheController.workspace).flexToggleCol(9,false);
			$(".fechamentoGrid", fechamentoEncalheController.workspace).flexToggleCol(11,false);
			$('.bt_sellAll', fechamentoEncalheController.workspace).hide();
		}
		
		if($("#permissaoBtnConfirmar", fechamentoEncalheController.workspace).val() != "true"){
			$("#btnEncerrarOperacaoEncalhe", fechamentoEncalheController.workspace).hide();
			$("#bt_cotas_ausentes", fechamentoEncalheController.workspace).hide();
		}
	},
	
	pesquisar : function(aplicaRegraMudancaTipo) {
		
		dataHolder.clearAction('fechamentoEncalhe');
		
		if (aplicaRegraMudancaTipo == null ){
			aplicaRegraMudancaTipo = false;
		}
		
		$('.divBotoesPrincipais', fechamentoEncalheController.workspace).hide();
		$('#bt_cotas_ausentes', fechamentoEncalheController.workspace).hide();
		$('#btAnaliticoEncalhe', fechamentoEncalheController.workspace).hide();

		fechamentoEncalheController.fechamentosManuais = [];
		
		$(".fechamentoGrid", fechamentoEncalheController.workspace).flexOptions({
			"url" : contextPath + '/devolucao/fechamentoEncalhe/pesquisar',
			params : [{
				name : "dataEncalhe",
				value : $('#fechamentoEncalhe-datepickerDe', fechamentoEncalheController.workspace).val()
			}, {
				name : "fornecedorId",
				value : $('#selectFornecedor', fechamentoEncalheController.workspace).val()
			}, {
				name : "boxId",
				value : $('#selectBoxEncalhe', fechamentoEncalheController.workspace).val()
			},
			{
				name : "aplicaRegraMudancaTipo",
				value : aplicaRegraMudancaTipo
			}],
			
			newp:1
		});
		
		$(".fechamentoGrid", fechamentoEncalheController.workspace).flexReload();
		$(".grids", fechamentoEncalheController.workspace).show();
		
		fechamentoEncalheController.vDataEncalhe = $('#fechamentoEncalhe-datepickerDe', fechamentoEncalheController.workspace).val();
		fechamentoEncalheController.vFornecedorId = $('#selectFornecedor', fechamentoEncalheController.workspace).val();
		fechamentoEncalheController.vBoxId = $('#selectBoxEncalhe', fechamentoEncalheController.workspace).val();
		
	},
	preprocessamentoGridFechamento : function(resultado) {
		
		if (typeof resultado.mensagens == "object") {
            exibirMensagemDialog(resultado.mensagens.tipoMensagem, resultado.mensagens.listaMensagens, "");
            return;
        } 
		
		if (resultado.rows && resultado.rows.length > 0) {
			
			if($("#permissaoBtnConfirmar", fechamentoEncalheController.workspace).val() == "true"){
				$('#bt_cotas_ausentes', fechamentoEncalheController.workspace).show();
			}
			
			$('.divBotoesPrincipais', fechamentoEncalheController.workspace).show();
			$('#btAnaliticoEncalhe', fechamentoEncalheController.workspace).show();
		}

		fechamentoEncalheController.desabilitarBotaoConfirmar(fechamentoEncalheController.modoBox.ativo);
		
		$.each(resultado.rows, function(index, row) {
			
			var fisicoDigitado = fechamentoEncalheController.fechamentosManuais[row.cell.produtoEdicao];
			
			var valorFisico = ((typeof fisicoDigitado != "undefined") && fisicoDigitado > 0) ? fisicoDigitado : row.cell.fisico == null ? '' : row.cell.fisico;

			var fechado = row.cell.fechado == false ? '' : 'disabled="disabled"';
			
			if (((typeof fisicoDigitado != "undefined") && fisicoDigitado > 0) || valorFisico > 0) {
				
				// calcular a diferença da linha após o fechamento.
				row.cell.diferenca = valorFisico - (row.cell.exemplaresDevolucao - row.cell.exemplaresDevolucaoJuramentado - row.cell.exemplaresVendaEncalhe);
				// row.cell.diferenca = row.cell.exemplaresDevolucao -  valorFisico; 
			}
			
			// row.cell.fisico = '<input tabindex="'+ index + '" class="numericInput" isEdicao="true" type="text" value="'+ (valorFisico != undefined ? valorFisico : "") +'" onkeypress="fechamentoEncalheController.nextInputExemplares('+index+',event); fechamentoEncalheController.retirarCheckBox('+index+', ' + row.cell.produtoEdicao + ');" tabindex="'+index+'" style="width: 60px" id = "'+row.cell.produtoEdicao+'"  name="fisico" onchange="fechamentoEncalheController.onChangeFisico(this, ' + index + ', ' +row.cell.produtoEdicao+')" ' + fechado + '/>';
			row.cell.fisico = '<input tabindex="'+ index + '" class="" isEdicao="true" type="text" value="'+ (valorFisico != undefined ? valorFisico : "") +'" onkeyup="this.value = this.value.replace(/[^0-9]*/gi, \'\')" onkeydown="fechamentoEncalheController.nextInputExemplares(this, '+index+',event); fechamentoEncalheController.retirarCheckBox('+index+', ' + row.cell.produtoEdicao + ');" tabindex="'+index+'" style="width: 60px" id = "'+row.cell.produtoEdicao+'" name="fisico" onchange="fechamentoEncalheController.onChangeFisico(this, ' + index + ', ' +row.cell.produtoEdicao+')" ' + fechado + ' maxlength="6" />';
			
			if((row.cell.replicar == 'true' || (!fechamentoEncalheController.modoBox.ativo && fechamentoEncalheController.checkAllGrid)) 
					&& ($.inArray(row.cell.produtoEdicao, fechamentoEncalheController.nonSelected) < 0)) {
				row.cell.replicar = '<input isEdicao="true" type="checkbox" onchange="fechamentoEncalheController.selecionarLinha('+ row.cell.produtoEdicao +', this.checked)" id="ch'+index+'" name="checkgroupFechamento" onclick="fechamentoEncalheController.replicar(' + index +', this, ' + row.cell.produtoEdicao + ');"' + fechado+ ' checked />';
			} else {
				row.cell.replicar = '<input isEdicao="true" type="checkbox" onchange="fechamentoEncalheController.selecionarLinha('+ row.cell.produtoEdicao +', this.checked)" id="ch'+index+'" name="checkgroupFechamento" onclick="fechamentoEncalheController.replicar(' + index +', this, ' + row.cell.produtoEdicao + ');"' + fechado+ '/>';
			}	
			
			if (fechado != '') {
				$('.divBotoesPrincipais', fechamentoEncalheController.workspace).hide();
			}
			
			if(!row.cell.exemplaresJuramentadoFormatado){
				row.cell.exemplaresJuramentadoFormatado = "0";
			}
			
			if(!row.cell.exemplaresVendaEncalheFormatado){
				row.cell.exemplaresVendaEncalheFormatado = "0";
			}
		});

		return resultado;
	},
	
	desabilitarBotaoConfirmar: function(value) {
		
		if (value) {
			
			$("#btnEncerrarOperacaoEncalhe").hide();

		} else {
			
			$("#btnEncerrarOperacaoEncalhe").show();
		}
	},
	
	selecionarLinha: function (idProdutoEdicao, checked) {
		
		if (fechamentoEncalheController.checkAllGrid && !checked) {
			
			var fisico = $("#" + idProdutoEdicao, fechamentoEncalheController.workspace).val();
			
			fechamentoEncalheController.nonSelected.push(idProdutoEdicao);
		}
		
		dataHolder.hold('fechamentoEncalhe', idProdutoEdicao , 'checado', checked);
	},
	
	replicarTodos : function() {
	
		var tabela = $('.fechamentoGrid', fechamentoEncalheController.workspace).get(0);
		
		for (var i = 0; i<tabela.rows.length; i++) {
			
			fechamentoEncalheController.replicarItem(i);
		}
		
		fechamentoEncalheController.isAllFechamentos = true;
	},
	
	replicarItens : function() {
		
		var index = 0;
		
		$('.fechamentoGrid', fechamentoEncalheController.workspace).find('tr').each(function(){
			
			if ($(this).find('input[name="checkgroupFechamento"]').is(":checked")) {
				
				fechamentoEncalheController.replicarItem(index);
			}
			
			index++;
		});
	},
	
	replicar:function(index, campo, produtoEdicao){

		$("#sel",this.workspace).attr("checked",false);
		fechamentoEncalheController.replicarItem(index);
		
		var inputFisico = $(campo).parents("tr").find("input[name='fisico']");
		
		fechamentoEncalheController.onChangeFisico(inputFisico, index, produtoEdicao);
		
		fechamentoEncalheController.isAllFechamentos = false;
	},
	
	replicarItem : function(index) {

		var tabela = $('.fechamentoGrid', fechamentoEncalheController.workspace).get(0);
		var valor = tabela.rows[index].cells[7].firstChild.innerHTML;
		var juramentado = parseInt(tabela.rows[index].cells[8].firstChild.innerHTML);
		var vendaEncalhe = parseInt(tabela.rows[index].cells[9].firstChild.innerHTML);
		var campo = tabela.rows[index].cells[10].firstChild.firstChild;
		var diferenca = tabela.rows[index].cells[11].firstChild;
		
		if (campo.disabled) {
			
			return;
		}
		
		fechamentoEncalheController.modoBox.alterarBox(true);
		
		if ($('#ch'+index).is(':checked')) {
			
			if ($(campo).val() != null || $(campo).val() != "") {
				
				$(campo).parent('div').children('.divEscondidoValorFisico_' + index).remove();
				$(campo).parent('div').append('<div class="divEscondidoValorFisico_' + index + '" style="display:none;">' + $(campo).val() + '</div>');
				
				$(campo).parent('div').children('.divEscondidoValorDiferenca_' + index).remove();
				$(campo).parent('div').append('<div class="divEscondidoValorDiferenca_' + index + '" style="display:none;">' + diferenca.innerHTML + '</div>');
			}
			
			campo.value = valor - juramentado - vendaEncalhe;
			diferenca.innerHTML = "0";
			
		} else {
			
			campo.value = "";
			diferenca.innerHTML = "";
			
			var valorAntigoFisico = $(campo).parent('div').children('.divEscondidoValorFisico_' + index).html();
			$(campo).val(valorAntigoFisico);
			
			var valorAntigoDiferenca = $(campo).parent('div').children('.divEscondidoValorDiferenca_' + index).html();
			diferenca.innerHTML = valorAntigoDiferenca;
		}			
	},
	
	checkAll:function(input){

		checkAll(input,"checkgroupFechamento");
		
		fechamentoEncalheController.replicarTodos(input.checked);
		
		fechamentoEncalheController.checkAllGrid = input.checked;
		
		fechamentoEncalheController.fechamentosManuais = new Array();
		fechamentoEncalheController.nonSelected = new Array();
	},
	
	onChangeFisico : function(campo, index, produtoEdicao) {
		
		var tabela = $('.fechamentoGrid', fechamentoEncalheController.workspace).get(0);
		var devolucao = parseInt(tabela.rows[index].cells[7].firstChild.innerHTML);
		var juramentado = parseInt(tabela.rows[index].cells[8].firstChild.innerHTML);
		var vendaEncalhe = parseInt(tabela.rows[index].cells[9].firstChild.innerHTML);
		var diferenca = tabela.rows[index].cells[11].firstChild;
		
		var value = eval($(campo).val());
		
		if (!value && isNaN(value)) {
			diferenca.innerHTML = "";
		} else {
			diferenca.innerHTML = value - (devolucao - juramentado - vendaEncalhe);
		}
		
		fechamentoEncalheController.fechamentosManuais[produtoEdicao] = campo.value;
		
		fechamentoEncalheController.isAllFechamentos = false;
		
		fechamentoEncalheController.modoBox.alterarBox(true);
	},
	
	gerarArrayFisico : function() {
		
		var tabela = $('.fechamentoGrid', fechamentoEncalheController.workspace).get(0);
		var fisico;
		var arr = new Array();
		
		for (var i=0; i<tabela.rows.length; i++) {
			fisico = tabela.rows[i].cells[10].firstChild.firstChild.value;
			arr.push(fisico);
		}
		
		return arr;
	},
	
	salvar: function(callback) {

		$.postJSON(
			contextPath + "/devolucao/fechamentoEncalhe/salvar",
			fechamentoEncalheController.populaParametrosFechamentoEncalheInformados(),
			function (result) {

				var tipoMensagem = result.tipoMensagem;
				var listaMensagens = result.listaMensagens;

				if (tipoMensagem && listaMensagens) {
					exibirMensagem(tipoMensagem, listaMensagens);
				}

				if (fechamentoEncalheController.modoBox.ativo) {
					fechamentoEncalheController.modoBox.alterado = false;
				}

				if (callback) {
					callback();
				} else {
					fechamentoEncalheController.pesquisar();
				}
				
				fechamentoEncalheController.isAllFechamentos = false;
			},
		  	null,
		   	false
		);
	},
	
	popup_encerrarEncalhe : function(isSomenteCotasSemAcao) {

		var dataEncalhe = $("#fechamentoEncalhe-datepickerDe", fechamentoEncalheController.workspace).val();
		
		$(".cotasGrid", fechamentoEncalheController.workspace).flexOptions({
			url: contextPath + "/devolucao/fechamentoEncalhe/cotasAusentes",
			params: [{name:'dataEncalhe', value: dataEncalhe }, {name:'isSomenteCotasSemAcao', value: isSomenteCotasSemAcao}],
			newp: 1,
		});
		
		$(".cotasGrid", fechamentoEncalheController.workspace).flexReload();
		
	},
	
	verificarEncerrarOperacaoEncalhe : function() {

		var dataEncalhe = $('#fechamentoEncalhe-datepickerDe', fechamentoEncalheController.workspace).val();

		var params = [
			{name:"dataEncalhe", value:dataEncalhe}, 
			{name:"operacao", value: "VERIFICACAO"}
		];
		
		$.postJSON(
			contextPath + "/devolucao/fechamentoEncalhe/verificarEncerrarOperacaoEncalhe",
			params,
			function (result) {
				
				var tipoMensagem, listaMensagens;
				
				if(result && result.tipoMensagem)
					tipoMensagem = result.tipoMensagem;
				
				if(result && result.listaMensagens)
					listaMensagens = result.listaMensagens;
				
				if (tipoMensagem && listaMensagens) {
					
					exibirMensagem(tipoMensagem, listaMensagens);
					
					return;
				}
				
				if (result.isNenhumaCotaAusente == 'true') {
					
					if ($( "#dialog-encerrarEncalhe", fechamentoEncalheController.workspace).dialog("isOpen")) {
						$( "#dialog-encerrarEncalhe", fechamentoEncalheController.workspace).dialog("destroy");
					}

					fechamentoEncalheController.popup_encerrar();
					
				} else {
					
					fechamentoEncalheController.isFechamento = true;
					
					fechamentoEncalheController.popup_encerrarEncalhe(true);
				}
			},
		  	null,
		   	false
		);
	},
	
	popup_encerrar : function() {
		
		$("#dataConfirma", fechamentoEncalheController.workspace).html($("#fechamentoEncalhe-datepickerDe", fechamentoEncalheController.workspace).val());
		
		$( "#dialog-confirm", fechamentoEncalheController.workspace ).dialog({
			resizable: false,
			height:'auto',
			width:400,
			modal: true,
			buttons: {
				"Confirmar": function() {

					var params = [
					      {name: 'dataEncalhe', value: $('#fechamentoEncalhe-datepickerDe', fechamentoEncalheController.workspace).val()}, 
					      {name: 'operacao', value: 'CONFIRMACAO'}
					];
					
					var _this = $(this);

					$.postJSON(
						contextPath + "/devolucao/fechamentoEncalhe/verificarEncerrarOperacaoEncalhe",
						params,
						function (result) {

							var tipoMensagem = result.mensagem.tipoMensagem;
							
							var listaMensagens = result.mensagem.listaMensagens;
							
							if (tipoMensagem && listaMensagens) {
								
								exibirMensagem(tipoMensagem, listaMensagens);
								
								_this.dialog("close");
								
								if(result.isImprimirBoletos){
									
									window.open(contextPath + "/devolucao/fechamentoEncalhe/imprimirBoletosCotasAusentes", 
											"_blank");
									
								}
								
								fechamentoEncalheController.emitirBoletosFechamentoEncalhe();
								
								$(".fechamentoGrid", fechamentoEncalheController.workspace).flexReload();

								return;
							}

							if (result.isNenhumaCotaAusente == 'true') {
								
								fechamentoEncalheController.popup_encerrar();
								
							} else {
								
								fechamentoEncalheController.isFechamento = true;
								
								fechamentoEncalheController.popup_encerrarEncalhe(true);
							}
	
							$(".fechamentoGrid", fechamentoEncalheController.workspace).flexReload();
							
							_this.dialog("close");
						},
					  	null,
					   	false
					);
				},
				
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			},
			form: $("#dialog-confirm", this.workspace).parents("form")
		});
	},
	
	encerrarFechamento : function() {
		
		salvar();
		
		/* verificar cotas pendentes */
		
		$.postJSON(
			contextPath + "/devolucao/fechamentoEncalhe/encerrarFechamento",
			{ 'dataEncalhe' : $('#fechamentoEncalhe-datepickerDe', fechamentoEncalheController.workspace).val() },
			function (result) {
				
			},
		  	null,
		   	true
		);
	},

	confirmar : function(){
		$(".dados", fechamentoEncalheController.workspace).show();
	},
	
	//Função executada ao des/check cotasAusentes e mudança de página
	checarTodasCotasGrid : function(checked, veioDoModal) {
		setTimeout (function () {
			if (checked) {
				$("input[type=checkbox][name='checkboxGridCotas']", fechamentoEncalheController.workspace).each(function() {
					if (this.disabled == false) {
						var elem = $("#textoCheckAllCotas");
						elem.html("Desmarcar todos");
						$(this).attr('checked', true);
					}
					
					if(veioDoModal){
						fechamentoEncalheController.checkMarcarTodosCotasAusentes = checked;
						
						//Limpa o array, inverte a lógica de envio, passa enviar os idCotas de exclusão na Controller
						fechamentoEncalheController.arrayCotasAusentesSession = [];
						
					}else{//Atualizacao de paginacao

						for(i=0;i < fechamentoEncalheController.arrayCotasAusentesSession.length;i++){

							//Desabilita os checks que foram desabilitados em um cenário Marcar todos
							if(this.value == fechamentoEncalheController.arrayCotasAusentesSession[i]){
								
								$(this).attr('checked', false);
							}
						}
					}
					
				});
					
	        } else {
				var elem = $("#textoCheckAllCotas");
				elem.html("Marcar todos");
				
				$("input[type=checkbox][name='checkboxGridCotas']", fechamentoEncalheController.workspace).each(function(){

					if(veioDoModal){
						$(this).attr('checked', false);
						fechamentoEncalheController.arrayCotasAusentesSession = [];
						
						fechamentoEncalheController.checkMarcarTodosCotasAusentes = checked;
					}
				});
				
			}
		}, 1);
		

	},

	preprocessamentoGrid : function(resultado) {	
		
		cotaBloqueadaController.verificaBloqueioCotaEncalhe(function(){}, function(){});
		
		if (resultado.mensagens) {
			
			exibirMensagem(resultado.mensagens.tipoMensagem, resultado.mensagens.listaMensagens);
			
			$(".cotasGrid", fechamentoEncalheController.workspace).hide();
			
			return resultado;
		}
		
		
//		$(resultado.rows).each(function(){
//			if( this.cell.postergado == true || this.cell.postergado == "true" ) {
//				this.cell.check = '<input isEdicao="true" type="checkbox" name="checkboxGridCotas" id="checkboxGridCotas" onclick="fechamentoEncalheController.preencherArrayCotasAusentes('+ this.cell.idCota +', this.checked)" value="' + this.cell.idCota +'" disabled="disabled" />';
//			}
//			
//		});
		
		
		var buttons = {
			"Postergar": function() {
				
				if(!verificarPermissaoAcesso(fechamentoEncalheController.workspace))
					return;

				cotaBloqueadaController.verificaBloqueioCotaEncalhe(fechamentoEncalheController.postergarCotas, function(){return;});
			},
			"Cobrar": function() {
				
				if(!verificarPermissaoAcesso(fechamentoEncalheController.workspace))
					return;
				
				cotaBloqueadaController.verificaBloqueioCotaEncalhe(fechamentoEncalheController.verificarCobrancaGerada, function(){return;});
			},
			"Cancelar": function() {
				
				$(this).dialog( "close" );
			}
		};
		
		if($("#permissaoColExemplDevolucao", fechamentoEncalheController.workspace).val() != "true"){
			delete buttons.Postergar;
			delete buttons.Cobrar;
		}
		
		
		$( "#dialog-encerrarEncalhe", fechamentoEncalheController.workspace ).dialog({
			resizable: false,
			height:500,
			width:650,
			modal: true,
			buttons: buttons,			
			beforeClose: function() {
				clearMessageDialogTimeout('dialogMensagemNovo');
			},
			form: $("#dialog-encerrarEncalhe", this.workspace).parents("form")
		});
		
		$("#checkTodasCotas").attr("checked", fechamentoEncalheController.checkMarcarTodosCotasAusentes);
		fechamentoEncalheController.checarTodasCotasGrid(fechamentoEncalheController.checkMarcarTodosCotasAusentes);
		
		$.each(resultado.rows, function(index, row) {
			
			var checkBox = '<span></span>';
			
			if (row.cell.indPossuiChamadaEncalheCota) { 
			
				if(row.cell.fechado) {

					checkBox = '<input isEdicao="true" type="checkbox" name="checkboxGridCotas" id="checkboxGridCotas" value="' + row.cell.idCota + '" disabled="disabled"/>';	
					
				
				} else {
					var checked = false;
					$.each(fechamentoEncalheController.arrayCotasAusentesSession, function(index, value){
						if(value == row.cell.idCota){
							if(row.cell.postergado == true || row.cell.postergado == "true") {
								checkBox = '<input checked="checked" isEdicao="true" type="checkbox" name="checkboxGridCotas" id="checkboxGridCotas" onclick="fechamentoEncalheController.preencherArrayCotasAusentes('+ row.cell.idCota +', this)" value="' + row.cell.idCota + '" disabled="disabled" />';
							} else {
								checkBox = '<input checked="checked" isEdicao="true" type="checkbox" name="checkboxGridCotas" id="checkboxGridCotas" onclick="fechamentoEncalheController.preencherArrayCotasAusentes('+ row.cell.idCota +', this)" value="' + row.cell.idCota + '" />';
							}

							checked = true;
						}
					});
					
					if(!checked){
						if(row.cell.postergado == true || row.cell.postergado == "true") {
							checkBox = '<input isEdicao="true" type="checkbox" name="checkboxGridCotas" id="checkboxGridCotas" onclick="fechamentoEncalheController.preencherArrayCotasAusentes('+ row.cell.idCota +', this)" value="' + row.cell.idCota + '" disabled="disabled" />';
						} else {
							checkBox = '<input isEdicao="true" type="checkbox" name="checkboxGridCotas" id="checkboxGridCotas" onclick="fechamentoEncalheController.preencherArrayCotasAusentes('+ row.cell.idCota +', this)" value="' + row.cell.idCota + '" />';
						}
						
					}
				}
				
			} else {
				
				if(row.cell.indMFCNaoConsolidado==true){
				
				    checkBox = '<input isEdicao="true" type="checkbox" onclick="return false" onkeydown="return false" checked="checked" name="checkboxGridCotas_comDivida" id="checkboxGridCotas" value="' + row.cell.idCota + '" />';	
				    
				    fechamentoEncalheController.preencherArrayCotasAusentes(row.cell.idCota, true);
				}
				else{
					
					checkBox = '<input isEdicao="true" type="checkbox" onclick="return false" onkeydown="return false" name="checkboxGridCotas_comDivida" id="checkboxGridCotas" value="' + row.cell.idCota + '" />';
				    
					fechamentoEncalheController.preencherArrayCotasAusentes(row.cell.idCota, false);
				}
			}
			
		    row.cell.check = checkBox;
		});

		
		$(".cotasGrid", fechamentoEncalheController.workspace).show();
		
		return resultado;
	},

	preencherArrayCotasAusentes : function(idCota, element){
		setTimeout (function () {
			
			var checked = $(element).attr("checked");
			
			//Inverte a lógica de envio, passa enviar os idCotas de exclusão na Controller
			if(fechamentoEncalheController.checkMarcarTodosCotasAusentes && !checked){
				checked = true;
			}
			
			if(checked){
				fechamentoEncalheController.arrayCotasAusentesSession.push(parseInt(idCota));
			}else{

				var newRef = fechamentoEncalheController.arrayCotasAusentesSession.slice();
				$.each(newRef, function(index, value){
					if(value == idCota){
						
						fechamentoEncalheController.arrayCotasAusentesSession.splice(index, 1);
					}
				});
			}
		}, 1);

	},
	
	
	obterCotasMarcadas : function() {
 
		var cotasAusentesSelecionadas = new Array();

		$("input[type=checkbox][name='checkboxGridCotas']:checked", fechamentoEncalheController.workspace).each(function(){
			cotasAusentesSelecionadas.push(parseInt($(this).val()));
		});

		$("input[type=checkbox][name='checkboxGridCotas_comDivida']:checked", fechamentoEncalheController.workspace).each(function(){
			cotasAusentesSelecionadas.push(parseInt($(this).val()));
		});

		
		return cotasAusentesSelecionadas;
	},
	
	carregarDialogPostergacao : function(dataPostergada) {
		
		var postergarTodas = $("#checkTodasCotas").attr("checked") == "checked";

		var cotasSelecionadas = fechamentoEncalheController.arrayCotasAusentesSession;

		if (postergarTodas || cotasSelecionadas.length > 0) {
			
			$("#dialog-postergar", fechamentoEncalheController.workspace).dialog({
				resizable: false,
				height:'auto',
				width:300,
				modal: true,
				buttons: {
					
					"Confirmar": function() {
						
						var dataPostergacao = $("#dtPostergada", fechamentoEncalheController.workspace).val();
						var dataEncalhe = $("#fechamentoEncalhe-datepickerDe", fechamentoEncalheController.workspace).val();
						
						$.postJSON(contextPath + "/devolucao/fechamentoEncalhe/postergarCotas",
									{ 'dataPostergacao' : dataPostergacao, 
									  'dataEncalhe' : dataEncalhe, 
									  'idsCotas' : cotasSelecionadas,
									  'postergarTodasCotas' : postergarTodas
									},
									function (result) {
	
										$("#dialog-postergar", fechamentoEncalheController.workspace).dialog("close");
										
										var tipoMensagem = result.tipoMensagem;
										var listaMensagens = result.listaMensagens;
										
										if (tipoMensagem && listaMensagens) {
											exibirMensagemDialog(tipoMensagem, listaMensagens, 'dialogMensagemEncerrarEncalhe');
										}

										fechamentoEncalheController.arrayCotasAusentesSession.length=[];
										
										fechamentoEncalheController.checkMarcarTodosCotasAusentes = false;
										
										fechamentoEncalheController.popup_encerrarEncalhe(false);
										
								        if (fechamentoEncalheController.isFechamento) {

								        	fechamentoEncalheController.isFechamento = false;
								        	
								        	fechamentoEncalheController.verificarEncerrarOperacaoEncalhe();
								        }
									},
								  	null,
								   	true,
								   	'dialogMensagemEncerrarEncalhe'
							);
					},
					
					"Cancelar": function() {
						
						$( this ).dialog( "close" );
					}
				},
				
				open: function() {
					
			        $("#dtPostergada", fechamentoEncalheController.workspace).val(dataPostergada);
					
				},
				
				beforeClose: function() {
					
					$("#dtPostergada", fechamentoEncalheController.workspace).val("");
					
					clearMessageDialogTimeout('dialogMensagemEncerrarEncalhe');
				},
				form: $("#dialog-postergar", this.workspace).parents("form")
			});
			
			
		} else {
			
			var listaMensagens = new Array();
			
			listaMensagens.push('Selecione pelo menos uma cota para postergar!');
			exibirMensagemDialog('WARNING', listaMensagens, 'dialogMensagemEncerrarEncalhe');
		}
		
	},
	
	postergarCotas : function() {
		
		var dataEncalhe = $("#fechamentoEncalhe-datepickerDe", fechamentoEncalheController.workspace).val();
		
		$.postJSON(contextPath + "/devolucao/fechamentoEncalhe/carregarDataPostergacao",
				{'dataEncalhe' : dataEncalhe},
				function (result) {

					var tipoMensagem = result.tipoMensagem;
					var listaMensagens = result.listaMensagens;
					
					if (tipoMensagem && listaMensagens) {
					
						exibirMensagemDialog(tipoMensagem, listaMensagens, 'dialogMensagemPostergarCotas');
				
					} else {
						
						fechamentoEncalheController.carregarDialogPostergacao(result);
						
					}
					
				},
			  	null,
			   	true,
			   	'dialogMensagemPostergarCotas');
		
		
		
	},
	
	
	verificarCobrancaGerada: function(){
		
		var cobrarTodas  = $("#checkTodasCotas", fechamentoEncalheController.workspace).attr("checked") == "checked";

		var idsCotas = fechamentoEncalheController.obterCotasMarcadas();

		$.postJSON(contextPath + '/devolucao/fechamentoEncalhe/verificarCobrancaGerada',
				{
					'idsCotas' : idsCotas,
					'cobrarTodasCotas': cobrarTodas
				},
		
			function(conteudo){
			
				if(conteudo && conteudo.tipoMensagem == 'WARNING') {
					
					$("#msgRegerarCobranca", fechamentoEncalheController.workspace).text(conteudo.listaMensagens[0]);
					
					$("#dialog-confirmar-regerar-cobranca", fechamentoEncalheController.workspace).dialog({
						resizable : false,
						height : 'auto',
						width : 680,
						modal : true,
						buttons : {
							"Confirmar" : function() {
								
								fechamentoEncalheController.cobrarCotas();

								$("#dialog-confirmar-regerar-cobranca", fechamentoEncalheController.workspace).dialog("close");
							},
							"Cancelar" : function(){
							
								$("#dialog-confirmar-regerar-cobranca", fechamentoEncalheController.workspace).dialog("close");
							}
						},
						form: $("#dialog-confirmar-regerar-cobranca", this.workspace).parents("form")
					});
					
				} else {
					
					fechamentoEncalheController.cobrarCotas();
				}
				
			}, null, true, "dialog-confirmar-regerar-cobranca"
		);
	},
	
	cobrarCotas : function() {

		var dataOperacao = $("#fechamentoEncalhe-datepickerDe", fechamentoEncalheController.workspace).val();
		var cobrarTodas  = $("#checkTodasCotas").attr("checked") == "checked";

		var idsCotas = fechamentoEncalheController.arrayCotasAusentesSession;

		$.postJSON(contextPath + "/devolucao/fechamentoEncalhe/cobrarCotas",
			{ 
				'dataOperacao' : dataOperacao, 
				'idsCotas' : idsCotas,
				'cobrarTodasCotas': cobrarTodas
			},
			function (result) {
				
				var tipoMensagem = result.tipoMensagem;
				var listaMensagens = result.listaMensagens;
				
				if (tipoMensagem && listaMensagens) {
					exibirMensagemDialog(tipoMensagem, listaMensagens, 'dialogMensagemEncerrarEncalhe');
				}

				$(".cotasGrid", fechamentoEncalheController.workspace).dialog("close");
				
				fechamentoEncalheController.arrayCotasAusentesSession.length=[];
				
				fechamentoEncalheController.checkMarcarTodosCotasAusentes = false;
				
				fechamentoEncalheController.popup_encerrarEncalhe(false);
				
				if (fechamentoEncalheController.isFechamento) {

		        	fechamentoEncalheController.isFechamento = false;
		        	
		        	//fechamentoEncalheController.verificarEncerrarOperacaoEncalhe();
		        }
			},
		  	null,
		   	true
		);
		
		setTimeout(
			fechamentoEncalheController.obterStatusCobrancaCota,
			5000
		);
	},

	gerarArquivoCotasAusentes : function(fileType) {

		var dataEncalhe = $("#fechamentoEncalhe-datepickerDe", fechamentoEncalheController.workspace).val();
		
		window.location = 
			contextPath + 
			"/devolucao/fechamentoEncalhe/exportarArquivo?" + 
			"dataEncalhe=" + dataEncalhe + 
			"&sortname=" + $(".cotasGrid", fechamentoEncalheController.workspace).flexGetSortName() +
			"&sortorder=" + $(".cotasGrid", fechamentoEncalheController.workspace).getSortOrder() +
			"&rp=" + $(".cotasGrid", fechamentoEncalheController.workspace).flexGetRowsPerPage() +
			"&page=" + $(".cotasGrid", fechamentoEncalheController.workspace).flexGetPageNumber() +
			"&fileType=" + fileType;

		return false;
	},
	
	imprimirArquivo : function(fileType) {

		var dataEncalhe = $("#fechamentoEncalhe-datepickerDe", fechamentoEncalheController.workspace).val();
		
		window.location = contextPath + "/devolucao/fechamentoEncalhe/imprimirArquivo?"
			+ "dataEncalhe=" + fechamentoEncalheController.vDataEncalhe
			+ "&fornecedorId="+ fechamentoEncalheController.vFornecedorId
			+ "&boxId=" + fechamentoEncalheController.vBoxId
			+ "&sortname=" + $(".fechamentoGrid", fechamentoEncalheController.workspace).flexGetSortName()
			+ "&sortorder=" + $(".fechamentoGrid", fechamentoEncalheController.workspace).getSortOrder()
			+ "&rp=" + $(".fechamentoGrid", fechamentoEncalheController.workspace).flexGetRowsPerPage()
			+ "&page=" + $(".fechamentoGrid", fechamentoEncalheController.workspace).flexGetPageNumber()
			+ "&fileType=" + fileType;

		return false;
	},

	verificarMensagemConsistenciaDados : function() {
		$.postJSON(
			contextPath + "/devolucao/fechamentoEncalhe/verificarMensagemConsistenciaDados",
			{ 
			    'dataEncalhe' : $('#fechamentoEncalhe-datepickerDe', fechamentoEncalheController.workspace).val(),
			    'fornecedorId' : $('#selectFornecedor', fechamentoEncalheController.workspace).val(),
				'boxId' :  $('#selectBoxEncalhe', fechamentoEncalheController.workspace).val()
		    },
			function (result) {
		    	
				var tipoMensagem = result.tipoMensagem;
				var listaMensagens = result.listaMensagens;
				
				if (tipoMensagem && listaMensagens) {
					if (tipoMensagem == "ERROR"  ){
						exibirMensagem("WARNING", listaMensagens);
					//	fechamentoEncalheController.pesquisar(false);
					} else {
						$('#mensagemConsistenciaDados', fechamentoEncalheController.workspace).html(listaMensagens[0]);
						fechamentoEncalheController.popup_mensagem_consistencia_dados();
					}
				} else {
					fechamentoEncalheController.pesquisar(false);
				}

    		},
		  	null,
		   	false
		);
	},

	popup_mensagem_consistencia_dados : function() {
		
		$( "#dialog-mensagem-consistencia-dados", fechamentoEncalheController.workspace ).dialog({
			resizable: false,
			height:'auto',
			width:400,
			modal: true,
			buttons: {
				"Confirmar": function() {
					fechamentoEncalheController.pesquisar(true);
					$(this).dialog( "close" );
				},
				
				"Cancelar": function() {
					$(this).dialog( "close" );
				}
			},
			beforeClose: function() {
				//clearMessageDialogTimeout('dialogMensagemNovo');
			},
			form: $("#dialog-mensagem-consistencia-dados", this.workspace).parents("form")
		});
	},
	
	 populaParametrosFechamentoEncalheInformados : function(){
		 
		linhasTabela = [];
		$('.fechamentoGrid', fechamentoEncalheController.workspace).find('tr').each(function(){
			var codigo = $(this).children('td[abbr="codigo"]').children('div').html().toString();
			var produtoEdicao = $(this).children('td').children('div').children('input[name=fisico]').attr("id");//Envia o id que eh setado com produtoEdicaoId
			var fisico = $(this).children('td').children('div').children('input[name=fisico]').val().toString();
			var checkbox = $(this).children('td').children('div').children('input[name=checkgroupFechamento]:checked').val() == "on" ? true : false;
			var envioController = {
				"codigo"  		: 	codigo,
				"produtoEdicao" : 	produtoEdicao,
				"fisico"		:	fisico,
				"checkbox" 		: 	checkbox
			};			
			$.postJSON(contextPath + "/devolucao/fechamentoEncalhe/enviarGridAnteriorParaSession", envioController, function() {
				  console.log( "success" );
			});
		});
		 
		 
		 var data = new Array();

		 var idBox = fechamentoEncalheController.modoBox.getCurrentBoxId();

		 data.push({name:"boxId", value: idBox});

		 data.push({name:"dataEncalhe", value: $('#fechamentoEncalhe-datepickerDe', fechamentoEncalheController.workspace).val()});
		 data.push({name:"fornecedorId", value: $('#selectFornecedor', fechamentoEncalheController.workspace).val()});
		 
		 if (fechamentoEncalheController.nonSelected) {

			 $.each(fechamentoEncalheController.nonSelected, function(index, value) {
				data.push({name:'listaNaoReplicados[' + index + '].produtoEdicao', value: value});
				data.push({name:'listaNaoReplicados[' + index + '].fisico', value: $("#" + value, fechamentoEncalheController.workspace).val()});
			 });

			 data.push({name:"isAllFechamentos", value: fechamentoEncalheController.checkAllGrid || fechamentoEncalheController.isAllFechamentos});
		 }

		 for(var index in fechamentoEncalheController.fechamentosManuais) { 
			 data.push({name: 'listaFechamento[' + index + '].produtoEdicao', value: index});
			 data.push({name: 'listaFechamento[' + index + '].fisico', value: fechamentoEncalheController.fechamentosManuais[index]});
		 }
		 /*
		 $.each(fechamentoEncalheController.fechamentosManuais, function(index, value) {
			
			 data.push({name: 'listaFechamento[' + index + '].produtoEdicao', value: value.produtoEdicao});
			 data.push({name: 'listaFechamento[' + index + '].fisico', value: value.fisico});
		 });
		 */
		 
		return data;
	},

	exibirMensagemBoxNaoSalvo: function() {

		$("#dialog-confirm-box-nao-salvo").dialog({
			resizable: false,
			height:'auto',
			width:400,
			modal: true,
			buttons: {
				"Sim": function() {
					fechamentoEncalheController.salvar(function() {
						fechamentoEncalheController.limpaGridPesquisa();
					});
					$(this).dialog( "close" );
				},				
				"Não": function() {
					$(this).dialog( "close" );
					fechamentoEncalheController.limpaGridPesquisa();
				}
			},
			form: $("#dialog-confirm-box-nao-salvo", this.workspace).parents("form")
		});
	},
	
	limpaGridPesquisa : function() {

		$("#sel", fechamentoEncalheController.workspace).uncheck();
		$(".fechamentoGrid", fechamentoEncalheController.workspace).clear();
		$('#divFechamentoGrid', fechamentoEncalheController.workspace).css("display", "none");
		
		fechamentoEncalheController.modoBox.clearBoxNaoSalvo();
	},
	
	salvarNoEncerrementoOperacao : function() {
		
		$.postJSON(
			contextPath + "/devolucao/fechamentoEncalhe/salvarNoEncerrementoOperacao",
			fechamentoEncalheController.populaParametrosFechamentoEncalheInformados(),
			function (result) {
				
				var tipoMensagem = result.tipoMensagem;
				
				var listaMensagens = result.listaMensagens;
				
				if (tipoMensagem && listaMensagens) {
					
					exibirMensagem(tipoMensagem, listaMensagens);
				} else {
					
					fechamentoEncalheController.verificarEncerrarOperacaoEncalhe();
				}
				
				fechamentoEncalheController.isAllFechamentos = false;
			},
		  	null,
		   	false
		);
	},
	
	analiticoEncalhe : function() {
		$('#workspace').tabs('addTab', "Anal&iacute;tico Encalhe", contextPath + "/devolucao/fechamentoEncalhe/analitico");
	},
	
	nextInputExemplares : function(curIndex,evt) {
		
		var num = (evt.keyCode != 0 ? evt.keyCode : evt.charCode);
		if(num>=48 & num<=57) {
			if (evt.keyCode == 13) {
				var nextElement = $('[tabindex=' + (curIndex + 1) + ']');
				nextElement.select();
				nextElement.focus();
			} 
	  		return true;
		} else {
			 return false; 
		}
	},
	
	retirarCheckBox : function(index,idProdutoEdicao) {
		
		if ($("#ch" + index).is(":checked")) {
			
			$("#ch" + index).attr("checked", false);
		}
		
		if ($("#sel").is(":checked")) {
			
			$("#sel").attr("checked", false);
		}
		
		fechamentoEncalheController.nonSelected.push(idProdutoEdicao);
	},
	
	obterStatusCobrancaCota : function() {
		
		$.postJSON(contextPath + "/devolucao/fechamentoEncalhe/obterStatusCobrancaCota", 
			null,
			function(result) {
				if(!result || result == '' || result == 'FINALIZADO' || result.tipoMensagem) {
					
					$('#mensagemLoading').text('Aguarde, carregando ...');
					
					if (result && result.tipoMensagem){
						exibirMensagem(result.tipoMensagem, result.listaMensagens);
					}
					
				} else {
					
					$('#mensagemLoading').text(result);
					
					setTimeout(
						fechamentoEncalheController.obterStatusCobrancaCota,
						5000
					);
				}
			}
		);	
	}, 

	obterStatusEmissaoBoletosFechamentoEncalhe : function() {
		
		$.postJSON(contextPath + "/financeiro/boletoEmail/obterStatusEmissaoBoletosFechamentoEncalhe", 
			null,
			function(result) {
			
				if(!result || result == '' || result == 'ENVIO_FINALIZADO' || result.tipoMensagem) {
					
					$('#mensagemLoading').text('Aguarde, carregando ...');
					
					if (result && result.tipoMensagem){
						
						exibirMensagem(result.tipoMensagem, result.listaMensagens);
					}
					
				} else {
					
					$('#mensagemLoading').text(result);
										
					setTimeout(fechamentoEncalheController.obterStatusEmissaoBoletosFechamentoEncalhe,5000);
				}
			}
		);	
	}, 
	
    emitirBoletosFechamentoEncalhe : function() {
		
		$.postJSON(contextPath + "/financeiro/boletoEmail/emitirBoletosFechamentoEncalhe", 
			null,
			function(result) {
					
				if (result && result.tipoMensagem){
						
					exibirMensagem(result.tipoMensagem, result.listaMensagens);
					
				}
			}
		);	
		
		setTimeout(fechamentoEncalheController.obterStatusEmissaoBoletosFechamentoEncalhe,5000);
	} 
	
}, BaseController);
//@ sourceURL=fechamentoEncalhe.js

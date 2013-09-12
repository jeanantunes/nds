var fechamentoEncalheController = $.extend(true, {

	vDataEncalhe : '',
	vFornecedorId : '',
	vBoxId : '',
	isFechamento : false,
	arrayCotasAusentesSession: [],
	checkMarcarTodosCotasAusentes : false,
	checkAllGrid: false,
	nonSelected: [],
	
	statusCobrancaCota: null,
	
	init : function() {
		
		var sizeNomeProduto = 110;
		
		if($("#permissaoColExemplDevolucao").val() != "true"){
			sizeNomeProduto = 315;
		}
		
		$("#datepickerDe", fechamentoEncalheController.workspace).datepicker({
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
				display : 'Exempl. Devolu&ccedil;&atilde;o',
				name : 'exemplaresDevolucaoFormatado',
				width : 100,
				sortable : true,
				align : 'center'
			}, {
				display : 'Total R$',
				name : 'totalFormatado',
				width : 80,
				sortable : false,
				align : 'right'
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
		
		if($("#permissaoColExemplDevolucao").val() != "true"){
			$(".fechamentoGrid", fechamentoEncalheController.workspace).flexToggleCol(6,false);
			$(".fechamentoGrid", fechamentoEncalheController.workspace).flexToggleCol(11,false);
			$("#btnEncerrarOperacaoEncalhe", fechamentoEncalheController.workspace).hide();
			$('.bt_sellAll', fechamentoEncalheController.workspace).hide();
		}
	},
	
	pesquisar : function(aplicaRegraMudancaTipo) {
		
		dataHolder.clearAction('fechamentoEncalhe', fechamentoEncalheController.workspace);
		
		if (aplicaRegraMudancaTipo == null ){
			aplicaRegraMudancaTipo = false;
		}
		
		$('.divBotoesPrincipais', fechamentoEncalheController.workspace).hide();
		$('#bt_cotas_ausentes', fechamentoEncalheController.workspace).hide();
		$('#btAnaliticoEncalhe', fechamentoEncalheController.workspace).hide();
		
		$(".fechamentoGrid", fechamentoEncalheController.workspace).flexOptions({
			"url" : contextPath + '/devolucao/fechamentoEncalhe/pesquisar',
			params : [{
				name : "dataEncalhe",
				value : $('#datepickerDe', fechamentoEncalheController.workspace).val()
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
		
		vDataEncalhe = $('#datepickerDe', fechamentoEncalheController.workspace).val();
		vFornecedorId = $('#selectFornecedor', fechamentoEncalheController.workspace).val();
		vBoxId = $('#selectBoxEncalhe', fechamentoEncalheController.workspace).val();
	},
	preprocessamentoGridFechamento : function(resultado) {
		
		if (typeof resultado.mensagens == "object") {
            exibirMensagemDialog(resultado.mensagens.tipoMensagem, resultado.mensagens.listaMensagens, "");
            return;
        } 
		
		if (resultado.rows && resultado.rows.length > 0) {
			$('#bt_cotas_ausentes', fechamentoEncalheController.workspace).show();
			$('.divBotoesPrincipais', fechamentoEncalheController.workspace).show();
			$('#btAnaliticoEncalhe', fechamentoEncalheController.workspace).show();
		}
		
		$.each(resultado.rows, function(index, row) {
			
			var valorFisico = row.cell.fisico == null ? '' : row.cell.fisico;
			if ( ( row.cell.diferenca == "0" && valorFisico == '' ) ||  valorFisico == '' ) {
					row.cell.diferenca = "";
			}
			
			var fechado = row.cell.fechado == false ? '' : 'disabled="disabled"';
			row.cell.fisico = '<input class="" isEdicao="true" type="text" onkeypress="fechamentoEncalheController.nextInputExemplares('+index+',event); fechamentoEncalheController.retirarCheckBox('+index+');" tabindex="'+index+'" style="width: 60px" id = "'+row.cell.produtoEdicao+'"  name="fisico" value="' + valorFisico + '" onchange="fechamentoEncalheController.onChangeFisico(this, ' + index + ')" ' + fechado + '/>';

			if((row.cell.replicar == 'true' || fechamentoEncalheController.checkAllGrid) && ($.inArray(row.cell.produtoEdicao, fechamentoEncalheController.nonSelected) < 0))
			{
				row.cell.replicar = '<input isEdicao="true" type="checkbox" onchange="fechamentoEncalheController.selecionarLinha('+ row.cell.produtoEdicao +', this.checked)" id="ch'+index+'" name="checkgroupFechamento" onclick="fechamentoEncalheController.replicar(' + index + ');"' + fechado+ ' checked />';
			}	
			else
			{
				row.cell.replicar = '<input isEdicao="true" type="checkbox" onchange="fechamentoEncalheController.selecionarLinha('+ row.cell.produtoEdicao +', this.checked)" id="ch'+index+'" name="checkgroupFechamento" onclick="fechamentoEncalheController.replicar(' + index + ');"' + fechado+ '/>';
			}	
			
			if (fechado != '') {
				$('.divBotoesPrincipais', fechamentoEncalheController.workspace).hide();
			}
		});
		
		return resultado;
	},
	
	selecionarLinha: function (idProdutoEdicao, checked) {
		
		if (fechamentoEncalheController.checkAllGrid && !checked) {
			
			fechamentoEncalheController.nonSelected.push(idProdutoEdicao);
		}
		
		dataHolder.hold('fechamentoEncalhe', idProdutoEdicao , 'checado', checked);
	},
	
	replicarTodos : function(replicar) {
	
		var tabela = $('.fechamentoGrid', fechamentoEncalheController.workspace).get(0);
		for (var i = 0; i<tabela.rows.length; i++) {
			if (replicar){
			
				fechamentoEncalheController.replicarItem(i);
			
			} else {
				
				fechamentoEncalheController.limparInputsFisico(i);
			}
		}
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
	
	replicar:function(index){
		$("#sel",this.workspace).attr("checked",false);
		fechamentoEncalheController.replicarItem(index);
	},
	
	limparInputsFisico: function(index) {
		
		var tabela = $('.fechamentoGrid', fechamentoEncalheController.workspace).get(0);
		var campo = tabela.rows[index].cells[8].firstChild.firstChild;
		var diferenca = tabela.rows[index].cells[9].firstChild;
		
		if (!campo.disabled) {

			campo.value = "";
			diferenca.innerHTML = "";
		}
	},
	
	replicarItem : function(index) {

		var tabela = $('.fechamentoGrid', fechamentoEncalheController.workspace).get(0);
		var valor = tabela.rows[index].cells[6].firstChild.innerHTML;
		var campo = tabela.rows[index].cells[8].firstChild.firstChild;
		var diferenca = tabela.rows[index].cells[9].firstChild;
		
		if($('#ch'+index).is(':checked'))
		{
			if($(campo).val() != null || $(campo).val() != "")
			{
				$(campo).parent('div').children('.divEscondidoValorFisico_' + index).remove();
				$(campo).parent('div').append('<div class="divEscondidoValorFisico_' + index + '" style="display:none;">' + $(campo).val() + '</div>');
			}
		}	
		
		if(campo.disabled){
			return;
		}
		
		if (!$("#ch" + index, this.workspace).attr("checked")) {
			
			campo.value = "";
			diferenca.innerHTML = "";
		
		} else {
		
			campo.value = valor;
			diferenca.innerHTML = "0";
		}
		
		
		if(! $('#ch'+index).is(':checked'))
		{
			var valorAntigo = $(campo).parent('div').children('.divEscondidoValorFisico_' + index).html();
			$(campo).val(valorAntigo);			
		}
	},
	
	checkAll:function(input){
		
		 
		if($('input[name=Todos]').is(":checked"))
		{
			gridVerificacaoEscritos = [];
			$('.fechamentoGrid', fechamentoEncalheController.workspace).find('tr').each(function(){
				if($(this).children('td').children('div').children('input[name=fisico]').val().toString() != '')
				{
					gridVerificacaoEscritos.push({codigo : $(this).children('td[abbr="codigo"]').children('div').html().toString(), 
							fisico : $(this).children('td').children('div').children('input[name=fisico]').val().toString()});
				}	
			});
		
		}	
		
		checkAll(input,"checkgroupFechamento");
		
		fechamentoEncalheController.replicarTodos(input.checked);
	
		if(! $('input[name=Todos]').is(":checked"))
		{	
			$(gridVerificacaoEscritos).each(function(key, valor){
				$('.fechamentoGrid', fechamentoEncalheController.workspace).find('tr').each(function(chave, valorSegundo){
					if(valor.codigo == $(valorSegundo).children('td[abbr="codigo"]').children('div').html().toString())
					{
						$(this).children('td').children('div').children('input[name=fisico]').val(valor.fisico);
					}
					
				});
			});
		}
		
		fechamentoEncalheController.checkAllGrid = input.checked;
	},
	
	onChangeFisico : function(campo, index) {
		
		var tabela = $('.fechamentoGrid', fechamentoEncalheController.workspace).get(0);
		var devolucao = parseInt(tabela.rows[index].cells[6].firstChild.innerHTML);
		var diferenca = tabela.rows[index].cells[9].firstChild;
		
		if (campo.value == "") {
			diferenca.innerHTML = "";
		} else {
			diferenca.innerHTML =campo.value - devolucao ;			
		}
	},
	
	gerarArrayFisico : function() {
		
		var tabela = $('.fechamentoGrid', fechamentoEncalheController.workspace).get(0);
		var fisico;
		var arr = new Array();
		
		for (i=0; i<tabela.rows.length; i++) {
			fisico = tabela.rows[i].cells[8].firstChild.firstChild.value;
			arr.push(fisico);
		}
		
		return arr;
	},
	
	salvar : function() {
			$.postJSON(
				contextPath + "/devolucao/fechamentoEncalhe/salvar",
				fechamentoEncalheController.populaParamentrosFechamentoEncalheInformados(),
				function (result) {

					var tipoMensagem = result.tipoMensagem;
					var listaMensagens = result.listaMensagens;
					
					if (tipoMensagem && listaMensagens) {
						exibirMensagem(tipoMensagem, listaMensagens);
					}
					fechamentoEncalheController.pesquisar();
					
				},
			  	null,
			   	false
			);
			
	},
	
	popup_encerrarEncalhe : function(isSomenteCotasSemAcao) {

		var dataEncalhe = $("#datepickerDe", fechamentoEncalheController.workspace).val();
		
		$(".cotasGrid", fechamentoEncalheController.workspace).flexOptions({
			url: contextPath + "/devolucao/fechamentoEncalhe/cotasAusentes",
			params: [{name:'dataEncalhe', value: dataEncalhe }, {name:'isSomenteCotasSemAcao', value: isSomenteCotasSemAcao}],
			newp: 1,
		});
		
		$(".cotasGrid", fechamentoEncalheController.workspace).flexReload();
		
	},
	
	verificarEncerrarOperacaoEncalhe : function() {

		var dataEncalhe = $('#datepickerDe', fechamentoEncalheController.workspace).val();

		var params = [
			{name:"dataEncalhe", value:dataEncalhe}, 
			{name:"operacao", value: "VERIFICACAO"}
		];
		
		$.postJSON(
			contextPath + "/devolucao/fechamentoEncalhe/verificarEncerrarOperacaoEncalhe",
			params,
			function (result) {
				
				console.log(result);
				
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
				
				fechamentoEncalheController.pesquisar();
				
			},
		  	null,
		   	false
		);
	},
	
	popup_encerrar : function() {
		
		$("#dataConfirma", fechamentoEncalheController.workspace).html($("#datepickerDe", fechamentoEncalheController.workspace).val());
		
		$( "#dialog-confirm", fechamentoEncalheController.workspace ).dialog({
			resizable: false,
			height:'auto',
			width:400,
			modal: true,
			buttons: {
				"Confirmar": function() {

					var params = [
					      {name: 'dataEncalhe', value: $('#datepickerDe', fechamentoEncalheController.workspace).val()}, 
					      {name: 'operacao', value: 'CONFIRMACAO'}
					];
					
					var _this = $(this);

					$.postJSON(
						contextPath + "/devolucao/fechamentoEncalhe/verificarEncerrarOperacaoEncalhe",
						params,
						function (result) {

							var tipoMensagem = result.tipoMensagem;
							var listaMensagens = result.listaMensagens;
							
							if (tipoMensagem && listaMensagens) {
								
								exibirMensagem(tipoMensagem, listaMensagens);
								
								_this.dialog("close");
								
								return;
								
							}

							if (result.isNenhumaCotaAusente == 'true') {
								
								fechamentoEncalheController.popup_encerrar();
								
							} else {
								
								fechamentoEncalheController.isFechamento = true;
								fechamentoEncalheController.popup_encerrarEncalhe(true);
								
							}
							
							fechamentoEncalheController.pesquisar();
							
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
			beforeClose: function() {
				$(".fechamentoGrid", fechamentoEncalheController.workspace).flexReload();
			},
			form: $("#dialog-confirm", this.workspace).parents("form")
		});
	},
	
	encerrarFechamento : function() {
		
		salvar();
		
		/* verificar cotas pendentes */
		
		$.postJSON(
			contextPath + "/devolucao/fechamentoEncalhe/encerrarFechamento",
			{ 'dataEncalhe' : $('#datepickerDe', fechamentoEncalheController.workspace).val() },
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
				
				fechamentoEncalheController.postergarCotas();
				
			},
			"Cobrar": function() {
				
				if(!verificarPermissaoAcesso(fechamentoEncalheController.workspace))
					return;
				
				fechamentoEncalheController.veificarCobrancaGerada();
				
			},
			"Cancelar": function() {
				$(this).dialog( "close" );
			}
		};
		
		if($("#permissaoColExemplDevolucao").val() != "true"){
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
								checkBox = '<input checked="checked" isEdicao="true" type="checkbox" name="checkboxGridCotas" id="checkboxGridCotas" onclick="fechamentoEncalheController.preencherArrayCotasAusentes('+ row.cell.idCota +', this.checked)" value="' + row.cell.idCota + '" disabled="disabled" />';
							} else {
								checkBox = '<input checked="checked" isEdicao="true" type="checkbox" name="checkboxGridCotas" id="checkboxGridCotas" onclick="fechamentoEncalheController.preencherArrayCotasAusentes('+ row.cell.idCota +', this.checked)" value="' + row.cell.idCota + '" />';
							}

							checked = true;
						}
					});
					
					if(!checked){
						if(row.cell.postergado == true || row.cell.postergado == "true") {
							checkBox = '<input isEdicao="true" type="checkbox" name="checkboxGridCotas" id="checkboxGridCotas" onclick="fechamentoEncalheController.preencherArrayCotasAusentes('+ row.cell.idCota +', this.checked)" value="' + row.cell.idCota + '" disabled="disabled" />';
						} else {
							checkBox = '<input isEdicao="true" type="checkbox" name="checkboxGridCotas" id="checkboxGridCotas" onclick="fechamentoEncalheController.preencherArrayCotasAusentes('+ row.cell.idCota +', this.checked)" value="' + row.cell.idCota + '" />';
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

	preencherArrayCotasAusentes : function(idCota, checked){
		setTimeout (function () {
			
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
	
	postergarCotas : function() {
		
		var dataEncalhe = $("#datepickerDe", fechamentoEncalheController.workspace).val();
		
		$.postJSON(contextPath + "/devolucao/fechamentoEncalhe/dataSugestaoPostergarCota",
				{ 'dataEncalhe' : dataEncalhe},
				function (result) {
						
			        $("#dtPostergada", fechamentoEncalheController.workspace).val(result.resultado);
				}
		);
		
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
						var dataEncalhe = $("#datepickerDe", fechamentoEncalheController.workspace).val();
						
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
				beforeClose: function() {
					
					$("#dtPostergada", fechamentoEncalheController.workspace).val("");
					
					clearMessageDialogTimeout('dialogMensagemEncerrarEncalhe');
				},
				form: $("#dialog-postergar", this.workspace).parents("form")
			});
	
			fechamentoEncalheController.carregarDataPostergacao();
			
		} else {
			
			var listaMensagens = new Array();
			
			listaMensagens.push('Selecione pelo menos uma cota para postergar!');
			exibirMensagemDialog('WARNING', listaMensagens, 'dialogMensagemEncerrarEncalhe');
		}
	},

	carregarDataPostergacao : function() {

		var dataPostergacao = $("#dtPostergada", fechamentoEncalheController.workspace).val();
		var dataEncalhe = $("#datepickerDe", fechamentoEncalheController.workspace).val();
		
		$.postJSON(contextPath + "/devolucao/fechamentoEncalhe/carregarDataPostergacao",
				{ 'dataEncalhe' : dataEncalhe, 'dataPostergacao' : dataPostergacao },
				function (result) {

					var tipoMensagem = result.tipoMensagem;
					var listaMensagens = result.listaMensagens;
					
					if (tipoMensagem && listaMensagens) {
						exibirMensagemDialog(tipoMensagem, listaMensagens, 'dialogMensagemPostergarCotas');
					} else {
						$("#dtPostergada", fechamentoEncalheController.workspace).val(result);
					}
				},
			  	null,
			   	true,
			   	'dialogMensagemPostergarCotas'
		);

	},
	
	veificarCobrancaGerada: function(){
		
		var cobrarTodas  = $("#checkTodasCotas").attr("checked") == "checked";

		var idsCotas = fechamentoEncalheController.obterCotasMarcadas();

		$.postJSON(contextPath + '/devolucao/fechamentoEncalhe/veificarCobrancaGerada',
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
								
								fechamentoEncalheController.statusCobrancaCota = setInterval(fechamentoEncalheController.obterStatusCobrancaCota,5000);
								
								$("#dialog-confirmar-regerar-cobranca", fechamentoEncalheController.workspace).dialog("close");
								fechamentoEncalheController.cobrarCotas();
							},
							"Cancelar" : function(){
							
								$("#dialog-confirmar-regerar-cobranca", fechamentoEncalheController.workspace).dialog("close");
							}
						},
						form: $("#dialog-confirmar-regerar-cobranca", this.workspace).parents("form")
					});
					
				} else {
					
					fechamentoEncalheController.statusCobrancaCota = setInterval(fechamentoEncalheController.obterStatusCobrancaCota,5000);
					
					fechamentoEncalheController.cobrarCotas();
				}
				
			}, null, true, "dialog-confirmar-regerar-cobranca"
		);
	},
	
	cobrarCotas : function() {

		var dataOperacao = $("#datepickerDe", fechamentoEncalheController.workspace).val();
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
	},

	gerarArquivoCotasAusentes : function(fileType) {

		var dataEncalhe = $("#datepickerDe", fechamentoEncalheController.workspace).val();
		
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

		var dataEncalhe = $("#datepickerDe", fechamentoEncalheController.workspace).val();
		
		window.location = contextPath + "/devolucao/fechamentoEncalhe/imprimirArquivo?"
			+ "dataEncalhe=" + vDataEncalhe
			+ "&fornecedorId="+ vFornecedorId
			+ "&boxId=" + vBoxId
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
			    'dataEncalhe' : $('#datepickerDe', fechamentoEncalheController.workspace).val(),
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
	
	 populaParamentrosFechamentoEncalheInformados : function(){
		 
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
		 
		 data.push({name:"dataEncalhe", value: $('#datepickerDe', fechamentoEncalheController.workspace).val()});
		 data.push({name:"fornecedorId", value: $('#selectFornecedor', fechamentoEncalheController.workspace).val()});
		 data.push({name:"boxId", value: $('#selectBoxEncalhe', fechamentoEncalheController.workspace).val()});

		 $("input[type=text][name='fisico']").each(function(index, value){
			
			 if ($.inArray($(value).attr('id'), fechamentoEncalheController.nonSelected) < 0) {

				 data.push({name: 'listaFechamento[' + index + '].produtoEdicao', value: $(value).attr('id')});
				 data.push({name: 'listaFechamento[' + index + '].fisico', value: $(value).val()});
			 }
		 });
		 
		return data;
	},

	 limpaGridPesquisa : function() {
		 
		 $(".fechamentoGrid", fechamentoEncalheController.workspace).clear();
		 $('#divFechamentoGrid', fechamentoEncalheController.workspace).css("display", "none");
	},
	
	salvarNoEncerrementoOperacao : function() {
		
		$.postJSON(
			contextPath + "/devolucao/fechamentoEncalhe/salvarNoEncerrementoOperacao",
			fechamentoEncalheController.populaParamentrosFechamentoEncalheInformados(),
			function (result) {
				
				var tipoMensagem = result.tipoMensagem;
				
				var listaMensagens = result.listaMensagens;
				
				if (tipoMensagem && listaMensagens) {
					
					exibirMensagem(tipoMensagem, listaMensagens);
				} else {
					
					fechamentoEncalheController.verificarEncerrarOperacaoEncalhe();
					
				}
			},
		  	null,
		   	false
		);
	},
	
	analiticoEncalhe : function() {
		$('#workspace').tabs('addTab', "Anal&iacute;tico Encalhe", "/nds-client/devolucao/fechamentoEncalhe/analitico");
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
	
	retirarCheckBox : function(index) {
		if($("#ch" + index).is(":checked")) {
			$("#ch" + index).attr("checked", false);
		}
		if($("#sel").is(":checked")) {
			$("#sel").attr("checked", false);
		}
	},
	
	obterStatusCobrancaCota : function() {
		
		$.postJSON(contextPath + "/devolucao/fechamentoEncalhe/obterStatusCobrancaCota", 
			null,
			function(result) {
				if(result=='FINALIZADO') {
					$('#mensagemLoading').text('Aguarde, carregando ...');
					for (var i = 1; i <= fechamentoEncalheController.statusCobrancaCota; i++)
				        window.clearInterval(i);
				}	
				$('#mensagemLoading').text(result);
			});	
	} 
	
}, BaseController);
//@ sourceURL=fechamentoEncalhe.js

var negociacaoDividaController = $.extend(true, {

	path : contextPath + '/financeiro/negociacaoDivida/',

	init : function() {
		negociacaoDividaController.initGridNegociacao();
		negociacaoDividaController.initGridNegociacaoDetalhe();
	},

	pesquisarCota : function(numeroCota) {

		$(".grids", this.workspace).hide();
		
		$.postJSON(contextPath + '/cadastro/cota/pesquisarPorNumero',
				"numeroCota=" + numeroCota, 
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
	
	pesquisarDetalhes : function() {
		
		var params = $("#negociacaoDividaForm", this.workspace).serialize();
		
		$(".negociacaoDetalheGrid", this.workspace).flexOptions({
			url : this.path + 'pesquisarDetalhes.json?' + params, 
			newp : 1
		});
			
		$(".negociacaoDetalhesGrid").flexReload();
		
		
	},
	
	montaColunaDetalhesAcao : function(data) {
		
		var total = '0,00';
		
		$.each(data.rows, function(index, value) {
			
			var detalhes = '<a href="javascript:;" onclick="negociacaoDividaController.popup_detalhe();" title="Ver Detalhes"><img src="' + contextPath + '/images/ico_detalhes.png" alt="Detalhes" border="0" /></a>    ';
			var acao = '<input type="checkbox" class="negociacaoCheck" onclick="negociacaoDividaController.verificarCheck()"></input> ';
			value.cell.detalhes = detalhes;
			value.cell.acao = acao;
			
			total = sumPrice(value.cell.total, total);
		});

		$("#total", this.workspace).html(total);
		
		return data;
	},
	
	calcularParcelas : function(){
		
		var params = $("#formaPgtoForm").serialize();
		alert(params);
	},
	
	
	popup_detalhe : function() {
		negociacaoDividaController.pesquisarDetalhes();
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
		$('#formaPgto_numEnomeCota').html('<strong>Cota:</strong> ' + $('#negociacaoDivida_numCota').val() +' - <strong>Nome: </strong>'+ $('#negociacaoDivida_nomeCota').html());
		$('#dividaSelecionada').html('<strong>Divida Selecionada:</strong> ' + $('#totalSelecionado').html());
		$("#dialog-formaPgto").dialog({
			form: $("#dialog-formaPgto", this.workspace).parents("formaPgtoForm"),
			resizable: false,
			height:550,
			width:760,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					$("#effect").show("highlight", {}, 1000, callback);
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	},
	
	geraLinhasCheque :function(value) {
		var tabela = $('#tabelaCheque').get(0);
		
		while(tabela.rows.length > 1){
			tabela.deleteRow(1);
		}
		for (var i=1; i <= value; i++){
			var linha = tabela.insertRow(i);
			var coluna1 = linha.insertCell(0);
			var coluna2 = linha.insertCell(1);
			var coluna3 = linha.insertCell(2);
			var coluna4 = linha.insertCell(3);
			
			for (var j=0; j < tabela.rows[i].cells.length; j++){
				tabela.rows[i].cells[j].style.textAlign = "center";
			}
			
			coluna1.innerHTML = '<td><input type="text" name="textfield" id="textfield" style="width:100px;" /></td>';
			coluna2.innerHTML = '<td><input type="text" name="textfield2" id="textfield2" style="width:100px; text-align:right;" /></td>';
			coluna3.innerHTML = '<td><input type="text" name="textfield3" id="textfield3" style="width:100px;" /></td>';
			coluna4.innerHTML = '<td align="center"><img src="'+contextPath+'/images/ico_excluir.gif" border="0" align="Excluir Linha" /></td>';
		}
	},
	
	geraLinhasParcelas : function(value) {
		var tabela = $('#tabelaParcelas').get(0);
		
		while(tabela.rows.length > 2){
			tabela.deleteRow(2);
		}
		for (var i=1; i <= value; i++){
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
			coluna1.innerHTML = i+'&ordf;';
			coluna2.innerHTML = '<input type="text" name="textfield" id="textfield" style="width: 65px;" />';
			coluna3.innerHTML = '<input type="text" name="textfield3" id="textfield3" style="width: 60px; text-align: right;" />';
			coluna4.innerHTML = '<input type="text" name="textfield13" id="textfield13" style="width: 60px; text-align: right;" />';
			coluna5.innerHTML = '<input type="text" name="textfield2" id="textfield2" style="width: 60px; text-align: right;" />';
			coluna6.innerHTML = '<input type="radio" name="radio" id="radio" value="radio" />';
			
		}
	},

	comissaoCota : function() {
		$('.comissaoAtual').show();
		$('.pgtos').hide();
		$('.semanal').hide();
		$('.quinzenal').hide();
		$('.mensal').hide();
		$('#gridVenctos').hide();
		$('#gridCheque').hide();
		$('#divChequeDeposito').hide();
		$('#divBoleto').hide();
		$('#divTransferencia').hide();
		
	},
	
	mostraPgto : function() {
		
		$('.comissaoAtual').hide();
		$('.pgtos').show();
	
	},
	
	mostraSemanal : function(){
		$('.semanal').show();
		$('.quinzenal').hide();
		$('.mensal').hide();
		
	},
	
	mostraMensal :function(){
		$('.semanal').hide();
		$('.quinzenal').hide();
		$('.mensal').show();
	},
	
	mostraDiario : function(){
		$('.semanal').hide();
		$('.quinzenal').hide();
		$('.mensal').hide();
	},
		
	mostraQuinzenal : function(){
		$('.semanal').hide();
		$('.quinzenal').show();
		$('.mensal').hide();
	},
	
	opcaoFormasPagto : function(value){
		
		
		if (value == 1 || value == 2 || value == 4){
			$('#gridVenctos').show();
			$('#gridCheque').hide();
			negociacaoDividaController.geraLinhasParcelas($('#selectParcelas').val());
			if(value == 1){
				$('#divChequeDeposito').hide();
				$('#divBoleto').show();
				$('#divTransferencia').hide();
			}else if(value == 2){
				$('#divChequeDeposito').hide();
				$('#divBoleto').hide();
				$('#divTransferencia').show();
			}else{
				$('#divChequeDeposito').show();
				$('#divBoleto').hide();
				$('#divTransferencia').hide();
			}
		}else if (value == 3){
			$('#gridVenctos').hide();
			$('#gridCheque').show();
			$('#divChequeDeposito').show();
			$('#divBoleto').hide();
			$('#divTransferencia').hide();
			negociacaoDividaController.geraLinhasCheque($('#selectParcelas').val());
		}else {
			$('#gridVenctos').hide();
			$('#gridCheque').hide();
			$('#divChequeDeposito').hide();
			$('#divBoleto').hide();
			$('#divTransferencia').hide();
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
		
		$("#negociacaoCheckAll").get(0).checked = todosChecados;
	},
	
	
	checkAll : function (check) {
		
		$(".negociacaoCheck").each(function(index, element) {
			element.checked = check.checked;
		});
		negociacaoDividaController.verificarCheck();
	},
	
	initGridNegociacao : function() {
		$(".negociacaoGrid").flexigrid({
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
				sortable : true,
				align : 'center',
			}],
			sortname : "dtEmissao",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 180
		});
	},
	
	initGridNegociacaoDetalhe : function() {
		$(".negociacaoDetalheGrid").flexigrid({
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

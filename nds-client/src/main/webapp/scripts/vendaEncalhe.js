var VENDA_PRODUTO = {
	
	vendaNova:true,
	tipoVenda:"",

	inicializar : function() {
		$(function() {

			$("#vendaEncalhesGridCota", VENDA_PRODUTO.workspace).flexigrid({
				preProcess:VENDA_PRODUTO.executarPreProcessamentoGridVenda,
				onSuccess: VENDA_PRODUTO.formatarCampos,
				dataType : 'json',
				colModel :[ {
					display : 'Código de Barras',
					name : 'codigoBarras',
					width : 100,
					sortable : true,
					align : 'left'
				}, { 
					display : 'Código',
					name : 'codigoProduto',
					width : 60,
					sortable : true,
					align : 'left'
				}, {
					display : 'Produto',
					name : 'nomeProduto',
					width : 120,
					sortable : true,
					align : 'left'
				}, {
					display : 'Edição',
					name : 'numeroEdicao',
					width : 50,
					sortable : true,
					align : 'center'
				}, {
					display : 'Preço c/ Desc. R$',
					name : 'precoDesconto',
					width : 90,
					sortable : true,
					align : 'center'
				}, {
					display : 'Qtde Disp.',
					name : 'qntDisponivel',
					width : 55,
					sortable : true,
					align : 'center'
				}, {
					display : 'Qtde Solic.',
					name : 'qntSolicitada',
					width : 55,
					sortable : true,
					align : 'center'
				}, {
					display : 'Total R$',
					name : 'total',
					width : 65,
					sortable : true,
					align : 'center'
				 
				}, {
					display : 'Tipo Venda',
					name : 'tipoVendaEncalhe',
					width : 135,
					sortable : true,
					align : 'left'
				}, {
				
					display : 'Forma de Venda',
					name : 'formaVenda',
					width : 130,
					sortable : true,
					align : 'left'
				}],
				width : 1000,
				height : 220
			});
		});
		
	},
	
	executarPreProcessamento:function (resultado){
		
		if (resultado.mensagens) {

			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			
			VENDA_PRODUTO.processarVisualizcaoImpressao(false);

			return resultado.tableModel;
		}
		
		// Monta as colunas com os inputs do grid
		$.each(resultado.tableModel.rows, function(index, row ) {
			
			var linkEdicao="";
			var linkExclusao="";
			
			if(row.cell.edicaoExclusaoItem){
				
				linkEdicao = '<a isEdicao="true" href="javascript:;" onclick="VENDA_PRODUTO.editar('+ row.cell.idVenda +');" style="margin-right: 5px;cursor:pointer">' +
				 '<img src="'+ contextPath +'/images/ico_editar.gif" hspace="5" border="0px" title="Editar Venda" />' +
				 '</a>';			
			 
				linkExclusao ='<a isEdicao="true" href="javascript:;" onclick="VENDA_PRODUTO.exibirDialogExclusao('+ row.cell.idVenda +' );" style="margin-right: 5px;cursor:pointer">' +
                '<img src="'+ contextPath +'/images/ico_excluir.gif" hspace="5" border="0px" title="Excluir Venda" />' +
                 '</a>';		 					 
				
				var acaoReimprimirComprovante = contextPath + "/devolucao/vendaEncalhe/reimprimirComprovanteVenda/" + row.cell.idVenda;
				
				
				linkReimpressao  ='<a href="'+acaoReimprimirComprovante+'" target="_blank" style="margin-right: 5px;cursor:pointer">' +
                '<img src="'+ contextPath +'/images/ico_impressora.gif" hspace="5" border="0px" title="Reimpressão do Comprovante de Venda" />' +
                 '</a>';	
				
				
			}
			else{
				
				linkEdicao = '<a isEdicao="true" href="javascript:;" style="margin-right: 5px;;cursor:default; opacity:0.4; filter:alpha(opacity=40)">' +
				 '<img src="'+ contextPath +'/images/ico_editar.gif" hspace="5" border="0px" title="Editar Venda" />' +
				 '</a>';			
			 
				linkExclusao ='<a isEdicao="true" href="javascript:;" style="margin-right: 5px;;cursor:default; opacity:0.4; filter:alpha(opacity=40)">' +
               '<img src="'+ contextPath +'/images/ico_excluir.gif" hspace="5" border="0px" title="Excluir Venda" />' +
                '</a>';		
				linkReimpressao  ='<a href="javascript:;" style="margin-right: 5px;cursor:default; opacity:0.4; filter:alpha(opacity=40)">' +
                '<img src="'+ contextPath +'/images/ico_impressora.gif" hspace="5" border="0px" title="Reimpressão do Comprovante de Venda" />' +
                 '</a>';	
			
			}
		
           row.cell.acao = linkReimpressao + linkEdicao + linkExclusao ; 
		});
		
		$("#totalGeral", VENDA_PRODUTO.workspace).html(resultado.totalGeral);
		
		VENDA_PRODUTO.processarVisualizcaoImpressao(true);
		
		return resultado.tableModel;
	},	
	editar:function(idVenda){
		
		VENDA_PRODUTO.limparDadosModalVenda();
		
		VENDA_PRODUTO.vendaNova = false;
		
		VENDA_PRODUTO.processarVisualizacaoDataVencimento();
		
		$("#vendaEncalhesGridCota", VENDA_PRODUTO.workspace).flexOptions({
			params:[{name:"idVendaEncalhe", value:idVenda}],
			url: contextPath + "/devolucao/vendaEncalhe/prepararDadosEdicaoVenda",
			newp: 1
		});

		$("#vendaEncalhesGridCota", VENDA_PRODUTO.workspace).flexReload();		
	},
	
	excluir:function(idVenda){
		
		$.postJSON(contextPath + "/devolucao/vendaEncalhe/excluir",
				"idVenda="+idVenda, 
				function(result){
					
					exibirMensagem(
							result.tipoMensagem, 
							result.listaMensagens
					);
			
					VENDA_PRODUTO.pesquisarVendas();
				}
		);
	},
	
	exibirDialogExclusao:function(idVenda){
		
		$("#dialog-excluirVenda", VENDA_PRODUTO.workspace ).dialog({
			resizable: false,
			height:'auto',
			width:250,
			modal: true,
			buttons: {
				"Confirmar": function() {
					VENDA_PRODUTO.excluir(idVenda);
					$( this ).dialog( "close" );
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			},
			form: $("#dialog-excluirVenda", this.workspace).parents("form")
		});
	},
		
	params:function(){
		
		var numeroCota = $("#vend-suplementar-numCota", VENDA_PRODUTO.workspace).val();
		var tipoVenda = $("#vend-suplementar-selectTipoVenda", VENDA_PRODUTO.workspace).val();
		var periodoInicial = $("#vend-suplementar-periodoDe", VENDA_PRODUTO.workspace).val();
		var periodoFinal = $("#vend-suplementar-periodoAte", VENDA_PRODUTO.workspace).val();
		
		var param = [];
		
		if(numeroCota.length > 0 ){
			param.push({name:"numeroCota",value:numeroCota});
		}
		
		if(tipoVenda.length > 0 ){
			param.push( {name:"tipoVenda",value:tipoVenda});
		}
		
		if(periodoInicial.length > 0 ){
			param.push({name:"periodoInicial",value:periodoInicial});
		}
		
		if(periodoFinal.length > 0 ){
			param.push({name:"periodoFinal",value:periodoFinal});
		}
		
		return param;
	},	
		
	pesquisarCotaSuccessCallBack:function(){
		$("#span_nome_cota", VENDA_PRODUTO.workspace).html($("#vend-suplementar-descricaoCota", VENDA_PRODUTO.workspace).val());
	},
	
	pesquisarCotaErrorCallBack:function(){
		$("#span_nome_cota", VENDA_PRODUTO.workspace).html("");
	},
	
	pesquisarCotaVendaSuccessCallBack:function() {
		
		$("#span_nome_cota_venda", VENDA_PRODUTO.workspace).html($("#vend-suplementar-descricaoCotaVenda", VENDA_PRODUTO.workspace).val());
		
		$.postJSON(contextPath + "/devolucao/vendaEncalhe/obterBoxCota", 
					{numeroCota:$("#vend-suplementar-numCotaVenda", VENDA_PRODUTO.workspace).val()},
					function(result) {
			 			$("#span_nome_box_venda", VENDA_PRODUTO.workspace).html(result);			
					},null,true);
					
		VENDA_PRODUTO.recalcularPrecoVendaItens(); 
	},
	
	pesquisarCotaVendaErrorCallBack:function(){
		$("#span_nome_cota_venda", VENDA_PRODUTO.workspace).html("");
		$("#span_nome_box_venda", VENDA_PRODUTO.workspace).html("");	
	},
	
	pesquisarVendas:function(){
			
		$("#vendaEncalheGridCota", VENDA_PRODUTO.workspace).flexOptions({
			url: contextPath + "/devolucao/vendaEncalhe/pesquisarVendas",
			params: VENDA_PRODUTO.params(),newp: 1
		});
		
		$("#vendaEncalheGridCota", VENDA_PRODUTO.workspace).flexReload();
	},
	
	processarVisualizcaoImpressao:function(isVisualiza){
		
		if(isVisualiza == true){
			 $("#infosRodape", VENDA_PRODUTO.workspace).show();
			 $("#gridVenda", VENDA_PRODUTO.workspace).show();
			 $("#labelTotalGeral", VENDA_PRODUTO.workspace).show();
			 $("#totalGeral", VENDA_PRODUTO.workspace).show();
			 
			 if($("#vend-suplementar-selectTipoVenda", VENDA_PRODUTO.workspace).val() == "ENCALHE"){
				$("#divImprimirEncalhe", VENDA_PRODUTO.workspace).show();
				$("#divImprimirSuplementar", VENDA_PRODUTO.workspace).hide();
			 }
			 else if ($("#vend-suplementar-selectTipoVenda", VENDA_PRODUTO.workspace).val() == "SUPLEMENTAR"){
				 $("#divImprimirEncalhe", VENDA_PRODUTO.workspace).hide();
				 $("#divImprimirSuplementar", VENDA_PRODUTO.workspace).show(); 
			 }
			 else{
				 $("#divImprimirEncalhe", VENDA_PRODUTO.workspace).hide();
				 $("#divImprimirSuplementar", VENDA_PRODUTO.workspace).hide();
			 }
		}
		else{
			$("#infosRodape", VENDA_PRODUTO.workspace).hide();
			$("#gridVenda", VENDA_PRODUTO.workspace).hide();	
			$("#labelTotalGeral", VENDA_PRODUTO.workspace).hide();
			$("#totalGeral", VENDA_PRODUTO.workspace).hide();
			$("#divImprimirEncalhe", VENDA_PRODUTO.workspace).hide();
			$("#divImprimirSuplementar", VENDA_PRODUTO.workspace).hide();
		}	
	},
	
	processarVisualizacaoDataVencimento:function(){
		
		if(VENDA_PRODUTO.vendaNova == true){
			
			$("#div_data_inclusao", VENDA_PRODUTO.workspace).show();
			$("#div_data_edicao", VENDA_PRODUTO.workspace).hide();
		}
		else {

			$("#div_data_inclusao", VENDA_PRODUTO.workspace).hide();
			$("#div_data_edicao", VENDA_PRODUTO.workspace).show();
		}
	},
	
	exportar:function(fileType){
		
		window.location = 
			contextPath + "/devolucao/vendaEncalhe/exportar?fileType=" + fileType;
	},
	
	novaVenda:function(){
		
		VENDA_PRODUTO.limparDadosModalVenda();
		
		VENDA_PRODUTO.vendaNova = true;
		
		 $.postJSON(contextPath + "/devolucao/vendaEncalhe/obterDatavenda", 
					null,
					function(resultado) {
						 $("#span_data_venda", VENDA_PRODUTO.workspace).html(resultado.data);
						 $("#vend-suplementar-dataVencimento", VENDA_PRODUTO.workspace).val(resultado.dataVencimentoDebito);
						 $("#vend-suplementar-dataVencimentoEdicao", VENDA_PRODUTO.workspace).val(resultado.dataVencimentoDebito);
						 VENDA_PRODUTO.processarVisualizacaoDataVencimento();
					},null,true);
		

		$("#vendaEncalhesGridCota", VENDA_PRODUTO.workspace).flexOptions({
			url: contextPath + "/devolucao/vendaEncalhe/prepararDadosVenda",
			newp: 1
		});
		
		$("#vendaEncalhesGridCota", VENDA_PRODUTO.workspace).flexReload();
	},
	
	showModalVendas:function(){
		
		$("#dialog-venda-encalhe-cota", VENDA_PRODUTO.workspace).dialog({
			resizable: false,
			height:510,
			width:1030,
			modal: true,
			

			
			buttons: [
			         {id:"btn_confirmar_venda",text:"Confirmar",
		        	  click: function(event) {
		        		  
		        		  if (event.originalEvent !== undefined)
						    VENDA_PRODUTO.confirmaVenda(true);
	        	
			         }},
		        	{id:"btn_cancelar_venda",text:"Cancelar",
			         click:function(){
			       // 	 if (event.originalEvent !== undefined)
						   $( this ).dialog( "close" );
						  
		        		}	  
		        	}  
				],
			form: $("#dialog-venda-encalhe-cota", this.workspace).parents("form")
		});
	},
	
	executarPreProcessamentoGridVenda:function(result){
		
		if (result.mensagens) {

			exibirMensagem(
					result.mensagens.tipoMensagem, 
					result.mensagens.listaMensagens
			);
			return result;
		}else{
			
			VENDA_PRODUTO.showModalVendas();
			
			if(VENDA_PRODUTO.vendaNova == true){
			
				return VENDA_PRODUTO.processarDadosNovaVenda(result);
			}
			
			return VENDA_PRODUTO.processarDadosEdicaoVenda(result) ;
		}
		
	},
	
	processarDadosEdicaoVenda:function(result){
		
		var tipoVenda ="";
		
		// Monta as colunas com os inputs do grid
		$.each(result.rows, function(index, row) {
			
			tipoVenda = row.cell.tipoVenda;
			
			var hiddenId = '<input type="hidden" name="idVendaEncalhe" value="' + row.cell.idVendaEncalhe + '" />';
			
			var inputCodBarras ='<input type="text" id="codBarras0" onkeypress="return event.keyCode != 13;" name="codBarras" style="width:90px;" disabled="disabled" value="'+row.cell.codigoBarras+'"/>';
			
			var inputCodProduto='<input type="text" id="codProduto0" value="'+row.cell.codigoProduto+'" disabled="disabled" name="codProduto" style="width:55px; float:left; margin-right:10px;"/>';
			
			var inputNomeProduto='<input type="text" id="nmProduto0" value="'+row.cell.nomeProduto+'" name="nmProduto" style="width:110px;" disabled="disabled" maxlenght="255"/>';
			
			var inputNumEdicao='<input type="text" id="numEdicao0" value="'+row.cell.numeroEdicao+'"  name="numEdicao" style="width:45px; text-align: center;" maxlenght="20" disabled="disabled" />';
			
			var inputPrecoDesconto='<input type="text" id="precoDesconto0" value="'+row.cell.precoDesconto+'" name="precoDesconto" style="width:75px; text-align: center;" disabled="disabled" />';
			
			var inputQntDisponivel='<input type="text" id="qntDisponivel0" name="qntDisponivel" style="width:50px; text-align: center;" maxlenght="20" value="'+row.cell.qntDisponivel+'" disabled="disabled" />';
			
			var inputQntSolicitada='<input type="text" id="qntSolicitada0" name="qntSolicitada" style="width:50px; text-align: center;" maxlenght="6" value="'+row.cell.qntSolicitada+'" onchange="VENDA_PRODUTO.totalizarValorProdutoSelecionado(0);" />';
			
			var inputTotal ='<input type="text" id="totalFormatado0" name="totalFormatado" value="'+row.cell.total+'" style="width:50px; text-align: center;" disabled="disabled" />';
			
			var inputHiddenTotal = '<input id="hiddenTotal'+index+'" name="hiddenTotal" type="hidden" value="'+row.cell.valorTotal+'">';
			
			var inputTipoVenda='<input type="text" id="tipoVenda'+index+'" name="tipoVenda" value="'+row.cell.descTipoVenda+'" disabled="disabled" />';
			
			var inputFormaVenda = VENDA_PRODUTO.getInputFormaVenda(tipoVenda, index, row.cell.formaComercializacao, row.cell.formaVenda);
			
			var inputHiddenTipoComercailizacao = '<input id="hiddenComercializacao'+index+'" name="hiddenComercializacao" type="hidden" value="'+row.cell.formaComercializacao+'">';
			
			$("#qntSolicitada0", VENDA_PRODUTO.workspace).keypress(function(e) {
				if (e.keyCode == 13) {
					VENDA_PRODUTO.totalizarValorProdutoSelecionado(0);
				}
			});
			
			row.cell.codigoBarras = inputCodBarras + hiddenId;
			row.cell.codigoProduto = inputCodProduto;
			row.cell.nomeProduto = inputNomeProduto;
			row.cell.numeroEdicao = inputNumEdicao;
			row.cell.precoDesconto = inputPrecoDesconto;
			row.cell.qntDisponivel = inputQntDisponivel;
			row.cell.qntSolicitada = inputQntSolicitada;
			row.cell.total =inputTotal + inputHiddenTotal;
			row.cell.tipoVendaEncalhe = inputTipoVenda;
			row.cell.formaVenda = inputFormaVenda + inputHiddenTipoComercailizacao;
			
			$("#span_nome_box_venda", VENDA_PRODUTO.workspace).html(row.cell.codBox);
			$("#vend-suplementar-dataVencimento", VENDA_PRODUTO.workspace).val(row.cell.dataVencimentoDebito);
			$("#vend-suplementar-dataVencimentoEdicao", VENDA_PRODUTO.workspace).val(row.cell.dataVencimentoDebito);
			$("#span_data_venda", VENDA_PRODUTO.workspace).html(row.cell.dataVenda);
			$("#span_nome_cota_venda", VENDA_PRODUTO.workspace).html(row.cell.nomeCota);
			$("#vend-suplementar-numCotaVenda", VENDA_PRODUTO.workspace).val(row.cell.numeroCota);
		});
		
		$("#vend-suplementar-numCotaVenda", VENDA_PRODUTO.workspace).attr("disabled","disabled");
		$("#qntSolicitada0", VENDA_PRODUTO.workspace).focus();
		
		VENDA_PRODUTO.tipoVenda = tipoVenda;
			
		return result;
	},
	
	processarDadosNovaVenda:function(result){
		
		// Monta as colunas com os inputs do grid 
		$.each(result.rows, function(index, row) {
			
			var funcaoError = "function(){ VENDA_PRODUTO.pesquisarDadosProdutoError("+ index + ')}';
			
			var parametroPesquisaProduto = '\'#codProduto' + index + '\', \'#nmProduto' + index + '\', \'#numEdicao' + index + '\', true, '+funcaoError+','+funcaoError+' ';
		
			var hiddenId = '<input type="hidden" name="id" value="' + index + '" />';
			
			var valorHiddenTotal = ( row.cell.valorTotal )?row.cell.valorTotal:"";
			
			var inputHiddenTotal = '<input id="hiddenTotal'+index+'" name="hiddenTotal" type="hidden" value="'+valorHiddenTotal+'">';
			
			var inputHiddenTipoVenda = '<input id="hiddenTipoVenda'+index+'" name="hiddenTipoVenda" type="hidden" value="'+row.cell.tipoVenda+'">';
			
			var inputHiddenTipoComercailizacao = '<input id="hiddenComercializacao'+index+'" name="hiddenComercializacao" type="hidden" value="'+row.cell.formaComercializacao+'">';
			
			$("#codBarras"+index, VENDA_PRODUTO.workspace).keypress(function(e) {
				//alert(e.keyCode);
				if (e.keyCode == 13) {
					VENDA_PRODUTO.pesquisarProdutoCodBarra("#codBarras"+index, index);
					e.preventDefault();
					return false;
				}
			});
			
			$("#qntSolicitada"+index, VENDA_PRODUTO.workspace).keypress(function(e) {
				if (e.keyCode == 13) {
					VENDA_PRODUTO.totalizarValorProdutoSelecionado(index);
					$("#codBarras"+index+1, VENDA_PRODUTO.workspace).focus();
					e.preventDefault();
					return false;
				}
			});
			
			row.cell.codigoBarras = VENDA_PRODUTO.getInputCodBarras(index,row.cell) + hiddenId;
			row.cell.codigoProduto = VENDA_PRODUTO.getInputCodigoProduto(index,row.cell,parametroPesquisaProduto);
			row.cell.nomeProduto = VENDA_PRODUTO.getInputNomeProduto(index,row.cell,parametroPesquisaProduto);
			row.cell.numeroEdicao = VENDA_PRODUTO.getInputNumeroEdicao(index,row.cell,funcaoError);
			row.cell.precoDesconto = VENDA_PRODUTO.getInputPrecoDesconto(index,row.cell);
			row.cell.qntDisponivel = VENDA_PRODUTO.getInputQntdeDisponivel(index,row.cell);
			row.cell.qntSolicitada = VENDA_PRODUTO.getInputQntdeSolicitada(index,row.cell);
			row.cell.total = VENDA_PRODUTO.getInputTotal(index,row.cell) + inputHiddenTotal;
			row.cell.tipoVendaEncalhe = VENDA_PRODUTO.getInputTipoVenda(index,row.cell) + inputHiddenTipoVenda;
			row.cell.formaVenda = VENDA_PRODUTO.getInputFormaVenda(result.tipoVenda, index, row.cell.formaComercializacao, row.cell.formaComercializacao == 'CONTA_FIRME') + inputHiddenTipoComercailizacao;
		
		});
		
		$("#vend-suplementar-numCotaVenda", VENDA_PRODUTO.workspace).attr("disabled",null);
		
		VENDA_PRODUTO.tipoVenda="";
		if(VENDA_PRODUTO.tipoVenda == 'SUPLEMENTAR'){
				$("input[name=qntSolicitada]", VENDA_PRODUTO.workspace).attr("class","sum_qntSolicitada_suplemtar");
				$("input[name=hiddenTotal]", VENDA_PRODUTO.workspace).attr("class","sum_total_suplemtar");
			}
			else{
				$("input[name=qntSolicitada]", VENDA_PRODUTO.workspace).attr("class","sum_qntSolicitada_encalhe");
				$("input[name=hiddenTotal]", VENDA_PRODUTO.workspace).attr("class","sum_total_encalhe");
		}
		return result;
	},
	
	getInputCodBarras:function(index,result){
		var valor = "";
		if(result && result.codigoBarras){
			valor = result.codigoBarras;
		}
		var parametroPesCodBarra ='\'#codBarras'+ index+ '\',' + index;
		
		return '<input type="text" id="codBarras'+index+'" onkeypress="return event.keyCode != 13;" name="codBarras" style="width:90px;" value="'+valor+'" onkeyup="VENDA_PRODUTO.pesquisarProdutoCodBarra('+parametroPesCodBarra+')"/>';
	},
	
	getInputCodigoProduto:function(index,result,parametroPesquisaProduto){
		var valor = "";
		if(result && result.codigoProduto){
			valor = result.codigoProduto;
		}
		return '<input value="'+valor+'" type="text" id="codProduto' + index + '" name="codProduto" style="width:55px; float:left; margin-right:10px;" maxlenght="255" onchange="pesquisaProdutoVendaEncalhe.pesquisarPorCodigoProduto(' + parametroPesquisaProduto + ');" />';
	},
	
	getInputNomeProduto:function(index,result,parametroPesquisaProduto){
		var valor = "";
		if(result && result.nomeProduto){
			valor = result.nomeProduto;
		}
		
		var parametroAutoCompleteProduto = '\'#nmProduto' + index + '\', true';
		
		return '<input value="'+valor+'" type="text" id="nmProduto' + index + '" name="nmProduto" style="width:110px;" maxlenght="255" />';
	},
	
	getInputNumeroEdicao:function(index,result,funcaoError){
		var valor = "";
		var disabilitado = 'disabled="disabled"';
		if(result && result.numeroEdicao){
			valor = result.numeroEdicao;
			disabilitado ="";
		}
		
		var funcaoSucces = "function(idCodigo, idEdicao){ VENDA_PRODUTO.pesquisarDadosProduto(idCodigo, idEdicao,"+ index + ")}";
		
		var parametroValidacaoEdicao = '\'#codProduto' + index + '\', \'#numEdicao' + index + '\', true, '+funcaoSucces+','+funcaoError+'';
		
		return '<input value="'+valor+'" type="text" id="numEdicao' + index + '"  name="numEdicao" style="width:45px;text-align: center;" maxlenght="20" onchange="pesquisaProdutoVendaEncalhe.validarNumEdicao(' + parametroValidacaoEdicao + ');" '+disabilitado+' />';
	},
	
	getInputPrecoDesconto:function(index,result){
		var valor = "";
		if(result && result.precoDesconto){
			valor = result.precoDesconto;
		}
		return '<input type="text" id="precoDesconto'+index+'" name="precoDesconto" value="'+valor+'" style="width:75px;text-align: center;" disabled="disabled" />';
	},
	
	getInputQntdeSolicitada:function(index,result){
		var valor = "";
		var disabilitado = 'disabled="disabled"';
		var classs = "";
		if(result && result.qntSolicitada){
			valor = result.qntSolicitada;
			disabilitado="";
			
			if( result.descTipoVenda ){
				classs = (result.descTipoVenda == 'Suplementar')?"sum_qntSolicitada_suplemtar":"sum_qntSolicitada_encalhe";
			}
		}
		return '<input class="'+classs+'" type="text" id="qntSolicitada'+index+'" name="qntSolicitada" style="width:50px;text-align: center;" maxlenght="6" value="'+valor+'" onchange="VENDA_PRODUTO.totalizarValorProdutoSelecionado('+index+');" '+disabilitado+' />';
	},
	
	getInputQntdeDisponivel:function(index,result){
		var valor = "";
		if(result && result.qntDisponivel){
			valor = result.qntDisponivel;
		}
		return '<input type="text" id="qntDisponivel'+index+'" name="qntDisponivel" style="width:50px;text-align: center;" maxlenght="20" value="'+valor+'" disabled="disabled" />';
	},
		
	getInputTotal:function(index,result){
		var valor = "";
		var classs = "";
		if(result && result.total){
			valor = result.total;
			if( result.descTipoVenda ){
				classs = (result.descTipoVenda == 'Suplementar')?"sum_total_suplemtar":"sum_total_encalhe";
			}
		}
		return '<input class="'+classs+'" type="text" id="totalFormatado'+index+'" name="totalFormatado" value="'+valor+'" style="width:50px;text-align: center;" disabled="disabled" />';
	},
	
	getInputTipoVenda:function(index,result){
		var valor = "";
		if(result && result.descTipoVenda){
			valor = result.descTipoVenda;
		}
		return '<input type="text" id="tipoVenda'+index+'" name="tipoVenda" value="'+valor+'" disabled="disabled" />';
	},
	
	getInputFormaVenda: function(tipoVenda, index, formaDefault, descFormaDefault, isProdutoContaFirme) {
		
		if (tipoVenda == 'SUPLEMENTAR'){
			
			if(isProdutoContaFirme){
				
				return '<input type="text" id="formaVenda'+index+'" name="formaVenda" value="'+ (descFormaDefault ? descFormaDefault : '') +'" disabled="disabled" />';
			}
			
			var disabled = '';
			
			if (VENDA_PRODUTO.vendaNova == false){
				
				disabled = 'disabled="disabled"';
			}
			
			return '<select style="width: 120px;" '+ disabled +' onchange="VENDA_PRODUTO.setCampoHiddenFormaVenda(this, '+ index +');">'+
					'<option '+ (formaDefault == 'CONTA_FIRME' ? 'selected="true"' : '') +' value="CONTA_FIRME">Conta Firme</option>' +
					'<option '+ (formaDefault == 'CONSIGNADO' ? 'selected="true"' : '') +' value="CONSIGNADO">Consignado</option></select>';
		}
		
		return '<input type="text" id="formaVenda'+index+'" name="formaVenda" value="'+ (descFormaDefault ? descFormaDefault : '') +'" disabled="disabled" />';
	},
	
	setCampoHiddenFormaVenda:function(item, index){
		
		$("#hiddenComercializacao" + index, VENDA_PRODUTO.workspace).val(item.value);
	},
	
	totalizarValorProdutoSelecionado:function(indiceLinha){
		
		if($("#qntSolicitada"+indiceLinha, VENDA_PRODUTO.workspace).val().trim().length > 0){
			
			var data = [
			            {name:"precoProduto", value:$("#precoDesconto"+indiceLinha, VENDA_PRODUTO.workspace).val()},
			            {name:"qntSolicitada", value:$("#qntSolicitada"+indiceLinha, VENDA_PRODUTO.workspace).val()},
			            {name:"qntDisponivel", value:$("#qntDisponivel"+indiceLinha, VENDA_PRODUTO.workspace).val()}];
			
			 $.postJSON(
						contextPath + "/devolucao/vendaEncalhe/totalizarValorProduto", 
						data,
						function(resultado) {					
							$("#hiddenTotal" + indiceLinha, VENDA_PRODUTO.workspace).val(resultado.total);
							$("#totalFormatado" + indiceLinha, VENDA_PRODUTO.workspace).val(resultado.totalFormatado);
							
							VENDA_PRODUTO.totalizarQntTotalGeral();
							VENDA_PRODUTO.totalizarQntSolicitadaGeral();
							
							var proximoIndex = indiceLinha + 1;
							
							$("#codBarras" + proximoIndex, VENDA_PRODUTO.workspace).focus();
						},
						function(resultado){
							$("#hiddenTotal" + indiceLinha, VENDA_PRODUTO.workspace).val("");
							$("#totalFormatado" + indiceLinha, VENDA_PRODUTO.workspace).val("");
							$("#qntSolicitada" + indiceLinha, VENDA_PRODUTO.workspace).focus();
							
							VENDA_PRODUTO.totalizarQntTotalGeral();
							VENDA_PRODUTO.totalizarQntSolicitadaGeral();
						}, 
						true
					);
		}else{
			$("#hiddenTotal" + indiceLinha, VENDA_PRODUTO.workspace).val("");
			$("#totalFormatado" + indiceLinha, VENDA_PRODUTO.workspace).val("");
			VENDA_PRODUTO.totalizarQntTotalGeral();
			VENDA_PRODUTO.totalizarQntSolicitadaGeral();
		}
	},
	
	totalizarQntDisponivelGeral:function(){
	
		var valorTotal = 0;
		$.each($("input[id^='qntDisponivel']", VENDA_PRODUTO.workspace), function(index,inp){
			if (inp.value){
				valorTotal += parseInt(inp.value);
			}
		});
		$("#span_total_disponivel_venda", VENDA_PRODUTO.workspace).html(valorTotal);
	},
	
	totalizarQntSolicitadaGeral:function(){
		
		var valorTotalSup = 0;
		$.each($(".sum_qntSolicitada_suplemtar", VENDA_PRODUTO.workspace), function(index,inp){
			valorTotalSup += parseInt(inp.value);
		});
		$("#span_qntSolicitada_suplementar_venda", VENDA_PRODUTO.workspace).text(valorTotalSup);
		
		var valorTotalEnc = 0;
		$.each($(".sum_qntSolicitada_encalhe", VENDA_PRODUTO.workspace), function(index,inp){
			valorTotalEnc += parseInt(inp.value);
		});
		$("#span_qntSolicitada_encalhe_venda", VENDA_PRODUTO.workspace).text(valorTotalEnc);
		
		$("#span_total_solicitado_venda", VENDA_PRODUTO.workspace).text(valorTotalSup + valorTotalEnc); 
	},
	
	totalizarQntTotalGeral:function(){
		
		var valorTotal = 0;
		$.each($(".sum_total_suplemtar", VENDA_PRODUTO.workspace), function(index,inp){
			valorTotal += parseFloat(inp.value);
		});
		$("#span_total_suplementar_venda", VENDA_PRODUTO.workspace).text($.formatNumber(valorTotal, {format:"#,##0.0000", locale:"br"}));
		
		valorTotal = 0;
		$.each($(".sum_total_encalhe", VENDA_PRODUTO.workspace), function(index,inp){
			valorTotal += parseFloat(inp.value);
		});
		$("#span_total_encalhe_venda", VENDA_PRODUTO.workspace).text($.formatNumber(valorTotal, {format:"#,##0.0000", locale:"br"}));
		
		valorTotal = 0;
		$.each($("input[id^='hiddenTotal']", VENDA_PRODUTO.workspace), function(index,inp){
			if (inp.value){
				valorTotal += parseFloat(inp.value);
			}
		});
		$("#span_total_geral_venda", VENDA_PRODUTO.workspace).text($.formatNumber(valorTotal, {format:"#,##0.0000", locale:"br"}));
	},
	
	obterDadosProduto: function(codigoProduto, edicaoProduto,index) {
		
		var data = [
			{name:"codigoProduto",value:codigoProduto},
			{name:"numeroEdicao",value:edicaoProduto},
			{name:"numeroCota",value: $("#vend-suplementar-numCotaVenda", VENDA_PRODUTO.workspace).val()}
		];
		
		 $.postJSON(
			contextPath + "/devolucao/vendaEncalhe/obterDadosDoProduto", 
			data,
			function(resultado) {
				
				VENDA_PRODUTO.atribuirDadosProduto(resultado, index);
				
				if(VENDA_PRODUTO.validaritemRepetido(index)== true){
					VENDA_PRODUTO.limparDadoVendaProduto(index);
					$("#codBarras"+index, VENDA_PRODUTO.workspace).focus();
					return;
				}
				
				var parent = $("#formaVenda"+index, VENDA_PRODUTO.workspace).parent();
				$("#formaVenda"+index, VENDA_PRODUTO.workspace).remove();
				parent.append(VENDA_PRODUTO.getInputFormaVenda(resultado.tipoVenda, index, resultado.formaComercializacao, resultado.formaVenda, resultado.formaComercializacao == 'CONTA_FIRME'));
				
				$("#numEdicao"+index, VENDA_PRODUTO.workspace).attr("disabled",null);
				$("#qntSolicitada"+index, VENDA_PRODUTO.workspace).attr("disabled",null);
				$("#qntSolicitada"+index, VENDA_PRODUTO.workspace).focus();
			
				VENDA_PRODUTO.totalizarQntDisponivelGeral();
			},
			function(resultado){

				if (resultado.mensagens) {

					exibirMensagemDialog(
							resultado.mensagens.tipoMensagem, 
							resultado.mensagens.listaMensagens,""
					);
					
					VENDA_PRODUTO.limparDadoVendaProduto(index);
					
					$("#codBarras"+index, VENDA_PRODUTO.workspace).focus();
					
					return;
				}
				
				VENDA_PRODUTO.totalizarQntDisponivelGeral();
				$("#hiddenTotal"+index, VENDA_PRODUTO.workspace).val("");
				$("#totalFormatado"+index, VENDA_PRODUTO.workspace).val("");
				$("#qntSolicitada"+index, VENDA_PRODUTO.workspace).attr("disabled","disabled");
			}, 
			true
		);
 	},
 	
 	pesquisarDadosProdutoError:function(indiceLinha){
 		
 		$("#codBarras"+indiceLinha, VENDA_PRODUTO.workspace).val("");
		$("#numEdicao"+indiceLinha, VENDA_PRODUTO.workspace).val("");
		$("#precoDesconto"+indiceLinha, VENDA_PRODUTO.workspace).val("");
		$("#qntDisponivel"+indiceLinha, VENDA_PRODUTO.workspace).val("");
		$("#qntSolicitada"+indiceLinha, VENDA_PRODUTO.workspace).val("");
		$("#hiddenTotal" + indiceLinha, VENDA_PRODUTO.workspace).val("");
		$("#totalFormatado" + indiceLinha, VENDA_PRODUTO.workspace).val("");
		$("#formaVenda"+indiceLinha, VENDA_PRODUTO.workspace).val("");
		$("#hiddenComercializacao" + indiceLinha, VENDA_PRODUTO.workspace).val("");
		$("#hiddenTipoVenda" + indiceLinha, VENDA_PRODUTO.workspace).val("");
		
		$("#qntSolicitada"+indiceLinha, VENDA_PRODUTO.workspace).attr("disabled","disabled");
		
		VENDA_PRODUTO.totalizarQntDisponivelGeral();
		VENDA_PRODUTO.totalizarQntSolicitadaGeral();
		VENDA_PRODUTO.totalizarQntTotalGeral();
 	},
 	
 	pesquisarDadosProduto:function(idCodigoProduto,idEdicaoProduto,index){
 		
 		codigoProduto = $(idCodigoProduto).val();

		edicaoProduto = $(idEdicaoProduto).val();
		
 		VENDA_PRODUTO.obterDadosProduto(codigoProduto, edicaoProduto,index);
 	},
 	
 	pesquisarProdutoCodBarra : function(idCodBarras,index){
 		
 		if($(idCodBarras).val().length <= 5){
 			return;
 		}
 		
 		var codBarras = $(idCodBarras).val();
 		
 		$.postJSON(contextPath + "/devolucao/vendaEncalhe/pesquisarProdutoCodBarra",
				{codBarra:codBarras}, 
				function(result){

					var codigoProduto = "";
					var numeroEdicao = null;
					
					 if (result.length > 1){
						
						$(idCodBarras, VENDA_PRODUTO.workspace).autocomplete({
							source: result,
							select: function(event, ui){
								
								codigoProduto = ui.item.chave.string;
								
								numeroEdicao = ui.item.chave.long;
								
			 					VENDA_PRODUTO.obterDadosProduto(codigoProduto,numeroEdicao,index);		
							},
							delay: 0
						});
						
						$(idCodBarras, VENDA_PRODUTO.workspace).autocomplete("search", codBarras);
					}
					else{
						
						$(idCodBarras, VENDA_PRODUTO.workspace).autocomplete({});
						
						codigoProduto = result[0].chave.string;
						
						numeroEdicao = result[0].chave.long;
						
	 					VENDA_PRODUTO.obterDadosProduto(codigoProduto,numeroEdicao,index);			
					}
				
 				}, function(result){
					
					//Verifica mensagens de erro do retorno da chamada ao controller.
					if (result.mensagens) {

						exibirMensagemDialog(
								result.mensagens.tipoMensagem, 
								result.mensagens.listaMensagens,""
						);
					}
					VENDA_PRODUTO.limparDadoVendaProduto(index);
					
					$(idCodBarras).focus();
					
				}, true,null
		);
 		
 	},
 
 	atribuirDadosProduto:function(resultado, index){

 		$("#codBarras"+index, VENDA_PRODUTO.workspace).val(resultado.codigoBarras);
 		$("#codProduto"+index, VENDA_PRODUTO.workspace).val(resultado.codigoProduto);
		$("#nmProduto"+index, VENDA_PRODUTO.workspace).val(resultado.nomeProduto);
		$("#numEdicao"+index, VENDA_PRODUTO.workspace).val(resultado.numeroEdicao);
		$("#precoDesconto"+index, VENDA_PRODUTO.workspace).val(resultado.precoDesconto);
		$("#qntDisponivel"+index, VENDA_PRODUTO.workspace).val(resultado.qntDisponivelProduto);
		$("#qntSolicitada"+index, VENDA_PRODUTO.workspace).val(resultado.qntProduto);
		$("#hiddenTotal"+index, VENDA_PRODUTO.workspace).val(resultado.valoTotalProduto);
		$("#totalFormatado"+index, VENDA_PRODUTO.workspace).val(resultado.valoTotalProduto);
		$("#formaVenda"+index, VENDA_PRODUTO.workspace).val(resultado.formaVenda);
		$("#tipoVenda"+index,VENDA_PRODUTO.workspace).val(resultado.tipoVendaEncalhe);
		$("#hiddenComercializacao" + index, VENDA_PRODUTO.workspace).val(resultado.formaComercializacao);
		$("#hiddenTipoVenda" + index, VENDA_PRODUTO.workspace).val(resultado.tipoVenda);
		
		if(resultado.tipoVendaEncalhe == 'Suplementar'){
			$("#qntSolicitada"+index, VENDA_PRODUTO.workspace).attr("class","sum_qntSolicitada_suplemtar");
			$("#hiddenTotal"+index, VENDA_PRODUTO.workspace).attr("class","sum_total_suplemtar");
		}
		else{
			$("#qntSolicitada"+index, VENDA_PRODUTO.workspace).attr("class","sum_qntSolicitada_encalhe");
			$("#hiddenTotal"+index, VENDA_PRODUTO.workspace).attr("class","sum_total_encalhe");
		}
 	},
 	
 	limparDadoVendaProduto:function(index){
 		
 		$("#codBarras"+index, VENDA_PRODUTO.workspace).val("");
 		$("#codProduto"+index, VENDA_PRODUTO.workspace).val("");
		$("#nmProduto"+index, VENDA_PRODUTO.workspace).val("");
		$("#numEdicao"+index, VENDA_PRODUTO.workspace).val("");
		$("#precoDesconto"+index, VENDA_PRODUTO.workspace).val("");
		$("#qntDisponivel"+index, VENDA_PRODUTO.workspace).val("");
		$("#qntSolicitada"+index, VENDA_PRODUTO.workspace).val("");
		$("#hiddenTotal"+index, VENDA_PRODUTO.workspace).val("");
		$("#totalFormatado"+index, VENDA_PRODUTO.workspace).val("");
		$("#formaVenda"+index, VENDA_PRODUTO.workspace).val("");
		$("#tipoVenda"+index,VENDA_PRODUTO.workspace).val("");
		$("#hiddenComercializacao" + index, VENDA_PRODUTO.workspace).val("");
		$("#hiddenTipoVenda" + index, VENDA_PRODUTO.workspace).val("");
		
		$("#qntSolicitada"+index, VENDA_PRODUTO.workspace).attr("disabled","disabled");
		$("#numEdicao"+index, VENDA_PRODUTO.workspace).attr("disabled","disabled");
		
		$("#qntSolicitada"+index, VENDA_PRODUTO.workspace).removeAttr("class");
		$("#hiddenTotal"+index, VENDA_PRODUTO.workspace).removeAttr("class");
		
		VENDA_PRODUTO.totalizarQntDisponivelGeral();
		VENDA_PRODUTO.totalizarQntSolicitadaGeral();
		VENDA_PRODUTO.totalizarQntTotalGeral();
 	},
 	
	formatarCampos:function(){

		if(VENDA_PRODUTO.vendaNova == false){
			
			if(VENDA_PRODUTO.tipoVenda == 'SUPLEMENTAR'){
				$("input[name=qntSolicitada]", VENDA_PRODUTO.workspace).attr("class","sum_qntSolicitada_suplemtar");
				$("input[name=hiddenTotal]", VENDA_PRODUTO.workspace).attr("class","sum_total_suplemtar");
			}
			else{
				$("input[name=qntSolicitada]", VENDA_PRODUTO.workspace).attr("class","sum_qntSolicitada_encalhe");
				$("input[name=hiddenTotal]", VENDA_PRODUTO.workspace).attr("class","sum_total_encalhe");
			}
		}
	
		$("input[name='codProduto']", VENDA_PRODUTO.workspace).numeric();
		
		
		$("input[name='nmProduto']",VENDA_PRODUTO.workspace).autocomplete({
			source:function(param ,callback) {
				$.postJSON(contextPath + "/produto/autoCompletarPorNomeProduto", { 'nomeProduto': param.term }, callback);
			},
			select : function(event, ui) {
				var row  =  $(this).parent().parent().parent();
				$("input[name='codProduto']",row).val(ui.item.chave.codigo);
				$( "input[name='numEdicao']",row).attr("disabled",false).focus();
				
			},
			minLength: 2,
			delay : 0,
		}).keyup(function(){
			this.value = this.value.toUpperCase();
		});
		$("input[name='qntSolicitada']", VENDA_PRODUTO.workspace).justInput(/[0-9]/);
		$("input[name='numEdicao']", VENDA_PRODUTO.workspace).numeric();
		
		VENDA_PRODUTO.totalizarQntDisponivelGeral();
		VENDA_PRODUTO.totalizarQntTotalGeral();
		VENDA_PRODUTO.totalizarQntSolicitadaGeral();
	},
	
	limparGridVenda: function (){
		$('#vendaEncalhesGridCota tr', VENDA_PRODUTO.workspace).remove();
	},
	
	validaritemRepetido:function(indiceLinha){
		
		var linhasDaGrid = $("#vendaEncalhesGridCota tr", VENDA_PRODUTO.workspace);
		var retorno  = false;
		$.each(linhasDaGrid, function(index, value) {
			
			if(indiceLinha!= index){
				
				if( $("#codProduto"+index, VENDA_PRODUTO.workspace).val()!= "" 
						&& $("#codProduto"+index, VENDA_PRODUTO.workspace).val() == $("#codProduto"+indiceLinha, VENDA_PRODUTO.workspace).val()
						&& $("#numEdicao"+index, VENDA_PRODUTO.workspace).val()!= "" 
						&& $("#numEdicao"+index, VENDA_PRODUTO.workspace).val() == $("#numEdicao"+indiceLinha, VENDA_PRODUTO.workspace).val()){
					
					var mensage = $('#nmProduto' + indiceLinha, VENDA_PRODUTO.workspace).val() +" - "+ $('#numEdicao' + indiceLinha, VENDA_PRODUTO.workspace).val();
					
					exibirMensagemDialog(
							"WARNING", 
							['O produto '+mensage+ ' já foi selecionado!'],""
					);
					
					VENDA_PRODUTO.limparDadoVendaProduto(indiceLinha);
					retorno = true;
					return false;
				}
			}
		});
		return retorno;
	},
	
	getListaProduto:function(){
		
		var linhasDaGrid = $("#vendaEncalhesGridCota tr", VENDA_PRODUTO.workspace);

		var listaVendas= [];
		
		$.each(linhasDaGrid, function(index, value) {

			var linha = $(value);
			
			var colunaCodigoBarras = linha.find("td")[0];
			var colunaCodigoProduto = linha.find("td")[1];
			var colunaNomeProduto = linha.find("td")[2];
			var colunaNumeroEdicao = linha.find("td")[3];
			var colunaQntDisponivel = linha.find("td")[5];
			var colunaQtdeSolicitada = linha.find("td")[6];

			var id = 
				$(colunaCodigoBarras).find("div").find('input[name="idVendaEncalhe"]', VENDA_PRODUTO.workspace).val();
			
			var codigoProduto = 
				$(colunaCodigoProduto).find("div").find('input[name="codProduto"]', VENDA_PRODUTO.workspace).val();
			
			var nomeProduto = 
				$(colunaNomeProduto).find("div").find('input[name="nmProduto"]', VENDA_PRODUTO.workspace).val();
				
			var numeroEdicao = 
				$(colunaNumeroEdicao).find("div").find('input[name="numEdicao"]', VENDA_PRODUTO.workspace).val();
			
			var qtdeDisponivel = 
				$(colunaQntDisponivel).find("div").find('input[name="qntDisponivel"]', VENDA_PRODUTO.workspace).val();
			
			var qtdeSolicitada = 
				$(colunaQtdeSolicitada).find("div").find('input[name="qntSolicitada"]', VENDA_PRODUTO.workspace).val();
			
			var tipoVenda =
				$(linha.find("td")[8]).find("div").find('input[name="hiddenTipoVenda"]', VENDA_PRODUTO.workspace).val();
			
			var formaVenda =
				$(linha.find("td")[9]).find("div").find('input[name="hiddenComercializacao"]', VENDA_PRODUTO.workspace).val();
			
			if (VENDA_PRODUTO.isAtributosVendaVazios(codigoProduto, numeroEdicao, qtdeSolicitada)) {

				return true;
			}
			
			listaVendas.push({name:"listaVendas[" + index + "].idVenda",value:id});
			listaVendas.push({name:"listaVendas[" + index + "].codigoProduto",value:codigoProduto});
			listaVendas.push({name:"listaVendas[" + index + "].nomeProduto",value:nomeProduto});
			listaVendas.push({name:"listaVendas[" + index + "].qntDisponivelProduto",value:qtdeDisponivel});
			listaVendas.push({name:"listaVendas[" + index + "].numeroEdicao",value:numeroEdicao});
			listaVendas.push({name:"listaVendas[" + index + "].qntProduto",value:qtdeSolicitada});
			listaVendas.push({name:"listaVendas[" + index + "].tipoVendaEncalhe",value:tipoVenda});
			listaVendas.push({name:"listaVendas[" + index + "].formaVenda",value:formaVenda});

		});
		
		return listaVendas;
	},
	
	isAtributosVendaVazios:function(codigoProduto, numeroEdicao, qtdeSolicitada) {

		if (!$.trim(codigoProduto) 
				|| !$.trim(numeroEdicao) 
				|| !$.trim(qtdeSolicitada)) {

			return true;
		}
	},
	
	confirmaVenda:function(valor){

		
		$("#btn_confirmar_venda", VENDA_PRODUTO.workspace).keypress(function(e) {
			if (e.keyCode == 13) {
				VENDA_PRODUTO.confirmaVenda(false);
			}
		});
		
		var metodo = (VENDA_PRODUTO.vendaNova == true)?"confirmaNovaVenda":"confirmaEdicaoVenda";
		
		var data = VENDA_PRODUTO.getListaProduto();
		data.push({name:"numeroCota",value:$("#vend-suplementar-numCotaVenda", VENDA_PRODUTO.workspace).val()});	
		data.push({name:"dataDebito", value:$("#vend-suplementar-dataVencimento", VENDA_PRODUTO.workspace).val()});		   
     
		 $.postJSON(
			contextPath + "/devolucao/vendaEncalhe/"+metodo, 
			data,
			function(result) {
				
				exibirMensagem(
						result.tipoMensagem, 
						result.listaMensagens
					);
				
				$("#dialog-venda-encalhe-cota", VENDA_PRODUTO.workspace).dialog( "close" );
				
				VENDA_PRODUTO.imprimeSlipVendaEncalhe();	
			},
			null, 
			true
		);
	},
	
	validarParametrosConsulta:function(){
		
		if( $("#vend-suplementar-selectTipoVenda", VENDA_PRODUTO.workspace).val().length > 0
				|| $("#vend-suplementar-numCota", VENDA_PRODUTO.workspace).val().length > 0 
				|| ( $("#vend-suplementar-periodoDe", VENDA_PRODUTO.workspace).val().length > 0
						&& $("#vend-suplementar-periodoAte", VENDA_PRODUTO.workspace).val().length > 0)){
			
			return true;
		}
		return false; 
	},
	
	imprimeSlipVendaEncalhe:function(){
		
		$('#download-iframe-vendaEncalhe', VENDA_PRODUTO.workspace).attr('src', contextPath + '/devolucao/vendaEncalhe/imprimeSlipVendaEncalhe');
	},
	
	imprimirSlipVenda:function(){
		
		$('#download-iframe-vendaEncalhe', VENDA_PRODUTO.workspace).attr('src', contextPath + '/devolucao/vendaEncalhe/imprimirSlipVenda');
	},
	
	limparDadosModalVenda:function(){
		
		$("#vend-suplementar-numCotaVenda", VENDA_PRODUTO.workspace).val("");
		$("#vend-suplementar-dataVencimento", VENDA_PRODUTO.workspace).val("");
		$("#span_nome_cota_venda", VENDA_PRODUTO.workspace).text("");
		$("#span_nome_box_venda", VENDA_PRODUTO.workspace).text("");
		$("#span_total_disponivel_venda", VENDA_PRODUTO.workspace).text("0");
		$("#span_total_solicitado_venda", VENDA_PRODUTO.workspace).text("0");
		$("#span_total_geral_venda", VENDA_PRODUTO.workspace).text("0,00");
		$("#span_total_suplementar_venda", VENDA_PRODUTO.workspace).text("0,00");
		$("#span_total_encalhe_venda", VENDA_PRODUTO.workspace).text("0,00");
		$("#span_qntSolicitada_suplementar_venda", VENDA_PRODUTO.workspace).text("0");
		$("#span_qntSolicitada_encalhe_venda", VENDA_PRODUTO.workspace).text("0");
	},
	
	recalcularPrecoVendaItens:function(){
		
		if( $("#vend-suplementar-numCotaVenda", VENDA_PRODUTO.workspace).val() == ""){
			return;
		}
		
		var linhasDaGrid = $("#vendaEncalhesGridCota tr", VENDA_PRODUTO.workspace);
		var listaVendas = [];
		
		$.each(linhasDaGrid, function(index, value) {
			var linha = $(value);
					
			var codigoBarras =
				$(linha.find("td")[0]).find("div").find('input[name="codBarras"]', VENDA_PRODUTO.workspace).val();
			
			var codigoProduto = 
				$(linha.find("td")[1]).find("div").find('input[name="codProduto"]', VENDA_PRODUTO.workspace).val();
			
			var nomeProduto = 
				$(linha.find("td")[2]).find("div").find('input[name="nmProduto"]', VENDA_PRODUTO.workspace).val();
			
			var numeroEdicao = 
				$(linha.find("td")[3]).find("div").find('input[name="numEdicao"]', VENDA_PRODUTO.workspace).val();
			
			var qntDisponivel =
				$(linha.find("td")[5]).find("div").find('input[name="qntDisponivel"]', VENDA_PRODUTO.workspace).val();
			
			var qtdeSolicitada = 
				$(linha.find("td")[6]).find("div").find('input[name="qntSolicitada"]', VENDA_PRODUTO.workspace).val();
			
			var descTipoVenda = 
				$(linha.find("td")[8]).find("div").find('input[name="tipoVenda"]', VENDA_PRODUTO.workspace).val();
			
			var tipoVenda =
				$(linha.find("td")[8]).find("div").find('input[name="hiddenTipoVenda"]', VENDA_PRODUTO.workspace).val();
			
			var formaVendaDesc =
				$(linha.find("td")[9]).find("div").find('input[name="formaVenda"]', VENDA_PRODUTO.workspace).val();
			
			var formaVenda =
				$(linha.find("td")[9]).find("div").find('input[name="hiddenComercializacao"]', VENDA_PRODUTO.workspace).val();
			
				
			if (VENDA_PRODUTO.isAtributosVendaVazios(codigoProduto, numeroEdicao, qtdeSolicitada)) {
				return true;
			}
			
			listaVendas.push({name: "listaVendas["+index+"].id", value: index});
			listaVendas.push({name: "listaVendas["+index+"].codigoBarras", value: codigoBarras});
			listaVendas.push({name: "listaVendas["+index+"].codigoProduto", value: codigoProduto});
			listaVendas.push({name: "listaVendas["+index+"].nomeProduto", value: nomeProduto});
			listaVendas.push({name: "listaVendas["+index+"].numeroEdicao", value: numeroEdicao});
			listaVendas.push({name: "listaVendas["+index+"].qntDisponivel", value: qntDisponivel});
			listaVendas.push({name: "listaVendas["+index+"].qntSolicitada", value: qtdeSolicitada});
			listaVendas.push({name: "listaVendas["+index+"].formaVenda", value: formaVendaDesc});
			listaVendas.push({name: "listaVendas["+index+"].descTipoVenda", value: descTipoVenda});
			listaVendas.push({name: "listaVendas["+index+"].tipoVenda", value: tipoVenda});
			listaVendas.push({name: "listaVendas["+index+"].formaComercializacao", value: formaVenda});
		});
	
		if(listaVendas.length > 0){
			
			listaVendas.push({name:"numeroCota",value:$("#vend-suplementar-numCotaVenda", VENDA_PRODUTO.workspace).val()});
			
			$("#vendaEncalhesGridCota", VENDA_PRODUTO.workspace).flexOptions({
				url: contextPath + "/devolucao/vendaEncalhe/recalcularValorDescontoItensVenda",
				params:listaVendas,
				newp: 1
			});
			
			$("#vendaEncalhesGridCota", VENDA_PRODUTO.workspace).flexReload();
		}	

	}
};

$(function() {

	$("input[name='vend-suplementar-numCota']", VENDA_PRODUTO.workspace).numeric();
	
	$("input[name='vend-suplementar-numCotaVenda']", VENDA_PRODUTO.workspace).numeric();
	
	$('input[id^="vend-suplementar-periodo"]', VENDA_PRODUTO.workspace).mask("99/99/9999");
	
	$('input[id^="vend-suplementar-dataVencimento"]', VENDA_PRODUTO.workspace).mask("99/99/9999");

	$('input[id^="vend-suplementar-periodo"]', VENDA_PRODUTO.workspace).datepicker({
		showOn: "button",
		buttonImage: contextPath+"/images/calendar.gif",
		buttonImageOnly: true,
		dateFormat: "dd/mm/yy"
	});
	
	$('input[id="vend-suplementar-dataVencimento"]', VENDA_PRODUTO.workspace).datepicker({
		showOn: "button",
		buttonImage: contextPath+"/images/calendar.gif",
		buttonImageOnly: true,
		dateFormat: "dd/mm/yy"
	});
//	
//	$("#btn_confirmar_venda", VENDA_PRODUTO.workspace).keypress(function(e) {
//		if (e.keyCode == 13) {
//			VENDA_PRODUTO.confirmaVenda();
//		}
//	});
//	
//	$('#btn_confirmar_venda').keyup(function(event) {
//		  if (event.keyCode == 13 ) {
//		     event.preventDefault();
//		   }
//		});
	
	
		
	$(".vendaEncalheGridCota", VENDA_PRODUTO.workspace).flexigrid({
		preProcess:VENDA_PRODUTO.executarPreProcessamento,
		onSuccess: function() {bloquearItensEdicao(VENDA_PRODUTO.workspace);},
		dataType : 'json',
		colModel : [ {
			display : 'Data',
			name : 'dataVenda',
			width : 60,
			sortable : true,
			align : 'center'
		}, {
			display : 'Cota',
			name : 'numeroCota',
			width : 40,
			sortable : true,
			align : 'left'
		}, {
			display : 'Nome',
			name : 'nomeCota',
			width : 120,
			sortable : true,
			align : 'left'
		}, {
			display : 'Código',
			name : 'codigoProduto',
			width : 60,
			sortable : true,
			align : 'left'
		}, {
			display : 'Produto',
			name : 'nomeProduto',
			width : 105,
			sortable : true,
			align : 'left'
		}, {
			display : 'Edição',
			name : 'numeroEdicao',
			width : 50,
			sortable : true,
			align : 'center'
		}, {
			display : 'Preço Desc. R$',
			name : 'precoDesconto',
			width : 75,
			sortable : true,
			align : 'center'
		}, {
			display : 'Qtde',
			name : 'qntProduto',
			width : 30,
			sortable : true,
			align : 'center'
		}, {
			display : 'Total R$',
			name : 'valoTotalProduto',
			width : 55,
			sortable : true,
			align : 'center'
		}, {
			display : 'Tipo Venda',
			name : 'tipoVendaEncalhe',
			width : 70,
			sortable : true,
			align : 'center'
		}, {
			display : 'Usuário',
			name : 'nomeUsuario',
			width : 70,
			sortable : true,
			align : 'left'
		}, {
			display : 'Forma Venda',
			name : 'comercializacao',
			width : 70,
			sortable : true,
			align : 'left'
		}, {
			display : 'Ação',
			name : 'acao',
			width : 60,
			sortable : true,
			align : 'center'
		}],
		sortname : "dataVenda",
		sortorder : "desc",
		usepager : true,
		useRp : true,
		rp : 15,
		showTableToggleBtn : true,
		width : 970,
		height : 220
	});
}, BaseController);

//@ sourceURL=vendaEncahe.js

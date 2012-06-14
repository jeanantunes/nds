<head>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/cota.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/produto.js"></script>

<script type="text/javascript">
var VENDA_PRODUTO = {
	
	vendaNova:true,
	tipoVenda:"",
		
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
				
				linkEdicao = '<a href="javascript:;" onclick="VENDA_PRODUTO.editar('+ row.cell.idVenda +');" style="cursor:pointer">' +
				 '<img src="'+ contextPath +'/images/ico_editar.gif" hspace="5" border="0px" title="Editar Venda" />' +
				 '</a>';			
			 
				linkExclusao ='<a href="javascript:;" onclick="VENDA_PRODUTO.exibirDialogExclusao('+ row.cell.idVenda +' );" style="cursor:pointer">' +
                '<img src="'+ contextPath +'/images/ico_excluir.gif" hspace="5" border="0px" title="Excluir Venda" />' +
                 '</a>';		 					 
				
			}
			else{
				
				linkEdicao = '<a href="javascript:;" style="cursor:default; opacity:0.4; filter:alpha(opacity=40)">' +
				 '<img src="'+ contextPath +'/images/ico_editar.gif" hspace="5" border="0px" title="Editar Venda" />' +
				 '</a>';			
			 
				linkExclusao ='<a href="javascript:;" style="cursor:default; opacity:0.4; filter:alpha(opacity=40)">' +
               '<img src="'+ contextPath +'/images/ico_excluir.gif" hspace="5" border="0px" title="Excluir Venda" />' +
                '</a>';		 					 
			
			}
		
           row.cell.acao = linkEdicao + linkExclusao; 
		});
		
		$("#totalGeral").html(resultado.totalGeral);
		
		VENDA_PRODUTO.processarVisualizcaoImpressao(true);
		
		return resultado.tableModel;
	},
	
	editar:function(idVenda){
		
		VENDA_PRODUTO.limparDadosModalVenda();
		
		VENDA_PRODUTO.vendaNova = false;
		
		$("#vendaEncalhesGrid").flexOptions({
			params:[{name:"idVendaEncalhe", value:idVenda}],
			url: "<c:url value='/devolucao/vendaEncalhe/prepararDadosEdicaoVenda'/>",
			newp: 1
		});
		
		$("#vendaEncalhesGrid").flexReload();
		
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
		
		$("#dialog-excluirVenda" ).dialog({
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
			}
		});
	},
		
	params:function(){
		
		var param = [{name:"numeroCota",value:$("#numCota").val()},
		             {name:"tipoVenda",value:$("#selectTipoVenda").val()},
		             {name:"periodoInicial",value:$("#periodoDe").val()},
		             {name:"periodoFinal",value:$("#periodoAte").val()}
					];
		
		return param;
	},	
		
	pesquisarCotaSuccessCallBack:function(){
		$("#span_nome_cota").html($("#descricaoCota").val());
	},
	
	pesquisarCotaErrorCallBack:function(){
		$("#span_nome_cota").html("");
	},
	
	pesquisarCotaVendaSuccessCallBack:function(){
		
		$("#span_nome_cota_venda").html($("#descricaoCotaVenda").val());
		
		$.postJSON(contextPath + "/devolucao/vendaEncalhe/obterBoxCota", 
					"numeroCota=" + $("#numCotaVenda").val(),
					function(result) {
			 			$("#span_nome_box_venda").html(result.box);			
					},null,true);
	},
	
	pesquisarCotaVendaErrorCallBack:function(){
		$("#span_nome_cota_venda").html("");
		$("#span_nome_box_venda").html("");	
	},
	
	pesquisarVendas:function(){
		
		if($("#selectTipoVenda").val() == -1){
			exibirMensagem('WARNING',['O preenchimento do campo [Tipo de Venda] é obrigatório!']);
			return false;
		}
		
		$("#vendaEncalheGrid").flexOptions({
			url: "<c:url value='/devolucao/vendaEncalhe/pesquisarVendas' />",
			params: VENDA_PRODUTO.params(),newp: 1
		});
		
		$("#vendaEncalheGrid").flexReload();
	},
	
	processarVisualizcaoImpressao:function(isVisualiza){
		
		if(isVisualiza == true){
			 $("#infosRodape").show();
			 $("#gridVenda").show();
			 $("#labelTotalGeral").show();
			 $("#totalGeral").show();
		}
		else{
			$("#infosRodape").hide();
			$("#gridVenda").hide();	
			$("#labelTotalGeral").hide();
			 $("#totalGeral").hide();
		}	
	},
	
	exportar:function(fileType){
		
		window.location = 
			contextPath + "/devolucao/vendaEncalhe/exportar?fileType=" + fileType;
	},
	
	novaVenda:function(tipoVenda){
		
		VENDA_PRODUTO.limparDadosModalVenda();
		
		VENDA_PRODUTO.vendaNova = true;
		
		VENDA_PRODUTO.tipoVenda = tipoVenda;
		
		 $.postJSON(contextPath + "/devolucao/vendaEncalhe/obterDatavenda", 
					null,
					function(resultado) {
						 $("#span_data_venda").html(resultado.data);
						 $("#dataVencimento").val(resultado.dataVencimentoDebito);
					},null,true);
		

		$("#vendaEncalhesGrid").flexOptions({
			url: "<c:url value='/devolucao/vendaEncalhe/prepararDadosVenda' />",
			newp: 1
		});
		
		$("#vendaEncalhesGrid").flexReload();
	},
	
	showModalVendas:function(){
		
		$("#dialog-venda-encalhe").dialog({
			resizable: false,
			height:460,
			width:1030,
			modal: true,
			buttons: {
				"Confirmar": function() {
					VENDA_PRODUTO.confirmaVenda();
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	},
	
	executarPreProcessamentoGridVenda:function(result){
		
		VENDA_PRODUTO.showModalVendas();
		
		if(VENDA_PRODUTO.vendaNova == true){
			return VENDA_PRODUTO.processarDadosNovaVenda(result);
		}
		
		return VENDA_PRODUTO.processarDadosEdicaoVenda(result) ;
	},
	
	processarDadosEdicaoVenda:function(result){
		
		// Monta as colunas com os inputs do grid
		$.each(result.rows, function(index, row) {
			
			var hiddenId = '<input type="hidden" name="idVendaEncalhe" value="' + row.cell.idVendaEncalhe + '" />';
			
			var inputCodBarras ='<input type="text" id="codBarras0" name="codBarras" style="width:160px;" disabled="disabled" value="'+row.cell.codigoBarras+'"/>';
			
			var inputCodProduto='<input type="text" id="codProduto0" value="'+row.cell.codigoProduto+'" disabled="disabled" name="codProduto" style="width:60px; float:left; margin-right:10px;"/>';
			
			var inputNomeProduto='<input type="text" id="nmProduto0" value="'+row.cell.nomeProduto+'" name="nmProduto" style="width:140px;" disabled="disabled" maxlenght="255"/>';
			
			var inputNumEdicao='<input type="text" id="numEdicao0" value="'+row.cell.numeroEdicao+'"  name="numEdicao" style="width:40px; text-align: center;" maxlenght="20" disabled="disabled" />';
			
			var inputPrecoCapa='<input type="text" id="precCapa0" value="'+row.cell.precoCapa+'" name="precCapa" style="width:65px; text-align: center;" disabled="disabled" />';
			
			var inputQntDisponivel='<input type="text" id="qntDisponivel0" name="qntDisponivel" style="width:65px; text-align: center;" maxlenght="20" value="'+row.cell.qntDisponivel+'" disabled="disabled" />';
			
			var inputQntSolicitada='<input type="text" id="qntSolicitada0" name="qntSolicitada" style="width:65px; text-align: center;" maxlenght="6" value="'+row.cell.qntSolicitada+'" onchange="VENDA_PRODUTO.totalizarValorProdutoSelecionado(0);" />';
			
			var inputTotal ='<input type="text" id="total0" name="total" value="'+row.cell.total+'" style="width:65px; text-align: center;" disabled="disabled" />';
			
			var inputFormaVenda='<input type="text" id="formaVenda0" name="formaVenda" value="'+row.cell.formaVenda+'" disabled="disabled" />';
			
			$("#qntSolicitada0").keypress(function(e) {
				if (e.keyCode == 13) {
					VENDA_PRODUTO.totalizarValorProdutoSelecionado(0);
				}
			});
			
			row.cell.codigoBarras = inputCodBarras + hiddenId;
			row.cell.codigoProduto = inputCodProduto;
			row.cell.nomeProduto = inputNomeProduto;
			row.cell.numeroEdicao = inputNumEdicao;
			row.cell.precoCapa = inputPrecoCapa;
			row.cell.qntDisponivel = inputQntDisponivel;
			row.cell.qntSolicitada = inputQntSolicitada;
			row.cell.total =inputTotal;
			row.cell.formaVenda = inputFormaVenda;
			
			$("#span_nome_box_venda").html(row.cell.codBox);
			$("#dataVencimento").val(row.cell.dataVencimentoDebito);
			$("#span_data_venda").html(row.cell.dataVenda);
			$("#span_nome_cota_venda").html(row.cell.nomeCota);
			$("#numCotaVenda").val(row.cell.numeroCota);
			$("#numCotaVenda").attr("disabled","disabled");
			$("#qntSolicitada0").focus();
		});
		
		return result;
	},
	
	processarDadosNovaVenda:function(result){
		
		// Monta as colunas com os inputs do grid
		$.each(result.rows, function(index, row) {
			
			var funcaoError = "function(){ VENDA_PRODUTO.pesquisarDadosProdutoError("+ index + ')}';
			
			var parametroPesquisaProduto = '\'#codProduto' + index + '\', \'#nmProduto' + index + '\', \'#numEdicao' + index + '\', true, '+funcaoError+','+funcaoError+' ';
			
			var parametroAutoCompleteProduto = '\'#nmProduto' + index + '\', true';
			
			var funcaoSucces = "function(idCodigo, idEdicao){ VENDA_PRODUTO.pesquisarDadosProduto(idCodigo, idEdicao,"+ index + ")}";
			
			var parametroValidacaoEdicao = '\'#codProduto' + index + '\', \'#numEdicao' + index + '\', true, '+funcaoSucces+','+funcaoError+'';
			
			var parametroPesCodBarra ='\'#codBarras'+ index+ '\',' + index;

			var hiddenId = '<input type="hidden" name="id" value="' + index + '" />';
			
			var inputCodBarras ='<input type="text" id="codBarras'+index+'" name="codBarras" style="width:160px;" value="" onchange="VENDA_PRODUTO.pesquisarProdutoCodBarra('+parametroPesCodBarra+')"/>';
			
			var inputCodProduto='<input type="text" id="codProduto' + index + '" name="codProduto" style="width:60px; float:left; margin-right:10px;" maxlenght="255" onchange="produto.pesquisarPorCodigoProduto(' + parametroPesquisaProduto + ');" />';
			
			var inputNomeProduto='<input type="text" id="nmProduto' + index + '" name="nmProduto" style="width:140px;" maxlenght="255" onkeyup="produto.autoCompletarPorNomeProduto(' + parametroAutoCompleteProduto + ');" onblur="produto.pesquisarPorNomeProduto(' + parametroPesquisaProduto + ')" />';
			
			var inputNumEdicao='<input type="text" id="numEdicao' + index + '"  name="numEdicao" style="width:40px;text-align: center;" maxlenght="20" onchange="produto.validarNumEdicao(' + parametroValidacaoEdicao + ');" disabled="disabled" />';
			
			var inputPrecoCapa='<input type="text" id="precCapa'+index+'" name="precCapa" value="" style="width:65px;text-align: center;" disabled="disabled" />';
			
			var inputQntDisponivel='<input type="text" id="qntDisponivel'+index+'" name="qntDisponivel" style="width:65px;text-align: center;" maxlenght="20" value="" disabled="disabled" />';
			
			var inputQntSolicitada='<input type="text" id="qntSolicitada'+index+'" name="qntSolicitada" style="width:65px;text-align: center;" maxlenght="6" value="" onblur="VENDA_PRODUTO.totalizarValorProdutoSelecionado('+index+');" />';
			
			var inputTotal ='<input type="text" id="total'+index+'" name="total" value="" style="width:65px;text-align: center;" disabled="disabled" />';
			
			var inputFormaVenda='<input type="text" id="formaVenda'+index+'" name="formaVenda" value="" disabled="disabled" />';
			
			$("#codBarras"+index).keypress(function(e) {
				if (e.keyCode == 13) {
					VENDA_PRODUTO.pesquisarProdutoCodBarra("#codBarras"+index, index);
				}
			});
			
			$("#qntSolicitada"+index).keypress(function(e) {
				if (e.keyCode == 13) {
					VENDA_PRODUTO.totalizarValorProdutoSelecionado(index);
				}
			});
			
			row.cell.codigoBarras = inputCodBarras + hiddenId;
			row.cell.codigoProduto = inputCodProduto;
			row.cell.nomeProduto = inputNomeProduto;
			row.cell.numeroEdicao = inputNumEdicao;
			row.cell.precoCapa = inputPrecoCapa;
			row.cell.qntDisponivel = inputQntDisponivel;
			row.cell.qntSolicitada = inputQntSolicitada;
			row.cell.total =inputTotal;
			row.cell.formaVenda = inputFormaVenda;
			
			$("#numCotaVenda").attr("disabled",null);
		});
		
		return result;
	},
	
	totalizarValorProdutoSelecionado:function(indiceLinha){
		
		if(VENDA_PRODUTO.validaritemRepetido(indiceLinha)== true){
			return;
		}
		
		if($("#qntSolicitada"+indiceLinha).val().trim().length > 0){
			
			var data = [
			            {name:"precoProduto", value:$("#precCapa"+indiceLinha).val()},
			            {name:"qntSolicitada", value:$("#qntSolicitada"+indiceLinha).val()},
			            {name:"qntDisponivel", value:$("#qntDisponivel"+indiceLinha).val()}];
			
			 $.postJSON(
						contextPath + "/devolucao/vendaEncalhe/totalizarValorProduto", 
						data,
						function(resultado) {					
							$("#total" + indiceLinha).val(resultado.total);
							
							VENDA_PRODUTO.totalizarQntTotalGeral();
							VENDA_PRODUTO.totalizarQntSolicitadaGeral();
							
							$("#codBarras" + indiceLinha +1).focus();
						},
						function(resultado){
							$("#total" + indiceLinha).val("");
							$("#qntSolicitada" + indiceLinha).focus();
							
							VENDA_PRODUTO.totalizarQntTotalGeral();
							VENDA_PRODUTO.totalizarQntSolicitadaGeral();
						}, 
						true
					);
		}else{
			$("#total" + indiceLinha).val("");
			VENDA_PRODUTO.totalizarQntTotalGeral();
			VENDA_PRODUTO.totalizarQntSolicitadaGeral();
		}
	},
	
	totalizarQntDisponivelGeral:function(){
	
		var total =  $("input[id^='qntDisponivel']").sum();
		$("#span_total_disponivel_venda").html(total);
	},
	
	totalizarQntSolicitadaGeral:function(){
		
		var total = $("input[id^='qntSolicitada']").sum();
		$("#span_total_solicitado_venda").text(total); 
	},
	
	totalizarQntTotalGeral:function(){
		
		var total = $("input[id^='total']").sum();
		$("#span_total_geral_venda").text(total);
		$("#span_total_geral_venda").formatCurrency({region: 'pt-BR', decimalSymbol: ',', symbol: ''});
	},
	
	obterDadosProduto:function(codigoProduto, edicaoProduto,index) {
	
		var data = "codigoProduto=" + codigoProduto
		 		 + "&numeroEdicao=" + edicaoProduto;

		 $.postJSON(
			contextPath + "/devolucao/vendaEncalhe/obterDadosDoProduto", 
			data,
			function(resultado) {
		
				VENDA_PRODUTO.atribuirDadosProduto(resultado, index);
				$("#numEdicao"+index).attr("disabled",null);
				
				VENDA_PRODUTO.totalizarQntDisponivelGeral();
			},
			function(resultado){
				VENDA_PRODUTO.totalizarQntDisponivelGeral();
				$("#total"+index).val("");
			}, 
			true
		);
 	},
 	
 	pesquisarDadosProdutoError:function(indiceLinha){
 		
 		$("#codBarras"+indiceLinha).val("");
		$("#numEdicao"+indiceLinha).val("");
		$("#precCapa"+indiceLinha).val("");
		$("#qntDisponivel"+indiceLinha).val("");
		$("#qntSolicitada"+indiceLinha).val("");
		$("#total" + indiceLinha).val("");
		$("#formaVenda"+indiceLinha).val("");
		
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
 		
 		if($(idCodBarras).val().length == 0){
 			return;
 		}
 		
 		$.postJSON(contextPath + "/devolucao/vendaEncalhe/pesquisarProdutoCodBarra",
				"codBarra="+$(idCodBarras).val(), 
				function(result){
					
 					VENDA_PRODUTO.obterDadosProduto(result.codigoProduto,result.nuemroEdicao,index);						
					
 					$("#qntSolicitada"+index).focus();
 					
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
 		
 		$("#codBarras"+index).val(resultado.produto.codigoBarras);
 		$("#codProduto"+index).val(resultado.produto.codigoProduto);
		$("#nmProduto"+index).val(resultado.produto.nomeProduto);
		$("#numEdicao"+index).val(resultado.produto.numeroEdicao);
		$("#precCapa"+index).val(resultado.produto.precoCapa);
		$("#qntDisponivel"+index).val(resultado.produto.qntDisponivelProduto);
		$("#qntSolicitada"+index).val(resultado.produto.qntProduto);
		$("#total"+index).val(resultado.produto.valoTotalProduto);
		$("#formaVenda"+index).val(resultado.produto.formaVenda);
 	},
 	
 	limparDadoVendaProduto:function(index){
 		
 		$("#codBarras"+index).val("");
 		$("#codProduto"+index).val("");
		$("#nmProduto"+index).val("");
		$("#numEdicao"+index).val("");
		$("#precCapa"+index).val("");
		$("#qntDisponivel"+index).val("");
		$("#qntSolicitada"+index).val("");
		$("#total"+index).val("");
		$("#formaVenda"+index).val("");
		
		VENDA_PRODUTO.totalizarQntDisponivelGeral();
		VENDA_PRODUTO.totalizarQntSolicitadaGeral();
		VENDA_PRODUTO.totalizarQntTotalGeral();
 	},
 	
	formatarCampos:function(){
		
		$("input[name='codProduto']").numeric();
		$("input[name='nmProduto']").autocomplete({source: ""});
		$("input[name='qntSolicitada']").numeric();
		
		VENDA_PRODUTO.totalizarQntDisponivelGeral();
		VENDA_PRODUTO.totalizarQntTotalGeral();
		VENDA_PRODUTO.totalizarQntSolicitadaGeral();
	},
	
	limparGridVenda: function (){
		$('#vendaEncalhesGrid tr').remove();
	},
	
	validaritemRepetido:function(indiceLinha){
		
		var linhasDaGrid = $("#vendaEncalhesGrid tr");
	
		$.each(linhasDaGrid, function(index, value) {
			
			if(indiceLinha!= index){
				
				if( $("#codProduto"+index).val()!= "" 
						&& $("#codProduto"+index).val() == $("#codProduto"+indiceLinha).val()
						&& $("#numEdicao"+index).val()!= "" 
						&& $("#numEdicao"+index).val() == $("#numEdicao"+indiceLinha).val()){
					
					exibirMensagemDialog(
							"WARNING", 
							['O produto '+"$('#codProduto"+indiceLinha+").val()"+ 'já foi selecionado!'],""
					);
					
					VENDA_PRODUTO.limparDadoVendaProduto(indiceLinha);
					return true;
				}
			}
		});
		return false;
	},
	
	getListaProduto:function(){
		
		var linhasDaGrid = $("#vendaEncalhesGrid tr");

		var listaVendas= "";
		
		$.each(linhasDaGrid, function(index, value) {

			var linha = $(value);
			
			var colunaCodigoBarras = linha.find("td")[0];
			var colunaCodigoProduto = linha.find("td")[1];
			var colunaNumeroEdicao = linha.find("td")[3];
			var colunaQtdeSolicitada = linha.find("td")[6];

			var id = 
				$(colunaCodigoBarras).find("div").find('input[name="idVendaEncalhe"]').val();
			
			var codigoProduto = 
				$(colunaCodigoProduto).find("div").find('input[name="codProduto"]').val();
			
			var numeroEdicao = 
				$(colunaNumeroEdicao).find("div").find('input[name="numEdicao"]').val();

			var qtdeSolicitada = 
				$(colunaQtdeSolicitada).find("div").find('input[name="qntSolicitada"]').val();
			
			if (VENDA_PRODUTO.isAtributosVendaVazios(codigoProduto, numeroEdicao, qtdeSolicitada)) {

				return true;
			}
						
			var venda = 'listaVendas[' + index + '].idVenda=' + id + '&';
			
			venda += 'listaVendas[' + index + '].codigoProduto=' + codigoProduto + '&';

			venda += 'listaVendas[' + index + '].numeroEdicao=' + numeroEdicao + '&';

			venda += 'listaVendas[' + index + '].qntProduto=' + qtdeSolicitada  + '&';
			
			venda += 'listaVendas[' + index + '].tipoVendaEncalhe=' + VENDA_PRODUTO.tipoVenda  + '&';
			
			listaVendas = (listaVendas + venda);

		});
		
		return listaVendas;
	},
	
	isAtributosVendaVazios:function(codigoProduto, numeroEdicao, qtdeSolicitada) {

		if (!$.trim(codigoProduto) 
				&& !$.trim(numeroEdicao) 
				&& !$.trim(qtdeSolicitada)) {

			return true;
		}
	},
	
	confirmaVenda:function(){
		
		var metodo = (VENDA_PRODUTO.vendaNova == true)?"confirmaNovaVenda":"confirmaEdicaoVenda";
		
		var data = VENDA_PRODUTO.getListaProduto()  
				   + "&numeroCota=" + $("#numCotaVenda").val()
				   + "&dataDebito=" + $("#dataVencimento").val();  
		 
		 $.postJSON(
			contextPath + "/devolucao/vendaEncalhe/"+metodo, 
			data,
			function(result) {
				
				exibirMensagem(
						result.tipoMensagem, 
						result.listaMensagens
					);
				
				$("#dialog-venda-encalhe").dialog( "close" );
				
				VENDA_PRODUTO.imprimeSlipVendaEncalhe($("#numCotaVenda").val());
			},
			null, 
			true
		);
	},
	
	imprimeSlipVendaEncalhe:function(numeroCota,vendas){
		
		document.location.assign("${pageContext.request.contextPath}/devolucao/vendaEncalhe/imprimeSlipVendaEncalhe?numeroCota="+numeroCota);
	},
	
	limparDadosModalVenda:function(){
		
		$("#numCotaVenda").val("");
		$("#dataVencimento").val("");
		$("#span_nome_cota_venda").text("");
		$("#span_nome_box_venda").text("");
		$("#span_total_disponivel_venda").text("0");
		$("#span_total_solicitado_venda").text("0");
		$("#span_total_geral_venda").text("0,00");
	}
};

$(function() {

	$("input[name='numCota']").numeric();
	
	$("input[name='numCotaVenda']").numeric();
	
	$('input[id^="periodo"]').mask("99/99/9999");
	
	$('input[id^="dataVencimento"]').mask("99/99/9999");

	$('input[id^="periodo"]').datepicker({
		showOn: "button",
		buttonImage: contextPath+"/images/calendar.gif",
		buttonImageOnly: true,
		dateFormat: "dd/mm/yy"
	});
	
	$('input[id^="dataVencimento"]').datepicker({
		showOn: "button",
		buttonImage: contextPath+"/images/calendar.gif",
		buttonImageOnly: true,
		dateFormat: "dd/mm/yy"
	});
		
	$(".vendaEncalheGrid").flexigrid({
		preProcess:VENDA_PRODUTO.executarPreProcessamento,
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
			width : 50,
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
			display : 'Preço Capa R$',
			name : 'precoCapa',
			width : 70,
			sortable : true,
			align : 'center'
		}, {
			display : 'Preço Desc. R$',
			name : 'precoDesconto',
			width : 70,
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
			width : 50,
			sortable : true,
			align : 'center'
		}, {
			display : 'Tipo Venda',
			name : 'tipoVendaEncalhe',
			width : 70,
			sortable : true,
			align : 'center'
		}, {
			display : 'Ação',
			name : 'acao',
			width : 60,
			sortable : true,
			align : 'center'
		}],
		sortname : "nomeCota",
		sortorder : "asc",
		usepager : true,
		useRp : true,
		rp : 15,
		showTableToggleBtn : true,
		width : 960,
		height : 220
	});
});

</script>

</head>
<body>
	<div id="dialog-excluirVenda" title="Atenção" style="display:none">
		<p>Confirma a Exclusão desta Venda de Encalhe?</p>
	</div>
	

<fieldset class="classFieldset">
   	   
   	   <legend> Pesquisar Encalhe</legend>
        <table width="950" cellspacing="1" cellpadding="2" border="0" class="filtro">
  			<tbody>
  				<tr>
				    <td width="53">Cota:</td>
				    
				    <td width="125">
				    	
				    	<input name="numCota" 
				               id="numCota" 
				               type="text"
				               maxlength="11"
				               style="width:70px; 
				               float:left; margin-right:5px;"
				               onchange="cota.pesquisarPorNumeroCota('#numCota', '#descricaoCota',false,
				              	  									 VENDA_PRODUTO.pesquisarCotaSuccessCallBack, 
				              	  									 VENDA_PRODUTO.pesquisarCotaErrorCallBack);"/>
				    </td>
				    
				    <td width="46">Nome:</td>
				    
				    <td width="320">
				    	<input type="hidden" name="descricaoCota" id="descricaoCota"/>
				    	<span class="dadosFiltro" id="span_nome_cota" style="display: inline;"></span>     
				    </td>
				    
				    <td width="86">Tipo de Venda:</td>
				    
				    <td width="180">
				    	<select id="selectTipoVenda" name="tipoVendaSelecionado">
				    		<option selected="selected" value="-1"> </option>
				      		<option value="">Todas</option>
				      		<option value="ENCALHE">Venda de Encalhe</option>
				      		<option value="SUPLEMENTAR">Venda de Suplementar</option>
				    	</select>
				    </td>
				    
				    <td width="104">&nbsp;</td>
				    
			  	</tr>
			  	<tr>
				    <td>Período:</td>
				    <td>
				    	<input type="text" style="width:80px; " id="periodoDe" name="periodoDe">
				    </td>
				    <td>Até:</td>
				    <td>
				    	<input type="text" style="width:80px; " id="periodoAte" name="periodoAte">
				    </td>
				    <td>&nbsp;</td>
				    <td>&nbsp;</td>
				    <td>
				    	<span class="bt_pesquisar"><a onclick="VENDA_PRODUTO.pesquisarVendas();" href="javascript:;">Pesquisar</a></span>
				    </td>
			  </tr>
        	</tbody>
        </table>

      </fieldset>
      
      <div class="linha_separa_fields">&nbsp;</div>
      
      <fieldset class="classFieldset">
       	  
       		<legend>Venda de Encalhes</legend>
        	
	    	<div class="gridVenda" id="gridVenda" style="display: none;">
				<table id="vendaEncalheGrid" class="vendaEncalheGrid"></table>
			</div>
	        
	       	<table width="100%" cellspacing="2" cellpadding="2" border="0">
	             <tbody><tr>
	               <td width="56%">
	               		<span title="Novo" class="bt_novos">
	               			<a onclick="popup_suplementar();" href="javascript:;">
	               				<img hspace="5" border="0" src="${pageContext.request.contextPath}/images/ico_salvar.gif">
	               					Venda Suplementar
	               			</a>
	               		</span>
	               		
	               		<span title="Novo" class="bt_novos">
	               			<a onclick="VENDA_PRODUTO.novaVenda('ENCALHE');" href="javascript:;">
	               				<img hspace="5" border="0" src="${pageContext.request.contextPath}/images/ico_salvar.gif">
	               				Venda Encalhe
	               			</a>
	               		</span>
	               
	               	<div style="display: none" id="infosRodape" class="infosRodape" >
	               
		               	<span title="Gerar Arquivo" class="bt_novos">
		               		<a href="javascript:;" onclick="VENDA_PRODUTO.exportar('XLS')">
		               			<img hspace="5" border="0" src="${pageContext.request.contextPath}/images/ico_excel.png">
		               			Arquivo
		               		</a>
		               	</span>
		
						<span title="Imprimir" class="bt_novos">
							<a href="javascript:;" onclick="VENDA_PRODUTO.exportar('PDF')">
								<img hspace="5" border="0" src="${pageContext.request.contextPath}/images/ico_impressora.gif">
								Imprimir
							</a>
						</span>
		               
		               <span style="" id="btSuplementar" title="Imprimir" class="bt_novos">
		               		<a target="_blank" href="imprimir_comprovante_suplementar.htm">
		               			<img hspace="5" border="0" src="${pageContext.request.contextPath}/images/ico_impressora.gif">
		               			Imprimir Slip de Suplementar
		               		</a>
		               </span>
	               
	               </div>
	             </td>
	               <td width="6%">&nbsp;</td>
	               <td width="14%">
               		<div style="display: none;" class="infosRodape" id="labelTotalGeral"><strong>Total Geral R$:</strong></div>
	               </td>
	               <td width="13%" align="left">
	               		<div style="display: none;" class="infosRodape" id="totalGeral" ></div>
	               </td>
	               <td width="11%"></td>
	             </tr>
	           </tbody>
	          </table>
	          
	          <jsp:include page="gridVenda.jsp"/>
    
      </fieldset>
</body>
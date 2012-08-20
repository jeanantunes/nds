<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>meuConteudo</title>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaProduto.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>

<script type="text/javascript">

var pesquisaProdutoExtratoEdicao = new PesquisaProduto();

var jsExtratoEdicao = {

	pesquisarExtratoEdicao : function() {
		
		var numeroEdicao = jQuery("#edicao").val();
		var codigoProduto = jQuery("#codigo").val();
		var nomeProduto = jQuery("#produto").val();
		var precoCapa = jQuery("#precoCapa").val();
		var nomeFornecedor = jQuery("#nomeFornecedor").val();
		
		$(".extratoEdicaoGrid").flexOptions({
			url: '<c:url value="/"/>estoque/extratoEdicao/pesquisaExtratoEdicao',
			preProcess: jsExtratoEdicao.getDataFromResult,
			dataType : 'json',
			params:[
		        {name:'numeroEdicao', value: numeroEdicao},
		        {name:'codigoProduto', value: codigoProduto},
		        {name:'nomeProduto', value: nomeProduto},
		        {name:'precoCapa', value: precoCapa},
		        {name:'nomeFornecedor', value: nomeFornecedor}
			]
		});
		
		$(".extratoEdicaoGrid").flexReload();

	},	
	
	pesquisarNomeFornecedor : function() {
		
		var data = "codigo=" + $("#codigo").val();
		
		$.postJSON('<c:url value="/"/>estoque/extratoEdicao/obterFornecedorDeProduto', data, 
				function(result){
			
			$("#nomeFornecedor").val(result);
			
		});
		
	},
	
	
	pesquisarPrecoCapa : function() {
		
		var data = "codigo=" + $("#codigo").val() +
		  		  "&edicao=" + $("#edicao").val();
		
		$.postJSON('<c:url value="/"/>estoque/extratoEdicao/obterProdutoEdicao', data, function(result){
			$("#precoCapa").val(result);
		});
		
	},
		
	pesquisarProdutoPorCodigo : function() {

		$("#nomeFornecedor").val("");
		$("#precoCapa").val("");
		
		pesquisaProdutoExtratoEdicao.pesquisarPorCodigoProduto('#codigo', '#produto', '#edicao', false, jsExtratoEdicao.pesquisarProdutoCallBack);	
	},

	pesquisarProdutoPorNome : function() {

		$("#nomeFornecedor").val("");
		$("#precoCapa").val("");
		
		pesquisaProdutoExtratoEdicao.pesquisarPorNomeProduto('#codigo', '#produto', '#edicao', false, jsExtratoEdicao.pesquisarProdutoCallBack);
	},

	validarNumeroEdicao : function() {

		$("#precoCapa").val("");
		
		pesquisaProdutoExtratoEdicao.validarNumEdicao('#codigo', '#edicao', false, jsExtratoEdicao.validarEdicaoCallBack);
	},
	
	validarEdicaoCallBack : function() {
		
		jsExtratoEdicao.pesquisarPrecoCapa();
			
	},
	
	pesquisarProdutoCallBack : function() {
		
		jsExtratoEdicao.pesquisarNomeFornecedor();
		
	},
		
	getDataFromResult : function(data) {
		
		jsExtratoEdicao.dadosPesquisa = {page: 0, total: 0};
		
		jsExtratoEdicao.saldoTotalExtratoEdicao = 0.0;
		
		var destacarValorSaldoTotal;
		
		if(typeof data.mensagens == "object") {
		
			$(".grids").hide();
			
			exibirMensagem(data.mensagens.tipoMensagem, data.mensagens.listaMensagens);
		
		} else {
			
			$.each(data, function(index, value) {
				
				  if(value[0] == "gridResult") {
					  
					  jsExtratoEdicao.dadosPesquisa = jsExtratoEdicao.destacarValorParcial(value[1]);
					  
				  } else if(value[0] == "saldoTotalExtratoEdicao") {
					  
					  jsExtratoEdicao.saldoTotalExtratoEdicao = value[1];
					  
				  } else if(value[0] == "destacarSaldoTotalExtratoEdicao") {
					  
					  destacarValorSaldoTotal = value[1];
					  
				  } 
				  
			});
			
			$(".grids").show();
			
		}
		
		if(destacarValorSaldoTotal == "S") {
			$("#saldoTotalExtratoEdicao").css("color", "red");
			$("#saldoTotalExtratoEdicao").html(jsExtratoEdicao.saldoTotalExtratoEdicao); 
		} else {
			$("#saldoTotalExtratoEdicao").css("color", "black");
			$("#saldoTotalExtratoEdicao").html(jsExtratoEdicao.saldoTotalExtratoEdicao); 
		}
		
		
		return jsExtratoEdicao.dadosPesquisa;
				
	},
	
	destacarValorParcial : function(data) {
		
		$.each(data.rows, function(index, value) {
				
				var destacarValorNegativo = value.cell[5];
				
				var valorParcial =  value.cell[4];
				
				if(destacarValorNegativo == "S") {
					
					value.cell[4] = '<span style="color: red">'+valorParcial+'</span>';
					
				} else {
					
					value.cell[4] = '<span style="color: black">'+valorParcial+'</span>';
					
				}
					
				
				
		});
		  
		return data;
		
	},
	
	carregarExtratoEdicaoGrid: function() {
		
		$(".extratoEdicaoGrid")
				.flexigrid(
						{	
							preProcess: jsExtratoEdicao.getDataFromResult,
							dataType : 'json',
							colModel : [ 
							    {
								display : 'Data',
								name : 'dataInclusao',
								width : 120,
								sortable : false,
								align : 'center'
							}, {
								display : 'Movimento',
								name : 'movimento',
								width : 370,
								sortable : false,
								align : 'left'
							}, {
								display : 'Entrada',
								name : 'entrada',
								width : 120,
								sortable : false,
								align : 'center'
							}, {
								display : 'Saída',
								name : 'saida',
								width : 120,
								sortable : false,
								align : 'center'
							}, {
								display : 'Parcial',
								name : 'parcial',
								width : 120,
								sortable : false,
								align : 'center'
							} ],

							showTableToggleBtn : true,
							width : 960,
							height : 180
						});
		

	}
	
};

$(function() {
	
	$("#edicao").numeric();
	
	$("#produto").autocomplete({
		source: ''
	});
	
	jsExtratoEdicao.carregarExtratoEdicaoGrid();
	
});

</script>

</head>

<body>

	<div class="container">

		<fieldset class="classFieldset">
		
			<legend> Extrato de Edição </legend>
			
			<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
				
				<tr>
					<td width="96">
						Código:
					</td>
					
					<td width="123">
					
						<input 	type="text" 
								name="codigo"
								id="codigo"
								maxlength="255"
								style="width: 70px; 
								float: left; 
								margin-right: 5px;"
								onchange="jsExtratoEdicao.pesquisarProdutoPorCodigo();" />

						
					
					</td>
					
					<td width="85">
						Produto:
					</td>
					
					<td width="236">

						<input type="text" name="produto" id="produto" style="width: 220px;" 
							   maxlength="255"
					       	   onkeyup="pesquisaProdutoExtratoEdicao.autoCompletarPorNomeProduto('#produto', false);"
					       	   onblur="jsExtratoEdicao.pesquisarProdutoPorNome();"/>

						
					
					</td>
					
					<td width="60">
						Edição:
					</td>
					
					<td width="186">
					
					<input type="text" style="width:70px;" name="edicao" id="edicao" maxlength="20"
						   onchange="jsExtratoEdicao.validarNumeroEdicao();"/>
								
					</td>
					
					<td width="128">&nbsp;</td>
				
				</tr>
				<tr>
					<td>Preço da Capa:</td>
					<td><input type="text" name="precoCapa" id="precoCapa"
						style="width: 70px; float: left; margin-right: 5px;"
						disabled="disabled" />
					</td>
					<td>Fornecedor:</td>
					<td><input type="text" name="textfield" id="nomeFornecedor"
						style="width: 220px;" disabled="disabled" />
					</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td><span class="bt_pesquisar"><a href="javascript:;"
							onclick="jsExtratoEdicao.pesquisarExtratoEdicao();">Pesquisar</a>
					</span>
					</td>
				</tr>
			</table>

		</fieldset>
		<div class="linha_separa_fields">&nbsp;</div>

		<fieldset class="classFieldset">
			<legend>Extrato de Edição</legend>
			<div class="grids" style="display: none;">
				<table class="extratoEdicaoGrid"></table>
				<table width="950" border="0" cellspacing="2" cellpadding="2">
					<tr>
						<td width="689">
							<span class="bt_novos" title="Gerar Arquivo">
								<a href="${pageContext.request.contextPath}/estoque/extratoEdicao/exportar?fileType=XLS">
									<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
									Arquivo
								</a>
							</span>
							<span class="bt_novos" title="Imprimir">
								<a href="${pageContext.request.contextPath}/estoque/extratoEdicao/exportar?fileType=PDF">
									<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />
									Imprimir
								</a>
							</span>
						</td>
						<td width="115"><strong>Saldo em Estoque:</strong>
						</td>
						<td width="126"><div id="saldoTotalExtratoEdicao"></div></td>
					</tr>
				</table>
			</div>


		</fieldset>

		<div class="linha_separa_fields">&nbsp;</div>

	</div>

</body>

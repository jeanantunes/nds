<head>
	<script language="javascript" type="text/javascript">
		function popup() {
			//$( "#dialog:ui-dialog" ).dialog( "destroy" );
		
			$("#dialog-novo").dialog({
				resizable: false,
				height:'auto',
				width:250,
				modal: true,
				buttons: {
					"Confirmar": function() {
						$(this).dialog("close");
						confirmar();
					},
					"Cancelar": function() {
						$(this).dialog("close");
					}
				},
				close : function(){
						$("#linkConfirmar").focus();
					}
			});
		};
		
		
		$(function() {
			$("#dataLancamento").datepicker({
				showOn: "button",
				buttonImage: "${pageContext.request.contextPath}/images/calendar.gif",
				buttonImageOnly: true,
				dateFormat: "dd/mm/yy"
			});
			$("#novaData").datepicker({
				showOn: "button",
				buttonImage: "${pageContext.request.contextPath}/images/calendar.gif",
				buttonImageOnly: true,
				dateFormat: "dd/mm/yy"
			});
			$("#produto").autocomplete({source: ""});
			
			$("#dataLancamento").mask("99/99/9999");
			$("#novaData").mask("99/99/9999");
			$("#edicao").mask("?99999999999999999999", {placeholder:""});
		});
		
		function pesquisar(){
			
			$("#resultado").hide();
			
			var data = "codigo=" + $("#codigo").val() +
			  "&produto=" + $("#produto").val() +
			  "&edicao=" + $("#edicao").val() +
			  "&dataLancamento=" + $("#dataLancamento").val();
			$.postJSON("<c:url value='/lancamento/furoProduto/pesquisar'/>", data, exibirProduto);
		}
		
		function exibirProduto(result){
			$("#txtProduto").text(result.nomeProduto);
			$("#txtEdicao").text(result.edicao);
			$("#txtQtdExemplares").text(result.quantidadeExemplares);
			if (result.pathImagem){
				$("#imagem").attr("src", result.pathImagem);
			} else {
				$("#imagem").attr("src", "${pageContext.request.contextPath}/images/logo_sistema.png");
			}
			$("#imagem").attr("alt", result.nomeProduto);
			$("#novaData").val(result.novaData);
			
			$("#codigoProdutoHidden").val(result.codigoProduto);
			$("#lancamentoHidden").val(result.idLancamento);
			$("#produtoEdicaoHidden").val(result.idProdutoEdicao);
						
			$("#resultado").show();
			$("#novaData").focus();
		}
		
		function pesquisarPorNomeProduto(){
			var produto = $("#produto").val();
			
			if (produto && produto.length > 0){
				$.postJSON("<c:url value='/lancamento/furoProduto/pesquisarPorNomeProduto'/>", "nomeProduto=" + produto, exibirAutoComplete);
			}
		}
		
		function exibirAutoComplete(result){
			$("#produto").autocomplete({
				source: result,
				select: function(event, ui){
					completarPesquisa(ui.item.chave);
				}
			});
		}
		
		function completarPesquisa(chave){
			$("#codigo").val(chave.codigoProduto);
			$("#edicao").focus();
		}
		
		function confirmar(){
			var data = "codigoProduto=" + $("#codigoProdutoHidden").val() + "&idProdutoEdicao=" + $("#produtoEdicaoHidden").val() +
			  "&novaData=" + $("#novaData").val() +
			  "&idLancamento=" + $("#lancamentoHidden").val();
			$.postJSON("<c:url value='/lancamento/furoProduto/confirmarFuro'/>", data, limparCampos);
		}
		
		function limparCampos(){
			$("#resultado").hide();
			$("#codigo").val("");
			$("#produto").val("");
			$("#edicao").mask("?99999999999999999999", {placeholder:""}).val("");
			$("#dataLancamento").val("");
			$("#novaData").val("");
			$("#codigo").focus();
		}
		
		function buscarNomeProduto(){
			if ($("#codigo").val().length > 0){
				var data = "codigoProduto=" + $("#codigo").val();
				$.postJSON("<c:url value='/lancamento/furoProduto/buscarNomeProduto'/>", data,
					function(result){
						if (result){
							$("#produto").val(result);
							$("#edicao").focus();
						} else {
							$("#produto").val("");
							$("#produto").focus();
						}
					}
				);
			}
		}
	</script>
	<style type="text/css">
		.dados, .dadosFiltro, .grids{display:none;}
	</style>
</head>

<body>
	<form action="lancamento/furoProduto/pesquisar" method="post">
		<div id="dialog-novo" title="Furo de Produto">
			<strong>Confirma o Furo de Produto?</strong>
		</div>
		<div class="areaBts">
			<div class="area">
				<span style="cursor: pointer;" class="bt_novos" onclick="popup();" rel="tipsy" title="Confirmar Furo de Produto">
	            	<a href="javascript:;" id="linkConfirmar"><img src="${pageContext.request.contextPath}/images/ico_check.gif" border="0" /></a>
	            </span>
			</div>
		</div>
		<div class="linha_separa_fields">&nbsp;</div>
    	<fieldset class="fieldFiltro">
			<legend> Furo de Produto</legend>
	        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
	        	<tr>
	        		<td width="45" align="right">Código:</td>
	        		<td width="79">
	        			<input type="text" style="width:70px;" name="codigo" id="codigo" maxlength="255" onblur="buscarNomeProduto();"/>
	        		</td>
					<td width="64" align="right">Produto:</td>
					<td width="196">
						<input type="text" name="produto" id="produto" style="width:150px;" maxlength="255" onkeyup="pesquisarPorNomeProduto();"/>
					</td>
					<td width="50" align="right">Edição:</td>
					<td width="90">
						<input type="text" style="width:70px;" name="edicao" id="edicao" maxlength="20"/>
					</td>
					<td width="150" align="right">Data Lançamento:</td>
					<td width="146">
						<input type="text" name="dataLancamento" id="dataLancamento" style="width:70px;"/>
					</td>
					<td width="258">
						<span style="cursor: pointer;" class="bt_novos" onclick="pesquisar();">
							<a href="javascript:;" id="linkPesquisar"><img src="${pageContext.request.contextPath}/images/ico_pesquisar.png" border="0" /></a>
						</span>
					</td>
	  			</tr>
	  		</table>
	  	</fieldset>
	  	
	  	<div class="linha_separa_fields">&nbsp;</div>
	  	
	  	<fieldset class="grids fieldGrid" id="resultado">
	  		<legend>Furo do Produto</legend>
	  			<div class="imgProduto">
	  				<img src="" alt="" id="imagem"/>
	  			</div>
	  			
	  		<div class="dadosProduto">	
	  			<strong id="txtProduto">Auto Motor Sport</strong>
	  			<br />
	  			<br />
	            <strong>Edição</strong>: <span id="txtEdicao">6556</span>
	            <br />
	            <br />
	            <strong>Qtde Exemplares</strong>: <span id="txtQtdExemplares">900</span>
	            <br />
	            <br />
	            <p>
	            	<strong>Nova Data:</strong>
	            	<input type="text" name="novaData" id="novaData" style="width:70px; margin-left:5px;" maxlength="10"/>
	            </p>
	            <br />
	            
	            
			</div>
			<input type="hidden" id="codigoProdutoHidden">
			<input type="hidden" id="produtoEdicaoHidden"/>
			<input type="hidden" id="lancamentoHidden"/>
		</fieldset>

	</form>
</body>
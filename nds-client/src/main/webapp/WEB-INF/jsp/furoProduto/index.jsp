<head>
	<script language="javascript" type="text/javascript">
		function popup() {
			//$( "#dialog:ui-dialog" ).dialog( "destroy" );
		
			$( "#dialog-novo" ).dialog({
				resizable: false,
				height:'auto',
				width:250,
				modal: true,
				buttons: {
					"Confirmar": function() {
						$( this ).dialog( "close" );
						$("#effect").hide("highlight", {}, 1000, callback);
						
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				}
			});
		};
		
		
		$(function() {
			$("#dataLancamento").datepicker({
				showOn: "button",
				buttonImage: "scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
				buttonImageOnly: true
			});
			$("#datepickerAte").datepicker({
				showOn: "button",
				buttonImage: "scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
				buttonImageOnly: true
			});
			
		});
		
		function pesquisar(){
			$.ajax({
				type: "POST",
				url: "lancamento/furoProduto/pesquisar",
				data: "codigo=" + $("#codigo").val() +
					  "&produto=" + $("#produto").val() +
					  "&edicao=" + $("#edicao").val() +
					  "&dataLancamento=" + $("#dataLancamento").val(),
				success: function(json){
					exibirProduto(json.result);
				},
				error: function(){
					alert("no donuts for ya");
				}
			});
		}
		
		function exibirProduto(result){
			//TODO tratar retorno pesquisa
			alert(result);
			$("#resultado").show();
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
	
		<div class="corpo">
		    <div class="container">
		    	<div id="effect" style="padding: 0 .7em;" class="ui-state-highlight ui-corner-all"> 
		    		<p>
		    			<span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
						<b>Furo de Produto < evento > com < status >.</b>
					</p>
				</div>
			
				<fieldset class="classFieldset">
					<legend> Furo de Produto</legend>
			        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
			        	<tr>
			        		<td width="45" align="right">Código:</td>
			        		<td width="79">
			        			<input type="text" style="width:70px;" name="codigo" id="codigo" />
			        		</td>
							<td width="64" align="right">Produto:</td>
							<td width="196">
								<input type="text" name="produto" id="produto" style="width:150px;" />
							</td>
							<td width="50" align="right">Edição:</td>
							<td width="90">
								<input type="text" style="width:70px;" name="edicao" id="edicao"/>
							</td>
							<td width="150" align="right">Data Lançamento:</td>
							<td width="146">
								<input type="text" name="dataLancamento" id="dataLancamento" style="width:70px;" />
							</td>
							<td width="258">
								<span style="cursor: pointer;" class="bt_pesquisar" onclick="pesquisar();">
									<a>Pesquisar</a>
								</span>
							</td>
			  			</tr>
			  		</table>
			  	</fieldset>
			  	
			  	<div class="linha_separa_fields">&nbsp;</div>
			  	
			  	<fieldset class="grids classFieldset" id="resultado">
			  		<legend>Furo do Produto</legend>
			  			<div class="imgProduto">
			  				<img src="capas/Auto_1.jpg" alt="Autosport" />
			  			</div>
			  			
			  		<div class="dadosProduto">	
			  			<strong>Auto Motor Sport</strong><br />
			  			<br />
			            <strong>Edição</strong>: 6556<br />
			            <br />
			            <strong>Qtde Exemplares</strong>: 900
			            <br />
			            <br />
			            <p>
			            	<strong>Nova Data:</strong>
			            	<input type="text" name="datepickerAte" id="datepickerAte" style="width:70px; margin-left:5px;" />
			            </p>
			            <br />
			            
			            <span class="bt_confirmar"><a href="javascript:;" onclick="popup();">Confirmar</a></span>
					</div>
				</fieldset>
		    </div>
		</div>
	</form>
</body>
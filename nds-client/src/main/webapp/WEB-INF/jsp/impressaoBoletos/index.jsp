<head>
	<script language="javascript" type="text/javascript">
	
		$(function() {
			$( "#datepickerDe" ).datepicker({
				showOn: "button",
				buttonImage: "${pageContext.request.contextPath}/images/calendar.gif",
				buttonImageOnly: true,
				dateFormat: "dd/mm/yy"
			});
			$( "#datepickerDivida" ).datepicker({
				showOn: "button",
				buttonImage: "${pageContext.request.contextPath}/images/calendar.gif",
				buttonImageOnly: true,
				dateFormat: "dd/mm/yy"
			});
			
			$(".impressosGrid").flexigrid({
				dataType : 'xml',
				colModel : [ {
					display : 'Box',
					name : 'box',
					width : 60,
					sortable : true,
					align : 'left'
				}, {
					display : 'Rota',
					name : 'rota',
					width : 80,
					sortable : true,
					align : 'left'
				},{
					display : 'Roteiro',
					name : 'roteiro',
					width : 100,
					sortable : true,
					align : 'left'
				},{
					display : 'Cota',
					name : 'Cota',
					width : 40,
					sortable : true,
					align : 'left'
				}, {
					display : 'Nome',
					name : 'nome',
					width : 180,
					sortable : true,
					align : 'left'
				}, {
					display : 'Vencimento',
					name : 'vencimento',
					width : 80,
					sortable : true,
					align : 'center'
				}, {
					display : 'Via',
					name : 'via',
					width : 50,
					sortable : true,
					align : 'center'
				}, {
					display : 'Emissão',
					name : 'emissao',
					width : 80,
					sortable : true,
					align : 'center'
				}, {
					display : 'Valor',
					name : 'valor',
					width : 80,
					sortable : true,
					align : 'right'
				},{
					display : 'Ação',
					name : 'Ação',
					width : 70,
					sortable : true,
					align : 'center'
				}],
				usepager : true,
				useRp : true,
				rp : 15,
				showTableToggleBtn : true,
				width : 960,
				height : 180
			});
		});
	
		function gerarDivida(){
			$("#aguarde").dialog({
				title: 'Processando',
				resizable: false,
				height:60,
				width:50,
				modal: true,
				open: function (){
						$(this).parent().children().children('.ui-dialog-titlebar-close').remove();
					  }
			});
			
			$("#aguarde").show();
			
			$.postJSON(
				'/nds-client/financeiro/impressaoBoletos/gerarDivida',
				null,
				function(result) {
					$("#aguarde").dialog("close");
					mostrar();
				},
				function(result) {
					$("#aguarde").dialog("close");
				},
				false
			);
		}
	
	</script>
</head>

<body>
	<div class="corpo">
		<br clear="all"/>
		<br />
	    <div class="container">
			<div id="effect" style="padding: 0 .7em;" class="ui-state-highlight ui-corner-all"> 
				<p><span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
				<b>Chamadão < evento > com < status >.</b></p>
			</div>
			<div style="display: none;" id="aguarde">Aguarde...</div>
			<fieldset class="classFieldset">
	   	    	<legend> Gerar Divida</legend>
	   	    		<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
	  					<tr>
						    <td width="29">Data:</td>
						    <td width="125"><input type="text" name="datepickerDe" id="datepickerDe" style="width:70px; float:left; margin-right:5px;" /></td>
						    <td width="49">Box:</td>
						    <td width="169"><input type="text" style="width:70px; float:left; margin-right:5px;" /></td>
						    <td width="43">Rota:</td>
						    <td width="198"><input type="text" style="width:160px; float:left; margin-right:5px;" /></td>
						    <td width="54">Roteiro:</td>
						    <td width="242"><input type="text" style="width:160px; float:left; margin-right:5px;" /></td>
					    </tr>
	 		 			<tr>
	    					<td>Cota:</td>
	    					<td>
	    						<input type="text" style="width:70px; float:left; margin-right:5px;" />
	      						<span class="classPesquisar"><a href="javascript:;">&nbsp;</a></span>
	      					</td>
	    					<td>Nome:</td>
	    					<td><input type="text" style="width:160px; float:left; margin-right:5px;" /></td>
						    <td>&nbsp;</td>
						    <td>&nbsp;</td>
						    <td>&nbsp;</td>
						    <td>
						    	<span class="bt_pesquisar">
						    		<a href="javascript:;" onclick="alert('Não foi feita a Geração de Dívida para esta data, clicque no botão Gerar Dívida.');">Pesquisar</a>
						    	</span>
	    						<span class="bt_novos" title="Gerar Dívida" style="margin-left:20px;">
	    							<a href="javascript:;" onclick="gerarDivida();">
	    								<img src="${pageContext.request.contextPath}/images/ico_check.gif" hspace="5" border="0" />
	    								Gerar Divida
	    							</a>
	    						</span>
	   						</td>
	    				</tr>
	  				</table>
	      	</fieldset>
			
			<div class="linha_separa_fields">&nbsp;</div>
			
			<fieldset class="classFieldset">
				<legend>Dividas Geradas</legend>
					<div class="grids" style="display:none;">
						<table class="impressosGrid"></table>
	          			<span class="bt_novos" title="Gerar Arquivo">
	          				<a href="javascript:;">
	          					<img src="${pageContext.request.contextPath}/images/ico_excel.png"  hspace="5" border="0" />
	          					Arquivo
	          				</a>
	          			</span>
						<span class="bt_novos" title="Imprimir">
							<a href="javascript:;">
								<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
								Imprimir
							</a>
						</span>
					</div>
	      	</fieldset>
			
			<div class="linha_separa_fields">&nbsp;</div>    
		</div>
	</div>
</body>
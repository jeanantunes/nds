<head>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/cota.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
	<script language="javascript" type="text/javascript">
	
	var GeraDivida = {
			
		executarPreProcessamento : function (resultado){
			
			//Verifica mensagens de erro do retorno da chamada ao controller.
			if (resultado.mensagens) {

				exibirMensagem(
					resultado.mensagens.tipoMensagem, 
					resultado.mensagens.listaMensagens
				);
				
				$("#grids").hide();

				return resultado.tableModel;
			}
			
			// Monta as colunas com os inputs do grid
			$.each(resultado.rows, function(index, row) {
				
				var nossoNumero  = row.cell.nossoNumero;
				
				var linkImpressao = '<a href="javascript:;" onclick="GeraDivida.imprimirDivida(' + nossoNumero + ');" style="cursor:pointer">' +
					 '<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0px" title="Imprime" />' +
					 '</a>';			
				
				var linkEmail ='<a href="javascript:;" style="cursor:default; opacity:0.4; filter:alpha(opacity=40)">' +
                				'<img src="${pageContext.request.contextPath}/images/ico_email.png" hspace="5" border="0px" title="Divida não tem suporte para Envio de Arquivo por E-Mail" />' +
                				'</a>';
				
				if(row.cell.suportaEmail == "true"){
				 
					linkEmail ='<a href="javascript:;" onclick="GeraDivida.enviarDivida(' + nossoNumero + ');" style="cursor:pointer">' +
	                 '<img src="${pageContext.request.contextPath}/images/ico_email.png" hspace="5" border="0px" title="Enviar Arquivo por E-Mail" />' +
	                  '</a>';		 					 
					
				}	 
				
                 row.cell.acao = linkImpressao + linkEmail ; 
			});
			
			$("#grids").show();
				
			return resultado;
		},
		
		enviarDivida:function(nossoNumero) {
			var data = [{ name: 'nossoNumero', value: nossoNumero}];
			
			$.postJSON("<c:url value='/financeiro/impressaoBoletos/enviarDivida' />", data,function(){
				GeraDivida.pesquisar();	
			});
		},
		
		formData: function(){
			
			var formData = [ {name:"dataMovimento",value:$("#dataMovimento").val()},
				             {name:"box",value:$("#box").val()},
				             {name:"rota",value:$("#rota").val()},
				             {name:"roteiro",value:$("#roteiro").val()},
				             {name:"numCota",value:$("#numCota").val()},
				             {name:"tipoCobranca",value:$("#tipoCobranca").val()}
				            ];
			return formData;
		},
		/**
			Executa a pesquisa de dividas geradas
		**/
		pesquisar: function (){
			
			$("#impressosGrid").flexOptions({
				url: "<c:url value='/financeiro/impressaoBoletos/consultar' />",
				params: GeraDivida.formData(),newp: 1
			});
			
			$("#impressosGrid").flexReload();

		},
		
		/**
			Executa o m�todo de gera��o de dividas
		**/
		gerarDivida : function (){
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
		},
		pesquisarCotaSuccessCallBack:function(){
			
			var data = "numeroCota=" + $("#numCota").val();
			
			$.postJSON("<c:url value='/financeiro/impressaoBoletos/pesquisarInfoCota' />",
					   data, function(result){
				
				$("#box").val(result.box);
				$("#rota").val(result.rota);
				$("#roteiro").val(result.roteiro);
				$("#tipoCobranca").val(result.tipoCobranca);
			});
			//Efetuar a pesquisa de box, rota roteiro
		},
		pesquisarCotaErrorCallBack:function(){
			$("#box").val("");
			$("#rota").val("");
			$("#roteiro").val("");
			$("#tipoCobranca").val("");
			
			GeraDivida.recarregarComboRoteiroRotas(null);
		},
		validarPesquisa:function(){
			
			var data = 'dataMovimento='+$("#dataMovimento").val();
			
			$.postJSON("<c:url value='/financeiro/impressaoBoletos/validarPesquisaDivida' />",
					data, function(result){
				
				if(result == "false"){
					GeraDivida.dialogPesquisaInvalida();
					$("#pesquisaInvalida").show();
					$("#grids").hide();
				}else{
					GeraDivida.pesquisar();
				}	
			});
		},
		dialogPesquisaInvalida:function(){
			
			$("#pesquisaInvalida").dialog({
				title: 'Atenção',
				resizable: false,
				height:120,
				width:330,
				modal: true,
				buttons : {
					"Fechar" : function() {
						$(this).dialog("close");
					}
				}
			});
		},
		
		imprimirDividas:function(tipoImpressao){
			
			$.postJSON("<c:url value='/financeiro/impressaoBoletos/validarImpressaoDividas' />",
					[{name:"tipoImpressao",value:tipoImpressao}], function(result){
				
				$("#impressosGrid").flexOptions({
					url: "<c:url value='/financeiro/impressaoBoletos/consultar' />",
					params: GeraDivida.formData(),newp: 1,
					onSuccess:GeraDivida.renderizarArquivos(result)
				});	
				
				$("#impressosGrid").flexReload();
			});
		},
		
		renderizarArquivos:function(result){
			
			if("BOLETO" == result){
				window.open("<c:url value='/financeiro/impressaoBoletos/imprimirBoletosEmMassa'/>",'page','toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no,width=1,height=1');
			}
			else if ("DIVIDA" == result) {
				window.open("<c:url value='/financeiro/impressaoBoletos/imprimirDividasEmMassa'/>",'page','toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no,width=1,height=1');
			}
		},
		
		imprimirDivida:function(nossoNumero){
			
			$.postJSON("<c:url value='/financeiro/impressaoBoletos/validarImpressaoDivida' />",
					[{name:"nossoNumero",value:nossoNumero}], function(result){
				
				if(result == "true"){
					
					$("#impressosGrid").flexOptions({
						url: "<c:url value='/financeiro/impressaoBoletos/consultar' />",
						params: GeraDivida.formData(),
						onSuccess:GeraDivida.renderizarArquivo(nossoNumero)
					});
					
					$("#impressosGrid").flexReload();
				}	
			});
		},
		
		renderizarArquivo: function (nossoNumero){

			window.open("<c:url value='/financeiro/impressaoBoletos/imprimirDivida?nossoNumero="+ nossoNumero +"'/>",'page','toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no,width=1,height=1');

		},
		
		recarregarComboRotas:function(idRoteiro){
			
			$.postJSON("<c:url value='/financeiro/impressaoBoletos/recarregarListaRotas' />",
					[{name:"roteiro",value:idRoteiro}], function(result){
				
				var comboRotas =  montarComboBox(result, true);
				
				$("#rota").html(comboRotas);	
			});
		},
		
	recarregarComboRoteiroRotas:function(idBox){
			
			$.postJSON("<c:url value='/financeiro/impressaoBoletos/recarregarRoteiroRota' />",
					[{name:"idBox",value:idBox}], function(result){
				
				var comboRotas =  montarComboBoxCustomJson(result.rotas, true);
				var comboRoteiros = montarComboBoxCustomJson(result.roteiros, true);
				
				$("#rota").html(comboRotas);
				$("#roteiro").html(comboRoteiros);
			});
		},
	
	habilitarAcaoGeracaoDivida:function(valor){
		
		$.postJSON("<c:url value='/financeiro/impressaoBoletos/habilitarAcaoGeracaoDivida' />",
				[{name:"dataPesquisa",value:valor}], function(result){
			
			if(result.isAcaoGeraDivida == true){
				$("#divGerarDivida").show();	
			}
			else{
				$("#divGerarDivida").hide();
			}
		});
	}
		
	};
	
	$(function() {
		
		$( "#dataMovimento" ).datepicker({
			showOn: "button",
			buttonImage: "${pageContext.request.contextPath}/images/calendar.gif",
			buttonImageOnly: true,
			dateFormat: "dd/mm/yy"
		});
		
		
		$("#descricaoCota").autocomplete({source: ""});
		
		$('input[id^="data"]').mask("99/99/9999");
		
		$("input[name='numCota']").numeric();
		
		$("#dataMovimento").focus();
		
		$("#impressosGrid").flexigrid({
			preProcess:GeraDivida.executarPreProcessamento,
			dataType : 'json',
			colModel : [ {
				display : 'Box',
				name : 'box',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Rota',
				name : 'rota',
				width : 60,
				sortable : true,
				align : 'left'
			},{
				display : 'Roteiro',
				name : 'roteiro',
				width : 80,
				sortable : true,
				align : 'left'
			},{
				display : 'Cota',
				name : 'numeroCota',
				width : 40,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nomeCota',
				width : 130,
				sortable : true,
				align : 'left'
			}, {
				display : 'Vencimento',
				name : 'dataVencimento',
				width : 75,
				sortable : true,
				align : 'center'
			}, {
				display : 'Via',
				name : 'vias',
				width : 50,
				sortable : true,
				align : 'center'
			}, {
				display : 'Emissão',
				name : 'dataEmissao',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : 'Valor',
				name : 'valor',
				width : 60,
				sortable : true,
				align : 'right'
			},{
				display : 'Tipo',
				name : 'tipoCobranca',
				width : 100,
				sortable : true,
				align : 'left'
			},{
				display : 'Ação',
				name : 'acao',
				width : 70,
				sortable : false,
				align : 'center'
			}],
			usepager : true,
			useRp : true,
			sortname : "rota,roteiro,numeroCota",
			sortorder : "asc",
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 180
		});
	});

	
	
	</script>
</head>

<body>
	<div style="display: none;" id="aguarde">Aguarde...</div>
	<div style="display: none;heigth:200px;  min-height:0px;" id="pesquisaInvalida">
		Não foi feita a Geração de Dívidas para esta data.
	</div>	
		<form id="pesquisaDividasForm"
				name="pesquisaDividasForm" 
				method="post">
			<fieldset class="classFieldset">
		   	    <legend> Gerar Dívida</legend>
		   	    	<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
	  					<tr>
						    <td width="29">Data:</td>
						    <td width="125"><input type="text" name="dataMovimento" id="dataMovimento" onchange="GeraDivida.habilitarAcaoGeracaoDivida(this.value);" style="width:70px; float:left; margin-right:5px;" /></td>
						    <td width="49">Box</td>
						    <td width="169">
						    	<select name="box" id="box" style="width:70px; float:left; margin-right:5px;" onchange="GeraDivida.recarregarComboRoteiroRotas(this.value)">
							      <option selected="selected" value="">Todos</option>
							      <c:forEach var="box" items="${listaBoxes}">
											<option value="${box.key}">${box.value}</option>
								  </c:forEach>
							    </select>
						    </td>
						    <td width="43">Roteiro</td>
						    <td width="198">
						    	<select name="roteiro" id="roteiro" style="width:160px; float:left; margin-right:5px;" onchange="GeraDivida.recarregarComboRotas(this.value)" >
							      <option selected="selected" value="">Todos</option>
							      <c:forEach var="roteiro" items="${listaRoteiros}">
											<option value="${roteiro.key}">${roteiro.value}</option>
								  </c:forEach>
							    </select>
						    </td>
						    <td width="54">Rota</td>
						    <td width="242">
						    	<select name="rota" id="rota" style="width:160px; float:left; margin-right:5px;"  >
							      <option selected="selected" value="">Todos</option>
							      <c:forEach var="rota" items="${listaRotas}">
											<option value="${rota.key}">${rota.value}</option>
								  </c:forEach>
							    </select>
						    </td>
					    </tr>
	 		 			<tr>
	    					<td>Cota:</td>
	    					<td>
	    						<input name="numCota" 
				              		   id="numCota" 
				              		   type="text"
				              		   maxlength="11"
				              		   style="width:70px; 
				              		   float:left; margin-right:5px;"
				              		   onchange="cota.pesquisarPorNumeroCota('#numCota', '#descricaoCota',false,
				              	  											GeraDivida.pesquisarCotaSuccessCallBack, 
				              	  											GeraDivida.pesquisarCotaErrorCallBack);" />
	      					</td>
	    					<td>Nome:</td>
	    					<td>
	    						 <input  name="descricaoCota" 
							      		 id="descricaoCota" 
							      		 type="text" 
							      		 class="nome_jornaleiro" 
							      		 maxlength="255"
							      		 style="width:130px;"
							      		 onkeyup="cota.autoCompletarPorNome('#descricaoCota');" 
							      		 onblur="cota.pesquisarPorNomeCota('#numCota', '#descricaoCota',false,
							      		 									GeraDivida.pesquisarCotaSuccessCallBack,
							      		 									GeraDivida.pesquisarCotaErrorCallBack);" />
	    					</td>
						    <td>Tipo</td>
						    <td>
						    	<select name="tipoCobranca" id="tipoCobranca" style="width: 200px;">
									<option selected="selected" value="">Todos</option>
									<c:forEach var="tipo" items="${listaTipoCobranca}">
										<option value="${tipo.key}">${tipo.value}</option>
									</c:forEach>
								</select>
								
						    </td>
						    <td>&nbsp;</td>
						    <td>
						    	<span class="bt_pesquisar">
						    		<a href="javascript:GeraDivida.validarPesquisa();">Pesquisar</a>
						    	</span>
	    						<div id="divGerarDivida" style="display: none">
		    						<span class="bt_novos" title="Gerar Dívida" style="margin-left:20px;">
		    							<a href="javascript:GeraDivida.gerarDivida();" id="btnGerarDivida">
		    								<img src="${pageContext.request.contextPath}/images/ico_check.gif" hspace="5" border="0" />
		    								Gerar Dívida
		    							</a>
		    						</span>
	    					   </div>
	   						</td>
	    				</tr>
		  		</table>
		    </fieldset>
		 </form>
		 
		<div class="linha_separa_fields">&nbsp;</div>
			
		<fieldset class="classFieldset">
			<legend>Dividas Geradas</legend>
				<div class="grids" id="grids" style="display:none;">
					<table class="impressosGrid" id="impressosGrid"></table>
          			<span class="bt_novos" title="Gerar Arquivo">
          				<a href="${pageContext.request.contextPath}/financeiro/impressaoBoletos/exportar?fileType=XLS">
          					<img src="${pageContext.request.contextPath}/images/ico_excel.png"  hspace="5" border="0" />
          					Arquivo
          				</a>
          			</span>
					<span class="bt_novos" title="Imprimir">
						<a href="${pageContext.request.contextPath}/financeiro/impressaoBoletos/exportar?fileType=PDF">
							<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
							Imprimir
						</a>
					</span>
					
					<span class="bt_novos" title="Imprimir Boletos">
						<a href="javascript:GeraDivida.imprimirDividas('BOLETO')">
							<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
							Imprimir Boletos
						</a>
					</span>
					
					<span class="bt_novos" title="Imprimir Dividas">
						<a href="javascript:GeraDivida.imprimirDividas('DIVIDA')">
							<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
							Imprimir Dívidas
						</a>
					</span>
					
				</div>
      	</fieldset>
			
		<div class="linha_separa_fields">&nbsp;</div>    
</body>

<head>
	
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
		
	<script>

		$(function() {
			
			inicializar();
		});
		
		function iniciarGrid() {
			$(".serviceGrid").flexigrid({
				preProcess: executarPreProcessamento,
				dataType : 'json',
				colModel : [ {
					display : 'C&oacute;digo',
					name : 'codigo',
					width : 70,
					sortable : true,
					align : 'left'
				}, {
					display : 'Descri&ccedil;&atilde;o',
					name : 'descricao',
					width : 200,
					sortable : true,
					align : 'left'
				}, {
					display : 'Taxa R$',
					name : 'taxa',
					width : 70,
					sortable : true,
					align : 'right'
				}, {
					display : 'Isen&ccedil;&atilde;o',
					name : 'isento',
					width : 115,
					sortable : true,
					align : 'center'
				}, {
					display : 'Base de C&aacute;lculo',
					name : 'baseCalculo',
					width : 220,
					sortable : true,
					align : 'left'
				}, {
					display : '% C&aacute;lculo sobre Base',
					name : 'percentualCalculoBase',
					width : 120,
					sortable : true,
					align : 'right'
				}, {
					display : 'Ação',
					name : 'acao',
					width : 60,
					sortable : false,
					align : 'center'
				}],
				sortname : "codigo",
				sortorder : "asc",
				usepager : true,
				useRp : true,
				rp : 15,
				showTableToggleBtn : true,
				width : 960,
				height : 255,
				singleSelect : true
			});
		}
		
		function inicializar() {
			
			iniciarGrid();
			aplicarMascaras();
		}

		function aplicarMascaras() {

			$("#codigoCadastro").numeric();
			$("#taxaFixaCadastro").numeric();
			$("#percentualCalculoBase").numeric();   	
		}

		function limparModalCadastro() {

			$("#codigoCadastro").removeAttr('disabled');
			$("#codigoCadastro").val("");
			$("#descricaoCadastro").val("");
			$("#taxaFixaCadastro").val("");
			$("#isencaoCadastro").val(false);
			$("#periodicidadeCadastro").val("");
			$("#baseCalculoCadastro").val("");
			$("#percentualCalculoBase").val("");

			aplicarMascaras();			
		}
		
		function pesquisar() {
	
			var codigo = $("#codigo").val();
			var descricao = $("#descricao").val();
			var periodicidade = $("#periodicidade").val();
	
			$(".serviceGrid").flexOptions({
				url: "<c:url value='/servico/cadastroServico/pesquisarServicos' />",
				params: [{name:'codigo', value: codigo },
					     {name:'descricao', value: descricao },
					     {name:'periodicidade', value: periodicidade }],
				newp: 1,
			});
			
			$(".serviceGrid").flexReload();
		}

		function editarServico(codigoServico) {

			$.postJSON(
				"<c:url value='/servico/cadastroServico/buscarServico' />",
				"codigo=" + codigo,
				function (result) {
					
					$("#codigoCadastro").attr('disabled', '');
					carregarEdicao(result);
					incluirNovo();
				}
			);
		}

		function carregarEdicao(servico) {

			$("#codigoCadastro").val(servico.codigo);
			$("#descricaoCadastro").val(servico.descricao);
			$("#taxaFixaCadastro").val(servico.taxa);
			$("#isencaoCadastro").val(servico.isento);
			$("#periodicidadeCadastro").val(servico.periodicidade);
			$("#baseCalculoCadastro").val(servico.baseCalculo);
			$("#percentualCalculoBase").val(servico.percentualCalculoBase);
		}
		
		function incluirNovo() {

			$("#dialog-novo").dialog({
				resizable : false,
				height : 'auto',
				width : 450,
				modal : true,
				buttons : {
					"Confirmar" : function() {

						var codigo = $("#codigoCadastro").val();
						var descricao = $("#descricaoCadastro").val();
						var taxa = $("#taxaFixaCadastro").val();
						var isencao = $("#isencaoCadastro").val();
						var periodicidade = $("#periodicidadeCadastro").val();
						var baseCalculo = $("#baseCalculoCadastro").val();
						var percentual = $("#percentualCalculoBase").val();   	
						
						$.postJSON("<c:url value='/servico/cadastroServico/salvarServico' />", 
								   "codigo=" + codigo +
								   "&descricao=" + descricao +
								   "&taxaFixa="+ taxa +
								   "&isento=" + isencao +
								   "&periodicidade=" + periodicidade +
								   "&baseCalculo=" + baseCalculo +
								   "&percentualCalculo=" + percentual,
								   function(result) {
								   		
								   		$("#dialog-novo").dialog("close");
								   		
										var tipoMensagem = result.tipoMensagem;
										var listaMensagens = result.listaMensagens;
										
										if (tipoMensagem && listaMensagens) {
											
											exibirMensagem(tipoMensagem, listaMensagens);
										}
												
										$(".serviceGrid").flexReload();
								   },
								   null,
								   true
						);
					},
					"Cancelar" : function() {
						$(this).dialog("close");
					}
				},
				beforeClose: function() {
					clearMessageDialogTimeout();
					limparModalCadastro();
				}
			});
		}
		
		function removerServico(codigo) {
			
			$("#dialog-excluir").dialog({
				resizable : false,
				height : 'auto',
				width : 450,
				modal : true,
				buttons : {
					"Confirmar" : function() {
						
						$.postJSON("<c:url value='/servico/cadastroServico/removerServico' />", 
								   "codigo=" + codigo,
								   function(result) {
								   		
								   		$("#dialog-excluir").dialog("close");
								   		
										var tipoMensagem = result.tipoMensagem;
										var listaMensagens = result.listaMensagens;
										
										if (tipoMensagem && listaMensagens) {
											
											exibirMensagem(tipoMensagem, listaMensagens);
										}
												
										$(".serviceGrid").flexReload();
								   },
								   null,
								   true
						);
					},
					"Cancelar" : function() {
						$(this).dialog("close");
					}
				},
				beforeClose: function() {
					clearMessageDialogTimeout();
				}
			});
		}
		
		function executarPreProcessamento(resultado) {
						
			$.each(resultado.rows, function(index, row) {
				
				var linkAprovar = '<a href="javascript:;" onclick="editarServico(' + row.cell.codigo + ');" style="cursor:pointer">' +
						     	  	'<img title="Aprovar" src="${pageContext.request.contextPath}/images/ico_editar.gif" hspace="5" border="0px" />' +
						  		  '</a>';
				
				var linkExcluir = '<a href="javascript:;" onclick="removerServico(' + row.cell.codigo + ');" style="cursor:pointer">' +
								   	 '<img title="Excluir" src="${pageContext.request.contextPath}/images/ico_excluir.gif" hspace="5" border="0px" />' +
								   '</a>';
				
				row.cell.acao = linkAprovar + linkExcluir;
			});
				
			$(".grids").show();
			
			return resultado;
		}
		
	</script>
	
	<style>
		label{ vertical-align:super;}
		#dialog-novo label{width:370px; margin-bottom:10px; float:left; font-weight:bold; line-height:26px;}
		#dialog-novo select, #dialog-novo input {
		    float: right!important;
		}
	</style>

</head>

<body>

	<div id="dialog-excluir" title="Excluir Servi&ccedil;o de Entrega">
	
		<jsp:include page="../messagesDialog.jsp" />
		
		<p>Confirma a exclus&atilde;o deste Servi&ccedil;o de Entrega?</p>
		
	</div>

	<!--modal -->
	<div id="dialog-novo" title="Incluir Novo Servi&ccedil;o de Entrega">
    
    	<label>C&oacute;digo:<input id="codigoCadastro" type="text" style="width:250px;" /></label>
    	
    	<br clear="all" />
    
    	<label>Descri&ccedil;&atilde;o:<input id="descricaoCadastro" type="text" style="width:250px;" /></label>
    	
    	<br clear="all" />
    	
    	<label>Taxa Fixa R$:<input id="taxaFixaCadastro" type="text" style="width:250px;" /></label>
    
    	<br clear="all" />
    	
    	<label>
    		<span style="float:left!important;" >Possibilita Isen&ccedil;&atilde;o:</span>
    		<input id="isencaoCadastro" type="checkbox" style="float:left!important;" />
    	</label>
    	
    	<br clear="all" />
    	
    	<label>Periodicidade:
		    <select id="periodicidadeCadastro" style="width:257px;">
			    <option value="" selected="selected">Selecione...</option>
				<option value="D" >Di&aacute;rio</option>
				<option value="S" >Semanal</option>
				<option value="M" >Mensal</option>
		    </select>
	    
    	</label>
    	
    	<br clear="all" />
	    
	    <label>Base de C&aacute;lculo:
		    <select id="baseCalculoCadastro" style="width:257px;">
			    <option value="" selected="selected">Selecione...</option>
			    <option value="B" >Faturamento Bruto</option>
			    <option value="L" >Faturamento L&iacute;quido</option>
		    </select>
	    </label>
	    
	    <br clear="all" />

    
    	<label>
    		<span style="float:left!important;" >(%) para c&aacute;lculo sobre base:</span>
    		<input id="percentualCalculoBase" type="text" style="float:left!important; width:70px; margin-left:5px;" />
    	</label>
 
	    <br />
	    
	    <br />
	  	
	  	<br clear="all" />
	
		<span class="bt_add">
			<a href="javascript:;" onclick="popup();">Incluir Novo</a>
		</span> 
	</div>
	
	
	<!-- pesquisa -->	
    <fieldset class="classFieldset">
    	<legend> Pesquisar Servi&ccedil;os de Entrega</legend>
   		<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
			<tr>
				<td width="46">C&oacute;digo:</td>
				<td width="118" ><input type="text" name="codigo" id="codigo" style="width:100px;"/></td>
				
				<td width="63">Descri&ccedil;&atilde;o:</td>
				<td width="250"><input type="text" name="descricao" id="descricao" style="width:222px;"/></td>
				
				<td width="82">Periodicidade:</td>
				<td width="251">
					<select name="periodicidade" id="periodicidade" style="width:120px;">
						<option value="" selected="selected">Selecione...</option>
						<option value="D" >Di&aacute;rio</option>
						<option value="S" >Semanal</option>
						<option value="M" >Mensal</option>
					</select>
				</td>
				<td width="104">
					<span class="bt_pesquisar" title="Pesquisar Servi�o">
						<a href="javascript:;" onclick="pesquisar();">Pesquisar</a>
					</span>
				</td>
			</tr>
		</table>
	</fieldset>

	<!-- GRID PAGINACAO -->
	<div class="linha_separa_fields">&nbsp;</div>
		<fieldset class="classFieldset">
	 		<legend>Servi&ccedil;os de Entrega Cadastrados</legend>
			
			<div class="grids" style="display:none;">
				<table class="serviceGrid"></table>
			</div>
	
			<span class="bt_novos" title="Novo">
				<a href="javascript:;" onclick="incluirNovo();" >
					<img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"/>
					Novo
				</a>
			</span>
		</fieldset>
		
		<div class="linha_separa_fields">&nbsp;</div>
	   
	</div>

</body>
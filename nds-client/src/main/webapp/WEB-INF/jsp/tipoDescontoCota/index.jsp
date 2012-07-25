<head>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/cota.js"></script>

<script language="javascript" type="text/javascript">

	$(function() {		
		$("#produto").autocomplete({source: ""});		
		$("#descontoGeral").mask("99.99");
		$("#descontoEspecifico").mask("99.99");
		$("#descontoProduto").mask("99.99");		
	});
	
	function pesquisar() {
		var retorno = verificarTipoDescontoSelecionado();				
		if(retorno == 'geral'){
			pesquisarDescontoGeral();						
		}else if(retorno == 'especifico'){
			pesquisarDescontoEspecifico();
		}else{
			pesquisarDescontoProduto();
		}	
	}

	function verificarTipoDescontoSelecionado(){
		var geral = $("#radioGeral").is(":checked");
		var especifico = $("#radioEspecifico").is(":checked");
		var produto = $("#radioProduto").is(":checked");
		if(geral){			
			return "geral";					
		}else if(especifico){			
			return "especifico";				
		}else if(produto){			
			return "produto";
		}	


		}
	
	function pesquisarDescontoGeral(){
		var descontoGeral = $("#descontoGeral").val();
		var dataAlteracao = $("#dataAlteracaoGeral").val();
		var usuario = $("#textfield24").val();		
		
		$(".tiposDescGeralGrid").flexOptions({
			url: "<c:url value='/administracao/tipoDescontoCota/pesquisarDescontoGeral'/>",
			params: [],
		    newp: 1,
		});
		
		$(".tiposDescGeralGrid").flexReload();
	}
	
	function pesquisarDescontoEspecifico(){
		 
		var cotaEspecifica = $("#cotaDaPesquisa").val();
		var nomeEspecifico = $("#nomeDaCotaDaPesquisa").val();
		
		$(".tiposDescEspecificoGrid").flexOptions({
			url: "<c:url value='/administracao/tipoDescontoCota/pesquisarDescontoEspecifico'/>",
			params: [
					 {name:'cotaEspecifica', value:cotaEspecifica},
			         {name:'nomeEspecifico', value:nomeEspecifico}
			         ],
		    newp: 1,
		});
		
		$(".tiposDescEspecificoGrid").flexReload();
	}
	
	function pesquisarDescontoProduto(){
		var codigo = $("#codigoPesquisa").val();
		var produto = $("#produtoPesquisa").val();
		
		$(".tiposDescProdutoGrid").flexOptions({
			url: "<c:url value='/administracao/tipoDescontoCota/pesquisarDescontoProduto'/>",
			params: [
					 {name:'codigo', value:codigo},
			         {name:'produto', value:produto}
			         ],
		    newp: 1,
		});
		
		$(".tiposDescProdutoGrid").flexReload();
	}
	
	function buscarNomeProduto(){
		if ($("#codigo").val().length > 0){
			var data = "codigoProduto=" + $("#codigo").val();
			$.postJSON("<c:url value='/lancamento/furoProduto/buscarNomeProduto'/>", data,
				function(result){
					if (result){
						$("#produto").val(result);	
						$("#edicaoProduto").focus();
					} else {
						$("#produto").val("");
						$("#produto").focus();
					}
				}
			);
		}else if ($("#codigoPesquisa").val().length > 0){
			var data = "codigoProduto=" + $("#codigoPesquisa").val();
			$.postJSON("<c:url value='/lancamento/furoProduto/buscarNomeProduto'/>", data,
				function(result){
					if (result){
						$("#produtoPesquisa").val(result);	
						$("#produtoPesquisa").focus();
					} else {
						$("#produtoPesquisa").val("");
						$("#produtoPesquisa").focus();
					}
				}
			);
		}
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
	}	
	
	
	function novoDescontoGeral() {

		var descontoGeral = $("#descontoGeral").val();
		var dataAlteracao = $("#dataAlteracaoGeral").val();
		var usuario = $("#textfield24").val();		
		
		$.postJSON("<c:url value='/administracao/tipoDescontoCota/novoDescontoGeral'/>",
					[
						{name: "descontoDistribuidor.desconto" , value: descontoGeral},
						{name: "descontoDistribuidor.dataAlteracao" , value: dataAlteracao}						
					],				   
				   function(result) {
			           fecharDialogs();
					   var tipoMensagem = result.tipoMensagem;
					   var listaMensagens = result.listaMensagens;
					   if (tipoMensagem && listaMensagens) {
					       exibirMensagem(tipoMensagem, listaMensagens);
				       }
					   pesquisar();
	               },
				   null,
				   true);

		$(".tiposDescGeralGrid").flexReload();
		
	}
	

	function novoDescontoEspecifico() {
		
		var cotaEspecifica = $("#cotaEspecifica").val();
		var nomeEspecifico = $("#nomeEspecifico").val();
		var descontoEspecifico = $("#descontoEspecifico").val();
		var dataAlteracaoEspecifico = $("#dataAlteracaoEspecifico").val();
		var usuarioEspecifico = $("#usuarioEspecifico").val()
		
		$.postJSON("<c:url value='/administracao/tipoDescontoCota/novoDescontoEspecifico'/>",
				[
					{name: "cotaEspecifica", value:cotaEspecifica },					
					{name: "descontoCota.desconto", value: descontoEspecifico },
					{name: "descontoCota.dataAlteracao", value: dataAlteracaoEspecifico }
					],				   
				   function(result) {
			           fecharDialogs();
					   var tipoMensagem = result.tipoMensagem;
					   var listaMensagens = result.listaMensagens;
					   if (tipoMensagem && listaMensagens) {
					       exibirMensagem(tipoMensagem, listaMensagens);
				       }
	                   pesquisar();
	               },
				   null,
				   true);
		$(".tiposDescEspecificoGrid").flexReload();
		
	}
	
	function novoDescontoProduto() {
		
		var codigo = $("#codigo").val();
		var produto = $("#produto").val();
		var edicaoProduto = $("#edicaoProduto").val();
		var descontoProduto = $("#descontoProduto").val();
		var dataAlteracaoProduto = $("#dataAlteracaoProduto").val();
		var usuarioProduto = $("#usuarioProduto").val()
		
		$.postJSON("<c:url value='/administracao/tipoDescontoCota/novoDescontoProduto'/>",
				   "codigo="+codigo+
				   "&produto="+ produto +
				   "&edicaoProduto="+ edicaoProduto +
				   "&descontoProduto="+ descontoProduto +
				   "&dataAlteracaoProduto="+ dataAlteracaoProduto +
				   "&usuarioProduto="+ usuarioProduto,
				   function(result) {
			           fecharDialogs();
					   var tipoMensagem = result.tipoMensagem;
					   var listaMensagens = result.listaMensagens;
					   if (tipoMensagem && listaMensagens) {
					       exibirMensagem(tipoMensagem, listaMensagens);
				       }
	                   pesquisar();
	               },
				   null,
				   true);
		$(".tiposDescEspecificoGrid").flexReload();		
	}
	
	function popup_geral() {
		
		 limparTelaCadastro();
	
		$( "#dialog-geral" ).dialog({
			resizable: false,
			height:230,
			width:400,
			modal: true,
			buttons: {
				"Confirmar": function() {								
					novoDescontoGeral();
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	};

	function popup_especifico() {		
		
		limparTelaCadastro();
	
		$( "#dialog-especifico" ).dialog({
			resizable: false,
			height:300,
			width:400,
			modal: true,
			buttons: {
				"Confirmar": function() {
					novoDescontoEspecifico();
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});	
		      
	};
	function popup_produto() {
		
		limparTelaCadastro();
	
		$( "#dialog-produto" ).dialog({
			resizable: false,
			height:320,
			width:400,
			modal: true,
			buttons: {
				"Confirmar": function() {
					novoDescontoProduto();
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});	
		      
	};
	function mostra_geral(){
		$( '#tpoGeral' ).show();
		$( '#tpoEspecifico' ).hide();
		$( '#tpoProduto' ).hide();
		$( '.especifico' ).hide();
		$( '.produto' ).hide();
		}
	function mostra_especifico(){
		$( '#tpoGeral' ).hide();
		$( '#tpoEspecifico' ).show();
		$( '.especifico' ).show();
		$( '#tpoProduto' ).hide();
		$( '.produto' ).hide();
		}
	function mostra_produto(){
		$( '#tpoGeral' ).hide();
		$( '#tpoEspecifico' ).hide();
		$( '.especifico' ).hide();
		$( '#tpoProduto' ).show();
		$( '.produto' ).show();
		}
	
	
	function executarPreProcessamento(resultado) {				
		
		if (resultado.mensagens) {

			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			
			$(".grids").show();

			return resultado;
		}

		//var tipoSelecionado = verificarTipoDescontoSelecionado();
		$.each(resultado.rows, function(index, row) {					

			var linkExcluir = '<a href="javascript:;" onclick="exibirDialogExclusao(' + row.cell.id + ');" style="cursor:pointer">' +
							   	 '<img title="Excluir Desconto" src="${pageContext.request.contextPath}/images/ico_excluir.gif" hspace="5" border="0px" />' +
							   '</a>';
			
			row.cell.acao = linkExcluir;
		});
		
		$(".grids").show();
		
		return resultado;
	}

	function exibirDialogExclusao(idDesconto, tipoSelecionado){		
		$("#dialog-excluirCota" ).dialog({
			resizable: false,
			height:'auto',
			width:250,
			modal: true,
			buttons: {
				"Confirmar": function() {
					excluirDesconto(idDesconto, tipoDesconto);
					$( this ).dialog( "close" );
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	}
	
	function excluirDesconto(idDesconto, tipoDesconto){		
		$.postJSON(contextPath + "/administracao/tipoDescontoCota/excluirDesconto",
				"idDesconto="+idDesconto+"&tipoDesconto="+tipoDesconto, 
				function(){
					pesquisar();
				}
		);
	}
	
	
	
	
	
	function limparTelaCadastro() {
		//Tela Geral
		$("#descontoGeral").val("");
		//Tela Especifico
		$("#cotaEspecifica").val("");
		$("#nomeEspecifico").val("");
		$("#descontoEspecifico").val("");
		//Tela Produto
		$("#codigo").val("");
		$("#produto").val("");
		$("#edicaoProduto").val("");
		$("#descontoProduto").val("");
		
	}
	
	function fecharDialogs() {
		$( "#dialog-geral" ).dialog( "close" );
		$( "#dialog-especifico" ).dialog( "close" );
		$( "#dialog-produto" ).dialog( "close" );
	}

</script>
<style type="text/css">
#dialog-box, .produto, .especifico{display:none;}
#dialog-box fieldset{width:570px!important;}
</style>
</head>

<body>
	<div id="dialog-excluirCota" title="Atenção" style="display:none">
		<p>Confirmar exclusão Desconto ?</p>
	</div>

	<div id="dialog-geral" title="Novo Tipo de Desconto Geral" style="display:none;">    
	    <table width="350" border="0" cellpadding="2" cellspacing="1" class="filtro">
	            <tr>
	              <td width="100">Desconto %:</td>
	              <td width="239"><input type="text" name="descontoGeral" id="descontoGeral" style="width:100px;"/></td>
	            </tr>
	            <tr>
	              <td>Data Alteração:</td>
	              <td><input type="text" name="dataAlteracaoGeral" id="dataAlteracaoGeral" style="width:100px;" disabled="disabled" value="${dataAtual}" /></td>
	            </tr>
	            <tr>
	              <td>Usuário:</td>
	              <td><input type="text" name="textfield24" id="textfield24" style="width:230px;" disabled="disabled" value="Junior Fonseca" /></td>
	            </tr>
	  </table>         
	
	</div>


	<div id="dialog-especifico" title="Novo Tipo de Desconto Especifico" style="display:none;">    
	    <table width="350" border="0" cellpadding="2" cellspacing="1" class="filtro">
	            <tr>
	              <td width="100">Cota:</td>
	              <td width="239"><input type="text" name="cotaEspecifica" id="cotaEspecifica" onchange="cota.pesquisarPorNumeroCota('#cotaEspecifica', '#nomeEspecifico');"  style="width:100px; float:left; margin-right:5px;" /></td>
	            </tr>
	            <tr>
	              <td>Nome:</td>
	              <td><input type="text" name="nomeEspecifico" id="nomeEspecifico" style="width:230px;" /></td>
	            </tr>
	            <tr>
	              <td>Desconto %:</td>
	              <td><input type="text" name="descontoEspecifico" id="descontoEspecifico" style="width:100px;" /></td>
	            </tr>
	            <tr>
	              <td>Data Alteração:</td>
	              <td><input type="text" name="dataAlteracaoEspecifico" id="dataAlteracaoEspecifico" style="width:100px;" disabled="disabled" value="${dataAtual}"/></td>
	            </tr>
	            <tr>
	              <td>Usuário:</td>
	              <td><input type="text" name="usuarioEspecifico" id="usuarioEspecifico" style="width:230px;" disabled="disabled" value="Junior Fonseca"/></td>
	            </tr>
	          </table>       
	
	</div>


	<div id="dialog-produto" title="Novo Tipo de Desconto Produto" style="display:none;">    
	    <table width="350" border="0" cellpadding="2" cellspacing="1" class="filtro">
	            <tr>
	              <td width="100">Código:</td>
	              <td width="239"><input type="text" name="textfield22" id="codigo"  style="width:100px; float:left; margin-right:5px;" onblur="buscarNomeProduto();" /><span class="classPesquisar"><a href="javascript:;">&nbsp;</a></span></td>
	            </tr>
	            <tr>
	              <td>Produto:</td>
	              <td><input type="text" name="textfield4" id="produto" style="width:230px;" onkeyup="pesquisarPorNomeProduto();" /></td>
	            </tr>	            
	            <tr>
	              <td>Edição:</td>
	              <td><input type="text" name="textfield5" id="edicaoProduto" style="width:100px;"/></td>
	            </tr>
	            <tr>
	              <td>Desconto %:</td>
	              <td><input type="text" name="textfield2" id="descontoProduto" style="width:100px;"/></td>
	            </tr>
	            <tr>
	              <td>Data Alteração:</td>
	              <td><input type="text" name="textfield3" id="dataAlteracaoProduto" style="width:100px;" disabled="disabled" value="${dataAtual}"/></td>
	            </tr>
	            <tr>
	              <td>Usuário:</td>
	              <td><input type="text" name="textfield" id="usuarioProduto" style="width:230px;" disabled="disabled" value="Junior Fonseca"/></td>
	            </tr>
	          </table>       
	
	</div>

<div class="corpo">   
    <br clear="all"/>
    <br />
   
    <div class="container">
    
     <div id="effect" style="padding: 0 .7em;" class="ui-state-highlight ui-corner-all"> 
				<p><span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
				<b>Tipo de Desconto < evento > com < status >.</b></p>
	</div>
    	
      <fieldset class="classFieldset">
   	    <legend> Pesquisar Tipo de Desconto Cota</legend>
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
              <td width="20"><input type="radio" name="radio" id="radioGeral" value="radio" onclick="mostra_geral();" /></td>
                <td width="47">Geral</td>
                <td width="20"><input type="radio" name="radio" id="radioEspecifico" value="radio" onclick="mostra_especifico();"  /></td>
                <td width="65">Específico</td>
                <td width="20"><input type="radio" name="radio" id="radioProduto" value="radio" onclick="mostra_produto();"  /></td>
                <td width="48">Produto</td>
                <td width="585">
                <div class="especifico">
                <label style="width:auto!important;">Cota:</label>
                <input name="cotaDaPesquisa" id="cotaDaPesquisa" type="text" style="width:80px; float:left;" onchange="cota.pesquisarPorNumeroCota('#cotaDaPesquisa', '#nomeDaCotaDaPesquisa');" />
                <label style="width:auto!important;">Nome:</label>
                <input name="nomeDaCotaDaPesquisa" id="nomeDaCotaDaPesquisa" type="text" style="width:160px; float:left;" />
                </div>
                
                <div class="produto">
                <label style="width:auto!important;">Código:</label>
                <input name="codigoPesquisa" id="codigoPesquisa" type="text" style="width:80px; float:left;" onblur="buscarNomeProduto();" />
                <label style="width:auto!important;">Produto:</label>
                <input name="produtoPesquisa" id="produtoPesquisa" type="text" style="width:160px; float:left;" />
                </div>
                </td>
              <td width="104"><span class="bt_pesquisar"><a href="javascript:;" onclick="pesquisar();">Pesquisar</a></span></td>
            </tr>
          </table>

      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
      <div class="grids" style="display:none;">
      <fieldset class="classFieldset" id="tpoGeral" style="display:none;">
       	  <legend>Tipos de Desconto Geral</legend>
        
        	<table class="tiposDescGeralGrid"></table>
            
       		<span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;">
       			<a href="${pageContext.request.contextPath}/administracao/tipoDescontoCota/exportar?fileType=XLS&tipoDesconto=geral">
					<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
					Arquivo
				</a>
       		</span>
             <span class="bt_novos" title="Imprimir">
             	<a href="${pageContext.request.contextPath}/administracao/tipoDescontoCota/exportar?fileType=PDF&tipoDesconto=geral">
					<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />
					Imprimir
				</a>
             </span>
           <span class="bt_novos" title="Novo"><a href="javascript:;" onclick="popup_geral();"><img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"/>Novo</a></span>
   
      </fieldset>
      
      
      <fieldset class="classFieldset" id="tpoEspecifico" style="display:none;">
       	  <legend>Tipos de Desconto Específico</legend>
        
        	<table class="tiposDescEspecificoGrid"></table>
       
			<span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;">
       			<a href="${pageContext.request.contextPath}/administracao/tipoDescontoCota/exportar?fileType=XLS&tipoDesconto=especifico">
					<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
					Arquivo
				</a>
       		</span>
             <span class="bt_novos" title="Imprimir">
             	<a href="${pageContext.request.contextPath}/administracao/tipoDescontoCota/exportar?fileType=PDF&tipoDesconto=especifico">
					<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />
					Imprimir
				</a>
             </span>
           <span class="bt_novos" title="Novo"><a href="javascript:;" onclick="popup_especifico();"><img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"/>Novo</a></span>
   
      </fieldset>
      
      
      <fieldset class="classFieldset" id="tpoProduto" style="display:none;">
       	  <legend>Tipos de Desconto Produto</legend>
        
       	<table class="tiposDescProdutoGrid"></table>
       
			<span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;">
       			<a href="${pageContext.request.contextPath}/administracao/tipoDescontoCota/exportar?fileType=XLS&tipoDesconto=produto">
					<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
					Arquivo
				</a>
       		</span>
             <span class="bt_novos" title="Imprimir">
             	<a href="${pageContext.request.contextPath}/administracao/tipoDescontoCota/exportar?fileType=PDF&tipoDesconto=produto">
					<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />
					Imprimir
				</a>
             </span>
           <span class="bt_novos" title="Novo"><a href="javascript:;" onclick="popup_produto();"><img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"/>Novo</a></span>
   
      </fieldset>
    </div>
      <div class="linha_separa_fields">&nbsp;</div>
       

        

    
    </div>
</div> 

<script>
	$(".tiposDescGeralGrid").flexigrid({
		preProcess: executarPreProcessamento,
			dataType : 'json',
			colModel : [ {
				display : '',
				name : 'sequencial',
				width : 60,
				sortable : true,
				align : 'center'
			},{
				display : 'Desconto %',
				name : 'desconto',
				width : 120,
				sortable : true,
				align : 'center'
			}, {
				display : 'Data Alteração',
				name : 'dtAlteracao',
				width : 150,
				sortable : true,
				align : 'center'
			}, {
				display : 'Usuário',
				name : 'usuario',
				width : 502,
				sortable : true,
				align : 'left'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 30,
				sortable : true,
				align : 'center'
			}],
			sortname : "sequencial",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 255
		});
		
		
		$(".tiposDescEspecificoGrid").flexigrid({
			preProcess: executarPreProcessamento,
			dataType : 'json',
			colModel : [ {
				display : 'Cota',
				name : 'numeroCota',
				width : 60,
				sortable : true,
				align : 'left'
			},{
				display : 'Nome',
				name : 'nomeCota',
				width : 350,
				sortable : true,
				align : 'left'
			}, {
				display : 'Desconto %',
				name : 'desconto',
				width : 150,
				sortable : true,
				align : 'center'
			}, {
				display : 'Data Alteração',
				name : 'dataAlteracao',
				width : 120,
				sortable : true,
				align : 'center'
			}, {
				display : 'Usuário',
				name : 'usuario',
				width : 150,
				sortable : true,
				align : 'left'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 30,
				sortable : true,
				align : 'center'
			}],
			sortname : "cota",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 255
		});
		
		$(".tiposDescProdutoGrid").flexigrid({
			preProcess: executarPreProcessamento,
			dataType : 'json',
			colModel : [ {
				display : 'Código',
				name : 'codigo',
				width : 70,
				sortable : true,
				align : 'left'
			},{
				display : 'Produto',
				name : 'produto',
				width : 228,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'edicao',
				width : 100,
				sortable : true,
				align : 'center'
			}, {
				display : 'Desconto %',
				name : 'desconto',
				width : 150,
				sortable : true,
				align : 'center'
			}, {
				display : 'Data Alteração',
				name : 'dtAlteracao',
				width : 120,
				sortable : true,
				align : 'center'			
			}, {
				display : 'Usuário',
				name : 'usuario',
				width : 150,
				sortable : true,
				align : 'left'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 30,
				sortable : true,
				align : 'center'
			}],
			sortname : "codigo",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 255
		});
		
		
</script>
</body>
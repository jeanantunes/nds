<head>
<script language="javascript" type="text/javascript">

	function novoFornecedor(isEdicao) {
		
		if (!isEdicao) {
			
			limparCamposModal();
		}
	
		$( "#dialogNovoFornecedor" ).dialog({
			resizable: false,
			height:610,
			width:840,
			modal: true,
			buttons: {
				"Confirmar": function() {
					cadastrarFornecedor();
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	};
	
	function limparCamposModal() {
		$("#idFornecedor").val("");
		$("#codigoInterface").val("");
		$("#razaoSocial").val("");
		$("#nomeFantasia").val("");
		$("#cnpj").val("");
		$("#inscricaoEstadual").val("");
		$("#responsavel").val("");
		$("#email").val("");
		$("#tipoFornecedor").val("");
		$("#possuiContrato").val("");
		$("#validadeContrato").val("");
	}
	
	function cadastrarFornecedor() {
		
		var formData = $("#formNovoFornecedor").serializeArray();
		
		$.postJSON(
			'<c:url value="/cadastro/fornecedor/cadastrarFornecedor" />',
			formData,
			function(result) {
				exibirMensagem(
					result.tipoMensagem, 
					result.listaMensagens
				);
				
				pesquisarFornecedores();
				
				$( this ).dialog( "close" );
			},
			function(result) {
				exibirMensagemDialog(
					result.mensagens.tipoMensagem, 
					result.mensagens.listaMensagens
				);
			},
			true
		);
	}
	
	function pesquisarFornecedores() {
		
		var formData = $("#formularioPesquisaFornecedores").serializeArray();

		$(".fornecedoresGrid").flexOptions({
			url : '<c:url value="/cadastro/fornecedor/pesquisarFornecedores" />',
			preProcess : processarResultadoFornecedores,
			params : formData
		});
		
		$(".fornecedoresGrid").flexReload();

		$(".grids").show();
	}

	function processarResultadoFornecedores(data) {
		
		if (data.mensagens) {

			exibirMensagem(
				data.mensagens.tipoMensagem,
				data.mensagens.listaMensagens
			);

			$(".grids").hide();

			return;
		}

		data = data.tableModel;

		var i;

		for (i = 0; i < data.rows.length; i++) {

			data.rows[i].cell.acao = getActionFornecedor(data.rows[i].id);
		}

		if (data.rows.length < 0) {

			$(".grids").hide();

			return;
		} 

		$(".grids").show();

		return data;
	}

	function editarFornecedor(idFornecedor) {

		$.postJSON(
			'<c:url value="/cadastro/fornecedor/editarFornecedor" />',
			{'idFornecedor': idFornecedor},
			function(result) {
				
				$("#idFornecedor").val(result.idFornecedor);
				$("#codigoInterface").val(result.codigoInterface);
				$("#razaoSocial").val(result.razaoSocial);
				$("#nomeFantasia").val(result.nomeFantasia);
				$("#cnpj").val(result.cnpj);
				$("#inscricaoEstadual").val(result.inscricaoEstadual);
				$("#responsavel").val(result.responsavel);
				$("#email").val(result.email);
				$("#tipoFornecedor").val(result.tipoFornecedor);
				$("#possuiContrato").val(result.possuiContrato);
				$("#validadeContrato").val(result.validadeContrato);
				
				novoFornecedor(true);
			},
			function(result) {
				exibirMensagem(
					result.mensagens.tipoMensagem, 
					result.mensagens.listaMensagens
				);
			}
		);
	}
	
	function getActionFornecedor(idFornecedor) {

		return '<a href="javascript:;" onclick="editarFornecedor('
				+ idFornecedor
				+ ')" '
				+ ' style="cursor:pointer;border:0px;margin:5px" title="Editar fornecedor">'
				+ '<img src="${pageContext.request.contextPath}/images/ico_editar.gif" border="0px"/>'
				+ '</a>'
				+ '<a href="javascript:;" onclick="confirmarExclusaoFornecedor('
				+ idFornecedor
				+ ')" '
				+ ' style="cursor:pointer;border:0px;margin:5px" title="Excluir fornecedor">'
				+ '<img src="${pageContext.request.contextPath}/images/ico_excluir.gif" border="0px"/>'
				+ '</a>';
	}

	function confirmarExclusaoFornecedor(idFornecedor) {
	
		$( "#dialog-excluir" ).dialog({
			resizable: false,
			height:170,
			width:380,
			modal: true,
			buttons: {
				"Confirmar": function() {
					excluirFornecedor(idFornecedor);
					$( this ).dialog( "close" );
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	};
	
	function excluirFornecedor(idFornecedor) {
	
		$.postJSON(
			'<c:url value="/cadastro/fornecedor/excluirFornecedor" />',
			{'idFornecedor': idFornecedor},
			function(result) {
				exibirMensagem(
					result.tipoMensagem, 
					result.listaMensagens
				);
				
				pesquisarFornecedores();
			},
			function(result) {
				exibirMensagem(
					result.mensagens.tipoMensagem, 
					result.mensagens.listaMensagens
				);
			}
		);
	}
	
	$(function() {
		
		$(".fornecedoresGrid").flexigrid({
			dataType : 'json',
			colModel : [  {
				display : 'C�digo',
				name : 'codigoInterface',
				width : 60,
				sortable : true,
				align : 'left'
			},{
				display : 'Razão Social',
				name : 'razaoSocial',
				width : 160,
				sortable : true,
				align : 'left'
			}, {
				display : 'CNPJ',
				name : 'cnpj',
				width : 120,
				sortable : true,
				align : 'left'
			}, {
				display : 'Responsável',
				name : 'responsavel',
				width : 130,
				sortable : true,
				align : 'left'
			}, {
				display : 'Telefone',
				name : 'telefone',
				width : 100,
				sortable : true,
				align : 'left'
			}, {
				display : 'E-Mail',
				name : 'email',
				width : 220,
				sortable : true,
				align : 'left'
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
			singleSelect: true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 255
		});
		
		$("#dataValidade").datepicker({
			showOn : "button",
			buttonImage: "${pageContext.request.contextPath}/images/calendar.gif",
			buttonImageOnly : true,
			dateFormat: 'dd/mm/yy'
		});
		
		$( "#dataValidade" ).mask("99/99/9999");
		
		$( "#tabFornecedores" ).tabs();
	});

	function mostraValidade(){
		
		if ($(".validade").css("display") == "none") {

			$( '.validade' ).fadeIn( "slow" );
		
		} else {

			$( '.validade' ).val("");

			$( '.validade' ).fadeOut( "slow" );
		}

	}
</script>
<style>
	.diasFunc label, .finceiro label{ vertical-align:super;}
	.validade{display:none;}
</style>
</head>

<body>

	<div id="dialog-excluir" title="Excluir Fornecedor">
		<p>Confirma a exclusão deste Fornecedor?</p>
	</div>
	
	<div class="container">
    
	<div id="effect" style="padding: 0 .7em;"
		 class="ui-state-highlight ui-corner-all">
		<p>
			<span style="float: left; margin-right: .3em;"
				class="ui-icon ui-icon-info">
			</span> 
		</p>
	</div>

      <fieldset class="classFieldset">
   	    <legend> Pesquisar Fornecedor</legend>
   	    <form id="formularioPesquisaFornecedores">
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
              <td width="81">Razão Social:</td>
              <td colspan="3">
              	<input type="text" name="filtroConsultaFornecedor.razaoSocial" id="textfield2" style="width:180px;"/>
              </td>
                <td width="34">CNPJ:</td>
                <td width="138"><input type="text" name="filtroConsultaFornecedor.cnpj" id="textfield" style="width:130px;"/></td>
                <td width="93">Nome Fantasia:</td>
                <td width="223"><input type="text" name="filtroConsultaFornecedor.nomeFantasia" id="textfield" style="width:150px;"/></td>
              <td width="148"><span class="bt_pesquisar"><a href="javascript:;" onclick="pesquisarFornecedores();">Pesquisar</a></span></td>
            </tr>
          </table>
		</form>
      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
      <fieldset class="classFieldset">
       	  <legend>Fornecedores Cadastrados</legend>
        <div class="grids" style="display:none;">
        	<table class="fornecedoresGrid"></table>
        </div>
        
        <span class="bt_novos" title="Novo">
           	<a href="javascript:;" onclick="novoFornecedor();">
           		<img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"/>
           		Novo
           	</a>
        </span>

      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>

    </div>
    
	<jsp:include page="novoFornecedor.jsp" />
	
</body>
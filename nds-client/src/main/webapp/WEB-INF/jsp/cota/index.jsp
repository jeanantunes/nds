<head>
<title>NDS - Novo Distrib</title>
<script language="javascript" type="text/javascript">
	
	function popup_cnpj() {
		
		$( "#dialog-cnpj" ).dialog({
			resizable: false,
			height:640,
			width:840,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					$("#effect").show("highlight", {}, 1000, callback);
					$(".grids").show();
					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	}
	
	function limparFormsTabs() {
		
		limparFormEndereco();
	}

	function popup_cpf(idCota) {

		if (idCota) {

			$.ajax({
				type: 'POST',
				url: '<c:url value="/cadastro/cota/editarCota" />',
				data: { "idCota": idCota }
			});
		
		} else {
			
			$.ajax({
				type: 'POST',
				url: '<c:url value="/cadastro/cota/novaCota" />'
			});
		}

		$( "#dialog-cpf" ).dialog({
			resizable: false,
			height:640,
			width:840,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					$("#effect").show("highlight", {}, 1000, callback);
					$(".grids").show();
					
				},
				"Cancelar": function() {
					$("#tabpf").tabs('select', 0);
					limparFormsTabs();
					$( this ).dialog( "close" );
				}
			}
		});

		$(".ui-dialog-titlebar-close").click(function() {

			$("#tabpf").tabs('select', 0);
			limparFormsTabs();
		});
	}
	
	$(function() {
		
		$( "#tabpf" ).tabs();
		
		$( "#tabpj" ).tabs();
	});
	
</script>
<style>

	.diasFunc label, .finceiro label, .materialPromocional label{ vertical-align:super;}
	.complementar label, .distribuicao label{ vertical-align:super; margin-right:5px; margin-left:5px;}

</style>

</head>

<body>

	<fieldset class="classFieldset">
	
		<legend>Cotas Cadastradas</legend>
	
		<div class="grids" style="display: none;">
			<table class="pessoasGrid"></table>
		</div>

		<span class="bt_novos" title="Novo">
			<a href="javascript:;" onclick="popup_cpf();">
				<img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0" />CPF
			</a>
		</span>
		
		<span class="bt_novos" title="Novo">
			<a href="javascript:;" onclick="popup_cpf(1);">
				<img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0" />Editar PF
			</a>
		</span>

	</fieldset>
	
	<div class="linha_separa_fields">&nbsp;</div>

	<div id="dialog-cpf" title="Nova Cota">

		<div id="tabpf">
			<ul>
			<li><a href="#tabpf-1">Dados Cadastrais</a></li>
			<li><a href="#tabpf-2" onclick="popularGridEnderecos()">Endereços</a></li>			
			<li><a href="#tabpf-3">Telefones</a></li>
			<li><a href="#tabpf-4">PDV</a></li>
			<li><a href="#tabpf-5">Financeiro</a></li>
			<li><a href="#tabpf-6">Bancos</a></li>
			<li><a href="#tabpf-7">Distribuição</a></li>
			</ul>
		
			<div id="tabpf-1"> </div>
			
			<div id="tabpf-2">
			
				<jsp:include page="../endereco/index.jsp"></jsp:include>
	
			</div>
			
			<div id="tabpf-3"> </div>
			<div id="tabpf-4"> </div>
			<div id="tabpf-5"> </div>
			<div id="tabpf-6"> </div>
			<div id="tabpf-7"> </div>
		
		</div>
		
	</div>
	
</body>
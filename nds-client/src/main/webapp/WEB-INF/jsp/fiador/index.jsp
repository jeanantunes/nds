<head>
	
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
	
	<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.price_format.1.7.js"></script>
	
	<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/scripts/fiador.js"></script>
		
		
	<script language="javascript" type="text/javascript">
		$(function(){
			fiadorController.init();
		});
	</script>	
	<style>
		.diasFunc label, .finceiro label{ vertical-align:super;}
	</style>
</head>

<body>
	
	<div id="fiadorController-dialog-excluir-fiador" class="dialog-excluir-fiador" title="Fiadores" style="display: none;">
		<p>Confirma esta Exclusão?</p>
	</div>
	
	<div id="fiadorController-dialog-cancelar-cadastro-fiador" title="Fiadores" style="display: none;">
		<p>Dados não salvos serão perdidos. Confirma o cancelamento?</p>
	</div>
	<div id="fiadorController-dialog-fiador" title="Novo Fiador" style="display: none;">
	
		<jsp:include page="../messagesDialog.jsp" />
	
		<div id="fiadorController-tabs">
			<ul>
				<li><a href="#fiadorController-tab-1">Dados Cadastrais</a></li>
				<li id="fiadorController-tabSocio"><a href="#fiadorController-tab-2" onclick="$('.fiadorController-trSocioPrincipal').show();fiadorController.carregarSocios();" >Sócios</a></li>
	            <li><a href="#fiadorController-tab-3" onclick="ENDERECO_FIADOR.popularGridEnderecos();">Endereços</a></li>
	            <li><a href="#fiadorController-tab-4" onclick="FIADOR.carregarTelefones();">Telefones</a></li>
	            <li><a href="#fiadorController-tab-5" onclick="fiadorController.carregarGarantias();">Garantia</a></li>
				<li><a href="#fiadorController-tab-6" onclick="fiadorController.carregarCotasAssociadas();">Cotas Associadas</a></li>
			</ul>
			
	        <div id="fiadorController-tab-1">
	        	<div id="fiadorController-cadastroCnpj" style="display: none;">
					<jsp:include page="dadosCadastraisCnpj.jsp"></jsp:include>
				</div>
				
				<div id="fiadorController-cadastroCpf" style="display: none;">
					<jsp:include page="dadosCadastraisCpf.jsp">
						<jsp:param value="fiadorController-" name="prefix"/>
					</jsp:include>
				</div>
	        </div>
	        
	        <div id="fiadorController-tab-2">
	        	<div id='fiadorController-tab-socios'>
				<jsp:include page="socios.jsp"></jsp:include>
				</div>
			</div>
	        
			<div id="fiadorController-tab-3">
				<jsp:include page="../endereco/index.jsp">
					<jsp:param value="ENDERECO_FIADOR" name="telaEndereco"/>
				</jsp:include>
	    	</div>
	    	
	        <div id="fiadorController-tab-4">
	        	<jsp:include page="../telefone/index.jsp">
	        		<jsp:param value="FIADOR" name="tela"/>
	        	</jsp:include>
			</div>
			
			<div id="fiadorController-tab-5">
				<jsp:include page="garantias.jsp"></jsp:include>
	    	</div>
	    
			<div id="fiadorController-tab-6">
				<jsp:include page="cotasAssociadas.jsp"></jsp:include>
	    	</div>
		</div>
	</div>
	<div class="areaBts">
		<div class="area">
            <span class="bt_novos" title="Novo">
            	<a href="javascript:;" onclick='$(".inicioAtividadeNovo").show();$(".inicioAtividadeEdicao").hide();popupCadastroFiadorCPF();'><img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"/>CPF</a>
            </span>
        	
        	<span class="bt_novos" title="Novo">
        		<a href="javascript:;" onclick="popupCadastroFiadorCNPJ();"><img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"/>CNPJ</a>
        	</span>
		</div>
	</div>
	<div class="linha_separa_fields">&nbsp;</div>
	<fieldset class="fieldFiltro">
   		<legend> Pesquisar Fiador</legend>
        	<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
				<tr>
					<td width="41">Nome:</td>
              		<td colspan="3">
              			<input type="text" name="textfield2" id="fiadorController-nomeFiadorPesquisa" style="width:180px;" maxlength="255"/>
              		</td>
                	<td width="74">CPF/CNPJ:</td>
                	<td width="139">
                		<input type="text" name="textfield" id="fiadorController-cpfCnpjFiadorPesquisa" style="width:130px;" maxlength="255"/>
                	</td>
              		<td width="480">
              			<span class="bt_pesquisar"><a href="javascript:fiadorController.exibirGridFiadoresCadastrados();">Pesquisar</a></span>
              		</td>
            	</tr>
          	</table>
	</fieldset>
    
    <div class="linha_separa_fields">&nbsp;</div>
    
    <div class="grids" style="display:none;">
	    <fieldset class="fieldGrid">
			<legend>Fiadores Cadastrados</legend>
	        	<div class="fiadorController-grids" style="display:none;" id="fiadorController-gridFiadoresCadastrados">
	        		<table class="fiadorController-pessoasGrid"></table>
	        	</div>
		</fieldset>
	</div>
</body>

            <span class="bt_novos" title="Novo">
            	<a href="javascript:;" onclick='$(".fiadorController-inicioAtividadeNovo").show();$(".fiadorController-inicioAtividadeEdicao").hide();fiadorController.popupCadastroFiadorCPF();'><img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"/>CPF</a>
            </span>
        	
        	<span class="bt_novos" title="Novo">
        		<a href="javascript:;" onclick="fiadorController.popupCadastroFiadorCNPJ();"><img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"/>CNPJ</a>
        	</span>
	</fieldset>
	
	<div class="linha_separa_fields">&nbsp;</div>
</body>
<script type="text/javascript">
$(function(){
	ENDERECO_FIADOR.init(fiadorController.workspace);
	FIADOR.init(fiadorController.workspace);
});
</script>
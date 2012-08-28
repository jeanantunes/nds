<form id="form-dialogNovoFornecedor">
<div id="fornecedorController-dialogNovoFornecedor" title="Novo Fornecedor" style="display:none">

	<jsp:include value="fornecedorController-dialogNovoFornecedor" page="../messagesDialog.jsp" />

	<div id="fornecedorController-tabFornecedores">
	
		<ul>
			<li><a href="#fornecedorController-dadosCadastrais">Dados Cadastrais</a></li>
			
			<li><a href="#fornecedorController-manutencaoEnderecos" onclick="ENDERECO_FORNECEDOR.popularGridEnderecos()">Endereços</a></li>
			
			<li><a href="#fornecedorController-manutencaoTelefones" onclick="FORNECEDOR.carregarTelefones()">Telefones</a></li>
		</ul>
	
		<div id="fornecedorController-dadosCadastrais">
			<div class="linha_separa_fields">&nbsp;</div>
			<fieldset style="width:770px; margin-left:10px; margin-bottom:5px;">
				<legend>Dados Cadastrais</legend>
				<jsp:include page="dadosCadastrais.jsp" />
			</fieldset>
			<br clear="all"/>
		</div>
		
		<div id="fornecedorController-manutencaoEnderecos">
			<div class="linha_separa_fields">&nbsp;</div>
			<fieldset style="width:770px; margin-left:10px; margin-bottom:5px;">
				<legend>Endereços</legend>
				<jsp:include page="../endereco/index.jsp">
					<jsp:param value="ENDERECO_FORNECEDOR" name="telaEndereco"/>
					<jsp:param value="fornecedorController-dialogNovoFornecedor" name="message"/>
				</jsp:include>
			</fieldset>
			<br clear="all"/>
		</div>
		
		<div id="fornecedorController-manutencaoTelefones">
			<div class="linha_separa_fields">&nbsp;</div>
			<fieldset style="width:770px; margin-left:10px; margin-bottom:5px;">
				<legend>Telefones</legend>
				<jsp:include page="../telefone/index.jsp">
					<jsp:param value="FORNECEDOR" name="tela"/>
					<jsp:param value="fornecedorController-dialogNovoFornecedor" name="message"/>
				</jsp:include>
			</fieldset>
			<br clear="all"/>
		</div>
	</div>

</div>
</form>

<script type="text/javascript">
$(function(){
	ENDERECO_FORNECEDOR.init(fornecedorController.workspace);
	FORNECEDOR.init(fornecedorController.workspace);
});
</script>

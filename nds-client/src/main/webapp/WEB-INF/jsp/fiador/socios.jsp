<div id="fiadorController-dialog-excluir-socio" title="Socios" style="display: none;">
	<p>Confirma esta Exclusão?</p>
</div>
<fieldset style="margin:5px; width:880px;">
<legend>Cadastrar Sócios</legend>
<jsp:include page="dadosCadastraisCpf.jsp">
	<jsp:param value="fiadorController-socio-" name="prefix"/>
</jsp:include>

<br />
<span class="bt_novos"><a href="javascript:fiadorController.adicionarSocio();" id="fiadorController-btnAddEditarSocio" rel="tipsy" title="Incluir Novo Sócio"><img src="${pageContext.request.contextPath}/images/ico_add.gif" hspace="5" border="0"/></a></span>
</fieldset>

<fieldset style="margin:5px; width:880px;">
	<legend>Sócios Cadastrados</legend>
	
	<table class="fiadorController-sociosGrid"></table>
	</fieldset>
<input type="hidden" id="fiadorController-idSocioEdicao"/>
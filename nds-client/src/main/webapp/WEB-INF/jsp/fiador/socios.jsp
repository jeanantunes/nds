<div id="fiadorController-dialog-excluir-socio" title="Socios" style="display: none;">
	<p>Confirma esta Exclusão?</p>
</div>

<jsp:include page="dadosCadastraisCpf.jsp">
	<jsp:param value="fiadorController-socio-" name="prefix"/>
</jsp:include>

<br />
<span class="bt_add"><a href="javascript:fiadorController.adicionarSocio();" id="fiadorController-btnAddEditarSocio">Incluir Novo</a></span>
<br />
<br />
<br clear="all" />
<strong>Sócios Cadastrados</strong>
<br />
<table class="fiadorController-sociosGrid"></table>

<input type="hidden" id="fiadorController-idSocioEdicao"/>
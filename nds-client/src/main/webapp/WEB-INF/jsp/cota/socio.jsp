
<jsp:include page="./novoSocio.jsp" />

<div id="dialog-excluir-socio" title="Socios" style="display: none;">
	<p>Confirma esta Exclusão?</p>
</div>

<input type="hidden" id="idSocio" value=""/>

<label><strong>Sócios Cadastrados</strong></label>
<table class="sociosPjGrid"></table>

<span class="bt_add">
	<a href="javascript:SOCIO_COTA.inicializarPopup();" id="btnAddSocio">Incluir Novo</a>
</span>

<br/>

<br/>




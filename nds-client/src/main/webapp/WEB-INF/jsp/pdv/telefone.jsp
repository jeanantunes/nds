
<fieldset>
	
	<legend>Telefones</legend>
	
	<jsp:include page="../telefone/index.jsp">
		<jsp:param value="TELEFONE_PDV" name="tela"/>
		<jsp:param value="idModalPDV" name="message"/>
	</jsp:include>
	
</fieldset>

<script type="text/javascript">
$(function(){
	TELEFONE_PDV.init(PDV.workspace);
});
</script>

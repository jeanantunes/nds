
<fieldset>
	
	<legend>Endereços</legend>
	
	<jsp:include page="../endereco/index.jsp">
		<jsp:param value="ENDERECO_PDV" name="telaEndereco"/>
		<jsp:param value="idModalPDV" name="message"/>
	</jsp:include>
	
</fieldset>

<script type="text/javascript">
$(function(){
	ENDERECO_PDV.init(PDV.workspace);
});
</script>





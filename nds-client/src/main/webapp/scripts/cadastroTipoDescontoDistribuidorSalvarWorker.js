self.onmessage = function(event) {
	
	$.postJSON(contextPath +"/financeiro/tipoDescontoCota/novoDescontoGeral",
			"desconto="+descontoGeral + "&" + fornecedores,				   
		   function(result) {
	        
			   if (result.tipoMensagem && result.tipoMensagem !="SUCCESS" && result.listaMensagens) {			      
				   exibirMensagemDialog(result.tipoMensagem, result.listaMensagens, "");
		       }
			   else{
				   exibirMensagem(result.tipoMensagem, result.listaMensagens, "");
				   tipoDescontoController.fecharDialogs();
				   tipoDescontoController.pesquisar();
				   $(".tiposDescGeralGrid",this.workspace).flexReload();
			   }
           },
		   null,
		   true,"idModalDescontoGeral");	
	
}
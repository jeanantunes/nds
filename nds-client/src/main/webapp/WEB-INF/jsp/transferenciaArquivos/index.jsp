<input id="permissaoAlteracao" type="hidden" value="${permissaoAlteracao}">
<head>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.form.js"></script>
<script type="text/javascript" src="scripts/jquery-upload/js/jquery.fileupload.js"></script>
<script type="text/javascript" src="scripts/jquery-upload/js/jquery.iframe-transport.js"></script>
<script type="text/javascript" src="scripts/jquery-upload/js/vendor/jquery.ui.widget.js"></script>
<script type="text/javascript" src="scripts/transferenciaArquivos.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.fileDownload.js"></script>
<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/scripts/usuario/usuario.js"></script>

<script language="javascript" type="text/javascript">

$(function(){
	transferenciaArquivosController.init();
});

</script>

<style type="text/css">
#dialog-novo fieldset, #dialog-detalhes fieldset{ width: 700px!important;}
#conteudo{width: 950px !important;}
.fieldFiltro {width: 930px !important;}

#dragandrophandler{
	border: 2px dashed #92AAB0;
	width: 450px;
	height: 200px;
	color: #92AAB0;
	text-align: center;
	vertical-align: middle;
	padding: 10px 0px 10px 10px; 
	font-size:200%; 
	display: table-cell;
}

.progressBar {
    width: 200px;
    height: 22px;
    border: 1px solid #ddd;
    border-radius: 5px; 
    overflow: hidden;
    display:inline-block;
    margin:0px 10px 5px 5px;
    vertical-align:top;
}
 
.progressBar div {
    height: 100%;
    color: #fff;
    text-align: right;
    line-height: 22px; /* same as #progressBar height if we want text middle aligned */
    width: 0;
    background-color: #0ba1b5; border-radius: 3px; 
}
.statusbar{
    border-top:1px solid #A9CCD1;
    min-height:25px;
    width:700px;
    padding:10px 10px 0px 10px;
    vertical-align:top;
}
.statusbar:nth-child(odd){
    background:#EBEFF0;
}
.filename{
	display:inline-block;
	vertical-align:top;
	width:250px;
}
.filesize{
	display:inline-block;
	vertical-align:top;
	color:#30693D;
	width:100px;
	margin-left:10px;
	margin-right:5px;
}

.abort{
    background-color:#A8352F;
    -moz-border-radius:4px;
    -webkit-border-radius:4px;
    border-radius:4px;display:inline-block;
    color:#fff;
    font-family:arial;font-size:13px;font-weight:normal;
    padding:4px 15px;
    cursor:pointer;
    vertical-align:top
    }

</style>

 
</head>

<body>
<div class="areaBts">
  <div class="area">
    <span class="bt_novos">
      <a href="javascript:;" rel='tipsy' id="configuracaoDiretorios" title='Configurar diret&oacute;rios'>
        <img src="images/ico_editar.gif" alt="Configurar diret&oacute;rios" hspace="5" border="0" />
      </a>
    </span>
  </div>
</div>
<br clear="all" />
<br />
<div class="corpo">
		
		<fieldset class="fieldFiltro" >
			<legend>Transfer&ecirc;ncia de arquivos</legend>
			<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
				<tr style="height: 60px;">
					<td width="150" style="font-size: 13px;">Selecione o diret&oacute;rio</td>
					<td width="500">
						<select name="comboDiretorios" id="comboDiretorios" style="width: 250px;">
							<option>Selecione...</option>
							<c:forEach items="${listaDiretorios}" var="diretorio">
								<option value="${diretorio.key}" label="${diretorio.value}" />
							</c:forEach>
						</select>
					</td>

					<td width="134"></td>
				</tr>
			</table>
			
			<table width="900" border="0" cellpadding="2" cellspacing="1" class="filtro">
				<tr>
					<td width="50">
						<span class="bt_confirmar_novo" title="Exibir downloads">
	           				<a onclick="transferenciaArquivosController.exibirDownload();" href="javascript:;">
	           					<img width="16" border="0" hspace="5" height="16" alt="Confirmar" src="${pageContext.request.contextPath}/images/ico_check.gif">
	           						&nbsp;Download de arquivos
   							</a>
	           			</span>
           			</td>
           			<td width="150">
	           			<span class="bt_confirmar_novo" title="Exibir upload">
	           				<a onclick="transferenciaArquivosController.exibirUpload();" href="javascript:;">
	           					<img width="16" border="0" hspace="5" height="16" alt="Confirmar" src="${pageContext.request.contextPath}/images/ico_check.gif">
	           						&nbsp;Upload de arquivos
           					</a>
	           			</span>
           			</td>
				</tr>
			</table>
			&nbsp;
			
			<div id="div-upload" style="display:none;">
				<div id="dragandrophandler">Arraste seus arquivos pra c&aacute;.</div>
				<br><br>
				<div id="status1"></div>
			</div>
			
		</fieldset>

	</div>

<div id="dialog-diretorios" title="Diret&oacute;rios cadastrados" style="display:none;">
  
  <div id="tabDiv" style="display:none;">
	  <fieldset style="width:600px; margin-top:8px;">
	    <legend>Diret&oacute;rios</legend>
	    <table id="gridDiretorios"></table>
	  </fieldset>
  </div>
  
  
  <div id="botaoDiretorios">
  	<td width="150">
		<span class="bt_confirmar_novo" title="Exibir diret&oacute;rios">
			<a onclick="transferenciaArquivosController.exibirDiretorios();" href="javascript:;">
				<img width="16" border="0" hspace="5" height="16" alt="Confirmar" src="${pageContext.request.contextPath}/images/ico_check.gif">
					&nbsp;Exibir diret&oacute;rios cadastrados
			</a>
		</span>
	</td>
  </div>
  
  <fieldset style="width:600px; margin-top:8px;">
  <legend>Novo diret&oacute;rio</legend>
  	<div>
		<label><b>Nome</b></label>
		<input type="text" id="nomeDiretorio" size="17em" style="float: left; margin-right: 25px; margin-top: 7px;"/>
		<label><b>Pasta</b></label>
		<input type="text" id="nomePasta" placeholder="Ex: nomeNovaPasta/subPasta" size="27em" style="float:left; margin-right: 25px; margin-top: 7px;"/>
		<span class="bt_novos">
			<a onclick="transferenciaArquivosController.salvarNovoDiretorio();" href="javascript:;">
				<img width="16" border="0" hspace="5" height="16" alt="Confirmar" src="${pageContext.request.contextPath}/images/ico_check.gif">
					&nbsp;Cadastrar
			</a>
		</span>
  	</div>
  </fieldset>
  
  <!-- 
  <div>
  	<span class="bt_novos">
			<a onclick="transferenciaArquivosController.exibirDiretorios();" href="javascript:;">
				<img width="16" border="0" hspace="5" height="16" alt="Confirmar" src="${pageContext.request.contextPath}/images/ico_check.gif">
					&nbsp;Exibir 
			</a>
		</span>
  </div>
   -->
  
</div>

<div id="dialog-download" title="Diretorio para download" style="display:none;">
  <fieldset style="width:650px; margin-top:8px;">
    <legend>Diret&oacute;rio para donwload</legend>
    
    <table id="gridDownload"></table>
     	<div id="botoesDownload">
		  	<td width="400">
		  		<tr>
					<span class="bt_confirmar_novo" title="Exibir diret&oacute;rios" style="float: left;">
						<a onclick="transferenciaArquivosController.downloadTodosArquivos();" href="javascript:;">
							<img width="16" border="0" hspace="5" height="16" alt="Confirmar" src="${pageContext.request.contextPath}/images/ico_salvar.gif">
								&nbsp;Baixar todos arquivos
						</a>
					</span>
					<span class="bt_confirmar_novo" title="Exibir diret&oacute;rios" style="float: right;">
						<a onclick="transferenciaArquivosController.excluirTodosArquivos();" href="javascript:;">
							<img width="16" border="0" hspace="5" height="16" alt="Confirmar" src="${pageContext.request.contextPath}/images/ico_bloquear.gif">
								&nbsp;Excluir todos arquivos
						</a>
					</span>
		  		</tr>
			</td>
  		</div>

  </fieldset>
</div>

<div id="dialog-excluir" title="Excluir">
	<p>Confirma a exclusão?</p>
</div>

</body>
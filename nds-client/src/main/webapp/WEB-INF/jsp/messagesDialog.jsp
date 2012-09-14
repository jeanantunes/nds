
<div name="${param.messageDialog}effectSuccessDialog" class="ui-state-default ui-corner-all" 
		style="display: none; position: absolute; width: 95%; z-index: 10;">
	<p>
		<span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
		<b name="${param.messageDialog}textSuccessDialog"></b>
		<span class="ui-state-default ui-corner-all" style="float:right; margin-right: 1px; margin-top: 1px;">
			<a href="javascript:;" onclick="esconde(false, $(this).closest('div'));" class="ui-icon ui-icon-close">&nbsp;</a>
		</span>					
	</p>
</div>
<div name="${param.messageDialog}effectWarningDialog" class="ui-state-highlight ui-corner-all" 
		style="display: none; position: absolute; width: 95%; z-index: 10;">
	<p>
		<span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
		<b name="${param.messageDialog}textWarningDialog"></b>
		<span class="ui-state-default ui-corner-all" style="float:right; margin-right: 1px; margin-top: 1px;">
			<a href="javascript:;" onclick="esconde(false, $(this).closest('div'));" class="ui-icon ui-icon-close">&nbsp;</a>
		</span>					
	</p>
</div>
<div name="${param.messageDialog}effectErrorDialog" class="ui-state-error ui-corner-all" 
		style="display: none; position: absolute; width: 95%; z-index: 10;">
	<p>
		<span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
		<b name="${param.messageDialog}textErrorDialog"></b>
		<span class="ui-state-default ui-corner-all" style="float:right; margin-right: 1px; margin-top: 1px;">
			<a href="javascript:;" onclick="esconde(false, $(this).closest('div'));" class="ui-icon ui-icon-close">&nbsp;</a>
		</span>					
	</p>
</div>
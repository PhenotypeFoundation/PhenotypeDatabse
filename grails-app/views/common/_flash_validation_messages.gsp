<g:if test="${flash.validationErrors}">
	<div class="message errormessage">
		<span class="title">Validation problems</span>
		<ul>
			<g:each var="error" in="${flash.validationErrors}">
				<li>${error.hasProperty('value') ? error.value : error}</li>
			</g:each>
		</ul>
	</div>
</g:if>  

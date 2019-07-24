<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:output
		method="html"
		encoding="UTF-8"
		omit-xml-declaration="yes"
		indent="yes"
		media-type="text/html"
	/>

	<xsl:template match="/">
		<HTML>
			<HEAD>
				<LINK crossorigin="anonymous" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" rel="stylesheet" type="text/css"/>
				<META charset="UTF-8"/>
				<SCRIPT src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></SCRIPT>
				<SCRIPT crossorigin="anonymous" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></SCRIPT>
				<SCRIPT crossorigin="anonymous" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></SCRIPT>
				<TITLE>index.html</TITLE>
				<STYLE>
					.margin-navbar {
					margin-top:72px !important;
					}
			
					.margin-top {
					margin-top:8px !important;
					}
			
					.margin-left-right {
					margin-left: 8px !important;
					margin-right: 8px !important;
					}
				</STYLE>
			</HEAD>
			<BODY class="margin-left-right">

				<DIV class="margin-navbar">
					<FORM action="{parameters/action}" method="post">
						<DIV class="form-group">
							<xsl:for-each select="parameters/tr">
								<INPUT type="text" class="form-control" name="{key}" value="{value}" />
							</xsl:for-each>
						</DIV>
						<INPUT type="submit" class="btn btn-primary" />
					</FORM>
				</DIV>
			</BODY>
		</HTML>

	</xsl:template>
</xsl:stylesheet>
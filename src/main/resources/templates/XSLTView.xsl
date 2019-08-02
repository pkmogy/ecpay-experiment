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
			<NAV class="navbar navbar-expand navbar-dark bg-dark fixed-top">
				<DIV class="container-fluid">
					<DIV class="navbar-header">
						<A class="navbar-brand" href="index.html">金流測試</A>
					</DIV>
					<DIV class="collapse navbar-collapse">
						<UL class="nav navbar-nav mr-auto">
							<LI class="nav-item dropdown">
								<A class="nav-link dropdown-toggle" data-toggle="dropdown" href="#">金流</A>
								<DIV class="dropdown-menu">
									<A  class="dropdown-item" href="http://127.0.0.1">金流訂單生成</A >
									<A  class="dropdown-item" href="http://127.0.0.1/info">金流訂單查詢</A >
								</DIV>
							</LI>
							<LI class="nav-item dropdown">
								<A class="nav-link dropdown-toggle" data-toggle="dropdown" href="#">電子發票</A>
								<DIV class="dropdown-menu">
									<A  class="dropdown-item" href="http://127.0.0.1/index3">電子發票生成</A >
									<A  class="dropdown-item" href="http://127.0.0.1/index3/Issue">電子發票查詢</A >
								</DIV>
							</LI>
							<LI class="nav-item dropdown">
								<A class="nav-link dropdown-toggle" data-toggle="dropdown" href="#">物流</A>
								<DIV class="dropdown-menu">
									<A  class="dropdown-item" href="http://127.0.0.1/index2">物流訂單生成</A >
									<A  class="dropdown-item" href="http://127.0.0.1/index2/info">物流訂單查詢</A >
									<A  class="dropdown-item" href="http://127.0.0.1/index2/print">產生托運單/一段標</A >
									<A  class="dropdown-item" href="http://127.0.0.1/index2/map">電子地圖</A >
								</DIV>
							</LI>
						</UL>
					</DIV>
				</DIV>
			</NAV>
			<BODY class="margin-left-right">
				<DIV class="margin-navbar">
					<FORM action="{parameters/action}" method="post">
						<DIV class="form-group">
							<xsl:for-each select="parameters/tr">
								<LABEL for=""><xsl:value-of select="key"/>:</LABEL>
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
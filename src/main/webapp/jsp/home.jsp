<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Home Page</title>
</head>

<body>

	<div>
		Welcome to home page. Click <a href="./login">here</a> to login.<br/>
		<h3>Hello ${userId}!</h3>
	</div>

</body>
</html>
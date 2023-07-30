<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/tablib.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Register</title>
<link rel="shortcut icon" href="<c:url value='/templates/user/img/iconvideo.png'/>" type="image/x-icon">
<%@include file="/common/head.jsp"%>
</head>
<body>

	<%@include file="/common/header.jsp"%>

	<div class="container-fluid tm-mt-60">
		<div class="row tm-mb-50">
			<div class="col-lg-12 col-12 mb-5 text-center">

				<h2 class="tm-text-primary mb-5">Register</h2>
				<form id="register-form" action="register" method="POST"
					class="tm-contact-form mx-auto">
					<div class="form-group">
						<input type="text" name="username" class="form-control rounded"
							placeholder="Username" required />
					</div>
					<div class="form-group">
						<input type="password" name="password" class="form-control rounded"
							placeholder="Password" required />
					</div>
					<div class="form-group">
						<input type="password" name="cfmPass" class="form-control rounded"
							placeholder="Confirm Password" required />
					</div>
					<div class="form-group">
						<input type="email" name="email" class="form-control rounded"
							placeholder="Email" required />
					</div>
					<div class="form-group">
						<button type="submit" class="btn btn-primary">Send</button>
					</div>
				</form>

			</div>
		</div>
	</div>

	<%@include file="/common/footer.jsp"%>
</body>
</html>
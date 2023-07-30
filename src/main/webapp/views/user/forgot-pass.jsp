<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/tablib.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Forgot Password</title>
<link rel="shortcut icon" href="<c:url value='/templates/user/img/iconvideo.png'/>" type="image/x-icon">
<%@include file="/common/head.jsp"%>
</head>
<body>

	<%@include file="/common/header.jsp"%>

	<div class="container-fluid tm-mt-60">
		<div class="row tm-mb-50">
			<div class="col-lg-12 col-12 mb-5 text-center">
				<h2 class="tm-text-primary mb-5">Forgot Password</h2>
				<div class="tm-contact-form mx-auto">
					<div class="form-group">
						<input type="email" id="email" name="email" class="form-control rounded"
							placeholder="Your email" required />
					</div>			
					<div class="form-group">
						<button id="sendBtn" type="submit" class="btn btn-primary">Send</button>
					</div>
					<!-- <h5 style="color: red;" id="messageReturn"></h5> -->
				</div>
			</div>
		</div>
	</div>

	<%@include file="/common/footer.jsp"%>
	<script>
		$('#sendBtn').click(function () {
			var email = $('#email').val();
			var formData = {'email': email};
			$.ajax({
				url: 'forgotPass',
				type: 'POST',
				data: formData
			}).then(function(data){
				Swal.fire({
					  icon: 'success',
					  title: 'Password has been reset, please check your email',
					  showConfirmButton: false,
					  timer: 3000
					});
				setTimeout(() => {
					window.location.href = 'http://localhost:8080/asmJava4/index';
				}, 4000);
			}).fail(function(error){
				Swal.fire({
					  icon: 'error',
					  title: 'Your infomation is not correct, please try again lates!',
					  showConfirmButton: false,
					  timer: 5000
					});
			});
		});
	</script>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!-- Page Loader -->
<div id="loader-wrapper">
	<div id="loader"></div>

	<div class="loader-section section-left"></div>
	<div class="loader-section section-right"></div>

</div>
<nav class="navbar navbar-expand-lg">
	<div class="container-fluid">
		<a class="navbar-brand" href="<c:url value='/index'/>"> <!-- <i class="fas fa-film mr-2"></i> -->
			<img alt="" src="<c:url value='/templates/user/img/iconvideo.png'/>">
			Online Entertainment
		</a>
		<button class="navbar-toggler" type="button" data-toggle="collapse"
			data-target="#navbarSupportedContent"
			aria-controls="navbarSupportedContent" aria-expanded="false"
			aria-label="Toggle navigation">
			<i class="fas fa-bars"></i>
		</button>
		<div class="collapse navbar-collapse" id="navbarSupportedContent">
			<ul class="navbar-nav ml-auto mb-2 mb-lg-0">
				<c:choose>
					<c:when test="${!empty sessionScope.currentUser}">
						<li class="nav-item">
							<a data-toggle="modal" data-target="#changeInfoModal" class="nav-link nav-link-1 active" aria-current="page">
							Welcome, ${sessionScope.currentUser.username}
							</a>
						</li>
						<li class="nav-item"><a class="nav-link nav-link-1"
							href="favorites">My favorites</a></li>
						<li class="nav-item"><a class="nav-link nav-link-2"
							href="history">History</a></li>
						<li class="nav-item"><a class="nav-link nav-link-2"
							href="logout">Log out</a></li>
					</c:when>
					<c:otherwise>
						<li class="nav-item"><a class="nav-link nav-link-2"
							href="login">Login</a></li>
						<li class="nav-item"><a class="nav-link nav-link-3"
							href="register">Register</a></li>
						<li class="nav-item"><a class="nav-link nav-link-4"
							href="forgotPass">Forgot password</a></li>
					</c:otherwise>
				</c:choose>

			</ul>
		</div>
	</div>
</nav>

<div class="tm-hero d-flex justify-content-center align-items-center"
	data-parallax="scroll"
	data-image-src="<c:url value='/templates/user/img/bgxanh3.jpg'/>">
	<form class="d-flex tm-search-form">
		<input class="form-control tm-search-input rounded-pill" type="search"
			placeholder="Search" aria-label="Search">
		<button
			class="btn btn-outline-success tm-search-btn rounded-pill ml-2"
			type="submit">
			<i class="fas fa-search"></i>
		</button>
	</form>
</div>

<!-- Modal -->
<!-- <div class="modal fade" id="changePassModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="exampleModalLabel">Change Password</h5>
        <a type="button" class="btn btn-outline-danger rounded-circle close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </a>
      </div>
      <div class="modal-body">
        <div class="form-group">
        	<label for="currentPass">Current Password</label>
        	<input type="password" class="form-control mt-2" name="currentPass" id="currentPass" placeholder="Current Password" required="required"/>
        </div>
        <div class="form-group">
        	<label for="newPass">New Password</label>
        	<input type="password" class="form-control mt-2" name="newPass" id="newPass" placeholder="New Password" required="required"/>
        </div>
      </div>
      <div class="modal-footer">
        <button type="button" class="rounded btn btn-outline-danger" data-dismiss="modal">Close</button>
        <button type="button" class="rounded btn btn-outline-success" id="changePassBtn">Save changes</button>
      </div>
    </div>
  </div>
</div> -->
<!-- Modal change Info-->
<div class="modal fade" id="changeInfoModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="exampleModalLabel">Change Info</h5>
        <a type="button" class="btn btn-outline-danger rounded-circle close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </a>
      </div>
      <div class="modal-body">
	        <div class="form-group">
	        	<label for="username">Username</label>
	        	<input type="text" class="form-control mt-2" name="username" id="username" value="${sessionScope.currentUser.username}" required="required" disabled="disabled"/>
	        </div>
	        <div class="form-group">
	        	<label for="currentEmail">Email</label>
	        	<input type="email" class="form-control mt-2" name="currentEmail" id="currentEmail" value="${sessionScope.currentUser.email}" required="required"/>
	        </div>
	        <div class="form-group">
	        	<label for="currentPass">Current Password</label>
	        	<input type="password" class="form-control mt-2" name="currentPass" id="currentPass" placeholder="Current Password" required="required"/>
	        </div>
	        <div class="form-group">
	        	<label for="newPass">New Password</label>
	        	<input type="password" class="form-control mt-2" name="newPass" id="newPass" placeholder="New Password" required="required"/>
	        </div>
      </div>
      <div class="modal-footer">
        <button type="button" class="rounded btn btn-outline-danger" data-dismiss="modal">Close</button>
        <button type="button" class="rounded btn btn-outline-success" id="changePassBtn">Save changes</button>
      </div>
    </div>
  </div>
</div>
<head>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    <title>login</title>
    <meta charset='utf-8'>
    <meta name='viewport' content='width=device-width, initial-scale=1'>
    <script src='https://code.jquery.com/jquery-3.6.0.min.js'></script>
    <script src='https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js'></script>
    <script src='https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js'></script>
    <link rel='stylesheet' href='http://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css'>
    <link rel='stylesheet' href='https://use.fontawesome.com/releases/v5.6.3/css/all.css'/>
    <link rel='stylesheet' href='../res/font.css'/>
</head>
<style>
  footer{
        background-color: rgb(225, 225, 225);
        padding-top: 2rem;
        padding-bottom: 2rem;
    }
</style>
<script>
function login() {
	$('#loginBtn').click(() => {
		if(!$('#userId').val()) {
			$('#errMsg').empty();
			$('#errMsg').text('아이디를 입력해주세요.');
			$('#loginCheckModal').modal();
		}
		if(!$('#userPw').val()) {
			$('#errMsg').empty();
			$('#errMsg').text('비밀번호를 입력해주세요.');
			$('#loginCheckModal').modal();
		}
	$.ajax({
		type:'post',
		url:'/user/login',
		data:{
			userId: $('#userId').val(),
			userPw: $('#userPw').val(),
			idRemember: $('#idRemember:checked').val()
		}
		}).done(result => {		
			location.href='/cinemanetwork';
		}).fail(err => {
			$('#errMsg').empty();
			$('#errMsg').text('아이디 또는 비밀번호를 확인해주세요.');
			$('#loginCheckModal').modal();
		});
	});
	$('#modalYes').click(() => {
		$('#loginCheckModal').modal('hide');
	});
}

$(login);
</script>
<body>
    <header id='header'>
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
                <div class='mx-auto border'><a class="navbar-brand" href='../main.html'>로고 이미지</a></div>
            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
              <span class="navbar-toggler-icon"></span>
            </button>
          
            <div class="collapse navbar-collapse" id="navbarSupportedContent">
              <ul class="navbar-nav mr-auto">
                <li class="nav-item active">
                  <a class="nav-link" href="#">영화</a>
                </li>
                <li class="nav-item dropdown">
                  <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    추천
                  </a>
                  <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                    <a class="dropdown-item" href="#">주제별</a>
                    <div class="dropdown-divider"></div>
                    <a class="dropdown-item" href="#">장르별</a>
                    <div class="dropdown-divider"></div>
                    <a class="dropdown-item" href="#">평점별</a>
                    
                  </div>
                </li>
                <li class="nav-item">
                  <a class="nav-link" href="../community/01.html">커뮤니티</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="../inquiry/01.html">고객센터</a>
                  </li>
                <li class="nav-item">
                    <a class="nav-link" href="../user/01.html">로그인</a>
                  </li>
              </ul>
              <form class="form-inline my-2 my-lg-0">
                <input class="form-control mr-sm-2" type="search" placeholder="Search" aria-label="Search">
                <button class="btn btn-outline-warning my-2 my-sm-0" type="submit"><i class='fas fa-search'></i></button>
              </form>
            </div>
          </nav>
    </header>
    <div class='container'>
        <div class='row'>
            <div class='col' style='float: none; margin:100 auto; text-align: center;'>
                <a class='navbar-brand' href='#'>
                    <div class='border'>
                     	 로고 이미지
                    </div>
                </a>
            </div>
        </div>
        <div class='row'>
            <div class='col' style='text-align: center;'>
                <form method='post' id='loginForm'>
                    <input type='text' name='userId' id='userId' placeholder='아이디'/><br><br>
                    <input type='password' name='userPw' id='userPw' placeholder='비밀번호'/><br><br>
                    <label><input type='checkbox' name='idRemember' id='idRemember' /> ID 기억하기</label><br>
                    <!--<input type='submit' value='로그인'/>-->
                    <button type="button" class="btn btn-secondary" id='loginBtn'>로그인</button>
                </form>
                <a href='../user/03.html' >아이디 찾기</a>
                <a href='../user/05.html' >비밀번호 찾기</a>
            </div>
        </div>
        <div class='row'>
            <div class='col text-center' style='float: none; margin:100 auto;'>
                <button class='btn btn-secondary' type='button' id='btn_register' onclick="location.href='userRegist' ">회원 가입</button>
            </div>
        </div>
    </div>
</body>

<div id='loginCheckModal' class='modal fade' tabindex='-1'>
	<div class='modal-dialog'>
		<div class='modal-content'>
			<div class='modal-header'>
				<button type='button' class='close' data-dismiss='modal'>
					<span>&times;</span>
				</button>
			</div>
			<div class='modal-body'>
				<p id='errMsg'></p>
			</div>
			<div class='modal-footer'>
				<button type='button' class='btn btn-primary' data-dismiss='modal' id='modalYes'>확인</button>
			</div>
		</div>
	</div>
</div>

<footer>
  <div class='row' style='float: none; margin:100 auto; text-align: center;'>
      <div class='col'>
          <p class=''>문의 : <a href="https://https://is.ezenac.co.kr/" data-toggle='tooltip'>https://is.ezenac.co.kr/</a> </p>
          <p class='text'>
              사업장주소 : 경기도 고양시 일산동구 장항동 890-4 마두법조빌딩 9F 10414 호스팅서비스사업자 : 이젠컴퓨터학원(주) 대표번호 : 031-994-8881 개발 문의 : 1577-0000 영화 문의 : 1588-1111</p>

              본 사이트에서 사용되는 이미지 중에는 등록된 개별 저작권 이미지가 포함되어 있습니다.

              개별 저작권 이미지의 경우 씨네마 네트워크(주)는 중개업자로서 저작권 소유자가 아니므로, 거래정보 및 거래 등에 사용하지 않습니다.

              본 사이트의 컨텐츠는 저작권법의 보호를 받는 바 무단 전재, 복사, 배포 등을 금합니다.
          </p>
      </div>
  </div>
</footer>
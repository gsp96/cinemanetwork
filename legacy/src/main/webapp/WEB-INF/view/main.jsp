<head>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    <title>main</title>
    <meta charset='utf-8'>
    <link rel="shortcut icon" href="icon.png">
    <link rel="apple-touch-icon" href="icon.ico">
    <link rel="icon" type="image/png" sizes="192x192" href="/android-icon-192x192.png">
    <meta name='viewport' content='width=device-width, initial-scale=1'>
    <script src='https://code.jquery.com/jquery-3.6.0.min.js'></script>
    <script src='https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js'></script>
    <script src='https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js'></script>
    <link rel='stylesheet' href='http://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css'>
    <link rel='stylesheet' href='https://use.fontawesome.com/releases/v5.6.3/css/all.css'/>
    <script src="https://code.jquery.com/jquery-3.2.1.min.js" type="text/javascript"></script>
    <%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
</head>
<script>

    function checkParams(key) {
	const queryString = new URLSearchParams(window.location.search)
	const entries = queryString.entries()
	
	let userData = ''
	for(entry of entries) {
		if(entry[1] == key) userData = entry
	}
	return userData
	console.log(userData)
}

function checkLogin() {
	var ssuserId = "<%=(String)session.getAttribute("userId")%>"
	if(checkParams('admin') && checkParams('admin')) {
		$('.login').toggle()
        $('.admin').prepend('<li class="nav-item"><a href="admin/main.html">관리자 페이지</a></li>')
		$('.logout').prepend('<li class="nav-item"><a href="main.html" >로그아웃</a></li>')
        $('.admin').toggle()
		$('.logout').toggle()
	} 
	console.log(ssuserId)
     if(( ssuserId != "null") && (ssuserId.length > 0)) {
	 	$('.login').toggle()
	 	$('.logout').prepend('<li class="nav-item"><a href="cinemanetwork" >로그아웃</a></li>')
        $('.mypage').prepend('<li class="nav-item"><a href="user/07.html" >마이페이지</a></li>')
	 	$('.logout').toggle()
        $('.mypage').toggle()
	 }
	$('.logout').click(() => {
		<% session.invalidate();%>
	})
}

$(() => {
	checkLogin()
})
</script>
<style>
    @font-face {
    font-family: 'S-CoreDream-6Bold';
    src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_six@1.2/S-CoreDream-6Bold.woff') format('woff');
    font-weight: normal;
    font-style: normal;
    }

    body {
        font-family: 'S-CoreDream-6Bold';
    }
    li { 
        list-style-type: none; 
    } 
    #slideShow {
        width: 500px; 
        height: 300px; 
        position: relative; 
        margin: 50px auto; 
        overflow: hidden; 
    } 
    .slides {
        position: absolute; 
        left: 0; 
        top: 0; 
        width: 2500px; 
        transition: 0.5s ease-out; 
    } 
    .slides li:first-child {
         margin-left: 100px; 
        } 
    .slides li:not(:last-child) { 
        float: left; margin-right: 100px; 
    } 
    .controller span { 
        position: absolute; 
        background-color: transparent; 
        color: black; text-align: center; 
        border-radius: 50%; 
        padding: 10px 20px; 
        top: 50%; font-size: 1.3em; 
        cursor: pointer; 
    } 
    .controller span:hover { 
        background-color: rgba(128, 128, 128, 0.11); 
    } 
    #img_slide_prev { 
        left: 10px; 
    } 
    #img_slide_prev:hover { 
        transform: translateX(-10px); 
    } 
    #img_slide_next { 
        right: 10px; 
    } 
    #img_slide_next:hover { 
        transform: translateX(10px); 
    }
    footer{
        background-color: rgb(225, 225, 225);
        padding-top: 2rem;
        padding-bottom: 2rem;
    }
    h6{
        color : rgb(255, 193, 7);
    }
</style>
<div style='position:fixed; bottom:20px;right:10px;'>
    <a class='border' href='#header'>위로가기</a>
</div>
<body>
    <header id='header'>
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
            <div class='p-4 border'><a href='main.html'>로고 이미지</a></div>
            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
              <span class="navbar-toggler-icon"></span>
            </button>
          
            <div class="collapse navbar-collapse" id="navbarSupportedContent">
              <ul class="navbar-nav mr-auto">
                <li class="nav-item active">
                  <a class="nav-link" href="movie/01.html">영화</a>
                </li>
                <li class="nav-item dropdown active">
                  <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    추천
                  </a>
                  <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                    <a class="dropdown-item" href="recommendation/01.html">장르별</a>
                    <div class="dropdown-divider"></div>
                    <a class="dropdown-item" href="recommendation/01.html">평점별</a>                    
                  </div>
                </li>
                <li class="nav-item active">
                  <a class="nav-link" href="community/01.html">커뮤니티</a>
                </li>
                <li class="nav-item active">
                    <a class="nav-link" href="inquiry/01.html">고객센터</a>
                  </li>
                <li class="nav-item active">
                    <a class="nav-link login" href="user/loginView">로그인</a>
                  </li>
                <li class="nav-item active">
                    <a class="nav-link admin" href="admin/main.html" style='display:none'></a>
                </li>
                <li class="nav-item active">
                    <a class="nav-link logout" href="main.html" style='display:none'></a>
                </li>
                <li class="nav-item active">
                    <a class="nav-link mypage" href="user/07.html" style='display:none'></a>
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
        <div class='row mt-5'>
            <div class='col-12'>
                <ul class="nav nav-tabs nav-fill" id='nav2'>
                    <li class="nav-item">
                    <a class="nav-link" href="recommendation/03.html"><span style="text-shadow:2px 2px 2px #000;"><h6>오늘의 추천</h6></span></a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="movie/01.html"><span style="text-shadow:2px 2px 2px #000;"><h6>영화</h6></span></a>
                    </li>
                    <li class="nav-item">
                    <a class="nav-link" href="community/01.html"><span style="text-shadow:2px 2px 2px #000;"><h6>커뮤니티</h6></span></a>
                    </li>
                </ul>
            </div>
        </div>
    </div>

    <div class='container'>
        <div class='row p-5'>
            <div class='col' style='padding:auto;'>
                <div class='preview_div'>
                </div>
                <div id='today_movie' class='carousel slide' data-ride='carousel' data-interval='4000'>
                    <div class="row">
                        <div class="col m-5">
                            <div class='carousel-inner  text-center' width='700' height='600' mazin='0 auto'>
                                <div class='carousel-item active' id='image1'>
                                    <div>
                                        <div class='mx-auto border'><a href='#'>포스터 이미지1</a></div>
                                    </div>
                                </div>
                                <div class='carousel-item' id='image2'>
                                    <div>
                                        <div class='mx-auto border'><a href='#'>포스터 이미지2</a></div>
                                    </div>
                                </div>
                                <div class='carousel-item' id='image3'>
                                    <div>
                                        <div class='mx-auto border'><a href='#'>포스터 이미지3</a></div>
                                    </div>
                                </div>
                            </div>
                            <a href='#today_movie' class='carousel-control-prev' data-slide='prev'>
                                <span class='carousel-control-prev-icon'></span>
                            </a>
                            <a href='#today_movie' class='carousel-control-next' data-slide='next'>
                                <span class='carousel-control-next-icon'></span>
                            </a>
                        </div>
                    </div>
                    <div class="row">
                        <div class='col'>
                            <ol class='carousel-indicators'>
                                <li data-target='#today_movie' data-slide-to='0' style="background-color: black;"></li>
                                <li data-target='#today_movie' data-slide-to='1' style="background-color: black;"></li>
                                <li data-target='#today_movie' data-slide-to='2' style="background-color: black;"></li>
                            </ol>
                        </div>                       
                    </div>
                </div>
            </div>            
        </div>
    </div>
    <!--노출되는 영상 이미지 끝-->
    <div class='tablist-prd-container'>
        <div class='container'>
            <div class='row'>
                <div class='col-md'>
                    <div class='tabbable-panel'>
                        <div class='tabbable-line'>
                            <ul class='nav nav-tabs nav-fill'>
                                <li class="nav-item active">
                                    <a href="#tab_default_1" data-toggle="tab">
                                    액션 &nbsp;</a>
                                </li>
                                <li class="nav-item">
                                    <a href="#tab_default_2" data-toggle="tab">
                                    로맨스 &nbsp;</a>
                                </li>
                                <li class="nav-item">
                                    <a href="#tab_default_3" data-toggle="tab">
                                    가족 &nbsp;</a>
                                </li>
                                <li class="nav-item">
                                    <a href="#tab_default_4" data-toggle="tab">
                                    드라마 &nbsp;</a>
                                </li>
                            </ul>
                            <div class="tab-content">
                                <div class="tab-pane active" id="tab_default_1">
                                    <div id='tradeTable' class='row m-4 row-cols-2 row-cols-md-4'>
                                        <div class='col mt-4 text-center'>
                                            <a href='#' class=''>
                                                <div id='poster2' class='mx-auto border'>포스터 이미지</div>
                                                <div class='text-center mt-1'>
                                                    <span>매트릭스</span>
                                                </div>
                                                <div id='' class='text-center mt-1'>
                                                    <span>4.6</span>
                                                </div>
                                            </a>
                                        </div>
                                        <div class='col mt-4 text-center'>
                                            <a href='#' class=''>
                                                <div id='poster2' class='mx-auto border'>포스터 이미지</div>
                                                <div class='text-center mt-1'>
                                                    <span>레지던트 이블</span>
                                                </div>
                                                <div id='' class='text-center mt-1'>
                                                    <span>4.0</span>
                                                </div>
                                            </a>
                                        </div><div class='col mt-4 text-center'>
                                            <a href='#' class=''>
                                                <div id='poster2' class='mx-auto border'>포스터 이미지</div>
                                                <div class='text-center mt-1'>
                                                    <span>베놈2</span>
                                                </div>
                                                <div id='' class='text-center mt-1'>
                                                    <span>3.5</span>
                                                </div>
                                            </a>
                                        </div><div class='col mt-4 text-center'>
                                            <a href='#' class=''>
                                                <div id='poster2' class='mx-auto border'>포스터 이미지</div>
                                                <div class='text-center mt-1'>
                                                    <span>프리가이</span>
                                                </div>
                                                <div id='' class='text-center mt-1'>
                                                    <span>3.0</span>
                                                </div>
                                            </a>
                                        </div><div class='col mt-4 text-center'>
                                            <a href='#' class=''>
                                                <div id='poster2' class='mx-auto border'>포스터 이미지</div>
                                                <div class='text-center mt-1'>
                                                    <span>테넷</span>
                                                </div>
                                                <div id='' class='text-center mt-1'>
                                                    <span>3.2</span>
                                                </div>
                                            </a>
                                        </div><div class='col mt-4 text-center'>
                                            <a href='#' class=''>
                                                <div id='poster2' class='mx-auto border'>포스터 이미지</div>
                                                <div class='text-center mt-1'>
                                                    <span>모가디슈</span>
                                                </div>
                                                <div id='' class='text-center mt-1'>
                                                    <span>2.8</span>
                                                </div>
                                            </a>
                                        </div><div class='col mt-4 text-center'>
                                            <a href='#' class=''>
                                                <div id='poster2' class='mx-auto border'>포스터 이미지</div>
                                                <div class='text-center mt-1'>
                                                    <span>분노의 질주</span>
                                                </div>
                                                <div id='' class='text-center mt-1'>
                                                    <span>4.0</span>
                                                </div>
                                            </a>
                                        </div><div class='col mt-4 text-center'>
                                            <a href='#' class=''>
                                                <div id='poster2' class='mx-auto border'>포스터 이미지</div>
                                                <div class='text-center mt-1'>
                                                    <span>저스티스 리그</span>
                                                </div>
                                                <div id='' class='text-center mt-1'>
                                                    <span>4.1</span>
                                                </div>
                                            </a>
                                        </div>
                                    </div>
                                    
                                </div>
                                <div class="tab-pane" id="tab_default_2">
                                    <p>
                                      
                                    </p>
                                    <p>
                                       
                                    </p>
                                    
                                </div>
                                <div class="tab-pane" id="tab_default_3">
                                    <p>
                                       
                                    </p>
                                    <p>
                                      
                                    </p>
                                   
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class='container p-5'>
                        <div class='row'>
                            <p>상영 예정작</p>
                        </div>
                        <div class='row'>
                            <div class='col' style='padding:auto;'>
                                <div class='preview_div'>
                                </div>
                                <div id='show_movie' class='carousel slide' data-ride='carousel' data-interval='4000'>
                                    <div class="row">
                                        <div class="col m-5">
                                            <div class='carousel-inner  text-center' width='700' height='600' mazin='0 auto'>
                                                <div class='carousel-item active' id='image1'>
                                                    <div>
                                                        <div class='mx-auto border'><a href='#'>포스터 이미지1</a></div>
                                                    </div>
                                                </div>
                                                <div class='carousel-item' id='image2'>
                                                    <div>
                                                        <div class='mx-auto border'><a href='#'>포스터 이미지2</a></div>
                                                    </div>
                                                </div>
                                                <div class='carousel-item' id='image3'>
                                                    <div>
                                                        <div class='mx-auto border'><a href='#'>포스터 이미지3</a></div>
                                                    </div>
                                                </div>
                                                <div class='carousel-item' id='image4'>
                                                    <div>
                                                        <div class='mx-auto border'><a href='#'>포스터 이미지4</a></div>
                                                    </div>
                                                </div>
                                                <div class='carousel-item' id='image5'>
                                                    <div>
                                                        <div class='mx-auto border'><a href='#'>포스터 이미지5</a></div>
                                                    </div>
                                                </div>
                                                <div class='carousel-item' id='image6'>
                                                    <div>
                                                        <div class='mx-auto border'><a href='#'>포스터 이미지6</a></div>
                                                    </div>
                                                </div>
                                                <div class='carousel-item' id='image7'>
                                                    <div>
                                                        <div class='mx-auto border'><a href='#'>포스터 이미지7</a></div>
                                                    </div>
                                                </div>
                                            </div>
                                            <a href='#show_movie' class='left carousel-control-prev' role='button' data-slide='prev'>
                                                <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
                                                <span class="sr-only">Previous</span>
                                            </a>
                                            <a href='#show_movie' class='right carousel-control-next' role='button' data-slide='next'>
                                                <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
                                                <span class="sr-only">Next</span>
                                            </a>
                                        </div>
                                    </div>
                                </div>
                            </div>            
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class='container'>
        <div class='row'>
            <div class='col m-2'>
                <section id="1">
                    <div class='mx-auto border'>포스터 이미지1</div>
                </section>
            </div>
            <div class='col m-2'>
                <section id="2">
                    <div class='mx-auto border'>포스터 이미지2</div>
                </section>
            </div>
        </div>
        <div class='row'>
            <div class='col m-2'>
                <section id="3">
                    <div class='mx-auto border'>포스터 이미지3</div>
                </section>
            </div>
            <div class='col m-2'>
                <section id="4">
                    <div class='mx-auto border'>포스터 이미지4</div>
                </section>
            </div>
        </div>
        <div class='row'>
            <div class='col m-2'>
                <section id="5">
                    <div class='mx-auto border'>포스터 이미지5</div>
                </section>
            </div>
            <div class='col m-2'>
                <section id="6">
                    <div class='mx-auto border'>포스터 이미지6</div>
                </section>
            </div>
        </div>
        <div class='row'>
            <div class='col m-2'>
                <section id="7">
                    <div class='mx-auto border'>포스터 이미지7</div>
                </section>
            </div>
            <div class='col m-2'>
                <section id="8">
                    <div class='mx-auto border'>포스터 이미지8</div>
                </section>
            </div>
        </div>
    </div>
</body>
    <!--이미지-->
    
  <footer>
    <div class='container text-center'>
        <div class='row-md-10'>
            <p class=''>문의 : <a href="https://https://is.ezenac.co.kr/" data-toggle='tooltip'>https://is.ezenac.co.kr/</a> </p>
            <div class='col text-center'>
                <span>사업장주소 : 경기도 고양시 일산동구 장항동 890-4 마두법조빌딩 9F 10414</span><br>
                <span>호스팅서비스사업자 : 이젠컴퓨터학원(주) 대표번호 : 031-994-8881 개발 문의 : 1577-0000 영화 문의 : 1588-1111</span><br>
                <span>본 사이트에서 사용되는 이미지 중에는 등록된 개별 저작권 이미지가 포함되어 있습니다.</span><br>
                <span>개별 저작권 이미지의 경우 씨네마 네트워크(주)는 중개업자로서 저작권 소유자가 아니므로, 거래정보 및 거래 등에 사용하지 않습니다.</span><br>
                <span>본 사이트의 컨텐츠는 저작권법의 보호를 받는 바 무단 전재, 복사, 배포 등을 금합니다.</span>
            </div>
        </div>
    </div>
  </footer>
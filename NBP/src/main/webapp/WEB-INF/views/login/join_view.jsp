<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입</title>
<script src="https://code.jquery.com/jquery.js"></script>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.1.3/dist/css/bootstrap.min.css"	integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO"crossorigin="anonymous">

<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script src="//dapi.kakao.com/v2/maps/sdk.js?appkey=0b38572f5de0a09cbaa96703ebb627d1&libraries=services"></script>
<script src="https://www.google.com/recaptcha/enterprise.js?render=6Les5h8pAAAAAE3rpx19KV_omuiqG_1NMyAKnMan" async defer></script>
<script src="https://accounts.google.com/gsi/client" async defer></script>
<script>
	function handleCredentialResponse(response) {
	    var profile = jwt_decode(response.credential);

	    $.ajax({
			url:'snsSelect.do',
			type:'POST',
			data : {
				name: profile.name,
				Email: profile.email
			},
			dataType: 'json',
			success: function(json) {
				var result = eval(json);
				if(result.code=="ok"){
					alert("가입한 ID입니다.");
					window.location.replace("main.do");
				}else{
					window.location.replace("SNS_join_view.do?name="+profile.name+"&Email="+profile.email);
				}
			}
		});
	}

	function form_check() {
		var a = "";
		var check = " ";
		
		if ($('#ID').val().length == 0) {
			alert("아이디는 5~20자의 영문 소문자, 숫자와 특수기호(_),(-)만 사용 가능합니다.");
			$('#ID').focus();
			return;
		}
		if ($('#ID').val().length < 4) {
			alert("아이디는 5~20자의 영문 소문자, 숫자와 특수기호(_),(-)만 사용 가능합니다.");
			$('#ID').focus();
			return;
		}
		if ($('#ID').val().length > 20) {
			alert("아이디는 5~20자의 영문 소문자, 숫자와 특수기호(_),(-)만 사용 가능합니다.");
			$('#ID').focus();
			return;
		}
		if ($('#PASSWORD').val().length == 0) {
			alert("비밀번호는 8~16자의 영문 대/소문자, 숫자, 특수문자를 사용해 주세요");
			$('#PASSWORD').focus();
			return;
		}
		if ($('#PASSWORD').val().length < 8) {
			alert("비밀번호는 8~16자의 영문 대/소문자, 숫자, 특수문자를 사용해 주세요");
			$('#PASSWORD').focus();
			return;
		}
		if ($('#PASSWORD').val().length > 16) {
			alert("비밀번호는 8~16자의 영문 대/소문자, 숫자, 특수문자를 사용해 주세요");
			$('#PASSWORD').focus();
			return;
		}
		if ($('#PASSWORD').val() != $('#pw_check').val()) {
			alert("비밀번호는 비밀번호가 일치하지 않습니다.");
			$('#PASSWORD').focus();
			return;
		}
		if ($('#NAME').val().length == 0) {
			alert("이름은 필수사항입니다.");
			$('#NAME').focus();
			return;
		}
		if ($('#EMAIL_check').val().length == 0) {
			alert("이메일은 필수사항입니다.");
			$('#EMAIL_check').focus();
			return;
		}
		if ($('#NICKNAME').val().length == 0) {
			alert("닉네임은 필수사항입니다.");
			$('#NICKNAME').focus();
			return;
		}
		if ($('#NICKNAME').val().length < 2) {
			alert("닉네임은 2글자 이상이어야 합니다.");
			$('#NICKNAME').focus();
			return;
		}
		if ($('#phone2').val().length == 0) {
			alert("전화번호는 필수사항입니다.");
			$('#phone2').focus();
			return;
		}
		if ($('#phone3').val().length == 0) {
			alert("전화번호는 필수사항입니다.");
			$('#phone3').focus();
			return;
		}
		if (a == check) {
			alert("이메일 인증은 필수사항입니다.");
			$('#EMAIL').focus();
			return;
		}
		var form = document.join_form;
	    
	    form.submit();
	}
	
	// address
	function execDaumPostcode() {
		new daum.Postcode({
			oncomplete : function(data) {
				var addr = data.address;
				document.getElementById("ADDRESS").value = addr;
			}
		}).open();
	}
	
	function recaptchaCallback() {
	    document.getElementById('capok').removeAttribute('disabled');
	}
	
	
	
	function email_check() {
		document.getElementById("check_id").value = "재전송";
		alert('인증번호가 발송되었습니다');
		form_submit();
	}
	
	function form_submit() {
		var email = document.getElementById("EMAIL_check").value;
		document.getElementById("EMAIL").value = email;
		$.ajax({
		    type: "POST",
		    contentType: "application/json",
		    url: "/api/emailCheck",
		    data: JSON.stringify({"mail": email}), // 보낼 데이터
		    dataType: 'json',
		    success: function (response) {
		        console.log("Success:", response);
		        a = response.authCode;
		    },
		    error: function (error) {
		        console.error("Error:", error);
		    }
		});
	}
	
	function check2() {
		check = document.getElementById("e_check").value;
		if(a == check){
			document.getElementById("EMAIL_check").disabled=true;
			document.getElementById("check_id").disabled=true;
			
			document.getElementById("e_check").disabled=true;
			document.getElementById("e_check2").disabled=true;
			alert('인증되었습니다');
			
		}else{
			alert('인증번호가 잘못 입력되었습니다. 다시 입력해주세요');
		}
	}
	
</script>
<style>
body {
     background-image:url('/image/join.png');
     background-size: cover;
     font-family: 'Roboto', sans-serif;
}

.login-box {
    margin-top: 75px;
    height: auto;
    background: #cfcfcf;
    text-align: center;
    box-shadow: 0 3px 6px rgba(0, 0, 0, 0.16), 0 3px 6px rgba(0, 0, 0, 0.23);
}

.login-key {
    height: 100px;
    font-size: 80px;
    line-height: 100px;
    background: -webkit-linear-gradient(#27EF9F, #0DB8DE);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
}

.login-title {
    margin-top: 15px;
    text-align: center;
    font-size: 30px;
    letter-spacing: 2px;
    margin-top: 15px;
    font-weight: bold;
    color: #000000;
}

.login-form {
    margin-top: 25px;
    text-align: left;
}

input[type=text] {
    background-color: #dbdbdb;
    border: none;
    border-bottom: 2px solid #ffffff;
    border-top: 0px;
    border-radius: 0px;
    font-weight: bold;
    outline: 0;
    margin-bottom: 20px;
    padding-left: 0px;
    color: #000000;
}

input[type=password] {
    background-color: #dbdbdb;
    border: none;
    border-bottom: 2px solid #ffffff;
    border-top: 0px;
    border-radius: 0px;
    font-weight: bold;
    outline: 0;
    padding-left: 0px;
    margin-bottom: 20px;
    color: #000000;
}

.form-group {
    margin-bottom: 40px;
    outline: 0px;
}

.form-control:focus {
    border-color: inherit;
    -webkit-box-shadow: none;
    box-shadow: none;
    border-bottom: 2px solid #ffffff;
    outline: 0;
    background-color: #ffffff;
    color: #000000;
}

input:focus {
    outline: none;
    box-shadow: 0 0 0;
}

label {
    margin-bottom: 0px;
}

.form-control-label {
    font-size: 12px;
    color: #000000;
    font-weight: bold;
    letter-spacing: 1px;
}

.btn-outline-primary {
    border-color: #0DB8DE;
    color: #0DB8DE;
    border-radius: 0px;
    font-weight: bold;
    letter-spacing: 1px;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.12), 0 1px 2px rgba(0, 0, 0, 0.24);
}

.btn-outline-primary:hover {
    background-color: #0DB8DE;
    right: 0px;
}

.login-btm {
    float: left;
}

.login-button {
    padding-right: 0px;
    text-align: right;
    margin-bottom: 25px;
}

.login-text {
    text-align: left;
    padding-left: 0px;
    color: #A2A4A4;
}

.loginbttm {
    padding: 0px;
}

#phone1{
    width: 200px;
    border: 1px solid #ffffff;
    box-sizing: border-box;
    border-radius: 10px;
    padding: 12px 13px;
    font-family: 'Roboto';
    font-style: normal;
    font-weight: 400;
    font-size: 14px;
    line-height: 16px;
}

#phone1:focus{
    border: 1px solid #ffffff;
    box-sizing: border-box;
    border-radius: 10px;
    outline: 3px solid #ffffff;
    border-radius: 10px;
}

.yws {
    padding: 10px;
    font-size:15px;
    font-weight:5px bold;
    color:#000000;
}
</style>
</head>
<body>
    <div class="container">
        <div class="row">
            <div class="col-lg-3 col-md-2"></div>
            <div class="col-lg-6 col-md-8 login-box">
                
                <div class="col-lg-12 login-title">
                    Join
                </div>

                <div class="col-lg-12 login-form">
                    <div class="col-lg-12 login-form">
                        <form id="join_form" name="join_form" action="userJoin" method="post">
                           <div class="form-group">
                                <input type="radio" name="BBANG" value="1" checked="checked">&nbsp;<label class="form-control-label">내빵이</label>&nbsp;
                        <input type="radio" name="BBANG" value="2">&nbsp;<label class="form-control-label">니빵이</label>
                            </div>
                            <div class="form-group">
                                <label class="form-control-label">아이디</label>
                                <input type="text" id="ID" name="ID" class="form-control" autocomplete='off'>
                            </div>
                            <div class="form-group">
                                <label class="form-control-label">비밀번호</label>
                                <input type="password" id="PASSWORD" name="PASSWORD" class="form-control">
                            </div>
                            <div class="form-group">
                                <label class="form-control-label">비밀번호 확인</label>
                                <input type="password" id ="pw_check" name="pw_check" class="form-control">
                            </div>
                            <div class="form-group">
                                <label class="form-control-label">이름</label>
                                <input type="text" id="NAME" name="NAME" size="20" class="form-control" autocomplete='off'>
                            </div>
                            <div class="form-group">
                                <label class="form-control-label">닉네임</label>
                                <input type="text" id="NICKNAME" name="NICKNAME" size="20" class="form-control" autocomplete='off'>
                            </div>
                     
                            <div class="form-group">
                                <select id="phone1" name="phone1" class="form-control" style="width:20%; display: inline-block; padding: 0px 10px;">
                                    <option value="010">010</option>
                                    <option value="016">016</option>
                                    <option value="017">017</option>
                                    <option value="018">018</option>
                                    <option value="019">019</option>
                                    <option value="011">011</option>
                                    </select>
                                    -
                                    <input type="text" style="width:35%; display: inline-block;" autocomplete='off' class="form-control" id="phone2" name="phone2" size="5" maxlength="4" oninput="this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1');">
                                    - 
                                    <input type="text" style="width:35%; display: inline-block;" autocomplete='off' class="form-control" id="phone3" name="phone3" size="5" maxlength="4" oninput="this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1');">
                            </div>
                            <div class="form-group">
                                <label class="form-control-label">이메일</label>
                                <input type="text" autocomplete='off' id="EMAIL_check" name="EMAIL_check" class="form-control">
                                <input type="button" id="check_id" name="check_id" value="전송" onclick="email_check()">
                                <input type="hidden" id="EMAIL" name="EMAIL"> 
                            </div>
                             <div class="form-group">
                                <label class="form-control-label">인증번호</label>
                                <input type="text"  autocomplete='off' id="e_check" name="e_check" class="form-control">
                                <input type="button" id="e_check2" name="e_check2" value="확인" onclick="check2()">
                            </div>
                            <div class="form-group">
                                <label class="form-control-label">주소</label>
                                <input type="text" class="form-control" id="ADDRESS" autocomplete='off' name="ADDRESS" size="50"> 
                                <input type="button" class="btn btn-outline-primary" onclick="execDaumPostcode()" value="주소 검색">
                            </div>
                            <div class="col-lg-12" style="margin-bottom:40px;">
                                </div>
                           
                            <div class="col-lg-12 loginbttm">
                        <div class="col-lg-6 login-btm login-text">
                           <a href="/oauth2/authorization/google">Google Login</a>
                        </div>
                        <div class="col-lg-6 login-btm login-button">
                                    <input type="button" class="btn btn-outline-primary" value="회원가입" id="capok"onclick="form_check()"/>
                                </div>
                        
                     </div>
                        </form>
                    </div>
                </div>
                <div class="col-lg-3 col-md-2"></div>
            </div>
        </div>
    </div>
    
    <div class="col-lg-12" style="text-align: center; margin: 10px auto">
		<a href="../loginView"><label class="form-control-label yws" >로그인</label></a> &nbsp;
		<a href="../search_id"><label class="form-control-label yws">아이디 찾기</label></a> &nbsp;
		<a href="../search_pw"><label class="form-control-label yws">패스워드 찾기</label></a>
	</div>
</body>
</html>
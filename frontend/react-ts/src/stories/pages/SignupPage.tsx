import styled from "styled-components";
import Btn from "../inputs/btn";
import { colors } from "../../styles/color";
import logo from "../../img/logo_withText.png";
import { useNavigate } from "react-router-dom";
import InputBox from "../inputs/InputBox";
import { useState } from "react";

const Title = styled.div<{ fontSize: string; margin: string }>`
  font-size: ${(props) => props.fontSize};
  margin: ${(props) => props.margin};
  font-family: "Bd";
`;
const Container = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100vh;
  width: 100vw;
  background-color: ${colors.BG_grey};
  color: ${colors.Font_black};
`;
const LeftAlignContainer = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: flex-end;
  align-items: center;
  width: 270px;
  margin: 10px;
  padding: 0;
`;
const RightAlignContainer = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  align-items: center;
  width: 100%;
`;
const WrappingContainer = styled.div<{
  flex_direction: string;
  justify_content: string;
}>`
  display: flex;
  flex-direction: ${(props) => props.flex_direction};
  justify-content: ${(props) => props.justify_content};
  align-items: center;
  width: 70vw;
`;
const StackConatiner = styled.div`
  position: relative;
`;
function SignupPage() {
  const [id, setId] = useState("");
  const [password, setPassword] = useState("");
  const [confirm, setConfirm] = useState("");

  const [idError, setIdError] = useState("");
  const [passwordError, setPasswordError] = useState("");
  const [confirmError, setConfirmError] = useState("");

  const [isIdCheck, setIsIdCheck] = useState(false); // 중복 검사를 했는지 안했는지
  const [isIdAvailable, setIsIdAvailable] = useState(false); // 아이디 사용 가능한지 아닌지

  const signupHandler = async (e) => {
    e.preventDefault();

    const idCheckresult = await idCheckHandler(id);
    if (idCheckresult) setIdError("");
    else return;
    if (!isIdCheck || !isIdAvailable) {
      alert("아이디 중복 검사를 해주세요.");
      return;
    }

    const passwordCheckResult = passwordCheckHandler(password, confirm);
    if (passwordCheckResult) {
      setPasswordError("");
      setConfirmError("");
    } else return;
  };

  const idCheckHandler = async (email: string) => {
    const check1 = /(@.*@)|(\.\.)|(@\.)|(^\.)/;
    const check2 = /^[a-zA-z0-9\-\.\_]+\@[a-zA-Z0-9\-\.]+([a-zA-Z]{2,4})$/;

    if (email === "") {
      setIdError("이메일를 입력해주세요.");
      setIsIdAvailable(false);
      return false;
    } else if (!check1.test(email) && check2.test(email)) {
      setIdError("이메일의 형식이 아닙니다.");
      setIsIdAvailable(false);
      return false;
    } else {
      setIdError("");
    }
  };

  const passwordCheckHandler = (password: string, confirm: string) => {
    const passwordRegex = /^[a-z\d!@*&-_]{8,16}$/;
    if (password === "") {
      setPasswordError("비밀번호를 입력해주세요.");
      return false;
    } else if (!passwordRegex.test(password)) {
      setPasswordError(
        "비밀번호는 8~16자의 영소문자, 숫자, !@*&-_만 입력 가능합니다."
      );
      return false;
    } else if (confirm !== password) {
      setPasswordError("");
      setConfirmError("비밀번호가 일치하지 않습니다.");
      return false;
    } else {
      setPasswordError("");
      setConfirmError("");
      return true;
    }
  };

  const onChangeIdHandler = (e: any) => {
    const idValue = e.target.value;
    setId(idValue);
    idCheckHandler(idValue);
  };

  const onChangePasswordHandler = (e) => {
    const { name, value } = e.target;
    if (name === "password") {
      setPassword(value);
      passwordCheckHandler(value, confirm);
    } else {
      setConfirm(value);
      passwordCheckHandler(password, value);
    }
  };

  const navigate = useNavigate();
  const GoToStartPage = () => {
    navigate("/");
  };
  const GoToLoginPage = () => {
    navigate("/Login");
  };
  // const [itemid, setItemid] = useState('');
  // const onChange = (event) => {
  //   setItemid(event.target.value);
  // }
  let url = "http://192.168.0.146:8080/mail/request";
  const emailcheck = () => {
    const fetchDatas = async () => {
      console.log(123);
      const response = await fetch(url, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ email: "jd06017@naver.com" }),
      });
      const data = await response.json();
      console.log(data);
    };
    fetchDatas();
  };

  return (
    <Container>
      <WrappingContainer flex_direction="column" justify_content="center">
        <img
          src={logo}
          width="150px"
          height="150px"
          onClick={GoToStartPage}
        ></img>
        <Title fontSize="30px" margin="10px">
          회원가입
        </Title>
        <RightAlignContainer>
          <Title fontSize="20px" margin="10px">
            이름
          </Title>
        </RightAlignContainer>
        <InputBox placeholder="이름"></InputBox>
        <RightAlignContainer>
          <Title fontSize="20px" m argin="10px">
            E-mail
          </Title>
        </RightAlignContainer>
        <StackConatiner>
          <InputBox
            placeholder="E-mail"
            onChange={onChangeIdHandler}
          ></InputBox>
          <Btn
            width="100px"
            height="33px"
            fontsize="15px"
            text="인증번호 발송"
            onClick={emailcheck}
          ></Btn>
        </StackConatiner>
        <RightAlignContainer>
          {idError && (
            <small
              style={{ color: "red" }}
              className={isIdAvailable ? "idAvailable" : ""}
            >
              {idError}
            </small>
          )}
        </RightAlignContainer>
        <StackConatiner>
          <InputBox placeholder="인증번호 입력"></InputBox>
          <Btn
            width="100px"
            height="33px"
            fontsize="15px"
            text="인증번호 확인"
          ></Btn>
        </StackConatiner>
        <RightAlignContainer>
          <Title fontSize="20px" margin="10px">
            Password
          </Title>
        </RightAlignContainer>
        <InputBox placeholder="Password"></InputBox>
        <InputBox placeholder="Password 확인"></InputBox>
        <StackConatiner>
          <LeftAlignContainer></LeftAlignContainer>
          <Btn width="100px" height="33px" fontsize="15px" text="가입"></Btn>
        </StackConatiner>
      </WrappingContainer>
    </Container>
  );
}
export default SignupPage;

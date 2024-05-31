import styled from "styled-components";
import Btn from "../inputs/btn";
import { colors } from "../../styles/color";
import logo from "../../img/logo_withText.png";
import { useNavigate } from "react-router-dom";
import InputBox from "../inputs/InputBox";
import { useState } from "react";
import useStore from "../store/store";

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
  const [confirm, setConfirm] = useState("");
  const [checkcode, setCheckcode] = useState("");
  const [idError, setIdError] = useState("이메일를 입력해주세요.");
  const [passwordError, setPasswordError] =
    useState("비밀번호를 입력해주세요.");
  const [confirmError, setConfirmError] = useState("");
  const [codecheckError, setCodecheckError] = useState("");
  const [noticeMail, setNoticeMail] = useState("");

  // const email = UserInfoStore((state) => state.email)
  const email = useStore((state) => state.email);
  const setEmail = useStore((state) => state.setEmail);
  const name = useStore((state) => state.name);
  const setName = useStore((state) => state.setName);
  const password = useStore((state) => state.password);
  const setPassword = useStore((state) => state.setPassword);

  const idCheckHandler = async (email: string) => {
    const check = /[a-z0-9]+@[a-z]+\.[a-z]{2,3}/;

    if (email === "") {
      setIdError("이메일를 입력해주세요.");
      return false;
    } else if (!check.test(email)) {
      setIdError("이메일의 형식이 아닙니다.");
      return true;
    } else {
      setIdError("");
      return false;
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
    // setId(idValue);
    setEmail(idValue);
    idCheckHandler(idValue);
  };

  const onChangePasswordHandler = (e: any) => {
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
  let url = "http://10.255.81.70:8030/mail/request";
  let checkCodeUrl = "http://10.255.81.70:8030/mail/verify";
  let signUpUrl = "http://10.255.81.70:8030/user/register";

  const emailcheck = () => {
    if (idError != "") return;
    setNoticeMail("인증 메일을 발송했습니다. 3분 이내 입력해주세요.");
    const fetchDatas = async () => {
      console.log("emailcheck");
      console.log(email);
      const response = await fetch(url, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ email: email }),
      });
      const data = await response.json();
      console.log(data);
    };
    fetchDatas();
  };

  const codeCheck = () => {
    const fetchDatas = async () => {
      console.log("인증번호 확인");
      const response = await fetch(checkCodeUrl, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ email: email, code: checkcode }),
      });
      const data = await response.json();
      console.log(response.status);
      if (response.status == 200) setCodecheckError("인증번호 확인되었습니다.");
      else setCodecheckError("인증번호를 다시 확인해주십시오.");
      console.log(data);
    };
    fetchDatas();
  };

  const signupHandler = () => {
    if (idError != "") {
      alert("Email 형식을 확인해주세요.");
      return;
    }
    if (passwordError != "") {
      alert("패스워드 형식을 확인해주세요.");
      return;
    }
    if (confirmError != "") {
      alert("패스워드가 일치하는지 확인해주세요.");
      return;
    }
    console.log(name);
    const fetchDatas = async () => {
      console.log("회원가입");
      console.log(email);
      console.log(password);
      const response = await fetch(signUpUrl, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          email: email,
          password: password,
          checkPassword: password,
          name: name,
        }),
      });
      const data = await response.json();
      console.log(data);
    };
    fetchDatas();
    GoToLoginPage();
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
          <Title fontSize="20px">이름</Title>
        </RightAlignContainer>
        <InputBox
          name="name"
          placeholder="이름"
          onChange={(e) => setName(e.target.value)}
        ></InputBox>
        <RightAlignContainer>
          <Title fontSize="20px">E-mail</Title>
        </RightAlignContainer>
        <StackConatiner>
          <InputBox
            name="E-Mail"
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
          {idError && <small style={{ color: "red" }}>{idError}</small>}
          {noticeMail && <small>{noticeMail}</small>}
        </RightAlignContainer>
        <StackConatiner>
          <InputBox
            name="auth"
            placeholder="인증번호 입력"
            onChange={(e) => setCheckcode(e.target.value)}
          ></InputBox>
          <Btn
            width="100px"
            height="33px"
            fontsize="15px"
            text="인증번호 확인"
            onClick={codeCheck}
          ></Btn>
        </StackConatiner>
        <RightAlignContainer>
          {codecheckError && <small>{codecheckError}</small>}
        </RightAlignContainer>
        <RightAlignContainer>
          <Title fontSize="20px">Password</Title>
        </RightAlignContainer>

        <InputBox
          name="password"
          placeholder="Password"
          onChange={onChangePasswordHandler}
        ></InputBox>
        <RightAlignContainer>
          {passwordError && (
            <small style={{ color: "red" }}>{passwordError}</small>
          )}
        </RightAlignContainer>
        <InputBox
          name="confirm"
          placeholder="Password 확인"
          onChange={onChangePasswordHandler}
        ></InputBox>
        <RightAlignContainer>
          {confirmError && (
            <small style={{ color: "red" }}>{confirmError}</small>
          )}
        </RightAlignContainer>
        <StackConatiner>
          <LeftAlignContainer></LeftAlignContainer>
          <Btn
            width="100px"
            height="33px"
            fontsize="15px"
            text="가입"
            onClick={signupHandler}
          ></Btn>
        </StackConatiner>
      </WrappingContainer>
    </Container>
  );
}
export default SignupPage;

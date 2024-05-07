import styled from "styled-components";
import Btn from "../btn";
import { colors } from "../../styles/color";
import logo from "../../img/logo_withText.png";
import { useNavigate } from "react-router-dom";
import InputBox from "../InputBox";

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
  width: 100%;
  margin: 10px;
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

function SignupPage() {
  const navigate = useNavigate();
  const GoToStartPage = () => {
    navigate("/");
  };
  const GoToLoginPage = () => {
    navigate("/Login");
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
        <Title fontSize="20px" margin="10px">
          이름
        </Title>
        <InputBox placeholder="이름"></InputBox>
        <Title fontSize="20px" margin="10px">
          E-mail
        </Title>
        <InputBox placeholder="E-mail"></InputBox>
        <Btn text="인증번호 발송"></Btn>
        <InputBox placeholder="인증번호 입력"></InputBox>
        <Btn text="인증번호 확인"></Btn>
        <Title fontSize="20px" margin="10px">
          Password
        </Title>
        <InputBox placeholder="Password"></InputBox>
        <InputBox placeholder="Password 확인"></InputBox>
        <LeftAlignContainer>
          <Btn text="가입"></Btn>
        </LeftAlignContainer>
      </WrappingContainer>
    </Container>
  );
}

export default SignupPage;

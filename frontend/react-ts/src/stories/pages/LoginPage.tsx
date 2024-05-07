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
  background-color: ${colors.BG_lightpink};
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

function LoginPage() {
  const navigate = useNavigate();
  const GoToStartPage = () => {
    navigate("/");
  };
  const GoToSignupPage = () => {
    navigate("/Signup");
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
          {" "}
          로그인{" "}
        </Title>
        <InputBox placeholder="E-mail"></InputBox>
        <InputBox placeholder="Password"></InputBox>
        <LeftAlignContainer>
          <Btn text="로그인"></Btn>
        </LeftAlignContainer>
      </WrappingContainer>
      <Title fontSize="15px" margin="10px" onClick={GoToSignupPage}>
        회원가입
      </Title>
    </Container>
  );
}

export default LoginPage;

import styled from "styled-components";
import Btn from "../inputs/btn";
import { colors } from "../../styles/color";
import logo from "../../img/logo_withText.png";
import { useNavigate } from "react-router-dom";
import InputBox from "../inputs/InputBox";
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
          <InputBox placeholder="E-mail"></InputBox>
          <Btn
            width="100px"
            height="33px"
            fontsize="15px"
            text="인증번호 발송"
          ></Btn>
        </StackConatiner>
        <StackConatiner>
          <InputBox placeholder="인증번호 입력"></InputBox>
          <Btn
            onClick={emailcheck}
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

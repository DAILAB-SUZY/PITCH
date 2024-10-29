import styled from "styled-components";
import logo from "../../img/logo_withText.png";
import { colors } from "../../styles/color";
import { useNavigate } from "react-router-dom";

const Container = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  width: 100vw;
  background-color: ${colors.BG_white};
`;

function StartPage() {
  const navigate = useNavigate();
  const GoToLoginPage = () => {
    navigate("/Login");
  };
  return (
    <Container>
      <img src={logo} width="100px" height="100px" onClick={GoToLoginPage}></img>
    </Container>
  );
}

export default StartPage;

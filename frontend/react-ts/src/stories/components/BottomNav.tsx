import styled from "styled-components";
import { colors } from "../../styles/color";
import { useState } from "react";
import { useNavigate } from "react-router-dom";

interface NavProps {
  onClick?: () => void;
}

const Container = styled.div`
  width: 100vw;
  height: 60px;
  display: flex;
  flex-direction: row;
  justify-content: space-around;
  align-items: center;
  z-index: 5;
  box-shadow: rgba(0, 0, 0, 0.3) 0px 1px 4px;
`;

const Button = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
`;

const Title = styled.div<{ fontSize: string; margin: string; color: string }>`
  font-size: 12px;
  margin: ${(props) => props.margin};
  color: ${(props) => props.color};
  font-family: "Rg";
`;

const BottomNav = ({ onClick }: NavProps) => {
  const navigate = useNavigate();
  const GoToAlbumChatPage = () => {
    navigate("/AlbumChatPage");
  };
  const GoToHomePage = () => {
    navigate("/Home");
  };
  const [activeNav, setActiveNav] = useState(2);

  return (
    <Container>
      <Button
        onClick={() => {
          setActiveNav(1);
          GoToAlbumChatPage();
        }}
      >
        <svg
          xmlns="http://www.w3.org/2000/svg"
          width="36"
          height="36"
          fill={activeNav === 1 ? colors.Button_active : colors.Button_inactive}
          viewBox="0 0 16 16"
        >
          <path d="M14.5 3a.5.5 0 0 1 .5.5v9a.5.5 0 0 1-.5.5h-13a.5.5 0 0 1-.5-.5v-9a.5.5 0 0 1 .5-.5zm-13-1A1.5 1.5 0 0 0 0 3.5v9A1.5 1.5 0 0 0 1.5 14h13a1.5 1.5 0 0 0 1.5-1.5v-9A1.5 1.5 0 0 0 14.5 2z" />
          <path d="M3 5.5a.5.5 0 0 1 .5-.5h9a.5.5 0 0 1 0 1h-9a.5.5 0 0 1-.5-.5M3 8a.5.5 0 0 1 .5-.5h9a.5.5 0 0 1 0 1h-9A.5.5 0 0 1 3 8m0 2.5a.5.5 0 0 1 .5-.5h6a.5.5 0 0 1 0 1h-6a.5.5 0 0 1-.5-.5" />
        </svg>
        <Title color={activeNav === 1 ? "black" : "grey"}>앨범챗</Title>
      </Button>
      <Button
        onClick={() => {
          setActiveNav(2);
          GoToHomePage();
        }}
      >
        <svg
          xmlns="http://www.w3.org/2000/svg"
          width="36"
          height="36"
          fill={activeNav === 2 ? colors.Button_active : colors.Button_inactive}
          viewBox="0 0 16 16"
        >
          <path d="M8.707 1.5a1 1 0 0 0-1.414 0L.646 8.146a.5.5 0 0 0 .708.708L2 8.207V13.5A1.5 1.5 0 0 0 3.5 15h9a1.5 1.5 0 0 0 1.5-1.5V8.207l.646.647a.5.5 0 0 0 .708-.708L13 5.793V2.5a.5.5 0 0 0-.5-.5h-1a.5.5 0 0 0-.5.5v1.293zM13 7.207V13.5a.5.5 0 0 1-.5.5h-9a.5.5 0 0 1-.5-.5V7.207l5-5z" />
        </svg>
        <Title color={activeNav === 2 ? "black" : "grey"}>홈</Title>
      </Button>
      <Button onClick={() => setActiveNav(3)}>
        <svg
          xmlns="http://www.w3.org/2000/svg"
          width="36"
          height="36"
          fill={activeNav === 3 ? colors.Button_active : colors.Button_inactive}
          viewBox="0 0 16 16"
        >
          <path d="M3 14s-1 0-1-1 1-4 6-4 6 3 6 4-1 1-1 1zm5-6a3 3 0 1 0 0-6 3 3 0 0 0 0 6" />
        </svg>
        <Title color={activeNav === 3 ? "black" : "grey"}>프로필</Title>
      </Button>
    </Container>
  );
};

export default BottomNav;

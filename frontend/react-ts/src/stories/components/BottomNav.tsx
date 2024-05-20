import styled from "styled-components";
import { colors } from "../../styles/color";
import { useState } from "react";

interface NavProps {
  onClick?: () => void;
}

const Container = styled.div`
  width: 90vw;
  height: 10vh;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
`;

const Button = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
`;

const Title = styled.div<{ fontSize: string; margin: string }>`
  font-size: ${(props) => props.fontSize};
  margin: ${(props) => props.margin};
  font-family: "Bd";
`;

const BottomNav = ({ onClick }: NavProps) => {
  const [activeNav, setActiveNav] = useState(1);

  return (
    <Container>
      <Button onClick={() => setActiveNav(1)}>
        <svg
          xmlns="http://www.w3.org/2000/svg"
          width="48"
          height="48"
          fill="currentColor"
          viewBox="0 0 16 16"
        >
          <path d="M8.707 1.5a1 1 0 0 0-1.414 0L.646 8.146a.5.5 0 0 0 .708.708L2 8.207V13.5A1.5 1.5 0 0 0 3.5 15h9a1.5 1.5 0 0 0 1.5-1.5V8.207l.646.647a.5.5 0 0 0 .708-.708L13 5.793V2.5a.5.5 0 0 0-.5-.5h-1a.5.5 0 0 0-.5.5v1.293zM13 7.207V13.5a.5.5 0 0 1-.5.5h-9a.5.5 0 0 1-.5-.5V7.207l5-5z" />
        </svg>
        <Title>홈</Title>
      </Button>
      <Button onClick={() => setActiveNav(1)}>
        <svg
          xmlns="http://www.w3.org/2000/svg"
          width="48"
          height="48"
          fill="currentColor"
          viewBox="0 0 16 16"
        >
          <path d="M8.707 1.5a1 1 0 0 0-1.414 0L.646 8.146a.5.5 0 0 0 .708.708L2 8.207V13.5A1.5 1.5 0 0 0 3.5 15h9a1.5 1.5 0 0 0 1.5-1.5V8.207l.646.647a.5.5 0 0 0 .708-.708L13 5.793V2.5a.5.5 0 0 0-.5-.5h-1a.5.5 0 0 0-.5.5v1.293zM13 7.207V13.5a.5.5 0 0 1-.5.5h-9a.5.5 0 0 1-.5-.5V7.207l5-5z" />
        </svg>
        <Title>홈</Title>
      </Button>
      <Button onClick={() => setActiveNav(1)}>
        <svg
          xmlns="http://www.w3.org/2000/svg"
          width="48"
          height="48"
          fill="currentColor"
          viewBox="0 0 16 16"
        >
          <path d="M8.707 1.5a1 1 0 0 0-1.414 0L.646 8.146a.5.5 0 0 0 .708.708L2 8.207V13.5A1.5 1.5 0 0 0 3.5 15h9a1.5 1.5 0 0 0 1.5-1.5V8.207l.646.647a.5.5 0 0 0 .708-.708L13 5.793V2.5a.5.5 0 0 0-.5-.5h-1a.5.5 0 0 0-.5.5v1.293zM13 7.207V13.5a.5.5 0 0 1-.5.5h-9a.5.5 0 0 1-.5-.5V7.207l5-5z" />
        </svg>
        <Title>홈</Title>
      </Button>
    </Container>
  );
};

export default BottomNav;

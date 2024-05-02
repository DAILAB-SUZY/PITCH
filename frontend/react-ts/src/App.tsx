import styled from "styled-components";
import { Button } from "./stories/Button";
<<<<<<< HEAD
import Btn from "./stories/btn";
import { Routes, Route, BrowserRouter } from "react-router-dom";
import LoginPage from "./stories/LoginPage";
=======
import InputBox from "./stories/InputBox";
>>>>>>> origin/feature/frontend/init

type titleType = {
  fontSize: string;
  margin: string;
};

const Title = styled.div<{ fontSize: string; margin: string }>`
  font-size: ${(props) => props.fontSize};
  margin: ${(props) => props.margin};
`;

const Container = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
`;

function App() {
  return (
<<<<<<< HEAD
    <BrowserRouter>
      <Routes>
        <Route path="" element={<LoginPage />} />
      </Routes>
    </BrowserRouter>
=======
    <div>
      <Container>
        <div>hi</div>
        <Title fontSize="45px" margin="0px">
          {" "}
          test123{" "}
        </Title>
        <Button primary size="small" label="Log out" />
      </Container>
      <InputBox text="E-mail"></InputBox>
    </div>
>>>>>>> origin/feature/frontend/init
  );
}

export default App;

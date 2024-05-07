import styled from "styled-components";
import { Button } from "./stories/Button";
import Btn from "./stories/btn";
import { Routes, Route, BrowserRouter } from "react-router-dom";
import LoginPage from "./stories/LoginPage";

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
    <BrowserRouter>
      <Routes>
        <Route path="" element={<LoginPage />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;

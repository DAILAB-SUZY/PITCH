import styled from "styled-components";
import { Button } from "./stories/Button";

const Title = styled.div`
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
  console.log(1);

  return (
    <div>
      <Container>
        hello world
        <div>hi</div>
        <Title fontSize="45px" margin="0px">
          {" "}
          test{" "}
        </Title>
        <Button size="small" label="Log out" />
      </Container>
    </div>
  );
}

export default App;

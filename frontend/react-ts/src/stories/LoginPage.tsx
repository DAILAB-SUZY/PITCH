import styled from "styled-components";
import { Button } from "./Button";
import Btn from "./btn";

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
  background-color: #ff668d;
`;

function LoginPage() {
  return (
    <div>
      <Container>
        <div>hi</div>
        <Title fontSize="45px" margin="0px">
          {" "}
          test123{" "}
        </Title>
        <Button primary size="small" label="Log out" />
        <Btn></Btn>
      </Container>
      <Btn></Btn>
    </div>
  );
}

export default LoginPage;

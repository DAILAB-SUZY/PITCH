import styled from "styled-components";

const Title = styled.div`
  font-size: ${(props) => props.fontSize};
  margin: ${(props) => props.margin};
`;

function App() {
  console.log(1);

  return (
    <div>
      hello world
      <div>hi</div>
      <Title fontSize="45px" margin="0px">
        {" "}
        test{" "}
      </Title>
    </div>
  );
}

export default App;

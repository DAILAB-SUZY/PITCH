import styled from "styled-components";

const Container = styled.input`
  width: 80%;
  height: 10px;
  background-color: white;
  color: #c1c1c1;
  border-radius: 10px;
  padding: 10px;
  display: flex;
  align-items: center;
`;

const InputBox = (type: { text: string }) => {
  return <Container placeholder={type.text}></Container>;
};

export default InputBox;

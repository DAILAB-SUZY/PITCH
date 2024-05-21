import styled from "styled-components";
import { colors } from "../../styles/color";

const Container = styled.div`
  margin: 10px;
  padding: 0;
  /* box-sizing: border-box; */
  position: relative;
  width: 270px;
`;

interface InputProps {
  name?: string;
  placeholder: string;
  onChange?: (e: any) => any;
  type?: string;
}

const Input = styled.input`
  font-size: 18px;
  color: #222222;
  font-family: "Rg";
  width: 250px;
  border: none;
  border-bottom: solid #e1e1e1 1px;
  border-radius: 5px 5px 0px 0px;
  padding: 10px;
  position: relative;
  background-color: rgba(255, 255, 255, 0.7);
  &:focus {
    outline: none;
    background-color: rgba(255, 255, 255, 1);
  }
  &:focus ~ span {
    width: 100%;
  }
`;

const Line = styled.span`
  display: block;
  position: absolute;
  bottom: 0;
  left: 0%;
  background-color: ${colors.Main_Pink};
  width: 0;
  height: 2px;
  border-radius: 2px;
  transition: 0.5s;
`;

const InputBox = ({ name, placeholder, onChange, type }: InputProps) => {
  return (
    <Container>
      <Input
        name={name}
        placeholder={placeholder}
        onChange={onChange}
        type={type}
      ></Input>
      <Line></Line>
    </Container>
  );
};

export default InputBox;

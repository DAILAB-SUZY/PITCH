import styled from "styled-components";
import { colors } from "../../styles/color";

interface BtnProps {
  text: string;
  onClick?: () => void;
}

const Btnstyle = styled.div`
  width: 100px;
  height: 40px;
  font-family: "Bd";
  background-color: ${colors.Main_Pink};
  color: ${colors.BG_white};
  border-radius: 10px;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 20px;
  transition-duration: 0.3s;
  cursor: pointer;

  &:hover {
    transition-duration: 0.1s;
    background: purple;
    color: white;
  }
`;

const Btn = ({ text, onClick }: BtnProps) => {
  return <Btnstyle onClick={onClick}> {text} </Btnstyle>;
};

export default Btn;

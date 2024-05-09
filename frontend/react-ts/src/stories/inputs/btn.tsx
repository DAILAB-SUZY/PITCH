import styled from "styled-components";
import { colors } from "../../styles/color";

const Btnstyle = styled.div<{
  fontSize: string;
  width: string;
  height: string;
}>`
  width: ${(props) => props.width};
  height: ${(props) => props.height};
  font-family: "Bd";
  background-color: ${colors.Main_Pink};
  color: ${colors.BG_white};
  border-radius: 10px;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: ${(props) => props.fontSize};
  transition-duration: 0.3s;
  cursor: pointer;
  position: absolute;
  right: 15px;
  top: 14px;

  &:hover {
    transition-duration: 0.1s;
    background: purple;
    color: white;
  }
`;

const Btn = (type: {
  text: string;
  fontsize: string;
  width: string;
  height: string;
}) => {
  return (
    <Btnstyle fontsize={type.fontsize} width={type.width} height={type.height}>
      {" "}
      {type.text}{" "}
    </Btnstyle>
  );
};

export default Btn;

import styled from 'styled-components';
import { colors } from '../../styles/color';
interface BtnProps {
  text: string;
  fontSize: string;
  width: string;
  height: string;
  onClick?: () => void;
}
const Btnstyle = styled.div<{
  fontSize: string;
  width: string;
  height: string;
}>`
  width: ${props => props.width};
  height: ${props => props.height};
  font-family: 'EB';
  background-color: ${colors.Main_Pink};
  color: ${colors.BG_white};
  border-radius: 10px;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: ${props => props.fontSize};
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
const Btn = ({ text, fontSize, width, height, onClick }: BtnProps) => {
  return (
    <Btnstyle onClick={onClick} fontSize={fontSize} width={width} height={height}>
      {' '}
      {text}{' '}
    </Btnstyle>
  );
};
export default Btn;

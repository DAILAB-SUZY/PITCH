import styled from "styled-components";
import { colors } from "../../styles/color";
import { useNavigate } from "react-router-dom";
import img from "../../img/cat.webp";
interface ButtonProps {
  onClick?: () => void;
}

const PlaylistContainer = styled.div`
  width: 85vw;
  height: 10vh;
  display: flex;
  align-items: center;
  justify-content: flex-start;
  flex-direction: row;
  overflow-x: auto;
`;

const Item = styled.div`
  width: 58px;
  height: 58px;
  display: inline-block;
  /* padding: 12px; */
  margin-right: 10px;
  background-color: #fff;
  border-radius: 100px;
  border-image-slice: 1;
  background-color: white;
  overflow: hidden;
`;

const RainbowBorder = styled.div`
  width: 58px;
  height: 58px;
  margin-right: 10px;
  border: 5px solid transparent;
  border-radius: 100px;
  background-image: linear-gradient(#fff, #fff),
    linear-gradient(to right, #ffdae0 50%, #fb96a5 100%);
  background-origin: border-box;
  background-clip: content-box, border-box;
`;

const PlaylistCircle = ({ onClick }: ButtonProps) => {
  const navigate = useNavigate();
  const GoToPlayListPage = () => {
    navigate("/PlayListPage");
  };

  const items = Array.from({ length: 20 }, (_, index) => `Item ${index + 1}`);

  return (
    <PlaylistContainer>
      {items.map((item, index) => (
        <RainbowBorder show="hidden">
          <Item key={index} onClick={() => GoToPlayListPage()}>
            <img src={img} width="58" height="58"></img>
          </Item>
        </RainbowBorder>
      ))}
    </PlaylistContainer>
  );
};

export default PlaylistCircle;

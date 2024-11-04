import styled from 'styled-components';
import { colors } from '../../styles/color';

const FavoriteArtistArea = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 20px;
  margin-bottom: 50px;
`;

const BG = styled.div`
  position: relative;
  display: flex;
  width: 360px;
  height: 360px;
  border-radius: 12px;
  background: linear-gradient(90deg, #6a85b6, #bac8e0);
  box-shadow: rgba(0, 0, 0, 0.2) 0px 4px 12px;
`;

const ImageArea = styled.div`
  position: absolute;
  display: flex;
  align-items: flex-start;
  justify-content: center;
  overflow: hidden;
  top: 0px;
  left: 0px;

  width: 360px;
  height: 360px;
  /* object-fit: cover; */
  z-index: 1;
  border-radius: 12px;
`;

const GradientBG = styled.div`
  position: absolute;
  top: 0px;
  left: 0px;
  z-index: 2;
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 12px;
  background: linear-gradient(0deg, rgba(0, 0, 0, 1) 0%, rgba(0, 0, 0, 0.3) 100%);
  backdrop-filter: blur(0px);
`;

const ContentArea = styled.div`
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
  padding: 20px;
`;

const ArtistTitleTextArea = styled.div`
  /* position: absolute;
  bottom: 10px;
  left: 10px; */
  width: 320px;
  height: 60px;
  display: flex;
  justify-content: flex-start;
  align-items: center;
  box-sizing: border-box;
  z-index: 3;
  margin-bottom: 80px;

  background-color: rgba(255, 102, 141, 0.2);
  border: solid 2px ${colors.Main_Pink};
  border-radius: 10px;
  padding: 10px;
`;

const FavoriteAlbumArea = styled.div`
  /* position: absolute;
  bottom: 10px;
  left: 10px; */
  width: 320px;
  height: 60px;
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  align-items: center;
  box-sizing: border-box;
  z-index: 3;
  margin-bottom: 10px;

  background-color: rgba(255, 102, 141, 0.2);
  border: solid 2px ${colors.Main_Pink};
  border-radius: 10px;
  padding: 40px 10px;
`;

const FavoriteAlbumCover = styled.div`
  width: 60px;
  height: 60px;
  object-fit: cover;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 10%;
  img {
    width: 100%;
    height: 100%;
    object-fit: cover; /* 여기서 스타일 적용 */
  }
`;

const FavoriteAlbumTextArea = styled.div`
  width: 220px;
  height: 60px;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  justify-content: space-around;
  margin-left: 10px;
`;

const Text = styled.div<{
  fontFamily: string;
  fontSize: string;
  color: string;
  opacity?: string;
}>`
  font-size: ${props => props.fontSize};
  font-family: ${props => props.fontFamily};
  color: ${props => props.color};
  margin-right: 15px;
  opacity: ${props => props.opacity};
`;

interface FavoriteArtistProps {
  FavoriteArtistData: {
    artistCover: string;
    artistName: string;
    albumCover: string;
    albumName: string;
    trackCover: string;
    trackName: string;
  };
  openSearch: (topic: string) => void;
}

function FavoriteArtistEditCard({ FavoriteArtistData, openSearch }: FavoriteArtistProps) {
  const data = FavoriteArtistData;
  console.log(data);
  return (
    <FavoriteArtistArea>
      <BG>
        <ImageArea>
          <img src={data.artistCover} width="auto" height="100%" object-fit="cover"></img>
        </ImageArea>
        <GradientBG> </GradientBG>
        <ContentArea>
          <ArtistTitleTextArea
            onClick={() => {
              openSearch('Artist');
            }}
          >
            <Text fontFamily="Bd" fontSize={data.artistName.length > 10 ? '40px' : '50px'} color="white">
              {data.artistName}
            </Text>
          </ArtistTitleTextArea>
          <FavoriteAlbumArea
            onClick={() => {
              openSearch('Artist-album');
            }}
          >
            <FavoriteAlbumCover>{data.albumCover !== '' ? <img src={data.albumCover} width="100%" height="100%" object-fit="cover"></img> : null}</FavoriteAlbumCover>
            <FavoriteAlbumTextArea>
              <Text fontFamily="Rg" fontSize="15px" color="white" opacity="0.7">
                Favorite Album
              </Text>
              <Text fontFamily="Bd" fontSize="22px" color="white">
                {data.albumName}
              </Text>
            </FavoriteAlbumTextArea>
          </FavoriteAlbumArea>
          <FavoriteAlbumArea
            onClick={() => {
              openSearch('Artist-track');
            }}
          >
            <FavoriteAlbumCover>{data.trackCover !== '' ? <img src={data.trackCover} width="100%" height="100%" object-fit="cover"></img> : null}</FavoriteAlbumCover>
            <FavoriteAlbumTextArea>
              <Text fontFamily="Rg" fontSize="15px" color="white" opacity="0.7">
                Favorite Song
              </Text>
              <Text fontFamily="Bd" fontSize="22px" color="white">
                {data.trackName}
              </Text>
            </FavoriteAlbumTextArea>
          </FavoriteAlbumArea>
        </ContentArea>
      </BG>
    </FavoriteArtistArea>
  );
}

export default FavoriteArtistEditCard;

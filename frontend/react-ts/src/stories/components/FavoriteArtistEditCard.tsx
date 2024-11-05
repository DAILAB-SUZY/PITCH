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

const NoImageArea = styled.div`
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

  background-color: linear-gradient(to top right, #989898, #f3f3f3);
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
  width: 320px;
  height: 320px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-between;
  padding: 20px;
`;

const ArtistTitleTextArea = styled.div`
  /* position: absolute;
  bottom: 10px;
  left: 10px; */
  width: 320px;
  height: auto;
  display: flex;
  justify-content: flex-start;
  align-items: center;
  box-sizing: border-box;
  z-index: 3;
  /* margin-bottom: 80px; */

  background-color: rgba(255, 102, 141, 0.2);
  border: solid 2px ${colors.Main_Pink};
  border-radius: 10px;
  padding: 10px;
`;

const ArtistAlbumTrackArea = styled.div`
  display: flex;
  width: 100%;
  height: auto;
  flex-direction: column;
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

const NoFavoriteAlbumCover = styled.div`
  width: 60px;
  height: 60px;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 10%;
  /* border: solid 2px ${colors.Main_Pink}; */
  background: linear-gradient(180deg, rgba(0, 0, 0, 0) 0%, ${colors.Main_Pink} 100%);
  opacity: 0.5;
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
  fontFamily?: string;
  fontSize?: string;
  color?: string;
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
  //const data = FavoriteArtistData;
  //console.log(data);
  return (
    <FavoriteArtistArea>
      <BG>
        {FavoriteArtistData.artistCover === null ? (
          <NoImageArea></NoImageArea>
        ) : (
          <ImageArea>
            <img src={FavoriteArtistData.artistCover} width="auto" height="100%" object-fit="cover"></img>
          </ImageArea>
        )}

        <GradientBG> </GradientBG>
        <ContentArea>
          <ArtistTitleTextArea
            onClick={() => {
              openSearch('Artist');
            }}
          >
            {FavoriteArtistData.artistName === (null || '') ? (
              <Text fontFamily="Rg" fontSize="15px" color="white">
                여기를 눌러 아티스트를 설정해주세요.
              </Text>
            ) : (
              <Text fontFamily="Bd" fontSize={FavoriteArtistData.artistName.length > 10 ? '35px' : '40px'} color="white">
                {FavoriteArtistData.artistName}
              </Text>
            )}
          </ArtistTitleTextArea>
          <ArtistAlbumTrackArea>
            <FavoriteAlbumArea
              onClick={() => {
                openSearch('Artist-album');
              }}
            >
              <FavoriteAlbumCover>
                {FavoriteArtistData.albumCover !== (null || '') ? (
                  <img src={FavoriteArtistData.albumCover} width="100%" height="100%" object-fit="cover"></img>
                ) : (
                  <NoFavoriteAlbumCover></NoFavoriteAlbumCover>
                )}
              </FavoriteAlbumCover>
              <FavoriteAlbumTextArea>
                <Text fontFamily="Rg" fontSize="15px" color="white" opacity="0.7">
                  Favorite Album
                </Text>
                {FavoriteArtistData.albumName !== (null || '') ? (
                  <Text fontFamily="Bd" fontSize="18px" color="white">
                    {FavoriteArtistData.albumName}
                  </Text>
                ) : (
                  <Text fontFamily="Rg" fontSize="18px" color="white">
                    설정하지 않았습니다.
                  </Text>
                )}
              </FavoriteAlbumTextArea>
            </FavoriteAlbumArea>
            <FavoriteAlbumArea
              onClick={() => {
                openSearch('Artist-track');
              }}
            >
              <FavoriteAlbumCover>
                {FavoriteArtistData.trackCover !== (null || '') ? (
                  <img src={FavoriteArtistData.trackCover} width="100%" height="100%" object-fit="cover"></img>
                ) : (
                  <NoFavoriteAlbumCover></NoFavoriteAlbumCover>
                )}
              </FavoriteAlbumCover>
              <FavoriteAlbumTextArea>
                <Text fontFamily="Rg" fontSize="15px" color="white" opacity="0.7">
                  Favorite Song
                </Text>
                {FavoriteArtistData.trackName !== (null || '') ? (
                  <Text fontFamily="Bd" fontSize="18px" color="white">
                    {FavoriteArtistData.trackName}
                  </Text>
                ) : (
                  <Text fontFamily="Rg" fontSize="18px" color="white">
                    설정하지 않았습니다.
                  </Text>
                )}
              </FavoriteAlbumTextArea>
            </FavoriteAlbumArea>
          </ArtistAlbumTrackArea>
        </ContentArea>
      </BG>
    </FavoriteArtistArea>
  );
}

export default FavoriteArtistEditCard;

import styled from 'styled-components';
import { colors } from '../../styles/color';
import { useNavigate } from 'react-router-dom';

const AlbumCard = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: flex-start;
  width: 160px;
  height: 200px;
  margin: 10px;
`;

const AlbumImageCard = styled.div`
  position: relative;
  display: flex;
  width: 160px;
  height: 160px;
  border-radius: 8px;
  background: linear-gradient(90deg, #6a85b6, #bac8e0);
  box-shadow: rgba(0, 0, 0, 0.2) 0px 4px 12px;
  overflow: hidden;
`;

const AlbumInfo = styled.div`
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  justify-content: flex-start;
  width: 100%;

  white-space: nowrap;
  /* overflow: hidden; // 너비를 넘어가면 안보이게 */
  text-overflow: ellipsis; // 글자가 넘어가면 말줄임(...) 표시
`;

const AlbumImageArea = styled.div`
  position: absolute;
  top: 0px;
  left: 0px;
  overflow: hidden;
  width: 160px;
  height: 160px;
  object-fit: cover;
  z-index: 1;
`;

const AlbumGradientBG = styled.div`
  position: absolute;
  top: 0px;
  left: 0px;
  z-index: 2;
  width: 160px;
  height: 160px;
  object-fit: cover;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(0deg, rgba(0, 0, 0, 1) 0%, rgba(0, 0, 0, 0) 70%);
  backdrop-filter: blur(0px);
`;

const AlbumContentArea = styled.div`
  width: 160px;
  height: 160px;
  display: flex;
  flex-direction: row;
  align-items: flex-end;
  justify-content: flex-end;
  padding: 10px;
  box-sizing: border-box;
  z-index: 3;
`;

const Text = styled.div<{ fontSize?: string; margin?: string; fontFamily?: string; opacity?: number; color?: string }>`
  font-size: ${props => props.fontSize};
  margin: ${props => props.margin};
  font-family: ${props => props.fontFamily};
  opacity: ${props => props.opacity};
  color: ${props => props.color};
  width: 100%;

  white-space: nowrap;
  overflow: hidden; // 너비를 넘어가면 안보이게
  text-overflow: ellipsis; // 글자가 넘어가면 말줄임(...) 표시
`;
interface DNA {
  dnaKey: number;
  dnaName: string;
}

interface User {
  id: number;
  username: string;
  profilePicture: string;
  dnas: DNA[];
}

interface AlbumLike extends User {} // User와 동일한 구조 확장

interface AlbumDetail {
  albumId: number;
  title: string;
  albumCover: string;
  artistName: string;
  genre: string | null;
  spotifyId: string;
  likes: AlbumLike[];
}

interface CommentAuthor extends User {} // User 구조 확장

interface AlbumChatComment {
  albumChatCommentId: number;
  content: string;
  createAt: string; // ISO 날짜 형식
  updateAt: string; // ISO 날짜 형식
  likes: User[];
  comments: AlbumChatComment[]; // 재귀적 구조
  author: CommentAuthor;
}

interface AlbumData {
  albumDetail: AlbumDetail;
  comments: AlbumChatComment[];
}

interface AlbumProps {
  album: AlbumData;
}

interface AlbumSearchResult {
  album: {
    albumArtist: {
      artistId: string;
      imageUrl: string;
      name: string;
    };
    albumId: string;
    imageUrl: string;
    name: string;
    total_tracks: number;
    release_date: string;
  };
}

type UnionType = AlbumProps | AlbumSearchResult;

function MostLikedCard({ album }: UnionType) {
  const navigate = useNavigate();
  const GoToAlbumPage = (spotifyAlbumId: string) => {
    navigate('/AlbumPage', { state: spotifyAlbumId });
  };
  return (
    <>
      {'albumDetail' in album ? (
        <AlbumCard onClick={() => GoToAlbumPage(album.albumDetail.spotifyId)}>
          <AlbumImageCard>
            <AlbumImageArea>
              <img src={album.albumDetail.albumCover} width="100%" height="100%" object-fit="cover"></img>
            </AlbumImageArea>
            <AlbumGradientBG> </AlbumGradientBG>
            <AlbumContentArea>
              <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill={colors.Button_active} className="bi bi-heart-fill" viewBox="0 0 16 16">
                <path fillRule="evenodd" d="M8 1.314C12.438-3.248 23.534 4.735 8 15-7.534 4.736 3.562-3.248 8 1.314" />
              </svg>
              <Text fontSize="18px" margin="10px 0px 0px 5px" fontFamily="EB" opacity={0.8} color={colors.BG_white}>
                {album.albumDetail.likes.length}
              </Text>
            </AlbumContentArea>
          </AlbumImageCard>
          <AlbumInfo>
            <Text fontSize="18px" margin="7px 0px 0px 5px" fontFamily="EB" opacity={1} color={colors.Font_black}>
              {album.albumDetail.title}
            </Text>
            <Text fontSize="15px" margin="0px 0px 0px 5px" fontFamily="EB" opacity={0.8} color={colors.Font_grey}>
              {album.albumDetail.artistName}
            </Text>
          </AlbumInfo>
        </AlbumCard>
      ) : (
        <AlbumCard onClick={() => GoToAlbumPage(album.albumId)}>
          <AlbumImageCard>
            <AlbumImageArea>
              <img src={album.imageUrl} width="100%" height="100%" object-fit="cover"></img>
            </AlbumImageArea>
            {/* <AlbumGradientBG> </AlbumGradientBG>
          <AlbumContentArea>
            <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill={colors.Button_active} className="bi bi-heart-fill" viewBox="0 0 16 16">
              <path fillRule="evenodd" d="M8 1.314C12.438-3.248 23.534 4.735 8 15-7.534 4.736 3.562-3.248 8 1.314" />
            </svg>
            <Text fontSize="18px" margin="10px 0px 0px 5px" fontFamily="EB" opacity={0.8} color={colors.BG_white}>
              {album.albumDetail.likes.length}
            </Text>
          </AlbumContentArea> */}
          </AlbumImageCard>
          <AlbumInfo>
            <Text fontSize="18px" margin="7px 0px 0px 5px" fontFamily="EB" opacity={1} color={colors.Font_black}>
              {album.name}
            </Text>
            <Text fontSize="15px" margin="0px 0px 0px 5px" fontFamily="EB" opacity={0.8} color={colors.Font_grey}>
              {album.albumArtist.name}
            </Text>
          </AlbumInfo>
        </AlbumCard>
      )}
    </>
  );
}

export default MostLikedCard;

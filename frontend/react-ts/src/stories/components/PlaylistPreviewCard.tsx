import styled from 'styled-components';
import { useNavigate } from 'react-router-dom';
import { useEffect, useRef, useState } from 'react';
import ColorThief from 'colorthief';

interface FriendsPlayList {
  playlistId: number;
  albumCover: string[];
  author: {
    id: number;
    username: string;
    profilePicture: string;
  };
}

interface PlaylistProps {
  playlists: FriendsPlayList[];
}

// 전체 컨테이너 (가로 스크롤 가능)
const PlaylistContainer = styled.div`
  width: 100vw;
  display: flex;
  overflow-x: scroll;
  padding: 20px;
`;

// 각 플레이리스트 박스
const PlaylistBox = styled.div<{ gradient: string }>`
  background: ${({ gradient }) => gradient || '#ddd'};
  border-radius: 12px;
  width: 170px;
  height: 220px;
  margin-right: 16px;
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
  box-shadow: rgba(0, 0, 0, 0.1) 0px 4px 12px;
`;

// 앨범 커버 이미지 묶음
const AlbumCoverStack = styled.div`
  display: flex;
  position: relative;
  width: 90px;
  height: 90px;
  margin-top: 30px;
  box-sizing: border-box;
`;

// 앨범 커버 이미지 스타일
const AlbumCover = styled.img`
  position: absolute;
  width: 100%;
  height: 100%;
  border-radius: 8px;
  object-fit: cover;
  right: -10px;
  top: -10px;
  &:nth-child(2) {
    right: 0px;
    top: 0px;
    z-index: 1;
  }
  &:nth-child(3) {
    right: 10px;
    top: 10px;
    z-index: 2;
  }
`;

const ProfileArea = styled.div`
  width: 150px;
  height: 70px;
  padding: 15px 10px 15px 10px;
  border-radius: 0px 0px 12px 12px;
  background: linear-gradient(0deg, rgba(0, 0, 0, 0.6) 0%, rgba(0, 0, 0, 0) 100%);
  display: flex;
  flex-direction: row;
  align-items: flex-end;
  justify-content: flex-start;
`;

// 프로필 이미지 스타일
// const ProfileImage = styled.img`
//   width: 35px;
//   height: 35px;
//   border-radius: 50%;
//   margin-right: 10px;
// `;

const ProfileImage = styled.div`
  display: flex;
  overflow: hidden;
  width: 35px;
  height: 35px;
  border-radius: 50%;
  margin-right: 5px;
`;
const ProfileImageCircle = styled.img`
  width: 100%;
  height: 100%;
  object-fit: cover; /* 비율 유지하며 꽉 채움 */
  object-position: center; /* 이미지 가운데 정렬 */
`;

const UserNameArea = styled.div`
  width: 110px;
  height: 35px;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  justify-content: space-evenly;
`;

// 사용자 이름 스타일
const UserName = styled.div<{ fontFamily: string }>`
  font-size: 13px;
  font-family: ${props => props.fontFamily};
  color: white;
`;

const PlaylistPreviewCard = ({ playlists }: PlaylistProps) => {
  const navigate = useNavigate();
  const GoToPlayListPage = (author: {}) => {
    navigate('/PlayListPage', { state: author });
  };

  const [playlistGradients, setPlaylistGradients] = useState<string[]>([]);
  const albumCoverRefs = useRef<(HTMLImageElement | null)[]>([]);
  const [imagesLoaded, setImagesLoaded] = useState<boolean[]>(new Array(playlists.length).fill(false));

  // 이미지 로드 상태 추적
  useEffect(() => {
    if (imagesLoaded.every(Boolean)) {
      extractColors();
    }
  }, [imagesLoaded]);

  // ColorThief로 앨범 커버에서 색상 추출
  const extractColors = () => {
    const colorThief = new ColorThief();
    const gradients: string[] = albumCoverRefs.current.map(img => {
      if (img && img.complete) {
        const colors = colorThief.getPalette(img, 2); // 가장 대비되는 두 가지 색상 추출
        const primaryColor = `rgb(${colors[0].join(',')})`;
        const secondaryColor = `rgb(${colors[1].join(',')})`;
        return `linear-gradient(135deg, ${primaryColor}, ${secondaryColor})`;
      }
      return '#000000'; // 이미지가 없거나 완전히 로드되지 않은 경우
    });
    setPlaylistGradients(gradients);
  };

  const handleImageLoad = (index: number) => {
    setImagesLoaded(prev => {
      const newLoaded = [...prev];
      newLoaded[index] = true;
      return newLoaded;
    });
  };
  const GoToMusicProfilePage = (userId: number) => {
    navigate('/MusicProfilePage', { state: userId });
  };
  return (
    <PlaylistContainer>
      {playlists.map(
        (playlist, index) =>
          playlist.albumCover.length !== 0 && (
            <PlaylistBox key={playlist.playlistId} gradient={playlistGradients[index] || `linear-gradient(135deg, #c5c5c5, #3f2932)`}>
              <AlbumCoverStack onClick={() => GoToPlayListPage({ ...playlist.author, page: 1 })}>
                {playlist.albumCover
                  .slice(0, 3)
                  .reverse()
                  .map((cover: any, coverIndex: any) => (
                    <AlbumCover
                      key={coverIndex}
                      src={cover}
                      alt={`Album Cover ${index + 1}`}
                      // ref={el => (albumCoverRefs.current[index] = el)}
                      ref={el => (coverIndex === playlist.albumCover.slice(0, 3).length - 1 ? (albumCoverRefs.current[index] = el) : null)}
                      crossOrigin="anonymous"
                      onLoad={() => handleImageLoad(index)} // 이미지 로드 상태 추적
                    />
                  ))}
              </AlbumCoverStack>
              <ProfileArea onClick={() => GoToMusicProfilePage(playlist.author.id)}>
                {/* <ProfileImage src={playlist.author.profilePicture} alt="Profile" /> */}
                <ProfileImage>
                  <ProfileImageCircle src={playlist.author.profilePicture} alt="Profile" />
                </ProfileImage>
                <UserNameArea>
                  <UserName fontFamily="Bd">{playlist.author.username}'s</UserName>
                  <UserName fontFamily="Rg">Playlist</UserName>
                </UserNameArea>
              </ProfileArea>
            </PlaylistBox>
          ),
      )}
    </PlaylistContainer>
  );
};

export default PlaylistPreviewCard;

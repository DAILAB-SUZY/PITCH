import styled from "styled-components";

const ChatBox = styled.div`
  width: 100%;
  height: 100%;
  margin-bottom: 20px;
  background-color: lightgray;
  border-radius: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
`;

const ChatBoxHeader = styled.div`
  margin-top: 10px;
  width: 90%;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
`;

const ChatBoxContent = styled.div`
  margin-top: 10px;
  width: 90%;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
`;

const ReactBox = styled.div`
  margin-top: 10px;
  width: 90%;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: end;
`;

const Reacts = styled.div`
  margin-top: 10px;
  width: 45%;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-between;
`;

const BoxHeader = styled.div`
  width: 40%;
  display: flex;
  flex-direction: row;
  align-items: end;
  justify-content: space-between;
`;

const Circle = styled.div`
  width: 30px;
  height: 30px;
  background-color: black;
  border-radius: 100px;
`;

const Text = styled.div<{ fontSize: string; margin: string }>`
  font-size: ${(props) => props.fontSize};
  margin: ${(props) => props.margin};
  font-family: "Rg";
`;

const AlbumChatBox = () => {
  return (
    <ChatBox>
      <ChatBoxHeader>
        <BoxHeader>
          <Circle></Circle>
          <Text fontSize="20px">준호</Text>
          <Text fontSize="15px">1시간 전</Text>
        </BoxHeader>
        <svg
          xmlns="http://www.w3.org/2000/svg"
          width="25"
          height="25"
          fill="currentColor"
          viewBox="0 0 16 16"
        >
          <path d="M3 9.5a1.5 1.5 0 1 1 0-3 1.5 1.5 0 0 1 0 3m5 0a1.5 1.5 0 1 1 0-3 1.5 1.5 0 0 1 0 3m5 0a1.5 1.5 0 1 1 0-3 1.5 1.5 0 0 1 0 3" />
        </svg>
      </ChatBoxHeader>
      <ChatBoxContent>
        <Text fontSize="20px">운동할 때 듣기 좋음</Text>
      </ChatBoxContent>
      <ReactBox>
        <Reacts>
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="16"
            height="16"
            fill="currentColor"
            viewBox="0 0 16 16"
          >
            <path
              fill-rule="evenodd"
              d="M8 1.314C12.438-3.248 23.534 4.735 8 15-7.534 4.736 3.562-3.248 8 1.314"
            />
          </svg>
          <Text fontSize="10px">좋아요 123개</Text>
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="14"
            height="14"
            fill="currentColor"
            viewBox="0 0 16 16"
          >
            <path d="M14 1a1 1 0 0 1 1 1v8a1 1 0 0 1-1 1H4.414A2 2 0 0 0 3 11.586l-2 2V2a1 1 0 0 1 1-1zM2 0a2 2 0 0 0-2 2v12.793a.5.5 0 0 0 .854.353l2.853-2.853A1 1 0 0 1 4.414 12H14a2 2 0 0 0 2-2V2a2 2 0 0 0-2-2z" />
            <path d="M3 3.5a.5.5 0 0 1 .5-.5h9a.5.5 0 0 1 0 1h-9a.5.5 0 0 1-.5-.5M3 6a.5.5 0 0 1 .5-.5h9a.5.5 0 0 1 0 1h-9A.5.5 0 0 1 3 6m0 2.5a.5.5 0 0 1 .5-.5h5a.5.5 0 0 1 0 1h-5a.5.5 0 0 1-.5-.5" />
          </svg>
          <Text fontSize="10px">답글 3개</Text>
        </Reacts>
      </ReactBox>
    </ChatBox>
  );
};

export default AlbumChatBox;

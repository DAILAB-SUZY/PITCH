////// Post 시간 계산 //////

export const updateTimeAgo = (CreateTime: string, UpdatedTime: string) => {
  const createTimeUnix = Date.parse(CreateTime) / 1000; // 초 단위 변환
  const updatedTimeUnix = UpdatedTime ? Date.parse(UpdatedTime) / 1000 : null;

  if (createTimeUnix) {
    if (updatedTimeUnix === null) {
      console.log('수정안됨');
      return formatTimeAgo(createTimeUnix);
    } else {
      console.log('수정됨');
      return formatTimeAgo(updatedTimeUnix);
    }
  }
  return '';
};

const formatTimeAgo = (unixTimestamp: number): string => {
  const currentTime = Math.floor(Date.now() / 1000); // 현재 시간 (초)
  const timeDifference = currentTime - Math.floor(unixTimestamp); // 경과 시간 (초)

  const minutesAgo = Math.floor(timeDifference / 60); // 경과 시간 (분)
  const hoursAgo = Math.floor(timeDifference / 3600); // 경과 시간 (시간)
  const daysAgo = Math.floor(timeDifference / 86400); // 경과 시간 (일)

  if (minutesAgo < 60) {
    return `${minutesAgo}분 전`;
  } else if (hoursAgo < 24) {
    return `${hoursAgo}시간 전`;
  } else {
    return `${daysAgo}일 전`;
  }
};

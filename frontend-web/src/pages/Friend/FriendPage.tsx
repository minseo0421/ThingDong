import * as S from '@/pages/Friend/FriendPage.style';
import Header from '@/components/molecules/Header/Header';
import { Image } from '@/components/atoms/Image/Image';
import { Text } from '@/components/atoms/Text/Text.styles';

import notification from '@/assets/images/friend/notification.png';
import search from '@/assets/images/friend/search.png';
import FriendList from '@/components/organisms/FriendList/FriendList';
import { useGetFriends } from '@/apis/Friend/Queries/useGetFriends';
import { useNavigate } from 'react-router-dom';
import { PATH } from '@/constants/path';
import { Background } from '@/components/atoms/Background/Background.style';
import Modal from '@/components/molecules/Modal/Modal';
import { useState } from 'react';

const FriendPage = () => {
  console.log(process.env.REACT_APP_SERVER_URL);
  const { thingguAlarmList, thingguList } = useGetFriends();
  const [modalOpen, setModalOpen] = useState(false);
  const navigate = useNavigate();
  console.log('thingguAlarmList', thingguAlarmList);
  console.log('thingguList', thingguList);
  const changeModalOpen = () => {
    setModalOpen(!modalOpen);
  };

  return (
    <Background>
      <Modal
        isOpen={modalOpen}
        onClose={changeModalOpen}
        height={25}
        $padding={0}
      >
        <Header text="띵구 요청" />
        <FriendList friends={thingguAlarmList} $paddidngBottom={0} />
      </Modal>
      <S.FriendContainer>
        <Header text="띵구">
          <S.FriendHeaderIcons>
            <Image
              src={search}
              width={100}
              height={100}
              $unit="%"
              onClick={() => navigate(PATH.FRIEND_SEARCH)}
            />
            <Image
              src={notification}
              width={100}
              height={100}
              $unit="%"
              onClick={changeModalOpen}
            />
          </S.FriendHeaderIcons>
        </Header>
        <S.NotificationNumberIcon>
          <Text size="small2" fontWeight="heavy" color="white">
            {thingguAlarmList.length}
          </Text>
        </S.NotificationNumberIcon>
        <S.Sun />
        {thingguList.length === 0 ? (
          <S.NoFriendTextContainer>
            <Text
              size="body2"
              fontWeight="bold"
              color="grey2"
              $margin="0 0 0.5rem"
            >
              띵구가 없어요...
            </Text>
            <Text size="body2" fontWeight="bold" color="grey2">
              검색창에 띵구를 검색해보세요!
            </Text>
          </S.NoFriendTextContainer>
        ) : (
          <FriendList friends={thingguList} />
        )}
      </S.FriendContainer>
    </Background>
  );
};

export default FriendPage;
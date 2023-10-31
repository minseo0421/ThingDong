import { Background } from '@/components/atoms/Background/Background.style';
import BottomNavBar from '@/components/molecules/BottomNavBar/BottomNavBar';
import { Outlet } from 'react-router-dom';

const MainPage = () => {
  return (
    <>
      <Background>
        <Outlet />
      </Background>
      <BottomNavBar />
    </>
  );
};

export default MainPage;
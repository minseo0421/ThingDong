import { Text } from '@/components/atoms/Text/Text.styles';
import { styled } from 'styled-components';

const FriendBlockContainer = styled.div`
  display: flex;
  background-color: ${({ theme }) => theme.color.white};
  align-items: center;
  justify-content: space-between;
  border-radius: 4rem;
  padding: 0.4rem;
  margin-bottom: 1rem;

  img:last-child {
    margin-right: 0.9rem;
  }
`;

const FriendBlockProfile = styled.div`
  display: flex;
  align-items: center;
`;

const FriendBlockText = styled(Text)`
  margin-left: 0.5rem;
`;

export { FriendBlockContainer, FriendBlockProfile, FriendBlockText };

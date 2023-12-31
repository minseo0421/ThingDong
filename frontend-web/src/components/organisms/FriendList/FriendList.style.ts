import { Text } from '@/components/atoms/Text/Text.styles';
import { styled } from 'styled-components';

const FriendListContainer = styled.div<{
  $paddidngBottom?: number;
  $height: number;
}>`
  height: ${({ $height }) => `${$height}%`};
  padding-bottom: ${({ $paddidngBottom }) => `${$paddidngBottom}em` ?? '5rem'};
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  position: relative;
`;

export { FriendListContainer };

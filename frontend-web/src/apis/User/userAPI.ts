import { instance } from '@/apis/instance';
import { UserInfo, UserLoginInfo } from '@/types/user';

const addUser = async (data: UserInfo) => {
  try {
    await instance.post('/users/signUp', data);
  } catch {
    new Error('user add error');
  }
};

const addLogin = async (data: UserLoginInfo) => {
  try {
    const response = await instance.post('/users/login', data);
    return response.data;
  } catch {
    new Error('login error');
  }
};

export { addUser, addLogin };
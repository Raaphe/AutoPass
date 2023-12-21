import axios from 'axios';
import UserCreationDTO from '../DTO/UserCreationDTO';

const BASE_URL: string = 'http://localhost:9090/';

class UserService {

    getUser = async (userId: number): Promise<any> => {
        try {
            const response = await axios.get(`${BASE_URL}user/${userId}`);
            return response.data;
        } catch (e) {
            throw new Error();
        }
    }

    createUser = async (userDTO: UserCreationDTO) => {
        try {
            const response = await axios.post(`${BASE_URL}user`, userDTO);
            return response.data;
        } catch (e) {
            throw new Error();
        }
    }

}

export default UserService;
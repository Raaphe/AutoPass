class UserCreationDTO {
    pwd: string;
    username: string;
    email: string;

    constructor(pwd: string, username: string, email: string) {
        this.pwd = pwd;
        this.username = username;
        this.email = email;
    }
}

export default UserCreationDTO;
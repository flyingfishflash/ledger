import { Role } from './role';

export class User {
    id: number;
    username: string;
    // password: string;
    // firstName: string;
    // lastName: string;
    // role: Role;
    roles: Role[];
    token?: string;
}

export interface UserSignUpRequest {
    email: string;
    firstName: string;
    lastName: string;
    password: string;
    roles: Role[];
    username: string;
}

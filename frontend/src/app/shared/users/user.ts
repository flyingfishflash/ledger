import { Role } from "./role";

export class User {
  id: number;
  username: string;
  roles: Role[];
  sessionId?: string;
}

export interface UserSignUpRequest {
  email: string;
  firstName: string;
  lastName: string;
  password: string;
  roles: Role[];
  username: string;
}

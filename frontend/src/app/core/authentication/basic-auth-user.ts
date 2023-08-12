import { HttpResponse } from '@angular/common/http';
import { BasicAuthUserRole } from './basic-auth-user-role';

export class BasicAuthUser {
  readonly id: number;
  readonly username: string;
  readonly roles: BasicAuthUserRole[];
  readonly sessionId: string = '';

  constructor(response: HttpResponse<any>) {
    this.id = response.body.content.id;
    this.username = response.body.content.username;
    this.roles = response.body.content.roles;
    this.sessionId = response.headers.get('x-auth-token') ?? '';
  }
}

export interface UserSignUpRequest {
  email: string;
  firstName: string;
  lastName: string;
  password: string;
  roles: BasicAuthUserRole[];
  username: string;
}

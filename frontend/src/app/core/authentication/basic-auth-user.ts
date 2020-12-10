import { BasicAuthUserRole } from "./basic-auth-user-role";
import { HttpResponse } from "@angular/common/http";

export class BasicAuthUser {
  readonly id: number;
  readonly username: string;
  readonly roles: BasicAuthUserRole[];
  readonly sessionId?: string;

  constructor(response: HttpResponse<any>) {
    this.id = response.body.response.body.id;
    this.username = response.body.response.body.username;
    this.roles = response.body.response.body.roles;
    this.sessionId = response.headers.get("x-auth-token");
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

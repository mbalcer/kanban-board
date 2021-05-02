export class JwtResponse {

  jwtToken: string;
  username: string;

  constructor(jwtToken: string, username: string) {
    this.jwtToken = jwtToken;
    this.username = username;
  }

}

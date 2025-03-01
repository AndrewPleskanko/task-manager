export class User {
  username?: string;
  password?: string;
  email?: string;
  role?: Role;
  avatarUrl?: string;
  joinedDate?: string;
  phone?: string;
  age?: number;
  status?: boolean;
}

export class Role {
  id?: number;
  name?: string;
}

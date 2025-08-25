// AppService
export interface Post{
    id: number;
    title: string;
    content: string;
    creatorId: number;
    creationTime: string;
}

// AppUser
export interface User{
  id: number;
  name: string;
  email: string;
  creationDate: string;
}

// Comment
export interface Comment{
  id: number;
  content: string;
  postId: number;
  creatorId: number;
  creationTime: string;
}
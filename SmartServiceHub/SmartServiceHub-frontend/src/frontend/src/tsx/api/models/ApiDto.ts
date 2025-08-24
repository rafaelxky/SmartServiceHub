export interface Post{
    id: number;
    title: string;
    content: string;
    creatorId: number;
    creationTime: string;
}

export interface User{
  id: number;
  name: string;
  email: string;
  creationDate: string;
}

export interface Comment{
  id: number;
  content: string;
  postId: number;
  creatorId: number;
  creationTime: string;
}
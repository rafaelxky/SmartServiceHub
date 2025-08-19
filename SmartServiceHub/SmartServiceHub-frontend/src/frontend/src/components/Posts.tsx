import { h } from 'preact';
import PostCard from './PostCard';
import type { Post } from '../tsx/types';
import 'bootstrap/dist/css/bootstrap.min.css';

interface Props {
  posts: Post[];
}

const Posts = ({ posts }: Props) => {
  return (
    <div>
      {posts.map((p) => (
        <PostCard key={p.id} post={p} />
      ))}
    </div>
  );
};

export default Posts;

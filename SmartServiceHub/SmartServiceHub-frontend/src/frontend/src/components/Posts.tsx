import PostCard from './PostCard';
import type { ServicePost } from '../tsx/api/models/ServicePost';
import 'bootstrap/dist/css/bootstrap.min.css';

interface Props {
  posts: ServicePost[];
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

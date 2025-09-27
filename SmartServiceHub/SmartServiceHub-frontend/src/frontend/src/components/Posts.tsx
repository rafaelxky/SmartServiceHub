import PostCard from './PostCard';
import type { ServicePost } from '../ts/api/models/ServicePost';
import 'bootstrap/dist/css/bootstrap.min.css';

interface Props {
  posts: ServicePost[];
}

const Posts = ({ posts }: Props) => {
  return (
    <div>
      <br />
      {posts.map((p) => (
        <PostCard key={p.id} post={p} />
      ))}
    </div>
  );
};

export default Posts;

import { Card } from 'react-bootstrap';
import type { ServicePost } from '../tsx/api/models/ServicePost';
import 'bootstrap/dist/css/bootstrap.min.css';

interface Props {
  post: ServicePost;
}

const PostCard = ({ post }: Props) => {
  return (
    <Card className="shadow-sm w-100">
      <Card.Body>
        <Card.Title>{post.title ?? "title"}</Card.Title>
        <Card.Text>{post.content ?? "content"}</Card.Text>
        <Card.Subtitle className="text-muted">
          By {post.creatorId ?? "author"} on {post.timestamp ?? "date"}
        </Card.Subtitle>
      </Card.Body>
    </Card>
  );
};

export default PostCard;

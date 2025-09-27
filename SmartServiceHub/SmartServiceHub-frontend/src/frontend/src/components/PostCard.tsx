import { Card } from 'react-bootstrap';
import type { ServicePost } from '../ts/api/models/ServicePost';
import 'bootstrap/dist/css/bootstrap.min.css';
import { userService } from '../pages/Context';
import { useState, useEffect } from 'preact/hooks';

interface Props {
  post: ServicePost;
}

const PostCard = ({ post }: Props) => {
  const [username, setUsername] = useState<string>('author'); // default

  useEffect(() => {
    userService.getUserById(post.creatorId)
      .then(user => setUsername(user.username))
      .catch(err => console.error(err));
  }, [post.creatorId]);

  return (
    <Card className="shadow-sm w-100">
      <Card.Body>
        <Card.Title>{post.title ?? "title"}</Card.Title>
        <Card.Text>{post.content ?? "content"}</Card.Text>
        <Card.Subtitle className="text-muted">
          By {username} on {new Date(post.timestamp).toLocaleDateString("de-DE") ?? "date"}
        </Card.Subtitle>
      </Card.Body>
    </Card>
  );
};

export default PostCard;

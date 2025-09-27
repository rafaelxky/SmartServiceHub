import { Card } from 'react-bootstrap';
import type { ServicePost } from '../ts/api/models/ServicePost';
import 'bootstrap/dist/css/bootstrap.min.css';
import { userService } from '../pages/Context';
import { useState, useEffect } from 'preact/hooks';
import { route } from 'preact-router';
import type { ServicePostPublicDto } from '../ts/api/models/dto/ServicePostPublicDto';

interface Props {
  post: ServicePostPublicDto;
}

const handleCommentClick = (postId: number) => {
  route(`/post/${postId}/comments`); 
};

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
          By {username} on {new Date(post.timestamp.valueOf()).toLocaleDateString("de-DE") ?? "date"}
        </Card.Subtitle>
      </Card.Body>
      <Card.Footer className="d-flex align-items-center">
        <button
          className="btn btn-link p-0 d-flex align-items-center"
          onClick={() => handleCommentClick(post.id)}
          style={{ textDecoration: 'none' }}
        >
          <i className="bi bi-chat me-2 fs-5"></i>
          {0} Comments
        </button>
      </Card.Footer> 
    </Card>
  );
};

export default PostCard;

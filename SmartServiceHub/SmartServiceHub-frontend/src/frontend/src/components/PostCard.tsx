import { h } from 'preact';
import { Card } from 'react-bootstrap';
import type { Post } from '../tsx/models';
import 'bootstrap/dist/css/bootstrap.min.css';

interface Props {
  post: Post;
}

const PostCard = ({ post }: Props) => {
  return (
    <Card className="shadow-sm w-100">
      <Card.Body>
        <Card.Title>{post.title}</Card.Title>
        <Card.Text>{post.content}</Card.Text>
        <Card.Subtitle className="text-muted">
          By {post.author} on {post.date}
        </Card.Subtitle>
      </Card.Body>
    </Card>
  );
};

export default PostCard;

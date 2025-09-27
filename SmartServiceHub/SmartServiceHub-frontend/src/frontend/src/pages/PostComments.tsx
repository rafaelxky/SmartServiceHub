import { useEffect, useState } from "preact/hooks";
import PostCard from "../components/PostCard";
import { commentService, servicePostService } from "./Context";
import type { CommentPublicDto } from "../ts/api/models/dto/CommentPublicDto";
import type { ServicePost } from "../ts/api/models/ServicePost";

interface Props {
  postId: string; // from route
}

const PostComments = ({ postId }: Props) => {
  const [comments, setComments] = useState<CommentPublicDto[]>([]);
  const [post, setPost] = useState<ServicePost | null>(null);

  // Fetch post info
  useEffect(() => {
    servicePostService.getServiceById(Number(postId))
      .then(data => setPost(data))
      .catch(err => console.error(err));
  }, [postId]);

  // Fetch comments
  useEffect(() => {
    commentService.getPostComments(Number(postId))
      .then(data => setComments(data))
      .catch(err => console.error(err));
  }, [postId]);

  if (!post) return <div>Loading post...</div>;

  return (
    <div>
      <PostCard key={postId} post={post}></PostCard>

      <h4 className="mt-4">Comments</h4>
      {comments.length === 0 ? (
        <p>No comments yet.</p>
      ) : (
        comments.map(c => (
          <div key={c.id} className="mb-2 p-2 border rounded">
            <strong>{c.userId ?? c.userId}</strong>: {c.content}
          </div>
        ))
      )}
    </div>
  );
};

export default PostComments;

import { h } from 'preact';
import { useState } from 'preact/hooks';
import { route } from 'preact-router';

const NewPost = () => {
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const handleSubmit = async (e: Event) => {
    e.preventDefault();

    const token = localStorage.getItem('token');
    if (!token) {
      setError('You must be logged in to create a post.');
      return;
    }

    try {
      const response = await fetch('http://127.0.0.1:8081/api/services', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`, // JWT auth
        },
        body: JSON.stringify({
          title,
          content,
        }),
      });

      if (!response.ok) {
        const data = await response.json();
        throw new Error(data.message || 'Failed to create post');
      }

      setSuccess('Post created successfully!');
      setError('');
      setTitle('');
      setContent('');

      // Optional: navigate to home or posts page
      console.log("Post created successfully!");
      route('/');
    } catch (err: any) {
      setError(err.message);
      setSuccess('');
    }
  };

  return (
    <div className="container mt-5 pt-5" style={{ maxWidth: '600px' }}>
      <h2 className="mb-4">Create a New Post</h2>
      {error && <div className="alert alert-danger">{error}</div>}
      {success && <div className="alert alert-success">{success}</div>}

      <form onSubmit={handleSubmit}>
        {/* Title */}
        <div className="mb-3">
          <label className="form-label">Title</label>
          <input
            type="text"
            className="form-control"
            value={title}
            onInput={(e: any) => setTitle(e.target.value)}
            required
          />
        </div>

        {/* Content */}
        <div className="mb-3">
          <label className="form-label">Content</label>
          <textarea
            className="form-control"
            rows={8}
            value={content}
            onInput={(e: any) => setContent(e.target.value)}
            required
          />
        </div>

        <button type="submit" className="btn btn-primary">
          Create Post
        </button>
      </form>
    </div>
  );
};

export default NewPost;

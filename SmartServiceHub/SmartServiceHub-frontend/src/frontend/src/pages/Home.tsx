import Posts from '../components/Posts';
import type { ServicePost } from '../ts/api/models/ServicePost';
import '../styles/debug.css'
import '../styles/styles.css'
import 'bootstrap/dist/css/bootstrap.min.css';
import {servicePostService} from './Context';
import NoPosts from '../components/NoPosts';
import type { ServicePostPublicDto } from '../ts/api/models/dto/ServicePostPublicDto';

const posts: ServicePostPublicDto[] = await servicePostService.getUniqueServicePost(5, 0);

const Home = () => (
    <div>
        <div className="container d-flex flex-column gap-3">
            {posts.length != 0 ? 
            <Posts posts={posts} />
            :
            <NoPosts/>
            }
        </div>
    </div>
);

export default Home;
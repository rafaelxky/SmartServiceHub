import 'bootstrap/dist/css/bootstrap.min.css';
import { Card } from 'react-bootstrap';

const NoPosts = () => {
    return (
        <div>
            <br />
            <Card className="shadow-sm w-100">
                <Card.Body>
                    <Card.Text>
                        Wow, this is really empty!
                    </Card.Text>
                </Card.Body>
            </Card>
        </div>
    )
}

export default NoPosts
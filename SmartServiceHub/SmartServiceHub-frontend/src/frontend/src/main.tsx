import { render } from 'preact'
import './index.css'
import { App } from './app.tsx'
import { Header } from './modules/header.tsx';

//render(<App />, document.getElementById('app')!)

class customHeaderElement extends HTMLElement {
    connectedCallback() {
    render(<Header />, this);
  }
}
customElements.define('custom-header', customHeaderElement);
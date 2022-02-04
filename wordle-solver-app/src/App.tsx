import React from 'react';
//import logo from './logo.svg';
import './App.css';

import {descend} from './util';

class App extends React.Component<{}, {guessList: Array<number>, wordList: Array<string>}> {
  constructor(props: {}) {
    super(props);

    descend("tree", [1, 1, 1, 1, 1]);
  }

  render() {
    return (
      <div className="App">
        <header className="App-header">
          <h1>Hello</h1>
        </header>
      </div>
    );
  }
}

export default App;

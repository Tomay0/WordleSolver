import React from 'react';
import './App.css';

import treeJson from "./balanced_tree.json"
import {descendTree, createTree, evaluateTree} from './tree';

class App extends React.Component<{}, {guessList: Array<number>, wordList: Array<string>}> {
  constructor(props: {}) {
    super(props);
    
    const tree = createTree(treeJson);

    console.log(tree);
    console.log(evaluateTree(tree));
    //console.log(descendTree(tree, "00000", [0, 0, 0, 0, 0]));
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

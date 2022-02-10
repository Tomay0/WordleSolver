import React from 'react';
import './App.css';
import TileRow from './TileRow'

import treeJson from "./balanced_tree.json"
import {descendTree, createTree, evaluateTree} from './tree';

export default class App extends React.Component<{}, {guessList: Array<number>, wordList: Array<string>}> {
  constructor(props: {}) {
    super(props);

    const tree = createTree(treeJson);

    console.log(tree);
    console.log(evaluateTree(tree));
    //console.log(descendTree(tree, "00000", [0, 0, 0, 0, 0]));
  }

  render() {
    return (
      <div className="app">
        <header className="app-header">
          <TileRow word="hello"></TileRow>
        </header>
      </div>
    );
  }
}

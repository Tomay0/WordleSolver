import React from 'react';
import './App.css';
import TileRow from './TileRow'

import treeJson from "./tree.json"
import {GuessTree, descendTree, createTree, evaluateTree} from './tree';


type GameState = {rows: Array<{word: string, wordState: Array<number>}>}

export default class App extends React.Component<{}, GameState> {
  tree: GuessTree;

  constructor(props: {}) {
    super(props);

    this.tree = createTree(treeJson);

    console.log(evaluateTree(this.tree));

    const topState = {word: this.tree["guess"].toUpperCase(), wordState: [0, 0, 0, 0, 0]};
    
    this.state = {rows: [topState]};
    this.next = this.next.bind(this);
    this.back = this.back.bind(this);
  }

  onUpdate(state: Array<number>) {
    this.state.rows[this.state.rows.length - 1].wordState = state;
    this.setState({rows: this.state.rows});
  }

  path(): string {
    return this.state.rows.map((row) => row.wordState.join("")).join("/")
  }

  validPath(): boolean {
    const descent = descendTree(this.tree, this.path());
    
    return !descent.isError && descent.value.toUpperCase() !== this.state.rows[this.state.rows.length - 1].word;
  }
  
  back() {
    if(this.state.rows.length < 2) return;

    this.state.rows.pop();
    this.setState({rows: this.state.rows});
  }

  next() {
    if (!this.validPath()) return;

    const descent = descendTree(this.tree, this.path());

    if (descent.isError) return;

    const row = {word: descent.value.toUpperCase(), wordState: [0, 0, 0, 0, 0]};

    this.state.rows.push(row);
    this.setState({rows: this.state.rows});
  }

  render() {
    const rows = this.state.rows.map((row, i) => (
        <TileRow word={row.word} key={i} onUpdate={state => this.onUpdate(state)} disabled={i !== this.state.rows.length - 1}/>
      )
    );

    return (
      <div className="app">
        <header className="app-header">
          <h1>Wordle Solver</h1>
          <p>This will always tell you the best word to guess next.</p>
          <ul>
            <li>Make your guess in Wordle with our best chosen word</li>
            <li>Enter the result from the game by clicking the letters</li>
            <li>Hit next to see the next word you should guess</li>
          </ul>
        </header>
        <div className="app-body">
          {rows}
          <div className="buttons">
            {<button className="btn" type="submit" disabled={!this.validPath()} onClick={this.next}>NEXT</button>}
            {<button className="btn" type="submit" disabled={this.state.rows.length === 1} onClick={this.back}>BACK</button>}
          </div>
        </div>
      </div>
    );
  }
}
